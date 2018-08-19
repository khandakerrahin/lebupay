<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mail Checking</title>
<style type="text/css">
input[type=text], textarea {
	width: 100%;
	padding: 12px 20px;
	margin: 8px 0;
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

input[type=submit] {
	width: 100%;
	background-color: #4CAF50;
	color: white;
	padding: 14px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input[type=submit]:hover {
	background-color: #45a049;
}

div {
	border-radius: 5px;
	background-color: #f2f2f2;
	padding: 20px;
	align: center;
}
</style>
</head>
<body>
	<div>
		<form action="send-mail" method="post">

			<label for="To">To</label> <input type="text" name="toMail"
				required="required"> <label for="To">From</label> <input
				type="text" name="fromMail" required="required"> <label
				for="To">PORT</label> <input type="text" name="port"
				required="required"> <label for="To">HOST</label> <input
				type="text" name="host" required="required"> <label for="To">Subject</label>
			<input type="text" name="subject" required="required"> <label
				for="To">Message</label>
			<textarea name="messageBody" required="required"></textarea>


			<input type="submit" value="Send">

		</form>
		<br> <br> <br>
		<div align="center">${result}</div>
	</div>
</body>
</html>