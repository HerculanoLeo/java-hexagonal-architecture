# Session — Usuários Online

## Classificação

- **DDD:** Supporting (segurança operacional)
- **Camada:** BFF + backend (`domain-security` para Keycloak)

## Responsabilidade

Listar sessões ativas (better-auth), agrupadas por usuário, e revogar sessão individual ou todas de um usuário.

## Regras de negócio

- Dados lidos da tabela Prisma `Session` (não do backend Java).
- Exibe IP, user-agent, timestamps.
- Revogação: invalida sessões Keycloak do usuário (logout global no IdP via `domain-security`) e remove a(s) sessão(ões)
  better-auth no Prisma; limpa tokens em `Account`.
- Mapeamento BFF → Keycloak: `Account.accountId` (`providerId = keycloak`).
- Semântica Keycloak: Admin API remove **todas** as sessões SSO do usuário (não há id de sessão Keycloak persistido no
  BFF).
- Acesso exige `SISTEMAS.SEGURANCA_USUARIOS_ONLINE`.

## Onde encontrar

| Artefato      | Path                                                         |
|---------------|--------------------------------------------------------------|
| Entity        | `frontend/src/entities/session/`                             |
| Server fn     | `usuarios-online.server-fn.ts`                               |
| Repository    | `usuarios-online.repository.ts`                              |
| Keycloak sync | `keycloak-session.server.ts`                                 |
| Backend       | `DELETE /seguranca/sessoes/{identityId}` (`domain-security`) |
| Prisma        | `Session`, `User`, `Account` models                          |
| Page          | `UsuariosOnlineListPage`                                     |
| Rota          | `/administracao/seguranca/usuarios-online`                   |

## Contratos

- `UsuarioOnlineGroupDto`, `UsuarioOnlineSessionDto`
- `revokeSessionSchema`, `revokeAllSessionsSchema`
- Backend: `DELETE /api/v1/seguranca/sessoes/{identityId}` (roles: `admin-sistemas`,
  `seguranca-usuarios-ativos-sistemas`)

## Dependências

- Auth (guard), Prisma, API cadastros (`fetchCadastros`), `domain-security` / `domain-identity`

## Testes

- Schemas: `revoke-session.schema.test.ts`, `revoke-all-sessions.schema.test.ts`
- Backend: `SessaoControllerTest`, `SessaoServiceImplTest`

## Relacionado

- Invalidação Keycloak — [supporting/sessao.md](../supporting/sessao.md)
- Histórico de logins — [supporting/historico-login.md](../supporting/historico-login.md)
- Identity Keycloak — [core/iam/identity-keycloak.md](../core/iam/identity-keycloak.md)
