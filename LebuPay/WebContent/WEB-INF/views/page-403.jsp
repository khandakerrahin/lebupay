<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="robots" content="noindex">
<meta name="googlebot" content="noindex">
<title>403 Forbidden</title>
<link href="https://fonts.googleapis.com/css?family=Roboto"
	rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">
<style type="text/css">
body {
	background: #f5f8fb; /* Old browsers */
	background: -moz-linear-gradient(top, #f5f8fb 100%, #ffffff 100%);
	/* FF3.6-15 */
	background: -webkit-linear-gradient(top, #f5f8fb 100%, #ffffff 100%);
	/* Chrome10-25,Safari5.1-6 */
	background: linear-gradient(to bottom, #f5f8fb 100%, #ffffff 100%);
	/* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5f8fb',
		endColorstr='#ffffff', GradientType=0); /* IE6-9 */
	font-family: 'Roboto', sans-serif;
}

.mainWraper {
	padding: 0;
	margin: 0;
}

.logoWraper {
	max-width: 960px;
	width: 100%;
	margin: 90px auto 80px;
	text-align: center;
}

.wraper {
	max-width: 960px;
	width: 100%;
	margin: 0 auto;
	text-align: center;
}

.forgotInput {
	border: 1px solid #66ab6f;
	border-radius: 50px;
	font-size: 16px;
	line-height: 40px;
	padding: 5px 15px;
	font-weight: normal;
	width: 90%;
	box-sizing: border-box;
}

.leftSec, .rightSec {
	display: inline-block;
	margin: 0 10px;
	width: 46%;
}

.leftSec label, .rightSec label {
	display: block;
	margin: 0 10px 10px;
	color: #414141;
}

.buttStyle button {
	background: #66ab6f;
	border-radius: 50px;
	border: none;
	padding: 13px 40px;
	color: #FFF;
	font-size: 16px;
	cursor: pointer;
}

.buttStyle button:hover {
	background: #fff;
	border: 1px solid #66ab6f;
	color: #66ab6f;
}

.buttStyle {
	margin-top: 40px;
}

.waterMark {
	background: url(<%=basePath%>resources/images/esclamBg.png) 50% 0
		no-repeat;
	min-height: 400px;
}

.error h1 {
	font-size: 80px;
	line-height: 100px;
	padding: 0;
	margin: 0 0 15px 0;
	color: #666;
}

.error p {
	padding: 0;
	margin: 0 0 15px 0;
	color: #ff0000;
}

.error span {
	padding: 0;
	margin: 0 0 15px 0;
	color: #333;
}

.error a {
	color: #333;
}

.error a:hover {
	text-decoration: none;
}
</style>
</head>
<body>
	<div class="mainWraper">
		<div class="logoWraper">
			<img src="<%=basePath%>resources/images/lebupay_logo.png" width="200"
				height="56" />
		</div>
		<div class="wraper">
			<div class="waterMark error">
				<h1 class="errorHead">403</h1>
				<p>Access denied</p>
				<span>Full authentication is required to access this
					resource.</span> <a href="<%=basePath%>index">Go To Login Page</a>
			</div>
		</div>
	</div>
</body>
</html>
