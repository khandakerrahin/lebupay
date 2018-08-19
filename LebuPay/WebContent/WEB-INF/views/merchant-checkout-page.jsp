<%@ include file="merchant-sandbox-header.jsp"%>

<div class="sandBox">
	<%-- <div class="sandBoxWrapper">
		<div class="row brdr-btm">
			<div class="col-md-10 col-sm-10 col-xs-12">
				<h2>Checkout Page Settings</h2>
			</div>
			<div class="col-md-2 col-sm-2 col-xs-12">
				<ul>
					<li><a href="javascript:void(0)"><span><span><img
									src="<%=basePath%>resources/images/white-settings.png" /></span></span></a></li>
				</ul>
			</div>
		</div>
		<div class="table-responsive customTable">
			<table class="table">
				<thead>
					<tr>
						<th>Enabled</th>
						<th>Template</th>
						<th>Mobile</th>
						<th>Created</th>
						<th width="12%">Actions</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<form>
								<div class="checkbox">
									<label><input type="checkbox" value=""></label>
								</div>
							</form>
						</td>
						<td>Web - Standard</td>
						<td>N</td>
						<td>28 Jan 2017</td>
						<td>
							<ul>
								<li class="yellowBg"><a href="javascript:void(0)"><span><span><img
												src="<%=basePath%>resources/images/edit.png" /></span></span></a></li>
							</ul>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div> --%>
	<div class="sandBoxWrapper checkoutPage">
		<div class="row brdr-btm">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<h2>My Checkout Page Design</h2>
			</div>
		</div>
		<div class="row">
			<form action="update-checkout-layout" method="post"
				enctype="multipart/form-data">
				<div class="col-md-6 col-sm-12 col-xs-12">
					<h3>Layout</h3>
					<div class="layoutFirst">
						<div id="layoutSlider" class="carousel slide" data-ride="carousel">

							<div class="carousel-inner" role="listbox">
								<div class="item active">
									<img src="<%=basePath%>resources/images/layout.png"
										alt="layout1" width="90" height="90">
								</div>

								<div class="item">
									<img src="<%=basePath%>resources/images/layout.png"
										alt="layout2" width="90" height="90">
								</div>

								<div class="item">
									<img src="<%=basePath%>resources/images/layout.png"
										alt="layout3" width="90" height="90">
								</div>

								<div class="item">
									<img src="<%=basePath%>resources/images/layout.png"
										alt="layout4" width="90" height="90">
								</div>
							</div>
							<a class="left carousel-control customArrow" href="#layoutSlider"
								role="button" data-slide="prev"> <img
								src="<%=basePath%>resources/images/checkout-leftarrow.png" />
							</a> <a class="right carousel-control customArrow-right"
								href="#layoutSlider" role="button" data-slide="next"> <img
								src="<%=basePath%>resources/images/checkout-rightarrow.png" />
							</a>
						</div>


					</div>
					<div class="layoutSecond checkoutUp">

						<div class="input-group">
							<div class="customBox">
								<span class="iArea"><img
									src="<%=basePath%>resources/images/banner-icon.png" /></span> <input
									type="text" name="bannerName" class="form-control customInput"
									value="${checkoutModel.bannerName}" readonly>
							</div>
							<label class="input-group-btn uploadBtn"> <span
								class="btn btn-file btn-warning"> Browse&hellip; <input onchange="ValidateSingleInput(this);"
									type="file" style="display: none;" multiple name="file" />
							</span>
							</label>
						</div>

					</div>
				</div>
				<div class="col-md-6 col-sm-12 col-xs-12 pDesignWrap email_buttons">
					<div class="customBox">
						<div class="innerDiv"></div>
						<input type="pan" class="form-control customInput required" readonly
							id="background" value="${checkoutModel.backgroundColour }"
							name="backgroundColour" placeholder="Background color:">
						<!-- <span class="colorArea">
	                                          <div id="colorSelector">
	                                              <div style="background-color: #e8622f"></div>
	                                          </div>
						</span> -->
					</div>
					<button type="submit" class="btn btn-default round pre">PREVIEW</button>
					<button type="submit" class="btn btn-default round save">SAVE</button>
					<!-- <button type="reset" class="btn btn-default round cancel">CANCEL</button> -->
					<button type="submit" class="btn btn-default round restore">RESTORE
						DEFAULT</button>
				</div>
			</form>
		</div>
	</div>
	<div class="sandBoxWrapper">
		<div class="row brdr-btm">
			<div class="col-md-10 col-sm-10 col-xs-12">
				<h2>Parameters</h2>
			</div>
			<div class="col-md-2 col-sm-2 col-xs-12">
				<ul>
					<li data-toggle="modal" data-target="#addParameter"><a
						href="javascript:void(0)" title="Add Parameter" rel="tooltip"><span><span><img
									src="<%=basePath%>resources/images/plus.png" /></span></span></a></li>
				</ul>
			</div>
		</div>
		<div class="table-responsive customTable">
			<table class="table">
				<thead>
					<tr>
						<th style="display: none;">Parameter Id</th>
						<th>Parameter Name</th>
						<th>Parameter Type</th>
						<th>Visible</th>
						<th>Persistent</th>
						<th>Mandatory</th>
						<th width="12%">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="parameterModel" items="${parameterModels }">
						<tr>
							<td class="parameterId" style="display: none;">${parameterModel.parameterId }</td>
							<td class="parameterName">${parameterModel.parameterName }</td>
							<td class="parameterType"><c:choose>
									<c:when test="${parameterModel.parameterType == 'ACTIVE' }">
									Editable
								</c:when>
									<c:otherwise>
									Read Only
								</c:otherwise>
								</c:choose></td>
							<td class="visible"><c:choose>
									<c:when test="${parameterModel.visible == 'ACTIVE' }">
									YES
								</c:when>
									<c:otherwise>
									NO
								</c:otherwise>
								</c:choose></td>
							<td class="persistent"><c:choose>
									<c:when test="${parameterModel.persistent == 'ACTIVE' }">
									YES
								</c:when>
									<c:otherwise>
									NO
								</c:otherwise>
								</c:choose></td>
							<td class="mandatory"><c:choose>
									<c:when test="${parameterModel.mandatory == 'ACTIVE' }">
									YES
								</c:when>
									<c:otherwise>
									NO
								</c:otherwise>
								</c:choose></td>
							<td>
								<ul>
									<c:choose>
										<c:when test="${parameterModel.isDeletable == 1}">
											<li class="yellowBg"><a title="Edit Name"
												rel="${parameterModel.parameterId }"
												data-is-deletable="${parameterModel.isDeletable }"
												class="edit" href="#"><span><span><img
															src="<%=basePath%>resources/images/edit.png" /></span></span></a></li>
											<li class="deleteBg"><a
												rel="${parameterModel.parameterId }" class="deleteParameter"
												href="#"><span><span><img
															src="<%=basePath%>resources/images/delete.png" /></span></span></a></li>
										</c:when>
										<c:otherwise>
											<li class="yellowBg disableEdit"><a title="Edit Name"
												rel="${parameterModel.parameterId }"
												data-is-deletable="${parameterModel.isDeletable }"
												class="edit" href="#"><span><span><img
															src="<%=basePath%>resources/images/edit_disabled.png" /></span></span></a></li>
											<li class="deleteBg disableDelete"><a
												rel="${parameterModel.parameterId }" class="deleteParameter"
												href="#"><span><span><img
															src="<%=basePath%>resources/images/delete_disabled.png" /></span></span></a></li>
										</c:otherwise>
									</c:choose>
								</ul>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>



