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
<div class="page-content fade-in-up">
	<html:form action="/HCCaseDistWiseAbstract"
		styleId="HCCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(distid)" styleId="distid" />
		<html:hidden property="dynaForm(distName)" styleId="distName" />
		<html:hidden property="dynaForm(caseStatus)" styleId="caseStatus" />
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
					<div class="ibox-title">District Wise Final Order Implemented Status</div>
				</div>
				<div class="ibox-body">
					<div class="row">
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
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<input type="button" name="getreport" value="Get Report"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div></div></div>
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
					<logic:present name="secdeptwise">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>District Name</th>
									<th>Total Cases</th>
									
									<th>Pending at Dist. Collector Login</th>
									<th>Pending at Dist. Nodal Officer</th>
									<th>Pending at Dist. Section Officer</th>
									
									<th>Scanned by APOLCMS Cell</th>
									<th>Petition Uploaded by Dept.</th>
									<th>Parawise Remarks Uploaded</th>
									<!-- <th>Parawise Remarks Approved by GP</th> -->
									<th>Counter filed</th>
									<th>Final Order Implemented</th>
									<th>Appeal Filed</th>
									<th>Dismissed</th>
									<th>Closed</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<bean:define id="pettTotals" value="0"></bean:define>
								<bean:define id="scanTotals" value="0"></bean:define>
								<bean:define id="closedTotals" value="0"></bean:define>
								<bean:define id="countersTotals" value="0"></bean:define>
								<bean:define id="pwrTotals" value="0"></bean:define>
								<bean:define id="gpoTotals" value="0"></bean:define>
								
								<bean:define id="dcTotals" value="0"></bean:define>
								<bean:define id="dnoTotals" value="0"></bean:define>
								<bean:define id="dsecTotals" value="0"></bean:define>
								
								<bean:define id="final_order" value="0"></bean:define>
								<bean:define id="appeal_order" value="0"></bean:define>
								<bean:define id="dismissed_order" value="0"></bean:define>

								<logic:iterate id="map" name="secdeptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><a
											href="javascript:ShowHODWise('${map.district_id}','${map.district_name }');">${map.district_name }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','ALL');">${map.total_cases }</a></td>
											
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','DC');">${map.pending_dc }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','DNO');">${map.pending_dno }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','DSEC');">${map.pending_dsec }</a></td>	
											
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','SCANNEDDOC');">${map.olcms_uploads }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','PET');">${map.petition_uploaded }</a></td>
										
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','COUNTERUPLOADED');">${map.counter_uploaded }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','PWRUPLOADED');">${map.pwrcounter_uploaded }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','FINALORDER');">${map.final_order }</a></td>
											<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','APPEALORDER');">${map.appeal_order }</a></td>
											<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','DISMISSEDORDER');">${map.dismissed_order }</a></td>
											<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.district_id}','${map.district_name }','CLOSED');">${map.closed_cases }</a></td>
											
											
									</tr>
									<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
									<bean:define id="pettTotals" value="${pettTotals + map.petition_uploaded }"></bean:define>
									<bean:define id="scanTotals" value="${scanTotals + map.olcms_uploads }"></bean:define>
									<bean:define id="closedTotals"
										value="${closedTotals + map.closed_cases }"></bean:define>
									<bean:define id="countersTotals"
										value="${countersTotals + map.counter_uploaded }"></bean:define>
									<bean:define id="pwrTotals"
										value="${pwrTotals + map.pwrcounter_uploaded }"></bean:define>
										
									<bean:define id="gpoTotals" value="${gpoTotals + map.gpoTotals }"></bean:define>
									
									<bean:define id="dcTotals" value="${dcTotals + map.pending_dc }"></bean:define>
									<bean:define id="dnoTotals" value="${dnoTotals + map.pending_dno }"></bean:define>
									<bean:define id="dsecTotals" value="${dsecTotals + map.pending_dsec }"></bean:define>
									
									<bean:define id="final_order" value="${final_order + map.final_order }"></bean:define>
									<bean:define id="appeal_order" value="${appeal_order + map.appeal_order }"></bean:define>
									<bean:define id="dismissed_order" value="${dismissed_order + map.dismissed_order }"></bean:define>
									
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
									
									<td colspan="1" style="text-align: right;">${dcTotals }</td>
									<td colspan="1" style="text-align: right;">${dnoTotals }</td>
									<td colspan="1" style="text-align: right;">${dsecTotals }</td>
									
									<td colspan="1" style="text-align: right;">${scanTotals }</td>
									<td colspan="1" style="text-align: right;">${pettTotals }</td>
									<td colspan="1" style="text-align: right;">${countersTotals }</td>
									<td colspan="1" style="text-align: right;">${pwrTotals }</td>
									<td colspan="1" style="text-align: right;">${final_order}</td>
									<td colspan="1" style="text-align: right;">${appeal_order}</td>
									<td colspan="1" style="text-align: right;">${dismissed_order}</td>
									<td colspan="1" style="text-align: right;">${closedTotals}</td>
								</tR>
							</tfoot>
						</table>

					</logic:present>
					

					<logic:present name="CASESLIST">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>CINo</th>
									<th>Scanned Affidavit</th>
									<!-- <th>Assigned to</th> -->
									<th>Date of Filing</th>
									<!-- <th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th> -->

									<th>Case Reg No.</th>
									<th>Prayer</th>

									<th>SR Number</th>
									<th>Filing Year</th>
									<th>Date of Next List</th>
									<th>Bench</th>
									<th>Judge Name</th>
									<th>Petitioner</th>
									<th>District</th>
									<th>Purpose</th>
									<th>Respondents</th>
									<th>Petitioner Advocate</th>
									<th>Respondent Advocate</th>
									<th>Orders</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }.</td>
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
										<td><logic:notEmpty name="map" property="date_of_filing">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty></td>

										<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
										<td>${map.type_name_fil }/ ${map.reg_no} / ${map.reg_year }</td>
										<td style="width: 300px;">${map.prayer }</td>

										<td>${map.fil_no}</td>
										<td>${map.fil_year }</td>
										<td><logic:notEmpty name="map" property="date_next_list">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_next_list">
																	${map.date_next_list }
																</logic:notEqual>
											</logic:notEmpty></td>
										<td>${map.bench_name }</td>
										<td>Hon'ble Judge : ${map.coram }</td>
										<td>${map.pet_name }</td>
										<td>${map.dist_name }</td>
										<td>${map.purpose_name }</td>
										<td>${map.res_name }, ${map.address}</td>

										<td>${map.pet_adv }</td>
										<td>${map.res_adv }</td>
										<td style="text-align: center;">${map.orderpaths }</td>
									</tr>

								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="19">&nbsp;</td>
								</tR>
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

<script type="text/javascript">

function fnShowCases() {
	$("#mode").val("unspecified");
	$("#HCCaseStatusAbstract").submit();
}



	function ShowHODWise(deptId, deptDesc) {
		$("#distid").val(deptId);
		$("#distName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseStatusAbstract").submit();
	}
	function showCasesWise(deptId, deptDesc, status) {
		$("#distid").val(deptId);
		$("#distName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#mode").val("getCasesList");
		$("#HCCaseStatusAbstract").submit();
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
