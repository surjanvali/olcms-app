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
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js"
	type="text/javascript"></script>

<!-- <script src="https://cdn.ckeditor.com/4.17.1/standard/ckeditor.js"></script> -->
<script type="text/javascript" src="//js.nicedit.com/nicEdit-latest.js"></script>

<!-- PAGE LEVEL STYLES-->
<style>
body {
	overflow-y: auto;
}
</style>

<div class="page-content fade-in-up">
	<html:form action="/AssignedNewCasesToEmp"
		styleId="AssignedCasesToSectionForm" enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(fileCino)" styleId="fileCino" />
		<html:hidden property="dynaForm(resident_id)" styleId="resident_id" />
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
					<h4 style="color:Black;">
						<logic:notEmpty name="HEADING">
									${HEADING}
								</logic:notEmpty>
					</h4>
				</div>
			</div>
			<div class="ibox-body">
				<logic:present name="USERSLIST">
					<logic:iterate id="map" name="USERSLIST">

						
						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Reported By: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.inserted_by}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Registration No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.ack_no}</div>
								
								<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>District : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.district_name}</div>
							
						</div>
						
						
						
						
						
						
						
						<%-- <div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of Next List </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.generated_date}</div>


							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of Decision : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.generated_date}</div>
								<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Department Description : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.dept_descs}</div>
							
						</div> --%>
						
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Advocate Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.advocatename}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Advocate No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.advocateccno}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Case Full Name : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.case_full_name}</div>
						</div>
						
