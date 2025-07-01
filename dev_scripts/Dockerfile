FROM alpine:latest

RUN apk add --no-cache curl jq

COPY get-ngrok-url.sh /usr/local/bin/get-ngrok-url.sh

RUN chmod +x /usr/local/bin/get-ngrok-url.sh

CMD ["get-ngrok-url.sh"]