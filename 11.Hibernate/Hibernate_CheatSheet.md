# Hibernate Cheat Sheet

This note is a serious, revision-focused guide to **Hibernate** in Java.

It covers:

- what Hibernate is
- why Hibernate is used
- how Hibernate works internally
- Hibernate architecture
- entity mapping and annotations
- configuration using `hibernate.cfg.xml`
- CRUD operations
- important `Session` methods and what each command does
- object lifecycle states
- caching, dirty checking, flush, commit, and transactions
- HQL, native SQL, and parameter binding
- relationships and fetch types
- complete examples using your `Alien` style project
- common mistakes, interview points, and Maven commands

---

## 1. What Is Hibernate?

Hibernate is an **ORM framework** for Java.

ORM stands for **Object-Relational Mapping**.

Hibernate maps:

- Java class -> database table
- Java object -> row
- Java field -> column

So instead of manually writing SQL and converting rows into Java objects every time, Hibernate lets you work with normal Java objects and handles most of the database mapping for you.

### Simple idea

Without Hibernate:

```java
String sql = "insert into alien_data (a_id, a_name, tech) values (?, ?, ?)";
```

With Hibernate:

```java
Alien alien = new Alien();
alien.setAid(101);
alien.setAname("Yashu");
alien.setTech("Java");

session.persist(alien);
```

Hibernate converts that object operation into SQL.

---

## 2. What Hibernate Does

Hibernate helps with:

- table mapping
- CRUD operations
- SQL generation
- transaction support
- object state tracking
- relationship mapping
- caching
- query APIs

### In one line

Hibernate acts like a translator between **Java objects** and **relational database tables**.

---

## 3. Why Hibernate Is Needed

Without Hibernate or ORM, developers usually need to:

- write SQL by hand
- open JDBC connections manually
- map result sets to Java objects manually
- manage insert, update, delete logic manually
- manage transactions carefully
- repeat a lot of boilerplate code

Hibernate reduces this work by:

- generating SQL for common operations
- mapping Java classes to tables
- tracking changes automatically
- handling common persistence operations cleanly

### But Hibernate is not magic

Hibernate makes life easier, but it does **not** remove the need to understand:

- SQL
- joins
- indexes
- transactions
- foreign keys
- performance

Good Hibernate developers also understand the database well.

---

## 4. Hibernate, JPA, and ORM

This is a common confusion point.

| Term | Meaning |
|---|---|
| ORM | general concept of mapping objects to relational tables |
| JPA / Jakarta Persistence | Java persistence specification/API |
| Hibernate | ORM framework and JPA provider |

### Important point

`JPA` is a specification.

`Hibernate` is an implementation of that persistence idea and API.

In your project, you are using:

- Hibernate core
- Jakarta Persistence annotations like `@Entity`, `@Id`, `@Table`

---

## 5. How Hibernate Works Internally

At a high level:

1. You create an entity class using annotations.
2. Hibernate reads those annotations.
3. Hibernate learns which class maps to which table.
4. You create or load Java objects.
5. Hibernate keeps track of those objects inside the session.
6. On `flush()` or `commit()`, Hibernate generates SQL.
7. SQL is executed on the database.

### Flow

```text
Java Class / Object
        |
        v
Hibernate Session
        |
        v
Persistence Context
        |
        v
Generated SQL
        |
        v
Database
```

### Example

If you write:

```java
session.persist(alien);
```

Hibernate may generate something like:

```sql
insert into alien_data (a_id, a_name, tech) values (101, 'Yashu', 'Java');
```

If you load an object and change it:

```java
Alien alien = session.get(Alien.class, 101);
alien.setTech("Spring");
```

then Hibernate may generate:

```sql
update alien_data set tech='Spring' where a_id=101;
```

This automatic update detection is called **dirty checking**.

---

## 6. Hibernate Architecture

Main building blocks:

| Component | Purpose |
|---|---|
| `Configuration` | reads Hibernate settings and mappings |
| `SessionFactory` | heavy object, created once, used to open sessions |
| `Session` | main interface to interact with database |
| `Transaction` | groups operations into a single unit of work |
| Entity class | Java class mapped to a table |

