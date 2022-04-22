<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%
	String atr = request.getAttribute("updateAtrb") != null
			? (String) request.getAttribute("updateAtrb")
			: null;
	if (atr == null)
		atr = "insert";
	//	String msgInWPform = (String) request.getAttribute("msgInWPform");
	//String auth = (String) session.getAttribute("canAccess");
	ArrayList otherAdmissions = request.getAttribute("otherAdmissions") != null
			? (ArrayList) request.getAttribute("otherAdmissions")
			: null;
	//out.println("otherAdmissions -- "+otherAdmissions);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->


<!-- Bootstrap -->
<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>css/style.css" rel="stylesheet" />
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet" />
<link href="css/bootstrap-datepicker.min.css" rel="stylesheet">

<link rel="stylesheet" href="css/font-awesome.min.css">

<link href="<%=basePath%>css/multiselect/jquery.multiselect.css"
	rel="stylesheet">
<style>
.case_heading {
	font-size: 17px;
	text-align: center;
	font-weight: bold;
	color: #641e16;
}

legend.scheduler-border-left {
	font-size: 1.2em !important;
	font-weight: bold !important;
	text-align: left !important;
	width: auto;
	padding: 0 10px;
	border-bottom: none;
	padding: 3px 65px;
	color: rgb(255, 255, 255);
	background: rgb(9, 105, 150);
	-webkit-clip-path: polygon(7% 0%, 100% 0%, 93% 100%, 0% 100%);
	clip-path: polygon(7% 0%, 100% 0%, 93% 100%, 0% 100%);
}

