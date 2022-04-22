<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<title>JSP to Change Password</title>
<style type="text/css">
.field-icon {
	float: right;
	margin-left: -25px;
	margin-top: -25px;
	position: relative;
	z-index: 2;
}

body {
	background-image: url('images/abstract-rectangles-bg2.jpg');
	background-repeat: no-repeat;
	background-size: cover;
	background-position: center;
}

.account-content {
	background-color: #fff;
}

/* blue bgt -*/
/*background-color: #4E9AF8; */
</style>
</head>
<body>
	<div class="page-content fade-in-up">

		<html:form action="/ChangePassword">
			<input type="hidden" name="mode" id="mode" />
			<div class="row">
				<div class="col-sm-12">

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

					<div class="ibox">
						<div class="ibox-head">
							<div class="ibox-title">Change Password</div>
							<div class="ibox-tools">
								<a class="ibox-collapse"><i class="fa fa-minus"></i> </a> <!-- <a
									class="dropdown-toggle" data-toggle="dropdown"><i
									class="fa fa-ellipsis-v"></i> </a>  <div class="dropdown-menu dropdown-menu-right">
					<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
						2</a>
				</div> -->
							</div>
						</div>
						<div class="ibox-body">
							<!-- <h4 class="m-t-0 header-title">
								<b>Change Password</b>
							</h4>
							<br> -->
							<div class="row">
								<div class="col-sm-12 col-xs-12 col-md-6">

									<div class="p-20">
										<div class="form-group">
											<label class="form-control-label">Current Password <span
												class="text-danger">*</span> </label>

											<html:password property="dynaProperties(CURRENTPSWD)"
												styleId="CURRENTPSWD" styleClass="form-control"
												maxlength="25" />
										</div>
										<div class="form-group">
											<label class="form-control-label">New Password <span
												class="text-danger">*</span> </label>

											<html:password property="dynaProperties(NEWPSWD)"
												styleId="NEWPSWD" styleClass="form-control input-md"
												maxlength="25" />

											<!-- <input id="password" name="password" type="password" placeholder="" class="form-control input-md" data-placement="bottom" data-toggle="popover" data-container="body" type="button" data-html="true"> -->
											<span toggle="#NEWPSWD"
												class="fa fa-fw fa-eye field-icon toggle-password"></span>
											<div id="popover-password">
												<p>
													Password Strength: <span id="result"> </span>
												</p>
												<div class="progress">
													<div id="password-strength"
														class="progress-bar progress-bar-success"
														role="progressbar" aria-valuenow="40" aria-valuemin="0"
														aria-valuemax="100" style="width:0%"></div>
												</div>
												<ul class="list-unstyled">
													<li class=""><span class="low-upper-case"><i
															class="fa fa-file-text" aria-hidden="true"></i> </span>&nbsp; 1
														lowercase &amp; 1 uppercase</li>
													<li class=""><span class="one-number"><i
															class="fa fa-file-text" aria-hidden="true"></i> </span> &nbsp;1
														number (0-9)</li>
													<li class=""><span class="one-special-char"><i
															class="fa fa-file-text" aria-hidden="true"></i> </span> &nbsp;1
														Special Character (!@#$%^&*).</li>
													<li class=""><span class="eight-character"><i
															class="fa fa-file-text" aria-hidden="true"></i> </span>&nbsp;
														Atleast 8 Character</li>
												</ul>
											</div>


										</div>
										<div class="form-group">
											<label class="form-control-label">Confirm Password <span
												class="text-danger">*</span> </label>
											<html:password property="dynaProperties(CONFIRMPSWD)"
												styleId="CONFIRMPSWD" styleClass="form-control"
												maxlength="25" />

											<html:hidden property="dynaProperties(CONFIRMPSWDTXT)"
												styleId="CONFIRMPSWDTXT" />
										</div>



										<div class="form-group pull-right">
											<button type="submit" id="submitBTN"
												class="btn btn-icon waves-effect waves-light btn-success m-b-5"
												onclick="return updatePassword();">
												<i class="fa fa-save"></i> Save
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</html:form>
	</div>
	<!-- jQuery  -->
	<script src="assets/js/jquery.min.js"></script>
	<!-- Validations Js -->
	<script src="js/CommonFunction.js"></script>
	<script src="js/md5.js" type="text/javascript"></script>

	<script type="text/javascript">
		$(document).ready(
				function() {
					$("#CURRENTPSWD").attr("placeholder", "Current Password");
					$("#NEWPSWD").attr("placeholder", "New Password");
					$("#CONFIRMPSWD").attr("placeholder", "Confirm Password");

					$("#NEWPSWD").keyup(function() {
						var password = $("#NEWPSWD").val();
						if (checkStrength(password) == false) {
							$("#submitBTN").attr("disabled", true);
						}
					});

					function checkStrength(password) {
						var strength = 0;

						//If password contains both lower and uppercase characters, increase strength value.
						if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) {
							strength += 1;
							$('.low-upper-case').addClass('text-success');
							$('.low-upper-case i').removeClass('fa-file-text')
									.addClass('fa-check');
							$('#popover-password-top').addClass('hide');

						} else {
							$('.low-upper-case').removeClass('text-success');
							$('.low-upper-case i').addClass('fa-file-text')
									.removeClass('fa-check');
							$('#popover-password-top').removeClass('hide');
						}

						//If it has numbers and characters, increase strength value.
						if (password.match(/([a-zA-Z])/)
								&& password.match(/([0-9])/)) {
							strength += 1;
							$('.one-number').addClass('text-success');
							$('.one-number i').removeClass('fa-file-text')
									.addClass('fa-check');
							$('#popover-password-top').addClass('hide');

						} else {
							$('.one-number').removeClass('text-success');
							$('.one-number i').addClass('fa-file-text')
									.removeClass('fa-check');
							$('#popover-password-top').removeClass('hide');
						}

						//If it has one special character, increase strength value.
						if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
							strength += 1;
							$('.one-special-char').addClass('text-success');
							$('.one-special-char i')
									.removeClass('fa-file-text').addClass(
											'fa-check');
							$('#popover-password-top').addClass('hide');

						} else {
							$('.one-special-char').removeClass('text-success');
							$('.one-special-char i').addClass('fa-file-text')
									.removeClass('fa-check');
							$('#popover-password-top').removeClass('hide');
						}

						if (password.length > 7) {
							strength += 1;
							$('.eight-character').addClass('text-success');
							$('.eight-character i').removeClass('fa-file-text')
									.addClass('fa-check');
							$('#popover-password-top').addClass('hide');

						} else {
							$('.eight-character').removeClass('text-success');
							$('.eight-character i').addClass('fa-file-text')
									.removeClass('fa-check');
							$('#popover-password-top').removeClass('hide');
						}

						// alert("strength:"+strength);

						// If value is less than 2

						if (strength < 2) {
							$('#result').removeClass()
							$('#password-strength').addClass(
									'progress-bar-danger');

							$('#result').addClass('text-danger').text(
									'Very Week');
							$('#password-strength').css('width', '10%');
						} else if (strength == 2) {
							$('#result').addClass('good');
							$('#password-strength').removeClass(
									'progress-bar-danger');
							$('#password-strength').addClass(
									'progress-bar-warning');
							$('#result').addClass('text-warning').text('Weak')
							$('#password-strength').css('width', '60%');
							return 'Weak'
						} else if (strength == 4) {
							$('#result').removeClass()
							$('#result').addClass('strong');
							$('#password-strength').removeClass(
									'progress-bar-warning');
							$('#password-strength').addClass(
									'progress-bar-success');
							$('#result').addClass('text-success')
									.text('Strong');
							$('#password-strength').css('width', '100%');

							return 'Strong'
						}

					}

					$(".toggle-password").click(function() {

						$(this).toggleClass("fa-eye fa-eye-slash");
						var input = $($(this).attr("toggle"));
						if (input.attr("type") == "password") {
							input.attr("type", "text");
						} else {
							input.attr("type", "password");
						}
					});

				});

		function updatePassword() {
			alert("validateLogin");
			if ($("#CURRENTPSWD").val() == ""
					|| $("#CURRENTPSWD").val().length < 4) {
				$("#CURRENTPSWD").focus();
				alert("Current Password Required.");
				return false;
			} else if ($("#NEWPSWD").val() == ""
					|| $("#NEWPSWD").val().length < 4) {
				$("#NEWPSWD").focus();
				alert("New Password Required.");
				return false;
			} else if ($("#CONFIRMPSWD").val() == ""
					|| $("#CONFIRMPSWD").val().length < 4) {
				$("#CONFIRMPSWD").focus();
				alert("Confirm Password Required.");
				return false;
			} else if ($("#CURRENTPSWD").val() == $("#NEWPSWD").val()) {
				$("#NEWPSWD").val("");
				$("#NEWPSWD").focus();
				alert("New Password is same as Current Password.");
				return false;
			} else if ($("#NEWPSWD").val() != $("#CONFIRMPSWD").val()) {
				$("#CONFIRMPSWD").val("");
				$("#CONFIRMPSWD").focus();
				alert("New Password & Confirm Password should match.");
				return false;
			} else {

				$("#CURRENTPSWD").val(hex_md5($("#CURRENTPSWD").val()));
				$("#NEWPSWD").val(hex_md5($("#NEWPSWD").val()));
				$("#CONFIRMPSWDTXT").val($("#CONFIRMPSWD").val());
				$("#CONFIRMPSWD").val(hex_md5($("#CONFIRMPSWD").val()));

				$("#mode").val("updatePassword");
				//swal($("#action").val());
				return true;
			}
		}
	</script>
</body>
</html>