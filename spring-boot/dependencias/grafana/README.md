# Grafana — starter

Importar os dashboards em `dependencias/grafana/dashboards/` no Grafana (**Dashboards → Import**). Após alterar os JSON, reimportar (ou sobrescrever) no Grafana Cloud.

**Containers:** `starter-api-cadastros`, `starter-webapp-gestao` e `starter-otel-collector`. Namespace `starter` (`COMPOSE_PROJECT_NAME` / `NAMESPACE`).

## Sinais por tipo de app

| Necessidade | Fonte | Onde |
|---|---|---|
| Online / uptime | Docker `container_uptime_seconds` (`container_name`) | Infra: Container Status / Uptime |
| Memória do container | Docker `container_memory_usage_*` | Infra: Memory Usage |
| Logs | Loki via atributos OTel (`service_name`, MDC `uid`) | App: Application Logs (+ filtro **UID**) |
| HTTP / JVM / Hikari / logback | Micrometer OTLP | App + Infra (só apps com registry OTLP) |

**Status e uptime** vêm do Docker. Série presente nos últimos 2 minutos significa container em execução. O valor é o tempo desde o start; um reinício zera o contador. Container parado some da API de stats e o painel fica OFFLINE. Esse sinal não lê o healthcheck do Docker nem o `/actuator/health`: processo no ar com a aplicação quebrada continua ONLINE.

**BFF (`starter-webapp-gestao`):** Node/TanStack — use logs e traces. Painéis HTTP Micrometer (`http_server_requests_*`) e JVM/Hikari preenchem sobretudo o `starter-api-cadastros`.

## Métricas Micrometer (Spring Boot)

HTTP, JVM, Hikari e logback dependem de Micrometer OTLP com as tags `application`, `namespace` e `environment`.

Família HTTP (timer Micrometer → Prometheus via OTLP):

- Contagem / taxa: `http_server_requests_milliseconds_count` → `rate(...[2m])` com unit `reqps`
- Latência média: `rate(..._milliseconds_sum) / rate(..._milliseconds_count)` com unit `ms`

Não usar `http_server_requests_count_total` nem `http_server_requests_sum_milliseconds_total` (não existem nesse pipeline). Throughput **não** deve usar `rate(*_milliseconds_sum)` — isso é ms acumulados/s (≈ RPS × latência_ms) e infla o gráfico (~1000× com latência ~1s).

CPU do container: `container_cpu_utilization_ratio` é gauge (0–1). Usar `avg_over_time` (não `rate`) e unit `percent` quando a query já multiplica por 100. O “limite” de memória no painel infra é `container_memory_usage_limit_bytes` (cgroup), não a RAM do host.

## Filtro UID (logs)

No dashboard **App**, variável **UID** (textbox). Cole o `uid` do JSON de erro 4xx/5xx ou de `[%X{uid}]` no log.

Requer o appender OTEL com `captureMdcAttributes=uid,usuario` (`logback-spring.xml`). Sem isso o atributo não chega ao Loki e o filtro não encontra linhas. Reimporte o JSON do dashboard após alterações.

## Importar no Grafana Cloud

1. Configure `GRAFANA_CLOUD_*` no `.env` do Compose.
2. **Dashboards → New → Import** → upload de cada JSON.
3. Remapeie datasources para a stack (`grafanacloud-herculanoleo-prom` / `grafanacloud-herculanoleo-logs` / `grafanacloud-herculanoleo-traces` ou equivalentes).
4. Ajuste variáveis `namespace` / `application` / `environment` se necessário.

## Collector

Collector de referência: `dependencias/opentelemetry/config-cloud.yaml` (imagem `otel/opentelemetry-collector-contrib:0.153.0`). O receiver `docker_stats` lê `/var/run/docker.sock` e envia `container.uptime`, filtrado por `COMPOSE_PROJECT_NAME`. Compose: `spring-boot/docker-compose.yaml`.
