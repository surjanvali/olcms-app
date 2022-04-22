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


<!DOCTYPE html>
<html>
<head>

<script language="JavaScript" src="js/md5.js" type="text/javascript"></script>
<style type="text/css">

.m-t-80 {
 margin-top: 80px;
}

</style>


</head>


<body class="bg-transparent">
<html:form styleClass="form-horizontal" action="/Login">
	<input type="hidden" name="action" id="action" />
	<!-- HOME -->
	<section>
		<div class="container-alt">
			<div class="row">
				<div class="col-sm-12">

					<div class="wrapper-page" style="max-width: 600px;">

						<div class="m-t-40 account-pages">
							<div class="text-center account-logo-box ">
								<h3 class="font-bold m-b-0 " style="color: #ffffff;">
								<logic:equal value="DC" property="deptCode" name="Loginform">DISTRICT COLLECTOR/EXECUTIVE DIRECTOR  LOGIN</logic:equal>
								<logic:equal value="APCOS" property="deptCode" name="Loginform">APCOS OFFCIALS LOGIN</logic:equal>
								<logic:equal value="DEPT" property="deptCode" name="Loginform">DEPARTMENT/CLIENT ORGANIZATION LOGIN</logic:equal>
								</h3>
							</div>
							<div class="account-content">
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


								
									<div class="form-group ">
										<div class="col-xs-12">
											<html:text styleClass="form-control" property="username"
												styleId="username" maxlength="50" />
										</div>
									</div>

									<div class="form-group">
										<div class="col-xs-12">
											<html:password styleClass="form-control" property="password"
												styleId="password" maxlength="100" />
										</div>
									</div>

									
 
 								<div class="form-group account-btn text-center ">
										<div class="col-xs-12">
											<button class="btn w-md btn-bordered btn-success waves-effect waves-light" type="submit" onclick="return validateLogin();">Log In</button>
										</div>
									</div>
									
 								
 									<!-- <div class="form-group text-center m-t-80">
										
									</div> -->
									<div class="form-group m-t-80">
										<div class="col-sm-6" >
											<a href="<%=basePath%>forgetUserId.do?mode=unspecified&main=forgotPassword" class="text-muted pull-left"><i class="fa fa-pencil m-r-5"></i> Forgot Password?</a>
										</div>
										<div class="col-sm-6">
											<a href="<%=basePath%>forgetUserId.do" class="text-muted pull-right"><i class="fa fa-pencil m-r-5"></i> Register?</a>
										</div>
									</div>

									

								

								<div class="clearfix"></div>

							</div>



						</div>
						<!-- end card-box-->


					</div>
					<!-- end wrapper -->

				</div>
			</div>
		</div>
	</section>
	</html:form>
	<!-- END HOME -->

	<script>
		var resizefunc = [];
	</script>
 <script src="assets/js/jquery.min.js"></script>
	<!-- Validations Js -->
	<script src="js/CommonFunction.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#username").attr("placeholder", "User Name");
			$("#password").attr("placeholder", "Password");
		});

		function validateLogin() {
			if ($("#username").val() == "" || $("#username").val().length < 2) {
				$("#username").focus();
				swal("User Name Required.");
				return false;
			} else if ($("#password").val() == "" || $("#password").val().length < 4) {
				$("#password").focus();
				swal("Password Required.");
				return false;
			} else {
			
				$("#password").val(hex_md5($("#password").val()));
				$("#action").val("validateEmployeeLogin");
				//swal($("#action").val());
				return true;
			}
		}
	</script>
</body>
</html>