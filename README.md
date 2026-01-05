# wallet-management-api
A fintech-style Wallet Management Backend built with Spring Boot, PostgreSQL, JWT authentication, and role-based authorization.
# Wallet Management API (Spring Boot)

A fintech-style backend system for managing digital wallets with secure authentication, role-based access, and transaction-safe credit/debit operations.

This project is designed following real-world fintech backend principles such as transactional integrity, audit logging, and clean architecture.

---

## Features

- JWT-based authentication
- Role-based authorization (USER, ADMIN)
- Wallet creation and balance management
- Secure credit and debit operations
- Transaction history tracking
- PostgreSQL database integration
- Global exception handling
- API documentation with Swagger

---

## System Design Overview

The system follows a layered architecture:

- **Controller Layer** – Handles API requests
- **Service Layer** – Business logic and transaction management
- **Repository Layer** – Database access using Spring Data JPA
- **Security Layer** – JWT authentication and authorization
- **Domain Layer** – Core entities such as User, Wallet, and Transaction

---

## Project Structure
com.yourcompany.wallet
│
├── config
│ └── security
├── controller
├── service
├── service.impl
├── repository
├── entity
│ ├── base
│ └── enums
├── dto
│ ├── request
│ └── response
├── exception
└── util

## 🗄️ Database Design

### Core Tables
- **users** – stores user credentials and status
- **roles** – defines system roles
- **user_roles** – maps users to roles
- **wallets** – maintains wallet balance and status
- **transactions** – records all credit/debit operations

### Fintech Principles Applied
- Monetary values handled using `BigDecimal`
- Transactions are append-only
- Wallet balance updates are transactional
- Audit fields for tracking changes

## 🔐 Security

- JWT tokens are used for stateless authentication
- Role-based authorization using Spring Security
- Secure password storage using encryption
- Protected APIs based on user roles

## 🧪 Error Handling

- Centralized global exception handling
- Meaningful HTTP status codes
- Validation errors handled gracefully

## 🛠️ Tech Stack

- **Java**
- **Spring Boot**
- **Spring Security (JWT)**
- **Spring Data JPA**
- **PostgreSQL**
- **Hibernate**
- **Swagger / OpenAPI**
- **Maven**
