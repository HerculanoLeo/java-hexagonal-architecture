# Shared Kernel

## Classificação

- **DDD:** Generic
- **Camada:** backend

## Responsabilidade

Tipos, exceções, utilitários e infraestrutura de eventos compartilhados entre todos os módulos `domain-*`.

## Conteúdo principal

| Pacote                      | Conteúdo                                                           |
|-----------------------------|--------------------------------------------------------------------|
| `shared/models/enums`       | `Status`, `TipoAcesso`, `TipoNotificacao`, `EstadoSigla`, …        |
| `shared/exceptions`         | `NotFoundException`, `ConflictException`, `BadRequestException`, … |
| `shared/events/app`         | `EventPublisherPort`                                               |
| `shared/utils`              | Helpers transversais                                               |
| `shared/models/annotations` | Anotações compartilhadas                                           |

## Onde encontrar

```
backend/shared-kernel/src/main/java/com/herculanoleo/starter/shared/
backend/shared-kernel/src/test/java/...
```

Modulith: `@ApplicationModule(id = "shared")` — **sem** `allowedDependencies` (raiz).

## Regras

- **Não** colocar lógica de negócio de bounded context aqui.
- Novos enums compartilhados só se usados por 2+ módulos.
- Exceções de domínio específicas ficam no módulo dono.

## Testes

- `AttributesMapperTest` e tests de utils

## Implementação futura

- Contratos de paginação server-side compartilhados se padronizar no backend
