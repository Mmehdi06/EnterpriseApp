# EnterpriseApp

## Description

This project is a web application built with Spring Boot, designed to [briefly describe the purpose of your application, e.g., manage user data, provide a REST API, etc.]. The application uses Docker Compose to manage the database and application services.

## Technologies Used

-  **Spring Boot**: For building the web application.
-  **Docker**: To containerize the application and manage dependencies.
-  **Docker Compose**: To define and run multi-container Docker applications.
-  **[Database Technology]**: [e.g., PostgreSQL, MySQL, etc.].

## Getting Started

### Prerequisites

Make sure you have the following installed on your machine:

-  [Docker](https://docs.docker.com/get-docker/)
-  [Docker Compose](https://docs.docker.com/compose/install/)
-  [Java 11 or higher](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (if you want to run the Spring Boot application locally without Docker)

### Clone the Repository

```bash
git clone https://github.com/Mmehdi06/EnterpriseApp.git
cd yEnterpriseApp
```
### Setup Database
```bash
docker compose up -d
```
### Launch spring App
Launch the spring app to access the webapp on port :8080
- [localhost:8000](http://localhost:8080)

### Database Setup
If your application requires a database setup, make sure to:
1. Configure the database connection in application.properties.
2. The database will be automatically seeded with initial data when the application starts. The DatabaseSeeder.java class populates the database with sample items and users automatically.
Seeding Data

### User Credentials
The following credentials are seeded into the database for testing:
- Normal User:
  - Username: `user`
  - Password: `user123`
- Admin User:
  - Username: `admin`
  - Password: `admin123`


