# CEP

## Classificação

- **DDD:** Supporting
- **Camada:** backend

## Responsabilidade

Consulta de endereço por CEP via APIs brasileiras (Brasil API, ViaCEP) com cache.

## Regras de negócio

- CEP normalizado e validado antes da consulta.
- Cache (`CEPCacheService`) para reduzir chamadas externas.
- Fallback entre providers Feign.

## Onde encontrar

| Artefato   | Path                                                         |
|------------|--------------------------------------------------------------|
| Controller | `domain-location/.../cep/api/controller/CEPController.java`  |
| Providers  | `domain-location/.../cep/infra/CEPBrasilAPIsV1Provider.java` |
| Cache      | `domain-location/.../cep/infra/CEPCacheService.java`         |
| Tests      | `domain-location/src/test/.../CEPControllerTest.java`        |

## Contratos

### REST

| Método | Path                            | Descrição     |
|--------|---------------------------------|---------------|
| GET    | `/api/v1/localizacao/cep/{cep}` | Consultar CEP |

## Implementação futura

- Entity slice frontend + componente de endereço reutilizável
