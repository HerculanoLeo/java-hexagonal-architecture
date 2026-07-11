# Starter — Painel Admin (TanStack Start)

BFF + UI administrativa: **TanStack Start**, Prisma, better-auth (Keycloak) e proxy para a API Java (`api-cadastros`).

> Valores de ambiente neste repositório são **dummy** / placeholders.

## Setup

```bash
cp .env.example .env.local
pnpm install
pnpm db:generate
pnpm dev
```

- App: http://localhost:3000 (ou porta do Vite)
- API Java esperada: `CADASTROS_URL` (padrão `http://localhost:9000/api/v1`)
- Keycloak: `KEYCLOAK_ISSUER` (dummy: `https://keycloak.dummy.com/realms/starter`)

## Scripts

| Comando | Uso |
|---------|-----|
| `pnpm dev` | Dev server |
| `pnpm test` | Vitest |
| `pnpm build` | Build produção |
| `pnpm db:migrate` | Prisma migrate (usa `.env.local`) |
| `pnpm db:seed` | Seed menu / platform-settings |

## Arquitetura

Feature-Sliced Design adaptado — ver [`.cursor/rules/`](.cursor/rules/) e [`docs/domains/`](../../docs/domains/README.md).

## Docker

`Dockerfile` é runtime-only (espera `dist/` + Prisma client gerado na pipeline). Imagem pública de exemplo: `starter-webapp-gestao`.
