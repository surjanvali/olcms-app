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
<style>
.myDiv {
	border: 5px outset red;
	background-color: lightblue;
	text-align: center;
}
</style>
<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
					High Court Cases List
				</logic:notEmpty>
	</h1>

</div> --%>
<div class="page-content fade-in-up">
	<html:form method="post" action="/DailyStatusEntryByGP"
		styleId="HighCourtCasesListForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="cino" property="dynaForm(cino)" />
		<html:hidden styleId="total" property="dynaForm(total)" />

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
				
				<!-- <div class="ibox-body">
					<div class="row"> -->
					<%--	<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date</label>
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
								<label class="font-bold">Date of Filing From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofToDate" property="dynaForm(dofToDate)"
										styleClass="form-control datepicker" />
								</div>
							</div>
						</div> --%>
					</div>
					
					<!-- <div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="submit" value="Get Cases"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div> -->


				<!-- </div> -->
			</div>

			<logic:notEmpty name="CASESLIST">
				<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Daily Status Entry Cases</div>
				</div>
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Cino</th>
										<th>Dept Code</th>
										<th>Case Type</th>
										<th>Case Reg No</th>
										<th>Case Reg Year</th>
										<th>Prayer</th>
										<th>Bench Id</th>
										<th>Cause List Id</th>
										<th>Cause List Type</th>
										<th>Judge Name</th>
										<th>Department</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<%-- <td>${map.cino}</td> --%>
											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

											</td>
											<td>${map.dept_code}</td>
											
											<td>${map.type_name_reg}</td>
											<td>${map.reg_no}</td>
											<td>${map.reg_year}</td>
											<td>${map.prayer}</td>
											
											<td>${map.bench_id}</td>
											<td>${map.causelist_id}</td>
											<td>${map.cause_list_type}</td>
											<td>${map.judge_name}</td>
											<td>${map.description}</td>
											<td>
												 <input type="button" id="btnShowPopup" value="Update DailyStatus"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}','Legacy');" />
											</td>
										</tr>
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
				</div>
			 <!-- 	<div class="row" style="text-align: center">
					<div class="col-md-12 col-xs-12">
						<input type="submit" name="submit" value="Submit"
							class="btn btn-success" onclick="return fnSubmitCategory();" />
					</div>
				</div> --> 

			</logic:notEmpty>
		</div>
		
	</html:form>
</div>

<!-- Modal  Start-->

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
				<!--  <button type="button" class="btn btn-danger" data-dismiss="modal"  onclick="return fnShowCases();">Close</button>  -->
				 <!-- <div class="btn btn-danger" data-dismiss="modal"><a href="./DailyStatusEntry.do" >Close</a></div>  -->
				<!-- <input type="submit" name="submit" value="Close" class="btn btn-danger" data-dismiss="modal" onclick="return fnShowCases();" /> -->
				<div class="form-group"> <a href="./DailyStatusEntryByGP.do" class="btn btn-danger border-0">Close</a>
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
        uiLibrary: 'bootstrap4'
    });
</script>
<script type="text/javascript">

function reloadParent(){
	location.reload(true);
}

function showDepts() {
	var chkdVal= $("#officerType:checked").val();	
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
	$.post("AjaxModels.do", data).done(function(res) {
		if (res != '' && (chkdVal=="S-HOD" || chkdVal=="D-HOD" || chkdVal=="DC-NO")) {
			$("#caseDept").select2('destroy');
			$("#caseDept").html(res);
			$("#caseDept").select2();
			$("#caseDept").select2("val", "0");
		}
		if (res != '' && (chkdVal=="SD-SO" || chkdVal=="OD-SO" || chkdVal=="DC-SO")) {
			$("#empDept").select2('destroy');
			$("#empDept").html(res);
			$("#empDept").select2();
			$("#empDept").select2("val", "0");
		}
	}).fail(function(exc) {
		alert("Error Occured.Please Try Again");
	});
}

