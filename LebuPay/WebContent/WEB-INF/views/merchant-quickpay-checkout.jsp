<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page import="java.util.*"%>
<%@ page import="com.lebupay.model.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<title>Merchant Administration | Dashboard</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="stylesheet"
	href="<%=basePath%>resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/font-awesome.css">
<!-- Theme style -->
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>resources/css/AdminLTE.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<link
	href="<%=basePath%>resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">
<!-- Date Picker -->
<link rel="stylesheet" href="<%=basePath%>resources/css/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/daterangepicker.css">
<!-- <style type="text/css">
.sandBox {
    background:${checkoutModel.backgroundColour}; !important;
 }
</style> -->
</head>
<body class="hold-transition skin-blue sidebar-mini noSidebar">
	<div class="wrapper">

		<header class="main-header newHeader"> <a href="./dashboard"
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu"><h1><c:choose>
    			<c:when test="${checkoutModel.bannerName == null }">
    				<img src="<%=basePath%>resources/insta-buy/lebupay_logo.png" class="logo" width="215" height="153" alt=""/>
    			</c:when>
    			<c:otherwise>
    				<img src="<%=basePath%>resources/banner/${checkoutModel.bannerName}" class="logo" width="215" height="153" alt=""/>
    			</c:otherwise>
    		</c:choose></h1></span>
		</a> <nav class="navbar navbar-static-top">

		<div class="col-md-12">
			
			

		</div>

		</nav> </header>

		<!-- Left side column. contains the logo and sidebar -->
		

		<!-- Content Wrapper. Contains page content -->

		<div class="content-wrapper">
			<section class="content">
			<div class="row sandBoxTab">
				<div class="col-xs-12">
					
					<div class="resp-tabs-container">
						<div class="sandBox">

<div class="sandBox" style="background: ${checkoutModel.backgroundColour};">
	
	<div class="sandBoxWrapper checkoutPage" style="background: ${checkoutModel.backgroundColour};">
		<div class="row brdr-btm">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<h2>My Checkout Page Design</h2>
			</div>
		</div>
		<div class="row">
			<form action="#" id="newForm" method="post">
				<%
			    	List<ParameterModel> parameterModels = (List<ParameterModel>) request.getAttribute("parameterModels");
			    	if(parameterModels.size() > 0){
			    		for(ParameterModel parameterModel:parameterModels){
			    		
			    	%>
				<div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
                        	<div class="inputBoxSec">
                         <label for=""><%=parameterModel.getParameterName() %><% if(parameterModel.getMandatory().equals("ACTIVE")){ %> * <%} %></label>
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath%>resources/images/banner-icon.png" /></span> 
                                    <input
									type="text" <% if(parameterModel.getMandatory().equals("ACTIVE")){ %> required <%} %> name="<%=parameterModel.getParameterName() %>" class="form-control customInput"
									value="" placeholder="Type your <%=parameterModel.getParameterName() %>" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
				<%
			    		
			    		}
			    	}
				    	%>
				<div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
                        	<div class="inputBoxSec">
                         <label for="">Card Nmber *</label>
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath%>resources/images/banner-icon.png" /></span> 
                                    <input required  
									type="text" name="cardNumber" placeholder="Type your Card Number" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
                <div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
                        	<div class="inputBoxSec">
                         <label for="">CVV *</label>
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath%>resources/images/banner-icon.png" /></span> 
                                    <input required  
									type="text"  name="cvv" placeholder="Type your CVV" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
                <div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
                        	<div class="inputBoxSec">
                         <label for="">Expire Date *</label>
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath%>resources/images/banner-icon.png" /></span> 
                                    <input required="required"
									 id="datepicker" readonly="readonly" data-name="Expiry Date"
									placeholder="MM/YYYY" data-date-format="mm/yyyy" type="text" class="form-control customInput" value="" ><input type="hidden"
																	name="expiryMonth" value="" /><input type="hidden"
																	name="expiryYear" value="" />
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
				
				<div class="col-md-12 col-sm-12 col-xs-12 pDesignWrap email_buttons">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
							<div class="customBox">
								<button type="submit" class="btn btn-default round save">SAVE</button>                                    
							</div>
							
						</div>

					</div>
				</div>
			</form>
		</div>
	</div>
	
</div>





</div>
</div>
</div>
</div>
</section>
</div>

<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
<div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->








<footer class="main-footer">
	<div
		class="col-md-9 col-sm-9 col-xs-12 pull-left hidden-xs footer-menu">
		<ul>
			<li><a href="javascript:void(0)">Developer Guide</a></li>
			<li><a href="javascript:void(0)">Privacy Policy </a></li>
			<li><a href="javascript:void(0)">Wallet</a></li>
			<li><a href="faq">FAQ</a></li>
			<li><a href="javascript:void(0)">Offers</a></li>
			<li><a href="javascript:void(0)">Grievance Policy</a></li>
			<!-- <li><a data-toggle="modal" data-target="#contactUsModal"
				href="javascript:void(0);">Contact Us</a></li> -->
		</ul>
	</div>
	<div class="col-md-3 col-sm-3 col-xs-12 pull-right hidden-xs social">
		<ul>
			<li class="f1"><a href="javascript:void(0)"><img
					src="<%=basePath%>resources/images/f1.png" /></a></li>
			<li class="f2"><a href="javascript:void(0)"><img
					src="<%=basePath%>resources/images/f2.png" /></a></li>
			<li class="f3"><a href="javascript:void(0)"><img
					src="<%=basePath%>resources/images/f3.png" /></a></li>
		</ul>
	</div>
	<div class="col-md-12">
		<span class="lp-copyright">&copy; 2017 -- All rights reserved</span>
	</div>
</footer>



</body>
</html>
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
    <!-- jQuery UI 1.11.4 -->
<script src="<%=basePath%>resources/js/jquery-ui.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<%=basePath%>resources/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="<%=basePath%>resources/js/app.min.js"></script>
<!-- custom js -->
<script src="<%=basePath%>resources/js/custom_script.js"></script>
<!-- daterangepicker -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
<script src="<%=basePath%>resources/js/daterangepicker.js"></script>
<!-- datepicker -->
<script src="<%=basePath%>resources/js/bootstrap-datepicker.js"></script>
<script src="<%=basePath%>resources/js/ajaxScript.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
<script src="<%=basePath%>resources/js/jquery.creditCardValidator.js"
	type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("#datepicker").change(function(){
		 var date = $(this).val();
		 var dateArr = date.split("/");
		 
		 $('input[name="expiryMonth"]').val(dateArr[0]);
		 $('input[name="expiryYear"]').val(dateArr[1]);
	 });
});
</script>