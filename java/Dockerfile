FROM maven:3.10.0-eclipse-temurin-17  AS builder
LABEL authors="Nicolas-End"


WORKDIR application/

COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk AS runner

WORKDIR application/

COPY --from=builder /application/target/*.jar spring-app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "spring-app.jar"]