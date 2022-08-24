<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />

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
				<logic:notEmpty name="HEADING"> ${HEADING } </logic:notEmpty>
			</div>
		</div>
		<div class="ibox-body">
			<html:form method="post" action="/LoginStatusReport"
				styleId="MLOAbstractForm">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />
				<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="form-group">
							<label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="MLO" onclick="changeReport();">
									<span class="input-span"></span>MLO (Legal)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="MLOS" onclick="changeReport();">
									<span class="input-span"></span>MLO (Subject)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="NO" onclick="changeReport();">
									<span class="input-span"></span>Nodal Officers (Legal)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="GP" onclick="changeReport();">
									<span class="input-span"></span>Government Pleaders (GPs)</html:radio>
							</label>
						</div>
					</div>
				</div>


				<div class="table-responsive">
					<logic:notEmpty name="MLOWISEDATA">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>Sl.No</th>
									<!-- <th>Department Code</th> -->
									<th>Department</th>
									<th>Employee Name</th>
									<logic:present name="GPREPORT">
										<th>e-Mail</th>
										<th>Mobile No.</th>
									</logic:present>
									<th>First Login</th>
									<th>Logged-In Days</th>
									<th>Not Logged-In Days</th>
									<th>Last Login</th>
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="map" name="MLOWISEDATA" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<%-- <td>${map.dept_code }</td> --%>
										<td style="max-width: 350px;">${map.description }</td>
										<td>${map.user_description }</td>
										<logic:present name="GPREPORT">
											<td>${map.emailid }</td>
											<td>${map.mobile_no }</td>

										</logic:present>

										<td>${map.firstlogin }</td>
										<td style="text-align: right;">${map.loggedindays }</td>
										<td style="text-align: right;">${map.notlogedindays }</td>
										<td>${map.lastlogin }</td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<logic:present name="GPREPORT">
										<td colspan="9">&nbsp;</td>
									</logic:present>
									<logic:notPresent name="GPREPORT">
										<td colspan="7">&nbsp;</td>
									</logic:notPresent>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>
				</div>
			</html:form>
		</div>
	</div>
</div>
<script src="https://apbudget.apcfss.in/js/select2.js"></script>
<script>
	function changeReport() {
		$("#mode").val("unspecified");
		$("#MLOAbstractForm").submit();
	}
</script>
