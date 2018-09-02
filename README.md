microservices-tracing
-----

This is a multi-module Spring Boot Angular v6 Maven project. It connects to elasticsearch and helps gather timing data needed to troubleshoot latency problems in microservice architectures.

The frontend Angular app is built using [angular-cli](https://cli.angular.io/). The project packages Angular application code as a [WebJar](https://www.webjars.org/).

This project uses following versions:

1. Spring Boot v2.0.2
2. Angular v6.0.0
3. Node v8.11.1
4. Yarn v1.3.2
5. Java 8
6. Bootstrap: 4.1.1
7. connects to ElasticSearch 5.50-5.68

## Running the full application

You can build the package as a single artifact by running the `./mvnw clean install`.
Next, you can run the application by executing:

```bash
$ java -jar backend/target/ngboot-app.jar
```

The application will be accessible at `http://localhost:8080`.

## Running the backend for development mode

There are multiple ways to run the backend. For development, you can use your favorite IDE and run the
`com.example.app.Application`. As soon as your code compiles, Spring Boot DevTools will reload the code.

You can also run the application using Maven.

```bash
$ cd backend
$  ../mvnw spring-boot:run
```

## Running the frontend for development mode

Make sure to install [node.js](https://nodejs.org/en/download/).

Once node.js is installed on your local machine change directory to frontend and run 
 ```
 ng serve
 ```

## Hot reloading

Both the front-end and back-end modules support hot reloading.
