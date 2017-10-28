# Servlet Authentication

## Summary

Secure resources using role-based approach.

## Goal

Introduce authentication using standard form-based authentication and secure app using admin and user roles.
Users and their roles are stored in H2 SQL database. 

### Table structure

| USERS |
|-------|
|user_name: varchar(255)|
|user_pass: varchar(255)|
|mood: varchar(5)|

| USER_ROLES |
|------------|
|user_name: varchar(255)|
|role_name: varchar(255)|

App ships with pre-configured DataSource `jdbc/authority`. You may use it to configure security realm and lookup
user profile that should be displayed on mood page.

App consists of 5 pages:

* home
* mood
* login
* login error
* Access denied error page
* Resource not found error page
* Users list page

### Home page `/`
If user is authenticated it automatically redirects to `/user/:username`, 
where `nickname` is authenticated user's nickname.
Otherwise it displays link to login page.

### Mood page `/user/:username`

Shows user mood. Anyone can access this page, but only profile owner should see buttons to change mood.
There should be button to choose happy mood and a button to choose sad mood.

Users that have not authenticated yet should see link to login page while authenticated users should see link to logout.
After clicking logout user should be redirected to home page.
 
### Login page `/login`

Shows login form with user name input (`name=j_username`), password input (`name=j_password`) 
and submit button (`type=submit`).

### Login error page

Displayed when user provides invalid credentials.
Shows `Authentication error` header and `Username, password or role incorrect, try again` message.
When user clicks `try again` link they should be redirected to `/login` page.

### Access denied error page

Should be displayed when authenticated user tries to access resources he does not have access to. 

### Resource not found error page

Should be displayed when user tries to access profile that does not exist. 

### Users list page `/users`

Shows list of users.
Only user with admin role is allowed to see this page. 

**You are allowed to modify only existing files inside src/main!**

## Commands

To run the app:

    mvn tomcat7:run

App will be now available at http://localhost:9090/app/

Default credentials :

|Username|Password|
|--------|--------|
|admin   |admin   |
|user    |user    |

To run tests with chrome:

    mvn test -Dbrowser=chrome
    
To run tests with phantomjs:

    mvn test
    
To run tests and static analysis:
 
    mvn verify
