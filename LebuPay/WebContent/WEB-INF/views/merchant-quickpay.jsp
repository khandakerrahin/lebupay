<%@ include file="merchant-sandbox-header.jsp"%>

<div class="panel-group customAccordian" id="accordion">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title" data-toggle="collapse"
				data-parent="#accordion" href="#collapse1">
				<a>Create an Individual link</a>
			</h4>
		</div>
		<div id="collapse1" class="panel-collapse collapse in">
			<div class="panel-body">
				<div class="row">
					<form action="add-quickpay" method="post">
						<div class="col-md-6 col-sm-6 col-xs-12">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/i1.png"></span> <input
										type="cName" class="form-control customInput required"
										id="email" placeholder="Product SKU" name="productSKU" tabindex="1">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/i2.png"></span> <input
										type="cName" class="form-control customInput required"
										id="email" placeholder="Amount" name="orderAmount" tabindex="3">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/i3.png"></span> <input
										type="cName" class="form-control customInput required"
										id="email" placeholder="Custom attribute 2" name="custom2" tabindex="5">
								</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-12">
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/ficon1.png"></span> <input
										type="cName" class="form-control customInput required"
										id="name" placeholder="Name" name="productName" tabindex="2">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/i4.png"></span> <input
										type="cName" class="form-control customInput required"
										id="email" placeholder="Custom attribute 1" name="custom1" tabindex="4">
								</div>
							</div>
							<div class="form-group">
								<div class="customBox">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/i3.png"></span> <input
										type="cName" class="form-control customInput required"
										id="email" placeholder="Custom attribute 3" name="custom3" tabindex="6">
								</div>
							</div>
						</div>
						<div class="col-md-3 col-xs-6">
							<button type="submit" class="btn keyBtn">PREVIEW</button>
						</div>
					</form>
					<c:if test="${generatedHTML != null }">
						<div class="col-md-12 col-md-12 col-sm-12 col-xs-12 m-t-20">
							<div class="form-group">
								<div class="customBox textArea">
									<span class="iArea"><img
										src="<%=basePath%>resources/images/c2.png"></span>
									<textarea class="form-control customInput" rows="5"
										id="comment" placeholder="Description" readonly="readonly">${generatedHTML}</textarea>
								</div>
							</div>
						</div>
					</c:if>
				</div>



			</div>
		</div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title collapsed" data-toggle="collapse"
					data-parent="#accordion" href="#collapse2">
					<a>Create bulk links</a>
				</h4>
			</div>
			<div id="collapse2" class="panel-collapse collapse">
				<div class="panel-body">
					<div class="row">
						<!-- 					<form method="POST" action="upload-csv" enctype="multipart/form-data">
						<div class="col-md-12">
							<div class="custom-file-upload">
								<label class="upload">Upload File:</label>&nbsp;
								<input type="file" id="file" class="customFile" name="file"/>
								<a class="choosenLink" href="download-instabuy">(Download sample csv file format)</a>
							</div>
						</div>
						<div class="col-md-12 vertpad">
							<button type="button" class="btn keyBtn keyBtn2">CREATE
								LINKS</button>
						</div>
					</form> -->

						<form id="fileUp" method="POST" action="upload-csv"
							enctype="multipart/form-data">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="form-group custom-file-upload resfile">

									<label class="upload">Upload File:</label>&nbsp;<input
										type="file" name="file" class="newfile" onchange="ValidateSingleInput(this);">
									<div class="input-group col-md-10 col-sm-12 col-xs-12">
										<input type="text" class="form-control file-upload-input"
											disabled placeholder="Upload CSV In Given Format"
											id="fileUpload"> <span class="input-group-btn">
											<button class="browse btn btn-primary file-upload-button"
												type="button">Browse</button>
										</span> <a class="choosenLink" href="download-quickpay">(Download
											sample csv file format)</a>
									</div>
								</div>
							</div>
							<div class="col-md-12 vertpad">
								<button type="submit" class="btn keyBtn keyBtn2">CREATE
									LINKS</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@ include file="merchant-sandbox-footer.jsp"%>


	<script type="text/javascript">
 			$(document).ready(function() {
						$('#fileUp').submit(function(event){
			
						var fileUpload = $('#fileUpload').val().trim();
						
							if(fileUpload == ''){
								jQuery.growl.error({
									message : "* Please select upload file"
								});
								event.preventDefault();// not submitted
							}
			
						});
			});
 </script>


	<%
						String message = (String) request.getAttribute("quickpay.product.sku.required");
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
						message = (String) request.getAttribute("quickpay.product.name.required");
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
						message = (String) request.getAttribute("csv.format.mismatch");
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
						message = (String) request.getAttribute("quickpay.order.amount.required");
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
						message = (String) request.getAttribute("quickpay.order.amount.not.number");
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
					<script>
var _validFileExtensions = [".csv"];    
function ValidateSingleInput(oInput) {
    if (oInput.type == "file") {
        var sFileName = oInput.value;
         if (sFileName.length > 0) {
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions.length; j++) {
                var sCurExtension = _validFileExtensions[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                    blnValid = true;
                    break;
                }
            }
             
            if (!blnValid) {
                alert("Sorry, allowed extensions is: " + _validFileExtensions.join(", "));
                oInput.value = "";
                return false;
            }
        }
    }
    return true;
}
</script>