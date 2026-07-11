# Java Hexagonal Architecture — Modular Monolith

Demonstração pública de **monolito modular** com **DDD** e **Arquitetura Hexagonal** (Ports & Adapters): backend Spring Boot + frontend TanStack Start (BFF).

> Este repositório **anonimizado** — sem credenciais reais e sem referências a empresas/clientes, espelha a arquitetura de um projeto real que gerencio como Arquiteto de Software.

## Estrutura

```
spring-boot/
  backend/           # Maven multi-module (API Java)
  dependencias/      # Keycloak realm + OpenTelemetry collector
  docker-compose.yaml
frontend/gestao/     # TanStack Start + Prisma (BFF / painel admin)
docs/domains/        # Catálogo de domínios / specs
AGENTS.md            # Guia para agentes de IA
```

## Backend — módulos Maven

| Módulo | Papel |
|--------|-------|
| `shared-kernel` | Enums, exceções, utilitários cross-cutting |
| `domain-identity` | Keycloak / identidade |
| `domain-authorize` | Usuário autenticado, RBAC de sessão |
| `domain-backoffice` | Usuários/grupos/roles da plataforma |
| `domain-notification` | E-mail transacional (Freemarker) |
| `domain-location` | CEP, UF, município |
| `domain-security` | Histórico de login e invalidação de sessão Keycloak |
| `api-cadastros` | Composition root (`/api/v1`) |

## Stack

**Backend:** Java 25, Spring Boot 4.0.7, Spring Modulith 2.0, Keycloak, Liquibase, PostgreSQL 18+, OpenTelemetry

**Frontend:** React 19, TanStack Start/Router/Query, Prisma, better-auth, Tailwind v4, shadcn/ui, Vitest

## Desenvolvimento

```bash
# Backend
cp spring-boot/.env.example spring-boot/.env
cd spring-boot/backend && mvn test
cd spring-boot/backend && mvn -pl api-cadastros -am spring-boot:run

# Frontend (BFF)
cp frontend/gestao/.env.example frontend/gestao/.env.local
cd frontend/gestao && pnpm install && pnpm dev
cd frontend/gestao && pnpm test
```

Variáveis: `spring-boot/.env.example` e `frontend/gestao/.env.example` (valores dummy).

## Documentação

- [Catálogo de domínios](docs/domains/README.md)
- [AGENTS.md](AGENTS.md)
- [Backend AGENTS.md](spring-boot/backend/AGENTS.md)

## Status

- **Spring Boot**: implementação principal (avançada)
- **Frontend (TanStack Start)**: painel admin + BFF incluído
- **Quarkus**: planejado (`quarkus/` ainda não incluído)
