<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'> -->
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css' />

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<!-- <link href="assetsnew/css/main.min.css" rel="stylesheet" /> -->

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
					High Court Cases List
				</logic:notEmpty>
	</h1>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/AssignmentOfCasesByAG"
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
					<div class="ibox-title">List of High Court Cases </div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Registration Year</label>
								<html:select styleId="regYear" property="dynaForm(regYear)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<html:option value="ALL">ALL</html:option>
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
								<label class="font-bold">Date of Registration(From Date)</label>
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
								<label class="font-bold">Date of Registration (To Date)</label>
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
						<%-- <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Purpose</label>
								<html:select styleId="purpose" property="dynaForm(purpose)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(purposeList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(purposeList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div> --%>
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
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
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
					</div>
					<!-- <div class="row">
		<div class="col-md-12 col-xs-12">
			<small>Cases filed in the year 2021 and 2022 were listed below. To view
				and process other cases please use the Year filter or select
				From and To dates. Press the "Get Cases" button for the Cases
				List. </small>
		</div>
	</div> -->
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="submit" value="Get Cases"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>

									</div>
			</div>

			<logic:notEmpty name="CASESLIST">
				<div class="ibox">
					
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th></th>
										<th>CINo</th>
										<th>Scanned Affidavit</th>
										<!-- <th>Assigned to</th> -->
										<th>Date of Filing</th>
										<!-- <th>Case Type</th>
										<th>Reg.No.</th>
										<th>Reg. Year</th> -->

										<th>Case Reg No.</th>
										<th>Prayer</th>

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
										<!-- <th>Assign To</th> -->
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>
												<%-- <input type="checkbox" name="caseIds" value="${map.cino}" id="caseIds" class="form-control"/> --%>
												<!-- <label class="ui-checkbox ui-checkbox-gray">
                                            <input type="checkbox">
                                            <span class="input-span"></span></label> -->

												<div class="form-group">
													<label class="ui-checkbox"> <input type="checkbox"
														name="caseIds" value="${map.cino}" id="caseIds" /> <span
														class="input-span"></span></label>
												</div>

											</td>
											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

											</td>
											<td><logic:notEmpty name="map"
													property="scanned_document_path1">
													<logic:notEqual value="-" name="map"
														property="scanned_document_path1">
														<%-- ./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf --%>
														<a href="./${map.scanned_document_path}" target="_new"
															class="btn btn-sm btn-info"><i
															class="glyphicon glyphicon-save"></i><span>Scanned
																Affidavit</span></a>
													</logic:notEqual>
												</logic:notEmpty></td>
											<%-- <td nowrap="nowrap">${map.globalorgname}<br />
												${map.fullname} - ${map.designation} <br />
												${map.mobile} - ${map.email}
											</td> --%>
											
											
											<td nowrap="nowrap"><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>

											<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
											<td nowrap="nowrap" >${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
											<td style="min-width: 300px;text-align: justify;"><logic:notEmpty
												name="map" property="prayer">

												<logic:equal value="-" name="map" property="prayer">
												N/A
												</logic:equal>

												<logic:notEqual value="-" name="map" property="prayer">
										
										
										${map.prayer }
										
										<button class="btn btn-info btn-xs" data-container="body"
														data-toggle="popover" data-trigger="hover"
														data-placement="top" data-content="${map.prayer_full }"
														data-original-title="" title="">View More</button>
												</logic:notEqual>
											</logic:notEmpty></td>

											<td>${map.fil_no}</td>
											<td>${map.fil_year }</td>
											<td nowrap="nowrap"><logic:notEmpty name="map" property="date_next_list">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_next_list">
																	${map.date_next_list }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.bench_name }</td>
											<td>Hon'ble Judge : ${map.coram }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name },${map.address}</td>

											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>
											<td style="text-align: center;">${map.orderpaths }</td>

										</tr>
										
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="19">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-12">
						<div class="ibox">
							<div class="ibox-head">
								<div class="ibox-title">Assign Cases</div>
								<div class="ibox-tools">
									<a class="ibox-collapse"><i class="fa fa-minus"></i></a>
								</div>
							</div>
							<div class="ibox-body">

								<%-- <div class="row">
									<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label>Remarks</label>
											<html:textarea styleId="caseRemarks"
												property="dynaForm(caseRemarks)" styleClass="form-control"
												cols="50" rows="5">

											</html:textarea>
										</div>
									</div>
								</div> --%>
								<div class="row">
					<div class="col-md-3 col-xs-12 pull-right">
						<b> Remarks: </b>
					</div>
					<div class="col-md-3 col-xs-12">
						<html:textarea styleId="caseRemarks"
							property="dynaForm(caseRemarks)" styleClass="form-control"
							cols="50" rows="5">
						</html:textarea>
					</div>
					
							<div class="col-md-3 col-xs-12 pull-right">
								<b> Upload file: </b>
							</div>
							<div class="col-md-3 col-xs-12">
								<html:file property="changeLetter" styleId="changeLetter"
									styleClass="form-control"></html:file>
							</div>
				</div>
								<hr />

								<div class="row">
									<div class="col-md-6">

											<div class="form-group">
												<label>Select Employee <bean:message
														key="mandatory" /></label>
												<html:select styleId="emp_id"
													property="dynaForm(emp_id)" styleClass="select2Class"
													style="width:100%;">
													<html:option value="0">---SELECT---</html:option>
													<logic:notEmpty name="CommonForm"
														property="dynaForm(AGOFFICELIST)">
														<html:optionsCollection name="CommonForm"
															property="dynaForm(AGOFFICELIST)" />
													</logic:notEmpty>
												</html:select>
											</div>
											<div class="form-group">
												<input type="submit" name="submit"
													value="Assign Cases"
													class="btn btn-sm btn-primary"
													onclick="return fnAssignCase();" />
											</div>
								</div>
							</div>
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
	
function fnAssignCase(){
		
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
		if($("#emp_id").val()==null || $("#emp_id").val()=="" || $("#emp_id").val()=="0"){
			alert("Select Emp.");
			$("#emp_id").focus();
			return false;
		}
		else{
			$("#mode").val("assignCase");
			$("#HighCourtCasesListForm").submit();
		}
	}
	
	function fnShowCases() {
		
		
		/* if(($("#regYear").val()=="" || $("#regYear").val()=="0") 
				&& ($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $("#dofFromDate").val() == "0")
		&& 	($("#purpose").val() == null || $("#purpose").val() == "" || $("#purpose").val() == "0")
		&& ($("#districtId").val() == null || $("#districtId").val() == "" || $("#districtId").val() == "0")
		){
			alert("Please select a filter to get the data.");
			return false;
		}
		
		if (!($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $("#dofFromDate").val() == "0")
				&& ($("#dofToDate").val() == null || $("#dofToDate").val() == "" || $("#dofToDate").val() == "0")
		)
		{
			alert("Select Filing To Date");
			$("#dofToDate").focus();
			return false;
		} */
		
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
		$.post("AssignmentOfCasesByAG.do", data).done(function(res) {
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
			srclink = "./AssignmentOfCasesByAG.do?mode=assignCase2EmpPage&cino=" + cino;
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
