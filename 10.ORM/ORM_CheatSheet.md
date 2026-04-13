# ORM Cheat Sheet

This note is a practical and theory-focused guide to ORM in Java.

It covers:

- what ORM is
- why ORM exists
- how ORM works internally
- ORM in Java
- JPA, Hibernate, and Spring Data JPA
- entity lifecycle, persistence context, caching, fetch types, and cascading
- how to implement ORM in plain Java and in Spring Boot
- complete examples
- best practices, pitfalls, and interview revision points

---

## 1. What Is ORM?

ORM stands for **Object-Relational Mapping**.

It is a technique that maps:

- Java classes -> database tables
- Java objects -> rows
- object fields -> columns
- object references -> table relationships

### Simple idea

Instead of writing code like:

```java
String sql = "select id, name, email from student where id = ?";
```

and manually converting database rows into Java objects, ORM lets you work more like this:

```java
Student student = entityManager.find(Student.class, 1L);
```

The ORM framework handles the mapping between the Java object and the relational database.

---

## 2. Why Do We Need ORM?

Without ORM, developers often need to:

- write lots of SQL by hand
- manually map rows to Java objects
- manage relationships manually
- repeat boilerplate for CRUD operations
- handle transaction and object state logic carefully

ORM helps by:

- reducing boilerplate
- improving productivity
- making code more object-oriented
- handling common CRUD operations automatically
- supporting relationships like one-to-one, one-to-many, many-to-one, and many-to-many
- integrating with transactions and caching

### But ORM is not magic

ORM is powerful, but you still need to understand:

- SQL
- indexes
- joins
- transactions
- lazy vs eager loading
- performance pitfalls

Good ORM usage does **not** replace database knowledge.

---

## 3. Real-World Analogy

Think of:

- Java objects as the way your application thinks
- relational tables as the way the database stores data

ORM is the translator between the two worlds.

Example:

| Java World | Database World |
|---|---|
| `Student` class | `student` table |
| `student.getName()` | `name` column |
| `student.getDepartment()` | foreign key / joined table |
| `List<Course>` | related rows in another table |

---

## 4. ORM in Java: Big Picture

In Java, ORM is usually discussed through these layers:

| Term | Meaning |
|---|---|
| ORM | the general concept of mapping objects to relational tables |
| JPA | the standard API/specification for persistence in Java |
| Jakarta Persistence | the modern name of JPA under Jakarta EE |
| Hibernate | the most popular ORM framework and JPA provider |
| Spring Data JPA | Spring abstraction built on top of JPA |

### Important distinction

`JPA` is not a framework by itself.

It is a **specification/API**.

Frameworks like:

- Hibernate
- EclipseLink
- OpenJPA

implement that specification.

In most modern Java projects:

- you write entity code using `jakarta.persistence.*`
- Hibernate acts as the JPA provider
- Spring Boot auto-configures much of the plumbing

---

## 5. Key Java ORM Terms

### Entity

A Java class mapped to a database table.

```java
@Entity
public class Student {
    @Id
    private Long id;
}
```

### Table

The relational database structure where records are stored.

### Primary Key

A unique identifier for each row.

In JPA:

- `@Id`
- often combined with `@GeneratedValue`

### Persistence Context

A managed context where entities are tracked by the ORM.

This is one of the most important ideas in JPA/Hibernate.

Inside the persistence context:

- entities are managed
- changes are tracked automatically
- ORM knows whether to `INSERT`, `UPDATE`, or `DELETE`

### EntityManager

The JPA interface used to interact with persistence context.

Common operations:

- `persist()`
- `find()`
- `merge()`
- `remove()`
- `flush()`

### Session

Hibernate's native API equivalent to JPA's `EntityManager`.

### Transaction

A unit of work that must succeed completely or fail completely.

ORM operations usually happen inside a transaction.

---

## 6. How ORM Works Internally

At a high level, this is the flow:

1. You define Java entity classes using annotations.
2. ORM reads metadata from annotations or XML mapping.
3. ORM knows which table and columns each class/field belong to.
4. You create, load, update, or delete Java objects.
5. ORM tracks object state inside the persistence context.
6. On `flush()` or transaction commit, ORM generates SQL.
7. SQL is executed against the database.
8. Query results are converted back into Java objects.

