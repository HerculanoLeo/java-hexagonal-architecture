FROM eclipse-temurin:25 AS base-otel
ADD https://github.com/grafana/grafana-opentelemetry-java/releases/download/v2.21.0/grafana-opentelemetry-java.jar /servers/docker/otel/opentelemetry-javaagent.jar
RUN apt-get -y update; apt-get -y install curl;

FROM base-otel AS runtime
COPY backend/api-cadastros/target/app.jar /servers/docker/app/runtime/app.jar

WORKDIR /servers/docker/app/runtime/

ENV OTEL_JAVAAGENT_ENABLED=true
ENV OTEL_TRACES_SAMPLER="parentbased_traceidratio"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://otel:4318"
ENV OTEL_EXPORTER_OTLP_PROTOCOL="http/protobuf"
ENV OTEL_TRACES_SAMPLER_ARG="0.2"
ENV JAVA_TOOL_OPTIONS="-javaagent:/servers/docker/otel/opentelemetry-javaagent.jar"

HEALTHCHECK --interval=1m --timeout=15s --retries=5 \
    CMD curl --fail http://localhost:8080/actuator/health | grep UP || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]