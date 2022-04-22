<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- PAGE LEVEL STYLES
-->
<style type="text/css">
label {
	font-weight: bold;
}
</style>
<!--  <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'> -->

<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />

<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING"> ${HEADING } </logic:notEmpty>
	</h1>
</div> --%>
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
			<div class="ibox-title" style="width: 100%;">
				<logic:notEmpty name="ACKDATA">
					<b>Acknowledgements Generated</b>
					<span class="pull-right"><input type="button" name="Submit"
						value="Submit Acknowledgement" class="btn btn-primary pull-right"
						onclick="showAckEntry();" /> </span>
				</logic:notEmpty>

				<logic:empty name="ACKDATA">Acknowledgement Generation</logic:empty>

			</div>
			<div class="ibox-tools">
				<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
				<!-- <a
					class="dropdown-toggle" data-toggle="dropdown"><i
					class="fa fa-ellipsis-v"></i> </a>  <div class="dropdown-menu dropdown-menu-right">
					<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
						2</a>
				</div> -->
			</div>
		</div>
		<div class="ibox-body">
			<html:form method="post" action="/GPOAck"
				enctype="multipart/form-data">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden styleId="ackId" property="dynaForm(ackId)" />
				<html:hidden styleId="respondentIds"
					property="dynaForm(respondentIds)" value="1" />
				<logic:empty name="ACKDATA">
					<!-- 
						<div class="page-heading">
							<h4 class="m-t-0 header-title">
								<b>Acknowledgement Generation</b>
							</h4>
						</div>
						<hr> 
					-->
					<%-- <div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Department <bean:message key="mandatory" />
								</label>
								<html:select styleId="deptId" property="deptId" multiple="true"
									styleClass="select2Class" style="width: 100%;">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(deptList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div> --%>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Department <bean:message key="mandatory" />
								</label>
								<table style="width: 100%;border: 1px solid #000;">
									<tr>
										<td style="width: 20%;">Respondent - 1</td>
										<td style="width: 80%;"><html:select styleId="deptId1" property="dynaForm(deptId1)" styleClass="select2Class"
												style="width: 100%;">
												<html:option value="0">---SELECT---</html:option>
												<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
													<html:optionsCollection name="CommonForm" property="dynaForm(deptList)" />
												</logic:notEmpty>
											</html:select>
											</td>
									</tr>
								</table>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> District <bean:message key="mandatory" />
								</label>
								<html:select styleId="distId" property="dynaForm(distId)"
									styleClass="select2Class" style="width: 100%;">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Petitioner Name <bean:message key="mandatory" />
								</label>
								<html:text styleId="petitionerName" styleClass="form-control"
									property="dynaForm(petitionerName)" maxlength="150" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Advocate CC No. <bean:message key="mandatory" />
								</label>
								<html:text styleId="advocateCCno" styleClass="form-control"
									property="dynaForm(advocateCCno)" maxlength="25" />
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Advocate Name <bean:message key="mandatory" />
								</label>
								<html:text styleId="advocateName" styleClass="form-control"
									property="dynaForm(advocateName)" maxlength="150" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Main Case No. (WP/WA/AS/CRP Nos.) </label>
								<html:text styleId="mainCaseNo" styleClass="form-control"
									property="dynaForm(mainCaseNo)" maxlength="25" />
							</div>
						</div>
					</div>
					<%-- 	
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label for="sel1">Select GP <bean:message
										key="mandatory" />
								</label>
								<html:select property="dynaForm(gpCode)"
									styleClass="select2Class" style="width: 100%;" styleId="gpCode">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty property="dynaForm(gpsList)" name="CommonForm">
										<html:optionsCollection property="dynaForm(gpsList)"
											name="CommonForm" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Upload Documents(If Any) </label>
								<html:file styleId="changeLetter" styleClass="form-control"
									property="changeLetter" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">

								<table id="RESPSTABID" class="table table-bordered"
									style="width: 100%;">
									<thead>
										<tr>
											<th colspan="3">Respondents <span class="pull-right"><input
													type="button" value="Add" class="btn btn-sm btn-success"
													name="add" id="addResp" /> <input type="button"
													value="Remove" class="btn btn-sm btn-danger" name="remove"
													id="removeResp" /></span></th>
										</tr>
										<tr>
											<th>Sl No.</th>
											<th>Respondent Name</th>
											<th>Address</th>
										</tr>
									</thead>
									<tbody>
										<tr id="1">
											<td>1.</td>
											<td><html:text styleId="respondantName_1"
													styleClass="form-control"
													property="dynaForm(respondantName_1)" maxlength="125" /></td>
											<td><html:textarea styleId="respondantAddr_1"
													styleClass="form-control"
													property="dynaForm(respondantAddr_1)" cols="50" rows="3" /></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div> --%>

					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Services / Non-Services <bean:message
										key="mandatory" />
								</label>
								<html:select property="dynaForm(serviceNonService)"
									styleClass="select2Class" style="width: 100%;"
									styleId="serviceNonService">
									<html:option value="0">---SELECT---</html:option>
									<html:option value="SERVICES">SERVICES</html:option>
									<html:option value="NON-SERVICES">NON-SERVICES</html:option>

								</html:select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group servicesDiv">
								<label> Services <bean:message key="mandatory" />
								</label>
								<html:select property="dynaForm(serviceType)"
									styleClass="select2Class" style="width: 100%;"
									styleId="serviceType">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty property="dynaForm(serviceTypesList)"
										name="CommonForm">
										<html:optionsCollection property="dynaForm(serviceTypesList)"
											name="CommonForm" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Remarks </label>
								<html:textarea styleId="remarks" styleClass="form-control"
									property="dynaForm(remarks)" rows="5" cols="50" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<logic:present name="saveAction">
								<input type="button" name="Submit" value="show Ack List"
									class="btn btn-primary pull-right" onclick="showAckList();" /> &nbsp;&nbsp;
												<logic:equal value="INSERT" name="saveAction">
									<input type="button" name="Submit" value="Submit"
										class="btn btn-success pull-right" onclick="saveAck();" />
								</logic:equal>
								<logic:equal value="UPDATE" name="saveAction">

									<input type="button" name="Submit" value="Update"
										class="btn btn-success pull-right" onclick="updateAck();" />
								</logic:equal>
							</logic:present>
						</div>
					</div>
				</logic:empty>
				<logic:notEmpty name="ACKDATA">
					<div class="row">
						<div class="col-md-12">
							<div class="table-responsive">
								<table class="table table-striped table-bordered table-hover"
									id="example">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Ack No.</th>
											<th>District</th>
											<th>Department</th>
											<th>Advocate Name</th>
											<th>Advocate CC No.</th>
											<th>Case Type</th>
											<th>Main Case No.</th>
											<th>Remarks</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										<logic:iterate id="map" name="ACKDATA" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.ack_no }</td>
												<td>${map.district_name }</td>
												<td>${map.dept_descs }</td>
												<td>${map.advocatename }</td>
												<td>${map.advocateccno }</td>
												<td>${map.case_full_name }</td>
												<td>${map.maincaseno }</td>
												<td>${map.remarks }</td>
												<td style="text-align: center;" nowrap="nowrap">
													<%-- <button type="button" class="btn btn-sm btn-warning"
														title="Edit Acknowledgement"
														onclick="editAck('${map.ack_no}')">
														<i class="fa fa-edit"></i><span>Edit</span>
													</button> --%>
													<button type="button" class="btn btn-sm btn-danger"
														title="Delete Acknowledgement"
														onclick="deleteAck('${map.ack_no}')">
														<i class="fa fa-trash"></i><span>Delete</span>
														<!-- <span>Delete</span> -->
													</button> <logic:present name="map" property="ack_file_path">
														<a href="./${map.ack_file_path}" target="_new"
															title="Download Acknowledgement"
															class="btn btn-sm btn-info"> <i class="fa fa-save"></i>
															<span>Download</span> <!-- <span>Download</span> -->
														</a>
													</logic:present> <logic:notPresent name="map" property="ack_file_path">
														<button type="button" class="btn btn-sm btn-info"
															onclick="downloadAck('${map.ack_no}')">
															<i class="fa fa-save"></i> <span>Download</span>
														</button>
													</logic:notPresent>

													<%-- <button type="button" class="btn btn-sm btn-info"
														onclick="downloadAck('${map.ack_no}')">
														<i class="fa fa-save"></i> <span>Download 2</span>
													</button> --%>
												</td>
											</tr>
										</logic:iterate>
									</tbody>
									<tfoot>
										<tR>
											<td colspan="10">&nbsp;</td>
										</tR>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</logic:notEmpty>
			</html:form>
		</div>
	</div>
