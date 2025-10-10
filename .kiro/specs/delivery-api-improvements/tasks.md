# Implementation Plan

- [x] 1. Setup testing infrastructure and dependencies


  - Add testing dependencies to pom.xml (JUnit 5, Mockito, MockMvc)
  - Create base test configuration classes
  - Setup test profiles and properties
  - _Requirements: 1.1, 1.2_

- [ ] 2. Implement unit tests for controllers
  - [ ] 2.1 Create ClienteControllerTest with MockMvc
    - Test POST /api/clientes endpoint
    - Test GET /api/clientes endpoint
    - Test GET /api/clientes/{id} endpoint
    - Test PUT /api/clientes/{id} endpoint
    - Test DELETE /api/clientes/{id} endpoint
    - _Requirements: 1.1, 1.2_

  - [ ] 2.2 Create RestauranteControllerTest with MockMvc
    - Test all CRUD operations
    - Test search and filter endpoints
    - Test status update endpoint
    - _Requirements: 1.1, 1.2_

  - [ ] 2.3 Create ProdutoControllerTest with MockMvc
    - Test product creation and updates
    - Test availability toggle endpoint
    - Test product listing by restaurant
    - _Requirements: 1.1, 1.2_

  - [ ] 2.4 Create PedidoControllerTest with MockMvc
    - Test order creation and status updates
    - Test order history endpoints
    - Test order filtering and search
    - _Requirements: 1.1, 1.2_

- [ ] 3. Implement unit tests for services
  - [ ] 3.1 Create ClienteServiceTest with Mockito
    - Test business logic for client operations
    - Test email uniqueness validation
    - Test client activation/deactivation
    - _Requirements: 1.2, 1.3_

  - [ ] 3.2 Create RestauranteServiceTest with Mockito
    - Test restaurant management logic
    - Test rating calculations
    - Test category filtering
    - _Requirements: 1.2, 1.3_

  - [ ] 3.3 Create ProdutoServiceTest with Mockito
    - Test product validation logic
    - Test price validation
    - Test restaurant association
    - _Requirements: 1.2, 1.3_

  - [ ] 3.4 Create PedidoServiceTest with Mockito
    - Test order creation logic
    - Test status transition validation
    - Test order calculation logic
    - _Requirements: 1.2, 1.3_





- [ ] 4. Add Swagger/OpenAPI documentation
  - [x] 4.1 Add SpringDoc OpenAPI dependency to pom.xml


    - Add springdoc-openapi-starter-webmvc-ui dependency
    - Configure OpenAPI properties
    - _Requirements: 2.1, 2.2_



  - [x] 4.2 Create OpenAPI configuration class


    - Define API info (title, version, description)
    - Configure security schemes for JWT
    - Setup tags for endpoint grouping

    - _Requirements: 2.1, 2.2_


  - [x] 4.3 Document controller endpoints with annotations




    - Add @Operation annotations to all endpoints
    - Add @ApiResponse annotations for different status codes
    - Add @Parameter annotations for path and query parameters
    - _Requirements: 2.2, 2.3_



  - [ ] 4.4 Document data models with @Schema annotations
    - Add @Schema annotations to all entity classes
    - Add @Schema annotations to DTO classes
    - Provide examples and descriptions


    - _Requirements: 2.2, 2.3_

- [x] 5. Implement Bean Validation



  - [ ] 5.1 Add validation annotations to entity models
    - Add @NotNull, @NotBlank, @Email annotations to Cliente
    - Add validation annotations to Restaurante model
    - Add validation annotations to Produto model




    - Add validation annotations to Pedido model
    - _Requirements: 3.1, 3.2, 3.3_






  - [ ] 5.2 Create custom validators
    - Create @UniqueEmail validator for client email uniqueness
    - Create @ValidPrice validator for positive prices
    - Create @ValidStatus validator for order status transitions
    - _Requirements: 3.1, 3.2_

  - [x] 5.3 Update controllers to use @Valid annotation


    - Add @Valid to request body parameters in all controllers

    - Update method signatures to handle validation
    - _Requirements: 3.1, 3.4_

  - [x] 5.4 Create DTO classes for API requests and responses





    - Create request DTOs for create/update operations


    - Create response DTOs for API responses
    - Add validation annotations to DTOs
    - _Requirements: 3.1, 3.2_

- [ ] 6. Implement global exception handling
  - [ ] 6.1 Create ErrorResponse and FieldError classes
    - Define standard error response structure


    - Create classes for field-level validation errors
    - _Requirements: 4.1, 4.2_



  - [ ] 6.2 Create GlobalExceptionHandler with @RestControllerAdvice
    - Handle MethodArgumentNotValidException for validation errors
    - Handle EntityNotFoundException for 404 errors
    - Handle DataIntegrityViolationException for duplicate data
    - Handle generic Exception for 500 errors
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

  - [ ] 6.3 Create custom exception classes
    - Create EntityNotFoundException for resource not found
    - Create DuplicateResourceException for duplicate resources
    - Create BusinessException for business logic violations
    - _Requirements: 4.2, 4.3_



  - [ ] 6.4 Update services to throw appropriate exceptions
    - Update all service methods to throw custom exceptions
    - Replace generic exceptions with specific ones
    - _Requirements: 4.2, 4.3, 4.4_



- [x] 7. Implement basic security with Spring Security


  - [x] 7.1 Add Spring Security and JWT dependencies



    - Add spring-boot-starter-security dependency
    - Add JWT library dependency (jjwt)
    - _Requirements: 5.1_





  - [ ] 7.2 Create User entity and repository
    - Create User entity with username, password, and roles
    - Create UserRepository interface
    - Create initial admin user data loader

    - _Requirements: 5.1, 5.2_

  - [ ] 7.3 Implement JWT token provider and authentication filter
    - Create JwtTokenProvider for token generation and validation

    - Create JwtAuthenticationFilter for request filtering
    - Create UserDetailsService implementation
    - _Requirements: 5.1, 5.4_

  - [ ] 7.4 Configure Spring Security
    - Create SecurityConfig class with security rules
    - Configure public endpoints (health, swagger, auth)
    - Configure protected endpoints with role-based access
    - Configure CORS for frontend integration
    - _Requirements: 5.1, 5.2, 5.3_

  - [ ] 7.5 Create authentication controller
    - Create AuthController with login endpoint
    - Implement login logic with JWT token generation
    - Add user registration endpoint (optional)
    - _Requirements: 5.1, 5.4_

- [ ] 8. Integration and final testing
  - [ ] 8.1 Run all tests and ensure they pass
    - Execute unit tests and verify coverage
    - Fix any failing tests
    - _Requirements: 1.3, 1.4_

  - [ ] 8.2 Test Swagger documentation
    - Verify Swagger UI is accessible
    - Test API endpoints through Swagger interface
    - _Requirements: 2.4_

  - [ ] 8.3 Test validation and error handling
    - Test validation errors return proper responses
    - Test exception handling returns consistent error format
    - _Requirements: 3.4, 4.1_

  - [ ] 8.4 Test security implementation
    - Test authentication with valid/invalid credentials
    - Test protected endpoints require authentication
    - Test role-based access control
    - _Requirements: 5.2, 5.3, 5.4_