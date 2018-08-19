<%@ include file="merchant-sandbox-header.jsp"%>

<div class="sandBoxWrapper">
	<div class="row brdr-btm">
		<div class="col-md-10 col-sm-10 col-xs-12">
			<h2>Email Invoicing</h2>
		</div>
		<%-- <div class="col-md-2 col-sm-2 col-xs-12">
			<ul>
				<li><a href="javascript:void(0)"><span><span><img
								src="<%=basePath%>resources/images/white-settings.png" /></span></span></a></li>
			</ul>
		</div> --%>
	</div>
	<form action="email-invoicing" method="post">
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
						type="cName" class="form-control customInput required" id="email"
						placeholder="First Name" name="firstName" value="${emailInvoicingModel.firstName }" tabindex="1">
				</div>
			</div>
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/e2.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Email Id" name="emailId" value="${emailInvoicingModel.emailId }" tabindex="3">
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/e1.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Last Name" name="lastName" value="${emailInvoicingModel.lastName }" tabindex="2">
				</div>
			</div>
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i4.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Mobile Number" name="mobileNumber" value="${emailInvoicingModel.mobileNumber }" tabindex="4">
				</div>
			</div>
		</div>
		<div class="col-md-12 vertpad">
			<h5>
				<b>From :</b> ${merchantModel.getEmailId()}
			</h5>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i3.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Invoice No" name="invoiceNo" value="${emailInvoicingModel.invoiceNo }" tabindex="5">
				</div>
			</div>
		</div>
		<div class="col-md-6 col-sm-12 col-xs-12">
			<div class="form-group">
				<div class="customBox">
					<span class="iArea"><img
						src="<%=basePath%>resources/images/i3.png"></span> <input
						type="cName" class="form-control customInput required" id="email"
						placeholder="Type" name="type" value="${emailInvoicingModel.type }" tabindex="6">
				</div>
			</div>
		</div>
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
								type="cName" class="form-control customInput required"
								id="email" placeholder="Amount" name="amount" value="${emailInvoicingModel.amount }" tabindex="7">
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
									<option value="BDT">BDT</option>
									<%-- <option value="<span>&#2547;</span>">BDT</option> --%>
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
								id="comment" placeholder="Description" name="description" tabindex="9">${emailInvoicingModel.description }</textarea>
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
								type="cName" class="form-control customInput required"
								id="email" placeholder="Subject" name="subject" value="${emailInvoicingModel.subject }" tabindex="8">
						</div>
					</div>
				</div>
				<!-- <div class="col-md-12 col-sm-12 col-xs-12">
					<h5>
						<b>Reference Key :</b> Pluggers
					</h5>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12">
					<label class="checkbox-inline"><input type="checkbox"
						value="">Email</label> <label class="checkbox-inline"><input
						type="checkbox" value="">SMS</label>
				</div> -->
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
</script>

<%
					String message = (String) request.getAttribute("invoice.send.success");
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
					message = (String) request.getAttribute("invoice.send.failed");
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
					message = (String) request.getAttribute("invoiceno.required");
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
