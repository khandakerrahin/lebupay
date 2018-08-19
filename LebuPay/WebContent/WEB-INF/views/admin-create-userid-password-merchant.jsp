<%@ include file="admin-header.jsp"%>

<!-- Main content -->
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">
			<div class="adminformBox panelAdmin borderGreen">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<span><img src="<%=basePath%>resources/images/user.png" /></span><a>Merchant's Bank Details
								</a>
						</h4>
					</div>

					<div class="panel-body">
						<div class="row">


							<form method="post" action="create-userid-password">
								<input type="hidden" name="csrfPreventionSalt"
									value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />


								<div class="col-md-6 col-sm-6 col-xs-12">
								<label>EBL Details:</label>
								<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png"></span> <input
												class="form-control customInput required" name="eblId"
												id="eblId" placeholder="EBL ID" value="${merchantModel.eblId}">
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/ficon1.png"></span> <input
												class="form-control customInput required" name="eblUserName"
												value="${merchantModel.eblUserName}" id="eblUserName"
												placeholder="EBL User Name">
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/ficon2.png" /></span> <input
												type="password" class="form-control customInput" id="eblPassword"
												placeholder="EBL Password" name="eblPassword" value="${merchantModel.eblPassword}">
										</div>
									</div>
									
									<label>CityBank Details:</label>
									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png" /></span> <input
												 class="form-control customInput" id="cityMerchantId"
												placeholder="Merchant ID" name="cityMerchantId" value="${merchantModel.cityMerchantId}" >
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


<%
					String message = (String) request.getAttribute("ebl.id.required");
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
					  message = (String) request.getAttribute("ebl.user.name.required");
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
					  message = (String) request.getAttribute("ebl.password.required");
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

