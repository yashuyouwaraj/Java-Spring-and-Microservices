# JDBC Cheat Note

This note is a practical reference for learning and revising JDBC.

It covers:

- What JDBC is
- How JDBC works
- How to connect Java to a database
- SQL command categories used from JDBC
- `Statement`, `PreparedStatement`, and `CallableStatement`
- `executeQuery`, `executeUpdate`, `execute`, batching, and transactions
- `ResultSet`, metadata, and common methods
- Common examples for insert, select, update, delete, and batch update
- Best practices and common mistakes

## 1. What Is JDBC?

JDBC stands for Java Database Connectivity.

It is the standard Java API used to connect Java applications to relational databases such as:

- MySQL
- Oracle Database
- PostgreSQL
- SQL Server
- MariaDB

JDBC lets Java code:

- open a database connection
- send SQL statements
- get results back
- update data
- manage transactions

## 2. JDBC Architecture

Main parts:

- Java application
- JDBC API (`java.sql` and `javax.sql`)
- JDBC driver
- Database

Flow:

1. Java program loads the JDBC driver.
2. Java gets a `Connection`.
3. Java creates a statement object.
4. Java sends SQL to the database.
5. Database returns either:
   - a `ResultSet`, or
   - an update count
6. Java closes resources.

## 3. Important JDBC Packages

## `java.sql`

Core JDBC package. Most commonly used classes and interfaces:

- `DriverManager`
- `Connection`
- `Statement`
- `PreparedStatement`
- `CallableStatement`
- `ResultSet`
- `ResultSetMetaData`
- `DatabaseMetaData`
- `SQLException`
- `Types`

## `javax.sql`

Commonly used more in enterprise apps:

- `DataSource`
- `RowSet`

For your current learning projects, `java.sql` is the main package.

## 4. JDBC Driver Types

Historically JDBC mentions 4 driver types:

1. Type 1: JDBC-ODBC bridge
2. Type 2: Native API driver
3. Type 3: Network protocol driver
4. Type 4: Thin / pure Java driver

Today, most real projects use Type 4 drivers.

Example for MySQL:

- Driver class: `com.mysql.cj.jdbc.Driver`
- Jar: MySQL Connector/J

## 5. Steps To Connect Java To Database

Basic steps:

1. Add JDBC driver jar to classpath.
2. Load/register driver.
3. Get `Connection`.
4. Create `Statement` or `PreparedStatement`.
5. Execute SQL.
6. Process results.
7. Close resources.

## 6. Basic JDBC Connection Example

```java
import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnectExample {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/jdbclearning";
        String user = "root";
        String password = "Root@1234";

        Connection con = DriverManager.getConnection(url, user, password);
        System.out.println("Connection successful");
        con.close();
    }
}
```

## 7. Connection URL Format

General pattern:

```text
jdbc:subprotocol:subname
```

MySQL example:

```text
jdbc:mysql://localhost:3306/jdbclearning
```

Common MySQL URL with options:

```text
jdbc:mysql://localhost:3306/jdbclearning?useSSL=false&serverTimezone=UTC
```

Meaning:

- `jdbc` -> JDBC protocol
- `mysql` -> database/driver type
- `localhost` -> host
- `3306` -> port
- `jdbclearning` -> database name

## 8. Ways To Get Connection

## Using `DriverManager`

Best for learning and small standalone apps.

```java
Connection con = DriverManager.getConnection(url, user, password);
```

## Using `DataSource`

Preferred in larger applications because it supports:

- better configuration
- connection pooling
- container integration

## 9. SQL Command Categories You Should Know

JDBC sends SQL to the database. So SQL categories matter.

## DDL: Data Definition Language

Used to define database structure:

- `CREATE`
- `ALTER`
- `DROP`
- `TRUNCATE`
- `RENAME`

Example:

```sql
CREATE TABLE studentinfo (
    id INT PRIMARY KEY,
    sname VARCHAR(50),
    sage INT,
    scity VARCHAR(50)
);
```

## DML: Data Manipulation Language

Used to change data:

- `INSERT`
- `UPDATE`
- `DELETE`

Examples:

