<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<title>Merchant Administration | Dashboard</title>
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
</head>
<body class="hold-transition skin-blue sidebar-mini noSidebar">
	<div class="wrapper">

		<header class="main-header newHeader"> <a href="./dashboard"
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu"><h1><img src="resources/images/lebupay_logo.png" alt="logo"></h1></span>
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

<div class="sandBox">
	
	<div class="sandBoxWrapper checkoutPage">
		<div class="row brdr-btm">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<h2>My Checkout Page Design</h2>
			</div>
		</div>
		<div class="row">
			<form action="update-checkout-layout" id="newForm" method="post" enctype="multipart/form-data">
				<div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group">
                        	<div class="inputBoxSec">
                         <label for="">Test</label>
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/banner-icon.png" /></span> 
                                    <input
									type="text" name="bannerName" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
				<div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group">
                        	<div class="inputBoxSec">
                         <label for="">Test</label>
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/banner-icon.png" /></span> 
                                    <input
									type="text" name="bannerName" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
                <div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group">
                        	<div class="inputBoxSec">
                         <label for="">Test</label>
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/banner-icon.png" /></span> 
                                    <input
									type="text" name="bannerName" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
                <div class="col-md-6 col-sm-12 col-xs-12 twoColumns">
					<div class="layoutSecond checkoutUp">
						<div class="input-group">
                        	<div class="inputBoxSec">
                         <label for="">Test</label>
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/banner-icon.png" /></span> 
                                    <input
									type="text" name="bannerName" class="form-control customInput"
									value="" >
                                    
							</div>
                         </div>
							
						</div>

					</div>
				</div>
			</form>
		</div>
	</div>
	
</div>



<!-- EDIT PARAMETER FORM START -->
<!-- <div id="editParameter" class="modal fade" role="dialog">
  <div class="modal-dialog">

    EDIT PARAMETER CONTENT
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"></h4>
      </div>
      <form action="updateChekoutParameter" method="post">
	      <div class="modal-body">
	      	<h3></h3>
	        <input type="hidden" name="parameterId" />
	        <ul class="form-wrapper clearfix">
			   <li class="clearfix">
			   	<label>Parameter Name:</label>
			   	<input name="parameterName" class="text" rel="Enter Parameter Name" type="text" maxlength="20">
			   </li>
			   <li class="clearfix">
			      <label>Parameter Type:</label>
			      <div id="editLabel" class="labeltxt">
			      	<span>
				      	<input id="rdEditLabeln" name="parameterType" class="Read Only" type="radio" value="INACTIVE">
				      	<label for="rdEditLabeln">Read Only</label>
				    </span>
				    <span>
				    	<input id="rdEditLabely" name="parameterType" class="Editable" type="radio" value="ACTIVE">
				    	<label for="rdEditLabely">Editable</label>
				    </span>
				  </div>
			   </li>
			   <li class="clearfix">
			      <label>Persistent:</label>
			      <div id="persistLabel" class="labeltxt">
			      	<span>
			      		<input id="rdPersistLabely" name="persistent" class="YES" type="radio" value="ACTIVE">
			      		<label for="rdPersistLabely">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdPersistLabeln" name="persistent" class="NO" type="radio" value="INACTIVE">
			      		<label for="rdPersistLabeln">NO</label>
			      	</span>
			      </div>
			   </li>
			   <li class="clearfix">
			      <label>Mandatory:</label>
			      <div id="mandatoryLabel" class="labeltxt">
			      	<span>
			      		<input id="rdMandatoryLabely" name="mandatory" class="YES" type="radio" value="ACTIVE">
			      		<label for="rdMandatoryLabely">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio" value="INACTIVE">
			      		<label for="rdMandatoryLabeln">NO</label>
			      	</span>
			      </div>
			   </li>
			   <li class="clearfix">
			      <label>Visible:</label>
			      <div id="visibleLabel" class="labeltxt">
			      	<span>
			      		<input id="rdVisibley" name="visible" type="radio" class="YES" value="ACTIVE">
			      		<label for="rdVisibley">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdVisiblen" name="visible" type="radio" class="NO" value="INACTIVE">
			      		<label for="rdVisiblen">NO</label>
			      	</span>
			      </div>
			   </li>
			</ul>
	      </div>
	      <div class="modal-footer">
	      	<button style="width: 77px; " class="btn btn-default" type="submit">Update</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
      </form>
    </div>

  </div>