### Easy memory line

- `Configuration` loads setup
- `SessionFactory` creates sessions
- `Session` does database work
- `Transaction` controls commit/rollback

---

## 7. Important Hibernate Classes

### `Configuration`

Used to configure Hibernate using:

- `hibernate.cfg.xml`
- annotated entity classes

Example:

```java
Configuration config = new Configuration();
config.addAnnotatedClass(Alien.class);
config.configure("hibernate.cfg.xml");
```

### `SessionFactory`

Creates `Session` objects.

Example:

```java
SessionFactory factory = config.buildSessionFactory();
```

### `Session`

Used to:

- save data
- fetch data
- update data
- delete data
- create queries

Example:

```java
Session session = factory.openSession();
```

### `Transaction`

Used to make sure a group of operations either:

- all succeed
- or all fail

Example:

```java
Transaction transaction = session.beginTransaction();
transaction.commit();
```

---

## 8. Hibernate Project Structure

Typical structure:

```text
src
 └─ main
    ├─ java
    │   └─ com/yashu
    │       ├─ Alien.java
    │       └─ Main.java
    └─ resources
        └─ hibernate.cfg.xml
```

### In your project

- entity class: `Alien.java`
- main runner: `Main.java`
- config file: `hibernate.cfg.xml`

---

## 9. Maven Dependencies

From your project:

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>8.0.0.Alpha1</version>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.10</version>
</dependency>
```

### What each dependency does

| Dependency | Purpose |
|---|---|
| `hibernate-core` | main Hibernate ORM library |
| `postgresql` | JDBC driver to connect Java to PostgreSQL |

### Note

Your project currently uses `Hibernate 8.0.0.Alpha1`, which is a pre-release build. For learning this is okay, but in real projects stable Hibernate versions are safer.

---

## 10. `hibernate.cfg.xml` Explained

Your project uses this style:

```xml
<hibernate-configuration xmlns="http://www.hibernate.org/xsd/orm/cfg">
    <session-factory>
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/hibernate</property>
        <property name="hibernate.connection.username">postgres</property>
        <property name="hibernate.connection.password">1947</property>

        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.show_sql">true</property>
    </session-factory>
</hibernate-configuration>
```

### What each property does

| Property | Meaning |
|---|---|
| `hibernate.connection.driver_class` | JDBC driver class |
| `hibernate.connection.url` | database connection URL |
| `hibernate.connection.username` | database username |
| `hibernate.connection.password` | database password |
| `hibernate.hbm2ddl.auto` | tells Hibernate how to handle schema/table creation |
| `hibernate.show_sql` | prints generated SQL in console |

### `hibernate.hbm2ddl.auto` values

| Value | What it does |
|---|---|
| `none` | do nothing to schema |
| `validate` | checks if table structure matches entity mappings |
| `update` | updates schema if needed without dropping data |
| `create` | drops old schema and creates new schema when app starts |
| `create-drop` | creates schema on start and drops it on shutdown |

### Rule of thumb

- use `update` for practice
- use `validate` or proper migration tools in serious production systems
- be very careful with `create` and `create-drop`

---

## 11. Entity Class and Mapping

Your current entity style:

```java
package com.yashu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alien_data")
public class Alien {
    @Id
    @Column(name = "a_id")
    private int aid;

    @Column(name = "a_name")
    private String aname;

    private String tech;
}
```

### What each annotation does

| Annotation | Meaning |
|---|---|
| `@Entity` | marks the class as a Hibernate entity |
| `@Table(name = "alien_data")` | maps entity to `alien_data` table |
| `@Id` | marks primary key |
| `@Column(name = "a_id")` | maps `aid` field to `a_id` column |
| `@Column(name = "a_name")` | maps `aname` field to `a_name` column |

### Important note

If `@Table` is not given, Hibernate usually uses the entity/class name as the table name.

If `@Column` is not given, Hibernate usually uses the field name as the column name.

---

## 12. Full Basic Example

### Entity

```java
package com.yashu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alien_data")
public class Alien {

