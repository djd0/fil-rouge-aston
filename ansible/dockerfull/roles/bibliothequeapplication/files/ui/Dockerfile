# RUN
FROM nginx:stable
MAINTAINER groupe2
ARG nexus_server
ARG env
ARG version

COPY ./config/${env}/default.conf /etc/nginx/conf.d/default.conf

RUN apt-get update && \
    apt-get install -y cron certbot python-certbot-nginx bash wget && \
    rm -rf /var/lib/apt/lists/* && \
    echo "@monthly certbot renew --nginx >> /var/log/cron.log 2>&1" >/etc/cron.d/certbot-renew && \
    crontab /etc/cron.d/certbot-renew && \
    wget http://${nexus_server}:8081/service/rest/v1/search/assets/download?repository=${version}&group=fr.bibliotheque&name=bibliotheque-ui&sort=version&direction=desc&e=tgz -O ui.tgz && \
    tar -xvfz ui.tgz -C /usr/share/nginx/html

VOLUME /etc/letsencrypt

EXPOSE 80 443
CMD [ "sh", "-c", "cron && nginx -g 'daemon off;'" ]
