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
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<!-- <link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" /> -->

</head>
<body>
	<html:form action="/HCFinalOrdersImplementedForm"
		styleId="HCOrdersIssuedReportId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(caseStatus)" styleId="caseStatus" />
		<html:hidden property="dynaForm(fileCino)" styleId="fileCino" />
		<div class="container-fluid">
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
			<div class="ibox-body">

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover"
						id="example" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Sl.No</th>
								<th>CINo</th>

								<th>Petitioner</th>
								<th>Respondents</th>
								<th>Prayer</th>
								<th>Orders</th>
								<th style="width: 150px !important;">Action / Status</th>
							</tr>
						</thead>
						<tbody>
							<logic:notEmpty name="CASESLIST">
								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.cino}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />
										</td>

										<td>${map.pet_name}</td>
										<td>${map.res_name},${map.address}</td>

										<td>${map.prayer}</td>
										<td style="text-align: center;">${map.orderpaths }</td>
										<td style="min-width: 150px !important;color: navy;"><logic:notEqual
												value="-" name="map" property="counter_approved_gp">
												<b>${map.casestatus1 } <br />${map.casestatus2 }<br />
													${map.casestatus3 } <br />${map.casestatus4 }
												</b>
											</logic:notEqual> <logic:notEqual value="T" name="map"
												property="counter_approved_gp">
												<br />

												<button class="btn btn-sm btn-primary"
													onclick="caseStatusUpdate('${map.cino}');">Update
													Status</button>
											</logic:notEqual></td>
								</logic:iterate>
							</logic:notEmpty>
						</tbody>
						<tfoot>
							<tR>
								<td colspan="19">&nbsp;</td>
							</tR>
						</tfoot>
					</table>
				</div>

			</div>
		</logic:present>
</div>
	</html:form>

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
	<!-- <script
		src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

	<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
		type="text/javascript"></script> -->
	<!-- <script src="https://apbudget.apcfss.in/js/select2.js"></script> -->
	<script>
		/* $('.datepicker').datepicker({
			uiLibrary : 'bootstrap4'
		}); */

		$(document).ready(function() {
			/* $(".select2Class").select2();
			$('.input-group.date').datepicker({
				format : "dd-mm-yyyy"
			}); */
		});

		function caseStatusUpdate(fileCino) {
			$("#mode").val("caseStatusUpdate");
			// alert("MODE:"+$("#mode").val());
			$("#fileCino").val("" + fileCino);
			$("#AssignedCasesToSectionForm").submit();
		}

		function viewCaseDtls(fileCino) {
			$("#mode").val("getCino");
			$("#fileCino").val("" + fileCino);
			$("#HCOrdersIssuedReportId").submit();
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

		function fnShowCases() {

			// var selectedOption = $("input:radio[name=dynaForm(reportType)]:checked").val()
			var selectedOption = $("#reportType").val();
			//alert("selectedOption:" + selectedOption);
			if (selectedOption == "FOI")
				$("#mode").val("getFinalOrdersImplReport");
			else if (selectedOption == "CC")
				$("#mode").val("getCCCasesReport");
			else if (selectedOption == "NEW")
				$("#mode").val("getNewCasesReport");
			else if (selectedOption == "LEGACY")
				$("#mode").val("getLegacyCasesReport");
			else
				$("#mode").val("unspecified");
			$("#HCOrdersIssuedReportId").submit();
		}
	</script>
</body>
</html>