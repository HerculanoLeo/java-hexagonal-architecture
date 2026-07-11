# Notificação

## Classificação

- **DDD:** Supporting
- **Camada:** backend

## Responsabilidade

Envio de e-mails transacionais (boas-vindas, troca de senha, ativação/inativação), templates Freemarker, fila com retry
cron.

## Regras de negócio

- Notificações registradas e enviadas assincronamente.
- Templates configuráveis via `NotificacaoConfiguracao` (Liquibase seeds).
- Retry até `max-tentativas` configurável; cron de reenvio.
- Listeners reagem a eventos de outros módulos (`NotificacaoBoasVindasEvent`, `NotificacaoTrocaSenhaEvent`,
  `NotificacaoUsuarioAtivadoEvent`, `NotificacaoUsuarioInativadoEvent`).

## Estrutura de código

```
backend/domain-notification/src/main/java/com/lodh8/starter/notification/
  api/controller/NotificacaoController.java
  app/NotificacaoService.java
  domain/events/NotificacaoBoasVindasEvent.java
  infra/NotificacaoEventListeners.java
  infra/persistence/NotificacaoEntity.java
backend/domain-notification/src/main/resources/freemarker-templates/
  mail/layout/          # layout base HTML
  mail/fragments/       # header, footer, botões, etc.
  mail/notificacao/     # templates por evento
  resources/styles.ftlh # CSS compartilhado (cores neutras)
backend/domain-notification/src/main/resources/db/changelog/notification/
```

## Onde encontrar

| Artefato     | Path                                                                                        |
|--------------|---------------------------------------------------------------------------------------------|
| Controller   | `domain-notification/.../NotificacaoController.java`                                        |
| Listeners    | `domain-notification/.../NotificacaoEventListeners.java`                                    |
| Templates    | `domain-notification/src/main/resources/freemarker-templates/mail/notificacao/*.ftlh`       |
| Layout / CSS | `freemarker-templates/mail/layout/`, `mail/fragments/`, `resources/styles.ftlh`             |
| Liquibase    | `domain-notification/src/main/resources/db/changelog/notification/` (schema `notification`) |
| Config       | `api-cadastros/src/main/resources/application.yaml` (`api.notificacao`)                     |
| Tests        | `domain-notification/src/test/.../NotificacaoRepositoryAdapterTest.java`                    |

## Contratos

### REST (`/api/v1/notificacao`)

| Método | Path                              | Authority                                | Descrição                                                                                                                        |
|--------|-----------------------------------|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| GET    | `/notificacao`                    | `admin-sistemas`, `notificacao-sistemas` | Listar notificações                                                                                                              |
| GET    | `/notificacao/{id}`               | `admin-sistemas`, `notificacao-sistemas` | Detalhe                                                                                                                          |
| POST   | `/notificacao/email/teste`        | `admin-sistemas`                         | Envia e-mail de teste (template genérico) ao usuário autenticado                                                                 |
| POST   | `/notificacao/teste/email/{tipo}` | `admin-sistemas`                         | Envia preview de template (`TESTE`, `BOAS_VINDAS`, `TROCA_SENHA`, `USUARIO_ATIVADO`, `USUARIO_INATIVADO`) ao usuário autenticado |

Sem UI no frontend — uso via Swagger/curl.

### Eventos (named interface `notification::events`)

- **Consome:** `NotificacaoRegisterEvent`, `NotificacaoBoasVindasEvent`, `NotificacaoTrocaSenhaEvent`,
  `NotificacaoUsuarioAtivadoEvent`, `NotificacaoUsuarioInativadoEvent`
- **Publica:** `NotificacaoRegisterEvent` (ciclo interno de envio)

## Dependências

- `shared-kernel` (enums `FreemarkerTemplate`, `TipoNotificacao`)
- SMTP, Freemarker
- Publicado por: `domain-backoffice` (boas-vindas, ativar/inativar), `domain-authorize` (troca senha)

## Testes

- `NotificacaoRepositoryAdapterTest`, tests de service

## Implementação futura

- UI admin de notificações — ver [roadmap/notificacao-admin-ui.md](../roadmap/notificacao-admin-ui.md)
- Canais além de e-mail (SMS, push)
