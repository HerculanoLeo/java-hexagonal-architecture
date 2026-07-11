# Estado

## Classificação

- **DDD:** Supporting
- **Camada:** backend

## Responsabilidade

CRUD/listagem de estados (UF) brasileiros para referência em formulários.

## Onde encontrar

| Artefato   | Path                                                                                         |
|------------|----------------------------------------------------------------------------------------------|
| Controller | `domain-location/.../estado/api/controller/EstadoController.java`                            |
| Service    | `domain-location/.../estado/app/impl/EstadoServiceImpl.java`                                 |
| Entity     | `domain-location/.../estado/infra/persistence/EstadoEntity.java`                             |
| Liquibase  | `domain-location/src/main/resources/db/changelog/location/` (schema `location`, `tb_estado`) |
| Tests      | `domain-location/src/test/.../EstadoControllerTest.java`                                     |

## Contratos

### REST

| Método   | Path                              | Descrição          |
|----------|-----------------------------------|--------------------|
| GET      | `/api/v1/localizacao/estado`      | Listar             |
| GET      | `/api/v1/localizacao/estado/{id}` | Por ID             |
| POST/PUT | `/api/v1/localizacao/estado`      | Manutenção (admin) |

## Dependências

- Enum `EstadoSigla` em `shared-kernel`

## Implementação futura

- Combobox em formulários frontend