```sql
INSERT INTO studentinfo(id, sname, sage, scity) VALUES (1, 'Yashu', 22, 'Delhi');
UPDATE studentinfo SET sage = 23 WHERE id = 1;
DELETE FROM studentinfo WHERE id = 1;
```

## DQL: Data Query Language

Used to read data:

- `SELECT`

Example:

```sql
SELECT * FROM studentinfo;
```

## TCL: Transaction Control Language

Used to manage transactions:

- `COMMIT`
- `ROLLBACK`
- `SAVEPOINT`

## DCL: Data Control Language

Used for permissions:

- `GRANT`
- `REVOKE`

## 10. Main JDBC Statement Types

JDBC has 3 main statement interfaces:

1. `Statement`
2. `PreparedStatement`
3. `CallableStatement`

## 11. Statement

Used when SQL is static and simple.

Example:

```java
Statement stmt = con.createStatement();
```

Example insert:

```java
String sql = "INSERT INTO studentinfo VALUES (1, 'Yashu', 22, 'Delhi')";
int rows = stmt.executeUpdate(sql);
```

Example select:

```java
String sql = "SELECT * FROM studentinfo";
ResultSet rs = stmt.executeQuery(sql);
```

## Important methods of `Statement`

- `executeQuery(String sql)` -> returns `ResultSet`, mainly for `SELECT`
- `executeUpdate(String sql)` -> returns `int`, mainly for `INSERT`, `UPDATE`, `DELETE`, DDL
- `execute(String sql)` -> returns `boolean`, used when result may be either data or update count
- `addBatch(String sql)` -> adds SQL command to batch
- `executeBatch()` -> executes all batch commands
- `clearBatch()` -> removes batched commands
- `close()` -> closes statement
- `getResultSet()` -> gets current `ResultSet`
- `getUpdateCount()` -> gets update count

## When to use `Statement`

Use it when:

- SQL is fixed
- there are no input parameters
- you are learning basic JDBC first

Avoid it for user input because of SQL injection risk.

## 12. PreparedStatement

Used for parameterized SQL.

This is the most commonly used JDBC statement type in real applications.

Advantages:

- safer against SQL injection
- easier to reuse
- cleaner code
- often faster for repeated execution

Example:

```java
String sql = "INSERT INTO studentinfo(id, sname, sage, scity) VALUES (?, ?, ?, ?)";
PreparedStatement ps = con.prepareStatement(sql);

ps.setInt(1, 1);
ps.setString(2, "Yashu");
ps.setInt(3, 22);
ps.setString(4, "Delhi");

int rows = ps.executeUpdate();
```

## Important setter methods of `PreparedStatement`

- `setInt(index, value)`
- `setString(index, value)`
- `setDouble(index, value)`
- `setFloat(index, value)`
- `setBoolean(index, value)`
- `setDate(index, value)`
- `setTime(index, value)`
- `setTimestamp(index, value)`
- `setLong(index, value)`
- `setNull(index, sqlType)`
- `setObject(index, value)`

Indexes start from `1`, not `0`.

## Important execution methods of `PreparedStatement`

- `executeQuery()`
- `executeUpdate()`
- `execute()`
- `addBatch()`
- `clearParameters()`
- `close()`

Notice:

- `Statement` methods take SQL as argument.
- `PreparedStatement` methods usually do not, because SQL is already given at creation time.

## Example: Select with `PreparedStatement`

```java
String sql = "SELECT * FROM studentinfo WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setInt(1, 1);

ResultSet rs = ps.executeQuery();
while (rs.next()) {
    System.out.println(rs.getInt("id") + " " + rs.getString("sname"));
}
```

## Example: Update with `PreparedStatement`

```java
String sql = "UPDATE studentinfo SET sage = ? WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setInt(1, 23);
ps.setInt(2, 1);

int rows = ps.executeUpdate();
if (rows > 0) {
    System.out.println("Update successful");
}
```

## Example: Delete with `PreparedStatement`

```java
String sql = "DELETE FROM studentinfo WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setInt(1, 1);

int rows = ps.executeUpdate();
System.out.println("Deleted rows: " + rows);
```

## 13. CallableStatement

Used to call stored procedures from Java.

Example:

