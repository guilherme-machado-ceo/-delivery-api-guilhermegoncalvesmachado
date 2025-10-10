package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService implements PedidoServiceInterface {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    @Autowired
    private ClienteServiceInterface clienteService;
    
    @Autowired
    private RestauranteServiceInterface restauranteService;
    
    @Autowired
    private ProdutoServiceInterface produtoService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO dto) {
        // 1. Validar cliente existe e está ativo
        ClienteResponseDTO cliente = clienteService.buscarClientePorId(dto.getClienteId());
        if (!cliente.getAtivo()) {
            throw new BusinessException("Cliente não está ativo");
        }
        
        // 2. Validar restaurante existe e está ativo
        RestauranteResponseDTO restaurante = restauranteService.buscarRestaurantePorId(dto.getRestauranteId());
        if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante não está ativo");
        }
        
        // 3. Validar todos os produtos existem e estão disponíveis
        BigDecimal totalItens = BigDecimal.ZERO;
        for (ItemPedidoDTO itemDto : dto.getItens()) {
            ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            
            if (!produto.getDisponivel()) {
                throw new BusinessException("Produto " + produto.getNome() + " não está disponível");
            }
            
            if (!produto.getRestauranteId().equals(dto.getRestauranteId())) {
                throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante selecionado");
            }
            
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            totalItens = totalItens.add(subtotal);
        }
        
        // 4. Calcular total do pedido (itens + taxa de entrega)
        BigDecimal taxaEntrega = restauranteService.calcularTaxaEntrega(dto.getRestauranteId(), dto.getCepEntrega());
        BigDecimal totalPedido = totalItens.add(taxaEntrega);
        
        // 5. Salvar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(modelMapper.map(cliente, Cliente.class));
        pedido.setRestaurante(modelMapper.map(restaurante, Restaurante.class));
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setCepEntrega(dto.getCepEntrega());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setTotal(totalPedido);
        
        pedido = pedidoRepository.save(pedido);
        
        // 6. Salvar itens do pedido
        for (ItemPedidoDTO itemDto : dto.getItens()) {
            ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(modelMapper.map(produto, Produto.class));
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
            
            itemPedidoRepository.save(item);
        }
        
        // 7. Retornar pedido criado
        return buscarPedidoPorId(pedido.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        
        PedidoResponseDTO response = modelMapper.map(pedido, PedidoResponseDTO.class);
        
        // Buscar itens do pedido
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(id);
        List<ItemPedidoResponseDTO> itensResponse = itens.stream()
                .map(item -> modelMapper.map(item, ItemPedidoResponseDTO.class))
                .collect(Collectors.toList());
        
        response.setItens(itensResponse);
        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
        // Validar se cliente existe
        clienteService.buscarClientePorId(clienteId);
        
        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);
        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        
        // Validar transições de status permitidas
        if (!isTransicaoStatusValida(pedido.getStatus(), status)) {
            throw new BusinessException("Transição de status não permitida: " + 
                    pedido.getStatus() + " -> " + status);
        }
        
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
        
        return buscarPedidoPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens, Long restauranteId) {
        BigDecimal totalItens = BigDecimal.ZERO;
        
        for (ItemPedidoDTO itemDto : itens) {
            ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            
            if (!produto.getRestauranteId().equals(restauranteId)) {
                throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante");
            }
            
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            totalItens = totalItens.add(subtotal);
        }
        
        return totalItens;
    }
    
    @Override
    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        
        // Verificar se o pedido pode ser cancelado
        if (!podeSerCancelado(pedido.getStatus())) {
            throw new BusinessException("Pedido não pode ser cancelado no status atual: " + pedido.getStatus());
        }
        
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        
        return buscarPedidoPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorStatus(StatusPedido status) {
        List<Pedido> pedidos = pedidoRepository.findByStatusOrderByDataPedidoDesc(status);
        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosRecentes() {
        List<Pedido> pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class))
                .collect(Collectors.toList());
    }
    
    // Métodos auxiliares privados
    private boolean isTransicaoStatusValida(StatusPedido statusAtual, StatusPedido novoStatus) {
        switch (statusAtual) {
            case PENDENTE:
                return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;
            case CONFIRMADO:
                return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;
            case PREPARANDO:
                return novoStatus == StatusPedido.PRONTO || novoStatus == StatusPedido.CANCELADO;
            case PRONTO:
                return novoStatus == StatusPedido.SAIU_ENTREGA;
            case SAIU_ENTREGA:
                return novoStatus == StatusPedido.ENTREGUE;
            case ENTREGUE:
            case CANCELADO:
                return false; // Status finais não podem ser alterados
            default:
                return false;
        }
    }
    
    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || 
               status == StatusPedido.CONFIRMADO || 
               status == StatusPedido.PREPARANDO;
    }
}