# Domínios BFF

> **Nota:** o código do frontend **não está incluído** neste repositório público. Estas specs descrevem o
> contrato/estrutura esperada para quando o BFF for publicado.

Domínios implementados exclusivamente no **TanStack Start** com persistência **Prisma** (schema `frontend`) ou auth
OAuth local. Não possuem módulo Java equivalente.

## Proxy vs Prisma

| Tipo                 | Entity slices                          | Integração                      |
|----------------------|----------------------------------------|---------------------------------|
| **Proxy**            | `usuario`, `grupo`, `role`, `auth`     | `fetchCadastros` → backend Java |
| **Prisma**           | `menu`, `platform-settings`, `session` | PostgreSQL schema `frontend`    |
| **Auth transversal** | `shared/auth/`                         | Keycloak + better-auth          |

## Subdomínios

- [auth.md](auth.md) — OAuth, guards, permissões
- [menu.md](menu.md) — navegação AppShell
- [platform-settings.md](platform-settings.md) — branding e layout
- [session.md](session.md) — usuários online / revogação de sessão

## Schema Prisma

`frontend/prisma/schema.prisma` — modelos `User`, `Session`, `Account`, `Menu`, `PlatformSettings`, …

## Implementação futura

Decisão atual: menu e branding **permanecem no BFF**. Reavaliar apenas com decisão explícita de mover para backend.
