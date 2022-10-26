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

.nav-tabs .nav-item {
	margin-bottom: -1px;
	background: #5a79c1;
	color: #fff;
	border-left: 1px solid #fff;
}

.nav-tabs active {
	background: #0b379b;
}
</style>
<!-- START PAGE CONTENT-->
<div class="page-content fade-in-up">
	<html:form action="/GPReport" styleId="AssignedCasesToSectionForm"
		enctype="multipart/form-data">
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
				<ul class="nav nav-tabs primary" role="tablist">
					<li class="nav-item active"><a class="nav-link" id="home-tab"
						data-toggle="tab" href="#home" role="tab" aria-controls="home"
						aria-selected="false"> <span class="d-block d-sm-none"><i
								class="fas fa-home"></i></span> <span class="d-none d-sm-block">Case
								Details</span>
					</a></li>

					<li class="nav-item"><a class="nav-link" id="instructions-tab"
						data-toggle="tab" href="#instructions" role="tab"
						aria-controls="instructions" aria-selected="true"> <span
							class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
							class="d-none d-sm-block">Instructions </span>
					</a></li>

					<!-- <li class="nav-item"><a class="nav-link"
						id="dialycasestatus-tab" data-toggle="tab" href="#dialycasestatus"
						role="tab" aria-controls="dialycasestatus" aria-selected="true">
							<span class="d-block d-sm-none"><i class="fas fa-user"></i></span>
							<span class="d-none d-sm-block">Dialy Case Status</span>
					</a></li> -->
					<!-- <li class="nav-item"><a class="nav-link" id="petitoner-tab"
						data-toggle="tab" href="#petitoner" role="tab"
						aria-controls="petitoner" aria-selected="true"> <span
							class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
							class="d-none d-sm-block">Petitioners</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="profile-tab"
						data-toggle="tab" href="#profile" role="tab"
						aria-controls="profile" aria-selected="true"> <span
							class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
							class="d-none d-sm-block">Respondents</span>
					</a></li> -->
					<li class="nav-item"><a class="nav-link" id="message-tab"
						data-toggle="tab" href="#message" role="tab"
						aria-controls="message" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="far fa-envelope"></i></span>
							<span class="d-none d-sm-block">IA Filings</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="parawise-tab"
						data-toggle="tab" href="#parawise" role="tab"
						aria-controls="parawise" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Parawise Remarks</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="counter-tab"
						data-toggle="tab" href="#counter" role="tab"
						aria-controls="counter" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Counter Details</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="setting-tab"
						data-toggle="tab" href="#setting" role="tab"
						aria-controls="setting" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Interim Orders</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="linkcases-tab"
						data-toggle="tab" href="#linkcases" role="tab"
						aria-controls="linkcases" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Tagged along Cases</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="objections-tab"
						data-toggle="tab" href="#objections" role="tab"
						aria-controls="objections" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Objections</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="history-tab"
						data-toggle="tab" href="#history" role="tab"
						aria-controls="history" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Case History</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="activities-tab"
						data-toggle="tab" href="#activities" role="tab"
						aria-controls="activities" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">OLCMS - Case Activities</span>
					</a></li>

					<li class="nav-item"><a class="nav-link" id="final-tab"
						data-toggle="tab" href="#final" role="tab" aria-controls="final"
						aria-selected="false"> <span class="d-block d-sm-none"><i
								class="fas fa-cog"></i></span> <span class="d-none d-sm-block">Final
								Orders</span>
					</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane" id="instructions" role="tabpanel"
						aria-labelledby="instructions-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="DEPTNSTRUCTIONS">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered"
												style="width:100%">
												<thead>
													<tr>
														<th colspan="6">Instructions Submitted by the
															Department</th>
													</tr>
													<tr>
														<th>Sl.No</th>
														<th>Description</th>
														<th>Submitted By</th>
														<th>Submitted On</th>
														<th>Uploaded File</th>
														<th>Reply to Instructions</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="map" name="DEPTNSTRUCTIONS" indexId="i">
														<tr>
															<td>${i+1 }.</td>
															<td>${map.instructions }</td>
															<td>${map.insert_by}</td>
															<td>${map.insert_time}</td>
															<td><logic:notEqual value="-" name="map"
											property="upload_fileno">
											<a href='${map.upload_fileno}' target='_new'
												class="btn btn-sm btn-info">View Uploaded File</a>
										</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>
													<td>
												 <input type="button" id="btnShowPopup" value="Reply to Instructions"
												class="btn btn-sm btn-success waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}','${map.legacy_ack_flag}');" />
											</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>
					</div>
					<%-- <div class="tab-pane" id="dialycasestatus" role="tabpanel"
						aria-labelledby="dialycasestatus-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="GPDAILYSTATUS">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered"
												style="width:100%">
												<thead>
													<tr>
														<th colspan="6">Daily Case Status updated by GP</th>
													</tr>
													<tr>
														<th>Sl.No</th>
														<th>Description</th>
														<th>Submitted By</th>
														<th>Submitted On</th>
														<th>Uploaded file</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="map" name="GPDAILYSTATUS" indexId="i">
														<tr>
															<td>${i+1 }.</td>
															<td>${map.status_remarks }</td>
															<td>${map.insert_by}</td>
															<td>${map.insert_time}</td>
															<td><logic:notEqual value="-" name="map"
											property="upload_fileno">
											<a href='${map.upload_fileno}' target='_new'
												class="btn btn-sm btn-info">View Uploaded File</a>
										</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>
					</div> --%>

					<div class="tab-pane active" id="home" role="tabpanel"
						aria-labelledby="home-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="USERSLIST">
									<logic:iterate id="map" name="USERSLIST">

										<div class="row">
											<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3 pull-right">
												<b>Download Affidavit : </b>
											</div>
											<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3">
												<logic:notEmpty name="map" property="scanned_document_path">
													<logic:notEqual value=" " name="map"
														property="scanned_document_path">

														<a href="./${map.scanned_document_path}" target="_new"
															title="Affidavit" class="btn btn-sm btn-info"> <i
															class="fa fa-save"></i> <span>Affidavit</span>
														</a>
													</logic:notEqual>
												</logic:notEmpty>
											</div>
										</div>

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
												<b>Court Est. Name: </b>
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
												<b>Petitioner Legal Heir : </b>
											</div>
											<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
												${map.pet_legal_heir}</div>
										</div>
										<div class="row">
											<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-right">
												<b>Respondent Name : </b>
											</div>
											<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
												${map.res_name}, ${map.address}</div>
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
										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
												<b>Prayer:</b> ${map.prayer}
											</div>
										</div>

									</logic:iterate>

								</logic:present>
								<hr />
								<div class="row">
									<logic:present name="actlist">
										<div class="col-md-6">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="4">ACTS List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Act</th>
														<th>Act Name</th>
														<th>Section</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="ccd" name="actlist" indexId="i">
														<tr>

															<td>${i+1}</td>
															<td>${ccd.act}</td>
															<td>${ccd.actname}</td>
															<td>${ccd.section}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</logic:present>
									<logic:present name="PETEXTRAPARTYLIST">
										<div class="col-md-6">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="3">Petitioner's List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Party No</th>
														<th>Party Name</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="pet" name="PETEXTRAPARTYLIST"
														indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${pet.party_no}</td>
															<td>${pet.party_name}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</logic:present>
									<logic:present name="RESEXTRAPARTYLIST">
										<div class="col-md-6">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="3">Respondent List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Party No</th>
														<th>Party Name</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="res" name="RESEXTRAPARTYLIST"
														indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${res.party_no}</td>
															<td>${res.party_name}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</logic:present>
								</div>
							</div>
						</div>
					</div>

					<%-- <div class="tab-pane" id="petitoner" role="tabpanel"
						aria-labelledby="petitoner-tab">
						<logic:present name="PETEXTRAPARTYLIST">
							<div class="row">
								<div class="col-md-12">
									<table class="table table-striped table-bordered table-hover"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th colspan="3">Petitioner's List</th>
											</tr>
											<tr>
												<th>Sl No.</th>
												<th>Party No</th>
												<th>Party Name</th>
											</tr>
										</thead>
										<tbody>
											<logic:iterate id="pet" name="PETEXTRAPARTYLIST" indexId="i">
												<tr>
													<td>${i+1}</td>
													<td>${pet.party_no}</td>
													<td>${pet.party_name}</td>
												</tr>
											</logic:iterate>
										</tbody>
									</table>
								</div>
							</div>
						</logic:present>
					</div>
					<div class="tab-pane" id="profile" role="tabpanel"
						aria-labelledby="profile-tab">

						<logic:present name="RESEXTRAPARTYLIST">
							<div class="row">
								<div class="col-md-12">
									<table class="table table-striped table-bordered table-hover"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th colspan="3">Respondent List</th>
											</tr>
											<tr>
												<th>Sl No.</th>
												<th>Party No</th>
												<th>Party Name</th>
											</tr>
										</thead>
										<tbody>
											<logic:iterate id="res" name="RESEXTRAPARTYLIST" indexId="i">
												<tr>
													<td>${i+1}</td>
													<td>${res.party_no}</td>
													<td>${res.party_name}</td>
												</tr>
											</logic:iterate>
										</tbody>
									</table>
								</div>
							</div>
						</logic:present>

					</div> --%>
					<div class="tab-pane" id="message" role="tabpanel"
						aria-labelledby="message-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="IAFILINGLIST">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="6">IAFilling List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Sr No</th>
														<th>IA NO</th>
														<th>IA Petitione rName</th>
														<th>IA Petitioner Dispoasal</th>
														<th>IA Date of Filling</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="iafi" name="IAFILINGLIST" indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${iafi.sr_no}</td>
															<td>${iafi.ia_no}</td>
															<td>${iafi.ia_pet_name}</td>
															<td>${iafi.ia_pend_disp}</td>
															<td>${iafi.date_of_filing}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>

					</div>

					<div class="tab-pane" id="linkcases" role="tabpanel"
						aria-labelledby="linkcases-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="LINKCASESLIST">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="4">Case Link List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Sr No</th>
														<th>Filling NO</th>
														<th>Case Number</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="link" name="LINKCASESLIST" indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${link.sr_no}</td>
															<td>${link.filing_number}</td>
															<td>${link.case_number}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>
					</div>


					<div class="tab-pane" id="objections" role="tabpanel"
						aria-labelledby="objections-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="OBJECTIONSLIST">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="6">Objections List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Objection Number</th>
														<th>Objection Description</th>
														<th>Scrunity Date</th>
														<th>Compliance Date</th>
														<th>Receipt Date</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="obj" name="OBJECTIONSLIST" indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${obj.objection_no}</td>
															<td>${obj.objection_desc}</td>
															<td>${obj.scrutiny_date}</td>
															<td>${obj.objections_compliance_by_date}</td>
															<td>${obj.obj_reciept_date}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>
					</div>

					<div class="tab-pane" id="setting" role="tabpanel"
						aria-labelledby="setting-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="INTERIMORDERSLIST">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="6">InterimOrder List</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Sr No</th>
														<th>Order NO</th>
														<th>Order Date</th>
														<th>Order Details</th>
														<th>Order Document</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="inter" name="INTERIMORDERSLIST"
														indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${inter.sr_no}</td>
															<td>${inter.order_no}</td>
															<td>${inter.order_date}</td>
															<td>${inter.order_details}</td>
															<td><logic:notEmpty name="inter"
																	property="order_document_path">

																	<logic:notEqual value="-" name="inter"
																		property="order_document_path">

																		<a href="./${inter.order_document_path}"
																			class="btn btn-sm btn-info" target="_new">
																			${inter.order_details}-${inter.order_no}</a>
																	</logic:notEqual>
																</logic:notEmpty></td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>

					</div>

					<div class="tab-pane" id="history" role="tabpanel"
						aria-labelledby="history-tab">
						<div class="ibox">
							<div class="ibox-body">
								<logic:present name="CASEHISTORYLIST">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="7">Case History Details</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Sr No</th>
														<th>Judge Name</th>
														<th>Business Date</th>
														<th>Hearing Date</th>
														<th>Purpose of Listing</th>
														<th>Cause Type</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="history" name="CASEHISTORYLIST"
														indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${history.sr_no}</td>
															<td>${history.judge_name}</td>
															<td>${history.business_date}</td>
															<td>${history.hearing_date}</td>
															<td>${history.purpose_of_listing}</td>
															<td>${history.causelist_type}</td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>

					</div>


					<div class="tab-pane" id="final" role="tabpanel"
						aria-labelledby="final-tab">
						<div class="ibox">
							<div class="ibox-body">

								<logic:present name="orderlist">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="6">Final Order Details</th>
													</tr>
													<tr>
														<th>Sl No.</th>
														<th>Sr No</th>
														<th>Order NO</th>
														<th>Order Date</th>
														<th>Order Details</th>
														<th>Order Document</th>
													</tr>
												</thead>
												<tbody>
													<logic:iterate id="order" name="orderlist" indexId="i">
														<tr>
															<td>${i+1}</td>
															<td>${order.sr_no}</td>
															<td>${order.order_no}</td>
															<td>${order.order_date}</td>
															<td>${order.order_details}</td>
															<td><logic:notEmpty name="order"
																	property="order_document_path">

																	<logic:notEqual value="-" name="order"
																		property="order_document_path">

																		<a href="./${order.order_document_path}"
																			class="btn btn-sm btn-info" target="">
																			${order.order_details}-${order.order_no}</a>
																	</logic:notEqual>
																</logic:notEmpty></td>
														</tr>
													</logic:iterate>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>

					</div>


					<div class="tab-pane" id="activities" role="tabpanel"
						aria-labelledby="activities-tab">
						<div class="ibox">
							<div class="ibox-body">
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

																		<a href='${activities.uploaded_doc_path}'
																			target='_new' class="btn btn-sm btn-info">View
																			Uploaded File</a>
																	</logic:notEqual>

																</logic:notEmpty></td>
														</tr>
													</logic:iterate>
												</tbody>

											</table>
										</div>
									</div>
								</logic:present>
							</div>
						</div>
					</div>

					<div class="tab-pane" id="parawise" role="tabpanel"
						aria-labelledby="parawise-tab">
						<div class="ibox">
							<div class="ibox-body">


								<logic:notEmpty name="pwrsuccessMsg">
									<div class="alert alert-success" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-check-all"></i> <strong>${pwrsuccessMsg}</strong>
									</div>
								</logic:notEmpty>
								<logic:notEmpty name="pwrerrorMsg">
									<div class="alert alert-danger" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-block-helper"></i> <strong>${pwrerrorMsg}</strong>
									</div>
								</logic:notEmpty>
								<logic:notEmpty name="pwrinfoMsg">
									<div class="alert alert-info" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-block-helper"></i> <strong>${pwrinfoMsg}</strong>
									</div>
								</logic:notEmpty>

								<logic:present name="ACTIVITIESDATA">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="7">Parawise Remarks history</th>
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
													<logic:iterate id="activities" name="ACTIVITIESDATA">
														<bean:define id="slno" value="0"></bean:define>
														<logic:equal value="Uploaded Parawise Remarks"
															name="activities" property="action_type">
															<tr>
																<td>${slno+1}</td>
																<td>${activities.inserted_on}</td>
																<td>${activities.action_type}</td>
																<td>${activities.inserted_by}</td>
																<td>${activities.assigned_to}</td>
																<td>${activities.remarks }</td>
																<td><logic:notEmpty name="activities"
																		property="uploaded_doc_path">

																		<logic:notEqual value="-" name="activities"
																			property="uploaded_doc_path">

																			<a href='${activities.uploaded_doc_path}'
																				target='_new' class="btn btn-sm btn-info">View
																				Uploaded File</a>
																		</logic:notEqual>

																	</logic:notEmpty></td>
															</tr>
															<bean:define id="slno" value="${slno+1 }" />
														</logic:equal>

													</logic:iterate>
												</tbody>

											</table>
										</div>
									</div>
								</logic:present>
