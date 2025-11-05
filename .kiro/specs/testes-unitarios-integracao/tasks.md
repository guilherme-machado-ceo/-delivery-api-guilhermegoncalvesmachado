# Implementation Plan

- [x] 1. Configurar dependências e ambiente de teste


  - Adicionar dependências JUnit 5, Mockito e Spring Boot Test no pom.xml
  - Configurar plugin JaCoCo para cobertura de código
  - Criar application-test.properties com configurações específicas de teste
  - Configurar banco H2 em memória para testes de integração
  - _Requirements: 4.1, 4.2_







- [ ] 2. Implementar testes unitários para ClienteService
  - [x] 2.1 Criar estrutura base do ClienteServiceTest




    - Configurar anotações @ExtendWith(MockitoExtension.class)
    - Configurar mocks para ClienteRepository e ModelMapper

    - Implementar métodos de setup e teardown
    - _Requirements: 1.1, 1.2_




  - [ ] 2.2 Implementar testes de cenários positivos do ClienteService
    - Testar método salvarCliente() com dados válidos
    - Testar busca de cliente por ID existente

    - Testar listagem de clientes ativos
    - Testar atualização de cliente com dados válidos


    - _Requirements: 1.1, 1.2_

  - [ ] 2.3 Implementar testes de cenários negativos do ClienteService
    - Testar validação de email duplicado
    - Testar busca de cliente por ID inexistente
    - Testar atualização de cliente inexistente

    - Validar que exceções corretas são lançadas

    - _Requirements: 1.1, 1.2_

  - [ ]* 2.4 Implementar testes de cobertura adicional para ClienteService
    - Testar métodos auxiliares e validações


    - Testar cenários de edge cases
    - Atingir meta de 80% de cobertura
    - _Requirements: 3.2_




- [ ] 3. Implementar testes unitários para PedidoService
  - [ ] 3.1 Criar estrutura base do PedidoServiceTest
    - Configurar anotações @ExtendWith(MockitoExtension.class)
    - Configurar mocks para repositories e services dependentes


    - Implementar métodos de setup com dados de teste
    - _Requirements: 1.1, 1.3_


  - [ ] 3.2 Implementar testes de criação e cálculo de pedidos
    - Testar criação de pedido com produtos válidos

    - Testar cálculo correto do valor total
    - Testar aplicação de taxa de entrega


    - Testar criação de itens do pedido


    - _Requirements: 1.1, 1.3_

  - [ ] 3.3 Implementar testes de validação de negócio do PedidoService
    - Testar validação de estoque insuficiente



    - Testar validação de produtos inexistentes
    - Testar validação de restaurante inativo

    - Testar validação de cliente inativo
    - _Requirements: 1.1, 1.3_

  - [ ] 3.4 Implementar testes de atualização e controle de status
    - Testar atualização de status do pedido
    - Testar cancelamento de pedido


    - Testar cenários de rollback em caso de erro
    - Validar transições de status permitidas
    - _Requirements: 1.1, 1.3_

  - [x]* 3.5 Implementar testes de cobertura adicional para PedidoService




    - Testar métodos de busca e listagem
    - Testar cenários de autorização e segurança
    - Atingir meta de 80% de cobertura
    - _Requirements: 3.2_

- [x] 4. Implementar testes de integração para ClienteController

  - [ ] 4.1 Configurar ambiente de teste de integração para ClienteController
    - Configurar @SpringBootTest e @AutoConfigureTestDatabase


    - Configurar @TestPropertySource para application-test.properties
    - Implementar @DirtiesContext para isolamento entre testes

    - Configurar TestRestTemplate ou MockMvc
    - _Requirements: 2.1, 2.2_





  - [ ] 4.2 Implementar testes de endpoints CRUD do ClienteController
    - Testar POST /api/clientes com dados válidos (status 201)
    - Testar POST /api/clientes com dados inválidos (status 400)
    - Testar GET /api/clientes/{id} existente (status 200)
    - Testar GET /api/clientes/{id} inexistente (status 404)
    - _Requirements: 2.1, 2.2_





  - [ ] 4.3 Implementar testes de listagem e atualização do ClienteController
    - Testar GET /api/clientes com paginação (status 200)
    - Testar PUT /api/clientes/{id} com dados válidos (status 200)
    - Testar PUT /api/clientes/{id} com dados inválidos (status 400)


    - Validar estrutura JSON das respostas
    - _Requirements: 2.1, 2.2_

  - [ ] 4.4 Implementar testes de validação e tratamento de erros
    - Testar cenários de validação Bean Validation
    - Testar respostas de erro padronizadas RFC 7807




    - Testar headers HTTP corretos
    - Verificar persistência no banco de dados H2
    - _Requirements: 2.1, 2.2_


