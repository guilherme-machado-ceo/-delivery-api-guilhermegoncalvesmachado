#!/bin/bash

# Script para parar a aplicação DeliveryTech
# Uso: ./scripts/stop.sh [--clean]

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

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log "Parando DeliveryTech API..."

# Parar todos os containers
docker-compose -f docker-compose.yml -f docker-compose.dev.yml down

# Se --clean foi passado, limpar volumes e imagens
if [ "$1" = "--clean" ]; then
    warning "Limpando volumes e imagens..."
    
    # Remover volumes
    docker volume rm -f delivery-postgres-data delivery-redis-data delivery-app-logs 2>/dev/null || true
    docker volume rm -f delivery-postgres-dev-data delivery-redis-dev-data 2>/dev/null || true
    docker volume rm -f delivery-nginx-logs delivery-prometheus-data delivery-grafana-data 2>/dev/null || true
    
    # Remover imagens da aplicação
    docker rmi -f $(docker images | grep delivery | awk '{print $3}') 2>/dev/null || true
    
    # Limpar imagens órfãs
    docker image prune -f
    
    warning "Limpeza completa realizada!"
fi

success "DeliveryTech API parada com sucesso!"

# Mostrar status
echo ""
echo "Status dos containers:"
docker ps -a | grep delivery || echo "Nenhum container da aplicação encontrado."