<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div class="page-content fade-in-up">
	<html:form method="post" action="/HighCourtCasesList"
		styleId="HighCourtCasesListForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds"
			property="dynaForm(selectedCaseIds)" />

		<div class="container-fluid">
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

			<logic:notEmpty name="CASESLIST">
				<div class="ibox">
					<div class="ibox-head">
						<div class="ibox-title">
							<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
						</div>
					</div>
					<div class="ibox-body">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CINo</th>
										<th>Date of Filing</th>
										<th>Case Type</th>
										<th>Reg.No.</th>
										<th>Reg. Year</th>
										<!-- <th>Filing No.</th>
										<th>Filing Year</th> -->
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
										<th>Status</th>
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
											<%-- <td>${map.fil_no}</td>
											<td>${map.fil_year }</td> --%>
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
											<td></td>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="17">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>


			</logic:notEmpty>
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
	function fnShowCases() {
		if (($("#regYear").val() == "" || $("#regYear").val() == "0")
				&& ($("#dofFromDate").val() == null
						|| $("#dofFromDate").val() == "" || $("#dofFromDate")
						.val() == "0")
				&& ($("#purpose").val() == null || $("#purpose").val() == "" || $(
						"#purpose").val() == "0")
				&& ($("#districtId").val() == null
						|| $("#districtId").val() == "" || $("#districtId")
						.val() == "0")) {
			alert("Please select a filter to get the data.");
			return false;
		}

		if (!($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $(
				"#dofFromDate").val() == "0")
				&& ($("#dofToDate").val() == null
						|| $("#dofToDate").val() == "" || $("#dofToDate").val() == "0")) {
			alert("Select Filing To Date");
			$("#dofToDate").focus();
			return false;
		}

		/*if ($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $("#dofFromDate").val() == "0") {
			alert("Select Filing From Date");
			$("#dofFromDate").focus();
			return false;
		} else if ($("#dofToDate").val() == null || $("#dofToDate").val() == "" || $("#dofToDate").val() == "0") {
			alert("Select Filing To Date");
			$("#dofToDate").focus();
			return false;
		}

		if ($("#purpose").val() == null || $("#purpose").val() == "" || $("#purpose").val() == "0") {
			alert("Select Purpose");
			$("#purpose").focus();
			return false;
		}

		if (($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $("#dofFromDate").val() == "0") && ($("#purpose").val() == null || $("#purpose").val() == "" || $("#purpose").val() == "0")) {
			alert("Select an Option to retrieve Data");
			$("#purpose").focus();
			return false;
		}*/

		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
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
