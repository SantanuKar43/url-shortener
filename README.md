# url-shortener

TODOS
---
1. Rate limiting -
   1. protect the server and the database

How to start the url-shortener application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/url-shortener-1.0-SNAPSHOT.jar server config-local.yml`
1. To check that your application is running enter url `http://localhost:80/hello-world`

Health Check
---

To see your applications health enter url `http://localhost:81/healthcheck`
