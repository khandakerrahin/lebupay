<%@ include file="merchant-header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt"%>
<%

List<TransactionModel> transactionModels = (List<TransactionModel>) request.getAttribute("transactionModels");

System.out.println("transactionModels==>"+transactionModels.size());

%>
<style type="text/css">
	.tableWrap ul li{
    background: none;
    border: none;
    }
    table.dataTable>tbody>tr.child ul{display:block; width:100%;}
    table.dataTable>tbody>tr.child ul li{display:inline;}
	@media screen and (max-width: 1077px){
		table.dataTable.dtr-inline.collapsed>tbody>tr>td:first-child:before, table.dataTable.dtr-inline.collapsed>tbody>tr>th:first-child:before {
		    top: 0px !important;
		    left: 0px !important;
		    height: 15px;
		    width: 15px;
		}
		.txnBtn{width:100%; display:block; margin:5px 0; text-align:center;}
	}
	
	.dataTables_length select {
    border: solid 1px #b9b9b9;
    border-radius: 50px;
    height: 40px;
    padding: 4px 10px;
    width: 200px !important;
    color: #989797;
    margin: 0px 10px;
    font-size: 15px;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/jquery.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/rowReorder.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/responsive.dataTables.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/buttons.dataTables.min.css" />
<!-- Datatables -->
<link href="<%=basePath%>resources/css/datatable/buttons.bootstrap.min.css" rel="stylesheet">
<link href="<%=basePath%>resources/css/datatable/fixedHeader.bootstrap.min.css" rel="stylesheet">
<link href="<%=basePath%>resources/css/datatable/responsive.bootstrap.min.css" rel="stylesheet">
<link href="<%=basePath%>resources/css/datatable/scroller.bootstrap.min.css" rel="stylesheet">

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content banner -->
	<section class="content-header">
		<div class="row">
			<div class="col-md-12 vertpad">
				<div class="content-banner">
					<h2>
						Simple Plan, <span>Instant Go Live</span>
					</h2>
					<h3>
						1.99% + <span>&#2547;</span>
						${merchantModel.getTransactionAmount()}
					</h3>
					<p>Per Successful Transaction</p>
				</div>
			</div>
		</div>
	</section>

	<!-- Main content -->
	<section class="content marginBtm">
		<div class="keyArea reskeys">
			<!-- Small boxes (Stat box) -->
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<h2>Keys</h2>
				</div>
				<!-- ./col -->
				<div class="col-md-4 col-sm-12 col-xs-12">
					<h3>Security Credentials</h3>
					<p>Access Key</p>
					<span>${merchantModel.getAccessKey()}</span>
					<p>Secret Key</p>
					<span>${merchantModel.getSecretKey()}</span>
				</div>
				<!-- ./col -->
				<div class="col-md-4 col-sm-12 col-xs-12">
					<h3>Download Key Stores</h3>
					<button type="button" class="btn keyBtn">LebuPay (Web)</button>
					<button type="button" class="btn keyBtn keyBtn2">Mobile
						SDK</button>
				</div>
				<!-- ./col -->
			</div>
		</div>
	</section>

	<section class="content marginBtm">
		<div class="adminformBox tableWrap bordergrey">
		<div class="panel panel-default">
					<div class="panel-heading bg-light-grey">
					<form action="list-transaction" method="post">
						<div class="row">
							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="input-group stylish-input-group form-group" id="masterEffectiveStartDate">
									<input class="form-control mySearch blockInput" aria-controls="subAdmin"
										placeholder="Start Date" type="text" name="toDate" value="${dataTableModel.toDate}"> <span
										class="input-group-addon">
											<span><i class="fa fa-calendar"></i></span>
									</span>
								</div>
							</div>
							
							<div class="col-md-2 col-sm-4 col-xs-12">
								<div class="input-group stylish-input-group form-group" id="masterEffectiveEndDate">
									<input class="form-control mySearch blockInput" aria-controls="subAdmin"
										placeholder="End Date" type="text" name="fromDate" value="${dataTableModel.fromDate}"> <span
										class="input-group-addon">
											<span><i class="fa fa-calendar"></i></span>
									</span>
								</div>
							</div>
							
							<div class="col-md-2 col-sm-4 col-xs-12">
								
									<button type="submit" class="btn btn-success myExcelDownload txnBtn">SUBMIT</button>
								
							</div>
							<div class="col-md-2 col-sm-4 col-xs-12">
								
									<a href="dashboard" class="btn btn-success myExcelDownload txnBtn">RESET</a>
							</div>
							<div class="col-md-2 col-sm-4 col-xs-12">
									<input type="button" class="claim btn btn-success myExcelDownload txnBtn" id="hideClaim" style="display: none;" value="Claim">
							</div>
							<c:if test="${fn:length(transactionModels) gt 0}">
							<% if(Objects.nonNull(transactionModels)){ 
								int totalClaim = 0;
								for(int i=0; i<transactionModels.size();i++){
									if(transactionModels.get(i).getTransactionStatus() == 0){
										totalClaim = totalClaim + 1;
									}
								}
								
								if(totalClaim > 0) { %>
									<div class="col-md-2 col-sm-4 col-xs-12">
										<div class="checkbox checkbox-primary">
					                        <input type="checkbox" id="selectAll1"><label for="selectAll1">SELECT ALL</label>
					                    </div>
				                    </div>
						<%	} 	} 	%>
							
						</c:if>
						</div>
						</form>
					</div>

					<div class="panel-body">
					<div class="transaction-list-table-outer">
					<div class="table-responsive">
						<table id="datatable-buttons1" class="table display dataTable no-footer dtr-inline">
							<thead>
								<tr>
									<th>Name</th> 
									<th>Order TXN ID</th> 
									<th>TXN Amount</th>
									<th>Payable Amount</th>
									<th>Bank</th>
									<th>TXN ID</th>
									<th>TXN Date</th>
									<th>TXN Date</th>
									<th>TXN Time</th>
									<th>Claim</th>
									<th>Transaction Status</th>
									
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${transactionModels}" var="transactionModel">
									<c:set var="slNo" value="${slNo + 1}" scope="page" />
									<tr>
										<td>${transactionModel.paymentModel.name}</td> 
										<td>${transactionModel.paymentModel.orderTransactionID}</td>
										<td>${transactionModel.grossAmount }</td>
										<td>${transactionModel.amount }</td>
										<td><c:out value="${empty transactionModel.bank ? 'NA' : transactionModel.bank}" /></td>
										<td>${transactionModel.txnId }</td>
										<td>${transactionModel.modifiedDate }</td>
										<c:set var="dateParts" value="${fn:split(transactionModel.modifiedDate, ',')}" />
										<td>${dateParts[0]},${dateParts[1]}</td>
										<td>${dateParts[2]}</td>
										
										<td>
											<div class="checkbox checkbox-primary">
						                    <c:choose>  
													<c:when test="${transactionModel.transactionStatus=='0'}">
													 	<input type="checkbox" data-name="claimed" id="txn${transactionModel.transactionId}">  
														<label for="txn${transactionModel.transactionId}">Claim</label>  
													</c:when> 
													<c:when test="${transactionModel.transactionStatus=='1'}">
													 	<input type="checkbox" data-name="claimed" id="txn${transactionModel.transactionId}" disabled> 
														<label style="color:#E08E0B" for="txn${transactionModel.transactionId}">Already Claimed</label>  
													</c:when>  
													<c:when test="${transactionModel.transactionStatus=='2'}">
													 	<input type="checkbox" data-name="claimed" id="txn${transactionModel.transactionId}" disabled> 
														<label style="color:#42AA87" for="txn${transactionModel.transactionId}">Disburse From Admin</label>
													</c:when>
													<c:when test="${transactionModel.transactionStatus=='3'}">
													 	<input type="checkbox" data-name="claimed" id="txn${transactionModel.transactionId}" disabled> 
														<label style="color:#E9242B" for="txn${transactionModel.transactionId}">Failed Transaction</label>
													</c:when>														 
													<c:otherwise>
													 	<input type="checkbox" data-name="claimed" id="txn${transactionModel.transactionId}">  
														<label style="color:#E9242B" for="txn${transactionModel.transactionId}">Failed Transaction</label>
													</c:otherwise>  
											</c:choose>
											</div>  
						                </td>
						                <td> 
						                	<c:choose>  
													<c:when test="${transactionModel.transactionStatus=='0'}">
														success
													</c:when> 
													<c:when test="${transactionModel.transactionStatus=='1'}">
													 	success  
													</c:when>  
													<c:when test="${transactionModel.transactionStatus=='2'}">
													 	success
													</c:when>
													<c:when test="${transactionModel.transactionStatus=='3'}">
													 	Failed
													</c:when>														 
													<c:otherwise>
													 	Failed
													</c:otherwise>  
											</c:choose>
											</td>
											
									</tr>
								</c:forEach>
							</tbody>
							</table>
							</div>
						</div>
					</div>
				</div>
			</div>
	</section>
	<!-- /.content -->
<%@ include file="merchant-footer.jsp"%>

<!-- DELETE PARAMETER FORM START -->
<div id="confirm" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- EDIT PARAMETER CONTENT-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Merchant Claim</h4>
			</div>
			<form action="claim-transaction" method="post">
				<input type="hidden" name="csrfPreventionSalt" value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
				<input type="hidden" name="transactionId" />
				<div class="modal-body">
					<input type="hidden" />
					<div id="message">Do you want to claim the Transactions?</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Claim</button>
					<button type="button" data-dismiss="modal" class="btn">Cancel</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- DELETE PARAMETER FORM END -->
<script type="text/javascript">
	
	$(document).ready(function() {
        $('[id^="selectAll"]').change(function(){
        	$('[id^="selectAll"]').prop('checked', $(this).prop('checked'));
        	$(this).closest('thead, tfoot').siblings('tbody').find('[data-name="claimed"][disabled!="disabled"]').prop('checked', $(this).prop('checked'));
        	if($(this).prop('checked')){
        		$('.claim').show();
        	}
        	else{
        		$('.claim').hide();
        	}
        });
        
        $("#datatable-buttons1 tbody").on('change','[id^="txn"]',function(){
        	$('[id^="selectAll"]').prop('checked', false);
        	if($(this).prop('checked')){
        		$('.claim').show();
        	}
        	else{
        		if($('[id^="txn"][disabled!="disabled"]:checked').length == 0){
        			$('.claim').hide();
        		}
        	}
        });
        
        
        $('.claim').click(function(){
        		var transactionId = "";
        		if($('[id^="selectAll"]').prop('checked')){
        			transactionId = "all";
        		} else{
        			transactionId = $('[id^="txn"][disabled!="disabled"]:checked').map(function(){
        				return this.id.replace("txn", "");
        			}).get().join();
        		}
        		$('input[name="transactionId"]').val(transactionId);
        		$('#confirm').modal("show");
        });
});
</script>
	

<script src="<%=basePath%>resources/js/moment.min.js"></script>
<script src="<%=basePath%>resources/js/bootstrap-datetimepicker.js"></script>
<script src="<%=basePath%>resources/js/moment-with-locales.js"></script>


<script type="text/javascript">
    $(function () {
        $('#masterEffectiveStartDate').datetimepicker({format : 'YYYY-MM-DD'});
       
        $('#masterEffectiveEndDate').datetimepicker({
            useCurrent: false,
            format : 'YYYY-MM-DD'
        });
        
        $("#masterEffectiveStartDate").on("dp.change", function (e) {
            $('#masterEffectiveEndDate').data("DateTimePicker").minDate(e.date);
            $(this).data('DateTimePicker').hide();
        });
        
        $("#masterEffectiveEndDate").on("dp.change", function (e) {
            $('#masterEffectiveStartDate').data("DateTimePicker").maxDate(e.date);
            $(this).data('DateTimePicker').hide();
        });
        $('.blockInput').keydown(function(e) {
        	   e.preventDefault();
        	   return false;
        	});
    });
</script>
<!-- Datatables -->
<script src="<%=basePath%>resources/js/datatable/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.bootstrap.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.buttons.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/buttons.bootstrap.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/buttons.flash.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/buttons.html5.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/buttons.print.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.fixedHeader.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.keyTable.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.responsive.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/responsive.bootstrap.js"></script>
<script src="<%=basePath%>resources/js/datatable/dataTables.scroller.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/jszip.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/pdfmake.min.js"></script>
<script src="<%=basePath%>resources/js/datatable/vfs_fonts.js"></script>

<script>
  $(document).ready(function() {
	  $(".btn-success").css("background-color","#008d4c");
	  $(".form-control input-sm").attr("placeholder","Search...");
    var handleDataTableButtons = function() {
      if ($("#datatable-buttons1").length) {
    	 var oTableStaticFlow =  $("#datatable-buttons1").DataTable({
        	 "columnDefs": [
        	               {
        	                   "targets": [7,8,10],
        	                   "visible": false,
        	                   "searchable": false
        	               }
        	           ],
        	"oLanguage": {
        	      "sLengthMenu": "Show Transactions _MENU_ ",
        	    },
        	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
        	"aaSorting": [],
          dom: "lBfrtip",
          buttons: [
			    {
			    extend: "csv",
			    className: "btn-sm tx-button",
			    title: "Merchant Transaction List",
			    exportOptions: {
	                columns: [0,1,2,3,4,5,7,8,10],
                   // rows: ':visible'
	            },
			  },  
			   /* {
			    extend: "excel",
			    className: "btn-sm tx-button",
			    title: "Merchant Transaction List",
			    exportOptions: {
	                columns: [0,1,2,3,4,5,7,8,10]
	            },
			  }, */  {
			    extend: "pdf",
			    className: "btn-sm tx-button",
			    orientation: 'landscape',
			    title: "Merchant Transaction List",
			    exportOptions: {
	                columns: [0,1,2,3,4,5,7,8,10],
                   // rows: ':visible'
	            },
			    
			  },
			   
          ],
         responsive: true
        });
    	 
    	 var cells = oTableStaticFlow.column(9).nodes();
    	 $("#selectAll1").click(function () {
    		    var cells = oTableStaticFlow.column(9).nodes(), // Cells from 1st column
    		        state = this.checked;
    		    for (var i = 0; i < cells.length; i += 1) {
    		    	if(!cells[i].querySelector("input[type='checkbox']").disabled)
    		        	cells[i].querySelector("input[type='checkbox']").checked = state;
    		      
    		    }
    		});
      }
    };

    TableManageButtons = function() {
      "use strict";
      return {
        init: function() {
          handleDataTableButtons();
        }
      };
    }();

    $('#datatable').dataTable();

    $('#datatable-keytable').DataTable({
      keys: true
    });

    $('#datatable-responsive').DataTable();

   

    $('#datatable-fixed-header').DataTable({
      fixedHeader: true
    });

    var $datatable = $('#datatable-checkbox');

    $datatable.dataTable({
    	
    	"bSort" : false
    });
    $datatable.on('draw.dt', function() {
      $('input').iCheck({
        checkboxClass: 'icheckbox_flat-green'
      });
    });

    TableManageButtons.init();
  });
  

</script>
	<%
		String message = (String) request.getAttribute("merchant.profile.update.success");
		if (message != null) {
	%>
<script>
 $(document).ready(function() {
 jQuery.growl.notice({
		message : "<%=message%>"
					});
				});
			</script>
	<% } %>

<%
	message = (String) request.getAttribute("image.format.not.supported");
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
	message = (String) request.getAttribute("image.null");
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
	
	