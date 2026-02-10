# ===== Build stage =====
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ===== Runtime stage =====
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# ✅ FORCE JVM + OS TIMEZONE
ENV TZ=UTC
ENTRYPOINT ["java","-Duser.timezone=UTC","-jar","app.jar"]

COPY --from=build /app/target/garage-api-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
