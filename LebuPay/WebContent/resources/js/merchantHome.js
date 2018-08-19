/*$( document ).ready(function() {
 $( ".businessPersonelDetails form" ).submit(function( event ) {
    event.preventDefault();
    
    $('#step1').hide();
    $(this).find("#serverRes").hide();
    $(this).find("#serverRes").next().hide();
    
    var url = $(this).attr("action");
    function successResponse(response, messageSecdom){
      messageSecdom.hide();
      if(response.status != "ACTIVE"){
    	  jQuery.growl.error({
				message : "*"+response.message
		   });
       //messageSecdom.next().html("*"+response.message);
    	  messageSecdom.next().html("");
      }
      else{
    	  jQuery.growl.notice({
				message : "Your Business and Personal Details is saved"
		   });
    	  $('#step2').show();
      }
    }
    
    
    var submissionInfos = $( this ).serializeArray();
    var ajaxData = {};
    $.each(submissionInfos, function( index, value ) {
     ajaxData[value.name] = value.value;
     if(value.name == "serviceModel.serviceId"){
      ajaxData["serviceModel"] = {"serviceId":value.value};
     }
    });
    
    var ajaxConfig = {
     method:$(this).attr("method"),
     url:url,
     data:ajaxData,
     contentType: 'multipart/form-data'
    }
    sendAjax(ajaxConfig, successResponse, $(this).find("#serverRes"));
 });
});*/

$( document ).ready(function() {
 $( ".businessPersonelDetails form" ).submit(function( event ) {
    event.preventDefault();
    
    $('#step1').hide();
    $(this).find("#serverRes").hide();
    $(this).find("#serverRes").next().hide();
    
    var url = $(this).attr("action");
    
    var data = new FormData();
    
    jQuery.each(jQuery('#logo')[0].files, function(i, file) {
        data.append('logo', file);
    });
    
    data.append('name',jQuery('#name').val());
    data.append('address',jQuery('#address').val());
    data.append('state',jQuery('#state').val());
    data.append('directorName',jQuery('#directorName').val());
    data.append('website',jQuery('#website').val());
    data.append('pin',jQuery('#pin').val());
    data.append('city',jQuery('#city').val());
    data.append('serviceId',jQuery('#serviceId').val());
    data.append('panNumber',jQuery('#panNumber').val());
    data.append('companyId',jQuery('#companyId').val());
    data.append('logoName',jQuery('#logoName').val());
    
    jQuery.ajax({
        url: url,
        data: data,
        cache: false,
        enctype: 'multipart/form-data',
        contentType: false,
        processData: false,
        type: 'POST',
        success: function(response){
        	if(response.status == 'ACTIVE'){
        		$('[href="sandbox"]').show();
        		$('.user-image').attr('src','merchant-logo-draw?imageName='+response.message);
        		jQuery.growl.notice({
    				message : "Your Business and Personal Details is saved"
    			});
    			$('#step2').show();
        	}
        	else{
        		jQuery.growl.error({
    				message : response.message
    			});
        		$('#step1').show();
        	}
        }
    });
    
 });
});