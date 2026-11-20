# Keycloak — starter (DEV)

| Arquivo | Uso |
|---------|-----|
| [`starter-realm.json`](starter-realm.json) | Import do realm |

```bash
kc.sh import --file starter-realm.json
```

Admin: `administrador@dummy.comx` / `DummyPass1`

`starter-api-cadastros` precisa do client scope **`kc-admin-api`** (default) — Keycloak ≥26.8.2 rejeita Admin REST com 401 sem ele, mesmo com `realm-admin` e login ok.
