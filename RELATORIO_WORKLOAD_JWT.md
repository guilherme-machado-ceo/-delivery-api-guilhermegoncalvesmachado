# Relatório de Workload: Implementação de Segurança e Autenticação JWT

## 1. Visão Geral

Este relatório documenta o conjunto de atualizações realizadas para implementar um sistema robusto de segurança e autenticação baseado em JSON Web Tokens (JWT) no projeto de delivery. O trabalho foi executado com base nas especificações do documento `Prática 07 - SEGURANÇA E AUTENTICAÇÃO JWT NO PROJETO DELIVERY.pdf`.

O objetivo principal foi transformar a API, que era totalmente aberta, em um sistema seguro com controle de acesso por perfil, proteção de dados sensíveis e rastreabilidade de ações. Todos os entregáveis solicitados no documento foram contemplados.

## 2. Resumo das Atividades Concluídas

A seguir, um resumo das atividades realizadas, alinhado com as tarefas do documento de requisitos.

### 2.1. Configuração do Spring Security (`ATIVIDADE 1`)

-   **Entidade `Usuario`**: A entidade `User` foi completamente refatorada para corresponder à especificação.
    -   **Campos**: `id`, `nome`, `email` (usado como username), `senha` (criptografada com BCrypt), `roles` (Enum), `ativo`, `dataCriacao`, e `restauranteId`.
    -   **Interface**: A entidade agora implementa `UserDetails` do Spring Security, simplificando a integração com o framework.
-   **`UsuarioRepository`**: O `UserRepository` foi atualizado para buscar usuários por `email` (`findByEmail`), em vez de `username`.
-   **`SecurityConfig`**: A configuração de segurança foi centralizada e ajustada para:
    -   **Endpoints Públicos**: `POST /api/auth/login`, `POST /api/auth/register`, `GET /api/restaurantes`, `GET /api/produtos`, e `/actuator/health` foram liberados.
    -   **Endpoints Protegidos**: Todas as demais rotas agora exigem autenticação.
    -   **Segurança de API**: CSRF foi desabilitado e CORS foi configurado para permitir acesso de qualquer frontend.
    -   **Criptografia**: O `PasswordEncoder` com BCrypt foi mantido como padrão para hashing de senhas.
    -   **Autorização Granular**: A segurança em nível de método foi habilitada com `@EnableMethodSecurity(prePostEnabled = true)`.

### 2.2. Implementação do JWT (`ATIVIDADE 2`)

-   **`TokenService`**: Um novo serviço de JWT foi implementado para substituir a lógica anterior.
    -   **Biblioteca**: A dependência foi atualizada para `com.auth0:java-jwt`, uma biblioteca moderna e segura.
    -   **Claims Customizados**: O token gerado agora inclui `userId`, `role`, e `restauranteId` para permitir que o backend tome decisões de autorização sem consultas extras ao banco.
    -   **Expiração**: O tempo de expiração do token foi configurado para 24 horas.
    -   **Segurança da Chave**: A chave secreta do JWT foi externalizada e agora é lida de uma variável de ambiente (`api.security.token.secret`), uma prática de segurança essencial.
-   **`SecurityFilter`**:
    -   Este filtro intercepta todas as requisições, extrai o token JWT do header `Authorization` e o valida.
    -   Se o token for válido, o usuário é carregado no `SecurityContextHolder`, autenticando a sessão para a requisição atual.
    -   Exceções de token inválido ou expirado são tratadas adequadamente, retornando `401 Unauthorized`.

### 2.3. Endpoints de Autenticação (`ATIVIDADE 3`)

-   **DTOs**: Foram criados Data Transfer Objects (DTOs) específicos para as operações de autenticação:
    -   `LoginRequest`, `LoginResponse`, `RegisterRequest`, e `UserResponse`.
-   **`AuthenticationController`**:
    -   O controller foi movido para o endpoint `/api/auth`.
    -   **`POST /api/auth/login`**: Autentica o usuário e retorna um `LoginResponse` com o token JWT e dados do usuário.
    -   **`POST /api/auth/register`**: Permite o registro de novos usuários, validando se o email já existe e criptografando a senha.
    -   **`GET /api/auth/me`**: Um endpoint protegido que retorna os dados do usuário atualmente autenticado.

### 2.4. Autorização por Perfis (`ATIVIDADE 4`)

-   **Autorização Granular**: A segurança em nível de método foi implementada com as anotações `@PreAuthorize` nos controllers.
    -   `RestauranteController`, `ProdutoController`, e `PedidoController` foram anotados para restringir o acesso com base nos `roles` (`ADMIN`, `RESTAURANTE`, `CLIENTE`), seguindo as regras de negócio.
-   **Serviços de Autorização**:
    -   Foram implementados os métodos `isOwner` e `canAccess` nos serviços para permitir a verificação de propriedade de um recurso. Por exemplo, um usuário com `role` `RESTAURANTE` só pode editar produtos que pertencem ao seu restaurante.
    -   Um utilitário `SecurityUtils` foi criado para facilitar o acesso aos dados do usuário logado.

### 2.5. Configuração e Testes

-   **Dados de Teste**: Um arquivo `data.sql` foi adicionado para popular o banco de dados com usuários de teste para cada perfil, facilitando a validação da API.
-   **Preservação da Lógica de Negócio**: Um esforço considerável foi feito para restaurar e garantir que toda a lógica de negócio original dos serviços (`RestauranteService`, `ProdutoService`, `PedidoService`) fosse preservada após a integração da camada de segurança.

## 3. Conclusão

A implementação atendeu a todos os requisitos funcionais e de segurança descritos no documento. A API de delivery agora possui um sistema de autenticação JWT completo e autorização granular baseada em perfis, garantindo que os dados estejam protegidos e que cada usuário tenha acesso apenas às funcionalidades permitidas para o seu perfil. O código está mais seguro, robusto e alinhado com as melhores práticas de desenvolvimento de APIs REST.
