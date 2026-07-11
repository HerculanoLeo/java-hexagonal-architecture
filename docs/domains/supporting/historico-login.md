# Histórico de Logins

## Classificação

- **DDD:** Supporting
- **Camada:** backend (`domain-security`) + BFF (detecção no login) + UI admin

## Responsabilidade

Auditoria de **logins bem-sucedidos**: quem autenticou, quando, IP, user-agent, tipo de acesso e tenant (
`relacionadoId`).

O BFF (better-auth) detecta a criação da sessão após OAuth Keycloak e registra no backend Java. Persistência no schema
PostgreSQL `security`.

## Regras de negócio

- v1: apenas sucessos (sem logins falhos / eventos Keycloak).
- Identidade, `TipoAcesso` e `relacionadoId` vêm do JWT no backend — não confiar no body para esses campos.
- Body do POST: metadados de rede/sessão (`ip`, `userAgent`, `sessaoBffId`) e snapshot opcional `email`/`nome`.
- Dedupe por `id_sessao_bff` (idempotente).
- Backoffice (`ST`): `id_relacionado` tipicamente `NULL`; `id_usuario` preenchido quando existir em
  `tb_sistema_usuario`.
- Filtro futuro por transportadora: `tipo` + `id_relacionado` (sem join obrigatório com cadastro de tenant).
- Falha no POST **não** bloqueia o login no BFF.

## Estrutura de código

```
backend/domain-security/src/main/java/com/lodh8/starter/security/historico/
  api/controller/HistoricoLoginController.java
  app/impl/HistoricoLoginServiceImpl.java
  infra/persistence/HistoricoLoginEntity.java
backend/domain-security/src/main/resources/db/changelog/security/
frontend/gestao/src/entities/historico-login/
frontend/gestao/src/shared/auth/auth.configuration.ts  # databaseHooks.session.create.after
```

## Onde encontrar

| Artefato   | Path                                                                                   |
|------------|----------------------------------------------------------------------------------------|
| Módulo     | `backend/domain-security`                                                              |
| Controller | `.../HistoricoLoginController.java`                                                    |
| Liquibase  | `domain-security/.../db/changelog/security/` (schema `security`, `tb_historico_login`) |
| Entity FE  | `frontend/gestao/src/entities/historico-login/`                                               |
| Hook login | `auth.configuration.ts` → `databaseHooks.session.create.after`                         |
| Page       | `HistoricoLoginsListPage`                                                              |
| Rota       | `/administracao/seguranca/historico-logins`                                            |
| Role       | `seguranca-historico-login-sistemas` (`SISTEMAS.SEGURANCA_HISTORICO_LOGINS`)           |

## Contratos

### REST (`/api/v1/seguranca/historico-logins`)

| Método | Auth                                                     | Uso                |
|--------|----------------------------------------------------------|--------------------|
| `POST` | JWT autenticado                                          | BFF registra login |
| `GET`  | `admin-sistemas` ou `seguranca-historico-login-sistemas` | Listagem admin     |

Filtros GET: `dataEventoDe`, `dataEventoAte`, `email`, `tipo`, `relacionadoId`, `usuarioId`.

### Claims (snapshot no login)

| Claim                       | Coluna           | Exemplo                      |
|-----------------------------|------------------|------------------------------|
| `application.type`          | `tipo`           | `ST`, `CL`                   |
| `application.relacionadoId` | `id_relacionado` | UUID da transportadora / org |

## Dependências

- `domain-authorize` (`OauthUserProviderPort`, `OAuthUser`)
- `domain-backoffice` (`UsuarioSistemaService` — vínculo opcional `id_usuario`)
- BFF: better-auth session + `getKeycloakAccessToken`

## Fora de escopo (v1)

- Logins falhos
- Eventos Keycloak
- Persistência Prisma do histórico
- Seletor rico de tenant / CRUD transportadora

## Referências

- [bff/session.md](../bff/session.md) — sessões ativas (complementar)
- [sessao.md](sessao.md) — invalidação Keycloak (agnóstica a tipo/tenant)
- [roadmap/multi-tenant.md](../roadmap/multi-tenant.md) — `TipoAcesso` + `relacionadoId`
