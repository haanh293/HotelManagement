package com.hotel.infrastructure.adapter.in.web;

import com.hotel.application.port.in.AuthUseCase;
import com.hotel.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        User user = authUseCase.login(username, password);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            authUseCase.register(user);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        try {
            String msg = authUseCase.forgotPassword(payload.get("email"));
            return ResponseEntity.ok(msg);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            authUseCase.resetPassword(
                payload.get("email"), 
                payload.get("token"), 
                payload.get("newPassword")
            );
            return ResponseEntity.ok("Đổi mật khẩu thành công! Hãy đăng nhập lại.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String credential = payload.get("credential");

        if (credential == null) {
            return ResponseEntity.badRequest().body("Thiếu Token Google!");
        }

        try {
            User user = authUseCase.loginWithGoogle(credential);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
 // URL: POST /api/auth/change-password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> payload) {
        try {
            // Lấy dữ liệu từ JSON (payload)
            // payload.get trả về Object nên cần ép kiểu hoặc toString
            Long userId = Long.valueOf(payload.get("userId").toString());
            String oldPassword = (String) payload.get("oldPassword");
            String newPassword = (String) payload.get("newPassword");
            String confirmPassword = (String) payload.get("confirmPassword");

            // Gọi Service xử lý
            authUseCase.changePassword(userId, oldPassword, newPassword, confirmPassword);
            
            return ResponseEntity.ok("Đổi mật khẩu thành công!");
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("ID người dùng không hợp lệ!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Dữ liệu gửi lên bị thiếu hoặc sai định dạng!");
        }
    }
}