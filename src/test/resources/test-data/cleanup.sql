-- Script de limpeza para testes
DELETE FROM item_pedido;
DELETE FROM pedidos;
DELETE FROM produtos;
DELETE FROM restaurantes;
DELETE FROM clientes;

-- Reset sequences (H2 specific)
ALTER SEQUENCE IF EXISTS cliente_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS restaurante_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS produto_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS pedido_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS item_pedido_seq RESTART WITH 1;