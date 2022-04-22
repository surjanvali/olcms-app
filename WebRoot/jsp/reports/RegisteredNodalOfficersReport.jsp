<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- <link rel="stylesheet" href="https://cdn.datatables.net/1.11.4/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.2.2/css/buttons.dataTables.min.css">

<style>
body {
	overflow-y: auto;
}
</style> -->
<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1>
	<ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="./Welcome.do"><i
				class="la la-home font-20"></i> </a></li>
		<li class="breadcrumb-item"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty></li>
	</ol>
</div> --%>
<div class="page-content fade-in-up">

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
				<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>

			</div>
			<logic:notEmpty name="SHOWNOTREGBTN">
				<span class="pull-right"> <input type="button" name="Submit"
					value="Not Registered Depts." class="btn btn-primary pull-right"
					onclick="getNotRegistered();" />
				</span>
			</logic:notEmpty>
		</div>
		<div class="ibox-body">

			<html:form method="post" action="/NodalOfficerAbstract"
				styleId="NodalOfficerReport">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />
				<div class="table-responsive">
					<logic:notEmpty name="DEPTWISENOS">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Sect. Department Code</th>
									<th>Sect. Department Name</th>
									<th>No. of Hods</th>
									<th>Nodal Officers Registered</th>
								</tr>
							</thead>
							<tbody>

								<bean:define id="hodsTotals" value="0"></bean:define>
								<bean:define id="noTotals" value="0"></bean:define>
								<logic:iterate id="map" name="DEPTWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode}</td>
										<td><a
											href="javascript:showOfficerWise('${map.deptcode}');">${map.deptname }</a>
										</td>
										<td style="text-align: right;">${map.hods}</td>
										<td style="text-align: right;">${map.nodalofficers }</td>
									</tr>
									<bean:define id="hodsTotals" value="${hodsTotals + map.hods }"></bean:define>
									<bean:define id="noTotals"
										value="${noTotals + map.nodalofficers }"></bean:define>
								</logic:iterate>

							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${hodsTotals }</td>
									<td colspan="1" style="text-align: right;">${noTotals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>
					<logic:notEmpty name="HODDEPTWISENOS">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>HOD Name</th>
									<th>Registered Count</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="regTotals" value="0"></bean:define>
								<logic:iterate id="map" name="HODDEPTWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><a
											href="javascript:showOfficerWise('${map.inserted_by}');">${map.dept_desc }</a>
										</td>
										<td style="text-align: right;">${map.registered_nodal_officers }</td>
									</tr>
									<bean:define id="regTotals"
										value="${regTotals + map.registered_nodal_officers }"></bean:define>
								</logic:iterate>

							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>

					<logic:notEmpty name="EMPWISENOS">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Employee Name</th>
									<th>Designation</th>
									<th>Mobile</th>
									<th>Email Id</th>
									<th>Aadhaar Number</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="EMPWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.dept_code }</td>
										<td>${map.description }</td>
										<td>${map.fullname_en }</td>
										<td>${map.designation_name_en }</td>
										<td>${map.mobileno }</td>
										<td>${map.emailid }</td>
										<td>${map.aadharno }</td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="8">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>


					<logic:notEmpty name="NONOTREGDATA">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="map" name="NONOTREGDATA" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.dept_code }</td>
										<td>${map.description }</td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>

				</div>
			</html:form>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showOfficerWise(deptId) {
		$("#deptId").val(deptId);
		$("#mode").val("getOfficerWise");
		$("#NodalOfficerReport").submit();
		//document.forms[0].mode.value="getOfficerWise";
		//document.forms[0].submit();
	}

	function showHODWise(deptId) {
		// alert(deptId);
		$("#deptId").val(deptId);
		$("#mode").val("getHODWiseReport");
		$("#NodalOfficerReport").submit();
		//document.forms[0].mode.value="getHODWiseReport";
		//document.forms[0].submit();
	}
	function getNotRegistered() {
		$("#mode").val("getNotRegistered");
		$("#NodalOfficerReport").submit();
	}
</script>
