package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.service.CachePerformanceTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para testes de performance do cache
 */
@RestController
@RequestMapping("/api/cache/test")
@Tag(name = "Cache Performance Tests", description = "Endpoints para testes de performance do cache")
public class CacheTestController {

    @Autowired
    private CachePerformanceTestService cachePerformanceTestService;

    @GetMapping("/performance/{clienteId}")
    @Operation(summary = "Executar teste de performance básico",
               description = "Compara o tempo de acesso com e sem cache para um cliente específico")
    public ResponseEntity<Map<String, Object>> testarPerformance(
            @Parameter(description = "ID do cliente para teste") @PathVariable Long clienteId,
            @Parameter(description = "Número de tentativas") @RequestParam(defaultValue = "100") int tentativas) {
        
        Map<String, Object> resultado = cachePerformanceTestService.executarTestePerformance(clienteId, tentativas);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/load")
    @Operation(summary = "Executar teste de carga concorrente",
               description = "Testa a performance do cache sob carga concorrente")
    public ResponseEntity<Map<String, Object>> testarCarga(
            @Parameter(description = "Lista de IDs de clientes") @RequestBody List<Long> clienteIds,
            @Parameter(description = "Número de threads") @RequestParam(defaultValue = "5") int threads,
            @Parameter(description = "Tentativas por thread") @RequestParam(defaultValue = "20") int tentativasPorThread) {
        
        if (clienteIds == null || clienteIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> resultado = cachePerformanceTestService.executarTesteCarga(clienteIds, threads, tentativasPorThread);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/invalidation/{clienteId}")
    @Operation(summary = "Testar invalidação de cache",
               description = "Testa o comportamento do cache durante invalidação")
    public ResponseEntity<Map<String, Object>> testarInvalidacao(
            @Parameter(description = "ID do cliente para teste") @PathVariable Long clienteId) {
        
        Map<String, Object> resultado = cachePerformanceTestService.executarTesteInvalidacao(clienteId);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/report/{clienteId}")
    @Operation(summary = "Gerar relatório completo de performance",
               description = "Executa todos os testes e gera um relatório completo")
    public ResponseEntity<Map<String, Object>> gerarRelatorioCompleto(
            @Parameter(description = "ID do cliente para teste") @PathVariable Long clienteId) {
        
        Map<String, Object> relatorio = cachePerformanceTestService.gerarRelatorioCompleto(clienteId);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/simple/{clienteId}")
    @Operation(summary = "Teste simples de cache",
               description = "Executa um teste básico para verificar se o cache está funcionando")
    public ResponseEntity<Map<String, Object>> testeSimples(
            @Parameter(description = "ID do cliente para teste") @PathVariable Long clienteId) {
        
        Map<String, Object> resultado = cachePerformanceTestService.executarTestePerformance(clienteId, 10);
        return ResponseEntity.ok(resultado);
    }
}