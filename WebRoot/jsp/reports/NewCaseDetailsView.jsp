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

<style>
body {
	overflow-y: auto;
}
</style>

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/HCNewCaseStatusAbstractReport">
		<html:hidden styleId="mode" property="mode" />
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

			<div class="ibox-body">
				<logic:present name="USERSLIST">
					<logic:iterate id="map" name="USERSLIST">
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petitioner Name : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.petitioner_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Advocate No : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.advocateccno}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Advocate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.advocatename}</div>
						</div>

						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Reg Year: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.reg_year}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Registration No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.reg_no}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Date Of Decision : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.inserted_time}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Case type: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.case_short_name}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Main Case No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.maincaseno}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Services : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.services_flag}</div>
						</div>

						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Filing Mode: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.mode_filing}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> District Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.district_name}</div>
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
											<td><logic:notEmpty name="activities"
													property="uploaded_doc_path">
													<logic:notEqual value="-" name="activities"
														property="uploaded_doc_path">
														<a href='${activities.uploaded_doc_path }' target='_new'
															class="btn btn-sm btn-info">View Uploaded File</a>
													</logic:notEqual>
												</logic:notEmpty></td>
										</tr>
									</logic:iterate>
								</tbody>

							</table>
						</div>
					</div>
				</logic:present>
				<div class="text-right">
					<button class="btn btn-info" type="button"
						onclick="javascript:window.print();">
						<i class="fa fa-print"></i> Print
					</button>
				</div>
			</div>
		</div>
	</html:form>
</div>
