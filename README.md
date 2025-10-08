
# 🍕 DeliveryTech API

> **API REST completa para sistema de delivery desenvolvida com Spring Boot e Java 21**

Uma API robusta e moderna para gerenciamento de delivery, com documentação automática, validações avançadas e tratamento de erros profissional.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

A **DeliveryTech API** é um sistema completo de delivery que permite gerenciar clientes, restaurantes, produtos e pedidos. Desenvolvida seguindo as melhores práticas de desenvolvimento, a API oferece:

- 🏗️ **Arquitetura em Camadas**: Separação clara entre controllers, services e repositories
- 📚 **Documentação Automática**: Interface Swagger para teste e documentação
- ✅ **Validações Robustas**: Validação automática de dados com Bean Validation
- 🛡️ **Tratamento de Erros**: Respostas padronizadas e informativas
- 🔐 **Preparada para Segurança**: Infraestrutura para autenticação JWT
- 🧪 **Testes Automatizados**: Cobertura de testes para garantir qualidade

## 🚀 Tecnologias e Ferramentas

### Core
- **Java 21 LTS** - Linguagem principal com recursos modernos
- **Spring Boot 3.2.2** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança (preparado para JWT)
- **Maven** - Gerenciamento de dependências

### Documentação e Validação
- **SpringDoc OpenAPI 3** - Documentação automática
- **Bean Validation** - Validação de dados
- **Swagger UI** - Interface interativa da API

### Banco de Dados
- **H2 Database** - Banco em memória para desenvolvimento
- **Hibernate** - ORM para mapeamento objeto-relacional

### Testes
- **JUnit 5** - Framework de testes
- **Spring Boot Test** - Testes de integração
- **Mockito** - Mocks para testes unitários

## ⚡ Recursos Modernos do Java 21

- **Records** - Estruturas de dados imutáveis
- **Text Blocks** - Strings multilinha
- **Pattern Matching** - Correspondência de padrões
- **Virtual Threads** - Threads leves e eficientes
- **Switch Expressions** - Expressões switch modernas

## 🏗️ Arquitetura

```
src/main/java/com/deliverytech/delivery/
├── 🎮 controller/          # Endpoints REST
├── 🔧 service/            # Lógica de negócio
├── 📊 repository/         # Acesso a dados
├── 🏛️ model/              # Entidades JPA
├── 📋 dto/               # Objetos de transferência
├── ⚙️ config/            # Configurações
├── ✅ validation/        # Validadores customizados
└── 🛡️ exception/         # Tratamento de exceções
```

## 🏃‍♂️ Como Executar

### Pré-requisitos
- **JDK 21** ou superior
- **Maven 3.9+** (ou use o wrapper incluído)
- **Git** para clonar o repositório

### Passos para Execução

1. **Clone o repositório**
   ```bash
   git clone https://github.com/guilherme-machado-ceo/-delivery-api-guilhermegoncalvesmachado.git
   cd delivery-api-guilhermegoncalvesmachado
   ```

2. **Execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```
   
3. **Acesse a aplicação**
   - **API**: http://localhost:8081
   - **Swagger UI**: http://localhost:8081/swagger-ui.html
   - **H2 Console**: http://localhost:8081/h2-console
   - **Health Check**: http://localhost:8081/health

### Executar Testes
```bash
./mvnw test
```

## 📋 Endpoints da API

### 👥 Clientes
- `POST /api/clientes` - Criar cliente
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Inativar cliente
- `GET /api/clientes/ativos` - Listar clientes ativos

### 🏪 Restaurantes
- `POST /api/restaurantes` - Criar restaurante
- `GET /api/restaurantes` - Listar restaurantes
- `GET /api/restaurantes/{id}` - Buscar restaurante por ID
- `PUT /api/restaurantes/{id}` - Atualizar restaurante
- `PATCH /api/restaurantes/{id}/status` - Alterar status
- `GET /api/restaurantes/categoria/{categoria}` - Buscar por categoria

### 🍔 Produtos
- `POST /api/produtos/restaurante/{restauranteId}` - Criar produto
- `GET /api/produtos/{id}` - Buscar produto por ID
- `GET /api/produtos/restaurante/{restauranteId}` - Produtos do restaurante
- `PUT /api/produtos/{id}` - Atualizar produto
- `PATCH /api/produtos/{id}/disponibilidade` - Alterar disponibilidade

### 📦 Pedidos
- `POST /api/pedidos` - Criar pedido
- `GET /api/pedidos/{id}` - Buscar pedido por ID
- `GET /api/pedidos/cliente/{clienteId}` - Pedidos do cliente
- `PATCH /api/pedidos/{id}/status` - Atualizar status do pedido
- `GET /api/pedidos/periodo` - Pedidos por período

### 🔧 Sistema
- `GET /health` - Status da aplicação
- `GET /info` - Informações da aplicação

## 🔧 Configuração

### Banco de Dados H2
- **URL**: `jdbc:h2:mem:deliverydb`
- **Usuário**: `sa`
- **Senha**: *(vazia)*
- **Console**: http://localhost:8081/h2-console

### Profiles Disponíveis
- **default** - Desenvolvimento (porta 8081)
- **test** - Testes automatizados
- **prod** - Produção (configurar conforme necessário)

## 📊 Modelos de Dados

### Cliente
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "11999999999",
  "endereco": "Rua das Flores, 123 - São Paulo/SP",
  "ativo": true,
  "dataCadastro": "2025-10-08T15:30:00"
}
```

