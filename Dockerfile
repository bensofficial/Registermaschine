# https://dzone.com/articles/build-package-and-run-spring-boot-apps-with-docker
FROM maven:3.8.6-jdk-11-slim AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-slim-bullseye

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
