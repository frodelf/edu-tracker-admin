FROM openjdk:17.0.2-jdk-slim-buster
COPY target/*.jar edu-tracker-admin.jar
ENTRYPOINT ["java", "-jar", "edu-tracker-admin.jar"]

