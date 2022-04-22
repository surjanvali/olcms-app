<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <!-- PLUGINS STYLES- ->
<link href="./assetsnew/vendors/DataTables/datatables.min.css" rel="stylesheet" />
THEME STYLES
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js" type="text/javascript"></script>
PAGE LEVEL STYLES
<style>
body {
	overflow-y: auto;
}
</style>
 -->

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<%-- <h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1> --%>
	<%-- 
	<ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="./Welcome.do"><i class="la la-home font-20"></i> </a> </li>
		<li class="breadcrumb-item"><logic:notEmpty name="HEADING"> ${HEADING } </logic:notEmpty></li>
	</ol> --%>
</div>
<div class="page-content fade-in-up">
	<html:form action="/UsersCreated">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(roleId)" styleId="roleId" />
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
			</div>
			<div class="ibox-body">
				<div class="table-responsive">
					<logic:notEmpty name="ROLEUSERS">

						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Role Name</th>
									<th>Registered Users</th>
								</tr>
							</thead>
							<tbody>

								<bean:define id="regTotals" value="0"></bean:define>
								<logic:iterate id="map" name="ROLEUSERS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>
											<%-- <a href="javascript:getRoleWiseUsers('${map.role_id}');">${map.role_name}</a> --%>
											<a
											href="./UsersCreated.do?mode=getRoleWiseUsers&roleId=${map.role_id}">${map.role_name}</a>
										</td>
										<td style="text-align: right;">${map.userscount }</td>
									</tr>
									<bean:define id="regTotals"
										value="${regTotals + map.userscount }"></bean:define>
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
					<logic:notEmpty name="USERSLIST">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>User Id</th>
									<th>User Name</th>
									<th>Created By</th>
									<th>Created On</th>
									<th>Mobile</th>
									<th>Email Id</th>
									<th>Aadhaar Number</th>
									<th>Send SMS Alert</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="USERSLIST" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.userid }</td>
										<td>${map.user_description }</td>
										<td>${map.created_by }</td>
										<td>${map.created_on }</td>
										<td>${map.mobileno }</td>
										<td>${map.emailid }</td>
										<td>${map.aadharno }</td>
										<td><logic:present name="userRoleType">
												<div id="SMSMSGDIV${i+1}"></div>
												<div id="SMSBTNDIV${i+1}">
													<button type="button" class="btn btn-sm btn-warning"
														onclick="sendSMS('${i+1}','${map.userid}','${userRoleType}')">
														Send SMS</button>
												</div>
											</logic:present></td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="9">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>
				</div>
			</div>
		</div>
	</html:form>
</div>

<script type="text/javascript">
	/* function getRoleWiseUsers(roleId) {
		$("#roleId").val("" + roleId);
		$("#mode").val("getRoleWiseUsers");
		alert($("#mode").val());
		// document.forms[0].mode.value = "getRoleWiseUsers";
		document.forms[0].submit();
	} */
	function sendSMS(adhar, uId, uType) {
		$("#SMSBTNDIV" + adhar).hide();
		var data = {
			mode : "sendCredentialsSMS",
			empUserId : uId,
			userType : uType
		}
		$.post("registerMLO.do", data).done(function(res) {

			$("#SMSMSGDIV" + adhar).html(res);
		}).fail(
				function(exc) {
					$("#SMSMSGDIV" + adhar).html(
							"<font color=red>Error : Invalid Data.</font>");
					alert("Error Occured.Please Try Again.");
				});
	}
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	/* $(document).ready(function() {

		if ($('#example')) {
			$('#example').dataTable({
				"pageLength" : 50,
				"buttons" : [ 'copy', 'excel', 'pdf', 'colvis' ]
			});
		}
		;
	}); */
</script>