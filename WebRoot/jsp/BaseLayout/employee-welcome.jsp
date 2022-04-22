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
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="A fully featured admin theme which can be used to build CRM, CMS, etc.">
<meta name="author" content="Coderthemes">

<link rel="shortcut icon" href="assets/images/favicon.ico">

<title>AP CORPORATION FOR OUTSOURCED SERVICES</title>


</head>


<body>

	<div class="row">
		<div class="col-sm-12">
			<div class="page-title-box">
				<div class="btn-group pull-right">
					<ol class="breadcrumb hide-phone p-0 m-0">
						<li><a href="#">Home</a>
						</li>
					</ol>
				</div>
				<h4 class="page-title">
					<%
					if (request.getSession().getAttribute("username") != null) {
						
						out.print(request.getSession().getAttribute("rolename")
								.toString() +" " +request.getSession().getAttribute("username")
								.toString());
					}
				%>
				</h4>
				
				
				
			</div>
		</div>
	</div>
	<div class="alert alert-icon alert-warning alert-dismissible fade in" style="color:maroon;" role="alert">
					<h4>
						<a href="downloads/APCOS User Manual for Timely Payment Process.pdf" target="_new"> Click here,</a> to download User guide for Attendance Submission 
						<br/> First, Submit the DDO Code, Head of Account (HOA) and map to candidates in respective screen (i.e. Payments >> Submit DDO Code & HOA)
					</h4>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div
								class="alert alert-danger col-xs-12 col-sm-12 col-md-12 col-lg-12"
								role="alert">
								<b>Instructions:</b><br />
								Before entering the attendance for placed
									candidates, Please ensure the correctness of the
									following.
								<ul>
									<li>1) Monthly Remuneration of candidate as per
										sanctioned order</li>
									<li>2) Bank Account Details of the candidate</li>
								</ul>
								Please make necessary corrections in the above
									details in case of any disturbances.
									
									<br/><br/>
									The Candidate for whom Placement intimation letters generated and CFMS Beneficiary ID created only visible for Attendance Submission.
									
									
									<br/>
									<br/><br/>
									<font color="#000000">for clarifications regarding Attendance, please call to 8919303076</font>
							</div>
						</div>
					</div>
				</div>  
	<logic:equal value="saved" name="commonBean" property="dynactforms(action)">
	
		<div class="form-group account-btn text-center m-t-10">
			<div class="col-xs-12">
			
				<logic:notEmpty name="commonBean" property="dynactforms(success)">
					<bean:write name="commonBean" property="dynactforms(success)"/>
				</logic:notEmpty>
				
				<logic:notEmpty name="commonBean" property="dynactforms(failure)">
					<bean:write name="commonBean" property="dynactforms(failure)"/>
				</logic:notEmpty>
				
			</div>
		</div>
	
	</logic:equal>
	
	<!-- end page title end breadcrumb -->


	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-color panel-success">
				<!-- <div class="panel-heading">
					<h3 class="panel-title">Employee Details</h3>
					<p class="panel-sub-title font-13 text-muted">Details retrieved from Aadhaar (or) UIDAI services. </p>
				</div> -->
				<div class="panel-body">
					<logic:present name="otpRespData">
						<logic:notEmpty name="otpRespData">

							<form class="form-horizontal" role="form">
								<div class="form-group">
									<label class="col-md-6 control-label">Aadhaar No :</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="aadhaar_no" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">Name
										:</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="name" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">Date
										of Birth :</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="dob" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">Gender
										:</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="gender" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">e-Mail
										:</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="email" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">Mobile
										:</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="phoneNumber" />
									</div>
								</div>


								<div class="form-group">
									<label class="col-md-6 control-label" for="example-email">Address
										:</label>
									<div class="col-md-6">
										<bean:write name="otpRespData" property="co" />
										,
										<bean:write name="otpRespData" property="house" />
										,
										<bean:write name="otpRespData" property="street" />
										,
										<bean:write name="otpRespData" property="landmark" />
										,
										<bean:write name="otpRespData" property="lc" />
										,
										<bean:write name="otpRespData" property="village_name" />
										,
										<bean:write name="otpRespData" property="mandal_name" />
										,
										<bean:write name="otpRespData" property="district_name" />
										,
										<bean:write name="otpRespData" property="statecode" />
										-
										<bean:write name="otpRespData" property="pincode" />
										.
									</div>
								</div>


							</form>


						</logic:notEmpty>

					</logic:present>
				</div>
			</div>
		</div>
	</div>



	<%-- <div class="row">
		<div class="col-sm-12">
			<div class="card-box">
				<h4 class="m-t-0 header-title">
					<b>Candidate Details</b>
				</h4>
				<!-- <p class="text-muted m-b-30 font-13">
                         Most common form control, text-based input fields. Includes support for all HTML5 types: <code>text</code>, <code>password</code>, <code>datetime</code>, <code>datetime-local</code>, <code>date</code>, <code>month</code>, <code>time</code>, <code>week</code>, <code>number</code>, <code>email</code>, <code>url</code>, <code>search</code>, <code>tel</code>, and <code>color</code>.
                     </p> -->
				
				<div class="row">
					<div class="col-md-12">
						
						<logic:present name="otpRespData">
							<logic:notEmpty name="otpRespData">
						
								<form class="form-horizontal" role="form">
									<div class="form-group">
										<label class="col-md-6 control-label">Aadhaar No :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="uid" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">Name :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="name" />
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">Date of Birth :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="dob" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">Gender :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="gender" />
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">e-Mail :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="email" />
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">Mobile :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="phoneNumber" />
										</div>
									</div>
									
									
									<div class="form-group">
										<label class="col-md-6 control-label" for="example-email">Address :</label>
										<div class="col-md-6">
											<bean:write name="otpRespData" property="co" />, 
											<bean:write name="otpRespData" property="house" />, 
											<bean:write name="otpRespData" property="street" />, 
											<bean:write name="otpRespData" property="landmark" />, 
											<bean:write name="otpRespData" property="lc" />, 
											<bean:write name="otpRespData" property="village_name" />, 
											<bean:write name="otpRespData" property="mandal_name" />, 
											<bean:write name="otpRespData" property="district_name" />, 
											<bean:write name="otpRespData" property="statecode" /> - <bean:write name="otpRespData" property="pincode" />.
										</div>
									</div>
									
		
								</form>
						
							
							</logic:notEmpty>
						
						</logic:present>
						
						
					</div>
				</div>
			</div>
		</div>
	</div> --%>

</body>
</html>
