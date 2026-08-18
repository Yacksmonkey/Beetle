# 🪲 Beetle Backend

Backend service for **Beetle**, a full-stack social recommendation platform where users can discover content, manage recommendations, interact with friends, and build a personalized recommendation history.

This repository contains the REST API, authentication system, business logic, persistence layer, social features, and recommendation management for Beetle.

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL**
* **Gradle**
* **JWT**
* **Google OAuth**
* **JUnit**
* **Mockito**
* **Supabase**
* **Render**

## ✨ Features

### 🔐 Authentication

* Email and password registration
* Email and password login
* Google OAuth authentication
* JWT-based authentication
* HTTP-only cookie authentication
* Current-user session validation
* Password recovery flow
* Password hashing with Spring Security

### 👤 User Profiles

Users can manage profile information including:

* Name
* Username
* Email
* Profile picture
* Phone
* Address
* Biography
* Public/private profile visibility

### 🎯 Recommendation System

Beetle supports several content categories:

* 🎬 Movies
* 📺 Series
* 🎵 Music
* 📚 Books

Users can interact with recommendation cards and maintain their own recommendation history.

Recommendation states include:

```text
DRAFT
SAVED
DISMISSED
```

Database constraints prevent duplicate relationships between users and recommendations.

### 👥 Social Features

The backend supports:

* User discovery
* Friend requests
* Accepting friend requests
* Friendship management
* Friends list
* Social activity feed

### ❤️ Likes & Comments

Users can interact with recommendation history entries through:

* Like
* Unlike
* Create comment
* List comments
* Delete comment

Interactions are persisted in PostgreSQL.

## 🏗 Architecture

The backend follows a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

The project separates responsibilities across areas such as:

```text
config/
controller/
dto/
entity/
repository/
security/
service/
```

This keeps API handling, business logic, security, and persistence concerns separated.

## 🔐 Security

Authentication and authorization are handled using **Spring Security** and **JWT**.

The JWT is stored in an **HTTP-only cookie**, preventing frontend JavaScript from directly accessing the authentication token.

The security implementation includes:

* Spring Security filter chain
* Custom JWT authentication filter
* Password hashing
* Google ID token verification
* CORS configuration
* Stateless authentication
* Public and authenticated API routes

## 🗄 Database

The application uses **PostgreSQL** with:

* Spring Data JPA
* Hibernate
* Entity relationships
* Database constraints
* Repository abstractions
* Pagination

Production persistence is hosted using **Supabase PostgreSQL**.

The deployed backend connects to PostgreSQL through the Supabase connection pooler.

## ⚙️ Database Connection Stability

Production uses the Supabase PostgreSQL Transaction Pooler.

An important JDBC configuration used by the deployed application is:

```text
prepareThreshold=0
```

This disables PostgreSQL JDBC server-side prepared statements for the connection.

This configuration improves compatibility with transaction-level connection pooling, where different physical database connections may be used between transactions.

It was an important part of maintaining stable database communication between the deployed Spring Boot backend and Supabase.

## 📡 REST API

The backend exposes REST endpoints for the main application areas, including:

```text
/api/auth
/api/password
/api/preferences
/api/history
/api/friends
/api/comments
```

These APIs are consumed by the separate Beetle frontend application.

## 🧪 Testing

Backend testing uses:

* **JUnit**
* **Mockito**

Tests are used to validate application behavior and isolate dependencies where appropriate.

## 🚀 Deployment

The backend is deployed using **Render**.

Production architecture:

```text
Beetle Frontend
       ↓
    REST API
       ↓
Spring Boot
       ↓
JPA / Hibernate
       ↓
Supabase PostgreSQL
```

The current deployment uses free-tier infrastructure because Beetle is a personal portfolio project.

Free-tier services may enter a sleep state after periods of inactivity, meaning the first request can take longer while the backend starts again.

This is an infrastructure limitation and does not affect normal application behavior once the service is running.

## 🖥 Running Locally

### Requirements

Make sure you have:

* **Java 21**
* **PostgreSQL**
* **Git**

The project includes the **Gradle Wrapper**, so installing Gradle globally is not required.

### Clone the repository

```bash
git clone <repository>
cd <repository>
```

### Environment Configuration

Configure the required database credentials through environment variables.

Example:

```env
DB_URL=jdbc:postgresql://localhost:5432/beetle_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

Additional authentication and external-service credentials must also be configured locally when those features are used.

Never commit real credentials, passwords, tokens, or API secrets to Git.

### Run with Gradle

Linux/macOS:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

## 📦 Build

Create a production build using the Gradle Wrapper.

Linux/macOS:

```bash
./gradlew clean build
```

Windows:

```bash
gradlew.bat clean build
```

The generated JAR will be available under:

```text
build/libs/
```

It can then be executed with:

```bash
java -jar build/libs/<application>.jar
```

## 🔑 Environment Variables

Production configuration and secrets should be managed through environment variables.

Typical variables include:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
PORT
```

Credentials related to authentication, email, storage, or external services must also remain outside the repository.

### ⚠️ Security

Never commit:

* Database passwords
* JWT secrets
* Google OAuth client secrets
* OAuth refresh tokens
* Supabase service role keys
* Production credentials

Use environment variables or the hosting provider's secret management system instead.

## 🔄 Frontend Integration

The backend is designed to work with the separate **Beetle Frontend** application.

Communication follows:

```text
Next.js Frontend
       ↓
      HTTP
       ↓
    REST API
       ↓
Spring Boot Backend
       ↓
   PostgreSQL
```

Keeping frontend and backend in separate repositories allows both applications to be developed and deployed independently.

## 🎯 Project Purpose

Beetle was built as a personal software engineering project to practice and demonstrate:

* Java backend development
* Spring Boot
* REST API design
* Spring Security
* JWT authentication
* Google OAuth
* Relational database design
* PostgreSQL
* JPA / Hibernate
* Social application features
* Automated testing
* Cloud deployment
* Production debugging
* Frontend/backend integration

## 📌 Project Status

**Beetle Backend is feature complete for the current project scope.**

The project focuses on maintaining a stable full-stack architecture and demonstrating real-world backend concepts without introducing unnecessary infrastructure or complexity.

---

Built as part of the **Beetle** project. 🪲
