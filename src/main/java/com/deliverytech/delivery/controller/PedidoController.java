package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.StatusPedido;
import com.deliverytech.delivery.service.PedidoServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {
    
    @Autowired
    private PedidoServiceInterface pedidoService;
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido. Requer perfil de CLIENTE.")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(pedido, "Pedido criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#id)")
    @Operation(summary = "Buscar pedido por ID", description = "Busca um pedido. ADMIN pode ver qualquer um, outros usuários apenas os seus.")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> buscarPedidoPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os pedidos com filtros", description = "Lista todos os pedidos. Requer perfil de ADMIN.")
    public ResponseEntity<PagedResponse<PedidoResumoDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long restauranteId,
            @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable,
            HttpServletRequest request) {
        PagedResponse<PedidoResumoDTO> pedidos = pedidoService.listarPedidosComFiltros(status, dataInicio, dataFim, clienteId, restauranteId, pageable, request.getRequestURL().toString());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Meus Pedidos", description = "Retorna o histórico de pedidos do cliente logado.")
    public ResponseEntity<ApiResponse<List<PedidoResumoDTO>>> buscarMeusPedidos() {
        // O service usará o SecurityUtils para pegar o ID do cliente logado
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(null); // O ID será pego do contexto de segurança
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/restaurante")
    @PreAuthorize("hasRole('RESTAURANTE')")
    @Operation(summary = "Pedidos do Restaurante", description = "Lista pedidos do restaurante do usuário logado.")
    public ResponseEntity<ApiResponse<List<PedidoResumoDTO>>> buscarPedidosDoRestaurante() {
        // O service usará o SecurityUtils para pegar o ID do restaurante logado
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorRestaurante(null); // O ID será pego do contexto de segurança
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @pedidoService.canAccess(#id))")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido. ADMIN ou dono do restaurante.")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> atualizarStatusPedido(@PathVariable Long id, @Valid @RequestBody StatusUpdateDTO statusUpdate) {
        PedidoResponseDTO pedido = pedidoService.atualizarStatusPedido(id, statusUpdate.getStatus());
        return ResponseEntity.ok(ApiResponse.success(pedido, "Status do pedido atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pedidoService.canAccess(#id)")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido. ADMIN ou o cliente dono do pedido (se o status permitir).")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> cancelarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(ApiResponse.success(pedido, "Pedido cancelado com sucesso"));
    }
}
