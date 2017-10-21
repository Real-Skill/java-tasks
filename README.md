# Session listener

## Summary

We need to show list of all active users. User is considered active if there is an active session for that user.
On the same page there is a form to change nickname. If user submits that form then session attribute "nickname"
is set with sent nickname.
Another form is used to log out by finishing user session.

## Goal

Your task is to manage map of active users sessions and nicknames. That map should be stored in ServletContext's
attribute "activeUsers".

When a new user enters the site then a new session is created. Page should show that user's session id in user list.
When that user changes nickname then user list should show that nickname instead session id.
When user logs out then nickname should not be displayed anymore in user list. Instead of that the list should contain
new session.

**You are not allowed to modify ActiveUsers class nor index.jsp!**

## Commands

To run the app:

    mvn tomcat7:run

App will be now available at http://localhost:9090/app/

To run tests with chrome:

    mvn test -Dbrowser=chrome
    
To run tests with phantomjs:

    mvn test
    
To run tests and static analysis:
 
    mvn verify
