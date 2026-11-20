# Backend — Guia para Agentes

Modular Monolith Java (**Spring Boot 4.2.x** + **Spring Modulith 2.2.x**) com deployável único **`api-cadastros`** (`/api/v1`).

## Documentação

| Recurso | Quando usar |
|---------|-------------|
| [`backend-architecture.mdc`](.cursor/rules/backend-architecture.mdc) | Service vs Port, layout, Modulith, `@MappedEnum`, Specs |
| [`backend-domains.mdc`](.cursor/rules/backend-domains.mdc) | Bounded contexts, DDD, matriz Modulith |
| [`backend-testing.mdc`](.cursor/rules/backend-testing.mdc) | Testes JUnit/Mockito, JaCoCo |
| [`backend-logging.mdc`](.cursor/rules/backend-logging.mdc) | INFO mascarado / DEBUG aberto, etapas da operação |
| [`postman-collection.mdc`](.cursor/rules/postman-collection.mdc) | Endpoint → collection Postman |
| [`../dependencias/collections/`](../dependencias/collections/) | Collection + environment local |

## Regra de ouro hexagonal

```
Controller / Filter  →  *Service  →  *ServiceImpl  →  *Port  →  *Adapter
```

- Controllers **nunca** injetam Port.
- `app/` **não** importa `infra/`.
- Cross-módulo: só NamedInterface em `app/` / `domain/` (+ `bc-*-events`).

## Módulos Maven

```
shared-kernel
  → bc-identity → bc-authorize / bc-plataforma
  → bc-notificacao-events → bc-notificacao
  → bc-localizacao, bc-seguranca
api-cadastros (composition root)
```

## Stack (resumo)

| Componente | Versão |
|------------|--------|
| Spring Boot | 4.2.x |
| Spring Modulith | 2.2.x |
| Spring Cloud | 2026.0.x |
| spring-mapper-enum | 2.0.0 (`@MappedEnum` + processor) |
| sentinel-flow | 1.1.0 |
| Java | 25 |
| PostgreSQL | **18+** (`uuidv7()`) |

## Persistência / Liquibase

- Changelogs por módulo; master em `api-cadastros`.
- Schemas DB: alinhar aos BCs (`plataforma`, `notificacao`, `localizacao`, `seguranca`).
- UUID via DB `uuidv7()` + `@Generated(INSERT)`.

## Classificação DDD

| Categoria | Módulos |
|-----------|---------|
| **Core** | identity, plataforma, authorize |
| **Supporting** | notificacao, localizacao, seguranca |
| **Generic** | shared-kernel, api-cadastros, `*-events` |

## Endpoints REST

Base: `/api/v1`

| Prefixo | Uso |
|---------|-----|
| `/sistema/*` | Plataforma (admins) |
| `/auth/*` | Self-service |
| `/seguranca/*` | Histórico / sessões |
| `/localizacao/*` | CEP, estado, município |
| `/notificacao` | E-mails transacionais |

## Comandos

```bash
cd spring-boot/backend && mvn test
cd spring-boot/backend && mvn test -pl bc-seguranca -am
cd spring-boot/backend && mvn test -Dtest=ApplicationTest -Dsurefire.failIfNoSpecifiedTests=false
cd spring-boot/backend && mvn -pl api-cadastros -am spring-boot:run
```

## Checklist — alterar domínio

1. Respeitar Service≠Port e fronteiras Modulith.
2. Lógica em `app/impl/`; SentinelFlow em validators.
3. Cross-module: NamedInterface ou `bc-*-events`.
4. Atualizar `*ServiceImplTest` / `*ControllerTest`.

## Checklist — novo domínio

1. Módulo `bc-*` + hexagonal.
2. `@ApplicationModule` + `@NamedInterface` em `app/` / `domain/`.
3. Eventos cross-module → `bc-*-events`.
4. Parent + `api-cadastros`.
5. `ApplicationTest` passa.
