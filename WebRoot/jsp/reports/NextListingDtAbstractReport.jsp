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
	<html:form action="/NextListingDtAbstract"
		styleId="HCCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
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
									<th>Sect. Department Code</th>
									<th>Sect. Department Name</th>
									<th>Total Cases</th>
									<th>Hearing Today</th>
									<th>Hearing Tomorrow</th>
									<th>With in this week</th>
									<th>7 - 14 days.</th>
									<th>14 - 21 days</th>
									<th>21 - 28 days</th>
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

								<logic:iterate id="map" name="secdeptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td><a
											href="javascript:ShowHODWise('${map.deptcode}','${map.description }');">${map.description }</a></td>
										<td style="text-align: right;">${map.total }</td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','today');">${map.today }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','tomorrow');">${map.tomorrow }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week1');">${map.week1 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week2');">${map.week2 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week3');">${map.week3 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week4');">${map.week4 }</a></td>
									</tr>
									<bean:define id="Totals" value="${Totals + map.total }"></bean:define>
									<bean:define id="pettTotals" value="${pettTotals + map.tomorrow }"></bean:define>
									<bean:define id="scanTotals" value="${scanTotals + map.today }"></bean:define>
									<bean:define id="closedTotals" value="${closedTotals + map.week1 }"></bean:define>
									<bean:define id="countersTotals" value="${countersTotals + map.week2 }"></bean:define>
									<bean:define id="pwrTotals" value="${pwrTotals + map.week3 }"></bean:define>
									<bean:define id="gpoTotals" value="${gpoTotals + map.week4 }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
									<td colspan="1" style="text-align: right;">${scanTotals }</td>
									<td colspan="1" style="text-align: right;">${pettTotals }</td>
									<td colspan="1" style="text-align: right;">${closedTotals }</td>
									<td colspan="1" style="text-align: right;">${countersTotals }</td>
									<td colspan="1" style="text-align: right;">${pwrTotals }</td>
									<td colspan="1" style="text-align: right;">${gpoTotals }</td>
								</tR>
							</tfoot>
						</table>

					</logic:present>
					<logic:present name="deptwise">

						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									<th>Hearing Today</th>
									<th>Hearing Tomorrow</th>
									<th>With in this week</th>
									<th>7 - 14 days.</th>
									<th>14 - 21 days</th>
									<th>21 - 28 days</th>

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

								<logic:iterate id="map" name="deptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td>${map.description }</td>
										<td style="text-align: right;">${map.total }</td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','today');">${map.today }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','tomorrow');">${map.tomorrow }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week1');">${map.week1 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week2');">${map.week2 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week3');">${map.week3 }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWise('${map.deptcode}','${map.description }','week4');">${map.week4 }</a></td>
									</tr>
									<bean:define id="Totals" value="${Totals + map.total }"></bean:define>
									<bean:define id="pettTotals" value="${pettTotals + map.tomorrow }"></bean:define>
									<bean:define id="scanTotals" value="${scanTotals + map.today }"></bean:define>
									<bean:define id="closedTotals" value="${closedTotals + map.week1 }"></bean:define>
									<bean:define id="countersTotals" value="${countersTotals + map.week2 }"></bean:define>
									<bean:define id="pwrTotals" value="${pwrTotals + map.week3 }"></bean:define>
									<bean:define id="gpoTotals" value="${gpoTotals + map.week4 }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
									<td colspan="1" style="text-align: right;">${scanTotals }</td>
									<td colspan="1" style="text-align: right;">${pettTotals }</td>
									<td colspan="1" style="text-align: right;">${closedTotals }</td>
									<td colspan="1" style="text-align: right;">${countersTotals }</td>
									<td colspan="1" style="text-align: right;">${pwrTotals }</td>
									<td colspan="1" style="text-align: right;">${gpoTotals }</td>
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
									<th>Current Status</th>
									<th>Date of Filing</th>
									<!-- <th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th> -->

									<th>Case Reg No.</th>
									<th>Prayer</th>

									<th>Filing No.</th>
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
										<td nowrap="nowrap"><span style="color: navy;font-weight: bold;text-align: center;"> ${map.current_status}</span>
										
											<logic:notEmpty name="map" property="fullname"><logic:notEqual value=" " name="map" property="fullname"><br />${map.fullname}</logic:notEqual></logic:notEmpty>
											<logic:notEmpty name="map" property="designation"><logic:notEqual value=" " name="map" property="designation"><br /> ${map.designation}</logic:notEqual></logic:notEmpty>
											<logic:notEmpty name="map" property="mobile"><logic:notEqual value=" " name="map" property="mobile"><br /> ${map.mobile}</logic:notEqual></logic:notEmpty>
											<logic:notEmpty name="map" property="email"><logic:notEqual value=" " name="map" property="email"><br /> ${map.email}</logic:notEqual></logic:notEmpty>
											<logic:notEmpty name="map" property="district_name"><logic:notEqual value=" " name="map" property="district_name"><br /> ${map.district_name}</logic:notEqual></logic:notEmpty>
												<%-- ${map.globalorgname}<br /> 
												 ${map.designation} <br />
												${map.mobile} <br /> ${map.email} <br /> ${map.district_name }--%>
										</td>
										<td><logic:notEmpty name="map" property="date_of_filing">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty></td>

										<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
										<td nowrap="nowrap">${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
										<td style="min-width: 350px;text-align: justify;"><logic:notEmpty
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
									<td colspan="20">&nbsp;</td>
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
	function ShowHODWise(deptId, deptDesc) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseStatusAbstract").submit();
	}
	function showCasesWise(deptId, deptDesc, status) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
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
