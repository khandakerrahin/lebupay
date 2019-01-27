
<%@ include file="merchant-sandbox-header.jsp"%>

<div class="sandBox">
	<div class="sandBoxWrapper">
		<div class="row brdr-btm">
			<div class="col-md-6 col-sm-6 col-xs-6">
				<h2>Contact Customer Care</h2>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-6">
				<button type="button" class="btn keyBtn keyBtn2"
					onclick="window.location='view-ticket'">View Tickets</button>
			</div>
		</div>
		<div class="row customer_care">
			<div class="col-md-6 col-sm-6 col-xs-12">
				<div class="row">
					<div class="col-sm-6 col-xs-12">
						<h3>
							<b>Recipient :</b> support@lebupay.com
						</h3>
					</div>
					<div class="col-sm-6 col-xs-12">
						<h3>
							<b>From :</b> ${merchantModel.getEmailId()}
						</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 vertpad">
						<form action="customer-care" method="post">
							<input type="hidden" name="csrfPreventionSalt"
								value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/c1.png"></span> <input
										type="cName" class="form-control customInput required"
										name="subject" id="email" placeholder="Subject">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox textArea">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/c2.png"></span>
									<textarea class="form-control customInput required" rows="5"
										id="comment" placeholder="Message" name="ticketMessage"></textarea>
								</div>
							</div>
							<button type="submit" class="btn keyBtn">SEND</button>
							<button type="reset" class="btn keyBtn keyBtn2">RESET</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>




<%@ include file="merchant-sandbox-footer.jsp"%>

<%
						String message = (String) request.getAttribute("common.successfully");
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
						message = (String) request.getAttribute("database.not.found");
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
						message = (String) request.getAttribute("merchant.ticket.details.required");
					 
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
						message = (String) request.getAttribute("merchant.ticket.subject.required");
					
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
