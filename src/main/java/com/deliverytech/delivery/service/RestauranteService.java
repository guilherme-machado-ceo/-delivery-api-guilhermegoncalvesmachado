package com.deliverytech.delivery.service;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RestauranteService {
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    public Restaurante cadastrar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
    }
    
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
    
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaIgnoreCase(categoria);
    }
    
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Restaurante atualizar(Long id, Restaurante restaurante) {
        Restaurante restauranteExistente = buscarPorId(id);
        
        restauranteExistente.setNome(restaurante.getNome());
        restauranteExistente.setCategoria(restaurante.getCategoria());
        restauranteExistente.setEndereco(restaurante.getEndereco());
        restauranteExistente.setTaxaEntrega(restaurante.getTaxaEntrega());
        restauranteExistente.setAvaliacao(restaurante.getAvaliacao());
        
        return restauranteRepository.save(restauranteExistente);
    }
    
    public void alterarStatus(Long id, boolean ativo) {
        Restaurante restaurante = buscarPorId(id);
        restaurante.setAtivo(ativo);
        restauranteRepository.save(restaurante);
    }
    
    public List<Restaurante> listarPorAvaliacao() {
        return restauranteRepository.findAllByOrderByAvaliacaoDesc();
    }
}