### Visual flow

```text
Java code
   |
   v
EntityManager / Hibernate Session
   |
   v
Persistence Context
   |
   v
ORM generates SQL
   |
   v
Database
```

### Example thought process of ORM

If you write:

```java
Student student = new Student();
student.setName("Aman");
entityManager.persist(student);
```

ORM may generate:

```sql
insert into student (name) values ('Aman');
```

If you later load a student and change a field:

```java
Student student = entityManager.find(Student.class, 1L);
student.setName("Aman Kumar");
```

then on flush/commit ORM may generate:

```sql
update student set name = 'Aman Kumar' where id = 1;
```

This automatic change detection is often called **dirty checking**.

---

## 7. Core ORM Features in Java

### 7.1 Mapping

ORM maps Java classes to tables and fields to columns.

### 7.2 CRUD support

It simplifies:

- create
- read
- update
- delete

### 7.3 Relationship mapping

It maps:

- one-to-one
- one-to-many
- many-to-one
- many-to-many

### 7.4 Query support

You can query using:

- JPQL
- Criteria API
- native SQL
- repository methods in Spring Data JPA

### 7.5 Dirty checking

Changes to managed entities are automatically detected.

### 7.6 Caching

ORM usually uses:

- first-level cache by default
- optional second-level cache

### 7.7 Transaction integration

ORM works closely with transactions so data stays consistent.

---

## 8. JPA vs Hibernate vs Spring Data JPA

This is one of the most common points of confusion.

| Technology | What it is | Example use |
|---|---|---|
| ORM | general concept | map object to table |
| JPA / Jakarta Persistence | Java persistence standard/API | `@Entity`, `EntityManager` |
| Hibernate | ORM framework and JPA provider | actual implementation |
| Spring Data JPA | Spring abstraction on top of JPA | repositories like `save()` and `findById()` |

### Quick memory trick

- **ORM** = concept
- **JPA** = standard
- **Hibernate** = implementation
- **Spring Data JPA** = convenience layer

---

## 9. Important JPA Annotations

### Entity and table mapping

| Annotation | Purpose |
|---|---|
| `@Entity` | marks class as persistent entity |
| `@Table(name = "...")` | maps entity to specific table |
| `@Id` | marks primary key |
| `@GeneratedValue` | auto-generates id |
| `@Column` | customizes column mapping |
| `@Transient` | field is not persisted |

### Relationship mapping

| Annotation | Purpose |
|---|---|
| `@OneToOne` | one record related to one record |
| `@OneToMany` | one record related to many records |
| `@ManyToOne` | many records related to one record |
| `@ManyToMany` | many records related to many records |
| `@JoinColumn` | configures foreign key column |
| `@JoinTable` | configures join table |

### Lifecycle and utility

| Annotation | Purpose |
|---|---|
| `@PrePersist` | called before insert |
| `@PostPersist` | called after insert |
| `@PreUpdate` | called before update |
| `@PostUpdate` | called after update |
| `@PreRemove` | called before delete |
| `@PostRemove` | called after delete |
| `@PostLoad` | called after entity is loaded |
| `@Version` | optimistic locking |

---

## 10. Basic Entity Example

```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    private int age;

    public Student() {
    }

    public Student(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

### What this does

- `Student` becomes a database entity
- `students` becomes the mapped table name
- `id` is the primary key
- ORM maps fields to columns automatically

---

## 11. Entity Lifecycle States

This is extremely important for interviews and debugging.

Modern Jakarta Persistence defines four main entity states:

| State | Meaning |
|---|---|
| New / Transient | object exists in Java, not yet stored |
| Managed / Persistent | tracked by persistence context |
| Detached | exists, has id, but is not currently tracked |
| Removed | marked for deletion |

### Example state flow

```text
new Student()         -> transient
persist(student)      -> managed
commit/close/detach   -> detached
remove(student)       -> removed
```

### Why this matters

Only **managed** entities are automatically tracked for updates.

If you change a detached entity, the database is not updated unless you reattach it, usually with `merge()`.

---

## 12. Persistence Context

The persistence context is the working memory of JPA/Hibernate.

Inside it:

- one entity instance per database identity is managed
- repeated lookups may return the same in-memory object
- modifications are tracked automatically

### Example

```java
Student s1 = entityManager.find(Student.class, 1L);
Student s2 = entityManager.find(Student.class, 1L);

