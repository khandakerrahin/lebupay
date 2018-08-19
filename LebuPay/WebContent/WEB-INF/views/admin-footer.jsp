<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path11 = request.getContextPath();
	String basePath11 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path11 + "/";
%>
</div>
<!-- /.content-wrapper -->

<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
<div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->
<footer class="main-footer">
	<div
		class="col-md-9 col-sm-9 col-xs-12 pull-left hidden-xs footer-menu">
		<!-- <ul>
      <li><a href="javascript:void(0)">Developer Guide</a></li>
      <li><a href="javascript:void(0)">Privacy Policy </a></li>
      <li><a href="javascript:void(0)">Wallet</a></li>
      <li><a href="javascript:void(0)">FAQ</a></li>
      <li><a href="javascript:void(0)">Offers</a></li>
      <li><a href="javascript:void(0)">Grievance Policy</a></li>
      <li><a href="javascript:void(0)">Contact Us</a></li>
    </ul> -->
	</div>
	<div class="col-md-3 col-sm-3 col-xs-12 pull-right hidden-xs social">
		<%-- <ul>
      <li class="f1"><a href="javascript:void(0)"><img src="<%=basePath11%>resources/images/f1.png" /></a></li>
      <li class="f2"><a href="javascript:void(0)"><img src="<%=basePath11%>resources/images/f2.png" /></a></li>
      <li class="f3"><a href="javascript:void(0)"><img src="<%=basePath11%>resources/images/f3.png" /></a></li>
    </ul> --%>
	</div>
	<div class="col-xs-12">
		<span>All rights reserved to Spider Digital Commerce (Bangladesh) Ltd</span>
	</div>
</footer>
<!-- jQuery 2.2.3 -->
<%-- <script src="<%=basePath11%>resources/js/jquery-2.2.3.min.js"></script> --%>
<!-- jQuery UI 1.11.4 -->
<script src="<%=basePath11%>resources/js/jquery-ui.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<%=basePath11%>resources/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="<%=basePath11%>resources/js/app.min.js"></script>
<!-- daterangepicker -->
<script src="<%=basePath11%>resources/js/moment.min.js"></script>
<script src="<%=basePath11%>resources/js/daterangepicker.js"></script>
<!-- datepicker -->
<script src="<%=basePath11%>resources/js/bootstrap-datepicker.js"></script>
<!-- custom js -->
<script src="<%=basePath11%>resources/js/custom_script.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath11%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath11%>resources/vendors/growl/js/jquery.growl.js" type="text/javascript"></script>
<script src="<%=basePath11%>resources/js/validation/loyaltyValidation.js" type="text/javascript"></script>
</body>
</html>