
Author: Chukwuebuka Nwaturuba
#
email: nwaturubachukwuebuka@gmail.com



# Auth Security Assessment

This is a Spring Boot multi-module project that implements JWT-based authentication and authorization. 
The idea was to keep all the security plumbing (JWT handling, error responses, logging) out of the
main application and inside a reusable starter library that any project can pull in as a dependency.

## Modules

**core-security-starter** – this is the library. It handles JWT creation and validation, 
plugs in the bearer-token filter, returns consistent 401/403 error responses, 
and logs every request with the authenticated user. 
None of this code needs to live in the app that uses it.

**sample-application** – a working Spring Boot app that pulls in the starter and demonstrates three endpoint tiers: public, authenticated, and admin-only.

## Project structure

The starter lives under `core-security-starter/src/main/java/com/example/security/`. The main packages are `autoconfigure`, `config`, `jwt`, `filter`, and `exception` — each one pretty self-explanatory by name.

The sample app is under `sample-application/src/main/java/com/example/app/` and follows a standard layered structure: controllers, services, a JPA model and repository, DTOs, and a `SecurityConfig` that wires everything together.

## Some notes on decisions made

The starter registers itself automatically via Spring Boot's auto-configuration mechanism — you just add it to your `pom.xml` and it wires everything up. The `JwtAuthenticationFilter` bean only gets created if the consuming app provides a `UserDetailsService`, which keeps the starter from making assumptions about how users are stored.

The sample app uses H2 in-memory so there's nothing to set up externally. If you wanted to swap in Postgres or MySQL you'd just change the datasource config.

For access control I used both URL-level rules in `SecurityConfig` and `@PreAuthorize` on the admin controller — a bit redundant but it shows both approaches working together.

Passwords are hashed with BCrypt. Sessions are stateless — no server-side session storage, everything lives in the signed token.

## How to run

You need Java 17 and Maven 3.8+.

Build everything from the root:

```bash
cd auth-security-assessment
mvn clean install
```

Then start the sample app:

```bash
cd sample-application
mvn spring-boot:run
```

It runs on port 8080. Two users are created on startup — `user / user123` (ROLE_USER) and `admin / admin123` (ROLE_USER + ROLE_ADMIN).

## Trying it out

Health check, no token needed:
```bash
curl http://localhost:8080/api/public/health
```

Login and get a token:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}'
```

Use the token to hit the authenticated endpoint:
```bash
curl http://localhost:8080/api/user/me \
  -H "Authorization: Bearer <your-token-here>"
```

Admin endpoint — works with the admin token, returns 403 with the user token:
```bash
curl http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <admin-token-here>"
```

## Running the tests

```bash
mvn test
```

There are 10 integration tests covering the full auth flow — login success/failure, public access, authenticated access, tampered tokens, and the 401/403 scenarios.

## JWT configuration

The secret and expiry can be overridden in `application.properties`:

```properties
security.jwt.secret=<base64-encoded-key>   # must decode to at least 32 bytes
security.jwt.expiry-ms=86400000            # 24 hours by default
```

