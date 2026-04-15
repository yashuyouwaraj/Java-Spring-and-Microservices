# Spring Data JPA Cheat Sheet

One-place revision sheet for the `15.Spring Data JPA` folder.

This sheet is based on:

- your local `ORM` project
- current official Spring Data JPA and Spring Boot docs

Official references used:

- Spring Data JPA reference: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- `JpaRepository` API: https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
- Spring guide, Accessing Data with JPA: https://spring.io/guides/gs/accessing-data-jpa
- Spring Boot SQL/H2 docs: https://docs.spring.io/spring-boot/reference/data/sql.html
- Spring Boot Maven plugin run docs: https://docs.spring.io/spring-boot/maven-plugin/run.html

## 1. What Is JPA?

JPA = Jakarta Persistence API.

It is a Java standard for working with relational databases using Java objects instead of writing only raw JDBC code everywhere.

JPA helps you:

- map Java classes to database tables
- map object fields to table columns
- save and fetch objects from database
- reduce boilerplate SQL interaction code

Important point:

JPA is a specification, not the implementation itself.

Common implementation:

- Hibernate

In your project:

- Spring Data JPA uses JPA
- Hibernate is the JPA provider behind the scenes
- H2 is the database

## 2. What Is Spring Data JPA?

Spring Data JPA is Spring’s abstraction on top of JPA.

It makes data access much easier by letting you define repository interfaces instead of writing all DAO code manually.

Main idea:

You write:

```java
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
```

Spring generates the implementation automatically at runtime.

That is the biggest feature to remember.

## 3. What Is ORM?

ORM = Object Relational Mapping.

It means:

- Java class <-> database table
- object field <-> table column

Example from your project:

- class `Product`
- table `product`

Fields:

- `prodId`
- `prodName`
- `price`

These become database columns.

## 4. How It Works Internally

Basic flow in your project:

1. Spring Boot starts.
2. DataSource is created using H2 config.
3. Hibernate starts as JPA provider.
4. Entity classes are scanned.
5. Repository interfaces are detected.
6. Spring creates repository implementations automatically.
7. Controller calls service.
8. Service calls repository.
9. Repository talks to DB through JPA/Hibernate.
10. Result comes back as Java objects.

Short chain:

Controller -> Service -> Repository -> JPA/Hibernate -> Database

## 5. Your Folder Structure and What It Teaches

Inside `15.Spring Data JPA/ORM`:

- `model/Product.java`
- `repository/ProductRepo.java`
- `service/ProductService.java`
- `controller/ProductController.java`
- `SimpleWebAppApplication.java`
- `application.properties`
- `pom.xml`

This project teaches:

- entity mapping
- repository-based persistence
- CRUD with Spring Data JPA
- REST + JPA integration
- H2 database with Boot

## 6. Main Theory You Must Remember

### Entity

An entity is a Java class mapped to a database table.

From your project:

```java
@Entity
public class Product {
}
```

Meaning:

Spring JPA/Hibernate will treat this class as a persistent DB entity.

### Primary Key

Every entity needs a primary key.

From your project:

```java
@Id
private int prodId;
```

Meaning:

`prodId` uniquely identifies each row.

### Repository

A repository is the data-access layer.

In your project:

```java
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
```

Meaning:

- entity type = `Product`
- primary key type = `Integer`

### Service Layer

Service layer contains business logic and talks to repository.

In your project:

```java
@Service
public class ProductService {
}
```

### Controller Layer

Controller handles HTTP requests and talks to service.

Your project combines REST API and JPA together.

## 7. Important Annotations

### `@SpringBootApplication`

Starts the Spring Boot app.

```java
@SpringBootApplication
public class SimpleWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleWebAppApplication.class, args);
    }
}
```

### `@Entity`

Marks a class as JPA entity.

### `@Id`

Marks the primary key field.

### `@Repository`

Marks repository layer bean.

In Spring Data JPA, repository interface can work even without explicitly adding `@Repository`, but using it is fine for learning clarity.

### `@Service`

Marks service layer bean.

### `@RestController`

Marks controller as REST API controller.

### `@Autowired`

Injects dependencies automatically.

## 8. Your Entity Class

Your `Product` class:

```java
@Component
@Entity
public class Product {
    @Id
    private int prodId;
    private String prodName;
    private int price;
}
```

What matters most for JPA:

- `@Entity`
- `@Id`
- no-args constructor
- getters/setters

Revision note:

