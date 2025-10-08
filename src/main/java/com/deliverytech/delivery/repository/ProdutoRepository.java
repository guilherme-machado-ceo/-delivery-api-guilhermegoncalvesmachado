package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestaurante(Restaurante restaurante);
    List<Produto> findByRestauranteAndCategoria(Restaurante restaurante, String categoria);
    List<Produto> findByRestauranteAndDisponivel(Restaurante restaurante, boolean disponivel);
}