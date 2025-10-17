package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ApiResponse;
import com.deliverytech.delivery.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Endpoints de relatórios e métricas de negócio")
public class RelatorioController {
    
    @Autowired
    private RelatorioService relatorioService;
    
    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Vendas por restaurante", description = "Relatório de vendas agrupadas por restaurante")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> vendasPorRestaurante(
            @Parameter(description = "Data inicial (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioVendasPorRestaurante(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(relatorio, "Relatório de vendas por restaurante gerado com sucesso"));
    }
    
    @GetMapping("/produtos-mais-vendidos")
    @Operation(summary = "Produtos mais vendidos", description = "Ranking dos produtos mais vendidos")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ranking gerado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> produtosMaisVendidos(
            @Parameter(description = "Número de produtos no ranking (padrão: 10)")
            @RequestParam(defaultValue = "10") int limite,
            @Parameter(description = "Data inicial (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Map<String, Object>> ranking = relatorioService.gerarRankingProdutosMaisVendidos(limite, dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(ranking, "Ranking de produtos mais vendidos gerado com sucesso"));
    }
    
    @GetMapping("/clientes-ativos")
    @Operation(summary = "Clientes mais ativos", description = "Ranking dos clientes mais ativos por número de pedidos")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ranking gerado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> clientesAtivos(
            @Parameter(description = "Número de clientes no ranking (padrão: 10)")
            @RequestParam(defaultValue = "10") int limite,
            @Parameter(description = "Data inicial (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Map<String, Object>> ranking = relatorioService.gerarRankingClientesAtivos(limite, dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(ranking, "Ranking de clientes mais ativos gerado com sucesso"));
    }
    
    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Pedidos por período", description = "Relatório de pedidos filtrados por período")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> pedidosPorPeriodo(
            @Parameter(description = "Data inicial (obrigatória)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (obrigatória)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        Map<String, Object> relatorio = relatorioService.gerarRelatorioPedidosPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(relatorio, "Relatório de pedidos por período gerado com sucesso"));
    }
    
    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard geral", description = "Métricas gerais para dashboard administrativo")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard gerado com sucesso")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard() {
        Map<String, Object> dashboard = relatorioService.gerarDashboardGeral();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard gerado com sucesso"));
    }
}