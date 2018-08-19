
<%@ include file="admin-header.jsp"%>

<!-- header start -->

<script src="<%=basePath%>resources/js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>resources/js/dataTables.buttons.min.js"></script>
<script src="<%=basePath%>resources/js/buttons.flash.min.js"></script>
<script src="<%=basePath%>resources/js/jszip.min.js"></script>
<script src="<%=basePath%>resources/js/pdfmake.min.js"></script>
<script src="<%=basePath%>resources/js/vfs_fonts.js"></script>
<script src="<%=basePath%>resources/js/buttons.html5.min.js"></script>
<script src="<%=basePath%>resources/js/buttons.print.min.js"></script>
<script src="<%=basePath%>resources/js/dataTables.rowReorder.min.js"></script>
<script src="<%=basePath%>resources/js/dataTables.responsive.min.js"></script>





<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/css/jquery.dataTables.min.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/css/rowReorder.dataTables.min.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/css/responsive.dataTables.min.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/css/buttons.dataTables.min.css" />
<!-- header end -->

<!-- Main content -->
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">



			<div class="adminformBox tableWrap bordergrey">
				<div class="panel panel-default">
					<div class="panel-heading bg-light-grey">
						<div class="row">
							<div class="col-md-3 col-sm-4 col-xs-12">
								<div class="input-group stylish-input-group">
									<input class="form-control mySearch" aria-controls="subAdmin"
										placeholder="Search" type="search"> <span
										class="input-group-addon">
										<button type="submit">
											<span><i class="fa fa-search"></i></span>
										</button>
									</span>
								</div>

							</div>

							<div class="col-md-9 col-sm-8 col-xs-12 text-right">
								<div class="head">
									<h2>Show Records</h2>
								</div>
								<div class="drop">
									<select name="subAdmin_length" aria-controls="subAdmin"
										class="mySelectOpt"><option value="10">10</option>
										<option value="25">25</option>
										<option value="50">50</option></select>
								</div>

								<a class="btn btn-success myExcelDownload" tabindex="0"
									aria-controls="subAdmin" href="#"><span>Excel</span></a>
							</div>

						</div>
					</div>

					<div class="panel-body">
						<table id="merchant" class="table display" cellspacing="0"
							width="100%" style="width: 100%;">
							<thead>
								<tr>
									<th>TICKET ID</th>
									<th>SUBJECT</th>
									<th>MESSAGE</th>
									<th>REPLY</th>
									<!-- <th>STATUS</th> -->
									<th class="datatableLinkButton">ACTION</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ticketModel" items="${ticketModels }"
									varStatus="loop">
									<tr class="${((loop.index +1)%2)==0?'odd':'even'}">
										<td>${ticketModel.ticketId }</td>
										<td>${ticketModel.subject }</td>
										<td>${ticketModel.ticketMessage }</td>
										<td>${ticketModel.reply }</td>
										<%-- <td>${ticketModel.status }</td> --%>
										<td><a href="" class="reply" data-url="reply-ticket"
											data-target="ticketId"><i class="fa fa-pencil-square-o"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</div>
				</div>
			</div>
		</div>
</section>
<!-- /.content -->
<%@ include file="admin-footer.jsp"%>

<!-- DELETE PARAMETER FORM START -->
<div id="confirm" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- EDIT PARAMETER CONTENT-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Delete Merchant</h4>
			</div>
			<form>
				<div class="modal-body">
					<input type="hidden" />
					<div id="message">You want to delete this Merchant?</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary" id="delete">Delete</button>
					<button type="button" data-dismiss="modal" class="btn">Cancel</button>
				</div>
			</form>
		</div>

	</div>
</div>
<!-- DELETE PARAMETER FORM END -->

<script src=".././resources/js/list-merchant.js"></script>
<script src=".././resources/js/JQDataTable.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var urlConfigObj = {"noOfPages":2, "url": "list-ticket-merchant", "method":"POST"};
		// it can autometicall set mobileNumber to mobile_number if other mapping is there then have to mention
        //var columnsVSTable = {"mobileNumber":"mobile_no"};
        var columnsVSTable = {"ticketMessage":"message"};
        var columns = [
		 { "data": "ticketId", "defaultContent": "", "visible": false },
         { "data": "subject", "defaultContent": "" },
         { "data": "ticketMessage", "defaultContent": "" },
         { "data": "reply", "defaultContent": "" },
        /*  { "data": "status", "defaultContent": "" }, */
         {
        	 "data": null,
             className: "center",
             defaultContent: '<a href="" class="reply" data-url="reply-ticket" data-target="ticketId"><i class="fa fa-pencil-square-o"></i></a>'
         }
        ];
        
        drawDataTable($("#merchant"), urlConfigObj, columns, columnsVSTable, true, null, null, ${totalSize }, "downloadListTicketMerchantExcel", null, 'rtp', $(".mySearch"), $(".myExcelDownload"), $(".mySelectOpt"));
	} );
	</script>
<%
					String message = (String) request.getAttribute("common.updated");
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
					message = (String) request.getAttribute("common.deleted");
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
					message = (String) request.getAttribute("common.not.deleted");
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
