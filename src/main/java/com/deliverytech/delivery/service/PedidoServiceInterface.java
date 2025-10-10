package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.StatusPedido;
import java.math.BigDecimal;
import java.util.List;

public interface PedidoServiceInterface {
    
    /**
     * Cria um pedido com transação complexa
     * Valida cliente, restaurante, produtos e calcula total
     */
    PedidoResponseDTO criarPedido(PedidoDTO dto);
    
    /**
     * Busca pedido por ID com todos os itens
     */
    PedidoResponseDTO buscarPedidoPorId(Long id);
    
    /**
     * Busca pedidos por cliente (histórico)
     */
    List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId);
    
    /**
     * Atualiza status do pedido validando transições
     */
    PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status);
    
    /**
     * Calcula total do pedido sem salvar
     */
    BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens, Long restauranteId);
    
    /**
     * Cancela pedido (apenas se permitido pelo status)
     */
    PedidoResponseDTO cancelarPedido(Long id);
    
    /**
     * Lista pedidos por status
     */
    List<PedidoResumoDTO> buscarPedidosPorStatus(StatusPedido status);
    
    /**
     * Busca pedidos recentes (últimos 10)
     */
    List<PedidoResumoDTO> buscarPedidosRecentes();
}