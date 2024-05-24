FROM maven:3.9.6-sapmachine-11 as builder
WORKDIR /build
COPY /src ./src
COPY pom.xml ./
RUN mvn -B clean package

FROM bellsoft/liberica-openjdk-alpine:11.0.19
WORKDIR /app
#COPY ./src/main/resources/db/migration ./db/migration
#COPY ./src/main/resources/application.properties ./
COPY --from=builder ./target/*.jar ./

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hospital-scheduler.jar"]