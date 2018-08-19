<%@ include file="merchant-sandbox-header.jsp"%>

<div class="sandBoxWrapper">
	<div class="row brdr-btm">
		<div class="col-md-10 col-sm-10 col-xs-12">
			<h2>Link For Payment</h2>
		</div>
		<%-- <div class="col-md-2 col-sm-2 col-xs-12">
			<ul>
				<li><a href="javascript:void(0)"><span><span><img
								src="<%=basePath%>resources/images/white-settings.png" /></span></span></a></li>
			</ul>
		</div> --%>
	</div>
	<form action="link" method="post" id="paymentViaLinkForm">
	<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
	<div class="row">
		<div class="col-md-12 vertpad">
			<!-- <h3 class="greenColor">Create New</h3> -->
			<h4>To :</h4>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/ficon1.png"></span> <input
						type="text" class="form-control customInput required" id="firstName"
						placeholder="First Name" name="firstName" value="${emailInvoicingModel.firstName }" tabindex="1">
				</div>
			</div>
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/e2.png"></span> <input
						type="text" class="form-control customInput required" id="email"
						placeholder="Email Id" name="emailId" value="${emailInvoicingModel.emailId }" tabindex="3">
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/e1.png"></span> <input
						type="text" class="form-control customInput required" id="lastName"
						placeholder="Last Name" name="lastName" value="${emailInvoicingModel.lastName }" tabindex="2">
				</div>
			</div>
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i4.png"></span> <input
						type="text" class="form-control customInput required" id="mobileNumber" onkeyup="return validateNumber(this,'0123456789');"
						placeholder="Mobile Number" name="mobileNumber" value="${emailInvoicingModel.mobileNumber }" tabindex="4">
				</div>
			</div>
		</div>
		<div class="col-md-12 vertpad">
			<%-- <h5>
				<b>From :</b> ${merchantModel.getEmailId()}
			</h5> --%>
		</div>
		<%-- <div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i3.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Invoice No" name="invoiceNo" value="${emailInvoicingModel.invoiceNo }">
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i3.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Type" name="type" value="${emailInvoicingModel.type }">
				</div>
			</div>
		</div> --%>
		<!-- <div class="col-md-12 vertpad greenAnchor">
			<h5>
				<b>Invoice URL :</b>
			</h5>
			<a href="javascript:void(0)">
				https://sandbox.lebupay.com/invoice/d77gl7rv1y/g6</a>
		</div> -->
		<div class="col-md-6 col-sm-12 col-xs-12 vertpad">
			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/i2.png"></span> <input
								type="text" class="form-control customInput required"
								id="amount" placeholder="Amount" name="amount" value="${emailInvoicingModel.amount }" tabindex="5">
						</div>
					</div>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/money-image.png"></span>
							<div class="custom-dropdown-menu">
								<select name="BDT">
									<option value="<span>&#2547;</span>">BDT</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-12 col-md-12 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox textArea">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/c2.png"></span>
							<textarea class="form-control customInput required" rows="5"
								id="comment" placeholder="Description" name="description" tabindex="7">${emailInvoicingModel.description }</textarea>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12 vertpad">
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea"><img
								src="<%=basePath%>resources/images/subject.png"></span> <input
								type="text" class="form-control customInput required"
								id="subject" placeholder="Subject" name="subject" value="${emailInvoicingModel.subject }" tabindex="6">
						</div>
					</div>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12">
					<h5>
						<b>Reference Key :</b> Pluggers
					</h5>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12">
					<label class="checkbox-inline"><input type="checkbox"
						value="email" name="pluggers" id="plug_1">Email</label> <label class="checkbox-inline"><input
						type="checkbox" value="sms" name="pluggers" id="plug_2">SMS</label>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12 email_buttons m-t-20">
					<!-- <button type="submit" class="btn btn-default round keyBtn">PREVIEW</button> -->
					<button type="submit" class="btn btn-default round keyBtn keyBtn2">CREATE
						& SEND</button>
					<!-- <button type="submit" class="btn btn-default round keyBtn darkBtn">PRINT
						& CREATE</button> -->
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" name="paymentLink1" id="paymentLink1" value="${basePaymentLink}">
	
	</form>
</div>

<%@ include file="merchant-sandbox-footer.jsp"%>


<script type="text/javascript">
$('form[action="email-invoicing"]').submit(function(){
	var mobileNo = $('[name="mobileNumber"]').val();
	if(mobileNo.indexOf('+880') == -1){
		$('[name="mobileNumber"]').val("+880" + mobileNo);
	}
});

