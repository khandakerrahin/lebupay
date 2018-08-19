<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*"%>
<%@ page import="com.lebupay.model.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<meta name="googlebot" content="noindex">
<title>Merchant Administration | Dashboard</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/bootstrap.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/font-awesome.css">
<!-- Theme style -->
<link rel="stylesheet" href="<%=basePath%>resources/css/AdminLTE.css">

<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/responsive.css">
<link
	href="<%=basePath%>resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">

<!-- Date Picker -->
<link rel="stylesheet" href="<%=basePath%>resources/css/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/daterangepicker.css">
<script src="<%=basePath%>resources/js/login/md5.js"
	type="text/javascript"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
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
</head>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">

		<header class="main-header"> <a href="./dashboard"
			class="logo"> <span class="logo-mini"><b>LP</b></span> <span
			class="logo-lg menu"><h1>LebuPay</h1></span>
		</a> <nav class="navbar navbar-static-top">

		<div class="col-md-12">

			<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
				role="button"> <span class="sr-only">Toggle navigation</span>
			</a>

			<div class="heading">
				<h2>${link}</h2>
			</div>
			<% 
						
						int count = 0;
						/* List<WalletModel> walletModels = new ArrayList<WalletModel>(); */
						MerchantSessionModel merchantModel = (MerchantSessionModel) session.getAttribute("merchantsessionModel");
					
					%>


			<div class="pull-right">
				<div class="btnArea">
					<!-- <button type="button" class="btn btn-warning sandBtn">SANDBOX</button> -->
					<a href="checkout" class="btn btn-warning sandBtn"
						style="display: block;">SANDBOX</a>
				</div>
				<div class="searchBar">
					<%-- <form class="navbar-form" role="search">
              <span class="search-img"><img src="<%=basePath%>resources/images/search.png" /></span>
                <input class="form-control customSearch" placeholder="Search..." name="srch-term" id="srch-term" type="text">
            </form> --%>
				</div>
				<div class="navbar-custom-menu loginArea">
					<ul class="nav navbar-nav">

						<%-- <li class="dropdown messages-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath%>resources/images/settings.png" />
                </a>
              </li> --%>
						<%

							if (merchantModel != null) {

								/* walletModels = merchantModel.getWalletModels();
								if (walletModels.size() > 0) {

									for (WalletModel walletModel : walletModels) {

										if (walletModel.getStatus().ordinal() == 1)
											count++;
									}
								} */
							}
						%>
						<%-- <li class="dropdown notifications-menu"><a href="#"
							class="dropdown-toggle walletUpdate" data-toggle="dropdown">
								<img src="<%=basePath%>resources/images/bell.png" /> <span
								class="label label-warning supVal"><%=count%></span>
						</a>
							<ul class="dropdown-menu">
								<li class="header">You have <%=count%> notifications
								</li>
								<li>
									<ul class="menu">
										<%
											if (merchantModel != null) {

												//walletModels = merchantModel.getWalletModels();
												//if (walletModels.size() > 0) {

													//for (WalletModel walletModel : walletModels) {
										%>
										<li><a href="#"
											data-walletId="<%=walletModel.getWalletId()%>">
												<i class="fa fa-users text-aqua"></i>You Have Received <%=walletModel.getAmount()%>
												from <%=walletModel.getSentMerchantModel()
								.getFirstName()%> on <%=walletModel.getCreatedDate()%>
										</a></li>


										<%
											//}
												//}
											}
										%>
									</ul>
								</li>
								<!-- <li class="footer"><a href="#">View all</a></li> -->
							</ul></li> --%>

						<%-- <li class="dropdown tasks-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath%>resources/images/warning.png" />
                  <span class="label label-danger supVal">9</span>
                </a>
                <ul class="dropdown-menu">
                  <li class="header">You have 9 tasks</li>
                  <li>

                    <ul class="menu">
                      <li>
                        <a href="#">
                          <h3>
                            Design some buttons
                            <small class="pull-right">20%</small>
                          </h3>
                          <div class="progress xs">
                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span class="sr-only">20% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>

                      <li>
                        <a href="#">
                          <h3>
                            Create a nice theme
                            <small class="pull-right">40%</small>
                          </h3>
                          <div class="progress xs">
                            <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span class="sr-only">40% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>

                      <li>
                        <a href="#">
                          <h3>
                            Some task I need to do
                            <small class="pull-right">60%</small>
                          </h3>
                          <div class="progress xs">
                            <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span class="sr-only">60% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>

                      <li>
                        <a href="#">
                          <h3>
                            Make beautiful transitions
                            <small class="pull-right">80%</small>
                          </h3>
                          <div class="progress xs">
                            <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span class="sr-only">80% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>

                    </ul>
                  </li>
                  <li class="footer">
                    <a href="#">View all tasks</a>
                  </li>
                </ul>
              </li> --%>

						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> <c:choose>
									<c:when test="${merchantModel.getLogoName() == null}">
										<img src="<%=basePath%>resources/images/men.png"
											class="user-image" alt="User Image">
									</c:when>
									<c:otherwise>
										<img
											src="merchant-logo-draw?imageName=${merchantModel.getLogoName()}"
											class="user-image" alt="User Image">
									</c:otherwise>
								</c:choose> <span class="hidden-xs">${merchantModel.getFirstName()}
									${merchantModel.getLastName()}</span> <img
								src="<%=basePath%>resources/images/dropdown-icon.png">
						</a>
							<ul class="dropdown-menu">

								<li class="user-header"><c:choose>
										<c:when test="${merchantModel.getLogoName() == null}">
											<img src="<%=basePath%>resources/images/men.png"
												class="user-image noFloat" alt="User Image">
										</c:when>
										<c:otherwise>
											<img
												src="merchant-logo-draw?imageName=${merchantModel.getLogoName()}"
												class="user-image  noFloat" alt="User Image">
										</c:otherwise>
									</c:choose>

									<p>
										${merchantModel.getFirstName()} ${merchantModel.getLastName()}
										<small></small>
									</p></li>

								<!-- <li class="user-body">
                    <div class="row">
                      <div class="col-xs-4 text-center">
                        <a href="#">Followers</a>
                      </div>
                      <div class="col-xs-4 text-center">
                        <a href="#">Sales</a>
                      </div>
                      <div class="col-xs-4 text-center">
                        <a href="#">Friends</a>
                      </div>
                    </div>

                  </li> -->

								<li class="user-footer">
									<div class="profile">
										<a href="./edit-profile" class="btn btn-default btn-flat">Profile</a>
									</div>
									<div class="edit_comp">
										<a href="./edit-company" class="btn btn-default btn-flat">Edit Company</a>
									</div>
									<div class="signOut">
										<a href="logout" class="btn btn-default btn-flat">Sign out</a>
									</div>
								</li>
							</ul></li>

					</ul>
				</div>
			</div>

		</div>

		</nav> </header>

		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar"> <!-- sidebar: style can be found in sidebar.less -->
		<section class="sidebar"> <!-- Sidebar user panel -->
		<div class="user-panel profileArea">
			<div class="image">
				<c:choose>
					<c:when test="${merchantModel.getLogoName() == null}">
						<img src="<%=basePath%>resources/images/men.png"
							class="img-circle user-image" alt="User Image">
					</c:when>
					<c:otherwise>
						<img
							src="merchant-logo-draw?imageName=${merchantModel.getLogoName()}"
							class="img-circle user-image" alt="User Image">
					</c:otherwise>
				</c:choose>

			</div>
			<div class="info">
				<h2>${merchantModel.getFirstName()}
					${merchantModel.getLastName()}</h2>
				<!-- <span>Art Director</span> -->
				<a href="./edit-profile">Edit Profile</a>
				<!-- <a href="#"><i class="fa fa-circle text-success"></i> Online</a> -->
			</div>
		</div>
		<div class="walletArea">
			<div class="leftTxt">
				<h4>Transaction Balance <span>&#2547;</span> <fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${transactionAmount}" /></h4>
			</div>
			
			<div class="leftTxt">
				<h4>Loyalty Point <span><fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${loyaltyPoint}" /></span></h4>
			</div>
			
			<!-- <div class="walletBtn">
				<a href="add-cash"><button type="button" class="btn keyBtn">
						<i class="fa fa-plus" aria-hidden="true"></i> ADD CASH
					</button></a> <a href="withdrawcash">
					<button type="button" class="btn keyBtn slickBtn" style="background-color: #ee592f!important;border: none;">WITHDRAW
						CASH</button>
				</a> <a href="wallet-transfer">
					<button type="button" class="btn keyBtn slickBtn" style="background-color: #66ab6f!important;border: none;">SEND
						CASH</button>
				</a>
				<a href="merchant-show-transactions">
					<button type="button" class="btn keyBtn slickBtn" style="background-color: #01b6d3!important;border: none;">SHOW TRANSACTION</button>
				</a>
			</div> -->
		</div>

		<!-- sidebar menu: : style can be found in sidebar.less --> <%-- <ul class="custom-menu off-sidebar">
			<li><a href="javascript:void(0)"> <img
					src="<%=basePath%>resources/images/exclusive-icon.png" />Saved
					Payment Options</span>
			</a>
				<ul class="menuPad">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<!-- Wrapper for slides -->
						<div class="carousel-inner" role="listbox">

							<c:forEach var="cardModel" items="${merchantModel.cardModels }"
								varStatus="loop">

								<div
									class="item <c:if test="${merchantModel.defaultType == 'CARD_MASTER' &&  merchantModel.defaultId == cardModel.cardId}">active</c:if>">
									<!-- <img src="images/card.png" alt="Chania" width="460" height="345"> -->
									<div class="row">
										<div class="debitCard clearfix">
											<div class="col-md-6 col-sm-4 col-xs-4">
												<img src="#" data-card="${cardModel.cardNumber }" />
											</div>
											<div class="col-md-6 col-sm-8 col-xs-8">
												<div class="bankName">
													<c:if
														test="${merchantModel.defaultType == 'CARD_MASTER' &&  merchantModel.defaultId == cardModel.cardId}">
														<h2>Default</h2>
													</c:if>
												</div>
												<div class="bankCaptcha">
													<input type="hidden" name="defaultId"
														value="${cardModel.cardId }"> <input type="hidden"
														name="defaultType" value="CARD_MASTER"> <label
														class="switch"> <input type="checkbox"
														value="card_${loop.index }"
														<c:if test="${merchantModel.defaultType == 'CARD_MASTER' &&  merchantModel.defaultId == cardModel.cardId}">checked</c:if>>
														<div class="slider round"></div>
													</label>
												</div>
											</div>
											<div class="col-md-12 col-sm-12 col-xs-12">
												<ul>
													<li>XXXX</li>
													<li>XXXX</li>
													<li>XXXX</li>
													<li>${fn:substring(cardModel.cardNumber, 12, 16)}</li>
												</ul>
											</div>
											<div class="col-md-12 col-sm-12 col-xs-12 vertpad">
												<div class="row">
													<div class="col-md-3 col-sm-3 col-xs-12">
														<h2>
															VALID <br />THRU
														</h2>
													</div>
													<div class="col-md-7 col-sm-7 col-xs-12">
														<h3>${cardModel.expiryMonth}/${cardModel.expiryYear}</h3>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

							</c:forEach>
							<c:forEach var="netBankingModel"
								items="${merchantModel.netBankingModels }" varStatus="loop">
								<div
									class="item <c:if test="${merchantModel.defaultType == 'NETBANKING_MASTER' &&  merchantModel.defaultId == netBankingModel.netBankingId}">active</c:if>">
									<!-- <img src="images/card.png" alt="Flower" width="460" height="345"> -->
									<div class="row">
										<div class="debitCard netBanking clearfix">

											<div class="col-md-5 col-sm-5 col-xs-12">


												<img src="<%=basePath%>resources/images/card-logo.png" />
												<img
													src="bank-logo-draw?imageName=${netBankingModel.getLogoName()}">
												<h3 style="text-align: center;">${netBankingModel.bankName}</h3>



											</div>
											<div class="col-md-7 col-sm-7 col-xs-12">
												<div class="bankName">
													<c:if
														test="${merchantModel.defaultType == 'NETBANKING_MASTER' &&  merchantModel.defaultId == netBankingModel.netBankingId}">
														<h2>Default</h2>
													</c:if>
												</div>
												<div class="bankCaptcha">
													<input type="hidden" name="defaultId"
														value="${netBankingModel.netBankingId }"> <input
														type="hidden" name="defaultType" value="NETBANKING_MASTER">
													<label class="switch"> <input type="checkbox"
														value="netbank_${loop.index }"
														<c:if test="${merchantModel.defaultType == 'NETBANKING_MASTER' &&  merchantModel.defaultId == netBankingModel.netBankingId}">checked</c:if>>
														<div class="slider round"></div>
													</label>
												</div>
											</div>

										</div>
									</div>
								</div>
							</c:forEach>

						</div>

						<!-- Left and right controls -->
						<a class="left carousel-control customArrow" href="#myCarousel"
							role="button" data-slide="prev"> <!-- <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> -->
							<img src="<%=basePath%>resources/images/left-slide.png" />
						</a> <a class="right carousel-control customArrow-right"
							href="#myCarousel" role="button" data-slide="next"> <!-- <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span> -->
							<img src="<%=basePath%>resources/images/right-slide.png" />
						</a>
					</div>
					<div class="walletBtn">
						<a href="add-card">
							<button type="button" class="btn keyBtn">
								<i class="fa fa-plus" aria-hidden="true"></i> ADD NEW CARD
							</button>
						</a> <a href="show-all">
							<button type="button" class="btn keyBtn slickBtn">SHOW
								ALL</button>
						</a>

					</div>
					<div id="serverRes" style="display: none;">
						<img src="<%=basePath%>resources/images/spinner.gif"
							alt="loading...">
					</div>
					<div></div>
				</ul></li>
			<li><a href="javascript:void(0)"> <img
					src="<%=basePath%>resources/images/payment-icon.png" />Exclusive
					offers</span>
			</a>
				<ul class="offerPad">
					<li><a href="index.html">
							<div class="leftImg">
								<img src="<%=basePath%>resources/images/offer-icon1.png" />
							</div>
							<div class="rightImg">
								<img src="<%=basePath%>resources/images/cart.png" />
							</div>
					</a></li>
					<li><a href="index.html">
							<div class="leftImg">
								<img src="<%=basePath%>resources/images/offer-icon2.png" />
							</div>
							<div class="rightImg">
								<img src="<%=basePath%>resources/images/cart.png" />
							</div>
					</a></li>
					<li><a href="index.html">
							<div class="leftImg">
								<img src="<%=basePath%>resources/images/offer-icon3.png" />
							</div>
							<div class="rightImg">
								<img src="<%=basePath%>resources/images/cart.png" />
							</div>
					</a></li>
					<li><a href="index.html">
							<div class="leftImg">
								<img src="<%=basePath%>resources/images/offer-icon4.png" />
							</div>
							<div class="rightImg">
								<img src="<%=basePath%>resources/images/cart.png" />
							</div>
					</a></li>
				</ul>
				<div class="walletArea">
					<button type="button" class="btn grnBtn">SHOW ALL</button>
				</div></li>
		</ul> --%> </section> <!-- /.sidebar --> </aside>
