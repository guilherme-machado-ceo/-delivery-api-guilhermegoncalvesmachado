package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários ativos
     */
    java.util.List<User> findByEnabledTrue();
}