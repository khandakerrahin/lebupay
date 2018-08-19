<%@ include file="merchant-header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- Main content -->
<section class="content-wrapper res-m-t">
	<div class="sandBox bg-white">
		<div class="sandBoxWrapper newAcc">
			<div class="pad_20">
				<div class="row">
					<div class="col-lg-12">
						<h3>
							<a href="#"><img
								src="<%=basePath%>resources/images/user-profile.png" /></a> <span>Edit Merchant Profile</span>
						</h3>
					</div>
				</div>

				<form method="post" action="otp-check">
					<input type="hidden" name="csrfPreventionSalt"
						value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
					<div class="row commonCss m-t-20">
						<div class="col-md-6 col-sm-12 col-xs-12 withdrawCash">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										class="form-control customInput required" readonly
										placeholder="First Name" name="firstName"
										value="${merchantModel.getFirstName()}">
								</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-12 col-xs-12 current-balance">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/e1.png"></span> <input
										class="form-control customInput required"
										placeholder="Last Name" name="lastName" readonly
										value="${merchantModel.getLastName()}">
								</div>
							</div>
						</div>
					</div>

					<div class="row m-t-20">
						<div class="col-md-6 col-sm-12 col-xs-12 verified">
							<div class="row">
								<%-- <div class="col-md-12 clearfix">
	                                <div class="pull-right">
	                                    <span><img src="<%=basePath%>resources/images/check-green.png"/></span>
	                                    <h5>Verified</h5>
	                                </div>
	                            </div> --%>

								<div class="col-md-12 m-t-10">
									<div class="form-group">
										<div class="customBox">

											<span class="iArea"><img
												src="<%=basePath%>resources/images/mobile.png"></span> <input
												class="form-control customInput required"
												placeholder="Mobile Number" name="mobileNo"
												value="${fn:replace(merchantModel.mobileNo, '+880', '')}">


										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col-md-6 col-sm-12 col-xs-12 verified">
							<div class="row">
								<div class="col-md-12 clearfix">
									<%-- <div class="pull-right">
	                                	<span><img src="<%=basePath%>resources/images/check-green.png"/></span>
	                                	<h5>Verified</h5>
	                            	</div> --%>
								</div>

								<div class="col-md-12 m-t-10">
									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/e2.png"></span> <input
												class="form-control customInput required"
												placeholder="Email Id" name="emailId"
												value="${merchantModel.getEmailId()}">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-t-20">
						<div class="col-md-6 col-sm-12 col-xs-12 verified">
							<div class="row">
								<div class="col-md-12">
									<div class="updateNdPassword">
										<input type="submit" class="btn otpBtn bold-btn"
											value="Send
											OTP">
											<a class="changePass" href="./change-password">Change
								Password</a>
										<!-- <input class="otpInput" type="text" />
									<button type="submit" class="btn keyBtn bold-btn">Confirm OTP</button>-->
									</div>
									<div></div>
								</div>
							</div>
						</div>
						<!-- <div class="row m-t-20">
						<div class="col-md-6 col-sm-12 updateNdPassword">
							<button type="submit" class="btn keyBtn bold-btn">UPDATE</button>
							<a class="changePass" href="./change-password">Change
								Password</a>
						</div>
					</div>
						<div class="row m-t-20">
							<div class="col-md-6 col-sm-12 col-xs-12 verified">
								<div class="row"></div>
								<div></div>
							</div>
						</div>
					</div>
					<div class="row m-t-20">
						<div class="col-md-6 col-sm-12 updateNdPassword">
							<button type="submit" class="btn keyBtn bold-btn">UPDATE</button>
							<a class="changePass" href="./change-password">Change
								Password</a>
						</div>

					</div> -->

				</form>

			</div>

		</div>


	</div>



</section>
<!-- /.content -->
<%@ include file="merchant-footer.jsp"%>
<script type="text/javascript">
$('form[action="otp-check"]').submit(function(){
	$('input[name="mobileNo"]').val("+880"+$('input[name="mobileNo"]').val());
});
</script>

<%
					String message = (String) request.getAttribute("merchant.profile.update.success");
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
					message = (String) request.getAttribute("merchant.profile.update.failed");
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
					message = (String) request.getAttribute("merchant.email.invalid");
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
					message = (String) request.getAttribute("merchant.email.exist");
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
					message = (String) request.getAttribute("common.not.updated");
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
					message = (String) request.getAttribute("password.change.success");
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
					message = (String) request.getAttribute("merchant.email.invalid");
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
					message = (String) request.getAttribute("merchant.email.exist");
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
					message = (String) request.getAttribute("merchant.mobile.no.already.exist");
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
					message = (String) request.getAttribute("merchant.mobile.no.length");
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