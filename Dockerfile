FROM maven:3.9.6-eclipse-temurin-21 as build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean -Dmaven.test.skip package

FROM bellsoft/liberica-openjdk-debian:latest

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build target/PartyPurse-0.0.1-SNAPSHOT.jar ./partypurse.jar

ENTRYPOINT ["java", "-jar", "./partypurse.jar"]