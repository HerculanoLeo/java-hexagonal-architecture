# Java Hexagonal Architecture — Modular Monolith

Demonstração pública de **monolito modular** com **DDD** e **Arquitetura Hexagonal** (Ports & Adapters), baseada em
Spring Boot.

> Este repositório é a versão **anonimizada** do backend de estudos. Sem credenciais reais e sem referências a
> empresas/clientes.

## Estrutura

```
spring-boot/
  backend/           # Maven multi-module (API)
  dependencias/      # Keycloak realm + OpenTelemetry collector
  docker-compose.yaml
docs/domains/        # Catálogo de domínios / specs
AGENTS.md            # Guia para agentes de IA
```

Frontend (TanStack Start / BFF) **não está incluído** neste repositório; a estrutura de specs em `docs/domains/` (
incluindo BFF) é mantida como referência.

## Módulos Maven

| Módulo                | Papel                                               |
|-----------------------|-----------------------------------------------------|
| `shared-kernel`       | Enums, exceções, utilitários cross-cutting          |
| `domain-identity`     | Keycloak / identidade                               |
| `domain-authorize`    | Usuário autenticado, RBAC de sessão                 |
| `domain-backoffice`   | Usuários/grupos/roles da plataforma                 |
| `domain-notification` | E-mail transacional (Freemarker)                    |
| `domain-location`     | CEP, UF, município                                  |
| `domain-security`     | Histórico de login e invalidação de sessão Keycloak |
| `api-cadastros`       | Composition root (`/api/v1`)                        |

## Stack

- Java 25
- Spring Boot 4.0.7
- Spring Modulith 2.0
- Keycloak (Admin Client + OAuth2 Resource Server)
- Liquibase + PostgreSQL 18+ (`uuidv7()`)
- OpenTelemetry (métricas, traces, logs)
- MapStruct, Lombok, SpringDoc OpenAPI, JaCoCo

## Desenvolvimento

```bash
cp spring-boot/.env.example spring-boot/.env
cd spring-boot/backend && mvn test
cd spring-boot/backend && mvn -pl api-cadastros -am spring-boot:run
```

Variáveis: ver `spring-boot/.env.example`. Perfil local: `application-local.yaml` (valores dummy).

## Documentação

- [Catálogo de domínios](docs/domains/README.md)
- [AGENTS.md](AGENTS.md)
- [Backend AGENTS.md](spring-boot/backend/AGENTS.md)

## Status

- **Spring Boot**: implementação principal (avançada)
- **Quarkus**: planejado (`quarkus/` ainda não incluído)
- **Frontend**: fora do escopo deste repositório público
