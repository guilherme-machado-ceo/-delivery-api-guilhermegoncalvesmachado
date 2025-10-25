package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.Role;
import java.util.Set;

public record LoginResponse(
        String token,
        String type,
        long expiresIn,
        UserResponse user
) {
    public LoginResponse(String token, long expiresIn, UserResponse user) {
        this(token, "Bearer", expiresIn, user);
    }
}
