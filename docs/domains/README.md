# Catálogo de Domínios

Documentação detalhada por domínio e subdomínio (repositório público anonimizado) para *
*desenvolvedores** e **agentes de IA**. Complementa as Cursor rules (concisas) com paths reais, regras de negócio e guia
de implementação futura.

## Como usar

1. Identifique o bounded context (Core IAM, Supporting, BFF, Generic).
2. Abra o `.md` do subdomínio para paths, contratos e dependências.
3. Consulte as rules por camada para padrões de código:
    - Backend: [`spring-boot/backend/.cursor/rules/`](../../spring-boot/backend/.cursor/rules/)
    - Frontend: [`frontend/gestao/.cursor/rules/`](../../frontend/gestao/.cursor/rules/)

## Classificação DDD

| Categoria        | Descrição                            | Pastas                                      |
|------------------|--------------------------------------|---------------------------------------------|
| **Core**         | Diferencial do produto — IAM         | [`core/iam/`](core/iam/README.md)           |
| **Supporting**   | Necessário, não diferenciador        | [`supporting/`](supporting/notificacao.md)  |
| **Generic**      | Infra transversal                    | [`generic/`](generic/shared-kernel.md)      |
| **BFF**          | Domínios só no TanStack Start/Prisma | [`bff/`](bff/README.md)                     |
| **Roadmap**      | Planejado, não implementado          | [`roadmap/`](roadmap/README.md)             |
| **Orquestração** | Fluxos cross-domain                  | [`orchestration/`](orchestration/README.md) |

## Mapa de navegação

### Core — IAM

| Subdomínio          | Doc                                                       | Backend             | Frontend             |
|---------------------|-----------------------------------------------------------|---------------------|----------------------|
| Identity (Keycloak) | [identity-keycloak.md](core/iam/identity-keycloak.md)     | `domain-identity`   | —                    |
| Usuario Sistema     | [usuario-sistema.md](core/iam/usuario-sistema.md)         | `domain-backoffice` | `entities/usuario` |
| Grupo Sistema       | [grupo-sistema.md](core/iam/grupo-sistema.md)             | `domain-backoffice` | `entities/grupo` |
| Role Sistema        | [role-sistema.md](core/iam/role-sistema.md)               | `domain-backoffice` | `entities/role` |
| Usuario Autenticado | [usuario-autenticado.md](core/iam/usuario-autenticado.md) | `domain-authorize`      | `entities/auth` |

Visão do bounded context: [core/iam/README.md](core/iam/README.md)

### Supporting

| Subdomínio          | Doc                                                             |
|---------------------|-----------------------------------------------------------------|
| Notificação         | [notificacao.md](supporting/notificacao.md)                     |
| Localização         | [localizacao/README.md](supporting/localizacao/README.md)       |
| CEP                 | [localizacao/cep.md](supporting/localizacao/cep.md)             |
| Estado              | [localizacao/estado.md](supporting/localizacao/estado.md)       |
| Município           | [localizacao/municipio.md](supporting/localizacao/municipio.md) |
| Histórico de logins | [historico-login.md](supporting/historico-login.md)             |
| Sessão (Keycloak)   | [sessao.md](supporting/sessao.md)                               |

### Orquestração

| Cenário                     | Doc                                                                      | Status       |
|-----------------------------|--------------------------------------------------------------------------|--------------|
| Índice e padrões            | [orchestration/README.md](orchestration/README.md)                       | —            |
| Cadastro usuário backoffice | [cadastro-usuario-sistema.md](orchestration/cadastro-usuario-sistema.md) | Implementado |
| Template cenário            | [_template.scenario.md](orchestration/_template.scenario.md)             | —            |

### Generic

| Componente    | Doc                                          |
|---------------|----------------------------------------------|
| Shared Kernel | [shared-kernel.md](generic/shared-kernel.md) |
| API Cadastros | [api-cadastros.md](generic/api-cadastros.md) |

### BFF

| Subdomínio                | Doc                                              |
|---------------------------|--------------------------------------------------|
| Auth (OAuth/guards)       | [auth.md](bff/auth.md)                           |
| Menu                      | [menu.md](bff/menu.md)                           |
| Platform Settings         | [platform-settings.md](bff/platform-settings.md) |
| Session / Usuários online | [session.md](bff/session.md)                     |

Visão BFF: [bff/README.md](bff/README.md)

### Roadmap

| Domínio               | Doc                                                        |
|-----------------------|------------------------------------------------------------|
| Multi-tenant          | [multi-tenant.md](roadmap/multi-tenant.md)                 |
| Observabilidade admin | [observabilidade.md](roadmap/observabilidade.md)           |
| Notificação admin UI  | [notificacao-admin-ui.md](roadmap/notificacao-admin-ui.md) |

## Endpoints REST (backend)

Base: `/api/v1`

| Prefixo                                     | Módulo       | Uso                             |
|---------------------------------------------|--------------|---------------------------------|
| `/sistema/usuario`                          | admin        | CRUD usuários sistema           |
| `/sistema/grupo`                            | admin        | CRUD grupos                     |
| `/sistema/roles`                            | admin        | Catálogo roles                  |
| `/auth/me`, `/auth/grupo`, `/auth/me/senha` | authorize    | Self-service                    |
| `/localizacao/cep`, `/estado`, `/municipio` | location     | Geografia BR                    |
| `/notificacao`                              | notification | Notificações                    |
| `/seguranca/historico-logins`               | security     | Histórico de logins             |
| `/seguranca/sessoes`                        | security     | Invalidação de sessões Keycloak |

## Novo subdomínio

1. Copie [`_template.subdomain.md`](_template.subdomain.md).
2. Coloque em `core/`, `supporting/`, `bff/` ou `roadmap/` conforme classificação.
3. Atualize este README e as rules `*-domains.mdc`.
4. Implemente código e testes conforme `*-architecture.mdc` e `*-testing.mdc`.

## Novo cenário de orquestração

1. Copie [`orchestration/_template.scenario.md`](orchestration/_template.scenario.md).
2. Documente fluxos multi-módulo em `orchestration/`.
3. Atualize [orchestration/README.md](orchestration/README.md) e [
   `backend-domains.mdc`](../../backend/.cursor/rules/backend-domains.mdc).

## Índices relacionados

- [`backend/AGENTS.md`](../../backend/AGENTS.md)
- [`AGENTS.md`](../../AGENTS.md) (raiz do repositório)
