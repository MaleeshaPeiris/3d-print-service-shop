# 3D Print Shop

A full stack 3D print shop built with Spring Boot, PostgreSQL, and vanilla HTML/JS.

## Tech Stack
- Java 17 + Spring Boot 3
- PostgreSQL
- Plain HTML + CSS + JavaScript

## Setup

1. Create the database:
```sql
   CREATE DATABASE printshop;
```

2. Set environment variables:
```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=yourpassword
```

3. Run the app:
```bash
   mvn spring-boot:run
```

4. Open http://localhost:8080

## Pages
- `/` — Shop front, browse and order products
- `/orders.html` — Look up orders by email
- `/admin.html` — Admin panel, manage products and orders