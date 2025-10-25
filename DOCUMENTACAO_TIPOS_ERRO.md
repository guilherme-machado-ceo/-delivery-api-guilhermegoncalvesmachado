# Documentação dos Tipos de Erro - DeliveryTech API

## 📋 Visão Geral

Esta documentação descreve todos os tipos de erro padronizados da API DeliveryTech, seguindo o padrão RFC 7807 (Problem Details for HTTP APIs).

## 🏗️ Estrutura Padrão de Erro

Todas as respostas de erro seguem a estrutura padronizada:

```json
{
  "success": false,
  "error": {
    "code": "CODIGO_DO_ERRO",
    "message": "Mensagem principal do erro",
    "details": "Detalhes específicos ou objeto com campos de validação"
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

## 🚨 Códigos HTTP e Tipos de Erro

### 400 Bad Request - Dados Inválidos

#### VALIDATION_ERROR
**Descrição:** Erro de validação em campos de entrada
**Quando ocorre:** Dados não atendem às regras de validação definidas nos DTOs

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Dados de entrada inválidos",
    "details": {
      "nome": "Nome do restaurante é obrigatório",
      "telefone": "Telefone deve estar no formato válido brasileiro",
      "preco": "Preço deve ser maior que zero"
    }
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

#### BUSINESS_RULE_VIOLATION
**Descrição:** Violação de regra de negócio
**Quando ocorre:** Operação não pode ser realizada devido a regras específicas do domínio

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "RESTAURANTE_INATIVO",
    "message": "Restaurante não está disponível para pedidos",
    "details": "Restaurante ID: 123"
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

### 404 Not Found - Recurso Não Encontrado

#### ENTITY_NOT_FOUND
**Descrição:** Entidade solicitada não foi encontrada
**Quando ocorre:** Busca por ID, email ou outros identificadores únicos

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "ENTITY_NOT_FOUND",
    "message": "Restaurante não encontrado com ID: 999",
    "details": null
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

### 409 Conflict - Conflito de Dados

#### DUPLICATE_RESOURCE
**Descrição:** Tentativa de criar recurso que já existe
**Quando ocorre:** Violação de constraints de unicidade

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "EMAIL_DUPLICADO",
    "message": "Já existe um cliente cadastrado com este email: teste@email.com",
    "details": "Email: teste@email.com"
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

### 422 Unprocessable Entity - Regra de Negócio

#### BUSINESS_CONSTRAINT_VIOLATION
**Descrição:** Dados válidos mas que violam regras de negócio complexas
**Quando ocorre:** Validações que dependem de estado ou contexto

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "PEDIDO_NAO_PODE_CANCELAR",
    "message": "Pedido não pode ser cancelado no status atual: EM_PREPARO",
    "details": "Pedido ID: 456, Status: EM_PREPARO"
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

### 500 Internal Server Error - Erro Interno

#### INTERNAL_SERVER_ERROR
**Descrição:** Erro inesperado no servidor
**Quando ocorre:** Exceções não tratadas, problemas de infraestrutura

**Exemplo:**
```json
{
  "success": false,
  "error": {
    "code": "INTERNAL_SERVER_ERROR",
    "message": "Erro interno do servidor",
    "details": "Ocorreu um erro inesperado. Tente novamente mais tarde."
  },
  "timestamp": "2025-10-25T14:30:00"
}
```

## 🔍 Códigos de Erro Específicos

### Validações de Campo

| Código | Descrição | Campo Afetado |
|--------|-----------|---------------|
| `CAMPO_OBRIGATORIO` | Campo obrigatório não informado | Qualquer campo required |
| `VALOR_INVALIDO` | Valor não atende ao formato esperado | Qualquer campo |
| `FORMATO_INVALIDO` | Formato específico não atendido | Email, telefone, CEP |
| `VALOR_FORA_INTERVALO` | Valor fora do range permitido | Números, datas |
| `TAMANHO_INVALIDO` | Tamanho de string inválido | Campos de texto |

### Validações Customizadas

| Código | Descrição | Validador |
|--------|-----------|-----------|
| `CEP_INVALIDO` | CEP não está no formato brasileiro | @ValidCEP |
| `TELEFONE_INVALIDO` | Telefone não é válido para Brasil | @ValidTelefone |
| `HORARIO_INVALIDO` | Horário não está no formato HH:MM-HH:MM | @ValidHorarioFuncionamento |
| `CATEGORIA_INVALIDA` | Categoria não existe no enum | @ValidCategoria |

### Regras de Negócio

| Código | Descrição | Contexto |
|--------|-----------|----------|
| `RESTAURANTE_INATIVO` | Restaurante não aceita pedidos | Criação de pedido |
| `PRODUTO_INDISPONIVEL` | Produto não está disponível | Adição ao pedido |
| `RESTAURANTE_FECHADO` | Fora do horário de funcionamento | Criação de pedido |
| `VALOR_MINIMO_NAO_ATINGIDO` | Pedido abaixo do valor mínimo | Finalização de pedido |

### Conflitos de Dados

| Código | Descrição | Recurso |
|--------|-----------|---------|
| `EMAIL_DUPLICADO` | Email já cadastrado | Cliente |
| `TELEFONE_DUPLICADO` | Telefone já cadastrado | Cliente/Restaurante |
| `RESTAURANTE_DUPLICADO` | Nome de restaurante já existe | Restaurante |
| `PRODUTO_DUPLICADO` | Produto já existe no restaurante | Produto |
| `USUARIO_DUPLICADO` | Username já existe | Usuário |
| `CPF_DUPLICADO` | CPF já cadastrado | Cliente |
| `CNPJ_DUPLICADO` | CNPJ já cadastrado | Restaurante |

## 🧪 Cenários de Teste

### Cenários Obrigatórios Implementados

1. **POST /api/restaurantes com nome vazio → 400**
   - Código: `VALIDATION_ERROR`
   - Campo: `nome`
   - Mensagem: "Nome do restaurante é obrigatório"

2. **POST /api/produtos com preço -10 → 400**
   - Código: `VALIDATION_ERROR`
   - Campo: `preco`
   - Mensagem: "Preço deve ser maior que zero"

3. **GET /api/restaurantes/999 → 404**
   - Código: `ENTITY_NOT_FOUND`
   - Mensagem: "Restaurante não encontrado com ID: 999"

4. **POST /api/pedidos sem itens → 400**
   - Código: `VALIDATION_ERROR`
   - Campo: `itens`
   - Mensagem: "Lista de itens não pode estar vazia"

5. **POST /api/restaurantes com telefone inválido → 400**
   - Código: `VALIDATION_ERROR`
   - Campo: `telefone`
   - Mensagem: "Telefone deve estar no formato válido brasileiro"

## 🔧 Implementação Técnica

### Classes Principais

- **ErrorResponse**: Classe principal para respostas de erro
- **ErrorDetails**: Detalhes específicos do erro
- **FieldError**: Erros de validação de campo
- **GlobalExceptionHandler**: Tratamento centralizado de exceções

### Hierarquia de Exceções

```
RuntimeException
├── BusinessException (400)
├── EntityNotFoundException (404)
├── ValidationException (400)
└── ConflictException (409)
```

### Anotações de Validação

- **Bean Validation**: @NotNull, @NotBlank, @Size, @Min, @Max, @DecimalMin, @DecimalMax
- **Validações Customizadas**: @ValidCEP, @ValidTelefone, @ValidHorarioFuncionamento, @ValidCategoria

## 📖 Guia de Uso para Desenvolvedores

### Como Tratar Erros no Frontend

```javascript
// Exemplo de tratamento no JavaScript/TypeScript
try {
  const response = await fetch('/api/restaurantes', {
    method: 'POST',
    body: JSON.stringify(restauranteData)
  });
  
  if (!response.ok) {
    const errorData = await response.json();
    
    switch (response.status) {
      case 400:
        // Tratar erros de validação
        if (errorData.error.details) {
          Object.keys(errorData.error.details).forEach(field => {
            showFieldError(field, errorData.error.details[field]);
          });
        }
        break;
        
      case 404:
        showMessage('Recurso não encontrado');
        break;
        
      case 409:
        showMessage('Dados já existem no sistema');
        break;
        
      case 500:
        showMessage('Erro interno. Tente novamente mais tarde.');
        break;
    }
  }
} catch (error) {
  showMessage('Erro de conexão');
}
```

### Como Lançar Exceções no Backend

```java
// Exemplos de uso das exceções customizadas

// Entidade não encontrada
throw EntityNotFoundException.restaurante(id);

// Regra de negócio violada
throw BusinessException.restauranteInativo(restauranteId);

// Conflito de dados
throw ConflictException.emailJaExiste(email);

// Validação customizada
throw ValidationException.cepInvalido(cep);
```

## 🚀 Benefícios da Padronização

1. **Consistência**: Todas as respostas de erro seguem o mesmo padrão
2. **Facilidade de Integração**: Clientes da API sabem o que esperar
3. **Debugging**: Códigos específicos facilitam a identificação de problemas
4. **Internacionalização**: Estrutura permite tradução de mensagens
5. **Monitoramento**: Códigos padronizados facilitam métricas e alertas

## 📝 Notas de Implementação

- Todos os erros incluem timestamp no formato ISO 8601
- Mensagens são em português brasileiro
- Códigos de erro são em UPPER_CASE com underscores
- Detalhes podem ser string simples ou objeto complexo
- Logs são gerados automaticamente para todos os erros
- Informações sensíveis nunca são expostas nas respostas

---

**Versão:** 1.0.0  
**Data:** 25 de Outubro de 2025  
**Responsável:** Equipe DeliveryTech