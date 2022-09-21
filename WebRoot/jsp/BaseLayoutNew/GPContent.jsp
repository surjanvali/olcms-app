<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'Content.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>
<body>
	<div class="page-content fade-in-up">
		<logic:equal value="6" name="role_id" scope="session">

			<logic:present name="YEARWISECASES">
				<div class="row">

					<logic:iterate id="innerData" name="YEARWISECASES">

						<div class="col-lg-2 col-md-3">
							<a
								href="./GPReport.do?mode=yearWiseCases&yearId=${innerData.reg_year}">
								<div class="ibox bg-primary color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${innerData.reg_year }</h2>
										<div class="m-b-5">Total Cases : ${innerData.casescount }</div>
										<!-- <i class="ti-shopping-cart widget-stat-icon"></i> -->
										<i class="ti-menu-alt widget-stat-icon"></i>
										<div>
											<!-- <i class="fa fa-level-up m-r-5"></i> -->
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:iterate>
				</div>
			</logic:present>

			<div class="row">
			
			<logic:notEmpty name="INSTRUCTIONSCOUNT">
					<logic:greaterThan value="0" name="INSTRUCTIONSCOUNT">
						<div class="col-lg-2 col-md-3">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->
							<a href="./GPReport.do?mode=viewInstructionsCases">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${INSTRUCTIONSCOUNT }</h2>
										<div class="m-b-5">Instructions Legacy</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>

				</logic:notEmpty>
				<logic:notEmpty name="INSTRUCTIONSCOUNTNEW">
					<logic:greaterThan value="0" name="INSTRUCTIONSCOUNTNEW">
						<div class="col-lg-2 col-md-3">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->
							<a href="./GPsReportNew.do?mode=viewInstructionsCasesNew">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${INSTRUCTIONSCOUNTNEW}</h2>
										<div class="m-b-5">Instructions New</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>

				</logic:notEmpty>
				<logic:notEmpty name="NEWCASES">
					<logic:greaterThan value="0" name="NEWCASES">
						<div class="col-lg-2 col-md-3">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->

							<a href="./AcksAbstractReport.do">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${NEWCASES }</h2>
										<div class="m-b-5">New Cases Registered</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>

									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>
				</logic:notEmpty>
				<logic:notEmpty name="parawiseCount">
					<logic:greaterThan value="0" name="parawiseCount">
						<div class="col-lg-2 col-md-3">
							<a
								href="./GPReport.do?mode=viewGPCases&pwCounterFlag=PR">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${parawiseCount }</h2>
										<div class="m-b-5">Parawise Remarks Pending for Approval Legacy</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>
				</logic:notEmpty>

				<logic:notEmpty name="counterFileCount">
					<logic:greaterThan value="0" name="counterFileCount">
						<div class="col-lg-2 col-md-3">
							<a
								href="./GPReport.do?mode=viewGPCases&pwCounterFlag=COUNTER">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${counterFileCount }</h2>
										<div class="m-b-5">Counter Filed and Pending for
											Approval Legacy</div>

										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>
				</logic:notEmpty>
				<logic:notEmpty name="parawiseCountNew">
					<logic:greaterThan value="0" name="parawiseCountNew">
						<div class="col-lg-2 col-md-3">
							<a
								href="./GPsReportNew.do?mode=viewGPCasesNew&pwCounterFlag=PR">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${parawiseCountNew}</h2>
										<div class="m-b-5">Parawise Remarks Pending for Approval (New)</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>
				</logic:notEmpty>

				<logic:notEmpty name="counterFileCountNew">
					<logic:greaterThan value="0" name="counterFileCountNew">
						<div class="col-lg-2 col-md-3">
							<a
								href="./GPsReportNew.do?mode=viewGPCasesNew&pwCounterFlag=COUNTER">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${counterFileCountNew }</h2>
										<div class="m-b-5">Counter Filed and Pending for
											Approval (New)</div>

										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>

				</logic:notEmpty>
			</div>

			<logic:present name="showReport1">
				<div class="row">
					<div class="col-lg-12">

						<jsp:include page="/jsp/reports/HCCaseStatusAbstractReport.jsp"></jsp:include>

					</div>
				</div>
			</logic:present>
			<logic:present name="recentActivities">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox">
							<div class="ibox-head">
								<div class="ibox-title">
									<h4 class="m-t-0 header-title">
										<b>Your Recent Activities </b>
									</h4>
								</div>
							</div>
							<div class="ibox-body">
								<div class="table-responsive">

									<table class="table table-striped table-bordered table-hover"
										id="example" cellspacing="0" width="100%">
										<thead>
											<tr>
												<th>Sl.No</th>
												<th>CINo.</th>
												<th>Activity</th>
												<th>Assigned To</th>
												<th>Time</th>
											</tr>
										</thead>
										<tbody>
											<logic:iterate id="map" name="recentActivities" indexId="i">
												<tr>
													<td>${i+1 }</td>
													<td>${map.cino }</td>
													<td>${map.action_type }</td>
													<td><logic:equal value="-" name="map"
															property="uploaded_doc_path">
													 ${map.deptdesc } ${map.fullname_en } ${map.post_name_en } 
													<logic:greaterThan value="0" name="map" property="dist_id">
														${map.district_name }
													</logic:greaterThan>
														</logic:equal> <logic:notEqual value="-" name="map"
															property="uploaded_doc_path">

															<a href="${map.uploaded_doc_path }" target="_new">
																Uploaded Document</a>

														</logic:notEqual></td>
													<td>${map.inserted_time }</td>
												</tr>
											</logic:iterate>
										</tbody>

										<!-- <tfoot>
										<tR>
											<td colspan="3" style="text-align: right;">&nbsp;</td>
										</tR>
									</tfoot> -->

									</table>
								</div>
							</div>
						</div>


					</div>
				</div>
			</logic:present>
		</logic:equal>
	</div>
</body>
</html>
