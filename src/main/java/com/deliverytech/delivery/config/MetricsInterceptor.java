package com.deliverytech.delivery.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MetricsInterceptor implements HandlerInterceptor {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    private static final String TIMER_ATTRIBUTE = "request.timer";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Timer.Sample sample = Timer.start(meterRegistry);
        request.setAttribute(TIMER_ATTRIBUTE, sample);
        
        // Contar acessos por endpoint
        String endpoint = getEndpointFromUri(request.getRequestURI());
        if (endpoint != null) {
            Counter.builder("api.requests")
                    .description("Número de requests por endpoint")
                    .tag("endpoint", endpoint)
                    .tag("method", request.getMethod())
                    .register(meterRegistry)
                    .increment();
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        Timer.Sample sample = (Timer.Sample) request.getAttribute(TIMER_ATTRIBUTE);
        if (sample != null) {
            String endpoint = getEndpointFromUri(request.getRequestURI());
            if (endpoint != null) {
                sample.stop(Timer.builder("api.response.time")
                        .description("Tempo de resposta por endpoint")
                        .tag("endpoint", endpoint)
                        .tag("method", request.getMethod())
                        .tag("status", String.valueOf(response.getStatus()))
                        .register(meterRegistry));
            }
        }
        
        // Contar erros
        if (response.getStatus() >= 400) {
            Counter.builder("api.errors")
                    .description("Número de erros por endpoint")
                    .tag("endpoint", getEndpointFromUri(request.getRequestURI()))
                    .tag("status", String.valueOf(response.getStatus()))
                    .register(meterRegistry)
                    .increment();
        }
    }
    
    private String getEndpointFromUri(String uri) {
        if (uri.startsWith("/api/restaurantes")) return "restaurantes";
        if (uri.startsWith("/api/produtos")) return "produtos";
        if (uri.startsWith("/api/pedidos")) return "pedidos";
        if (uri.startsWith("/api/clientes")) return "clientes";
        if (uri.startsWith("/api/relatorios")) return "relatorios";
        if (uri.startsWith("/actuator")) return "actuator";
        if (uri.startsWith("/swagger-ui")) return "swagger";
        return "outros";
    }
}