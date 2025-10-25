package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.exception.DuplicateResourceException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.util.ClienteTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ClienteService
 * Foca na lógica de negócio isolada, usando mocks para dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Testes Unitários")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDTO validClienteDTO;
    private Cliente validCliente;
    private ClienteResponseDTO validClienteResponseDTO;

    @BeforeEach
    void setUp() {
        // Preparar dados de teste para cada método
        validClienteDTO = ClienteTestData.createValidClienteDTO();
        validCliente = ClienteTestData.createValidCliente();
        validClienteResponseDTO = ClienteTestData.createValidClienteResponseDTO();
        
        // Reset mocks para garantir isolamento entre testes
        reset(clienteRepository, modelMapper);
    }

    // ========== TESTES DE CADASTRO DE CLIENTE ==========

    @Test
    @DisplayName("Deve cadastrar cliente com dados válidos")
    void should_SaveCliente_When_ValidDataProvided() {
        // Given
        when(clienteRepository.findByEmail(validClienteDTO.getEmail()))
            .thenReturn(Optional.empty());
        when(modelMapper.map(validClienteDTO, Cliente.class))
            .thenReturn(validCliente);
        when(clienteRepository.save(any(Cliente.class)))
            .thenReturn(validCliente);
        when(modelMapper.map(validCliente, ClienteResponseDTO.class))
            .thenReturn(validClienteResponseDTO);

        // When
        ClienteResponseDTO result = clienteService.cadastrarCliente(validClienteDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(validClienteDTO.getNome());
        assertThat(result.getEmail()).isEqualTo(validClienteDTO.getEmail());
        
        verify(clienteRepository).findByEmail(validClienteDTO.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
        verify(modelMapper).map(validClienteDTO, Cliente.class);
        verify(modelMapper).map(validCliente, ClienteResponseDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void should_ThrowDuplicateException_When_EmailAlreadyExists() {
        // Given
        Cliente clienteExistente = ClienteTestData.createClienteWithEmail(validClienteDTO.getEmail());
        when(clienteRepository.findByEmail(validClienteDTO.getEmail()))
            .thenReturn(Optional.of(clienteExistente));

        // When & Then
        assertThatThrownBy(() -> clienteService.cadastrarCliente(validClienteDTO))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("Cliente")
            .hasMessageContaining("email")
            .hasMessageContaining(validClienteDTO.getEmail());

        verify(clienteRepository).findByEmail(validClienteDTO.getEmail());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // ========== TESTES DE BUSCA POR ID ==========

    @Test
    @DisplayName("Deve retornar cliente quando ID existe")
    void should_ReturnCliente_When_ValidIdProvided() {
        // Given
        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.of(validCliente));
        when(modelMapper.map(validCliente, ClienteResponseDTO.class))
            .thenReturn(validClienteResponseDTO);

        // When
        ClienteResponseDTO result = clienteService.buscarClientePorId(clienteId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(clienteId);
        
        verify(clienteRepository).findById(clienteId);
        verify(modelMapper).map(validCliente, ClienteResponseDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado por ID")
    void should_ThrowNotFoundException_When_ClienteNotFoundById() {
        // Given
        Long clienteId = 999L;
        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.buscarClientePorId(clienteId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Cliente")
            .hasMessageContaining(clienteId.toString());

        verify(clienteRepository).findById(clienteId);
        verify(modelMapper, never()).map(any(), eq(ClienteResponseDTO.class));
    }

    // ========== TESTES DE BUSCA POR EMAIL ==========

    @Test
    @DisplayName("Deve retornar cliente quando email existe")
    void should_ReturnCliente_When_ValidEmailProvided() {
        // Given
        String email = "joao@email.com";
        when(clienteRepository.findByEmail(email))
            .thenReturn(Optional.of(validCliente));
        when(modelMapper.map(validCliente, ClienteResponseDTO.class))
            .thenReturn(validClienteResponseDTO);

        // When
        ClienteResponseDTO result = clienteService.buscarClientePorEmail(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        
        verify(clienteRepository).findByEmail(email);
        verify(modelMapper).map(validCliente, ClienteResponseDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado por email")
    void should_ThrowNotFoundException_When_ClienteNotFoundByEmail() {
        // Given
        String email = "inexistente@email.com";
        when(clienteRepository.findByEmail(email))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.buscarClientePorEmail(email))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Cliente não encontrado com email: " + email);

        verify(clienteRepository).findByEmail(email);
        verify(modelMapper, never()).map(any(), eq(ClienteResponseDTO.class));
    }

    // ========== TESTES DE ATUALIZAÇÃO ==========

    @Test
    @DisplayName("Deve atualizar cliente com dados válidos")
    void should_UpdateCliente_When_ValidDataProvided() {
        // Given
        Long clienteId = 1L;
        ClienteDTO updateDTO = ClienteTestData.createClienteDTOForUpdate();
        Cliente clienteExistente = ClienteTestData.createClienteWithId(clienteId);
        Cliente clienteAtualizado = ClienteTestData.createClienteWithId(clienteId);
        ClienteResponseDTO responseDTO = ClienteTestData.createClienteResponseDTOWithId(clienteId);

        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class)))
            .thenReturn(clienteAtualizado);
        when(modelMapper.map(clienteAtualizado, ClienteResponseDTO.class))
            .thenReturn(responseDTO);

        // When
        ClienteResponseDTO result = clienteService.atualizarCliente(clienteId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(clienteId);
        
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(any(Cliente.class));
        verify(modelMapper).map(clienteAtualizado, ClienteResponseDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar cliente inexistente")
    void should_ThrowNotFoundException_When_UpdatingNonExistentCliente() {
        // Given
        Long clienteId = 999L;
        ClienteDTO updateDTO = ClienteTestData.createClienteDTOForUpdate();
        
        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.atualizarCliente(clienteId, updateDTO))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Cliente")
            .hasMessageContaining(clienteId.toString());

        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com email duplicado")
    void should_ThrowDuplicateException_When_UpdatingWithDuplicateEmail() {
        // Given
        Long clienteId = 1L;
        String novoEmail = "novo@email.com";
        ClienteDTO updateDTO = ClienteTestData.createClienteDTOWithEmail(novoEmail);
        Cliente clienteExistente = ClienteTestData.createClienteWithId(clienteId);
        Cliente outroCliente = ClienteTestData.createClienteWithId(2L);
        
        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.findByEmail(novoEmail))
            .thenReturn(Optional.of(outroCliente));

        // When & Then
        assertThatThrownBy(() -> clienteService.atualizarCliente(clienteId, updateDTO))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("Cliente")
            .hasMessageContaining("email")
            .hasMessageContaining(novoEmail);

        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).findByEmail(novoEmail);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // ========== TESTES DE ATIVAR/DESATIVAR ==========

    @Test
    @DisplayName("Deve ativar/desativar cliente existente")
    void should_ToggleClienteStatus_When_ValidIdProvided() {
        // Given
        Long clienteId = 1L;
        Cliente cliente = ClienteTestData.createClienteWithId(clienteId);
        boolean statusOriginal = cliente.isAtivo();
        
        when(clienteRepository.findById(clienteId))
            .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class)))
            .thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteResponseDTO.class))
            .thenReturn(validClienteResponseDTO);

        // When
        ClienteResponseDTO result = clienteService.ativarDesativarCliente(clienteId);

        // Then
        assertThat(result).isNotNull();
        assertThat(cliente.isAtivo()).isNotEqualTo(statusOriginal);
        
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(cliente);
        verify(modelMapper).map(cliente, ClienteResponseDTO.class);
    }

    // ========== TESTES DE LISTAGEM ==========

    @Test
    @DisplayName("Deve retornar lista de clientes ativos")
    void should_ReturnActiveClientes_When_ListingActiveClientes() {
        // Given
        List<Cliente> clientesAtivos = Arrays.asList(
            ClienteTestData.createClienteWithId(1L),
            ClienteTestData.createClienteWithId(2L)
        );
        List<ClienteResponseDTO> responseDTOs = Arrays.asList(
            ClienteTestData.createClienteResponseDTOWithId(1L),
            ClienteTestData.createClienteResponseDTOWithId(2L)
        );

        when(clienteRepository.findByAtivoTrue())
            .thenReturn(clientesAtivos);
        when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class)))
            .thenReturn(responseDTOs.get(0), responseDTOs.get(1));

        // When
        List<ClienteResponseDTO> result = clienteService.listarClientesAtivos();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        verify(clienteRepository).findByAtivoTrue();
        verify(modelMapper, times(2)).map(any(Cliente.class), eq(ClienteResponseDTO.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há clientes ativos")
    void should_ReturnEmptyList_When_NoActiveClientes() {
        // Given
        when(clienteRepository.findByAtivoTrue())
            .thenReturn(Arrays.asList());

        // When
        List<ClienteResponseDTO> result = clienteService.listarClientesAtivos();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(clienteRepository).findByAtivoTrue();
        verify(modelMapper, never()).map(any(Cliente.class), eq(ClienteResponseDTO.class));
    }
}