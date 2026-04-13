# REST API and Web Services Cheat Sheet

This folder is a learning journey from basic REST resource classes to database-backed REST APIs with Spring Boot and Spring Data JPA.

Use this file for fast revision before practice, interviews, or while building mini-projects.

## 1. What This Topic Is About

### REST API
REST stands for Representational State Transfer.

A REST API is a web service where:
- resources are exposed through URLs
- HTTP methods represent actions
- data is usually transferred as JSON, sometimes XML
- the server and client stay loosely coupled

Example resource:
- `Alien`
- base URL: `/aliens`

Example operations:
- `GET /aliens` -> fetch all aliens
- `GET /aliens/101` -> fetch one alien
- `POST /aliens/alien` -> create alien
- `PUT /aliens/alien` -> update alien
- `DELETE /aliens/101` -> delete alien

### Web Services
Web services are ways for applications to communicate over a network.

In this topic, the focus is mostly on REST-style web services using:
- JAX-RS / Jersey in the earlier examples
- Spring Boot REST controllers in the later examples
- Spring Data JPA + MySQL in the final example

## 2. Big Picture Flow

This topic progresses like this:

1. Return a Java object from a resource class.
2. Convert object to XML.
3. Return collections from a mock repository.
4. Switch response format to JSON.
5. Support `GET`, `POST`, `PUT`, `DELETE`.
6. Replace mock list with MySQL + JDBC.
7. Replace manual JDBC repository with Spring Data JPA.
8. Expose endpoints through Spring Boot with much less boilerplate.

That progression is exactly what your folder shows.

## 3. Folder-Wise Summary

### `ResourceClass`
Goal:
- understand what a resource class is
- return a plain Java object from a URL

Key idea:
- a POJO becomes a response body
- `@Path`, `@GET`, and `@Produces` define the endpoint

What happens:
- `AlienResource` exposes `/aliens`
- `getAlien()` returns one `Alien`
- response type is XML

Why this matters:
- this is the first time we treat Java code as an HTTP endpoint

### `MockRepository`
Goal:
- return data from a repository-like class instead of hardcoding one object in the resource

Key idea:
- separate data storage logic from request-handling logic

What happens:
- `AlienRepository` stores aliens in an `ArrayList`
- resource calls repository methods
- supports fetching all aliens and fetching one alien by id

Why this matters:
- introduces the repository pattern
- makes controller/resource class thinner

### `Creating a Resourse`
Goal:
- create new data through HTTP `POST`

What happens:
- adds `createAlien(Alien a1)`
- resource receives object from request body
- repository adds the object to the list

Why this matters:
- first CRUD expansion beyond read operations

### `Working with JSON`
Goal:
- move from XML responses to JSON responses

What happens:
- `@Produces(MediaType.APPLICATION_JSON)`
- `@Consumes(MediaType.APPLICATION_JSON)` for POST

Why JSON:
- lighter than XML
- most frontend and API clients prefer JSON
- standard choice for modern REST APIs

### `MySql Repository`
Goal:
- move from in-memory list to real database storage

What happens:
- JDBC connection created using `DriverManager`
- SQL queries used for CRUD
- supports `GET`, `POST`, `PUT`, `DELETE`

Important learning:
- how Java talks to MySQL manually
- difference between SQL layer and resource layer

### `SpringBootRest`
Goal:
- build REST endpoints using Spring Boot instead of Jersey resource style

What happens:
- `@RestController` replaces JAX-RS-style resource handling
- `@GetMapping("aliens")` exposes endpoint
- returns Java list directly and Spring serializes it to JSON

Why this matters:
- much simpler for modern Java backend development
- less configuration, faster development

### `Spring JPA Rest`
Goal:
- connect Spring Boot REST API to database using JPA repositories

What happens:
- `Alien` is a JPA entity with `@Entity` and `@Id`
- `AlienRepository` extends `CrudRepository<Alien, Integer>`
- `AlienResource` injects repository and calls `findAll()`
- database properties live in `application.properties`

Why this matters:
- no manual JDBC boilerplate
- Spring generates repository implementation automatically
- much closer to real production backend development

## 4. Core Theory You Should Remember

### Resource
A resource is the thing your API manages.

Examples:
- `Alien`
- `User`
- `Product`
- `Order`

### Endpoint
An endpoint is a URL + HTTP method combination.

Examples:
- `GET /aliens`
- `POST /aliens/alien`

### Representation
Representation means the format used to send resource data.

Examples:
- JSON
- XML

### Statelessness
REST APIs are usually stateless.

Meaning:
- each request contains all needed information
- server does not depend on previous client requests to understand the current one

### CRUD Mapping