<!-- EDIT PARAMETER FORM START -->
<!-- <div id="editParameter" class="modal fade" role="dialog">
  <div class="modal-dialog">

    EDIT PARAMETER CONTENT
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"></h4>
      </div>
      <form action="updateChekoutParameter" method="post">
	      <div class="modal-body">
	      	<h3></h3>
	        <input type="hidden" name="parameterId" />
	        <ul class="form-wrapper clearfix">
			   <li class="clearfix">
			   	<label>Parameter Name:</label>
			   	<input name="parameterName" class="text" rel="Enter Parameter Name" type="text" maxlength="20">
			   </li>
			   <li class="clearfix">
			      <label>Parameter Type:</label>
			      <div id="editLabel" class="labeltxt">
			      	<span>
				      	<input id="rdEditLabeln" name="parameterType" class="Read Only" type="radio" value="INACTIVE">
				      	<label for="rdEditLabeln">Read Only</label>
				    </span>
				    <span>
				    	<input id="rdEditLabely" name="parameterType" class="Editable" type="radio" value="ACTIVE">
				    	<label for="rdEditLabely">Editable</label>
				    </span>
				  </div>
			   </li>
			   <li class="clearfix">
			      <label>Persistent:</label>
			      <div id="persistLabel" class="labeltxt">
			      	<span>
			      		<input id="rdPersistLabely" name="persistent" class="YES" type="radio" value="ACTIVE">
			      		<label for="rdPersistLabely">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdPersistLabeln" name="persistent" class="NO" type="radio" value="INACTIVE">
			      		<label for="rdPersistLabeln">NO</label>
			      	</span>
			      </div>
			   </li>
			   <li class="clearfix">
			      <label>Mandatory:</label>
			      <div id="mandatoryLabel" class="labeltxt">
			      	<span>
			      		<input id="rdMandatoryLabely" name="mandatory" class="YES" type="radio" value="ACTIVE">
			      		<label for="rdMandatoryLabely">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio" value="INACTIVE">
			      		<label for="rdMandatoryLabeln">NO</label>
			      	</span>
			      </div>
			   </li>
			   <li class="clearfix">
			      <label>Visible:</label>
			      <div id="visibleLabel" class="labeltxt">
			      	<span>
			      		<input id="rdVisibley" name="visible" type="radio" class="YES" value="ACTIVE">
			      		<label for="rdVisibley">Yes</label>
			      	</span>
			      	<span>
			      		<input id="rdVisiblen" name="visible" type="radio" class="NO" value="INACTIVE">
			      		<label for="rdVisiblen">NO</label>
			      	</span>
			      </div>
			   </li>
			</ul>
	      </div>
	      <div class="modal-footer">
	      	<button style="width: 77px; " class="btn btn-default" type="submit">Update</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
      </form>
    </div>

  </div>
