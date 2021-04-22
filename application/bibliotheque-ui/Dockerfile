# BUILD
FROM node:15.12.0-alpine AS build
LABEL stage=build
WORKDIR src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

# RUN
FROM nginx:stable

ARG CERTBOT_EMAIL
ARG DOMAIN_LIST

MAINTAINER groupe2

RUN apt-get update \
  && apt-get install -y cron certbot python-certbot-nginx bash wget \
  && certbot certonly --standalone --agree-tos -m "${CERTBOT_EMAIL}" -n -d ${DOMAIN_LIST} \
  && rm -rf /var/lib/apt/lists/* \
  && echo "@monthly certbot renew --nginx >> /var/log/cron.log 2>&1" >/etc/cron.d/certbot-renew \
  && crontab /etc/cron.d/certbot-renew

VOLUME /etc/letsencrypt

COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=build src/app/dist /usr/share/nginx/html

EXPOSE 80

CMD [ "sh", "-c", "cron && nginx -g 'daemon off;'" ]
