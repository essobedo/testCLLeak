# Project Description

This project aims to help to reproduce the `ClassLoader` leak that happens when using the `ClassValue` in a project using `Groovy` and `RestAssured`.

## Requirements

* Java 11 or higher
* Maven 3

## Steps to reproduce

Simple run the next command:

```
mvn clean test -Dclassvalue=true
```

This will launch the test using `GroovyClassValueJava7` which is a subtype of `ClassValue`. After a couple a seconds, the test should fail with the following error message:

```
[ERROR] java.lang.OutOfMemoryError: Java heap space
[ERROR] Terminating due to java.lang.OutOfMemoryError: Java heap space
```

A heap dump is automatically generated under the root directory with a name of type `java_pid%p.hprof` where `%p` is the process id of surefire.

To launch the same test with `GroovyClassValuePreJava7` which is the `Groovy` version of `ClassValue`, simply use the command:

```
mvn clean test -Dclassvalue=false
```

The test should pass