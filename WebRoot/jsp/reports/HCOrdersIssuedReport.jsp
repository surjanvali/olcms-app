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
<!-- <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'> -->
<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<link href="./assetsnew/vendors/select2/dist/css/select2.min.css" rel="stylesheet" />
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css" rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<div class="page-content fade-in-up">
	<html:form action="/HCOrdersIssuedReport"
		styleId="HCCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(caseStatus)" styleId="caseStatus" />
		<html:hidden property="dynaForm(reportType)" styleId="reportType" />
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
				<div class="ibox-title">Orders Issued Report</div>
			</div>
			<div class="ibox-body">
				<%-- <div class="row">
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label>District</label>
							<html:select styleId="districtId" property="dynaForm(districtId)"
								styleClass="form-control select2Class">
								<html:option value="0">---ALL---</html:option>
								<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
									<html:optionsCollection name="CommonForm"
										property="dynaForm(distList)" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>
				</div> --%>
				<div class="row">

					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label class="font-bold">From Date</label>
							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="fromDate" property="dynaForm(fromDate)"
									styleClass="form-control datepicker" />

							</div>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label class="font-bold">To Date</label>
							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i></span>
								<html:text styleId="toDate" property="dynaForm(toDate)"
									styleClass="form-control datepicker" />

							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 col-xs-12">
						<input type="submit" name="submit" value="Show Report"
							class="btn btn-success" onclick="return fnShowCases();" />
						<!-- <input
							type="submit" name="submit2" value="Show Department Wise"
							class="btn btn-success" onclick="return fnShowDeptWise();" /> <input
							type="submit" name="submit3" value="Show District Wise"
							class="btn btn-success" onclick="return fnShowDistWise();" /> -->
					</div>
				</div>
				<%-- </div>

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
			<div class="ibox-body"> --%>
			<br />
				<div class="row">
					<div class="col-md-12 col-xs-12">
						<div class="table-responsive">
							<logic:present name="secdeptwise">
								<table class="table table-striped table-bordered table-hover"
									id="example" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Sect.Department Code</th>
											<th>Department Name</th>
											<th>Total Cases</th>
											<th>Interim Orders Cases</th>
											<th>Interim Orders Issued</th>
											<th>Final Orders Cases</th>
											<th>Final Orders Issued</th>
										</tr>
									</thead>
									<tbody>
										<bean:define id="Totals" value="0"></bean:define>
										<bean:define id="interimOrdersCasesTot" value="0"></bean:define>
										<bean:define id="interimOrdersTot" value="0"></bean:define>
										<bean:define id="finalOrdersCasesTot" value="0"></bean:define>
										<bean:define id="finalOrdersTot" value="0"></bean:define>
										
										<logic:iterate id="map" name="secdeptwise" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.deptcode }</td>
												<td><a
													href="javascript:ShowHODWise('${map.deptcode}','${map.description }');">${map.description }</a></td>
												<td style="text-align: right;">${map.total_cases }</td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','IO','SD');">${map.interim_order_cases }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','IO','SD');">${map.interim_orders }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','FO','SD');">${map.final_order_cases }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','FO','SD');">${map.final_orders }</a></td>

											</tr>
											<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
											
											<bean:define id="interimOrdersCasesTot" value="${interimOrdersCasesTot + map.interim_order_cases }"></bean:define>
											<bean:define id="interimOrdersTot" value="${interimOrdersTot + map.interim_orders }"></bean:define>
											<bean:define id="finalOrdersCasesTot" value="${finalOrdersCasesTot + map.final_order_cases }"></bean:define>
											<bean:define id="finalOrdersTot" value="${finalOrdersTot + map.final_orders }"></bean:define>
											
										</logic:iterate>
									</tbody>

									<tfoot>
										<tR>
											<td colspan="3">Totals</td>
											<td colspan="1" style="text-align: right;">${Totals }</td>
											<td colspan="1" style="text-align: right;">${interimOrdersCasesTot }</td>
											<td colspan="1" style="text-align: right;">${interimOrdersTot }</td>
											<td colspan="1" style="text-align: right;">${finalOrdersCasesTot }</td>
											<td colspan="1" style="text-align: right;">${finalOrdersTot }</td>
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
											<th>Interim Orders Cases</th>
											<th>Interim Orders Issued</th>
											<th>Final Orders Cases</th>
											<th>Final Orders Issued</th>
										</tr>
									</thead>
									<tbody>
										<bean:define id="Totals" value="0"></bean:define>
										<bean:define id="interimOrdersCasesTot" value="0"></bean:define>
										<bean:define id="interimOrdersTot" value="0"></bean:define>
										<bean:define id="finalOrdersCasesTot" value="0"></bean:define>
										<bean:define id="finalOrdersTot" value="0"></bean:define>
										
										<logic:iterate id="map" name="deptwise" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.deptcode }</td>
												<td>${map.description }</td>
												
												<td style="text-align: right;">${map.total_cases }</td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','IO','HOD');">${map.interim_order_cases }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','IO','HOD');">${map.interim_orders }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','FO','HOD');">${map.final_order_cases }</a></td>
												<td style="text-align: right;"><a
													href="javascript:showCasesWise('${map.deptcode}','${map.description }','FO','HOD');">${map.final_orders }</a></td>

											</tr>
											<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
											
											<bean:define id="interimOrdersCasesTot" value="${interimOrdersCasesTot + map.interim_order_cases }"></bean:define>
											<bean:define id="interimOrdersTot" value="${interimOrdersTot + map.interim_orders }"></bean:define>
											<bean:define id="finalOrdersCasesTot" value="${finalOrdersCasesTot + map.final_order_cases }"></bean:define>
											<bean:define id="finalOrdersTot" value="${finalOrdersTot + map.final_orders }"></bean:define>
											
										</logic:iterate>
									</tbody>

									<tfoot>
										<tR>
											<td colspan="3">Totals</td>
											<td colspan="1" style="text-align: right;">${Totals }</td>
											<td colspan="1" style="text-align: right;">${interimOrdersCasesTot }</td>
											<td colspan="1" style="text-align: right;">${interimOrdersTot }</td>
											<td colspan="1" style="text-align: right;">${finalOrdersCasesTot }</td>
											<td colspan="1" style="text-align: right;">${finalOrdersTot }</td>
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
										<%-- <td nowrap="nowrap">${map.globalorgname}<br />
												${map.fullname} - ${map.designation} <br />
												${map.mobile} - ${map.email}
											</td> --%>
										<td nowrap="nowrap"><logic:notEmpty name="map" property="date_of_filing">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty></td>

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
										<td nowrap="nowrap"><logic:notEmpty name="map" property="date_next_list">
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
<script>
	/* $('.datepicker').datepicker({
		uiLibrary : 'bootstrap4'
	}); */

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
	});

	function fnShowCases() {
		$("#mode").val("unspecified");
		$("#HCCaseStatusAbstract").submit();
	}

	function ShowHODWise(deptId, deptDesc) {
		/*$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseStatusAbstract").submit();*/
		$(location).attr("href", "./HCOrdersIssuedReport.do?mode=HODwisedetails&deptName="+deptDesc+"&deptId="+deptId+"&fromDate="+$("#fromDate").val()+"&toDate="+$("#toDate").val());
	}
	function showCasesWise(deptId, deptDesc, status, repType) {
		/*$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#mode").val("getCasesList");
		$("#HCCaseStatusAbstract").submit();*/
		$(location).attr("href", "./HCOrdersIssuedReport.do?mode=getCasesList&caseStatus="+status+"&repType="+repType+"&deptName="+deptDesc+"&deptId="+deptId+"&fromDate="+$("#fromDate").val()+"&toDate="+$("#toDate").val());
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
