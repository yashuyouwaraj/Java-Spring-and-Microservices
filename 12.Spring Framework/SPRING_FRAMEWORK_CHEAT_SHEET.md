# Spring Framework Cheat Sheet

One-place revision sheet for the projects inside `12.Spring Framework`.

## 1. What Is Spring?

Spring is a Java framework used to build applications in a clean, modular, and maintainable way.

Its main job is:

- create objects for you
- manage object life cycle
- connect objects together
- reduce manual `new` keyword usage
- support enterprise apps, web apps, REST APIs, microservices, and more

Short idea:

Instead of this:

```java
Laptop laptop = new Laptop();
Dev dev = new Dev(laptop);
```

Spring does this wiring for us.

## 2. Core Theory You Must Remember

### IOC

IOC = Inversion of Control.

Normally, your class creates dependencies itself.
In Spring, control moves to the Spring container.

Spring container:

- creates beans
- injects dependencies
- manages configuration

### Bean

A bean is simply an object created and managed by Spring.

Examples from your folder:

- `Dev`
- `Laptop`
- `Desktop`

### Dependency Injection

Dependency Injection means giving one object the required dependency from outside.

Example:

- `Dev` needs `Laptop`
- Spring injects `Laptop` into `Dev`

Types used in your projects:

- setter injection
- constructor injection
- autowiring
- annotation-based injection in Spring Boot

### ApplicationContext

`ApplicationContext` is the Spring container.

Classic Spring example:

```java
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
Dev obj = context.getBean(Dev.class);
```

This means:

1. load configuration
2. create beans
3. return required bean

## 3. How Spring Works Internally

Very short flow:

1. You define beans and configuration.
2. Spring reads XML or annotations.
3. Spring container creates objects.
4. Spring injects dependencies.
5. You ask Spring for a bean.
6. You use that bean in your app.

In your code:

- XML projects use `spring.xml`
- Boot projects use annotations like `@Component`, `@Autowired`, `@SpringBootApplication`

## 4. Your Folder Structure and What Each Project Teaches

### `DemoSpring`

Basic Spring XML bean creation.

Use this to revise:

- what a bean is
- how `spring.xml` works
- how container loads objects

### `Constructor and Setter Injection`

Use this to revise:

- constructor injection
- setter injection
- primitive value injection
- reference injection

### `Autowire in Spring`

Use this to revise:

- classic XML autowiring
- `autowire="byName"`
- interface-based dependency injection
- selecting implementation bean

### `DependencyInjection using Springboot`

Use this to revise:

- component scanning
- `@Component`
- Boot container startup
- bean retrieval using `context.getBean(...)`

### `Autowire using Spring Boot`

Use this to revise:

- `@Autowired`
- `@Qualifier`
- `@Primary`
- field vs constructor vs setter injection

### `DemoApp`

Use this to revise:

- first web app
- `@RestController`
- `@RequestMapping`
- running Spring Boot web server

## 5. Classic Spring vs Spring Boot

### Spring Framework (classic)

- more manual configuration
- often uses XML or explicit config classes
- you load `ApplicationContext` yourself
- good for learning bean wiring basics

### Spring Boot

- built on top of Spring
- less boilerplate
- auto-configuration support
- embedded server for web apps
- easy to run with Maven wrapper

Revision line:

Spring teaches the concepts.
Spring Boot makes those concepts faster to use in real projects.

## 6. Required Setup

### Tools

Install:

- JDK
- Maven, or use Maven Wrapper (`mvnw`, `mvnw.cmd`)
- IntelliJ IDEA or VS Code

### Java Version

Your Boot projects currently use:

```xml
<java.version>25</java.version>
```

So you need a matching JDK if you want to run them exactly as configured.

### Maven Dependency Pattern

Classic Spring projects use:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.6</version>
</dependency>
```

Boot projects use:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.5</version>
</parent>
```

