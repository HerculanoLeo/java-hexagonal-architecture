# Auth (BFF)

## Classificação

- **DDD:** Generic (cross-cutting) + Core (permissões)
- **Camada:** BFF

## Responsabilidade

Login OAuth via Keycloak (better-auth), sessão, guards de rota, mapeamento de roles JWT para permissões UI (`SISTEMAS`).

## Regras de negócio

- Rotas autenticadas exigem sessão válida (`beforeLoad` guards).
- Roles extraídas do token Keycloak (`realm_access`, `groups`).
- Admin routes verificam enum `SISTEMAS` (ex.: `USUARIOS`, `GRUPOS`, `CONFIGURACAO`).
- **`admin-sistemas` (`SISTEMAS.ADMIN`)**: super user da plataforma (Usuário principal). `hasRole` / `isPlatformAdmin`
  concedem qualquer permissão de UI/menu/rota. A role **não** é atribuível via grupo no frontend (
  `filterAssignableSistemaRoles`).
- Handler OAuth: `/api/auth/$` (better-auth).

## Onde encontrar

| Artefato           | Path                                                    |
|--------------------|---------------------------------------------------------|
| Config better-auth | `frontend/gestao/src/shared/auth/auth.configuration.ts`        |
| Session server     | `frontend/gestao/src/shared/auth/auth.session.server.ts`       |
| Guards             | `frontend/gestao/src/shared/auth/auth-guards.server-fn.ts`     |
| Permissões         | `frontend/gestao/src/shared/auth/auth-permissions.enum.ts`     |
| API handler        | `frontend/gestao/src/routes/api/auth/$.ts`                     |
| Login page         | `frontend/gestao/src/routes/(unauthenticated)/login.tsx`       |
| Guard UI           | `frontend/gestao/src/components/features/auth/GuardComponent/` |

## Contratos

- Enum `SISTEMAS` alinhado a roles Keycloak/backend
- `AuthenticatedContext`, `KeycloakTokenClaims`

## Dependências

- Keycloak (externo), Prisma (sessões better-auth)
- Consumido por todos os domínios autenticados

## Testes

- `shared/auth/*.test.ts` (guards, permissions, token)

## Implementação futura

- Roles 100% do token server-side (sem depender de `me()` para menu)
- Multi-tenant: contexto de organização ativa no token/sessão