| Operation | HTTP Method | Typical Endpoint | Meaning |
|---|---|---|---|
| Create | `POST` | `/aliens` or `/aliens/alien` | add new resource |
| Read all | `GET` | `/aliens` | get all resources |
| Read one | `GET` | `/aliens/{id}` | get one resource |
| Update | `PUT` | `/aliens/{id}` or `/aliens/alien` | replace/update resource |
| Delete | `DELETE` | `/aliens/{id}` | remove resource |

## 5. Important Annotations

### JAX-RS / Jersey Annotations

| Annotation | Meaning |
|---|---|
| `@Path("aliens")` | maps class or method to URL path |
| `@GET` | handles HTTP GET |
| `@POST` | handles HTTP POST |
| `@PUT` | handles HTTP PUT |
| `@DELETE` | handles HTTP DELETE |
| `@Produces(...)` | tells response format |
| `@Consumes(...)` | tells request body format |
| `@PathParam("id")` | reads path value from URL |

Simple example:

```java
@Path("aliens")
public class AlienResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Alien getAlien(@PathParam("id") int id) {
        return repo.getAliens(id);
    }
}
```

### Spring REST Annotations

| Annotation | Meaning |
|---|---|
| `@RestController` | controller whose methods return response body directly |
| `@GetMapping` | handles GET |
| `@PostMapping` | handles POST |
| `@PutMapping` | handles PUT |
| `@DeleteMapping` | handles DELETE |
| `@RequestBody` | maps request JSON to Java object |
| `@PathVariable` | reads path value from URL |
| `@Autowired` | injects dependency |

Simple example:

```java
@RestController
public class AlienResource {

    @Autowired
    AlienRepository repo;

    @GetMapping("aliens")
    public List<Alien> getAliens() {
        return (List<Alien>) repo.findAll();
    }
}
```

### JPA Annotations

| Annotation | Meaning |
|---|---|
| `@Entity` | marks class as database entity |
| `@Id` | marks primary key |
| `@Table` | optional custom table name |
| `@Column` | optional custom column name |

Example:

```java
@Entity
public class Alien {
    @Id
    private int id;
    private String name;
    private int points;
}
```

## 6. How Serialization Works

Serialization means converting Java objects into a transferable format.

In your projects:
- JAXB annotations help XML conversion
- Spring Boot usually uses Jackson for JSON conversion automatically

Example Java object:

```java
Alien a = new Alien();
a.setId(101);
a.setName("Yashu");
a.setPoints(100);
```

Possible JSON response:

```json
{
  "id": 101,
  "name": "Yashu",
  "points": 100
}
```

Possible XML response:

```xml
<alien>
  <id>101</id>
  <name>Yashu</name>
  <points>100</points>
</alien>
```

## 7. Understanding Your Local Examples

### Example 1: Single object resource
From the early resource class example:

```java
@GET
@Produces(MediaType.APPLICATION_XML)
public Alien getAlien() {
    Alien a = new Alien();
    a.setName("Yashu");
    a.setPoints("100");
    return a;
}
```

Theory:
- `@GET` means browser/client sends a read request
- method returns one Java object
- framework converts it to XML

### Example 2: Read all from mock repository

```java
@GET
@Produces(MediaType.APPLICATION_XML)
public List<Alien> getAlien() {
    return repo.getAliens();
}
```

Theory:
- resource delegates to repository
- repository stores data in memory
- good for learning architecture before adding database complexity

### Example 3: Read one by path variable

```java
@GET
@Path("{id}")
@Produces(MediaType.APPLICATION_JSON)
public Alien getAlien(@PathParam("id") int id) {
    return repo.getAliens(id);
}
```

Theory:
- URL like `/aliens/101`
- `101` becomes method parameter `id`
- method returns matching object

### Example 4: Create using POST

```java
@POST
@Path("alien")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Alien createAlien(Alien a1) {
    repo.createAlien(a1);
    return a1;
}
```

Theory:
- client sends JSON in request body
- framework converts JSON to Java object
- repository stores it
- response returns created object

### Example 5: Update using PUT

```java
@PUT
@Path("alien")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Alien updateAlien(Alien a1) {
    repo.updateAlien(a1);
    return a1;
}
```

Theory:
- update existing record
- usually sent with full or near-full object body

### Example 6: Delete using DELETE

```java
@DELETE
@Path("{id}")
@Produces(MediaType.APPLICATION_JSON)
public Alien killAlien(@PathParam("id") int id) {
    Alien a1 = repo.getAliens(id);
    if (a1.getId() != 0) {
        repo.deleteAlien(id);
    }
    return a1;
}
```

Theory:
- client asks server to remove one record
- endpoint identifies record by id

### Example 7: Spring Boot REST

```java
@RestController
public class AlienResource {

    @GetMapping("aliens")
    public List<Alien> getAliens() {
        List<Alien> aliens = new ArrayList<>();
        return aliens;
    }
}
```

