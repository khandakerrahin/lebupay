<%@ include file="admin-header.jsp"%>

<!-- Main content -->
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">
			<div class="adminformBox panelAdmin borderGreen">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<span><img src="<%=basePath%>resources/images/user.png" /></span><a>Edit
								Reply</a>
						</h4>
					</div>

					<form method="post" action="reply-contactus">
						<input type="hidden" name="contactUsId"
							value="${contactUsModel.contactUsId }" />
						<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
						<div class="panel-body">


							<div class="row">
								<div class="form-group">
									<div class="customBox">
										<span class="iArea"><img
											src="http://localhost:7080/LebuPay/resources/images/m4.png"></span>
										<input class="form-control customInput required"
											readonly="readonly" id="name" field-name="Name"
											data-valied="blank" name="name" placeholder="Name"
											type="text" value="${contactUsModel.name }">
									</div>
								</div>
								<div class="form-group">
									<div class="customBox">
										<span class="iArea"><img
											src="http://localhost:7080/LebuPay/resources/images/a7.png"></span>
										<input class="form-control customInput required"
											readonly="readonly" field-name="Email" id="email"
											data-valied="blank-email" name="emailId" placeholder="Email"
											type="text" value="${contactUsModel.emailId }">
									</div>
								</div>
								<div class="form-group">
									<div class="customBox">
										<span class="iArea"><img
											src="http://localhost:7080/LebuPay/resources/images/c1.png"></span>
										<input class="form-control customInput required"
											readonly="readonly" field-name="Subject" data-valied="blank"
											id="email" name="subject" placeholder="Subject"
											type="text" value="${contactUsModel.subject }">
									</div>
								</div>
								<div class="form-group">
									<div class="customBox textArea">
										<span class="iArea"><img
											src="http://localhost:7080/LebuPay/resources/images/c2.png"></span>
										<textarea class="form-control customInput required"
											readonly="readonly" field-name="Message" data-valied="blank"
											rows="5" name="contactUsMessage" id="comment"
											placeholder="Message">${contactUsModel.contactUsMessage }</textarea>
									</div>
								</div>
								<div class="form-group">
									<div class="customBox textArea">
										<span class="iArea"><img
											src="http://localhost:7080/LebuPay/resources/images/c2.png"></span>
										<textarea class="form-control customInput required"
											field-name="Reply" data-valied="blank" rows="5"
											name="reply" id="reply" placeholder="Reply">${contactUsModel.reply }</textarea>
									</div>
								</div>
								<button type="submit" class="btn keyBtn">Reply</button>
								<button type="reset" class="btn keyBtn keyBtn2">RESET</button>
							</div>

						</div>

						<div class="row  m-t-30">
							<div class="col-md-12 col-xs-12">
								<div class="drop"></div>

							</div>
						</div>
						<!-- <div class="row  m-t-30">
							<div class="col-md-12 col-xs-12">
								<button type="submit" class="btn keyBtn">SUBMIT</button>
							</div>
						</div> -->
				</div>
				</form>
			</div>
		</div>
	</div>
	</div>
</section>
<!-- /.content -->

<%@ include file="admin-footer.jsp"%>

<%
					String message = (String) request.getAttribute("database.not.found");
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
					message = (String) request.getAttribute("general.pblm");
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
					message = (String) request.getAttribute("common.error");
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
					message = (String) request.getAttribute("contactus.reply.required");
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
					message = (String) request.getAttribute("contactus.subject.required");
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
					message = (String) request.getAttribute("contactus.message.required");
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


$(document).ready(function(){
	$('form').submit(function(event){
		 var isValied = true;
		  var form = $(this); 
		  $(this).find("[data-valied]").each(function(){
			   var valiedArr = $(this).attr("data-valied").split("-");
			   for(i=0; i< valiedArr.length && isValied; i++){
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
		  ///    validation check end
		  
		  if(!isValied){
			  event.preventDefault();
		  }
	});
});
</script>
