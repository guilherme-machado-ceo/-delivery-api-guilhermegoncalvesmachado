package com.deliverytech.delivery.service;

import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private RestauranteService restauranteService;
    
    public Produto cadastrar(Produto produto, Long restauranteId) {
        if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
        produto.setRestaurante(restaurante);
        
        return produtoRepository.save(produto);
    }
    
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }
    
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
        return produtoRepository.findByRestaurante(restaurante);
    }
    
    public List<Produto> listarPorRestauranteECategoria(Long restauranteId, String categoria) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
        return produtoRepository.findByRestauranteAndCategoria(restaurante, categoria);
    }
    
    public Produto atualizar(Long id, Produto produto) {
        Produto produtoExistente = buscarPorId(id);
        
        if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setCategoria(produto.getCategoria());
        produtoExistente.setDisponivel(produto.isDisponivel());
        
        return produtoRepository.save(produtoExistente);
    }
    
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = buscarPorId(id);
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }
}