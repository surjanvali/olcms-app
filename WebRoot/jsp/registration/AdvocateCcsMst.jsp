<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- PAGE LEVEL STYLES
-->
<style type="text/css" data-genuitec-lp-enabled="false"
	data-genuitec-file-id="wc1-501"
	data-genuitec-path="/apolcms/WebRoot/jsp/registration/GPOAck.jsp">
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


	<logic:notEmpty name="valExist"> ${valExist} </logic:notEmpty>
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
	<html:form action="/AddAdvocatecceDetails">
   	<html:hidden property="mode" value=""/>
		<div class="ibox">
						<div class="ibox-head">
							<div class="ibox-title" style="width: 100%;">
									Add Advocate Cce 
							</div>
							<div class="ibox-tools">
								<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
								<!-- <a
									class="dropdown-toggle" data-toggle="dropdown"><i
									class="fa fa-ellipsis-v"></i> </a>  <div class="dropdown-menu dropdown-menu-right">
									<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
										2</a>
								</div> -->
							</div>
						</div>
		

						<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> &nbsp;&nbsp;&nbsp;  Advocate Code <bean:message key="mandatory" />
									</label>
									&nbsp;&nbsp;&nbsp;<html:text styleId="advocateCode" styleClass="form-control" property="dynaForm(advocateCode)" maxlength="10" onkeypress="return isNumberKey(this)"/>
								</div>
							</div>
							
						</div>
						<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> &nbsp;&nbsp;&nbsp; Advocate Name <bean:message key="mandatory" />
									</label>
									&nbsp;&nbsp;&nbsp;<html:text styleId="advocateName" styleClass="form-control"
										property="dynaForm(advocateName)" maxlength="150" />
								</div>
							</div>
						</div>
						 
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<input type="button" name="Submit" value="Submit" class="btn btn-success pull-left" onclick="saveAck();" />
							</div>
						</div>
						
			
		</div>
	
	</html:form>
	</div>
						

<script src="https://apbudget.apcfss.in/js/select2.js"></script>

<script type="text/javascript">



	$( document ).ready(function() {
	
	   
	});


	function saveAck() {
		if ($("#advocateCode").val() == null || $("#advocateCode").val() == "" || $("#advocateCode").val() == "0") {
			alert("Advocate Code  Required");
			$("#advocateCode").focus();
			return false;
		}  else if ($("#advocateName").val() == null || $("#advocateName").val() == "" || $("#advocateName").val() == "0") {
			alert("Advocate Name Required");
			$("#advocateName").focus();
			return false;
		} else if (confirm("Do you want to Proceed and save Advocate cce details?")) {
			document.forms[0].mode.value = "saveDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}


	

	function isNumberKey(evt) {
		var charCode = (evt.which) ? evt.which : event.keyCode;
		if (charCode > 31 && (charCode < 48 || charCode > 57))
			return false;
		return true;
	}

	function addressType(i) {
		if (i.value.length > 0) {
			//i.value = i.value.replace(/[^\sA-Za-z0-9:.\/,\\]+/g, '');
			i.value = i.value.replace(/[^\sA-Za-z0-9,\/.\\:;#]+/g, '');
		}
	}

	function OnlyNumbersAndDot(evt) {
		var e = window.event || evt; // for trans-browser compatibility  
		var charCode = e.which || e.keyCode;
		if ((charCode > 45 && charCode < 58) || charCode == 8) {
			return true;
		}
		return false;
	}
	function trim(s) {
		return s.replace(/^\s*/, "").replace(/\s*$/, "");
	}

	function validateEmail(inputText) {
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (inputText.value.match(mailformat)) {
			return true;
		} else {
			alert("You have entered an invalid email address!");
			inputText.focus();
			return false;
		}
	}

</script>
