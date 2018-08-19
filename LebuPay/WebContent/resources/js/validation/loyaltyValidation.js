$(document).ready( function() {
			
			$("#amountToPoint").submit(function(event) {
				
					var flag = true;
					
					var point = $("#point").val();
					var amount = $("#amount").val();
					
				
					if (isBlank(point, 'Point ')) {
						flag = false;
					}
					
					if(flag){
						if (validatePercentage(point, 'Point ')) {
							flag = false;
						}
					}
					
					if(flag){
						if (isBlank(amount, 'Percentage ')) {
							flag = false;
						}
					}
					
					if(flag){
						if (validatePercentage(amount, 'Percentage ')) {
							flag = false;
						}
					}
					
					if (flag) {
						return true;
					}

					return flag;
					
				});
			
			
			$("#pointToAmount").submit(function(event) {

				var flag = true;
				
				var point = $("#point").val();
				var amount = $("#amount").val();
				
			
				if(flag){
					if (isBlank(amount, 'Percentage ')) 
						flag = false;
				}
				
				if(flag){
					if (validatePercentage(amount, 'Percentage ')) 
						flag = false;
				}
				
				if (isBlank(point, 'Point ')) 
					flag = false;
				
				
				if(flag){
					if (validatePercentage(point, 'Point ')) 
						flag = false;
				}
				
				
				if (flag) {
					return true;
				}

				return flag;
				
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
