<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="jQuery/ui/jquery-1.7.1.js"></script>
<script type="text/javascript" src="jQuery/ui/jquery.ui.core.js"></script>
<script type="text/javascript">
	$(function() {
		function tally(selector) {
			$(selector).each(
					function() {
						var total = 0, column = $(this).siblings(selector)
								.andSelf().index(this);

						$("#example tbody tr").each(
								function() {
									total += parseFloat($(
											'.sum:eq(' + column + ')', this)
											.html()) || 0;

								});
						$(this).html(total);
					});
		}
		tally('td.total');
	});

	function ShowHODWise(deptId, deptDesc) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseDocsUploadAbstractNewId").submit();
	}

	function showCasesWise(deptId, deptDesc, respondenttype, status) {

		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#respondenttype").val(respondenttype);
		$("#mode").val("getCasesList");
		$("#HCCaseDocsUploadAbstractNewId").submit();
	}
	function showCasesWiseType(deptId, deptDesc,respondenttype, status) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#respondenttype").val(respondenttype);
		$("#caseStatus").val(status);
		$("#mode").val("getCasesList");
		$("#HCCaseDocsUploadAbstractNewId").submit();
	}

</script>
</head>
<body>
	<html:form action="/HCCaseDocsUploadAbstractNew"
		styleId="HCCaseDocsUploadAbstractNewId">
		<input type="hidden" id="mode" name="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(respondenttype)" styleId="respondenttype" />
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
					<logic:present name="details">
						<table class="table table-striped table-bordered table-hover"
							id="example" style="width:100%">
							<thead>
								<tr>
									<th>SL No</th>
									<th>Sect. Department Code</th>
									<th>Sect. Department Name</th>
									<th>Cases Count (as Respondent One)</th>
									<th>Cases Count (as  Other Respondent)</th>
									<!-- <th>Total</th> -->
									<th>Closed</th>
									<th>Counter filed</th>
									<th>Parawise Remarks Uploaded</th>
									<th>Parawise Remarks Approved by GP</th>
							</thead>
							<tbody>
							<!-- Here All is nothing but Secreteriat department............... -->
								<logic:notEmpty name="details">
									<logic:iterate name="details" id="map" indexId="j">
										<tr>
											<td align="center">${j+1}</td>
											<td align="center">${map.dept_code}</td>
											<td align="left"><a href="javascript:ShowHODWise('${map.dept_code}','${map.description }');">${map.description }</a></td>
											<td align="right"><a class="sum" href="javascript:showCasesWiseType('${map.dept_code}','${map.description }','1','ALL');">${map.cases_respondent_one}</a></td>
											<td align="right"><a class="sum" href="javascript:showCasesWiseType('${map.dept_code}','${map.description }','2','ALL');">${map.cases_respondent_other}</a></td>
											<td align="right"><a class="sum"   href="javascript:showCasesWise('${map.dept_code}','${map.description }','ALL','CLOSED');">${map.closed_cases}</a></td>
											<td align="right"><a class="sum"   href="javascript:showCasesWise('${map.dept_code}','${map.description }','ALL','COUNTERUPLOADED');">${map.counter_uploaded}</a></td>
											<td align="right"><a class="sum"  href="javascript:showCasesWise('${map.dept_code}','${map.description }','ALL','PWRUPLOADED');">${map.pwrcounter_uploaded}</a></td>
											<td align="right"><a class="sum"	href="javascript:showCasesWise('${map.dept_code}','${map.description }','ALL','GPCOUNTER');">${map.counter_approved_gp}</a></td>
										
										
										</tr>
										<bean:define id="cases_respondent_one" value="${cases_respondent_one+map.cases_respondent_one}"></bean:define>
										<bean:define id="cases_respondent_other" value="${cases_respondent_other+map.cases_respondent_other}"></bean:define>
										<bean:define id="closed_cases" value="${closed_cases+map.closed_cases}"></bean:define>
										<bean:define id="counter_uploaded" value="${counter_uploaded+map.counter_uploaded}"></bean:define>
										<bean:define id="pwrcounter_uploaded" value="${pwrcounter_uploaded+map.pwrcounter_uploaded}"></bean:define>
										<bean:define id="counter_approved_gp" value="${counter_approved_gp+map.counter_approved_gp}"></bean:define>
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="details">
									<tr>
										<td colspan="10" align="center">NO Data Found</td>
									</tr>
								</logic:empty>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="3">Total</td>
									<td align="right" class=""><bean:write  name="cases_respondent_one"></bean:write></td>
									<td align="right" class=""><bean:write  name="cases_respondent_other"></bean:write></td>
									<td align="right" class=""><bean:write  name="closed_cases"></bean:write></td>
									<td align="right" class=""><bean:write  name="counter_uploaded"></bean:write></td>
									<td align="right" class=""><bean:write  name="pwrcounter_uploaded"></bean:write></td>
									<td align="right" class=""><bean:write  name="counter_approved_gp"></bean:write></td>
									<!-- <td align="right" class="total"></td> -->
								</tr>
							</tfoot>
						</table>
					</logic:present>

						<!-- Here HOD is nothing but HOD Wise department............... -->

					<logic:present name="deptwise">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Cases Count (as Respondent One)</th>
									<th>Cases Count (as  Other Respondent)</th>
									<th>Closed</th>
									<th>Counter filed</th>
									<th>Parawise Remarks Uploaded</th>
									<th>Parawise Remarks Approved by GP</th>
								</tr>
							</thead>
							<tbody>
								<logic:notEmpty name="deptwise">
									<logic:iterate id="map" name="deptwise" indexId="i">
										<tr>
											<td>${i+1 }</td>
											<td>${map.dept_code }</td>
											<td>${map.description }</td>
											
											
											<td align="right"><a class="sum" href="javascript:showCasesWiseType('${map.dept_code}','${map.description }','1','HOD');">${map.total_resp1}</a></td>
											<td align="right"><a class="sum" href="javascript:showCasesWiseType('${map.dept_code}','${map.description }','2','HOD');">${map.total_resp_other}</a></td>
											<td style="text-align: right;"><a class="sum"
												href="javascript:showCasesWise('${map.dept_code}','${map.description }','HOD','CLOSED');">${map.closed_cases }</a></td>
											<td style="text-align: right;"><a class="sum"
												href="javascript:showCasesWise('${map.dept_code}','${map.description }','HOD','COUNTERUPLOADED');">${map.counter_uploaded }</a></td>
											<td style="text-align: right;"><a class="sum"
												href="javascript:showCasesWise('${map.dept_code}','${map.description }','HOD','PWRUPLOADED');">${map.pwrcounter_uploaded }</a></td>
											<td style="text-align: right;"><a class="sum"
												href="javascript:showCasesWise('${map.dept_code}','${map.description }','HOD','GPCOUNTER');">${map.counter_approved_gp }</a></td>
										</tr>
										
										<bean:define id="total_resp1" value="${total_resp1+map.total_resp1}"></bean:define>
										<bean:define id="total_resp_other" value="${total_resp_other+map.total_resp_other}"></bean:define>
										<bean:define id="closed_cases" value="${closed_cases+map.closed_cases}"></bean:define>
										<bean:define id="counter_uploaded" value="${counter_uploaded+map.counter_uploaded}"></bean:define>
										<bean:define id="pwrcounter_uploaded" value="${pwrcounter_uploaded+map.pwrcounter_uploaded}"></bean:define>
										<bean:define id="counter_approved_gp" value="${counter_approved_gp+map.counter_approved_gp}"></bean:define>

									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="deptwise">
									<tr>
										<td colspan="10" align="center">NO Data Found</td>
									</tr>
								</logic:empty>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="3">Total</td>
									<td align="right" class=""><bean:write  name="total_resp1"></bean:write></td>
									<td align="right" class=""><bean:write  name="total_resp_other"></bean:write></td>
									<td align="right" class=""><bean:write  name="closed_cases"></bean:write></td>
									<td align="right" class=""><bean:write  name="counter_uploaded"></bean:write></td>
									<td align="right" class=""><bean:write  name="pwrcounter_uploaded"></bean:write></td>
									<td align="right" class=""><bean:write  name="counter_approved_gp"></bean:write></td>
								


								</tr>
							</tfoot>
						</table>
					</logic:present>
					<logic:present name="CASESLIST">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Ack No</th>
									<th>Date</th>
									<th>District</th>
									<th>Case Type</th>
									<th>Main Case No.</th>
									<th>Departments/Respondents</th>
									<th>Advocate CC No</th>
									<th>Advocate Name</th>
									<th>Download/Print</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.ack_no}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" />
										</td>
										<td>${map.generated_date }</td>
										<td>${map.district_name}</td>
										<td>${map.case_full_name }</td>
										<td>${map.maincaseno}</td>
										<td>${map.dept_descs}</td>
										<td>${map.advocateccno}</td>
										<td>${map.advocatename}</td>
										<td style="text-align: center;" nowrap="nowrap"><logic:present
												name="map" property="ack_file_path">
												<a href="./${map.ack_file_path}" target="_new"
													title="Print Acknowledgement" class="btn btn-sm btn-info">
													<i class="fa fa-save"></i> <span>Acknowledgement</span>
												</a>
											</logic:present> <logic:present name="map" property="barcode_file_path">
												<a href="./${map.barcode_file_path}" target="_new"
													title="Print Barcode" class="btn btn-sm btn-info"> <i
													class="fa fa-save"></i> <span>Barcode</span>
												</a>
											</logic:present> <logic:present name="map" property="ack_no">
												<logic:notEqual value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
														target="_new" title="Print Barcode"
														class="btn btn-sm btn-info">
												</logic:notEqual>


												<logic:equal value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
														target="_new" title="Print Barcode"
														class="btn btn-sm btn-info">
												</logic:equal>

												<i class="fa fa-save"></i>
												<span>Scanned Affidavit</span>
												</a>
											</logic:present></td>
									</tr>

								</logic:iterate>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="20">&nbsp;</td>
								</tr>
							</tfoot>
						</table>
					</logic:present>
				</div>
			</div>
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
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>
<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js" type="text/javascript"></script>
<script type="text/javascript">
function viewCaseDetailsPopup(ack_no) {
	//alert("hai....");
	var heading = "View Case Details for ACK NO : " + ack_no;
	var srclink = "";
	if (ack_no != null && ack_no != "" && ack_no != "0") {
		srclink = "./HCNewCaseStatusAbstractReport.do?mode=getAckNo&SHOWPOPUP=SHOWPOPUP&cino="
				+ ack_no;
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
</body>
</html>