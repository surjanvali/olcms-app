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
	<html:form method="post" action="/EcourtsCaseMappingWithNewAckNo"
		styleId="acksAbstractFormId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds"
			property="dynaForm(selectedCaseIds)" />
			<html:hidden styleId="ackNo"
			property="dynaForm(ackNo)" />
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
					<div class="ibox-title">Assignment of New Cases</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="fromDate" property="dynaForm(fromDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">To Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="toDate" property="dynaForm(toDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Advocate Name</label>
								<html:text styleId="advcteName" property="dynaForm(advcteName)"
									styleClass="form-control" maxlength="25" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="showcases" value="Show Cases"
								class="btn btn-success" onclick="return fnShowCases();" />
							<!-- <input
								type="button" name="showcases2" value="Show Department Wise"
								class="btn btn-success" onclick="return fnShowDeptWise();" /> <input
								type="button" name="showcases3" value="Show District Wise"
								class="btn btn-success" onclick="return fnShowDistWise();" /> -->
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
										<th>Ack No.</th>
										<th>Date</th>
										<th>Case Type</th>
										<th>Main Case No.</th>
										<th>Departments / Respondents</th>
										<th>Download / Print</th>
										<!-- <th>Advocate CC No.</th>
										<th>Advocate Name</th> -->
										 <th>Main Case No</th> 
										
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
														name="caseIds" value="${map.ack_no}@${map.respondent_slno}" id="caseIds" /> <span
														class="input-span"></span></label>
												</div>
											</td> --%>
											<td>${map.ack_no }<logic:notEqual value="-" name="map"
													property="hc_ack_no">

													<span style="color: navy;font-weight: bold;">${map.hc_ack_no}</span>

												</logic:notEqual></td>
											<td nowrap="nowrap">${map.generated_date}</td>
											<td>${map.case_full_name }</td>
											<td>${map.maincaseno }</td>
											<td nowrap="nowrap">${map.dept_descs}</td>
											<%-- <td>${map.advocateccno }</td>
											<td>${map.advocatename }</td> --%>
<td style="text-align: center;" nowrap="nowrap">
											 <logic:notEqual value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
														target="_new" title="Scanned Affidavit"
														class="btn btn-sm btn-info">
												</logic:notEqual> <logic:equal value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
														target="_new" title="Scanned Affidavit"
														class="btn btn-sm btn-info">
												</logic:equal> <i class="fa fa-save"></i> <span>Scanned Affidavit</span> </a></td>
											 <td>
											<div class="row col-md-12">
										<div class="row col-md-4">
											<html:select property="dynaForm(caseType1_${map.ack_no})"
												styleClass="select2Class" 
												styleId="caseType1_${map.ack_no}" onchange="getCaseTypedetails();">
												<html:option value="0">-type select-</html:option>
												<logic:notEmpty property="dynaForm(caseTypesListShrt)"
													name="CommonForm">
													<html:optionsCollection
														property="dynaForm(caseTypesListShrt)" name="CommonForm" />
												</logic:notEmpty>
											</html:select></div>
										<div class="row col-md-4">
											
												
												<html:text styleId="regYear1_${map.ack_no}" styleClass="form-control"
												property="dynaForm(regYear1_${map.ack_no})" value="2022" />
											</div>
										<div class="row col-md-4">
											<html:text styleId="mainCaseNo_${map.ack_no}" styleClass="form-control"
												property="dynaForm(mainCaseNo_${map.ack_no})"  maxlength="7"  onkeypress="return isNumberKey(this);"/></div></div>
											 
											 </td> 
											
												<td><div class="col-md-12 col-xs-12">
							<input type="button" name="showcases" value="submit"
								class="btn btn-success" onclick="return fnShowCases('${map.ack_no}');" /> </div></td>
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
	function fnShowCases(val) {
		$("#ackNo").val(val);
		//alert("sss"+$("#caseType1_"+val).val());
		
		if ($("#caseType1_"+val).val() == null
				|| $("#caseType1_"+val).val() == ""
				|| $("#caseType1_"+val).val() == "0") {
			alert("Select case Type");
			return false;
		}
		
		if ($("#regYear1_"+val).val() == null
				|| $("#regYear1_"+val).val() == ""
					|| $("#regYear1_"+val).val() == "0") {
			alert("Select year");
			return false;
		}
		
		if ($("#mainCaseNo_"+val).val() == null
				|| $("#mainCaseNo_"+val).val() == ""
					|| $("#mainCaseNo_"+val).val() == "0") {
			alert("Enter case No");
			return false;
		}
		
		
		$("#mode").val("submitDetails");
		 $("#acksAbstractFormId").submit();
		//return true;
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
			$(".disthodDiv").show();
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
		//alert("fnAssign2DeptHOD");
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
		
	//alert("fnAssign2DistHOD");
		
		var testval = [];
		$('#caseIds:checked').each(function() {
			testval.push($(this).val());
		});
		
		//alert("testval--"+testval);
		$("#selectedCaseIds").val(testval);
		var chkdVal = $("#officerType:checked").val();

		// alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if ($("#selectedCaseIds").val() == null
				|| $("#selectedCaseIds").val() == ""
				|| $("#selectedCaseIds").val() == "0") {
			alert("Select atleast a case to submit.");
			return false;
		}
		
		
		
		//alert("chkdVal--"+chkdVal);
		
		if ($("#caseDist").val() == null || $("#caseDist").val() == ""
				|| $("#caseDist").val() == "0") {
			alert("Select District.");
			$("#caseDist").focus();
			return false;
		}
		

		else if ((chkdVal == "DC-NO" || chkdVal == "DC") && ($("#distDept").val() == null || $("#distDept").val() == "" || $("#distDept").val() == "0")) {
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
		
		//alert("fnAssign2DeptHOD2 ");
		
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