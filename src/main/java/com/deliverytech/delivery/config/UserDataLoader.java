package com.deliverytech.delivery.config;

import com.deliverytech.delivery.model.Role;
import com.deliverytech.delivery.model.User;
import com.deliverytech.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Profile("!prod")
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Criar usuário admin se não existir
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ADMIN));
            admin.setEnabled(true);
            
            userRepository.save(admin);
            System.out.println("Usuário admin criado com senha: admin123");
        }
        
        // Criar usuário manager se não existir
        if (!userRepository.existsByUsername("manager")) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRoles(Set.of(Role.MANAGER));
            manager.setEnabled(true);
            
            userRepository.save(manager);
            System.out.println("Usuário manager criado com senha: manager123");
        }
        
        // Criar usuário comum se não existir
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(Set.of(Role.USER));
            user.setEnabled(true);
            
            userRepository.save(user);
            System.out.println("Usuário user criado com senha: user123");
        }
    }
}