and starter dependencies like:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
```

or:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

## 7. Configuration Files You Need to Know

### A. `pom.xml`

Used for:

- project metadata
- dependencies
- plugins
- Java version

### B. `spring.xml`

Used in classic Spring projects for:

- declaring beans
- wiring dependencies
- constructor injection
- setter injection
- autowiring rules

### C. `application.properties`

Used in Spring Boot for:

- app configuration
- server port
- logging
- custom properties

Example:

```properties
spring.application.name=demo
server.port=8081
```

## 8. Bean Configuration in XML

Basic syntax:

```xml
<bean id="dev" class="com.yashu.Dev"/>
<bean id="lap1" class="com.yashu.Laptop"/>
```

Meaning:

- `id` = bean name
- `class` = actual Java class Spring should create

## 9. Setter Injection

Setter injection means Spring first creates the object, then calls setter methods.

Example Java:

```java
public class Dev {
    private Laptop laptop;

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }
}
```

Example XML:

```xml
<bean id="dev" class="com.yashu.Dev">
    <property name="laptop" ref="lap1"/>
</bean>

<bean id="lap1" class="com.yashu.Laptop"/>
```

Use when:

- dependency is optional
- object can exist before dependency is injected

## 10. Constructor Injection

Constructor injection means dependency is passed when object is created.

Example Java from your pattern:

```java
public class Dev {
    private Laptop laptop;

    public Dev(Laptop laptop) {
        this.laptop = laptop;
    }
}
```

Example XML from your project style:

```xml
<bean id="dev" class="com.yashu.Dev">
    <constructor-arg ref="lap1"/>
</bean>

<bean id="lap1" class="com.yashu.Laptop"/>
```

Injecting primitive values:

```xml
<bean id="dev" class="com.yashu.Dev">
    <constructor-arg index="0" value="14"/>
</bean>
```

Use when:

- dependency is mandatory
- object should not be created without required dependency

Best revision point:

Constructor injection is usually preferred for required dependencies.

## 11. Reference Injection vs Value Injection

### Reference Injection

Used when dependency is another object.

```xml
<property name="laptop" ref="lap1"/>
```

### Value Injection

Used when dependency is a simple value.

```xml
<property name="age" value="12"/>
```

Remember:

- `ref` for object bean
- `value` for primitive/string value

## 12. Autowiring in Classic Spring XML

Your project uses:

```xml
<bean id="dev" class="com.yashu.Dev" autowire="byName"/>
```

Your `Dev` class has:

```java
private Computer com;
```

And XML has:

```xml
<bean id="com" class="com.yashu.Laptop" primary="true"/>
```

How `byName` works:

- Spring looks for a bean whose id matches the property name
- property name is `com`
- bean id is also `com`
- so Spring injects that bean

Common XML autowire modes:

- `byName`
- `byType`
- `constructor`

Quick memory rule:

- `byName` matches variable/property name
- `byType` matches data type
- `constructor` injects through constructor

## 13. Spring Boot Basics

Main class pattern from your Boot projects:

```java
@SpringBootApplication
public class MyAppApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MyAppApplication.class, args);
    }
}
```

`@SpringBootApplication` combines major setup features:

- configuration
- component scanning
- auto-configuration

What happens on run:

1. Boot starts Spring container
2. scans classes in package and subpackages
3. creates beans for annotated components
4. injects dependencies
5. starts embedded server if web dependency exists

## 14. `@Component`

Used to tell Spring:

"Create an object of this class and manage it as a bean."

Example from your project:

```java
@Component
public class Dev {
}
```

Without `@Component`, Spring Boot will not automatically create that bean unless you define it manually.

## 15. `@Autowired`

Used to inject dependency automatically.

Your project example:

```java
@Autowired
@Qualifier("laptop")
private Computer comp;
```

Meaning:

- inject a bean of type `Computer`
- if multiple beans exist, choose the bean named `laptop`

## 16. `@Qualifier` and `@Primary`

You have two implementations:

- `Laptop implements Computer`
- `Desktop implements Computer`

When multiple beans of same type exist, Spring gets confused unless you tell it which one to use.

### Option 1: `@Qualifier`

```java
@Autowired
@Qualifier("laptop")
private Computer comp;
```

### Option 2: `@Primary`

```java
@Component
@Primary
public class Laptop implements Computer {
}
```

Rule:

- use `@Qualifier` when you want a specific bean
- use `@Primary` when you want one default bean

## 17. Injection Types in Spring Boot

### Field Injection

Used in your project:

```java
@Autowired
private Computer comp;
```

Easy for learning, but not best for production design.

### Setter Injection

```java
private Computer comp;

