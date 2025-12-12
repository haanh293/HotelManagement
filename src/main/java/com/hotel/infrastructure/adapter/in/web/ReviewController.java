package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.ReviewUseCase;
import com.hotel.domain.model.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewUseCase useCase;

    public ReviewController(ReviewUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        return ResponseEntity.ok(useCase.addReview(review));
    }

    @GetMapping("/hotel/{hotelId}") 
    public ResponseEntity<List<Review>> getReviewsByHotel(@PathVariable Long hotelId) {
        // 2. Gọi hàm mới trong Service
        return ResponseEntity.ok(useCase.getReviewsByHotel(hotelId));
    }
}