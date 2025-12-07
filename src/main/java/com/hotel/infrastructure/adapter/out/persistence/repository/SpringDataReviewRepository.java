package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataReviewRepository extends JpaRepository<ReviewJpaEntity, Long> {
    // Tìm các đánh giá của một phòng cụ thể
    List<ReviewJpaEntity> findByRoomId(Long roomId);
}