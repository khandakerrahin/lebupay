
<%
	String path1 = request.getContextPath();
	String basePath11 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path1 + "/";
%>

</div>
</div>
</div>
</div>
</section>
</div>
<!-- Custom CSS -->
<link href="<%=basePath11%>resources/css/styles.css" rel="stylesheet">
<!-- Responsive CSS -->
<link href="<%=basePath11%>resources/css/media.css" rel="stylesheet">
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
									src="<%=basePath11%>resources/images/m4.png"></span> <input
									class="form-control customInput required" id="name"
									field-name="Name" data-valied="blank" name="name"
									placeholder="Name" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath11%>resources/images/a7.png"></span> <input
									class="form-control customInput required" field-name="Email"
									id="email" data-valied="blank-email" name="emailId"
									placeholder="Email" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath11%>resources/images/c1.png"></span> <input
									class="form-control customInput required" field-name="Subject"
									data-valied="blank" id="email" name="querySubject"
									placeholder="Subject" type="text">
							</div>
						</div>
						<div class="form-group">
							<div class="customBox textArea">
								<span class="iArea"><img
									src="<%=basePath11%>resources/images/c2.png"></span>
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
		class="col-md-9 col-sm-8 col-xs-12 pull-left hidden-xs footer-menu">
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
	<div class="col-md-3 col-sm-4 col-xs-12 pull-right hidden-xs social">
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
	<div class="col-xs-12">
		<span class="lp-copyright">&copy; All rights reserved to Spider Digital Commerce (Bangladesh) Ltd</span>
	</div>
</footer>

<!-- jQuery UI 1.11.4 -->
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<%=basePath11%>resources/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="<%=basePath11%>resources/js/app.min.js"></script>
<!-- responsive tab cum accordian js -->
<!-- <script src="js/easy-responsive-tabs.js"></script> -->
<!--color picker js--->
<script src="<%=basePath11%>resources/js/color-picker.min.js"></script>
<!-- custom js -->
<script src="<%=basePath11%>resources/js/custom_script.js"></script>
<!-- daterangepicker -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
<script src="<%=basePath11%>resources/js/daterangepicker.js"></script>
<!-- datepicker -->
<script src="<%=basePath11%>resources/js/bootstrap-datepicker.js"></script>
<script src="<%=basePath11%>resources/js/ajaxScript.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath11%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath11%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
<script src="<%=basePath11%>resources/js/jquery.creditCardValidator.js"
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