fieldset.scheduler-border {
	border: 1.5px groove rgb(234, 236, 238) !important;
	padding: 0 1.4em 1.4em 1.4em !important;
	margin: 20px 0px;
	-webkit-box-shadow: 0px 0px 0px 0px #000;
	box-shadow: 0px 0px 0px 0px #000;
}
</style>
</head>
<body>
	<html:form action="/Contempt" styleId="Contempt"
		enctype="multipart/form-data">
		<%-- <html:hidden property="property(action)" styleId="action" /> --%>
		<html:hidden property="action" styleId="action" />

		<%-- <html:hidden property="property(isCaseExist)" value="" />
			<html:hidden property="property(serialNo)" />
			<html:hidden property="caseid" value="" />
			<html:hidden property="property(status)" /> --%>

		<input type="hidden" name="property(mytempvar1)" />
		<input type="hidden" name="property(mytempvar2)" />

		<div class="container shadow-left-right"
			style="min-height:500px;background:#fff;">
			<div class="row">
				<div class="case_heading" style="margin-top:20px;">
					<span>CONTEMPT CASE - PARTICULARS ENTRY SCREEN</span>
				</div>
			</div>
			<logic:present name="msg">
				<div>
					<span style="align:center"><font size="4" color="green">${msg
							}</font> </span>
				</div>
			</logic:present>
			<logic:present name="msg1">
				<div>
					<span style="align:center"><font size="4" color="red">${msg1
							}</font> </span>
				</div>
			</logic:present>
			<fieldset class="scheduler-border">
				<legend class="scheduler-border-left">Case Particulars</legend>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1">Case ID<bean:message key="mandatory" />
							</label>

							<html:select property="property(caseId)"
								styleClass="form-control" styleId="caseId" style="width:100%;"
								onchange="getCaseDetails();">
								<logic:notEmpty property="property(caseList)"
									name="contemptform">
									<html:optionsCollection property="property(caseList)"
										name="contemptform" />
								</logic:notEmpty>
							</html:select>
						</div>
					</div>


					<logic:present name="caseIdDetails">
						<%-- <logic:iterate id=""></logic:iterate> --%>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Court Name : </label>
								<bean:define id="courtName" name="contemptform"></bean:define>
								<bean:write name="courtName" property="property(courtName)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Nature of Petition : </label>
								<bean:define id="caseType" name="contemptform"></bean:define>
								<bean:write name="caseType" property="property(caseType)"></bean:write>
							</div>
						</div>


						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Number : </label>
								<bean:define id="caseNo" name="contemptform"></bean:define>

								<bean:write name="caseNo" property="property(caseNo)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Year :</label>
								<bean:define id="caseYear" name="contemptform"></bean:define>
								<bean:write name="caseYear" property="property(caseYear)"></bean:write>


							</div>
						</div>


						<!-- <div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<input type="button" name="btn" value=" Next "
									onclick="showDetails();"
									class="btn btn-primary text-center pull-right" id="btn_show"
									style="margin-top: 24px; margin-bottom: -9px; margin-right: 11px;">
							</div>

						</div> -->
					</logic:present>
					<logic:present name="caseIdDetailsNotFound">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 " align="right">
							<br> <br /> <span style="align:right"><font size="2"
								color="red">${caseIdDetailsNotFound }</font> </span>
						</div>
					</logic:present>
				</div>
			</fieldset>
			<logic:present name="caseIdDetails">
				<fieldset class="scheduler-border">
					<legend class="scheduler-border-left">Contempt
						Information: </legend>



					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<input type="button" name="btn" value=" Back "
								onclick="hideDetails();"
								class="btn btn-info text-center pull-right" id="btn_hide"
								style="margin-top: -24px; margin-bottom: -9px; margin-right: 11px;">
						</div>

					</div>


					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Filed By</label>
								<bean:message key="mandatory" />
								<html:select property="property(filedBy)" styleId="filedBy"
									style="width: 525;" styleClass="form-control">
									<logic:notEmpty property="property(filedByList)"
										name="contemptform">
										<html:optionsCollection property="property(filedByList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>

						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Stage</label>
								<bean:message key="mandatory" />
								<html:select property="property(stage)" style="width: 525;"
									styleId="stage" styleClass="form-control">
									<logic:notEmpty property="property(stagesList)"
										name="contemptform">
										<html:optionsCollection property="property(stagesList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Date Of Filing</label>
								<bean:message key="mandatory" />
								<!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
								<div class="input-group date" data-date-format="dd/mm/yyyy">
									<html:text property="property(filingdate)" styleId="filingdate"
										styleClass="form-control" readonly="true"></html:text>
									<div class="input-group-addon">
										<span><i class="fa fa-calendar" aria-hidden="true"></i>
										</span>
									</div>
								</div>
							</div>
						</div>

						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Date Of Listing</label>
								<bean:message key="mandatory" />
								<!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
								<div class="input-group date" data-date-format="dd/mm/yyyy">
									<html:text property="property(listingdate)"
										styleId="listingdate" styleClass="form-control"
										readonly="true"></html:text>
									<div class="input-group-addon">
										<span><i class="fa fa-calendar" aria-hidden="true"></i>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
							<div class="form-group">
								<label for="sel1">Description Of Contempt</label>
								<bean:message key="mandatory" />
								<html:textarea property="property(appealDescription)" cols="60"
									styleClass="form-control" styleId="appealDescription"></html:textarea>
								<br />
								<div id="interimlDescriptionChars">Characters left: 1000</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Vakalat Filed On</label>
								<bean:message key="mandatory" />
								<!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
								<div class="input-group date" data-date-format="dd/mm/yyyy">
									<html:text property="property(vakalatFiledDate)"
										styleId="vakalatFiledDate" styleClass="form-control"
										readonly="true"></html:text>
									<div class="input-group-addon">
										<span><i class="fa fa-calendar" aria-hidden="true"></i>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1"> Upload scanned copy of Contempt </label>

								<logic:notEmpty property="property(documentUploaded)"
									name="contemptform">
									<html:hidden property="property(documentUploaded)"
										styleId="documentUploaded" name="contemptform" />
									<a
										href="<%=basePath%><bean:write property='property(documentUploaded)' name='contemptform'/>"
										target="_blank">View document uploaded earlier</a>
								</logic:notEmpty>
								<html:file property="property(document)" />
							</div>

						</div>
					</div>
					<div
						class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-right">
						<input type="button" name="btn" value=" Submit "
							onclick="submitContemptDetails();"
							class="btn btn-danger text-center" id="btn_submit">
					</div>
				</fieldset>
			</logic:present>
		</div>


		<script src="jQuery/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/script.js"></script>
		<script src="js/bootstrap-datepicker.min.js"></script>
		<script>
			$(document)
					.ready(
							function() {

								$('.input-group.date').datepicker({
									format : "dd/mm/yyyy"
								});

								$("#appealDescription")
										.keyup(
												function() {
													$(
															"#interimlDescriptionChars")
															.text(
																	"Characters left: "
																			+ (1000 - $(
																					this)
																					.val().length));
													if ($("#appealDescription")
															.val().length > 1000)
														$("#appealDescription")
																.val(
																		$(this)
																				.val()
																				.substr(
																						0,
																						1000));
												});
							});

			function getCaseDetails() {

				document.getElementById("action").value = "getCaseDetails";
				/* alert($("#action").val()); */
				document.forms[0].submit();
			}
			function showDetails() {
				$("#next_move").show();
				$("#btn_show").hide();

			}
			function hideDetails() {
				$("#btn_show").show();
				$("#next_move").hide();

			}

			function submitContemptDetails() {

				if (document.getElementById("filedBy").value == ""
						|| document.getElementById("filedBy").value == "0") {
					alert("Select filedBy");
					document.getElementById("filedBy").focus();
					return false;
				} else if (document.getElementById("stage").value == ""
						|| document.getElementById("stage").value == "0") {
					alert("Select Stage");
					document.getElementById("stage").focus();
					return false;
				} else if (document.getElementById("filingdate").value == ""
						|| document.getElementById("filingdate").value == "") {
					alert("Please Enter Date Of Filing ");
					document.getElementById("filingdate").focus();
					return false;
				} else if (document.getElementById("listingdate").value == "") {
					alert(" Please  Enter Date Of Listing ");
					document.getElementById("listingdate").focus();
					return false;
				} else if (document.getElementById("appealDescription").value == "") {
					alert("Enter Description More than 50 Charcters");
					document.getElementById("appealDescription").focus();
					return false;
				} else if (document.getElementById("vakalatFiledDate").value == "") {
					alert("Enter Vakalat filed Date");
					document.getElementById("vakalatFiledDate").focus();
					return false;
				}
				if (confirm("Do you want to save Contempt Details?")) {
					document.getElementById("action").value = "insertDetails";
					document.forms[0].submit();
				} else
					return false;
			}
		</script>
	</html:form>





</body>
</html>