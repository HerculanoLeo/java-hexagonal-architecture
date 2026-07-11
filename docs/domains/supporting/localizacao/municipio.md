# Município

## Classificação

- **DDD:** Supporting
- **Camada:** backend

## Responsabilidade

CRUD/listagem de municípios vinculados a estados.

## Onde encontrar

| Artefato   | Path                                                                                            |
|------------|-------------------------------------------------------------------------------------------------|
| Controller | `domain-location/.../municipio/api/controller/MunicipioController.java`                         |
| Service    | `domain-location/.../municipio/app/impl/MunicipioServiceImpl.java`                              |
| Entity     | `domain-location/.../municipio/infra/persistence/MunicipioEntity.java`                          |
| Liquibase  | `domain-location/src/main/resources/db/changelog/location/` (schema `location`, `tb_municipio`) |
| Tests      | `domain-location/src/test/.../MunicipioControllerTest.java`                                     |

## Contratos

### REST

| Método   | Path                                 | Descrição                  |
|----------|--------------------------------------|----------------------------|
| GET      | `/api/v1/localizacao/municipio`      | Listar (filtro por estado) |
| GET      | `/api/v1/localizacao/municipio/{id}` | Por ID                     |
| POST/PUT | `/api/v1/localizacao/municipio`      | Manutenção                 |

## Dependências

- Subdomínio estado (FK estado)

## Implementação futura

- Busca autocomplete no frontend
- Carga IBGE automatizada
