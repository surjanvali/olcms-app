<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

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
		<div class="ibox-head">
			<div class="ibox-title">
				<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>

			</div>
		</div>
		<div class="ibox-body">
			<html:form action="/AllHighCourtCasesReport"
				styleId="AllHighCourtCasesReportForm">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(hodCode)" styleId="hodCode" />
				<logic:notEmpty name="DEPTWISEHCCASES">
					<div class="table-responsive">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									<th>Assigned Cases</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="regTotals" value="0"></bean:define>
								<bean:define id="regAssgTotals" value="0"></bean:define>
								<logic:iterate id="map" name="DEPTWISEHCCASES" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptshortname }</td>
										<td>
											<%-- <a href="javascript:showAllCases('${map.deptshortname}');">${map.description }</a> --%>
											<a
											href="./AllHighCourtCasesReport.do?mode=showAllCases&hodCode=${map.deptshortname}">${map.description }</a>

										</td>
										<td style="text-align: right;">${map.total }</td>
										<td style="text-align: right;">${map.assigned }</td>
									</tr>
									<bean:define id="regTotals" value="${regTotals + map.total }"></bean:define>
									<bean:define id="regAssgTotals"
										value="${regAssgTotals + map.assigned }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
									<td colspan="1" style="text-align: right;">${regAssgTotals }</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</logic:notEmpty>

				<logic:notEmpty name="CASESLIST">
					<div class="row">
						<div class="table-responsive">
							<table id="example"
								class="table table-striped table-bordered table-hover"
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
										<!-- <th>Date of Next List</th>
									<th>Bench</th> -->
										<th>Judge Name</th>
										<th>Petitioner</th>
										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>
										<th>Orders</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }</td>
											<td>
												<%-- <button class="btn btn-sm btn-primary"
												onclick="viewCaseDtls('${map.cino}');">${map.cino}</button> --%>

												<input type="button" id="btnShowPopup" value="${map.cino}"
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
											<%-- <td><logic:notEmpty name="map" property="date_next_list">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty>
										</td>
										<td>${map.bench_name }</td> --%>
											<td>Hon'ble Judge : ${map.coram }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name }</td>
											<td style="text-align: center;">${map.orderpaths }</td>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="14">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</logic:notEmpty>
			</html:form>
		</div>
	</div>
</div>

<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:200px;">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<p>
					<iframe src="" id="page" name="model_window"
						style="width:100%;min-height:350px;;border:0px;"> </iframe>
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
	function showAllCases(hodCode) {
		$("#mode").val("showAllCases");
		$("#hodCode").val("" + hodCode);
		$("#AssignedCasesToSectionForm").submit();
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