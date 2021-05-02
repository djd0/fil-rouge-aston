#!/bin/sh

if [ ! -f /var/www/html/data/index.php ]; then
  echo "Init data folder"
  mv /opt/src/data/* /var/www/html/data && \
  chown -R www-data /var/www/html
fi

docker-php-entrypoint apache2-foreground