$(document).ready(function(){
	
	
	$("#copyLink").click(function(){
		var copyText = document.getElementById("paymentLink");
		copyText.value = document.getElementById("paymentLink").value;
		copyText.select();
		document.execCommand("copy");
		//alert("Copied the text: " + copyText.value);
	});
	
	$("#paymentViaLinkForm").submit(function(){
	 	
		if($.trim($("#firstName").val()) == ""){
			jQuery.growl.error({
				message : "Please enter first name."
			});
			return false;
		}
		else if($.trim($("#lastName").val()) == ""){
			jQuery.growl.error({
				message : "Please enter last name."
			});
			return false;
		}
		
		if($.trim($("#email").val()) == ""){
			jQuery.growl.error({
				message : "Please enter email."
			});
			return false;
		}
		else if($.trim($("#email").val()) != "" && !validateEmail($.trim($("#email").val()))){
			jQuery.growl.error({
				message : "Please enter email in correct format."
			});
			return false;
		}
		
		if($.trim($("#mobileNumber").val()) == ""){
			jQuery.growl.error({
				message : "Please enter mobile number."
			});
			return false;
		}
		
		else if($.trim($("#amount").val()) == ""){
			jQuery.growl.error({
				message : "Please enter amount."
			});
			return false;
		}
		else if($.trim($("#subject").val()) == ""){
			jQuery.growl.error({
				message : "Please enter subject."
			});
			return false;
		}
		else if($.trim($("#comment").val()) == ""){
			jQuery.growl.error({
				message : "Please enter description."
			});
			return false;
		}
		else if($.trim($("#mobileNumber").val()) != ""){
			
			var permit = "0123456789";
			var num = $("#mobileNumber").val();
			var flag = 0;
			
			if(num.length > 0) {
				for (var i = 0; i < num.length; i++){
					//alert(permit.indexOf(num[i]));		
					if(permit.indexOf(num[i]) == -1) {
						flag = 1;
						break;
					}
				}
				//alert(flag);
				if(flag == 1) {
					//alert(num.lenght);
					jQuery.growl.error({
						message : "Please enter mobile no. in correct format"
					});
					//alert(newNumber);
					return false;
				}
				
			}
		}
		
		var plug = 5;
		var flg = 0;
		for(i = 1;i <= 5; i++) {
						
			if($("#plug_"+i).prop("checked") == true) {
				
				flg = 1;
			}
		}
		
		if(flg == 0) {
			jQuery.growl.error({
				message : "Please enter atleast one plugger."
			});
			
			return false;
		}
		
		
		
	});
});

function validateEmail(email) {
	  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  return re.test(email);
}

function validateNumber(number, permit) {
	
	var num = $(number).val();
	var flag = 0;
	for (var i = 0; i < num.length; i++){
			
		if(permit.indexOf(num[i]) == -1) {
			flag = 1;
			break;
		}
	}
	
	if(flag == 1) {
		
		var newNumber = num.substr(0, num.length-1);
		$('#mobileNumber').val(newNumber);
	}
	
	return false;
}



</script>


<%
String message = (String) request.getAttribute("link.send.success");
if (message != null) {
%>
	<script>
	$(document).ready(function() {
		jQuery.growl.notice({
			message : "<%=message%>"
		});
		
		$('#merchant_copy_link_modal').modal('show');
	});
	</script>
<%
}
%>

					
<%
message = (String) request.getAttribute("link.send.failed");
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
message = (String) request.getAttribute("merchant.first.name.required");
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
message = (String) request.getAttribute("merchant.last.name.required");
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
message = (String) request.getAttribute("merchant.email.required");
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
message = (String) request.getAttribute("merchant.mobile.no.required");
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
message = (String) request.getAttribute("pluggers.required");
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
message = (String) request.getAttribute("type.required");
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
message = (String) request.getAttribute("bdt.required");
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
message = (String) request.getAttribute("amount.required");
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
message = (String) request.getAttribute("subject.required");
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
message = (String) request.getAttribute("description.required");
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
message = (String) request.getAttribute("amount.numeric");
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
message = (String) request.getAttribute("amount.negative.error");
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
<% message = (String) request.getAttribute("amount.decimal.error");
	if (message != null) { %>
		<script>
		$(document).ready(function() {
			jQuery.growl.error({
				message : "<%=message%>"
			});
		});
		</script>
<% } %>	

<% message = (String) request.getAttribute("merchant.mobile.no.invalid.format");
	if (message != null) { %>
		<script>
		$(document).ready(function() {
			jQuery.growl.error({
				message : "<%=message%>"
			});
		});
		</script>
<% } %>	


<div class="modal payment_link_modal" id="merchant_copy_link_modal" role="dialog" data-backdrop="static" data-keyboard="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">Payment Link</h4>
      </div>
      <div class="modal-body">
        
        <% 
		message = (String) request.getAttribute("link.send.success");
		if (message != null) {
			String basePaymentLink = (String) request.getAttribute("basePaymentLink");
		%>
		<lable>Copy below link:</lable>
		<textarea name="paymentLink" id="paymentLink" class="form-control required paymentLinkText" readonly rows="1" >${basePaymentLink}</textarea>
		
		<%	
		}
		%>
		
          </div>
          <div class="modal-footer">
          <!--  <a href="javascript:void(0)" id="copyLink" class="copy_link">Copy Link</a>-->
      	<button type="button" class="btn btn-default copy_link btn-warning pull-left" id="copyLink">Copy Link</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<!-- Copy link modal for merchant end -->					