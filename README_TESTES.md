# Guia de Execução de Testes - DeliveryTech API

## 📋 Visão Geral

Este documento fornece instruções completas para executar os testes unitários e de integração da API DeliveryTech, incluindo análise de cobertura de código e geração de relatórios.

## 🏗️ Estrutura de Testes

### Tipos de Teste Implementados

- **Testes Unitários**: Isolam a lógica de negócio usando mocks
- **Testes de Integração**: Validam comportamento end-to-end da API
- **Cobertura de Código**: Análise com JaCoCo (meta: 80% nos serviços)

### Estrutura de Diretórios

```
src/test/
├── java/com/deliverytech/delivery/
│   ├── service/                    # Testes unitários dos serviços
│   │   ├── ClienteServiceTest.java
│   │   └── PedidoServiceTest.java
│   ├── controller/                 # Testes de integração dos controladores
│   │   ├── ClienteControllerIT.java
│   │   └── PedidoControllerIT.java
│   ├── util/                       # Utilitários e dados de teste
│   │   ├── ClienteTestData.java
│   │   └── PedidoTestData.java
│   └── config/                     # Configurações específicas de teste
│       └── TestConfig.java
└── resources/
    ├── application-test.properties  # Configurações de teste
    ├── logback-test.xml            # Configuração de logging
    └── test-data/                  # Scripts SQL e dados de teste
        ├── test-schema.sql
        └── cleanup.sql
```

## 🚀 Comandos de Execução

### Executar Todos os Testes

```bash
# Executar todos os testes com relatório de cobertura
mvn clean test jacoco:report

# Executar apenas testes unitários
mvn test -Dtest="**/*Test"

# Executar apenas testes de integração
mvn test -Dtest="**/*IT"
```

### Executar Testes Específicos

```bash
# Executar testes de um serviço específico
mvn test -Dtest=ClienteServiceTest

# Executar testes de um controlador específico
mvn test -Dtest=ClienteControllerIT

# Executar método específico
mvn test -Dtest=ClienteServiceTest#should_SaveCliente_When_ValidDataProvided
```

### Executar com Perfil de Teste

```bash
# Forçar uso do perfil de teste
mvn test -Dspring.profiles.active=test

# Executar com logging detalhado
mvn test -Dlogging.level.com.deliverytech=DEBUG
```

### Verificar Cobertura de Código

```bash
# Executar testes e verificar se cobertura atende critério (80%)
mvn clean test jacoco:check

# Gerar apenas relatório de cobertura (após executar testes)
mvn jacoco:report
```

## 📊 Relatórios de Cobertura

### Localização dos Relatórios

Após executar `mvn clean test jacoco:report`, os relatórios são gerados em:

```
target/site/jacoco/
├── index.html              # Relatório principal
├── jacoco.xml              # Dados XML para CI/CD
└── jacoco.csv              # Dados CSV para análise
```

### Visualizar Relatórios

1. **Relatório HTML**: Abra `target/site/jacoco/index.html` no navegador
2. **Métricas por Classe**: Navegue pelas packages para ver detalhes
3. **Linhas Não Cobertas**: Código destacado em vermelho

### Metas de Cobertura

- **Serviços**: Mínimo 80% de cobertura de linha
- **Controladores**: Mínimo 70% de cobertura de linha
- **DTOs**: Mínimo 60% de cobertura de linha
- **Classes Excluídas**: Configurações, Application main, Models

## 🔧 Configuração do Ambiente

### Dependências Necessárias

As seguintes dependências já estão configuradas no `pom.xml`:

- JUnit 5 (jupiter)
- Mockito Core e JUnit Jupiter
- Spring Boot Test
- AssertJ (assertions fluentes)
- TestContainers (opcional)
- JaCoCo Maven Plugin

### Banco de Dados de Teste

- **H2 Database**: Banco em memória para testes
- **URL**: `jdbc:h2:mem:testdb`
- **Console H2**: Disponível em `/h2-console` durante testes
- **Auto DDL**: `create-drop` (recria schema a cada execução)

### Perfil de Teste

O arquivo `application-test.properties` configura:

- Banco H2 em memória
- Logging detalhado para debug
- Desabilitação de cache
- Configurações específicas para testes

