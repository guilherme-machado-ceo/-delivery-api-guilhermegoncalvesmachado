package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.StatusPedido;
import com.deliverytech.delivery.service.PedidoServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente, restaurante ou produto não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> criarPedido(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do pedido a ser criado", required = true
            ) PedidoDTO pedidoDTO) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(pedido, "Pedido criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Busca pedido completo com todos os itens")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> buscarPedidoPorId(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }
    
    @GetMapping
    @Operation(summary = "Listar pedidos com filtros", description = "Lista pedidos com filtros opcionais e paginação")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<PagedResponse<PedidoResumoDTO>> listarPedidos(
            @Parameter(description = "Filtrar por status do pedido")
            @RequestParam(required = false) StatusPedido status,
            @Parameter(description = "Data inicial (formato: yyyy-MM-dd)")
            @RequestParam(required = false) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: yyyy-MM-dd)")
            @RequestParam(required = false) LocalDate dataFim,
            @Parameter(description = "ID do cliente")
            @RequestParam(required = false) Long clienteId,
            @Parameter(description = "ID do restaurante")
            @RequestParam(required = false) Long restauranteId,
            @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable,
            HttpServletRequest request) {
        
        PagedResponse<PedidoResumoDTO> pedidos = pedidoService.listarPedidosComFiltros(
            status, dataInicio, dataFim, clienteId, restauranteId, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(pedidos);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza status validando transições permitidas")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Transição de status não permitida")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> atualizarStatusPedido(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Novo status do pedido", required = true
            ) StatusUpdateDTO statusUpdate) {
        PedidoResponseDTO pedido = pedidoService.atualizarStatusPedido(id, statusUpdate.getStatus());
        return ResponseEntity.ok(ApiResponse.success(pedido, "Status do pedido atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela pedido se permitido pelo status atual")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado no status atual")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> cancelarPedido(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(ApiResponse.success(pedido, "Pedido cancelado com sucesso"));
    }
    
    @GetMapping("/clientes/{clienteId}/pedidos")
    @Operation(summary = "Histórico do cliente", description = "Retorna histórico de pedidos do cliente")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Histórico de pedidos retornado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ApiResponse<List<PedidoResumoDTO>>> buscarPedidosPorCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }
    
    @GetMapping("/restaurantes/{restauranteId}/pedidos")
    @Operation(summary = "Pedidos do restaurante", description = "Lista pedidos de um restaurante específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos do restaurante"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<List<PedidoResumoDTO>>> buscarPedidosPorRestaurante(
            @Parameter(description = "ID do restaurante", required = true)
            @PathVariable Long restauranteId) {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorRestaurante(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }
    
    @PostMapping("/calcular")
    @Operation(summary = "Calcular total sem salvar", description = "Calcula total do pedido sem salvar no banco")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Total calculado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto ou restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<CalculoResultadoDTO>> calcularTotalPedido(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados para cálculo do pedido", required = true
            ) CalculoPedidoDTO calculoDTO) {
        BigDecimal total = pedidoService.calcularTotalPedido(calculoDTO.getItens(), calculoDTO.getRestauranteId());
        
        CalculoResultadoDTO resultado = new CalculoResultadoDTO();
        resultado.setTotalItens(total);
        resultado.setTaxaEntrega(BigDecimal.ZERO); // Seria calculada baseada no CEP
        resultado.setTotalPedido(total);
        
        return ResponseEntity.ok(ApiResponse.success(resultado, "Total calculado com sucesso"));
    }
}