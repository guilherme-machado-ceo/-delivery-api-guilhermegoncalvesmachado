package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.Role;
import com.deliverytech.delivery.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserResponse(
        Long id,
        String nome,
        String email,
        Set<String> roles,
        Long restauranteId
) {
    public static UserResponse from(User user) {
        Set<String> roleNames = user.getRoles().stream()
                                    .map(Role::name)
                                    .collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getNome(), user.getEmail(), roleNames, user.getRestauranteId());
    }
}
