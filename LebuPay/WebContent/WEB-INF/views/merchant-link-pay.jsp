<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<title>Merchant Transaction</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="resources/css/font-awesome.css">
<!-- color picker css -->
<link href="resources/css/color-picker.min.css" rel="stylesheet">
<!-- Theme style -->
<link rel="stylesheet" href="resources/css/AdminLTE.css">
<link rel="stylesheet" href="resources/css/custom.css">
<link rel="stylesheet" href="resources/css/responsive.css">
<link href="resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i" rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon" href="resources/images/favicon.ico">
<!-- jQuery 2.2.3 -->
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
<script src="<%=basePath%>resources/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){ 
    $("#myTab a").click(function(e){
    	e.preventDefault();
    	$(this).tab('show');
    });
});
</script>
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


<!-- <script type="text/javascript">
$(document).ready(function(){ 
    $("#myTab a").click(function(e){
    	e.preventDefault();
    	$(this).tab('show');
    });
});
</script> -->
</head>
<body class="hold-transition skin-blue sidebar-mini noSidebar merchant-link-page">
<script type="text/javascript" src="https://cdn.ywxi.net/js/1.js" async></script>
<div class="wrapper">
  <header class="main-header newHeader"> <a href="./index"
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu">
    <h1><img src="<%=basePath%>resources/insta-buy/lebupay_logo.png" alt="logo"></h1>
    </span> </a>
    <nav class="navbar navbar-static-top">
      <div class="col-md-12">
        
      </div>
    </nav>
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <section class="content">
      <div class="row sandBoxTab">
        <div class="col-xs-12">
          <div class="resp-tabs-container">
            <div class="sandBox">
              <div class="sandBox">
                <div class="sandBoxWrapper checkoutPage">
                  <div class="row brdr-btm">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                      <h2>Payment Method</h2>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6 col-sm-6 col-xs-12">
                    <div class="orderSummery">
                    	<h3>Order Summery</h3>
                        <div class="contSec">
                        	<p><span>Merchant:</span><strong>${merchantName }</strong></p>
                            <p><span>Transaction ID:</span><strong>${transactionId }</strong></p>
                            <p class="price"><span>Total:</span><strong>&#2547; <fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${grossAmount}" /></strong></p>
                            <p class="cancelOrder"><a href="${returnUrl }">Cancel Order</a>and return to LebuPay</p>
                        </div>
                    </div>	
                    </div>
                    
                    <div class="col-md-6 col-sm-6 col-xs-12">
                    <div class="paymentMethod">
                   	  <h3>Select Payment Method</h3>
                        <div class="contSec">
                        
                        <ul class="nav nav-tabs" id="myTab">
                        	<c:if test="${not empty eBLUserName}">
                        		<c:if test="${not empty eBLUserPassword}">
	                        		<c:if test="${not empty eBLUserId}">
	                            		<li class="active" id="secA"><a href="#sectionA">Card</a></li>
	                           		</c:if>
                        		</c:if>
                           </c:if> 
                            <li  id="secB"><a href="#sectionB">bKash</a></li>
                            <c:if test="${not empty citybankMerchantId}">
						    	 <li><a href="#sectionC">Card</a></li>
							</c:if>
                            
   						 </ul>
                         
   						 <div class="tab-content">
   						 <c:if test="${not empty eBLUserName}">
                        		<c:if test="${not empty eBLUserPassword}">
	                        		<c:if test="${not empty eBLUserId}">
			      						<div id="sectionA" class="tab-pane fade in active">
			                             	<div class="eachBox">
				                                <h4>Debit/Credit Card</h4>
				                                <ul>
				                                	<li class="card_logos"><a href="ebl?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/visa.png" alt="visa card"></a></li>
				                                    <li class="card_logos"><a href="ebl?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/mastercard.png" alt="master card"></a></li>
				                                    <li class="card_logos diner"><a href="ebl?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/diners_club.png" alt="diners club"></a></li>
				                                </ul>
			                                </div>
			                            </div>
                            		</c:if>
                        		</c:if>
                           </c:if> 
        					<div id="sectionB" class="tab-pane fade">
                               <div class="eachBox bkashl">
	                                <h4>bKash</h4>
	                                <ul>
	                                	<li class="card_logos"><a href="bkash?transactionId=${transactionId1 }"><img src="resources/images/bkash.png" alt="bkash"></a></li>
	                                </ul>
                                </div>
                            </div>
                          <c:if test="${not empty citybankMerchantId}">
    						 <div id="sectionC"  class="tab-pane fade">
	                               <div class="eachBox">
		                                <h4>Debit/Credit Card</h4>
		                                <ul ><!-- onclick="return clickOnEMI()" -->
		                                	<li class="card_logos"><a href="paycitybank?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/visa.png" alt="visa card"></a></li>
		                                    <li class="card_logos"><a href="paycitybank?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/mastercard.png" alt="master card"></a></li>
		                                    <li class="card_logos diner"><a href="paycitybank?transactionId=${transactionId1 }"><img src="<%=basePath%>resources/images/amex.png" alt="diners club"></a></li>
		                                </ul>
	                             	</div>
	                          </div>
   						 </c:if>
				 	</div>
                </div>
            </div>
         </div>
     </div>
                  
     <div class="payment_page_footer">
		<div class="row">
			<div class="col-md-12">
				<p>Payments powered by:</p>
				<ul>
					<li><img src="<%=basePath%>resources/images/verified-by-visa.png" alt="verified by visa"></li>
					<li><img src="<%=basePath%>resources/images/mastercard-secure-code.png" alt="mastercard secured code"></li>
					<li><img src="<%=basePath%>resources/images/american-express-safekey.png" alt="american express safekey"></li>
					<li><img src="<%=basePath%>resources/images/diner-club-protect.png" alt="diners-club-protect"></li>
										
				</ul>
			</div>
		</div>
	</div>
                  
                </div>         
                              
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
<footer>
<div class="copyright_sec payment_page_footer_btm">
	<div class="row">
			<div class="col-md-6"><p>&copy; All rights reserved to Spider Digital Commerce (Bangladesh) Ltd</p></div>
			<div class="col-md-6 mcafee_secured_logo">
				<script src="https://cdn.ywxi.net/js/inline.js?w=90"></script>
				
			</div>
		</div>
	</div>
</footer>
		<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
  <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->
<%
String message = (String) request.getAttribute("transactionidInvalid");
	if (message != null) { %>
			<script>
		 		$(document).ready(function(){ jQuery.growl.error({ message : "<%=message%>" }); });
			</script>
<% } %>
<script type="text/javascript">
var eBLUserName = '${eBLUserName}';
var eBLUserPassword = '${eBLUserPassword}';
var eBLUserId = '${eBLUserId}';

if(eBLUserName == "" || eBLUserPassword == "" || eBLUserId==""){
	$("#sectionA").removeClass("in active");
	$("#sectionB").addClass("in active");
	
	$("#secA").removeClass("active");
	$("#secB").addClass("active");
	
}
</script>