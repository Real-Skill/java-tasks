<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chatroom: ${chatroomName}</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/chat">Chatrooms</a>
<h1 id="chatroomName">${chatroomName}</h1>

<form name="chatroomForm" action="" method="POST"></form>
</body>
</html>
