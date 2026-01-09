# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# CSVs are packaged inside the JAR by Maven, so no need to copy separately
# COPY --from=build /app/src/main/resources/*.csv /app/resources/
# Spring Boot puts src/main/resources into the JAR.
# But for the dynamic "add file later" we might need a volume. For now, embedding is fine.

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
