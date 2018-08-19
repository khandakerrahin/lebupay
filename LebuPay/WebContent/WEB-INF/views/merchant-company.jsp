<%@ include file="merchant-header.jsp"%>
<script src="<%=basePath%>resources/js/jquery-2.2.3.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>resources/css/merchant_responsive.css">
<!-- Main content -->
<section class="content-wrapper res-m-t">
	<div class="sandBox bg-white">
		<div class="sandBoxWrapper newAcc">
			<div class="pad_20 lessPadSm">

				<section class="formWrapper">
					<div class="fullContainer">

						<!--upperSection start-->

						<div class="row upperSection">
							<div class="col-md-8 col-lg-8 ">
								<h1>
									Merchant Registration form<span>: Part 1</span>
								</h1>
								<div class="cardSec">
									<ul>
										<li><a href="#"><img
												src="<%=basePath%>resources/images/ebl-skypay.jpg" alt=""
												width="270" height="50"></a></li>
										<li><a href="#"><img
												src="<%=basePath%>resources/images/diners_club.png" alt=""
												></a></li>
										<li><a href="#"><img
												src="<%=basePath%>resources/images/mastercard.png" alt=""
												></a></li>
										<li><a href="#"><img
												src="<%=basePath%>resources/images/visa.png" alt=""
												></a></li>
									</ul>
								</div>
							</div>
							<div class="col-md-4 col-lg-4">
								<ul class="logoSec">
									<li><a href="#"><img
											src="<%=basePath%>resources/images/lebupay-logo.jpg" alt=""
											width="215" height="153"></a></li>
									<li><a href="#"><img
											src="<%=basePath%>resources/images/eastern-math-logo.jpg"
											alt="" width="162" height="153"></a></li>
								</ul>
							</div>
						</div>

						<!--upperSection end-->

						<form id="merchantRegistration" class="registrationForm"
							name="registrationForm" method="post" action="company-info">
							<input type="hidden" name="csrfPreventionSalt"
								value="<%=request.getAttribute("csrfPreventionSaltPage")%>" />
							<div class="row">

								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12  bankUse">
									<span>Bank use only</span>
								</div>
								<div class="col-md-5 col-lg-5 col-sm-6 col-xs-12 marchantCategory">
									<label>Merchant Category Code</label> <input type="text"
										class="codeSec" name="codeSec" value="${companyModel.codeSec}">
								</div>
								<div class="col-md-4 col-lg-4 col-sm-6 col-xs-12 marchantId">
									<label>Merchant ID</label> <input type="text" class="codeSec" value="${merchantId}"
										readonly="readonly">
								</div>

							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 profileHdng">
									<h2>Merchant Profile</h2>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<label for="registered">Registered as:</label> <input
										type="text" name="companyName" class="registed"
										id="registered" value="${companyModel.companyName}">
								</div>
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<label for="dba">DBA:</label> <input type="text" name="dba"
										class="registed" id="dba" value="${companyModel.dba}">
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<div class="leftColumn form-group eachFld typeFld">
										<label>Type:</label>
										<div class="checkgroup" id="abc">
											

											<div class="checkboxSec ">
												<label for="proprietor"><input type="checkbox"
													name="services" value="Proprietor">Proprietor</label>
											</div>
											<div class="checkboxSec">
												<label for="partnership"><input type="checkbox"
													name="services" value="Partnership" id="Partnership">Partnership</label>
											</div>
											<div class="checkboxSec">
												<label for="limitedcompany"><input type="checkbox"
													name="services" value="Limited company">Limited
													company</label>
											</div>
											<div class="checkboxSec">
												<label for="trust"><input type="checkbox"
													value="Trust" name="services">Trust</label>
											</div>
											
											<c:if test="${not empty companyModel.services}">
												<c:forEach items="${companyModel.services}" var="element">
												
												<script type="text/javascript">
												
												var userId = '${element}';
												
												if(userId == 'Partnership'){
													$('[value="Partnership"]').prop("checked", true);
												}
												if(userId == 'Limited company'){
													$('[value="Limited company"]').prop("checked", true);
												}
												if(userId == 'Proprietor'){
													$('[value="Proprietor"]').prop("checked", true);
												}
												if(userId == 'Trust'){
													$('[value="Trust"]').prop("checked", true);
												}
												
												</script>
												
												</c:forEach>


											</c:if>
											
											
											
											
											
											
										</div>

									</div>
									<div class="rightColumn form-group eachFld businessFld">
										<label for="business">Years in Business:</label> <input
											type="text" id="business" class="business"
											name="yearsInBusiness"
											value="${companyModel.yearsInBusiness}">

									</div>
								</div>

							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<div class="leftColumn form-group eachFld typeFld">
										<label for="phnNbr">Phone:</label> <input type="text"
											id="phnNbr" class="phnNbr" name="phone"
											value="${companyModel.phone}">


									</div>
									<div class="rightColumn form-group eachFld businessFld">
										<label for="fax">Fax:</label> <input type="text" id="fax"
											class="fax" name="fax" value="${companyModel.fax}">

									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<div class="leftColumn form-group eachFld typeFld">
										<label for="site">Website:</label> <input type="text"
											id="site" class="site" name="website"
											value="${companyModel.website}">


									</div>
									<div class="rightColumn form-group eachFld businessFld">
										<label for="ip">IP:</label> <input type="text" id="ip"
											class="ip" name="ip" value="${companyModel.ip}">

									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld typeFld">
									<label for="address">Address:</label> <input type="text"
										id="address" class="address" name="address"
										value="${companyModel.address}">

								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld typeFld">
									<label for="worth">Net Worth:</label> <input type="text"
										id="worth" class="worth" name="netWorth"
										value="${companyModel.netWorth}">

								</div>
								<div class="col-md-12 col-lg-12 form-group eachFld othersBank">
									<label>Other Bank IPG:</label>
									<div class="checkgroup" id="def">
										<div class="checkboxSec ">
											<label for="brac"><input type="checkbox" value="BRAC"
												name="otherBanks">BRAC</label>
										</div>
										<div class="checkboxSec">
											<label for="city"><input type="checkbox" value="city"
												name="otherBanks">city</label>
										</div>
										<div class="checkboxSec">
											<label for="dutch"><input type="checkbox"
												value="Dutch-Bangla" name="otherBanks">Dutch-Bangla</label>
										</div>
										<div class="checkboxSec otherCheckBox">
											<label for="others"><input type="checkbox" id="oth"
												value="">Others:</label>
										</div>
										<input type="text" name="others" id="others"
											disabled="disabled" value="${companyModel.others}">
											
										<c:if test="${not empty companyModel.otherBanks}">
												<c:forEach items="${companyModel.otherBanks}" var="element">
												
												<script type="text/javascript">
												
												var userId = '${element}';
												
												if(userId == 'BRAC'){
													$('[value="BRAC"]').prop("checked", true);
												}
												if(userId == 'city'){
													$('[value="city"]').prop("checked", true);
												}
												if(userId == 'Dutch-Bangla'){
													$('[value="Dutch-Bangla"]').prop("checked", true);
												}
												
												
												</script>
												
												</c:forEach>


											</c:if>	
											
											<c:if test="${not empty companyModel.others}">
                                              <script type="text/javascript">
                                              $("#oth").prop("checked", true);
                                              $('#others').attr('disabled', false);
                                              </script>
                                            </c:if>
											
											
											
											
											
									</div>



								</div>
								<div class="col-md-12 col-lg-12 form-group eachFld typeFld">
									<label for="mechandising">merchandising:</label> <input
										type="text" id="mechandising" class="mechandising"
										name="mechandising" value="${companyModel.mechandising}">

								</div>
							</div>

							<div class="row">

								<div class="col-md-12 col-lg-12 form-group eachFld typeFld">
									<label for="contactPerson">Contact Person:</label> <input
										type="text" id="contactPerson" class="contactPerson"
										name="contactPerson" value="${companyModel.contactPerson}">

								</div>

								<div class="col-md-12 col-lg-12 form-group eachFld typeFld">
									<label for="designation">Designation:</label> <input
										type="text" id="designation" class="designation"
										name="designation" value="${companyModel.designation}">

								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<div class="leftColumn form-group eachFld typeFld">
										<label for="email">Email:</label> <input type="email"
											id="email" class="email" name="email"
											value="${companyModel.email}">


									</div>
									<div class="rightColumn form-group eachFld businessFld">
										<label for="mob">Mobile:</label> <input type="text" id="mob"
											class="mob" name="mobile" value="${companyModel.mobile}">

									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 form-group eachFld">
									<div class="leftColumn form-group eachFld typeFld">
										<label for="email">NID:</label> <input type="text" id="email"
											class="email" name="nid" value="${companyModel.nid}">


									</div>
									<div class="rightColumn form-group eachFld businessFld">
										<label for="mob">Pan No:</label> <input type="text" id="mob"
											class="mob" name="panNo" value="${companyModel.panNo}">

									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-12 col-lg-12 transaction">
									<label>Transaction Profile:</label>
									<div class="profileBox">
										<label>Projected number of transactions per month</label> <input
											type="text" class="" name="projectNoOfTpm"
											value="${companyModel.projectNoOfTpm}">
									</div>
									<div class="profileBox ">
										<label>Projected volume of transactions per month</label> <input
											type="text" class="" name="projectVolOfTpm"
											value="${companyModel.projectVolOfTpm}">
									</div>
									<div class="profileBox">
										<label>Maximum amount in a single transaction</label> <input
											type="text" class="" name="maxAmtSt"
											value="${companyModel.maxAmtSt}">
									</div>
									<div class="profileBox">
										<label>Maximum number of transactions per day</label> <input
											type="text" class="" name="maxNoTpd"
											value="${companyModel.maxNoTpd}">
									</div>
									<div class="profileBox">
										<label>Maximum valume of transactions per day</label>. <input
											type="text" class="" name="maxVolTpd"
											value="${companyModel.maxVolTpd}">
									</div>
								</div>
							</div>

							<div class="box-footer">
								<button class="btn btn-primary" type="submit">Submit</button>
								<a href="skip-company?merchantID=${merchantId}" class="btn btn-primary" type="submit">Skip</a>
							</div>
						</form>
						<section class="aggregatorProfile">
							<h2>Aggregator Profile</h2>
							<div class="leftBoxdetails">
								<p>
									<strong>Registered As:</strong> SpiderDigital Commerce
									(Bangladesh) Limited
								</p>
								<p>
									<strong>Doing Business As:</strong> Aggregator
								</p>
								<p>
									<strong>Phone:</strong> +880 29 141384
								</p>
								<p>
									<strong>Fax:</strong> +880 2 9141384
								</p>
								<p>
									<strong> Email:</strong> support@lebupay.com
								</p>
								<p>
									<strong>Website:</strong> www.lebupay.com
								</p>
								<p>
									<strong>Address:</strong> House: 50, Road 10A, Dhanmondi-1209
								</p>
							</div>
							<div class="rightBoxdetals">
								<p>
									<strong>Contact Person: </strong>Md. Sazzadul Islam
								</p>
								<p>
									<strong>Designation:</strong> N/A
								</p>
								<p>
									<strong>Mobile:</strong> +8801711340150
								</p>
								<p>
									<strong>Email:</strong> sazzad@spiderdxb.com
								</p>
								<p>
									<strong>Payment Account at EBL Name:</strong> Spider Digital
									Commerce (Bangladesh) Ltd.
								</p>
								<p>
									<strong>Number:</strong> 1061060326696
								</p>
							</div>
							<div class="clear"></div>
							<p>
								<strong>Aggregator Undertaking:</strong> I confirm that - (a)
								all information provided are correct and up-to-date (b) I am
								fully responsible for all acts and omissions of the Merchants
								associated with me (c) breach of any term in the Aggregator
								Agreement, any laws and regulations either by me (Aggregator) or
								a Merchant, may give rise to legal action against me, as EBL
								deems fit.
							</p>
							<p>I, as undersigned on behalf of the aggregator, am
								requesting to register the above mentioned merchant in 'EBL
								SKYPAY - Online Payment Gateway Service' as per following
								previously agreed MSF rates.</p>
						</section>
						<section class="customerDetails">
							<div class="leftBoxdetails">
								<div class="rowSec">
									<div class="column theader">MSF</div>
									<div class="column theader">Visa</div>
									<div class="column theader">Master card</div>
									<div class="column theader">Diners Club</div>
								</div>
								<div class="rowSec">
									<div class="column">
										<span>On-us</span>
									</div>
									<div class="column"></div>
									<div class="column"></div>
									<div class="column"></div>
								</div>
								<div class="rowSec">
									<div class="column">
										<span>Not-On-us</span>
									</div>
									<div class="column"></div>
									<div class="column"></div>
									<div class="column"></div>
								</div>

							</div>
							<div class="rightBoxdetails">
								<h2>
									Aggregator<span>(Sign,date and seal)</span>
								</h2>
								<div class="agrDetails"></div>
							</div>
						</section>

					</div>
				</section>


			</div>

		</div>


	</div>