</div> -->

<div class="modal fade" id="editParameter" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content start-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body clearfix">
				<form class="form-horizontal" method="post"
					action="update-checkout-parameter">
					<input type="hidden" name="parameterId" />
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Name:</label>
						<div class="col-md-6 col-sm-6">
							<input name="parameterName" class="text form-control"
								rel="Enter Parameter Name" type="text" maxlength="20">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Type:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdEditLabeln" name="parameterType"
								class="Read Only" type="radio" value="INACTIVE">Read
								Only</label> <label><input id="rdEditLabely"
								name="parameterType" class="Editable" type="radio"
								value="ACTIVE">Editable</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Persistent:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdPersistLabely" name="persistent"
								class="YES" type="radio" value="ACTIVE">Yes</label> <label><input
								id="rdPersistLabeln" name="persistent" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Mandatory:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdMandatoryLabely" name="mandatory"
								class="YES" type="radio" value="ACTIVE">Yes</label> <label><input
								id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Visible:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdVisibley" name="visible" type="radio"
								class="YES" value="ACTIVE">Yes</label> <label><input
								id="rdVisiblen" name="visible" type="radio" class="NO"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-10">
							<button type="submit" class="btn btn-success">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- Modal content end-->
	</div>
</div>
<!-- EDIT PARAMETER FORM END -->

<!-- ADD PARAMETER FORM START -->

<div class="modal fade" id="addParameter" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content start-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">&times;</button>
				<h4 class="modal-title">Parameter Add</h4>
			</div>
			<div class="modal-body clearfix">
				<form class="form-horizontal" method="post"
					action="add-checkout-parameter">
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Name:</label>
						<div class="col-md-6 col-sm-6">
							<input name="parameterName" data-name="Parameter Name"
								class="text form-control" rel="Enter Parameter Name" type="text"
								maxlength="20">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Parameter
							Type:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdEditLabeln"
								data-name="Parameter Type" name="parameterType"
								class="Read Only" type="radio" value="INACTIVE">Read
								Only</label> <label><input id="rdEditLabely"
								name="parameterType" class="Editable" type="radio"
								value="ACTIVE">Editable</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Persistent:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdPersistLabely" data-name="Persistent"
								name="persistent" class="YES" type="radio" value="ACTIVE">Yes</label>
							<label><input id="rdPersistLabeln" name="persistent"
								class="NO" type="radio" value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Mandatory:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdMandatoryLabely"
								data-name="Mandatory" name="mandatory" class="YES" type="radio"
								value="ACTIVE">Yes</label> <label><input
								id="rdMandatoryLabeln" name="mandatory" class="NO" type="radio"
								value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Visible:</label>
						<div class="radio col-md-8 col-sm-8">
							<label><input id="rdVisibley" data-name="Visible"
								name="visible" type="radio" class="YES" value="ACTIVE">Yes</label>
							<label><input id="rdVisiblen" name="visible" type="radio"
								class="NO" value="INACTIVE">No</label>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-10">
							<button type="submit" class="btn btn-success">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- Modal content end-->
	</div>
