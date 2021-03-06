<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String bkash = request.getParameter("bkash") != null ? request.getParameter("bkash") : "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<title>Merchant Transaction</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="resources/css/font-awesome.css">
<!-- color picker css -->
<link href="resources/css/color-picker.min.css" rel="stylesheet">
<!-- Theme style -->
<link rel="stylesheet" href="resources/css/AdminLTE.css">
<link rel="stylesheet" href="resources/css/custom.css">
<link rel="stylesheet" href="resources/css/responsive.css">
<link rel="stylesheet" href="resources/css/mk-notifications.css">
<link href="resources/css/notify.css" rel="stylesheet">
<link
	href="resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon"
	href="resources/images/favicon.ico">
<!-- jQuery 2.2.3 -->
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
<script src="<%=basePath%>resources/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/vendors/growl/css/jquery.growl.css" />
<script src="<%=basePath%>resources/vendors/growl/js/jquery.growl.js"
	type="text/javascript"></script>
<script src="<%=basePath%>resources/js/jquery.notify.js"></script>


<style>
.navImg {
	width: 58px;
	margin-top: -10% !important;
}

#infoName {
	font-size: 20px;
}

#infoItem {
	font-size: 19px;
}

#paySection {
	border: 1px solid black;
}

.images_list li {
	list-style: none;
	float: left;
	width: 90px;
	height: 70px;
	margin-right: 10px;
}

.images_list li span {
	display: none;
	position: absolute;
	top: 0px;
	left: 0px;
	width: 24px;
	height: 24px;
}

.border {
	border: 6px solid #D8D8D8;
	background: url(upload/check.jpg);
}

.selected {
	border: 6px solid green;
	position: relative;
}

.hidden {
	display: none;
}

.images_list li.selected span {
	display: block;
}

#mainBox {
	padding-left: 5%;
}

#cancelOrderTag {
	padding-left: 25%;
}

@media ( max-width : 723px) {
	.lebuImg {
		margin-top: 0% !important;
	}
	#brand1 {
		margin-top: -7% !important;
	}
	#mainBox {
		padding-left: 0%;
	}
	#cancelOrderTag {
		padding-left: 0%;
	}
}
</style>
<script type="text/javascript">
$(document).ready(function(){ 
    $("#myTab a").click(function(e){
    	e.preventDefault();
    	$(this).tab('show');
    });
});
</script>
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
	 //  e.preventDefault();
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
	
	
	function copyToClipboard (containerid) {
        var data = document.getElementById(containerid).innerText;
        var type="";
        if(data.length >= 11 && data.length <=16){
        	type="mobile";
        }else{
        	type="amount";
        }
        var textarea = document.createElement('textarea')
        textarea.id = 'temp_element'
        textarea.style.height = 0
        document.body.appendChild(textarea)
        textarea.value = document.getElementById(containerid).innerText
        var selector = document.querySelector('#temp_element')
        selector.select()
        document.execCommand('copy')
        document.body.removeChild(textarea);
        if(type=="mobile"){
        	document.getElementById(containerid).innerHTML="<style>#msgtext{ background-color:#228B22; color: #fff; text-align: center; border-radius: 6px; padding-top: 8px; padding-bottom: 5px; }</style> <b id='msgtext'>bKash Number Copied</b>";	
        }else if(type=="amount"){
        	document.getElementById(containerid).innerHTML="<style>#msgtext{ background-color:#228B22; color: #fff; text-align: center; border-radius: 6px; padding-top: 8px; padding-bottom: 5px; }</style> <b id='msgtext'>Amount Copied</b>";
        }
        
        setInterval(function(){
            document.getElementById(containerid).innerText = data;
        }, 1500);
    }
</script>
<script type="text/javascript">
if( self == top ) {
document.documentElement.style.display = 'block' ; 
} 
</script>


