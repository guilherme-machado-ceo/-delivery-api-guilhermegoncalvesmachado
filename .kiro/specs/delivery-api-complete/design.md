# Design Document

## Overview

O design da API de delivery completa segue os princípios REST com documentação OpenAPI, padronização de respostas HTTP e arquitetura em camadas. A solução implementa controllers completos para todas as entidades (Restaurante, Produto, Pedido, Relatório), com documentação interativa via Swagger UI e testes de integração abrangentes.

## Architecture

### Camadas da Aplicação

```
┌─────────────────────────────────────┐
│           Controllers               │
│  (REST Endpoints + Swagger Docs)   │
├─────────────────────────────────────┤
│             Services                │
│      (Business Logic)              │
├─────────────────────────────────────┤
│           Repositories              │
│        (Data Access)               │
├─────────────────────────────────────┤
│            Database                 │
│         (H2/MySQL)                 │
└─────────────────────────────────────┘
```

### Padrões de Design

- **Controller-Service-Repository**: Separação clara de responsabilidades
- **DTO Pattern**: Objetos específicos para transferência de dados
- **Response Wrapper**: Padronização de respostas da API
- **Exception Handling**: Tratamento centralizado de erros
- **Pagination**: Implementação consistente em listagens

## Components and Interfaces

### 1. Controllers REST

#### RestauranteController
- **Endpoints Completos**: 8 endpoints incluindo cálculo de taxa e busca por proximidade
- **Filtros**: Categoria, ativo, busca por nome
- **Paginação**: Implementada em listagens
- **Documentação**: Swagger completo com exemplos

#### ProdutoController  
- **CRUD Completo**: Create, Read, Update, Delete
- **Funcionalidades Especiais**: Toggle disponibilidade, busca por nome
- **Relacionamentos**: Produtos por restaurante, por categoria
- **Validações**: Preço, disponibilidade, associação com restaurante

#### PedidoController
- **Operações Avançadas**: Criação completa, cálculo sem salvar
- **Filtros**: Status, data, cliente, restaurante
- **Histórico**: Por cliente e por restaurante
- **Status**: Transições controladas de status

#### RelatorioController (Novo)
- **Vendas**: Por restaurante e período
- **Rankings**: Produtos mais vendidos, clientes ativos
- **Métricas**: Agregações e estatísticas de negócio

### 2. DTOs e Responses

#### Estruturas Padronizadas
```java
// Response wrapper padrão
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;
}

// Response paginada
public class PagedResponse<T> {
    private List<T> content;
    private PageInfo page;
    private Map<String, String> links;
}

// Response de erro
public class ErrorResponse {
    private boolean success;
    private ErrorDetails error;
    private LocalDateTime timestamp;
}
```

### 3. Documentação OpenAPI

#### Configuração Swagger
- **Metadados**: Título, versão, descrição da API
- **Segurança**: Esquemas JWT documentados
- **Tags**: Agrupamento por funcionalidade
- **Servidores**: Ambientes dev/prod

#### Anotações Obrigatórias
- `@Tag`: Agrupamento de endpoints
- `@Operation`: Descrição de cada endpoint
- `@ApiResponse`: Códigos de resposta possíveis
- `@Parameter`: Documentação de parâmetros
- `@Schema`: Documentação de DTOs

## Data Models

### Entidades Principais

#### Restaurante
```java
@Entity
@Schema(description = "Dados do restaurante")
public class Restaurante {
    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;
    
    @Schema(description = "Nome do restaurante", example = "Pizza Express")
    private String nome;
    
    @Schema(description = "Categoria", example = "Pizza")
    private String categoria;
    
    @Schema(description = "Status ativo", example = "true")
    private Boolean ativo;
}
```

#### Produto
```java
@Entity
@Schema(description = "Produto do restaurante")
public class Produto {
    @Schema(description = "ID único do produto")
    private Long id;
    
    @Schema(description = "Nome do produto")
    private String nome;
    
    @Schema(description = "Preço do produto")
    private BigDecimal preco;
    
    @Schema(description = "Disponibilidade")
    private Boolean disponivel;
}
```

### DTOs de Request/Response

#### RestauranteDTO (Request)
- Validações: `@NotBlank`, `@NotNull`
- Campos obrigatórios documentados
- Exemplos para cada campo

#### RestauranteResponseDTO (Response)
- Todos os dados do restaurante
- Campos calculados (avaliação, tempo entrega)
- Links relacionados

## Error Handling

### Estratégia de Tratamento

#### GlobalExceptionHandler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex) {
        // Retorna 400 com detalhes dos campos inválidos
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
        EntityNotFoundException ex) {
        // Retorna 404 com mensagem clara
    }
}
```

#### Códigos HTTP Padronizados
- **200 OK**: Busca bem-sucedida
- **201 Created**: Recurso criado (com header Location)
- **204 No Content**: Operação sem retorno
- **400 Bad Request**: Dados inválidos (com detalhes)
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito de dados (duplicação)
- **500 Internal Server Error**: Erro interno

### Estrutura de Erro Padronizada
```json
{
  "success": false,
  "error": {
    "code": "ENTITY_NOT_FOUND",
    "message": "Restaurante não encontrado",
    "details": "Nenhum restaurante encontrado com ID: 999",
    "fields": [
      {
        "field": "nome",
        "message": "Nome é obrigatório"
      }
    ]
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Testing Strategy

### Testes de Integração com MockMvc

#### Estrutura dos Testes
```java
@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
class RestauranteControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void deveListarRestaurantesComFiltros() throws Exception {
        mockMvc.perform(get("/api/restaurantes")
                .param("categoria", "Pizza")
                .param("ativo", "true")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page.number").value(0));
    }
}
```

#### Cenários Obrigatórios
1. **Sucesso**: Criação, busca, atualização
2. **Erro**: Dados inválidos, recurso não encontrado
3. **Paginação**: Metadados corretos
4. **Filtros**: Funcionamento de todos os filtros
5. **Códigos HTTP**: Verificação de status corretos

### Collection Postman/Insomnia

#### Estrutura da Collection
- **Ambientes**: Dev, Test, Prod
- **Variáveis**: IDs dinâmicos, tokens
- **Testes Automatizados**: Validação de responses
- **Cenários de Erro**: Documentados e testáveis

#### Cenários Principais
1. **Fluxo Completo**: Criar restaurante → Adicionar produtos → Fazer pedido
2. **Filtros e Busca**: Testar todos os filtros implementados
3. **Paginação**: Navegar entre páginas
4. **Relatórios**: Validar cálculos e agregações
5. **Erros**: Testar validações e tratamento de erros

### Validação da Documentação

#### Checklist Swagger
- [ ] Interface acessível em /swagger-ui.html
- [ ] Todos os endpoints documentados
- [ ] Exemplos funcionais
- [ ] "Try it out" operacional
- [ ] Schemas corretos para DTOs
- [ ] Códigos de resposta documentados

## Implementation Notes

### Dependências Necessárias
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### Configurações
```properties
# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

### Padrões de Implementação
1. **Controllers**: Sempre usar `@Valid` em request bodies
2. **Responses**: Sempre usar wrappers padronizados
3. **Paginação**: Implementar em todas as listagens
4. **Documentação**: Anotar todos os endpoints e DTOs
5. **Testes**: Cobrir cenários de sucesso e erro