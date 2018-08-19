function sendAjax(ajaxConfig, successFunction, messageSecdom){
	/*if(typeof messageSecdom !== "undefined"){
		messageSecdom.show();
		messageSecdom.next().show().html("Please Wait ...");
	}*/
	
	
	var config = {
		dataType : "json",
		type: ajaxConfig.method,
		url: ajaxConfig.url,
		success : function(response) {
			successFunction(response, messageSecdom, ajaxConfig);
		},
	};
	if(ajaxConfig.method.toLowerCase() == "post" && typeof ajaxConfig.data != "undefined"){
		config.contentType = "application/json; charset=utf-8";
		config.data = JSON.stringify(ajaxConfig.data);
	}
	$.ajax(config);
}
