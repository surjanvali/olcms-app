<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" /> -->

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
			<html:form method="post" action="/OfficersRegistered" styleId="MLOAbstractForm">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />


				<div class="row">
					<div class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label> Designation/ Role <bean:message key="mandatory" />
							</label>
							<html:select styleId="officerType"
								property="dynaForm(officerType)" style="width : 100%;"
								styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="MLO">Middle Level Officer (Legal)</html:option>
								<html:option value="NO">Nodal Officer (Legal)</html:option>
								<html:option value="DNO">Nodal Officer (Legal - District Level)</html:option>
							</html:select>
						</div>

					</div>
					<div class="col-md-4 col-sm-6 col-xs-12 district">
						<div class="form-group">
							<label>District</label>
							<html:select styleId="districtId" property="dynaForm(districtId)"
								styleClass="form-control">
								<html:option value="0">---SELECT---</html:option>
								<logic:notEmpty name="CommonForm" property="dynaForm(DCLIST)">
									<html:optionsCollection name="CommonForm"
										property="dynaForm(DCLIST)" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>
					<div class="col-md-4 col-sm-6 col-xs-12">
						<button class="btn btn-md btn-success" onclick="changeReport();">Show
							Details</button>
					</div>
				</div>

				<%-- <div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="form-group">
							<label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="MLO">
									<span class="input-span"></span>MLO (Legal)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="NO">
									<span class="input-span"></span>Nodal Officers (Legal)</html:radio>
							</label> <label class="ui-radio ui-radio-inline"> <html:radio
									property="dynaForm(officerType)" styleId="officerType"
									value="DNO">
									<span class="input-span"></span>Nodal Officers (Legal - District Level)</html:radio>
							</label>
						</div>
					</div>
				</div> 
				<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<button class="btn btn-md btn-success pull-right"
							onclick="changeReport();">Show Details</button>
					</div>
				</div>
--%>


				<logic:notEmpty name="EMPWISEDATA">
					<hr />
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
				</logic:notEmpty>
			</html:form>
		</div>
	</div>
</div>
<!-- <script src="https://apbudget.apcfss.in/js/select2.js"></script> -->
<script>
	$(document).ready(function() {
		
		if ($("#officerType").val() == "DNO") {
			$(".district").show();
		} else {
			$(".district").hide();
		}
		
		$("#officerType").change(function() {
			if ($("#officerType").val() == "DNO") {
				$(".district").show();
			} else {
				$(".district").hide();
			}
		});
	});

	function changeReport() {
		$("#mode").val("unspecified");
		$("#MLOAbstractForm").submit();
	}
</script>