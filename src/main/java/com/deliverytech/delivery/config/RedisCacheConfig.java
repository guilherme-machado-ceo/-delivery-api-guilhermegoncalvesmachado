package com.deliverytech.delivery.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração de Cache Distribuído usando Redis
 * Ativado apenas quando Redis está disponível e configurado
 */
@Configuration
@Profile({"prod", "redis"})
@ConditionalOnProperty(name = "spring.data.redis.host")
public class RedisCacheConfig {

    /**
     * Configuração do RedisTemplate para operações manuais
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configurar serialização
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = createJsonSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // Configurar serializers
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Cache Manager do Redis com configurações específicas por cache
     */
    @Bean
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // Configuração padrão
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // TTL padrão de 30 minutos
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(createJsonSerializer()))
                .disableCachingNullValues(); // Não cachear valores nulos

        // Configurações específicas por cache
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Cache de clientes - 30 minutos
        cacheConfigurations.put(CacheConfig.CLIENTES_CACHE, 
                defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Cache de restaurantes - 1 hora
        cacheConfigurations.put(CacheConfig.RESTAURANTES_CACHE, 
                defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // Cache de produtos - 15 minutos
        cacheConfigurations.put(CacheConfig.PRODUTOS_CACHE, 
                defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Cache de pedidos - 5 minutos
        cacheConfigurations.put(CacheConfig.PEDIDOS_CACHE, 
                defaultConfig.entryTtl(Duration.ofMinutes(5)));
        
        // Cache de estatísticas - 10 minutos
        cacheConfigurations.put(CacheConfig.ESTATISTICAS_CACHE, 
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware() // Suporte a transações
                .build();
    }

    /**
     * Cria o serializer JSON para objetos Redis
     */
    private Jackson2JsonRedisSerializer<Object> createJsonSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, 
                ObjectMapper.DefaultTyping.NON_FINAL);
        
        // Suporte para Java 8 Time API
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }
}