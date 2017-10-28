<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit: ${page.title}</title>
</head>
<body>
<form method="POST" action="${pageContext.request.contextPath}/cms${path}">
    <div>
        <label for="path">Path</label>
        <input id="path" name="path" placeholder="/some-path" disabled="true" value="${path}"/>
    </div>
    <div>
        <label for="title">Title</label>
        <input id="title" name="title" placeholder="Title" value="${page.title}"/>
    </div>
    <div>
        <label for="body">Body</label>
        <textarea id="body" name="body" placeholder="Title">${page.body}</textarea>
    </div>
    <input type="checkbox" name="draft" value="true" style="display:none"/>
    <button id="submit" type="submit">Submit</button>
    <button id="submitDraft" type="submit" onclick="form.draft.checked=true">Save draft</button>
</form>
</body>
</html>
