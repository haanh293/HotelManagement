package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.FavoriteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataFavoriteRepository extends JpaRepository<FavoriteJpaEntity, Long> {
    // Tìm danh sách yêu thích của một khách
    List<FavoriteJpaEntity> findByGuestId(Long guestId);
    
    // Kiểm tra xem khách đã thích khách sạn này chưa (để nút trái tim sáng lên)
    Optional<FavoriteJpaEntity> findByGuestIdAndHotelId(Long guestId, Long hotelId);
    
    // Xóa (Bỏ thích)
    void deleteByGuestIdAndHotelId(Long guestId, Long hotelId);
}