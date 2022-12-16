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
.row {
 border :1px solid black;
}
.pull-rightt{
border:1px 1px 1px 1px solid black;
}
.row > div > div{
  background: lightgrey;
  border: 1px solid grey;
}
.cell{
 border: 1px 1px 1px 1px;
}
</style>

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									<font style="color:green">${HEADING }</font>
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/AssignedCasesToSection">
		<html:hidden styleId="mode" property="mode" />
		<tr class="row1" >
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
			<div class="ibox-body table" style="border: 2px solid black;">
				<logic:present name="USERSLIST">
				<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
				      <thead>
						<tr class="row1" >
							<th colspan="6"><font style="color:#3d80eb">Case Details</font></th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="map" name="USERSLIST">
					      
						 <tr class="row1" >
							<td class="cell" align="right">
								 Date of filing: 
							</td>
							<td class="cell" >
								<b>${map.date_of_filing}</b>
							</td>

							<td class="cell" align="right">
								 Case Type : 
							</td>
							<td class="cell" >
								<b>${map.type_name_reg}</b></td>
							<td class="cell" align="right">
								 Filing No.:
							</td>
							<td class="cell" align="right">
								<b>${map.fil_no}</b></td>
						</tr>
						<tr class="row1" >

							<td class="cell" align="right">
								 Filing Year: 
							</td>
							<td class="cell" >
								<b>${map.fil_year}</b></td>
							<td class="cell" align="right">
								Registration No:
							</td>
							<td class="cell" >
								<b>${map.reg_no}</b></td>
							<td class="cell" align="right">
								 Est Code: 
							</td>
							<td class="cell" >
								<b>${map.est_code}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								Case ID:
							</td>
							<td class="cell" >
								<b>${map.case_type_id}</b></td>

							<td class="cell" align="right">
								Cause Type: 
							</td>
							<td class="cell" >
								<b>${map.causelist_type}</b></td>
							<td class="cell" align="right">
								 Bench Name: 
							</td>
							<td class="cell" >
								<b>${map.bench_name}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								Judicial Branch: 
							</td>
							<td class="cell" >
								<b>${map.judicial_branch}</b></td>
							<td class="cell" align="right">
								Coram: 
							</td>
							<td class="cell" >
								<b>${map.coram}</b></td>
							<td class="cell" align="right">
								Court Est Name: 
							</td>
							<td class="cell" >
								<b>${map.court_est_name}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								 State Name:
							</td>
							<td class="cell" >
								<b>${map.state_name}</b></td>
							<td class="cell" align="right">
								District : 
							</td>
							<td class="cell" >
								<b>${map.dist_name}</b></td>
							<td class="cell" align="right">
								Date Of First List : 
							</td>
							<td class="cell" >
								<b>${map.date_first_list}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								Date Of Next List 
							</td>
							<td class="cell" >
								<b>${map.date_next_list}</b></td>


							<td class="cell" align="right">
								Date Of Decision : 
							</td>
							<td class="cell" >
								<b>${map.date_of_decision}</b></td>
							<td class="cell" align="right">
								Purpose : 
							</td>
							<td class="cell" >
								<b>${map.purpose_name}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								Petitioner Name:
							</td>
							<td class="cell" >
								<b>${map.pet_name}</b></td>

							<td class="cell" align="right">
								Petitioner Advocate : 
							</td>
							<td class="cell" >
								<b>${map.pet_adv}</b></td>
							<td class="cell" align="right">
								Petitioner Legal Heir : 
							</td>
							<td class="cell" >
								<b>${map.pet_legal_heir}</b></td>
						</tr>
						<tr class="row1" >
							<td class="cell" align="right">
								Respondent Name : 
							</td>
							<td class="cell" >
								<b>${map.res_name}, 
								${map.address}</b>
								</td>
							<td class="cell" align="right">
								Respondent Advocate : 
							</td>
							<td class="cell" >
								<b>${map.res_adv}</b></td>
							<td class="cell" align="right">
								Respondent Advocate : 
							</td>
							<td class="cell" >
								<b>${map.res_adv}</b></td>
						</tr>

						<tr class="row1" >
							<td class="cell" align="right">
								Prayer:								
								
							</td>
							<td class="cell" colspan="5">
								<b>${map.prayer}</b>
							</td>
						</tr>
					</logic:iterate>
					</tbody>
					</table>
				</logic:present>
				
				<hr />

				<logic:present name="actlist">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="4"><font style="color:#3d80eb">ACTS List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Act</th>
										<th class="cell" >Act Name</th>
										<th class="cell" >Section</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="ccd" name="actlist" indexId="i">
										<tr class="row1" >

											<td class="cell" >${i+1}</td>
											<td class="cell" >${ccd.act}</td>
											<td class="cell" >${ccd.actname}</td>
											<td class="cell" >${ccd.section}</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div>
				</logic:present>

				<logic:present name="PETEXTRAPARTYLIST">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="3"><font style="color:#3d80eb">Petitioner's List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Party No</th>
										<th class="cell" >Party Name</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="pet" name="PETEXTRAPARTYLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${pet.party_no}</td>
											<td class="cell" >${pet.party_name}</td>

										</tr>

									</logic:iterate>
								</tbody>
							</table>
						</div>
					
				</logic:present>

				<logic:present name="RESEXTRAPARTYLIST">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="4"><font style="color:#3d80eb">Respondent List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Party No</th>
										<th class="cell" >Party Name</th>
										<th class="cell" >Address</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="res" name="RESEXTRAPARTYLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${res.party_no}</td>
											<td class="cell" >${res.party_name}</td>
											<td class="cell" >${res.address}</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div>
					
				</logic:present>

				<logic:present name="IAFILINGLIST">
					
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="6"><font style="color:#3d80eb">IAFilling List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Sr No</th>
										<th class="cell" >IA NO</th>
										<th class="cell" >IA Petitioner Name</th>
										<th class="cell" >IA Petitioner Dispoasal</th>
										<th class="cell" >IA Date of Filling</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="iafi" name="IAFILINGLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${iafi.sr_no}</td>
											<td class="cell" >${iafi.ia_no}</td>
											<td class="cell" >${iafi.ia_pet_name}</td>
											<td class="cell" >${iafi.ia_pend_disp}</td>
											<td class="cell" >${iafi.date_of_filing}</td>

										</tr>

									</logic:iterate>
								</tbody>

							</table>

						</div>
				</logic:present>

				<logic:present name="INTERIMORDERSLIST">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="6"><font style="color:#3d80eb">InterimOrder List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Sr No</th>
										<th class="cell" >Order NO</th>
										<th class="cell" >Order Date</th>
										<th class="cell" >Order Details</th>
										<th class="cell" >Order Document</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="inter" name="INTERIMORDERSLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${inter.sr_no}</td>
											<td class="cell" >${inter.order_no}</td>
											<td class="cell" >${inter.order_date}</td>
											<td class="cell" >${inter.order_details}</td>

											<td class="cell" ><logic:notEmpty name="inter"
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
				</logic:present>

				<logic:present name="LINKCASESLIST">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="4"><font style="color:#3d80eb">Case Link List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Sr No</th>
										<th class="cell" >Filling NO</th>
										<th class="cell" >Case Number</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="link" name="LINKCASESLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${link.sr_no}</td>
											<td class="cell" >${link.filing_number}</td>
											<td class="cell" >${link.case_number}</td>

										</tr>
									</logic:iterate>
								</tbody>

							</table>

						</div>
				</logic:present>

				<logic:present name="OBJECTIONSLIST">
			
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="6"><font style="color:#3d80eb">Objections List</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Objection Number</th>
										<th class="cell" >Objection Description</th>
										<th class="cell" >Scrunity Date</th>
										<th class="cell" >Compliance Date</th>
										<th class="cell" >Receipt Date</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="obj" name="OBJECTIONSLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${obj.objection_no}</td>
											<td class="cell" >${obj.objection_desc}</td>
											<td class="cell" >${obj.scrutiny_date}</td>
											<td class="cell" >${obj.objections_compliance_by_date}</td>
											<td class="cell" >${obj.obj_reciept_date}</td>

										</tr>
									</logic:iterate>
								</tbody>

							</table>

					</div>
				</logic:present>

				<logic:present name="CASEHISTORYLIST">
				
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="7"><font style="color:#3d80eb">Case History Details</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Sr No</th>
										<th class="cell" >Judge Name</th>
										<th class="cell" >Business Date</th>
										<th class="cell" >Hearing Date</th>
										<th class="cell" >Purpose of Listing</th>
										<th class="cell" >Cause Type</th>

									</tr>
								</thead>
								<tbody>
									<logic:iterate id="history" name="CASEHISTORYLIST" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${history.sr_no}</td>
											<td class="cell" >${history.judge_name}</td>
											<td class="cell" >${history.business_date}</td>
											<td class="cell" >${history.hearing_date}</td>
											<td class="cell" >${history.purpose_of_listing}</td>
											<td class="cell" >${history.causelist_type}</td>



										</tr>
									</logic:iterate>
								</tbody>

							</table>
					</div>
				</logic:present>

				<logic:present name="orderlist">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="6"><font style="color:#3d80eb">Final Order Details</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Sr No</th>
										<th class="cell" >Order NO</th>
										<th class="cell" >Order Date</th>
										<th class="cell" >Order Details</th>
										<th class="cell" >Order Document</th>


									</tr>
								</thead>
								<tbody>
									<logic:iterate id="order" name="orderlist" indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${order.sr_no}</td>
											<td class="cell" >${order.order_no}</td>
											<td class="cell" >${order.order_date}</td>
											<td class="cell" >${order.order_details}</td>
											<td class="cell" ><logic:notEmpty name="order"
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
				</logic:present>


				<logic:present name="ACTIVITIESDATA">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="7"><font style="color:#3d80eb">Case Activities</font></th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Date</th>
										<th class="cell" >Activity</th>
										<th class="cell" >Updated By</th>
										<th class="cell" >Assigned to</th>
										<th class="cell" >Remarks</th>
										<th class="cell" >Uploaded Document</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="activities" name="ACTIVITIESDATA"
										indexId="i">
										<tr class="row1" >
											<td class="cell" >${i+1}</td>
											<td class="cell" >${activities.inserted_on}</td>
											<td class="cell" >${activities.action_type}</td>
											<td class="cell" >${activities.inserted_by}</td>
											<td class="cell" >${activities.assigned_to}</td>
											<td class="cell" >${activities.remarks }</td>
											<td class="cell" ><logic:notEmpty name="activities"
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
				</logic:present>



				<logic:present name="OLCMSCASEDATA">
				<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
				       <thead>
						<tr class="row1" >
							<th colspan="6"><font style="color:#3d80eb">Uploaded Details</font></th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="datamap" name="OLCMSCASEDATA">

						<tr class="row1" >
							<td class="cell" align="right">
								Uploaded Petition : 
							</td>
							<td class="cell" >
								<logic:notEmpty name="datamap" property="petition_document">
									<a href='${datamap.petition_document }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</td>
							<td class="cell" align="right">
								Case Status : 
							</td>
							<td class="cell" >
								<b>${datamap.ecourts_case_status }</b></td>

							<td class="cell" align="right">
								Uploaded Judgement Order : 
							</td>
							<td class="cell" >
								<logic:notEmpty name="datamap" property="judgement_order">
									<a href='${datamap.judgement_order }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</td>
						</tr>
						<tr class="row1" >
							<td class="cell"  align="right">
								PWR Submitted Date: 
							</td>

							<td class="cell" >
								<b>${datamap.pwr_submitted_date }</b></td>

							<td class="cell" align="right">
								Uploaded Counter Filed : 
							</td>
							<td class="cell" >
								<logic:notEmpty name="datamap" property="counter_filed">
									<a href='${datamap.counter_filed }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</td>
							<td class="cell" align="right">
								Remarks : 
							</td>
							<td class="cell" >
								<b>${datamap.remarks }</b></td>
						</tr>

						<tr class="row1" >
							<td class="cell"  align="right">
								Corresponding GP:
							</td>
							<td class="cell" >
								<b>${datamap.corresponding_gp }</b></td>

							<td class="cell"  align="right">
								 PWR Uploaded: 
							</td>
							<td class="cell" >
								<b>${datamap.pwr_uploaded }</b></td>


							<td class="cell"  align="right">
								Action Taken Order
							</td>

							<td class="cell" >
								<logic:notEmpty name="datamap" property="action_taken_order">
									<a href='${datamap.action_taken_order }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>
							</td>
						</tr>



						<tr class="row1" >
							<td class="cell"  align="right">
								PWR Received Date: 
							</td>
							<td class="cell" >
								<b>${datamap.pwr_received_date }</b></td>

							<td class="cell"  align="right">
								PWR Approved GP: 
							</td>
							<td class="cell" >
								<b>${datamap.pwr_approved_gp }</b></td>

							<td class="cell"  align="right">
								PWR Approved GP Date: 
							</td>
							<td class="cell" >
								<b>${datamap.pwr_gp_approved_date }</b></td>
						</tr>

						<tr class="row1" >
							<td class="cell"  align="right">
								Uploaded Appeal Filed: 
							</td>

							<td class="cell" >
								<logic:notEmpty name="datamap" property="appeal_filed">
									<a href='${datamap.appeal_filed_copy }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</td>
							<td class="cell"  align="right">
								Appeal Filed: 
							</td>
							<td class="cell" >
								<b>${datamap.appeal_filed }</b></td>



							<td class="cell"  align="right">
								Uploaded PWR File: 
							</td>

							<td class="cell" >
								<logic:notEmpty name="datamap" property="pwr_uploaded">
									<a href='${datamap.pwr_uploaded_copy }' target='_new'
										class="btn btn-sm btn-info">View Uploaded File</a>
								</logic:notEmpty>

							</td>

							

						</tr>
						<tr>
						<td class="cell"  align="right">
								Appeal Filed Date: 
							</td>

							<td class="cell" colspan="5">
								<b>${datamap.appeal_filed_date }</b></td>
						</tr>


					</logic:iterate>
					</tbody>
					</table>
				</logic:present>

				<%-- 



						<div class="parawiseRemarksdiv">
							<tr class="row1" >
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

							<tr class="row1" >
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
							<tr class="row1" >
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
							<tr class="row1" >
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

						<tr class="row1" >
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