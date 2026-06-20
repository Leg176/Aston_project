FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY target/eureka-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]