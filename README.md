# RNFT Marketplace

## Application Services

-   [marketplace-service](./marketplace-service) : the marketplace service.
-   [api-gateway](./api-gateway) : The Api gateway and authentication service.

## Environment variables

```env
PG_PASSWORD = <DATABASE PASSWORD>

MAIL_SMTP_HOST= <SMTP SERVER>
MAIL_SMTP_PORT= <SMTP PORT>
MAIL_SMTP_USER= <SMTP USER>
MAIL_SMTP_PASSWORD= <SMTP PASSWORD>
MAIL_SMTP_FROM= <SMTP FROM ADDRESS>

JWT_GATEWAY_SECRET= <JWT SECRET KEY USED BY THE GATEWAY SERVICE>
JWT_SHARED_SECRET= <JWT SECERET KEY USED BY THE ALL SERVICES>
CORS_ALLOW_ORIGIN= <LIST OF ALLOWED ORIGINS SEPARATED BY COMMA>

IPFS_API_AUTHORIZATION = <IPFS API AUTHENTICATION JWT>
IPFS_API_URL = <IPFS API ENDPOINT>
IPFS_GATEWAY = <IPFS GATEWAY URL>

RABBITMQ_VHOST= <RABBIT MQ VHOST>
RABBITMQ_USER= <RABBIT MQ USERNAME>
RABBITMQ_PASS= <RABBIT MQ PASSWORD>

AES_PASSWORD= <PASSWORD USED FOR ENCRYPTING/DECRYPTING QR CODES>
```

## Helpers scripts

Building all service to jar files

```shell
./build-all.sh
```

Building a particular service

```shell
./build.sh <service-name>
```

Running a particular service

```shell
./run.sh <service-name>
```

Start all services as docker containers

```shell
./start-services.sh
```
