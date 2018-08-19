<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ page import="com.lebupay.model.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex">
<meta name="googlebot" content="noindex">
<title>LebuPay Administration | Dashboard</title>
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
<!-- Theme style -->
<link rel="stylesheet" href="<%=basePath%>resources/css/AdminLTE.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/custom.css">
<link rel="stylesheet" href="<%=basePath%>resources/css/responsive.css">
<link href="<%=basePath%>resources/css/css.css" rel="stylesheet">
<!-- Date Picker -->
<link rel="stylesheet" href="<%=basePath%>resources/css/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="<%=basePath%>resources/css/daterangepicker.css">
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>resources/images/favicon.ico">

<script src="<%=basePath%>resources/js/jquery-3.1.1.min.js"></script>


<script src="<%=basePath%>resources/js/ckeditor/ckeditor.js"></script>
<script src="<%=basePath%>resources/js/ckeditor/samples/js/sample.js"></script>
<link rel="stylesheet"
	href="<%=basePath%>resources/js/ckeditor/samples/toolbarconfigurator/lib/codemirror/neo.css">
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

		<header class="main-header">
			<a href="./dashboard" class="logo"> <span class="logo-mini"><!-- <b>PG</b> --></span>
				<span class="logo-lg menu"><h1>Lebupay</h1></span>
			</a>

			<nav class="navbar navbar-static-top">
				<div class="col-md-12">
					<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
						role="button"> <span class="sr-only">Toggle navigation</span>
					</a>

					<div class="heading">
			          <h2>${link}</h2>
			        </div>

					<div class="notification pull-right">
						<div class="navbar-custom-menu loginArea">
							<ul class="nav navbar-nav">

								<%-- <li class="dropdown messages-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath3%>resources/images/settings.png" />
                </a>
              </li>

              <li class="dropdown notifications-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath3%>resources/images/bell.png" />
                  <span class="label label-warning supVal">10</span>
                </a>
                <ul class="dropdown-menu">
                  <li class="header">You have 10 notifications</li>
                  <li>
                    <ul class="menu">
                      <li>
                        <a href="#">
                          <i class="fa fa-users text-aqua"></i> 5 new members joined today
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <i class="fa fa-warning text-yellow"></i> Very long description here that may not fit into the
                          page and may cause design problems
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <i class="fa fa-users text-red"></i> 5 new members joined
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <i class="fa fa-shopping-cart text-green"></i> 25 sales made
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <i class="fa fa-user text-red"></i> You changed your username
                        </a>
                      </li>
                    </ul>
                  </li>
                  <li class="footer"><a href="#">View all</a></li>
                </ul>
              </li>

              <li class="dropdown tasks-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="<%=basePath3%>resources/images/warning.png" />
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
								<%
	String path3 = request.getContextPath();
	String basePath3 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path3 + "/";
