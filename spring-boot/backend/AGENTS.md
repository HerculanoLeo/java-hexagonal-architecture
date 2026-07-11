# Backend — Guia para Agentes

Modular Monolith Java (**Spring Boot 4.0.7** + **Spring Modulith 2.0**) com deployável único **`api-cadastros`** (
`/api/v1`).

## Documentação

| Recurso                                                                        | Quando usar                                                        |
|--------------------------------------------------------------------------------|--------------------------------------------------------------------|
| [**Catálogo de domínios**](../../../docs/domains/README.md)                    | Encontrar implementações, regras de negócio, paths, roadmap        |
| [**Orquestração cross-domain**](../../../docs/domains/orchestration/README.md) | Fluxos multi-módulo (onboarding, aprovação, eventos)               |
| [`backend-architecture.mdc`](.cursor/rules/backend-architecture.mdc)           | Estrutura hexagonal, Modulith, spring-mapper-enum v2, SentinelFlow |
| [`backend-domains.mdc`](.cursor/rules/backend-domains.mdc)                     | Bounded contexts, DDD, eventos, fronteira BFF                      |
| [`backend-testing.mdc`](.cursor/rules/backend-testing.mdc)                     | Testes JUnit/Mockito, starters `-test` modulares, JaCoCo           |
| Frontend (BFF)                                                                 | Não incluído neste repositório — ver specs em `docs/domains/bff/`  |

## Módulos Maven

```
shared-kernel → domain-identity → domain-backoffice / domain-authorize
              → domain-notification, domain-location, domain-security
api-cadastros (agrega todos)
```

## Stack (resumo)

| Componente         | Versão                                    |
|--------------------|-------------------------------------------|
| Spring Boot        | 4.0.7                                     |
| Spring Modulith    | 2.0.0                                     |
| Spring Cloud       | 2025.1.2                                  |
| spring-mapper-enum | 2.0.0 (GitHub Packages)                   |
| sentinel-flow      | validação fluente nos services            |
| Java               | 25                                        |
| PostgreSQL         | **18+** (`uuidv7()`, schemas por domínio) |

## Persistência / Liquibase

- Changelogs por módulo em `domain-*/src/main/resources/db/changelog/{domain}/`; master em `api-cadastros`.
- Schemas de domínio: `backoffice`, `notification`, `location`, `security`. Infra Modulith:
  `public.event_publication`.
- Squash de baseline: resetar o banco (`DROP SCHEMA` / recriar DB + limpar `databasechangelog*`) antes de subir a API.

## Classificação DDD (resumo)

| Categoria      | Módulos                             |
|----------------|-------------------------------------|
| **Core**       | identity, plataformadmin, authorize |
| **Supporting** | notification, location, security    |
| **Generic**    | shared-kernel, api-cadastros        |

## Endpoints REST

Base: `/api/v1`

| Prefixo                                                | Uso                                                  |
|--------------------------------------------------------|------------------------------------------------------|
| `/sistema/usuario`, `/sistema/grupo`, `/sistema/roles` | Backoffice IAM (operadores da plataforma)            |
| `/auth/me`, `/auth/grupo`, `/auth/me/senha`            | Self-service                                         |
| `/seguranca/historico-logins`, `/seguranca/sessoes`    | Segurança (auditoria de login, invalidação Keycloak) |
| `/localizacao/*`                                       | CEP, estado, município                               |
| `/notificacao`                                         | E-mails transacionais                                |

## Fronteira BFF

| Backend Java                           | BFF (Prisma)                            |
|----------------------------------------|-----------------------------------------|
| CRUD usuario/grupo/role, perfil, senha | menu, platform-settings, sessões online |
| Keycloak via domain-identity           | OAuth login (better-auth)               |

## Comandos

> **Observação — Maven multi-módulo:** rode **sempre** a partir do POM pai (`backend/`).  
> `cd backend/domain-* && mvn compile|test` (ou qualquer submódulo isolado) **falha**: o Maven não resolve os outros
> artefatos internos (`com.lodh8.starter:*`) de que o alvo depende.  
> Para um módulo específico, use `-pl` **com** `-am` a partir do pai (ex.: `mvn -pl domain-security -am test`).

```bash
cd backend && mvn test                                    # testes (reator completo)
cd backend && mvn test -pl domain-security -am            # módulo + dependências
cd backend && mvn test -Dtest=ApplicationTest -Dsurefire.failIfNoSpecifiedTests=false  # Modulith
cd backend && mvn -pl api-cadastros -am spring-boot:run   # subir API (requer env)
```

## Checklist — alterar domínio existente

1. Ler doc em [`docs/domains/`](../../../docs/domains/README.md) do subdomínio.
2. Respeitar fronteiras Modulith (`package-info.java`).
3. Lógica em `app/impl/`, não em controller ou entity JPA.
4. Regras de negócio: SentinelFlow em `app/*Validator` (Bean Validation nos DTOs).
5. Comunicação cross-module: named interfaces ou eventos.
6. Atualizar testes (`*ServiceImplTest`, `*ControllerTest`).
7. Espelhar contrato no frontend se expor API nova.
8. Atualizar doc do subdomínio se regras mudarem.

## Checklist — novo domínio

1. Copiar [`docs/domains/_template.subdomain.md`](../../../docs/domains/_template.subdomain.md).
2. Criar `domain-{context}` Maven module + hexagonal layout.
3. `@ApplicationModule` + `@NamedInterface`.
4. Registrar em `pom.xml` parent e `api-cadastros/pom.xml`.
5. Liquibase se persistir; controller se API pública.
6. `ApplicationTest` deve passar.
