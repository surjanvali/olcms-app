<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
body {
	overflow-y: auto;
}

.nav-tabs .nav-item {
	margin-bottom: -1px;
	background: #5a79c1;
	color: #fff;
	border-left: 1px solid #fff;
}

.nav-tabs active {
	background: #0b379b;
}
</style>
</head>
<body>

	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<logic:notPresent name="CAUSELISTCASES">

				<logic:notEmpty name="errorMsg">
					<div class="alert alert-danger" role="alert">
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<i class="mdi mdi-block-helper"></i> <strong>${errorMsg}</strong>
					</div>
				</logic:notEmpty>

			</logic:notPresent>

			<ul class="nav nav-tabs primary" role="tablist">
				<li class="nav-item"><a class="nav-link"
					id="yesterdaysCauseList-tab" data-toggle="tab"
					href="#yesterdaysCauseList" role="tab"
					aria-controls="yesterdaysCauseList" aria-selected="true"> <span
						class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
						class="d-none d-sm-block">Yesterday's Cause List</span>
				</a></li>
				<li class="nav-item active"><a class="nav-link"
					id="todaysCauseList-tab" data-toggle="tab " href="#todaysCauseList"
					role="tab" aria-controls="todaysCauseList" aria-selected="true">
						<span class="d-block d-sm-none"><i class="fas fa-user"></i></span>
						<span class="d-none d-sm-block">Today's Cause List</span>
				</a></li>

				<li class="nav-item"><a class="nav-link"
					id="tomorrowsCauseList-tab" data-toggle="tab"
					href="#tomorrowsCauseList" role="tab"
					aria-controls="tomorrowsCauseList" aria-selected="true"> <span
						class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
						class="d-none d-sm-block">Tomorrow's Cause List</span>
				</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane" id="yesterdaysCauseList" role="tabpanel"
					aria-labelledby="yesterdaysCauseList-tab">
					<div class="ibox">
						<div class="ibox-body">
							<logic:present name="CAUSELISTCASESYESTERDAY">
								<div class="table-responsive">
									<table class="table table-striped table-bordered"
										style="width:100%">
										<thead>
											<tr>
												<th>Sl.No</th>
												<th>Causelist Date</th>
												<th>CINo</th>
												<th>Date of Filing</th>
												<th>Case Reg No.</th>
												<th>Petitioner</th>
												<th>Respondents</th>
												<th>Petitioner Advocate</th>
												<th>Respondent Advocate</th>
											</tr>
										</thead>
										<tbody>
											<logic:iterate id="map" name="CAUSELISTCASESYESTERDAY"
												indexId="i">
												<tr>
													<td>${i+1 }.</td>
													<td>${map.causelist_date}</td>
													<td>${map.cino}</td>
													<td><logic:notEmpty name="map"
															property="date_of_filing">
															<logic:notEqual value="0001-01-01" name="map"
																property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
														</logic:notEmpty></td>


													<td>${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>

													<td>${map.pet_name }</td>
													<td>${map.res_name },${map.address}</td>

													<td>${map.pet_adv }</td>
													<td>${map.res_adv }</td>
												</tr>
											</logic:iterate>
										</tbody>
										<tfoot>
											<tR>
												<td colspan="9">&nbsp;</td>
											</tR>
										</tfoot>
									</table>
								</div>
							</logic:present>
						</div>
					</div>
				</div>

				<div class="tab-pane" id="todaysCauseList" role="tabpanel"
					aria-labelledby="todaysCauseList-tab">
					<div class="ibox">
						<div class="ibox-body">
							<logic:present name="CAUSELISTCASESTODAY">
								<div class="row">
									<div class="table-responsive">
										<table class="table table-striped table-bordered"
											style="width:100%">
											<thead>
												<tr>
													<th>Sl.No</th>
													<th>Causelist Date</th>
													<th>CINo</th>
													<th>Date of Filing</th>
													<th>Case Reg. No.</th>
													<th>Petitioner</th>
													<th>Respondents</th>
													<th>Petitioner Advocate</th>
													<th>Respondent Advocate</th>
												</tr>
											</thead>
											<tbody>
												<logic:iterate id="map" name="CAUSELISTCASESTODAY"
													indexId="i">
													<tr>
														<td>${i+1 }.</td>
														<td>${map.causelist_date}</td>
														<td>${map.cino}</td>
														<td><logic:notEmpty name="map"
																property="date_of_filing">
																<logic:notEqual value="0001-01-01" name="map"
																	property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
															</logic:notEmpty></td>


														<td>${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>

														<td>${map.pet_name }</td>
														<td>${map.res_name },${map.address}</td>

														<td>${map.pet_adv }</td>
														<td>${map.res_adv }</td>
													</tr>
												</logic:iterate>
											</tbody>
											<tfoot>
												<tR>
													<td colspan="9">&nbsp;</td>
												</tR>
											</tfoot>
										</table>
									</div>
								</div>
							</logic:present>
						</div>
					</div>
				</div>

				<div class="tab-pane" id="tomorrowsCauseList" role="tabpanel"
					aria-labelledby="tomorrowsCauseList-tab">
					<div class="ibox">
						<div class="ibox-body">
							<logic:present name="CAUSELISTCASESTOMORROW">
								<div class="table-responsive">
									<table class="table table-striped table-bordered"
										style="width:100%">
										<thead>
											<tr>
												<th>Sl.No</th>
												<th>Causelist Date</th>
												<th>CINo</th>
												<th>Date of Filing</th>
												<th>Case Reg No.</th>
												<th>Petitioner</th>
												<th>Respondents</th>
												<th>Petitioner Advocate</th>
												<th>Respondent Advocate</th>
											</tr>
										</thead>
										<tbody>
											<logic:iterate id="map" name="CAUSELISTCASESTOMORROW"
												indexId="i">
												<tr>
													<td>${i+1 }.</td>
													<td>${map.causelist_date}</td>
													<td>${map.cino}</td>
													<td><logic:notEmpty name="map"
															property="date_of_filing">
															<logic:notEqual value="0001-01-01" name="map"
																property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
														</logic:notEmpty></td>


													<td>${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>

													<td>${map.pet_name }</td>
													<td>${map.res_name },${map.address}</td>

													<td>${map.pet_adv }</td>
													<td>${map.res_adv }</td>
												</tr>
											</logic:iterate>
										</tbody>
										<tfoot>
											<tR>
												<td colspan="9">&nbsp;</td>
											</tR>
										</tfoot>
									</table>
								</div>
							</logic:present>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

</body>
</html>