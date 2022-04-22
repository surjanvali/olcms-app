<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1>

</div> --%>
<div class="page-content fade-in-up">
	<html:form method="post" action="/DeptMaster"
		styleId="DeptWiseReportForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="dept" property="dynaForm(dept)" />
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

		<logic:present name="saveAction">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">Department Entry</div>
					<div class="ibox-tools">
						<a class="ibox-collapse"><i class="fa fa-minus"></i> </a> <a
							class="dropdown-toggle" data-toggle="dropdown"><i
							class="fa fa-ellipsis-v"></i> </a>
						<div class="dropdown-menu dropdown-menu-right">
							<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
								2</a>
						</div>
					</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Secr.Department code <bean:message
										key="mandatory" />
								</label>

								<logic:equal value="INSERT" name="saveAction">
									<html:text property="dynaForm(secr_code)" styleId="secr_code"
										styleClass="form-control"></html:text>
								</logic:equal>

								<logic:equal value="UPDATE" name="saveAction">
									<html:text property="dynaForm(secr_code)" styleId="secr_code"
										readonly="true" styleClass="form-control"></html:text>
								</logic:equal>
							</div>
							<div class="form-group">
								<label> Department Code<bean:message key="mandatory" />
								</label>
								<logic:equal value="INSERT" name="saveAction">
									<html:text property="dynaForm(dept_code)" styleId="dept_code"
										styleClass="form-control"></html:text>
								</logic:equal>

								<logic:equal value="UPDATE" name="saveAction">
									<html:text property="dynaForm(dept_code)" styleId="dept_code"
										readonly="true" styleClass="form-control"></html:text>
								</logic:equal>
							</div>
							<div class="form-group">
								<label> Description <bean:message key="mandatory" />
								</label>
								<html:text styleId="description" styleClass="form-control"
									property="dynaForm(description)" maxlength="150" />
							</div>

							<div class="form-group">
								<label> DDO Code </label>
								<html:text styleId="ddo_code" styleClass="form-control"
									property="dynaForm(ddo_code)" maxlength="12" />
							</div>
							<div class="form-group">
								<label>DDO Designation </label>
								<html:text styleId="ddo_desg" styleClass="form-control"
									property="dynaForm(ddo_desg)" maxlength="150" />
							</div>

							<div class="form-group">
								<input type="button" name="Submit" value="show List"
									class="btn btn-primary pull-right" onclick="showDeptList();" />
								&nbsp;&nbsp;
								<logic:equal value="INSERT" name="saveAction">
									<input type="button" name="Submit" value="Submit"
										class="btn btn-success pull-right" onclick="saveDept();" />
								</logic:equal>
								<logic:equal value="UPDATE" name="saveAction">
									<input type="button" name="Submit" value="Update"
										class="btn btn-success pull-right" onclick="updateDept();" />
								</logic:equal>
							</div>
						</div>
					</div>
				</div>
			</div>
		</logic:present>

		<logic:notEmpty name="deptdata">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title" style="width: 100%;">
						<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>

						<span class="pull-right"> <input type="button"
							name="Submit" value="Add New Dept"
							class="btn btn-primary pull-right" onclick="showDeptEntry();" />
						</span>

					</div>
					<!-- <div class="ibox-tools">
						<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
						<a
							class="dropdown-toggle" data-toggle="dropdown"><i
							class="fa fa-ellipsis-v"></i> </a>
						<div class="dropdown-menu dropdown-menu-right">
							<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
								2</a>
						</div>
					</div> -->
				</div>


				<div class="ibox-body">
					<!-- <div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<input type="button" name="Submit" value="Add New Dept"
								class="btn btn-primary pull-right" onclick="showDeptEntry();" />
						</div>
					</div>
					<hr /> -->
					<div class="table-responsive">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Sect Dept Code</th>
									<th>Department Code</th>
									<th>Description</th>
									<th>DDO Code</th>
									<th>DDO Designation</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="deptdata" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.sdeptcode}</td>
										<td>${map.deptcode}</td>
										<td>${map.description }</td>
										<td>${map.ddo_code}</td>
										<td>${map.ddo_desg}</td>

										<td style="text-align: center;" nowrap="nowrap">
											<button type="button" class="btn btn-sm btn-warning"
												title="Edit dept" onclick="editDept('${map.dept_id}')">
												<i class="ti-pencil-alt"></i>
												<!-- <span>Edit</span> -->
											</button>
											<button type="button" class="btn btn-sm btn-danger"
												title="Delete dept" onclick="deleteDept('${map.dept_id}')">
												<i class="ti-trash"></i>
												<!-- <span>Delete</span> -->
											</button>
										</td>
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
	</html:form>
