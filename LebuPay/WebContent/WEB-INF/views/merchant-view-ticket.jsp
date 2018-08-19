<%@ include file="merchant-sandbox-header.jsp"%>
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
<section class="content marginBtm">
	<div class="row">
		<div class="col-md-12">



			<div class="adminformBox tableWrap bordergrey">
				<div class="panel panel-default">
					<!-- changes html start -->
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

					<!-- changes html end -->

					<div class="panel-body">

						<table id="merchantViewTicket" class="display table"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>TICKET SUBJECT</th>
									<th>TICKET DETAILS</th>
									<th>TICKET REPLY</th>
									<th>STATUS</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ticketModel" items="${ticketModels }"
									varStatus="loop">
									<tr class="${((loop.index +1)%2)==0?'odd':'even'}">
										<td>${ticketModel.subject }</td>
										<td>${ticketModel.ticketMessage }</td>
										<td>${ticketModel.reply }</td>
										<td>${ticketModel.status }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>


					</div>

					<!--  <div class="paginationWrap">
                         	<ul class="pagination">
                          		<li><a href="#"><i class="fa fa-angle-double-left"></i></a></li>
                          		<li class="active"><a href="#">1</a></li>
                          		<li><a href="#">2</a></li>
                          		<li><a href="#">3</a></li>
                          		<li><a href="#"><i class="fa fa-angle-double-right"></i></a></li>
                        	</ul>
                            </div> -->
				</div>
			</div>
		</div>
	</div>
</section>


<!-- DELETE PARAMETER FORM START -->
<div id="confirm" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- EDIT PARAMETER CONTENT-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Delete Sub Merchant</h4>
			</div>
			<form action="delete-sub-merchant" method="post">
				<div class="modal-body">
					<input type="hidden" name="merchantId" />
					<div id="message">You want to delete this Sub Merchant?</div>
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

<%@ include file="merchant-sandbox-footer.jsp"%>

<script src="<%=basePath%>resources/js/clear_modal.js"></script>

<script src=".././resources/js/JQDataTable.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var urlConfigObj = {"noOfPages":2, "url": "view-ticket", "method":"POST"};
		// it can autometicall set mobileNumber to mobile_number if other mapping is there then have to mention
        //var columnsVSTable = {"mobileNumber":"mobile_no"};
        var columnsVSTable = {"ticketSubject":"subject", "ticketReply":"reply"};
        var columns = [
         { "data": "subject", "defaultContent": "" },
         { "data": "ticketMessage", "defaultContent": "" },
         { "data": "reply", "defaultContent": "" },
         { "data": "status", "defaultContent": "" }
        ];
        
        function changeActiveDeactive( row, data, index ){
          	 // Output the data for the visible rows to the browser's console
           	//	if(data.status == null){
          	 if(data.status == 'ACTIVE'){
           		$(row).children(':eq(3)').html(data.status);
           	}
           	else{
           		//alert(data.status);
           		$(row).children(':eq(3)').html("CLOSED");
           	}
         }
        
        
        drawDataTable($("#merchantViewTicket"), urlConfigObj, columns, columnsVSTable, true, null, null, ${totalSize }, null, changeActiveDeactive, 'rtp', $(".mySearch"), $(".myExcelDownload"), $(".mySelectOpt"));
        //drawDataTable($("#ticket"), urlConfigObj, columns, columnsVSTable, true);
        
       /*  
        
        //*************************
        
        
        
        function changeActiveDeactive( row, data, index ){
        	 // Output the data for the visible rows to the browser's console
        	 if(data.status == 'INACTIVE'){
        		 $(row).children(':eq(4)').html('<span class="label label-default">'+ data.status +'</span>');
        	 }
        	 else{
        		 $(row).children(':eq(4)').html('<span class="label label-success">'+ data.status +'</span>');
        	 }
        }
        
        drawDataTable($("#subAdmin"), urlConfigObj, columns, columnsVSTable, true, null, null, ${totalSize }, "downloadSubAdminListExcel", changeActiveDeactive, 'rtp', $(".mySearch"), $(".myExcelDownload"), $(".mySelectOpt")); */
	    
	} );
	</script>
