<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Lebupay</title>
<link rel="stylesheet"
	href="<%=basePath%>resources/css/bootstrap.min.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<!-- <script src="js/jquery-2.1.1.js"></script> -->
<link
	href="https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">
<!-- <script src="js/bootstrap.js"></script> -->
<!-- <script src="js/custom_script.js"></script> -->
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">
</head>

<body class="admin_login">
	<div class="wrapper container">
		<div class="row">
			<div class="col-md-5 col-sm-5 col-xs-12 whiteBox">
				<div class="roundHead"></div>
				<h1>Administrator</h1>
				<form action="./login" method="post">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/ficon1.png" /></span> <input
								type="text" name="userName" class="form-control customInput"
								id="email" placeholder="Email">
						</div>
					</div>
					<div class="form-group">
						<div class="customBox">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/ficon2.png" /></span> <input
								type="password" class="form-control customInput" id="pwd"
								placeholder="Password" name="password">
						</div>
					</div>
					<!-- <div class="checkbox">
            <label><input type="checkbox"> Remember me</label>
          </div> -->
					<button type="submit" class="btn btn-default customBtn">LOG
						IN</button>
				</form>
			</div>
		</div>
	</div>
	<!-- jQuery 2.2.3 -->
	<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="<%=basePath%>resources/js/bootstrap.min.js"></script>
	<!-- custom script -->
	<script src="<%=basePath%>resources/js/custom_script.js"></script>
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
	<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
		type="text/javascript"></script>
	<script src="<%=basePath%>resources/js/login/md5.js"
		type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$('form[action="./login"]').submit(function(){
			$('[type="password"]').each(function(){
				$(this).val(md5($(this).val()));
			});
		});
	});
	</script>
</body>

</html>

<%
						 String message = (String) request.getAttribute("admin.login.failed");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.error({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>

<%
						 message = (String) request.getAttribute("admin.user.name.required");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.error({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>

<%
						 message = (String) request.getAttribute("admin.password.required");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.error({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>

<%
						 message = (String) request.getAttribute("admin.email.not.exist");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.error({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>

<%
					message = (String) request.getAttribute("password.change.success");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.notice({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>