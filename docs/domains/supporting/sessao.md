# Sessão (Keycloak) — Invalidação

## Classificação

- **DDD:** Supporting
- **Camada:** backend (`domain-security`) + BFF (orquestra revogação)

## Responsabilidade

Invalidar sessões SSO no Keycloak de forma **agnóstica** a tipo de usuário (`TipoAcesso`) e tenant. Usado pelo BFF em
Usuários online após revogar sessão better-auth.

## Regras de negócio

- Identifica o usuário apenas pelo **identity id** do IdP (Keycloak user id).
- Remove **todas** as sessões Keycloak desse usuário (logout global no IdP).
- Não depende de `UsuarioSistema` / backoffice nem de tenant.
- Acesso: `admin-sistemas` ou `seguranca-usuarios-ativos-sistemas`.

## Estrutura de código

```
backend/domain-security/src/main/java/com/lodh8/starter/security/sessao/
  api/controller/SessaoController.java
  app/SessaoService.java
  app/impl/SessaoServiceImpl.java
```

## Onde encontrar

| Artefato   | Path                                                              |
|------------|-------------------------------------------------------------------|
| Controller | `domain-security/.../sessao/api/controller/SessaoController.java` |
| Service    | `domain-security/.../sessao/app/impl/SessaoServiceImpl.java`      |
| Identity   | `UsuarioService.invalidateSessions` (`domain-identity`)           |
| BFF        | `frontend/gestao/src/entities/session/api/keycloak-session.server.ts`    |

## Contratos

### REST (`/api/v1/seguranca/sessoes`)

| Método   | Path                              | Auth                                                     | Descrição              |
|----------|-----------------------------------|----------------------------------------------------------|------------------------|
| `DELETE` | `/seguranca/sessoes/{identityId}` | `admin-sistemas` ou `seguranca-usuarios-ativos-sistemas` | Logout global Keycloak |

## Dependências

- `domain-identity` (`UsuarioService`)
- BFF: Prisma `Account.accountId` + better-auth `Session`

## Testes

- `SessaoControllerTest`, `SessaoServiceImplTest`

## Relacionado

- [bff/session.md](../bff/session.md) — listagem/revogação better-auth
- [historico-login.md](historico-login.md) — auditoria de logins
- [core/iam/identity-keycloak.md](../core/iam/identity-keycloak.md)
