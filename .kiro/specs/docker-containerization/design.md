# Design Document

## Overview

Este documento descreve o design da containerização da aplicação DeliveryTech usando Docker e Docker Compose, incluindo arquitetura de containers, estratégias de build e orquestração de serviços.

## Architecture

### Container Architecture
```
┌─────────────────────────────────────┐
│           Load Balancer             │
│            (Nginx)                  │
├─────────────────────────────────────┤
│         Application Layer           │
│      (Spring Boot Container)        │
├─────────────────────────────────────┤
│          Cache Layer                │
│        (Redis Container)            │
├─────────────────────────────────────┤
│        Database Layer               │
│      (PostgreSQL Container)         │
├─────────────────────────────────────┤
│         Monitoring Layer            │
│    (Prometheus + Grafana)           │
└─────────────────────────────────────┘
```

### Multi-Stage Build Strategy
1. **Build Stage**: Compilação com Maven e JDK completo
2. **Runtime Stage**: Execução com JRE otimizado
3. **Security**: Usuário não-root para execução
4. **Optimization**: Layers cacheáveis e imagem mínima

## Components and Interfaces

### Docker Services
- **delivery-api**: Aplicação Spring Boot principal
- **postgres**: Banco de dados PostgreSQL
- **redis**: Cache distribuído Redis
- **nginx**: Proxy reverso e load balancer
- **prometheus**: Coleta de métricas
- **grafana**: Dashboard de monitoramento

### Network Configuration
- **frontend-network**: Comunicação externa (nginx)
- **backend-network**: Comunicação interna (app, db, cache)
- **monitoring-network**: Rede de monitoramento

### Volume Configuration
- **postgres-data**: Persistência do banco de dados
- **redis-data**: Persistência do cache (opcional)
- **app-logs**: Logs da aplicação
- **grafana-data**: Configurações do Grafana

## Data Models

### Environment Variables
```yaml
# Application
SPRING_PROFILES_ACTIVE: prod
SERVER_PORT: 8080
JAVA_OPTS: -Xmx512m -Xms256m

# Database
DB_HOST: postgres
DB_PORT: 5432
DB_NAME: deliverytech
DB_USER: delivery_user
DB_PASSWORD: ${DB_PASSWORD}

# Cache
REDIS_HOST: redis
REDIS_PORT: 6379
CACHE_TYPE: redis

# Security
JWT_SECRET: ${JWT_SECRET}
```

### Port Mapping
- **Application**: 8080 (internal) → 9090 (external)
- **Database**: 5432 (internal only)
- **Redis**: 6379 (internal only)
- **Nginx**: 80/443 (external)
- **Prometheus**: 9091 (monitoring)
- **Grafana**: 3000 (monitoring)

## Error Handling

### Container Health Checks
- **Application**: HTTP health endpoint
- **Database**: PostgreSQL connection test
- **Redis**: Redis ping command
- **Nginx**: HTTP status check

### Restart Policies
- **Application**: restart: unless-stopped
- **Database**: restart: always
- **Cache**: restart: unless-stopped
- **Monitoring**: restart: on-failure

## Testing Strategy

### Local Development
- Docker Compose para desenvolvimento local
- Hot reload com volumes de código
- Banco H2 para testes rápidos

### Production Deployment
- Imagens otimizadas para produção
- PostgreSQL para persistência
- Redis para cache distribuído
- Nginx para proxy e SSL

### Monitoring and Logging
- Logs centralizados via Docker logging
- Métricas via Prometheus
- Dashboards via Grafana
- Health checks automatizados