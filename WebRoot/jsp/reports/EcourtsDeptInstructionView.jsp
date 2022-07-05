<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js"
	type="text/javascript"></script>

<style>
body {
	overflow-y: auto;
}
</style>

<div class="page-content fade-in-up">
	<html:form action="/EcourtsDeptInstruction">
		<html:hidden styleId="mode" property="mode" />
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
<logic:notPresent name="existData">
<logic:empty name="existDataDeptWise">
		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b>Submit New Instruction</b>
					</h4>
				</div>
			</div>
			<div class="ibox-body">
				<html:hidden styleId="cino" property="dynaForm(cino)" />
				<div class="row">
					<div class="col-md-6 col-xs-12">
						<b> Instructions: </b>
					</div>
					<div class="col-md-6 col-xs-12">
						<html:textarea styleId="instructions"
							property="dynaForm(instructions)" styleClass="form-control"
							cols="50" rows="5">
						</html:textarea>
					</div>
				</div>
			</div>
			<div class="ibox-footer text-center">
				<div class="row">
					<div class="col-md-12 col-xs-12 text-center">
						<input type="submit" name="submit" value="Submit"
							class="btn btn-success" onclick="return fnSubmitCategory();" />
					</div>
				</div>
			</div>
		</div>
		</logic:empty>
		</logic:notPresent>

		<logic:present name="existData">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Instructions submitted</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Description</th>
										<th>Submitted On</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="existData" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.instructions }</td>
											<td>${map.insert_time}</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</logic:present>
		
		<logic:notEmpty name="existDataDeptWise">
				<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Submitted Instructions </div>
				</div>
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Department Code</th>
										<th>CIN no</th>
										<th>Total Instructions</th>

									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="existDataDeptWise" indexId="i">
										<tr>
											<td>${i+1}.</td>
											<td>${map.dept_code }</td>
											<td>${map.cino }</td>
											<td>${map.instructions}</td>

										</tr>

									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="14">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
				

			</logic:notEmpty>
		


	</html:form>
</div>

<script type="text/javascript">
	function fnSubmitCategory() {
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	}
</script>