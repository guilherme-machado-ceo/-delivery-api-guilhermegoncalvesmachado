#!/bin/bash

# Script para inicializar a aplicação DeliveryTech com Docker Compose
# Uso: ./scripts/start.sh [dev|prod]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    error "Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Verificar se Docker Compose está disponível
if ! command -v docker-compose > /dev/null 2>&1; then
    error "Docker Compose não está instalado."
    exit 1
fi

# Determinar ambiente
ENVIRONMENT=${1:-dev}

log "Iniciando DeliveryTech API em modo: $ENVIRONMENT"

# Criar arquivo .env se não existir
if [ ! -f .env ]; then
    warning "Arquivo .env não encontrado. Criando a partir do .env.example..."
    cp .env.example .env
    warning "Por favor, revise o arquivo .env antes de continuar."
fi

# Configurar arquivos de compose baseado no ambiente
case $ENVIRONMENT in
    "dev")
        COMPOSE_FILES="-f docker-compose.yml -f docker-compose.dev.yml"
        log "Modo desenvolvimento: Hot reload habilitado, monitoramento desabilitado"
        ;;
    "prod")
        COMPOSE_FILES="-f docker-compose.yml"
        log "Modo produção: Todos os serviços habilitados"
        ;;
    *)
        error "Ambiente inválido: $ENVIRONMENT. Use 'dev' ou 'prod'"
        exit 1
        ;;
esac

# Parar containers existentes
log "Parando containers existentes..."
docker-compose $COMPOSE_FILES down

# Limpar volumes órfãos (opcional)
if [ "$2" = "--clean" ]; then
    warning "Limpando volumes órfãos..."
    docker volume prune -f
fi

# Build das imagens
log "Construindo imagens..."
docker-compose $COMPOSE_FILES build --no-cache

# Iniciar serviços
log "Iniciando serviços..."
docker-compose $COMPOSE_FILES up -d

# Aguardar serviços ficarem prontos
log "Aguardando serviços ficarem prontos..."
sleep 10

# Verificar status dos serviços
log "Verificando status dos serviços..."
docker-compose $COMPOSE_FILES ps

# Verificar health checks
log "Verificando health checks..."
sleep 30

# Mostrar logs da aplicação
log "Últimos logs da aplicação:"
docker-compose $COMPOSE_FILES logs --tail=20 delivery-api

# URLs de acesso
success "DeliveryTech API iniciada com sucesso!"
echo ""
echo "URLs de acesso:"
echo "  - API: http://localhost:9090"
echo "  - Swagger UI: http://localhost:9090/swagger-ui/index.html"
echo "  - Health Check: http://localhost:9090/actuator/health"

if [ "$ENVIRONMENT" = "prod" ]; then
    echo "  - Nginx: http://localhost"
    echo "  - Prometheus: http://localhost:9091"
    echo "  - Grafana: http://localhost:3000 (admin/admin123)"
fi

echo ""
echo "Comandos úteis:"
echo "  - Ver logs: docker-compose $COMPOSE_FILES logs -f"
echo "  - Parar: docker-compose $COMPOSE_FILES down"
echo "  - Reiniciar: docker-compose $COMPOSE_FILES restart"
echo "  - Status: docker-compose $COMPOSE_FILES ps"