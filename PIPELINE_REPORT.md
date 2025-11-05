# Pipeline CI/CD - DeliveryTech API

Data: 2025-11-05

## Objetivo
Descrever o pipeline configurado (GitHub Actions) que compila, testa, empacota a aplicação, constrói a imagem Docker, e pode fazer deploy automático para um ambiente de homologação via SSH.

## Arquivo de configuração
- Local: `.github/workflows/ci-cd.yml`
- Gatilhos: `push` e `pull_request` nas branches `master` e `main`.

## Jobs e etapas
1. build-and-test
   - Checkout do código
   - Setup JDK 23 (Temurin)
   - Cache do repositório Maven (~/.m2)
   - mvn clean verify (executa testes unitários e de integração configurados)
   - mvn package (gera o JAR)
   - Upload do artefato (target/*.jar) como artifact do workflow

2. docker-image
   - Executa mesmo que o job anterior falhe (condição `if: always()` para facilitar debug do build)
   - Usa buildx para construir a imagem Docker (o repositório já possui um `Dockerfile` multi-stage)
   - Se os secrets `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` estiverem configurados, faz login e push da imagem para o Docker Hub com a tag `delivery-api:${{ github.sha }}`

3. deploy-to-test (opcional — depende de secrets)
   - Executa somente se os secrets `DEPLOY_HOST`, `DEPLOY_USER` e `SSH_PRIVATE_KEY` estiverem definidos
   - Usa `webfactory/ssh-agent` para carregar a chave e executar um comando `ssh` que puxa a imagem do Docker Hub e a executa no host remoto

4. smoke-test (opcional)
   - Executa um teste simples de disponibilidade consultando `/actuator/health` no host de homologação

## Secrets necessários (opcionais para deploy)
- `DOCKERHUB_USERNAME` — usuário Docker Hub (para push automático)
- `DOCKERHUB_TOKEN` — token/senha do Docker Hub
- `DEPLOY_HOST` — IP ou host do servidor de homologação
- `DEPLOY_USER` — usuário SSH do servidor
- `SSH_PRIVATE_KEY` — chave privada SSH (no formato PEM) para conectar ao servidor

> Observação: sem esses secrets o pipeline ainda roda as etapas de build e testes; o push e o deploy são condicionais.

## Como monitorar a execução
1. Acesse o repositório no GitHub.
2. Clique na aba "Actions".
3. Selecione o workflow "CI/CD".
4. Você verá as execuções recentes com status (success/failure).
5. Clique em uma execução para ver logs detalhados por job e etapa.
6. Artefatos produzidos (jar) aparecem na seção "Artifacts" da execução; faça o download para inspeção.

## Como validar o deploy
- Se o deploy for habilitado, o job `smoke-test` faz chamadas HTTP para `http://<DEPLOY_HOST>:8080/actuator/health`.
- Você também pode acessar `http://<DEPLOY_HOST>:8080/swagger-ui.html` (se Swagger estiver configurado) e o H2 Console caso tenha sido exposto (não recomendado em prod).

## Logs / Prints
- Não é possível executar GitHub Actions a partir deste ambiente local e coletar logs aqui.
- Depois de abrir o workflow no GitHub e disparar um push, a execução e logs estarão disponíveis em: `https://github.com/<your-org-or-user>/<repo>/actions`.

## Observações e próximos passos
- Recomendado configurar um registro privado (Docker Hub, GHCR) com permissões e criar secrets correspondentes.
- Para deploy mais robusto, usar ferramentas como Ansible, Terraform ou Kubernetes (Helm) para orquestração.
- Incluir etapas de security scan (Snyk, Trivy) e code-quality (SonarCloud).

## Arquivos adicionados
- `.github/workflows/ci-cd.yml` — workflow do GitHub Actions
- `PIPELINE_REPORT.md` — este relatório

## Reproduzir localmente (quick)
1. Build e testes localmente:

```bash
./mvnw clean verify
```

2. Build da imagem localmente:

```bash
docker build -t delivery-api:local .
```

3. Rodar contêiner local:

```bash
docker run -p 8080:8080 delivery-api:local
```

---

Se quiser, eu posso:  
- Adicionar um job que publica a imagem no GitHub Container Registry (GHCR) em vez do Docker Hub;  
- Implementar um deploy via SSH com um script mais completo (stop/start, backup de dados);  
- Adicionar exemplos de secrets e um README com passo-a-passo para configurar o ambiente no GitHub.  
Diga qual opção prefere e eu continuo.