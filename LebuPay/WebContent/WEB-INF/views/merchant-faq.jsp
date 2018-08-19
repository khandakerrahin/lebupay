<%@ include file="merchant-header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">

	<section class="content marginBtm">
		<div class="sandBox bg-white">
			<div class="sandBoxWrapper">
				<div class="row brdr-btm">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<h2>FAQs</h2>
					</div>
				</div>
				<div class="row customer_care">
					<div class="col-md-12 vertpad">
						<div class="panel-group customAccordian faqs" id="accordion">
							<c:forEach var="faqModel" items="${faqModels }" varStatus="loop">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h4 class="panel-title collapsed" data-toggle="collapse"
											data-parent="#accordion" href="#collapse${loop.index }">
											<a>${faqModel.question }</a>
										</h4>
									</div>
									<div id="collapse${loop.index }"
										class="panel-collapse collapse">
										<div class="panel-body">
											<p>${faqModel.answer }</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="merchant-footer.jsp"%>