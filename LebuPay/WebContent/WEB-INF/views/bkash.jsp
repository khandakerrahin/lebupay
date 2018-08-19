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
<style>
	.responsive {
		display: block;
	    margin-left: auto;
	    margin-right: auto;
	    width: 18%;
	    min-width: 150px;
	    max-width: 200px;
	    height: auto;
	    padding-bottom: 25px;
	}

	#bkashPaymentFlowImg {
	    border-radius: 5px;
	    cursor: pointer;
	    transition: 0.3s;
	}
	
	#bkashPaymentFlowImg:hover {opacity: 0.7;}
	
	/* The Modal (background) */
	.modal {
	    display: none; /* Hidden by default */
	    position: fixed; /* Stay in place */
	    z-index: 1; /* Sit on top */
	    padding-top: 100px; /* Location of the box */
	    left: 0;
	    top: 0;
	    width: 100%; /* Full width */
	    height: 100%; /* Full height */
	    overflow: auto; /* Enable scroll if needed */
	    background-color: rgb(0,0,0); /* Fallback color */
	    background-color: rgba(0,0,0,0.9); /* Black w/ opacity */
	}
	
	/* Modal Content (image) */
	.modal-content {
	    margin: auto;
	    display: block;
	    width: 80%;
	    max-width: 700px;
	}
	
	/* Caption of Modal Image */
	#caption {
	    margin: auto;
	    display: block;
	    width: 80%;
	    max-width: 700px;
	    text-align: center;
	    color: #ccc;
	    padding: 10px 0;
	    height: 150px;
	}
	
	/* Add Animation */
	.modal-content, #caption {    
	    -webkit-animation-name: zoom;
	    -webkit-animation-duration: 0.6s;
	    animation-name: zoom;
	    animation-duration: 0.6s;
	}
	
	@-webkit-keyframes zoom {
	    from {-webkit-transform:scale(0)} 
	    to {-webkit-transform:scale(1)}
	}
	
	@keyframes zoom {
	    from {transform:scale(0)} 
	    to {transform:scale(1)}
	}
	
	/* The Close Button */
	.close {
		position: absolute;
    top: 15px;
    right: 35px;
    color: #f1f1f1;
    font-size: 40px;
    font-weight: bold;
    transition: 0.3s;
	}
	
	.close:hover,
	.close:focus {
	    color: #bbb;
	    text-decoration: none;
	    cursor: pointer;
	}
	
	/* 100% Image Width on Smaller Screens */
	@media only screen and (max-width: 700px){
	    .modal-content {
	        width: 100%;
	    }
	}
</style>
<title>bkash</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet"
	href="resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="resources/css/font-awesome.css">
<!-- tab cum accordian css -->
<!-- <link rel="stylesheet" href="css/easy-responsive-tabs.css">
  <link href="http://www.jqueryscript.net/css/jquerysctipttop.css" rel="stylesheet" type="text/css"> -->
<!-- color picker css -->
<link href="resources/css/color-picker.min.css"
	rel="stylesheet">
<!-- Theme style -->
<link rel="stylesheet" href="resources/css/AdminLTE.css">
<link rel="stylesheet" href="resources/css/custom.css">
<link rel="stylesheet" href="resources/css/responsive.css">
<link
	href="resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon"
	href="resources/images/favicon.ico">
