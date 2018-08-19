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
								Merchant</a>
						</h4>
					</div>

					<div class="panel-body">
						<div class="row">


							<form method="post" action="edit-merchant">
								<input type="hidden" name="csrfPreventionSalt"
									value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />


								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/ficon1.png"></span> <input
												class="form-control customInput required" name="firstName"
												value="${merchantModel.firstName}" id="firstname"
												placeholder="First Name">
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/e2.png"></span> <input
												class="form-control customInput required" id="mobileno"
												name="mobileNo" value="${merchantModel.mobileNo}"
												placeholder="Mobile No.">
										</div>
									</div>

									<%-- <div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/ficon1.png"></span> <input
												class="form-control customInput" name="password"
												type="hidden" value="${merchantModel.password }" /> <input
												class="form-control customInput" type="password"
												placeholder="Password" />
										</div>
									</div> --%>

									<div class="form-group">

										<label>Select Status</label> <select class="select"
											name="status" id="status">


											<c:forEach items="${statusDetails}" var="eStatus">
												<c:if test= "${eStatus != 'ACTIVATED'}">
													<option value="${eStatus}"
														<c:out value="${merchantModel.status == eStatus ? 'selected': ''}"/>>
														
														<c:out value="${eStatus}" />
													</option>
												</c:if>
											</c:forEach>

										</select>

									</div>

								</div>


								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/ficon1.png"></span> <input
												class="form-control customInput required" id="lastName"
												name="lastName" value="${merchantModel.lastName}"
												placeholder="Last Name">
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/e2.png"></span> <input
												class="form-control customInput required" id="email"
												name="emailId" value="${merchantModel.emailId}"
												placeholder="Email">
										</div>
									</div>

								</div>

								<input type="hidden" name="merchantId"
									value="${merchantModel.merchantId }" />


								<div class="row  m-t-30">
									<div class="col-md-12 col-xs-12">
										<button type="submit" class="btn keyBtn">SUBMIT</button>
									</div>
								</div>

							</form>

						</div>


					</div>

				</div>
			</div>
		</div>
	</div>
</section>
<!-- /.content -->

<%@ include file="admin-footer.jsp"%>

<script src="<%=basePath%>resources/js/login/md5.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('form[action="edit-merchant"]').submit(function(){
			$('[type="password"]').each(function(){
				if($(this).val().trim() !== ''){
					var encData = md5($(this).val());
					$('[name="password"][type="hidden"]').val(encData);
					$(this).val(encData);
				}
			});
		});
	});
	</script>

<%
					String message = (String) request.getAttribute("merchant.first.name.required");
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
					message = (String) request.getAttribute("status.required");
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