# Design Document

## Overview

Este documento detalha o design técnico para implementar melhorias na API DeliveryTech, incluindo testes unitários, documentação Swagger, validações Bean Validation, tratamento centralizado de exceções e segurança básica com Spring Security.

## Architecture

### Current Architecture
- **Presentation Layer**: Controllers REST
- **Business Layer**: Services com lógica de negócio
- **Data Layer**: Repositories JPA
- **Database**: H2 em memória

### Enhanced Architecture
- **Security Layer**: Spring Security com JWT
- **Validation Layer**: Bean Validation
- **Exception Handling**: Global Exception Handler
- **Documentation**: OpenAPI/Swagger
- **Testing Layer**: Unit Tests com MockMvc e Mockito

## Components and Interfaces

### 1. Testing Infrastructure

#### Test Configuration
- **MockMvc**: Para testes de controllers
- **@MockBean**: Para mock de services
- **TestContainers**: Para testes de integração (opcional)
- **JUnit 5**: Framework de testes

#### Test Structure
```
src/test/java/
├── controller/
│   ├── ClienteControllerTest.java
│   ├── RestauranteControllerTest.java
│   ├── ProdutoControllerTest.java
│   └── PedidoControllerTest.java
├── service/
│   ├── ClienteServiceTest.java
│   ├── RestauranteServiceTest.java
│   ├── ProdutoServiceTest.java
│   └── PedidoServiceTest.java
└── config/
    └── TestConfig.java
```

### 2. API Documentation

#### Swagger Configuration
- **SpringDoc OpenAPI**: Biblioteca para documentação
- **@OpenAPIDefinition**: Configuração global da API
- **@Operation**: Documentação de endpoints
- **@Schema**: Documentação de modelos

#### Documentation Structure
- API Info: Título, versão, descrição
- Security Schemes: JWT Bearer Token
- Tags: Agrupamento por funcionalidade
- Examples: Exemplos de request/response

### 3. Validation Layer

#### Bean Validation Annotations
- **@Valid**: Validação em controllers
- **@NotNull, @NotBlank**: Campos obrigatórios
- **@Email**: Validação de email
- **@Min, @Max**: Validação numérica
- **@Size**: Validação de tamanho

#### Custom Validators
- **@UniqueEmail**: Validador customizado para email único
- **@ValidStatus**: Validador para status de pedido
- **@ValidPrice**: Validador para preços positivos

### 4. Exception Handling

#### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Tratamento de ValidationException
    // Tratamento de EntityNotFoundException
    // Tratamento de DataIntegrityViolationException
    // Tratamento de Exception genérica
}
```

#### Error Response Structure
```java
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;
}
```

### 5. Security Layer

#### Spring Security Configuration
- **JWT Authentication**: Token-based authentication
- **Password Encoding**: BCrypt para senhas
- **Role-based Access**: ADMIN, USER roles
- **CORS Configuration**: Para frontend

#### Security Components
- **JwtAuthenticationFilter**: Filtro para validação JWT
- **JwtTokenProvider**: Geração e validação de tokens
- **UserDetailsService**: Carregamento de usuários
- **SecurityConfig**: Configuração de segurança

## Data Models

### Enhanced Models with Validation

#### Cliente Model
```java
@Entity
public class Cliente {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @UniqueEmail
    private String email;
    
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;
    
    // outros campos...
}
```

#### User Model (New)
```java
@Entity
public class User {
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // outros campos...
}
```

### DTOs for API

#### Request DTOs
- **ClienteCreateRequest**: Dados para criação
- **ClienteUpdateRequest**: Dados para atualização
- **LoginRequest**: Credenciais de login

#### Response DTOs
- **ClienteResponse**: Dados do cliente
- **AuthResponse**: Token JWT
- **ErrorResponse**: Detalhes de erro

## Error Handling

### Exception Types
1. **ValidationException**: Dados inválidos (400)
2. **EntityNotFoundException**: Recurso não encontrado (404)
3. **DuplicateResourceException**: Recurso duplicado (409)
4. **UnauthorizedException**: Não autenticado (401)
5. **ForbiddenException**: Sem permissão (403)

### Error Response Format
```json
{
  "timestamp": "2025-10-08T15:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados de entrada inválidos",
  "path": "/api/clientes",
  "fieldErrors": [
    {
      "field": "email",
      "message": "Email deve ser válido"
    }
  ]
}
```

## Testing Strategy

### Unit Tests
- **Controller Tests**: MockMvc para testar endpoints
- **Service Tests**: Mockito para mock de dependencies
- **Repository Tests**: @DataJpaTest para testes de dados
- **Validation Tests**: Testes de Bean Validation

### Test Coverage Goals
- **Controllers**: 90% cobertura
- **Services**: 85% cobertura
- **Repositories**: 80% cobertura
- **Overall**: 80% cobertura mínima

### Test Scenarios
1. **Happy Path**: Cenários de sucesso
2. **Validation Errors**: Dados inválidos
3. **Not Found**: Recursos inexistentes
4. **Security**: Acesso não autorizado
5. **Edge Cases**: Casos extremos

## Security Implementation

### Authentication Flow
1. User submits credentials
2. System validates credentials
3. JWT token is generated
4. Token is returned to client
5. Client includes token in subsequent requests
6. System validates token on each request

### Authorization Levels
- **PUBLIC**: Endpoints públicos (health, swagger)
- **USER**: Endpoints para usuários autenticados
- **ADMIN**: Endpoints administrativos

### JWT Configuration
- **Secret Key**: Configurável via properties
- **Expiration**: 24 horas
- **Refresh Token**: Implementação futura
- **Claims**: username, roles, exp

## Implementation Phases

### Phase 1: Testing Infrastructure
- Setup test dependencies
- Create base test classes
- Implement controller tests

### Phase 2: API Documentation
- Add Swagger dependencies
- Configure OpenAPI
- Document existing endpoints

### Phase 3: Validation Layer
- Add validation annotations
- Create custom validators
- Update controllers

### Phase 4: Exception Handling
- Create global exception handler
- Define error response structure
- Update error handling

### Phase 5: Security Implementation
- Add Spring Security
- Implement JWT authentication
- Configure authorization rules