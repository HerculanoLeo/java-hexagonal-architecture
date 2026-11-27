# OpenBao — integração

Este starter usa **[OpenBao](https://openbao.org/)** (API compatível com Vault) como source of truth de **config runtime + secrets**. A API (`api-cadastros`) sobe atrás do **OpenBao Agent**: o Agent autentica (AppRole), materializa um **YAML Spring** no tmpfs e só então o JAR sobe. Alterações no KV atualizam o ficheiro; o Spring faz **live reload** sem reiniciar o container.

Motivos principais:

- Secrets e configs tunáveis **não** ficam no `.env` / imagem.
- Live reload (flags, URLs, passwords, mail, etc.) via ficheiro + `ContextRefresher`.
- Certificados (PFX/PEM) podem ser renderizados no mesmo volume tmpfs, sob demanda.

Perfil `local` continua sem OpenBao (`application-local.yml`).

## O que este repositório já faz

| Peça | Onde |
|------|------|
| Imagem com CLI `bao` + entrypoint | `Dockerfile` / `docker-entrypoint.sh` — Agent em background (modo file) ou PID 1 com `exec` (modo env) |
| Import do YAML runtime | `spring.config.import=optional:file:…/application-runtime.yml` + `OpenBaoRuntimeEnvironmentPostProcessor` (`YamlPropertySourceLoader`) |
| Watcher + refresh | `OpenBaoRuntimeConfigWatcher` → `ContextRefresher` |
| Beans recriáveis | DataSource, JavaMailSender, cliente Keycloak admin, CORS (`@RefreshScope`) |
| Refresh manual | `POST /api/v1/actuator/refresh` — authority `admin-sistemas` (`/actuator/health` continua público); só relê o YAML local (não puxa do OpenBao) |

Fluxo em runtime:

```text
KV (keys flat)
  → OpenBao Agent (AppRole; template → YAML)
      → /run/openbao/config/application-runtime.yml
      → (opcional) /run/openbao/certs/...
  → entrypoint espera o YAML, depois java -jar
  → Spring importa o YAML + FileWatcher → refresh in-process
```

KV v2 é *static secret*: o Agent **não** recebe evento no write — re-lê no `static_secret_render_interval` (no deploy tipicamente `30s`; default upstream `5m`). O Spring só reage quando o ficheiro no tmpfs muda.


## O que a infra / deploy precisa fornecer

Checklist para integrar este projeto a um OpenBao real:

1. **KV v2** com path por app/ambiente (`secret/<projeto>/<app>/<env>`), **mapa flat** de keys.
2. **AppRole** (ou auth equivalente) com policy de **read** só nesse path; `role_id` + `secret_id` no container.
3. **Montagem bootstrap**: `/openbao/agent.hcl`, `/openbao/role_id`, `/openbao/secret_id` (tipicamente `:ro`).
4. **`agent.hcl` em modo ficheiro** (`template { }` apenas — **não** misturar com `exec`/`env_template`):
   - template mapeia keys flat → árvore Spring em `/run/openbao/config/application-runtime.yml`;
   - **sem** bloco `exec` — o entrypoint sobe `bao agent` em background, espera o YAML e faz `exec java -jar`.
5. **tmpfs** (ou volume RW efémero) em `/run/openbao`.
6. **Keys** alinhadas ao mapa do template Agent (DB, Keycloak, CORS, mail, flags, OTEL app, …).
7. **Certificados (opcional):** `file_templates` (`kv_key` + `destination` + `encoding`).

Sem esses passos, o entrypoint cai no `java -jar` direto (local) e o watcher não arranca se `/run/openbao` não existir.

## Contrato das keys

- **KV flat** no OpenBao (ex.: `CADASTROS_DB_PASSWORD`, `KEYCLOAK_HOST`).
- O Agent **traduz** para YAML Spring (`spring.datasource.*`, `spring.mail.*`, `api.*`, …).
- Não colocar passwords no Compose `.env` quando o Agent estiver ativo — só plumbing de deploy.

## Limites neste starter

- Collector OTEL / sidecars que ainda usem `env_template` + restart do processo.
- Frontend (BFF) — secrets próprios; fora deste corte.
- Mudanças que exigem redeploy: `server.port`, `context-path`, imagem, changelogs Liquibase (só no boot).
