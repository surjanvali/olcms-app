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
	<html:form method="post" action="/AssignedNewCasesToEmp"
		styleId="acksAbstractFormId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds"
			property="dynaForm(selectedCaseIds)" />
			<html:hidden styleId="fileCino"
			property="dynaForm(fileCino)" />
			<html:hidden property="dynaForm(resident_id)" styleId="resident_id" />
			
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
					<div class="ibox-title">Assigned New Cases</div>
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
										<th>Ack No.</th>
										<th>Date</th>
										<th>District</th>
										<th>Case Type</th>
										<th>Main Case No.</th>
										<th>Departments / Respondents</th>
										<th>Advocate CC No.</th>
										<th>Advocate Name</th>
										<!-- <th>Remarks</th> -->
										<th>Download / Print</th>
										<th style="width: 150px !important;">Action</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASEWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }</td>

										<%-- 	<td>
												<div class="form-group">
													<label class="ui-checkbox"> <input type="checkbox"
														name="caseIds" value="${map.ack_no }" id="caseIds" /> <span
														class="input-span"></span></label>
												</div>
											</td> --%>
											<td>${map.ack_no }<logic:notEqual value="-" name="map"
													property="hc_ack_no">

													<span style="color: navy;font-weight: bold;">${map.hc_ack_no }</span>

												</logic:notEqual></td>
											<td nowrap="nowrap">${map.generated_date }</td>
											<td>${map.district_name }</td>
											<td>${map.case_full_name }</td>
											<td>${map.maincaseno }</td>
											<td nowrap="nowrap">${map.dept_descs }</td>
											<td>${map.advocateccno }</td>
											<td>${map.advocatename }</td>



										<td style="text-align: center;" nowrap="nowrap"><logic:present
													name="map" property="ack_file_path">
													<a href="./${map.ack_file_path}" target="_new"
														title="Print Acknowledgement" class="btn btn-sm btn-info">
														<i class="fa fa-save"></i> <span>Acknowledgement</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <logic:present name="map" property="barcode_file_path">
													<a href="./${map.barcode_file_path}" target="_new"
														title="Print Barcode" class="btn btn-sm btn-info"> <i
														class="fa fa-save"></i> <span>Barcode</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <logic:notEqual value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
														target="_new" title="Print Scanned Affidavit"
														class="btn btn-sm btn-info">
												</logic:notEqual> <logic:equal value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
														target="_new" title="Print Scanned Affidavit"
														class="btn btn-sm btn-info">
												</logic:equal> <i class="fa fa-save"></i> <span>Scanned Affidavit</span> </a></td>
											
											<td style="min-width: 150px !important;">

												<button class="btn btn-sm btn-primary"
													onclick="caseStatusUpdate('${map.ack_no}@${map.respondent_slno}');">Update
													Status</button>
											</td>
										</tr>
									</logic:iterate>
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
</div>
			</logic:notEmpty>

		</div>
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

	function caseStatusUpdate(fileCino) {
		$("#mode").val("caseStatusUpdate");
		$("#fileCino").val("" + fileCino);
		$("#AssignedCasesToSectionForm").submit();
	}
	
	function fnShowDistWise() {
		$("#mode").val("showDistWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowDeptWise() {
		$("#mode").val("showDeptWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowCases() {
		$("#mode").val("showCaseWise");
		// $("#acksAbstractFormId").submit();
		return true;
	}

	function showDeptCases(deptid) {
		$("#districtId").val($("#districtId").val());
		$("#deptId").val(deptid);
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		// $("#acksAbstractForm").submit();
		$("#acksAbstractFormId").submit();
		//$(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + $("#districtId").val() + "&deptId=" + deptid + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}

	function showDistCases(distid) {
		$("#districtId").val(distid);
		$("#deptId").val($("#deptId").val());
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		$("#acksAbstractFormId").submit();
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
		$.post("AjaxModels.do", data)
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