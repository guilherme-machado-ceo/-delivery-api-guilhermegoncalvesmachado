-- Schema para testes H2
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    endereco TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS restaurantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco TEXT,
    cep VARCHAR(8),
    telefone VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    restaurante_id BIGINT NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    tempo_preparo INTEGER,
    peso DECIMAL(5,3),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    taxa_entrega DECIMAL(10,2),
    endereco_entrega TEXT,
    cep_entrega VARCHAR(8),
    observacoes TEXT,
    forma_pagamento VARCHAR(50),
    valor_troco DECIMAL(10,2),
    data_entrega_desejada TIMESTAMP,
    nome_contato VARCHAR(100),
    telefone_contato VARCHAR(20),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    observacao TEXT,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_cliente_email ON clientes(email);
CREATE INDEX IF NOT EXISTS idx_cliente_ativo ON clientes(ativo);
CREATE INDEX IF NOT EXISTS idx_restaurante_ativo ON restaurantes(ativo);
CREATE INDEX IF NOT EXISTS idx_produto_restaurante ON produtos(restaurante_id);
CREATE INDEX IF NOT EXISTS idx_produto_disponivel ON produtos(disponivel);
CREATE INDEX IF NOT EXISTS idx_pedido_cliente ON pedidos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_pedido_restaurante ON pedidos(restaurante_id);
CREATE INDEX IF NOT EXISTS idx_pedido_status ON pedidos(status);
CREATE INDEX IF NOT EXISTS idx_pedido_data ON pedidos(data_pedido);
CREATE INDEX IF NOT EXISTS idx_item_pedido ON item_pedido(pedido_id);