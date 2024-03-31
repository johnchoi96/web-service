# web-service

## Inbound Port

* 8080

## Application Layer Protocol

* HTTPS (Self-signed for now so the browser will complain)

## Endpoints

* [SwaggerUI at `/api-docs`](https://ec2-3-130-128-69.us-east-2.compute.amazonaws.com:8080/api-docs)

## Public IP/DNS

* https://ec2-3-130-128-69.us-east-2.compute.amazonaws.com:8080

## How to run this application

* Set the environment variable to `spring.profiles.active=local` for local development.
    - i.e. `java -jar target/web-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=local`
* Create a directory called `secrets/` under the root directory with the required files.