<logic:present name="PWRSUBMITION">
									<logic:equal value="ENABLE" name="PWRSUBMITION">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 form-group">
										<label>Upload Petition <bean:message key="mandatory" /></label>
										<html:file styleClass="form-control"
											styleId="petitionDocument"
											property="dynaForm(petitionDocument)" />
										<span class="help-block"><bean:message
												key="upload.document.validation.msg" /></span>

										<logic:notEmpty name="CommonForm"
											property="dynaForm(petitionDocumentOld)">
											<a
												href='<bean:write name="CommonForm" property="dynaForm(petitionDocumentOld)"/>'
												target='_new' class="btn btn-sm btn-info">View Uploaded
												File</a>
										</logic:notEmpty>

									</div>

									<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label>Case Status <bean:message key="mandatory" />
											</label>
											<html:select styleId="ecourtsCaseStatus"
												property="dynaForm(ecourtsCaseStatus)"
												styleClass="form-control">
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
												target='_new' class="btn btn-sm btn-info">View Uploaded
												File</a>
										</logic:notEmpty>

									</div>
									<div class="col-sm-6 form-group casecloseddiv">
										<label>Action Taken Order <bean:message
												key="mandatory" /></label>

										<html:file styleClass="form-control"
											styleId="actionTakenOrder"
											property="dynaForm(actionTakenOrder)" />
										<span class="help-block"><bean:message
												key="upload.document.validation.msg" /></span>

										<logic:notEmpty name="CommonForm"
											property="dynaForm(actionTakenOrderOld)">
											<a
												href='<bean:write name="CommonForm" property="dynaForm(actionTakenOrderOld)"/>'
												target='_new' class="btn btn-sm btn-info">View Uploaded
												File</a>
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
											<label for="sel1"> Date of Submission of Parawise
												Remarks to GP/SC <font color="red">*</font>
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

								<div class="row">
									<div class="col-sm-6 form-group">
										<label>Action <bean:message key="mandatory" />
										</label>
										<html:select styleId="actionToPerform"
											property="dynaForm(actionToPerform)"
											styleClass="form-control">
											<html:option value="Parawise Remarks">Parawise Remarks</html:option>

										</html:select>
									</div>
									<div class="col-sm-6 form-group"></div>
								</div>

								<div class="row">
									<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
										<div class="form-group">
											<label for="sel1" id="remaeksTextId">Remarks: </label>

											<html:textarea cols="600" styleId="remarks"
												property="dynaForm(remarks)"
												style="width: 1000px; height: 250px;">
											</html:textarea>
										</div>
									</div>
								</div>
								

										<button class="btn btn-md btn-success" type="button"
											name="forward"
											onclick="return gpApprove('Parawise Remarks');">Approve</button>

										<button class="btn btn-md btn-danger" type="button"
											name="forward" onclick="return gpReject('Parawise Remarks');">Return</button>
									</logic:equal>
								</logic:present>
							</div>
						</div>
					</div>


					<div class="tab-pane" id="counter" role="tabpanel"
						aria-labelledby="counter-tab">
						<div class="ibox">
							<div class="ibox-body">

								<logic:notEmpty name="countersuccessMsg">
									<div class="alert alert-success" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-check-all"></i> <strong>${countersuccessMsg}</strong>
									</div>
								</logic:notEmpty>
								<logic:notEmpty name="countererrorMsg">
									<div class="alert alert-danger" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-block-helper"></i> <strong>${countererrorMsg}</strong>
									</div>
								</logic:notEmpty>
								<logic:notEmpty name="errorMsg">
									<div class="alert alert-info" role="alert">
										<button type="button" class="close" data-dismiss="alert"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<i class="mdi mdi-block-helper"></i> <strong>${pwrinfoMsg}</strong>
									</div>
								</logic:notEmpty>
								<logic:present name="ACTIVITIESDATA">
									<div class="row">
										<div class="col-md-12">
											<table class="table table-striped table-bordered table-hover"
												cellspacing="0" width="100%">
												<thead>
													<tr>
														<th colspan="7">Counter Affidavit history</th>
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
													<logic:iterate id="activities" name="ACTIVITIESDATA">
														<bean:define id="slno" value="0"></bean:define>
														<logic:equal value="Uploaded Counter" name="activities"
															property="action_type">
															<tr>
																<td>${slno+1}</td>
																<td>${activities.inserted_on}</td>
																<td>${activities.action_type}</td>
																<td>${activities.inserted_by}</td>
																<td>${activities.assigned_to}</td>
																<td>${activities.remarks }</td>
																<td><logic:notEmpty name="activities"
																		property="uploaded_doc_path">

																		<logic:notEqual value="-" name="activities"
																			property="uploaded_doc_path">

																			<a href='${activities.uploaded_doc_path}'
																				target='_new' class="btn btn-sm btn-info">View
																				Uploaded File</a>
																		</logic:notEqual>

																	</logic:notEmpty></td>
															</tr>
															<bean:define id="slno" value="${slno+1 }" />
														</logic:equal>

													</logic:iterate>
												</tbody>

											</table>
										</div>
									</div>
								</logic:present>
								<logic:present name="COUNTERSUBMITION">
									<logic:equal value="ENABLE" name="COUNTERSUBMITION">
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

													<html:file styleClass="form-control"
														styleId="appealFileCopy"
														property="dynaForm(appealFileCopy)" />
													<span class="help-block"><bean:message
															key="upload.document.validation.msg" /></span>

													<logic:notEmpty name="CommonForm"
														property="dynaForm(appealFileCopyOld)">
														<a
															href='<bean:write name="CommonForm" property="dynaForm(appealFileCopyOld)"/>'
															target='_new' class="btn btn-sm btn-info">View
															Uploaded File</a>
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
										<div>
											<div class="row">
												<div class="col-sm-6 form-group ">
													<label>Counter Filed</label>
													<html:select styleId="counterFiled"
														property="dynaForm(counterFiled)"
														styleClass="form-control">
														<html:option value="0">---SELECT---</html:option>
														<html:option value="No">No</html:option>
														<html:option value="Yes">Yes</html:option>
													</html:select>
												</div>
												<div class="col-sm-6 form-group counteruploaddiv">
													<label>Counter File Upload</label>

													<html:file styleClass="form-control"
														styleId="counterFileCopy"
														property="dynaForm(counterFileCopy)" />
													<span class="help-block"><bean:message
															key="upload.document.validation.msg" /></span>

													<logic:notEmpty name="CommonForm"
														property="dynaForm(counterFileCopyOld)">
														<a
															href='<bean:write name="CommonForm" property="dynaForm(counterFileCopyOld)"/>'
															target='_new' class="btn btn-sm btn-info">View
															Uploaded File</a>
													</logic:notEmpty>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-sm-6 form-group">
												<label>Action <bean:message key="mandatory" />
												</label>
												<html:select styleId="actionToPerform"
													property="dynaForm(actionToPerform)"
													styleClass="form-control">
													<html:option value="Counter Affidavit">Counter Affidavit</html:option>
													<%-- <html:option value="Interim Orders">Interim Orders</html:option>
												<html:option value="Appeal">Appeal</html:option>
												<html:option value="Contempt">Contempt</html:option>
												<html:option value="Judgement">Judgement</html:option> --%>
												</html:select>
											</div>
											<div class="col-sm-6 form-group"></div>
										</div>

										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
												<div class="form-group">
													<label for="sel1" id="remaeksTextId">Remarks: </label>

													<html:textarea cols="600" styleId="remarks"
														property="dynaForm(remarks)"
														style="width: 1000px; height: 250px;">
													</html:textarea>
												</div>
											</div>
										</div>
										<button class="btn btn-md btn-success" type="button"
											name="forward"
											onclick="return gpApprove('Counter Affidavit');">Update
											& Finalize Counter</button>

										<button class="btn btn-md btn-danger" type="button"
											name="forward"
											onclick="return gpReject('Counter Affidavit');">Return</button>
									</logic:equal>
								</logic:present>
							</div>
						</div>
					</div>
				</div>

			</div>
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
		

	</html:form>
