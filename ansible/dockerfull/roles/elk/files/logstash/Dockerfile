# https://www.docker.elastic.co/
FROM docker.elastic.co/logstash/logstash:7.12.0

COPY ./logstash.yml /usr/share/logstash/config/logstash.yml
COPY ./config/logstash.conf /usr/share/logstash/pipeline