System.out.println(s1 == s2); // usually true inside same persistence context
```

### Why it is powerful

- avoids duplicate object copies
- enables dirty checking
- reduces unnecessary SQL in some cases

---

## 13. Dirty Checking

Dirty checking means Hibernate/JPA tracks changes to managed entities.

Example:

```java
entityManager.getTransaction().begin();

Student student = entityManager.find(Student.class, 1L);
student.setEmail("newmail@example.com");

entityManager.getTransaction().commit();
```

You did not call `update()`.

Still, ORM will detect the changed field and issue SQL during flush/commit.

### Important note

This works only for entities that are currently managed.

---

## 14. Flush vs Commit

These are related but not identical.

| Term | Meaning |
|---|---|
| `flush()` | synchronize in-memory changes with the database |
| `commit()` | finalize transaction permanently |

### Key idea

`flush()` may send SQL early, but the transaction may still roll back.

`commit()` makes the transaction durable.

---

## 15. Caching in ORM

### First-level cache

- enabled by default
- tied to one `EntityManager` / Hibernate `Session`
- stores managed entities in the persistence context

### Second-level cache

- optional
- shared across sessions
- can reduce repeated database access
- usually configured with external providers

For learning, focus first on the first-level cache.

---

## 16. Fetch Types

When an entity has relationships, ORM decides when related data should be loaded.

### `FetchType.EAGER`

Load related data immediately.

### `FetchType.LAZY`

Load related data only when actually accessed.

### Example

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

### Rule of thumb

- prefer `LAZY` for most relationships
- use `EAGER` carefully

### Common problem

If you access a lazy field after the session is closed, you may get a lazy loading exception with Hibernate.

---

## 17. Cascading

Cascade tells ORM whether operations on one entity should automatically affect related entities.

Common cascade types:

- `PERSIST`
- `MERGE`
- `REMOVE`
- `REFRESH`
- `DETACH`
- `ALL`

Example:

```java
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Student> students = new ArrayList<>();
```

Meaning:

- if department is persisted, related students may also be persisted
- if department is removed, related students may also be removed

### Warning

Do not use cascade blindly.

Especially be careful with `REMOVE` and `ALL`.

---

## 18. Relationship Mapping Examples

## 18.1 Many-to-One

Many students belong to one department.

```java
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
```

```java
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```

## 18.2 One-to-Many

One department has many students.

```java
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();
}
```

## 18.3 One-to-One

One student has one address.

```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "address_id")
private Address address;
```

## 18.4 Many-to-Many

Many students can enroll in many courses.

```java
@ManyToMany
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private List<Course> courses = new ArrayList<>();
```

---

## 19. Query Options in ORM

### 19.1 `find()` by primary key

```java
Student student = entityManager.find(Student.class, 1L);
```

### 19.2 JPQL

JPQL uses entity names and field names, not table/column names.

```java
List<Student> students = entityManager
    .createQuery("select s from Student s where s.age > :age", Student.class)
    .setParameter("age", 18)
    .getResultList();
```

### 19.3 Native SQL

```java
List<Student> students = entityManager
    .createNativeQuery("select * from students", Student.class)
    .getResultList();
