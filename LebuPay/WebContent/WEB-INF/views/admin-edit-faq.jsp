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
								src="<%=basePath%>resources/images/net-banking.png" /></span><a>Edit
								FAQ</a>
						</h4>
					</div>
					<form method="post" action="edit-faq">
						<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
						<div class="panel-body">


							<div class="row">
								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png"></span> <input
												class="form-control customInput required" name="question"
												id="question" placeholder="FAQ Question"
												value="${faqModel.question }" field-name="FAQ Question"
												data-valied="blank">
										</div>
									</div>
									<div class="form-group">
										<div class="customBox textArea">
											<span class="iArea"><img
												src="http://localhost:7080/LebuPay/resources/images/c2.png"></span>
											<textarea class="form-control customInput required"
												field-name="FAQ Answer" data-valied="blank" rows="5"
												name="answer" placeholder="FAQ Answer">${faqModel.answer }</textarea>
										</div>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="http://localhost:7080/LebuPay/resources/images/i1.png"></span>
											<div class="custom-dropdown-menu">
												<select name="typeModel.typeId">
													<c:choose>
														<c:when test="${faqModel.typeModel.typeId == 2 }">
															<!-- <option value="2" selected="selected">Customer</option> -->
															<option value="1">Merchant</option>
															<option value="6">Others</option>
														</c:when>
														<c:when test="${faqModel.typeModel.typeId == 1 }">
															<!-- <option value="2">Customer</option> -->
															<option value="1" selected="selected">Merchant</option>
															<option value="6">Others</option>
														</c:when>
														<c:when test="${faqModel.typeModel.typeId == 6 }">
															<!-- <option value="2">Customer</option> -->
															<option value="1">Merchant</option>
															<option value="6" selected="selected">Others</option>
														</c:when>
													</c:choose>

												</select>
											</div>
										</div>
									</div>


									<div class="form-group">

										<label>Select Status</label> <select class="select"
											name="status" id="status">


											<c:forEach items="${statusDetails}" var="eStatus">

												<option value="${eStatus}"
													<c:out value="${faqModel.status == eStatus ? 'selected': ''}"/>><c:out
														value="${eStatus}" /></option>
											</c:forEach>

										</select>

									</div>

								</div>

							</div>

							<input type="hidden" name="faqId" value="${faqModel.faqId}" />

							<div class="row  m-t-30">
								<div class="col-md-12 col-xs-12">
									<div class="drop"></div>

								</div>
							</div>
							<div class="row  m-t-30">
								<div class="col-md-12 col-xs-12">
									<button type="submit" class="btn keyBtn">SUBMIT</button>
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
		$('form').submit(function(event){
			$('[data-valied]').each(function(){
				if($(this).val().trim() === ""){
					jQuery.growl.error({
						message : $(this).attr('field-name')+" Required"
					});
					event.preventDefault();
				}
			});
		});
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
					message = (String) request.getAttribute("faq.question.required");
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
					message = (String) request.getAttribute("faq.ans.required");
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