```java
CallableStatement cs = con.prepareCall("{call getStudentById(?)}");
cs.setInt(1, 1);
ResultSet rs = cs.executeQuery();
```

Common methods:

- `registerOutParameter(index, sqlType)`
- `setInt(...)`
- `setString(...)`
- `execute()`
- `executeQuery()`
- `executeUpdate()`

Example with output parameter:

```java
CallableStatement cs = con.prepareCall("{call get_student_name(?, ?)}");
cs.setInt(1, 1);
cs.registerOutParameter(2, java.sql.Types.VARCHAR);

cs.execute();
String name = cs.getString(2);
```

## 14. The 3 Main Execute Methods

This is one of the most important JDBC interview and exam topics.

## `executeQuery()`

Use for:

- `SELECT`

Returns:

- `ResultSet`

Example:

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM studentinfo");
```

## `executeUpdate()`

Use for:

- `INSERT`
- `UPDATE`
- `DELETE`
- DDL like `CREATE TABLE`, `DROP TABLE`, `ALTER TABLE`

Returns:

- `int` update count

Meaning of return value:

- `> 0` -> rows affected
- `0` -> no rows affected, or sometimes DDL executed

Example:

```java
int rows = stmt.executeUpdate("UPDATE studentinfo SET sage = 22 WHERE id = 1");
```

## `execute()`

Use when:

- you do not know whether SQL returns a `ResultSet` or update count
- calling dynamic SQL or some stored procedures

Returns:

- `true` if first result is a `ResultSet`
- `false` if first result is an update count or no result

Example:

```java
boolean result = stmt.execute("SELECT * FROM studentinfo");
if (result) {
    ResultSet rs = stmt.getResultSet();
}
```

## 15. ResultSet

`ResultSet` stores the data returned by a query.

Example:

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM studentinfo");
```

Each row is accessed using the cursor.

Initially the cursor is before the first row.

## Common navigation methods

- `next()`
- `previous()`
- `first()`
- `last()`
- `beforeFirst()`
- `afterLast()`
- `absolute(int row)`
- `relative(int rows)`

Most basic JDBC usage relies on `next()`.

## Common getter methods

- `getInt(columnIndex)`
- `getInt(columnLabel)`
- `getString(columnIndex)`
- `getString(columnLabel)`
- `getDouble(...)`
- `getDate(...)`
- `getObject(...)`
- `getBoolean(...)`

Example:

```java
while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("sname");
    int age = rs.getInt("sage");
    String city = rs.getString("scity");

    System.out.println(id + " " + name + " " + age + " " + city);
}
```

## ResultSet types

- `TYPE_FORWARD_ONLY`
- `TYPE_SCROLL_INSENSITIVE`
- `TYPE_SCROLL_SENSITIVE`

## ResultSet concurrency

- `CONCUR_READ_ONLY`
- `CONCUR_UPDATABLE`

Example:

```java
Statement stmt = con.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_READ_ONLY
);
```

## 16. ResultSetMetaData

Used to get information about query result columns.

Example:

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM studentinfo");
ResultSetMetaData meta = rs.getMetaData();

int count = meta.getColumnCount();
for (int i = 1; i <= count; i++) {
    System.out.println(meta.getColumnName(i) + " -> " + meta.getColumnTypeName(i));
}
```

Common methods:

- `getColumnCount()`
- `getColumnName(int column)`
- `getColumnLabel(int column)`
- `getColumnType(int column)`
- `getColumnTypeName(int column)`
- `isNullable(int column)`

## 17. DatabaseMetaData

Used to get information about the database and driver.

Example:

```java
DatabaseMetaData meta = con.getMetaData();

System.out.println(meta.getDatabaseProductName());
System.out.println(meta.getDatabaseProductVersion());
System.out.println(meta.getDriverName());
System.out.println(meta.getURL());
System.out.println(meta.getUserName());
```

Common uses:

- find database name
- find driver version
- check supported features
- inspect tables and columns

## 18. Batch Updates

Batch update means sending multiple update statements together.

Useful for:

- performance
- bulk insert
- bulk update

Can be used with:

- `Statement`
- `PreparedStatement`
- `CallableStatement`

## Batch with `Statement`

```java
Statement stmt = con.createStatement();