<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Generated date: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.generated_date}</div>

							
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Main Case No.: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.maincaseno}</div>
								<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Department Description : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.dept_descs}</div>
						</div>

						

					</logic:iterate>

				</logic:present>
				<hr />
				<logic:present name="ACTIVITIESDATA">
					<div class="row">
						<div class="col-md-12">
							<table class="table table-striped table-bordered table-hover"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th colspan="7">Case Activities</th>
									</tr>
									<tr>
										<th>Sl No.</th>
										<th>Date</th>
										<th>Activity</th>
										<th>Updated By</th>
										<th>Assigned to</th>
										<th>Remarks</th>
										<th>Uploaded Document</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="activities" name="ACTIVITIESDATA"
										indexId="i">
										<tr>
											<td>${i+1}</td>
											<td>${activities.inserted_on}</td>
											<td>${activities.action_type}</td>
											<td>${activities.inserted_by}</td>
											<td>${activities.assigned_to}</td>
											<td>${activities.remarks }</td>
											<td style="text-align: center"><logic:notEmpty name="activities"
													property="uploaded_doc_path">

													<logic:notEqual value="---" name="activities"
														property="uploaded_doc_path">

														<a href='${activities.uploaded_doc_path}' target='_new'
															class="btn btn-sm btn-info">View Uploaded File</a>
													</logic:notEqual>
													<logic:equal value="---" name="activities"
														property="uploaded_doc_path">
														---
													</logic:equal>
												</logic:notEmpty></td>
										</tr>
									</logic:iterate>
								</tbody>

							</table>
						</div>
					</div>
				</logic:present>
				<hr />

				<div class="row">
					<%-- <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 form-group">
						<label>Upload Petition <bean:message key="mandatory" /></label>
						<html:file styleClass="form-control" styleId="petitionDocument"
							property="dynaForm(petitionDocument)" />
						<span class="help-block"><bean:message
								key="upload.document.validation.msg" /></span>

						<logic:notEmpty name="CommonForm"
							property="dynaForm(petitionDocumentOld)">
							<a
								href='<bean:write name="CommonForm" property="dynaForm(petitionDocumentOld)"/>'
								target='_new' class="btn btn-sm btn-info">View Uploaded File</a>
						</logic:notEmpty>

					</div> --%>

					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label>Case Status <bean:message key="mandatory" />
							</label>
							<html:select styleId="ecourtsCaseStatus"
								property="dynaForm(ecourtsCaseStatus)" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="Pending">Pending</html:option>
								<html:option value="Closed">Closed</html:option>
							</html:select>
						</div>
					</div>
				</div>


				<div class="row">
					<div class="col-sm-6 form-group casecloseddiv">
						<label>Upload Judgement Order <bean:message
								key="mandatory" /></label>

						<html:file styleClass="form-control" styleId="judgementOrder"
							property="dynaForm(judgementOrder)" />
						<span class="help-block"><bean:message
								key="upload.document.validation.msg" /></span>

						<logic:notEmpty name="CommonForm"
							property="dynaForm(judgementOrderOld)">
							<a
								href='<bean:write name="CommonForm" property="dynaForm(judgementOrderOld)"/>'
								target='_new' class="btn btn-sm btn-info">View Uploaded File</a>
						</logic:notEmpty>

					</div>
					<div class="col-sm-6 form-group casecloseddiv">
						<label>Action Taken Order <bean:message key="mandatory" /></label>

						<html:file styleClass="form-control" styleId="actionTakenOrder"
							property="dynaForm(actionTakenOrder)" />
						<span class="help-block"><bean:message
								key="upload.document.validation.msg" /></span>

						<logic:notEmpty name="CommonForm"
							property="dynaForm(actionTakenOrderOld)">
							<a
								href='<bean:write name="CommonForm" property="dynaForm(actionTakenOrderOld)"/>'
								target='_new' class="btn btn-sm btn-info">View Uploaded File</a>
						</logic:notEmpty>

					</div>
				</div>

				<div class="parawiseRemarksdiv">
					<div class="row">
						<div class="col-sm-6 form-group">
							<label>Parawise Remarks Submitted</label>
							<html:select styleId="parawiseRemarksSubmitted"
								property="dynaForm(parawiseRemarksSubmitted)"
								styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="No">No</html:option>
								<html:option value="Yes">Yes</html:option>
							</html:select>
						</div>

						<div class="col-sm-6 form-group parawiseRemarkssubmitteddiv">
							<label for="sel1"> Date of Submission of Parawise Remarks
								to GP/SC <font color="red">*</font>
							</label>
							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="parawiseRemarksDt"
									property="dynaForm(parawiseRemarksDt)"
									styleClass="form-control datepicker" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6 form-group parawiseRemarkssubmitteddiv">
							<label>Upload Parawise Remarks</label>
							<html:file styleClass="form-control"
								styleId="parawiseRemarksCopy"
								property="dynaForm(parawiseRemarksCopy)" />


							<span class="help-block"><bean:message
									key="upload.document.validation.msg" /></span>

							<logic:notEmpty name="CommonForm"
								property="dynaForm(parawiseRemarksCopyOld)">
								<a
									href='<bean:write name="CommonForm" property="dynaForm(parawiseRemarksCopyOld)"/>'
									target='_new' class="btn btn-sm btn-info">View Uploaded
									File</a>
							</logic:notEmpty>
						</div>

						<div class="col-sm-6 form-group parawiseRemarkssubmitteddiv">

							<label for="sel1"> Parawise Remarks Approved by GP<font
								color="red">*</font></label>
							<html:select property="dynaForm(pwr_gp_approved)"
								styleId="pwr_gp_approved" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="No">No</html:option>
								<html:option value="Yes">Yes</html:option>
							</html:select>
						</div>
					</div>

					<div class="row parawiseRemarksapproveddiv">
						<div class="col-xs-12 col-sm-6 form-group ">
							<label class="font-bold">Date of Approval of Parawise
								Remarks by GP/SC <font color="red">*</font>
							</label>
							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="dtPRApprovedToGP"
									property="dynaForm(dtPRApprovedToGP)"
									styleClass="form-control datepicker" />
							</div>
						</div>

						<div class="col-xs-12 col-sm-6 form-group">
							<label class="font-bold">Date of Receipt of Approved
								Parawise Remarks from GP/SC <font color="red">*</font>
							</label>
							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="dtPRReceiptToGP"
									property="dynaForm(dtPRReceiptToGP)"
									styleClass="form-control datepicker" />
							</div>
						</div>

					</div>
				</div>
				<div class="appealfileddiv">
					<div class="row">
						<div class="col-sm-6 form-group ">
							<label>Appeal Filed</label>
							<html:select styleId="appealFiled"
								property="dynaForm(appealFiled)" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="No">No</html:option>
								<html:option value="Yes">Yes</html:option>
							</html:select>
						</div>
						<div class="col-sm-6 form-group appealuploaddiv">
							<label>Upload Appeal Copy</label>

							<html:file styleClass="form-control" styleId="appealFileCopy"
								property="dynaForm(appealFileCopy)" />
							<span class="help-block"><bean:message
									key="upload.document.validation.msg" /></span>

							<logic:notEmpty name="CommonForm"
								property="dynaForm(appealFileCopyOld)">
								<a
									href='<bean:write name="CommonForm" property="dynaForm(appealFileCopyOld)"/>'
									target='_new' class="btn btn-sm btn-info">View Uploaded
									File</a>
							</logic:notEmpty>

						</div>
						<div class="col-sm-6 form-group appealuploaddiv">
							<label>Appeal Date</label>

							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="appealFiledDt"
									property="dynaForm(appealFiledDt)"
									styleClass="form-control datepicker" />
							</div>
						</div>
					</div>
				</div>
				<div class="counterfileddiv">
					<div class="row">
						<div class="col-sm-6 form-group ">
							<label>Counter Filed</label>
							<html:select styleId="counterFiled"
								property="dynaForm(counterFiled)" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="No">No</html:option>
								<html:option value="Yes">Yes</html:option>
							</html:select>
						</div>
						<div class="col-sm-6 form-group counteruploaddiv">
							<label>Counter File Upload</label>

							<html:file styleClass="form-control" styleId="counterFileCopy"
								property="dynaForm(counterFileCopy)" />
							<span class="help-block"><bean:message
									key="upload.document.validation.msg" /></span>

							<logic:notEmpty name="CommonForm"
								property="dynaForm(counterFileCopyOld)">
								<a
									href='<bean:write name="CommonForm" property="dynaForm(counterFileCopyOld)"/>'
									target='_new' class="btn btn-sm btn-info">View Uploaded
									File</a>
							</logic:notEmpty>

						</div>
					</div>
				</div>

				
				<div class="row">
					<div class="col-sm-6 form-group">
						<label>Action <bean:message key="mandatory" />
						</label>
						<html:select styleId="actionToPerform"
							property="dynaForm(actionToPerform)" styleClass="form-control">
							<html:option value="0">---SELECT---</html:option>
							<html:option value="Parawise Remarks">Parawise Remarks</html:option>
							<html:option value="Counter Affidavit">Counter Affidavit</html:option>
							
						</html:select>
					</div>
					<div class="col-sm-6 form-group"></div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<div class="form-group">
							<label for="sel1" id="remaeksTextId">Remarks: </label>
							<script type="text/javascript" src="js/nicEdit-latest.js"></script>
							<script type="text/javascript">
								bkLib.onDomLoaded(function() {
									new nicEditor({
										fullPanel : true
									}).panelInstance('remarks');
								});
							</script>
							<html:textarea cols="600" styleId="remarks"
								property="dynaForm(remarks)" style="width: 500%; height: 250px;">
							</html:textarea>
						</div>
					</div>
				</div>


				<hr />
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<logic:present name="SHOWBACKBTN">
							<button class="btn btn-md btn-primary" type="submit"
								name="update" onclick="return sendbackFn();">Send Back</button>
						</logic:present>
						
						<logic:empty name="STATUSUPDATEBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="update" onclick="return updateCaseDetails();">Update
								Case Details</button>
						</logic:empty>

						<logic:notEmpty name="STATUSUPDATEBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="update" onclick="return updateCaseDetails2();">Update
								Case Details</button>
						</logic:notEmpty>

						<logic:notEmpty name="SHOWNOBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="forward" onclick="return forwardCase();">Forward
								to Nodal Officer</button>
						</logic:notEmpty>

						<logic:notEmpty name="SHOWMLOBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="forward" onclick="return forwardCase();">Forward
								to MLO</button>
						</logic:notEmpty>

						<logic:notEmpty name="SHOWHODDEPTBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="forward" onclick="return forwardCase();">Forward
								to HOD</button>
						</logic:notEmpty>


						<logic:notEmpty name="SHOWSECDEPTBTN">
							<button class="btn btn-md btn-success" type="submit"
								name="forward" onclick="return forwardCase();">Forward
								to Sect. Dept.</button>
						</logic:notEmpty>

						<logic:notEmpty name="SHOWGPBTN">

							<div class="form-group">
								<label>Select GP</label>
								<html:select styleId="gpCode" property="dynaForm(gpCode)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT GP---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(GPSLIST)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(GPSLIST)" />
									</logic:notEmpty>
								</html:select>
							</div>

							<button class="btn btn-md btn-success" type="button"
								name="forward" onclick="return forwardCase2GP();">Forward
								to GP for Approval.</button>
						</logic:notEmpty>


						<logic:notEmpty name="SHOWGPAPPROVEBTN">

							<button class="btn btn-md btn-success" type="button"
								name="forward" onclick="return gpApprove();">Approve</button>

							<button class="btn btn-md btn-danger" type="button"
								name="forward" onclick="return gpReject();">Return</button>

						</logic:notEmpty>


					</div>
				</div>
			</div>
		</div>
	</html:form>
