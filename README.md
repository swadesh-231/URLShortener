# URL Shortener Service

A Spring Boot-based URL shortening service with user authentication, click tracking, and analytics capabilities.

## Features

- **User Authentication**: JWT-based authentication system with registration and login
- **URL Shortening**: Generate short, random 8-character URLs for long links
- **Click Tracking**: Monitor clicks on shortened URLs with timestamp tracking
- **Analytics**: View click statistics and trends over time
- **User Management**: Each user can manage their own collection of shortened URLs
- **Secure API**: Role-based access control with Spring Security

## Tech Stack

- **Backend**: Spring Boot 3.x
- **Security**: Spring Security with JWT authentication
- **Database**: JPA/Hibernate (configurable datasource)
- **Build Tool**: Maven
- **Libraries**:
  - Lombok (boilerplate reduction)
  - ModelMapper (DTO mapping)
  - JJWT (JWT tokens)
  - dotenv-java (environment configuration)

## Architecture

The application follows a layered architecture:

```
├── controller/       # REST API endpoints
├── service/          # Business logic
├── repository/       # Data access layer
├── model/            # JPA entities
├── dto/              # Data transfer objects
├── security/         # Security configuration & JWT handling
└── exception/        # Global exception handling
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL/MySQL or any JPA-compatible database

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd urlshortener
```

### 2. Configure Environment Variables

Create a `.env` file in the project root:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/urlshortener
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

JWTSECRET=your_base64_encoded_secret_key_here
JWTEXPIRATIONMS=86400000
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register a New User
```http
POST /api/auth/public/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

#### Login
```http
POST /api/auth/public/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### URL Management (Requires Authentication)

#### Create Short URL
```http
POST /api/url/shorturl
Authorization: Bearer <token>
Content-Type: application/json

{
  "originalUrl": "https://www.example.com/very/long/url"
}
```

**Response:**
```json
{
  "id": 1,
  "originalUrl": "https://www.example.com/very/long/url",
  "shortUrl": "aB3xY7zQ",
  "clickCount": 0,
  "createdDate": "2025-12-11T10:30:00",
  "username": "john_doe"
}
```

#### Get User's URLs
```http
GET /api/url/myurls
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "originalUrl": "https://www.example.com/very/long/url",
    "shortUrl": "aB3xY7zQ",
    "clickCount": 42,
    "createdDate": "2025-12-11T10:30:00",
    "username": "john_doe"
  }
]
```

### Public Endpoints

#### Redirect Short URL
```http
GET /{shortUrl}
```

This endpoint redirects to the original URL and records a click event.

## Database Schema

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `email`
- `password` (Encrypted)
- `role` (Default: ROLE_USER)

### URL Mapping Table
- `id` (Primary Key)
- `original_url`
- `sort_url` (Short URL code)
- `click_count`
- `created_date`
- `user_id` (Foreign Key)

### Click Table
- `id` (Primary Key)
- `click_date`
- `url_mapping_id` (Foreign Key)

## Security

- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (configurable)
- Role-based access control (ROLE_USER)
- CORS disabled by default (configure as needed)
- All authenticated endpoints require valid JWT token

## Error Handling

The application includes global exception handling for:
- `UserAlreadyExistsException` (409 Conflict)
- `UserNotFoundException` (404 Not Found)
- `UserNotAuthorizedException` (401 Unauthorized)
- Generic exceptions (500 Internal Server Error)

## Future Enhancements

- Custom short URL aliases
- URL expiration dates
- QR code generation
- Geographic click analytics
- Browser and device tracking
- Bulk URL shortening
- API rate limiting
