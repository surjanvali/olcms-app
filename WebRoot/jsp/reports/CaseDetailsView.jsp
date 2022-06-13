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
	<html:form action="/AssignedCasesToSection">
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
			<%-- <div class="ibox-head">
				<div class="ibox-title">
					<h5 style="color:Black;">
						<logic:notEmpty name="cino">
									${cino}
								</logic:notEmpty>
					</h5>
				</div>
			</div> --%>
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
								<b>Petetioner Name: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_name}</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petetioner Advacate : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_adv}</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Petetioner Legal Heir : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.pet_legal_heir}</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Respondent Name : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${map.res_name}</div>
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
					</logic:iterate>
				</logic:present>
				<hr />

				<logic:present name="actlist">
					<div class="row">
						<div class="col-md-12">
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
					</div>
				</logic:present>

				<logic:present name="PETEXTRAPARTYLIST">
					<div class="row">
						<div class="col-md-12">
							<table class="table table-striped table-bordered table-hover"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th colspan="3">Petetioner's List</th>
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
										<th>IA PetetionerName</th>
										<th>IA PetetionerDispoasal</th>
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
									<logic:iterate id="inter" name="INTERIMORDERSLIST" indexId="i">
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
									<logic:iterate id="history" name="CASEHISTORYLIST" indexId="i">
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



				<logic:present name="OLCMSCASEDATA">
					<logic:iterate id="datamap" name="OLCMSCASEDATA">

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Uploaded Petition : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="petition_document">
									<a href='${datamap.petition_document }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Case Status : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.ecourts_case_status }</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Uploaded Judgement Order : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="judgement_order">
									<a href='${datamap.judgement_order }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>PWR Submitted Date: </b>
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.pwr_submitted_date }</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Uploaded Counter Filed : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="counter_filed">
									<a href='${datamap.counter_filed }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Remarks : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.remarks }</div>
						</div>

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Corresponding GP: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.corresponding_gp }</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b> PWR Uploaded: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.pwr_uploaded }</div>


							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Action Taken Order</b>
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="action_taken_order">
									<a href='${datamap.action_taken_order }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</div>
						</div>



						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>PWR Received Date: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.pwr_received_date }</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>PWR Approved GP: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.pwr_approved_gp }</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>PWR Approved GP Date: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.pwr_gp_approved_date }</div>
						</div>

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Uploaded Appeal Filed: </b>
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="appeal_filed">
									<a href='${datamap.appeal_filed_copy }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Appeal Filed: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.appeal_filed }</div>



							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Uploaded PWR File: </b>
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<logic:notEmpty name="datamap" property="pwr_uploaded">
									<a href='${datamap.pwr_uploaded_copy }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<b>Appeal Filed Date: </b>
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								${datamap.appeal_filed_date }</div>

						</div>


					</logic:iterate>
				</logic:present>

				<%-- 



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
							<div class="col-xs-12 col-sm-6 form-group">
								<label class="font-bold">Remarks <font color="red">*</font>
								</label>
								<html:textarea styleId="remarks" property="dynaForm(remarks)"
									styleClass="form-control" />
							</div>
						</div>

					</logic:iterate>
				</logic:present> --%>
				<!-- <div
					class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-rightt">
					<input type="button" name="btn" value=" Print"
						onclick=" return print();" class="btn btn-primary text-center"
						id="btn_submit" />
				</div> -->
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
<!-- 
<script type="text/javascript">
	function print() {
		document.forms[0].mode.value = "printView";
		document.forms[0].submit();
	}
</script>
 -->