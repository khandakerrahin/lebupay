<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
html {
	display: none;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forget password</title>
<script src="<%=basePath%>resources/js/jquery-2.1.1.js"></script>
<script src="<%=basePath%>resources/js/login/md5.js"
	type="text/javascript"></script>
<script type="text/javascript">
$(window).on('keydown',function(event)
	    {
	    if(event.keyCode==123)
	    {
	       
	        return false;
	    }
	    else if(event.ctrlKey && event.shiftKey && event.keyCode==73)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	    else if(event.ctrlKey && event.keyCode==73)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	    else if(event.ctrlKey && event.keyCode==85)
	    {
	       
	        return false;  //Prevent from ctrl+shift+i
	    }
	});
	
	$(document).on("contextmenu",function(e)
	{
	   e.preventDefault();
	});
	
	
	
	function disableCtrlKeyCombination(e)
	{
	//list all CTRL + key combinations you want to disable
	var forbiddenKeys = new Array('a', 'n',  'x', 'j' , 'w','M');
	var key;
	var isCtrl;
	if(window.event)
	{
	key = window.event.keyCode;     //IE
	if(window.event.ctrlKey)
	isCtrl = true;
	else
	isCtrl = false;
	}
	else
	{
	key = e.which;     //firefox
	if(e.ctrlKey)
	isCtrl = true;
	else
	isCtrl = false;
	}
	//if ctrl is pressed check if other key is in forbidenKeys array
	if(isCtrl)
	{
	for(i=0; i<forbiddenKeys.length; i++)
	{
	//case-insensitive comparation
	if(forbiddenKeys[i].toLowerCase() == String.fromCharCode(key).toLowerCase())
	{
	//alert('Key combination CTRL + '+String.fromCharCode(key) +' has been disabled.');
	return false;
	}
	}
	}
	return true;
	}
	
	
	
</script> 
<script type="text/javascript">
if( self == top ) {
document.documentElement.style.display = 'block' ; 
} 
</script>
<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet"> 
<style type="text/css">
body {
	background: #f5f8fb; /* Old browsers */
	background: -moz-linear-gradient(top, #f5f8fb 100%, #ffffff 100%); /* FF3.6-15 */
	background: -webkit-linear-gradient(top, #f5f8fb 100%, #ffffff 100%); /* Chrome10-25,Safari5.1-6 */
	background: linear-gradient(to bottom, #f5f8fb 100%, #ffffff 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5f8fb', endColorstr='#ffffff', GradientType=0 ); /* IE6-9 */
    font-family:'Roboto', sans-serif;
}
.mainWraper {
	padding: 0;
	margin: 0;
}
.logoWraper {
	max-width: 960px;
	width: 100%;
	margin: 90px auto 80px;
	text-align: center;
}
.wraper {
	max-width: 960px;
	width: 100%;
	margin: 0 auto;
	text-align: center;
}
.forgotInput {
	border: 1px solid #66ab6f;
	border-radius: 50px;
	font-size: 16px;
	line-height: 40px;
	padding: 5px 15px;
	font-weight: normal;
	width:90%;
	box-sizing:border-box;
}
.leftSec, .rightSec {
	display: inline-block;
	margin: 0 10px;
	width:46%;
	vertical-align:top;
}
.leftSec label, .rightSec label{
	display: block;
	margin: 0 10px 10px;
	color:#414141;
	
}
.buttStyle button{ background:#66ab6f; border-radius: 50px; border:none; padding:13px 40px; color:#FFF; font-size:16px; cursor:pointer;}
.buttStyle button:hover{ background:#fff; border:1px solid #66ab6f; color:#66ab6f;}
.buttStyle{ margin-top:40px;}
.alerttext{ color:#ff0000; font-size:12px; line-height:17px; margin-top:10px; display:block;}
		
</style>

<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
</head>
<body>

<div class="mainWraper">
  <div class="logoWraper"><img src="<%=basePath%>resources/images/lebupay_logo.png" width="200" height="56" /></div>
  <div class="wraper">
    <form action="mail-forgot-password" method="post">
      <input type="hidden" name="merchantId" value="${merchantId }" />
      <input type="hidden" name="csrfPreventionSalt"
						value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
      <div class="leftSec">
        <label>Password</label>
        <input type="password" name="password" required class="forgotInput" />
		<span class="alerttext">Password length should be within 6-20 including at least one number, one lowercase and uppercase character, one special character. (Eg:- Wb07f8591@)</span>
      </div>
      <div class="rightSec">
        <label>Confirm Password</label>
        <input type="password" name="confirmPassword" required class="forgotInput" />
		<span class="alerttext">Password length should be within 6-20 including at least one number, one lowercase and uppercase character, one special character. (Eg:- Wb07f8591@)</span>
      </div>
      <div class="buttStyle"><button type="submit">Submit</button></div>
    </form>
  </div>
</div>

	
	<script type="text/javascript">
$(document).ready(function (){
	$("form").submit(function(event){
		var passwordArr = [$(this).find('[name="password"]'), $(this).find('[name="confirmPassword"]')];
		if(passwordArr[0].val() != passwordArr[1].val()){
			jQuery.growl.error({
				message :"*Password mismatched"
			});
			
			event.preventDefault();
		}
		else{
			for(i = 0; i < passwordArr.length; i++){
				
				var password = passwordArr[i].val();
				var md5password = password;//md5(password);

				passwordArr[i].val(md5password);
				passwordArr[i].css("background-color", 'White');
			}
			
		}
		
		
		
	});
});
</script>


<%
					String message = (String) request.getAttribute("error");
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
					
</body>
</html>