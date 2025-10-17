package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    /**
     * Busca todos os itens de um pedido específico
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    /**
     * Busca itens por produto
     */
    List<ItemPedido> findByProdutoId(Long produtoId);
    
    /**
     * Remove todos os itens de um pedido
     */
    void deleteByPedidoId(Long pedidoId);
    
    /**
     * Busca produtos mais vendidos por período
     */
    @org.springframework.data.jpa.repository.Query(value = "SELECT p.id, p.nome, r.nome, SUM(ip.quantidade), SUM(ip.subtotal) " +
           "FROM item_pedido ip " +
           "JOIN produto p ON ip.produto_id = p.id " +
           "JOIN restaurante r ON p.restaurante_id = r.id " +
           "JOIN pedido ped ON ip.pedido_id = ped.id " +
           "WHERE ped.data_pedido BETWEEN :inicio AND :fim " +
           "GROUP BY p.id, p.nome, r.nome " +
           "ORDER BY SUM(ip.quantidade) DESC " +
           "LIMIT :limite", nativeQuery = true)
    List<Object[]> findProdutosMaisVendidos(
        @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio, 
        @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim,
        @org.springframework.data.repository.query.Param("limite") int limite
    );
}