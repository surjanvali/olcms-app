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
		enctype="multipart/form-data" styleId="HighCourtCasesListForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds"
			property="dynaForm(selectedCaseIds)" />
		<html:hidden styleId="respondentIds"
			property="dynaForm(respondentIds)" />


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

					<div class="ibox-body">
						<div class="table-responsive">
							<%--<div class="row">
								<div class="col-md-12 col-xs-12">
									<b>Selected Cases</b>
								</div>
							</div>

							 <html:textarea styleId="testselectedCaseIds"
								property="dynaForm(testselectedCaseIds)"
								styleClass="form-control" cols="50" rows="3" />
							<br /> --%>

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<!-- <th></th> -->
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
										<!-- <th>Purpose</th>
										<th>Respondents</th>
										<th>Petitioner Advocate</th>
										<th>Respondent Advocate</th>
										<th>Orders</th> -->
										<!-- <th>Assign To</th> -->
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }.</td>
										<%-- 	<td>

												<div class="form-group">
													<label class="ui-checkbox"> <input type="checkbox"
														name="caseIds" value="${map.cino}" id="caseIds"
														onchange="countValues();" /> <span class="input-span"></span></label>
												</div>

											</td> --%>
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


											<td nowrap="nowrap"><logic:notEmpty name="map"
													property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>

											<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
											<td nowrap="nowrap">${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
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
											<td nowrap="nowrap"><logic:notEmpty name="map"
													property="date_next_list">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_next_list">
																	${map.date_next_list }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.bench_name }</td>
											<td>Hon'ble Judge : ${map.coram }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<%-- <td>${map.purpose_name }</td>
											<td>${map.res_name },${map.address}</td>

											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>
											<td style="text-align: center;">${map.orderpaths }</td> --%>

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

				<%-- <div class="row">
					<div class="col-md-12">
						<div class="ibox">
							<div class="ibox-head">
								<div class="ibox-title">Assign Cases</div>
								<div class="ibox-tools">
									<a class="ibox-collapse"><i class="fa fa-minus"></i></a>
								</div>
							</div>
							<div class="ibox-body">

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
											<label>Select Employee <bean:message key="mandatory" /></label>
											<html:select styleId="emp_id" property="dynaForm(emp_id)"
												styleClass="select2Class" style="width:100%;">
												<html:option value="0">---SELECT---</html:option>
												<logic:notEmpty name="CommonForm"
													property="dynaForm(AGOFFICELIST)">
													<html:optionsCollection name="CommonForm"
														property="dynaForm(AGOFFICELIST)" />
												</logic:notEmpty>
											</html:select>
										</div>
										<div class="form-group">
											<input type="submit" name="submit" value="Assign Cases"
												class="btn btn-sm btn-primary"
												onclick="return fnAssignCase();" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
 --%>			</logic:notEmpty>
		</div>
			
			
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Assign Cases</div>
				</div>

				<div class="ibox">

					<div class="row">

						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<table id="RESPSTABID" class="table table-bordered">
								<thead>
									<tr>
										<th colspan="10">Case Details: <span class="pull-right"><input
												type="button" value="Add" class="btn btn-sm btn-success"
												name="add" id="addResp" /> <input type="button"
												value="Remove" class="btn btn-sm btn-danger" name="remove"
												id="removeResp" /></span></th>
									</tr>
									<tr>
										<th style="width: 10%;">Sl No.</th>
										<th style="width: 30%;">Case Type</th>
										<th style="width: 30%;">Case Year</th>
										<th style="width: 10%;">Case Number</th>
									<!-- 	<th style="width: 20%;">Case Details</th> -->

									</tr>
								</thead>

								<tbody>
									<logic:notPresent name="cfmsdata">
										<tr id="1">
											<td>${i+1}</td>
											<td><html:select styleId="caseTypeId1"
													property="dynaForm(caseTypeId1)"
													styleClass="form-control select2Class">
													<html:option value="0">---SELECT---</html:option>
													<logic:notEmpty name="CommonForm"
														property="dynaForm(caseTypesList)">
														<html:optionsCollection name="CommonForm"
															property="dynaForm(caseTypesList)" />
													</logic:notEmpty>
												</html:select></td>
											<td><html:select styleId="regYear1"
													property="dynaForm(regYear1)"
													styleClass="form-control select2Class">
													<html:option value="0">---SELECT---</html:option>
													<%-- <html:option value="ALL">ALL</html:option> --%>
													<logic:notEmpty name="CommonForm"
														property="dynaForm(yearsList)">
														<html:optionsCollection name="CommonForm"
															property="dynaForm(yearsList)" />
													</logic:notEmpty>
												</html:select></td>
											<td><html:text styleId="caseNumber1"
													styleClass="form-control" style="height:30px;width:300px"
													property="dynaForm(caseNumber1)" maxlength="50" /></td>
													
												<!-- 	<td><div class="col-md-12 col-xs-12">
													<input type="submit" name="submit" value="Case Details"
														class="btn btn-success"
														onclick="return getData();" />
												</div></td> -->
										</tr>
									</logic:notPresent>
									<logic:present name="cfmsdata">
										<logic:iterate id="map" name="cfmsdata" indexId="i">
											<tr id="1">

												<td>${i+1}</td>
												<td><html:text styleId="caseTypeId${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														property="dynaForm(caseTypeId${i+1})" maxlength="10" /></td>
												<td><html:text styleId="regYear${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														property="dynaForm(regYear${i+1})" maxlength="15" /></td>
												<td><html:text styleId="caseNumber${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														property="dynaForm(caseNumber${i+1})" maxlength="50" /></td>
														<%-- <td>
													<input type="submit" name="submit" value="Case Details${i+1}" 
														class="btn btn-success"
														onclick="return getData();" />
												</td> --%>
											</tr>
										</logic:iterate>
									</logic:present>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="ibox">
								<div class="ibox-body">
									<div class="row ">
							<div class="col-md-3 col-xs-12 pull-right">
								<b> Enter Remarks </b>
							</div>
							<div class="col-md-3 col-xs-12">
								<html:textarea styleId="caseRemarks"
									property="dynaForm(caseRemarks)" styleClass="form-control"
									cols="50" rows="5">
								</html:textarea>
							</div>
						</div>
						<div class="row ">
							<div class="col-md-3 col-xs-12 pull-right">
								<b> Upload file: </b>
							</div><br>
							<div class="col-md-3 col-xs-12">
								<html:file property="changeLetter" styleId="changeLetter"
									styleClass="form-control"></html:file>
							</div>
						</div>
									<hr />
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label>Select Employee <bean:message key="mandatory" /></label>
												<html:select styleId="emp_id" property="dynaForm(emp_id)"
													styleClass="select2Class" style="width:100%;">
													<html:option value="0">---SELECT---</html:option>
													<logic:notEmpty name="CommonForm"
														property="dynaForm(AGOFFICELIST)">
														<html:optionsCollection name="CommonForm"
															property="dynaForm(AGOFFICELIST)" />
													</logic:notEmpty>
												</html:select>
											</div>
											<div class="row" style="text-align: right">
												<div class="col-md-12 col-xs-12">
													<input type="submit" name="submit" value="Assign Cases"
														class="btn btn-success"
														onclick="return fnSubmitCategory();" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
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
	
	$('#panel-modal').modal({backdrop : 'static', keyboard : false});
	$('#panel-modal').modal('show');
	
	
	$(".btnClosePopup").click(function () {
		$("#panel-modal").modal("hide");
	});
});

	/* function getData() {
		
		if($("#caseTypeId1").val()==null || $("#caseTypeId1").val()=="" || $("#caseTypeId1").val()=="0" ){
			alert("Enter case Type");
			$("#caseTypeId").focus();
			return false;
		} 
		if($("#regYear1").val()==null || $("#regYear1").val()=="" || $("#regYear1").val()=="0" ){
			alert("Enter Reg Year");
			$("#regYear1").focus();
			return false;
		} 
		if($("#caseNumber1").val()==null || $("#caseNumber1").val()=="" || $("#caseNumber1").val()=="0" ){
			alert("Enter case Type");
			$("#caseNumber1").focus();
			return false;
		} 
		
		
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	} */
	
	
	function countValues(){
		//alert("val--"+val);
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#testselectedCaseIds").val(testval);
		// alert("testselectedCaseIds:"+$("#testselectedCaseIds").val());
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
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
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




<script type="text/javascript">
	function fnSubmitCategory() {
		
		if($("#caseTypeId1").val()==null || $("#caseTypeId1").val()=="" || $("#caseTypeId1").val()=="0" ){
			alert("Enter case Type");
			$("#caseTypeId1").focus();
			return false;
		} 
		if($("#regYear1").val()==null || $("#regYear1").val()=="" || $("#regYear1").val()=="0" ){
			alert("Enter Reg Year");
			$("#regYear").focus();
			return false;
		} 
		if($("#caseNumber1").val()==null || $("#caseNumber1").val()=="" || $("#caseNumber1").val()=="0" ){
			alert("Enter case Type");
			$("#caseNumber1").focus();
			return false;
		} 
		
		
		
		if($("#caseRemarks").val()==null || $("#caseRemarks").val()=="" ){
			alert("Enter Remarks");
			$("#caseRemarks").focus();
			return false;
		}
		
		if($("#emp_id").val()==null || $("#emp_id").val()=="" || $("#emp_id").val()=="0"){
			alert("Select Employee");
			$("#emp_id").focus();
			return false;
		}
		
		$("#mode").val("assignCase");
		$("#respondentIds").val(
				$("#RESPSTABID tbody tr").length);
		$("#HighCourtCasesListForm").submit();
	}

	$("#addResp")
			.click(
					function() {
						//alert("wait....")
						let rowfyable = $("#RESPSTABID").closest('table');
						//let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
						let rowCount = $("#RESPSTABID tbody tr").length; //$('tbody', rowfyable).rows.length;
						let rowCount2 = rowCount + 1;
						let caseTypeId = $("#caseTypeId" + rowCount).val();
						let regYear = $("#regYear" + rowCount).val();
						let caseNumber = $("#caseNumber" + rowCount).val();

						//alert("prevVal--"+caseNumber);
						if ((caseTypeId != null && caseTypeId != "" && caseTypeId != "All")
								&& (regYear != null && regYear != "" && regYear != "0") && (caseNumber != null && caseNumber != "" && caseNumber != "0")) {
							console.log("rowCount:"+rowCount);
							// console.log("rowCount2:"+rowCount2);

							$('tbody', rowfyable)
									.append(
											"<tr id='"+rowCount2+"'><td>"
													+ rowCount2
													+ ".</td><td><select name='dynaForm(caseTypeId" + rowCount2 + ")' id='caseTypeId" + rowCount2 + "' style='width: 100%;' ></select></td><td>"
													+ "<select name='dynaForm(regYear" + rowCount2 + ")' id='regYear" + rowCount2 + "' style='width: 100%;' ></select></td>"
													+ "<td><input type='text' name='dynaForm(caseNumber"
													+ rowCount2
													+ ")' id='caseNumber"
													+ rowCount2
													+ "' style='height:30px;width:300px' ></text></td></tr>"+ "");  /* <td><input type='submit' name='submit' value='Case Details'onclick='return getData();' /></td> */
							
							$("#caseTypeId" + rowCount + " option").clone().appendTo("#caseTypeId" + rowCount2);
							$("#caseTypeId" + rowCount2).select2();
							$("#caseTypeId" + rowCount2).select2("val", "0");
							
							
							
							$("#regYear" + rowCount + " option").clone().appendTo("#regYear" + rowCount2);
							$("#regYear" + rowCount2).select2();
							$("#regYear" + rowCount2).select2("val", "0");
							

							$("#respondentIds").val(
									$("#RESPSTABID tbody tr").length);
						}
//alert("caseTypeId---"+caseTypeId);
						
						if ((caseTypeId == "" || caseTypeId == "0")) {
							alert("Pease Select Case Type");
							$("#caseTypeId" + rowCount).focus();
						}
					//	alert("regYear---"+regYear);
						if ((regYear == "" ||regYear == "0")) {
							alert(" Pease Select Case Reg Year");
							$("#regYear" + rowCount).focus();
						}
						//alert("caseNumber---"+caseNumber);
						if ((caseNumber == "")) {
							alert("Enter Case Number");
							$("#caseNumber" + rowCount).focus();
						}

					});

	$("#removeResp").click(function() {
		let rowfyable = $("#RESPSTABID").closest('table');
		let rowCount = $("#RESPSTABID tbody tr").length;
		if (rowCount > 1) {
			$('tbody tr:last', rowfyable).remove();
		}
		else {
			alert ("Should have atleast one valid Case Details")
		}
	});

</script>


