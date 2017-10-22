# Simple in-memory servlet chat

## Summary

Create simple in-memory chatroom using JSP and Servlets.

## Goal

App should compose of 3 pages:

* chatroom list
* chatroom details
* nickname picker

User must pick unique username before they are allowed to chatroom list or chatroom details.

### Page /
Automatically redirects to `/chat`.

### Page /chat

Shows list of all chatrooms as links to chatroom details. Links should have `chatroom` css class.
When you click on chatroom name you should be redirected to that chatroom page (`/chat/:chatroomName`).
 
This page also shows list of all active users (sessions). User is considered active if a session exists that holds
nickname in session attribute.

Shows form to create new chatroom (`POST` to `/chat`).
Form must have name newChatroomForm. Input with new chatroom name must have name attribute set to `name`.
If chat already exists, a warning and same form should be displayed ("Chatroom name already taken").
If chat does not exist it should be created and user should be redirected to that chatroom (`/chat/:chatroomName`).

If there are any errors on the page the error text must be selectable by `error` class.

### Page `/chat/:chatroomName`, i.e. `/chat/lobby`

Shows name of chatroom (tag must be selectable by id `chatroomName`).
Shows link to chatrooms, chatroom name and list of messages:

    ${nickname} ${date}
    ${messageText}
    ${nickname} ${date}
    ${messageText}

Each message must be wrapped with div with css class `message`. Author must be selectable by `author` css class
 and messageText must be selectable by `messageText` css class.

Shows for to add new message (`POST` to `/chat/:chatroomName`).
Form must have name chatroomForm. Input with new message must have name attribute set to `message`.

### Page with nickname picker

Shows form with input with name `nickname`.
If user nickname is not unique show error "Nickname already taken".
If there are any errors on the page the error text must be selectable by `error` class.


**You are allowed to modify only existing files inside src/main!**

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
