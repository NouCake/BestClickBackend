FROM nginx:latest

EXPOSE 80
EXPOSE 443

COPY docker/nginx.conf /etc/nginx/nginx.conf
COPY src/main/resources/springboot.cert /etc/nginx/cert.cert
COPY src/main/resources/springboot.key /etc/nginx/cert.key

