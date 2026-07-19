FROM maven:3.8.8-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .

COPY calculator/pom.xml calculator/pom.xml
COPY gateway/pom.xml gateway/pom.xml
COPY deal/pom.xml deal/pom.xml
COPY statement/pom.xml statement/pom.xml
COPY dossier/pom.xml dossier/pom.xml

RUN mvn -pl gateway dependency:go-offline

COPY gateway/src gateway/src
RUN mvn -pl gateway clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
