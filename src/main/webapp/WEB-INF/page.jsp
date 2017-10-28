<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${page.title}</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/cms" id="list">Pages</a>
<a href="?edit" id="edit">Edit</a>
<h1 id="title">${page.title}</h1>
<div id="cmsBody">${page.body}</div>
</body>
</html>
