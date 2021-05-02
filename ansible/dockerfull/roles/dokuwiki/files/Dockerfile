FROM php:7.4.16-apache-buster

COPY data.tgz /opt/src/data.tgz
COPY dokuwiki-stable.tgz /opt/src/dokuwiki-stable.tgz
COPY custom-start.sh /custom-start.sh

RUN chmod +x /custom-start.sh && \
    cd /opt/src/ && \
    tar zxf dokuwiki-stable.tgz && \
    tar zxf data.tgz && \
    rm -fr /var/www/html && \
    rm -fr dokuwiki-stable.tgz && \
    rm -fr data.tgz && \
    mv dokuwiki /var/www/html/ && \
    chown -R www-data /var/www/html

CMD ["/custom-start.sh"]
