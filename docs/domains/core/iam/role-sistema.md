# Role Sistema

## Classificação

- **DDD:** Core
- **Camada:** backend + frontend (proxy, read-only)

## Responsabilidade

Catálogo read-only de permissões (roles) disponíveis para composição de grupos e guards de UI.

## Regras de negócio

- Listagem sincronizada com roles do Keycloak/realm.
- Sem CRUD administrativo no starter atual — roles gerenciadas no IdP ou configuração de realm.
- Nomes de roles alinhados ao enum `SISTEMAS` no frontend.

## Estrutura de código

### Backend

```
backend/domain-backoffice/src/main/java/com/lodh8/starter/backoffice/roles/
  api/controller/RoleSistemaController.java
  app/impl/RoleSistemaServiceImpl.java
  infra/RoleSistemaMapperAdapter.java
```

### Frontend

```
frontend/gestao/src/entities/role/
  api/role.service.ts
  model/role.dto.ts
```

## Onde encontrar

| Artefato      | Path                                                            |
|---------------|-----------------------------------------------------------------|
| Controller    | `domain-backoffice/.../RoleSistemaController.java`              |
| BFF           | `frontend/gestao/src/entities/role/api/role.service.ts`                |
| Permissões UI | `frontend/gestao/src/shared/auth/auth-permissions.enum.ts`             |
| Tests         | `domain-backoffice/src/test/.../RoleSistemaControllerTest.java` |

## Contratos

### REST (`/api/v1/sistema/roles`)

| Método | Path             | Descrição    |
|--------|------------------|--------------|
| GET    | `/sistema/roles` | Listar todas |

### Frontend

- `RoleDto`: `{ id, nome, descricao, tipo? }`

## Dependências

- `domain-identity` (roles provider)
- Consumido por: grupo-sistema, menu (filtragem), auth guards

## Testes

- `RoleSistemaControllerTest`

## Implementação futura

- UI admin para roles se sair do Keycloak-only
- Roles dinâmicas por tenant
