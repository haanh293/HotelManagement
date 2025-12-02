package com.hotel.application.service;

import com.hotel.application.port.in.AuthUseCase;
import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final SpringDataUserRepository userRepo;

    public AuthService(SpringDataUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User login(String username, String password) {
        // 1. Tìm user trong DB
        UserJpaEntity entity = userRepo.findByUsername(username).orElse(null);

        // 2. Kiểm tra mật khẩu (So sánh chuỗi thường cho nhanh)
        if (entity != null && entity.getPassword().equals(password)) {
            return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
        }
        
        // 3. Đăng nhập thất bại
        return null; 
    }
    @Override
    public void register(User user) {
        // 1. Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // 2. Chuyển đổi từ Domain sang Entity
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole()); // Mặc định có thể để là "GUEST" hoặc lấy từ input

        // 3. Lưu vào DB
        userRepo.save(entity);
    }
}