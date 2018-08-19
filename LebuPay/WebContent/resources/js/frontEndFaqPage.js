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

$( document ).ready(function() {
	function getTokenFromServer() {
		var getTokenURL = "get-token"
		var resFromServer = '';
		$.ajax({
			/*async : false,*/
			type : "POST",
			url : getTokenURL,
			contentType : 'application/text',
			cache : false,
			success : function(response) {
				resFromServer = response
			}
		});
		return resFromServer;
	}
	
	$( "form" ).submit(function( event ) {
		  event.preventDefault();
		  $(this).find("#serverRes").hide();
		  $(this).find("#serverRes").next().hide();
		  
		  // response function built start
		  var url = $(this).attr("action");
		  var successResponse = null;
		  
		  if(url.toLowerCase().indexOf("merchant") != -1){
			  //   merchant section
			  if(url == "merchant/login"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "* "+response.message
						  });
						  //messageSecdom.next().html("*"+response.message);
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  window.location = "merchant/dashboard"
					  }
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
			  else if(url == "merchant/registration"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  var msg = "";
						  if(typeof response.multipleMessage != "undefined" && response.multipleMessage.length > 0){
							  for(i=0; i<response.multipleMessage.length; i++){
								  jQuery.growl.error({
										message : "* "+response.multipleMessage[i]
								  });
								  msg += "*"+response.multipleMessage[i]+"<br>";
							  }  
						  }
						  else{
							  jQuery.growl.error({
									message : "* "+response.message
							  });
							  msg +=  "* "+response.message;
						  }
						  
						  //messageSecdom.next().html(msg);
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  messageSecdom.closest("form").find("input[type='password']").val("");
						  $('#merchant_signup_form_modal').modal('hide');
						  $('#merchant_mobile_verification_modal').modal('show');
					  }
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
			  else if(url == "merchant/phone-validation"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "*Please enter valid code"
						  });
						  //messageSecdom.next().html("*Please enter valied code");
						  messageSecdom.next().html("");
					  }
					  else{
						  $('#merchant_mobile_verification_modal').modal('hide');
						  jQuery.growl.notice({
								message : "Your mobile no is successfully verified"
						  });
						  //alert("Your mobile no is verified successfully")
						  $('#merchant_login_modal_form').modal('show');
					  }
				  }; 
			  }
			  else if(url == "merchant/forgot-password"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "*Please enter valid email id"
						  });
						  //messageSecdom.next().html("*Please enter valied email id");
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  jQuery.growl.notice({
								message : "*Please check your email id to change your Password"
						  });
						  //messageSecdom.next().html("*Please check your email id to change your Password");
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
		  }
		  else{
			  //   customer section
			  if(url == "customer/login"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "*"+response.message
						  });
						  //messageSecdom.next().html("*"+response.message);
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  window.location = "customer/dashboard"
					  }
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
			  else if(url == "customer/registration"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  var msg = "";
						  if(typeof response.multipleMessage != "undefined" && response.multipleMessage.length > 0){
							  for(i=0; i<response.multipleMessage.length; i++){
								  msg += "*"+response.multipleMessage[i]+"<br>";
								  jQuery.growl.error({
										message : "*"+response.multipleMessage[i]
								  });
							  }
						  }
						  else{
							  jQuery.growl.error({
									message : "* "+response.message
							  });
							  msg +=  "* "+response.message;
						  }
						  //messageSecdom.next().html(msg);
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  $('#customer_signup_form_modal').modal('hide');
						  $('#customer_mobile_verification_modal').modal('show');
					  }
					  
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
			  else if(url == "customer/phone-validation"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "*Please enter valid code"
						  });
						  //messageSecdom.next().html("*Please enter valied code");
						  messageSecdom.next().html("");
					  }
					  else{
						  $('#customer_mobile_verification_modal').modal('hide');
						  jQuery.growl.notice({
								message : "Your mobile no is successfully verified"
						  });
						  //alert("Your mobile no is verified successfully")
						  $('#customer_login_modal_form').modal('show');
					  }
				  }; 
			  }
			  else if(url == "customer/forgot-password"){
				  successResponse = function(response, messageSecdom){
					  messageSecdom.hide();
					  if(response.status != "ACTIVE"){
						  jQuery.growl.error({
								message : "*Please enter valid email id"
						  });
						  //messageSecdom.next().html("*Please enter valied email id");
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  else{
						  jQuery.growl.notice({
								message : "*Please check your email id to change your Password"
						  });
						  //messageSecdom.next().html("*Please check your email id to change your Password");
						  messageSecdom.next().html("");
						  messageSecdom.closest("form").find("input[type='password']").val("");
					  }
					  $('input[name="csrfPreventionSalt"]').val(response.csrfPreventionSalt);
				  }; 
			  }
			  else if(url == "./contact-us"){
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
			  }
		  }
		  
		// response function built end
		  
		  ///    validation check start
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
		  
		  
		  
		  
		  if(isValied){
			  if(form.find("input[type='password']").length != 0){
				  /*
			  	 * Password Hashing
			  	 */
			  	/*var salt = getTokenFromServer();
				var saltLength = salt.length;
				var saltLengthHalf = Math.round(saltLength / 2);*/
				var firstPart = "";//salt.substr(0, saltLengthHalf);
				var secondPart = "";//salt.substr(saltLengthHalf, saltLength);
				var password = form.find('[name="password"]').val();
				var md5password = firstPart + md5(password) + secondPart;

				form.find("input[type='password']").val(md5password);
				form.find("input[type='password']").css("background-color", 'White');
			  }
			  	
				
			  var submissionInfos = $( this ).serializeArray();
			  var ajaxData = {};
			  var form = $(this);
			  $.each(submissionInfos, function( index, value ) {
				  ajaxData[value.name] = value.value;
				  if(form.find("#selectCountry").length != 0 && value.name == "mobileNumber"){
					  ajaxData[value.name] = form.find("#selectCountry").val()+value.value;
				  }
				  else if(value.name == "g-recaptcha-response"){
					  ajaxData["gRecaptchaResponse"] = ajaxData[value.name];
				  }
			  });
			  
			  /*console.log(ajaxData);*/
			  var ajaxConfig = {
						method:"post",
						url:url,
						data:ajaxData
			  }
			  sendAjax(ajaxConfig, successResponse, $(this).find("#serverRes"));
		  }
		  
	});
	
	$("a:contains(Resend Code)").click(function( event ) {
		 event.preventDefault();
		var url = $(this).attr("href");
		var ajaxConfig = {
				method:"get",
				url:url
		}
		function successResponse(response, messageSecdom){
			  messageSecdom.hide();
			  if(response.status != "ACTIVE"){
				  jQuery.growl.error({
						message : "*Internal Server Error"
				   });
				  //messageSecdom.next().html("*Internal Server Error");
			  }
			  else{
				  jQuery.growl.notice({
						message : "*Your Code is Successfully Send to your mobile number"
				   });
				  //messageSecdom.next().html("*Your Code is Send Successfully");
			  }
		  }
		sendAjax(ajaxConfig, successResponse, $(this).prev().prev());
	});
});