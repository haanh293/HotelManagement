package com.hotel.application.port.in;

import com.hotel.domain.model.User;

public interface AuthUseCase {
    User login(String username, String password);
    void register(User user);
 // Hàm yêu cầu quên mật khẩu (Trả về String thông báo)
    String forgotPassword(String email);
    
    // Hàm đặt lại mật khẩu mới
    void resetPassword(String email, String token, String newPassword);
    User loginWithGoogle(String credential);
}