</div> -->

<div class="modal fade" id="editParameter" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content start-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body clearfix">
				<form class="form-horizontal" method="post"
					action="update-checkout-parameter">
					<input type="hidden" name="parameterId" />
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Name:</label>
						<div class="col-md-6 col-sm-6">
							<input name="parameterName" class="text form-control"
								rel="Enter Parameter Name" type="text" maxlength="20">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Type:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdEditLabeln" name="parameterType"
								class="Read Only" type="radio" value="INACTIVE">Read
								Only</label> <label><input id="rdEditLabely"
								name="parameterType" class="Editable" type="radio"
								value="ACTIVE">Editable</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Persistent:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdPersistLabely" name="persistent"
								class="YES" type="radio" value="ACTIVE">Yes</label> <label><input
								id="rdPersistLabeln" name="persistent" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Mandatory:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdMandatoryLabely" name="mandatory"
								class="YES" type="radio" value="ACTIVE">Yes</label> <label><input
								id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Visible:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdVisibley" name="visible" type="radio"
								class="YES" value="ACTIVE">Yes</label> <label><input
								id="rdVisiblen" name="visible" type="radio" class="NO"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-10">
							<button type="submit" class="btn btn-success">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- Modal content end-->
	</div>
</div>
<!-- EDIT PARAMETER FORM END -->

<!-- ADD PARAMETER FORM START -->

<div class="modal fade" id="addParameter" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content start-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">&times;</button>
				<h4 class="modal-title">Parameter Add</h4>
			</div>
			<div class="modal-body clearfix">
				<form class="form-horizontal" method="post"
					action="add-checkout-parameter">
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Name:</label>
						<div class="col-md-6 col-sm-6">
							<input name="parameterName" data-name="Parameter Name"
								class="text form-control" rel="Enter Parameter Name" type="text"
								maxlength="20">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Type:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdEditLabeln"
								data-name="Parameter Type" name="parameterType"
								class="Read Only" type="radio" value="INACTIVE">Read
								Only</label> <label><input id="rdEditLabely"
								name="parameterType" class="Editable" type="radio"
								value="ACTIVE">Editable</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Persistent:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdPersistLabely" data-name="Persistent"
								name="persistent" class="YES" type="radio" value="ACTIVE">Yes</label>
							<label><input id="rdPersistLabeln" name="persistent"
								class="NO" type="radio" value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Mandatory:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdMandatoryLabely"
								data-name="Mandatory" name="mandatory" class="YES" type="radio"
								value="ACTIVE">Yes</label> <label><input
								id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Visible:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdVisibley" data-name="Visible"
								name="visible" type="radio" class="YES" value="ACTIVE">Yes</label>
							<label><input id="rdVisiblen" name="visible" type="radio"
								class="NO" value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-10">
							<button type="submit" class="btn btn-success">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- Modal content end-->
	</div>
</div>
<!-- ADD PARAMETER FORM END -->

<!-- DELETE PARAMETER FORM START -->
<div id="confirm" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- EDIT PARAMETER CONTENT-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Delete Parameter</h4>
			</div>
			<form action="delete-checkout-parameter" method="post">
				<div class="modal-body">
					<input type="hidden" name="parameterId" />
					<div id="message"></div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary" id="delete">Delete</button>
					<button type="button" data-dismiss="modal" class="btn">Cancel</button>
				</div>
			</form>
		</div>

	</div>
</div>
<!-- DELETE PARAMETER FORM END -->




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




<!-- Contact Us Modal Start-->

<div id="contactUsModal" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Contact Us</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="col-md-12 vertpad">
					<form action=".././contact-us" id="contactUs" method="post">
						<div class="form-group">
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/m4.png"></span> <input
									class="form-control customInput required" id="name"
									field-name="Name" data-valied="blank" name="name"
									placeholder="Name" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/a7.png"></span> <input
									class="form-control customInput required" field-name="Email"
									id="email" data-valied="blank-email" name="emailId"
									placeholder="Email" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox">
								<span class="iArea"><img
									src="resources/images/c1.png"></span> <input
									class="form-control customInput required" field-name="Subject"
									data-valied="blank" id="email" name="querySubject"
									placeholder="Subject" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox textArea">
								<span class="iArea"><img
									src="resources/images/c2.png"></span>
								<textarea class="form-control customInput required"
									field-name="Message" data-valied="blank" rows="5"
									name="queryDetails" id="comment" placeholder="Message"></textarea>
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
					src="resources/images/f1.png" /></a></li>
			<li class="f2"><a href="javascript:void(0)"><img
					src="resources/images/f2.png" /></a></li>
			<li class="f3"><a href="javascript:void(0)"><img
					src="resources/images/f3.png" /></a></li>
		</ul>
	</div>
	<div class="col-md-12">
		<span class="lp-copyright">&copy; 2017 -- All rights reserved</span>
	</div>
