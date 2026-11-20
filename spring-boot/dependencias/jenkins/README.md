# Jenkins — starter

Scripts de Pipeline do projeto **starter**, executados pelas shared libraries Jenkins (`github-trigger`, `build-shared-library`, `deploy-shared-library`).

```text
GitHub webhook
  → job trigger  (github-trigger.jenkinsfile)
  → job build    (build.jenkinsfile)
  → job deploy   (deploy.jenkinsfile)  → ansible-pipelines
```

## Arquivos

| Arquivo | Job Jenkins sugerido | Library |
|---------|----------------------|---------|
| [`github-trigger.jenkinsfile`](github-trigger.jenkinsfile) | `lodh8/starter/github-trigger` | `github-trigger` |
| [`build.jenkinsfile`](build.jenkinsfile) | `lodh8/starter/build` | `build-shared-library` |
| [`deploy.jenkinsfile`](deploy.jenkinsfile) | `lodh8/starter/deploy` | `deploy-shared-library` |

No Jenkins: pasta `lodh8` → pasta `starter` (Display Name **LODH8 / Starter**) → pipelines com o mesmo nome dos arquivos (sem `.jenkinsfile`).

Os scripts usam paths absolutos `lodh8/starter/build` e `lodh8/starter/deploy` no `build job:`.

## Pré-requisitos

### Shared libraries no Jenkins

Cadastre as Global Pipeline Libraries (Modern SCM) conforme o README do repo `jenkins-shared-libraries`:

| Nome | Library Path |
|------|--------------|
| `github-trigger` | `github-trigger` |
| `build-shared-library` | `build` |
| `deploy-shared-library` | `deploy` |

Agents: label `worker` (build e deploy). O `github-trigger` continua em `agent any` (fixado na shared library).

### Version API

Registrar o alias `starter` por ambiente (`develop`, `staging`, `production`) antes da primeira build. A lib busca/atualiza semver em `{versionApiUri}/version/starter/{env}`.

### deploy-ansible-pipelines (GitHub)

O deploy faz checkout de `https://github.com/HerculanoLeo/deploy-ansible-pipelines.git` com a credencial `github-herculano`. É necessário existir:

- `ansible/ansible.cfg`
- `ansible/playbooks/deploy.yml`
- inventário em `ansible/inventories/{develop|staging|production}/`
- host limit `{project}-{environment}` → ex.: `starter-staging`

`DeployChoices` na shared library já inclui o slug `starter`.

### Credenciais Jenkins (IDs usados pelos scripts)

| ID | Uso |
|----|-----|
| `github-herculano` | Checkout da app, shared libraries e `deploy-ansible-pipelines` (GitHub App). Em **Avançado**, use *Specify accessible repositories* com Owner `HerculanoLeo` — *Infer accessible repository* **quebra** o load de Pipeline Libraries |
| `pipeline` | Auth HTTP da Version API + become Ansible |
| `dockerhubaccount` | Push das imagens no build (default da lib) |
| `dockerhub` | Login no registry no deploy |
| `pipeline-maven-settings` | Config File Provider — `settings.xml` (GitHub Packages / deps privadas) |
| `develop-env-secrets` / `staging-env-secrets` / `production-env-secrets` | **Opcional** — Secret file stub (pode ficar em branco). Destino: **Infisical** como secret manager; o arquivo Jenkins é só ponte até lá |
| `proxmox-*-ssh` / `ec2-prd-01-ssh` | SSH por ambiente (ajuste os IDs reais) |

Substitua `REPLACE_ME` nos webhooks Discord de build e deploy.

Na shared library, `secretVarsCredentialId` é opcional: se omitido, o Ansible roda sem `-e @$SECRET_VARS_FILE`. Com ID apontando para um Secret file vazio, o job valida a credencial sem injetar vars reais.

## Generic Webhook Trigger (job `github-trigger`)

**Não basta** apontar o webhook do GitHub para a URL do job. O job usa o plugin **Generic Webhook Trigger**.

| Param | JSONPath |
|-------|----------|
| `GIT_BRANCH_PUSH` | `$.ref` |
| `GIT_BRANCH_MERGE` | `$.pull_request.base.ref` |

Token do job (já configurado no Jenkins): `lodh8-starter-github`

URL do webhook no GitHub:

```text
https://jenkins.lodh8.com/generic-webhook-trigger/invoke?token=lodh8-starter-github
```

Eventos sugeridos: **Push** e **Pull request**. Content type: `application/json`.

Mapeamento de ambiente (shared library):

- branch `develop` → `develop` (trigger default **off**)
- branch `staging` → `staging` (on)
- tag semver `X.Y.Z` → `production` (on)

O checkbox “GitHub hook trigger for GITScm polling” **não** é o mecanismo deste fluxo.

## O que a build produz

| Etapa | Detalhe |
|-------|---------|
| Maven | `./spring-boot/backend/pom.xml` → `clean package -DskipTests` (JAR `api-cadastros/target/app.jar`) |
| Node | `frontend/gestao` → `pnpm install` + `pnpm build` (artefatos `dist/`, Prisma generate) |
| Docker | `lodh8/starter-api-cadastros` (context `spring-boot`) e `lodh8/starter-webapp-gestao` (context `frontend/gestao`) |

Tags de imagem vêm da Version API (`X.Y.Z-develop` / `X.Y.Z-staging` / `X.Y.Z` em production).

Compose local (`spring-boot/docker-compose.yaml`) usa nomes sem namespace Docker Hub (`starter-api-cadastros:${VERSION}`); em runtime o Ansible deve puxar `lodh8/starter-*` (ou retag conforme o inventário).

## Deploy

Último elo do fluxo. Com `autoDeployEnvs` (default da lib: `staging`), a build dispara o job `deploy` automaticamente após push bem-sucedido nesse ambiente. Deploy manual: rode `starter/deploy` com `DEPLOY_ENV` e `IMAGE_VERSIONS`.

Antes do primeiro deploy: SSH por ambiente e, se quiser manter o stub, Secret files vazios (`develop-env-secrets`, etc.). Secrets reais virão do Infisical.