stmt.addBatch("INSERT INTO studentinfo VALUES (101, 'A', 20, 'Pune')");
stmt.addBatch("INSERT INTO studentinfo VALUES (102, 'B', 21, 'Delhi')");
stmt.addBatch("UPDATE studentinfo SET sage = 25 WHERE id = 101");

int[] result = stmt.executeBatch();
```

## Batch with `PreparedStatement`

```java
String sql = "INSERT INTO studentinfo(id, sname, sage, scity) VALUES (?, ?, ?, ?)";
PreparedStatement ps = con.prepareStatement(sql);

ps.setInt(1, 201);
ps.setString(2, "Ram");
ps.setInt(3, 20);
ps.setString(4, "Hyd");
ps.addBatch();

ps.setInt(1, 202);
ps.setString(2, "Shyam");
ps.setInt(3, 21);
ps.setString(4, "Chennai");
ps.addBatch();

int[] result = ps.executeBatch();
```

Important methods:

- `addBatch()`
- `addBatch(String sql)`
- `executeBatch()`
- `clearBatch()`

Return type:

- `int[]`, each element shows update count for one command

## 19. Transactions

A transaction is a group of SQL operations treated as one unit.

Important for:

- consistency
- rollback on failure

By default many JDBC connections use auto-commit mode.

That means each statement is committed automatically.

## Important transaction methods

- `setAutoCommit(boolean)`
- `commit()`
- `rollback()`
- `setSavepoint()`
- `rollback(Savepoint sp)`
- `releaseSavepoint(Savepoint sp)`
- `getTransactionIsolation()`
- `setTransactionIsolation(level)`

## Transaction example

```java
try {
    con.setAutoCommit(false);

    PreparedStatement ps1 =
        con.prepareStatement("UPDATE account SET balance = balance - ? WHERE id = ?");
    PreparedStatement ps2 =
        con.prepareStatement("UPDATE account SET balance = balance + ? WHERE id = ?");

    ps1.setDouble(1, 500);
    ps1.setInt(2, 1);
    ps1.executeUpdate();

    ps2.setDouble(1, 500);
    ps2.setInt(2, 2);
    ps2.executeUpdate();

    con.commit();
} catch (Exception e) {
    con.rollback();
}
```

## Transaction isolation levels

- `Connection.TRANSACTION_NONE`
- `Connection.TRANSACTION_READ_UNCOMMITTED`
- `Connection.TRANSACTION_READ_COMMITTED`
- `Connection.TRANSACTION_REPEATABLE_READ`
- `Connection.TRANSACTION_SERIALIZABLE`

Example:

```java
con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
```

## 20. Create Table Example

```java
Statement stmt = con.createStatement();

String sql = "CREATE TABLE studentinfo (" +
             "id INT PRIMARY KEY, " +
             "sname VARCHAR(50), " +
             "sage INT, " +
             "scity VARCHAR(50))";

stmt.executeUpdate(sql);
```

## 21. Insert Example

```java
String sql = "INSERT INTO studentinfo(id, sname, sage, scity) VALUES (?, ?, ?, ?)";
PreparedStatement ps = con.prepareStatement(sql);

ps.setInt(1, 1);
ps.setString(2, "Yashu");
ps.setInt(3, 22);
ps.setString(4, "Pune");

int rows = ps.executeUpdate();
System.out.println("Inserted rows: " + rows);
```

## 22. Select Example

```java
String sql = "SELECT id, sname, sage, scity FROM studentinfo";
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery();

while (rs.next()) {
    System.out.println(
        rs.getInt("id") + " " +
        rs.getString("sname") + " " +
        rs.getInt("sage") + " " +
        rs.getString("scity")
    );
}
```

## 23. Update Example

```java
String sql = "UPDATE studentinfo SET sage = ? WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);

ps.setInt(1, 25);
ps.setInt(2, 1);

int rows = ps.executeUpdate();
if (rows > 0) {
    System.out.println("Updated successfully");
} else {
    System.out.println("No matching row found");
}
```

## 24. Delete Example

```java
String sql = "DELETE FROM studentinfo WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setInt(1, 1);

