<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta name="description"
	content="Lebupay is an affordable online payment gateway provider in Bangladesh. We enable small to large buiness with payment gateway solution. You will get the money in your account automatically within two days.">
<meta name="keywords"
	content="online payment, payment gateway bangladesh, payment platform bangladesh, payment gateway, afforadle payment gateway,payment gateway for small business, reliable payment gateway, spider digital commerce">

<script src="<%=basePath%>resources/js/jquery-2.1.1.js"></script>
<script type="text/javascript">
/* $(window).on('keydown',function(event)
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
	}); */
	
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
<style>
html {
	display: none;
}
</style>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Lebupay | Payment Gateway | No Extra Charge</title>

<!-- Bootstrap core CSS -->
<link href="<%=basePath%>resources/css/bootstrap.min.css"
	rel="stylesheet">
<%-- <link rel="stylesheet" href="<%=basePath%>resources/css/bootstrap.css"> --%>
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<!-- Custom CSS -->
<link href="<%=basePath%>resources/css/styles.css" rel="stylesheet">
<!-- Responsive CSS -->
<link href="<%=basePath%>resources/css/media.css" rel="stylesheet">

<link rel="stylesheet"
	href="<%=basePath%>resources/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/style.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/responsive.css">

<!--[if IE]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<script src="<%=basePath%>resources/js/bootstrap.js"></script>
<script src="<%=basePath%>resources/js/custom_script.js"></script>


<script src="<%=basePath%>resources/js/login/md5.js"
	type="text/javascript"></script>
<script src="<%=basePath%>resources/js/ajaxScript.js"></script>
<script src="<%=basePath%>resources/js/landingpage.js"></script>
<script src="<%=basePath%>resources/js/clear_modal.js"></script>

<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">
<script type="text/javascript">
	if (self == top) {
		document.documentElement.style.display = 'block';
	}
</script>
<!-- Added by ajmain 20190425 -->
<style>
.msg {
	font-weight: initial;
	width: 100%;
	border: 1px solid;
	padding: 10px;
	color: grey;
}

.msg-error {
	border-color: #d32f2f;
	background-color: #ef5350;
	color: white;
}

.msg-success {
	border-color: #15600b;
	background-color: #02cf32;
	color: white;
}
</style>
</head>

