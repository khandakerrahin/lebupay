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
<script src="<%=basePath%>resources/js/jquery-2.1.1.js"></script>
<!-- <script type="text/javascript">
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
<style>
html {
	display: none;
}
</style> -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Lebupay</title>

<!-- Bootstrap core CSS -->
<link href="<%=basePath%>resources/css/bootstrap/bootstrap.min.css"
	rel="stylesheet">
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
</head>

<body>

	<section class="sec_banner01 bg-image-full">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<button data-toggle="modal" class="btn-lg merchant-login-btn" data-target="#merchant_login_modal_form">MERCHANT
					LOGIN</button>
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

	<section class="sec_banner02">
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
			<div class="col-lg-6">
				<img class="image_full_width banner_03"
					src="<%=basePath%>resources/images/banner_image03.png" alt="">
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner04">
	<div class="container">
		<div class="row">
			<div class="col-lg-6">
				<h3 class="subhead">About us</h3>
				<p>LebuPay is brought to you by Spider Digital Commerce
					(Bangladesh) Ltd. We help our partners to organize lead product and
					service innovation initiaves in the digital space. We use design
					principles and innovation models to help our partners create and
					capture value in the digital economy.</p>
				<div>
					<a href="#" class="white_rounded_btn"> Know more </a>
				</div>
			</div>
		</div>
	</div>
	</section>

	<section class="sec_banner05">
	<div class="container">
		<div class="row">
			<div class="col-lg-6 offset-lg-4">
				<h2>
					<span style="font-weight: bold; font-size: 1em;"> For
						Pricing </span> <br /> or other questions
				</h2>
				<div class="cont">
					<form>
						<div class="row">
							<label for="phone" class="col-lg-3"> Phone- </label> 
							<label class="col-lg-6 lbl_txt"> Demo txt </label>
						</div>
						<div class="row">
							<label for="mail" class="col-lg-3"> Mail- </label> <input
								type="text" class="col-lg-6 no_brdr_field" id="mail">
						</div>
						<div class="row">
							<label for="messenger" class="col-lg-3"> Messenger- </label> <input
								type="text" class="col-lg-6 no_brdr_field" id="messenger">
						</div>
					</form>
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
	<script type="text/javascript">
		$(".refreshcaptcha").click(function(){
			$(this).parent().find('#captcha_image').attr("src", "./ImageCreator?aa="+Math.random());
		});
	</script>
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
	<div class="modal fade lp-modal" id="merchant_forget_password_modal" role="dialog"  data-backdrop="static" data-keyboard="false">
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
		id="merchant_mobile_verification_modal" role="dialog"  data-backdrop="static" data-keyboard="false">
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
	
	<!--signup modal for merchant start-->

	<div class="modal fade lp-modal" id="merchant_signup_form_modal" role="dialog" data-backdrop="static" data-keyboard="false">
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
								<form action="merchant/registration" class="lp-form">
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
											style="color: #000; margin: 0 auto; font-size: 24px; padding: 14px 0px 15px 6px; display:inline-block; vertical-align:top;"></i>
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

	<div class="modal fade lp-modal" id="merchant_login_modal_form"	role="dialog" data-backdrop="static" data-keyboard="false">
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
								<h4 class="lp-modal-title lp-light-font">Merchant Login</h4>
								<form action="merchant/login" class="lp-form" method="post">
									<input type="hidden" name="csrfPreventionSalt"
										value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
									<label> <span class="lp-sprite lp-f-name"></span> <input
										type="text" name="userName" field-name="Email or Mobile"
										data-valied="blank-email_mobile"
										placeholder="Email / Mobile number">
									</label> <label> <span class="lp-sprite lp-pass"></span> <input
										type="password" name="password" field-name="Password"
										data-valied="blank" placeholder="Password">
									</label> <input
										class="lp-form-submit-button lp-login-button lp-button green lp-transition"
										type="submit" value="log in">
									<%-- <div id="serverRes" style="display: none;"><img src="<%=basePath%>resources/images/spinner.gif" alt="loading..."></div><div></div> --%>
								</form>
								<div class="lp-terms-line lp-forgot-password-link">
									<div class="row">
										<a class="col-xs-6" href="javascript:void(0)"
											data-toggle="modal" data-dismiss="modal"
											data-target="#merchant_signup_form_modal">Sign Up</a> <a
											class="col-xs-6" href="javascript:void(0)"
											data-toggle="modal" data-dismiss="modal"
											data-target="#merchant_forget_password_modal">Forgot
											Password</a>
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

	<div id="contactUsModal" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
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
</body>
</html>
