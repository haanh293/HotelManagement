package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataReviewRepository extends JpaRepository<ReviewJpaEntity, Long> {
    // Tìm các đánh giá của 1 hotel cụ thể
    List<ReviewJpaEntity> findByHotelId(Long hotelId);
}