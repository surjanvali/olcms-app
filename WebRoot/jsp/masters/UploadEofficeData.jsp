<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PAGE LEVEL STYLES
-->
<style type="text/css">
label {
	font-weight: bold;
}
</style>
<!--  <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'> -->

<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />

<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING"> ${HEADING } </logic:notEmpty>
	</h1>
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
			<div class="ibox-title" style="width: 100%;">
				Upload E-Office Data
			</div>
			<div class="ibox-tools">
				<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
				
			</div>
		</div>
		<div class="ibox-body">
			<html:form method="post" action="/UploadEofficeData"
				enctype="multipart/form-data">
				<html:hidden styleId="mode" property="mode" />
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label> District <bean:message key="mandatory" />
							</label>
							<html:select styleId="distId" property="dynaForm(distId)"
								styleClass="select2Class" style="width: 100%;">
								<html:option value="0">---SELECT---</html:option>
								<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
									<html:optionsCollection name="CommonForm"
										property="dynaForm(distList)" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
						<div class="form-group">
							<label> Upload File</label>
							<html:file styleId="changeLetter" styleClass="form-control"
								property="changeLetter" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

						<input type="button" name="Submit" value="Submit"
							class="btn btn-success pull-right" onclick="saveAck();" />
					</div>
				</div>
			</html:form>
		</div>
	</div>
</div>

<script src="https://apbudget.apcfss.in/js/select2.js"></script>
<script type="text/javascript">
	function saveAck() {
		document.forms[0].mode.value = "saveData";
		document.forms[0].submit();
	}

	$(document).ready(function() {
		$('.select2Class').select2();
	});

	function showAckEntry() {
		document.forms[0].mode.value = "displayAckForm";
		document.forms[0].submit();
	}

	function showAckList() {
		document.forms[0].mode.value = "getAcknowledementsList";
		document.forms[0].submit();
	}

	function editAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			document.forms[0].mode.value = "displayAckEditForm";
			document.forms[0].submit();
		}
	}
</script>