function changeReport(){
	
	var chkdVal= $("#officerType:checked").val();	
	
	$(".depthodDiv").hide();
	$(".depthodSectionDiv").hide();
	$(".distDiv").hide();
	$(".disthodDiv").hide();
	$(".distdepthodSectionDiv").hide();
	
	if(chkdVal=="S-HOD"){
		$(".depthodDiv").show();
	} else if(chkdVal=="D-HOD"){
		$(".depthodDiv").show();
	} else if(chkdVal=="SD-SO"){
		$(".depthodSectionDiv").show();
	} else if(chkdVal=="OD-SO"){
		$(".depthodSectionDiv").show();
	} else if(chkdVal=="DC"){
		$(".distDiv").show();
	}else if(chkdVal=="DC-NO"){
		$(".distDiv").show();
		$(".disthodDiv").show();
	}else if(chkdVal=="DC-SO"){
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
	$('#panel-modal').modal({backdrop : 'static', keyboard : false});
	$('#panel-modal').modal('show');
	
	
	$(".btnClosePopup").click(function () {
		$("#panel-modal").modal("hide");
	});
});

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}
	
	
	
	
	function fnShowCases() {
		
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	}
	
	function getClose() {
		alert("wait");
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	}
	
	
function fnSubmitCategory() {
	
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
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
	
	


/*
function populateDeptSecs(cino) {
	$("#LOADINGPAGEGIF").html('<img src="< %=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
	$("#empSection" + cino).select2('destroy');
	var data = {
		mode : "AjaxAction",
		empDept : $("#empDept" + cino).val(),
		getType : "getEmpDeptSectionsList"
	}
	$.post("AjaxModels.do", data).done(function(res) {
		if (res != '') {
			$("#empSection" + cino).html(res);
		}
	}).fail(function(exc) {
		alert("Error Occured.Please Try Again");
	});
	$("#empSection" + cino).select2();
	$("#empSection" + cino).select2("val", "0");
	setTimeout(function() {
		$("#LOADINGPAGEGIF").html("");
	}, 900);
}

function populatePostDetails(cino) {
	$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
	$("#empPost" + cino).select2('destroy');
	var data = {
		mode : "AjaxAction",
		empSec : $("#empSection" + cino).val(),
		empDept : $("#empDept" + cino).val(),
		getType : "getEmpPostsList"
	}
	$.post("AjaxModels.do", data).done(function(res) {
		if (res != '') {
			$("#empPost" + cino).html(res);
		}
	}).fail(function(exc) {
		alert("Error Occured.Please Try Again");
	});
	$("#empPost" + cino).select2();
	$("#empPost" + cino).select2("val", "0");
	setTimeout(function() {
		$("#LOADINGPAGEGIF").html("");
	}, 900);
}

function populateEmpDetails(cino) {
	$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		$("#employeeId" + cino).select2('destroy');
		var data = {
			mode : "AjaxAction",
			empDept : $("#empDept" + cino).val(),
			empSec : $("#empSection" + cino).val(),
			empPost : $("#empPost" + cino).val(),
			getType : "getEmpsList"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#employeeId" + cino).html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#employeeId" + cino).select2();
		$("#employeeId" + cino).select2("val", "0");
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}

*/

	function showEdit(val) {
		$("#mloId").val(val);
		document.forms[0].mode.value = "editEmployee";
		document.forms[0].submit();
	}

	function deleteData(val) {
		$("#mloId").val(val);
		document.forms[0].mode.value = "deleteEmployeeDetails";
		document.forms[0].submit();
	}

	function isNumberKey(evt) {
		var charCode = (evt.which) ? evt.which : event.keyCode;
		if (charCode > 31 && (charCode < 48 || charCode > 57))
			return false;
		return true;
	}

	function addressType(i) {
		if (i.value.length > 0) {
			//i.value = i.value.replace(/[^\sA-Za-z0-9:.\/,\\]+/g, '');
			i.value = i.value.replace(/[^\sA-Za-z0-9,\/.\\:;#]+/g, '');
		}
	}

	function OnlyNumbersAndDot(evt) {
		var e = window.event || evt; // for trans-browser compatibility  
		var charCode = e.which || e.keyCode;
		if ((charCode > 45 && charCode < 58) || charCode == 8) {
			return true;
		}
		return false;
	}
	function trim(s) {
		return s.replace(/^\s*/, "").replace(/\s*$/, "");
	}

	function validateEmail(inputText) {
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (inputText.value.match(mailformat)) {
			return true;
		} else {
			alert("You have entered an invalid email address!");
			inputText.focus();
			return false;
		}
	}

	function updateEmployeeDetails() {
		designationId = $("#designationId").val();
		employeeId = $("#employeeId").val();
		mobileNo = $("#mobileNo").val();
		emailId = $("#emailId").val();
		aadharNo = $("#aadharNo").val();

		if (designationId == null || designationId == "" || designationId == "0") {
			alert("Select Designation");
			$("#designationId").focus();
			return false;
		}

		if (employeeId == null || employeeId == "" || employeeId == "0") {
			alert("Select Employee");
			$("#employeeId").focus();
			return false;
		}

		if (mobileNo == null || mobileNo == "" || mobileNo == "0") {
			alert("Enter Mobile Number");
			$("#mobileNo").focus();
			return false;
		}

		if (mobileNo != null && mobileNo != "" && mobileNo.length != 10) {
			alert("Enter 10-digit Mobile Number");
			$("#mobileNo").focus();
			return false;
		}


		if (emailId == null || emailId == "" || emailId == "0") {
			alert("Enter Email Id");
			$("#emailId").focus();
			return false;
		}
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (!emailId.match(mailformat)) {
			alert("You have entered an invalid email address!");
			$("#emailId").focus();
			return false;
		}

		if (aadharNo == null || aadharNo == "" || aadharNo == "0") {
			alert("Enter Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (aadharNo != null && aadharNo != "" && aadharNo.length != 12) {
			alert("Enter 12-digit Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (confirm("Do you want to Update employee details?")) {
			document.forms[0].mode.value = "updateEmployeeDetails";
			document.forms[0].submit();
		}
		else
			return false;

	}

	function addEmployee() {
		designationId = $("#designationId").val();
		employeeId = $("#employeeId").val();
		mobileNo = $("#mobileNo").val();
		emailId = $("#emailId").val();
		aadharNo = $("#aadharNo").val();

		if (designationId == null || designationId == "" || designationId == "0") {
			alert("Select Designation");
			$("#designationId").focus();
			return false;
		}

		if (employeeId == null || employeeId == "" || employeeId == "0") {
			alert("Select Employee");
			$("#employeeId").focus();
			return false;
		}

		if (mobileNo == null || mobileNo == "" || mobileNo == "0") {
			alert("Enter Mobile Number");
			$("#mobileNo").focus();
			return false;
		}

		if (mobileNo != null && mobileNo != "" && mobileNo.length != 10) {
			alert("Enter 10-digit Mobile Number");
			$("#mobileNo").focus();
			return false;
		}


		if (emailId == null || emailId == "" || emailId == "0") {
			alert("Enter Email Id");
			$("#emailId").focus();
			return false;
		}

		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (!emailId.match(mailformat)) {
			alert("You have entered an invalid email address!");
			$("#emailId").focus();
			return false;
		}

		if (aadharNo == null || aadharNo == "" || aadharNo == "0") {
			alert("Enter Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (aadharNo != null && aadharNo != "" && aadharNo.length != 12) {
			alert("Enter 12-digit Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (confirm("Do you want to save employee details?")) {
			document.forms[0].mode.value = "saveEmployeeDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}

	function verifyAadhaar() {
		var aadharNo = $("#aadharNo").val();
		if (validate(aadharNo)) {

		} else {
			alert("Invalid aadhar no.");
			$("#aadharNo").val("");
		}
	}

	function assignCaseToEmployee(cino) {
		var heading = "Assign Case to Employee";
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./DailyStatusEntry.do?mode=assignCase2EmpPage&cino=" + cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
	function viewCaseDetailsPopup(cino) {
		var heading = "View Instructions for CINO : "+cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino=" + cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
	function viewCaseDetailsPopup1(cino,caseType) {
		var heading = "View Instructions Details for CINO : "+cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./DailyStatusEntryByGP.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino=" + cino+"&caseType="+caseType;
			 //alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
</script>