<body>

	<section class="sec_banner01 bg-image-full sec_banner01_mobile">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<button data-toggle="modal" class="btn-lg merchant-login-btn"
					data-target="#merchant_login_modal_form" onclick="hideFunction()">MERCHANT LOGIN</button>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-6">
				<img class="image_full_width banner_01"
					src="<%=basePath%>resources/images/banner_image01.png" alt="">
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner02 sec_banner02_mobile">
	<div class="container">
		<div class="row">
			<div class="col-lg-6">
				<img class="image_full_width banner_01"
					src="<%=basePath%>resources/images/banner_image02.png" alt="">
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner03">
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center">
				<h2 class="gry_subhead">Features</h2>
				<div class="row text-center">
					<div class="col-lg-4 col-sm-4 col-xs-12">
						<div class="fbox">
							<img
								src="<%=basePath%>resources/images/social_payment_link_icon.png"
								alt="social payment link">
							<h4 class="fea_subhead">Social Payment Link</h4>
							<p class="feature_brief">
								One click payment link for Facebook, <br /> Messenger, SMS, or
								any other social media platform
							</p>
						</div>
					</div>
					<div class="col-lg-4 col-sm-4 col-xs-12">
						<div class="fbox">
							<img src="<%=basePath%>resources/images/payment_form_icon.png"
								alt="All forms of Payment">
							<h4 class="fea_subhead">All Forms of Payment</h4>
							<p class="feature_brief">
								Payment through credit/debit cards,<br /> bKash, Rocket, bank
								accounts, or agent banking
							</p>
						</div>
					</div>
					<div class="col-lg-4 col-sm-4 col-xs-12">
						<div class="fbox">
							<img src="<%=basePath%>resources/images/emi_payment_icon.png"
								alt="EMI payment">
							<h4 class="fea_subhead">EMI Payment</h4>
							<p class="feature_brief">Convenience of paying through EMI</p>
						</div>
					</div>
				</div>
				<div class="row text-center">
					<div
						class="col-lg-4 col-lg-offset-2 col-sm-4 offset-xs-0 col-xs-12">
						<div class="fbox">
							<img
								src="<%=basePath%>resources/images/website_integration_icon.png"
								alt="Web Integration">
							<h4 class="fea_subhead">Web Integration</h4>
							<p class="feature_brief">Easy integration with websites via
								API</p>
						</div>
					</div>
					<div class="col-lg-4 col-sm-4 col-xs-12">
						<div class="fbox">
							<img src="<%=basePath%>resources/images/business_tools_icon.png"
								alt="Business Tools">
							<h4 class="fea_subhead">Business Tools</h4>
							<p class="feature_brief">
								Dashboard and other features for <br />managing all
								transactions
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner04">
	<div class="container">
		<div class="row">
			<div class="col-lg-6">
				<h2 class="pricing_head">
					<span style="font-weight: bold; font-size: 1em;"> For
						Pricing </span> <br /> or other questions
				</h2>
				<div class="cont">
					<form>
						<div class="row">
							<label for="phone" class="col-lg-3 col-xs-4"> Phone- </label> <span
								class="col-lg-5"> +88 01911310261</span>

						</div>
						<div class="row">
							<label for="mail" class="col-lg-3 col-xs-4"> Mail- </label> <span
								class="col-lg-5"> info@lebupay.com</span>

						</div>
						<div class="row">
							<label 0for="messenger" class="col-lg-3 col-xs-4">
								Messenger-</label> <span class="col-lg-5"> m.me/lebupay</span>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner05">
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-6 col-xs-12 ">
				<h3 class="subhead">About us</h3>
				<p class="grn">LebuPay is brought to you by Spider Digital
					Commerce (Bangladesh) Ltd. We help our partners to organize lead
					product and service innovation initiaves in the digital space. We
					use design principles and innovation models to help our partners
					create and capture value in the digital economy.</p>
				<div>
					<a href="http://www.spiderdigital.io/" class="white_rounded_btn">
						Know more </a>
				</div>
			</div>
		</div>
	</div>
	</section>

	<footer>
	<div class="container">
		<div class="row">
			<div class="col-lg-8">
				<p class="copyright_info">&copy; All rights reserved to Spider
					Digital Commerce (Bangladesh) Ltd</p>
			</div>
			<div class="col-lg-4">
				<div class="social">
					<ul>
						<li><a class="lp-fb lp-transition fb"
							href="https://web.facebook.com/lebupay/"><i
								class="fa fa-facebook " aria-hidden="true" target="_blank"></i></a></li>
						<li><a class="lp-tw lp-transition tw"
							href="https://twitter.com/lebupay"><i class="fa fa-twitter"
								aria-hidden="true" target="_blank"></i></a></li>
						<li><a class="lp-in lp-transition li"
							href="https://www.linkedin.com/company/lebupay"><i
								class="fa fa-linkedin" aria-hidden="true" target="_blank"></i></a></li>
						<li><a class="lp-in lp-transition ins"
							href="https://www.instagram.com/lebupay/"><i
								class="fa fa-instagram" aria-hidden="true" target="_blank"></i></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	</footer>

	<%
	String message = (String) request
			.getAttribute("password.change.success");
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

	<!--forget password modal for merchant start-->
	<div class="modal fade lp-modal" id="merchant_forget_password_modal"
		role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog lp-modal-dialog" role="document">
			<div class="modal-content lp-modal-content">
				<div class="modal-header lp-modal-header">
					<button type="button" class="close lp-modal-close"
						data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body lp-modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-sm-7 col-xs-12 lp-modal-form-part">
								<h4 class="lp-modal-title lp-light-font">Forget Password</h4>
								<form action="merchant/forgot-password" class="lp-form">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
									<label> <span class="lp-sprite lp-email"></span> <input
										type="text" field-name="Email Id" placeholder="Email Id"
										data-valied="blank-email" name="emailId">
									</label> <input
										class="lp-form-submit-button lp-button green lp-transition"
										type="submit" value="send">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
								</form>
							</div>
							<div class="col-sm-5 lp-no-display">
								<img src="<%=basePath%>resources/images/form_image.png"
									class="img-responsive" width="329" height="213" alt="">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--forget password modal for merchant end-->

	<!--mobile verification code modal for merchant start-->
	<div class="modal fade lp-modal"
		id="merchant_mobile_verification_modal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog lp-modal-dialog" role="document">
			<div class="modal-content lp-modal-content">
				<div class="modal-header lp-modal-header">
					<button type="button" class="close lp-modal-close"
						data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body lp-modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-sm-7 col-xs-12 lp-modal-form-part">
								<h4 class="lp-modal-title lp-light-font">Mobile
									Verification</h4>
								<form action="merchant/phone-validation" class="lp-form">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
									<label> <span class="lp-sprite lp-phone"></span> <input
										type="text" name="phoneCode" maxlength="6"
										field-name="Verification Code"
										placeholder="Enter Verification Code" data-valied="blank">
									</label> <input
										class="lp-form-submit-button lp-button green lp-transition"
										type="submit" value="submit">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
									<a href="merchant/resend">Resend Code</a>
								</form>
							</div>
							<div class="col-sm-5 lp-no-display">
								<img src="<%=basePath%>resources/images/form_image.png"
									class="img-responsive" width="329" height="213" alt="">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--mobile verification code modal for merchant end-->

	<!-- email & mobile verification code modal for merchant start-->
	<!--<div class="modal fade lp-modal"
		id="merchant_complete_registration" role="dialog"  data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog lp-modal-dialog" role="document">
			<div class="modal-content lp-modal-content">
				<div class="modal-header lp-modal-header">
					<button type="button" class="close lp-modal-close"
						data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body lp-modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-sm-7 col-xs-12 lp-modal-form-part">
								<h4 class="lp-modal-title lp-light-font">Mobile
									Verification</h4>
								<form action="merchant/complete-validation" method="POST" class="lp-form" id="complete-validation-form">
									<input type="hidden" name="csrfPreventionSalt"
										value="" />
									<label> <span class="lp-sprite lp-email"></span> <input
										type="text" name="emailId" id="completeVerificationEmailId" maxlength="6"
										field-name="Email"
										placeholder="Email" >
									</label> 
									<label><span class="lp-sprite lp-phone"></span> <input
										type="text" name="phoneCode" id="completeVerificationPhoneCode" maxlength="6"
										field-name="Verification Code"
										placeholder="Enter Verification Code" >
									</label>
									<input
										class="lp-form-submit-button lp-button green lp-transition"
										type="submit" value="submit">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
									<a href="merchant/resend">Resend Code</a>
								</form>
							</div>
							<div class="col-sm-5 lp-no-display">
								<img src="resources/images/form_image.png"
									class="img-responsive" width="329" height="213" alt="">
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>-->
	<!-- email & mobile verification code modal for merchant end -->

	<!--signup modal for merchant start-->

	<div class="modal fade lp-modal" id="merchant_signup_form_modal"
		role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog lp-modal-dialog" role="document">
			<div class="modal-content lp-modal-content">
				<div class="modal-header lp-modal-header">
					<button type="button" class="close lp-modal-close"
						data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body lp-modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-sm-7 col-xs-12 lp-modal-form-part">
								<h4 class="lp-modal-title lp-light-font">Merchant
									Registration</h4>
									
									<div id="regErr"></div>
									<!-- commented by Ajmain 20190425 -->
								<%-- <form action="merchant/registration" class="lp-form">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
									<label> <span class="lp-sprite lp-f-name"></span> <input
										type="text" placeholder="First Name" field-name="First Name"
										data-valied="blank" name="firstName">
									</label> <label> <span class="lp-sprite lp-l-name"></span> <input
										type="text" placeholder="Last Name" field-name="Last Name"
										data-valied="blank" name="lastName">
									</label> <label> <span class="lp-sprite lp-email"></span> <input
										type="email" placeholder="Email Id" field-name="Email Id"
										data-valied="blank-email" name="emailId">
									</label>
									<div class="lp-form-group-field clearfix">
										<label class="pull-left"> <span
											class="lp-sprite lp-phone"></span> <!-- <div class="lp-custom-select-arrow">
												<i class="fa fa-chevron-down" aria-hidden="true"></i>
											</div> --> <input type="text" readonly
											value="Bangladesh (+880)"> <input type="hidden"
											id="selectCountry" value="+880">
										</label> <label class="pull-left"> <input
											class="lp-moNo-padding" maxlength="20" type="tel"
											field-name="Mobile Number" name="mobileNo"
											data-valied="blank-mobile" placeholder="Mobile Number">
										</label>
									</div>
									<label> <span class="lp-sprite lp-pass"></span> <input
										type="password" placeholder="Password" field-name="Password"
										data-valied="blank-password" name="password">
									</label> <label> <span class="lp-sprite lp-conf-pass"></span> <input
										type="password" placeholder="Confirm Password"
										field-name="Confirm Password" data-valied="blank-confirm"
										data-confirm="password" name="confirmPassword">
									</label>
									<div id="merchantRecaptcha">
										<a href="javascript:void(0);"> <img src="./ImageCreator"
											alt="" title="" id="captcha_image"> <i
											class="fa fa-refresh refreshcaptcha" aria-hidden="true"
											style="color: #000; margin: 0 auto; font-size: 24px; padding: 14px 0px 15px 6px; display: inline-block; vertical-align: top;"></i>
										</a> <label> <span class="lp-sprite lp-f-name"></span> <input
											type="text" placeholder="Enter Captcha" field-name="Captcha"
											data-valied="blank" name="captcha">
										</label>
									</div>
									<label class="lp-terms-line"> <input type="checkbox"
										data-valied="checked">
										<div>
											I agree to <a href="">Payment Terms of Use</a> &amp; <a
												href="">Privacy Policy</a>
										</div>
									</label> <input
										class="lp-form-submit-button lp-button green lp-transition"
										type="submit" value="sign up">
									<div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div>
								</form> --%>
								
								<!-- Added by Ajmain 20190425 -->
								<form action="#" class="lp-form" method="post" id="registerForm"
									style="display: none">

									<label> <span class="lp-sprite lp-f-name"></span> <input
										type="text" placeholder="Full Name" field-name="First Name"
										data-valied="blank" name="firstName" id="regName" required>
									</label> <label> <span class="lp-sprite lp-email"></span> <input
										type="email" placeholder="Email Id" field-name="Email Id"
										data-valied="blank-email" name="emailId1" id="regEmail"
										required>
									</label> <label> <span class="lp-sprite lp-phone"></span><input
										class="lp-moNo-padding" maxlength="20" type="tel"
										field-name="Mobile Number" name="mobileNo1"
										data-valied="blank-mobile" placeholder="Mobile Number"
										id="regPhone" onkeyup="checkMobile(this)" required>
									</label> <input
										class="lp-form-submit-button lp-login-button lp-button green lp-transition"
										type="submit" value="Next">
								</form>




								<!-- Added by Ajmain 20190425 -->
								<form action="#" class="lp-form"
									style="display: none" id="finalForm">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" id="finalCSRF"/>
										
										<input type="hidden" name="firstName"
										value="" required id="fullNameFinal"/>
										<input type="hidden" name="emailId"
										value="" required id="emailFinal"/>
										<input type="hidden" name="mobileNo"
										value="" required id="mobileFinal"/>
										
									<small style="padding-left: 6px; font-size: 10px; display:none" id="regNote">
