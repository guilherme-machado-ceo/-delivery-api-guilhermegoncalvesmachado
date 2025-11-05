# 🧪 Guia de Execução de Testes - DeliveryTech API

Este documento fornece instruções completas para executar os testes unitários e de integração da API DeliveryTech.

## 📋 **Índice**

- [Pré-requisitos](#pré-requisitos)
- [Estrutura de Testes](#estrutura-de-testes)
- [Comandos de Execução](#comandos-de-execução)
- [Relatórios de Cobertura](#relatórios-de-cobertura)
- [Estratégia de Testes](#estratégia-de-testes)
- [Troubleshooting](#troubleshooting)

## 🔧 **Pré-requisitos**

- **Java 21** ou superior
- **Maven 3.8+**
- **Git** para versionamento

## 📁 **Estrutura de Testes**

```
src/test/java/
├── com/deliverytech/delivery/
│   ├── config/
│   │   └── TestConfig.java                    # Configurações específicas de teste
│   ├── controller/
│   │   ├── ClienteControllerIT.java           # Testes de integração - Cliente
│   │   └── PedidoControllerIT.java            # Testes de integração - Pedido
│   ├── service/
│   │   ├── ClienteServiceTest.java            # Testes unitários - ClienteService
│   │   └── PedidoServiceTest.java             # Testes unitários - PedidoService
│   └── util/
│       ├── ClienteTestData.java               # Dados de teste - Cliente
│       ├── PedidoTestData.java                # Dados de teste - Pedido
│       ├── ProdutoTestData.java               # Dados de teste - Produto
│       └── RestauranteTestData.java           # Dados de teste - Restaurante
└── resources/
    ├── application-test.properties            # Configurações de teste
    └── test-data/
        └── test-schema.sql                    # Schema para testes
```

## ⚡ **Comandos de Execução**

### **Executar Todos os Testes**
```bash
mvn test
```

### **Executar Testes com Relatório de Cobertura**
```bash
mvn clean test jacoco:report
```

### **Executar Testes Específicos**

#### Testes Unitários Apenas
```bash
mvn test -Dtest="*Test"
```

#### Testes de Integração Apenas
```bash
mvn test -Dtest="*IT"
```

#### Teste Específico por Classe
```bash
mvn test -Dtest=ClienteServiceTest
mvn test -Dtest=PedidoServiceTest
mvn test -Dtest=ClienteControllerIT
```

#### Teste Específico por Método
```bash
mvn test -Dtest=ClienteServiceTest#should_SaveCliente_When_ValidDataProvided
mvn test -Dtest=PedidoServiceTest#should_CreatePedido_When_ValidProductsProvided
```

### **Executar com Perfil de Teste Específico**
```bash
mvn test -Dspring.profiles.active=test
```

### **Executar com Logs Detalhados**
```bash
mvn test -Dlogging.level.com.deliverytech=DEBUG
```

### **Verificar Cobertura Mínima**
```bash
mvn clean test jacoco:check
```

## 📊 **Relatórios de Cobertura**

### **Gerar Relatório HTML**
```bash
mvn clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

### **Visualizar Relatório**
1. Execute o comando acima
2. Abra o arquivo `target/site/jacoco/index.html` no navegador
3. Navegue pelas classes para ver detalhes de cobertura

### **Meta de Cobertura**
- **Mínimo exigido:** 80% de cobertura de linha nos serviços
- **Classes excluídas:** DTOs, Models, Configurações, Application main

### **Interpretando o Relatório**
- 🟢 **Verde:** Linhas cobertas pelos testes
- 🔴 **Vermelho:** Linhas não cobertas
- 🟡 **Amarelo:** Linhas parcialmente cobertas

## 🎯 **Estratégia de Testes**

### **Testes Unitários**
- **Objetivo:** Testar lógica de negócio isolada
- **Ferramentas:** JUnit 5, Mockito, AssertJ
- **Cobertura:** Services e componentes de negócio
- **Isolamento:** Mocks para dependências externas

#### **Padrões Utilizados:**
- **Nomenclatura:** `should_ExpectedResult_When_Condition`
- **Estrutura:** Given-When-Then (AAA Pattern)
- **Mocks:** `@Mock` para dependências, `@InjectMocks` para classe testada

### **Testes de Integração**
- **Objetivo:** Testar comportamento completo da API
- **Ferramentas:** Spring Boot Test, TestRestTemplate, H2
- **Cobertura:** Controllers e fluxo completo
- **Isolamento:** Banco H2 em memória, `@DirtiesContext`

#### **Validações Realizadas:**
- Códigos de status HTTP (200, 201, 400, 404, 409)
- Estrutura JSON das respostas
- Persistência no banco de dados
- Headers HTTP corretos

### **Dados de Teste**
- **TestData Classes:** Builders para criação de dados consistentes
- **Cenários:** Dados válidos, inválidos e casos extremos
- **Isolamento:** Cada teste usa dados independentes

## 🔍 **Configurações de Teste**

### **Banco de Dados**
- **Tipo:** H2 em memória
- **URL:** `jdbc:h2:mem:testdb`
- **Configuração:** `application-test.properties`
- **Isolamento:** Dados limpos entre testes

### **Perfil de Teste**
- **Profile:** `test`
- **Segurança:** Desabilitada para testes
- **Cache:** Configuração simplificada
- **Logs:** Nível DEBUG para debugging

### **Configurações Específicas**
```properties
# H2 Database para testes
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Logs detalhados
logging.level.com.deliverytech=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Segurança desabilitada
spring.security.enabled=false
```

## 🚨 **Troubleshooting**

### **Problemas Comuns**

#### **Testes Falhando por Dependências**
```bash
# Limpar e reinstalar dependências
mvn clean install -DskipTests
mvn test
```

#### **Erro de Conexão com Banco**
- Verificar se H2 está nas dependências
- Confirmar configurações em `application-test.properties`
- Verificar se perfil `test` está ativo

#### **Testes de Integração Lentos**
- Usar `@DirtiesContext` apenas quando necessário
- Verificar se dados estão sendo limpos corretamente
- Considerar usar `@Transactional` com rollback

#### **Cobertura Baixa**
```bash
# Verificar quais linhas não estão cobertas
mvn clean test jacoco:report
# Abrir target/site/jacoco/index.html
```

#### **Falha na Verificação de Cobertura**
```bash
# Executar apenas o relatório sem verificação
mvn clean test jacoco:report
# Depois verificar manualmente o relatório
```

### **Logs de Debug**
```bash
# Executar com logs detalhados
mvn test -Dlogging.level.com.deliverytech=DEBUG -Dlogging.level.org.springframework=DEBUG
```

### **Executar Teste Individual com Debug**
```bash
mvn test -Dtest=ClienteServiceTest -Dlogging.level.com.deliverytech=TRACE
```

## 📈 **Métricas de Qualidade**

### **Objetivos de Cobertura**
- **Services:** ≥ 80% cobertura de linha
- **Controllers:** ≥ 70% cobertura (via testes de integração)
- **Utilitários:** ≥ 90% cobertura

### **Tipos de Teste**
- **Unitários:** ~60% dos testes (rápidos, isolados)
- **Integração:** ~40% dos testes (completos, realistas)

### **Performance**
- **Testes Unitários:** < 100ms por teste
- **Testes de Integração:** < 2s por teste
- **Suite Completa:** < 30s total

## 🎉 **Boas Práticas**

### **Escrevendo Testes**
1. **Nomes descritivos:** Use convenção `should_When_Then`
2. **Arrange-Act-Assert:** Estruture testes claramente
3. **Um conceito por teste:** Cada teste valida uma coisa
4. **Dados independentes:** Use TestData builders
5. **Mocks mínimos:** Mock apenas dependências externas

### **Manutenção**
1. **Execute testes frequentemente** durante desenvolvimento
2. **Mantenha cobertura alta** mas foque na qualidade
3. **Refatore testes** junto com código de produção
4. **Use testes como documentação** do comportamento esperado

### **CI/CD**
1. **Testes obrigatórios** antes de merge
2. **Relatórios automáticos** de cobertura
3. **Falha de build** se cobertura < 80%
4. **Notificações** de testes falhando

---

## 📞 **Suporte**

Para dúvidas sobre os testes:
1. Consulte este README
2. Verifique os logs de execução
3. Analise o relatório de cobertura
4. Consulte a documentação do Spring Boot Testing

**Comandos de Referência Rápida:**
```bash
# Execução básica
mvn test

# Com cobertura
mvn clean test jacoco:report

# Teste específico
mvn test -Dtest=ClienteServiceTest

# Com logs detalhados
mvn test -Dlogging.level.com.deliverytech=DEBUG
```

---

*Documentação atualizada em Outubro 2025*  
*DeliveryTech API - Sistema de Testes v2.0*