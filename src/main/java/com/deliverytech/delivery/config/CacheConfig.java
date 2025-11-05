package com.deliverytech.delivery.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 * Configuração de Cache para a aplicação DeliveryTech
 * Suporta cache local (ConcurrentMap) e distribuído (Redis)
 */
@Configuration
public class CacheConfig implements CachingConfigurer {

    // Nomes dos caches utilizados na aplicação
    public static final String CLIENTES_CACHE = "clientes";
    public static final String RESTAURANTES_CACHE = "restaurantes";
    public static final String PRODUTOS_CACHE = "produtos";
    public static final String PEDIDOS_CACHE = "pedidos";
    public static final String ESTATISTICAS_CACHE = "estatisticas";

    /**
     * Configuração de Cache Local usando ConcurrentMapCache
     * Usado quando Redis não está disponível ou para desenvolvimento
     */
    @Bean
    @Primary
    @Profile({"default", "dev", "test"})
    public CacheManager localCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Configurar os caches disponíveis
        cacheManager.setCacheNames(Arrays.asList(
            CLIENTES_CACHE,
            RESTAURANTES_CACHE, 
            PRODUTOS_CACHE,
            PEDIDOS_CACHE,
            ESTATISTICAS_CACHE
        ));
        
        // Permitir criação dinâmica de novos caches
        cacheManager.setAllowNullValues(false);
        
        return cacheManager;
    }

    /**
     * Gerador de chaves personalizado para cache
     * Cria chaves mais legíveis e organizadas
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator() {
            @Override
            public Object generate(Object target, java.lang.reflect.Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getSimpleName()).append(".");
                sb.append(method.getName()).append(":");
                
                for (Object param : params) {
                    if (param != null) {
                        sb.append(param.toString()).append(":");
                    }
                }
                
                // Remove o último ":"
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ':') {
                    sb.setLength(sb.length() - 1);
                }
                
                return sb.toString();
            }
        };
    }

    /**
     * Tratamento de erros de cache
     * Garante que falhas no cache não quebrem a aplicação
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, 
                                          org.springframework.cache.Cache cache, Object key) {
                // Log do erro mas não quebra a aplicação
                System.err.println("Erro ao buscar no cache: " + cache.getName() + 
                                 ", chave: " + key + ", erro: " + exception.getMessage());
                // Não relança a exceção - permite fallback para banco de dados
            }

            @Override
            public void handleCachePutError(RuntimeException exception, 
                                          org.springframework.cache.Cache cache, Object key, Object value) {
                // Log do erro mas não quebra a aplicação
                System.err.println("Erro ao salvar no cache: " + cache.getName() + 
                                 ", chave: " + key + ", erro: " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, 
                                            org.springframework.cache.Cache cache, Object key) {
                // Log do erro mas não quebra a aplicação
                System.err.println("Erro ao remover do cache: " + cache.getName() + 
                                 ", chave: " + key + ", erro: " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, 
                                            org.springframework.cache.Cache cache) {
                // Log do erro mas não quebra a aplicação
                System.err.println("Erro ao limpar cache: " + cache.getName() + 
                                 ", erro: " + exception.getMessage());
            }
        };
    }

    @Override
    public CacheResolver cacheResolver() {
        return null; // Usa o resolver padrão
    }
}