- [ ] 5. Implementar testes de integração para PedidoController
  - [x] 5.1 Configurar ambiente de teste de integração para PedidoController

    - Configurar @SpringBootTest com perfil de teste
    - Configurar dados de teste para clientes, restaurantes e produtos
    - Implementar métodos de setup para cenários complexos
    - Configurar isolamento de dados entre testes
    - _Requirements: 2.1, 2.3_

  - [x] 5.2 Implementar testes de criação de pedidos completos



    - Testar POST /api/pedidos com pedido válido completo
    - Testar validação de produtos inexistentes
    - Testar validação de estoque insuficiente


    - Testar cálculo de valor total nos responses
    - _Requirements: 2.1, 2.3_



  - [ ] 5.3 Implementar testes de consulta e histórico de pedidos
    - Testar GET /api/pedidos/cliente/{id} para histórico do cliente
    - Testar GET /api/pedidos/{id} para detalhes do pedido
    - Testar filtros de status e data
    - Validar paginação de resultados


    - _Requirements: 2.1, 2.3_

  - [ ] 5.4 Implementar testes de atualização de status e cenários de erro
    - Testar PUT /api/pedidos/{id}/status para atualização de status
    - Testar cenários de erro com payloads padronizados
    - Testar validações de autorização por perfil
    - Verificar integridade dos dados após operações
    - _Requirements: 2.1, 2.3_




- [ ] 6. Configurar cobertura de código e relatórios
  - [ ] 6.1 Configurar JaCoCo Maven Plugin
    - Adicionar plugin JaCoCo no pom.xml com configuração completa
    - Configurar goals de execução (prepare-agent, report, check)
    - Definir regras de cobertura mínima (80% para serviços)

    - Configurar exclusões para classes de configuração
    - _Requirements: 3.1, 3.3_


  - [ ] 6.2 Implementar automação de execução e relatórios
    - Configurar execução automática via Maven
    - Configurar falha de build se cobertura < 80%


    - Criar comandos para execução de testes específicos
    - Documentar comandos Maven para execução local
    - _Requirements: 3.1, 3.3, 4.2_


  - [ ] 6.3 Gerar e validar relatórios de cobertura
    - Executar mvn clean test jacoco:report
    - Analisar relatório HTML gerado
    - Identificar áreas com baixa cobertura



    - Documentar métricas de qualidade obtidas
    - _Requirements: 3.1, 3.3_

- [ ] 7. Criar utilitários e configurações de teste
  - [ ] 7.1 Implementar classes de dados de teste (TestData)





    - Criar ClienteTestData com builders para cenários de teste
    - Criar PedidoTestData com dados válidos e inválidos
    - Criar ProdutoTestData para testes de integração


    - Implementar RestauranteTestData para cenários completos
    - _Requirements: 2.1, 2.2, 2.3_

  - [ ] 7.2 Configurar perfis e propriedades de teste
    - Finalizar application-test.properties com todas as configurações



    - Configurar logging específico para testes
    - Configurar desabilitação de cache durante testes
    - Implementar @TestConfiguration para beans específicos
    - _Requirements: 4.1, 4.2_

  - [ ]* 7.3 Implementar utilitários de teste adicionais
    - Criar TestUtils para operações comuns
    - Implementar matchers customizados para assertions
    - Criar helpers para validação de JSON
    - Implementar cleanup automático de dados
    - _Requirements: 2.1, 4.1_

- [ ] 8. Documentar e finalizar implementação
  - [ ] 8.1 Criar documentação de execução de testes
    - Criar README com instruções para executar testes
    - Documentar comandos Maven para diferentes cenários
    - Explicar estratégia de testes adotada
    - Documentar estrutura de diretórios de teste
    - _Requirements: 4.2_

  - [ ] 8.2 Validar implementação completa
    - Executar todos os testes e verificar sucesso
    - Validar cobertura de código ≥ 80% nos serviços
    - Testar execução em diferentes ambientes
    - Verificar isolamento adequado entre testes
    - _Requirements: 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3_

  - [ ]* 8.3 Criar relatório final de implementação
    - Gerar relatório HTML do JaCoCo final
    - Criar screenshots das métricas de cobertura
    - Documentar análise dos resultados obtidos
    - Criar demonstração de execução via linha de comando
    - _Requirements: 3.3, 4.2_