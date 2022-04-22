<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<script language="JavaScript" src="js/md5.js" type="text/javascript"></script>

</head>
<body>

	<div class="row">
		<div class="col-sm-12">
			<div class="page-title-box">
				<div class="btn-group pull-right">
					<ol class="breadcrumb hide-phone p-0 m-0">
						<li><a href="#">Home</a></li>
						<li class="active">Change Password</li>
					</ol>
				</div>
				<h4 class="page-title">
					Hello
					<%
					if (request.getSession().getAttribute("username") != null) {

						out.print(request.getSession().getAttribute("rolename")
								.toString()
								+ " "
								+ request.getSession().getAttribute("username")
										.toString());
					}
				%>
				</h4>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-sm-12 col-xs-12 col-md-8 col-lg-6">
			<div class="panel panel-color panel-teal">
				<div class="panel-heading">
					<h3 class="panel-title">Change Password</h3>
				</div>
				<div class="panel-body">

					<logic:present name="successMsg">
						<logic:notEmpty name="successMsg">
							<div
								class="alert alert-icon alert-success alert-dismissible fade in"
								role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<i class="mdi mdi-check-all"></i>
								<bean:write name="successMsg" />
							</div>
						</logic:notEmpty>
					</logic:present>

					<logic:present name="errorMsg">
						<logic:notEmpty name="errorMsg">

							<div
								class="alert alert-icon alert-danger alert-dismissible fade in"
								role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<i class="mdi mdi-block-helper"></i>
								<bean:write name="errorMsg" />
							</div>
						</logic:notEmpty>
					</logic:present>


					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">

							<html:form action="/ChangePassword" styleClass="form-horizontal">
								<input type="hidden" name="status" id="status" value="savePassword" />
								<html:hidden property="passwordText" styleId="passwordText" />

								<div class="form-group">
									<label
										class="col-xs-12 col-sm-12 col-md-6 col-lg-6 control-label">Old
										/ Present Password :</label>
									<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
										<html:password property="oldPassword" size="20"
											styleClass="inputBox form-control" />
										<html:errors property="oldPassword" />
									</div>
								</div>
								<div class="form-group">
									<label
										class="col-xs-12 col-sm-12 col-md-6 col-lg-6 control-label"
										for="postName">New Password :</label>
									<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
										<html:password property="newPassword" size="20"
											styleClass="inputBox form-control" />
										<html:errors property="newPassword" />
									</div>
								</div>
								<div class="form-group">
									<label
										class="col-xs-12 col-sm-12 col-md-6 col-lg-6 control-label"
										for="noOfPersonsReq">Retype New Password :</label>
									<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
										<html:password property="retypenewPassword" size="20"
											styleClass="inputBox form-control" />
										<html:errors property="newPassword" />
									</div>

								</div>

								<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<!-- <button type="submit" onclick="return getCandidatesList();"
										class="btn btn-success waves-effect waves-light pull-right">Get
										Candidates List</button> -->
									<div class="pull-right">
										<html:submit property="submit" styleClass="btn btn-success"
											style="margin-right:20px" onclick="return validateEqual();"
											value="Change Password" />
										&nbsp;&nbsp;&nbsp;&nbsp;
										<html:reset styleClass="btn btn-danger" />
									</div>
								</div>


							</html:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>






	<script src="assets/js/jquery.min.js"></script>
	<!-- Validations Js -->
	<script src="js/CommonFunction.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$(window).keydown(function(event) {
				if (event.keyCode == 13) {

					Login();
				}
			});
		});
		$("#password").attr("placeholder", "Password").val("").focus().blur();

		function validateEqual() {
			//alert("Ur in validateEqual Function in js");
			var i = 0;
			if (document.forms[0].oldPassword.value == "") {
				document.forms[0].oldPassword.focus();
				swal("Old / Present Password cannot be empty");
				return false;
			}
			else if (document.forms[0].newPassword.value == "") {
				document.forms[0].newPassword.focus();
				swal("New Password cannot be empty");
				return false;
			}
			else if (document.forms[0].oldPassword.value == document.forms[0].newPassword.value) {
				
				document.forms[0].newPassword.value = "";
				document.forms[0].retypenewPassword.value = "";
				document.forms[0].newPassword.focus();
				swal("Please Enter the New Password other than Old Password");
				return false;
			}
			else if (document.forms[0].retypenewPassword.value == "") {
				document.forms[0].retypenewPassword.focus();
				swal("Retype New Password cannot be empty");
				return false;
			}
			else if (document.forms[0].newPassword.value != document.forms[0].retypenewPassword.value) {
				document.forms[0].retypenewPassword.value = "";
				document.forms[0].retypenewPassword.focus();
				swal("New Password is not equal to Retype New Password");
				return false;
			}
			else{
				// alert("old pwd="+document.forms[0].oldPassword.value);
				// alert("old pwd="+document.forms[0].newPassword.value);
				// alert("Ur in validateEqual Function in js");
				document.forms[0].passwordText.value = document.forms[0].retypenewPassword.value;
				document.forms[0].oldPassword.value = hex_md5(document.forms[0].oldPassword.value);
				document.forms[0].newPassword.value = hex_md5(document.forms[0].newPassword.value);
				document.forms[0].retypenewPassword.value = hex_md5(document.forms[0].retypenewPassword.value);
				// document.forms[0].oldPassword.value
				// document.forms[0].submit(); 
				//swal("alert submit");
				return true;
			}
		}
	</script>
</body>
</html>