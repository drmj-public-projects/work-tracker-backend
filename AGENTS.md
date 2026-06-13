# AGENTS.md — Work Tracker Guidelines

## Architecture

Controllers → UseCases → Services → Repositories

## Responsibilities

**Controllers**

* HTTP only (request/response)
* No business logic

**UseCases**

* Business rules + validations
* 1 action = 1 use case
* Small, focused methods

**Services**

* Work with entities (not DTOs)
* CRUD + simple calculations
* No complex business logic

**Repositories**

* Data access only

---

## Core Rules

### UseCases

* Validate:

    * user ownership
    * organization ownership
    * business constraints (e.g. active session)

### Services

* Persist, fetch, calculate only

---

## Error Handling

* All messages in `ErrorMessage.java` (enum)
* Use the new constructor that accepts `ErrorMessage` directly:

```java
throw new BusinessException(ErrorMessage.CONSTANT);
throw new NotFoundException(ErrorMessage.CONSTANT);
```

* This automatically attaches the `errorCode` (derived from the enum name, e.g. `PLACE_NOT_FOUND`) to the response.
* If you must throw with a raw String, the `GlobalExceptionHandler` will attempt to resolve the `errorCode` by matching the message against the enum. Fallback is `GENERIC`.

---

## Lombok (Required)

* @RequiredArgsConstructor
* @Builder
* @Getter / @Setter
* @NoArgsConstructor / @AllArgsConstructor

---

## Soft Delete (All Entities)

```java
@SQLDelete(sql = "UPDATE table SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
```

---

## DTOs

* Never return entities

```java
@Builder
class ResponseDto {
  static ResponseDto fromEntity(Entity e) { ... }
}
```

---

## Auth User

```java
UUID userId = UUID.fromString(authentication.getName());
```

* Never trust request IDs

---

## API Response

* Always use:

```java
ApiResponse<T>
```

* Fields:
  - `status` (int): HTTP-like status code
  - `errorCode` (String): Machine-readable error code (e.g. `PLACE_NOT_FOUND`, `SESSION_OVERLAP`). Null on success.
  - `message` (String): Human-readable message in English
  - `data` (T): Payload on success, null on error

---

## Must Do

* Validate org ownership
* Filter deleted data
* 1 active session per user
* Calculate duration + total_pay in backend

---

## Avoid

* Logic in Controllers/Repositories
* Returning entities
* Hardcoded errors
* Large UseCases
* Unnecessary bidirectional relations

---

## Flow

Controller → UseCase → Service → Repository
