package com.hotel.application.port.in;

import com.hotel.domain.model.User;

public interface AuthUseCase {
    User login(String username, String password);
    void register(User user);
    String forgotPassword(String email);
    void resetPassword(String email, String token, String newPassword);
    User loginWithGoogle(String credential);
    
    void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword);
}