</div>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>
<script type="text/javascript" src="js/nicEdit-latest.js"></script>
<script type="text/javascript">
	bkLib.onDomLoaded(function() {
		new nicEditor({
			fullPanel : true
		}).panelInstance('remarks');
	});
	bkLib.onDomLoaded(function() {
		new nicEditor({
			fullPanel : true
		}).panelInstance('remarks2');
	});
</script>
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

		if ($("#petitionDocument").val() == ""
				|| $("#petitionDocument").val() == null) {
			alert("Petition Document Required.");
			$("#petitionDocument").focus();
			return false;
		} else if ($("#ecourtsCaseStatus").val() == ""
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

	function gpApprove(val) {
		alert("rms--"+$("#remarks").val())
		$("#mode").val("gpApprove");
		$("#actionToPerform").val(val);
		$("#AssignedCasesToSectionForm").submit();
		return true;
	}
	function gpReject(val) {
		$("#mode").val("gpReject");
		$("#actionToPerform").val(val);
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

						$("#parawiseRemarksSubmitted").change(function() {
							if ($("#parawiseRemarksSubmitted").val() == "Yes") {
								$(".parawiseRemarkssubmitteddiv").show();
							} else {
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

						$("#appealFiled").change(function() {
							if ($("#appealFiled").val() == "Yes") {
								$(".appealuploaddiv").show();
							} else {
								$(".appealuploaddiv").hide();
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
						$("#petitionDocument")
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
	function viewCaseDetailsPopup1(cino, caseNo) {
		//alert("hai"+cino+caseNo);
		var heading = "View/Submit Daily Status for Case : "+cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./DailyStatusEntry.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino=" +cino+"&caseType="+caseNo;
			//srclink = "./EcourtsDeptInstructionNew.do?mode=getCasesList&cino="+cino+"&caseType="+caseNo;
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
</body>
</html>