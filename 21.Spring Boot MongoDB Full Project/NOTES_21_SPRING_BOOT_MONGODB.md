# Module 21 Cheat Sheet

This note covers:

- `21.Spring Boot MongoDB Full Project`

Based on:

- [joblisting](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting:1>)

## What It Is

Spring Boot + MongoDB means:

- Spring Boot builds the backend API
- MongoDB stores data as documents
- Spring Data MongoDB connects Java classes to MongoDB collections

MongoDB is a NoSQL document database.

Instead of tables and rows:

- MongoDB uses collections
- each record is a document
- documents are JSON-like

This is useful for:

- job posts
- product data
- comments
- logs
- nested or flexible data

## Why Use It

- fast CRUD development
- flexible schema
- easy storage of arrays and nested objects
- great Spring Boot integration
- good for rapidly changing data models

## Your Project Files

- [application.properties](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/resources/application.properties:1>)
- [Post.java](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/java/com/yashu/joblisting/model/Post.java:1>)
- [PostRepository.java](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/java/com/yashu/joblisting/repository/PostRepository.java:1>)
- [SearchRepositoryImpl.java](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/java/com/yashu/joblisting/repository/SearchRepositoryImpl.java:1>)
- [PostController.java](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/java/com/yashu/joblisting/controller/PostController.java:1>)

## Main Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

## MongoDB Configuration

Example:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/mydb
spring.data.mongodb.database=mydb
```

Your project currently uses:

```properties
spring.application.name=joblisting
spring.data.mongodb.uri=...
spring.data.mongodb.database=yashu
```

## Important Annotations

- `@Document` maps class to collection
- `@Id` marks document id
- `@RestController` creates REST API
- `@Autowired` injects repository/service
- `@CrossOrigin` allows frontend calls

## Example Document Model

```java
@Document(collection = "JobPost")
public class Post {
    @Id
    private String id;
    private String profile;
    private String desc;
    private int exp;
    private String[] techs;
}
```

## Repository Layer

```java
public interface PostRepository extends MongoRepository<Post, String> {
}
```

Common built-in methods:

- `save()`
- `findAll()`
- `findById()`
- `deleteById()`
- `count()`

## Custom Query Methods

```java
List<Post> findByProfile(String profile);
List<Post> findByExpGreaterThan(int exp);
List<Post> findByTechsContaining(String tech);
```

## Controller Example

```java
@GetMapping("/allPosts")
public List<Post> getAllPosts() {
    return repo.findAll();
}

@PostMapping("/post")
public Post addPost(@RequestBody Post post) {
    return repo.save(post);
}
```

## Atlas Search In Your Project

Your [SearchRepositoryImpl.java](</d:/Full Stack/Practice/Java, Spring, and Microservices/21.Spring Boot MongoDB Full Project/joblisting/src/main/java/com/yashu/joblisting/repository/SearchRepositoryImpl.java:1>) uses:

- `MongoClient`
- aggregate pipeline
- `$search`
- `$sort`
- `$limit`

Example:

```java
AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
    new Document("$search",
        new Document("text",
            new Document("query", text)
                .append("path", Arrays.asList("techs", "desc", "profile")))),
    new Document("$sort", new Document("exp", 1L)),
    new Document("$limit", 5L)
));
```

Important:

- `$search` is a MongoDB Atlas Search feature
- it is not just normal local MongoDB search
- usually needs Atlas Search index setup

## MongoDB Shell Commands

```javascript
show dbs
use yashu
show collections
db.JobPost.find()
db.JobPost.find().pretty()
db.JobPost.insertOne({
  profile: "Java Developer",
  desc: "Spring Boot and MongoDB",
  exp: 2,
  techs: ["Java", "Spring Boot", "MongoDB"]
})
db.JobPost.find({ exp: { $gte: 2 } })
db.JobPost.updateOne(
  { profile: "Java Developer" },
  { $set: { exp: 3 } }
)
db.JobPost.deleteOne({ profile: "Java Developer" })
```

## Run Commands

Run app:

```powershell
cd "D:\Full Stack\Practice\Java, Spring, and Microservices\21.Spring Boot MongoDB Full Project\joblisting"
mvn spring-boot:run
```

Package jar:

```powershell
mvn clean package
```

Run tests:

```powershell
mvn test
```

## API Examples

Get all posts:

```http
GET /allPosts
```

Search posts:

```http
GET /posts/java
```

Add post:

```http
POST /post
Content-Type: application/json

{
  "profile": "Backend Developer",
  "desc": "Works with Spring Boot and MongoDB",
  "exp": 2,
  "techs": ["Java", "Spring", "MongoDB"]
}
```

## Docker + MongoDB

Run MongoDB:

```powershell
docker run -d --name mongo-dev -p 27017:27017 mongo
```

Run MongoDB with authentication:

```powershell
docker run -d --name mongo-secure -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=secret mongo
```

Open shell:

```powershell
docker exec -it mongo-secure mongosh -u admin -p secret --authenticationDatabase admin
```

Spring config for Docker MongoDB:

```properties
spring.data.mongodb.uri=mongodb://admin:secret@localhost:27017/mydb?authSource=admin
spring.data.mongodb.database=mydb
```

Compose example:

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://admin:secret@mongo:27017/mydb?authSource=admin
    depends_on:
      - mongo

  mongo:
    image: mongo
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: secret
```

Important:

- outside Docker use `localhost`
- inside Compose use `mongo`

## Common Errors

`MongoTimeoutException`

- database not running
- wrong URI
- wrong credentials

`Authentication failed`

- wrong username/password
- wrong `authSource`

`UnknownHostException: mongo`

- using Compose hostname outside Docker

`$search` not working`

- Atlas Search index missing
- not running against Atlas Search setup

## Best Practices

- keep credentials out of source code
- prefer env vars
- use repository methods for normal CRUD
- use custom repository/aggregate only when needed
- add `@Id` explicitly in document models

Safer config:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=${MONGODB_DATABASE:yashu}
```

## Fast Revision Summary

- MongoDB stores documents, not rows
- `@Document` maps class to collection
- `MongoRepository` gives CRUD methods
- Spring Boot makes config and APIs easy
- Atlas Search uses `$search` in aggregation

## Best Revision Material

- Spring Boot NoSQL docs: https://docs.spring.io/spring-boot/reference/data/nosql.html
- Spring Data MongoDB reference: https://docs.spring.io/spring-data/mongodb/reference/
- MongoDB CRUD docs: https://www.mongodb.com/docs/manual/crud/
- MongoDB Atlas Search docs: https://www.mongodb.com/docs/atlas/atlas-search/aggregation-stages/search/
- Official Mongo Docker image: https://hub.docker.com/_/mongo
- MongoDB with Docker overview: https://www.mongodb.com/compatibility/docker

## 5 Quick Practice Tasks

1. Create one new document model with `@Document`.
2. Add one custom repository query.
3. Insert sample data using Postman.
4. Query Mongo shell manually.
5. Run MongoDB from Docker and connect Spring Boot to it.