<!-- <script type="text/javascript">
$(document).ready(function(){ 
    $("#myTab a").click(function(e){
    	e.preventDefault();
    	$(this).tab('show');
    });
});
</script> -->
</head>
<body
	class="hold-transition skin-blue sidebar-mini noSidebar merchant-link-page">
	<script type="text/javascript" src="https://cdn.ywxi.net/js/1.js" async></script>
	<div class="wrapper" style="padding-bottom: 0px">

		<nav class="navbar navbar-default"
			style="margin-bottom: 0% !important">
		<div class="container-fluid">
			<div class="nav-header">
				<div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-xs-6">
								<a class="navbar-brand" href="#"> <img alt="${merchantName}"
									id="brand2"
									src="<%=basePath%>resources/images/merchantLogo/${logo}"
									class="img img-responsive navImg">
								</a>
							</div>
							<div class="col-xs-6">
								<a class="navbar-brand" href="#" style="float: right;"> <img
									alt="LebuPay" id="brand1"
									class="img img-responsive navImg lebuImg"
									src="<%=basePath%>resources/insta-buy/lebupay_logo.png"
									style="width: 100% !important;">
								</a>
							</div>

						</div>
					</div>

				</div>
			</div>
		</div>
		</nav>
		<!-- <nav class="navbar navbar-static-top">
		<div class="col-md-12"></div>
		</nav> -->
		<!-- Left side column. contains the logo and sidebar -->
		<!-- Content Wrapper. Contains page content -->
		<%-- <div class="content-wrapper">
			<section class="content">
			<div class="row sandBoxTab">
				<div class="col-xs-12">
					<div class="resp-tabs-container">
						<div class="sandBox">
							<div class="sandBox">
								<div class="sandBoxWrapper checkoutPage">
									<div class="row">
										<div class="col-md-12 col-sm-12 col-xs-12">
											<div class="orderSummery">
												<h3>Order Summary</h3>
												<div class="contSec">
													<p>
														<span>Merchant:</span><strong>${merchantName }</strong>
													</p>
													<p>
														<span>Transaction ID:</span><strong>${transactionId }</strong>
													</p>
													<p class="price">
														<span>Total:</span><strong>&#2547; <fmt:formatNumber
																type="number" maxFractionDigits="2"
																value="${grossAmount}" /></strong>
													</p>
													<p class="cancelOrder">
														<a href="${returnUrl }">Cancel Order</a>and return to
														LebuPay
													</p>
												</div>
											</div>
										</div>

										<!-- <div class="col-md-6 col-sm-6 col-xs-12"> -->
											<div class="paymentMethod">
												<h3>Select Payment Method</h3>


												<div class="contSec">
													<ul class="nav nav-tabs" id="myTab">

														<li id="secA"><a href="#sectionA"> Payment
																Options</a></li>

														<li id="secD"><a href="#sectionD"> Payment
																Options</a></li>

														<li id="secC"><a href="#sectionC"> Payment
																Options</a></li>

														<!--  Changed by Wasif Ahmed -->

														<li id="secB"><a href="#sectionB">bKash</a></li>

													</ul>

													<div class="tab-content">
														<div id="sectionA" class="tab-pane fade in active">
															<div class="eachBox">
																<ul>
																	<li class="card_logos"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li class="card_logos diner"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/diners_club.png"
																			alt="diners club"></a></li>
																	<li id="bkash_hov" class="card_logos"><a
																		href="bkash?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/bkash.png"
																			style="width: 25px" alt="bkash"></a></li>
																</ul>
															</div>
														</div>

														<div id="sectionD" class="tab-pane  fade in active">
															<div class="eachBox">
																<ul>
																	<li class="card_logos"><a
																		href="sebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="sebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li class="card_logos"><a
																		href="bkash?transactionId=${transactionId1 }"><img
																			src="resources/images/bkash_v2.png"
																			style="width: 25px" alt="bkash"></a></li>
																</ul>
															</div>
														</div>

														<div id="sectionC" class="tab-pane fade in active ">
															<div class="eachBox">
																<ul>
																	<!-- onclick="return clickOnEMI()" -->
																	<li class="card_logos"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li class="card_logos diner"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/amex.png"
																			alt="diners club"></a></li>
																	<li class="card_logos"><a
																		href="bkash?transactionId=${transactionId1 }"><img
																			src="resources/images/bkash_v2.png"
																			style="width: 25px" alt="bkash"></a></li>
																</ul>
															</div>
														</div>

														<div id="sectionB" class="tab-pane fade in active">
															<div class="eachBox bkashl">
																<ul>
																	<li class="card_logos"><a
																		href="bkash?transactionId=${transactionId1 }"><img
																			src="resources/images/bkash_v2.png"
																			style="width: 25px" alt="bkash"></a></li>
																</ul>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>




									<div class="payment_page_footer">
										<div class="row">
											<div class="col-md-12">
												<p>Payments powered by:</p>
												<ul>
													<li><img
														src="<%=basePath%>resources/images/verified-by-visa.png"
														alt="verified by visa"></li>
													<li><img
														src="<%=basePath%>resources/images/mastercard-secure-code.png"
														alt="mastercard secured code"></li>
													<li><img
														src="<%=basePath%>resources/images/american-express-safekey.png"
														alt="american express safekey"></li>
													<li><img
														src="<%=basePath%>resources/images/diner-club-protect.png"
														alt="diners-club-protect"></li>
												</ul>
											</div>
										</div>
									</div>

								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
			</section>
		</div>
		
 --%>

		<div style="float: right; margin-bottom: 5px; padding-right: 10px;">
			<b>English</b> <label class="switch"
				style="padding-top: -30px; margin-bottom: 0px;"> <input
				type="checkbox" id="changeLang" name="changeLang" value="changeLang" />
				<span class="slider round"></span>
			</label> <b>???????????????</b>
		</div>
		<div class="content-wrapper">
			<section class="content" style="margin-top:0px">
			<div class="row sandBoxTab">
				<div class="col-xs-12">
					<div class="resp-tabs-container">
						<div class="sandBox">
							<div class="sandBox">
								<div class="sandBoxWrapper checkoutPage"
									style="padding-top: 0px !important; margin-top: -6px !important; padding-bottom: 0px">
									<div class="row">
										<div class="col-md-3 col-sm-3 col-xs-12"></div>
										<div class="col-md-6 col-sm-6 col-xs-12" id="mainBox">
											<div class="orderSummery" style="padding-top: 5px;">
												<h3 id="english1">Order Summary</h3>
												<h3 id="bangla1" style="display: none">?????????????????? ??????????????????</h3>
												<div class="contSec">
													<div class="row">
														<div class="col-xs-8">
															<b id="infoName" class="english1">Merchant Name</b> <b
																id="infoName" class="bangla1" style="display: none">?????????????????????????????????
																?????????</b><br> <i id="infoItem">${merchantName }</i>&nbsp;<i
																class="fa fa-question-circle" aria-hidden="true"></i>

														</div>
														<div class="col-xs-4">
															<b id="infoName" class="english1">Amount</b> <b
																id="infoName" class="bangla1" style="display: none">??????????????????</b><br>
															<strong id="infoItem">&#2547; <fmt:formatNumber
																	type="number" maxFractionDigits="2"
																	value="${grossAmount}" />

															</strong>
														</div>
													</div>

													<br>
													<div class="contSec"
														style="border-top: 1px solid #66ab6f; border-radius: 20px">
														<div class="eachBox" style="text-align: center">
															<%
																String city = request.getAttribute("CITY").toString();
																String ebl = request.getAttribute("EBL").toString();
																String sebl = request.getAttribute("SEBL").toString();
															%>

															<%
																if (ebl.equals("Y")) {
															%>
															<div id="eblDiv">
																<ul>

																	<li class="card_logos"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li class="card_logos diner"><a
																		href="ebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/diners_club.png"
																			alt="diners club"></a></li>
																	<li id="bkash_hov" class="card_logos"><a href="#"
																		id="bkashClicked"><img
																			src="<%=basePath%>resources/images/bkash4.png"
																			style="width: 27px; height: 26px !important;"
																			alt="bkash"><b
																			style="font-size: 9px; color: #000;">bKash</b></a></li>
																</ul>
															</div>
															<%
																} else if (city.equals("Y")) {
															%>
															<div id="cityDiv">
																<ul>

																	<li class="card_logos"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li class="card_logos diner"><a
																		href="paycitybank?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/amex.png"
																			alt="diners club"></a></li>
																	<li id="bkash_hov" class="card_logos"><a href="#"
																		id="bkashClicked"><img
																			src="<%=basePath%>resources/images/bkash4.png"
																			style="width: 27px; height: 26px !important;"
																			alt="bkash"><b
																			style="font-size: 9px; color: #000;">bKash</b></a></li>
																</ul>
															</div>

															<%
																} else if (sebl.equals("Y")) {
															%>
															<div id="seblDiv">
																<ul>

																	<li class="card_logos"><a
																		href="sebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/visa.png"
																			alt="visa card"></a></li>
																	<li class="card_logos"><a
																		href="sebl?transactionId=${transactionId1 }"><img
																			src="<%=basePath%>resources/images/mastercard.png"
																			alt="master card"></a></li>
																	<li id="bkash_hov" class="card_logos"><a href="#"
																		id="bkashClicked"><img
																			src="<%=basePath%>resources/images/bkash4.png"
																			style="width: 27px; height: 26px !important;"
																			alt="bkash"><b
																			style="font-size: 9px; color: #000;">bKash</b></a></li>
																</ul>
															</div>
															<%
																} else {
															%>
															<div id="showBkashDiv">
																<ul>
																	<li id="bkash_hov" class="card_logos"><a href="#"
																		id="bkashClicked"><img
																			src="<%=basePath%>resources/images/bkash4.png"
																			style="width: 27px; height: 26px !important;"
																			alt="bkash"><b
																			style="font-size: 9px; color: #000;">bKash</b></a></li>
																</ul>
															</div>
															<%
																}
															%>
														</div>
														<div id="bkashDiv" style="display: none">


															<div id="englishhead">
																<p class="bkash-p" id="bkashPaymentFlowImg"
																	style="font-size: 14px;">
																	Please make a payment/transfer of BDT <strong
																		id="amount-id" onclick="copyToClipboard('amount-id')">${amount}</strong>
																	to the bKash wallet <strong id="wallet-id"
																		onclick="copyToClipboard('wallet-id')">${wallet}</strong>
																	and enter the bKash transaction reference ID below.<a
																		href="#" onclick="return false;" id="show_me_tag">
																		(Show Me How)</a>
																</p>
															</div>
															<div id="banglahead" style="display: none">
																<p class="bkash-p" id="bkashPaymentFlowImg"
																	style="font-size: 14px;">
																	????????????????????? ????????? <strong id="wallet-id1"
																		onclick="copyToClipboard('wallet-id1')">${wallet}</strong>
																	?????????????????? <strong id="amount-id1"
																		onclick="copyToClipboard('amount-id1')">${amount}</strong>
																	???????????? ??????????????? ????????????????????? ???????????? ????????? ??????????????? ?????????????????? ???????????????
																	?????????????????????????????? ?????????????????? ?????????????????? ???????????? | <a href="#"
																		onclick="return false;" id="show_me_tag1">
																		(???????????????????????????)</a>
																</p>
															</div>

															<form action="bkash" method="post"
																style="padding-top: 25px;">
																<input type="hidden" name="transactionId1"
																	value="${transactionId1 }" readonly> <input
																	type="hidden" name="csrfPreventionSalt"
																	value="<%=request.getAttribute("csrfPreventionSaltPage")%>"
																	required />



																<!-- The Modal -->
																<div id="myModal" class="modal">
																	<span class="close">&times;</span> <img
																		class="modal-content" id="img01">
																	<div id="caption"></div>
																</div>



																<script>
							// Get the modal
							var modal = document.getElementById('myModal');
							
							// Get the image and insert it inside the modal - use its "alt" text as a caption
						//	var img = document.getElementById('bkashPaymentFlowImg'); //20180106 Wasif 							
							var img = document.getElementById('show_me_tag');
							var img1 = document.getElementById('show_me_tag1');
							var modalImg = document.getElementById("img01");
							var captionText = document.getElementById("caption");
							img.onclick = function(){
							    modal.style.display = "block";
							    modalImg.src = "<%=basePath%>resources/images/bkashPaymentFlow.png";
							    captionText.innerHTML = "bKash Payment Flow";
							}
							img1.onclick = function(){
							    modal.style.display = "block";
							    modalImg.src = "<%=basePath%>resources/images/bkashPaymentFlow.png";
							    captionText.innerHTML = "bKash Payment Flow";
							}
							window.onclick = function(event) {
							    if (event.target == modal) {
							        modal.style.display = "none";
							    }
							}
							
							// Get the <span> element that closes the modal
							var span = document.getElementsByClassName("close")[0];
							
							// When the user clicks on <span> (x), close the modal
							span.onclick = function() { 
							    modal.style.display = "none";
							}
							</script>

																<div class="form-group eachFld resp_bkash">
																	<input type="hidden" name="wallet" value="${wallet }">
																	<input type="hidden" name="reference"
																		value="${reference }"> <input type="hidden"
																		name="counter" value="${counter }"> <label
																		for="registered" class="bkash_transaction"
																		style="font-size: 14px;">Transaction ID</label> <input
																		type="text" name="trx_id"
																		class="registed bkash_transaction" required
																		onpaste="return true">
																</div>
																<div class="form-group eachFld">
																	<button class="btn btn-primary verify_btn_bkash"
																		style="background-image : url(<%=basePath%>resources/images/green.jpg);"
																		type="submit">Verify For ${amount } BDT</button>
																</div>
															</form>


															<div class="row">
																<div class="col-md-7" style="">
																	<div id="english">
																		To bKash your Payment follow the steps below: <br />

																		01. Go to your bKash Mobile Menu by dialing *247# <br />
																		02. Choose ???Payment??? <br /> 03. Enter the Merchant
																		bKash Account Number <strong>${wallet}</strong> you
																		want to pay to <br /> 04. Enter the amount you want
																		to pay <br /> 05. Enter a reference 1 against your
																		payment <br /> 06. Enter the Counter Number 1 <br />
																		07. Now enter your bKash Mobile Menu PIN to confirm <br />
																	</div>
																	<div id="bangla" style="display: none">
																		??????????????? ????????????????????? ???????????? ??????????????? ????????????????????? ?????????????????? ????????????- <br />
																		?????? *?????????# ??????????????? ????????? ??????????????? ?????????????????? ???????????????????????? ????????? <br />
																		?????? ??????????????????????????? ????????????????????? ???????????? <br /> ?????? ??????????????????????????? ???????????????
																		????????????????????? ?????????????????? <strong>${wallet}</strong> ????????? <br />
																		?????? ??????????????? ??????????????? ?????????????????? ??????????????? <br /> ?????? ???????????????????????????
																		?????????????????? ??? ??????????????? <br /> ?????? ???????????????????????? ?????????????????? ??? ??????????????? <br />
																		?????? ??????????????? ??????????????? ?????????????????? ?????????????????? ??????????????? ???????????? ?????????????????????
																		????????????????????? ???????????? <br />
																	</div>
																</div>
																<div class="col-md-5" id="imgQR">
																	<img src="<%=basePath%>resources/images/bkashQR.png"
																		alt="bkash QR Code" class="responsive" width="180"
																		height="200">
																</div>
															</div>
															<script>
      $("#changeLang").on("click", function() {
        if ($(this).prop("checked")) {
          $("#english").css("display", "none");
          $("#bangla").css("display", "block");
          $(".english1").css("display", "none");
          $(".bangla1").css("display", "block");
          $("#english1").css("display", "none");
          $("#bangla1").css("display", "block");
          $("#englishhead").css("display", "none");
          $("#banglahead").css("display", "block");
        } else {
          $("#english").css("display", "block");
          $("#bangla").css("display", "none");
          $(".english1").css("display", "block");
          $(".bangla1").css("display", "none");
          $("#english1").css("display", "block");
          $("#bangla1").css("display", "none");
          $("#englishhead").css("display", "block");
          $("#banglahead").css("display", "none");
        }
      });
    </script>




														</div>
														<%--  --%>

													</div>
													<p class="cancelOrder"
														style="text-align: center; display: ruby;">
														<style>
