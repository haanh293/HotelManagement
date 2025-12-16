package com.hotel.application.service;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.application.port.in.InvoiceUseCase;
import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import com.hotel.domain.model.Invoice;             
import com.hotel.domain.model.InvoiceStatus;
import org.springframework.stereotype.Service;
import com.hotel.infrastructure.adapter.out.external.EmailService; // Import Service gửi mail
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository; // Import Repo Guest
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService implements BookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final EmailService emailService;             
    private final SpringDataGuestRepository guestRepo;
    private final InvoiceUseCase invoiceUseCase;
    public BookingService(BookingRepositoryPort bookingRepositoryPort, EmailService emailService, SpringDataGuestRepository guestRepo, InvoiceUseCase invoiceUseCase) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.emailService = emailService;
        this.guestRepo = guestRepo;
        this.invoiceUseCase = invoiceUseCase;
    }
    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        // Đảm bảo khách đã chọn loại phòng
        if (booking.getRoomType() == null || booking.getRoomType().isEmpty()) {
            throw new RuntimeException("Vui lòng chọn loại phòng (roomType)!");
        }
        
        // (Tùy chọn) Kiểm tra các trường khác nếu cần
        // if (booking.getViewType() == null) ...
        // ------------------------------------

        // 2. Kiểm tra phòng trống
        boolean isAvailable = bookingRepositoryPort.isRoomAvailable(
                booking.getRoomId(), 
                booking.getCheckInDate(), 
                booking.getCheckOutDate()
        );

        if (!isAvailable) {
            throw new RuntimeException("Phòng đã có người đặt trong thời gian này!");
        }

        // 3. Lưu
        String randomCode = "BK-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        booking.setBookingCode(randomCode);
        
        booking.setStatus("CONFIRMED");
        
        // Lưu booking
        Booking savedBooking = bookingRepositoryPort.save(booking);
     // --- 6. LOGIC MỚI: TỰ ĐỘNG TẠO INVOICE (UNPAID) ---
        // =================================================================
        Invoice newInvoice = new Invoice();
        newInvoice.setBookingId(savedBooking.getId());       // Liên kết với Booking vừa tạo
        newInvoice.setTotalAmount(savedBooking.getTotalAmount().doubleValue()); // Lấy tổng tiền
        newInvoice.setPaymentDate(null);                     // Chưa thanh toán nên ngày = null
        newInvoice.setPaymentMethod(null);                   // Chưa chọn phương thức
        newInvoice.setStatus(InvoiceStatus.UNPAID);          // Trạng thái mặc định
        
        // Gọi InvoiceService để lưu
        invoiceUseCase.createInvoice(newInvoice);
        try {
            // Lấy thông tin khách hàng để biết email
            GuestJpaEntity guest = guestRepo.findById(booking.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng"));

            String emailSubject = "Xác nhận đặt phòng thành công - Mã: " + savedBooking.getBookingCode();
            String emailBody = "Cảm ơn bạn đã đặt phòng tại Hotel Imperial!\n\n" +
                    "Mã đặt phòng của bạn: " + savedBooking.getBookingCode() + "\n" +
                    "Ngày nhận phòng: " + savedBooking.getCheckInDate() + "\n" +
                    "Ngày trả phòng: " + savedBooking.getCheckOutDate() + "\n" +
                    "Tổng tiền: " + savedBooking.getTotalAmount() + "\n\n" +
                    "Vui lòng đưa mã này cho lễ tân khi làm thủ tục nhận phòng.";

            emailService.sendEmail(guest.getEmail(), emailSubject, emailBody);

        } catch (Exception e) {
            // Log lỗi nhưng không chặn luồng (Vẫn cho đặt phòng thành công dù gửi mail lỗi)
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
        invoiceUseCase.cancelInvoiceByBookingId(id);
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
}