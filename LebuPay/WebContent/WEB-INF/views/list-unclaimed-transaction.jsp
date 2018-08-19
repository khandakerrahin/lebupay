
<%@ include file="admin-header.jsp"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/jquery.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/rowReorder.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/responsive.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/buttons.dataTables.min.css" />
<!-- header end -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt"%>
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
									<th>TRANSACTION ID</th>
									<th>FIRST NAME</th>
									<th>NAME</th>
									<th>EMAIL ID</th>
									<th>MOBILE</th>
									<th>EBL ID</th>
									<!-- <th>EBL USERNAME</th> -->
									<th>BALANCE AMOUNT</th>
									<th>LOYALTY POINT</th>
									<th>TXN AMOUNT</th>
									<th>PAYABLE AMOUNT</th>
									<th>TXN ID</th>
									<th>TXN DATE</th>
									<!-- <th>STATUS</th> -->
									<!-- <th class="datatableLinkButton">ACTION</th> -->
								</tr>
							</thead>
							<tbody>
								<c:forEach var="transactionModel" items="${transactionModels }"
									varStatus="loop">
									<tr class="${((loop.index +1)%2)==0?'odd':'even'}">
										<td>${transactionModel.transactionId }</td>
										<td>${transactionModel.merchantModel.firstName}</td>
										<td>${transactionModel.merchantModel.lastName }</td>
										<td>${transactionModel.merchantModel.emailId}</td>
										<td>${transactionModel.merchantModel.mobileNo}</td>
										<td>${transactionModel.merchantModel.eblId}</td>
										<%-- <td>${transactionModel.merchantModel.eblUserName}</td> --%>
										<td><fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${transactionModel.balance }" /></td>
										<td>${transactionModel.loyaltyPoint }</td>
										<td>${transactionModel.grossAmount }</td>
										<td>${transactionModel.amount }</td>
										<td>${transactionModel.txnId }</td>
										<td>${transactionModel.createdDate }</td>
										<%-- <td>${transactionModel.status }</td> --%>
										<!-- <td><a href="" class="edit" data-url="edit-merchant"
											data-target="merchantId"><i class="fa fa-pencil-square-o"></i></a>
											<a href="" data-url="delete-sub-merchant"
											data-target="merchantId" class="remove"><i
												class="fa fa-trash-o"></i></a></td> -->
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
		var urlConfigObj = {"noOfPages":2, "url": "list-unclaimed-merchant", "method":"POST"};
		// it can autometicall set mobileNumber to mobile_number if other mapping is there then have to mention
        //var columnsVSTable = {"mobileNumber":"mobile_no"};
         var columnsVSTable = {"transactionId": "TRANSACTION_ID","grossAmount":"tm.GROSS_AMOUNT","merchantModel.emailId":"m.EMAIL_ID","merchantModel.mobileNo":"m.MOBILE_NO","merchantModel.eblId":"m.EBL_ID"};
        var columns = [
		  { "data": "transactionId", "defaultContent": "", "visible": false },
		 { "data": "merchantModel.firstName", "defaultContent": "", "visible": false },
		 { "data": "merchantModel.lastName", "defaultContent": "" },
		 { "data": "merchantModel.emailId", "defaultContent": "" },
		 { "data": "merchantModel.mobileNo", "defaultContent": "" },
		 { "data": "merchantModel.eblId", "defaultContent": "" },
		// { "data": "merchantModel.eblUserName", "defaultContent": "" },
		 { "data": "balance", "defaultContent": "" },
		 { "data": "loyaltyPoint", "defaultContent": "" },
		 { "data": "grossAmount", "defaultContent": "" },
         { "data": "amount", "defaultContent": "" },
         { "data": "txnId", "defaultContent": "" },
         { "data": "createdDate", "defaultContent": "" }
        /*  { "data": "status", "defaultContent": "" } */
         /* {
        	 "data": null,
             className: "center",
             defaultContent: '<a href="" class="edit" data-url="edit-merchant" data-target="merchantId"><i class="fa fa-pencil-square-o"></i></a>   <a href="" data-url="delete-merchant" data-target="merchantId" class="remove"><i class="fa fa-trash-o"></i></a>'
         } */
        ];
        
        function changeActiveDeactive( row, data, index ){
       	 // Output the data for the visible rows to the browser's console
        	 $(row).children(':eq(0)').html(data.merchantModel.firstName + ' ' + data.merchantModel.lastName);
        	 /*var dateParts = data.createdDate.split('#');
        	$(row).children(':eq(7)').html(dateParts[0] + ', ' + dateParts[1]); */
       }
        
        drawDataTable($("#merchant"), urlConfigObj, columns, columnsVSTable, true, null, null, ${totalSize }, "downloadListUnclaimedMerchantExcel", changeActiveDeactive, 'rtp', $(".mySearch"), $(".myExcelDownload"), $(".mySelectOpt"));
        //drawDataTable($("#submerchant"), urlConfigObj, columns, columnsVSTable, true);
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