</style>
														<a href="${returnUrl }" class="english1"
															id="cancelOrderTag">Cancel Order</a> <b class="english1">and
															return to LebuPay</b> <a href="${returnUrl }" class="bangla1"
															id="cancelOrderTag" style="display: none;">??????????????????
															??????????????? ????????????</a><b class="bangla1" style="display: none;">
															????????? ???????????? ?????????</b>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-1 col-sm-1 col-xs-12"></div>
									</div>

									<div class="payment_page_footer" style="margin-top: 0px">
										<div class="row">
											<div class="col-md-12">
												<p>Payments powered by:</p>
												<ul style="padding-bottom: 0px !important">
													<li><img
														src="<%=basePath%>resources/images/verified-by-visa.png"
														alt="verified by visa"></li>
													<li><img
														src="<%=basePath%>resources/images/mastercard-secure-code.png"
														alt="mastercard secured code"></li>
													<li><img
														src="<%=basePath%>resources/images/american-express-safekey.png"
														alt="american express safekey"></li>
													<li><img
														src="<%=basePath%>resources/images/diner-club-protect.png"
														alt="diners-club-protect"></li>

												</ul>
												<div class="row">
													<div class="col-md-3"></div>
													<div class="col-md-6">
														<p>&copy; All rights reserved to Spider Digital
															Commerce (Bangladesh) Ltd</p>
													</div>
													<div class="col-md-3">
														<div class="mcafee_secured_logo">
															<script src="https://cdn.ywxi.net/js/inline.js?w=90"></script>
															<div
																style="display: inline-block; width: 90px; height: 37px; background-size: cover; background-image: url(&amp;quot;https://cdn.ywxi.net/meter/localhost:8081/102.svg&amp;quot;); cursor: pointer; float: right;"
																class="mfes-trustmark" data-url="/102.svg"
																oncontextmenu="return false;"></div>

														</div>
													</div>
												</div>


												<!-- <div class="mcafee_secured_logo">
													<script src="https://cdn.ywxi.net/js/inline.js?w=90"></script>

												</div> -->
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</section>
		</div>

		<!-- <footer>
		<div class="copyright_sec payment_page_footer_btm">
			<div class="row">
				<div class="col-md-6">
					<p>&copy; All rights reserved to Spider Digital Commerce
						(Bangladesh) Ltd</p>
				</div>
				<div class="col-md-6 mcafee_secured_logo">
					<script src="https://cdn.ywxi.net/js/inline.js?w=90"></script>

				</div>
			</div>
		</div>
		</footer> -->
		<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>
	</div>
	<script>