</section>
<!-- /.content -->

<%@ include file="merchant-footer.jsp"%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#oth").click(function() {
			$("#others").val("");
			if ($(this).is(":checked")) {
				//$("#others").val("");
				$('#others').attr('disabled', false);
			} else {
				$('#others').attr('disabled', true);
			}
		});
	});

	 $("#merchantRegistration")
			.submit(
					function(event) {

						var flag = true;

						var merchantCategoryCode = $("input[name=codeSec]")
								.val();
						var companyName = $("input[name=companyName]").val();
						var dba = $("input[name=dba]").val();
						var yearsInBusiness = $("input[name=yearsInBusiness]")
								.val();
						var phone = $("input[name=phone]").val();
						var fax = $("input[name=fax]").val();
						var website = $("input[name=website]").val();
						var ip = $("input[name=ip]").val();
						var address = $("input[name=address]").val();
						var netWorth = $("input[name=netWorth]").val();
						var mechandising = $("input[name=mechandising]").val();
						var contactPerson = $("input[name=contactPerson]")
								.val();
						var designation = $("input[name=designation]").val();
						var email = $("input[name=email]").val();
						var mobile = $("input[name=mobile]").val();
						var nid = $("input[name=nid]").val();
						var panNo = $("input[name=panNo]").val();
						var projectNoOfTpm = $("input[name=projectNoOfTpm]")
								.val();
						var projectVolOfTpm = $("input[name=projectVolOfTpm]")
								.val();
						var maxAmtSt = $("input[name=maxAmtSt]").val();
						var maxNoTpd = $("input[name=maxNoTpd]").val();
						var maxVolTpd = $("input[name=maxVolTpd]").val();
						var others = $("input[name=others]").val();

						if (isBlank(merchantCategoryCode,
								"Merchant Category Code")) {
							return false;
						}
						if (isBlank(companyName, "Registered As")) {
							return false;
						}
						if (isBlank(dba, "DBA")) {
							return false;
						}

						if ($("#abc input:checkbox:checked").length > 0) {

						} else {
							jQuery.growl.error({
								message : "Type have to set"
							});
							return false;
							// none is checked
						}

						if (isBlank(yearsInBusiness, "Years In Business")) {
							return false;
						}
						if ($.isNumeric(yearsInBusiness)) {

						} else {
							jQuery.growl.error({
								message : "Years in Business Should be Numeric"
							});
							return false;
						}
						if (isBlank(phone, "Phone No")) {
							return false;
						}
						if (isBlank(fax, "Fax")) {
							return false;
						}
						if (isBlank(website, "Website")) {
							return false;
						}

						if (isBlank(ip, "Ip")) {
							return false;
						}
						if (isBlank(address, "Address")) {
							return false;
						}
						if (isBlank(netWorth, "Net Worth")) {
							return false;
						}
						if ($.isNumeric(netWorth)) {

						} else {
							jQuery.growl.error({
								message : "Net Worth Should be Numeric"
							});
							return false;
						}

						if ($("#def input:checkbox:checked").length > 0) {
							if ($("#oth").is(":checked")) {
								if (isBlank(others, "Others")) {
									return false;
								}

							}

						} else {
							jQuery.growl.error({
								message : "Please Select any Other Bank IPG"
							});
							return false;
							// none is checked
						}

						if (isBlank(mechandising, "Merchandising")) {
							return false;
						}
						if (isBlank(contactPerson, "Contract Person")) {
							return false;
						}
						if (isBlank(designation, "Designation")) {
							return false;
						}
						if (isBlank(email, "Email")) {
							return false;
						}
						if (isBlank(mobile, "Mobile")) {
							return false;
						}
						if (isBlank(nid, "NID")) {
							return false;
						}
						if (isBlank(panNo, "Pan No")) {
							return false;
						}
						if (isBlank(projectNoOfTpm,
								"Project Number of Transaction per month")) {
							return false;
						}

						if ($.isNumeric(projectNoOfTpm)) {

						} else {
							jQuery.growl.error({
								message : "Project Number of Transaction per month Should be Numeric"
							});
							return false;
						}
						if (isBlank(projectVolOfTpm,
								"Project Volume of Transaction per month")) {
							return false;
						}
						if ($.isNumeric(projectVolOfTpm)) {

						} else {
							jQuery.growl.error({
								message : "Project Volume of Transaction per month Should be Numeric"
							});
							return false;
						}
						if (isBlank(maxAmtSt,
								"Maximum Amount Single Transaction")) {
							return false;
						}
						if ($.isNumeric(maxAmtSt)) {

						} else {
							jQuery.growl.error({
								message : "Maximum Amount Single Transaction Should be Numeric"
							});
							return false;
						}
						if (isBlank(maxNoTpd,
								"Maximum Number of Transaction per day")) {
							return false;
						}
						if ($.isNumeric(maxNoTpd)) {

						} else {
							jQuery.growl.error({
								message : "Maximum Number of Transaction per day Should be Numeric"
							});
							return false;
						}
						if (isBlank(maxVolTpd,
								"Maximum Volume of Transaction per day")) {
							return false;
						}
						if ($.isNumeric(maxVolTpd)) {

						} else {
							jQuery.growl.error({
								message : "Maximum Volume of Transaction per day Should be Numeric"
							});
							return false;
						}

						if (flag) {
							return true;
						}

						return false;
					}); 

	/**
	 * is used to check filed is blank or not and show the msg
	 */
	function isBlank(string, field) {
		if (string == null || string == '') {
			jQuery.growl.error({
				message : field + " can not be blank"
			});

			return true;
		}
		return false;
	}
					function myFunction(a, b) {
						//alert("called")
					    return a * b;
					}				
					
