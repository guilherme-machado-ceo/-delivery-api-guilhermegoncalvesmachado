package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
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
public class ProdutoService implements ProdutoServiceInterface {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto) {
        // Validar se restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", dto.getRestauranteId()));
        
        if (!restaurante.isAtivo()) {
            throw new BusinessException("Não é possível cadastrar produtos para restaurante inativo");
        }
        
        // Validações de negócio
        if (dto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }
        
        Produto produto = modelMapper.map(dto, Produto.class);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true);
        
        Produto produtoSalvo = produtoRepository.save(produto);
        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", restauranteId));
        
        List<Produto> produtos = produtoRepository.findByRestauranteAndDisponivelTrue(restaurante);
        
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));
        
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }
    
    @Override
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));
        
        // Validar se o novo restaurante existe (se mudou)
        if (!produtoExistente.getRestaurante().getId().equals(dto.getRestauranteId())) {
            Restaurante novoRestaurante = restauranteRepository.findById(dto.getRestauranteId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante", dto.getRestauranteId()));
            
            if (!novoRestaurante.isAtivo()) {
                throw new BusinessException("Não é possível vincular produto a restaurante inativo");
            }
            
            produtoExistente.setRestaurante(novoRestaurante);
        }
        
        // Validações de negócio
        if (dto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }
        
        // Atualizar campos
        produtoExistente.setNome(dto.getNome());
        produtoExistente.setDescricao(dto.getDescricao());
        produtoExistente.setPreco(dto.getPreco());
        produtoExistente.setCategoria(dto.getCategoria());
        
        if (dto.getDisponivel() != null) {
            produtoExistente.setDisponivel(dto.getDisponivel());
        }
        
        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }
    
    @Override
    public ProdutoResponseDTO alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));
        
        produto.setDisponivel(disponivel);
        
        Produto produtoAtualizado = produtoRepository.save(produto);
        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria) {
        List<Produto> produtos = produtoRepository.findByCategoriaAndDisponivelTrue(categoria);
        
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarProdutosDisponiveis() {
        List<Produto> produtos = produtoRepository.findByDisponivelTrue();
        
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    // Métodos legados para compatibilidade (deprecated)
    @Deprecated
    public Produto cadastrar(Produto produto, Long restauranteId) {
        if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Restaurante não encontrado"));
        produto.setRestaurante(restaurante);
        
        return produtoRepository.save(produto);
    }
    
    @Deprecated
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Produto não encontrado"));
    }
    
    @Deprecated
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Restaurante não encontrado"));
        return produtoRepository.findByRestaurante(restaurante);
    }
    
    @Deprecated
    public List<Produto> listarPorRestauranteECategoria(Long restauranteId, String categoria) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Restaurante não encontrado"));
        return produtoRepository.findByRestauranteAndCategoria(restaurante, categoria);
    }
    
    @Deprecated
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
    
    @Deprecated
    public void alterarDisponibilidadeLegado(Long id, boolean disponivel) {
        Produto produto = buscarPorId(id);
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }
}