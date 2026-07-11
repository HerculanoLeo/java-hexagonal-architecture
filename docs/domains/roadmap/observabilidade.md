# Observabilidade Admin

## Classificação (planejada)

- **DDD:** Generic
- **Camada:** BFF + APIs externas

## Objetivo

Painel admin para métricas, health e traces (Grafana / Actuator / OpenTelemetry).

## Evidências atuais

- Backend: Actuator (`health`, `info`, `refresh`) + push OTLP in-process (métricas Micrometer, traces Micrometer
  Tracing, logs via Logback `OpenTelemetryAppender`)
- OpenTelemetry collector no `docker-compose.yaml` (sem Java agent; sem scrape Prometheus / client OAuth de
  observabilidade)

## Implementação sugerida

1. Embed Grafana links/deeplinks (dados já chegam via OTLP → collector → backends).
2. Page `/administracao/observabilidade` com iframe/deeplinks.
3. Guard com role admin existente (não há mais `observabilidade-sistemas` para scrape).

## Referências

- [generic/api-cadastros.md](../generic/api-cadastros.md) — Actuator + OTLP in-process
