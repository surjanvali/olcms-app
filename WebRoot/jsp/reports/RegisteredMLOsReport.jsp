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

			<html:form method="post" action="/MLOAbstract"
				styleId="MLOAbstractForm">

				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />

				<div class="table-responsive">

					<logic:notEmpty name="EMPWISENOS">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellpadding="0" cellspacing="0">
							<thead>
								<%-- <tr><th colspan="8"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty></th></tr> --%>
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

					<logic:notEmpty name="MLONOTREGDATA">
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
								<logic:iterate id="map" name="MLONOTREGDATA" indexId="i">
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
<script>
	function getNotRegistered() {
		$("#mode").val("getNotRegistered");
		$("#MLOAbstractForm").submit();
	}
</script>