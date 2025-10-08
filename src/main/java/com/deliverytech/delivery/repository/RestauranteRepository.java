package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    List<Restaurante> findByCategoriaIgnoreCase(String categoria);
    List<Restaurante> findByAtivo(boolean ativo);
    
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findAllByOrderByAvaliacaoDesc();
}