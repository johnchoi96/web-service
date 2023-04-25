# web-service

## Inbound Port

* 8080

## Application Layer Protocol

* HTTPS (Self-signed for now so the browser will complain)

## Endpoints

* [Documentation](ENDPOINTS.md)

## Public IP/DNS

* WIP: no elastic IP is assigned for now

## How to run this application

* Set the environment variable to `spring.profiles.active=local` for local development.
    - i.e. `java -jar target/web-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=local`