</footer>

<!-- jQuery UI 1.11.4 -->
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="resources/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="resources/js/app.min.js"></script>
<!-- responsive tab cum accordian js -->
<!-- <script src="js/easy-responsive-tabs.js"></script> -->
<!--color picker js--->
<script src="resources/js/color-picker.min.js"></script>
<!-- custom js -->
<script src="resources/js/custom_script.js"></script>
<!-- daterangepicker -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
<script src="resources/js/daterangepicker.js"></script>
<!-- datepicker -->
<script src="resources/js/bootstrap-datepicker.js"></script>
<script src="resources/js/ajaxScript.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/vendors/growl/css/jquery.growl.css" />
<script src="resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
<script src="resources/js/jquery.creditCardValidator.js"
	type="text/javascript"></script>
<script type="text/javascript">
function isValidEmailAddress(emailAddress) {
    var pattern = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    return pattern.test(emailAddress);
}
function isValidateMobileNumber(mobileNumber) {
    var filter = /^((\+[1-9]{1,4}[ \-]*)|(\([0-9]{2,3}\)[ \-]*)|([0-9]{2,4})[ \-]*)*?[0-9]{3,4}?[ \-]*[0-9]{3,4}?$/;
    return filter.test(mobileNumber);
}
function isNumberWithPlus(number){
	var filter = /^[\+]?[0-9]+$/;
	return filter.test(number);
}


$('div#myCarousel,div#viewAllCards').find('input[type="checkbox"]').change(function(event){
	if($(this).is(":checked")){
		function successResponse(response, messageSecdom, ajaxConfig){
			  messageSecdom.hide();
			  messageSecdom.next().hide();
			  
			  if(response.status != "ACTIVE"){
				  messageSecdom.next().html("*"+response.message);
				  $('input[value="'+ ajaxConfig.checkBox +'"]').prop("checked", false);
				  jQuery.growl.error({
						message : "Default Can't Be Set"
				  });
			  }
			  else{
				  $('input[type="checkbox"]').prop("checked", false);
				  $('input[value="'+ ajaxConfig.checkBox +'"]').prop("checked", true);
				  
				  $('input[type="checkbox"]').each(function(){
					  $(this).parent().parent().prev().html("");
					  $(this).parent().parent().parent().parent().prev().prev().show();
				  });
				  $('input[value="'+ ajaxConfig.checkBox +'"]').parent().parent().prev().html("<h2>Default</h2>");
				  $('input[value="'+ ajaxConfig.checkBox +'"]').parent().parent().parent().parent().prev().prev().hide();
				  messageSecdom.next().html('');
				  jQuery.growl.notice({
						message : "Default Set Successfully"
				  });
			  }
		  }
		
		  var ajaxData = {};
		  ajaxData[$(this).parent().prev().attr('name')] = $(this).parent().prev().val();
		  ajaxData[$(this).parent().prev().prev().attr('name')] = $(this).parent().prev().prev().val();
		
		  var ajaxConfig = {
					method:"post",
					url:'updateDefaultCardNetbankingDetails',
					data:ajaxData,
					checkBox:$(this).val()
		  }
		  sendAjax(ajaxConfig, successResponse, $("#serverRes"));
	}
	else{
		$(this).prop("checked", true);
		return false;
	}
});

var daleteForm = null;
$('a:contains(Delete)').click(function(){
	daleteForm = $(this).next();
	$("#confirm").modal('show');
});