Note: Must have an uppercase, lowercase, numeric and special character. Length is between 8 to 32</small>
									<label> <span class="lp-sprite lp-pass"></span> <input
										type="password" placeholder="Password" field-name="Password"
										data-valied="blank-password" name="password" minlength=8 maxlength=32 required id="regPass" style="margin-bottom: 5px; width:92%;">
									
										<i class="fa fa-info-circle" aria-hidden="true" style="color: red" onclick="showData('regNote')"></i>
									
									</label> <label> <span class="lp-sprite lp-conf-pass"></span> <input
										type="password" placeholder="Confirm Password"
										field-name="Confirm Password" data-valied="blank-confirm"
										data-confirm="password" id="confirmPass" name="confirmPassword" minlength=8 maxlength=32 onkeyup="checkPass(this.value, 'matchpassErr')" style="margin-bottom: 5px;">
									</label>
									<div id="matchpassErr"></div>
									<div id="merchantRecaptcha">
									<div class="row">
										<div class="col-sm-5">
										<a href="javascript:void(0);"> <img src="./ImageCreator"
											alt="" title="" id="captcha_image"> <i
											class="fa fa-refresh refreshcaptcha" aria-hidden="true"
											style="color: #000; margin: 0 auto; font-size: 24px; padding: 14px 0px 15px 6px; display: inline-block; vertical-align: top;"></i>
										</a>
										</div>
										<div class="col-sm-6">
										<label> <span class="lp-sprite lp-f-name"></span> <input
											type="text" placeholder="Enter Captcha" field-name="Captcha"
											data-valied="blank" name="captcha" id="captchaVal" style="width: 273px;margin-bottom:0px !important">
										</label>
										</div>
									
									</div>
										 
									</div>
									<label class="lp-terms-line" style="margin-top: 0px; padding-top:0px;"> <input type="checkbox"
										data-valied="checked" onclick="acceptCheck(this)" checked>
										<div>
											I agree to <a href="#">Payment Terms of Use</a> &amp; <a
												href="#">Privacy Policy</a>
										</div>
									</label> 
									<div class="row">
									
									<div class="col-xs-6">
									
									<input
										class="lp-form-submit-button lp-button green lp-transition"
										type="submit" value="sign up" id="signUpButton">
									</div>
									
									<div class="col-xs-6">
									<button
										class="lp-form-submit-button lp-button green lp-transition"
										id="backButton" style="background-color: #f96b6b">Back</button>
									</div>
									
									</div>
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
								</form>
								
							</div>
							<div class="col-sm-5 lp-no-display">
								<img src="<%=basePath%>resources/images/form_image.png"
									class="img-responsive" width="329" height="213" alt="">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!--signup modal for merchant end-->

	<!--login modal for merchant start-->

	<div class="modal fade lp-modal" id="merchant_login_modal_form"
		role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog lp-modal-dialog" role="document">
			<div class="modal-content lp-modal-content">
				<div class="modal-header lp-modal-header">
					<button type="button" class="close lp-modal-close"
						data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body lp-modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-sm-7 col-xs-12 lp-modal-form-part">
								<h4 class="lp-modal-title lp-light-font" id="modalTitle">Login/Register</h4>
								<div id="logErr"></div>

								<!-- Added by Ajmain 20190425 -->
								<form action="#" class="lp-form" method="post" id="checkForm">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />

									<label id="checkFormLabel"> <span
										class="lp-sprite lp-f-name"></span> <input type="text"
										name="userName" field-name="Email or Mobile"
										data-valied="blank-email_mobile"
										placeholder="Email / Mobile number" id="checkUsername"
										required data-toggle="tooltip" data-placement="top" title="This field can not be empty">
									</label> <input
										class="lp-form-submit-button lp-login-button lp-button green lp-transition"
										id="loginCheck" type="submit" value="Submit" name="submit">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
								</form>






								<!-- Added by Ajmain 20190425 -->
								<form action="merchant/login" class="lp-form" method="post"
									id="loginForm" style="display: none">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" id="loginCSRF" />
									<label> <span class="lp-sprite lp-f-name"></span> <input
										type="text" name="userName" field-name="Email or Mobile"
										data-valied="blank-email_mobile"
										placeholder="Email / Mobile number" id="userName" required style="width: 92%">
									</label> 
									<small style="padding-left: 6px; font-size: 10px; display:none" id="loginNote">
