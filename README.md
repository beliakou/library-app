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

Get book by id:
```
curl -i -X GET -H 'Authorization:Basic dXNlcjp1c2VyUGFzc3dvcmQ=' localhost:8080/books/4
```

Create book:
```
curl -i -X POST \
    -H 'Authorization:Basic YWRtaW46YWRtaW5QYXNzd29yZA==' \
    -H 'Content-type:application/json' \
    -d '{"isbn": "12345", "name": "Test book", "author": "http://localhost:8080/api/authors/3", "categories": ["http://localhost:8080/api/categories/1", "http://localhost:8080/api/categories/3"]}'\
    localhost:8080/api/books
```

Update book:
```
curl -i -X PATCH \                                                                                     
    -H 'Authorization:Basic YWRtaW46YWRtaW5QYXNzd29yZA==' \
    -H 'Content-type:application/json' \
    -d '{"isbn": "54321", "name": "Updated test book", "author": "http://localhost:8080/api/authors/1", "categories": ["http://localhost:8080/api/categories/2", "http://localhost:8080/api/categories/4"]}'\
    localhost:8080/api/books/5
```
