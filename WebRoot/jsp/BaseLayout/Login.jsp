<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>APOLCMS</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" src="js/md5.js" type="text/javascript"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="assets/css/font-awesome.min.css">
<script type="text/javascript">
	function LoginChecking() {
		//alert("LoginChecking");
		if (document.forms[0].username.value == "") {
			alert("Please enter the username.");
			document.forms[0].username.focus();
			return false;
		} else if (document.forms[0].password.value == "") {
			alert("Please enter the password.");
			document.forms[0].password.focus();
			return false;
		} else {

			//document.getElementById("loadingbox").innerHTML = "<center><img src='images/ajax-loader.gif'/><br> <font color=white size=3 >Please Wait ..Checking UserId & Password .</font> <center> ";
			//document.getElementById("loginbox").style.display = "none";
			$("#password").val(hex_md5($("#password").val()));
			document.forms[0].mode.value = "validateLogin";
			//alert("" + document.forms[0].mode.value);
			return true;
			//document.forms[0].submit();
		}
	}
</script>
</head>
<body>

	<section class="top_menubar ">
		<div class="container-fluid menu_topbar navbar-fixed-top">
			<div class="container ">
				<div class="row ">
					<div class="col-xs-3 col-sm-3 col-md-1 col-lg-1">
						<img src="images/aplogo.png" class="img-responsive aplogo">
					</div>
					<div class="col-xs-9 col-sm-9 col-md-5 col-lg-5">
						<div class="title">
							<h4>
								ONLINE LEGAL CASE MANAGEMENT SYSTEM<br> <span>GOVERNMENT
									OF ANDHRA PRADESH</span>
							</h4>
						</div>
						<!-- 					 				  <div style="position: relative;top: 179px;left: -90px;"><img src="images/square.png" style="width:60px;"/></div>
                        -->
					</div>
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
						<!-- <nav class="navbar navbar2 ">
                        <div class="container-fluid">
                           
                        </div>
                     </nav> -->


						<nav class="navbar navbar-inverse">
							<div class="container-fluid">
								<div class="navbar-header">
									<button type="button" class="navbar-toggle"
										data-toggle="collapse" data-target="#myNavbar">
										<span class="icon-bar"></span> <span class="icon-bar"></span>
										<span class="icon-bar"></span>
									</button>
								</div>
								<div class="collapse navbar-collapse" id="myNavbar">
									<ul class="nav navbar-nav">
										<li><a href="./">Home</a></li>
										<li><a href="./">About Us</a></li>
										<li><a href="./">Contact Us</a></li>
										<li><a href="./privacypolicy.html">Privacy Policy</a></li>
									</ul>

								</div>
							</div>
						</nav>
					</div>
				</div>
			</div>
		</div>
		<div class="container pd70">
			<div class="col-xs-12 col-sm-12 col-md-7 col-lg-7">
				<div class="row">
					<h3 class="fonthead main_heading">
						Andhra Pradesh Online Legal<br>Case Management<br>System
						(APOLCMS)
					</h3>
					<div class="msg_board">
						<ul class="nav nav-tabs">
							<li class="active"><a data-toggle="tab" href="#tab1">Message
									Board</a></li>
						</ul>
						<div class="tab-content">
							<div id="tab1" class="tab-pane fade in active">
								<marquee width="97%" direction="left">
									<p>This is a sample scrolling text that has scrolls texts
										to right.</p>
								</marquee>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-12 col-md-5 col-lg-5">
				<div class="login_blk">
					<logic:notEmpty name="msg">
						<div class="alert alert-danger" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<i class="mdi mdi-block-helper"></i> <strong>${msg}</strong>
						</div>
					</logic:notEmpty>
					
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
					

					<html:form method="POST" action="/Login" focus="username">
						<html:hidden property="mode" />
						<h3 class="fonthead login_head">LOGIN</h3>
						<div class="form-group">
							<input type="text" name="username"
								class="form-control form-control2 border-0" id="username"
								size="20" maxlength="50" onblur="TextToUpper()"
								placeholder="Username" />
						</div>

						<div class="form-group">
							<input type="password" name="password"
								class="form-control form-control2 border-0" id="password"
								placeholder="Password" required autocomplete="off">
						</div>

						<div class="form-group">
							<!-- <a href="./AdvocateRegistation.do" class="btn btn-md btn-primary border-0">Advocate Registration</a> -->
							
							<input type="submit" class="btn btn-md btn-success border-0 pull-right"
								id="login" onclick="return LoginChecking();" value="LOGIN">
								
								
						</div>
					</html:form>
				</div>
			</div>
		</div>
	</section>
	<%-- <section class="top_menubar ">
		<div class="container-fluid menu_topbar navbar-fixed-top">
			<div class="container ">
				<div class="row ">
					<div class="col-xs-3 col-sm-3 col-md-1 col-lg-1">
						<img src="images/aplogo.png" class="img-responsive aplogo">
					</div>
					<div class="col-xs-9 col-sm-9 col-md-7 col-lg-7">
						<div class="title">
							<h4>
								ANDHRA PRADESH ONLINE LEGAL CASE Management SYSTEM<br>
								<span>GOVERNMENT OF ANDHRA PRADESH</span>
							</h4>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
						<!-- <nav class="navbar navbar2 ">
                        <div class="container-fluid">
                           
                        </div>
                     </nav> -->


						<nav class="navbar navbar-inverse">
							<div class="container-fluid">
								<!-- <div class="navbar-header">
									<button type="button" class="navbar-toggle"
										data-toggle="collapse" data-target="#myNavbar">
										<span class="icon-bar"></span> <span class="icon-bar"></span>
										<span class="icon-bar"></span>
									</button>
								</div> -->
								<div class="collapse navbar-collapse" id="myNavbar">
									<ul class="nav navbar-nav">
										<li><a href="#">Home</a></li>
										<li><a href="#">About Us</a></li>
										<li><a href="#">Contact Us</a></li>
									</ul>

								</div>
							</div>
						</nav>
					</div>
				</div>
			</div>
		</div>
		<div class="container pd70">
			<div class="col-xs-12 col-sm-12 col-md-7 col-lg-7">
				<div class="row">
					<h3 class="fonthead main_heading">
						Andhra Pradesh Online Legal<br>Case Management<br>System (APOLCMS)
					</h3>
					<div class="msg_board">
						<ul class="nav nav-tabs">
							<li class="active"><a data-toggle="tab" href="#tab1">Message
									Board</a></li>
						</ul>
						<div class="tab-content">
							<div id="tab1" class="tab-pane fade in active">
								<marquee width="97%" direction="left">
									<p>APOLCMS is Under Development.</p>
								</marquee>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-12 col-md-5 col-lg-5">
				<div class="login_blk">
					<html:form method="POST" action="/Login" focus="username">
						<html:hidden property="mode" />
						<h3 class="fonthead login_head">LOGIN</h3>
						<div class="form-group">
							<input type="text" name="username" class="form-control"
								id="username" size="20" maxlength="50" onblur="TextToUpper()"
								placeholder="Username" />
						</div>

						<div class="form-group">
							<input type="password" name="password" class="form-control"
								id="password" placeholder="Password" required autocomplete="off">
						</div>

						<div class="form-group">
							<input type="submit" class="btn btn-success btn-block" id="login"
								onclick="return LoginChecking();" value="LOGIN">
						</div>
					</html:form>
				</div>
			</div>
		</div>
	</section> --%>
	<section class="about_content">
		<div class="container">
			<div class="row">
				<div
					class="col-xs-12 col-sm-12 col-md-4 col-lg-4 col-sm-offset-1  inner-wrapper pd5 ">
					<img src="images/law.png" class="img-responsive" />
				</div>
				<div class="col-sm-5">
					<div class="about-cont">
						<h1 class="fonthead main_head2">
							About Andhra Pradesh Online Legal<br> Case Management<br>
							System
						</h1>
						<p>AP Online Legal Case Management System (APOLCMS)
							facilitates departments / officers to manage and monitor court
							cases of any type pending in different courts. It can provide
							latest information about any pending case of any point of time.
							APOLCMS helps the departments/officers to track the cases,
							prepare cause-list well in advance, maintain complete history of
							the case including follow-up action taken part from other
							activities.</p>
						<p>APOLCMS can generate a number of MIS reports including
							query based reports based on given parametric values..</p>
					</div>
				</div>
			</div>
		</div>
	</section>
	<section class="icon_items">
		<div class="container">
			<div class="row">
				<div class="col-sm-5">
					<div class="col-xs-12 col-sm-12 copl-md-6 col-lg-6 pd5">
						<div class="icon_block">
							<img src="images/complaint.svg" class="img-responsive">
							<h3>CASE FILING</h3>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 copl-md-6 col-lg-6 pd5">
						<div class="icon_block icon_block2">
							<img src="images/judgement.svg" class="img-responsive">
							<h3>JUDGEMENT</h3>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 copl-md-6 col-lg-6 pd5">
						<div class="icon_block icon_block2">
							<img src="images/contempt.svg" class="img-responsive">
							<h3>CONTEMPT</h3>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 copl-md-6 col-lg-6 pd5">
						<div class="icon_block ">
							<img src="images/appeal.svg" class="img-responsive">
							<h3>APPEAL</h3>
						</div>
					</div>
				</div>
				<div class="col-sm-3">
					<h4 class="fonthead more">More</h4>
					<ul id="nav2">
						<li class=""><a href="#">Para Wise Remarks</a></li>
						<li><a href="#">Counter Affidavit</a></li>
						<li><a href="#">Interlocutory</a></li>
						<li><a href="#"> MIS Reports</a></li>
					</ul>
				</div>
				<div class="col-sm-3">
					<h4 class="fonthead more2">More</h4>
					<ul id="nav2">
						<li class=""><a href="#">ecourts</a></li>
						<li><a href="#"> AP High Court</a></li>
						<li><a href="#">Supreme Court Of India</a></li>
						<li><a href="#"> AP State Legal Service Authority </a></li>
					</ul>
				</div>
			</div>
		</div>
	</section>
	<section class="footer">
		<div class="container" style="padding-bottom:20px; padding-top:20px;">
			<footer id="footer">
				<div class="main-footer widgets-dark typo-light">
					<div class="container">
						<div class="row">
							<h3 style="color:#f0c8a4;" class="fonthead">Related Links</h3>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<ul class="footer_list">
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> ecourts</a></li>
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> AP High Court</a></li>
								</ul>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<ul class="footer_list">
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> Supreme Court Of India</a></li>
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> GOIR AP</a></li>
								</ul>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-4">
								<ul class="footer_list">
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> AP State Legal Service
											Authority</a></li>
									<li><a href="#" target="_blank"><i
											class="fa fa-angle-right"></i> Government Of AP Portal</a></li>
								</ul>
							</div>
						</div>
						<div class="row">
							<p style="color:#fff; text-align:center; padding-top:50px;">
								Designed and Developed by <span style="color:#ccb087;">APCFSS</span>
							</p>
						</div>
					</div>
				</div>
			</footer>
		</div>
	</section>
	<!-- <script src="jQuery/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script> -->

	<script type="text/javascript">
		function TextToUpper() {
			//alert("hello");		
			document.forms[0].username.value = document.forms[0].username.value
					.toUpperCase();
		}

		/* function CheckForm(formObj) {
			LoginChecking();
		} */

		$(function() {
			$("#username").attr("placeholder", "User ID");
		});
	</script>
</body>
</html>