For JPA, `@Entity` is the important one.
`@Component` is usually not necessary on entity classes in typical projects.

## 9. Repository Theory

Your repository:

```java
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
```

Official Spring docs describe `JpaRepository` as a JPA-specific extension of repository support.

By extending `JpaRepository`, you automatically get methods like:

- `findAll()`
- `findById(id)`
- `save(entity)`
- `deleteById(id)`
- `existsById(id)`
- `count()`

This is why Spring Data JPA reduces a lot of boilerplate.

## 10. Built-in CRUD Methods in Your Project

### Get all products

```java
repo.findAll();
```

### Get one product by id

```java
repo.findById(prodId);
```

### Insert product

```java
repo.save(prod);
```

### Update product

```java
repo.save(prod);
```

### Delete product

```java
repo.deleteById(prodId);
```

Important memory point:

In JPA, `save()` is commonly used for both insert and update.

## 11. Service Layer in Your Project

Your `ProductService` acts like a middle layer:

```java
public List<Product> getProducts() {
    return repo.findAll();
}
```

```java
public Product getProductById(int prodId) {
    return repo.findById(prodId).orElse(new Product());
}
```

```java
public void addProduct(Product prod) {
    repo.save(prod);
}
```

```java
public void updateProduct(Product prod) {
    repo.save(prod);
}
```

```java
public void deleteProduct(int prodId) {
    repo.deleteById(prodId);
}
```

This is a good revision point:

- controller handles request
- service handles logic
- repository handles DB

## 12. REST + JPA Together

Your `ProductController` exposes JPA-backed endpoints:

### Get all

```java
@GetMapping("/products")
public List<Product> getProducts() {
    return service.getProducts();
}
```

### Get one

```java
@GetMapping("/products/{prodId}")
public Product getProductById(@PathVariable int prodId) {
    return service.getProductById(prodId);
}
```

### Add

```java
@PostMapping("/products")
public void addProduct(@RequestBody Product product) {
    service.addProduct(product);
}
```

### Update

```java
@PutMapping("/products")
public void updateProduct(@RequestBody Product product) {
    service.updateProduct(product);
}
```

### Delete

```java
@DeleteMapping("/products/{prodId}")
public void deleteProduct(@PathVariable int prodId) {
    service.deleteProduct(prodId);
}
```

## 13. Dependencies in `pom.xml`

Your project uses:

### Spring Web MVC

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

For:

- REST controllers
- web server
- request handling

### Spring Data JPA

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

For:

- JPA support
- Hibernate integration
- repositories
- ORM features

### H2 Database

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

For:

- lightweight in-memory database

### H2 Console

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-h2console</artifactId>
</dependency>
```

For:

- browser-based H2 console

## 14. Configuration in `application.properties`

Your current file contains:

```properties
spring.application.name=simpleWebApp
spring.datasource.url=jdbc:h2:mem:yashu
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.show-sql=true
```

Meaning:

- app name = `simpleWebApp`
- H2 in-memory database is used
- H2 driver is selected
- SQL statements are printed in console

Useful properties for this project:

```properties
spring.datasource.url=jdbc:h2:mem:yashu
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 15. H2 Console

From Spring Boot docs:

- H2 console is auto-configurable in servlet web apps
- `spring-boot-h2console` must be on classpath
- `spring.h2.console.enabled=true` enables it when needed

Use:

```text
http://localhost:8080/h2-console
```

Login values:

- JDBC URL: `jdbc:h2:mem:yashu`
- User Name: `sa`
- Password: blank

## 16. How To Start a Spring Data JPA Project

### Option A: Spring Initializr

Use:

```text
https://start.spring.io
```

Choose:

- Maven
- Java
- Spring Boot

Add dependencies:

- Spring Web
- Spring Data JPA
- H2 Database

### Option B: Based on your current project

1. Create Boot project.
2. Add JPA dependency.
3. Add DB dependency.
4. Create entity.
5. Create repository interface.
6. Create service layer.
7. Create controller.
8. Configure datasource.
9. Run app.

## 17. How To Implement Spring Data JPA

Implementation flow:

1. Add `spring-boot-starter-data-jpa`.
2. Add database dependency like H2.
3. Configure datasource properties.
4. Create entity class with `@Entity`.
5. Mark primary key using `@Id`.
6. Create repository extending `JpaRepository`.
7. Use repository inside service.
8. Expose data using controller if building REST API.
9. Run and test.

## 18. How JPA Saves Data

When you call:

```java
repo.save(product);
```

