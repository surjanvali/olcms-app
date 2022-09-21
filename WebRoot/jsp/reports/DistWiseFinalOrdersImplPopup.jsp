<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />

</head>
<body>
<div class="page-content fade-in-up">
	<html:form action="/HCFinalOrdersImplemented"
		styleId="HCOrdersIssuedReportId">
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
						style="width:100%;min-height:700px;;border:0px;"> </iframe>
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

	<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$('.datepicker').datepicker({
			uiLibrary : 'bootstrap4'
		});

		$(document).ready(function() {
			$(".select2Class").select2();
			$('.input-group.date').datepicker({
				format : "dd-mm-yyyy"
			});
		});
		</script>
		<script type="text/javascript">
		
		
		function fnShowCases() {
			var selectedOption = $("#reportType").val();
			
			alert("selectedOption:" + selectedOption);
			if (selectedOption == "FOI"){
				$("#mode").val("unspecified");
				$("#HCOrdersIssuedReportId").submit();
			}else if (selectedOption == "CC"){
				$("#mode").val("getCCCasesReport");
				$("#HCOrdersIssuedReportId").submit();
		}else if (selectedOption == "NEW"){
				$("#mode").val("getNewCasesReport");
				$("#HCOrdersIssuedReportId").submit();
		}else if (selectedOption == "LEGACY"){
				$("#mode").val("getLegacyCasesReport");
				$("#HCOrdersIssuedReportId").submit();
			
		}else{
				$("#mode").val("unspecified");
			$("#HCOrdersIssuedReportId").submit();
		}
		}
		
		function viewCaseDetailsPopup(cino) {
			var heading = "View Case Details for CINO : " + cino;
			var srclink = "";
			if (cino != null && cino != "" && cino != "0") {
				srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="+ cino;
				//alert("LINK:"+srclink);
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
</body>
</html>