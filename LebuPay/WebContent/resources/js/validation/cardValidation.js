$(document).ready(
		function() {
			
			$('#cardPercentageValidation').submit(function(event) {

					var flag = true;
					
					var listCardTypeIds = document.getElementsByName("listCardTypeIds");
					var listPercentage = document.getElementsByName("listPercentage");
					var listFlatFees = document.getElementsByName("listFlatFees");
					
					for(var i=0;i<listCardTypeIds.length;i++){
						
						
						if(flag){
							if (isBlank(listPercentage[i].value, 'Percentage ')) 
								flag = false;
						}
						
						if(flag){
							if (validatePercentage(listPercentage[i].value, 'Percentage ')) 
								flag = false;
						}
						
						if(flag){
							if (isBlank(listFlatFees[i].value, 'Flat Fees')) 
								flag = false;
						}
						
						if(flag){
							if (validatePercentage(listFlatFees[i].value, 'Flat Fees ')) 
								flag = false;
						}
						
					
					}
					
					if (flag) {
						return true;
					}

					return false;
					
				});
			
		});


function isBlank(string, data) {
	if (string == null || string.trim() == "") {
		jQuery.growl.error({
			message : data + " can not be blank"
		});
		return true;
	}

	return false;
}

function validatePercentage(per,data) {
var pattern = /\d+(\.\d{1,2})?/;

if(!$.trim(per).match(pattern)){
	jQuery.growl.error({
		message : data + " Should Be numeric"
	});
	return true;
}

return false;
}