    @Id
    @Column(name = "a_id")
    private int aid;

    @Column(name = "a_name")
    private String aname;

    private String tech;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }
}
```

### Main class

```java
package com.yashu;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Alien alien = new Alien();
        alien.setAid(101);
        alien.setAname("Yashu");
        alien.setTech("Java");

        SessionFactory factory = new Configuration()
                .addAnnotatedClass(Alien.class)
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(alien);

        transaction.commit();
        session.close();
        factory.close();
    }
}
```

---

## 13. What Each Main Line Does

This section is important for revision.

### `new Configuration()`

Creates a Hibernate configuration object.

### `.addAnnotatedClass(Alien.class)`

Registers the `Alien` entity so Hibernate knows it is a mapped class.

### `.configure("hibernate.cfg.xml")`

Loads Hibernate configuration settings from `hibernate.cfg.xml`.

### `.buildSessionFactory()`

Builds the `SessionFactory`.

This is expensive, so create it once and reuse it.

### `factory.openSession()`

Opens a new database session.

### `session.beginTransaction()`

Starts a transaction.

### `session.persist(alien)`

Marks a new object for insertion into the database.

### `transaction.commit()`

Flushes changes and permanently saves them to the database.

### `session.close()`

Closes the session and releases session resources.

### `factory.close()`

Closes the factory and releases Hibernate resources.

---

## 14. Lifecycle of an Entity Object

This is one of the most important Hibernate topics.

According to Jakarta Persistence, an entity can be in these main states:

| State | Meaning |
|---|---|
| Transient / New | object exists in Java only, not linked to DB |
| Persistent / Managed | object is tracked by Hibernate session |
| Detached | object has DB identity but session is closed or object is detached |
| Removed | object is marked for deletion |

### Example flow

```text
new Alien()            -> transient
session.persist(alien) -> persistent
session.close()        -> detached
session.remove(alien)  -> removed
```

### Why this matters

Hibernate only automatically tracks changes for objects that are in the **persistent/managed** state.

---

## 15. Session Methods and What They Do

This is a very important revision table.

| Method | What it does |
|---|---|
| `persist(obj)` | inserts a new entity into persistence context; saved on flush/commit |
| `get(Class, id)` | immediately fetches entity by primary key; returns `null` if not found |
| `find(Class, id)` | JPA-style fetch by primary key |
| `merge(obj)` | copies detached object state into a managed entity and returns the managed instance |
| `remove(obj)` | marks entity for deletion |
| `detach(obj)` | removes object from session tracking |
| `clear()` | detaches all managed entities from current session |
| `flush()` | sends pending SQL changes to DB but does not finalize transaction |
| `close()` | closes current session |
| `createQuery(...)` | creates HQL/JPQL query |
| `createNativeQuery(...)` | creates raw SQL query |
| `beginTransaction()` | starts transaction |

### Very important difference: `persist()` vs `merge()`

`persist()`:

- used for new objects
- makes the same object managed

`merge()`:

- usually used for detached objects
- copies state into a managed object
- returns a managed instance
- the original passed object does **not** become managed automatically

Hibernate documentation describes `merge()` as copying state to the persistent object with the same identifier and returning the persistent instance.

---

## 16. `get()` vs `find()`

Both are used to fetch by primary key.

Example:

```java
Alien a1 = session.get(Alien.class, 101);
Alien a2 = session.find(Alien.class, 101);
```

### Practical difference for your revision

| Method | Notes |
|---|---|
| `get()` | Hibernate method, usually returns object or `null` |
| `find()` | JPA-style method, cleaner for JPA-based code |

For beginner-level practice, both are fine for primary key fetch.

---

## 17. CRUD Operations in Hibernate

CRUD means:

- Create
- Read
- Update
- Delete

---

## 18. Create / Insert

### Code

```java
Alien alien = new Alien();
alien.setAid(104);
alien.setAname("Ruchi");
alien.setTech("AWS");

SessionFactory factory = new Configuration()
        .addAnnotatedClass(Alien.class)
        .configure()
        .buildSessionFactory();

