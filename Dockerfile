FROM openjdk:18-alpine
ADD target/*.jar app.jar
COPY . /app
ENTRYPOINT [ "java", "-jar", "app.jar" ]