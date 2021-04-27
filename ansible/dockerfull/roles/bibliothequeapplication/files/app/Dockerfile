FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine
ARG nexus_server
ARG version

RUN wget http://${nexus_server}:8081/service/rest/v1/search/assets/download?repository=${version}&group=fr.bibliotheque&name=bibliotheque-services&sort=version&direction=desc -O app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
