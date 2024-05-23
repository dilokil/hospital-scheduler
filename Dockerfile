FROM bellsoft/liberica-openjdk-alpine:11.0.19
WORKDIR /app
COPY ./src/main/resources/db/migration ./db/migration
COPY ./src/main/resources/application.properties ./
COPY ./target/*.jar ./

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hospital-scheduler.jar"]