```

### 19.4 Criteria API

Useful for dynamic type-safe query building, though more verbose.

---

## 20. Java ORM Implementation Path

There are two common learning paths:

1. Plain Java + JPA provider like Hibernate
2. Spring Boot + Spring Data JPA + Hibernate

The second path is more common in modern backend projects.

---

## 21. How To Implement ORM in Plain Java

This section shows the core JPA-style flow in Java SE.

### Step 1. Add dependencies

Typical dependencies:

- Hibernate ORM
- Jakarta Persistence API
- JDBC driver like MySQL Connector/J
- optional database like H2 for practice

### Step 2. Create an entity

Use `@Entity` and related annotations.

### Step 3. Configure `persistence.xml`

Place it in:

```text
src/main/resources/META-INF/persistence.xml
```

Example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="student-unit">
        <class>com.example.Student</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/ormdb"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="Root@1234"/>

            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

### Step 4. Bootstrap `EntityManagerFactory`

```java
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("student-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Student student = new Student("Aman", "aman@example.com", 22);
        em.persist(student);

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
```

### Step 5. Read, update, delete

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("student-unit");
EntityManager em = emf.createEntityManager();

em.getTransaction().begin();

Student student = em.find(Student.class, 1L);
student.setAge(23);

em.getTransaction().commit();

em.getTransaction().begin();
Student studentToDelete = em.find(Student.class, 1L);
em.remove(studentToDelete);
em.getTransaction().commit();

em.close();
emf.close();
```

### What happens behind the scenes

- `persist()` marks object for insertion
- `find()` loads from database or first-level cache
- changing a managed entity triggers dirty checking
- `remove()` marks entity for deletion
- `commit()` flushes SQL and finalizes transaction

---

## 22. How To Implement ORM in Spring Boot

This is the most practical setup for real projects.

### Step 1. Dependencies

For Maven, common dependencies are:

- `spring-boot-starter-data-jpa`
- `mysql-connector-j`
- `spring-boot-starter-web` if building REST APIs

### Step 2. `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ormdb
spring.datasource.username=root
spring.datasource.password=Root@1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Step 3. Create entity

```java
package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private int age;

    public Student() {
    }

    public Student(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

### Step 4. Create repository

```java
package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

### Step 5. Use repository in service

```java
package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student input) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(input.getName());
        student.setEmail(input.getEmail());
        student.setAge(input.getAge());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
```

### Step 6. Optional REST controller

```java
package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
```

### Why Spring Data JPA feels easier

You do not manually create:

- `EntityManagerFactory`
- `EntityManager`
- SQL for common CRUD
- repository implementation classes

Spring Boot + Spring Data JPA handles a lot of setup automatically.

---

## 23. Repository Methods You Should Know

If you extend `JpaRepository<T, ID>`, common methods include:

- `save(entity)`
- `findById(id)`
- `findAll()`
- `deleteById(id)`
- `existsById(id)`
- `count()`
- `saveAndFlush(entity)`
- `getReferenceById(id)`

### Derived query methods

Spring Data JPA can generate queries from method names.

Example:

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByName(String name);
    List<Student> findByAgeGreaterThan(int age);
    Student findByEmail(String email);
}
```

This is very useful, but for complex queries you may still use:

- `@Query`
- JPQL
- native SQL

---

## 24. Example With `@Query`

```java
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.age >= :minAge")
    List<Student> findAdultStudents(int minAge);
}
```

---

## 25. ORM vs JDBC

This comparison is important because ORM builds on top of database access patterns that JDBC exposes more directly.

| Point | JDBC | ORM |
|---|---|---|
| Level | low-level API | higher-level abstraction |
| SQL writing | manual | often automatic or reduced |
| Mapping rows to objects | manual | automatic |
| Boilerplate | more | less |
| Fine SQL control | very high | moderate to high |
| Productivity | slower | faster for common cases |
| Learning SQL deeply | excellent | still needed |
| Performance tuning | very explicit | must understand generated SQL |

### Rule of thumb

- use ORM for most business applications
- use JDBC or native SQL when very fine control is needed

In real systems, both are often used together.

---

## 26. Common ORM Interview Questions

### What is ORM?

ORM is a technique that maps Java objects to relational database tables and handles persistence operations between them.

### What is JPA?

JPA, now Jakarta Persistence, is the Java specification/API for object-relational persistence.

### Is Hibernate the same as JPA?

No.

JPA is the specification.
Hibernate is an implementation/provider of that specification.

### What is the persistence context?

It is the set of managed entity instances tracked by an `EntityManager`.

### What is dirty checking?

Automatic detection of changes made to managed entities so ORM can generate update SQL during flush/commit.

### Difference between `persist()` and `merge()`?

- `persist()` is mainly for new entities
- `merge()` copies state from a detached entity into a managed entity

### What is lazy loading?

Related data is fetched only when accessed, not immediately when the parent entity is loaded.

### What is cascading?

It propagates operations like persist, merge, or remove from one entity to related entities.

---

## 27. Common Mistakes in ORM

### 1. Thinking ORM removes the need for SQL knowledge

It does not.

### 2. Using `EAGER` everywhere

This often causes extra joins and poor performance.

### 3. Ignoring generated SQL

Always inspect SQL in development.

### 4. Accessing lazy relations after session closes

This causes lazy initialization problems.

### 5. Overusing `CascadeType.ALL`

It may delete or persist more data than intended.

### 6. Returning entities directly in all APIs

In larger systems, DTOs are often better than exposing raw entities.

### 7. Not understanding N+1 query problem

Example:

- one query loads departments
- then separate queries load students for each department

This can badly affect performance.

### 8. Treating `save()` as a magical update for every situation

You still need to understand entity state and transaction boundaries.

---

## 28. Best Practices

- understand the database design first
- prefer `LAZY` loading by default for relationships
- use transactions properly
- keep entities simple and focused
- inspect generated SQL during learning
- use DTOs in API boundaries when needed
- index frequently queried columns
- avoid huge object graphs in one request
- choose native SQL or JDBC for very specialized queries when necessary
- learn both ORM and SQL together

---

## 29. Modern Java Notes

### `javax.persistence` vs `jakarta.persistence`

Older projects may use:

```java
import javax.persistence.Entity;
```

Modern projects use:

```java
import jakarta.persistence.Entity;
```

If you are learning modern Spring Boot and modern Hibernate, you should expect `jakarta.persistence`.

---

## 30. Fast Revision Summary

### One-line definition

ORM maps Java objects to relational database tables and automates persistence operations.

### Core Java ORM stack

- Java entity classes
- JPA / Jakarta Persistence annotations and API
- Hibernate as provider
- Spring Data JPA for repository abstraction

### Core lifecycle

- transient
- managed
- detached
- removed

### Core operations

- `persist()`
- `find()`
- `merge()`
- `remove()`
- `flush()`

### Core ideas

- persistence context
- dirty checking
- caching
- lazy loading
- cascading
- transactions

---

## 31. Mini End-to-End Example

### Entity

```java
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
```

### Repository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
}
```

