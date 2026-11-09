# E-Library Service

A small REST API for browsing books and managing borrow/return records.

## Tech stack

- Java 17
- Spring Boot 3
- Spring Web, Spring Data JPA, Bean Validation
- H2 database (in-memory, so the project runs without installing a database)
- Maven

## Run locally

Requirements: JDK 17+ and Maven 3.8+.

```bash
mvn spring-boot:run
```

The service starts at `http://localhost:8080`. Sample books are inserted on startup.

The H2 console is available at `http://localhost:8080/h2-console`:

- JDBC URL: `jdbc:h2:mem:elibrary`
- User name: `sa`
- Password: leave blank

## API

### Browse books

```http
GET /api/books?page=0&size=10
GET /api/books?keyword=java
```

Returns a paginated list. `keyword` searches the title and author.

### Get book details

```http
GET /api/books/1
```

### Borrow a book

```http
POST /api/borrowings
Content-Type: application/json

{
  "userId": 1001,
  "bookId": 1
}
```

### View current borrowings

```http
GET /api/borrowings?userId=1001
```

Only records that have not been returned are included.

### Return a book

```http
POST /api/borrowings/1/return?userId=1001
```

The borrowing record must belong to the supplied user.

## Design notes

- The project is intentionally a single Spring Boot application. The assignment is small enough that splitting it into services would add operational overhead without a clear benefit.
- Controllers handle HTTP input/output; services contain business rules; repositories handle persistence.
- A borrowing operation runs in a transaction. The book row is selected with a pessimistic write lock before stock is changed, so two requests cannot both borrow the last copy.
- A borrowing record is retained after return. Its status and return time provide a simple history rather than deleting the record.
- API errors use consistent HTTP status codes and a small JSON response.

## Assumptions and scope

- Authentication and user registration are outside this exercise. `userId` is supplied by the caller to keep the example focused on the library workflow. In a real service, it should come from the authenticated principal, not from a request parameter or body.
- There is no due-date, reservation queue, fine, or renewal workflow because those were not required.
- H2 is used to make the project easy to run locally. It can be replaced with MySQL by changing the datasource dependency and configuration.
- The sample data is for local demonstration only.
