<%@ page import="io.realskill.task.java.ActiveUsers" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chat</title>
</head>
<body>
<%

    request.setAttribute("activeUsers", ActiveUsers.get(application).values());

%>
<h1>Active users</h1>
<table>
    <c:forEach items="${activeUsers}" var="user">
        <tr>
            <td class="user">${user}</td>
        </tr>
    </c:forEach>
</table>
<hr/>
<%

    if ("POST".equals(request.getMethod())) {
        final String nickname = request.getParameter("nickname");
        if (null != nickname && !nickname.trim().isEmpty()) {
            session.setAttribute("nickname", nickname);
        }
        final String logout = request.getParameter("logout");
        if (null != logout) {
            session.invalidate();
        }
        response.sendRedirect(".");
    }

%>
<form method="POST">
    <input name="nickname" type="text" placeholder="Nickname"/>
    <button id="changeNickname">Change nickname</button>
</form>
<form method="POST">
    <input type="hidden" name="logout"/>
    <button id="logout">Logout</button>
</form>
</body>
</html>
