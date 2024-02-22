
#
#COPY src src
#COPY pom.xml pom.xml
#
#RUN mvn clean -Dmaven.test.skip package
#
#FROM bellsoft/liberica-openjdk-debian:latest
#
#RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
#USER spring-boot
#
#WORKDIR /app
#
#COPY --from=build target/PartyPurse-0.0.1-SNAPSHOT.jar ./partypurse.jar
#
#ENTRYPOINT ["java", "-jar", "./partypurse.jar"]

FROM eclipse-temurin:21-alpine as base


WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=postgresql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]


FROM base as builder

RUN ./mvnw clean package -Dmaven.test.skip

FROM eclipse-temurin:21-alpine
EXPOSE 8080
COPY --from=builder /app/target/PartyPurse-0.0.1-SNAPSHOT.jar /PartyPurse.jar
CMD ["java", "-jar", "/PartyPurse.jar"]





#FROM maven:3.9.6-eclipse-temurin-21-jammy as builder
#
#WORKDIR /app
#COPY . /app/.
#RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
#
#
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#COPY --from=builder /app/target/*.jar /app/*.jar
#
#EXPOSE 8181
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]
