<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<div class="page-content fade-in-up">
	<html:form action="/HighCourtCasesCategoryUpdation"
		styleId="HCCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(reportType)" styleId="reportType" />
		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="dashboard-cat-title">
					<logic:notEmpty name="successMsg">
						<div class="alert alert-success" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<i class="mdi mdi-check-all"></i> <strong>${successMsg}</strong>
						</div>
					</logic:notEmpty>
					<logic:notEmpty name="errorMsg">
						<div class="alert alert-danger" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<i class="mdi mdi-block-helper"></i> <strong>${errorMsg}</strong>
						</div>
					</logic:notEmpty>
				</div>
			</div>
		</div>

		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">Finance Category Updation Report</div>
			</div>
		
				
			<br />
				<div class="row">
					<div class="col-md-12 col-xs-12">
						<div class="table-responsive">
							<logic:present name="secdeptwise">
								<table class="table table-striped table-bordered table-hover"
									id="example" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<!-- <th>Sect.Department Code</th> -->
											<th>Department Name</th>
											<th>A1</th>
											<th>A2</th>
											<th>B1</th>
											<th>B2</th>
											<th>C1</th>
											<th>C2</th>	
										</tr>
									</thead>
									<tbody>
										<bean:define id="A1" value="0"></bean:define>
										<bean:define id="A2" value="0"></bean:define>
										<bean:define id="B1" value="0"></bean:define>
										<bean:define id="B2" value="0"></bean:define>
										<bean:define id="C1" value="0"></bean:define>
										<bean:define id="C2" value="0"></bean:define>
										<logic:iterate id="map" name="secdeptwise" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<%-- <td>${map.deptcode }</td> --%>
												<td><a
													href="javascript:ShowHODWise('${map.deptcode}','${map.description }');">${map.description }</a></td>
												
												<%-- <td style="text-align: right;">${map.a1 }</td>
												<td style="text-align: right;">${map.a2 }</td>
												<td style="text-align: right;">${map.b1 }</td>
												<td style="text-align: right;">${map.b2 }</td>
												<td style="text-align: right;">${map.c1 }</td>
												<td style="text-align: right;">${map.c2 }</td> --%>
												
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','A1');">${map.a1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','A2');">${map.a2 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','B1');">${map.b1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','B2');">${map.b2 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','C1');">${map.c1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','C2');">${map.c2 }</a></td>
												
												
												<bean:define id="A1" value="${A1 + map.a1 }"></bean:define>
												<bean:define id="A2" value="${A2 + map.a2 }"></bean:define>
												<bean:define id="B1" value="${B1 + map.b1 }"></bean:define>
												<bean:define id="B2" value="${B2 + map.b2 }"></bean:define>
												<bean:define id="C1" value="${C1 + map.c1 }"></bean:define>
												<bean:define id="C2" value="${C2 + map.c2 }"></bean:define>

											</tr>
											
										</logic:iterate>
									</tbody>

									<tfoot>
										<tR>
											<td colspan="2">Totals</td>
											<td colspan="1" style="text-align: right;">${A1 }</td>
											<td colspan="1" style="text-align: right;">${A2 }</td>
											<td colspan="1" style="text-align: right;">${B1 }</td>
											<td colspan="1" style="text-align: right;">${B2 }</td>
											<td colspan="1" style="text-align: right;">${C1 }</td>
											<td colspan="1" style="text-align: right;">${C2 }</td>
												
										</tR>
									</tfoot>

								</table>

							</logic:present>
							<logic:present name="deptwise">

								<table class="table table-striped table-bordered table-hover"
									id="example" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<!-- <th>Department Code</th> -->
											<th>HOD</th>
											<th>A1</th>
											<th>A2</th>
											<th>B1</th>
											<th>B2</th>
											<th>C1</th>
											<th>C2</th>	
										</tr>
									</thead>
									<tbody>
										<bean:define id="A1" value="0"></bean:define>
										<bean:define id="A2" value="0"></bean:define>
										<bean:define id="B1" value="0"></bean:define>
										<bean:define id="B2" value="0"></bean:define>
										<bean:define id="C1" value="0"></bean:define>
										<bean:define id="C2" value="0"></bean:define>
										
										
										<logic:iterate id="map" name="deptwise" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<%-- <td>${map.deptcode }</td> --%>
												<td><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','All');">${map.description }</a></td>
												
												
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','A1');">${map.a1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','A2');">${map.a2 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','B1');">${map.b1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','B2');">${map.b2 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','C1');">${map.c1 }</a></td>
													<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','C2');">${map.c2 }</a></td>
												
												
												<bean:define id="A1" value="${A1 + map.a1 }"></bean:define>
												<bean:define id="A2" value="${A2 + map.a2 }"></bean:define>
												<bean:define id="B1" value="${B1 + map.b1 }"></bean:define>
												<bean:define id="B2" value="${B2 + map.b2 }"></bean:define>
												<bean:define id="C1" value="${C1 + map.c1 }"></bean:define>
												<bean:define id="C2" value="${C2 + map.c2 }"></bean:define>
											</tr>
											
										</logic:iterate>
									</tbody>

									<tfoot>
										<tR>
											<td colspan="2">Totals</td>
											<td colspan="1" style="text-align: right;">${A1 }</td>
											<td colspan="1" style="text-align: right;">${A2 }</td>
											<td colspan="1" style="text-align: right;">${B1 }</td>
											<td colspan="1" style="text-align: right;">${B2 }</td>
											<td colspan="1" style="text-align: right;">${C1 }</td>
											<td colspan="1" style="text-align: right;">${C2 }</td>
										</tR>
									</tfoot>

								</table>


							</logic:present>

							<logic:present name="CASESLIST">

								<table id="example" class="table table-striped table-bordered"
									style="width:100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>CINo</th>
											<th>Finance Category</th>
											<th>Work Name</th>
											<th>Estimated Cost (In laksh)</th>
											<th>Administrative Sanction</th>
											<th>Grant Value</th>
											<th>CFMS Bill Id</th>
											<!-- <th>CFMS Bill status</th> -->
											<th>CFMS Bill Amount</th>
											<th>Date of Filing</th>
											<th>Case Type</th>
											<th>Reg.No.</th>
											<th>Reg. Year</th>
											<th>Filing No.</th>
											<th>Filing Year</th>
											<th>Date of Next List</th>
											<th>Bench</th>
											<th>Judge Name</th>
											<th>Petitioner</th>
											<th>District</th>
											<th>Purpose</th>
											<th>Respondents</th>
											<th>Petitioner Advocate</th>
											<th>Respondent Advocate</th>
											
										</tr>
									</thead>
									<tbody>
										<bean:define id="est_cost_tot" value="0"></bean:define>
										<bean:define id="bill_amount_tot" value="0"></bean:define>
										<logic:iterate id="map" name="CASESLIST" indexId="i">
											<tr>
												<td>${i+1 }.</td>
												<td><input type="button" id="btnShowPopup"
													value="${map.cino}"
													class="btn btn-sm btn-info waves-effect waves-light"
													onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

												</td>
												<td style="text-align: center;">${map.finance_category }</td>
												<td style="text-align: center;">${map.work_name}</td>
												<td style="text-align: center;">${map.est_cost}</td>
												<td style="text-align: center;">${map.admin_sanction }</td>
												<td style="text-align: center;">${map.grant_val }</td>
												<td style="text-align: center;">${map.cfms_bill }</td>
												<%-- <td style="text-align: center;">${map.bill_status }</td> --%>
												<td style="text-align: center;">${map.bill_amount}</td>
												<td><logic:notEmpty name="map"
														property="date_of_filing">
														<logic:notEqual value="0001-01-01" name="map"
															property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
													</logic:notEmpty></td>
												<td>${map.type_name_fil }</td>
												<td>${map.reg_no}</td>
												<td>${map.reg_year }</td>
												<td>${map.fil_no}</td>
												<td>${map.fil_year }</td>
												<td><logic:notEmpty name="map"
														property="date_next_list">
														<logic:notEqual value="0001-01-01" name="map"
															property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
													</logic:notEmpty></td>
												<td>${map.bench_name }</td>
												<td>Hon'ble Judge : ${map.coram }</td>
												<td>${map.pet_name }</td>
												<td>${map.dist_name }</td>
												<td>${map.purpose_name }</td>
												<td>${map.res_name }</td>

												<td>${map.pet_adv }</td>
												<td>${map.res_adv }</td>
												
												<bean:define id="est_cost_tot" value="${est_cost_tot + map.est_cost}"></bean:define>
												<bean:define id="bill_amount_tot" value="${bill_amount_tot + map.bill_amount}"></bean:define>
												
											</tr>

										</logic:iterate>
									</tbody>
									<tfoot>
										<tR>
											<td colspan="4">Totals</td>
											<td colspan="1" style="text-align: right;">${est_cost_tot}</td>
											<td colspan="3" style="text-align: right;"></td>
											<td colspan="1" style="text-align: right;">${bill_amount_tot}</td>
											<td colspan="20" style="text-align: right;"></td>
											
										</tR>
									</tfoot>

								</table>
							</logic:present>

						</div>
					</div>
				</div>
			</div>
		</div>
	</html:form>
</div>
<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:200px;">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header"
				style="background-color: #3498db;color: #fff;">
				<button type="button" class="close" data-dismiss="modal">
					&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<p>
					<iframe src="" id="page" name="model_window"
						style="width:100%;min-height:600px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>
<!-- <script src="https://apbudget.apcfss.in/js/select2.js"></script> -->
<script>
	$('.datepicker').datepicker({
		uiLibrary : 'bootstrap4'
	});

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
	});

	function fnShowCases() {
		$("#mode").val("unspecified");
		$("#HCCaseStatusAbstract").submit();
	}

	function ShowHODWise(deptId, deptDesc) {
		/*$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseStatusAbstract").submit();*/
		$(location).attr("href", "./HighCourtCasesCategoryUpdation.do?mode=HODwisedetails&deptName="+deptDesc+"&deptId="+deptId);
	}
	function showCasesWise(deptId, deptDesc, repType) {
		/*$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#mode").val("getCasesList");
		$("#HCCaseStatusAbstract").submit();*/
		$(location).attr("href", "./HighCourtCasesCategoryUpdation.do?mode=getCasesList&reportType="+repType+"&deptName="+deptDesc+"&deptId="+deptId);
	}

	function viewCaseDetailsPopup(cino) {
		var heading = "View Case Details for CINO : " + cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="
					+ cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};
</script>
