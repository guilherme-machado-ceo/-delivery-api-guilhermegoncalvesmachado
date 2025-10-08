package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByClienteAndStatus(Cliente cliente, String status);
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente = ?1 ORDER BY p.dataPedido DESC")
    List<Pedido> findClienteOrderByDataDesc(Cliente cliente);
}