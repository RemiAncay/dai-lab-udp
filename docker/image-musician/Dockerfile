FROM alpine:latest
RUN apk add --no-cache openjdk21
WORKDIR /app
COPY musician-app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]