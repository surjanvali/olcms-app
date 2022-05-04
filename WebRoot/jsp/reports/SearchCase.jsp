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
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									High Court Cases List
								</logic:notEmpty>
	</h1>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/SearchCase"
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

			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Search Case</div>
				</div>
				<div class="ibox-body">
					<!-- <h4 class="m-t-0 header-title">
						<b>High Court Cases List</b>
					</h4>
					<hr /> -->

					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label for="sel1"><bean:message key="wpaffd.casetype" />
									<bean:message key="mandatory" /> </label>
								<html:select property="dynaForm(caseType)"
									styleClass="select2Class" style="width: 100%;"
									styleId="caseType">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty property="dynaForm(caseTypesList)"
										name="CommonForm">
										<html:optionsCollection property="dynaForm(caseTypesList)"
											name="CommonForm" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Registration Year</label>
								<html:select styleId="regYear" property="dynaForm(regYear)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
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
								<label class="font-bold">Case Reg. No</label>

								<html:text styleId="regNo" property="dynaForm(regNo)"
									styleClass="form-control" maxlength="6" />

							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">CIN No</label>

								<html:text styleId="cinNo" property="dynaForm(cinNo)"
									styleClass="form-control" maxlength="25" />

							</div>
						</div>
						
					</div>
					
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="submit" value="Get Case"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>


				</div>
			</div>

			<logic:notEmpty name="CASESLIST">
				<div class="ibox">
					<%-- <div class="ibox-head">
				<div class="ibox-title">
					<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
				</div>
			</div> --%>
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th></th>
										<th>CINo</th>
										<th>Date of Filing</th>
										<th>Case Type</th>
										<th>Reg.No.</th>
										<th>Reg. Year</th>
										<!-- <th>Filing No.</th>
										<th>Filing Year</th>
										<th>Date of Next List</th>
										<th>Bench</th>
										<th>Judge Name</th> -->
										<th>Petitioner</th>

										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>

										<th>Petitioner Advocate</th>
										<th>Respondent Advocate</th>

										<th>Orders</th>
										<!-- <th>Assign To</th> -->
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td></td>
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
											<td>${map.fil_year }</td>
											<td><logic:notEmpty name="map" property="date_next_list">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.bench_name }</td>
											<td>Hon'ble Judge : ${map.coram }</td> --%>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name }</td>

											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>

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
				</div>

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
	
	
	function fnAssign2DeptHOD(){
		
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#selectedCaseIds").val(testval);
		 //alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if($("#selectedCaseIds").val()==null || $("#selectedCaseIds").val()=="" || $("#selectedCaseIds").val()=="0"){
			alert("Select atleast a case to submit.");
			return false;
		}
		if($("#caseDept").val()==null || $("#caseDept").val()=="" || $("#caseDept").val()=="0"){
			alert("Select Department.");
			$("#caseDept").focus();
			return false;
		}
		else{
			$("#mode").val("assign2DeptHOD");
			$("#HighCourtCasesListForm").submit();
		}
	}
	
