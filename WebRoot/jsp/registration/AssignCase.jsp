<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />
<style>
.btn-success {
	background-color: #40babc !important;
	border: 1px solid #40babc !important;
	border-radius: 0px;
}

.btn-success:hover {
	background-color: #40babc !important;
	border: 1px solid #40babc !important;
	border-radius: 0px;
}
</style>
<!-- <div class="content-wrapper">
	Main content
	<div class="content">
		<div class="bg-white"> -->
<div class="row">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 card">
		<html:form method="post" action="/HighCourtCasesList">
			<html:hidden styleId="mode" property="mode" />
			<html:hidden styleId="cINO" property="dynaForm(cINO)" />

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
			<!-- <div class="card-box"> -->

			<!-- <h4 class="m-t-0 header-title">
							<b>Assign Case to Employee</b>
						</h4>
						<hr /> -->
			<div class="row">

				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="col-sm-6 col-xs-12">
						<div class="form-group">
							<label>Department <bean:message key="mandatory" /></label>
							<html:select styleId="empDept" property="dynaForm(empDept)"
								styleClass="select2Class" onchange="populateDeptSecs();"
								style="width:150px;" value="${deptCode}">
								<html:option value="0">---SELECT---</html:option>
								<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
									<html:optionsCollection name="CommonForm"
										property="dynaForm(deptList)" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>
					<div class="col-sm-6 col-xs-12">
						<div class="form-group">
							<label>Section <bean:message key="mandatory" /></label>
							<html:select styleId="empSection" property="dynaForm(empSection)"
								styleClass="select2Class" onchange="populatePostDetails();"
								style="width:150px;">
								<html:option value="0">---SELECT---</html:option>
								<logic:notEmpty name="CommonForm"
									property="dynaForm(empSectionList)">
									<html:optionsCollection name="CommonForm"
										property="dynaForm(empSectionList)" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>
					<div class="col-sm-6 col-xs-12">
						<div class="form-group">
							<label>Post <bean:message key="mandatory" /></label>
							<html:select styleId="empPost" property="dynaForm(empPost)"
								styleClass="select2Class" onchange="populateEmpDetails();"
								style="width:150px;">
								<html:option value="0">---SELECT---</html:option>
							</html:select>
						</div>
					</div>
					<div class="col-sm-6 col-xs-12">
						<div class="form-group">
							<label>Employee Name <bean:message key="mandatory" /></label>
							<html:select styleId="employeeId" property="dynaForm(employeeId)"
								styleClass="select2Class" style="width:150px;">
								<html:option value="0">---SELECT---</html:option>
							</html:select>
						</div>
					</div>
					<div class="col-sm-12 col-xs-12">
						<div class="form-group pull-right">
							<button type="button" name="assign"
								class="btn btn-md btn-primary" onclick="assignCourtCase2();">
								<i class="glyphicon glyphicon-edit"></i> <span>Assign
									Case</span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</html:form>
	</div>
</div>
<!-- /.row -->
<!-- </div>
		/.container-fluid
	</div>
	/.content
</div> -->

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>

<script src="https://apbudget.apcfss.in/js/select2.js"></script>

<script type="text/javascript">
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	$(document).ready(function() {
		$(".select2Class").select2();
	});
	
	
	
	function populateDeptSecs() {
		$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		$("#empSection").select2('destroy');
		var data = {
			mode : "AjaxAction",
			empDept : $("#empDept").val(),
			getType : "getEmpDeptSectionsList"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#empSection").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#empSection").select2();
		$("#empSection").select2("val", "0");
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}

	function populatePostDetails() {
		$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		$("#empPost").select2('destroy');
		var data = {
			mode : "AjaxAction",
			empSec : $("#empSection").val(),
			empDept : $("#empDept").val(),
			getType : "getEmpPostsList"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#empPost").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#empPost").select2();
		$("#empPost").select2("val", "0");
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}

	function populateEmpDetails() {
		$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		$("#employeeId").select2('destroy');
		var data = {
			mode : "AjaxAction",
			empDept : $("#empDept").val(),
			empSec : $("#empSection").val(),
			empPost : $("#empPost").val(),
			getType : "getEmpsList"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				$("#employeeId").html(res);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});
		$("#employeeId").select2();
		$("#employeeId").select2("val", "0");
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}
	
	
	
	
	function assignCourtCase2() {
	var cINO = $("#cINO").val();
		// alert("AJAX SUBMIT:	"+cINO);
		if (cINO == null || cINO == "" || cINO == "0") {
			alert("Invalid data");
			return false;
		} 
		else if ($("#empDept").val() == null || $("#empDept").val() == "" || $("#empDept").val() == "0") {
			alert("Select Employee Department");
			$("#empDept").focus();
			return false;
		}
		else if ($("#empSection").val() == null || $("#empSection").val() == "" || $("#empSection").val() == "0") {
			alert("Select Employee Section");
			$("#empSection").focus();
			return false;
		} else if ($("#empPost").val() == null || $("#empPost").val() == "" || $("#empPost").val() == "0") {
			alert("Select Post");
			$("#empPost").focus();
			return false;
		} else if ($("#employeeId").val() == null || $("#employeeId").val() == "" || $("#employeeId").val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId").focus();
			return false;
		} else {
			document.forms[0].mode.value = "assignCase";
			document.forms[0].submit();
		}
	}
	
	
	function assignCourtCase() {
	var cINO = $("#cINO").val();
		// alert("AJAX SUBMIT:	"+cINO);
		if (cINO == null || cINO == "" || cINO == "0") {
			alert("Invalid data");
			return false;
		} 
		else if ($("#empDept").val() == null || $("#empDept").val() == "" || $("#empDept").val() == "0") {
			alert("Select Employee Department");
			$("#empDept").focus();
			return false;
		}
		else if ($("#empSection").val() == null || $("#empSection").val() == "" || $("#empSection").val() == "0") {
			alert("Select Employee Section");
			$("#empSection").focus();
			return false;
		} else if ($("#empPost").val() == null || $("#empPost").val() == "" || $("#empPost").val() == "0") {
			alert("Select Post");
			$("#empPost").focus();
			return false;
		} else if ($("#employeeId").val() == null || $("#employeeId").val() == "" || $("#employeeId").val() == "0") {
			alert("Select Employee to Assign");
			$("#employeeId").focus();
			return false;
		} else {
			//document.forms[0].mode.value = "assignCourtCaseToEmp";
			//document.forms[0].submit();
			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Saving your data. Please wait...');
			$("#ASSIGN").hide();
			var data = {
				mode : "assignCase2Employee",
				cino : cINO,
				emp_dept : $("#empDept").val(),
				emp_section : $("#empSection").val(),
				emp_post : $("#empPost").val(),
				emp_id : $("#employeeId").val()
			}
			$.post("HighCourtCasesList.do", data).done(function(res) {
				$("#MSG").html(res);

			}).fail(function(exc) {
				// $("#ASSIGN"+cINO).html(res);
				$("#ASSIGN").show();
				$("#MSG").html(res);
				// alert("Error Occured.Please Try Again.");
			});
			$("#MSG").focus();
			setTimeout(function() {
				$("#LOADINGPAGEGIF").html("");
			}, 900);
		}
	}
</script>