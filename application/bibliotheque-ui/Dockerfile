# BUILD
FROM node:15.12.0-alpine AS build
ARG angular_build_env
LABEL stage=build
WORKDIR src/app
COPY package.json ./
RUN npm install
COPY . .
RUN ${angular_build_env}

# RUN
FROM nginx:stable
MAINTAINER groupe2

ARG env

COPY ./env/${env}/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build src/app/dist /usr/share/nginx/html

RUN apt-get update && \
    apt-get install -y cron certbot python-certbot-nginx bash wget && \
    rm -rf /var/lib/apt/lists/* && \
    echo "@monthly certbot renew --nginx >> /var/log/cron.log 2>&1" >/etc/cron.d/certbot-renew && \
    crontab /etc/cron.d/certbot-renew

VOLUME /etc/letsencrypt

EXPOSE 80 443
CMD [ "sh", "-c", "cron && nginx -g 'daemon off;'" ]