int rows = ps.executeUpdate();
System.out.println("Deleted rows: " + rows);
```

## 25. Full Simple JDBC Program

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcFullExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbclearning";
        String user = "root";
        String password = "Root@1234";

        String sql = "SELECT * FROM studentinfo WHERE id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (
                Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement ps = con.prepareStatement(sql)
            ) {
                ps.setInt(1, 1);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getInt("id"));
                        System.out.println(rs.getString("sname"));
                        System.out.println(rs.getInt("sage"));
                        System.out.println(rs.getString("scity"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 26. Try-With-Resources

This is the best modern way to close JDBC resources.

Example:

```java
String sql = "SELECT * FROM studentinfo";

try (
    Connection con = DriverManager.getConnection(url, user, password);
    PreparedStatement ps = con.prepareStatement(sql);
    ResultSet rs = ps.executeQuery()
) {
    while (rs.next()) {
        System.out.println(rs.getString("sname"));
    }
}
```

Benefits:

- automatic closing
- cleaner code
- fewer resource leaks

## 27. SQLException

JDBC errors are usually handled with `SQLException`.

Example:

```java
catch (SQLException e) {
    System.out.println("Message: " + e.getMessage());
    System.out.println("SQLState: " + e.getSQLState());
    System.out.println("ErrorCode: " + e.getErrorCode());
}
```

Useful methods:

- `getMessage()`
- `getSQLState()`
- `getErrorCode()`
- `printStackTrace()`

## 28. Common Connection Methods

Most useful methods of `Connection`:

- `createStatement()`
- `prepareStatement(String sql)`
- `prepareCall(String sql)`
- `setAutoCommit(boolean)`
- `commit()`
- `rollback()`
- `close()`
- `isClosed()`
- `getMetaData()`
- `setReadOnly(boolean)`
- `setTransactionIsolation(int level)`
- `getTransactionIsolation()`
- `setSavepoint()`

## 29. Common Statement Methods

Most useful methods of `Statement`:

- `executeQuery(String sql)`
- `executeUpdate(String sql)`
- `execute(String sql)`
- `addBatch(String sql)`
- `executeBatch()`
- `clearBatch()`
- `close()`
- `getResultSet()`
- `getUpdateCount()`

## 30. Common PreparedStatement Methods

Most useful methods of `PreparedStatement`:

- `setInt(...)`
- `setString(...)`
- `setDouble(...)`
- `setLong(...)`
- `setBoolean(...)`
- `setDate(...)`
- `setTimestamp(...)`
- `setObject(...)`
- `clearParameters()`
- `executeQuery()`
- `executeUpdate()`
- `execute()`
- `addBatch()`
- `close()`

## 31. Common ResultSet Methods

Most useful methods of `ResultSet`:

- `next()`
- `previous()`
- `first()`
- `last()`
- `getInt(...)`
- `getString(...)`
- `getDouble(...)`
- `getDate(...)`
- `getObject(...)`
- `wasNull()`
- `close()`

## 32. Common CallableStatement Methods

Most useful methods of `CallableStatement`:

- `registerOutParameter(...)`
- `getInt(...)`
- `getString(...)`
- `getObject(...)`
- `execute()`
- `executeQuery()`
- `executeUpdate()`

## 33. Frequently Used SQL Commands In JDBC Programs

These are the SQL commands you will most often send through JDBC:

- `CREATE DATABASE`
- `CREATE TABLE`
- `ALTER TABLE`
- `DROP TABLE`
- `TRUNCATE TABLE`
- `INSERT INTO`
- `UPDATE`
- `DELETE`
- `SELECT`
- `WHERE`
- `ORDER BY`
- `GROUP BY`
- `HAVING`
- `INNER JOIN`
- `LEFT JOIN`
- `RIGHT JOIN`
- `COMMIT`
- `ROLLBACK`
- `SAVEPOINT`
- `GRANT`
- `REVOKE`

Important point:

JDBC itself does not invent a separate query language.

You use SQL through JDBC.

## 34. JDBC Types Mapping Examples

Some common SQL-to-Java mappings:

- SQL `INT` -> Java `int` / `Integer`
- SQL `BIGINT` -> Java `long` / `Long`
- SQL `VARCHAR` -> Java `String`
- SQL `DOUBLE` -> Java `double` / `Double`
- SQL `DATE` -> `java.sql.Date`
- SQL `TIME` -> `java.sql.Time`
- SQL `TIMESTAMP` -> `java.sql.Timestamp`
- SQL `BOOLEAN` -> `boolean` / `Boolean`

## 35. Auto Loading Driver vs `Class.forName`

Older code often uses:

```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

