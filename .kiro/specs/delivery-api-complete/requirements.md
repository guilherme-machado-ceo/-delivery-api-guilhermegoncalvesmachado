# Requirements Document

## Introduction

Este documento define os requisitos para completar a API de delivery com controllers REST completos, documentação Swagger/OpenAPI, padronização de respostas e testes de integração. O sistema deve fornecer uma API profissional e bem documentada para integrações com aplicações mobile e web.

## Glossary

- **API**: Application Programming Interface - interface para comunicação entre sistemas
- **Controller**: Componente responsável por receber e processar requisições HTTP
- **DTO**: Data Transfer Object - objeto para transferência de dados entre camadas
- **Swagger/OpenAPI**: Ferramenta para documentação interativa de APIs REST
- **MockMvc**: Framework para testes de integração de controllers Spring
- **Paginação**: Divisão de resultados em páginas para melhor performance
- **CORS**: Cross-Origin Resource Sharing - política de acesso entre domínios

## Requirements

### Requirement 1

**User Story:** Como desenvolvedor de aplicação mobile, eu quero uma API REST completa para restaurantes, para que eu possa integrar funcionalidades de listagem, busca e cálculo de taxa de entrega.

#### Acceptance Criteria

1. WHEN uma requisição POST é feita para /api/restaurantes, THE Sistema SHALL criar um novo restaurante e retornar status 201
2. WHEN uma requisição GET é feita para /api/restaurantes com filtros, THE Sistema SHALL retornar lista paginada com metadados
3. WHEN uma requisição GET é feita para /api/restaurantes/{id}/taxa-entrega/{cep}, THE Sistema SHALL calcular e retornar a taxa de entrega
4. WHEN uma requisição GET é feita para /api/restaurantes/proximos/{cep}, THE Sistema SHALL retornar restaurantes próximos ao CEP informado

### Requirement 2

**User Story:** Como desenvolvedor de aplicação web, eu quero uma API REST completa para produtos, para que eu possa gerenciar o catálogo de produtos dos restaurantes.

#### Acceptance Criteria

1. WHEN uma requisição POST é feita para /api/produtos, THE Sistema SHALL criar um novo produto e retornar status 201
2. WHEN uma requisição GET é feita para /api/restaurantes/{restauranteId}/produtos, THE Sistema SHALL retornar todos os produtos do restaurante
3. WHEN uma requisição PATCH é feita para /api/produtos/{id}/disponibilidade, THE Sistema SHALL alternar a disponibilidade do produto
4. WHEN uma requisição GET é feita para /api/produtos/buscar com parâmetro nome, THE Sistema SHALL retornar produtos que contenham o nome informado

### Requirement 3

**User Story:** Como desenvolvedor de sistema de pedidos, eu quero uma API REST avançada para pedidos, para que eu possa gerenciar todo o ciclo de vida dos pedidos.

#### Acceptance Criteria

1. WHEN uma requisição POST é feita para /api/pedidos, THE Sistema SHALL criar um pedido completo com itens e retornar status 201
2. WHEN uma requisição GET é feita para /api/pedidos com filtros, THE Sistema SHALL retornar pedidos filtrados por status e data
3. WHEN uma requisição POST é feita para /api/pedidos/calcular, THE Sistema SHALL calcular o total do pedido sem salvar
4. WHEN uma requisição GET é feita para /api/clientes/{clienteId}/pedidos, THE Sistema SHALL retornar o histórico de pedidos do cliente

### Requirement 4

**User Story:** Como analista de negócios, eu quero uma API de relatórios, para que eu possa acompanhar métricas de vendas e performance dos restaurantes.

#### Acceptance Criteria

1. WHEN uma requisição GET é feita para /api/relatorios/vendas-por-restaurante, THE Sistema SHALL retornar vendas agrupadas por restaurante
2. WHEN uma requisição GET é feita para /api/relatorios/produtos-mais-vendidos, THE Sistema SHALL retornar ranking dos produtos mais vendidos
3. WHEN uma requisição GET é feita para /api/relatorios/clientes-ativos, THE Sistema SHALL retornar lista de clientes mais ativos
4. WHEN uma requisição GET é feita para /api/relatorios/pedidos-por-periodo, THE Sistema SHALL retornar pedidos filtrados por período

### Requirement 5

**User Story:** Como desenvolvedor que integra com a API, eu quero documentação Swagger/OpenAPI completa, para que eu possa entender e testar todos os endpoints facilmente.

#### Acceptance Criteria

1. WHEN eu acesso /swagger-ui.html, THE Sistema SHALL exibir interface Swagger funcionando
2. WHEN eu visualizo um endpoint na documentação, THE Sistema SHALL mostrar exemplos de request e response
3. WHEN eu uso "Try it out" no Swagger, THE Sistema SHALL executar a requisição e mostrar o resultado
4. WHERE um DTO é usado, THE Sistema SHALL documentar todos os campos com @Schema

### Requirement 6

**User Story:** Como desenvolvedor frontend, eu quero respostas padronizadas e códigos HTTP corretos, para que eu possa tratar as respostas de forma consistente.

#### Acceptance Criteria

1. WHEN uma operação é bem-sucedida, THE Sistema SHALL retornar código HTTP apropriado (200, 201, 204)
2. WHEN ocorre um erro de validação, THE Sistema SHALL retornar status 400 com detalhes dos erros
3. WHEN um recurso não é encontrado, THE Sistema SHALL retornar status 404 com mensagem clara
4. WHERE há listagem de dados, THE Sistema SHALL implementar paginação com metadados

### Requirement 7

**User Story:** Como desenvolvedor de testes, eu quero testes de integração completos, para que eu possa garantir a qualidade e funcionamento correto da API.

#### Acceptance Criteria

1. WHEN os testes de integração são executados, THE Sistema SHALL validar todos os controllers com MockMvc
2. WHEN um cenário de sucesso é testado, THE Sistema SHALL verificar status code e estrutura da resposta
3. WHEN um cenário de erro é testado, THE Sistema SHALL verificar tratamento correto de exceções
4. WHERE há paginação implementada, THE Sistema SHALL validar metadados de paginação nos testes