$("#confirm").find("#delete").click(function(){
	daleteForm.submit();
});

	$(document).ready(function(){
		$("#navbar2 .active").removeClass("active");
		var arr = window.location.pathname.split("/");
		var pathName = arr[arr.length-1];
		$("#navbar2").find('a[href="'+ pathName +'"], a[externalSel~="'+ pathName +'"]').parent().addClass("active");
		
		$('#contactUs').submit(function(event){
			event.preventDefault();
			var url = $(this).attr("action");
			var successResponse = null;
			
			successResponse = function(response){
				if(response.status != "ACTIVE"){
					jQuery.growl.error({
						message : "* "+response.message
					});
				}
				else{
					$('#contactUsModal').modal('hide');
					$('#contactUs')[0].reset();
					jQuery.growl.notice({
						message : "* "+response.message
					});
				}
			};
			
			 var isValied = true;
			 var form = $(this); 
			  $(this).find("[data-valied]").each(function(){
				   var valiedArr = $(this).attr("data-valied").split("-");
				   for(i=0; i< valiedArr.length; i++){
					   switch(valiedArr[i]){
						   case "blank": if($(this).val().trim() == ""){
										   jQuery.growl.error({
												message : $(this).attr('field-name')+" can't be blank"
										   });
							   			   //form.find("#serverRes").next().show();
							   			   //form.find("#serverRes").next().html("*Please fill all required filds");
										   isValied = false;
									     }
						                 break;
						   case "email": if(!isValidEmailAddress($(this).val().trim())){
										   jQuery.growl.error({
												message : "*Please Insert valid email id"
										   });
							   			   //form.find("#serverRes").next().show();
							   			   //form.find("#serverRes").next().html("*Please Insert valied email");
										   isValied = false;
									     }
						                 break;
						   case "confirm": 
								   			var password = $(this).attr("data-confirm").trim()
								   			if($(this).val().trim() != form.find("[name='"+password+"']").val().trim()){
								   			   jQuery.growl.error({
													message : "*Password mismatched"
											   });
								   			   //form.find("#serverRes").next().show();
								   			   //form.find("#serverRes").next().html("*Password mismatched");
											   isValied = false;
										    }
							                break;
						   case "mobile": if(!isValidateMobileNumber($(this).val().trim())){
										   jQuery.growl.error({
												message : "*Please Insert valid mobile no"
										   });
							   			   //form.find("#serverRes").next().show();
							   			   //form.find("#serverRes").next().html("*Please Insert valied mobile no");
										   isValied = false;
									     }
						                 break;
						   case "password": /*if(!(/^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,16}$/.test($(this).val().trim()))){
											   jQuery.growl.error({
													message : "*Password length should be within 6-20 including at least one number, one lowercase and uppercase character, one special character"
											   });
											   isValied = false;
											}*/
							   				break;
						   case "email_mobile": if(isNumberWithPlus($(this).val().trim()) && !isValidateMobileNumber($(this).val().trim())){
						  							jQuery.growl.error({
														message : "*Please Insert valid mobile no"
												    });
						   							/*form.find("#serverRes").next().show();
										   			form.find("#serverRes").next().html("*Please Insert valied mobile no");*/
													isValied = false;
						   						}
						   						else if(!isNumberWithPlus($(this).val().trim()) && !isValidEmailAddress($(this).val().trim())){
												   jQuery.growl.error({
														message : "*Please Insert valid email"
												   });
									   			   //form.find("#serverRes").next().show();
									   			   //form.find("#serverRes").next().html("*Please Insert valied email");
												   isValied = false;
											     }
								                 break;
						   case "checked": if(!$(this).is(':checked')){
											   jQuery.growl.error({
													message : "*Please agree payment terms & privacy policies"
											   });
											   /*form.find("#serverRes").next().show();
									   		   form.find("#serverRes").next().html("*Please agree payment terms");*/
											   isValied = false;
						   				   }
						   				   break;
					   }
				   }
			  });
			
			 if(isValied){
				 var submissionInfos = $( this ).serializeArray();
					var ajaxData = {};
					$.each(submissionInfos, function( index, value ) {
					  ajaxData[value.name] = value.value;
					});
					var ajaxConfig = {
							method:"post",
							url:url,
							data:ajaxData
					}
					sendAjax(ajaxConfig, successResponse);
			 }
			
		});
		
		$('.walletUpdate').click(function(event){
			event.preventDefault();
			successResponse = function(response, messageSecdom, ajaxConfig){
				 window.location.reload();
			  }
		  	var ajaxConfig = {
					method:"get",
					/* url:'update-wallet?walletId='+$(this).attr('data-walletId') */
							url:'update-wallet'
			}
			sendAjax(ajaxConfig, successResponse);
	   });
		
		
		if($('input[name="cardNumber"]').length > 0){
			$('input[name="cardNumber"]').validateCreditCard(function(result) {
		       /*  $('.log').html('Card type: ' + (result.card_type == null ? '-' : result.card_type.name)
		                 + '<br>Valid: ' + result.valid
		                 + '<br>Length valid: ' + result.length_valid
		                 + '<br>Luhn valid: ' + result.luhn_valid); */
		          if(result.card_type == null){
		        	  $('.cardImg').attr('src', '.././resources/images/card-icon-form.png');
		          }
		          else{
		        	  $('.cardImg').attr('src', '.././resources/images/'+ result.card_type.name +'.png');
		          }
		    });
		}
		
		
		$('[data-card]').each(function(){
			var that = this;
			$('<input>').val($(this).attr('data-card')).validateCreditCard(function(result) {
			     if(result.card_type == null){
			   	  $(that).attr('src', '.././resources/images/card-icon-form.png');
			     }
			     else{
			   	  $(that).attr('src', '.././resources/images/'+ result.card_type.name +'.png');
			     }
			});
		});
	});