Session session = factory.openSession();
Transaction transaction = session.beginTransaction();

session.persist(alien);

transaction.commit();
session.close();
factory.close();
```

### What happens

1. object is created in Java
2. `persist()` makes it managed
3. on `commit()`, Hibernate generates `INSERT`

### SQL idea

```sql
insert into alien_data (a_id, a_name, tech) values (104, 'Ruchi', 'AWS');
```

---

## 19. Read / Fetch

### Code

```java
Session session = factory.openSession();
Alien alien = session.get(Alien.class, 101);
System.out.println(alien);
session.close();
```

### What happens

- Hibernate queries the table using primary key
- returns object if found
- returns `null` if not found

### SQL idea

```sql
select a_id, a_name, tech from alien_data where a_id = 101;
```

---

## 20. Update

### Case 1: update managed object

```java
Session session = factory.openSession();
Transaction transaction = session.beginTransaction();

Alien alien = session.get(Alien.class, 101);
alien.setTech("Spring Boot");

transaction.commit();
session.close();
```

### What happens

- `alien` becomes managed after `get()`
- field changes are tracked
- on commit, Hibernate sends `UPDATE`

This is **dirty checking**.

### SQL idea

```sql
update alien_data set tech='Spring Boot' where a_id=101;
```

### Case 2: update detached object using `merge()`

```java
Alien alien = new Alien();
alien.setAid(101);
alien.setAname("Yashu");
alien.setTech("Microservices");

Session session = factory.openSession();
Transaction transaction = session.beginTransaction();

session.merge(alien);

transaction.commit();
session.close();
```

### What `merge()` does here

- takes detached or external object data
- copies it to a managed entity with same id
- performs update during commit if needed

---

## 21. Delete

### Code

```java
Session session = factory.openSession();
Transaction transaction = session.beginTransaction();

Alien alien = session.get(Alien.class, 104);
if (alien != null) {
    session.remove(alien);
}

transaction.commit();
session.close();
```

### What happens

- entity is loaded
- `remove()` marks it for deletion
- on commit, Hibernate executes `DELETE`

### SQL idea

```sql
delete from alien_data where a_id=104;
```

---

## 22. Dirty Checking

Dirty checking means Hibernate automatically detects changes in managed entities.

Example:

```java
Alien alien = session.get(Alien.class, 101);
alien.setTech("Docker");
```

You did not call any explicit `update()` method here.

Hibernate still notices the change and updates the database during flush/commit.

### Why it works

Because the entity is managed inside the session.

---

## 23. `flush()` vs `commit()`

This is important.

| Term | Meaning |
|---|---|
| `flush()` | synchronizes session changes with DB by sending SQL |
| `commit()` | finalizes the transaction permanently |

### Key idea

`flush()` may execute SQL early, but the transaction can still be rolled back.

`commit()` makes changes permanent.

---

## 24. Transactions

A transaction is a unit of work.

If something fails, you can roll back and avoid partial data.

### Typical pattern

```java
Transaction tx = null;

try (Session session = factory.openSession()) {
    tx = session.beginTransaction();

    session.persist(alien);

    tx.commit();
} catch (Exception e) {
    if (tx != null) {
        tx.rollback();
    }
}
```

### Why transactions matter

- keeps data consistent
- prevents half-completed operations
- required for many write operations

---

## 25. Caching in Hibernate

### First-level cache

- built into Hibernate by default
- works at `Session` level
- stores objects already loaded in that session

Example:

```java
Alien a1 = session.get(Alien.class, 101);
Alien a2 = session.get(Alien.class, 101);
```

Inside the same session, Hibernate may return the same managed object without hitting the database again.

### Second-level cache

- optional
- shared across sessions
- requires extra configuration/provider

For revision, focus first on the first-level cache.

---

## 26. Querying in Hibernate

Hibernate supports:

- primary key fetch
- HQL
- native SQL
- criteria APIs

---

## 27. HQL

HQL stands for **Hibernate Query Language**.

It looks like SQL, but it works with:

- class names
- field names

not table names and column names.

### Example

```java
String hql = "from Alien where tech = :technology";

