# Multi-tenant / Organização

## Classificação (planejada)

- **DDD:** Core (extensão)
- **Camada:** backend + frontend

## Objetivo

Suporte a múltiplos clientes/organizações com isolamento de dados, seletor de tenant na topbar e permissões por escopo.

## Evidências atuais

- Regras de UI em `frontend-architecture.mdc` mencionam multi-tenant
- **Sem** entity `tenant` ou `domain-tenant` no código
- Sem seletor de cliente implementado
- `TipoAcesso` + `application.relacionadoId` preparam extensão futura

## Implementação sugerida

1. Backend: novo módulo `domain-tenant` ou extensão de `domain-backoffice` com `organizacaoId` em entidades.
2. JWT/claims: organização ativa no token ou header.
3. Frontend: seletor na topbar, filtros por tenant em listagens, estados vazios por escopo.
4. Keycloak: groups ou attributes por organização.
5. Documentar subdomínios em `docs/domains/core/` após implementação.

## Referências

- [bff/platform-settings.md](../bff/platform-settings.md) — branding por tenant
- [core/iam/usuario-sistema.md](../core/iam/usuario-sistema.md) — usuários backoffice
- [IAM — orquestração cadastro](../orchestration/cadastro-usuario-sistema.md)
