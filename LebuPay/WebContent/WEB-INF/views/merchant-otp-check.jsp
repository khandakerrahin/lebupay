<%@ include file="merchant-header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- Main content -->
<section class="content-wrapper res-m-t">
	<div class="sandBox bg-white">
		<div class="sandBoxWrapper newAcc">
			<div class="pad_20">
				<form method="post" action="update-profile">
					<input type="hidden" name="csrfPreventionSalt"
						value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
					<div class="otpCheck">
						<h2>Enter OTP</h2>

						<p>OTP is sent successfully to your primary number ending in
							*******${fn:substring(merchantModelProfile.mobileNo,
 							 fn:length(merchantModelProfile.mobileNo)-4,
						   fn:length(merchantModelProfile.mobileNo))}</p>
						<div class="inputBox">
							<input type="text" name="phoneCode"
								class="otpCheckInput borderBott" placeholder="Enter OTP" /> <label><a
								href="resend-otp">Resend OTP</a></label>
						</div>
						<button class="ButtOTP">SUBMIT OTP</button>
						<input type="hidden" name="firstName"
							value="${merchantModelProfile.firstName }" /> <input
							type="hidden" name="lastName"
							value="${merchantModelProfile.lastName }" /> <input
							type="hidden" name="mobileNo"
							value="${merchantModelProfile.mobileNo }" /> <input
							type="hidden" name="emailId"
							value="${merchantModelProfile.emailId }" />

					</div>
				</form>
			</div>

		</div>


	</div>



</section>
<!-- /.content -->
<%@ include file="merchant-footer.jsp"%>
<%
	String message = (String) request.getAttribute("common.phone.code.resend.successfully");
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

<%
	message = (String) request.getAttribute("common.invalid.phone.code");
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
	message = (String) request.getAttribute("common.phone.code.required");
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