Session session = factory.openSession();
List<Alien> aliens = session.createQuery(hql, Alien.class)
        .setParameter("technology", "Java")
        .getResultList();
session.close();
```

### Meaning

- `Alien` is entity class name
- `tech` is Java field name
- `:technology` is named parameter

---

## 28. Common HQL Commands

| HQL | What it does |
|---|---|
| `from Alien` | fetch all `Alien` objects |
| `from Alien where aid = :id` | fetch by condition |
| `select aname from Alien` | fetch only names |
| `update Alien set tech = :tech where aid = :id` | bulk update |
| `delete from Alien where aid = :id` | bulk delete |

### Example

```java
List<Alien> list = session.createQuery("from Alien", Alien.class).getResultList();
```

---

## 29. Native SQL

If needed, you can run raw SQL.

### Example

```java
List<Alien> aliens = session
        .createNativeQuery("select * from alien_data", Alien.class)
        .getResultList();
```

### When to use native SQL

- database-specific features
- complex queries
- fine control

Use it carefully because it ties code more directly to the database.

---

## 30. Parameter Binding

Always prefer parameters over string concatenation.

### Good

```java
Alien alien = session.createQuery(
        "from Alien where aid = :id", Alien.class)
        .setParameter("id", 101)
        .uniqueResult();
```

### Why

- cleaner
- safer
- easier to maintain

---

## 31. Primary Key Generation

Your project currently sets id manually:

```java
alien.setAid(101);
```

Hibernate can also generate ids automatically.

### Example

```java
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int aid;
```

### What this does

Database generates the primary key automatically when row is inserted.

### Common strategies

| Strategy | Meaning |
|---|---|
| `IDENTITY` | database auto-increment/identity column |
| `SEQUENCE` | uses DB sequence |
| `TABLE` | uses a table to generate ids |
| `AUTO` | Hibernate/provider chooses strategy |

---

## 32. Important Mapping Annotations

| Annotation | Use |
|---|---|
| `@Entity` | marks class as entity |
| `@Table` | maps to a specific table |
| `@Id` | marks primary key |
| `@GeneratedValue` | auto-generates primary key |
| `@Column` | customizes column mapping |
| `@Transient` | field is not stored in DB |
| `@Lob` | large object like long text/blob |
| `@Enumerated` | maps enum values |

---

## 33. Relationship Mapping

Hibernate can map relationships between entities.

Main relationship types:

- one-to-one
- one-to-many
- many-to-one
- many-to-many

---

## 34. Many-to-One Example

Many employees can belong to one department.

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
```

### Meaning

- many `Employee` rows can point to one `Department`
- `department_id` becomes foreign key

---

## 35. One-to-Many Example

```java
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
```

### Meaning

- one department has many employees
- `mappedBy = "department"` means the relationship is owned by the `department` field in `Employee`

---

## 36. One-to-One Example

```java
@OneToOne
@JoinColumn(name = "passport_id")
private Passport passport;
```

### Meaning

- one entity is connected to one other entity

---

## 37. Many-to-Many Example

```java
@ManyToMany
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private List<Course> courses;
```

### Meaning

- many students can take many courses
- a join table is used

---

## 38. Fetch Types

When entities have relationships, Hibernate decides when related data should load.

| Fetch Type | Meaning |
|---|---|
| `EAGER` | load related data immediately |
| `LAZY` | load related data only when accessed |

### Example

```java
@ManyToOne(fetch = FetchType.LAZY)
private Department department;
```

### Rule of thumb

Prefer `LAZY` in most cases unless you truly need immediate loading.

### Common problem

If you access lazy data after the session is closed, you may get lazy initialization problems.

---

## 39. Cascading

Cascade means an operation on one entity can automatically affect related entities.

Common types:

- `PERSIST`
- `MERGE`
- `REMOVE`
- `REFRESH`
- `DETACH`
- `ALL`

### Example

```java
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;
```

### Meaning

If the parent is persisted or removed, related children may also be affected.

### Warning

Do not use `CascadeType.ALL` blindly, especially with delete operations.

