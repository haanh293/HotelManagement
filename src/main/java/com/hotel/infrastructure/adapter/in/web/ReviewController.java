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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Review>> getReviewsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(useCase.getReviewsByRoom(roomId));
    }
}