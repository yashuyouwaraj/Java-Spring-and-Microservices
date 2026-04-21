# Module 22 Cheat Sheet

This note covers:

- `22.Docker for Java Developers`

Based on:

- [student-app](</d:/Full Stack/Practice/Java, Spring, and Microservices/22.Docker for Java Developers/student-app:1>)

## What It Is

Docker helps package and run applications in containers.

Key ideas:

- `Dockerfile` = recipe for image
- `image` = packaged application
- `container` = running image
- `docker compose` = run multiple services together

In this module, Docker is used to run:

- Spring Boot application
- PostgreSQL database

## Why Use Docker

- same environment everywhere
- easier project setup
- easy DB startup
- useful for deployment
- important for microservices

## Your Project Files

- [Dockerfile](</d:/Full Stack/Practice/Java, Spring, and Microservices/22.Docker for Java Developers/student-app/Dockerfile:1>)
- [docker-compose.yml](</d:/Full Stack/Practice/Java, Spring, and Microservices/22.Docker for Java Developers/student-app/docker-compose.yml:1>)
- [application.properties](</d:/Full Stack/Practice/Java, Spring, and Microservices/22.Docker for Java Developers/student-app/src/main/resources/application.properties:1>)
- [pom.xml](</d:/Full Stack/Practice/Java, Spring, and Microservices/22.Docker for Java Developers/student-app/pom.xml:1>)

## Dockerfile

Example from your project:

```dockerfile
FROM eclipse-temurin:25-jdk
ADD target/student-app.jar student-app.jar
ENTRYPOINT ["java","-jar","/student-app.jar"]
```

What each line means:

- `FROM` selects base image
- `ADD` copies jar into image
- `ENTRYPOINT` tells container how to start app

## Docker Compose

Compose starts multiple containers together.

In your project:

- one container runs Spring Boot
- one container runs PostgreSQL

Example:

```yaml
services:
  app:
    build: .
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/docker-spring
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 1947
    ports:
      - "8080:8080"

  postgres:
    image: postgres:latest
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 1947
      POSTGRES_DB: docker-spring
    ports:
      - "5432:5432"
```

## Why Compose Is Useful

- one command starts everything
- automatic networking
- easier than starting services one by one
- app can connect to DB container by service name

## Most Important Networking Rule

Outside Docker:

- use `localhost`

Inside Compose:

- use service name like `postgres`

Examples:

Local app:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/docker-spring
```

Inside Compose:

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/docker-spring
```

## Build and Run Commands

Compile and package without tests:

```powershell
mvn clean package -DskipTests
```

Build Docker image:

```powershell
docker build -t student-app .
```

Run only app container:

```powershell
docker run -p 8080:8080 student-app
```

Run app + database:

```powershell
docker compose up --build
```

Stop services:

```powershell
docker compose down
```

Remove services and volumes:

```powershell
docker compose down -v
```

## Useful Docker Commands

See images:

```powershell
docker images
```

See running containers:

```powershell
docker ps
```

See all containers:

```powershell
docker ps -a
```

See logs:

```powershell
docker logs <container-name>
```

Follow Compose logs:

```powershell
docker compose logs -f
```

Enter container shell:

```powershell
docker exec -it <container-name> sh
```

Restart:

```powershell
docker compose restart
```

## PostgreSQL in Docker

Run PostgreSQL manually:

```powershell
docker run -d --name pg-dev -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1947 -e POSTGRES_DB=docker-spring postgres
```

Connect from Spring Boot:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/docker-spring
spring.datasource.username=postgres
spring.datasource.password=1947
```

Inside Compose:

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/docker-spring
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: 1947
```

## Java Version Matching

This is very important.

Your Maven build version and Docker runtime version must match.

Example:

`pom.xml`

```xml
<java.version>25</java.version>
```

`Dockerfile`

```dockerfile
FROM eclipse-temurin:25-jdk
```

If they do not match, you can get:

- `UnsupportedClassVersionError`

## Common Errors

`release version XX not supported`

- Maven uses older JDK than project target

`UnsupportedClassVersionError`

- app built with newer Java than Docker runtime

`UnknownHostException: postgres`

- using Docker hostname outside Docker

`connection refused`

- DB not started
- wrong port
- wrong host
- wrong username/password

`yaml parse error`

- bad indentation
- wrong list syntax
- wrong port mapping syntax

Wrong:

```yaml
- "8080":"8080"
```

Correct:

```yaml
- "8080:8080"
```

## Healthcheck

Healthcheck helps app wait until DB is ready.

Example:

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U postgres -d docker-spring"]
  interval: 10s
  timeout: 5s
  retries: 5
```

## Good Practices

- use environment variables for credentials
- keep local config and Docker config separate
- use healthchecks
- use `docker compose`
- keep `Dockerfile` simple
- do not hardcode secrets in public repos

Better Spring config:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/docker-spring}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:1947}
```

## Maven Commands You Should Know

Run app:

```powershell
mvn spring-boot:run
```

Compile:

```powershell
mvn clean compile
```

Package:

```powershell
mvn clean package
```

Package without running tests:

```powershell
mvn clean package -DskipTests
```

Skip test compilation and execution completely:

```powershell
mvn clean package -Dmaven.test.skip=true
```

## Fast Revision Summary

- Docker packages apps into images
- containers run those images
- Compose runs multiple services together
- app talks to DB using service name inside Compose
- use `localhost` outside Docker
- JDK versions must match between Maven and Docker

## Best Revision Material

- Docker Compose getting started: https://docs.docker.com/compose/gettingstarted/
- Compose file reference: https://docs.docker.com/reference/compose-file/
- Dockerfile reference: https://docs.docker.com/reference/builder/
- Spring Boot container images: https://docs.spring.io/spring-boot/reference/packaging/container-images/
- PostgreSQL Docker image: https://hub.docker.com/_/postgres

## 5 Quick Practice Tasks

1. Build jar with `mvn clean package -DskipTests`.
2. Build image with `docker build`.
3. Run app alone with `docker run`.
4. Run app + DB with `docker compose up --build`.
5. Break the config on purpose and debug host, port, and Java version issues.
