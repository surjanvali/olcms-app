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
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<%-- <h1 class="page-title">
		<logic:notEmpty name="HEADING">
					New Cases Abstract Report
				</logic:notEmpty>
	</h1> --%>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/DistrictNodalOfficerAbstactReport"
		styleId="acksAbstractFormId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(districtId)" styleId="districtId" />
		<html:hidden property="dynaForm(district_name)"
			styleId="district_name" />
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


			<logic:present name="DISTWISEACKS">
				<div class="ibox">
					<!-- <div class="ibox-head">
						<div class="ibox-title"></div>
					</div> -->
					<div class="ibox-body">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th colspan="3">${HEADING }</th>
									</tr>
									<tr>
										<th>Sl.No</th>
										<th>District</th>
										<th>Nodal Officers Registered</th>
									</tr>
								</thead>
								<tbody>
									<bean:define id="totTot" value="0"></bean:define>
									<logic:iterate id="map" name="DISTWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><a
												href="javascript:showDistCases('${map.distid}','${map.district_name }');">
													${map.district_name } </a></td>
											<td style="text-align: right;">${map.acks }</td>
										</tr>
										<bean:define id="totTot" value="${totTot + map.acks }"></bean:define>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="2" style="text-align: center;">Totals</td>
										<td style="text-align: right;">${totTot }</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</logic:present>

			<logic:notEmpty name="EMPWISEDATA">
				<div class="ibox">
					<div class="ibox-head">
						<div class="ibox-head">
							<div class="ibox-title">${HEADING }</div>
						</div>
					</div>
					<div class="ibox-body">
						<div class="table-responsive">
							<table class="table table-striped table-bordered table-hover"
								id="example" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Department Code</th>
										<th>Department</th>
										<th>Employee Name</th>
										<th>Designation</th>
										<th>Mobile No</th>
										<th>eMail</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="EMPWISEDATA" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.dept_id }</td>
											<td>${map.description }</td>
											<td>${map.fullname_en }</td>
											<td>${map.designation_name_en }</td>
											<td>${map.mobileno }</td>
											<td>${map.emailid }</td>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="7">&nbsp;</td>
									</tR>
								</tfoot>
							</table>

						</div>
					</div>
				</div>
			</logic:notEmpty>

		</div>
	</html:form>
</div>


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

	/* function fnShowDistWise() {
		$("#mode").val("showDistWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowDeptWise() {
		$("#mode").val("showDeptWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowCases() {
		$("#mode").val("showCaseWise");
		$("#acksAbstractFormId").submit();
		//return true;
	} */

	function showDistCases(distid, district_name) {
		$("#districtId").val(distid);
		$("#district_name").val(district_name);
		//$("#deptId").val($("#deptId").val());
		$("#mode").val("showCaseWise");
		//alert("mode:" +distid);
		//alert("districtId:" +$("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		$("#acksAbstractFormId").submit();
		// document.forms[0].submit(); */
		////-- $(location).attr( "href", "./DistrictNodalOfficerAbstactReport.do?mode=showCaseWise&districtId=" + distid + "&deptId=" + $("#deptId").val() + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());

	}
</script>