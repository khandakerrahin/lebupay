function clearModule(){
	$('form').each(function(){
		this.reset();
	});
}
$(document).ready(function() {
	$('.modal button[type="button"][aria-label="Close"]').click(function(){
		clearModule();
		
		
		if($('customerRecaptcha').length != 0){
			grecaptcha.reset($('customerRecaptcha').attr('id'));
		}
		if($('merchantRecaptcha').length != 0){
			grecaptcha.reset($('merchantRecaptcha').attr('id'));
		}
	});
});
