# 🐳 Guia Completo - Docker e Docker Compose

Este guia fornece instruções detalhadas para usar a containerização da aplicação DeliveryTech.

## 📋 **Índice**

- [Pré-requisitos](#pré-requisitos)
- [Configuração Inicial](#configuração-inicial)
- [Comandos Básicos](#comandos-básicos)
- [Ambientes](#ambientes)
- [Monitoramento](#monitoramento)
- [Troubleshooting](#troubleshooting)
- [Comandos Avançados](#comandos-avançados)

## 🔧 **Pré-requisitos**

### **Software Necessário**
```bash
# Docker
docker --version  # >= 20.10.0
docker-compose --version  # >= 2.0.0

# Git
git --version
```

### **Recursos Mínimos**
- **RAM:** 4GB disponível
- **Disk:** 10GB livres
- **CPU:** 2 cores
- **Rede:** Portas 80, 3000, 5432, 6379, 9090, 9091 disponíveis

## ⚙️ **Configuração Inicial**

### **1. Clonar Repositório**
```bash
git clone <repository-url>
cd delivery-api
```

### **2. Configurar Variáveis de Ambiente**
```bash
# Copiar arquivo de exemplo
cp .env.example .env

# Editar configurações (opcional)
nano .env
```

### **3. Dar Permissões aos Scripts**
```bash
chmod +x scripts/start.sh
chmod +x scripts/stop.sh
```

## 🚀 **Comandos Básicos**

### **Iniciar Aplicação**

#### **Desenvolvimento (Recomendado para começar)**
```bash
# Usando script (recomendado)
./scripts/start.sh dev

# Ou manualmente
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

#### **Produção (Todos os serviços)**
```bash
# Usando script (recomendado)
./scripts/start.sh prod

# Ou manualmente
docker-compose up -d
```

### **Parar Aplicação**
```bash
# Parada normal
./scripts/stop.sh

# Limpeza completa (remove volumes e imagens)
./scripts/stop.sh --clean
```

### **Verificar Status**
```bash
# Status dos containers
docker-compose ps

# Logs em tempo real
docker-compose logs -f

# Logs de um serviço específico
docker-compose logs -f delivery-api
```

## 🌍 **Ambientes**

### **Desenvolvimento (dev)**
- **Serviços:** API + PostgreSQL + Redis
- **Debug:** Porta 5005 habilitada
- **Hot Reload:** Preparado (volumes de código)
- **Monitoramento:** Desabilitado por padrão
- **Restart:** Manual

### **Produção (prod)**
- **Serviços:** Todos (API + DB + Cache + Nginx + Monitoring)
- **SSL:** Preparado para certificados
- **Rate Limiting:** Habilitado
- **Monitoramento:** Prometheus + Grafana
- **Restart:** Automático

## 📊 **Monitoramento**

### **URLs de Acesso**
- **Aplicação:** http://localhost:9090
- **Swagger:** http://localhost:9090/swagger-ui/index.html
- **Health:** http://localhost:9090/actuator/health
- **Prometheus:** http://localhost:9091
- **Grafana:** http://localhost:3000 (admin/admin123)

### **Métricas Disponíveis**
- **JVM:** Memory, GC, Threads
- **HTTP:** Request rate, response time, errors
- **Database:** Connection pool, query time
- **Cache:** Hit rate, evictions
- **Custom:** Business metrics

## 🔍 **Troubleshooting**

### **Problemas Comuns**

#### **Container não inicia**
```bash
# Verificar logs
docker-compose logs delivery-api

# Verificar health check
docker inspect delivery-api | grep Health -A 10

# Verificar recursos
docker stats
```

#### **Erro de conexão com banco**
```bash
# Verificar se PostgreSQL está rodando
docker-compose ps postgres

# Testar conexão
docker exec delivery-postgres pg_isready -U delivery_user

# Verificar logs do banco
docker-compose logs postgres
```

#### **Cache não funciona**
```bash
# Verificar Redis
docker-compose ps redis

# Testar Redis
docker exec delivery-redis redis-cli ping

# Verificar configurações de cache
curl http://localhost:9090/api/cache/health
```

#### **Nginx não responde**
```bash
# Verificar configuração
docker exec delivery-nginx nginx -t

# Recarregar configuração
docker exec delivery-nginx nginx -s reload

# Verificar upstream
curl -I http://localhost:9090/actuator/health
```

### **Comandos de Debug**

#### **Acessar Container**
```bash
# Aplicação
docker exec -it delivery-api sh

# PostgreSQL
docker exec -it delivery-postgres psql -U delivery_user deliverytech

# Redis
docker exec -it delivery-redis redis-cli

# Nginx
docker exec -it delivery-nginx sh
```

#### **Verificar Recursos**
```bash
# Uso de recursos
docker stats

# Espaço em disco
docker system df

# Limpar recursos não utilizados
docker system prune -f
```

## 🛠️ **Comandos Avançados**

### **Build e Deploy**

#### **Build Manual**
```bash
# Build apenas da aplicação
docker build -t delivery-api:latest .

# Build com cache limpo
docker build --no-cache -t delivery-api:latest .

# Build multi-platform
docker buildx build --platform linux/amd64,linux/arm64 -t delivery-api:latest .
```

#### **Deploy Específico**
```bash
# Apenas aplicação
docker-compose up -d delivery-api

# Apenas banco
docker-compose up -d postgres

# Recrear serviço
docker-compose up -d --force-recreate delivery-api
```

### **Backup e Restore**

#### **Backup PostgreSQL**
```bash
# Backup completo
docker exec delivery-postgres pg_dump -U delivery_user deliverytech > backup_$(date +%Y%m%d_%H%M%S).sql

# Backup com compressão
docker exec delivery-postgres pg_dump -U delivery_user -Fc deliverytech > backup_$(date +%Y%m%d_%H%M%S).dump
```

#### **Restore PostgreSQL**
```bash
# Restore SQL
docker exec -i delivery-postgres psql -U delivery_user deliverytech < backup.sql

# Restore dump
docker exec -i delivery-postgres pg_restore -U delivery_user -d deliverytech backup.dump
```

### **Scaling e Load Balancing**

#### **Escalar Aplicação**
```bash
# Múltiplas instâncias
docker-compose up -d --scale delivery-api=3

# Verificar load balancing
for i in {1..10}; do curl -s http://localhost/api/health; done
```

### **Monitoramento Avançado**

#### **Métricas Customizadas**
```bash
# Estatísticas de cache
curl http://localhost:9090/api/cache/statistics

# Teste de performance
curl http://localhost:9090/api/cache/test/simple/1

# Métricas Prometheus
curl http://localhost:9090/actuator/prometheus
```

---

## 📝 **Configurações Personalizadas**

### **Variáveis de Ambiente Importantes**
```bash
# Aplicação
SPRING_PROFILES_ACTIVE=docker
JAVA_OPTS=-Xmx512m -Xms256m

# Banco
DB_HOST=postgres
DB_NAME=deliverytech
DB_USER=delivery_user
DB_PASSWORD=sua_senha_segura

# Cache
REDIS_HOST=redis
CACHE_TYPE=redis

# Segurança
JWT_SECRET=sua_chave_jwt_super_secreta
```

### **Portas Customizadas**
```yaml
# No docker-compose.yml
ports:
  - "${API_PORT:-9090}:8080"      # API
  - "${DB_PORT:-5432}:5432"       # PostgreSQL
  - "${REDIS_PORT:-6379}:6379"    # Redis
  - "${NGINX_PORT:-80}:80"        # Nginx
```

---

## 🎉 **Comandos de Referência Rápida**

```bash
# Iniciar desenvolvimento
./scripts/start.sh dev

# Iniciar produção
./scripts/start.sh prod

# Parar aplicação
./scripts/stop.sh

# Ver logs
docker-compose logs -f delivery-api

# Status dos serviços
docker-compose ps

# Rebuild da aplicação
docker-compose build --no-cache delivery-api

# Reiniciar serviço
docker-compose restart delivery-api

# Backup do banco
docker exec delivery-postgres pg_dump -U delivery_user deliverytech > backup.sql

# Limpar tudo
./scripts/stop.sh --clean
```

---

*Guia atualizado em Outubro 2025*  
*DeliveryTech API - Docker Guide v2.2*