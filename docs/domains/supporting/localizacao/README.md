# Localização

## Classificação

- **DDD:** Supporting
- **Camada:** backend

## Responsabilidade

Dados geográficos brasileiros: CEP (consulta APIs externas), estados e municípios (cadastro referência).

## Subdomínios

- [cep.md](cep.md) — consulta CEP com cache
- [estado.md](estado.md) — UF
- [municipio.md](municipio.md) — municípios por estado

## Módulo

`backend/domain-location`

Schema PostgreSQL: `location` — Liquibase em `domain-location/src/main/resources/db/changelog/location/`.

## Dependências

- `shared-kernel` apenas (Modulith)
- APIs externas: Brasil API / ViaCEP (Feign)

## Implementação futura

- Uso em formulários de endereço no frontend admin
- Integração com multi-tenant (endereços por organização)
