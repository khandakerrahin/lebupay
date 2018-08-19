<%@ include file="merchant-sandbox-header.jsp"%>
<div class="sandBoxWrapper">
	<div class="row brdr-btm">
		<div class="col-md-10 col-sm-10 col-xs-12">
			<h2>Merchant Card Percentage</h2>
		</div>
	</div>
	<form action="link" method="post" id="">
	<div class="row">
		<div class="col-md-12 vertpad">
			<h4></h4>
		</div>
	<div class="col-md-4 col-sm-12 col-xs-12">
		<label style=text-align:center;>Card Type</label>
	</div>
	<div class="col-md-4 col-sm-12 col-xs-12">
		<label style=text-align:center;>Percentage</label>
	</div>
	<div class="col-md-4 col-sm-12 col-xs-12">
		<label style=text-align:center;>Fixed</label>
	</div>
	<c:choose>
		<c:when test="${not empty cardTypePercentageModels}">										
			<c:forEach items="${cardTypePercentageModels}" var="data">
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
							${data.cardType} 
					</div>
				</div>
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea">
								<img src="<%=basePath%>resources/images/i2.png">
							</span> 
							<input type="text" class="form-control customInput required" value="${data.cardPercentage}" tabindex="1" readonly>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea">
								<img src="<%=basePath%>resources/images/i2.png">
							</span> 
							<input type="text" class="form-control customInput required" value="${data.fixed}" tabindex="2" readonly>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach items="${cardTypeModels}" var="data">
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
							${data.cardType} 
					</div>
				</div>
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea">
								<img src="<%=basePath%>resources/images/i2.png">
							</span> 
							<input type="text" class="form-control customInput required" value="0" tabindex="1" readonly>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-sm-12 col-xs-12">
					<div class="form-group">
						<div class="customBox">
							<span class="iArea">
								<img src="<%=basePath%>resources/images/i2.png">
							</span> 
							<input type="text" class="form-control customInput required" value="0" tabindex="2" readonly>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	</div>
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
<%
message = (String) request.getAttribute("amount.decimal.error");
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