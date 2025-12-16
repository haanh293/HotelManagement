package com.hotel.application.service;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import org.springframework.stereotype.Service;
import com.hotel.infrastructure.adapter.out.external.EmailService; // Import Service gửi mail
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository; // Import Repo Guest
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService implements BookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final EmailService emailService;             
    private final SpringDataGuestRepository guestRepo;
    
 // Thông tin ngân hàng của bạn (Hard-code)
    private static final String BANK_ID = "vietinbank";
    private static final String ACCOUNT_NO = "108878399150";
    private static final String ACCOUNT_NAME = "NGUYEN HA ANH";
    private static final String TEMPLATE = "compact"; // Giao diện QR gọn gàng
    public BookingService(BookingRepositoryPort bookingRepositoryPort, EmailService emailService, SpringDataGuestRepository guestRepo) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.emailService = emailService;
        this.guestRepo = guestRepo;
    }
    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        // 1. Validation
        if (booking.getRoomType() == null || booking.getRoomType().isEmpty()) {
            throw new RuntimeException("Vui lòng chọn loại phòng (roomType)!");
        }

        // 2. Check phòng trống
        boolean isAvailable = bookingRepositoryPort.isRoomAvailable(
                booking.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (!isAvailable) {
            throw new RuntimeException("Phòng đã có người đặt trong thời gian này!");
        }

        // 3. Tạo mã và Lưu
        String randomCode = "BK-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        booking.setBookingCode(randomCode);
        booking.setStatus("CONFIRMED"); // Mặc định là Đã xác nhận (nhưng chưa thanh toán)

        Booking savedBooking = bookingRepositoryPort.save(booking);

        // 4. Gửi Email kèm Link QR Code
        try {
            GuestJpaEntity guest = guestRepo.findById(booking.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng"));

            // --- TẠO LINK VIETQR ---
            long amount = savedBooking.getTotalAmount().longValue();
            String content = "Thanh toan " + savedBooking.getBookingCode();
            
            // Encode nội dung để tránh lỗi URL (ví dụ khoảng trắng thành %20)
            String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8);
            String encodedName = URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8);

            String qrLink = String.format("https://img.vietqr.io/image/%s-%s-%s.png?amount=%d&addInfo=%s&accountName=%s",
                    BANK_ID, ACCOUNT_NO, TEMPLATE, amount, encodedContent, encodedName);

            // --- SOẠN EMAIL ---
            String emailSubject = "Xác nhận đặt phòng & Thanh toán - Mã: " + savedBooking.getBookingCode();
            
            StringBuilder body = new StringBuilder();
            body.append("Cảm ơn bạn đã đặt phòng tại Hotel Imperial!\n\n");
            body.append("Mã đặt phòng: ").append(savedBooking.getBookingCode()).append("\n");
            body.append("Tổng tiền: ").append(String.format("%,d", amount)).append(" VND\n\n");
            body.append("VUI LÒNG THANH TOÁN ĐỂ CHÚNG TÔI HOÀN TẤT ĐƠN ĐẶT CỦA BẠN.\n");
            body.append("Bạn có thể quét mã QR hoặc bấm vào link bên dưới để lấy mã thanh toán:\n");
            body.append(qrLink).append("\n\n"); // Link bấm vào sẽ hiện ảnh QR
            body.append("Ngân hàng: VietinBank\n");
            body.append("Số tài khoản: 108878399150\n");
            body.append("Chủ tài khoản: NGUYEN HA ANH\n");
            body.append("Nội dung chuyển khoản: ").append(content).append("\n\n");
            body.append("Vui lòng đưa mã đặt phòng cho lễ tân khi làm thủ tục nhận phòng.");

            emailService.sendEmail(guest.getEmail(), emailSubject, body.toString());

        } catch (Exception e) {
            System.err.println("Lỗi gửi email đặt phòng: " + e.getMessage());
        }

        return savedBooking;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepositoryPort.findAll();
    }
    @Override
    @Transactional
    public void cancelBooking(Long id) {
        // 1. Tìm đơn đặt phòng
        Booking booking = bookingRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng!"));

        // Kiểm tra nếu đã hủy rồi thì thôi
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Đơn đặt phòng này đã bị hủy trước đó!");
        }

        // 2. Tính số ngày từ Hiện tại đến Check-in
        LocalDate today = LocalDate.now();
        LocalDate checkInDate = booking.getCheckInDate();
        
        // ChronoUnit.DAYS.between(start, end) trả về số ngày
        long daysUntilCheckIn = ChronoUnit.DAYS.between(today, checkInDate);

        // 3. Logic Hoàn tiền
        int refundPercent = 0;
        if (daysUntilCheckIn >= 7) {
            refundPercent = 100;
        } else if (daysUntilCheckIn >= 3) {
            refundPercent = 50;
        } else {
            refundPercent = 0;
        }

        // Tính số tiền hoàn lại (Dùng BigDecimal để tính toán tiền tệ)
        BigDecimal totalAmount = booking.getTotalAmount();
        BigDecimal refundAmount = totalAmount
                .multiply(BigDecimal.valueOf(refundPercent))
                .divide(BigDecimal.valueOf(100));

        // 4. Cập nhật trạng thái Booking
        booking.setStatus("CANCELLED");
        bookingRepositoryPort.save(booking);
        // 5. Gửi Email thông báo
        try {
            GuestJpaEntity guest = guestRepo.findById(booking.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng"));

            String emailSubject = "Xác nhận Hủy phòng & Thông tin hoàn tiền - Mã: " + booking.getBookingCode();
            
            StringBuilder body = new StringBuilder();
            body.append("Xin chào ").append(guest.getFullName()).append(",\n\n");
            body.append("Chúng tôi xác nhận đơn đặt phòng ").append(booking.getBookingCode()).append(" của bạn đã được HỦY thành công.\n\n");
            body.append("--- CHI TIẾT HOÀN TIỀN ---\n");
            body.append("- Ngày hủy: ").append(today).append("\n");
            body.append("- Ngày Check-in dự kiến: ").append(checkInDate).append("\n");
            body.append("- Thời gian hủy trước: ").append(daysUntilCheckIn).append(" ngày.\n");
            
            if (daysUntilCheckIn < 0) {
                 body.append("- Lưu ý: Bạn đang hủy sau ngày check-in.\n");
            }

            body.append("- Chính sách áp dụng: ");
            if (refundPercent == 100) body.append("Hoàn 100% (Hủy trước >= 7 ngày)\n");
            else if (refundPercent == 50) body.append("Hoàn 50% (Hủy trước 3-6 ngày)\n");
            else body.append("Không hoàn tiền (Hủy trước < 3 ngày)\n");

            body.append("- Tổng tiền đơn hàng: ").append(String.format("%,.0f", totalAmount)).append(" VND\n");
            body.append("- SỐ TIỀN HOÀN LẠI: ").append(String.format("%,.0f", refundAmount)).append(" VND\n\n");
            
            body.append("Số tiền sẽ được hoàn về phương thức thanh toán ban đầu của bạn trong vòng 5-7 ngày làm việc.\n");
            body.append("Cảm ơn bạn đã quan tâm đến Hotel Imperial. Rất mong được phục vụ bạn trong tương lai!\n");

            emailService.sendEmail(guest.getEmail(), emailSubject, body.toString());

        } catch (Exception e) {
            System.err.println("Lỗi gửi email hủy phòng: " + e.getMessage());
            // Không throw exception để đảm bảo việc hủy phòng vẫn diễn ra thành công dù lỗi mail
        }
    }
    @Override
    public List<Booking> getBookingsByGuest(Long guestId) {
        return bookingRepositoryPort.findByGuestId(guestId);
    }
    @Override
    public Booking getBookingByCode(String bookingCode) {
        return bookingRepositoryPort.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng với mã: " + bookingCode));
    }
    @Override
    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng!"));
        
        booking.setStatus(status); // Ví dụ: update từ CONFIRMED sang PAID
        return bookingRepositoryPort.save(booking);
    }
}