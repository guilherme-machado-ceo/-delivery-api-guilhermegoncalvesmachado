# 🤝 Contribuindo para DeliveryTech API

Obrigado por considerar contribuir para o projeto DeliveryTech API! Este documento fornece diretrizes para contribuições.

## 📋 Como Contribuir

### 1. Fork e Clone
```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/SEU-USUARIO/delivery-api-guilhermegoncalvesmachado.git
cd delivery-api-guilhermegoncalvesmachado
```

### 2. Configurar Ambiente
```bash
# Certifique-se de ter Java 21 instalado
java -version

# Execute os testes para verificar se tudo está funcionando
./mvnw test
```

### 3. Criar Branch
```bash
# Crie uma branch para sua feature/correção
git checkout -b feature/nome-da-sua-feature
# ou
git checkout -b fix/nome-da-correcao
```

## 🎯 Tipos de Contribuição

### 🐛 Correção de Bugs
- Descreva o bug claramente
- Inclua passos para reproduzir
- Adicione testes que falham antes da correção
- Implemente a correção
- Verifique se os testes passam

### ✨ Novas Funcionalidades
- Discuta a funcionalidade em uma issue primeiro
- Siga os padrões de arquitetura existentes
- Adicione testes para a nova funcionalidade
- Atualize a documentação se necessário

### 📚 Documentação
- Melhore README.md
- Adicione comentários no código
- Atualize documentação da API
- Corrija erros de digitação

### 🧪 Testes
- Adicione testes unitários
- Melhore cobertura de testes
- Adicione testes de integração

## 📏 Padrões de Código

### Java
- Use Java 21 e recursos modernos
- Siga convenções de nomenclatura Java
- Mantenha métodos pequenos e focados
- Use records quando apropriado

### Spring Boot
- Siga padrões Spring Boot
- Use anotações apropriadas
- Mantenha separação de responsabilidades

### Testes
- Nomes descritivos para testes
- Arrange-Act-Assert pattern
- Mocks quando necessário

## 🏗️ Estrutura do Projeto

```
src/main/java/com/deliverytech/delivery/
├── controller/     # Controllers REST
├── service/       # Lógica de negócio
├── repository/    # Acesso a dados
├── model/         # Entidades JPA
├── dto/          # DTOs
├── config/       # Configurações
├── validation/   # Validadores
└── exception/    # Exceções
```

## ✅ Checklist para Pull Request

- [ ] Código segue os padrões do projeto
- [ ] Testes foram adicionados/atualizados
- [ ] Todos os testes passam
- [ ] Documentação foi atualizada
- [ ] Commit messages são claros
- [ ] Branch está atualizada com master

## 📝 Mensagens de Commit

Use o padrão Conventional Commits:

```
tipo(escopo): descrição

feat(api): adicionar endpoint de busca por categoria
fix(validation): corrigir validação de email
docs(readme): atualizar instruções de instalação
test(service): adicionar testes para ClienteService
```

### Tipos
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `test`: Testes
- `refactor`: Refatoração
- `style`: Formatação
- `chore`: Tarefas de manutenção

## 🧪 Executando Testes

```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=ClienteServiceTest

# Com cobertura
./mvnw test jacoco:report
```

## 📋 Reportando Issues

### 🐛 Bug Report
- Título claro e descritivo
- Passos para reproduzir
- Comportamento esperado vs atual
- Versão do Java e sistema operacional
- Logs relevantes

### 💡 Feature Request
- Descrição clara da funcionalidade
- Justificativa para a adição
- Exemplos de uso
- Possível implementação

## 🔍 Code Review

### Para Revisores
- Seja construtivo e respeitoso
- Foque na qualidade do código
- Verifique testes e documentação
- Teste localmente se necessário

### Para Contribuidores
- Responda aos comentários
- Faça alterações solicitadas
- Mantenha discussão focada no código
- Seja paciente com o processo

## 🚀 Deploy e Release

- Apenas maintainers fazem releases
- Versioning segue Semantic Versioning
- Changelog é atualizado automaticamente
- Deploy é feito via CI/CD

## 📞 Contato

- **Issues**: Use GitHub Issues para bugs e features
- **Discussões**: Use GitHub Discussions para perguntas
- **Email**: Para questões privadas

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a MIT License.

---

**Obrigado por contribuir! 🎉**