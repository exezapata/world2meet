FROM openjdk:21-slim

WORKDIR /app

RUN apt-get update && apt-get install -y netcat-openbsd

COPY target/spaceships-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT  ["java", "-jar", "app.jar"]