<!-- jQuery 2.2.3 -->
<script src="resources/js/jquery-2.2.3.min.js"></script>
<script src="<%=basePath%>resources/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
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
<body class="hold-transition skin-blue sidebar-mini noSidebar merchant-link-page">
<script type="text/javascript" src="https://cdn.ywxi.net/js/1.js" async></script>
<div class="wrapper">
  <header class="main-header newHeader"> <a href="./dashboard"
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu">
    <h1><img src="resources/insta-buy/lebupay_logo.png" alt="logo"></h1>
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
                      <h2>bKash</h2>
                    </div>
                  </div>
                  <div class="row">
                  	<div class="col-md-12 col-sm-12 col-xs-12">
                  	<p class="bkash-p" id="bkashPaymentFlowImg">Please make a payment/transfer of BDT <strong>${amount }</strong> to the bKash wallet <strong>${wallet }</strong> and enter the bKash transaction reference ID below.<a href="#" onclick="return false;" > (Show Me How)</a></p>
                  	<form action="bkash" method="post" style="padding-top: 25px;">
	                  	<input type="hidden" name ="transactionId1" value="${transactionId }" readonly>
	                  	<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" required/>
	                  	<!-- <div class="form-group eachFld resp_bkash">
							<label for="registered" style="float: none;">Wallet</label> <input type="text" name="wallet" class="registed" value="" readonly required>
						</div>
						<div class="form-group eachFld resp_bkash">
							<label for="registered" style="float: none;">Reference</label> <input type="text" name ="reference" value="" readonly class="registed" required>
						</div>
						<div class="form-group eachFld resp_bkash">
							<label for="registered" style="float: none;">Counter</label> <input type="text" name ="counter" value="" readonly class="registed" required>
						</div> -->
						<div>
							<img src="<%=basePath%>resources/images/bkashQR.png" alt="bkash QR Code" class="responsive" width="200" height="200">
							<!-- The Modal -->
							<div id="myModal" class="modal">
							  <span class="close">&times;</span>
							  <img class="modal-content" id="img01">
							  <div id="caption"></div>
							</div>
							
						</div>
														
							<script>
							// Get the modal
							var modal = document.getElementById('myModal');
							
							// Get the image and insert it inside the modal - use its "alt" text as a caption
							var img = document.getElementById('bkashPaymentFlowImg');
							var modalImg = document.getElementById("img01");
							var captionText = document.getElementById("caption");
							img.onclick = function(){
							    modal.style.display = "block";
							    modalImg.src = "<%=basePath%>resources/images/bkashPaymentFlow.png";
							    captionText.innerHTML = "bKash Payment Flow";
							}
							window.onclick = function(event) {
							    if (event.target == modal) {
							        modal.style.display = "none";
							    }
							}
							
							// Get the <span> element that closes the modal
							var span = document.getElementsByClassName("close")[0];
							
							// When the user clicks on <span> (x), close the modal
							span.onclick = function() { 
							    modal.style.display = "none";
							}
							</script>
							
						<div class="form-group eachFld resp_bkash">
							<input type="hidden" name="wallet" value="${wallet }" >
							<input type="hidden" name ="reference" value="${reference }" >
							<input type="hidden" name ="counter" value="${counter }" >
							<label for="registered" class="bkash_transaction">Transaction ID (TrxID)</label> <input type="text" name="trx_id" class="registed bkash_transaction" required >
						</div>
						<div class="form-group eachFld" >
							<button class="btn btn-primary verify_btn_bkash" style="background-image : url(<%=basePath%>resources/images/green.jpg);" type="submit">Verify For ${amount } BDT </button>
						</div>
                  	</form>
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
	<!--<div class="payment_page_footer">
		<div class="row">
			<div class="col-md-12">
				<p>Payments powered by:</p>
				<ul>
					<li><img src="<%=basePath%>resources/images/diner-club-protect.jpg" alt="diners-club-protect"></li>
					<li><img src="<%=basePath%>resources/images/mastercard-secure-code.jpg" alt="mastercard secured code"></li>
					<li><img src="<%=basePath%>resources/images/verified-by-visa.jpg" alt="verified by visa"></li>
					<li><img src="<%=basePath%>resources/images/american-express-safekey.jpg" alt="american express safekey"></li>
					
				</ul>
			</div>
		</div>
	</div>-->
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
	String message2 = (String) request.getAttribute("failure");
	if (message2 != null) {
%>
<script>
 $(document).ready(function() {
 jQuery.growl.error({
		message : "<%=message2%>"
					});
				});
</script>
<% } %>					