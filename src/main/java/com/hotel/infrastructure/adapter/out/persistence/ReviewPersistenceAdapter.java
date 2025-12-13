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
        
        // --- 1. SỬA ĐỔI: setHotelId thay vì setRoomId ---
        entity.setHotelId(review.getHotelId()); 
        // ------------------------------------------------
        
        entity.setRating(review.getRating());
        entity.setComment(review.getComment());
        entity.setReviewDate(review.getReviewDate());
        
        return mapToDomain(repo.save(entity));
    }

    // --- 2. SỬA ĐỔI: Tìm theo HotelId ---
    // Lưu ý: Bạn cần sửa tên hàm này trong interface ReviewRepositoryPort 
    // và SpringDataReviewRepository tương ứng.
    @Override
    public List<Review> findByHotelId(Long hotelId) {
        return repo.findByHotelId(hotelId)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Review> findAll() {
        return repo.findAll()             // 1. Gọi JpaRepository lấy hết Entity
                .stream()                 // 2. Mở Stream để xử lý
                .map(this::mapToDomain)   // 3. Chuyển từng Entity thành Domain
                .collect(Collectors.toList()); // 4. Gom lại thành List
    }
    private Review mapToDomain(ReviewJpaEntity e) {
        // --- 3. SỬA ĐỔI: Constructor nhận hotelId ---
        return new Review(
            e.getId(), 
            e.getGuestId(), 
            e.getHotelId(), // <-- Thay thế e.getRoomId()
            e.getRating(), 
            e.getComment(), 
            e.getReviewDate()
        );
    }
}