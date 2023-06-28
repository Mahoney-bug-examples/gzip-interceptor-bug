# gzip-interceptor-bug

## Reproduce the bug

This app is to show that you cannot post more than 72K to the server.

1. Run `mvn test`
2. Observe that the `RequestSizeTest.can_post_more_than_72K_to_the_server` test
   fails

## How to start the gzip-interceptor-bug application

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/gzip-interceptor-bug-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`