@Autowired
public void setComp(Computer comp) {
    this.comp = comp;
}
```

### Constructor Injection

```java
private final Computer comp;

public Dev(Computer comp) {
    this.comp = comp;
}
```

Best revision point:

Constructor injection is generally the best practice for required dependencies.

## 18. Web Layer Basics from `DemoApp`

Your controller:

```java
@RestController
public class Hello {

    @RequestMapping("/")
    public String greet() {
        return "Hello world, Welcome to Spring Boot";
    }
}
```

Meaning:

- `@RestController` marks class as REST controller
- `@RequestMapping("/")` maps root URL
- method return value becomes HTTP response body

When app runs:

- Boot starts web server
- open browser at `http://localhost:8080/`
- response is shown

## 19. How To Start a Spring Project

### Option A: Classic Spring

1. Create Maven project.
2. Add `spring-context` dependency in `pom.xml`.
3. Create Java classes.
4. Create `spring.xml` in `src/main/resources`.
5. Define beans in XML.
6. Load `ApplicationContext`.
7. Get bean and use it.

### Option B: Spring Boot

1. Create Spring Boot Maven project.
2. Add starter dependencies.
3. Create main class with `@SpringBootApplication`.
4. Create components/controllers/services.
5. Run main class or Maven command.

## 20. How To Run Your Projects

Open terminal in the specific project folder.

### Classic Spring projects

Examples:

- `DemoSpring`
- `Constructor and Setter Injection`
- `Autowire in Spring`

Commands:

```powershell
mvn compile
mvn test
mvn package
```

If you want to run the main class directly after compilation:

```powershell
java -cp target/classes com.yashu.App
```

Note:

For dependency-based execution, IDE run is often easier for classic Spring beginner projects unless exec plugin is configured.

### Spring Boot projects

Examples:

- `DependencyInjection using Springboot`
- `Autowire using Spring Boot`
- `DemoApp`

Run using Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Or if Maven is installed:

```powershell
mvn spring-boot:run
```

Build jar:

```powershell
.\mvnw.cmd clean package
```

Run packaged jar:

```powershell
java -jar target\myApp-0.0.1-SNAPSHOT.jar
```

For `DemoApp`, jar name will match that project artifact instead.

## 21. Most Important Maven Commands

### `mvn compile`

- compiles source code

### `mvn test`

- runs tests

### `mvn package`

- creates jar

### `mvn clean`

- deletes old build output in `target`

### `mvn clean package`

- clean build from scratch

### `mvn spring-boot:run`

- runs Spring Boot app without manually creating jar

### `.\mvnw.cmd spring-boot:run`

- same as above, but uses project-provided Maven wrapper
- preferred when wrapper exists

## 22. Best Command Choice Per Folder

### `DemoSpring`

```powershell
mvn compile
java -cp target/classes com.yashu.App
```

### `Constructor and Setter Injection`

```powershell
mvn compile
java -cp target/classes com.yashu.App
```

### `Autowire in Spring`

```powershell
mvn compile
java -cp target/classes com.yashu.App
```

### `DependencyInjection using Springboot`

```powershell
.\mvnw.cmd spring-boot:run
```

### `Autowire using Spring Boot`

```powershell
.\mvnw.cmd spring-boot:run
```