Note: Must have an uppercase, lowercase, numeric and special character. Length is between 8 to 32</small>
									<label id="passLabel"> <span class="lp-sprite lp-pass"></span>
										<input type="password" name="password" field-name="Password" style="margin-bottom: 0px; padding-bottom: 0px; width: 92%"
										data-valied="blank" placeholder="Password" id="loginPass" minlength=8 maxlength=32 data-toggle="tooltip" data-placement="top" title="Must have an uppercase, lowercase, numeric and special character. Length is between 8 to 32">
										<style>
										#loginSubmit{
											width: 92%;
										}
								
										</style>
										<i class="fa fa-info-circle" aria-hidden="true" style="color: red" onclick="showData('loginNote')"></i>
									</label> 
									<input
										class="lp-form-submit-button lp-login-button lp-button green lp-transition"
										type="submit" value="log in" id="loginSubmit">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
								</form>




								


								<div class="lp-terms-line lp-forgot-password-link">
									<div class="row" style="text-align: center !important;">
										<a class="col-xs-12" href="javascript:void(0)"
											data-toggle="modal" data-dismiss="modal"
											data-target="#merchant_forget_password_modal">Forgot
											Password</a>
										<!-- <a
											class="col-xs-6" href="javascript:void(0)"
											data-toggle="modal" data-dismiss="modal"
											data-target="#merchant_complete_registration">Complete your Signup</a> -->
									</div>
								</div>
							</div>
							<div class="col-sm-5 lp-no-display">
								<img src="<%=basePath%>resources/images/form_image_2.png"
									class="img-responsive" width="202" height="255" alt="">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Contact Us Modal Start-->

	<div id="contactUsModal" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Contact Us</h4>
				</div>
				<div class="modal-body clearfix">
					<div class="col-md-12 vertpad">
						<form action="./add-contactus" id="contactUs" method="post">
							<input type="hidden" name="csrfPreventionSalt"
								value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/m4.png"></span> <input
										class="form-control customInput required" id="name"
										field-name="Name" data-valied="blank" name="name"
										placeholder="Name" type="text">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/a7.png"></span> <input
										class="form-control customInput required" field-name="Email"
										id="email" data-valied="blank-email" name="emailId"
										placeholder="Email" type="text">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/c1.png"></span> <input
										class="form-control customInput required" field-name="Subject"
										data-valied="blank" id="email" name="subject"
										placeholder="Subject" type="text">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox textArea">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/c2.png"></span>
									<textarea class="form-control customInput required"
										field-name="Message" data-valied="blank" rows="5"
										name="contactUsMessage" id="comment" placeholder="Message"></textarea>
								</div>
							</div>
							<button type="submit" class="btn keyBtn">SEND</button>
							<button type="reset" class="btn keyBtn keyBtn2">RESET</button>
						</form>
					</div>

					<!-- <div class="col-md-12">
                                	<div class="social-icons">
                                    <h3>Follow us :</h3>
                                        <a class="btn btn-social-icon btn-dropbox"><i class="fa fa-dropbox"></i></a>
                                        <a class="btn btn-social-icon btn-facebook"><i class="fa fa-facebook"></i></a>
                                        <a class="btn btn-social-icon btn-google"><i class="fa fa-google-plus"></i></a>
                                        <a class="btn btn-social-icon btn-instagram"><i class="fa fa-instagram"></i></a>
                                        <a class="btn btn-social-icon btn-linkedin"><i class="fa fa-linkedin"></i></a>
                                        <a class="btn btn-social-icon btn-twitter"><i class="fa fa-twitter"></i></a>
                                    </div>
                  
                                </div> -->
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>
	<!--Contact Us Modal end-->
	<script>
	//Added by Ajmain 20190425
	
