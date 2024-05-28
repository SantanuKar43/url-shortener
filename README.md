# url-shortener

TODOS
---
1. Handle concurrency -
   1. users trying to save the same url 
   2. or delete the same id 
   3. or delete the url and save it at the same time
2. Rate limiting -
   1. protect the server and the database
3. Request validations -
   1. set url max length/size

How to start the url-shortener application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/url-shortener-1.0-SNAPSHOT.jar server config-local.yml`
1. To check that your application is running enter url `http://localhost:80`

Health Check
---

To see your applications health enter url `http://localhost:81/healthcheck`
