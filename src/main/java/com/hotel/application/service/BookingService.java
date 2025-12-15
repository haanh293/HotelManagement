package com.hotel.application.service;

import com.hotel.application.port.in.BookingUseCase;
import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import org.springframework.stereotype.Service;
import com.hotel.infrastructure.adapter.out.external.EmailService; // Import Service gửi mail
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository; // Import Repo Guest
import java.util.List;
import java.util.UUID;

@Service
public class BookingService implements BookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final EmailService emailService;              // 1. Thêm EmailService
    private final SpringDataGuestRepository guestRepo;
    public BookingService(BookingRepositoryPort bookingRepositoryPort, EmailService emailService, SpringDataGuestRepository guestRepo) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.emailService = emailService;
        this.guestRepo = guestRepo;
    }

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
    public void cancelBooking(Long id) {
        // 1. Tìm đặt phòng theo ID
        Booking booking = bookingRepositoryPort.findById(id).orElse(null);
        
        // 2. Nếu tìm thấy thì đổi trạng thái
        if (booking != null) {
            booking.setStatus("CANCELLED");
            bookingRepositoryPort.save(booking);
        }
    }
    @Override
    public List<Booking> getBookingsByGuest(Long guestId) {
        return bookingRepositoryPort.findByGuestId(guestId);
    }
}