$("#checkForm").on("submit", function(e){
	e.preventDefault();
	var username = $("#checkUsername").val();
	
	$("#loginCheck").prop("disabled", true);
	var valid= false;
	var value = "";
	if(username.match(/^[0-9]+$/) != null){
		if(checkPhone(username)){
			valid = true;
			value="phone";
			}
		}else{
			if(checkEmail(username)){
				valid = true;
				value="email";
				}
			}
	if(valid){
		$("#checkUsername").css("border", "1px solid #d6d5d5");
		$("#logErr").html("");
		var data = {
				"username" : username
				};
		$.ajax({
	        type: "post",
	        url: "merchant/login1",
	        data: data,
	        success: function(resp) {
	            console.log(resp);
	            var s = resp.split(",");
	            var response = s[0];
	            var salt = s[1];
	            
	if(response == 1){
		$("#finalCSRF").val(salt);
		$("#logErr").html("");
		$("#loginCheck").prop("disabled", false);
		$("#modalTitle").text("Merchant Login");
		$("#loginForm").css("display", "block");
		$("#checkForm").css("display", "none");
		$("#userName").val(username);
		$("#loginPass").focus();
	}else if(response == 2){
		$("#merchant_login_modal_form").modal("hide");
		$("#merchant_signup_form_modal").modal("show");
		$("#logErr").html("");
		$("#loginCheck").prop("disabled", false);
		$("#registerForm").css("display", "block");
		$("#regName").focus();
if(value=="phone"){
	$("#regPhone").val(username);
}else if(value == "email"){
	$("#regEmail").val(username);
}
	} else{
		
		$("#checkUsername").css("border", "1px solid red");
		$("#loginCheck").prop("disabled", false);
		$("#logErr").html("<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>Invalid email or phone number.</div> <br>");
		}
	            },
	            error: function(err){
	console.log(err);
	                }
		});
		}else{
			$("#checkUsername").css("border", "1px solid red");
			$("#loginCheck").prop("disabled", false);
			$("#logErr").html("<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;' onclick='removediv()'>&#9888;</b>Invalid email or phone number.</div> <br>");
			}
	
});