Spring Data JPA + Hibernate decide whether to:

- insert a new row
- or update an existing row

General memory rule:

- new entity id not present in DB -> insert
- same id already exists -> update/merge behavior

## 19. Commands To Run Your Project

Run these in:

```text
d:\Full Stack\Practice\Java, Spring, and Microservices\15.Spring Data JPA\ORM
```

### Run app

```powershell
.\mvnw.cmd spring-boot:run
```

### Run tests

```powershell
.\mvnw.cmd test
```

### Clean and build

```powershell
.\mvnw.cmd clean package
```

### Run packaged jar

```powershell
java -jar target\simpleWebApp-0.0.1-SNAPSHOT.jar
```

### If Maven is installed globally

```powershell
mvn spring-boot:run
mvn test
mvn clean package
```

## 20. Useful Boot Run Variations

From official Boot Maven plugin docs:

### Run on custom port

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Run with profile

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### Run with JVM args

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m"
```

## 21. How To Use the API

### Get all products

```powershell
curl http://localhost:8080/products
```

### Get one product

```powershell
curl http://localhost:8080/products/101
```

### Add product

```powershell
curl -X POST http://localhost:8080/products ^
  -H "Content-Type: application/json" ^
  -d "{\"prodId\":101,\"prodName\":\"Iphone\",\"price\":50000}"
```

### Update product

```powershell
curl -X PUT http://localhost:8080/products ^
  -H "Content-Type: application/json" ^
  -d "{\"prodId\":101,\"prodName\":\"Updated Iphone\",\"price\":55000}"
```

### Delete product

```powershell
curl -X DELETE http://localhost:8080/products/101
```

## 22. Important JPA Concepts

### Persistence

Persistence means storing object data permanently in database.

### Entity Manager

JPA internally works through an `EntityManager`.

Spring Data JPA hides most of this complexity for you.

### Hibernate

Hibernate is the implementation that handles:

- SQL generation
- table mapping
- entity state management

### Dialect

The dialect tells Hibernate which SQL style to use for a database.

For H2, Hibernate uses `H2Dialect`.

## 23. Common JPA Terms

### POJO

Plain Java Object.

### Entity

POJO mapped to DB table.

### Repository

Spring Data abstraction for DB access.

### Table

Database table for entity data.

### Column

Database field corresponding to entity field.

## 24. Typical Interview / Viva Questions

### What is JPA?

A Java specification for ORM and database persistence.

### What is Spring Data JPA?

Spring abstraction that simplifies JPA-based data access using repositories.

### What is ORM?

Mapping Java objects to relational database tables.

### What is `JpaRepository`?

A Spring Data JPA repository interface that provides built-in CRUD and JPA operations.

### Why use `@Entity`?

To mark a class as database-mapped persistent entity.

### Why use `@Id`?

To define the primary key.

### Why use H2?

For lightweight in-memory DB during learning and testing.

### Why use `save()` for both insert and update?

Because Spring Data JPA manages persistence state and uses one repository method for both common write cases.

## 25. Common Mistakes

- forgetting `@Entity`
- forgetting `@Id`
- no no-args constructor
- wrong datasource URL
- H2 console dependency missing
- H2 console property commented out
- expecting `/h2-console` to work without enabling it
- assuming entity class must always be `@Component`
- forgetting repository package should be under main app scan package
- confusing JPA with JDBC

## 26. JPA vs JDBC Quick Compare

| Topic | JDBC | Spring Data JPA |
|---|---|---|
| Query style | manual SQL | repository methods + ORM |
| Mapping | manual row mapping | entity mapping |
| Boilerplate | more | less |
| CRUD code | manually written | auto-provided |
| Best use in learning | understand DB basics | build faster with ORM |

## 27. One-Minute Revision

Remember this chain:

1. `@Entity` maps class to table.
2. `@Id` marks primary key.
3. `JpaRepository<Entity, IdType>` gives CRUD methods automatically.
4. Service calls repository.
5. Controller exposes API.
6. Hibernate works behind Spring Data JPA.
7. H2 is the in-memory database here.
8. `save()` handles insert and update.
9. `findAll()`, `findById()`, and `deleteById()` are built in.

## 28. Your Project Summary

Your `ORM` project demonstrates:

- Spring Boot + Spring Web + Spring Data JPA
- H2 in-memory database
- entity mapping with `Product`
- repository abstraction with `ProductRepo`
- CRUD operations through REST endpoints

That makes it a solid first Spring Data JPA revision project.
