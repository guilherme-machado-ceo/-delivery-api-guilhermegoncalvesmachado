package com.deliverytech.delivery.service;

import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.StatusPedido;
import com.deliverytech.delivery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteService clienteService;
    
    public Pedido criar(Pedido pedido) {
        // Validações básicas
        if (pedido.getCliente() == null || pedido.getRestaurante() == null) {
            throw new IllegalArgumentException("Cliente e restaurante são obrigatórios");
        }
        
        // Verifica se o cliente existe
        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        pedido.setCliente(cliente);
        
        // Define status inicial
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }
    
    public List<Pedido> listarPorCliente(Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        return pedidoRepository.findByCliente(cliente);
    }
    
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
    
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }
    
    public List<Pedido> buscarPedidosClienteOrdenadosPorData(Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        return pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente);
    }
}