package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.service.CacheMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para monitoramento e gerenciamento de cache
 */
@RestController
@RequestMapping("/api/cache")
@Tag(name = "Cache Management", description = "Endpoints para monitoramento e gerenciamento de cache")
public class CacheController {

    @Autowired
    private CacheMetricsService cacheMetricsService;

    @GetMapping("/statistics")
    @Operation(summary = "Obter estatísticas de todos os caches", 
               description = "Retorna estatísticas detalhadas de hit/miss para todos os caches")
    public ResponseEntity<Map<String, Object>> getAllCacheStatistics() {
        return ResponseEntity.ok(cacheMetricsService.getAllCacheStatistics());
    }

    @GetMapping("/statistics/{cacheName}")
    @Operation(summary = "Obter estatísticas de um cache específico",
               description = "Retorna estatísticas detalhadas de hit/miss para um cache específico")
    public ResponseEntity<Map<String, Object>> getCacheStatistics(
            @Parameter(description = "Nome do cache") @PathVariable String cacheName) {
        
        if (!cacheMetricsService.cacheExists(cacheName)) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cacheMetricsService.getCacheStatistics(cacheName));
    }

    @GetMapping("/manager-info")
    @Operation(summary = "Obter informações do Cache Manager",
               description = "Retorna informações sobre o tipo e configuração do cache manager")
    public ResponseEntity<Map<String, Object>> getCacheManagerInfo() {
        return ResponseEntity.ok(cacheMetricsService.getCacheManagerInfo());
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Limpar todos os caches",
               description = "Remove todos os dados de todos os caches")
    public ResponseEntity<String> clearAllCaches() {
        cacheMetricsService.clearAllCaches();
        return ResponseEntity.ok("Todos os caches foram limpos com sucesso");
    }

    @DeleteMapping("/clear/{cacheName}")
    @Operation(summary = "Limpar um cache específico",
               description = "Remove todos os dados de um cache específico")
    public ResponseEntity<String> clearCache(
            @Parameter(description = "Nome do cache") @PathVariable String cacheName) {
        
        if (!cacheMetricsService.cacheExists(cacheName)) {
            return ResponseEntity.notFound().build();
        }
        
        cacheMetricsService.clearCache(cacheName);
        return ResponseEntity.ok("Cache '" + cacheName + "' foi limpo com sucesso");
    }

    @DeleteMapping("/statistics/clear")
    @Operation(summary = "Limpar estatísticas de cache",
               description = "Zera todos os contadores de hit/miss/eviction")
    public ResponseEntity<String> clearStatistics() {
        cacheMetricsService.clearStatistics();
        return ResponseEntity.ok("Estatísticas de cache foram zeradas com sucesso");
    }

    @GetMapping("/health")
    @Operation(summary = "Verificar saúde do cache",
               description = "Verifica se o sistema de cache está funcionando corretamente")
    public ResponseEntity<Map<String, Object>> getCacheHealth() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "cacheManager", cacheMetricsService.getCacheManagerInfo(),
            "availableCaches", cacheMetricsService.getCacheManagerInfo().get("cacheNames")
        );
        return ResponseEntity.ok(health);
    }
}