$("#registerForm").on("submit",function(e){
	e.preventDefault();
	var email = $("#regEmail").val();
	var phone = $("#regPhone").val();
	var name = $("#regName").val();

	console.log(email);
	console.log(phone);
	if(checkEmail(email) && checkPhone(phone) && name.length > 0){
		$("#regPhone").css("border", "1px solid #d6d5d5");
		$("#regEmail").css("border", "1px solid #d6d5d5");
		$("#regErr").html("");
		$("#finalForm").css("display", "block");
		$("#registerForm").css("display", "none");
		$("#fullNameFinal").val(name);
		$("#emailFinal").val(email);
		$("#mobileFinal").val(phone);
		$("#regPass").focus();
		$("#confirmPass").val("");
		$("#captchaVal").val("");
		
		}else{
			if(!checkEmail(email) && !checkPhone(phone)){
				$("#regErr").html("<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;' onclick='removediv()'>&#9888;</b>Invalid email and phone number format.</div> <br>");
				$("#regEmail").css("border", "1px solid red");
				$("#regPhone").css("border", "1px solid red");
				}else{
					if(!checkEmail(email)){
						$("#regErr").html("<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;' onclick='removediv()'>&#9888;</b>Invalid email format.</div> <br>");
						$("#regEmail").css("border", "1px solid red");
						}
					if(!checkPhone(phone)){
						$("#regErr").html("<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;' onclick='removediv()'>&#9888;</b>Invalid phone number.</div> <br>");
						$("#regPhone").css("border", "1px solid red");
						}
					}
			
			
			}
});

