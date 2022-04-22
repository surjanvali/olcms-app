<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
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
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/AssignedCasesToSection"
		styleId="AssignedCasesToSectionForm">
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
					<h5 style="color:Black;">
						<logic:notEmpty name="HEADING">
									${HEADING}
								</logic:notEmpty>
					</h5>
				</div>
			</div>
			<div class="ibox-body">
				<logic:present name="CASEDATA">
					<logic:iterate id="map" name="CASEDATA">

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Date of filing: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_of_filing}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Case Type : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.type_name_reg}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Filing No.: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.fil_no}</div>
						</div>
						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Filing Year: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.fil_year}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Registration No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.reg_no}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Est Code: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.est_code}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Case ID: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.case_type_id}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Cause Type: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.causelist_type}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> Bench Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.bench_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Judicial Branch: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.judicial_branch}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Coram: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.coram}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Court Est Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.court_est_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b> State Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.state_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>District : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.dist_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Date Of First List : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_first_list}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Date Of Next List </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_next_list}</div>


							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Date Of Decision : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.date_of_decision}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Purpose : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.purpose_name}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Petitioner Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_name}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Petitioner Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_adv}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Petitioner Legal Heir:: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_legal_heir}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Respondent Name : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Respondent Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_adv}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
								<b>Respondent Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_adv}</div>
						</div>
					</logic:iterate>
					<hr />
				</logic:present>
				<logic:present name="ACTIVITIESDATA">

					<table class="table table-striped table-bordered table-hover"
						cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Sl no.</th>
								<th>Activity</th>
								<th>Submitted by</th>
								<th>Submitted On</th>
								<th>Counter / Remarks</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="inner" name="ACTIVITIESDATA" indexId="i">
								<tr>
									<td>${i+1 }</td>

									<td>${inner.action_type }</td>
									<td>${inner.inserted_by }</td>
									<td>${inner.inserted_on }</td>
									<td>
										<div
											style="min-width:250px;height: 150px; overflow-y: scroll;">
											${inner.remarks }</div>
									</td>
								</tr>
							</logic:iterate>

						</tbody>
					</table>

				</logic:present>
				<%-- <div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<label class="control-label">Upload File </label>
						<html:file property="dynaForm(uploadCounterFile)"
							styleId="uploadCounterFile" styleClass="form-control"></html:file>
						</div>
					</div>
				<hr /> --%>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<html:textarea property="dynaForm(counterRemarks)"
							styleId="counterRemarks" style="height: 150px;" cols="100"></html:textarea>
					</div>
				</div>
				<hr />
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<button class="btn btn-md btn-success pull-right"
							onclick="submitCounter();">Save Counter</button>

						<!-- <button class="btn btn-md btn-success pull-right"
							onclick="submitCounter();">Send</button> -->

					</div>
				</div>
			</div>
		</div>
	</html:form>
</div>
<script>
	// CKEDITOR.replace('counterRemarks');

	bkLib.onDomLoaded(function() {
		//new nicEditor().panelInstance('area1');
		var myNicEditor = new nicEditor({
			fullPanel : true
		}).panelInstance('counterRemarks');
		//new nicEditor({iconsPath : '../nicEditorIcons.gif'}).panelInstance('area3');
		//new nicEditor({buttonList : ['fontSize','bold','italic','underline','strikeThrough','subscript','superscript','html','image']}).panelInstance('area4');
		//new nicEditor({maxHeight : 100}).panelInstance('area5');
		myNicEditor.css("width : 250px;");
	});

	function submitCounter() {
		$("#mode").val("saveCounterFileData");
		$("#AssignedCasesToSectionForm").submit();
	}
</script>