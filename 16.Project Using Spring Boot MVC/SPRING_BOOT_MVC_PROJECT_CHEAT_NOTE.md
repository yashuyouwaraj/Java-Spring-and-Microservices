# Spring Boot MVC Project Cheat Note

One-place revision note for `16.Project Using Spring Boot MVC`.

This note is based on:

- your backend project: `ecom-proj`
- your frontend project: `ecom-frontend-5-main`
- official Spring Boot / Spring Data JPA / Vite references

Useful official references:

- Spring Boot Maven plugin: https://docs.spring.io/spring-boot/maven-plugin/run.html
- Spring Data JPA reference: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- Vite docs: https://vite.dev/

## 1. What This Project Is

This is a full-stack e-commerce style project.

It has two parts:

- backend: Spring Boot + Spring Web MVC + Spring Data JPA + H2
- frontend: React + Vite + Axios + React Router

Short idea:

- frontend shows products and sends requests
- backend handles APIs and database work
- database stores product details

## 2. Folder Overview

### `ecom-proj`

Spring Boot backend project.

Use it to revise:

- REST controllers
- service layer
- repository layer
- JPA entities
- file upload
- image storage in database
- H2 database

### `ecom-frontend-5-main`

React frontend project.

Use it to revise:

- React components
- Vite dev server
- Axios API calls
- routing
- UI integration with backend

## 3. Project Architecture

Backend flow:

1. Client sends request from React frontend.
2. Axios calls backend endpoint like `/api/products`.
3. `ProductController` receives the request.
4. `ProductService` handles logic.
5. `ProductRepo` talks to the database.
6. JPA/Hibernate manages table operations.
7. Response returns to frontend.

Short chain:

Frontend -> Controller -> Service -> Repository -> Database

## 4. Backend Technologies Used

Your backend `pom.xml` shows:

- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `h2`
- `spring-boot-h2console`
- `spring-boot-devtools`
- `lombok`

Meaning:

- Web MVC for APIs
- JPA for ORM/database
- H2 as in-memory DB
- H2 console for DB view
- Devtools for dev reload help
- Lombok to reduce boilerplate

## 5. Frontend Technologies Used

Your frontend `package.json` shows:

- `react`
- `vite`
- `axios`
- `react-router-dom`
- `bootstrap`
- `react-bootstrap`

Meaning:

- React builds UI
- Vite runs frontend dev server
- Axios talks to backend
- Router handles page navigation
- Bootstrap styles UI

## 6. Core Backend Files

### `Product.java`

This is the JPA entity.

Important annotations:

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
```

Important fields:

- `id`
- `name`
- `desc`
- `brand`
- `price`
- `category`
- `releaseDate`
- `available`
- `stockQuantity`
- `imageName`
- `imageType`
- `imageData`

Important note:

```java
@Lob
private byte[] imageData;
```

This means image bytes are stored in the database as large object data.

### `ProductRepo.java`

Repository layer:

```java
public interface ProductRepo extends JpaRepository<Product,Integer>
```

Spring automatically gives:

- `findAll()`
- `findById()`
- `save()`
- `deleteById()`

It also has a custom JPQL query:

```java
@Query("SELECT p from Product p WHERE ...")
List<Product> searchProducts(String keyword);
```

This searches in:

- name
- desc
- brand
- category

### `ProductService.java`

Business logic layer.

It handles:

- get all products
- get one product
- add product
- update product
- delete product
- search products

It also converts uploaded image file into:

- image name
- image type
- image bytes

### `ProductController.java`

REST API layer.

Base path:

```java
@RequestMapping("/api")
```

Cross-origin enabled:

```java
@CrossOrigin
```

So frontend can call backend from another port.

## 7. Important Endpoints

### Get all products

```http
GET /api/products
```

### Get one product

```http
GET /api/product/{id}
```

### Add product with image

```http
POST /api/product
Content-Type: multipart/form-data
```

### Get product image

```http
GET /api/product/{productId}/image
```

### Update product with image

```http
PUT /api/product/{id}
Content-Type: multipart/form-data
```

### Delete product

```http
DELETE /api/product/{id}
```

## 8. Important Controller Concepts

### `@RestController`

Makes class a REST controller.

### `@RequestMapping("/api")`

Adds a base URL for all endpoints.

### `@CrossOrigin`

Allows requests from frontend running on another origin like:

```text
http://localhost:5173
```

### `@RequestPart`

Used for multipart form data.

Example from your project:

```java
public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
                                    @RequestPart("imageFile") MultipartFile imageFile)
