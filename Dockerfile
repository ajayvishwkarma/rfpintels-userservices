FROM openjdk:11-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rfpintels-userservices.jar
ENTRYPOINT java -Dspring.profiles.active=${ENV} -jar rfpintels-userservices.jar