# Implementation Plan

- [ ] 1. Completar RestauranteController com todos os endpoints
  - [x] 1.1 Implementar endpoint GET /api/restaurantes/{id}/taxa-entrega/{cep}


    - Criar método para calcular taxa de entrega baseada no CEP
    - Integrar com serviço de cálculo de distância
    - Retornar TaxaEntregaResponse com valor e tempo estimado
    - _Requirements: 1.1_



  - [ ] 1.2 Implementar endpoint GET /api/restaurantes/proximos/{cep}
    - Criar método para buscar restaurantes próximos ao CEP
    - Implementar lógica de cálculo de distância

    - Ordenar por proximidade e tempo de entrega
    - _Requirements: 1.1_

  - [ ] 1.3 Adicionar documentação Swagger completa ao RestauranteController
    - Adicionar @Operation em todos os métodos
    - Adicionar @ApiResponse para todos os códigos de status
    - Adicionar @Parameter para todos os parâmetros
    - _Requirements: 5.1, 5.2_

  - [x] 1.4 Implementar paginação no endpoint GET /api/restaurantes




    - Modificar método para aceitar Pageable
    - Retornar PagedResponse com metadados
    - Adicionar links de navegação (first, last, next, prev)
    - _Requirements: 6.4_


- [ ] 2. Implementar ProdutoController completo
  - [ ] 2.1 Criar estrutura básica do ProdutoController
    - Criar classe controller com @RestController e @RequestMapping
    - Adicionar @Tag para documentação Swagger
    - Injetar ProdutoService

    - _Requirements: 2.1_

  - [ ] 2.2 Implementar CRUD básico de produtos
    - POST /api/produtos - Criar produto
    - GET /api/produtos/{id} - Buscar por ID
    - PUT /api/produtos/{id} - Atualizar produto

    - DELETE /api/produtos/{id} - Remover produto
    - _Requirements: 2.1, 2.2_

  - [ ] 2.3 Implementar endpoints especiais de produtos
    - PATCH /api/produtos/{id}/disponibilidade - Toggle disponibilidade
    - GET /api/restaurantes/{restauranteId}/produtos - Produtos do restaurante


    - GET /api/produtos/categoria/{categoria} - Por categoria
    - GET /api/produtos/buscar?nome={nome} - Busca por nome
    - _Requirements: 2.2, 2.4_


  - [ ] 2.4 Adicionar documentação Swagger ao ProdutoController
    - Documentar todos os endpoints com @Operation
    - Adicionar exemplos de request/response
    - Documentar códigos de erro possíveis
    - _Requirements: 5.1, 5.2, 5.3_


- [ ] 3. Implementar PedidoController avançado
  - [ ] 3.1 Criar estrutura do PedidoController
    - Criar classe controller com anotações básicas
    - Injetar PedidoService e dependências necessárias
    - Configurar documentação Swagger inicial
    - _Requirements: 3.1_


  - [ ] 3.2 Implementar operações básicas de pedidos
    - POST /api/pedidos - Criar pedido completo com itens
    - GET /api/pedidos/{id} - Buscar pedido completo
    - GET /api/pedidos - Listar com filtros (status, data)
    - PATCH /api/pedidos/{id}/status - Atualizar status


    - _Requirements: 3.1, 3.2_

  - [ ] 3.3 Implementar operações avançadas de pedidos
    - DELETE /api/pedidos/{id} - Cancelar pedido

    - GET /api/clientes/{clienteId}/pedidos - Histórico do cliente
    - GET /api/restaurantes/{restauranteId}/pedidos - Pedidos do restaurante
    - POST /api/pedidos/calcular - Calcular total sem salvar
    - _Requirements: 3.3, 3.4_


  - [ ] 3.4 Implementar filtros e paginação para pedidos
    - Adicionar filtros por status, data, cliente, restaurante
    - Implementar paginação com metadados
    - Adicionar ordenação por data de criação
    - _Requirements: 6.4_


- [ ] 4. Criar RelatorioController novo
  - [ ] 4.1 Criar estrutura do RelatorioController
    - Criar classe controller com anotações
    - Injetar RelatorioService
    - Configurar documentação Swagger
    - _Requirements: 4.1_

  - [ ] 4.2 Implementar relatórios de vendas
    - GET /api/relatorios/vendas-por-restaurante - Vendas por restaurante
    - GET /api/relatorios/pedidos-por-periodo - Pedidos por período
    - Adicionar filtros de data (dataInicio, dataFim)
    - _Requirements: 4.1, 4.4_

  - [ ] 4.3 Implementar relatórios de ranking
    - GET /api/relatorios/produtos-mais-vendidos - Top produtos
    - GET /api/relatorios/clientes-ativos - Clientes mais ativos
    - Implementar paginação nos rankings
    - _Requirements: 4.2, 4.3_

  - [ ] 4.4 Adicionar documentação completa aos relatórios
    - Documentar todos os endpoints de relatório
    - Adicionar exemplos de responses com dados
    - Documentar parâmetros de filtro e período
    - _Requirements: 5.1, 5.2_

