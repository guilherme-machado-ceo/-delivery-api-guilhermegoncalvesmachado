package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.StatusPedido;
import com.deliverytech.delivery.service.PedidoServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoServiceInterface pedidoService;
    
    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com transação complexa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente, restaurante ou produto não encontrado"),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<PedidoResponseDTO> criarPedido(
            @Valid @RequestBody @Parameter(description = "Dados do pedido") PedidoDTO pedidoDTO) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Busca pedido completo com todos os itens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(
            @PathVariable @Parameter(description = "ID do pedido") Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    @GetMapping("/clientes/{clienteId}")
    @Operation(summary = "Buscar pedidos por cliente", description = "Retorna histórico de pedidos do cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResumoDTO>> buscarPedidosPorCliente(
            @PathVariable @Parameter(description = "ID do cliente") Long clienteId) {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza status validando transições permitidas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "422", description = "Transição de status não permitida")
    })
    public ResponseEntity<PedidoResponseDTO> atualizarStatusPedido(
            @PathVariable @Parameter(description = "ID do pedido") Long id,
            @RequestBody @Parameter(description = "Novo status") StatusUpdateDTO statusUpdate) {
        PedidoResponseDTO pedido = pedidoService.atualizarStatusPedido(id, statusUpdate.getStatus());
        return ResponseEntity.ok(pedido);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela pedido se permitido pelo status atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado no status atual")
    })
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(
            @PathVariable @Parameter(description = "ID do pedido") Long id) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }
    
    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido", description = "Calcula total sem salvar o pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total calculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto ou restaurante não encontrado")
    })
    public ResponseEntity<CalculoResultadoDTO> calcularTotalPedido(
            @Valid @RequestBody @Parameter(description = "Dados para cálculo") CalculoPedidoDTO calculoDTO) {
        BigDecimal total = pedidoService.calcularTotalPedido(calculoDTO.getItens(), calculoDTO.getRestauranteId());
        
        CalculoResultadoDTO resultado = new CalculoResultadoDTO();
        resultado.setTotalItens(total);
        resultado.setTaxaEntrega(BigDecimal.ZERO); // Seria calculada baseada no CEP
        resultado.setTotalPedido(total);
        
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar pedidos por status", description = "Lista pedidos filtrados por status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada")
    })
    public ResponseEntity<List<PedidoResumoDTO>> buscarPedidosPorStatus(
            @PathVariable @Parameter(description = "Status do pedido") StatusPedido status) {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/recentes")
    @Operation(summary = "Buscar pedidos recentes", description = "Retorna os 10 pedidos mais recentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos recentes retornada")
    })
    public ResponseEntity<List<PedidoResumoDTO>> buscarPedidosRecentes() {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosRecentes();
        return ResponseEntity.ok(pedidos);
    }
}