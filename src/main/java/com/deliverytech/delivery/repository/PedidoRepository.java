package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status);
    List<Pedido> findTop10ByOrderByDataPedidoDesc();
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);
    
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor")
    List<Pedido> findPedidosComValorMaiorQue(@Param("valor") BigDecimal valor);
    
    @Query("SELECT p FROM Pedido p WHERE p.status = :status AND p.dataPedido BETWEEN :inicio AND :fim")
    List<Pedido> findRelatorioPorPeriodoEStatus(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim, 
        @Param("status") StatusPedido status);
    
    @Query(value = "SELECT c.*, COUNT(p.id) as total_pedidos FROM cliente c " +
           "LEFT JOIN pedido p ON p.cliente_id = c.id " +
           "GROUP BY c.id ORDER BY total_pedidos DESC", nativeQuery = true)
    List<Object[]> rankingClientesPorNumeroPedidos();
}