# API Cadastros

## Classificação

- **DDD:** Generic (composition root)
- **Camada:** backend

## Responsabilidade

Ponto de entrada Spring Boot que agrega todos os módulos de domínio. Configuração, segurança, Liquibase e
observabilidade — **sem regra de negócio**.

## Onde encontrar

| Artefato                        | Path                                                                                          |
|---------------------------------|-----------------------------------------------------------------------------------------------|
| Application                     | `backend/api-cadastros/src/main/java/com/lodh8/starter/Application.java`               |
| Config                          | `backend/api-cadastros/src/main/java/com/lodh8/starter/cadastros/infra/configuration/` |
| ValidationConfig (SentinelFlow) | `backend/api-cadastros/.../configuration/ValidationConfig.java`                               |
| Liquibase master                | `backend/api-cadastros/src/main/resources/db/changelog/db.changelog-master.yaml`              |
| Liquibase platform/modulith     | `backend/api-cadastros/src/main/resources/db/changelog/{platform,modulith}/`                  |
| application.yaml                | `backend/api-cadastros/src/main/resources/application.yaml`                                   |
| Dockerfile                      | `backend/api-cadastros/Dockerfile`                                                            |
| Modulith test                   | `backend/api-cadastros/src/test/java/com/lodh8/starter/ApplicationTest.java`           |

## Configuração relevante

- **Runtime:** Spring Boot **4.0.7**, Spring Modulith **2.0.0**
- **Context path:** `/api/v1`
- **OAuth2 RS:** JWT Keycloak (`spring.security.oauth2.resource-server.jwt`)
- **Datasource + Liquibase:** PostgreSQL **18+** (`spring-boot-starter-liquibase`); master agrega changelogs dos
  módulos; schemas de domínio `backoffice`, `notification`, `location`, `security`; `event_publication` em
  `public` (Spring Modulith)
- PKs UUID de domínio: `uuidv7()` gerado pelo banco
- Roles: MG (`CADASTROS_DB_MG_*`) executa migrations; app (`CADASTROS_DB_USERNAME`) recebe grants via parâmetro
  `app_role`
- **OpenAPI:** `/openapi/swagger-ui` (springdoc 3.x)
- **Actuator:** health, info, refresh
- **Observabilidade:** push OTLP in-process (`spring-boot-starter-opentelemetry` + Logback `OpenTelemetryAppender`) →
  collector; sem Java agent
- **Enums:** `@EnableMapperEnum` + spring-mapper-enum **2.0.0**
- **Validação:** SentinelFlow beans + `CommonExceptionHandler` → 400 por campo

## Módulos agregados

`shared-kernel`, `domain-identity`, `domain-authorize`, `domain-backoffice`, `domain-location`, `domain-notification`,
`domain-security`

## Deploy

- Docker Compose: `cadastros` (9000→8080), `gestao` (5137→3000), `otel-collector`
- Imagens: `backend/api-cadastros/Dockerfile` (JAR pré-buildado), `frontend/gestao/Dockerfile` (runtime; `pnpm build` na
  pipeline antes do `docker build`)
- Variáveis: ver `.env.example` na raiz
- OTEL: push in-process → collector (`dependencias/opentelemetry/config-cloud.yaml`) → Grafana Cloud

## Implementação futura

- Profile-specific configs para multi-tenant
- Gateway externo se extrair microserviços
- Validators SentinelFlow para notificação e localização
