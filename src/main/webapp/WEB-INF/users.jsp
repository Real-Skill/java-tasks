<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Users of mood</title>
</head>
<body>
<ul>
    <c:forEach items="${users}" var="user">
        <li><a href="user/${user}" class="user">${user}</a></li>
    </c:forEach>
</ul>
</body>
</html>