$("#backButton").on("click", function(e){
	$("#finalForm").css("display", "none");
	$("#registerForm").css("display", "block");
});



$("#finalForm").on("submit", function(e){
	e.preventDefault();
	$("#signUpButton").prop("disabled", true);
	var csrf= $("#finalCSRF").val();
	var name = $("#fullNameFinal").val();
	var email = $("#emailFinal").val();
	var phone = $("#mobileFinal").val();
	var password = $("#regPass").val();
	var matchPass = $("#confirmPass").val();
	var capcha = $("#captchaVal").val();
	console.log(phone);
	if(checkPhone(phone) && checkEmail(email) && csrf.length > 0 && name.length > 0 && checkPass(matchPass, "") && capcha.length > 0){
		$("#regErr").html("");
		$("#regPhone").css("border", "1px solid #d6d5d5");
		$("#regEmail").css("border", "1px solid #d6d5d5");
		$("#confirmPass").css("border", "1px solid #d6d5d5");
		$("#regName").css("border", "1px solid #d6d5d5");
		$("#captchaVal").css("border", "1px solid #d6d5d5");
		var data = {
				"csrf": csrf,
"fullName" : name,
"email" : email,
"phone" : phone,
"password" : password,
"confirmPass" : matchPass,
"capcha" : capcha
				};
		console.log(data);
		$.ajax({
	        type: "post",
	        url: "merchant/registration",
	        data: data,
	        success: function(resp) {
	        	$('#captcha_image').attr("src", "./ImageCreator?aa="+Math.random());
	            console.log(resp);
	            $("#signUpButton").prop("disabled", false);
	            var s = resp.split("|");
	            var response = s[0];
	            var salt = s[1];
	            $("#finalCSRF").val(salt);
	            $("#loginCSRF").val(salt);
	            $("#regErr").html(response+ "<br>");

	            if(response.includes('success')){
	            	$("#finalForm").css("display", "none");
					$("#registerForm").css("display", "none");
					$("#checkForm").css("display", "block");
					$("#merchant_login_modal_form").modal("hide");
					$("#merchant_signup_form_modal").modal("hide");
					$("#merchant_mobile_verification_modal").modal("show");
		            }
	            if(response.includes('name')){
	            	$("#finalForm").css("display", "none");
					$("#registerForm").css("display", "block");
					$("#regName").css("border", "1px solid red");
		            }
	            if(response.includes('email')){
	            	$("#finalForm").css("display", "none");
					$("#registerForm").css("display", "block");
					$("#regEmail").css("border", "1px solid red");
		            }
	            if(response.includes('phone')){
	            	$("#finalForm").css("display", "none");
					$("#registerForm").css("display", "block");
					$("#regPhone").css("border", "1px solid red");
		            }
	            if(response.includes('password')){
	            	$("#finalForm").css("display", "block");
					$("#registerForm").css("display", "none");
					$("#confirmPass").css("border", "1px solid red");
		            }
	            if(response.includes('captcha')){
	            	$("#finalForm").css("display", "block");
					$("#registerForm").css("display", "none");
					$("#captchaVal").css("border", "1px solid red");
		            }
	            
	            
	        }, error: function(resp){
	            console.log(resp);
	            }
		}); 
		}else{
			$("#signUpButton").prop("disabled", false);
			if(!checkPhone(phone)){
				$("#finalForm").css("display", "none");
				$("#registerForm").css("display", "block");
				$("#regPhone").css("border", "1px solid red");
				$("#regErr").html(
			            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Invalid phone number format</div><br>"
			        );
				}
			if(!checkEmail(email)){
				$("#finalForm").css("display", "none");
				$("#registerForm").css("display", "block");
				$("#regEmail").css("border", "1px solid red");
				$("#regErr").html(
			            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Invalid email format</div><br>"
			        );
				}
			if(!checkPass(matchPass, "")){
				$("#confirmPass").css("border", "1px solid red");
				$("#regErr").html(
			            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Password does not match</div><br>"
			        );
				}
			if(name.length < 1){
				$("#finalForm").css("display", "none");
				$("#registerForm").css("display", "block");
				$("#regName").css("border", "1px solid red");
				$("#regErr").html(
			            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Merchant name is required</div><br>"
			        );
				}
			if(capcha.length < 1){
				$("#captchaVal").css("border", "1px solid red");
				$("#regErr").html(
			            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Capcha is required</div><br>"
			        );
				}
			}
	
});




