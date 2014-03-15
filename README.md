reflection-access-tests
=======================

Project contains jmh perfomance test of reflection in comparison with cglib and direct access


How to run tests

```bash
mvn clean install 
java -jar target/microbenchmarks.jar ".*" -wi 5 -i 5 -f 1
```
