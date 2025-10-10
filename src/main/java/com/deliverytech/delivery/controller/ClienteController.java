package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @PostMapping
    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteResponseDTO novoCliente = clienteService.cadastrarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
    
    @GetMapping
    @Operation(summary = "Listar clientes ativos", description = "Retorna apenas os clientes ativos")
    @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso")
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(clienteService.listarClientesAtivos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id, 
            @Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, clienteDTO));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar cliente", description = "Alterna o status ativo do cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.ativarDesativarCliente(id));
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Busca cliente por email para login/autenticação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarClientePorEmail(
            @Parameter(description = "Email do cliente", required = true) @PathVariable String email) {
        return ResponseEntity.ok(clienteService.buscarClientePorEmail(email));
    }
}