<%@ include file="admin-header.jsp"%>

<!-- Main content -->
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">
			<div class="adminformBox panelAdmin borderGreen">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<span><img
								src="<%=basePath%>resources/images/net-banking.png" /></span><a>Add
								Amount For Point</a>
						</h4>
					</div>
					<form method="post" action="point-to-amount" id="pointToAmount">
						<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" /> <input
							type="hidden" name="loyaltyId" value="${loyaltyModel.loyaltyId}">

						<div class="panel-body">


							<div class="row">
								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png"></span> <input
												class="form-control customInput required" name="amount"
												id="amount" placeholder="Amount"
												value="${loyaltyModel.amount}" field-name="Amount"
												data-valied="blank">
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png"></span> <input
												class="form-control customInput required" name="point"
												id="point" placeholder="Point" value="${loyaltyModel.point}"
												field-name="Point" data-valied="blank">
										</div>
									</div>

								</div>

							</div>

							<div class="row  m-t-30">
								<div class="col-md-12 col-xs-12">
									<div class="drop"></div>

								</div>
							</div>
							<div class="row  m-t-30">
								<div class="col-md-12 col-xs-12">
									<input type="submit" class="btn keyBtn" value="SUBMIT">
								</div>
							</div>

						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</section>
<!-- /.content -->

<%@ include file="admin-footer.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
/* 		$('form').submit(function(event){
			$('[data-valied]').each(function(){
				if($(this).val().trim() === ""){
					jQuery.growl.error({
						message : $(this).attr('field-name')+" Required"
					});
					event.preventDefault();
				}
			});
		}); */
	});
</script>
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
					message = (String) request.getAttribute("loyalty.amount.required");
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
					message = (String) request.getAttribute("loyalty.point.required");
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
					message = (String) request.getAttribute("common.message");
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
message = (String) request.getAttribute("loyalty.amount.valid");
	if (message != null) {
%>
	<script>
		 $(document).ready(function() {
		 jQuery.growl.error({
				message : "<%=message%>"
							});
						});
	</script>
<% } %>

<%
message = (String) request.getAttribute("loyalty.point.valid");
	if (message != null) {
%>
	<script>
		 $(document).ready(function() {
		 jQuery.growl.error({
				message : "<%=message%>"
							});
						});
	</script>
<% } %>
					