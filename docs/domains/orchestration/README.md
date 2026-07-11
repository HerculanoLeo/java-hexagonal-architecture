# Orquestração de Domínios

Documentação de **fluxos que cruzam módulos** — passos síncronos, eventos, compensação e fronteiras Modulith.
Complementa [`_template.subdomain.md`](../_template.subdomain.md), que cobre um subdomínio isolado.

## Quando usar qual template

| Situação                                         | Template                                              |
|--------------------------------------------------|-------------------------------------------------------|
| Um subdomínio, CRUD, regras locais               | [`_template.subdomain.md`](../_template.subdomain.md) |
| Fluxo multi-módulo (onboarding, aprovação, saga) | [`_template.scenario.md`](_template.scenario.md)      |

## Cenários documentados

| Cenário                                                    | Status           | Descrição                                                                     |
|------------------------------------------------------------|------------------|-------------------------------------------------------------------------------|
| [Cadastro usuário backoffice](cadastro-usuario-sistema.md) | **Implementado** | Referência: orquestração síncrona + eventos (identity → admin → notification) |

## Padrões reutilizáveis (código existente)

| Padrão                              | Onde                                                       | Uso                                                       |
|-------------------------------------|------------------------------------------------------------|-----------------------------------------------------------|
| Orquestração síncrona + compensação | `UsuarioSistemaServiceImpl.register()`                     | Keycloak + persistência local; rollback se falhar         |
| Coreografia por eventos             | `UsuarioSistemaCriadoEvent` → `NotificacaoBoasVindasEvent` | Efeitos colaterais desacoplados (notification, auditoria) |

## Domínios envolvidos no cenário atual

| Módulo                | DDD        | Papel na orquestração                                                    |
|-----------------------|------------|--------------------------------------------------------------------------|
| `domain-backoffice`   | Core       | **Backoffice** — operadores (`/sistema/*`); dono do processo de cadastro |
| `domain-identity`     | Core       | Keycloak (ports)                                                         |
| `domain-notification` | Supporting | E-mails transacionais via eventos                                        |

## Novo cenário

1. Copie [`_template.scenario.md`](_template.scenario.md).
2. Salve em `docs/domains/orchestration/{nome}.md`.
3. Atualize esta tabela e, se necessário, [`docs/domains/README.md`](../README.md) e [
   `backend-domains.mdc`](../../backend/.cursor/rules/backend-domains.mdc).
