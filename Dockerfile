FROM openjdk:13-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
ARG PORT=8098

COPY ${JAR_FILE} app.jar
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app.jar"]
