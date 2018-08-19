<%@ include file="merchant-header.jsp"%>

<!-- Main content -->
<section class="content-wrapper res-m-t">
	<div class="sandBox bg-white">
		<div class="sandBoxWrapper newAcc">
			<div class="pad_20">
				<div class="row">
					<div class="col-lg-12">
						<h3>
							<%--                     	<a href="#"><img src="<%=basePath%>resources/images/user-profile.png"/></a> --%>
							<span>Change Password</span>
						</h3>
					</div>
				</div>

				<form method="post" action="./change-password">
					<input type="hidden" name="csrfPreventionSalt"
						value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
					<div class="row commonCss m-t-20">

						<div class="col-md-6 col-sm-12 col-xs-12 withdrawCash">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										class="form-control customInput required"
										placeholder="Old Password" name="password" type="password">
								</div>
								<div class="infoText">Please enter your old password</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-12 col-xs-12 withdrawCash">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										class="form-control customInput required"
										placeholder="New Password" name="confirmPassword"
										type="password">
								</div>
								<div class="infoText">Password length should be within
									6-20 including at least one number, one lowercase and uppercase
									character, one special character. (Eg:- Wb07f8591@)</div>
							</div>
						</div>

					</div>

					<div class="row commonCss m-t-20">

						<div class="col-md-6 col-sm-12 col-xs-12 withdrawCash">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										class="form-control customInput required"
										placeholder="Confirm New Password" name="confirmNewPassword"
										type="password">
								</div>
								<div class="infoText">Password length should be within
									6-20 including at least one number, one lowercase and uppercase
									character, one special character. (Eg:- Wb07f8591@)</div>
							</div>
						</div>

					</div>

					<div class="row m-t-20">
						<div class="col-md-6 col-sm-12 updateNdPassword">
							<button type="submit" class="btn keyBtn bold-btn">UPDATE</button>
						</div>
					</div>

				</form>

			</div>

		</div>


	</div>



</section>
<!-- /.content -->
<%@ include file="merchant-footer.jsp"%>


<script type="text/javascript">
 
 	$(document).ready(function() {
		$('form').submit(function(event){
			$('input[type="password"]').each(function(){
				var isError = false;
				if($(this).val().trim() == ""){
					jQuery.growl.error({
						message : "*"+$(this).attr('placeholder')+" should be required"
					});
					isError = true;
					event.preventDefault();// not submitted
				}
				/* if(!isError){
					$(this).val(md5($(this).val()));
				} */
			});
		});
	});
 </script>




<%
					String message = (String) request.getAttribute("common.error");
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
<%
						}
			%>


<%
					 message = (String) request.getAttribute("merchant.old.password.required");
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
					 message = (String) request.getAttribute("merchant.password.wrong");
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
					 message = (String) request.getAttribute("merchant.new.password.required");
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
					 message = (String) request.getAttribute("merchant.confirm.password.required");
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
					 message = (String) request.getAttribute("merchant.password.and.confirm-password.not.matching");
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
					 message = (String) request.getAttribute("merchant.password.format");
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
