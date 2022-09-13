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
<!-- START PAGE CONTENT-->

<div class="page-content fade-in-up">
	<html:form action="/HCFinalOrdersImplementedForm"
		styleId="AssignedCasesToSectionForm" enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(fileCino)" styleId="fileCino" />
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
								<b> Date of filing: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_of_filing}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Case Type : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.type_name_reg}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Filing No.: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.fil_no}</div>
						</div>
						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Filing Year: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.fil_year}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Registration No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.reg_no}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Est Code: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.est_code}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Case ID: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.case_type_id}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Cause Type: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.causelist_type}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Bench Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.bench_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Judicial Branch: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.judicial_branch}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Coram: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.coram}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Court Est Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.court_est_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> State Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.state_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>District : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.dist_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of First List : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_first_list}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of Next List </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_next_list}</div>


							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of Decision : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_of_decision}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Purpose : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.purpose_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petitioner Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_name}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petitioner Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_adv}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petitioner Legal Heir : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_legal_heir}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Name : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_name}, ${map.address}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_adv}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_adv}</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<b>Prayer:</b> ${map.prayer}
							</div>
						</div>

					</logic:iterate>

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
							<label>Select Action <bean:message key="mandatory" />
							</label>
							<html:select styleId="ecourtsCaseStatus"
								property="dynaForm(ecourtsCaseStatus)" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="final">Final order implemented</html:option>
								<html:option value="appeal">Appeal filed </html:option>
								<html:option value="dismissed">Dismissed/No Action Required </html:option>
							</html:select>
						</div>
					</div>
				</div>


				<div class="row finaldiv">
					<%-- <div class="col-sm-6 form-group casecloseddiv">
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
						<logic:notEmpty name="final_order">
									<a href="https://apolcms.ap.gov.in/${final_order}" target="_blank" class="btn btn-sm btn-info">Final Order</a>
								</logic:notEmpty>
					</div> --%>
					
					<div class="col-sm-6 form-group casecloseddiv">
						<label>Signed Copy Of Final Judgement <bean:message
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
						<logic:notEmpty name="final_order">
									<a href="https://apolcms.ap.gov.in/${final_order}" target="_blank" class="btn btn-sm btn-info">Final Order</a>
								</logic:notEmpty>
					</div>
					
					
					<div class="col-sm-6 form-group finaldiv">
						<label>Action Taken Report <bean:message key="mandatory" /></label>

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
					<div class="col-sm-6 form-group finaldiv">
							<label>Order Implemented Date</label>

							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="implementedDt"
									property="dynaForm(implementedDt)"
									styleClass="form-control datepicker" />
							</div>
						</div>
					
					
					</div>
					
					<div class="row appealdiv">
					
				<%-- 	
					<div class="col-sm-6 form-group appealuploaddiv">
							<label>Appeal Filed</label>
							<html:select styleId="appealFiled"
								property="dynaForm(appealFiled)" styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="No">No</html:option>
								<html:option value="Yes">Yes</html:option>
							</html:select>
						</div> --%>
					
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
						<div class="col-sm-6 form-group appealdiv">
							<label>Appeal Filed Date</label>

							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="appealFiledDt"
									property="dynaForm(appealFiledDt)"
									styleClass="form-control datepicker" />
							</div>
						</div>

					</div>
					
					
					<div class="row dismisseddiv">
							<div class="col-sm-6 form-group dismisseddiv">
							<label>Upload Copy</label>

							<html:file styleClass="form-control" styleId="dismissedFileCopy"
								property="dynaForm(dismissedFileCopy)" />
							<span class="help-block"><bean:message
									key="upload.document.validation.msg" /></span>

							<logic:notEmpty name="CommonForm"
								property="dynaForm(dismissedFileCopyOld)">
								<a
									href='<bean:write name="CommonForm" property="dynaForm(dismissedFileCopyOld)"/>'
									target='_new' class="btn btn-sm btn-info">View Uploaded
									File</a>
							</logic:notEmpty>

						</div>
						

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
								property="dynaForm(remarks)" style="width: 100%; height: 100px;">
							</html:textarea>
						</div>
					</div>
				</div>


				<hr />
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						
						<%-- <logic:empty name="STATUSUPDATEBTN"> --%>
							<button class="btn btn-md btn-success" type="submit"
								name="update" onclick="return updateCaseDetails();">Update
								Case Details</button>
						<%-- </logic:empty> --%>


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

		 if ($("#ecourtsCaseStatus").val() == ""
				|| $("#ecourtsCaseStatus").val() == null
				|| $("#ecourtsCaseStatus").val() == "0") {

			alert("Select Action.");
			$("#ecourtsCaseStatus").focus();
			return false;
		} else if ($("#ecourtsCaseStatus").val() == "final"
				&& ($("#judgementOrder").val() == null || $("#judgementOrder")
						.val() == "")) {
			alert("Judgement Order Required.");
			$("#judgementOrder").focus();
			return false;
		}else if ($("#ecourtsCaseStatus").val() == "final"
			&& ($("#actionTakenOrder").val() == null || $("#actionTakenOrder")
					.val() == "")) {
		alert("Action Taken Order Required.");
		$("#actionTakenOrder").focus();
		return false;
	}else if ($("#ecourtsCaseStatus").val() == "final"
		&& ($("#implementedDt").val() == null || $("#implementedDt")
				.val() == "")) {
	alert("implemented Date Required.");
	$("#implementedDt").focus();
	return false;
} else if ($("#ecourtsCaseStatus").val() == "appeal"
				&& ($("#appealFileCopy").val() == null || $("#appealFileCopy").val() == "")) {
			alert("Appeal File Copy Required.");
			$("#appealFileCopy").focus();
			return false;
		}else if ($("#ecourtsCaseStatus").val() == "appeal"
				&& ($("#appealFiledDt").val() == null || $("#appealFiledDt").val() == "")) {
			alert("Appeal Time Required.");
			$("#appealFiledDt").focus();
			return false;
		}else if ($("#ecourtsCaseStatus").val() == "dismissed"
				&& ($("#dismissedFileCopy").val() == null || $("#dismissedFileCopy").val() == "")) {
			alert("Dismissed File Copy Required.");
			$("#dismissedFileCopy").focus();
			return false;
		}  else {
			$("#mode").val("updateCaseDetails");
			//$("#AssignedCasesToSectionForm").submit();
			return true;
		}dismissedFileCopydismissedFileCopy
	}

	function forwardCase() {
		$("#mode").val("forwardCaseDetails");
		//$("#AssignedCasesToSectionForm").submit();
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

						$(".dismisseddiv").hide();
						$(".finaldiv").hide();

						$(".appealdiv").hide();
						
						$("#ecourtsCaseStatus")
								.change(
										function() {
											$(".dismisseddiv").hide();
											$(".finaldiv").hide();
											$(".appealdiv").hide();
											if ($("#ecourtsCaseStatus").val() == "final") {
												$(".finaldiv").show();
												$(".dismisseddiv").hide();
												$(".appealdiv").hide();
											} else if ($("#ecourtsCaseStatus").val() == "appeal") {
												$(".appealdiv").show();
												$(".dismisseddiv").hide();
												$(".finaldiv").hide();
											}else if ($("#ecourtsCaseStatus").val() == "dismissed") {
												$(".dismisseddiv").show();
												$(".finaldiv").hide();
											}
										});

						
						
						
							$(".dismisseddiv").hide();
							$(".finaldiv").hide();
							$(".appealdiv").hide();
							if ($("#ecourtsCaseStatus").val() == "final") {
								$(".finaldiv").show();
								$(".dismisseddiv").hide();
								$(".appealdiv").hide();
							} else if ($("#ecourtsCaseStatus").val() == "appeal") {
								$(".appealdiv").show();
								$(".dismisseddiv").hide();
								$(".finaldiv").hide();
							}else if ($("#ecourtsCaseStatus").val() == "dismissed") {
								$(".dismisseddiv").show();
								$(".finaldiv").hide();
							}

						/* this function will call when onchange event fired */
						$("#judgementOrder")
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
											var allowedExtensionsRegx = /(\.pdf|\.PDF)$/i;
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
						
						
						
						
						
						
						$("#appealFileCopy")
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
									var allowedExtensionsRegx = /(\.pdf|\.PDF)$/i;
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
						
						
						
						
						$("#dismissedFileCopy")
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
									var allowedExtensionsRegx = /(\.pdf|\.PDF)$/i;
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