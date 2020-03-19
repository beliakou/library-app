### Requirements

To build application: Maven, JDK 11, Docker
To run application: Docker, Docker Compose  

### Start application

Build application:
```
mvn clean verify
```

Start with test database:
```
docker-compose up
```

Application is running on port 8080

Predefined credentials are:

- Login: `user`, password: `userPassword` - has `USER` role
- Login: `admin`, password: `adminPassword` - has `ADMIN` role

### Sample requests

Get all books:
```
curl -i -X GET -H 'Authorization:Basic dXNlcjp1c2VyUGFzc3dvcmQ=' localhost:8080/books
```

Create book:
```
curl -i -X POST -H 'Authorization:Basic YWRtaW46YWRtaW5QYXNzd29yZA==' -H 'Content-type:application/json' -d '{"isbn": "12345", "name": "Test book", "authorId": 1, "categories": [1,3]}' localhost:8080/books
```

Update book:
```
curl -i -X PUT -H 'Authorization:Basic YWRtaW46YWRtaW5QYXNzd29yZA==' -H 'Content-type:application/json' -d '{"isbn": "54321", "name": "Another book", "authorId": 2, "categories": [1,2]}' localhost:8080/books/7
```
