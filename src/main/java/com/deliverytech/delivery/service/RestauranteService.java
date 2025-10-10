package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService implements RestauranteServiceInterface {
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO dto) {
        // Validações de negócio
        if (dto.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega não pode ser negativa");
        }
        
        if (dto.getAvaliacao() != null && (dto.getAvaliacao() < 0 || dto.getAvaliacao() > 5)) {
            throw new BusinessException("Avaliação deve estar entre 0 e 5");
        }
        
        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
        restaurante.setAtivo(true);
        
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarRestaurantePorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria) {
        List<Restaurante> restaurantes = restauranteRepository.findByCategoria(categoria);
        
        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesDisponiveis() {
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        
        return restaurantesAtivos.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto) {
        Restaurante restauranteExistente = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        // Validações de negócio
        if (dto.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega não pode ser negativa");
        }
        
        if (dto.getAvaliacao() != null && (dto.getAvaliacao() < 0 || dto.getAvaliacao() > 5)) {
            throw new BusinessException("Avaliação deve estar entre 0 e 5");
        }
        
        // Atualizar campos
        restauranteExistente.setNome(dto.getNome());
        restauranteExistente.setCategoria(dto.getCategoria());
        restauranteExistente.setEndereco(dto.getEndereco());
        restauranteExistente.setTaxaEntrega(dto.getTaxaEntrega());
        restauranteExistente.setAvaliacao(dto.getAvaliacao());
        
        Restaurante restauranteAtualizado = restauranteRepository.save(restauranteExistente);
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
    }
    
    @Override
    public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", restauranteId));
        
        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante não está disponível para entrega");
        }
        
        // Lógica simples de cálculo de taxa baseada no CEP
        // Em um sistema real, isso seria integrado com APIs de geolocalização
        BigDecimal taxaBase = restaurante.getTaxaEntrega();
        
        // Simular cálculo baseado na distância (primeiros dígitos do CEP)
        if (cep != null && cep.length() >= 5) {
            String prefixoCep = cep.substring(0, 5);
            
            // Lógica fictícia: CEPs mais distantes têm taxa maior
            switch (prefixoCep.substring(0, 2)) {
                case "01", "02", "03", "04", "05" -> {
                    // São Paulo centro - taxa normal
                    return taxaBase;
                }
                case "06", "07", "08", "09" -> {
                    // São Paulo zona leste/oeste - taxa +50%
                    return taxaBase.multiply(new BigDecimal("1.5"));
                }
                default -> {
                    // Outras regiões - taxa +100%
                    return taxaBase.multiply(new BigDecimal("2.0"));
                }
            }
        }
        
        return taxaBase;
    }
    
    @Override
    public RestauranteResponseDTO ativarDesativarRestaurante(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        restaurante.setAtivo(!restaurante.isAtivo());
        
        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
    }
    
    // Métodos legados para compatibilidade (deprecated)
    @Deprecated
    public Restaurante cadastrar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    @Deprecated
    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Restaurante não encontrado"));
    }
    
    @Deprecated
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
    
    @Deprecated
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }
    
    @Deprecated
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findTop5ByOrderByNomeAsc();
    }
    
    @Deprecated
    public Restaurante atualizar(Long id, Restaurante restaurante) {
        Restaurante restauranteExistente = buscarPorId(id);
        
        restauranteExistente.setNome(restaurante.getNome());
        restauranteExistente.setCategoria(restaurante.getCategoria());
        restauranteExistente.setEndereco(restaurante.getEndereco());
        restauranteExistente.setTaxaEntrega(restaurante.getTaxaEntrega());
        restauranteExistente.setAvaliacao(restaurante.getAvaliacao());
        
        return restauranteRepository.save(restauranteExistente);
    }
    
    @Deprecated
    public void alterarStatus(Long id, boolean ativo) {
        Restaurante restaurante = buscarPorId(id);
        restaurante.setAtivo(ativo);
        restauranteRepository.save(restaurante);
    }
    
    @Deprecated
    public List<Restaurante> listarPorAvaliacao() {
        return restauranteRepository.findByAtivoTrue();
    }
}