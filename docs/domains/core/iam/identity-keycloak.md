# Identity (Keycloak)

## Classificação

- **DDD:** Core (subcamada interna)
- **Camada:** backend

## Responsabilidade

Camada anti-corrupção sobre o Keycloak. Executa operações de usuários, grupos, roles e client API sem expor detalhes do
IdP aos demais módulos.

## Regras de negócio

- Usuários criados/atualizados no Keycloak com atributos de tipo de acesso e grupo.
- Flag `main` (persistida em `domain-backoffice`) → `UsuarioProviderPort.updateAdminRole` atribui/remove a realm role
  `admin-sistemas` **diretamente no usuário** (não no grupo).
- Senhas validadas via Passay (`PasswordConstraintPassyAdapter`).
- Sem controllers REST públicos — consumido por `domain-backoffice`, `domain-authorize` e `domain-security` (invalidação
  de sessões).

## Estrutura de código

```
backend/domain-identity/src/main/java/com/lodh8/starter/identity/
  usuario/app/UsuarioService.java
  usuario/app/impl/UsuarioServiceImpl.java
  grupos/app/GrupoService.java
  roles/app/RoleProviderPort.java
  clientapi/
  infra/                    # Keycloak Admin Client, mappers, cache
  infra/attribute/          # Named interface: infra-attributes
```

## Onde encontrar

| Artefato          | Path                                                            |
|-------------------|-----------------------------------------------------------------|
| Usuario service   | `domain-identity/.../usuario/app/impl/UsuarioServiceImpl.java`  |
| Grupo service     | `domain-identity/.../grupos/app/impl/GrupoServiceImpl.java`     |
| Role provider     | `domain-identity/.../roles/app/port/RoleProviderPort.java`      |
| Keycloak adapters | `domain-identity/.../infra/`                                    |
| Modulith config   | `domain-identity/.../identity/package-info.java`                |
| Tests             | `domain-identity/src/test/java/.../UsuarioServiceImplTest.java` |

## Contratos

### Named interfaces (Modulith)

- `identity::usuarios`, `identity::usuarios-domain`
- `identity::grupos`, `identity::grupos-domain`
- `identity::roles`, `identity::roles-domain`
- `identity::infra-attributes`

### Domain records

- `UsuarioRegister`, `UsuarioUpdate` em `identity/usuario/domain/`

## Dependências

- **Módulos:** `shared-kernel` apenas
- **Externos:** Keycloak Admin Client, OAuth2

## Testes

- `UsuarioServiceImplTest`, testes de mappers/adapters em `domain-identity/src/test/`

## Implementação futura

- Sincronização bidirecional com multi-tenant quando `domain-tenant` existir
- Eventos de identity publicados para auditoria
