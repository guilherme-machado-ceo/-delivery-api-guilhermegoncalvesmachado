-- Script de inicialização do PostgreSQL para DeliveryTech
-- Executado automaticamente na primeira inicialização do container

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Configurar timezone
SET timezone = 'America/Sao_Paulo';

-- Criar schema se não existir
CREATE SCHEMA IF NOT EXISTS delivery;

-- Configurar search_path
ALTER DATABASE deliverytech SET search_path TO delivery, public;

-- Criar usuário adicional para leitura (opcional)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'delivery_reader') THEN
        CREATE ROLE delivery_reader WITH LOGIN PASSWORD 'reader_pass123';
    END IF;
END
$$;

-- Conceder permissões de leitura
GRANT CONNECT ON DATABASE deliverytech TO delivery_reader;
GRANT USAGE ON SCHEMA delivery TO delivery_reader;
GRANT USAGE ON SCHEMA public TO delivery_reader;

-- Configurações de performance
ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';
ALTER SYSTEM SET max_connections = 100;
ALTER SYSTEM SET shared_buffers = '128MB';
ALTER SYSTEM SET effective_cache_size = '256MB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;

-- Recarregar configurações
SELECT pg_reload_conf();

-- Log de inicialização
INSERT INTO pg_stat_statements_info (dealloc) VALUES (0) ON CONFLICT DO NOTHING;

-- Mensagem de sucesso
DO $$
BEGIN
    RAISE NOTICE 'DeliveryTech database initialized successfully!';
    RAISE NOTICE 'Database: deliverytech';
    RAISE NOTICE 'User: delivery_user';
    RAISE NOTICE 'Timezone: America/Sao_Paulo';
    RAISE NOTICE 'Extensions: uuid-ossp, pg_trgm';
END
$$;