### `DemoApp`

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080/
```

## 23. Example Revision Flow for Each Topic

### Basic XML Bean

```xml
<bean id="dev" class="com.yashu.Dev"/>
```

Spring creates:

```java
Dev dev = new Dev();
```

### Setter Injection

```xml
<bean id="dev" class="com.yashu.Dev">
    <property name="laptop" ref="lap1"/>
</bean>
```

Spring does conceptually:

```java
Dev dev = new Dev();
dev.setLaptop(lap1);
```

### Constructor Injection

```xml
<bean id="dev" class="com.yashu.Dev">
    <constructor-arg ref="lap1"/>
</bean>
```

Spring does conceptually:

```java
Dev dev = new Dev(lap1);
```

### Boot Component

```java
@Component
public class Dev {}
```

Spring Boot scans and creates bean automatically.

## 24. Important Classes/Annotations for Viva or Interview Revision

### Classes

- `ApplicationContext`
- `ClassPathXmlApplicationContext`
- `SpringApplication`

### XML tags

- `<bean>`
- `<property>`
- `<constructor-arg>`

### Annotations

- `@SpringBootApplication`
- `@Component`
- `@Autowired`
- `@Qualifier`
- `@Primary`
- `@RestController`
- `@RequestMapping`

## 25. Typical Questions and Short Answers

### What is a bean?

An object managed by Spring container.

### What is IOC?

Control of object creation is given to Spring container.

### What is dependency injection?

Providing required object from outside instead of creating it inside the class.

### Difference between constructor and setter injection?

- constructor injection is for required dependencies
- setter injection is for optional or changeable dependencies

### Difference between Spring and Spring Boot?

- Spring gives the framework and core concepts
- Spring Boot reduces setup and adds auto-configuration and embedded server support

### Why use `@Qualifier`?

To choose one bean when multiple beans of same type exist.

### Why use `@Primary`?

To mark one bean as default choice.

## 26. Common Mistakes

- forgetting to add dependency in `pom.xml`
- wrong package/class name in XML bean definition
- wrong bean id in `ref`
- using `byName` autowire but bean id does not match property name
- multiple beans of same type without `@Qualifier` or `@Primary`
- putting classes outside component scan package in Boot
- trying to run Boot app without matching JDK version

## 27. One-Minute Exam Revision

Remember this chain:

1. Spring container creates beans.
2. Beans are Java objects managed by Spring.
3. DI means Spring injects required dependencies.
4. In classic Spring, configuration is often in `spring.xml`.
5. In Boot, configuration is mostly annotation based.
6. `@Component` creates bean automatically.
7. `@Autowired` injects dependency.
8. `@Qualifier` selects one specific bean.
9. `@Primary` sets default bean.
10. `@RestController` handles web requests.

## 28. Quick Compare Table

| Topic | Classic Spring | Spring Boot |
|---|---|---|
| Setup | More manual | Faster |
| Configuration | XML / manual config | Annotation + auto-config |
| Container start | `ClassPathXmlApplicationContext` | `SpringApplication.run(...)` |
| Bean creation | XML `<bean>` | `@Component` |
| Dependency injection | XML or config | `@Autowired` / constructor injection |
| Web server | external/manual | embedded |

## 29. Folder-to-Concept Mapping

| Folder | Main Concept |
|---|---|
| `DemoSpring` | first bean creation with XML |
| `Constructor and Setter Injection` | setter and constructor DI |
| `Autowire in Spring` | XML autowiring |
| `DependencyInjection using Springboot` | Boot DI basics |
| `Autowire using Spring Boot` | `@Autowired`, `@Qualifier`, `@Primary` |
| `DemoApp` | REST controller and first web app |

## 30. Final Revision Summary

If you remember only one thing, remember this:

Spring manages objects for you.
Those managed objects are called beans.
Spring connects beans using Dependency Injection.
Classic Spring often uses XML.
Spring Boot uses annotations and reduces configuration.

That is the full base of everything in this folder.
