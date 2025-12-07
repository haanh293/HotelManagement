package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.ReviewRepositoryPort;
import com.hotel.domain.model.Review;
import com.hotel.infrastructure.adapter.out.persistence.entity.ReviewJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataReviewRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewPersistenceAdapter implements ReviewRepositoryPort {
    private final SpringDataReviewRepository repo;

    public ReviewPersistenceAdapter(SpringDataReviewRepository repo) {
        this.repo = repo;
    }

    @Override
    public Review save(Review review) {
        ReviewJpaEntity entity = new ReviewJpaEntity();
        entity.setId(review.getId());
        entity.setGuestId(review.getGuestId());
        entity.setRoomId(review.getRoomId());
        entity.setRating(review.getRating());
        entity.setComment(review.getComment());
        entity.setReviewDate(review.getReviewDate());
        return mapToDomain(repo.save(entity));
    }

    @Override
    public List<Review> findByRoomId(Long roomId) {
        return repo.findByRoomId(roomId).stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    private Review mapToDomain(ReviewJpaEntity e) {
        return new Review(e.getId(), e.getGuestId(), e.getRoomId(), e.getRating(), e.getComment(), e.getReviewDate());
    }
}