</div>

<script src="https://apbudget.apcfss.in/js/select2.js"></script>

<script type="text/javascript">

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}
	
	$(document).ready(function() {
		$(".servicesDiv").hide();
		$("#serviceNonService").change(function(){
			if($("#serviceNonService").val()=="SERVICES"){
				$(".servicesDiv").show();
			}
			else{
				$("#serviceType").val("0");
				$(".servicesDiv").hide();
			}
		});
		
		$("#addResp").click(function(){
			let rowfyable = $("#RESPSTABID").closest('table');
			  let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
			  let rowCount = $("#RESPSTABID tbody tr").length; //$('tbody', rowfyable).rows.length;
			  $('tbody', rowfyable).append("<tr id='"+randomNo+"'><td>"+(rowCount + 1)+".</td><td><input type='text' id='respondantName_"+randomNo+"' class='form-control' name='dynaForm(respondantName_"+randomNo+")' maxlength='125' /></td>"
			  +"<td><textarea id='respondantAddr_"+randomNo+"'class='form-control' name='dynaForm(respondantAddr_"+randomNo+")' cols='50' rows='3' /></td></tr>");
			  $("#respondentIds").val($("#respondentIds").val()+","+randomNo);
		});
		$("#removeResp").click(function(){
			let rowfyable = $("#RESPSTABID").closest('table');
			let rowCount = $("#RESPSTABID tbody tr").length;
			if(rowCount > 1){
				$('tbody tr:last', rowfyable).remove();
			}
		});
		
		
 		$("#designationId").change(function() {

			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');

			$("#employeeId").select2('destroy');
			var data = {
				mode : "AjaxAction",
				deptId : $("#deptId").val(),
				designationId : $("#designationId").val(),
				getType : "getEmployeesList"
			}
			$.post("AjaxModels.do", data).done(function(res) {
				$("#employeeId").html(res);
			}).fail(function(exc) {
				$("#employeeId").html("<option value='0'>---SELECT---</option>");
				alert("Error Occured.Please Try Again.");
			});

			$("#employeeId").select2();
			$("#employeeId").select2("val", "0");

			$("#mobileNo").val("");
			$("#emailId").val("");
			$("#aadharNo").val("");

			setTimeout(function() {
				$("#LOADINGPAGEGIF").html("");
			}, 900);
		});

		$('.select2Class').select2();
	});

	function saveAck() {
		if ($("#distId").val() == null || $("#distId").val() == "" || $("#distId").val() == "0") {
			alert("District Required");
			$("#distId").focus();
			return false;
		} else if ($("#deptId").val() == null || $("#deptId").val() == "" || $("#deptId").val() == "0") {
			alert("Department Required");
			$("#deptId").focus();
			return false;
		} else if ($("#advocateName").val() == null || $("#advocateName").val() == "" || $("#advocateName").val() == "0") {
			alert("Advocate Name Required");
			$("#advocateName").focus();
			return false;
		} else if ($("#advocateCCno").val() == null || $("#advocateCCno").val() == "" || $("#advocateCCno").val() == "0") {
			alert("Advocate CC No. Required");
			$("#advocateCCno").focus();
			return false;
		} else if ($("#caseType").val() == null || $("#caseType").val() == "" || $("#caseType").val() == "0") {
			alert("Case Type Required");
			$("#caseType").focus();
			return false;
		} else if ($("#serviceType").val() == null || $("#serviceType").val() == "" || $("#serviceType").val() == "0") {
			alert("Service Type Required");
			$("#serviceType").focus();
			return false;
		} /* else if ($("#mainCaseNo").val() == null || $("#mainCaseNo").val() == "" || $("#mainCaseNo").val() == "0") {
			alert("Main Case No. Required");
			$("#mainCaseNo").focus();
			return false;
		} */ else if ($("#remarks").val() == null || $("#remarks").val() == "" || $("#remarks").val() == "0") {
			alert("Remarks Required");
			$("#remarks").focus();
			return false;
		} else if (confirm("Do you want to Proceed and save Acknowledgement details?")) {
			document.forms[0].mode.value = "saveAckDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}


	function updateAck() {
		if ($("#distId").val() == null || $("#distId").val() == "" || $("#distId").val() == "0") {
			alert("District Required");
			$("#distId").focus();
			return false;
		} else if ($("#deptId").val() == null || $("#deptId").val() == "" || $("#deptId").val() == "0") {
			alert("Department Required");
			$("#deptId").focus();
			return false;
		} else if ($("#advocateName").val() == null || $("#advocateName").val() == "" || $("#advocateName").val() == "0") {
			alert("Advocate Name Required");
			$("#advocateName").focus();
			return false;
		} else if ($("#advocateCCno").val() == null || $("#advocateCCno").val() == "" || $("#advocateCCno").val() == "0") {
			alert("Advocate CC No. Required");
			$("#advocateCCno").focus();
			return false;
		} else if ($("#caseType").val() == null || $("#caseType").val() == "" || $("#caseType").val() == "0") {
			alert("Case Type Required");
			$("#caseType").focus();
			return false;
		} else if ($("#mainCaseNo").val() == null || $("#mainCaseNo").val() == "" || $("#mainCaseNo").val() == "0") {
			alert("Main Case No. Required");
			$("#mainCaseNo").focus();
			return false;
		} else if ($("#remarks").val() == null || $("#remarks").val() == "" || $("#remarks").val() == "0") {
			alert("Remarks Required");
			$("#remarks").focus();
			return false;
		} else if (confirm("Do you want to Proceed and update Acknowledgement details?")) {
			document.forms[0].mode.value = "updateAckDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}


	function showAckEntry() {
		document.forms[0].mode.value = "displayAckForm";
		document.forms[0].submit();
	}

	function showAckList() {
		document.forms[0].mode.value = "getAcknowledementsList";
		document.forms[0].submit();
	}

	function editAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			document.forms[0].mode.value = "displayAckEditForm";
			document.forms[0].submit();
		}
	}
	
	
	function downloadAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			document.forms[0].mode.value = "downloadAckPDF";
			document.forms[0].submit();
		}
	}
	
	function deleteAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			if (confirm("Do you want to Proceed and Delete Acknowledgement details?")) {
				document.forms[0].mode.value = "deleteAck";
				document.forms[0].submit();
			}
		}
	}

	function populateDetails() {
		$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		var data = {
			mode : "AjaxAction",
			empId : $("#employeeId").val(),
			getType : "getEmpDetails"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				// alert("RES:"+res);
				// alert("RES1:"+res.split("#")[1]);
				$("#mobileNo").val(res.split("#")[1]);
				$("#emailId").val(res.split("#")[2]);
				$("#aadharNo").val(res.split("#")[3]);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});

		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}

	/* <script type="text/javascript">
			   $(document).ready(function() {
				    $("#designationId").change(function(){
						var data = {
							mode : "AjaxAction",
							designationId : $("#designationId").val(),
							getType : "getEmployeesList"
						}
						$.post("AjaxModels.do",data).done(function(res){
							$("#employeeId").html(res);
						}).fail(function(exc){
							$("#employeeId").html("<option value='0'>---SELECT---</option>");
							alert("Error Occured.Please Try Again");
						});
					});
					
				}); */

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




	function verifyAadhaar() {
		var aadharNo = $("#aadharNo").val();
		if (validate(aadharNo)) {

		} else {
			alert("Invalid aadhar no.");
			$("#aadharNo").val("");
		}
	}
</script>