```

Meaning:

- one part contains product JSON/object
- another part contains uploaded image file

### `MultipartFile`

Represents uploaded file in Spring.

Used for:

- image upload
- PDF upload
- form file handling

## 9. JPA and Database Concepts

### `@Entity`

Maps Java class to DB table.

### `@Id`

Primary key.

### `@GeneratedValue(strategy = GenerationType.IDENTITY)`

Database generates id automatically.

### `JpaRepository`

Provides built-in CRUD methods.

### `@Query`

Lets you write custom JPQL query.

## 10. Application Properties

Your backend currently uses:

```properties
spring.application.name=ecom-proj
spring.datasource.url=jdbc:h2:mem:yashu
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
```

Meaning:

- app name = `ecom-proj`
- H2 in-memory database
- show SQL in console
- Hibernate updates schema automatically
- DB initialization is deferred properly

Useful additions you may enable:

```properties
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
server.port=8080
```

## 11. Data Initialization Note

Your backend has:

```text
src/main/resources/data1.sql
```

Important:

Spring Boot usually auto-runs `data.sql`, not `data1.sql`.

So if you want automatic sample data loading without extra config, the usual file name is:

```text
data.sql
```

Inference from your files:

`data1.sql` may be for manual use, or it may need renaming/config if you expect Boot to auto-load it.

## 12. Frontend API Connection

Your frontend uses:

```javascript
const API = axios.create({
  baseURL: "http://localhost:8080/api",
});
```

Meaning:

- React frontend expects backend at port `8080`
- all API calls go through `/api`

Frontend dev server from Vite will usually run on:

```text
http://localhost:5173
```

So full local setup is often:

- frontend: `5173`
- backend: `8080`

## 13. How To Start This Project

### Start backend

Go to:

```powershell
cd "d:\Full Stack\Practice\Java, Spring, and Microservices\16.Project Using Spring Boot MVC\ecom-proj"
```

Run:

```powershell
.\mvnw.cmd spring-boot:run
```

### Start frontend

Open another terminal:

```powershell
cd "d:\Full Stack\Practice\Java, Spring, and Microservices\16.Project Using Spring Boot MVC\ecom-frontend-5-main"
```

Run:

```powershell
npm install
npm run dev
```

Then open:

```text
http://localhost:5173
```

## 14. Main Commands Used In This Project

### Backend Maven wrapper commands

Run app:

```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:

```powershell
.\mvnw.cmd test
```

Clean and package:

```powershell
.\mvnw.cmd clean package
```

Run jar:

```powershell
java -jar target\ecom-proj-0.0.1-SNAPSHOT.jar
```

### Frontend npm commands

Install dependencies:

```powershell
npm install
```

Run frontend dev server:

```powershell
npm run dev
```

Build production frontend:

```powershell
npm run build
```

Preview production build:

```powershell
npm run preview
```

Run lint:

```powershell
npm run lint
```

## 15. Useful Extra Commands Even If Not Used Daily

These are good revision commands around Spring Boot, Maven, frontend, and debugging.

### Backend Maven commands

Compile only:

```powershell
.\mvnw.cmd compile
```

Show dependency tree:

```powershell
.\mvnw.cmd dependency:tree
```

Clean only:

