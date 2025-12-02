package com.hotel.infrastructure.adapter.in.web;

import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SpringDataUserRepository userRepo;

    public UserController(SpringDataUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // API 1: Lấy User theo Username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        UserJpaEntity entity = userRepo.findByUsername(username).orElse(null);
        if (entity != null) {
            return ResponseEntity.ok(new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole()));
        }
        return ResponseEntity.notFound().build();
    }
    
    // API 2: Lấy User theo ID
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        UserJpaEntity entity = userRepo.findById(id).orElse(null);
        if (entity != null) {
            return ResponseEntity.ok(new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole()));
        }
        return ResponseEntity.notFound().build();
    }
}