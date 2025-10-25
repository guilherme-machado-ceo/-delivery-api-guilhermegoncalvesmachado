
package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.Role;

import java.util.Set;

public record RegisterDTO(String username, String password, Set<Role> roles) {
}
