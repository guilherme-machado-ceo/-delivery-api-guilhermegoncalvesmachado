-- Senha para todos os usuários é "123456"
-- A senha criptografada com BCrypt é: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC

INSERT INTO usuario (id, nome, email, senha, ativo, data_criacao, restaurante_id) VALUES
(1, 'Admin Sistema', 'admin@delivery.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', true, NOW(), null),
(2, 'João Cliente', 'joao@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', true, NOW(), null),
(3, 'Pizza Palace', 'pizza@palace.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', true, NOW(), 1),
(4, 'Carlos Entregador', 'carlos@entrega.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', true, NOW(), null);

INSERT INTO usuario_roles (usuario_id, roles) VALUES
(1, 'ADMIN'),
(2, 'CLIENTE'),
(3, 'RESTAURANTE'),
(4, 'ENTREGADOR');
