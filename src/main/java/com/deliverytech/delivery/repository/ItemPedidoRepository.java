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
}