</div>

<script type="text/javascript">
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function deleteDept(dept_id) {
		if (dept_id != null && dept_id != "") {
			$("#dept").val(dept_id);
			if (confirm("Do you want to Proceed and Delete Acknowledgement details?")) {
				$("#mode").val("deleteDept");
				$("#DeptWiseReportForm").submit();
			}
		}
	}

	function showDeptEntry() {
		$("#mode").val("displayForm");
		$("#DeptWiseReportForm").submit();
	}
	function showDeptList() {
		$("#mode").val("getDeptList");
		$("#DeptWiseReportForm").submit();
	}

	function editDept(dept_id) {
		if (dept_id != null && dept_id != "") {
			$("#dept").val(dept_id);
			$("#mode").val("editForm");
			$("#DeptWiseReportForm").submit();

		}
	}

	function updateDept() {
		if ($("#secr_code").val() == null || $("#secr_code").val() == ""
				|| $("#secr_code").val() == "0") {
			alert("Secretariat  Code  Required");
			$("#secr_code").focus();
			return false;
		} else if ($("#dept_code").val() == null || $("#dept_code").val() == ""
				|| $("#dept_code").val() == "0") {
			alert("Deptartment  Code  Required");
			$("#dept_code").focus();
			return false;
		} else if ($("#description").val() == null
				|| $("#description").val() == ""
				|| $("#description").val() == "0") {
			alert("Description is Required");
			$("#description").focus();
			return false;
		} /*else if ($("#ddo_code").val() == null || $("#ddo_code").val() == "" || $("#ddo_code").val() == "0") {
								alert("DDO Code  Required");
								$("#ddo_code").focus();
								return false;
							} else if ($("#ddo_desg").val() == null || $("#ddo_desg").val() == "" || $("#ddo_desg").val() == "0") {
								alert("DDO Designation Type Required");
								$("#ddo_desg").focus();
								return false;

							}*/
		else if (confirm("Do you want to Proceed and update Department details?")) {
			$("#mode").val("updateDetails");
			$("#DeptWiseReportForm").submit();
		} else
			return false;
	}

	function saveDept() {

		alert("saveDept");

		if ($("#secr_code").val() == null || $("#secr_code").val() == ""
				|| $("#secr_code").val() == "0") {
			alert("Secretariat  Code  Required");
			$("#secr_code").focus();
			return false;
		} else if ($("#dept_code").val() == null || $("#dept_code").val() == ""
				|| $("#dept_code").val() == "0") {
			alert("Deptartment  Code  Required");
			$("#dept_code").focus();
			return false;
		} else if ($("#description").val() == null
				|| $("#description").val() == ""
				|| $("#description").val() == "0") {
			alert("Description is Required");
			$("#description").focus();
			return false;
		} /*else if ($("#ddo_code").val() == null || $("#ddo_code").val() == "" || $("#ddo_code").val() == "0") {
								alert("DDO Code  Required");
								$("#ddo_code").focus();
								return false;
							} else if ($("#ddo_desg").val() == null || $("#ddo_desg").val() == "" || $("#ddo_desg").val() == "0") {
								alert("DDO Designation Type Required");
								$("#ddo_desg").focus();
								return false;
							} */
		else if (confirm("Do you want to Proceed and save Department details?")) {
			$("#mode").val("saveDetails");
			$("#DeptWiseReportForm").submit();
		} else
			return false;
	}
</script>