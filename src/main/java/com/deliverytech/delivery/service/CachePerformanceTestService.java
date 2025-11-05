package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Serviço para testes de performance do cache
 */
@Service
public class CachePerformanceTestService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CacheMetricsService cacheMetricsService;

    /**
     * Executa teste de performance comparando acesso com e sem cache
     */
    public Map<String, Object> executarTestePerformance(Long clienteId, int numeroTentativas) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Limpar cache antes do teste
        cacheMetricsService.clearAllCaches();
        cacheMetricsService.clearStatistics();
        
        // Teste sem cache (primeira execução)
        long tempoSemCache = medirTempoExecucao(() -> {
            for (int i = 0; i < numeroTentativas; i++) {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erros para o teste
                }
            }
        });
        
        // Teste com cache (execuções subsequentes)
        long tempoComCache = medirTempoExecucao(() -> {
            for (int i = 0; i < numeroTentativas; i++) {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erros para o teste
                }
            }
        });
        
        // Calcular métricas
        double melhoria = tempoSemCache > 0 ? 
            ((double) (tempoSemCache - tempoComCache) / tempoSemCache) * 100 : 0;
        
        resultado.put("clienteId", clienteId);
        resultado.put("numeroTentativas", numeroTentativas);
        resultado.put("tempoSemCacheMs", tempoSemCache);
        resultado.put("tempoComCacheMs", tempoComCache);
        resultado.put("melhoriaPercentual", String.format("%.2f%%", melhoria));
        resultado.put("fatorMelhoria", tempoComCache > 0 ? (double) tempoSemCache / tempoComCache : 0);
        resultado.put("estatisticasCache", cacheMetricsService.getAllCacheStatistics());
        
        return resultado;
    }

    /**
     * Executa teste de carga concorrente
     */
    public Map<String, Object> executarTesteCarga(List<Long> clienteIds, int threadCount, int tentativasPorThread) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Limpar cache antes do teste
        cacheMetricsService.clearAllCaches();
        cacheMetricsService.clearStatistics();
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // Primeira execução (sem cache)
        long inicioSemCache = System.currentTimeMillis();
        List<CompletableFuture<Void>> futuresSemCache = new ArrayList<>();
        
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < tentativasPorThread; j++) {
                    Long clienteId = clienteIds.get(j % clienteIds.size());
                    try {
                        clienteService.buscarClientePorId(clienteId);
                    } catch (Exception e) {
                        // Ignora erros para o teste
                    }
                }
            }, executor);
            futuresSemCache.add(future);
        }
        
        // Aguardar conclusão
        CompletableFuture.allOf(futuresSemCache.toArray(new CompletableFuture[0])).join();
        long tempoSemCache = System.currentTimeMillis() - inicioSemCache;
        
        // Segunda execução (com cache)
        long inicioComCache = System.currentTimeMillis();
        List<CompletableFuture<Void>> futuresComCache = new ArrayList<>();
        
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < tentativasPorThread; j++) {
                    Long clienteId = clienteIds.get(j % clienteIds.size());
                    try {
                        clienteService.buscarClientePorId(clienteId);
                    } catch (Exception e) {
                        // Ignora erros para o teste
                    }
                }
            }, executor);
            futuresComCache.add(future);
        }
        
        // Aguardar conclusão
        CompletableFuture.allOf(futuresComCache.toArray(new CompletableFuture[0])).join();
        long tempoComCache = System.currentTimeMillis() - inicioComCache;
        
        executor.shutdown();
        
        // Calcular métricas
        int totalOperacoes = threadCount * tentativasPorThread;
        double throughputSemCache = tempoSemCache > 0 ? (double) totalOperacoes / (tempoSemCache / 1000.0) : 0;
        double throughputComCache = tempoComCache > 0 ? (double) totalOperacoes / (tempoComCache / 1000.0) : 0;
        double melhoriaThroughput = throughputSemCache > 0 ? 
            ((throughputComCache - throughputSemCache) / throughputSemCache) * 100 : 0;
        
        resultado.put("configuracao", Map.of(
            "threadCount", threadCount,
            "tentativasPorThread", tentativasPorThread,
            "totalOperacoes", totalOperacoes,
            "clienteIds", clienteIds
        ));
        
        resultado.put("resultados", Map.of(
            "tempoSemCacheMs", tempoSemCache,
            "tempoComCacheMs", tempoComCache,
            "throughputSemCache", String.format("%.2f ops/sec", throughputSemCache),
            "throughputComCache", String.format("%.2f ops/sec", throughputComCache),
            "melhoriaThroughput", String.format("%.2f%%", melhoriaThroughput)
        ));
        
        resultado.put("estatisticasCache", cacheMetricsService.getAllCacheStatistics());
        
        return resultado;
    }

    /**
     * Executa teste de invalidação de cache
     */
    public Map<String, Object> executarTesteInvalidacao(Long clienteId) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Limpar cache e estatísticas
        cacheMetricsService.clearAllCaches();
        cacheMetricsService.clearStatistics();
        
        try {
            // 1. Primeira busca (miss - carrega no cache)
            long tempo1 = medirTempoExecucao(() -> {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erro
                }
            });
            
            // 2. Segunda busca (hit - vem do cache)
            long tempo2 = medirTempoExecucao(() -> {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erro
                }
            });
            
            // 3. Invalidar cache (simular atualização)
            cacheMetricsService.clearCache("clientes");
            
            // 4. Terceira busca (miss - recarrega no cache)
            long tempo3 = medirTempoExecucao(() -> {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erro
                }
            });
            
            // 5. Quarta busca (hit - vem do cache novamente)
            long tempo4 = medirTempoExecucao(() -> {
                try {
                    clienteService.buscarClientePorId(clienteId);
                } catch (Exception e) {
                    // Ignora erro
                }
            });
            
            resultado.put("clienteId", clienteId);
            resultado.put("tempos", Map.of(
                "primeiraBusca_miss_ms", tempo1,
                "segundaBusca_hit_ms", tempo2,
                "terceiraBusca_miss_apos_invalidacao_ms", tempo3,
                "quartaBusca_hit_ms", tempo4
            ));
            
            resultado.put("analise", Map.of(
                "cacheEfetivo", tempo2 < tempo1 && tempo4 < tempo3,
                "invalidacaoFuncional", tempo3 > tempo2,
                "melhoriaCache", String.format("%.2f%%", 
                    tempo1 > 0 ? ((double)(tempo1 - tempo2) / tempo1) * 100 : 0)
            ));
            
        } catch (Exception e) {
            resultado.put("erro", "Erro durante teste: " + e.getMessage());
        }
        
        resultado.put("estatisticasCache", cacheMetricsService.getAllCacheStatistics());
        
        return resultado;
    }

    /**
     * Mede o tempo de execução de uma operação
     */
    private long medirTempoExecucao(Runnable operacao) {
        long inicio = System.currentTimeMillis();
        operacao.run();
        return System.currentTimeMillis() - inicio;
    }

    /**
     * Gera relatório completo de performance
     */
    public Map<String, Object> gerarRelatorioCompleto(Long clienteId) {
        Map<String, Object> relatorio = new HashMap<>();
        
        relatorio.put("testeBasico", executarTestePerformance(clienteId, 100));
        relatorio.put("testeInvalidacao", executarTesteInvalidacao(clienteId));
        
        // Teste de carga se houver múltiplos clientes
        List<Long> clienteIds = List.of(clienteId);
        relatorio.put("testeCarga", executarTesteCarga(clienteIds, 5, 20));
        
        relatorio.put("timestamp", System.currentTimeMillis());
        relatorio.put("resumo", "Relatório completo de performance do cache");
        
        return relatorio;
    }
}