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
								Content Management</a>
						</h4>
					</div>
					<form method="post" action="add-content-management">
						<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
						<div class="panel-body">


							<div class="row">
								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/a3.png"></span> <input
												class="form-control customInput required" name="path"
												id="question" placeholder="Path" field-name="Path"
												value="${contentModel.path}" data-valied="blank">
										</div>
									</div>
									<div class="form-group">
										<%-- <div class="customBox textArea">
											<span class="iArea"><img
												src="<%=basePath%>resources/images/c2.png"></span>
											<!-- <textarea class="form-control customInput required"
												field-name="Content" data-valied="blank" rows="5"
												name="content" placeholder="Content"></textarea> -->
												<textarea  name="content" id="content" class="form-control customInput required"></textarea>
										</div> --%>

										<textarea name="content" id="content"
											class="form-control customInput required"
											placeholder="Content" field-name="Content">${contentModel.content}</textarea>
									</div>

									<div class="form-group">
										<div class="customBox">
											<span class="iArea"><img
												src="http://localhost:7080/LebuPay/resources/images/i1.png"></span>
											<div class="custom-dropdown-menu">
												<select name="typeModel.typeId">
													<!-- <option value="2">Customer</option> -->
													<option value="1">Merchant</option>
													<option value="6">Others</option>
												</select>
											</div>
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

<script>
/**
 * use to load cke editor
 */
	initSample();	
</script>

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
					message = (String) request.getAttribute("content.path.required");
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
					message = (String) request.getAttribute("content.content.required");
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
