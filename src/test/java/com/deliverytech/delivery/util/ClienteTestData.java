package com.deliverytech.delivery.util;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.model.Cliente;
import java.time.LocalDateTime;

/**
 * Classe utilitária para criação de dados de teste relacionados a Cliente
 */
public class ClienteTestData {

    public static ClienteDTO createValidClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@email.com");
        dto.setTelefone("11999999999");
        dto.setEndereco("Rua das Flores, 123 - São Paulo/SP");
        return dto;
    }

    public static ClienteDTO createClienteDTOWithEmail(String email) {
        ClienteDTO dto = createValidClienteDTO();
        dto.setEmail(email);
        return dto;
    }

    public static ClienteDTO createInvalidClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome(""); // Nome inválido
        dto.setEmail("email-invalido"); // Email inválido
        dto.setTelefone("123"); // Telefone inválido
        dto.setEndereco(""); // Endereço inválido
        return dto;
    }

    public static Cliente createValidCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua das Flores, 123 - São Paulo/SP");
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        return cliente;
    }

    public static Cliente createClienteWithId(Long id) {
        Cliente cliente = createValidCliente();
        cliente.setId(id);
        return cliente;
    }

    public static Cliente createClienteWithEmail(String email) {
        Cliente cliente = createValidCliente();
        cliente.setEmail(email);
        return cliente;
    }

    public static ClienteResponseDTO createValidClienteResponseDTO() {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(1L);
        dto.setNome("João Silva");
        dto.setEmail("joao@email.com");
        dto.setTelefone("11999999999");
        dto.setEndereco("Rua das Flores, 123 - São Paulo/SP");
        dto.setAtivo(true);
        dto.setDataCadastro(LocalDateTime.now());
        return dto;
    }

    public static ClienteResponseDTO createClienteResponseDTOWithId(Long id) {
        ClienteResponseDTO dto = createValidClienteResponseDTO();
        dto.setId(id);
        return dto;
    }

    // Dados para cenários específicos de teste
    public static ClienteDTO createClienteDTOForUpdate() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");
        dto.setTelefone("11888888888");
        dto.setEndereco("Rua Nova, 456 - São Paulo/SP");
        return dto;
    }

    public static Cliente createInactiveCliente() {
        Cliente cliente = createValidCliente();
        cliente.setAtivo(false);
        return cliente;
    }

    // Builder pattern para maior flexibilidade
    public static class ClienteDTOBuilder {
        private ClienteDTO dto = new ClienteDTO();

        public ClienteDTOBuilder nome(String nome) {
            dto.setNome(nome);
            return this;
        }

        public ClienteDTOBuilder email(String email) {
            dto.setEmail(email);
            return this;
        }

        public ClienteDTOBuilder telefone(String telefone) {
            dto.setTelefone(telefone);
            return this;
        }

        public ClienteDTOBuilder endereco(String endereco) {
            dto.setEndereco(endereco);
            return this;
        }

        public ClienteDTO build() {
            return dto;
        }
    }

    public static ClienteDTOBuilder builder() {
        return new ClienteDTOBuilder();
    }
}