# OpenBao — integração

Este starter usa **[OpenBao](https://openbao.org/)** (API compatível com Vault) como source of truth de **config runtime + secrets**. A API (`api-cadastros`) sobe atrás do **OpenBao Agent**: o Agent autentica (AppRole), materializa um ficheiro de propriedades e só então executa o JAR. Alterações no KV atualizam o ficheiro; o Spring faz **live reload** sem reiniciar o container.

Motivos principais:

- Secrets e configs tunáveis **não** ficam no `.env` / imagem.
- Live reload (flags, URLs, passwords, mail, etc.) via ficheiro + `ContextRefresher`.
- Certificados (PFX/PEM) podem ser renderizados no mesmo volume tmpfs, sob demanda.

Perfil `local` continua sem OpenBao (`application-local.yml`).

## O que este repositório já faz

| Peça | Onde |
|------|------|
| Imagem com CLI `bao` + entrypoint | `spring-boot/backend/api-cadastros/Dockerfile`, `docker-entrypoint.sh` — se existir `/openbao/agent.hcl`, PID 1 = `bao agent` |
| Import do ficheiro runtime | `application.yml` → `spring.config.import=optional:file:/run/openbao/config/application-runtime.properties` |
| Watcher + refresh | `OpenBaoRuntimeConfigWatcher` → `ContextRefresher` |
| Beans recriáveis | DataSource, JavaMailSender, cliente Keycloak admin, CORS (`@RefreshScope`) |
| Refresh manual | `POST /api/v1/actuator/refresh` — authority `admin-sistemas` (`/actuator/health` continua público) |
| Placeholders Spring | `${CADASTROS_*}`, `${KEYCLOAK_*}`, `SPRING_MAIL_*`, etc. — preenchidos pelas keys do ficheiro |

Fluxo em runtime:

```text
KV (keys flat)
  → OpenBao Agent (AppRole; PID 1)
      → /run/openbao/config/application-runtime.properties
      → (opcional) /run/openbao/certs/...
      → exec java -jar
  → Spring importa o .properties + FileWatcher → refresh in-process
```

## O que a infra / deploy precisa fornecer

Checklist para integrar este projeto a um OpenBao real:

1. **KV v2** com path por app/ambiente, por exemplo `secret/<projeto>/<app>/<env>`, **mapa flat** de keys (mesmos nomes que os placeholders Spring).
2. **AppRole** (ou auth equivalente) com policy de **read** só nesse path; `role_id` + `secret_id` disponíveis no container.
3. **Montagem bootstrap** no container:
   - `/openbao/agent.hcl`, `/openbao/role_id`, `/openbao/secret_id` (tipicamente `:ro` a partir do host).
4. **`agent.hcl` em modo ficheiro** (não só `env_template`):
   - template → `/run/openbao/config/application-runtime.properties` (`KEY=value` a partir do KV);
   - `exec` = `java -jar app.jar`;
   - `restart_on_secret_changes = "never"` (o reload é do Spring, não do processo).
5. **tmpfs** (ou volume RW efémero) em `/run/openbao` para o Agent gravar config/certs — separado do `/openbao` de bootstrap.
6. **Lista de keys** alinhada ao que a app lê (DB, Keycloak, CORS, mail, flags de notificação, OTEL app, …). Novas keys = KV + lista do Agent + placeholder/`application.yml` se necessário.
7. **Certificados (opcional):** entradas `file_templates` no Agent (`kv_key` + `destination` absoluto sob `/run/openbao` + `encoding` raw/base64); a app referencia o path via property.

Sem esses passos, o entrypoint cai no `java -jar` direto (útil em local) e o watcher não arranca se `/run/openbao` não existir.

## Contrato das keys

- Preferir **keys flat** no mesmo objeto KV (ex.: `CADASTROS_DB_PASSWORD`, `KEYCLOAK_HOST`), não um blob YAML único.
- O Agent gera `application-runtime.properties`; o Spring resolve `${…}` em `application.yml`.
- Não colocar passwords no Compose `.env` quando o Agent estiver ativo — só plumbing de deploy (nome do serviço OTEL, paths de volume, etc.).

## Limites neste starter

- Collector OTEL / sidecars que ainda usem `env_template` + restart do processo.
- Frontend (BFF) — secrets próprios; fora deste corte.
- Mudanças que exigem redeploy: `server.port`, `context-path`, imagem, changelogs Liquibase (só no boot).
