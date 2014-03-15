reflection-access-tests
=======================

Project contains jmh perfomance test of reflection in comparison with cglib and direct access

Hot to run tests:

mvn clean install 
java -jar target/microbenchmarks.jar ".*" -wi 5 -i 5 -f 1
