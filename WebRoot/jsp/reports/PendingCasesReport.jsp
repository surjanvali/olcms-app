<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<!-- <link href="./assetsnew/vendors/DataTables/datatables.min.css" rel="stylesheet" /> -->
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<div class="page-content fade-in-up">
	<html:form action="/pendingCaseReport"
		styleId="pendingCaseReport">
		<html:hidden styleId="mode" property="mode" />
		
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(caseStatus)" styleId="caseStatus" />
		<html:hidden property="dynaForm(reportLevel)" styleId="reportLevel" />
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
		
		<logic:notPresent name="show_flag">
			<html:hidden styleId="deptId" property="dynaForm(deptId)" />
		</logic:notPresent>
        <logic:equal name="show_flag" value="Y">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Total Pending cases Against The Government Of Andhra Pradesh Report</div>
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
								<label class="font-bold">Date of Registration (From
									Date)</label>
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
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Petitioner Name</label>
								<html:text styleId="petitionerName"
									property="dynaForm(petitionerName)" styleClass="form-control" />

							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Respondent Name</label>
								<html:text styleId="respodentName"
									property="dynaForm(respodentName)" styleClass="form-control" />

							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Service Category</label>
								<html:select styleId="categoryServiceId"
									property="dynaForm(categoryServiceId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(categoryServiceList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(categoryServiceList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Respondent Advocate</label>
								<html:select styleId="res_adv_Id"
									property="dynaForm(res_adv_Id)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(ResAdvList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(ResAdvList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="button" name="getreport" value="Get Report"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>
				</div>
			</div>
		</logic:equal>
		

		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty> </b>
					</h4>
				</div>
			</div>
			<div class="ibox-body">
				<div class="table-responsive">
					<logic:present name="data">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>CINO</th>
									<th>Case Type</th>
									<th>Main Case No</th>
									<th>Year</th>
									<th>Department</th>
									<th>District</th>
									<th>Service/Non-Service</th>
									<th>Petitioner</th>
									<th>Ptr.Advocate</th>
									<th>Respondent</th>
									<th>Respondent Advocate</th>
									<th>Prayer</th>
									<th>IA Prayer</th>
									<th>Counter Affidavit Filed or not</th>
									<th>Vacate Stay Filed or not</th>
									<th>Pending/Disposed</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>

								<logic:iterate id="map" name="data" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.cino}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />
										</td>
										<td>${map.case_type }</td>
										<td>${map.main_case_no }</td>
										<td>${map.year }</td>
										<td>${map.dept_name }</td>
										<td>${map.dist_name }</td>
										<td>${map.category_service }</td>
										<td>${map.petitioner }</td>
										<td>${map.petioner_advocate }</td>
										<td>${map.respondent }</td>
										<td>${map.respondent_advocate }</td>
										<td style="min-width: 350px;text-align: justify;">
										<logic:notEmpty
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
										<td>-</td>
										<td>-</td>
										<td>-</td>
										<td>${map.pending_disposed }</td>
									</tr>
									
								</logic:iterate>
							</tbody>
							<tfoot>
								
							</tfoot>
						</table>

					</logic:present>
				</div>
			</div>
		</div>
	</html:form>
</div>
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

<script type="text/javascript">

$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
	});

	function fnShowCases() {
		$("#mode").val("getdata");
		$("#pendingCaseReport").submit();
	}

	function ShowHODWise(deptId, deptDesc) {
		document.forms["pendingCaseReport"].elements["deptId"].value = deptId;
		document.forms["pendingCaseReport"].elements["deptName"].value = deptDesc;
		document.forms["pendingCaseReport"].elements["mode"].value = "HODwisedetails";
		/*
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");*/
		$("#pendingCaseReport").submit();
	}
	function showCasesWise(deptId, deptDesc, status, level) {

		document.forms["pendingCaseReport"].elements["deptId"].value = deptId;
		document.forms["pendingCaseReport"].elements["deptName"].value = deptDesc;
		document.forms["pendingCaseReport"].elements["mode"].value = "getCasesList";
		document.forms["pendingCaseReport"].elements["caseStatus"].value = status;
		document.forms["pendingCaseReport"].elements["reportLevel"].value = level;

		/*$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#reportLevel").val(level);
		$("#mode").val("getCasesList");*/
		$("#pendingCaseReport").submit();
	}

	function viewCaseDetailsPopup(cino) {
		var heading = "View Case Details for CINO : " + cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="
					+ cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};
	
</script>
