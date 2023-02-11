FROM openjdk:17-alpine
COPY /target/*.jar grocery.jar
ENTRYPOINT ["java","-jar","grocery.jar"]

