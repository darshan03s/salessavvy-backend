FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar salessavvybackend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "salessavvybackend.jar"]