%>
								<li class="dropdown user user-menu"><a href="#"
									class="dropdown-toggle" data-toggle="dropdown"> <img
										src="<%=basePath3%>resources/images/login-men.png"
										class="user-image" alt="User Image"> <span
										class="hidden-xs"> ${adminModel.firstName }
											${adminModel.lastName }</span> <img
										src="<%=basePath3%>resources/images/dropdown-icon.png">
								</a>
									<ul class="dropdown-menu">

										<li class="user-header"><img
											src="<%=basePath3%>resources/images/men.png"
											class="img-circle" alt="User Image">

											<p>${adminModel.firstName }&nbsp;${adminModel.lastName }</p></li>

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
											<div class="pull-left">
												<a href="./edit-admin" class="btn btn-default btn-flat">Profile</a>
											</div>
											<div class="pull-right">
												<a href="./logout" class="btn btn-default btn-flat">Sign
													out</a>
											</div>
										</li>
									</ul></li>

							</ul>
						</div>
						<%-- <div class="searchBar">
            <form class="navbar-form" role="search">
              <span class="search-img"><img src="<%=basePath3%>resources/images/search.png" /></span>
                <input class="form-control customSearch" placeholder="Search..." name="srch-term" id="srch-term" type="text">
            </form>
          </div> --%>
					</div>

				</div>

			</nav>
		</header>

		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- Sidebar user panel -->
				<div class="user-panel profileArea">
					<div class="image">
						<img src="<%=basePath3%>resources/images/men.png"
							class="img-circle" alt="User Image">
					</div>
					<div class="info">
						<h2>${adminModel.firstName }&nbsp;${adminModel.lastName }</h2>
						<a href="./edit-admin">Edit Profile</a>
						<!-- <a href="#"><i class="fa fa-circle text-success"></i> Online</a> -->
					</div>
				</div>
				<ul class="custom-menu sidebar-menu">
					<li class="treeview active"><a href="./dashboard"> <img
							src="<%=basePath3%>resources/images/dashboard.png"><span
							class="sidetxt">Dashboard</span>
					</a> <!-- <ul class="treeview-menu">
              <li class=""><a href="javascript:void(0)"><i class="fa fa-circle-o"></i> Dashboard 1</a></li>
              <li><a href="javascript:void(0)"><i class="fa fa-circle-o"></i> Dashboard 2</a></li>
              <li><a href="javascript:void(0)"><i class="fa fa-circle-o"></i> Dashboard 3</a></li>
            </ul> --></li>
					<%-- <li class="treeview"><a href="javascript:void(0)"> <img
					src="<%=basePath3%>resources/images/features.png"><span
					class="sidetxt">Role Management</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="./add-role"><img
							src="<%=basePath3%>resources/images/a1.png" />Add Role</a></li>
					<li><a href="./list-role"><img
							src="<%=basePath3%>resources/images/a2.png" /> List Role</a></li>

				</ul></li>
			<li class="treeview"><a href="javascript:void(0)"> <img
					src="<%=basePath3%>resources/images/features.png"><span
					class="sidetxt">Sub Admin</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="./add-subadmin"><img
							src="<%=basePath3%>resources/images/a1.png" />Add Sub Admin</a></li>
					<li><a href="./list-sub-admin"><img
							src="<%=basePath3%>resources/images/a2.png" /> List Sub Admin</a></li>

				</ul></li> --%>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Merchant Management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./list-active-merchant"><img
									src="<%=basePath3%>resources/images/a2.png" />Merchant List</a></li>
							<%-- <li><a href="./list-active-merchant"><img
									src="<%=basePath3%>resources/images/a2.png" /> List De-Active
									Merchant</a></li> --%>
						</ul></li>
					<%-- <li class="treeview"><a href="javascript:void(0)"> <img
					src="<%=basePath3%>resources/images/features.png"><span
					class="sidetxt">Customer</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="./list-customer"><img
							src="<%=basePath3%>resources/images/a2.png" /> List Customer</a></li>
				</ul></li> --%>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Ticket Management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./list-ticket-merchant"><img
									src="<%=basePath3%>resources/images/a2.png" /> List Ticket
									Merchant</a></li>
							<%-- <li><a href="./list-ticket-customer"><img
							src="<%=basePath3%>resources/images/a2.png" /> List Ticket
							Customer</a></li> --%>
						</ul></li>
					<%-- <li class="treeview"><a href="javascript:void(0)"> <img
					src="<%=basePath3%>resources/images/features.png"><span
					class="sidetxt">Bank</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="./add-bank"><img
							src="<%=basePath3%>resources/images/a1.png" />Add Bank</a></li>
					<li><a href="./list-bank"><img
							src="<%=basePath3%>resources/images/a2.png" /> List Bank</a></li>
				</ul></li>
			<li class="treeview"><a href="javascript:void(0)"> <img
					src="<%=basePath3%>resources/images/features.png"><span
					class="sidetxt">Contact Us</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="./list-query"><img
							src="<%=basePath3%>resources/images/a1.png" />Contact Us List</a></li>
				</ul></li> --%>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">FAQ Management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./add-faq"><img
									src="<%=basePath3%>resources/images/a1.png" />Add FAQ</a></li>
							<li><a href="./list-faq"><img
									src="<%=basePath3%>resources/images/a2.png" />List FAQ</a></li>
						</ul></li>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Banner Management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./add-banner"><img
									src="<%=basePath3%>resources/images/a1.png" />Add Banner</a></li>
							<li><a href="./list-banner"><img
									src="<%=basePath3%>resources/images/a2.png" />List Banner</a></li>
						</ul></li>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Loyalty Management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./amount-to-point"><img
									src="<%=basePath3%>resources/images/a1.png" />Add Point For
									Amount</a></li>
							<li><a href="./point-to-amount"><img
									src="<%=basePath3%>resources/images/a2.png" />Add Amount For
									Point</a></li>
						</ul></li>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Content management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./add-content-management"><img
									src="<%=basePath3%>resources/images/a1.png" />Add Content
									management</a></li>
							<li><a href="./list-content"><img
									src="<%=basePath3%>resources/images/a2.png" />List Content
									management</a></li>
						</ul></li>
					<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Transaction management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./list-disburse-transaction"><img
									src="<%=basePath3%>resources/images/a2.png" />List Disburse
									Transactions</a></li>
							<li><a href="./list-claimed-transaction"><img
									src="<%=basePath3%>resources/images/a2.png" />List Claimed
									Transactions</a></li>
							<li><a href="./list-unclaimed-transaction"><img
									src="<%=basePath3%>resources/images/a2.png" />List UnClaimed
									Transactions</a></li>
						</ul></li>
						
						<li class="treeview"><a href="javascript:void(0)"> <img
							src="<%=basePath3%>resources/images/features.png"><span
							class="sidetxt">Contact Us management</span>
					</a>
						<ul class="treeview-menu">
							<li><a href="./list-contactus"><img
									src="<%=basePath3%>resources/images/a2.png" />List Contact Us</a></li>
						</ul></li>
					<!-- <li class="treeview active">
            <a href="javascript:void(0)">
              <img src="images/layout-icon.png"><span class="sidetxt">Layouts</span>
            </a>
            <ul class="treeview-menu">
              <li><a href="pages/charts/chartjs.html"><img src="images/l1.png" /> Page Layouts</a></li>
              <li><a href="pages/charts/morris.html"><img src="images/l2.png" /> Sidebar Layouts</a></li>
              <li><a href="pages/charts/flot.html"><img src="images/l3.png" /> Custom Layouts</a></li>
            </ul>
          </li> -->
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">