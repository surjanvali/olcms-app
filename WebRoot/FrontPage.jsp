<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>APOLCMS</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/style.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<script src="js/bootstrap.min.js"></script>

<style>
.bg_login {
	background-image: linear-gradient(rgba(29, 147, 202, 0.7),
		rgba(29, 147, 202, 0.7) ), url('images/bg_img.jpg');
	height: 400px;
}

.msg-board3 {
	background: #335288;
	min-height: 305px;
	margin-top: -20px;
}

h3.notice {
	color: #fff;
	margin-top: 15px;
	padding: 18px;
	font-size: 20px;
	font-weight: 500;
	background: #335288;
}

.images-arrow img {
	padding: 5px;
	width: 30px;
	background: #07749c;
	border-radius: 50%;
	height: 30px;
	margin: 30px 20px 0px 0px;
}

.carousel-inner p {
	color: #fff;
}

.msg-board3 p {
	padding: 8px 18px 8px 18px;
}

.inner-box {
	padding: 5px;
	margin-bottom: 15px;
	background: #fff;
	box-shadow: 0 2px 18px 1px rgb(61 61 61/ 10%);
	border-radius: 5px;
}

.inner-box a {
	text-decoration: none;
}

.inner-box h4 {
	line-height: 22px;
	height: auto;
	overflow: hidden;
	font-size: 19px;
	font-weight: 500;
	text-align: left;
	text-transform: capitalize;
	margin-top: 16px;
}
</style>
</head>
<body>


	<div class="container">
		<div class="row bg_white">
			<div class="col-xs-12 col-sm-3 col-md-1 col-lg-1">
				<img src="images/aplogo.png" class="img-responsive aplogo" />
			</div>

			<div class="col-xs-12 col-sm-9 col-md-11 col-lg-11">
				<div class="title">
					<h3>Online Legal Case Monitoring System (APOLCMS)</h3>
					<h4>Government of Andhra Pradesh</h4>
				</div>
			</div>

		</div>

		<div class="row bg_login">
			<div class="col-xs-12 col-sm-12 col-md-8 col-lg-8">
				<div class="login_blk_right">
					<h3>Online Legal Case Monitoring System</h3>

					<p>AP Online Legal Case Monitoring System (APOLCMS) facilitates departments
						/ officers to manage and monitor court cases of any type pending
						in different courts. It can provide latest information about any
						pending case of any point of time. APOLCMS helps the
						departments/officers to track the cases, prepare cause-list well
						in advance, maintain complete history of the case including
						follow-up action taken part from other activities.</p>

					<p>APOLCMS can generate a number of MIS reports including query
						based reports based on given parametric values.</p>
				</div>
			</div>

			<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
				<div class="login_blk">
				<html:form method="POST" action="/Login" focus="username">
						<html:hidden property="mode" />
				<p style="color:red; text-align:center; font-size:16px;">Site is under development</p>
					<h3>Login</h3>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="User Name" />
					</div>

					<div class="form-group">
						<input type="password" class="form-control" placeholder="Password" />
					</div>

					<div class="form-group">
						<input type="submit" class="btn btn-success btn-block" id="login"
								onclick="return LoginChecking();" value="LOGIN">
					</div>

					<!-- <div class="form-group text-right">
							<a href="#" class="forget">Forgot Password ?</a>
						</div>
								 -->
					</html:form>
				</div>
			</div>
		</div>


		<div class="row message-box">

			<div
				class="col-xs-12 col-sm-12 col-md-2 col-lg-2 message-box-title  visible-md visible-lg"
				style="padding:0px;margin:0px;">
				<span>LATEST UPDATES</span>
			</div>

			<div class="col-xs-12 col-sm-12 col-md-10 col-lg-10 "
				style="padding:0px;margin:0px;">

				<div class="message-box-right">
					<marquee behavior="scroll" direction="left"
						onmouseover="this.stop();" onmouseout="this.start();">
						Welcome To ANDHRA PRADESH ONLINE LEGAL CASE MONITORING SYSTEMS </marquee>
				</div>

			</div>

		</div>






		<div class="row bg_white bg_item1"
			style="padding-top:20px; padding-bottom:20px;">
			<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/complain.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Case Filing</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/remarks.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Para Wise Remarks</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/affidavit.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Counter Affidavit</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/law.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Interlocutory</h4>
							</a>
						</div>
					</div>
				</div>

			</div>


			<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/contempt.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Contempt</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/law.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Judgement</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/appeal.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>Appeal</h4>
							</a>
						</div>
					</div>
				</div>


				<div class="inner-box">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-3">
							<img src="images/update.png" style="width:55px;">
						</div>
						<div class="col-md-9 col-sm-9 col-xs-9">
							<a href="#"><h4>MIS Reports</h4>
							</a>
						</div>
					</div>
				</div>

			</div>


			<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  inner-wrapper "
				style="padding:5px">
				<div class="msg-board3">
					<h3 class="notice">Notice Board</h3>

					<div id="text-carousel" class="carousel slide" data-ride="carousel"
						data-interval="8000">
						<div class="carousel-inner">
							<div class="item active">
								<div class="carousel-content">
									<p>APOLCMS Portal under development.</p>
								</div>
							</div>
							<div class="item">
								<div class="carousel-content">
									<p>The Portal will be available soon.</p>
								</div>
							</div>
						</div>
						<div class="images-arrow pull-right  prev">
							<a href="#text-carousel" data-slide="prev"><img
								src="images/arrow2.png" class="rotate_left next"> </a> <a
								href="#text-carousel" data-slide="next"><img
								src="images/arrow1.png"> </a>
						</div>
					</div>


				</div>
			</div>

		</div>






		<div class="row footer">
			<div class="col-x12 col-sm-12 col-md-12 col-lg-12">
				<p>
					Designed and Developed By <a href="">APCFSS</a>
				</p>
			</div>
		</div>

	</div>

</body>
</html>
