package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);
    List<Restaurante> findTop5ByOrderByNomeAsc();
    
    @Query("SELECT r, SUM(p.valorTotal) as totalVendas FROM Restaurante r LEFT JOIN Pedido p ON p.restaurante = r GROUP BY r")
    List<Object[]> getTotalVendasPorRestaurante();
}