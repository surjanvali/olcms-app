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
		<logic:notEqual value="13" name="role_id" scope="session">
			<div class="row">
				<logic:notEmpty name="dashboardCounts">
					<logic:iterate id="innerData" name="dashboardCounts">
						<logic:greaterThan value="0" name="innerData" property="assigned">
							<div class="col-lg-3 col-md-6">
								<a href="./AssignedCasesToSection.do">
									<div class="ibox bg-danger color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerData.assigned }</h2>
											<div class="m-b-5">Assigned Cases</div>
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
						</logic:greaterThan>

						<logic:greaterThan value="0" name="innerData" property="total">
							<div class="col-lg-3 col-md-6">
								<div class="ibox bg-primary color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${innerData.total }</h2>
										<div class="m-b-5">Total Cases</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small> &nbsp;</small>
										</div>
									</div>
								</div>
							</div>
						</logic:greaterThan>

						<logic:greaterThan value="0" name="innerData"
							property="assignment_pending">
							<logic:notEqual value="6" name="role_id" scope="session">
								<div class="col-lg-3 col-md-6">
									<a
										href="./HighCourtCasesList.do?mode=getCasesList&src=dashBoard">
										<div class="ibox bg-danger color-white widget-stat">
											<div class="ibox-body">
												<h2 class="m-b-5 font-strong">${innerData.assignment_pending }</h2>
												<div class="m-b-5">Pending for Assignment</div>
												<i class="fa fa-file-text-o widget-stat-icon"></i>
												<div>
													<small> &nbsp;</small>
												</div>
											</div>
										</div>
									</a>
								</div>
							</logic:notEqual>

						</logic:greaterThan>

						<logic:greaterThan value="0" name="innerData"
							property="approval_pending">
							<div class="col-lg-3 col-md-6">
								<a href="./AssignedCasesToSection.do">
									<div class="ibox bg-danger color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerData.approval_pending }</h2>
											<div class="m-b-5">Pending for Approval</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>
						<logic:greaterThan value="0" name="innerData"
							property="closedcases">
							<div class="col-lg-3 col-md-6">
								<a href="./ClosedCasesReport.do">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerData.closedcases }</h2>
											<div class="m-b-5">Closed by MLO / NO / Section
												Officers</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>
					</logic:iterate>

				</logic:notEmpty>


				<logic:notEmpty name="NEWCASES">
					<logic:greaterThan value="0" name="NEWCASES">
						<div class="col-lg-3 col-md-6">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->

							<a href="./AcksAbstractReport.do">
								<div class="ibox bg-primary color-white widget-stat">
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


				<logic:notEmpty name="NEWCASESCOUNTS">
					<logic:iterate id="innerCases" name="NEWCASESCOUNTS">
						
						<logic:greaterThan value="0" name="innerCases"
							property="total">
							<div class="col-lg-3 col-md-6">
								<a href="./AcksAbstractReport.do">
									<div class="ibox bg-primary color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerCases.total }</h2>
											<div class="m-b-5">Total New Cases Registered</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>

										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>
						
						<logic:greaterThan value="0" name="innerCases"
							property="assignment_pending">
							<div class="col-lg-3 col-md-6">
								<a href="./AssignmentAndNewCases.do">
									<div class="ibox bg-danger color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerCases.assignment_pending }</h2>
											<div class="m-b-5">New Cases Pending For Assignment</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>

										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>
						
						<logic:greaterThan value="0" name="innerCases"
							property="approval_pending">
							<div class="col-lg-3 col-md-6">
								<a href="./AssignmentAndNewCases.do">
									<div class="ibox bg-danger color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${innerCases.approval_pending }</h2>
											<div class="m-b-5">New Cases Pending For Approval</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>

										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

					</logic:iterate>
				</logic:notEmpty>



				<logic:notEmpty name="parawiseCount">
					<logic:greaterThan value="0" name="parawiseCount">
						<div class="col-lg-3 col-md-6">
							<a
								href="./AssignedCasesToSection.do?mode=unspecified&pwCounterFlag=PR">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${parawiseCount }</h2>
										<div class="m-b-5">Parawise Remarks Pending for Approval</div>
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
						<div class="col-lg-3 col-md-6">
							<a
								href="./AssignedCasesToSection.do?mode=unspecified&pwCounterFlag=COUNTER">
								<div class="ibox bg-danger color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${counterFileCount }</h2>
										<div class="m-b-5">Counter Filed and Pending for
											Approval</div>

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



				<logic:notEmpty name="INTERIMCASES">
					<logic:greaterThan value="0" name="INTERIMCASES">
						<div class="col-lg-3 col-md-6">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->

							<!-- <a href="./HCOrdersIssuedReport.do?mode=getCasesList&caseStatus=IO"> -->
								<a href="./HCOrdersIssuedReport.do">
								<div class="ibox bg-info color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${INTERIMCASES}</h2>
										<div class="m-b-5">No. of Cases with Interim Orders</div>
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
				<logic:notEmpty name="INTERIMORDERS">
					<logic:greaterThan value="0" name="INTERIMORDERS">
						<div class="col-lg-3 col-md-6">
							<!-- <a href="./HCOrdersIssuedReport.do?mode=getCasesList&caseStatus=IO"> -->
							<a href="./HCOrdersIssuedReport.do">
								<div class="ibox bg-info color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${INTERIMORDERS}</h2>
										<div class="m-b-5">Interim Orders Issued</div>
										<i class="fa fa-file-text-o widget-stat-icon"></i>
										<div>
											<small>&nbsp;</small>
										</div>
									</div>
								</div>
							</a>
						</div>
					</logic:greaterThan>
				</logic:notEmpty>
				<logic:notEmpty name="FINALORDERS">
					<logic:greaterThan value="0" name="FINALORDERS">
						<div class="col-lg-3 col-md-6">
							<!-- <a href="./GPOAck.do?mode=deptWiseCases"> -->
							<!-- <a href="./HCOrdersIssuedReport.do?mode=getCasesList&caseStatus=FO"> -->
							<a href="./HCOrdersIssuedReport.do">
								<div class="ibox bg-success color-white widget-stat">
									<div class="ibox-body">
										<h2 class="m-b-5 font-strong">${FINALORDERS}</h2>
										<div class="m-b-5">Final Order Issued</div>
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
				<div class="col-lg-3 col-md-6">
					<a href="./HighCourtCauseList.do">
						<div class="ibox bg-warning color-white widget-stat">
							<div class="ibox-body">
								<h2 class="m-b-5 font-strong">Cause List</h2>
								<div class="m-b-5">&nbsp;</div>
								<i class="fa fa-file-text-o widget-stat-icon"></i>
								<div>
									<small> &nbsp;</small>
								</div>
								<!-- <i class="fa fa-money widget-stat-icon"></i>
								<div>
									<i class="fa fa-level-up m-r-5"></i><small>22% higher</small>
								</div> -->
							</div>
						</div>
					</a>
				</div>

			</div>


			<logic:notEmpty name="disposedCasesStatus">
				<div class="row">
					<logic:iterate id="inner" name="disposedCasesStatus">

						<logic:greaterThan value="0" name="inner" property="disposed">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=DISPOSED">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.disposed }</h2>
											<div class="m-b-5">Disposed Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>


						<logic:greaterThan value="0" name="inner" property="allowed">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=ALLOWED">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.allowed }</h2>
											<div class="m-b-5">Allowed Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

						<logic:greaterThan value="0" name="inner" property="dismissed">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=DISMISSED">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.dismissed }</h2>
											<div class="m-b-5">Dismissed Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

						<logic:greaterThan value="0" name="inner" property="withdrawn">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=WITHDRAWN">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.withdrawn }</h2>
											<div class="m-b-5">Withdrawn Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

						<logic:greaterThan value="0" name="inner" property="closed">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=CLOSED">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.closed }</h2>
											<div class="m-b-5">Closed Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

						<logic:greaterThan value="0" name="inner" property="returned">
							<div class="col-lg-3 col-md-6">
								<a
									href="./HCCaseStatusAbstractReport.do?mode=getCasesGroupList&src=dashBoard&caseCategory=RETURNED">
									<div class="ibox bg-success color-white widget-stat">
										<div class="ibox-body">
											<h2 class="m-b-5 font-strong">${inner.returned }</h2>
											<div class="m-b-5">Returned Cases</div>
											<i class="fa fa-file-text-o widget-stat-icon"></i>
											<div>
												<small> &nbsp;</small>
											</div>
										</div>
									</div>
								</a>
							</div>
						</logic:greaterThan>

					</logic:iterate>
				</div>
			</logic:notEmpty>

			<logic:notEmpty name="SHOWABSTRACTS">
				<div class="row">
					<div class="col-lg-3 col-md-6">
						<div class="ibox bg-success color-white widget-stat">
							<div class="ibox-body">
								<h2 class="m-b-5 font-strong">25</h2>
								<div class="m-b-5">States</div>
								<i class="fa fa-file-text-o widget-stat-icon"></i>
								<div>
									<small> &nbsp;</small>
								</div>
								<!-- <i class="ti-shopping-cart widget-stat-icon"></i>
						<div>
							<i class="fa fa-level-up m-r-5"></i><small>25% higher</small>
						</div> -->
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6">
						<div class="ibox bg-info color-white widget-stat">
							<div class="ibox-body">
								<h2 class="m-b-5 font-strong">581</h2>
								<div class="m-b-5">Districts</div>
								<i class="fa fa-file-text-o widget-stat-icon"></i>
								<div>
									<small> &nbsp;</small>
								</div>
								<!-- <i class="ti-bar-chart widget-stat-icon"></i>
						<div>
							<i class="fa fa-level-up m-r-5"></i><small>17% higher</small>
						</div> -->
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6">
						<div class="ibox bg-warning color-white widget-stat">
							<div class="ibox-body">
								<h2 class="m-b-5 font-strong">42</h2>
								<div class="m-b-5">Benches</div>
								<i class="fa fa-file-text-o widget-stat-icon"></i>
								<div>
									<small> &nbsp;</small>
								</div>
								<!-- <i class="fa fa-money widget-stat-icon"></i>
						<div>
							<i class="fa fa-level-up m-r-5"></i><small>22% higher</small>
						</div> -->
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6">
						<div class="ibox bg-danger color-white widget-stat">
							<div class="ibox-body">
								<h2 class="m-b-5 font-strong">1,95,814</h2>
								<div class="m-b-5">Cases</div>
								<i class="fa fa-file-text-o widget-stat-icon"></i>
								<div>
									<small> &nbsp;</small>
								</div>
								<!-- <i class="ti-user widget-stat-icon"></i>
						<div>
							<i class="fa fa-level-down m-r-5"></i><small>-12% Lower</small>
						</div> -->
							</div>
						</div>
					</div>
				</div>
			</logic:notEmpty>

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
		</logic:notEqual>
	</div>
</body>
</html>
