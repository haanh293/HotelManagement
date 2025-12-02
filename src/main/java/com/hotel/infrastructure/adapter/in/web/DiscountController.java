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
}