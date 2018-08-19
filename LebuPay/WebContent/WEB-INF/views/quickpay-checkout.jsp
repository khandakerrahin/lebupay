<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page import="java.util.*"%>
<%@ page import="com.lebupay.model.*"%>
<%@ page import="com.lebupay.common.MessageUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
MessageUtil messageUtil = new MessageUtil();
PaymentModel paymentModel = (PaymentModel) request.getAttribute("paymentModel");
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
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
	<script type="text/javascript">
$(window).on('keydown',function(event)
	    {
	    if(event.keyCode==123)
	    {
	       
	        return false;
	    }
	    else if(event.ctrlKey && event.shiftKey && event.keyCode==73)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	    else if(event.ctrlKey && event.keyCode==73)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	    else if(event.ctrlKey && event.keyCode==85)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	});
	
	$(document).on("contextmenu",function(e)
	{
	   e.preventDefault();
	});
	
	
	
	function disableCtrlKeyCombination(e)
	{
	//list all CTRL + key combinations you want to disable
	var forbiddenKeys = new Array('a', 'n',  'x', 'j' , 'w','M');
	var key;
	var isCtrl;
	if(window.event)
	{
	key = window.event.keyCode;     //IE
	if(window.event.ctrlKey)
	isCtrl = true;
	else
	isCtrl = false;
	}
	else
	{
	key = e.which;     //firefox
	if(e.ctrlKey)
	isCtrl = true;
	else
	isCtrl = false;
	}
	//if ctrl is pressed check if other key is in forbidenKeys array
	if(isCtrl)
	{
	for(i=0; i<forbiddenKeys.length; i++)
	{
	//case-insensitive comparation
	if(forbiddenKeys[i].toLowerCase() == String.fromCharCode(key).toLowerCase())
	{
	//alert('Key combination CTRL + '+String.fromCharCode(key) +' has been disabled.');
	return false;
	}
	}
	}
	return true;
	}
	
	
	
</script> 
<script type="text/javascript">
if( self == top ) {
document.documentElement.style.display = 'block' ; 
} 
</script>
</head>
<body class="hold-transition skin-blue sidebar-mini noSidebar">
	<div class="wrapper">

		<header class="main-header newHeader chkt_hdr"> <a href="./index"
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu"><h1><c:choose>
    			<c:when test="${checkoutModel.bannerName == null }">
    				<img src="<%=basePath%>resources/insta-buy/lebupay_logo.png" class="logo_quickpay_checkout" width="200" height="56" alt=""/>
    			</c:when>
    			<c:otherwise>
    				<img src="<%=basePath%>resources/banner/${checkoutModel.bannerName}" class="logo_quickpay_checkout" width="200" height="56" alt=""/>
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
			<form action="checkout-payment" id="newForm" method="post">
			<input type="hidden" name ="SESSIONKEY" value="${SESSIONKEY}">
			<input type="hidden" name ="customerDetails" value="" id="customerDetails">
			<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
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
									type="text" <% if(parameterModel.getMandatory().equals("ACTIVE")){ %> required <%} %>  <% if(parameterModel.getParameterType().equals("INACTIVE")){ %> readonly <%} %>  name="<%=parameterModel.getParameterName() %>" class="form-control customInput"
									value="<%if(parameterModel.getParameterName().equals(messageUtil.getBundle("merchant.checkout.parameter1"))) {
												String firstName = "";
												String lastName = "";
												String name = "";
										 		if(Objects.nonNull(paymentModel.getFirstName()))
										 			firstName = paymentModel.getFirstName();
										 		if(Objects.nonNull(paymentModel.getLastName()))
										 			lastName = paymentModel.getLastName();
										 		
										 		name = firstName +" "+ lastName;
										out.println(name.trim());
										
									} else if(parameterModel.getParameterName().equals(messageUtil.getBundle("merchant.checkout.parameter2")))
										if(Objects.nonNull(paymentModel.getEmailId()))
										out.println(paymentModel.getEmailId());
										else
											out.println();	
									else if(parameterModel.getParameterName().equals(messageUtil.getBundle("merchant.checkout.parameter4")))
										if(Objects.nonNull(paymentModel.getMobileNumber()))
											out.println(paymentModel.getMobileNumber());
											else
												out.println();	
									else if(parameterModel.getParameterName().equals(messageUtil.getBundle("merchant.checkout.parameter5")))
										out.println(paymentModel.getAmount());
									else
										out.println();%>" placeholder="Type your <%=parameterModel.getParameterName() %>" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
				<%
			    		
			    		}
			    	}
				    	%>
				<%-- <div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
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
				</div> --%>
				
				
				<div class="col-md-12 col-sm-12 col-xs-12 pDesignWrap email_buttons">
					<div class="layoutSecond checkoutUp">
						<div class="input-group" style="background:none;">
							<div class="customBox">
								<input type="submit" class="btn btn-default round save" value="Proceed">                                  
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
	<div class="col-md-3 col-sm-3 col-xs-12 pull-right hidden-xs  lp-social-icon">
		<ul>			
			<li><a class="lp-fb lp-transition" href="https://web.facebook.com/lebupay/"><i
				class="fa fa-facebook" aria-hidden="true" target="_blank"></i></a></li>
		<li><a class="lp-tw lp-transition" href="https://twitter.com/lebupay"><i
				class="fa fa-twitter" aria-hidden="true" target="_blank"></i></a></li>
		<li><a class="lp-in lp-transition" href="https://www.linkedin.com/company/lebupay"><i
				class="fa fa-linkedin" aria-hidden="true" target="_blank"></i></a></li>
		<li><a class="lp-in lp-transition" href="https://www.instagram.com/lebupay/"><i
				class="fa fa-instagram" aria-hidden="true" target="_blank"></i></a></li>
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
	 
	  $('form').submit(function(event){
		  
		  var details = {};
		  $.each($('#newForm').find('input[type="text"]').serializeArray(), function(i, field) {
			  
			  var fieldName = field.name;
			  if(fieldName == 'Email')
				  fieldName = 'email';
			  else if(fieldName == 'Mobile')
				  fieldName = 'mobileNumber';
			  else if(fieldName == 'Name')
				  fieldName = 'name';
			  details[fieldName] = field.value;
		   
		  });
		  //alert(JSON.stringify(details));
		  $("#customerDetails").val(JSON.stringify(details));
	 });
});
</script>
<!-- <script>
    $(document).ready(function() {
        function disableBack() { window.history.forward() }
		alert("D")
        window.onload = disableBack();
        window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
    });
</script> -->