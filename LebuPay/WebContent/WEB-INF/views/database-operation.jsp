<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Operation</title>
</head>
<body>
	<style type="text/css">
textarea {
	width: 50em; /* the inital width of the textarea */
	height: 20em; /* the inital height of the textarea */
	max-width: 50em; /* the maximum width of the textarea */
	max-height: 25em; /* the maximum height of the textarea */
	padding: 1em;
	font-family: "Montserrat", "sans-serif";
	font-size: 1em;
	border: 0.1em solid #ccc;
	border-radius: 0.5em;
	background-color: #ffffe6;
}

.button {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 15px 32px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	margin: 4px 2px;
	cursor: pointer;
}
</style>

	<form action="database-operation" method="post">
		<table align="center">
			<tr>
				<td colspan="2"><textarea name="query"
						placeholder="Write Your Query Here......."></textarea></td>
			</tr>
			<tr>
			</tr>
			<tr>
			</tr>

			<tr>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit"
					class="button" value="Submit"></td>
			</tr>
		</table>
	</form>
	<br>
	<div align="center">
		<h1>
			<font color="red"><c:if test="${not empty query}">Your Query is ::: </c:if></font>${query}
		</h1>
	</div>
	<br>
	<br>
	<br>
	<div align="center">${result}</div>
</body>
</html>