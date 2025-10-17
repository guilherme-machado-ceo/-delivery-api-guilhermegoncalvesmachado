package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import java.util.List;

public interface ProdutoServiceInterface {
    
    /**
     * Cadastra um novo produto validando se o restaurante existe
     */
    ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto);
    
    /**
     * Busca produtos por restaurante (apenas disponíveis)
     */
    List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId);
    
    /**
     * Busca produto por ID com validação de disponibilidade
     */
    ProdutoResponseDTO buscarProdutoPorId(Long id);
    
    /**
     * Atualiza produto com validações completas
     */
    ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto);
    
    /**
     * Altera disponibilidade do produto (toggle)
     */
    ProdutoResponseDTO alterarDisponibilidade(Long id, boolean disponivel);
    
    /**
     * Busca produtos por categoria
     */
    List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria);
    
    /**
     * Lista todos os produtos disponíveis
     */
    List<ProdutoResponseDTO> listarProdutosDisponiveis();
    
    /**
     * Busca produtos por nome (contém)
     */
    List<ProdutoResponseDTO> buscarProdutosPorNome(String nome);
}