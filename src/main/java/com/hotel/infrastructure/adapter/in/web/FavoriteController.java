package com.hotel.infrastructure.adapter.in.web;

import com.hotel.infrastructure.adapter.out.persistence.entity.FavoriteJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataFavoriteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final SpringDataFavoriteRepository favoriteRepo;

    public FavoriteController(SpringDataFavoriteRepository favoriteRepo) {
        this.favoriteRepo = favoriteRepo;
    }

    // 1. Lấy danh sách yêu thích của khách (Theo Guest ID)
    @GetMapping("/{guestId}")
    public ResponseEntity<List<FavoriteJpaEntity>> getFavorites(@PathVariable Long guestId) {
        return ResponseEntity.ok(favoriteRepo.findByGuestId(guestId));
    }

    // 2. Thêm vào yêu thích
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Long> payload) {
        Long guestId = payload.get("guestId");
        Long hotelId = payload.get("hotelId");

        // Kiểm tra đã thích chưa
        if (favoriteRepo.findByGuestIdAndHotelId(guestId, hotelId).isPresent()) {
            return ResponseEntity.badRequest().body("Bạn đã thích khách sạn này rồi!");
        }

        FavoriteJpaEntity fav = new FavoriteJpaEntity(guestId, hotelId);
        favoriteRepo.save(fav);
        return ResponseEntity.ok("Đã thêm vào danh sách yêu thích!");
    }

    // 3. Bỏ thích (Xóa)
    @DeleteMapping
    @Transactional
    public ResponseEntity<?> removeFavorite(@RequestParam Long guestId, @RequestParam Long hotelId) {
        favoriteRepo.deleteByGuestIdAndHotelId(guestId, hotelId);
        return ResponseEntity.ok("Đã xóa khỏi danh sách yêu thích!");
    }
}