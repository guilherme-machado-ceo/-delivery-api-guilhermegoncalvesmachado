package com.deliverytech.delivery.service;

import com.deliverytech.delivery.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Serviço para coleta e análise de métricas de cache
 */
@Service
public class CacheMetricsService {

    @Autowired
    private CacheManager cacheManager;

    // Contadores de hit/miss por cache
    private final Map<String, AtomicLong> hitCounters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> missCounters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> evictionCounters = new ConcurrentHashMap<>();

    /**
     * Registra um hit no cache
     */
    public void recordCacheHit(String cacheName) {
        hitCounters.computeIfAbsent(cacheName, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * Registra um miss no cache
     */
    public void recordCacheMiss(String cacheName) {
        missCounters.computeIfAbsent(cacheName, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * Registra uma eviction no cache
     */
    public void recordCacheEviction(String cacheName) {
        evictionCounters.computeIfAbsent(cacheName, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * Obtém estatísticas de um cache específico
     */
    public Map<String, Object> getCacheStatistics(String cacheName) {
        Map<String, Object> stats = new HashMap<>();
        
        long hits = hitCounters.getOrDefault(cacheName, new AtomicLong(0)).get();
        long misses = missCounters.getOrDefault(cacheName, new AtomicLong(0)).get();
        long evictions = evictionCounters.getOrDefault(cacheName, new AtomicLong(0)).get();
        long total = hits + misses;
        
        stats.put("cacheName", cacheName);
        stats.put("hits", hits);
        stats.put("misses", misses);
        stats.put("evictions", evictions);
        stats.put("totalRequests", total);
        stats.put("hitRate", total > 0 ? (double) hits / total : 0.0);
        stats.put("missRate", total > 0 ? (double) misses / total : 0.0);
        
        // Informações do cache atual
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            stats.put("cacheType", cache.getClass().getSimpleName());
            stats.put("nativeCache", cache.getNativeCache().getClass().getSimpleName());
        }
        
        return stats;
    }

    /**
     * Obtém estatísticas de todos os caches
     */
    public Map<String, Object> getAllCacheStatistics() {
        Map<String, Object> allStats = new HashMap<>();
        
        // Estatísticas por cache
        Map<String, Object> cacheStats = new HashMap<>();
        cacheStats.put(CacheConfig.CLIENTES_CACHE, getCacheStatistics(CacheConfig.CLIENTES_CACHE));
        cacheStats.put(CacheConfig.RESTAURANTES_CACHE, getCacheStatistics(CacheConfig.RESTAURANTES_CACHE));
        cacheStats.put(CacheConfig.PRODUTOS_CACHE, getCacheStatistics(CacheConfig.PRODUTOS_CACHE));
        cacheStats.put(CacheConfig.PEDIDOS_CACHE, getCacheStatistics(CacheConfig.PEDIDOS_CACHE));
        cacheStats.put(CacheConfig.ESTATISTICAS_CACHE, getCacheStatistics(CacheConfig.ESTATISTICAS_CACHE));
        
        allStats.put("caches", cacheStats);
        
        // Estatísticas globais
        long totalHits = hitCounters.values().stream().mapToLong(AtomicLong::get).sum();
        long totalMisses = missCounters.values().stream().mapToLong(AtomicLong::get).sum();
        long totalEvictions = evictionCounters.values().stream().mapToLong(AtomicLong::get).sum();
        long totalRequests = totalHits + totalMisses;
        
        Map<String, Object> globalStats = new HashMap<>();
        globalStats.put("totalHits", totalHits);
        globalStats.put("totalMisses", totalMisses);
        globalStats.put("totalEvictions", totalEvictions);
        globalStats.put("totalRequests", totalRequests);
        globalStats.put("globalHitRate", totalRequests > 0 ? (double) totalHits / totalRequests : 0.0);
        globalStats.put("globalMissRate", totalRequests > 0 ? (double) totalMisses / totalRequests : 0.0);
        
        allStats.put("global", globalStats);
        
        // Informações do cache manager
        allStats.put("cacheManager", cacheManager.getClass().getSimpleName());
        allStats.put("availableCaches", cacheManager.getCacheNames());
        
        return allStats;
    }

    /**
     * Limpa todas as estatísticas
     */
    public void clearStatistics() {
        hitCounters.clear();
        missCounters.clear();
        evictionCounters.clear();
    }

    /**
     * Limpa um cache específico
     */
    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            recordCacheEviction(cacheName);
        }
    }

    /**
     * Limpa todos os caches
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(this::clearCache);
    }

    /**
     * Verifica se um cache existe
     */
    public boolean cacheExists(String cacheName) {
        return cacheManager.getCache(cacheName) != null;
    }

    /**
     * Obtém informações sobre o cache manager
     */
    public Map<String, Object> getCacheManagerInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("type", cacheManager.getClass().getSimpleName());
        info.put("cacheNames", cacheManager.getCacheNames());
        
        // Informações específicas por cache
        Map<String, Object> cacheDetails = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Map<String, Object> details = new HashMap<>();
                details.put("cacheType", cache.getClass().getSimpleName());
                details.put("nativeCacheType", cache.getNativeCache().getClass().getSimpleName());
                cacheDetails.put(cacheName, details);
            }
        }
        info.put("cacheDetails", cacheDetails);
        
        return info;
    }
}