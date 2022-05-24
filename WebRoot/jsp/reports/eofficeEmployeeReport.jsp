<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
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


			<html:form method="post" action="/eofficeEmployeeReport"
				styleId="MLOAbstractForm">

				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />

				<logic:present name="DISPLAYFILTER">
					<div class="row">
						<div class="col-md-4 col-sm-6 col-xs-12 district">
							<div class="form-group">
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)" styleClass="form-control">
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
				</logic:present>
				<logic:notPresent name="DISPLAYFILTER">
					<html:hidden styleId="districtId" property="dynaForm(districtId)" />
				</logic:notPresent>

				<div class="table-responsive">

					<logic:notEmpty name="regData">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<%-- <tr><th colspan="8"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty></th></tr> --%>
								<tr>
									<th>Sl No.</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>No. of Employess</th>

								</tr>
							</thead>
							<tbody>
								<bean:define id="noofEmployees" value="0"></bean:define>
								<logic:iterate id="map" name="regData" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.code}</td>
										<td><a
											href="javascript:showOfficerWise('${map.global_org_name}');">${map.global_org_name }</a>
										</td>
										<td style="text-align: right;">${map.count}</td>


									</tr>
									<bean:define id="noofEmployees"
										value="${noofEmployees + map.count }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${noofEmployees }</td>

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
									<th>Employee Code</th>
									<th>Employee Id</th>
									<th>Employee Identity</th>
									<th>Global Org Id</th>
									<th>Global Org Name</th>
									<th>Fullname</th>
									<th>Designation Id</th>
									<th>Designation Name</th>
									<th>Post Name</th>
									<th>Org Unit Name</th>
									<th>Marking</th>
									<th>Mobile1</th>
									<th>Email</th>

									<th>Address Type</th>
									<th>Primary</th>
									<th>OU Head</th>




								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="EMPWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.employee_code }</td>
										<td>${map.employee_id }</td>
										<td>${map.employee_identity }</td>
										<td>${map.global_org_id }</td>
										<td>${map.global_org_name }</td>
										<td>${map.fullname_en }</td>
										<td>${map.designation_id }</td>
										<td>${map.designation_name_en }</td>
										<td>${map.post_name_en }</td>
										<td>${map.org_unit_name_en }</td>
										<td>${map.marking_abbr }</td>
										<td>${map.mobile1 }</td>
										<td>${map.email }</td>
										<td>${map.address_type }</td>
										<td>${map.is_primary }</td>
										<td>${map.is_ou_head }</td>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="17">&nbsp;</td>
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
	function getNotRegistered() {
		$("#mode").val("getNotRegistered");
		$("#MLOAbstractForm").submit();
	}
	function changeReport() {
		$("#mode").val("viewReport");
		$("#MLOAbstractForm").submit();
	}
	function showOfficerWise(deptId) {
		$("#deptId").val(deptId);
		// alert("DEPT ID :" + $("#deptId").val());
		$("#mode").val("getOfficerWise");
		$("#MLOAbstractForm").submit();
		//document.forms[0].mode.value="getOfficerWise";
		//document.forms[0].submit();
	}
</script>
