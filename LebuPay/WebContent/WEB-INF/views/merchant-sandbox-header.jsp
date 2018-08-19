<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
<link rel="stylesheet"
	href="<%=basePath%>resources/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/font-awesome.css">

<!-- tab cum accordian css -->
<!-- <link rel="stylesheet" href="css/easy-responsive-tabs.css">
  <link href="http://www.jqueryscript.net/css/jquerysctipttop.css" rel="stylesheet" type="text/css"> -->
<!-- color picker css -->
<link href="<%=basePath%>resources/css/color-picker.min.css"
	rel="stylesheet">
<!-- Theme style -->
<link rel="stylesheet" href="<%=basePath%>resources/css/AdminLTE.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/responsive.css">
<link
	href="<%=basePath%>resources/css/css.css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i"
	rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">

<!-- jQuery 2.2.3 -->
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
			class="logo"> <span class="logo-mini"><b>PG</b></span> <span
			class="logo-lg menu"><h1>LebuPay</h1></span>
		</a> <nav class="navbar navbar-static-top">

		<div class="col-md-12">

			<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
				role="button"> <span class="sr-only">Toggle navigation</span>
			</a>

			<div class="heading">
				<h2>${link}</h2>
			</div>
			<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
			<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
			<%
	String path22 = request.getContextPath();
	String basePath22 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path22 + "/";
%>

			<%@ page import="java.util.*"%>
			<%@ page import="com.lebupay.model.*"%>
			<%List<String> menus = new ArrayList<String>(); %>
			<div class="pull-right">

				<% 
						
						int count = 0;
						/* List<WalletModel> walletModels = new ArrayList<WalletModel>(); */
						MerchantSessionModel merchantModel = (MerchantSessionModel) session.getAttribute("merchantModel");
					
					
					%>
				<div class="navbar-custom-menu loginArea">
					<ul class="nav navbar-nav">

						<%--  <li class="dropdown messages-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath22%>resources/images/settings.png" />
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
								<img src="<%=basePath22%>resources/images/bell.png" /> <span
								class="label label-warning supVal"><%=count%></span>
						</a>
							<ul class="dropdown-menu">
								<li class="header">You have <%=count%> notifications
								</li>
								<li>
									<ul class="menu">
										<%
											if (merchantModel != null) {

												/* walletModels = merchantModel.getWalletModels();
												if (walletModels.size() > 0) {

													for (WalletModel walletModel : walletModels) { */
										%>
										<li><a href="#"
											data-walletId="<%=walletModel.getWalletId()%>">
												<i class="fa fa-users text-aqua"></i>You Have Received <%=walletModel.getAmount()%>
												from <%=walletModel.getSentMerchantModel()
								.getFirstName()%> on <%=walletModel.getCreatedDate()%>
										</a></li>


										<%
											/* }
												} */
											}
										%>
									</ul>
								</li>
								<!-- <li class="footer"><a href="#">View all</a></li> -->
							</ul></li> --%>

						<%-- <li class="dropdown tasks-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath22%>resources/images/warning.png" />
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
										<img src="<%=basePath22%>resources/images/men.png"
											class="user-image" alt="User Image">
									</c:when>
									<c:otherwise>
										<img
											src="merchant-logo-draw?imageName=${merchantModel.getLogoName()}"
											class="user-image" alt="User Image">
									</c:otherwise>
								</c:choose> <span class="hidden-xs"> ${merchantModel.getFirstName()}
									${merchantModel.getLastName()}</span> <img
								src="<%=basePath22%>resources/images/dropdown-icon.png">
						</a>
							<ul class="dropdown-menu">

								<li class="user-header"><c:choose>
										<c:when test="${merchantModel.getLogoName() == null}">
											<img src="<%=basePath22%>resources/images/men.png"
												class="user-image noFloat" alt="User Image">
										</c:when>
										<c:otherwise>
											<img
												src="merchant-logo-draw?imageName=${merchantModel.getLogoName()}"
												class="user-image noFloat" alt="User Image">
										</c:otherwise>
									</c:choose>

									<p>
										${merchantModel.getFirstName()} ${merchantModel.getLastName()}
										<!--   <small>Member since Nov. 2012</small> -->
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
						<img src="<%=basePath22%>resources/images/men.png"
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
				<!-- <a href="javascript:void(0)"><i class="fa fa-circle text-success"></i> Online</a> -->
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


		</section> <!-- /.sidebar --> </aside>

		<!-- Content Wrapper. Contains page content -->

		<div class="content-wrapper">
			<section class="content">
			<div class="row sandBoxTab">
				<div class="col-xs-12">
					<div class="customNav">
						<nav class="navbar navbar-default">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle collapsed"
								data-toggle="collapse" data-target="#navbar2">
								<span class="sr-only">Toggle navigation</span> <span
									class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
						</div>
						<div id="navbar2" class="navbar-collapse collapse">
							<ul class="nav navbar-nav navbar-right">




								<li><a href="checkout">
										<div class="tabImg">
											<img class="outImg"
												src="<%=basePath22%>resources/images/s2.png" /> <img
												class="overImg"
												src="<%=basePath22%>resources/images/s2-hover.png" />
										</div>
										<div class="tabTxt">
											<h2>
												Checkout <br />Page Settings
											</h2>
										</div>
								</a></li>

								<li><a href="email-invoicing">
										<div class="tabImg">
											<img class="outImg"
												src="<%=basePath22%>resources/images/s5.png" /> <img
												class="overImg"
												src="<%=basePath22%>resources/images/s5-hover.png" />
										</div>
										<div class="tabTxt">
											<h2>
												Email <br />Invoicing
											</h2>
										</div>
								</a></li>

								<li><a href="quickpay">
										<div class="tabImg">
											<img class="outImg"
												src="<%=basePath22%>resources/images/s6.png" /> <img
												class="overImg"
												src="<%=basePath22%>resources/images/s6-hover.png" />
										</div>
										<div class="tabTxt">
											<h2>Quick-Pay</h2>
										</div>
								</a></li>
								
								
								<li><a href="link">
										<div class="tabImg">
											<img class="outImg"
												src="<%=basePath22%>resources/images/s6.png" /> <img
												class="overImg"
												src="<%=basePath22%>resources/images/s6-hover.png" />
										</div>
										<div class="tabTxt">
											<h2>Pay via Link</h2>
										</div>
								</a></li>

								<li>
									<a href="customer-care" externalSel="view-ticket">
											<div class="tabImg">
												<img class="outImg"
													src="<%=basePath22%>resources/images/s9.png" /> <img
													class="overImg"
													src="<%=basePath22%>resources/images/s9-hover.png" />
											</div>
											<div class="tabTxt">
												<h2>
													Customer <br />Care
												</h2>
											</div>
									</a>
								</li>
								
								<li>
									<a href="card-percentage">
										<div class="tabImg">
											<img class="outImg" src="<%=basePath22%>resources/images/card-percentage.png" /> 
											<img class="overImg" src="<%=basePath22%>resources/images/card-percentage.png" />
										</div>
										<div class="tabTxt">
											<h2>
												Card <br />Percentage
											</h2>
										</div>
									</a>
								</li>
							</ul>
						</div>
						<!--/.nav-collapse --> </nav>
					</div>
					<div class="resp-tabs-container">
						<div class="sandBox">