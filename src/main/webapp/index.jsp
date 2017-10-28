<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mood</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/login" class="login">Login</a>
<% if (null != request.getUserPrincipal()) {%>
<script>location = '${pageContext.request.contextPath}/user/${pageContext.request.userPrincipal.name}';</script>
<%}%>
</body>
</html>
