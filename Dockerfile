FROM openjdk:17-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app/conta.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/conta.jar"]