## 🧪 Estratégia de Testes

### Testes Unitários

**Características:**
- Isolamento completo com mocks
- Foco na lógica de negócio
- Execução rápida (< 100ms por teste)
- Cobertura de cenários positivos e negativos

**Exemplo de Execução:**
```bash
# Executar apenas testes unitários
mvn test -Dtest="**/*Test" -Dtest="!**/*IT"
```

### Testes de Integração

**Características:**
- Contexto Spring completo
- Banco H2 real
- Validação de serialização JSON
- Verificação de códigos HTTP
- Isolamento entre testes com `@DirtiesContext`

**Exemplo de Execução:**
```bash
# Executar apenas testes de integração
mvn test -Dtest="**/*IT"
```

### Nomenclatura de Testes

Seguimos o padrão: `should_ExpectedBehavior_When_StateUnderTest`

**Exemplos:**
- `should_SaveCliente_When_ValidDataProvided()`
- `should_ThrowException_When_EmailAlreadyExists()`
- `should_Return404_When_ClienteNotFound()`

## 🐛 Troubleshooting

### Problemas Comuns

#### 1. Testes Falhando por Dependências

```bash
# Limpar e reinstalar dependências
mvn clean install -DskipTests
mvn test
```

#### 2. Erro de Conexão com Banco H2

Verifique se o `application-test.properties` está correto:
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.jpa.hibernate.ddl-auto=create-drop
```

#### 3. Testes de Integração Lentos

```bash
# Executar apenas testes unitários para desenvolvimento rápido
mvn test -Dtest="**/*Test" -Dtest="!**/*IT"
```

#### 4. Cobertura Abaixo da Meta

```bash
# Ver relatório detalhado
mvn jacoco:report
# Abrir target/site/jacoco/index.html
```

### Logs de Debug

Para debug detalhado durante testes:

```bash
mvn test -Dlogging.level.com.deliverytech=DEBUG -Dlogging.level.org.springframework.test=DEBUG
```

## 📈 Métricas e Qualidade

### Métricas Coletadas

- **Cobertura de Linha**: Percentual de linhas executadas
- **Cobertura de Branch**: Percentual de condicionais testadas
- **Complexidade Ciclomática**: Complexidade dos métodos
- **Tempo de Execução**: Performance dos testes

### Critérios de Qualidade

- ✅ Cobertura ≥ 80% nos serviços
- ✅ Todos os testes passando
- ✅ Tempo total < 30 segundos
- ✅ Isolamento entre testes
- ✅ Nomenclatura consistente

## 🔄 Integração Contínua

### Comandos para CI/CD

```bash
# Pipeline completo
mvn clean test jacoco:report jacoco:check

# Falhar build se cobertura < 80%
mvn clean test jacoco:check

# Gerar relatórios para artifacts
mvn test jacoco:report
```

### Arquivos de Saída para CI

- `target/surefire-reports/`: Relatórios JUnit XML
- `target/site/jacoco/jacoco.xml`: Dados de cobertura XML
- `target/site/jacoco/index.html`: Relatório visual

## 📚 Recursos Adicionais

### Documentação de Referência

- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Maven Plugin Guide](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

### Comandos Úteis de Referência

```bash
# Execução básica
mvn test                                    # Todos os testes
mvn test -Dtest=ClienteServiceTest         # Teste específico
mvn clean test jacoco:report               # Com cobertura

# Perfis e configurações
mvn test -Dspring.profiles.active=test     # Perfil específico
mvn test -DfailIfNoTests=false             # Não falhar se sem testes

# Debug e análise
mvn test -X                                # Debug do Maven
mvn test -Dmaven.surefire.debug            # Debug dos testes
```

---

## 🎯 Resumo Executivo

Este sistema de testes fornece:

- **62+ cenários de teste** cobrindo funcionalidades críticas
- **Cobertura automatizada** com meta de 80%
- **Execução rápida** para feedback imediato
- **Isolamento completo** entre testes
- **Relatórios detalhados** para análise de qualidade

Para execução rápida durante desenvolvimento:
```bash
mvn test jacoco:report
```

Para verificação completa antes de commit:
```bash
mvn clean test jacoco:check
```