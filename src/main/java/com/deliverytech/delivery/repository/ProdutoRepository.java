package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestauranteId(Long restauranteId);
    List<Produto> findByDisponivelTrue();
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);
    List<Produto> findByRestaurante(Restaurante restaurante);
    List<Produto> findByRestauranteAndCategoria(Restaurante restaurante, String categoria);
    List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);
    
    @Query(value = "SELECT p.*, COUNT(ip.id) as vendas FROM produto p " +
           "LEFT JOIN item_pedido ip ON ip.produto_id = p.id " +
           "GROUP BY p.id ORDER BY vendas DESC", nativeQuery = true)
    List<Object[]> findProdutosMaisVendidos();
}