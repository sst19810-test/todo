# 📝 Todo App — Spring Boot REST API


---

## 🚀 Running the App

```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

App starts at: `http://localhost:8080`

---

## 📦 Project Structures

```
src/main/java/com/example/todo/
├── TodoApplication.java          # Entry point
├── controller/
│   └── TodoController.java       # REST endpoints
├── service/
│   └── TodoService.java          # Business logic + in-memory store
├── model/
│   ├── Todo.java                 # Todo entity
│   ├── TodoRequest.java          # Create/Update DTO (with validation)
│   └── ApiResponse.java          # Generic API response wrapper
└── exception/
    ├── TodoNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🔌 API Endpoints

| Method   | Endpoint                    | Description                          |
|----------|-----------------------------|--------------------------------------|
| `POST`   | `/api/todos`                | Create a new todo                    |
| `GET`    | `/api/todos`                | Get all todos                        |
| `GET`    | `/api/todos?completed=true` | Filter by completion status          |
| `GET`    | `/api/todos?priority=HIGH`  | Filter by priority (LOW/MEDIUM/HIGH) |
| `GET`    | `/api/todos?search=keyword` | Search by title or description       |
| `GET`    | `/api/todos/{id}`           | Get a todo by ID                     |
| `PUT`    | `/api/todos/{id}`           | Update a todo                        |
| `PATCH`  | `/api/todos/{id}/toggle`    | Toggle completion status             |
| `DELETE` | `/api/todos/{id}`           | Delete a todo                        |
| `DELETE` | `/api/todos/completed`      | Delete all completed todos           |
| `GET`    | `/api/todos/stats`          | Get statistics                       |

---

## 📋 Request / Response Examples

### Create a Todo
```http
POST /api/todos
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Build a REST API with in-memory storage",
  "priority": "HIGH",
  "completed": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Todo created successfully",
  "data": {
    "id": 6,
    "title": "Learn Spring Boot",
    "description": "Build a REST API with in-memory storage",
    "completed": false,
    "priority": "HIGH",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Get All Todos
```http
GET /api/todos
```

### Filter Todos
```http
GET /api/todos?completed=false
GET /api/todos?priority=HIGH
GET /api/todos?search=bug
```

### Update a Todo
```http
PUT /api/todos/1
Content-Type: application/json

{
  "title": "Buy groceries (updated)",
  "description": "Also get juice",
  "priority": "MEDIUM",
  "completed": false
}
```

### Toggle Completion
```http
PATCH /api/todos/1/toggle
```

### Get Stats
```http
GET /api/todos/stats
```
**Response:**
```json
{
  "success": true,
  "data": {
    "total": 5,
    "completed": 1,
    "pending": 4,
    "byPriority": {
      "LOW": 2,
      "MEDIUM": 1,
      "HIGH": 2
    }
  }
}
```

---

## 🏗️ Todo Model

| Field         | Type      | Required | Notes                        |
|---------------|-----------|----------|------------------------------|
| `title`       | String    | ✅ Yes   | 1–100 chars                  |
| `description` | String    | ❌ No    | Max 500 chars                |
| `completed`   | boolean   | ❌ No    | Default: `false`             |
| `priority`    | Enum      | ❌ No    | `LOW`, `MEDIUM`, `HIGH`      |

---

## ⚡ Quick Test with cURL

```bash
# Create
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"My first todo","priority":"HIGH"}'
h
# Get all
curl http://localhost:8080/api/todos

# Toggle complete
curl -X PATCH http://localhost:8080/api/todos/1/toggle

# Delete
curl -X DELETE http://localhost:8080/api/todos/1

# Stats
curl http://localhost:8080/api/todos/stats
```

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2**
- **Spring Web MVC**
- **Bean Validation (JSR-380)**
- **Lombok**
- **ConcurrentHashMap** (in-memory store)
- **NO localhost no nothingsdfs*
- 
  
