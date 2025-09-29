# SalesSavvy — Backend

Spring Boot backend for an e-commerce application providing user auth, product management, cart, orders, and payment processing.

## Features
- JWT authentication
- User registration and role-based access
- Product and category management
- Cart and order management
- Payment integration
- Admin endpoints for management and reports

## Requirements
- Java 17
- Maven 
- MySQL 8

## Quick start
1. Create .env and .env.prod from .env.example
```bash
cp .env.example .env
cp .env.example .env.prod
```
2. Fill the credentials
3. Docker compose with .env
```bash
docker compose -f compose.dev.yml up --build
```
4. Docker compose with .env.prod
```bash
docker compose up --build
```

Refer frontend repository here: https://github.com/darshan03s/salessavvy

