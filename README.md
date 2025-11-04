# Movie Review Website (Spring Boot + PostgreSQL)

Minimal full-stack app where users can register, login (JWT), browse movies, and post reviews with ratings.

## Tech Stack
- Backend: Spring Boot (Web, Data JPA, Security, JWT)
- DB: PostgreSQL
- Frontend: HTML + CSS (+ minimal JS)

## Quick Start
1. Prereqs: JDK 17, Maven, PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE moviereview;
   ```
3. Configure DB in `src/main/resources/application.properties` (username/password).
4. Build & run:
   ```bash
   mvn spring-boot:run
   ```
5. Open `http://localhost:8080/` to view the frontend, or use the REST API under `/api/*`.

## REST Endpoints (summary)
- Auth: `POST /api/auth/register`, `POST /api/auth/login`
- Users: `GET /api/users/me`
- Movies: `GET /api/movies`, `GET /api/movies/{id}`, `POST/PUT/DELETE /api/movies` (admin)
- Reviews: `GET /api/reviews/movie/{movieId}`, `POST /api/reviews`, `PUT/DELETE /api/reviews/{id}`

Include `Authorization: Bearer <token>` for protected endpoints after login.

## SQL Tables (optional explicit DDL)
See `sql/schema.sql` for DDL if you prefer to create tables manually in pgAdmin4.

## Notes
- You may alternatively generate this project via Spring Initializr (`spring.io/initializr`) with the same dependencies.
- Default JWT expiration is 24h; configure via `app.jwt.*`.


