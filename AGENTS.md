# java-hexagonal-architecture — Guia para Agentes

Starter público de **painel administrativo SaaS** multi-usuário: backend Java (**Spring Boot 4.2** + Modulith 2.2) + frontend TanStack Start (BFF) + Keycloak.

> Repositório **anonimizado** (sem credenciais reais).

## Comece aqui

| Recurso | Descrição |
|---------|-----------|
| [**Catálogo de domínios**](docs/domains/README.md) | **Referência principal** — subdomínios, paths no código, regras, roadmap |
| [**Infisical / secrets**](docs/infisical/README.md) | Decisão secret manager (OSS, limites, alternativas, OpenBao paralelo, homelab) |
| [Backend AGENTS.md](spring-boot/backend/AGENTS.md) | API Java, Modulith, endpoints |
| [Frontend rules](frontend/gestao/.cursor/rules/) | FSD, UI, entity slices |

## Estrutura do repositório

```
spring-boot/
  backend/           # Maven multi-module (Spring Boot + Modulith)
  dependencias/      # Keycloak + OpenTelemetry + Grafana + Postman collections
  docker-compose.yaml
frontend/gestao/     # TanStack Start + Prisma (BFF)
docs/domains/        # Documentação / specs por domínio
docs/infisical/      # Secret manager (Infisical + OpenBao paralelo)
```

## Cursor rules

### Backend (`spring-boot/backend/.cursor/rules/`)

| Rule | Foco |
|------|------|
| `backend-architecture.mdc` | Hexagonal, Modulith, convenções |
| `backend-domains.mdc` | DDD, bounded contexts |
| `backend-testing.mdc` | JUnit, JaCoCo |
| `backend-logging.mdc` | INFO mascarado / DEBUG aberto, etapas |
| `postman-collection.mdc` | Endpoint → Postman (`dependencias/collections/`) |

### Frontend (`frontend/gestao/.cursor/rules/`)

| Rule | Foco |
|------|------|
| `frontend-architecture.mdc` | FSD, AppShell, TanStack |
| `frontend-domains.mdc` | Entity slices, BFF vs Java |
| `frontend-testing.mdc` | Vitest unitário |

## Fluxo de dados

```
Browser → TanStack Start (BFF) → Prisma (menu, settings, sessions)
       → Backend Java /api/v1 (IAM, localização, notificação, segurança)
       → Keycloak (OAuth)
```

## Comandos úteis

```bash
cd spring-boot/backend && mvn test
cd frontend/gestao && pnpm test
cd frontend/gestao && pnpm dev
```

## Novo subdomínio

1. Documentar em [`docs/domains/`](docs/domains/README.md) (copiar `_template.subdomain.md`).
2. Implementar backend e/ou frontend conforme classificação.
3. Atualizar rules `*-domains.mdc` se necessário.
