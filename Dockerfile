FROM openjdk:18-alpine
ADD target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]