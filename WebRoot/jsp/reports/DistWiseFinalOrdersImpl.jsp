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
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />

</head>
<body>
	<html:form action="/HCFinalOrdersImplemented"
		styleId="HCOrdersIssuedReportId">
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
				<div class="ibox-title">District Wise Report</div>
			</div>
			<div class="ibox-body">

				<%-- <div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="form-group">
							<label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(reportType)" styleId="reportType" value="CC"
									onclick="changeReport();">
									<span class="input-span"></span>CC (Contempt Cases)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(reportType)" styleId="reportType"
									value="FOI" onclick="changeReport();">
									<span class="input-span"></span>Final Orders Implementation Report</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(reportType)" styleId="reportType"
									value="NEW" onclick="changeReport();">
									<span class="input-span"></span>Fresh Cases Report</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(reportType)" styleId="reportType"
									value="LEGACY" onclick="changeReport();">
									<span class="input-span"></span>Legacy Cases Report</html:radio>
							</label>
						</div>
					</div>
				</div> --%>
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label>Report Type </label>
							<html:select styleId="reportType" property="dynaForm(reportType)"
								styleClass="form-control select2Class">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="CC">CC (Contempt Cases)</html:option>
								<html:option value="FOI">Final Orders Implementation Report</html:option>
								<html:option value="NEW">Fresh Cases Report</html:option>
								<html:option value="LEGACY">Legacy Cases Report</html:option>
							</html:select>
						</div>
					</div>


				</div>
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
					</div>
				</div>
			</div>
		</div>
		<logic:present name="FINALORDERSREPORT">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">District Wise Final Orders Implementation Report</div>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>District</th>
									<th>Orders Issued</th>
									<th>Orders Implemented</th>
									<th>Appeal Filed</th>
									<th>Pending</th>
									<th>Action Taken</th>
								</tr>
							</thead>
							<tbody>

								<bean:define id="casescountTot" value="0"></bean:define>
								<bean:define id="order_implementedTot" value="0"></bean:define>
								<bean:define id="appeal_filedTot" value="0"></bean:define>
								<bean:define id="pendingTot" value="0"></bean:define>
								<bean:define id="actionTakenTot" value="0"></bean:define>

								<logic:iterate id="map" name="FINALORDERSREPORT" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td>${map.district_name}</td>
										<td style="text-align: right;">${map.casescount}</td>
										<td style="text-align: right;">${map.order_implemented }</td>
										<td style="text-align: right;">${map.appeal_filed}</td>
										<td style="text-align: right;">${map.pending }</td>
										<td style="text-align: right;">${map.actoin_taken_percent }</td>
									</tr>
									<bean:define id="casescountTot"
										value="${map.casescount + casescountTot}"></bean:define>
									<bean:define id="order_implementedTot"
										value="${map.order_implemented + order_implementedTot}"></bean:define>
									<bean:define id="appeal_filedTot"
										value="${map.appeal_filed + appeal_filedTot}"></bean:define>
									<bean:define id="pendingTot"
										value="${map.pending + pendingTot}"></bean:define>
										<bean:define id="actionTakenTot"
										value="${map.actoin_taken_percent + actionTakenTot}"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${casescountTot }</td>
									<td colspan="1" style="text-align: right;">${order_implementedTot }</td>
									<td colspan="1" style="text-align: right;">${appeal_filedTot }</td>
									<td colspan="1" style="text-align: right;">${pendingTot }</td>
									<td colspan="1" style="text-align: right;">${actionTakenTot}</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</logic:present>
		<logic:present name="CCCASESREPORT">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">District Wise Contempt Cases Report</div>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>District</th>
									<th>Cases Count</th>
									<th>Counters Filed</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="casescountTot" value="0"></bean:define>
								<bean:define id="counterscountTot" value="0"></bean:define>
								<logic:iterate id="map" name="CCCASESREPORT" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td>${map.district_name}</td>
										<td style="text-align: right;">${map.casescount}</td>
										<td style="text-align: right;">${map.counterscount }</td>
									</tr>
									<bean:define id="casescountTot"
										value="${map.casescount + casescountTot}"></bean:define>
									<bean:define id="counterscountTot"
										value="${map.counterscount + counterscountTot}"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${casescountTot }</td>
									<td colspan="1" style="text-align: right;">${counterscountTot }</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</logic:present>
		<logic:present name="FRESHCASESREPORT">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">District Wise New Cases Report</div>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>District</th>
									<th>Cases Count</th>
									<th>Counters Filed</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="casescountTot" value="0"></bean:define>
								<bean:define id="counterscountTot" value="0"></bean:define>
								<logic:iterate id="map" name="FRESHCASESREPORT" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td>${map.district_name}</td>
										<td style="text-align: right;">${map.casescount}</td>
										<td style="text-align: right;">${map.counterscount }</td>
									</tr>
									<bean:define id="casescountTot"
										value="${map.casescount + casescountTot}"></bean:define>
									<bean:define id="counterscountTot"
										value="${map.counterscount + counterscountTot}"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${casescountTot }</td>
									<td colspan="1" style="text-align: right;">${counterscountTot }</td>
								</tR>
							</tfoot>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</logic:present>
		<logic:present name="LEGACYCASESREPORT">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">District Wise Legacy Cases Report</div>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>District</th>
									<th>Cases Count</th>
									<th>Counters Filed</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="casescountTot" value="0"></bean:define>
								<bean:define id="counterscountTot" value="0"></bean:define>
								<logic:iterate id="map" name="LEGACYCASESREPORT" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td>${map.district_name}</td>
										<td style="text-align: right;">${map.casescount}</td>
										<td style="text-align: right;">${map.counterscount }</td>
										<bean:define id="casescountTot"
											value="${map.casescount + casescountTot}"></bean:define>
										<bean:define id="counterscountTot"
											value="${map.counterscount + counterscountTot}"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${casescountTot }</td>
									<td colspan="1" style="text-align: right;">${counterscountTot }</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</logic:present>
	</html:form>
	<script type="text/javascript"
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script
		src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
	<script
		src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

	<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
		type="text/javascript"></script>
	<!-- <script src="https://apbudget.apcfss.in/js/select2.js"></script> -->
	<script>
		$('.datepicker').datepicker({
			uiLibrary : 'bootstrap4'
		});

		$(document).ready(function() {
			$(".select2Class").select2();
			$('.input-group.date').datepicker({
				format : "dd-mm-yyyy"
			});
		});

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