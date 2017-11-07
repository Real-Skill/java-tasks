# Servlet JWT Filter

## Summary

Implement WebFilter that authenticates user with JSON Web Tokens.

## Goal

There are two endpoints in this API:

* GET /me
* POST /login
* POST /seed

You have to implement LoginServlet and WebTokenFilter that would authenticate user with JWToken and do something
using standard mechanisms so that `request.getUserPrincipal()` invoked by `UserServlet` (serving GET /me)
would result in username of authenticated user.

### GET /me
Returns JSON representation of currently authenticated user i.e.

    { "username": "Jack", "admin": true }
    
Authentication is based on JSON Web Token sent as "Authorization" header i.e.

    Authorization: Bearer abc.def.xyz
    
### POST /login
Accepts form payload with `username` and `password` and checks these credentials are valid against database.
If credentials are valid it returns JSON Web Token. 
Token must be created using:

* algorithm taken from servletContext attribute `jwtAlgorithm`
* issuer: auth0
* subject: `username`

### POST /seed
Used by tests to seed database. Make sure your filter does not restrict access to this resource.

 

### Table structure

| USERS |
|-------|
|username: varchar(255)|
|password: varchar(255)|
|admin: boolean|

App ships with pre-configured DataSource `jdbc/authority`.

**You are allowed to modify only LoginServlet and and WebTokenFilter files inside src/main!**

## Commands

To run the app:

    mvn tomcat7:run

App will be now available at http://localhost:9090/app/

Default credentials :

|Username|Password|
|--------|--------|
|admin   |admin   |
|user    |user    |

To run tests:
 
    mvn clean test
