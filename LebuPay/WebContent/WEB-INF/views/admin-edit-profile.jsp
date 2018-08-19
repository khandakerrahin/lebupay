<%@ include file="admin-header.jsp"%>

<!-- Main content -->
<section class="content marginBtm">
	<div class="sandBox bg-white">
		<div class="sandBoxWrapper newAcc">
			<div class="pad_20">
				<div class="row">
					<div class="col-lg-12">
						<h3>
							<a href="#"><img
								src="<%=basePath%>resources/images/user-profile.png" /></a> <span>Edit
								Profile</span>
						</h3>
					</div>
				</div>

				<form method="post" action="update-profile">
					<input type="hidden" name="csrfPreventionSalt"
						value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
					<div class="row commonCss m-t-20">
						<div class="col-md-6 col-sm-12 col-xs-12 withdrawCash">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										class="form-control customInput required"
										placeholder="First Name" name="firstName"
										value="${adminModel.getFirstName()}">
								</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-12 col-xs-12 current-balance">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/e1.png"></span> <input
										class="form-control customInput required"
										placeholder="Last Name" name="lastName"
										value="${adminModel.getLastName()}">
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
												src="<%=basePath%>resources/images/mobile.png"></span>
											<!-- <div class="select-add-on">
	                                     	<select>
	                                        	<option>India (91+)</option>
	                                            <option>Afghanistan (43+)</option>
	                                            <option>Albania (355+)</option>
	                                        </select>
	                                     </div> -->
											<%-- 	                                     <input class="form-control required" placeholder="Mobile Number" name="mobileNo" value="${adminModel.mobileNo}"> --%>
											<input class="form-control customInput required"
												placeholder="Mobile Number" name="mobileNo" readonly
												value="${fn:replace(adminModel.mobileNo, '+880', '')}">

										</div>
									</div>
								</div>
							</div>
						</div>

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
												src="<%=basePath%>resources/images/e2.png"></span> <input
												class="form-control customInput required"
												placeholder="Email Id" name="emailId"
												value="${adminModel.getEmailId()}" readonly>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-t-20">
						<div class="col-md-6 col-sm-12 updateNdPassword">
							<button type="submit" class="btn keyBtn bold-btn">UPDATE</button>
							<a class="changePass" href="./change-password">Change
								Password</a>
						</div>
					</div>

				</form>

			</div>

		</div>


	</div>



</section>
<!-- /.content -->
<%@ include file="admin-footer.jsp"%>


<%
					String message = (String) request.getAttribute("admin.profile.update.success");
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
					message = (String) request.getAttribute("admin.profile.update.failed");
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
					message = (String) request.getAttribute("admin.first.name.required");
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
					message = (String) request.getAttribute("admin.last.name.required");
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
					message = (String) request.getAttribute("admin.email.required");
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
					message = (String) request.getAttribute("admin.email.invalid");
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
					message = (String) request.getAttribute("admin.email.exist");
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
					message = (String) request.getAttribute("admin.mobileNo.invalid");
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
					message = (String) request.getAttribute("admin.mobileNo.exist");
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
					message = (String) request.getAttribute("admin.mobileNo.required");
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