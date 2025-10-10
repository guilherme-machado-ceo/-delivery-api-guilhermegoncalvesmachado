package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca usuário por username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Verifica se existe usuário com o username
     */
    boolean existsByUsername(String username);
    
    /**
     * Busca usuários ativos
     */
    java.util.List<User> findByEnabledTrue();
}