### Usage

```java
Product product = new Product("Laptop", 55000);
productRepository.save(product);

Product loaded = productRepository.findById(product.getId()).orElseThrow();
loaded.setPrice(53000);
productRepository.save(loaded);

productRepository.deleteById(loaded.getId());
```

### SQL idea behind it

```sql
insert into product (name, price) values ('Laptop', 55000);
select id, name, price from product where id = ?;
update product set price = 53000 where id = ?;
delete from product where id = ?;
```

---

## 32. When To Use ORM and When To Be Careful

### ORM is a great fit for:

- CRUD-heavy business apps
- enterprise applications
- Spring Boot backends
- projects with many related entities

### Be extra careful when:

- performance is highly sensitive
- queries are extremely complex
- bulk operations are heavy
- reporting SQL is advanced
- object graph loading becomes too large

In those cases, mix ORM with:

- JPQL
- native queries
- JDBC

---

## 33. Final Memory Map

```text
ORM
 -> concept of object-relational mapping

JPA / Jakarta Persistence
 -> standard API/specification

Hibernate
 -> ORM framework and JPA provider

Spring Data JPA
 -> repository abstraction on top of JPA

Entity
 -> Java class mapped to table

EntityManager
 -> manages persistence context

Persistence Context
 -> tracks managed entities

Dirty Checking
 -> auto-detects updates

Flush
 -> sends SQL

Commit
 -> finalizes transaction
```

---

## 34. Source Notes

This cheat sheet was structured using current official references for Java ORM behavior and terminology, especially:

- Jakarta Persistence API docs for `EntityManager` lifecycle and persistence context
- Hibernate ORM quickstart documentation for Java SE/JPA bootstrapping with `persistence.xml`
- Spring Data JPA `JpaRepository` API for repository capabilities and common methods

Links:

- https://jakarta.ee/specifications/persistence/4.0/apidocs/jakarta.persistence/jakarta/persistence/entitymanager
- https://jakarta.ee/specifications/persistence/3.2/jakarta-persistence-spec-3.2.pdf
- https://docs.hibernate.org/stable/orm/quickstart/html_single/
- https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
