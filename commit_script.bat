@echo off
echo Aguardando liberacao do git lock...
:retry
git add -A 2>nul
if %errorlevel% neq 0 (
    timeout /t 2 /nobreak >nul
    goto retry
)

echo Fazendo commit...
git commit -m "feat: Implementacoes completas do sistema DeliveryTech - Outubro 2025

Principais implementacoes:

Backend:
- Sistema de gestao de restaurantes aprimorado com DTOs padronizados
- Enum CategoriaRestaurante com 12 categorias e metadados
- RestauranteService refatorado com busca avancada
- TaxaEntregaService para calculo inteligente de taxa de entrega
- Repository otimizado com 15+ queries customizadas
- Correcoes de seguranca JWT (atualizacao para API mais recente)

Frontend:
- Pagina de gestao de restaurantes completa (Restaurantes.tsx)
- Servicos de integracao com API (restauranteService.ts)
- Tipos TypeScript completos e configuracao otimizada
- Correcoes de imports React em todos os componentes

Correcoes tecnicas:
- Atualizacao JwtTokenProvider para nova API JJWT
- Correcao de anotacoes @NonNull no JwtAuthenticationFilter
- Remocao de imports nao utilizados no UserPrincipal
- Validacoes Bean Validation em todos os DTOs

Metricas:
- 15+ novos arquivos criados
- 20+ arquivos modificados
- ~2000 linhas de codigo adicionadas
- 8+ novos endpoints API

Roadmap definido para proximas fases ate Janeiro 2026"

echo Fazendo push...
git push origin master

echo Commit e push concluidos!
pause