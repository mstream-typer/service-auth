FROM openjdk:8u131-jre-alpine

COPY target/typer-service-auth-*-standalone.jar /typer-service-auth.jar

EXPOSE 8080

USER 1001

CMD java -jar /typer-service-auth.jar