In modern JDBC 4+, drivers can often auto-register if the driver jar is on the classpath.

Still, many beginner examples keep `Class.forName(...)` because:

- it is explicit
- it helps understanding
- it works clearly in learning code

## 36. Common Mistakes In JDBC

- wrong package or classpath for driver jar
- wrong database URL
- wrong username/password
- not closing resources
- using `Statement` with user input
- mixing up parameter order in `PreparedStatement`
- assuming `executeUpdate()` returning `0` always means an error
- forgetting `con.commit()` when auto-commit is off
- reading wrong column name from `ResultSet`
- using index `0` in `PreparedStatement` or `ResultSet`

## 37. JDBC Best Practices

- prefer `PreparedStatement` over `Statement`
- use try-with-resources
- keep DB credentials outside source code in real projects
- use transactions for related operations
- check update count after `executeUpdate()`
- log useful SQL exceptions
- validate user input before storing
- separate utility code for connection handling
- use batch updates for bulk operations

## 38. Small Utility Class Example

```java
package com.yashu.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JavaUtil {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/jdbclearning";
        String user = "root";
        String password = "Root@1234";
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

## 39. Interview-Style Quick Differences

## `Statement` vs `PreparedStatement`

- `Statement` uses raw SQL string each time
- `PreparedStatement` uses placeholders `?`
- `PreparedStatement` is safer and usually preferred

## `executeQuery()` vs `executeUpdate()`

- `executeQuery()` -> for `SELECT`, returns `ResultSet`
- `executeUpdate()` -> for `INSERT`, `UPDATE`, `DELETE`, DDL, returns `int`

## `next()` vs `getXXX()`

- `next()` moves cursor to next row
- `getXXX()` reads column value from current row

## `Connection` vs `Statement`

- `Connection` represents session with DB
- `Statement` represents SQL executor created from a connection

## 40. Short Revision Sheet

Remember this line:

`Load driver -> Get connection -> Create statement -> Execute SQL -> Process result -> Close resources`

Most common objects:

- `Connection`
- `PreparedStatement`
- `ResultSet`

Most common methods:

- `getConnection(...)`
- `prepareStatement(...)`
- `setInt(...)`, `setString(...)`
- `executeQuery()`
- `executeUpdate()`
- `next()`
- `getInt(...)`, `getString(...)`
- `commit()`, `rollback()`
- `close()`

## 41. Recommended Learning Order

1. Connection
2. `Statement`
3. `PreparedStatement`
4. `ResultSet`
5. CRUD operations
6. User input with parameterized queries
7. Batch update
8. Transactions
9. Metadata
10. Stored procedures

## 42. Official References Used

This cheat note was prepared using official Oracle JDBC tutorial/API pages and the official MySQL Connector/J documentation:

- Oracle Java Tutorials JDBC basics: https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html
- Oracle Getting Started with JDBC: https://docs.oracle.com/javase/tutorial/jdbc/basics/gettingstarted.html
- Oracle Establishing a Connection: https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
- Oracle Processing SQL Statements: https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html
- Oracle Using Prepared Statements: https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
- Oracle Retrieving and Modifying Values from Result Sets: https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
- Oracle Using Transactions: https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html
- Oracle `java.sql` package summary: https://docs.oracle.com/en/java/javase/12/docs/api/java.sql/java/sql/package-summary.html
- MySQL Connector/J reference: https://dev.mysql.com/doc/connector-j/en/connector-j-reference.html
- MySQL Connector/J download page: https://dev.mysql.com/downloads/connector/j/

## 43. Final Advice

For learning JDBC well, focus on these 5 things first:

1. how to connect
2. how `PreparedStatement` works
3. when to use `executeQuery` vs `executeUpdate`
4. how to read data using `ResultSet`
5. how to commit and rollback transactions

If you master these, the rest of JDBC becomes much easier.