</div>
<!-- ADD PARAMETER FORM END -->

<!-- DELETE PARAMETER FORM START -->
<div id="confirm" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- EDIT PARAMETER CONTENT-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Delete Parameter</h4>
			</div>
			<form action="delete-checkout-parameter" method="post">
				<div class="modal-body">
					<input type="hidden" name="parameterId" />
					<div id="message"></div>
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

<script>

$(function() {

	  // We can attach the `fileselect` event to all file inputs on the page
	  $(document).on('change', ':file', function() {
	    var input = $(this),
	        numFiles = input.get(0).files ? input.get(0).files.length : 1,
	        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
	    input.trigger('fileselect', [numFiles, label]);
	  });

	  // We can watch for our custom `fileselect` event like this
	  $(document).ready( function() {
	      $(':file').on('fileselect', function(event, numFiles, label) {

	          var input = $(this).parents('.input-group').find(':text'),
	              log = numFiles > 1 ? numFiles + ' files selected' : label;

	          if( input.length ) {
	              input.val(log);
	          } else {
	              if( log ) alert(log);
	          }

	      });
	  });
	  
	});

  

</script>


<script type="text/javascript">

$(".edit").click(function(event){
	event.preventDefault();
	var editParameter = $("#editParameter");
	
	if($(this).attr('data-is-deletable') != 0){
		$("#editParameter").find(".modal-header h4").html("Update Custom Parameter");
		$("#editParameter").find('form [name="parameterName"]').prop("readonly", false);
	}
	else{
		$("#editParameter").find(".modal-header h4").html("Update System Parameter");
		$("#editParameter").find('form [name="parameterName"]').prop("readonly", true);
	}
	
	var parameterId = $(this).attr("rel");
	
	function successResponse(response, messageSecdom){
		  messageSecdom.hide();
		  
		  $.each(response, function( index, value ) {
			  editParameter.find('input[type="hidden"][name="'+ index +'"], input[type="text"][name="'+ index +'"]').val(value);
			  editParameter.find('input[type="radio"][name="'+ index +'"][value="'+ value +'"]').prop("checked", true);
		});
	};
	
	
	var ajaxConfig = {
			method:"post",
			url:"get-checkout-parameter",
			data:{"parameterId":parameterId}
  	}
	
	
	/* $.ajax({
		type : "POST",
		url : "getChekoutParameterById",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify({"parameterId":parameterId}),
		success : function(data) {
			$.each(data, function( index, value ) {
				  editParameter.find('input[type="hidden"][name="'+ index +'"], input[type="text"][name="'+ index +'"]').val(value);
				  editParameter.find('input[type="radio"][name="'+ index +'"][value="'+ value +'"]').prop("checked", true);
			});
			
		}
	}); */
	
	sendAjax(ajaxConfig, successResponse, $(this).find("#serverRes"));
	
	/* $(this).closest("tr").children().each(function(){
		var classAttrName = $(this).attr("class");
		if(typeof classAttrName != "undefined"){
			classAttrName = classAttrName.trim();
			editParameter.find('[type="text"][name="'+ classAttrName +'"],[type="hidden"][name="'+ classAttrName +'"]').val($(this).html());
			editParameter.find('[type="radio"][class="'+ $(this).html().trim() +'"][name="'+ classAttrName +'"]').prop('checked', true);
		}
	}); */
	
	editParameter.modal('show');
});

$(".restore").click(function(event){
	event.preventDefault();
	window.location = "restore-default-layout";
});

$(".pre").click(function(event){
	event.preventDefault();
	window.open('preview-quickpay-checkout','_blank');
	//window.location = "preview-quickpay-checkout";
});

$('.cancel[type="reset"]').click(function(event){
	event.preventDefault();
	window.location.reload();
});

$(".deleteParameter").click(function(event){
	event.preventDefault();
	if($(this).parent().prev().find("a").attr('data-is-deletable') != 0){
		$("#confirm").find('[name="parameterId"]').val($(this).attr('rel'));
		$("#confirm").find('#message').html("Are you sure to Delete "+$(this).closest("tr").find(".parameterName").html()+" parameter?");
		$("#confirm").modal('show');
	}
});

