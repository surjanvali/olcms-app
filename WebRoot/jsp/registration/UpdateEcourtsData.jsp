<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<%-- <h1 class="page-title">
		<logic:notEmpty name="HEADING">
					New Cases Abstract Report
				</logic:notEmpty>
	</h1> --%>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/UpdateEcourtsData"
		styleId="updateEcourtsDataFormId" enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />

		<%-- <html:hidden styleId="selectedCaseIds" property="dynaForm(selectedCaseIds)" /> --%>

		<html:hidden styleId="cino" property="dynaForm(cino)" />
		<html:hidden styleId="cinoTotal" property="dynaForm(cinoTotal)" />
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

			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Import/Update e-Courts Data</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Type</label>
								<html:select styleId="caseTypeId"
									property="dynaForm(caseTypeId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(caseTypesList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(caseTypesList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Department</label>
								<html:select styleId="deptId" property="dynaForm(deptId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(deptList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>

						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Registration Year</label>
								<html:select styleId="regYear" property="dynaForm(regYear)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(yearsList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(yearsList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Filing From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofFromDate"
										property="dynaForm(dofFromDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Filing To Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofToDate" property="dynaForm(dofToDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
					</div>


					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="button" name="getreport" value="Get Report"
								class="btn btn-info" onclick="return fnShowCases();" /> <input
								type="button" name="updateCases" value="Update Cases Data"
								class="btn btn-warning" onclick="return fnUpdateCasesData();" />
							<input type="button" name="updateCases2"
								value="Import Cases Data" class="btn btn-success"
								onclick="return fnImportCinosData();" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">

					<div class="ibox">
						<div class="ibox-head">
							<div class="ibox-title">Import/Update e-Courts Data from
								Excel</div>
						</div>
						<div class="ibox-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="font-bold">Upload New Cases List in
											Excel</label>
										<html:file property="changeLetter" styleId="changeLetter"
											styleClass="form-control"></html:file>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<input type="button" name="uploadandRetrieve"
										value="Upload and Retrieve Data" class="btn btn-success"
										onclick="return fnUploadandRetrieve();" />
								</div>

							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<div class="ibox">
						<div class="ibox-head">
							<div class="ibox-title">Import/Update e-Courts Cause List
								Data</div>
						</div>
						<div class="ibox-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="font-bold">Cause List Date</label>
										<div class="input-group date">
											<span class="input-group-addon bg-white"><i
												class="fa fa-calendar"></i></span>
											<html:text styleId="causeListDate"
												property="dynaForm(causeListDate)"
												styleClass="form-control datepicker" />

										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<input type="button" name="getCauseList"
										value="Import Cause List" class="btn btn-primary"
										onclick="return fnGetCauseList();" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">

					<div class="ibox">
						<div class="ibox-head">
							<div class="ibox-title">Send SMS Alerts to All</div>
						</div>
						<div class="ibox-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<input type="button" name="sendSMSalerts"
										value="Send Alerts" class="btn btn-success"
										onclick="return fnSendSMSalerts();" />
								</div>

							</div>
						</div>
					</div>
				</div>


			</div>

			<logic:notEmpty name="CASEWISEACKS">
				<div class="ibox">
					<div class="ibox-body">
						<div class="table-responsive">
							<table class="table table-striped table-bordered table-hover"
								id="example">
								<thead>
									<tr>
										<th>Sl.No</th>
										<!-- <th></th> -->
										<th>CINO.</th>
										<th>Dept code</th>
										<th>Description</th>
										<th>Case Type</th>
										<th>Main Case No.</th>
										<th>Departments / Respondents</th>
										<th>Advocate CC No.</th>
										<th>Advocate Name</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASEWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }</td>

											<%-- <td>
												<div class="form-group">
													<label class="ui-checkbox"> <input type="checkbox"
														name="caseIds" value="${map.ack_no }" id="caseIds" /> <span
														class="input-span"></span></label>
												</div>
											</td> --%>
											<td>${map.cino }</td>
											<td nowrap="nowrap">${map.deptcode }</td>
											<td>${map.description }</td>
											<td>${map.total_cases }</td>
											<td>${map.withsectdept }</td>
											<td nowrap="nowrap">${map.withmlo }</td>
											<td>${map.withhod }</td>
											<td>${map.withnodal }</td>

											<td style="text-align: center;" nowrap="nowrap"><div
													class="row">
													<div class="col-md-12 col-xs-12">
														<input type="submit" name="cino" id="cino"
															class="btn btn-success"
															onclick="return updateCino('${map.cino}');" />
													</div>
												</div></td>
										</tr>
									</logic:iterate>
									<%-- <tr>
										<td colspan="7" style="text-align: center;"><div class="row">
												<div class="col-md-12 col-xs-12">
													<input type="submit" name="cinoTotal" id="cinoTotal" 
														class="btn btn-success"
														onclick="return updateCinoTotal('${cinoTotal}');" />
												</div>
											</div></td>
									</tr> --%>
								</tbody>
								<tfoot>
									<tR>
										<logic:present name="DISPLAYOLD">
											<td colspan="14">
										</logic:present>
										<logic:notPresent name="DISPLAYOLD">
											<td colspan="12">
										</logic:notPresent>
										&nbsp;
										</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>


			</logic:notEmpty>
	</html:form>