</div>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<script>
	$('.datepicker').datepicker({
		uiLibrary : 'bootstrap4'
	});
</script>
<script>
	function sendbackFn() {
		$("#mode").val("sendBackCaseDetails");
		$("#AssignedCasesToSectionForm").submit();
	}

	function updateCaseDetails2() {
		if ($("#ecourtsCaseStatus").val() == ""
				|| $("#ecourtsCaseStatus").val() == null
				|| $("#ecourtsCaseStatus").val() == "0") {

			alert("Case Status Required.");
			$("#ecourtsCaseStatus").focus();
			return false;
		} else {
			$("#mode").val("updateCaseDetails");
			$("#AssignedCasesToSectionForm").submit();
		}
	}

	function updateCaseDetails() {
		
		
		
		/* if ($("#ecourtsCaseStatus").val() == ""
				|| $("#ecourtsCaseStatus").val() == null
				|| $("#ecourtsCaseStatus").val() == "0") {

			alert("Case Status Required.");
			$("#ecourtsCaseStatus").focus();
			return false;
		}else {
			$("#mode").val("updateCaseDetails");
			$("#AssignedCasesToSectionForm").submit();
			return true;
		} */
		
		
		 if ($("#ecourtsCaseStatus").val() == ""
			|| $("#ecourtsCaseStatus").val() == null
			|| $("#ecourtsCaseStatus").val() == "0") {

		alert("Case Status Required.");
		$("#ecourtsCaseStatus").focus();
		return false;
	} else if ($("#ecourtsCaseStatus").val() == "Closed"
			&& ($("#judgementOrder").val() == null || $("#judgementOrder")
					.val() == "")) {
		alert("Judgement Order Required.");
		$("#judgementOrder").focus();
		return false;
	} else if ($("#ecourtsCaseStatus").val() == "Closed"
			&& ($("#actionTakenOrder").val() == null || $(
					"#actionTakenOrder").val() == "")) {
		alert("Action Taken Order Required.");
		$("#actionTakenOrder").focus();
		return false;
	} else {
		$("#mode").val("updateCaseDetails");
		//$("#AssignedCasesToSectionForm").submit();
		return true;
	}
		
		
		
		
		
	}

	function forwardCase() {
		$("#mode").val("forwardCaseDetails");
		$("#AssignedCasesToSectionForm").submit();
		return true;
	}
	function forwardCase2GP() {
		if ($("#gpCode").val() != "" && $("#gpCode").val() != "0") {
			$("#mode").val("forwardCaseDetails2GP");
			$("#AssignedCasesToSectionForm").submit();
			return true;
		} else {
			$("#gpCode").focus();
			alert("Select GP to submit.");
			return fasle;
		}
	}

	function gpApprove() {
		$("#mode").val("gpApprove");
		$("#AssignedCasesToSectionForm").submit();
		return true;
	}
	function gpReject() {
		$("#mode").val("gpReject");
		$("#AssignedCasesToSectionForm").submit();
		return true;
	}

	$(document)
			.ready(
					function() {

						$(".casecloseddiv").hide();
						$(".casependingdiv").hide();

						$(".counterfileddiv").hide();
						$(".counteruploaddiv").hide();

						$(".parawiseRemarksdiv").hide();
						$(".parawiseRemarkssubmitteddiv").hide();
						$(".parawiseRemarksapproveddiv").hide();

						$(".appealfileddiv").hide();
						$(".appealuploaddiv").hide();

						$("#ecourtsCaseStatus")
								.change(
										function() {
											$(".casecloseddiv").hide();
											$(".casependingdiv").hide();
											$(".parawiseRemarksdiv").hide();
											$(".counterfileddiv").hide();
											$(".counteruploaddiv").hide();

											$(".appealfileddiv").hide();
											$(".appealuploaddiv").hide();
											// alert("ecourtsCaseStatus::"+$("#ecourtsCaseStatus").val());
											if ($("#ecourtsCaseStatus").val() == "Closed") {
												$(".casecloseddiv").show();
												$(".appealfileddiv").show();

											} else if ($("#ecourtsCaseStatus")
													.val() == "Pending") {
												$(".casependingdiv").show();
												$(".parawiseRemarksdiv").show();
												$(".counterfileddiv").show();
												//$(".parawiseRemarkssubmitteddiv").hide();
											}
										});

						$("#appealFiled").change(function() {
							if ($("#appealFiled").val() == "Yes") {
								$(".appealuploaddiv").show();
							} else {
								$(".appealuploaddiv").hide();
							}
						});

						$("#parawiseRemarksSubmitted").change(function() {
							
						//	alert("--"+$("#parawiseRemarksSubmitted").val());
							if ($("#parawiseRemarksSubmitted").val() == "Yes"  ) {
								$(".parawiseRemarkssubmitteddiv").show();
							} else  {  //if($("#parawiseRemarksSubmitted").val() == "No" )
								$(".parawiseRemarksSubmitteddiv").hide();
							}
						});

						$("#pwr_gp_approved").change(function() {
							if ($("#pwr_gp_approved").val() == "Yes") {
								$(".parawiseRemarksapproveddiv").show();
							} else {
								$(".parawiseRemarksapproveddiv").hide();
							}
						});

						$("#counterFiled")
								.change(
										function() {

											$(".counteruploaddiv").hide();
											//$(".parawiseRemarksdiv").hide();
											if ($("#counterFiled").val() == "Yes") {
												$(".counteruploaddiv").show();
											} else if ($("#counterFiled").val() == "No"
													&& $("#ecourtsCaseStatus")       
															.val() == "Pending") {
												//$(".parawiseRemarksdiv").show();
											}
										});
						
						
						

						if ($("#ecourtsCaseStatus").val() == "Closed") {
							$(".casecloseddiv").show();
							$(".appealfileddiv").show();
						//	$(".parawiseRemarkssubmitteddiv").hide();

						} else if ($("#ecourtsCaseStatus").val() == "Pending") {
							$(".casependingdiv").show();
							$(".parawiseRemarksdiv").show();
							$(".counterfileddiv").show();
							//$(".parawiseRemarkssubmitteddiv").hide();
						}

						if ($("#parawiseRemarksSubmitted").val() == "Yes") {
							$(".parawiseRemarkssubmitteddiv").show();
						} else {
							$(".parawiseRemarksSubmitteddiv").hide();
						}

						if ($("#counterFiled").val() == "Yes") {
							$(".counteruploaddiv").show();
						}

						if ($("#appealFiled").val() == "Yes") {
							$(".appealuploaddiv").show();
						} else {
							$(".appealuploaddiv").hide();
						}

						// alert($("#pwr_gp_approved").val());

						if ($("#pwr_gp_approved").val() == "Yes") {
							$(".parawiseRemarksapproveddiv").show();
						} else {
							$(".parawiseRemarksapproveddiv").hide();
						}

						/* this function will call when onchange event fired */
						$("#parawiseRemarksCopy")
								.on(
										"change",
										function() {
											/* current this object refer to input element */
											var $input = $(this);
											/* collect list of files choosen */
											var files = $input[0].files;
											var filename = files[0].name;
											/* getting file extenstion eg- .jpg,.png, etc */
											var extension = filename
													.substr(filename
															.lastIndexOf("."));
											/* define allowed file types */
											var allowedExtensionsRegx = /(\.doc|\.DOC|\.pdf|\.PDF)$/i;
											/* testing extension with regular expression */
											var isAllowed = allowedExtensionsRegx
													.test(extension);
											var fileSize = files[0].size;
											/* 1024 = 1MB */
											var size = Math
													.round((fileSize / 1024));
											/* checking for less than or equals to 2MB file size */

											if (isAllowed == false) {
												alert("Invalid File type for the upload.");
												$(this).focus();
												$(this).val("");
												return false;
											} else if (size > 100 * 1024) {
												alert("Invalid file size");
												$(this).focus();
												$(this).val("");
												return false;
											}
										});
						
						
						$("#counterFileCopy")
						.on(
								"change",
								function() {
									/* current this object refer to input element */
									var $input = $(this);
									/* collect list of files choosen */
									var files = $input[0].files;
									var filename = files[0].name;
									/* getting file extenstion eg- .jpg,.png, etc */
									var extension = filename
											.substr(filename
													.lastIndexOf("."));
									/* define allowed file types */
									var allowedExtensionsRegx = /(\.doc|\.DOC|\.pdf|\.PDF)$/i;
									/* testing extension with regular expression */
									var isAllowed = allowedExtensionsRegx
											.test(extension);
									var fileSize = files[0].size;
									/* 1024 = 1MB */
									var size = Math
											.round((fileSize / 1024));
									/* checking for less than or equals to 2MB file size */

									if (isAllowed == false) {
										alert("Invalid File type for the upload.");
										$(this).focus();
										$(this).val("");
										return false;
									} else if (size > 100 * 1024) {
										alert("Invalid file size");
										$(this).focus();
										$(this).val("");
										return false;
									}
								});

					});
</script>