Theory:
- no JAX-RS annotations needed
- Spring MVC handles routing + JSON conversion
- simpler and more common in Spring applications

### Example 8: Spring Data JPA REST

```java
public interface AlienRepository extends CrudRepository<Alien, Integer> {
}
```

Theory:
- no implementation class written manually
- Spring generates implementation at runtime
- gives you methods like `findAll()`, `findById()`, `save()`, `deleteById()`

## 8. JDBC Version vs JPA Version

### JDBC approach in `MySql Repository`
You manually write:
- DB connection
- SQL queries
- `PreparedStatement`
- `ResultSet`
- mapping from row to object

Example:

```java
String sql = "INSERT INTO alien values(?, ?, ?)";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setInt(1, a1.getId());
pstmt.setString(2, a1.getName());
pstmt.setInt(3, a1.getPoints());
pstmt.executeUpdate();
```

Pros:
- full SQL control
- good for understanding low-level database interaction

Cons:
- verbose
- repetitive
- easier to make mistakes

### JPA approach in `Spring JPA Rest`
You mainly write:
- entity class
- repository interface
- controller
- config

Example:

```java
repo.save(alien);
repo.findAll();
repo.findById(101);
repo.deleteById(101);
```

Pros:
- less boilerplate
- faster development
- cleaner code

Cons:
- you need to understand framework conventions
- sometimes custom queries need extra annotations

## 9. Important Commands for Practice

These are the most useful commands for this topic.

### Maven commands

Run Spring Boot app:

```bash
./mvnw spring-boot:run
```

Windows alternative:

```powershell
.\mvnw.cmd spring-boot:run
```

Build project:

```bash
./mvnw clean package
```

Run packaged jar:

```bash
java -jar target/rest-0.0.1-SNAPSHOT.jar
```

Run tests:

```bash
./mvnw test
```

### Useful `curl` commands for REST testing

Get all aliens:

```bash
curl http://localhost:8080/aliens
```

Get one alien:

```bash
curl http://localhost:8080/aliens/101
```

Create alien:

```bash
curl -X POST http://localhost:8080/aliens/alien \
  -H "Content-Type: application/json" \
  -d "{\"id\":103,\"name\":\"Nova\",\"points\":300}"
```

Update alien:

```bash
curl -X PUT http://localhost:8080/aliens/alien \
  -H "Content-Type: application/json" \
  -d "{\"id\":103,\"name\":\"Nova Updated\",\"points\":350}"
```

Delete alien:

```bash
curl -X DELETE http://localhost:8080/aliens/103
```

PowerShell-friendly version:

```powershell
curl.exe -X POST http://localhost:8080/aliens/alien -H "Content-Type: application/json" -d "{\"id\":103,\"name\":\"Nova\",\"points\":300}"
```

### MySQL commands

Open MySQL:

```bash
mysql -u root -p
```

Create database:

```sql
CREATE DATABASE restdb;
```

Use database:

```sql
USE restdb;
```

Create table:

```sql
CREATE TABLE alien (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  points INT
);
```

Insert sample data:

```sql
INSERT INTO alien (id, name, points) VALUES
(101, 'Yashu', 100),
(102, 'Deepika', 200),
(103, 'John', 300);
```

Verify data:

```sql
SELECT * FROM alien;
```

## 10. Spring Boot + JPA Setup Notes From Your Project

Your `Spring JPA Rest` project currently uses:
- Java `21`
- Spring Boot parent `4.0.5`
- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `mysql-connector-j`

Current app config:

```properties
spring.application.name=rest
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/restdb
spring.datasource.username=root
spring.datasource.password=Root@1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Common useful additions:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Why add them:
- `ddl-auto=update` helps create/update tables during learning
- `show-sql=true` shows generated SQL in console

## 11. Best Practice Version of the Final Spring REST API

Your current `Spring JPA Rest` example returns all aliens only.

A more complete revision version would look like this:

```java
@RestController
public class AlienResource {

    @Autowired
    private AlienRepository repo;

    @GetMapping("/aliens")
    public List<Alien> getAliens() {
        return (List<Alien>) repo.findAll();
    }