function acceptCheck(email) {
    if (email.checked) {
        $('#signUpButton').css('display', 'block');
    } else {
    	$('#signUpButton').css('display', 'none');
    }

}


function checkPass(match, tag){
	var password = $("#regPass").val();
	if(password.length < 1){
		if(tag.length < 1){
			return false;
		}else{
			$("#matchpassErr").html(
		            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Password is empty </div><br>"
		        );
			}

		}else{
			if(tag.length < 1){
				if(password != match){
					return false;
					}else{
return true;
						}
				}else{
					if(password != match && match.length > 5){
						if(tag.length < 1){
							return false;
							}else{
								$("#matchpassErr").html(
							            "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px;'>&#9888;</b>Password does not match </div><br>"
							        );
								}
						
						}else if(password == match && match.length > 5){
							if(tag.length < 1){
								return true;
								}else{
									$("#matchpassErr").html("");
									}

									}

					}
	
			
			}
		}



function checkEmail(email) {

    var emailVal = email;
    var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    console.log(email);
    console.log(reg.test(String(email).toLowerCase()));
    if (reg.test(String(email).toLowerCase())) {
        return true;
    } else {
        return false;
    }
}

function checkPhone(mobile) {
    var phone = mobile.replace(/[^0-9+]/gi, '');
    phone.replace(' ', '');
    var phoneVal = phone;
    phoneVal = phoneVal.replace(/\s/g, '');
    phoneVal = phoneVal.replace('+88', '');
    phoneVal = phoneVal.replace('+', '');
    if (phoneVal.indexOf("88") == 0) {
        phoneVal = phoneVal.replace('88', '');
    }
    if (phoneVal.indexOf("1") == 0) {
        phoneVal = phoneVal.replace('1', '01');
    }
    
        if (phoneVal.length >= 11) {
        if (phoneVal.length != 11 ) {
return false;
        } else if ((phoneVal.startsWith("017") || phoneVal.startsWith("013") || phoneVal.startsWith("019") ||
                phoneVal.startsWith("014") || phoneVal.startsWith("015") || phoneVal.startsWith("018") || phoneVal
                .startsWith("016"))) {
            return true;
        } else {

            return false;

        }
    }else{
return false;
        }
}


function checkMobile(mobile) {
    var phone = mobile.value.replace(/[^0-9+-]/gi, '');
    phone.replace(' ', '');
    mobile.value = phone;
    var phoneVal = mobile.value;
    phoneVal = phoneVal.replace(/\s/g, '');
    phoneVal = phoneVal.replace('+88', '');
    if (phoneVal.indexOf("88") == 0) {
        phoneVal = phoneVal.replace('88', '');
    }
    if (phoneVal.indexOf("1") == 0) {
        phoneVal = phoneVal.replace('1', '01');
    }
    
    if (phoneVal.length >= 11) {
        if (phoneVal.length > 11) {

            var msg =
                "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px'>&#9888;</b> Invalid Phone Number</div>";
            /* $mbl.html(msg);
            $("#payee_phone").css("border-color", "red"); */
        } else if ((phoneVal.startsWith("017") || phoneVal.startsWith("013") || phoneVal.startsWith("019") ||
                phoneVal.startsWith("014") || phoneVal.startsWith("015") || phoneVal.startsWith("018") || phoneVal
                .startsWith("016"))) {
            
           /*  $mbl.html("");
            $("#payee_phone").css("border-color", "#f2f2f2"); */
        } else {

            var msg =
                "<div class='msg msg-error z-depth-3'> <b style='font-size: 17px'>&#9888;</b> Invalid Phone Number</div>";
            /* $mbl.html(msg);
            $("#payee_phone").css("border-color", "red"); */

        }
    }
}

function showData(data){
	$("#"+data).toggle();
	
}

	</script>
	
		<script type="text/javascript">
		$(".refreshcaptcha").click(function(){
			$(this).parent().find('#captcha_image').attr("src", "./ImageCreator?aa="+Math.random());
		});
	</script>

</body>
</html>