var bkash ="<%=bkash%>";
	if(bkash == "Y"){
		var div = document.getElementById("bkashDiv");
		if (div.style.display === "none") {

			div.style.display = "block";
			$("#bkash_hov").css("opacity", "1");
		    $("#bkash_hov").css("border", "3px solid green");
		  } else {
			  div.style.display = "none";

		    $("#bkash_hov").css("border", "1px solid #ddd");
		  }
		}
	$(".eachBox").click(function(e){
		e.preventDefault();
		console.log(5443);
		var div = document.getElementById("bkashDiv");
		if (div.style.display === "none") {
		  } else {
			  div.style.display = "none";
		    $("#bkash_hov").css("border", "1px solid #ddd");
		  }
		
		});
	$("#bkashClicked").on("click", function(e){
		e.preventDefault();
		var div = document.getElementById("bkashDiv");
		if (div.style.display === "none") {

			div.style.display = "block";
			$("#bkash_hov").css("opacity", "1");
		    $("#bkash_hov").css("border", "3px solid green");
		  } else {
			  div.style.display = "none";
		    $("#bkash_hov").css("border", "1px solid #ddd");
		  }
		
		});
		
	</script>
	<!-- ./wrapper -->
	<%
		String message = (String) request.getAttribute("transactionidInvalid");
		if (message != null) {
	%>
	<script>
		 		$(document).ready(function(){ jQuery.growl.error({ 
			 		message : "<%=message%>
		"
			});
		});
	</script>
	<%
		}
	%>

	<script type="text/javascript">
		/*
		 var eBLUserName = '${eBLUserName}';
		 var eBLUserPassword = '${eBLUserPassword}';
		 var eBLUserId = '${eBLUserId}';

		 var sEBLUserName = '${sEBLUserName}';
		 var sEBLUserPassword = '${sEBLUserPassword}';
		 var sEBLUserId = '${sEBLUserId}';
		 var cITYMid = '${citybankMerchantId}';/**/
		var ebl = '${EBL}';
		var sebl = '${SEBL}';
		var city = '${CITY}';

		/*
		 if(eBLUserName == "" || eBLUserPassword == "" || eBLUserId==""){
		 $("#sectionA").removeClass("in active");
		 $("#secA").removeClass("active");
		 if(sEBLUserName == "" || sEBLUserPassword == "" || sEBLUserId==""){
		 $("#sectionD").removeClass("in active");
		 $("#secD").removeClass("active");
		 if(cITYMid == ""){
		 $("#sectionC").removeClass("in active");//CITY
		 $("#sec").removeClass("active");//CITY
		 $("#sectionB").addClass("in active");//bkash
		 $("#secB").addClass("active");//bkash
		 }else{
		 $("#sectionC").addClass("in active");//CITY
		 $("#sec").addClass("active");//CITY
		 }
		 }else{
		 $("#sectionD").addClass("in active");//SEBL
		 $("#secD").addClass("active");//SEBL
		 }
		 }/**/

		if (ebl == "N") {
			if (sebl == "N") {
				if (city == "N") {
					$("#secB").show();
					$("#sectionB").show();

					$('#secA').remove();//EBL
					$("#sectionA").remove();
					$('#secC').remove();//CITY
					$("#sectionC").remove();
					$("#secD").remove();//SEBL
					$("#sectionD").remove();
				} else {
					$('#secC').show();
					$("#sectionC").show();

					$('#secA').remove();//EBL
					$("#sectionA").remove();
					$('#secB').remove();//BKASH
					$("#sectionB").remove();
					$("#secD").remove();//SEBL
					$("#sectionD").remove();
				}
			} else {
				$('#secD').show();
				$("#sectionD").show();

				$('#secA').remove();//EBL
				$("#sectionA").remove();
				$('#secB').remove();//BKASH
				$("#sectionB").remove();
				$("#secC").remove();//CITY
				$("#sectionC").remove();
			}
		} else {

			$('#secA').show();
			$("#sectionA").show();

			$('#secD').remove();//SEBL
			$("#sectionD").remove();
			$('#secB').remove();//BKASH
			$("#sectionB").remove();
			$("#secC").remove();//CITY
			$("#sectionC").remove();

		}
		$(".fa").on("click", function(e) {
			$.notify({
				title : 'Transaction Details',
				content : "<b>TrxID:</b> ${transactionId}",
				timeout : 0
			});
		});
	</script>