FROM eclipse-temurin:25-jre

RUN apt-get -y update; apt-get -y install curl;

COPY backend/api-cadastros/target/app.jar /servers/docker/app/runtime/app.jar

WORKDIR /servers/docker/app/runtime/

HEALTHCHECK --interval=1m --timeout=15s --retries=5 \
    CMD curl --fail http://localhost:8080/api/v1/actuator/health | grep UP || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
