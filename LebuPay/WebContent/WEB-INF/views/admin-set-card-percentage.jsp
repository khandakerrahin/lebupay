<%@ include file="admin-header.jsp"%>
<%@ page import="com.lebupay.model.*"%>
<%@ page import="java.util.*"%>
<% 
List<CardTypeModel> cardTypeModels = (List<CardTypeModel>) request.getAttribute("cardTypeModels");
List<MerchantCardPercentageModel> merchantCardPercentageModels = (List<MerchantCardPercentageModel>) request.getAttribute("merchantCardPercentageModels");
%>
<!-- Main content -->
<section class="content marginBtm">
<div class="row">
	<div class="col-md-12">
		<div class="adminformBox panelAdmin borderGreen">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<span><img src="<%=basePath%>resources/images/user.png" /></span><a>Set Card Percentage</a>
					</h4>
				</div>
				<div class="panel-body">
				<div class="row">
					<form method="post" action="admin-set-card-percentage" id="cardPercentageValidation">
						<input type="hidden" name="csrfPreventionSalt" value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
						<input type="hidden" name="merchantId" value="${merchantModel.merchantId }" />
					    <div class="item form-group">
						     <div class="row true_money_table_hdng">
						     <label class="col-md-4 col-xs-4 ">Card Type</label>
						     <label class="col-md-4 col-xs-4 ">%</label>
						     <label class="col-md-4 col-xs-4 ">Flat Fees</label>
						     </div>
						     <%
						    if(Objects.nonNull(cardTypeModels))
						    	 for(int i = 0; i < cardTypeModels.size(); i++) {
						 		
						     %>
						     <div class="row true_money_box card_percentage">
						     <% 
						     if (Objects.nonNull(merchantCardPercentageModels) && merchantCardPercentageModels != null && merchantCardPercentageModels.size() > 0) { 
						     	
						    	 if(String.valueOf(merchantCardPercentageModels.get(i).getCardTypeId()).equals(String.valueOf(cardTypeModels.get(i).getCardTypeId()))) {
						     %>
								<div class="col-md-4 col-xs-4 ">
									<label class="card_percentage_label"><%= cardTypeModels.get(i).getCardType() %></label>
									<input type="hidden" name="listCardTypeIds" value="<%
											if (Objects.nonNull(merchantCardPercentageModels) && merchantCardPercentageModels != null && merchantCardPercentageModels.size() > 0){
                                                   if(Objects.nonNull(merchantCardPercentageModels.get(i).getCardTypeId()))
                                                    	 out.println(merchantCardPercentageModels.get(i).getCardTypeId());
											} else {
                                                   out.println(cardTypeModels.get(i).getCardTypeId()); 
													}   %>">
								</div>
								<div class="col-md-4 col-xs-4">  
									<input class="form-control numbersOnly" name="listPercentage"  pattern="\d+(\.\d{1,2})?" required
										placeholder="Please enter Percentage" type="text"
										value="<%
												if (Objects.nonNull(merchantCardPercentageModels) && merchantCardPercentageModels != null && merchantCardPercentageModels.size() > 0){
	                                                   if(Objects.nonNull(merchantCardPercentageModels.get(i).getPercentage()))
	                                                    	 out.println(merchantCardPercentageModels.get(i).getPercentage());
											    	 } else {
					                              	   out.println("0");
												}
										%>">
								</div>
								<div class="col-md-4 col-xs-4 ">
									<input class="form-control numbersOnly" name="listFlatFees" pattern="\d+(\.\d{1,2})?" required
										placeholder="Please enter Flat Fees" type="text"
										value="<%
												if (Objects.nonNull(merchantCardPercentageModels) && merchantCardPercentageModels != null && merchantCardPercentageModels.size() > 0){
	                                                   if(Objects.nonNull(merchantCardPercentageModels.get(i).getFlatFees()))
	                                                    	 out.println(merchantCardPercentageModels.get(i).getFlatFees());
												} else {
	                                                	   out.println("0");
												}
										%>">
								</div>
							<% } } else { %>
								
								<div class="col-md-4 col-xs-4 ">
									<label class="card_percentage_label"><%= cardTypeModels.get(i).getCardType() %></label>
									<input type="hidden" name="listCardTypeIds" value="<%= cardTypeModels.get(i).getCardTypeId() %>">
								</div>
								<div class="col-md-4 col-xs-4">  
									<input class="form-control numbersOnly" name="listPercentage"  pattern="\d+(\.\d{1,2})?" required placeholder="Please enter Percentage" type="text" value="0">
								</div>
								<div class="col-md-4 col-xs-4 ">
									<input class="form-control numbersOnly" name="listFlatFees" pattern="\d+(\.\d{1,2})?" required placeholder="Please enter Flat Fees" type="text" value="0">
								</div>
								
							<% } %>
							</div>
						     
						     <% } %>
	                    </div>
						<div class="row  m-t-30">
							<div class="col-md-12 col-xs-12">
								<input type="submit" class="btn keyBtn" value="UPDATE">
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
<script src="<%=basePath%>resources/js/login/md5.js" type="text/javascript"></script>
<script src="<%=basePath%>resources/js/validation/cardValidation.js" type="text/javascript"></script>
<%
String message = (String) request.getAttribute("invalid.merchant");
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
message = (String) request.getAttribute("merchant.id.required");
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

<% message = (String) request.getAttribute("card.typeid.required");
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

<% message = (String) request.getAttribute("card.typeid.maxlength");
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

<% message = (String) request.getAttribute("card.typeid.specialChar");
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
	message = (String) request.getAttribute("percentage.maxlength");
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
	message = (String) request.getAttribute("percentage.specialChar");
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
	message = (String) request.getAttribute("percentage.required");
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
	message = (String) request.getAttribute("flatfee.maxlength");
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
	message = (String) request.getAttribute("flatfee.specialChar");
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
	message = (String) request.getAttribute("flatfee.required");
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
<% } %>

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
<% } %>

<%
	message = (String) request.getAttribute("flatfee.numeric");
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
	message = (String) request.getAttribute("percentage.numeric");
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
	message = (String) request.getAttribute("card.typeid.numeric");
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
	message = (String) request.getAttribute("cardflatfee.negative.error");
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
	message = (String) request.getAttribute("percentage.negative.error");
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
	message = (String) request.getAttribute("percentage.max.error");
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