</div>


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>

<script>
	$('.datepicker').datepicker({
		uiLibrary : 'bootstrap4'
	});

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "yyyy-mm-dd"
		});
	});

	function fnUploadandRetrieve() {
		$("#mode").val("uploadandRetrieveEcourtsData");
		$("#updateEcourtsDataFormId").submit();
		//return true;
	}

	function fnSendSMSalerts() {
		$("#mode").val("sendSMSalerts");
		$("#updateEcourtsDataFormId").submit();
		//return true;
	}

	function fnShowDistWise() {
		$("#mode").val("showDistWise");
		$("#updateEcourtsDataFormId").submit();
		//return true;
	}
	function fnShowDeptWise() {
		$("#mode").val("showDeptWise");
		$("#updateEcourtsDataFormId").submit();
		//return true;
	}
	function fnShowCases() {
		$("#mode").val("showCaseWise");
		$("#updateEcourtsDataFormId").submit();
		///return true;
	}

	function fnUpdateCasesData() {
		$("#mode").val("updateCino");
		$("#updateEcourtsDataFormId").submit();
		///return true;
	}

	function fnImportCinosData() {
		$("#mode").val("importNewCinosData");
		$("#updateEcourtsDataFormId").submit();
		///return true;
	}

	function fnGetCauseList() {
		if ($("#causeListDate").val() == "") {
			alert("Select a Date");
			return false;
		} else {
			$("#mode").val("retrieveCauseList");
			$("#updateEcourtsDataFormId").submit();
			///return true;
		}
	}

	function updateCino(cino) {
		//$("#districtId").val($("#districtId").val());
		$("#cino").val(cino);
		$("#mode").val("updateCino");
		//alert("mode:" + $("#mode").val());
		//alert("cino:" + $("#cino").val());
		//alert("deptId:" + $("#deptId").val());
		// $("#acksAbstractForm").submit();
		$("#updateEcourtsDataFormId").submit();
		//$(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + $("#districtId").val() + "&deptId=" + deptid + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}

	function updateCinoTotal(cinoTotal) {
		//$("#districtId").val($("#districtId").val());
		$("#cino").val(cinoTotal);
		$("#mode").val("updateCino");
		//alert("cino:" + $("#cino").val());
		$("#updateEcourtsDataFormId").submit();
		//$(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + $("#districtId").val() + "&deptId=" + deptid + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}

	function showDeptCases(deptid) {
		$("#districtId").val($("#districtId").val());
		$("#deptId").val(deptid);
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		// $("#acksAbstractForm").submit();
		$("#updateEcourtsDataFormId").submit();
		//$(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + $("#districtId").val() + "&deptId=" + deptid + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}

	function showDistCases(distid) {
		$("#districtId").val(distid);
		$("#deptId").val($("#deptId").val());
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		$("#updateEcourtsDataFormId").submit();
		// document.forms[0].submit(); */
		// $(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + distid + "&deptId=" + $("#deptId").val() + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());

	}

	function showDepts() {
		var chkdVal = $("#officerType:checked").val();
		/* if($("#caseDept"))
			$("#caseDept").select2('destroy');
		if($("#empDept"))
			$("#empDept").select2('destroy'); */
		// alert($("#empDept").val());
		var data = {
			mode : "AjaxAction",
			typeCode : chkdVal,
			getType : "getDeptList"
		}
		$
				.post("AjaxModels.do", data)
				.done(
						function(res) {
							if (res != ''
									&& (chkdVal == "S-HOD"
											|| chkdVal == "D-HOD" || chkdVal == "DC-NO")) {
								$("#caseDept").select2('destroy');
								$("#caseDept").html(res);
								$("#caseDept").select2();
								$("#caseDept").select2("val", "0");
							}
							if (res != ''
									&& (chkdVal == "SD-SO"
											|| chkdVal == "OD-SO" || chkdVal == "DC-SO")) {
								$("#empDept").select2('destroy');
								$("#empDept").html(res);
								$("#empDept").select2();
								$("#empDept").select2("val", "0");
							}
						}).fail(function(exc) {
					alert("Error Occured.Please Try Again");
				});
	}

	function changeReport() {

		var chkdVal = $("#officerType:checked").val();

		$(".depthodDiv").hide();
		$(".depthodSectionDiv").hide();
		$(".distDiv").hide();
		$(".disthodDiv").hide();
		$(".distdepthodSectionDiv").hide();

		if (chkdVal == "S-HOD") {
			$(".depthodDiv").show();
		} else if (chkdVal == "D-HOD") {
			$(".depthodDiv").show();
		} else if (chkdVal == "SD-SO") {
			$(".depthodSectionDiv").show();
		} else if (chkdVal == "OD-SO") {
			$(".depthodSectionDiv").show();
		} else if (chkdVal == "DC") {
			$(".distDiv").show();
		} else if (chkdVal == "DC-NO") {
			$(".distDiv").show();
			$(".disthodDiv").show();
		} else if (chkdVal == "DC-SO") {
			$(".depthodSectionDiv").show();
			$(".distdepthodSectionDiv").show();
		}
		showDepts();
	}

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
		if ($('#example2')) {
			$('#example2').DataTable();
		}

		// alert("Cases filed in the year 2021 listed below. To view and process other cases please use the Year filter or select From and To dates. Press the 'Get Cases' button for the Cases List.");
		$('#panel-modal').modal({
			backdrop : 'static',
			keyboard : false
		});
		$('#panel-modal').modal('show');

		$(".btnClosePopup").click(function() {
			$("#panel-modal").modal("hide");
		});
	});

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function populateDeptSecs() {
		$("#empSection").select2('destroy');
		// alert($("#empDept").val());
		var data = {
			mode : "AjaxAction",
			empDept : $("#empDept").val(),
			getType : "getEmpDeptSectionsList",
			distCode : $("#caseDist1") ? $("#caseDist1").val() : "0"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#empSection").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#empSection").select2();
		$("#empSection").select2("val", "0");

	}

	function populatePostDetails() {
		$("#empPost").select2('destroy');
		var data = {
			mode : "AjaxAction",
			empSec : $("#empSection").val(),
			empDept : $("#empDept").val(),
			getType : "getEmpPostsList",
			distCode : $("#caseDist1") ? $("#caseDist1").val() : "0"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#empPost").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#empPost").select2();
		$("#empPost").select2("val", "0");

	}

	function populateEmpDetails() {
		$("#employeeId").select2('destroy');
		var data = {
			mode : "AjaxAction",
			empDept : $("#empDept").val(),
			empSec : $("#empSection").val(),
			empPost : $("#empPost").val(),
			getType : "getEmpsList",
			distCode : $("#caseDist1") ? $("#caseDist1").val() : "0"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#employeeId").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#employeeId").select2();
		$("#employeeId").select2("val", "0");

	}

	function fnAssign2DeptHOD() {

		var testval = [];
		$('#caseIds:checked').each(function() {
			testval.push($(this).val());
		});
		$("#selectedCaseIds").val(testval);
		//alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if ($("#selectedCaseIds").val() == null
				|| $("#selectedCaseIds").val() == ""
				|| $("#selectedCaseIds").val() == "0") {
			alert("Select atleast a case to submit.");
			return false;
		}
		if ($("#caseDept").val() == null || $("#caseDept").val() == ""
				|| $("#caseDept").val() == "0") {
			alert("Select Department.");
			$("#caseDept").focus();
			return false;
		} else {
			$("#mode").val("assign2DeptHOD");
			$("#HighCourtCasesListForm").submit();
		}
	}

	function fnAssign2DistHOD() {

		var testval = [];
		$('#caseIds:checked').each(function() {
			testval.push($(this).val());
		});
		$("#selectedCaseIds").val(testval);
		var chkdVal = $("#officerType:checked").val();

		// alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if ($("#selectedCaseIds").val() == null
				|| $("#selectedCaseIds").val() == ""
				|| $("#selectedCaseIds").val() == "0") {
			alert("Select atleast a case to submit.");
			return false;
		}
		if ($("#caseDist").val() == null || $("#caseDist").val() == ""
				|| $("#caseDist").val() == "0") {
			alert("Select District.");
			$("#caseDist").focus();
			return false;
		}

		else if (chkdVal == "DC-NO"
				&& ($("#distDept").val() == null || $("#distDept").val() == "" || $(
						"#distDept").val() == "0")) {
			alert("Select Department.");
			$("#distDept").focus();
			return false;
		} else {
			$("#mode").val("assign2DistCollector");
			// alert($("#mode").val());

			$("#HighCourtCasesListForm").submit();
		}
	}

	function fnAssign2DeptHOD2() {

		var testval = [];
		$('#caseIds:checked').each(function() {
			testval.push($(this).val());
		});
		$("#selectedCaseIds").val(testval);
		// alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if ($("#selectedCaseIds").val() == null
				|| $("#selectedCaseIds").val() == ""
				|| $("#selectedCaseIds").val() == "0") {
			alert("Select atleast a case to submit.");
			return false;
		} else if ($("#empDept").val() == null || $("#empDept").val() == ""
				|| $("#empDept").val() == "0") {
			alert("Select Employee Department");
			$("#empDept").focus();
			return false;
		} else if ($("#empSection").val() == null
				|| $("#empSection").val() == ""
				|| $("#empSection").val() == "0") {
			alert("Select Employee Section");
			$("#empSection").focus();
			return false;
		} else if ($("#empPost").val() == null || $("#empPost").val() == ""
				|| $("#empPost").val() == "0") {
			alert("Select Post");
			$("#empPost").focus();
			return false;
		} else if ($("#employeeId").val() == null
				|| $("#employeeId").val() == ""
				|| $("#employeeId").val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId").focus();
			return false;
		} else {
			$("#mode").val("assignMultiCases2Section");
			$("#HighCourtCasesListForm").submit();
		}
	}
</script>