- [ ] 5. Implementar classes de resposta padronizada
  - [x] 5.1 Criar classes de response wrapper


    - Criar ApiResponse<T> genérica
    - Criar PagedResponse<T> para listagens
    - Criar ErrorResponse para erros
    - Criar ValidationErrorResponse para erros de validação
    - _Requirements: 6.1, 6.2_





  - [ ] 5.2 Implementar GlobalExceptionHandler
    - Criar @RestControllerAdvice para tratamento global
    - Tratar MethodArgumentNotValidException (400)
    - Tratar EntityNotFoundException (404)
    - Tratar DataIntegrityViolationException (409)
    - Tratar Exception genérica (500)
    - _Requirements: 6.1, 6.2, 6.3_

  - [ ] 5.3 Atualizar todos os controllers para usar responses padronizadas
    - Modificar RestauranteController para usar ApiResponse
    - Modificar ProdutoController para usar wrappers
    - Modificar PedidoController para usar responses padronizadas
    - Modificar RelatorioController para usar wrappers
    - _Requirements: 6.1, 6.4_

- [ ] 6. Completar documentação Swagger/OpenAPI
  - [ ] 6.1 Configurar OpenAPI avançado
    - Criar OpenAPIConfig com metadados completos
    - Configurar informações da API (título, versão, descrição)
    - Adicionar informações de contato e licença
    - Configurar servidores (dev, prod)
    - _Requirements: 5.1, 5.2_

  - [ ] 6.2 Documentar todos os DTOs com @Schema
    - Adicionar @Schema em RestauranteDTO e ResponseDTO
    - Adicionar @Schema em ProdutoDTO e ResponseDTO
    - Adicionar @Schema em PedidoDTO e ResponseDTO
    - Adicionar exemplos e descrições em todos os campos
    - _Requirements: 5.3, 5.4_

  - [ ] 6.3 Configurar grupos de tags organizados
    - Organizar endpoints por funcionalidade
    - Configurar ordem de exibição das tags
    - Adicionar descrições detalhadas para cada tag
    - _Requirements: 5.1_

  - [ ] 6.4 Validar documentação Swagger completa
    - Verificar se Swagger UI está acessível
    - Testar "Try it out" em todos os endpoints
    - Validar exemplos de request/response
    - Verificar se todos os schemas estão corretos
    - _Requirements: 5.4_

- [ ] 7. Implementar testes de integração completos
  - [ ] 7.1 Criar RestauranteControllerIT
    - Testar todos os endpoints CRUD
    - Testar filtros (categoria, ativo, busca)
    - Testar paginação e metadados
    - Testar cálculo de taxa de entrega
    - Testar busca por proximidade
    - _Requirements: 7.1, 7.2_

  - [ ] 7.2 Criar ProdutoControllerIT
    - Testar CRUD completo de produtos
    - Testar toggle de disponibilidade
    - Testar listagem por restaurante
    - Testar busca por nome e categoria
    - _Requirements: 7.1, 7.2_

  - [ ] 7.3 Criar PedidoControllerIT
    - Testar criação de pedido completo
    - Testar filtros por status e data
    - Testar histórico por cliente e restaurante
    - Testar cálculo sem salvar
    - Testar cancelamento de pedido
    - _Requirements: 7.1, 7.2_

  - [ ] 7.4 Criar RelatorioControllerIT
    - Testar relatórios de vendas
    - Testar rankings de produtos e clientes
    - Testar filtros por período
    - Validar cálculos e agregações
    - _Requirements: 7.1, 7.2_

- [ ] 8. Criar Collection Postman/Insomnia
  - [ ] 8.1 Configurar ambiente e variáveis
    - Criar ambiente de desenvolvimento
    - Configurar variáveis para base URL
    - Configurar variáveis para IDs dinâmicos
    - _Requirements: 7.3_

  - [ ] 8.2 Criar requests para todos os endpoints
    - Criar pasta para cada controller
    - Adicionar todos os endpoints com exemplos
    - Configurar testes automatizados nas requests
    - _Requirements: 7.3, 7.4_

  - [ ] 8.3 Documentar cenários de erro
    - Criar requests para testar validações
    - Criar requests para testar recursos não encontrados
    - Criar requests para testar conflitos de dados
    - _Requirements: 7.2, 7.4_

  - [ ] 8.4 Criar cenários de fluxo completo
    - Cenário: Criar restaurante → Adicionar produtos → Fazer pedido
    - Cenário: Listar restaurantes com filtros e paginação
    - Cenário: Gerar relatórios por período
    - _Requirements: 7.3, 7.4_

- [ ] 9. Validação final e testes de aceitação
  - [ ] 9.1 Executar todos os testes e validar cobertura
    - Executar testes unitários existentes
    - Executar todos os testes de integração
    - Verificar cobertura de código
    - Corrigir testes que falharem
    - _Requirements: 7.1, 7.4_

  - [ ] 9.2 Validar Swagger UI e documentação
    - Acessar /swagger-ui.html e verificar carregamento
    - Testar "Try it out" em endpoints principais
    - Validar exemplos e schemas
    - Verificar organização por tags
    - _Requirements: 5.4_

  - [ ] 9.3 Testar cenários obrigatórios via Swagger
    - Cenário 1: Listar restaurantes com filtros
    - Cenário 2: Buscar produtos de um restaurante
    - Cenário 3: Criar pedido completo
    - Cenário 4: Relatório de vendas por período
    - _Requirements: 7.4_

  - [ ] 9.4 Validar padronização de respostas
    - Verificar códigos HTTP corretos em todos os endpoints
    - Validar estrutura de ApiResponse em sucessos
    - Validar estrutura de ErrorResponse em erros
    - Verificar headers apropriados (Content-Type, Location)
    - _Requirements: 6.1, 6.2, 6.3_