</script>
</body>
</html>

<script src="resources/js/clear_modal.js"></script>

<script>

$(function() {

	  // We can attach the `fileselect` event to all file inputs on the page
	  $(document).on('change', ':file', function() {
	    var input = $(this),
	        numFiles = input.get(0).files ? input.get(0).files.length : 1,
	        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
	    input.trigger('fileselect', [numFiles, label]);
	  });

	  // We can watch for our custom `fileselect` event like this
	  $(document).ready( function() {
	      $(':file').on('fileselect', function(event, numFiles, label) {

	          var input = $(this).parents('.input-group').find(':text'),
	              log = numFiles > 1 ? numFiles + ' files selected' : label;

	          if( input.length ) {
	              input.val(log);
	          } else {
	              if( log ) alert(log);
	          }

	      });
	  });
	  
	});

  

</script>


<script type="text/javascript">

$(".edit").click(function(event){
	event.preventDefault();
	var editParameter = $("#editParameter");
	
	if($(this).attr('data-is-deletable') != 0){
		$("#editParameter").find(".modal-header h4").html("Update Custom Parameter");
		$("#editParameter").find('form [name="parameterName"]').prop("readonly", false);
	}
	else{
		$("#editParameter").find(".modal-header h4").html("Update System Parameter");
		$("#editParameter").find('form [name="parameterName"]').prop("readonly", true);
	}
	
	var parameterId = $(this).attr("rel");
	
	function successResponse(response, messageSecdom){
		  messageSecdom.hide();
		  
		  $.each(response, function( index, value ) {
			  editParameter.find('input[type="hidden"][name="'+ index +'"], input[type="text"][name="'+ index +'"]').val(value);
			  editParameter.find('input[type="radio"][name="'+ index +'"][value="'+ value +'"]').prop("checked", true);
		});
	};
	
	
	var ajaxConfig = {
			method:"post",
			url:"get-checkout-parameter",
			data:{"parameterId":parameterId}
  	}
	
	
	/* $.ajax({
		type : "POST",
		url : "getChekoutParameterById",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify({"parameterId":parameterId}),
		success : function(data) {
			$.each(data, function( index, value ) {
				  editParameter.find('input[type="hidden"][name="'+ index +'"], input[type="text"][name="'+ index +'"]').val(value);
				  editParameter.find('input[type="radio"][name="'+ index +'"][value="'+ value +'"]').prop("checked", true);
			});
			
		}
	}); */
	
	sendAjax(ajaxConfig, successResponse, $(this).find("#serverRes"));
	
	/* $(this).closest("tr").children().each(function(){
		var classAttrName = $(this).attr("class");
		if(typeof classAttrName != "undefined"){
			classAttrName = classAttrName.trim();
			editParameter.find('[type="text"][name="'+ classAttrName +'"],[type="hidden"][name="'+ classAttrName +'"]').val($(this).html());
			editParameter.find('[type="radio"][class="'+ $(this).html().trim() +'"][name="'+ classAttrName +'"]').prop('checked', true);
		}
	}); */
	
	editParameter.modal('show');
});