---

## 40. `persist()` vs Older `save()`

In older Hibernate tutorials, you may see:

```java
session.save(alien);
```

Modern code usually prefers:

```java
session.persist(alien);
```

### Why

`persist()` aligns with Jakarta Persistence/JPA style and is the cleaner modern choice for new entities.

---

## 41. `openSession()` vs `getCurrentSession()`

| Method | Meaning |
|---|---|
| `openSession()` | opens a brand new session |
| `getCurrentSession()` | returns context-bound current session if configured |

For simple Java SE practice projects, `openSession()` is common and easier to understand.

---

## 42. Exception Handling Pattern

A safer Hibernate write pattern:

```java
SessionFactory factory = new Configuration()
        .addAnnotatedClass(Alien.class)
        .configure()
        .buildSessionFactory();

Transaction tx = null;

try (Session session = factory.openSession()) {
    tx = session.beginTransaction();

    Alien alien = new Alien();
    alien.setAid(105);
    alien.setAname("Deepika");
    alien.setTech("Cloud");

    session.persist(alien);

    tx.commit();
} catch (Exception e) {
    if (tx != null) {
        tx.rollback();
    }
    e.printStackTrace();
} finally {
    factory.close();
}
```

### Why this is better

- avoids leaked sessions
- handles rollback properly
- safer for real applications

---

## 43. Maven Commands and What They Do

This is useful for revision and practice.

Run these from the project folder that contains `pom.xml`.

### `mvn compile`

Compiles main source code.

### `mvn test`

Compiles and runs test code.

### `mvn package`

Compiles code, runs tests, and packages the app into a JAR.

### `mvn install`

Builds the project and installs the packaged artifact into your local Maven repository.

### `mvn clean`

Deletes the `target` directory.

### Common combinations

| Command | What it does |
|---|---|
| `mvn clean compile` | removes old build files and compiles project |
| `mvn clean test` | cleans and runs tests |
| `mvn clean package` | cleans, compiles, tests, and creates JAR |
| `mvn clean install` | full local build and local repository install |

### Note

Maven lifecycle phases run in order. So `mvn package` runs earlier required phases like `compile` and `test` before packaging.

---

## 44. PostgreSQL Commands You Commonly Need

Since your config points to PostgreSQL:

### Open PostgreSQL shell

```bash
psql -U postgres
```

### Create database

```sql
create database hibernate;
```

### List databases

```sql
\l
```

### Connect to database

```sql
\c hibernate
```

### Show tables

```sql
\dt
```

### Show table structure

```sql
\d alien_data
```

### Select data

```sql
select * from alien_data;
```

### Delete all rows

```sql
delete from alien_data;
```

### Drop table

```sql
drop table alien_data;
```

Use destructive SQL carefully.

---

## 45. SQL Commands Hibernate Commonly Generates

| Hibernate action | SQL idea |
|---|---|
| `session.persist(obj)` | `insert into ...` |
| `session.get(...)` | `select ... where id = ?` |
| change managed entity field | `update ... set ... where id = ?` |
| `session.remove(obj)` | `delete from ... where id = ?` |

This is why turning on:

```xml
<property name="hibernate.show_sql">true</property>
```

is very useful while learning.

---

## 46. Common Interview Questions

### What is Hibernate?

Hibernate is a Java ORM framework that maps Java objects to relational database tables and manages persistence.

### What is ORM?

ORM is the technique of mapping object-oriented classes and objects to relational database tables and rows.

### What is a session?

A `Session` is Hibernate's main interface for interacting with the database and managing entity state.

### What is `SessionFactory`?

A heavyweight, thread-safe factory used to create sessions.

### What is dirty checking?

Automatic detection of changes made to managed entities.

### What is the difference between `persist()` and `merge()`?

- `persist()` is for new entities
- `merge()` copies detached object state into a managed instance

### What is first-level cache?

It is the session-level cache enabled by default in Hibernate.

### What is HQL?

Hibernate Query Language, which works with entity names and field names instead of table and column names.

---

## 47. Common Mistakes

