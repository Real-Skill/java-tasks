<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User's mood</title>
</head>
<body>
<div class="mood">${null == mood ? 'unknown': mood}</div>
<%if (null != request.getAttribute("owner")) {%>
<form method="post">
    <input type="hidden" name="mood"/>
    <button id="makeHappy" onclick="form.mood.value=':)'">:)</button>
    <button id="makeSad" onclick="form.mood.value=':('">:(</button>
</form>
<%} else if (null == request.getUserPrincipal()) {%>
<a href="${pageContext.request.contextPath}/login" class="login">Login</a>
<%}
    if (null != request.getUserPrincipal()) {%>
<a href="${pageContext.request.contextPath}/logout" class="logout">Logout</a>
<%}%>
</body>
</html>
