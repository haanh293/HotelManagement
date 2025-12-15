package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.DiscountUseCase;
import com.hotel.domain.model.Discount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountUseCase discountUseCase;

    public DiscountController(DiscountUseCase discountUseCase) {
        this.discountUseCase = discountUseCase;
    }

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        return ResponseEntity.ok(discountUseCase.createDiscount(discount));
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return ResponseEntity.ok(discountUseCase.getAllDiscounts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountUseCase.deleteDiscount(id);
        return ResponseEntity.ok().build();
    }
    // Kiểm tra mã (Chỉ check, không trừ số lượng)
    // URL: GET /api/discounts/check?code=SALE50&guestId=1
    @GetMapping("/check")
    public ResponseEntity<?> checkDiscount(@RequestParam String code, @RequestParam Long guestId) {
        try {
            return ResponseEntity.ok(discountUseCase.checkValidDiscount(code, guestId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // ÁP DỤNG MÃ (SẼ TRỪ SỐ LƯỢNG) ---
    // URL: POST /api/discounts/apply?code=SALE50&guestId=1
    @PostMapping("/apply")
    public ResponseEntity<?> applyDiscount(@RequestParam String code, @RequestParam Long guestId) {
        try {
            // Gọi hàm applyDiscount trong Service để trừ quantity
            Discount discount = discountUseCase.applyDiscount(code, guestId);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu mã hết hạn hoặc hết số lượng
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}