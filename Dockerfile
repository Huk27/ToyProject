FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :api:bootJar --no-daemon
RUN ls -l /app/api/build/libs/
RUN ls -l /app/
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/api/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]