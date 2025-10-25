package com.deliverytech.delivery.util;

import com.deliverytech.delivery.model.User;
import com.deliverytech.delivery.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityUtils")
public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return (user != null) ? user.getId() : null;
    }

    public static boolean hasRole(String role) {
        User user = getCurrentUser();
        if (user == null || user.getRoles() == null) {
            return false;
        }
        try {
            Role targetRole = Role.valueOf(role);
            return user.getRoles().contains(targetRole);
        } catch (IllegalArgumentException e) {
            return false; // Role não existe na enumeração
        }
    }
}
