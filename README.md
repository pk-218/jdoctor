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