    @GetMapping("/aliens/{id}")
    public Alien getAlien(@PathVariable int id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping("/aliens")
    public Alien addAlien(@RequestBody Alien alien) {
        return repo.save(alien);
    }

    @PutMapping("/aliens")
    public Alien updateAlien(@RequestBody Alien alien) {
        return repo.save(alien);
    }

    @DeleteMapping("/aliens/{id}")
    public String deleteAlien(@PathVariable int id) {
        repo.deleteById(id);
        return "Deleted " + id;
    }
}
```

Why this version is better:
- consistent endpoints
- uses `@RequestBody` explicitly
- uses repository methods directly
- matches common Spring REST style

## 12. Request and Response Examples

### `GET /aliens`

Response:

```json
[
  {
    "id": 101,
    "name": "Yashu",
    "points": 100
  },
  {
    "id": 102,
    "name": "John",
    "points": 200
  }
]
```

### `GET /aliens/101`

Response:

```json
{
  "id": 101,
  "name": "Yashu",
  "points": 100
}
```

### `POST /aliens`

Request body:

```json
{
  "id": 104,
  "name": "Arya",
  "points": 450
}
```

Response:

```json
{
  "id": 104,
  "name": "Arya",
  "points": 450
}
```

## 13. Interview and Revision Points

### What is the difference between `@Controller` and `@RestController`?
- `@Controller` is usually for MVC views
- `@RestController` returns data directly as response body

### Why do we use repository classes?
- to separate data access logic from endpoint logic
- to improve maintainability and readability

### Why use `PreparedStatement`?
- avoids SQL injection risk better than string concatenation
- handles values safely

### Why move from JDBC to JPA?
- less code
- easier CRUD
- better integration with Spring

### Why JSON is preferred over XML in REST APIs?
- smaller payload
- easier frontend integration
- easier to read in most API tools

### What does `CrudRepository<Alien, Integer>` mean?
- entity type is `Alien`
- primary key type is `Integer`

### What does `@Entity` do?
- maps Java class to database table

### What does `@Id` do?
- marks primary key field

## 14. Common Mistakes in This Topic

### 1. Mixing XML and JSON expectations
If `@Produces(MediaType.APPLICATION_XML)` is used, client receives XML, not JSON.

### 2. Forgetting `@Consumes` for POST/PUT in JAX-RS
Without it, request body parsing may fail.

### 3. Using string concatenation in SQL
Bad:

```java
String sql = "SELECT * FROM alien where id=" + id;
```

Better:

```java
String sql = "SELECT * FROM alien WHERE id = ?";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setInt(1, id);
```

### 4. Forgetting DB table or database setup
The API may run, but queries fail if `restdb` or table `alien` is missing.

### 5. Field naming inconsistency
Your class uses `Id` with uppercase `I`.
In Java, `id` is usually preferred for cleaner conventions.

### 6. Missing JPA schema settings
If table is not auto-created and DB is empty, JPA code may fail unless schema is prepared.

## 15. Minimal End-to-End Flow

This is the easiest mental model for the final project:

1. Client sends HTTP request.
2. Spring controller/resource receives request.
3. Controller calls repository.
4. Repository fetches or saves data.
5. Data returns as Java object.
6. Framework converts object to JSON/XML.
7. Client receives response.

## 16. Quick Revision in 60 Seconds

- REST API exposes resources using URLs and HTTP methods.
- `GET`, `POST`, `PUT`, `DELETE` map to CRUD.
- JAX-RS uses `@Path`, `@GET`, `@POST`, `@Produces`, `@Consumes`.
- Spring Boot uses `@RestController`, `@GetMapping`, `@PostMapping`.
- Mock repository teaches architecture before real DB.
- JDBC uses SQL manually.
- JPA uses entities and repository interfaces.
- `CrudRepository` gives ready-made CRUD methods.
- JSON is the default modern API format.
- MySQL config lives in `application.properties`.

## 17. Quick Revision in 10 Questions

1. What is a resource in REST?
2. What is the difference between `GET` and `POST`?
3. What does `@Path("{id}")` mean?
4. Why do we use `@Produces`?
5. Why is JSON more common than XML?
6. What problem does a repository solve?
7. What is the difference between JDBC and JPA?
8. What does `@Entity` do?
9. What methods come from `CrudRepository`?
10. How does Spring convert Java objects to JSON?

## 18. Official References Used

These were used to align the cheat sheet with current official guidance and common commands:

- Spring guide: Accessing Data with JPA  
  https://spring.io/guides/gs/accessing-data-jpa/
- Spring guide: Accessing JPA Data with REST  
  https://spring.io/guides/gs/accessing-data-rest/
- Spring Framework reference for web/REST controllers  
  https://docs.spring.io/spring-framework/reference/
- Spring Data repository API docs  
  https://docs.spring.io/spring-data/commons/docs/current/api/
- Jakarta RESTful Web Services explained  
  https://jakarta.ee/learn/specification-guides/restful-web-services-explained/
- Jakarta RESTful Web Services tutorial  
  https://jakarta.ee/learn/docs/jakartaee-tutorial/current/websvcs/rest/rest.html
- MySQL Connector/J developer guide  
  https://dev.mysql.com/doc/connector-j/en/

## 19. Final Takeaway

This topic teaches one very important backend idea:

start simple, understand HTTP and resources first, then add serialization, then CRUD, then database connectivity, then framework automation.

That is exactly what your folder does:
- resource class
- repository
- JSON
- database
- Spring Boot
- JPA

If you understand that progression, you understand the whole topic.
