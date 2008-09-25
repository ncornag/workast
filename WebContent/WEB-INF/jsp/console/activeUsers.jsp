<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<title>Usuarios activos</title>
</head>
	<body>
		<h3>Usuarios activos</h3>
		<table border="1">
			<tr>
				<th>username</th>
			</tr>
			<c:forEach items="${users}" var="u">
				<tr>
					<td><c:out value="${u.username}" /></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>