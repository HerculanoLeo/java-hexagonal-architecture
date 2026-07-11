# Usuario Autenticado

## Classificação

- **DDD:** Core
- **Camada:** backend + frontend (proxy)

## Responsabilidade

Self-service do usuário logado: consultar/atualizar perfil, grupo associado e trocar senha.

## Regras de negócio

- Endpoints exigem JWT válido (OAuth2 Resource Server).
- `/auth/me` retorna dados do usuário autenticado.
- `/auth/me` (PUT) atualiza o nome (Keycloak + persistência local) para `USUARIO_SISTEMA`.
- `/auth/grupo` retorna o grupo de permissão; se o usuário não tiver grupo e for **usuário principal** (`main=true` no
  banco), retorna grupo sintético `"Usuário principal"` com as roles do JWT. Sem grupo e sem main → `204 No Content`.
- Troca de senha valida senha atual e política Passay; publica `NotificacaoTrocaSenhaEvent`.
- No frontend, `hasRole` trata `admin-sistemas` como bypass de qualquer role exigida (super user).

## Estrutura de código

### Backend

```
backend/domain-authorize/src/main/java/com/lodh8/starter/authorize/
  api/controller/UsuarioAutenticadoController.java
  app/impl/UsuarioAutenticadoServiceImpl.java
  infra/authentication/
```

### Frontend

```
frontend/gestao/src/entities/auth/
  api/auth.service.ts
  model/meus-dados-*.schema.ts
frontend/gestao/src/routes/(authenticated)/me/
frontend/gestao/src/components/pages/me/
```

## Onde encontrar

| Artefato   | Path                                                     |
|------------|----------------------------------------------------------|
| Controller | `domain-authorize/.../UsuarioAutenticadoController.java` |
| BFF        | `frontend/gestao/src/entities/auth/api/auth.service.ts`         |
| Auth infra | `frontend/gestao/src/shared/auth/`                              |
| Tests      | `domain-authorize/src/test/.../`                         |

## Contratos

### REST (`/api/v1/auth`)

| Método | Path             | Descrição            |
|--------|------------------|----------------------|
| GET    | `/auth/me`       | Perfil autenticado   |
| GET    | `/auth/grupo`    | Grupo do usuário     |
| PUT    | `/auth/me`       | Atualizar meus dados |
| PUT    | `/auth/me/senha` | Trocar senha         |

### Eventos

- **Publica:** (indireto) `NotificacaoTrocaSenhaEvent` via notification

### Frontend (Zod)

- `meusDadosUpdateSchema`, `trocarSenhaRequestSchema`
- `UsuarioAutenticadoDto`, `MeusDadosProfileDto`

## Dependências

- `domain-identity` (usuarios, grupos)
- `shared-kernel`, OAuth2 JWT
- Rotas: `/me/dados`, `/me/senha`

## Testes

- Controller/service tests em `domain-authorize/src/test/`
- Frontend: `meus-dados-*-schema.test.ts`

## Implementação futura

- Avatar/foto de perfil
- Preferências de usuário além de senha
