## NOTE

An incomplete side project from Nov 2023 that I kinda finished in Jan 2025. 

I left this incomplete because IntelliJ provides a context action to change multi-line strings to text blocks since Java 13 preview came in! (curb your enthusiasm score plays 🫠) Yet I believed this project had some relevance until I learned about [OpenRewrite](https://docs.openrewrite.org/) that has a Gradle plugin that lets you run a script to migrate to text blocks.

Now that I found OpenRewrite, I am currently using it for an internal project at work. So this project now rests :/

But why the name JDoctor? Well, I thought I could salvage this to make a CLI tool to parse a Java codebase and analyze it, fix some things etc. But these things are better written with OpenRewrite recipes.


# Adopt Text Block
With the advent of text blocks in Java 13, we can now use triple quotes `"""` for multi-line strings. In an effort to adopt this in a large codebase, this project makes use of [javaparser](https://javaparser.org/) to parse existing multi-line strings and replace then with text blocks.

## Problem
At my org in the large codebase we work on, I have come across several 600+ character strings containing SQL queries that are cumbersome to read at one go. Also, I have seen instances multi-line strings with too many `\n` line escapes and `+` operators to concatenate strings that look ugly.      
Text blocks provide a better, more readable alternative for these cases.


Below is an example of a JPQL query
### Without text block
```java
String multiLineSqlQuery = "SELECT e "
        + "FROM Employee e "
        + "WHERE e.department = :dept "
        + "AND e.salary >= :minSalary";
```

### With text block
```java
String multiLineSqlQuery =  """
                    SELECT e
                    FROM Employee e
                    WHERE e.department = :dept
                        AND e.salary >= :minSalary
                    """;
```

## Solution
The initial idea was to write an IntelliJ plugin for this but it would be platform-dependent. Hence, with javaparser, a command-line utility can be provided which would simply take the file/directory path which needs to adopt text blocks.


## Finally, what does it do?

For below input Java file,

```java
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaExample {

    final String TAG = "JpaExample";
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();
        int salary = 100000;

        try {
            String multiLineSqlQuery = """
                    SELECT e
                    FROM Employee e
                    WHERE e.department = :dept
                    AND e.salary >= :minSalary
                    """;

            String multiLineSqlQueryWithVariables = "SELECT e "
                    + "FROM Employee e "
                    + "WHERE e.department = :dept "
                    + "AND e.salary >= " + salary;

            Query query = em.createQuery(multiLineSqlQuery);
            query.setParameter("dept", "Sales");
            query.setParameter("minSalary", 50000);

            List<Employee> results = query.getResultList();

            for (Employee employee : results) {
                System.out.println("Employee Name: " + employee.getName());
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
```

You get -

```java
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaExample {

    final String TAG = "JpaExample";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();
        int salary = 100000;
        try {
            String multiLineSqlQuery = """
                SELECT e
                FROM Employee e
                WHERE e.department = :dept
                AND e.salary >= :minSalary
                """;
            String multiLineSqlQueryWithVariables = """
                SELECT e  FROM Employee e  WHERE e.department = :dept  AND e.salary >=  salary""";
            Query query = em.createQuery(multiLineSqlQuery);
            query.setParameter("dept", "Sales");
            query.setParameter("minSalary", 50000);
            List<Employee> results = query.getResultList();
            for (Employee employee : results) {
                System.out.println("Employee Name: " + employee.getName());
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
```

So, it kinda works. 

I didn't fix it yet to inlining variables because this would look better if I use `String.format` over here. Again, finding the variable again from the parsed variable declarations with another vistor (maybe two) is a lot more effort than it is with OpenRewrite.