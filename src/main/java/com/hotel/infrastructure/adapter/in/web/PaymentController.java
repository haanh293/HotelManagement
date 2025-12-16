package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.InvoiceUseCase;
import com.hotel.application.service.PaymentService;
import com.hotel.domain.model.Invoice;
import com.hotel.domain.model.InvoiceStatus;
import com.hotel.infrastructure.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final InvoiceUseCase invoiceUseCase;

    public PaymentController(PaymentService paymentService, InvoiceUseCase invoiceUseCase) {
        this.paymentService = paymentService;
        this.invoiceUseCase = invoiceUseCase;
    }

    // 1. API TẠO URL THANH TOÁN
    // URL: GET /api/payment/create-payment?invoiceId=1
    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam Long invoiceId) {
        try {
            // Lấy hóa đơn
            Invoice invoice = invoiceUseCase.getInvoiceById(invoiceId);
            if (invoice == null) return ResponseEntity.badRequest().body("Hóa đơn không tồn tại");

            // Tạo URL VNPay
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String paymentUrl = paymentService.createVnPayPayment(invoice, baseUrl, VNPayConfig.getIpAddress(request));

            // Cập nhật trạng thái thành PENDING
            invoiceUseCase.updateInvoiceStatus(invoiceId, InvoiceStatus.PENDING);

            // Trả về URL để Frontend chuyển hướng
            return ResponseEntity.ok(Collections.singletonMap("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. API NHẬN KẾT QUẢ TỪ VNPAY (RETURN URL)
    @GetMapping("/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Lấy các tham số VNPay trả về
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        
        // Xóa hash params để kiểm tra chữ ký
        if (fields.containsKey("vnp_SecureHashType")) fields.remove("vnp_SecureHashType");
        if (fields.containsKey("vnp_SecureHash")) fields.remove("vnp_SecureHash");

        // Kiểm tra chữ ký bảo mật
        // Sắp xếp và hash lại dữ liệu nhận được
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(java.net.URLEncoder.encode(fieldValue, java.nio.charset.StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        
        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());

        // --- XỬ LÝ KẾT QUẢ ---
        if (signValue.equals(vnp_SecureHash)) {
            // Chữ ký hợp lệ
            String transactionStatus = request.getParameter("vnp_TransactionStatus");
            String invoiceIdStr = request.getParameter("vnp_TxnRef");
            Long invoiceId = Long.parseLong(invoiceIdStr);

            if ("00".equals(transactionStatus)) {
                // Thanh toán THÀNH CÔNG -> Cập nhật PAID
                invoiceUseCase.updateInvoiceStatus(invoiceId, InvoiceStatus.PAID);
                response.sendRedirect("http://localhost:3000/payment-success"); // Chuyển hướng về Frontend
            } else {
                // Thanh toán THẤT BẠI -> Cập nhật UNPAID hoặc FAILED
                invoiceUseCase.updateInvoiceStatus(invoiceId, InvoiceStatus.UNPAID);
                response.sendRedirect("http://localhost:3000/payment-failed");
            }
        } else {
            // Chữ ký không hợp lệ (Có thể do tấn công giả mạo)
            response.sendRedirect("http://localhost:3000/payment-error");
        }
    }
}