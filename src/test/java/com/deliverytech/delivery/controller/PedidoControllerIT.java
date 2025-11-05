package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.config.TestConfig;
import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.util.PedidoTestData;
import com.deliverytech.delivery.util.ClienteTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de integração para PedidoController
 * Valida o comportamento completo da API de pedidos incluindo validações e cálculos
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestConfig.class)
@DisplayName("PedidoController - Testes de Integração")
class PedidoControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;
    private HttpHeaders headers;
    private Cliente clienteTestData;
    private Restaurante restauranteTestData;
    private Produto produtoTestData;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/pedidos";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Limpar dados antes de cada teste
        itemPedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();
        
        // Criar dados base para testes
        setupTestData();
    }

    private void setupTestData() {
        // Criar cliente de teste
        clienteTestData = ClienteTestData.createValidCliente();
        clienteTestData.setId(null);
        clienteTestData = clienteRepository.save(clienteTestData);

        // Criar restaurante de teste
        restauranteTestData = PedidoTestData.createValidRestaurante();
        restauranteTestData.setId(null);
        restauranteTestData = restauranteRepository.save(restauranteTestData);

        // Criar produto de teste
        produtoTestData = new Produto();
        produtoTestData.setNome("Pizza Margherita");
        produtoTestData.setDescricao("Deliciosa pizza com molho de tomate e mussarela");
        produtoTestData.setPreco(new BigDecimal("25.90"));
        produtoTestData.setCategoria("Pizza");
        produtoTestData.setRestaurante(restauranteTestData);
        produtoTestData.setDisponivel(true);
        produtoTestData = produtoRepository.save(produtoTestData);
    }

    // ========== TESTES DE CRIAÇÃO DE PEDIDO (POST) ==========

    @Test
    @DisplayName("Deve criar pedido completo com dados válidos e retornar 201")
    void should_CreatePedido_When_ValidDataProvided() {
        // Given
        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 2)))
            .formaPagamento("PIX")
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<PedidoResponseDTO> response = restTemplate.postForEntity(
            baseUrl, request, PedidoResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(response.getBody().getCliente().getId()).isEqualTo(clienteTestData.getId());
        assertThat(response.getBody().getRestaurante().getId()).isEqualTo(restauranteTestData.getId());
        assertThat(response.getBody().getValorTotal()).isGreaterThan(BigDecimal.ZERO);

        // Verificar persistência no banco
        List<Pedido> pedidosNoBanco = pedidoRepository.findAll();
        assertThat(pedidosNoBanco).hasSize(1);
        
        List<ItemPedido> itensNoBanco = itemPedidoRepository.findAll();
        assertThat(itensNoBanco).hasSize(1);
        assertThat(itensNoBanco.get(0).getQuantidade()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar 400 quando dados do pedido são inválidos")
    void should_Return400_When_InvalidPedidoData() {
        // Given
        PedidoDTO pedidoInvalido = PedidoTestData.createInvalidPedidoDTO();
        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoInvalido, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getCode()).contains("VALIDATION");
        assertThat(response.getBody().getError().getFields()).isNotEmpty();

        // Verificar que não foi persistido
        List<Pedido> pedidosNoBanco = pedidoRepository.findAll();
        assertThat(pedidosNoBanco).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar 400 quando produto não existe")
    void should_Return400_When_ProductNotExists() {
        // Given
        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(999L, 1))) // Produto inexistente
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Produto");
    }

    @Test
    @DisplayName("Deve retornar 400 quando produto não está disponível")
    void should_Return400_When_ProductUnavailable() {
        // Given
        // Tornar produto indisponível
        produtoTestData.setDisponivel(false);
        produtoRepository.save(produtoTestData);

        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 1)))
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("não está disponível");
    }

    @Test
    @DisplayName("Deve retornar 400 quando cliente está inativo")
    void should_Return400_When_ClienteInactive() {
        // Given
        // Tornar cliente inativo
        clienteTestData.setAtivo(false);
        clienteRepository.save(clienteTestData);

        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 1)))
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Cliente não está ativo");
    }

    @Test
    @DisplayName("Deve retornar 400 quando restaurante está inativo")
    void should_Return400_When_RestauranteInactive() {
        // Given
        // Tornar restaurante inativo
        restauranteTestData.setAtivo(false);
        restauranteRepository.save(restauranteTestData);

        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 1)))
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Restaurante não está ativo");
    }

    // ========== TESTES DE BUSCA POR ID (GET) ==========

    @Test
    @DisplayName("Deve retornar pedido existente com detalhes completos")
    void should_ReturnPedidoDetails_When_ValidIdProvided() {
        // Given
        Pedido pedido = createPedidoCompleto();

        // When
        ResponseEntity<PedidoResponseDTO> response = restTemplate.getForEntity(
            baseUrl + "/" + pedido.getId(), PedidoResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(pedido.getId());
        assertThat(response.getBody().getCliente().getId()).isEqualTo(clienteTestData.getId());
        assertThat(response.getBody().getRestaurante().getId()).isEqualTo(restauranteTestData.getId());
        assertThat(response.getBody().getItens()).isNotEmpty();
        assertThat(response.getBody().getValorTotal()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve retornar 404 quando pedido não existe")
    void should_Return404_When_PedidoNotFound() {
        // Given
        Long idInexistente = 999L;

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
            baseUrl + "/" + idInexistente, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Pedido não encontrado");
    }

    // ========== TESTES DE HISTÓRICO POR CLIENTE (GET) ==========

    @Test
    @DisplayName("Deve retornar histórico de pedidos do cliente")
    void should_ReturnClientePedidos_When_ValidClienteId() {
        // Given
        Pedido pedido1 = createPedidoCompleto();
        Pedido pedido2 = createPedidoCompleto();

        // When
        ResponseEntity<PedidoResumoDTO[]> response = restTemplate.getForEntity(
            baseUrl + "/cliente/" + clienteTestData.getId(), PedidoResumoDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        
        for (PedidoResumoDTO resumo : response.getBody()) {
            assertThat(resumo.getId()).isIn(pedido1.getId(), pedido2.getId());
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cliente não tem pedidos")
    void should_ReturnEmptyList_When_ClienteHasNoPedidos() {
        // Given
        Cliente outroCliente = ClienteTestData.createValidCliente();
        outroCliente.setId(null);
        outroCliente.setEmail("outro@email.com");
        outroCliente = clienteRepository.save(outroCliente);

        // When
        ResponseEntity<PedidoResumoDTO[]> response = restTemplate.getForEntity(
            baseUrl + "/cliente/" + outroCliente.getId(), PedidoResumoDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    // ========== TESTES DE ATUALIZAÇÃO DE STATUS (PUT) ==========

    @Test
    @DisplayName("Deve atualizar status do pedido e retornar 200")
    void should_UpdatePedidoStatus_When_ValidStatusProvided() {
        // Given
        Pedido pedido = createPedidoCompleto();
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;

        // When
        ResponseEntity<PedidoResponseDTO> response = restTemplate.exchange(
            baseUrl + "/" + pedido.getId() + "/status?status=" + novoStatus,
            HttpMethod.PUT,
            null,
            PedidoResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(novoStatus);

        // Verificar persistência no banco
        Pedido pedidoAtualizado = pedidoRepository.findById(pedido.getId()).orElse(null);
        assertThat(pedidoAtualizado).isNotNull();
        assertThat(pedidoAtualizado.getStatus()).isEqualTo(novoStatus);
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar status de pedido inexistente")
    void should_Return404_When_UpdatingNonExistentPedidoStatus() {
        // Given
        Long idInexistente = 999L;
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            baseUrl + "/" + idInexistente + "/status?status=" + novoStatus,
            HttpMethod.PUT,
            null,
            ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Pedido não encontrado");
    }

    // ========== TESTES DE CÁLCULO DE VALORES ==========

    @Test
    @DisplayName("Deve calcular corretamente o valor total com múltiplos itens")
    void should_CalculateCorrectTotal_When_MultipleItems() {
        // Given
        // Criar segundo produto
        Produto produto2 = new Produto();
        produto2.setNome("Refrigerante");
        produto2.setDescricao("Coca-Cola 350ml");
        produto2.setPreco(new BigDecimal("5.00"));
        produto2.setCategoria("Bebida");
        produto2.setRestaurante(restauranteTestData);
        produto2.setDisponivel(true);
        produto2 = produtoRepository.save(produto2);

        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(
                PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 2), // 25.90 * 2 = 51.80
                PedidoTestData.createItemPedidoDTOWithProduct(produto2.getId(), 3) // 5.00 * 3 = 15.00
            ))
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<PedidoResponseDTO> response = restTemplate.postForEntity(
            baseUrl, request, PedidoResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        
        // Total esperado: 51.80 + 15.00 + taxa de entrega
        BigDecimal totalItens = new BigDecimal("66.80");
        assertThat(response.getBody().getValorTotal()).isGreaterThan(totalItens); // Inclui taxa de entrega
        assertThat(response.getBody().getItens()).hasSize(2);
    }

    // ========== TESTES DE VALIDAÇÃO DE PAYLOADS ==========

    @Test
    @DisplayName("Deve validar estrutura JSON das respostas")
    void should_ValidateJSONStructure_When_CreatingPedido() {
        // Given
        PedidoDTO pedidoDTO = PedidoTestData.builder()
            .clienteId(clienteTestData.getId())
            .restauranteId(restauranteTestData.getId())
            .enderecoEntrega("Rua das Flores, 123 - São Paulo/SP")
            .cepEntrega("01234-567")
            .itens(Arrays.asList(PedidoTestData.createItemPedidoDTOWithProduct(produtoTestData.getId(), 1)))
            .build();

        HttpEntity<PedidoDTO> request = new HttpEntity<>(pedidoDTO, headers);

        // When
        ResponseEntity<PedidoResponseDTO> response = restTemplate.postForEntity(
            baseUrl, request, PedidoResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        PedidoResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        
        // Validar estrutura completa da resposta
        assertThat(body.getId()).isNotNull();
        assertThat(body.getCliente()).isNotNull();
        assertThat(body.getCliente().getId()).isNotNull();
        assertThat(body.getCliente().getNome()).isNotNull();
        assertThat(body.getRestaurante()).isNotNull();
        assertThat(body.getRestaurante().getId()).isNotNull();
        assertThat(body.getRestaurante().getNome()).isNotNull();
        assertThat(body.getStatus()).isNotNull();
        assertThat(body.getDataPedido()).isNotNull();
        assertThat(body.getValorTotal()).isNotNull();
        // Taxa de entrega não está disponível diretamente no PedidoResponseDTO
        assertThat(body.getItens()).isNotNull();
        assertThat(body.getItens()).isNotEmpty();
        
        // Validar estrutura dos itens
        ItemPedidoResponseDTO item = body.getItens().get(0);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getProduto()).isNotNull();
        assertThat(item.getProduto().getId()).isNotNull();
        assertThat(item.getQuantidade()).isNotNull();
        assertThat(item.getPrecoUnitario()).isNotNull();
        assertThat(item.getPrecoTotal()).isNotNull();
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Pedido createPedidoCompleto() {
        Pedido pedido = new Pedido();
        pedido.setCliente(clienteTestData);
        pedido.setRestaurante(restauranteTestData);
        pedido.setEnderecoEntrega("Rua das Flores, 123 - São Paulo/SP");
        pedido.setCepEntrega("01234567");
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTaxaEntrega(new BigDecimal("5.00"));
        pedido.setTotal(new BigDecimal("30.90"));
        pedido = pedidoRepository.save(pedido);

        // Criar item do pedido
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produtoTestData);
        item.setQuantidade(1);
        item.setPrecoUnitario(produtoTestData.getPreco());
        item.setSubtotal(produtoTestData.getPreco());
        itemPedidoRepository.save(item);

        return pedido;
    }
}