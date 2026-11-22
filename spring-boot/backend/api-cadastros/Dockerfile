FROM eclipse-temurin:25-jre

ARG OPENBAO_VERSION=2.7.1
ARG TARGETARCH

RUN apt-get -y update \
    && apt-get -y install --no-install-recommends curl ca-certificates tar \
    && ARCH="$(uname -m)" \
    && case "$ARCH" in \
         x86_64|amd64) BAO_ARCH=amd64 ;; \
         aarch64|arm64) BAO_ARCH=arm64 ;; \
         *) echo "unsupported arch: $ARCH" >&2; exit 1 ;; \
       esac \
    && curl -fsSL \
         "https://github.com/openbao/openbao/releases/download/v${OPENBAO_VERSION}/openbao_${OPENBAO_VERSION}_linux_${BAO_ARCH}.tar.gz" \
         -o /tmp/openbao.tar.gz \
    && mkdir -p /tmp/openbao \
    && tar -xzf /tmp/openbao.tar.gz -C /tmp/openbao \
    && install -m 0755 "$(find /tmp/openbao -type f -name bao | head -n1)" /usr/local/bin/bao \
    && rm -rf /tmp/openbao /tmp/openbao.tar.gz \
    && rm -rf /var/lib/apt/lists/*

COPY backend/api-cadastros/target/app.jar /servers/docker/app/runtime/app.jar
COPY backend/api-cadastros/docker-entrypoint.sh /servers/docker/app/runtime/docker-entrypoint.sh

WORKDIR /servers/docker/app/runtime/
RUN chmod +x docker-entrypoint.sh

HEALTHCHECK --interval=1m --timeout=15s --start-period=90s --retries=5 \
    CMD curl --fail http://127.0.0.1:8080/api/v1/actuator/health | grep UP || exit 1

EXPOSE 8080

ENTRYPOINT ["./docker-entrypoint.sh"]
