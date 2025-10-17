package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    
    List<Pedido> findByRestauranteIdOrderByDataPedidoDesc(Long restauranteId);
    
    @Query("SELECT p FROM Pedido p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:dataInicio IS NULL OR DATE(p.dataPedido) >= :dataInicio) AND " +
           "(:dataFim IS NULL OR DATE(p.dataPedido) <= :dataFim) AND " +
           "(:clienteId IS NULL OR p.cliente.id = :clienteId) AND " +
           "(:restauranteId IS NULL OR p.restaurante.id = :restauranteId) " +
           "ORDER BY p.dataPedido DESC")
    Page<Pedido> findWithFilters(
        @Param("status") StatusPedido status,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim,
        @Param("clienteId") Long clienteId,
        @Param("restauranteId") Long restauranteId,
        Pageable pageable
    );
    
    // Métodos para relatórios
    @Query("SELECT r.id, r.nome, COUNT(p.id), COALESCE(SUM(p.total), 0), " +
           "CASE WHEN COUNT(p.id) > 0 THEN COALESCE(SUM(p.total), 0) / COUNT(p.id) ELSE 0 END " +
           "FROM Restaurante r LEFT JOIN Pedido p ON p.restaurante.id = r.id " +
           "WHERE p.dataPedido BETWEEN :inicio AND :fim " +
           "GROUP BY r.id, r.nome ORDER BY SUM(p.total) DESC")
    List<Object[]> findVendasPorRestaurante(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query(value = "SELECT c.id, c.nome, c.email, COUNT(p.id), COALESCE(SUM(p.total), 0), " +
           "CASE WHEN COUNT(p.id) > 0 THEN COALESCE(SUM(p.total), 0) / COUNT(p.id) ELSE 0 END " +
           "FROM cliente c LEFT JOIN pedido p ON p.cliente_id = c.id " +
           "WHERE p.data_pedido BETWEEN :inicio AND :fim " +
           "GROUP BY c.id, c.nome, c.email ORDER BY COUNT(p.id) DESC " +
           "LIMIT :limite", nativeQuery = true)
    List<Object[]> findClientesMaisAtivos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, @Param("limite") int limite);
    
    long countByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim")
    BigDecimal sumTotalByDataPedidoBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    long countByStatusAndDataPedidoBetween(StatusPedido status, LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT DATE(p.dataPedido), COUNT(p.id), COALESCE(SUM(p.total), 0) " +
           "FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim " +
           "GROUP BY DATE(p.dataPedido) ORDER BY DATE(p.dataPedido)")
    List<Object[]> findPedidosPorDia(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}