```powershell
.\mvnw.cmd clean
```

Skip tests while packaging:

```powershell
.\mvnw.cmd clean package -DskipTests
```

Run with custom port:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

Run with profile:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Run with JVM arguments:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m"
```

### Frontend npm extra commands

Check outdated packages:

```powershell
npm outdated
```

Audit dependencies:

```powershell
npm audit
```

Auto-fix some audit issues:

```powershell
npm audit fix
```

Remove installed modules and reinstall:

```powershell
Remove-Item -Recurse -Force node_modules
npm install
```

### Git commands useful around projects

Check status:

```powershell
git status
```

See changed files:

```powershell
git diff --name-only
```

See commit history:

```powershell
git log --oneline --decorate -n 10
```

## 16. Backend Testing Commands

Test the backend API with browser, Postman, or curl.

### Get all products

```powershell
curl http://localhost:8080/api/products
```

### Get one product

```powershell
curl http://localhost:8080/api/product/1
```

### Delete one product

```powershell
curl -X DELETE http://localhost:8080/api/product/1
```

For multipart `POST` and `PUT`, Postman or Thunder Client is easier than raw terminal commands during learning.

## 17. Multipart Request Example

Your backend expects:

- `product`
- `imageFile`

So frontend or Postman must send `multipart/form-data`.

Conceptually:

- `product` part contains product fields
- `imageFile` part contains uploaded file

This is different from normal JSON-only APIs.

## 18. Search Feature Note

Your service and repository support searching:

```java
List<Product> searchProducts(String keyword)
```

But in `ProductController`, the method currently exists without a mapping annotation.

Inference from your code:

```java
public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword)
```

This will not be exposed as an API endpoint unless you add something like:

```java
@GetMapping("/products/search")
```

So for revision:

- service supports search
- repository supports search
- controller mapping still needs to expose it

## 19. Important Concepts for Viva / Interview

### Why use layered architecture?

Because it separates:

- controller logic
- business logic
- data access logic

### Why use `ResponseEntity`?

To control:

- response body
- HTTP status code
- content type

### Why use `@Lob`?

To store large binary data such as image bytes.

### Why use `@CrossOrigin`?

Because frontend and backend run on different ports during development.

### Why use `JpaRepository`?

Because it provides CRUD operations automatically.

### Why use H2?

Because it is lightweight and easy for development/testing.

## 20. Project Workflow Revision

Daily development flow:

1. Start backend.
2. Start frontend.
3. Open frontend in browser.
4. Frontend calls backend APIs.
5. Backend stores/fetches product data.
6. H2/JPA handle persistence.
7. Test create/update/delete/search flows.

## 21. One-Minute Revision

Remember this:

1. `Product` is the entity.
2. `ProductRepo` handles DB through JPA.
3. `ProductService` contains business logic.
4. `ProductController` exposes `/api` endpoints.
5. React frontend calls backend with Axios.
6. Images are uploaded using multipart form data.
7. H2 runs as in-memory DB.
8. Vite runs frontend, Spring Boot runs backend.

## 22. Quick Command Summary

### Backend

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
.\mvnw.cmd clean package
java -jar target\ecom-proj-0.0.1-SNAPSHOT.jar
```

### Frontend

```powershell
npm install
npm run dev
npm run build
npm run preview
npm run lint
```

### Useful extras

```powershell
.\mvnw.cmd dependency:tree
.\mvnw.cmd clean package -DskipTests
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
npm outdated
npm audit
git status
```

## 23. Final Summary

This project is a full-stack Spring Boot MVC + React e-commerce app.

Backend responsibilities:

- expose product APIs
- upload images
- persist data with JPA

Frontend responsibilities:

- display products
- send user actions to backend
- provide UI for add/update/delete flows

This makes `16.Project Using Spring Boot MVC` a very strong revision project because it combines:

- REST APIs
- MVC layering
- JPA
- H2
- file upload
- React frontend integration
