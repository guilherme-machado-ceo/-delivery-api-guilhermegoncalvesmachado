package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.util.PedidoTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PedidoService
 * Foca na lógica de negócio isolada, usando mocks para dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService - Testes Unitários")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ClienteServiceInterface clienteService;

    @Mock
    private RestauranteServiceInterface restauranteService;

    @Mock
    private ProdutoServiceInterface produtoService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PedidoService pedidoService;

    private PedidoDTO validPedidoDTO;
    private Pedido validPedido;
    private PedidoResponseDTO validPedidoResponseDTO;
    private ClienteResponseDTO validClienteResponseDTO;
    private RestauranteResponseDTO validRestauranteResponseDTO;
    private ProdutoResponseDTO validProdutoResponseDTO;

    @BeforeEach
    void setUp() {
        // Preparar dados de teste para cada método
        validPedidoDTO = PedidoTestData.createValidPedidoDTO();
        validPedido = PedidoTestData.createValidPedido();
        validPedidoResponseDTO = PedidoTestData.createValidPedidoResponseDTO();
        validClienteResponseDTO = PedidoTestData.createValidRestauranteResponseDTO();
        validRestauranteResponseDTO = PedidoTestData.createValidRestauranteResponseDTO();
        validProdutoResponseDTO = PedidoTestData.createValidProdutoResponseDTO();
        
        // Reset mocks para garantir isolamento entre testes
        reset(pedidoRepository, itemPedidoRepository, clienteService, 
              restauranteService, produtoService, modelMapper);
    }

    // ========== TESTES DE CRIAÇÃO DE PEDIDO ==========

    @Test
    @DisplayName("Deve criar pedido com produtos válidos")
    void should_CreatePedido_When_ValidProductsProvided() {
        // Given
        when(clienteService.buscarClientePorId(validPedidoDTO.getClienteId()))
            .thenReturn(validClienteResponseDTO);
        when(restauranteService.buscarRestaurantePorId(validPedidoDTO.getRestauranteId()))
            .thenReturn(validRestauranteResponseDTO);
        when(produtoService.buscarProdutoPorId(any(Long.class)))
            .thenReturn(validProdutoResponseDTO);
        when(restauranteService.calcularTaxaEntrega(any(Long.class), any(String.class)))
            .thenReturn(new BigDecimal("5.00"));
        when(modelMapper.map(any(ClienteResponseDTO.class), eq(Cliente.class)))
            .thenReturn(validPedido.getCliente());
        when(modelMapper.map(any(RestauranteResponseDTO.class), eq(Restaurante.class)))
            .thenReturn(validPedido.getRestaurante());
        when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(validPedido);
        when(pedidoService.buscarPedidoPorId(any(Long.class)))
            .thenReturn(validPedidoResponseDTO);

        // When
        PedidoResponseDTO result = pedidoService.criarPedido(validPedidoDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validPedido.getId());
        
        verify(clienteService).buscarClientePorId(validPedidoDTO.getClienteId());
        verify(restauranteService).buscarRestaurantePorId(validPedidoDTO.getRestauranteId());
        verify(produtoService, atLeastOnce()).buscarProdutoPorId(any(Long.class));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente está inativo")
    void should_ThrowBusinessException_When_ClienteInactive() {
        // Given
        ClienteResponseDTO clienteInativo = PedidoTestData.createValidRestauranteResponseDTO();
        clienteInativo.setAtivo(false);
        
        when(clienteService.buscarClientePorId(validPedidoDTO.getClienteId()))
            .thenReturn(clienteInativo);

        // When & Then
        assertThatThrownBy(() -> pedidoService.criarPedido(validPedidoDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Cliente não está ativo");

        verify(clienteService).buscarClientePorId(validPedidoDTO.getClienteId());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante está inativo")
    void should_ThrowBusinessException_When_RestauranteInactive() {
        // Given
        RestauranteResponseDTO restauranteInativo = PedidoTestData.createInactiveRestauranteResponseDTO();
        
        when(clienteService.buscarClientePorId(validPedidoDTO.getClienteId()))
            .thenReturn(validClienteResponseDTO);
        when(restauranteService.buscarRestaurantePorId(validPedidoDTO.getRestauranteId()))
            .thenReturn(restauranteInativo);

        // When & Then
        assertThatThrownBy(() -> pedidoService.criarPedido(validPedidoDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Restaurante não está ativo");

        verify(clienteService).buscarClientePorId(validPedidoDTO.getClienteId());
        verify(restauranteService).buscarRestaurantePorId(validPedidoDTO.getRestauranteId());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não está disponível")
    void should_ThrowBusinessException_When_ProductUnavailable() {
        // Given
        ProdutoResponseDTO produtoIndisponivel = PedidoTestData.createUnavailableProdutoResponseDTO();
        
        when(clienteService.buscarClientePorId(validPedidoDTO.getClienteId()))
            .thenReturn(validClienteResponseDTO);
        when(restauranteService.buscarRestaurantePorId(validPedidoDTO.getRestauranteId()))
            .thenReturn(validRestauranteResponseDTO);
        when(produtoService.buscarProdutoPorId(any(Long.class)))
            .thenReturn(produtoIndisponivel);

        // When & Then
        assertThatThrownBy(() -> pedidoService.criarPedido(validPedidoDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("não está disponível");

        verify(produtoService).buscarProdutoPorId(any(Long.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não pertence ao restaurante")
    void should_ThrowBusinessException_When_ProductNotFromRestaurant() {
        // Given
        ProdutoResponseDTO produtoOutroRestaurante = PedidoTestData.createProdutoResponseDTOWithRestaurante(2L);
        
        when(clienteService.buscarClientePorId(validPedidoDTO.getClienteId()))
            .thenReturn(validClienteResponseDTO);
        when(restauranteService.buscarRestaurantePorId(validPedidoDTO.getRestauranteId()))
            .thenReturn(validRestauranteResponseDTO);
        when(produtoService.buscarProdutoPorId(any(Long.class)))
            .thenReturn(produtoOutroRestaurante);

        // When & Then
        assertThatThrownBy(() -> pedidoService.criarPedido(validPedidoDTO))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("não pertence ao restaurante");

        verify(produtoService).buscarProdutoPorId(any(Long.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ========== TESTES DE CÁLCULO DE VALOR ==========

    @Test
    @DisplayName("Deve calcular corretamente o valor total do pedido")
    void should_CalculateCorrectTotal_When_MultipleItems() {
        // Given
        List<ItemPedidoDTO> itens = PedidoTestData.createMultipleItemsPedidoDTO();
        ProdutoResponseDTO produto1 = PedidoTestData.createProdutoResponseDTOWithId(1L);
        produto1.setPreco(new BigDecimal("10.00"));
        ProdutoResponseDTO produto2 = PedidoTestData.createProdutoResponseDTOWithId(2L);
        produto2.setPreco(new BigDecimal("15.00"));
        ProdutoResponseDTO produto3 = PedidoTestData.createProdutoResponseDTOWithId(3L);
        produto3.setPreco(new BigDecimal("8.00"));

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto1);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);
        when(produtoService.buscarProdutoPorId(3L)).thenReturn(produto3);

        // When
        BigDecimal total = pedidoService.calcularTotalPedido(itens, 1L);

        // Then
        // (10.00 * 2) + (15.00 * 1) + (8.00 * 3) = 20.00 + 15.00 + 24.00 = 59.00
        assertThat(total).isEqualByComparingTo(new BigDecimal("59.00"));
        
        verify(produtoService).buscarProdutoPorId(1L);
        verify(produtoService).buscarProdutoPorId(2L);
        verify(produtoService).buscarProdutoPorId(3L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao calcular total com produto de outro restaurante")
    void should_ThrowBusinessException_When_CalculatingTotalWithWrongRestaurant() {
        // Given
        List<ItemPedidoDTO> itens = Arrays.asList(PedidoTestData.createValidItemPedidoDTO());
        ProdutoResponseDTO produtoOutroRestaurante = PedidoTestData.createProdutoResponseDTOWithRestaurante(2L);
        
        when(produtoService.buscarProdutoPorId(any(Long.class)))
            .thenReturn(produtoOutroRestaurante);

        // When & Then
        assertThatThrownBy(() -> pedidoService.calcularTotalPedido(itens, 1L))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("não pertence ao restaurante");

        verify(produtoService).buscarProdutoPorId(any(Long.class));
    }

    // ========== TESTES DE BUSCA POR ID ==========

    @Test
    @DisplayName("Deve retornar pedido quando ID existe")
    void should_ReturnPedido_When_ValidIdProvided() {
        // Given
        Long pedidoId = 1L;
        List<ItemPedido> itens = Arrays.asList(new ItemPedido());
        
        when(pedidoRepository.findById(pedidoId))
            .thenReturn(Optional.of(validPedido));
        when(modelMapper.map(validPedido, PedidoResponseDTO.class))
            .thenReturn(validPedidoResponseDTO);
        when(itemPedidoRepository.findByPedidoId(pedidoId))
            .thenReturn(itens);
        when(modelMapper.map(any(ItemPedido.class), eq(ItemPedidoResponseDTO.class)))
            .thenReturn(PedidoTestData.createValidItemPedidoResponseDTO());

        // When
        PedidoResponseDTO result = pedidoService.buscarPedidoPorId(pedidoId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(pedidoId);
        
        verify(pedidoRepository).findById(pedidoId);
        verify(itemPedidoRepository).findByPedidoId(pedidoId);
        verify(modelMapper).map(validPedido, PedidoResponseDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não encontrado por ID")
    void should_ThrowNotFoundException_When_PedidoNotFound() {
        // Given
        Long pedidoId = 999L;
        when(pedidoRepository.findById(pedidoId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pedidoService.buscarPedidoPorId(pedidoId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Pedido não encontrado");

        verify(pedidoRepository).findById(pedidoId);
        verify(itemPedidoRepository, never()).findByPedidoId(any(Long.class));
    }

    // ========== TESTES DE ATUALIZAÇÃO DE STATUS ==========

    @Test
    @DisplayName("Deve atualizar status do pedido")
    void should_UpdateStatus_When_ValidTransition() {
        // Given
        Long pedidoId = 1L;
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;
        
        when(pedidoRepository.findById(pedidoId))
            .thenReturn(Optional.of(validPedido));
        when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(validPedido);
        when(pedidoService.buscarPedidoPorId(pedidoId))
            .thenReturn(validPedidoResponseDTO);

        // When
        PedidoResponseDTO result = pedidoService.atualizarStatusPedido(pedidoId, novoStatus);

        // Then
        assertThat(result).isNotNull();
        assertThat(validPedido.getStatus()).isEqualTo(novoStatus);
        
        verify(pedidoRepository).findById(pedidoId);
        verify(pedidoRepository).save(validPedido);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar status de pedido inexistente")
    void should_ThrowNotFoundException_When_UpdatingNonExistentPedido() {
        // Given
        Long pedidoId = 999L;
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;
        
        when(pedidoRepository.findById(pedidoId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pedidoService.atualizarStatusPedido(pedidoId, novoStatus))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Pedido não encontrado");

        verify(pedidoRepository).findById(pedidoId);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ========== TESTES DE CANCELAMENTO ==========

    @Test
    @DisplayName("Deve cancelar pedido existente")
    void should_CancelPedido_When_ValidIdProvided() {
        // Given
        Long pedidoId = 1L;
        
        when(pedidoRepository.findById(pedidoId))
            .thenReturn(Optional.of(validPedido));
        when(pedidoRepository.save(any(Pedido.class)))
            .thenReturn(validPedido);
        when(pedidoService.buscarPedidoPorId(pedidoId))
            .thenReturn(validPedidoResponseDTO);

        // When
        PedidoResponseDTO result = pedidoService.cancelarPedido(pedidoId);

        // Then
        assertThat(result).isNotNull();
        assertThat(validPedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        
        verify(pedidoRepository).findById(pedidoId);
        verify(pedidoRepository).save(validPedido);
    }

    // ========== TESTES DE LISTAGEM ==========

    @Test
    @DisplayName("Deve retornar pedidos por cliente")
    void should_ReturnPedidos_When_SearchingByCliente() {
        // Given
        Long clienteId = 1L;
        List<Pedido> pedidos = Arrays.asList(
            PedidoTestData.createPedidoWithId(1L),
            PedidoTestData.createPedidoWithId(2L)
        );
        List<PedidoResumoDTO> resumos = Arrays.asList(
            PedidoTestData.createValidPedidoResumoDTO(),
            PedidoTestData.createValidPedidoResumoDTO()
        );

        when(clienteService.buscarClientePorId(clienteId))
            .thenReturn(validClienteResponseDTO);
        when(pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId))
            .thenReturn(pedidos);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResumoDTO.class)))
            .thenReturn(resumos.get(0), resumos.get(1));

        // When
        List<PedidoResumoDTO> result = pedidoService.buscarPedidosPorCliente(clienteId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        verify(clienteService).buscarClientePorId(clienteId);
        verify(pedidoRepository).findByClienteIdOrderByDataPedidoDesc(clienteId);
        verify(modelMapper, times(2)).map(any(Pedido.class), eq(PedidoResumoDTO.class));
    }

    @Test
    @DisplayName("Deve retornar pedidos por restaurante")
    void should_ReturnPedidos_When_SearchingByRestaurante() {
        // Given
        Long restauranteId = 1L;
        List<Pedido> pedidos = Arrays.asList(PedidoTestData.createPedidoWithId(1L));
        List<PedidoResumoDTO> resumos = Arrays.asList(PedidoTestData.createValidPedidoResumoDTO());

        when(restauranteService.buscarRestaurantePorId(restauranteId))
            .thenReturn(validRestauranteResponseDTO);
        when(pedidoRepository.findByRestauranteIdOrderByDataPedidoDesc(restauranteId))
            .thenReturn(pedidos);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResumoDTO.class)))
            .thenReturn(resumos.get(0));

        // When
        List<PedidoResumoDTO> result = pedidoService.buscarPedidosPorRestaurante(restauranteId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        
        verify(restauranteService).buscarRestaurantePorId(restauranteId);
        verify(pedidoRepository).findByRestauranteIdOrderByDataPedidoDesc(restauranteId);
        verify(modelMapper).map(any(Pedido.class), eq(PedidoResumoDTO.class));
    }

    @Test
    @DisplayName("Deve retornar pedidos por status")
    void should_ReturnPedidos_When_SearchingByStatus() {
        // Given
        StatusPedido status = StatusPedido.PENDENTE;
        List<Pedido> pedidos = Arrays.asList(PedidoTestData.createPedidoWithStatus(status));
        List<PedidoResumoDTO> resumos = Arrays.asList(PedidoTestData.createValidPedidoResumoDTO());

        when(pedidoRepository.findByStatusOrderByDataPedidoDesc(status))
            .thenReturn(pedidos);
        when(modelMapper.map(any(Pedido.class), eq(PedidoResumoDTO.class)))
            .thenReturn(resumos.get(0));

        // When
        List<PedidoResumoDTO> result = pedidoService.buscarPedidosPorStatus(status);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        
        verify(pedidoRepository).findByStatusOrderByDataPedidoDesc(status);
        verify(modelMapper).map(any(Pedido.class), eq(PedidoResumoDTO.class));
    }
}