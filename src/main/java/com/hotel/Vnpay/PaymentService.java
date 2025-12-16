package com.hotel.Vnpay;

import com.hotel.application.port.out.BookingRepositoryPort;
import com.hotel.domain.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VnPayService vnPayService;
    private final BookingRepositoryPort bookingRepositoryPort;
    private static final String VNPAY_ORDER_INFO_PREFIX = "Thanh toan don hang ";
    public String createVnPayPayment(Long booKingId, String ipAddress) throws Exception {
        Booking booking = bookingRepositoryPort.findById(booKingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        String orderInfo = VNPAY_ORDER_INFO_PREFIX + booking.getId();
        VnPayModel vnPayModel = VnPayModel.builder()
                .price(booking.getTotalAmount())
                .vnTxnRef(booKingId.toString())
                .orderInfo(VNPAY_ORDER_INFO_PREFIX+" "+booKingId)
                .language("vn")
                .ipAddress(ipAddress)
                .build();

        return vnPayService.createUrl(vnPayModel);
    }
    public boolean handleVnPayReturn(Map<String, String> params) {
        boolean isValidSignature = vnPayService.verifySignature(params);
        if (!isValidSignature) {
            throw new RuntimeException("Thanh toán không hop lệ");
        }
        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        if (txnRef == null || txnRef.isBlank()) {
            throw new RuntimeException("Thanh toán không hop lệ");
        }
        Booking booking = bookingRepositoryPort.findById(Long.parseLong(txnRef))
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        boolean success = "00".equals(responseCode);
        if (success ) {
            booking.setStatus("PAID");
            bookingRepositoryPort.save(booking);
        } else {
            booking.setStatus("PAYMENT_FAILED");
            bookingRepositoryPort.save(booking);
        }
        System.out.println(responseCode);
        return success;
    }

}
