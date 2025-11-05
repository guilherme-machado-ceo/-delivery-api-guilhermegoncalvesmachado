package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.config.TestConfig;
import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.dto.ErrorResponse;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.util.ClienteTestData;

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


import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de integração para ClienteController
 * Valida o comportamento completo da API incluindo serialização JSON e códigos HTTP
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestConfig.class)
@DisplayName("ClienteController - Testes de Integração")
class ClienteControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;



    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/clientes";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Limpar dados antes de cada teste
        clienteRepository.deleteAll();
    }

    // ========== TESTES DE CRIAÇÃO (POST) ==========

    @Test
    @DisplayName("Deve criar cliente com dados válidos e retornar 201")
    void should_CreateCliente_When_ValidDataProvided() {
        // Given
        ClienteDTO clienteDTO = ClienteTestData.createValidClienteDTO();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO, headers);

        // When
        ResponseEntity<ClienteResponseDTO> response = restTemplate.postForEntity(
            baseUrl, request, ClienteResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo(clienteDTO.getNome());
        assertThat(response.getBody().getEmail()).isEqualTo(clienteDTO.getEmail());
        assertThat(response.getBody().getAtivo()).isTrue();

        // Verificar persistência no banco
        List<Cliente> clientesNoBanco = clienteRepository.findAll();
        assertThat(clientesNoBanco).hasSize(1);
        assertThat(clientesNoBanco.get(0).getEmail()).isEqualTo(clienteDTO.getEmail());
    }

    @Test
    @DisplayName("Deve retornar 400 quando dados são inválidos")
    void should_Return400_When_InvalidDataProvided() {
        // Given
        ClienteDTO clienteInvalido = ClienteTestData.createInvalidClienteDTO();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteInvalido, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getType()).contains("validation");
        assertThat(response.getBody().getErrors()).isNotEmpty();

        // Verificar que não foi persistido
        List<Cliente> clientesNoBanco = clienteRepository.findAll();
        assertThat(clientesNoBanco).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar 409 quando email já existe")
    void should_Return409_When_EmailAlreadyExists() {
        // Given
        Cliente clienteExistente = ClienteTestData.createValidCliente();
        clienteRepository.save(clienteExistente);

        ClienteDTO clienteComEmailDuplicado = ClienteTestData.createClienteDTOWithEmail(
            clienteExistente.getEmail());
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteComEmailDuplicado, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetail()).contains("email");

        // Verificar que apenas o cliente original existe
        List<Cliente> clientesNoBanco = clienteRepository.findAll();
        assertThat(clientesNoBanco).hasSize(1);
    }

    // ========== TESTES DE BUSCA POR ID (GET) ==========

    @Test
    @DisplayName("Deve retornar cliente existente com status 200")
    void should_ReturnCliente_When_ValidIdProvided() {
        // Given
        Cliente cliente = ClienteTestData.createValidCliente();
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // When
        ResponseEntity<ClienteResponseDTO> response = restTemplate.getForEntity(
            baseUrl + "/" + clienteSalvo.getId(), ClienteResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(clienteSalvo.getId());
        assertThat(response.getBody().getNome()).isEqualTo(cliente.getNome());
        assertThat(response.getBody().getEmail()).isEqualTo(cliente.getEmail());
    }

    @Test
    @DisplayName("Deve retornar 404 quando cliente não existe")
    void should_Return404_When_ClienteNotFound() {
        // Given
        Long idInexistente = 999L;

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
            baseUrl + "/" + idInexistente, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetail()).contains("Cliente");
        assertThat(response.getBody().getDetail()).contains(idInexistente.toString());
    }

    // ========== TESTES DE LISTAGEM (GET) ==========

    @Test
    @DisplayName("Deve retornar lista de clientes ativos com status 200")
    void should_ReturnActiveClientes_When_ListingClientes() {
        // Given
        Cliente cliente1 = ClienteTestData.createClienteWithId(null);
        Cliente cliente2 = ClienteTestData.createClienteWithId(null);
        Cliente clienteInativo = ClienteTestData.createInactiveCliente();
        clienteInativo.setId(null);

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        clienteRepository.save(clienteInativo);

        // When
        ResponseEntity<ClienteResponseDTO[]> response = restTemplate.getForEntity(
            baseUrl, ClienteResponseDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2); // Apenas clientes ativos
        
        for (ClienteResponseDTO cliente : response.getBody()) {
            assertThat(cliente.getAtivo()).isTrue();
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há clientes ativos")
    void should_ReturnEmptyList_When_NoActiveClientes() {
        // Given
        Cliente clienteInativo = ClienteTestData.createInactiveCliente();
        clienteInativo.setId(null);
        clienteRepository.save(clienteInativo);

        // When
        ResponseEntity<ClienteResponseDTO[]> response = restTemplate.getForEntity(
            baseUrl, ClienteResponseDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    // ========== TESTES DE ATUALIZAÇÃO (PUT) ==========

    @Test
    @DisplayName("Deve atualizar cliente com dados válidos e retornar 200")
    void should_UpdateCliente_When_ValidDataProvided() {
        // Given
        Cliente cliente = ClienteTestData.createValidCliente();
        cliente.setId(null);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteDTO dadosAtualizacao = ClienteTestData.createClienteDTOForUpdate();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dadosAtualizacao, headers);

        // When
        ResponseEntity<ClienteResponseDTO> response = restTemplate.exchange(
            baseUrl + "/" + clienteSalvo.getId(), 
            HttpMethod.PUT, 
            request, 
            ClienteResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(clienteSalvo.getId());
        assertThat(response.getBody().getNome()).isEqualTo(dadosAtualizacao.getNome());
        assertThat(response.getBody().getEmail()).isEqualTo(dadosAtualizacao.getEmail());

        // Verificar persistência no banco
        Cliente clienteAtualizado = clienteRepository.findById(clienteSalvo.getId()).orElse(null);
        assertThat(clienteAtualizado).isNotNull();
        assertThat(clienteAtualizado.getNome()).isEqualTo(dadosAtualizacao.getNome());
    }

    @Test
    @DisplayName("Deve retornar 400 ao atualizar com dados inválidos")
    void should_Return400_When_UpdatingWithInvalidData() {
        // Given
        Cliente cliente = ClienteTestData.createValidCliente();
        cliente.setId(null);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteDTO dadosInvalidos = ClienteTestData.createInvalidClienteDTO();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dadosInvalidos, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            baseUrl + "/" + clienteSalvo.getId(), 
            HttpMethod.PUT, 
            request, 
            ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getCode()).contains("VALIDATION");

        // Verificar que dados originais não foram alterados
        Cliente clienteOriginal = clienteRepository.findById(clienteSalvo.getId()).orElse(null);
        assertThat(clienteOriginal).isNotNull();
        assertThat(clienteOriginal.getNome()).isEqualTo(cliente.getNome());
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar cliente inexistente")
    void should_Return404_When_UpdatingNonExistentCliente() {
        // Given
        Long idInexistente = 999L;
        ClienteDTO dadosAtualizacao = ClienteTestData.createClienteDTOForUpdate();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dadosAtualizacao, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            baseUrl + "/" + idInexistente, 
            HttpMethod.PUT, 
            request, 
            ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("Cliente");
    }

    @Test
    @DisplayName("Deve retornar 409 ao atualizar com email duplicado")
    void should_Return409_When_UpdatingWithDuplicateEmail() {
        // Given
        Cliente cliente1 = ClienteTestData.createClienteWithEmail("cliente1@email.com");
        cliente1.setId(null);
        Cliente cliente2 = ClienteTestData.createClienteWithEmail("cliente2@email.com");
        cliente2.setId(null);
        
        Cliente cliente1Salvo = clienteRepository.save(cliente1);
        Cliente cliente2Salvo = clienteRepository.save(cliente2);

        // Tentar atualizar cliente1 com email do cliente2
        ClienteDTO dadosAtualizacao = ClienteTestData.createClienteDTOWithEmail(cliente2.getEmail());
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dadosAtualizacao, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            baseUrl + "/" + cliente1Salvo.getId(), 
            HttpMethod.PUT, 
            request, 
            ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).contains("email");
    }

    // ========== TESTES DE ATIVAR/DESATIVAR (PATCH) ==========

    @Test
    @DisplayName("Deve ativar/desativar cliente e retornar 200")
    void should_ToggleClienteStatus_When_ValidIdProvided() {
        // Given
        Cliente cliente = ClienteTestData.createValidCliente();
        cliente.setId(null);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        boolean statusOriginal = clienteSalvo.isAtivo();

        // When
        ResponseEntity<ClienteResponseDTO> response = restTemplate.exchange(
            baseUrl + "/" + clienteSalvo.getId() + "/toggle-status", 
            HttpMethod.PATCH, 
            null, 
            ClienteResponseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAtivo()).isNotEqualTo(statusOriginal);

        // Verificar persistência no banco
        Cliente clienteAtualizado = clienteRepository.findById(clienteSalvo.getId()).orElse(null);
        assertThat(clienteAtualizado).isNotNull();
        assertThat(clienteAtualizado.isAtivo()).isNotEqualTo(statusOriginal);
    }

    // ========== TESTES DE VALIDAÇÃO DE HEADERS E CONTENT-TYPE ==========

    @Test
    @DisplayName("Deve retornar headers corretos nas respostas")
    void should_ReturnCorrectHeaders_When_MakingRequests() {
        // Given
        ClienteDTO clienteDTO = ClienteTestData.createValidClienteDTO();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO, headers);

        // When
        ResponseEntity<ClienteResponseDTO> response = restTemplate.postForEntity(
            baseUrl, request, ClienteResponseDTO.class);

        // Then
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).contains("application/json");
    }

    @Test
    @DisplayName("Deve retornar 415 quando Content-Type é inválido")
    void should_Return415_When_InvalidContentType() {
        // Given
        HttpHeaders invalidHeaders = new HttpHeaders();
        invalidHeaders.setContentType(MediaType.TEXT_PLAIN);
        
        ClienteDTO clienteDTO = ClienteTestData.createValidClienteDTO();
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO, invalidHeaders);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}