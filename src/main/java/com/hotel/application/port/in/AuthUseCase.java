package com.hotel.application.port.in;

import com.hotel.domain.model.User;

public interface AuthUseCase {
    User login(String username, String password);
    void register(User user);
}