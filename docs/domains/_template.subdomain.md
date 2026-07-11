# Template — Subdomínio

Use este template ao documentar um novo subdomínio. Copie para o path adequado em `docs/domains/`.

---

# {Nome do Subdomínio}

## Classificação

- **DDD:** Core | Supporting | Generic
- **Camada:** backend | BFF | ambos

## Responsabilidade

{2–3 frases sobre o que o subdomínio faz.}

## Regras de negócio

- {Invariante ou validação 1}
- {Fluxo principal}
- {Restrições}

## Estrutura de código

### Backend

```
{árvore de pastas relevante}
```

### Frontend

```
{árvore de pastas relevante, ou "N/A"}
```

## Onde encontrar

| Artefato   | Path     |
|------------|----------|
| Controller | `{path}` |
| Service    | `{path}` |
| Domain     | `{path}` |
| Tests      | `{path}` |
| Migrations | `{path}` |

## Contratos

### REST

| Método | Path          | Descrição |
|--------|---------------|-----------|
| GET    | `/api/v1/...` | ...       |

### Eventos

- **Publica:** `{EventName}`
- **Consome:** `{EventName}`

### Frontend (Zod / DTOs)

- `{schema}.ts`

## Dependências

- **Subdomínios:** {lista}
- **Modulith:** {named interfaces}
- **Externos:** {Keycloak, SMTP, etc.}

## Testes

- `{TestClass}.java` ou `{file}.test.ts`
- Ao alterar: cobrir {cenários}

## Implementação futura

- {Gap ou TODO}
- {Evolução planejada}
