# 3D Print Shop

A full stack 3D print shop built with Spring Boot, PostgreSQL, and vanilla HTML/JS.
Covers the full request lifecycle from browser → controller → service → repository → database.

## Tech Stack

- Java 17 + Spring Boot 3
- PostgreSQL + Spring Data JPA
- JWT Authentication (jjwt 0.12.6)
- Bean Validation (jakarta.validation)
- Vanilla HTML + CSS + JavaScript

## Features

- Browse and order 3D printed products
- Customer order tracking by email
- JWT protected admin panel
- Product management (add, edit, delete)
- Order management (view, update status, delete)
- Server side validation with clean error responses
- Global exception handling

## Setup

### 1. Create the database

```sql
CREATE DATABASE printshop;
```

### 2. Copy the example config

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

Then edit `application.yml` and fill in your values.

### 3. Set environment variables

**Mac/Linux:**
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your-super-secret-key-min-32-characters
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=yourpassword
```

**Windows:**
```cmd
set DB_USERNAME=postgres
set DB_PASSWORD=yourpassword
set JWT_SECRET=your-super-secret-key-min-32-characters
set ADMIN_USERNAME=admin
set ADMIN_PASSWORD=yourpassword
```

### 4. Run the app

```bash
mvn spring-boot:run
```

### 5. Open the app
http://localhost:8080

## Pages

| Page | Access | Description |
|---|---|---|
| `/` | Public | Shop front — browse and order products |
| `/orders.html` | Public | Look up orders by email |
| `/login.html` | Public | Admin login |
| `/admin.html` | Admin only | Manage products and orders |

## API Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/products` | Public | List all products |
| GET | `/api/products/{id}` | Public | Get product by ID |
| POST | `/api/products` | Admin | Create product |
| PUT | `/api/products/{id}` | Admin | Update product |
| DELETE | `/api/products/{id}` | Admin | Delete product |
| GET | `/api/orders` | Admin | List all orders |
| GET | `/api/orders/customer?email=x` | Public | Orders by email |
| POST | `/api/orders` | Public | Place an order |
| PATCH | `/api/orders/{id}/status` | Admin | Update order status |
| DELETE | `/api/orders/{id}` | Admin | Delete order |
| POST | `/api/auth/login` | Public | Login, returns JWT token |

