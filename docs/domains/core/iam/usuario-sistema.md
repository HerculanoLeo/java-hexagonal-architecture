# Usuario Sistema

> **Alias doc:** usuário **backoffice** (operador da plataforma). Fluxo
> cross-domain: [orquestração cadastro backoffice](../../orchestration/cadastro-usuario-sistema.md).

## Classificação

- **DDD:** Core
- **Camada:** backend + frontend (proxy)

## Responsabilidade

Administração de **usuários backoffice** (operadores da plataforma): listagem, cadastro, edição, ativação e inativação.
Orquestra `domain-identity` (Keycloak) com persistência local de metadados.

## Regras de negócio

- E-mail único por usuário sistema.
- Cadastro delega criação de identidade ao `UsuarioService` (Keycloak) com `TipoAcesso.USUARIO_SISTEMA`.
- Usuário normalmente vinculado a um **grupo** de permissão (roles do grupo).
- Flag **`main` (Usuário principal)** persistida em `tb_sistema_usuario.main` e sincronizada com a realm role Keycloak
  `admin-sistemas` (atribuída **diretamente no usuário**, não via grupo). Super user da plataforma: acesso total mesmo
  sem grupo.
- Em leituras (`GET`), `main` vem do banco via domínio/DTO — sem consulta extra ao Keycloak.
- Grupo é **obrigatório** apenas quando `main=false`; com `main=true` o grupo é opcional.
- Status `ATIVO` / `INATIVO` controla acesso.
- Criação publica `UsuarioSistemaCriadoEvent` → e-mail de boas-vindas.

## Estrutura de código

### Backend

```
backend/domain-backoffice/src/main/java/com/lodh8/starter/backoffice/usuarios/
  api/controller/UsuarioSistemaController.java
  api/dtos/
  app/impl/UsuarioSistemaServiceImpl.java
  domain/UsuarioSistema.java
  domain/event/UsuarioSistemaCriadoEvent.java
  infra/persistence/UsuarioSistemaEntity.java
  infra/UsuarioSistemaListeners.java
backend/domain-backoffice/src/main/resources/db/changelog/backoffice/
```

Persistência local: schema PostgreSQL `backoffice`, tabela `tb_sistema_usuario` (PK `uuidv7()`).

### Frontend

```
frontend/gestao/src/entities/usuario/
  api/usuario.service.ts
  api/usuario.queries.ts
  model/usuario*.schema.ts
frontend/gestao/src/components/pages/administracao/usuarios/
frontend/gestao/src/routes/(authenticated)/administracao/acesso/usuarios/
```

## Onde encontrar

| Artefato   | Path                                                               |
|------------|--------------------------------------------------------------------|
| Controller | `domain-backoffice/.../UsuarioSistemaController.java`              |
| Service    | `domain-backoffice/.../UsuarioSistemaServiceImpl.java`             |
| Listener   | `domain-backoffice/.../UsuarioSistemaListeners.java`               |
| BFF proxy  | `frontend/gestao/src/entities/usuario/api/usuario.service.ts`             |
| Tests      | `domain-backoffice/src/test/.../UsuarioSistemaControllerTest.java` |

## Contratos

### REST (`/api/v1/sistema/usuario`)

| Método | Path                             | Descrição        |
|--------|----------------------------------|------------------|
| GET    | `/sistema/usuario`               | Listar/buscar    |
| GET    | `/sistema/usuario/{id}`          | Por ID           |
| GET    | `/sistema/usuario/{id}/grupo`    | Grupo do usuário |
| POST   | `/sistema/usuario`               | Cadastrar        |
| PUT    | `/sistema/usuario/{id}`          | Atualizar        |
| PUT    | `/sistema/usuario/{id}/ativar`   | Ativar           |
| PUT    | `/sistema/usuario/{id}/inativar` | Inativar         |

### Eventos

- **Publica:** `UsuarioSistemaCriadoEvent`, `UsuarioSistemaAtualizadoEvent`, `UsuarioSistemaAtivadoEvent`,
  `UsuarioSistemaInativadoEvent`
- **Consome:** (via listener) → republica `NotificacaoBoasVindasEvent`

### Frontend (Zod)

- `usuarioDtoSchema`, `usuarioRegisterSchema`, `usuarioUpdateSchema`, `usuarioSearchSchema`

## Dependências

- `domain-identity` (usuarios), `domain-backoffice/grupos`, `notification::events`, `shared-kernel`
- Guard frontend: `SISTEMAS.USUARIOS`

## Testes

- `UsuarioSistemaServiceImplTest`, `UsuarioSistemaControllerTest`
- Frontend: `usuario-*-schema.test.ts`

## Implementação futura

- TODO em `UsuarioSistemaListeners`: notificações para ativar/inativar
- Paginação server-side na listagem (contrato preparado em `data-table-page.dto.ts`)
- Isolamento multi-tenant por organização — [multi-tenant.md](../../roadmap/multi-tenant.md)
