package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import java.util.List;

public interface ClienteServiceInterface {
    
    /**
     * Cadastra um novo cliente validando email único
     */
    ClienteResponseDTO cadastrarCliente(ClienteDTO dto);
    
    /**
     * Busca cliente por ID com tratamento de não encontrado
     */
    ClienteResponseDTO buscarClientePorId(Long id);
    
    /**
     * Busca cliente por email para login/autenticação
     */
    ClienteResponseDTO buscarClientePorEmail(String email);
    
    /**
     * Atualiza cliente validando existência
     */
    ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto);
    
    /**
     * Ativa/desativa cliente (toggle status ativo)
     */
    ClienteResponseDTO ativarDesativarCliente(Long id);
    
    /**
     * Lista apenas clientes ativos
     */
    List<ClienteResponseDTO> listarClientesAtivos();
}