### 1. Forgetting transaction commit

If you do not commit, database changes may not be saved.

### 2. Closing session too early

Then managed tracking stops, and lazy data may fail to load.

### 3. Using `create` carelessly in `hbm2ddl.auto`

This can delete and recreate tables.

### 4. Confusing `persist()` with `merge()`

These are not the same.

### 5. Creating `SessionFactory` again and again

It should usually be created once and reused.

### 6. Ignoring SQL output

Generated SQL should be checked during learning and debugging.

### 7. Thinking Hibernate removes the need for SQL knowledge

It does not.

---

## 48. Best Practices

- create `SessionFactory` once
- always use transactions for write operations
- close `Session` properly
- inspect generated SQL
- prefer `persist()` for new objects
- use `merge()` carefully for detached objects
- keep entity classes clean and focused
- understand table design and keys
- prefer parameter binding in queries
- use `LAZY` thoughtfully for relationships

---

## 49. Quick Revision Table

| Topic | Key idea |
|---|---|
| Hibernate | Java ORM framework |
| Entity | Java class mapped to table |
| `@Entity` | marks persistent class |
| `@Table` | custom table name |
| `@Id` | primary key |
| `Configuration` | loads setup |
| `SessionFactory` | creates sessions |
| `Session` | performs DB operations |
| `Transaction` | manages commit/rollback |
| `persist()` | insert new entity |
| `get()` / `find()` | fetch entity by id |
| `merge()` | update detached entity state |
| `remove()` | delete entity |
| dirty checking | auto update on managed object change |
| first-level cache | session-level cache |
| HQL | query using entity names and fields |
| `flush()` | send SQL |
| `commit()` | make transaction permanent |

---

## 50. End-to-End Mini Revision Example

```java
Configuration config = new Configuration()
        .addAnnotatedClass(Alien.class)
        .configure();

SessionFactory factory = config.buildSessionFactory();

try (Session session = factory.openSession()) {
    Transaction tx = session.beginTransaction();

    Alien alien = new Alien();
    alien.setAid(201);
    alien.setAname("Aman");
    alien.setTech("Hibernate");

    session.persist(alien);

    Alien fetched = session.get(Alien.class, 201);
    fetched.setTech("Hibernate ORM");

    tx.commit();
}

factory.close();
```

### What happens

1. Hibernate reads config
2. session opens
3. transaction begins
4. new `Alien` is persisted
5. fetched object becomes managed
6. changing field triggers dirty checking
7. commit performs insert/update as needed

---

## 51. How To Revise Hibernate Fast

If revising quickly, focus on these in order:

1. ORM meaning
2. Hibernate architecture
3. entity annotations
4. `Configuration`, `SessionFactory`, `Session`, `Transaction`
5. CRUD with `persist`, `get/find`, `merge`, `remove`
6. entity lifecycle states
7. dirty checking
8. `flush` vs `commit`
9. HQL basics
10. relationships and fetch types

---

## 52. Final Memory Map

```text
Hibernate
 -> Java ORM framework

Entity
 -> Java class mapped to table

Configuration
 -> reads config and mappings

SessionFactory
 -> creates Session objects

Session
 -> performs DB operations

Transaction
 -> commit / rollback

persist()
 -> insert new object

get() / find()
 -> fetch by primary key

merge()
 -> copy detached state into managed entity

remove()
 -> delete entity

Dirty Checking
 -> managed object changes become SQL update

flush()
 -> send SQL

commit()
 -> make transaction permanent
```

---

## 53. Source Notes

This cheat sheet was aligned with:

- Jakarta Persistence API/state definitions for entity lifecycle and persistence context behavior
- Hibernate `Session` API documentation for methods like `persist()` and `merge()`
- Apache Maven lifecycle documentation for command behavior

Official references:

- https://jakarta.ee/specifications/platform/11/apidocs/jakarta/persistence/entitymanager
- https://jakarta.ee/specifications/persistence/3.2/jakarta-persistence-spec-3.2
- https://docs.jboss.org/hibernate/orm/6.0/javadocs/org/hibernate/Session.html
- https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle
