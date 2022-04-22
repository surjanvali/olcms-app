<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1>
	<%-- <ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="./Welcome.do"><i
				class="la la-home font-20"></i> </a></li>
		<li class="breadcrumb-item"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty></li>
	</ol> --%>
</div>
<div class="page-content fade-in-up">

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
		<%-- <div class="ibox-head">
			<div class="ibox-title">
				<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
			</div>
		</div> --%>
		<div class="ibox-body">

			<html:form method="post" action="/CaseAssignmentStatus"
				styleId="CaseAssignmentStatusForm">

				<html:hidden styleId="mode" property="mode" />

				<html:hidden property="dynaForm(deptId)" styleId="deptId" />
				<html:hidden property="dynaForm(deptName)" styleId="deptName" />
				<html:hidden property="dynaForm(actionType)" styleId="actionType" />

				<div class="table-responsive">
					<logic:notEmpty name="DEPTWISEHCCASES">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									<th>Assigned To HOD / Nodal Officers</th>
									<th>Assigned To Section (Sect. Dept.)</th>
									<th>Assigned To Section (HOD)</th>
								</tr>
							</thead>
							<tbody>

								<bean:define id="regTotals" value="0"></bean:define>
								<bean:define id="regAssgTotalsno" value="0"></bean:define>
								<bean:define id="regAssgTotalssectsec" value="0"></bean:define>
								<bean:define id="regAssgTotalshodsec" value="0"></bean:define>
								<logic:iterate id="map" name="DEPTWISEHCCASES" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptshortname }</td>
										<td>${map.description }</td>

										<!-- 
										<td style="text-align: right;"><a href="./CaseAssignmentStatus.do?mode=getCasesList&type=total&deptCode=${map.deptshortname}">${map.total }</a></td>
										<td style="text-align: right;"><a href="./CaseAssignmentStatus.do?mode=getCasesList&type=assigned2HOD&deptCode=${map.deptshortname}">${map.assigned_to_hod }</a></td>
										<td style="text-align: right;"><a href="./CaseAssignmentStatus.do?mode=getCasesList&type=assigned2SectSec&deptCode=${map.deptshortname}">${map.assigned_to_sect_sec }</a></td>
										<td style="text-align: right;"><a href="./CaseAssignmentStatus.do?mode=getCasesList&type=assigned2HodSec&deptCode=${map.deptshortname}">${map.assigned_to_hod_sec }</a></td>
										 -->

										<td style="text-align: right;"><a
											href="javascript:getCasesList('total','${map.deptshortname}','${map.description}');">${map.total }</a></td>
										<td style="text-align: right;"><a
											href="javascript:getCasesList('assigned2HOD','${map.deptshortname}','${map.description}');">${map.assigned_to_hod }</a></td>
										<td style="text-align: right;"><a
											href="javascript:getCasesList('assigned2SectSec','${map.deptshortname}','${map.description}');">${map.assigned_to_sect_sec }</a></td>
										<td style="text-align: right;"><a
											href="javascript:getCasesList('assigned2HodSec','${map.deptshortname}','${map.description}');">${map.assigned_to_hod_sec }</a></td>

									</tr>
									<bean:define id="regTotals" value="${regTotals + map.total }"></bean:define>
									<bean:define id="regAssgTotalsno"
										value="${regAssgTotalsno + map.assigned_to_hod }"></bean:define>
									<bean:define id="regAssgTotalssectsec"
										value="${regAssgTotalssectsec + map.assigned_to_sect_sec }"></bean:define>
									<bean:define id="regAssgTotalshodsec"
										value="${regAssgTotalshodsec + map.assigned_to_hod_sec }"></bean:define>

								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
									<td colspan="1" style="text-align: right;">${regAssgTotalsno }</td>
									<td colspan="1" style="text-align: right;">${regAssgTotalssectsec }</td>
									<td colspan="1" style="text-align: right;">${regAssgTotalshodsec }</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>
					<logic:present name="CASESLIST">

						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>CINo</th>
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
									<th>Orders</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.cino}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

										</td>
										<td><logic:notEmpty name="map" property="date_of_filing">
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
										<td><logic:notEmpty name="map" property="date_next_list">
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
										<td style="text-align: center;">${map.orderpaths }</td>
									</tr>

								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="20">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:present>

				</div>
			</html:form>
		</div>
	</div>
</div>

<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:50px;">
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
						style="width:100%;min-height:550px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
	function getCasesList(type, deptId, deptName) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptName);
		$("#actionType").val(type);
		$("#mode").val("getCasesList");
		$("#CaseAssignmentStatusForm").submit();
	}

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
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
