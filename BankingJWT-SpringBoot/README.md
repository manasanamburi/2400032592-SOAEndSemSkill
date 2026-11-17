# Banking API - Token Based Authentication (Spring Boot + JWT)

## Requirements
Java 17+ and Maven 3.6+.

## Import and run
1. Extract the ZIP.
2. Eclipse: File > Import > Maven > Existing Maven Projects > select this folder > Finish.
   IntelliJ: File > Open > select this folder.
   VS Code: File > Open Folder > select this folder (Java Extension Pack recommended).
3. Wait for Maven dependencies to download.
4. Run `BankingApplication.java`, or open a terminal in the project folder and run:
   `mvn spring-boot:run`
5. Server: http://localhost:8080

## Demo login
Username: `manasa`
Password: `Bank@12345`

## Test in Postman
### A. Login and get JWT
POST http://localhost:8080/api/auth/login
Body > raw > JSON:
{
  "username": "manasa",
  "password": "Bank@12345"
}
Copy `accessToken` from the response.

### B. Check balance
GET http://localhost:8080/api/banking/balance
Authorization tab > Bearer Token > paste accessToken.
Expected response includes accountHolder `manasa`, currency `INR`, balance `50000.0`.

### C. View transactions
GET http://localhost:8080/api/banking/transactions
Authorization > Bearer Token > paste the same token.

### D. Test protection
Call GET http://localhost:8080/api/banking/balance without a token.
Expected HTTP status: 401 Unauthorized.
Wrong login password also returns 401.

## Important
This is a local educational demo with one in-memory user and a demo signing key. It is not
production banking software. Real deployments need secure secret storage, HTTPS, database-backed
users and accounts, account ownership checks, audit logs, rate limiting, token revocation/refresh,
and additional transaction verification.
