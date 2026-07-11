# java-hexagonal-architecture — Guia para Agentes

Starter público de **painel administrativo SaaS** multi-usuário: backend Java (**Spring Boot 4.0.7** + Modulith 2.0) +
Keycloak.

> Repositório **anonimizado** (sem credenciais reais). Frontend TanStack Start fica fora deste repo; as specs em
`docs/domains/` (incluindo BFF) são mantidas como referência.

## Comece aqui

| Recurso                                            | Descrição                                                                |
|----------------------------------------------------|--------------------------------------------------------------------------|
| [**Catálogo de domínios**](docs/domains/README.md) | **Referência principal** — subdomínios, paths no código, regras, roadmap |
| [Backend AGENTS.md](spring-boot/backend/AGENTS.md) | API Java, Modulith, endpoints                                            |

## Estrutura do repositório

```
spring-boot/
  backend/           # Maven multi-module (Spring Boot + Modulith)
  dependencias/      # Keycloak realm + OpenTelemetry
  docker-compose.yaml
docs/domains/        # Documentação / specs por domínio
```

## Cursor rules

### Backend (`spring-boot/backend/.cursor/rules/`)

| Rule                       | Foco                            |
|----------------------------|---------------------------------|
| `backend-architecture.mdc` | Hexagonal, Modulith, convenções |
| `backend-domains.mdc`      | DDD, bounded contexts           |
| `backend-testing.mdc`      | JUnit, JaCoCo                   |

## Fluxo de dados

```
Cliente → Backend Java /api/v1 (IAM, localização, notificação, segurança)
        → Keycloak (OAuth)
```

## Comandos úteis

```bash
cd spring-boot/backend && mvn test
cd spring-boot/backend && mvn -pl api-cadastros -am spring-boot:run
```

## Novo subdomínio

1. Documentar em [`docs/domains/`](docs/domains/README.md) (copiar `_template.subdomain.md`).
2. Implementar backend conforme classificação.
3. Atualizar rules `*-domains.mdc` se necessário.
