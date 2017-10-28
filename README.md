# Servlet CMS

## Summary

Create simple in-memory cms using JSP and Servlets.

## Goal

App should compose of 3 pages:

* page list
* page in view mode
* page in edit mode

### Page /
Automatically redirects to `/cms`.

### Page /cms

Shows list of all published pages (not drafts) as links to page details (view mode).
Published pages are shared among all users.
Links should have `page` css class.
When you click on page title (the link) you should be redirected to that page (`/cms/:page`).
 
This page also shows form to start creating new page (`GET` to `/cms/:page?edit&path=...`).
Form input with new page path must have name attribute set to `path`.
Upon submitting the form ser should be redirected to page edit page (`/cms/:page?edit&path=...`).

### Page `/cms/:page`, i.e. `/cms/portfolio`

Shows page title (tag must be selectable by id `title`) and page body (selectable by id `cmsBody`).
Shows link to pages list (id `list`), link to edit current page (id `edit`).

### Page `/cms/:page?edit`

Shows form with inputs with names `path`, `title`, `body`.
If user is editing existing public page or draft, those fields must pre-filled.
Path input should be disabled.

User may either save the page by clicking Save button (id `save`) or save draft.
Drafts are saved only for duration of the session and should not be shared with other users.

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
