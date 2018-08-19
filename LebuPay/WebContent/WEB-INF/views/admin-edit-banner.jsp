<%@ include file="admin-header.jsp"%>
<!-- Main content -->
<style type="text/css">
.upload-image-preview img {
  width: 50px;
  height: 50px;
}
</style>
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">
			<div class="adminformBox panelAdmin borderGreen">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<span><img
								src="<%=basePath%>resources/images/net-banking.png" /></span><a>Edit
								Banner</a>
						</h4>
					</div>
					<form method="post" action="edit-banner"
						enctype="multipart/form-data">
						<input type="hidden" name="csrfPreventionSalt"
							value="<%=request.getAttribute("csrfPreventionSaltPage")%>" /> <input
							type="hidden" name="imageName" value="${bannerModel.imageName}" />
						<input type="hidden" name="bannerId"
							value="${bannerModel.bannerId}" />
						<div class="panel-body">


							<div class="row">
								<div class="col-md-6 col-sm-6 col-xs-12">

									<div class="form-group">
										<label class="input-group-btn uploadBtn"
											style="display: inline; vertical-align: middle"> <span
											class="btn btn-file btn-warning" style=""> Browse
												Image<input name="file" id="file" type="file"
												onchange="ValidateSingleInput(this);" style="display: none;"
												accept="image/x-png,image/gif,image/jpeg,image/jpg,image/bmp" />
										</span>
										</label>
										<div
											style="display: block; font-size: 12px; margin-top: 10px;">(Accepts
											only *.jpg, *.jpeg, *.bmp, *.gif, *.png) (File Size Limit
											500KB)</div>
											 <div class="upload-image-preview"></div>
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


									<div class="form-group">

										<label>Select Status</label> <select class="select"
											name="status" id="status">


											<c:forEach items="${statusDetails}" var="eStatus">

												<option value="${eStatus}"
													<c:out value="${bannerModel.status == eStatus ? 'selected': ''}"/>><c:out
														value="${eStatus}" /></option>
											</c:forEach>

										</select>

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
var _validFileExtensions = [ ".jpg", ".jpeg", ".bmp", ".gif", ".png" ];
function ValidateSingleInput(oInput) {
	if (oInput.type == "file") {
		var sFileName = oInput.value;
		if (sFileName.length > 0) {
			var blnValid = false;
			var fileUpload = document.getElementById("logo");
			var size = parseFloat(fileUpload.files[0].size / 1024).toFixed(2);
			            //alert(size + " KB.");
			if(size > 500) {
				
				 jQuery.growl.error({
						message : "Please Select Image File Within 500KB"
									});
				 return false;
			}
			            
			for (var j = 0; j < _validFileExtensions.length; j++) {
				var sCurExtension = _validFileExtensions[j];
				if (sFileName.substr(sFileName.length - sCurExtension.length,
						sCurExtension.length).toLowerCase() == sCurExtension
						.toLowerCase()) {
					blnValid = true;
					break;
				}
			}

			if (!blnValid) {
				
				 jQuery.growl.error({
						message : "Please Select Proper Image File"
									});
				oInput.value = "";
				return false;
			}
		}
	}
	return true;
}

</script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("input[name=file]").change(function () {
		    if (this.files && this.files[0]) {
		        var reader = new FileReader();

		        reader.onload = function (e) {
		            var img = $('<img>').attr('src', e.target.result);
		            $('.upload-image-preview').html(img);
		        };

		        reader.readAsDataURL(this.files[0]);
		    }
		});
		
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
