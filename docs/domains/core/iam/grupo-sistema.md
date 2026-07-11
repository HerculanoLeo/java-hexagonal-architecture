# Grupo Sistema

## Classificação

- **DDD:** Core
- **Camada:** backend + frontend (proxy)

## Responsabilidade

Grupos de permissão que agregam roles do catálogo. Usuários são associados a um grupo para definir RBAC.

## Regras de negócio

- Nome único por grupo.
- Cada grupo possui um conjunto de roles (pode ser vazio); strings alinhadas ao Keycloak.
- CRUD completo; exclusão bloqueada quando o grupo possui usuários vinculados (`ConflictException`).
- Sincronização de grupo com Keycloak via `domain-identity`.

## Estrutura de código

### Backend

```
backend/domain-backoffice/src/main/java/com/herculanoleo/starter/plataformadmin/grupos/
  api/controller/GrupoSistemaController.java
  app/impl/GrupoSistemaServiceImpl.java
  domain/GrupoSistema.java
  infra/persistence/
```

### Frontend

```
frontend/src/entities/grupo/
frontend/src/components/pages/administracao/grupos/
frontend/src/routes/.../administracao/acesso/grupos/
```

## Onde encontrar

| Artefato   | Path                                                             |
|------------|------------------------------------------------------------------|
| Controller | `domain-backoffice/.../GrupoSistemaController.java`              |
| Service    | `domain-backoffice/.../GrupoSistemaServiceImpl.java`             |
| BFF        | `frontend/src/entities/grupo/api/grupo.service.ts`               |
| Tests      | `domain-backoffice/src/test/.../GrupoSistemaControllerTest.java` |

## Contratos

### REST (`/api/v1/sistema/grupo`)

| Método | Path                  | Descrição |
|--------|-----------------------|-----------|
| GET    | `/sistema/grupo`      | Listar    |
| GET    | `/sistema/grupo/{id}` | Por ID    |
| POST   | `/sistema/grupo`      | Criar     |
| PUT    | `/sistema/grupo/{id}` | Atualizar |
| DELETE | `/sistema/grupo/{id}` | Excluir   |

### Frontend (Zod)

- `grupoDtoSchema`, `grupoRegisterSchema`, `grupoUpdateSchema`, `grupoSearchSchema`

## Dependências

- `domain-identity` (grupos, roles), `domain-backoffice/roles` (catálogo)
- Consumido por: usuario-sistema, usuario-autenticado
- Guard: `SISTEMAS.GRUPOS`

## Testes

- `GrupoSistemaServiceImplTest`, `GrupoSistemaControllerTest`
- Frontend: `grupo-*-schema.test.ts`

## Implementação futura

- Grupos por tenant
