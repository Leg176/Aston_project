FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/main-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]