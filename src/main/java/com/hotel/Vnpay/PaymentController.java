package com.hotel.Vnpay;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/vnpay/{bookingId}")
    public ResponseEntity<String> createVnPayPayment(
            @PathVariable Long bookingId,
            HttpServletRequest request
    ) throws Exception {
        String ipAddress = extractClientIp(request);
        String paymentUrl = paymentService.createVnPayPayment(bookingId, ipAddress);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/vnpay-return")
    // Kiểu trả về vẫn là ResponseEntity<String>
    public ResponseEntity<VnPayDTO> handleVnPayReturn(@RequestParam Map<String, String> allParams) {
        boolean isValid = paymentService.handleVnPayReturn(allParams);
        Long orderId = Long.parseLong(allParams.get("vnp_TxnRef"));
        if (isValid) {

            // Trả về 200 OK với chuỗi thông báo thành công
            VnPayDTO successDTO = VnPayDTO.builder()
                    .code(200)
                    .orderId(orderId)
                    .build();
            return ResponseEntity.ok(successDTO);
        } else {
            VnPayDTO errorDTO = VnPayDTO.builder()
                    .code(500)
                    .orderId(orderId)
                    .build();
            return ResponseEntity.badRequest().body(errorDTO);

        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}