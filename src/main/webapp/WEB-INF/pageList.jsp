<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CMS</title>
</head>
<body>
<h1>Pages</h1>
<table>

</table>
<h1>Drafts</h1>
<table>

</table>

<script>
    function onSubmit(form) {
        function toPermalink(str) {
            return str.trim().toLowerCase().replace(/ /g, '-');
        }

        var path = '/' === form.path.value.charAt(0) ? toPermalink(form.path.value) : '/' +
        toPermalink(form.path.value);
        form.action = '${pageContext.request.contextPath}/cms' + path;
    }
</script>
<h1>Create new page</h1>
<form name="newPageForm" method="GET">
    <input id="submit" type="submit" value="Create" onclick="onSubmit(form)"/>
</form>

</body>
</html>