$(".restore").click(function(event){
	event.preventDefault();
	window.location = "restore-default-layout";
});

$(".pre").click(function(event){
	event.preventDefault();
	window.open('preview-quickpay-checkout','_blank');
	//window.location = "preview-quickpay-checkout";
});

$('.cancel[type="reset"]').click(function(event){
	event.preventDefault();
	window.location.reload();
});

$(".deleteParameter").click(function(event){
	event.preventDefault();
	if($(this).parent().prev().find("a").attr('data-is-deletable') != 0){
		$("#confirm").find('[name="parameterId"]').val($(this).attr('rel'));
		$("#confirm").find('#message').html("Are you sure to Delete "+$(this).closest("tr").find(".parameterName").html()+" parameter?");
		$("#confirm").modal('show');
	}
});

$("#addParameter form").submit(function(event){
	if($(this).find('input[type="text"]').val().trim() === ""){
		event.preventDefault();
		jQuery.growl.error({
			message : "*Please enter parameter name"
		});
	}
	else if(/[^A-Za-z0-9]/g.test($(this).find('input[type="text"]').val().trim())){
		event.preventDefault();
		jQuery.growl.error({
			message : "*Please enter valid parameter name"
		});
	}
	var form = $(this); 
	form.find('input[type="radio"][data-name]').each(function(){
		/* console.log($(this).attr('data-name')); */
		if(!form.find('[name="'+ $(this).attr('name') +'"]').is(':checked')){
			event.preventDefault();
			jQuery.growl.error({
				message : "*Please select "+ $(this).attr('data-name')
			});
		}
	});
	
});
$("#editParameter form").submit(function(event){
	 if(/[^A-Za-z0-9]/g.test($(this).find('input[type="text"]').val().trim())){
			event.preventDefault();
			jQuery.growl.error({
				message : "*Please enter valid parameter name"
			});
		}
});




/***
* Tooltip
*/
$( function()
		{   
		   
		    
			
		    var targets = $( '[rel~=tooltip]' ),
		        target  = false,
		        tooltip = false,
		        title   = false;
				
					
		    targets.bind( 'mouseenter', function()
		    {
		        target  = $( this );
		        tip     = target.attr( 'title' );
		        tooltip = $( '<div class="addTooltip"></div>' );
		 
		        if( !tip || tip == '' )
		            return false;
		 
		        target.removeAttr( 'title' );
		        tooltip.css( 'opacity', 0 )
		               .html( tip )
		               .appendTo( 'body' );
		 
		        var init_tooltip = function()
		        {
		            if( $( window ).width() < tooltip.outerWidth() * 1.5 )
		                tooltip.css( 'max-width', $( window ).width() / 2 );
		            else
		                tooltip.css( 'max-width', 340 );
		 
		            var pos_left = target.offset().left + ( target.outerWidth() / 2 ) - ( tooltip.outerWidth() / 2 ),
		                pos_top  = target.offset().top - tooltip.outerHeight() - 20;
		 
		            if( pos_left < 0 )
		            {
		                pos_left = target.offset().left + target.outerWidth() / 2 - 20;
		                tooltip.addClass( 'left' );
		            }
		            else
		                tooltip.removeClass( 'left' );
		 
		            if( pos_left + tooltip.outerWidth() > $( window ).width() )
		            {
		                pos_left = target.offset().left - tooltip.outerWidth() + target.outerWidth() / 2 + 20;
		                tooltip.addClass( 'right' );
		            }
		            else
		                tooltip.removeClass( 'right' );
		 
		            if( pos_top < 0 )
		            {
		                var pos_top  = target.offset().top + target.outerHeight();
		                tooltip.addClass( 'top' );
		            }
		            else
		                tooltip.removeClass( 'top' );
		 
		            tooltip.css( { left: pos_left, top: pos_top } )
		                   .animate( { top: '+=10', opacity: 1 }, 50 );
		        };
		 
		        init_tooltip();
		        $( window ).resize( init_tooltip );
		 
		        var remove_tooltip = function()
		        {
		            tooltip.animate( { top: '-=10', opacity: 0 }, 50, function()
		            {
		                $( this ).remove();
		            });
		 
		            target.attr( 'title', tip );
		        };
		 
		        target.bind( 'mouseleave', remove_tooltip );
		        tooltip.bind( 'click', remove_tooltip );
		    });
		});
</script>





















