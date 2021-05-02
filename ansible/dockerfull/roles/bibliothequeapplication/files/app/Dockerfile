FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine
ARG nexus_server
ARG version

ADD "http://${nexus_server}:8081/service/rest/v1/search/assets/download?sort=version&direction=desc&repository=${version}&group=fr.bibliotheque&name=bibliotheque-services" app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
