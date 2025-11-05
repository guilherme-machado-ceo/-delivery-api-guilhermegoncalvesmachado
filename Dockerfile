# Multi-stage Dockerfile para DeliveryTech API
# Otimizado para produção com imagem mínima

# ================================
# STAGE 1: Build Stage
# ================================
FROM openjdk:21-jdk-alpine AS builder

# Metadados da imagem
LABEL maintainer="DeliveryTech Team"
LABEL description="DeliveryTech API - Build Stage"
LABEL version="2.1.0"

# Instalar dependências necessárias para o build
RUN apk add --no-cache \
    curl \
    bash \
    && rm -rf /var/cache/apk/*

# Configurar diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven primeiro (para cache de layers)
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Dar permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Baixar dependências (layer cacheável)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN ./mvnw clean package -DskipTests -B

# Verificar se o JAR foi criado
RUN ls -la target/

# ================================
# STAGE 2: Runtime Stage
# ================================
FROM openjdk:21-jre-alpine AS runtime

# Metadados da imagem final
LABEL maintainer="DeliveryTech Team"
LABEL description="DeliveryTech API - Production Ready"
LABEL version="2.1.0"

# Instalar dependências de runtime
RUN apk add --no-cache \
    curl \
    bash \
    tzdata \
    && rm -rf /var/cache/apk/*

# Configurar timezone
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S delivery && \
    adduser -u 1001 -S delivery -G delivery

# Configurar diretório da aplicação
WORKDIR /app

# Criar diretórios necessários
RUN mkdir -p /app/logs && \
    chown -R delivery:delivery /app

# Copiar JAR da aplicação do stage de build
COPY --from=builder /app/target/delivery-api-*.jar app.jar

# Ajustar permissões
RUN chown delivery:delivery app.jar

# Configurar usuário de execução
USER delivery

# Configurar variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080

# Expor porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Metadata adicional
LABEL org.opencontainers.image.title="DeliveryTech API"
LABEL org.opencontainers.image.description="API REST para sistema de delivery de comida"
LABEL org.opencontainers.image.version="2.1.0"
LABEL org.opencontainers.image.created="2025-10-01"
LABEL org.opencontainers.image.source="https://github.com/deliverytech/delivery-api"