function fnAssign2DistHOD(){
		
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#selectedCaseIds").val(testval);
		 var chkdVal= $("#officerType:checked").val();
		 
		 // alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if($("#selectedCaseIds").val()==null || $("#selectedCaseIds").val()=="" || $("#selectedCaseIds").val()=="0"){
			alert("Select atleast a case to submit.");
			return false;
		}
		if($("#caseDist").val()==null || $("#caseDist").val()=="" || $("#caseDist").val()=="0"){
			alert("Select District.");
			$("#caseDist").focus();
			return false;
		}
		
		else if(chkdVal=="DC-NO" && ($("#distDept").val()==null || $("#distDept").val()=="" || $("#distDept").val()=="0")){
			alert("Select Department.");
			$("#distDept").focus();
			return false;
		}
		else{
			$("#mode").val("assign2DistCollector");
			// alert($("#mode").val());
			
			$("#HighCourtCasesListForm").submit();
		}
	}
	
	function fnAssign2DeptHOD2(){
		
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#selectedCaseIds").val(testval);
		// alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if($("#selectedCaseIds").val()==null || $("#selectedCaseIds").val()=="" || $("#selectedCaseIds").val()=="0"){
			alert("Select atleast a case to submit.");
			return false;
		}
		else if ($("#empDept").val() == null || $("#empDept").val() == "" || $("#empDept").val() == "0") {
			alert("Select Employee Department");
			$("#empDept").focus();
			return false;
		}
		else if ($("#empSection").val() == null || $("#empSection").val() == "" || $("#empSection").val() == "0") {
			alert("Select Employee Section");
			$("#empSection").focus();
			return false;
		} else if ($("#empPost").val() == null || $("#empPost").val() == "" || $("#empPost").val() == "0") {
			alert("Select Post");
			$("#empPost").focus();
			return false;
		} else if ($("#employeeId").val() == null || $("#employeeId").val() == "" || $("#employeeId").val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId").focus();
			return false;
		} 
		else{
			$("#mode").val("assignMultiCases2Section");
			$("#HighCourtCasesListForm").submit();
		}
	}
	
	
	function fnShowCases() {
		
		
		
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
	
	
	
	
	function assignCourtCase2() {
	var cINO = $("#cINO").val();
		// alert("AJAX SUBMIT:	"+cINO);
		if (cINO == null || cINO == "" || cINO == "0") {
			alert("Invalid data");
			return false;
		} 
		else if ($("#empDept").val() == null || $("#empDept").val() == "" || $("#empDept").val() == "0") {
			alert("Select Employee Department");
			$("#empDept").focus();
			return false;
		}
		else if ($("#empSection").val() == null || $("#empSection").val() == "" || $("#empSection").val() == "0") {
			alert("Select Employee Section");
			$("#empSection").focus();
			return false;
		} else if ($("#empPost").val() == null || $("#empPost").val() == "" || $("#empPost").val() == "0") {
			alert("Select Post");
			$("#empPost").focus();
			return false;
		} else if ($("#employeeId").val() == null || $("#employeeId").val() == "" || $("#employeeId").val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId").focus();
			return false;
		} else {
			document.forms[0].mode.value = "assignCase";
			document.forms[0].submit();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	function assignCourtCase(cINO) {
		// alert("AJAX SUBMIT:	"+cINO);
		if (cINO == null || cINO == "" || cINO == "0") {
			alert("Invalid data");
			return false;
		} else if ($("#empDept" + cINO).val() == null || $("#empDept" + cINO).val() == "" || $("#empDept" + cINO).val() == "0") {
			alert("Select Employee Department");
			$("#empDept" + cINO).focus();
			return false;
		} else if ($("#empSection" + cINO).val() == null || $("#empSection" + cINO).val() == "" || $("#empSection" + cINO).val() == "0") {
			alert("Select Employee Section");
			$("#empSection" + cINO).focus();
			return false;
		} else if ($("#empPost" + cINO).val() == null || $("#empPost" + cINO).val() == "" || $("#empPost" + cINO).val() == "0") {
			alert("Select Post");
			$("#empPost" + cINO).focus();
			return false;
		} else if ($("#employeeId" + cINO).val() == null || $("#employeeId" + cINO).val() == "" || $("#employeeId" + cINO).val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId" + cINO).focus();
			return false;
		} else {
			//document.forms[0].mode.value = "assignCourtCaseToEmp";
			//document.forms[0].submit();
			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Saving your data. Please wait...');
			$("#ASSIGN" + cINO).hide();
			var data = {
				mode : "assignCase2Employee",
				cino : cINO,
				emp_dept : $("#empDept" + cINO).val(),
				emp_section : $("#empSection" + cINO).val(),
				emp_post : $("#empPost" + cINO).val(),
				emp_id : $("#employeeId" + cINO).val()
			}
			$.post("HighCourtCasesList.do", data).done(function(res) {
				$("#MSG" + cINO).html(res);

			}).fail(function(exc) {
				// $("#ASSIGN"+cINO).html(res);
				$("#ASSIGN" + cINO).show();
				$("#MSG" + cINO).html(res);
			// alert("Error Occured.Please Try Again.");
			});
			$("#MSG" + cINO).focus();
			setTimeout(function() {
				$("#LOADINGPAGEGIF").html("");
			}, 900);
		}
	}

	/*$(document).ready(function() {
		$(".select2Class").select2();
		
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
		
		//$(".select2_demo_1").select2();
		if ($('#example2')) {
			$('#example2').DataTable();
			var table = $('#example2').DataTable({
				dom : 'Bfrtip',
				buttons : [ {
					extend : 'pdfHtml5',
					orientation : 'landscape',
					title : 'Issue type wise pendency Report',
					pageSize : 'LEGAL'
				}, {
					extend : 'excelHtml5',
					title : 'Issue type wise pendency Report'
				}, {
					extend : 'print',
					title : 'Issue type wise pendency Report'
				} ],
				responsive : true,
				"lengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"pageLength" : -1,
			});
			
			new $.fn.dataTable.FixedHeader( table );*/

			/* $('#example2').dataTable({
				"pageLength" : 10,
				"buttons" : [ 'copy', 'excel', 'pdf', 'colvis' ]
			}); */

		/* $(document).ready(function() {
			var table = $('#example').DataTable({
				lengthChange : false,
				buttons : [ 'copy', 'excel', 'pdf', 'colvis' ]
			});

			table.buttons().container()
				.appendTo('#example_wrapper .col-sm-6:eq(0)');
		}); 
		}

	// $("#purpose").select2();
	});*/

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

	
	
	function viewCaseDetailsPopup(cino) {
		var heading = "View Case Details for CINO : "+cino;
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
	
</script>
