FROM maven:3.9.6-sapmachine-11 as builder
WORKDIR /build
COPY /src ./src
COPY pom.xml ./
RUN mvn -B clean package

FROM bellsoft/liberica-openjdk-alpine:11.0.19
WORKDIR /app
COPY --from=builder /build/target/*.jar ./

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hospital-scheduler.jar"]