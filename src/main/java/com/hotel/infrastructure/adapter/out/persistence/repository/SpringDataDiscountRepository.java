package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.DiscountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SpringDataDiscountRepository extends JpaRepository<DiscountJpaEntity, Long> {

    // --- LOGIC CỐT LÕI  ---
    // Tìm mã thỏa mãn:
    // 1. Đúng mã code
    // 2. Chưa hết hạn (endDate >= hôm nay)
    // 3. Chưa bị dùng (isUsed = false)
    // 4. (QUAN TRỌNG) guestId là NULL (ai cũng dùng đc) HOẶC guestId trùng với người đang nhập
    @Query("SELECT d FROM DiscountJpaEntity d WHERE d.code = :code " +
           "AND d.endDate >= :today " +
           "AND d.isUsed = false " +
           "AND (d.guestId IS NULL OR d.guestId = :guestId)")
    Optional<DiscountJpaEntity> findValidDiscount(@Param("code") String code, 
                                                  @Param("guestId") Long guestId, 
                                                  @Param("today") LocalDate today);
}