$("#addParameter form").submit(function(event){
	if($(this).find('input[type="text"]').val().trim() === ""){
		event.preventDefault();
		jQuery.growl.error({
			message : "*Please enter parameter name"
		});
	}
	else if(/[^A-Za-z0-9]/g.test($(this).find('input[type="text"]').val().trim())){
		event.preventDefault();
		jQuery.growl.error({
			message : "*Please enter valid parameter name"
		});
	}
	var form = $(this); 
	form.find('input[type="radio"][data-name]').each(function(){
		/* console.log($(this).attr('data-name')); */
		if(!form.find('[name="'+ $(this).attr('name') +'"]').is(':checked')){
			event.preventDefault();
			jQuery.growl.error({
				message : "*Please select "+ $(this).attr('data-name')
			});
		}
	});
	
});
$("#editParameter form").submit(function(event){
	 if(/[^A-Za-z0-9]/g.test($(this).find('input[type="text"]').val().trim())){
			event.preventDefault();
			jQuery.growl.error({
				message : "*Please enter valid parameter name"
			});
		}
});




/***
* Tooltip
*/
$( function()
		{   
		   
		    
			
		    var targets = $( '[rel~=tooltip]' ),
		        target  = false,
		        tooltip = false,
		        title   = false;
				
					
		    targets.bind( 'mouseenter', function()
		    {
		        target  = $( this );
		        tip     = target.attr( 'title' );
		        tooltip = $( '<div class="addTooltip"></div>' );
		 
		        if( !tip || tip == '' )
		            return false;
		 
		        target.removeAttr( 'title' );
		        tooltip.css( 'opacity', 0 )
		               .html( tip )
		               .appendTo( 'body' );
		 
		        var init_tooltip = function()
		        {
		            if( $( window ).width() < tooltip.outerWidth() * 1.5 )
		                tooltip.css( 'max-width', $( window ).width() / 2 );
		            else
		                tooltip.css( 'max-width', 340 );
		 
		            var pos_left = target.offset().left + ( target.outerWidth() / 2 ) - ( tooltip.outerWidth() / 2 ),
		                pos_top  = target.offset().top - tooltip.outerHeight() - 20;
		 
		            if( pos_left < 0 )
		            {
		                pos_left = target.offset().left + target.outerWidth() / 2 - 20;
		                tooltip.addClass( 'left' );
		            }
		            else
		                tooltip.removeClass( 'left' );
		 
		            if( pos_left + tooltip.outerWidth() > $( window ).width() )
		            {
		                pos_left = target.offset().left - tooltip.outerWidth() + target.outerWidth() / 2 + 20;
		                tooltip.addClass( 'right' );
		            }
		            else
		                tooltip.removeClass( 'right' );
		 
		            if( pos_top < 0 )
		            {
		                var pos_top  = target.offset().top + target.outerHeight();
		                tooltip.addClass( 'top' );
		            }
		            else
		                tooltip.removeClass( 'top' );
		 
		            tooltip.css( { left: pos_left, top: pos_top } )
		                   .animate( { top: '+=10', opacity: 1 }, 50 );
		        };
		 
		        init_tooltip();
		        $( window ).resize( init_tooltip );
		 
		        var remove_tooltip = function()
		        {
		            tooltip.animate( { top: '-=10', opacity: 0 }, 50, function()
		            {
		                $( this ).remove();
		            });
		 
		            target.attr( 'title', tip );
		        };
		 
		        target.bind( 'mouseleave', remove_tooltip );
		        tooltip.bind( 'click', remove_tooltip );
		    });
		});
</script>

<%
	String message = (String) request
			.getAttribute("merchant.checkout.parameterId.required");
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
	message = (String) request
			.getAttribute("merchant.checkout.parameterName.required");
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
	message = (String) request
			.getAttribute("merchant.checkout.parameterName.duplicate");
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
	message = (String) request
			.getAttribute("merchant.checkout.parameterType.required");
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
	message = (String) request
			.getAttribute("merchant.checkout.visible.required");
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
	message = (String) request
			.getAttribute("merchant.checkout.persistent.required");

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
	message = (String) request
			.getAttribute("merchant.checkout.mandatory.required");

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
	message = (String) request.getAttribute("image.format.mismatch");

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
	message = (String) request.getAttribute("restore.successfully");

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
	message = (String) request.getAttribute("common.updated");

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
	message = (String) request.getAttribute("common.successfully");

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
<script>
var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];    
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
                alert("Sorry, allowed extensions are: " + _validFileExtensions.join(", "));
                oInput.value = "";
                return false;
            }
        }
    }
    return true;
}
</script>