</script>

<%
					String message = (String) request.getAttribute("database.not.found");
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
					
					<%
					message = (String) request.getAttribute("general.pblm");
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

<%
					message = (String) request.getAttribute("common.error");
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
					
					<%
					message = (String) request.getAttribute("merchant.company.successfully");
						if (message != null) {
					%>
<script>
 $(document).ready(function() {
 jQuery.growl.notice({
		message : "<%=message%>"
					});
				});
			</script>
<%
						}
					%>
					
	<%
     message = (String) request.getAttribute("merchant.companycategoryCode.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.companyname.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.dba.required");
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

					
					<%
     message = (String) request.getAttribute("merchant.company.service.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.yearsinbusiness.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.phone.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.fax.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.website.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.ip.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.company.address.required");
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
     

					<%
     message = (String) request.getAttribute("merchant.otherBank.required");
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
     

					<%
     message = (String) request.getAttribute("merchant.networth.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.nid.required");
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

					
					<%
     message = (String) request.getAttribute("merchant.contactperson.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.designation.required");
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
     

					<%
     message = (String) request.getAttribute("merchant.email.required");
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

					<%
     message = (String) request.getAttribute("merchant.mobile.required");
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

					
					<%
     message = (String) request.getAttribute("merchant.panNo.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.projectNoOfTpm.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.projectVolOfTpm.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.maxAmtSt.required");
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
     

					
					<%
     message = (String) request.getAttribute("merchant.maxVolTpd.required");
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
					


