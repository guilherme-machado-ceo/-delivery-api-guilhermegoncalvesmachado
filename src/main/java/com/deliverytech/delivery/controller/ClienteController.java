package com.deliverytech.delivery.controller;

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
    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.cadastrar(cliente);
        return ResponseEntity.ok(novoCliente);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> buscarPorId(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Cliente> atualizar(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id, 
            @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar cliente", description = "Inativa um cliente (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> inativar(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/ativos")
    @Operation(summary = "Listar clientes ativos", description = "Retorna apenas os clientes que estão ativos no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso")
    public ResponseEntity<List<Cliente>> listarAtivos() {
        return ResponseEntity.ok(clienteService.buscarAtivos());
    }
}