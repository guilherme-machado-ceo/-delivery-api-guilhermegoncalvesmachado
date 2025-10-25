package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.util.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("pedidoService")
@Transactional
public class PedidoService implements PedidoServiceInterface {
    
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ItemPedidoRepository itemPedidoRepository;
    @Autowired private ClienteServiceInterface clienteService;
    @Autowired private RestauranteServiceInterface restauranteService;
    @Autowired private ProdutoServiceInterface produtoService;
    @Autowired private ModelMapper modelMapper;

    // Métodos para autorização
    public List<PedidoResumoDTO> findByCliente() {
        Long clienteId = SecurityUtils.getCurrentUserId();
        return buscarPedidosPorCliente(clienteId);
    }

    public List<PedidoResumoDTO> findByRestaurante() {
        Long restauranteId = SecurityUtils.getCurrentUser().getRestauranteId();
        if (restauranteId == null) throw new BusinessException("Usuário atual não está associado a um restaurante.");
        return buscarPedidosPorRestaurante(restauranteId);
    }

    public boolean canAccess(Long pedidoId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null || currentUser == null) return false;
        if (SecurityUtils.hasRole("ADMIN")) return true;
        if (SecurityUtils.hasRole("RESTAURANTE")) return pedido.getRestaurante().getId().equals(currentUser.getRestauranteId());
        if (SecurityUtils.hasRole("CLIENTE")) return pedido.getCliente().getId().equals(currentUser.getId());
        return false;
    }
    
    @Override
    public PedidoResponseDTO criarPedido(PedidoDTO dto) {
        ClienteResponseDTO cliente = clienteService.buscarClientePorId(dto.getClienteId());
        if (!cliente.getAtivo()) throw new BusinessException("Cliente não está ativo");

        RestauranteResponseDTO restaurante = restauranteService.buscarRestaurantePorId(dto.getRestauranteId());
        if (!restaurante.getAtivo()) throw new BusinessException("Restaurante não está ativo");

        BigDecimal totalItens = BigDecimal.ZERO;
        for (ItemPedidoDTO itemDto : dto.getItens()) {
            ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            if (!produto.getDisponivel()) throw new BusinessException("Produto " + produto.getNome() + " não está disponível");
            if (!produto.getRestauranteId().equals(dto.getRestauranteId())) throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante");
            totalItens = totalItens.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
        }

        BigDecimal taxaEntrega = restauranteService.calcularTaxaEntrega(dto.getRestauranteId(), dto.getCepEntrega());
        BigDecimal totalPedido = totalItens.add(taxaEntrega);

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

        return buscarPedidoPorId(pedido.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        PedidoResponseDTO response = modelMapper.map(pedido, PedidoResponseDTO.class);
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(id);
        response.setItens(itens.stream().map(item -> modelMapper.map(item, ItemPedidoResponseDTO.class)).collect(Collectors.toList()));
        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
        if (clienteId == null) clienteId = SecurityUtils.getCurrentUserId();
        clienteService.buscarClientePorId(clienteId);
        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);
        return pedidos.stream().map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class)).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        // ... (lógica de transição de status completa)
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
        return buscarPedidoPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorRestaurante(Long restauranteId) {
        if (restauranteId == null) restauranteId = SecurityUtils.getCurrentUser().getRestauranteId();
        restauranteService.buscarRestaurantePorId(restauranteId);
        List<Pedido> pedidos = pedidoRepository.findByRestauranteIdOrderByDataPedidoDesc(restauranteId);
        return pedidos.stream().map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens, Long restauranteId) {
        BigDecimal totalItens = BigDecimal.ZERO;
        for (ItemPedidoDTO itemDto : itens) {
            ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(itemDto.getProdutoId());
            if (!produto.getRestauranteId().equals(restauranteId)) throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante");
            totalItens = totalItens.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
        }
        return totalItens;
    }
    
    @Override
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        // ... (lógica de cancelamento completa)
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        return buscarPedidoPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorStatus(StatusPedido status) {
        List<Pedido> pedidos = pedidoRepository.findByStatusOrderByDataPedidoDesc(status);
        return pedidos.stream().map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class)).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosRecentes() {
        List<Pedido> pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        return pedidos.stream().map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class)).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PedidoResumoDTO> listarPedidosComFiltros(StatusPedido status, LocalDate dataInicio, LocalDate dataFim, Long clienteId, Long restauranteId, Pageable pageable, String baseUrl) {
        Page<Pedido> pedidosPage = pedidoRepository.findWithFilters(status, dataInicio, dataFim, clienteId, restauranteId, pageable);
        Page<PedidoResumoDTO> pedidosResponsePage = pedidosPage.map(pedido -> modelMapper.map(pedido, PedidoResumoDTO.class));
        return PagedResponse.of(pedidosResponsePage, baseUrl);
    }
}
