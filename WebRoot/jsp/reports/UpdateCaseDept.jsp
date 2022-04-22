<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<div class="page-content fade-in-up">
	<html:form action="/UpdateCaseDept" styleId="UpdateCaseReportForm">
		<html:hidden styleId="mode" property="mode" />

		<html:hidden property="dynaForm(selectVal)" styleId="selectValId" />
		<html:hidden property="dynaForm(rowVal)" styleId="rowVal" />
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
					<h4 class="m-t-0 header-title">
						<b><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty> </b>
					</h4>
				</div>
			</div>
			<logic:present name="UpdateCase">
				<div class="ibox-body">
					<div class="table-responsive">
						<table id="example" cellpadding="0" cellspacing="0"
							class="table table-striped table-bordered" style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Name</th>
									<th>Cases Count</th>
									<th>Sect. Dept./HOD</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
							<bean:define id="regTotals" value="0"></bean:define>
								<logic:iterate id="map" name="UpdateCase" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.dept_name }</td>
										<td style="text-align: right;">${map.count }</td>
										<td><html:select styleId="dept_select${map.rowid }"
												property="dynaForm(dept_select${map.rowid })"
												value="${map.dept_code}"
												styleClass="form-control select2Class"
												style="max-width:300px;">
												<html:option value="0">---SELECT---</html:option>
												<html:optionsCollection property="dynaForm(dept_list)"
													name="CommonForm" />
											</html:select></td>
										<td><input type="button" name="btn" value="Save"
											onclick="updatecase('${map.rowid }');"
											class="btn btn-primary btn-md" /></td>
									</tr>
									
									<bean:define id="regTotals"
										value="${regTotals + map.count }"></bean:define>
									
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
									<td colspan="2">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</logic:present>
		</div>
	</html:form>
</div>
<!-- <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script> -->
<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".select2Class").select2();
	});

	function updatecase(deptName) {

		if (deptName == "" || $("#dept_select" + deptName + "").val() == "0") {
			alert("Select Dept.");
			$("#dept_select" + deptName + "").focus();
			return false;
		} else {
			$("#rowVal").val(deptName);
			$("#mode").val("submitdetails");
			$("#UpdateCaseReportForm").submit();
		}
	}
</script>