### Restaurante
```json
{
  "id": 1,
  "nome": "Pizzaria do João",
  "categoria": "Italiana",
  "endereco": "Rua X, 100 - São Paulo/SP",
  "taxaEntrega": 5.00,
  "avaliacao": 4.5,
  "ativo": true
}
```

### Produto
```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "descricao": "Molho, mussarela, tomate e manjericão",
  "preco": 45.00,
  "categoria": "Pizza",
  "disponivel": true,
  "restaurante": { "id": 1 }
}
```

### Pedido
```json
{
  "id": 1,
  "cliente": { "id": 1 },
  "restaurante": { "id": 1 },
  "dataPedido": "2025-10-08T15:30:00",
  "status": "CONFIRMADO",
  "valorTotal": 50.00,
  "enderecoCoberto": "Rua das Flores, 123",
  "observacoes": "Sem cebola"
}
```

## ✅ Validações Implementadas

- **Email**: Formato válido e único
- **Telefone**: 10 ou 11 dígitos
- **Preços**: Valores positivos
- **Campos obrigatórios**: Validação automática
- **Tamanhos**: Limitação de caracteres
- **Relacionamentos**: Integridade referencial

## 🛡️ Tratamento de Erros

A API retorna erros padronizados com informações detalhadas:

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
      "rejectedValue": "email-invalido",
      "message": "Email deve ter um formato válido"
    }
  ]
}
```

## 🧪 Testes

O projeto inclui testes automatizados para garantir a qualidade:

- **Testes de Repositório**: Validação de consultas JPA
- **Testes de Integração**: Verificação de endpoints
- **Cobertura**: Funcionalidades principais testadas

```bash
# Executar todos os testes
./mvnw test

# Executar com relatório de cobertura
./mvnw test jacoco:report
```

## 📚 Documentação

### Swagger UI
Acesse http://localhost:8081/swagger-ui.html para:
- 📖 Visualizar todos os endpoints
- 🧪 Testar a API interativamente
- 📋 Ver exemplos de request/response
- 🔍 Explorar modelos de dados

### Relatórios Técnicos
- [📋 Relatório de Implementação](RELATORIO_IMPLEMENTACAO.md)
- [🔄 Relatório de Atualização](RELATORIO_ATUALIZACAO.md)
- [⭐ Relatório de Melhorias](RELATORIO_MELHORIAS.md)

## 🚀 Próximos Passos

- [ ] Implementação completa de JWT
- [ ] Testes unitários para controllers
- [ ] Cache com Redis
- [ ] Métricas com Actuator
- [ ] Deploy em container Docker
- [ ] Integração com banco PostgreSQL

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Desenvolvedor

**Guilherme Gonçalves Machado**
- 🎓 Arquitetura de Sistemas - Turma TI 03362
- 💼 Desenvolvido com Java 21 e Spring Boot 3.2.x
- 📧 Email: guilherme.ceo@hubtsyr.com
- 🔗 LinkedIn: https://www.linkedin.com/in/guilhermegoncalvesmachado/

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**

*Desenvolvido com ❤️ usando Java 21 e Spring Boot*