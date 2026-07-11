# Notificação Admin UI

## Classificação (planejada)

- **DDD:** Supporting
- **Camada:** backend + frontend

## Objetivo

Interface admin para consultar fila de notificações, reenviar, editar templates e configurações (
`NotificacaoConfiguracao`).

## Evidências atuais

- Enum `SISTEMAS.NOTIFICACAO` — sem rota/UI
- Backend `domain-notification` funcional (API + listeners + cron)
- Templates Freemarker em `freemarker-templates/mail/notificacao/`

## Implementação sugerida

1. Estender `NotificacaoController` se faltar endpoints de listagem/detalhe/reenvio.
2. Entity slice `entities/notificacao` no frontend (proxy).
3. Pages: listagem, detalhe, preview de template.
4. Documentar em `docs/domains/supporting/notificacao.md` após entrega.

## Referências

- [supporting/notificacao.md](../supporting/notificacao.md)
