# OpenBao — secret manager

**Escolha do starter:** [OpenBao](https://openbao.org/) (fork OSS do Vault, MPL-2.0, OpenSSF) é o source of truth de secrets em runtime.

Provisionamento: repositório [`homelab-iac`](https://github.com/HerculanoLeo/homelab-iac) — VM `homelab-openbao` (`10.0.1.23`), role Ansible `openbao`, FQDN `openbao.lodh8.com`.

## Por quê

- Licença **MPL-2.0** (OSI); ACL/OIDC/policies sem paywall de custom roles.
- Modelo B: AppRole + Agent — secrets de runtime **não** ficam no `.env` versionado/Jenkins.
- Paths: `secret/<projeto>/<app>/<env>`.

## Stack lab

```text
Laptop → Pi-hole DNS (openbao.<domínio>)
           → Nginx TLS (Pi) → VM OpenBao :8200
                └─ Raft em /opt/homelab/openbao/data
```

| Item | Valor |
|------|--------|
| Hostname / IP / VMID | `homelab-openbao` / `10.0.1.23` / `203` |
| Seal | `static` + chave no `vault.yml` Ansible (gitignored) |
| KV | v2 em `secret/` |
| Auth UI | OIDC Keycloak + RBAC Identity Groups |
| Machine identity | AppRole (`<project>-<service>-<env>`) |

## Fluxo com o starter

1. Seed KV em `homelab-iac` → `ansible/inventory/group_vars/proxmox_openbao/kv_starter.yml`.
2. Deploy via [`jenkins-ansible-pipelines`](https://github.com/HerculanoLeo/jenkins-ansible-pipelines): Agent monta `/openbao`; compose usa imagem `lodh8/otel-collector` com Bao Agent quando há `agent.hcl`.
3. Jenkins Secret file (`develop-env-secrets`, …) só carrega **bootstrap** AppRole (`openbao_approle_creds`), não passwords de app.

Detalhes operacionais: README / `docs/secrets` do `homelab-iac`. Deploy: `docs/projetos/starter` no repo ansible-pipelines.
