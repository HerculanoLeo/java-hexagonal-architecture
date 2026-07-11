# IAM — Gestão de Identidade e Acesso

## Visão geral

Bounded context **Core** do starter: painel administrativo SaaS com RBAC multi-usuário.

## Módulos backend

| Módulo              | Papel                                             |
|---------------------|---------------------------------------------------|
| `domain-identity`   | Anti-corrupção Keycloak (usuarios, grupos, roles) |
| `domain-backoffice` | Orquestração de negócio + persistência local      |
| `domain-authorize`  | Contexto do usuário autenticado (self-service)    |

## Subdomínios

- [identity-keycloak.md](identity-keycloak.md) — camada Keycloak (interna)
- [usuario-sistema.md](usuario-sistema.md) — admin CRUD usuários
- [grupo-sistema.md](grupo-sistema.md) — grupos de permissão
- [role-sistema.md](role-sistema.md) — catálogo de roles
- [usuario-autenticado.md](usuario-autenticado.md) — perfil e senha do usuário logado

## Fluxo de eventos (exemplo)

```
UsuarioSistemaCriadoEvent → NotificacaoBoasVindasEvent → e-mail de boas-vindas
```

Cenário completo: [orchestration/cadastro-usuario-sistema.md](../../orchestration/cadastro-usuario-sistema.md)

## Frontend

Entity slices: `usuario`, `grupo`, `role`, `auth` + auth transversal em `shared/auth/`.

Ver [bff/README.md](../../bff/README.md) para domínios exclusivos do BFF.
