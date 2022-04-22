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
<%
	String atr = (String) request.getAttribute("updateAtrb");
	String msgInPRform = (String) request.getAttribute("msgInPRform");
	if (atr == null)
		atr = "insert";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'ParawiseRemarks.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>css/style.css" rel="stylesheet" />
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet" />
<link href="css/bootstrap-datepicker.min.css" rel="stylesheet">

<script
	src="jQuery/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
<script src="js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" language="javascript" src="js/ajax.js"></script>
<script type="text/javascript" src="js/CommonFunction.js"></script>


<style type="text/css">
element.style {
	width: 300px;
}
.mb20{margin-bottom:20px;}

.headername {
	color: rgb(91, 78, 85);
	border-left-style: solid;
	border-bottom-width: 5px;
	border-color: rgb(255, 127, 56);
	border-width: 4px;
	font-size: 16px;
	margin-bottom: 20px;
	padding: 5px 10px 5px 5px;
	font-weight: normal;
	background-color: rgb(239, 239, 239);
}
</style>
<style>
.case_heading{
font-size: 17px;
text-align: center;
font-weight: bold;
color: #641e16;
margin-top:20px;
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
	background: rgb(9 105 150);
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
	<div class="container shadow-left-right"
		style="min-height:500px;background:#fff;">


		<html:form action="/ParaRemarks" enctype="multipart/form-data">
			<html:hidden property="action" styleId="action" />
			<html:hidden property="isCaseExist" value="" />

			<div class="case_heading">
					<span><bean:message key="ParawiseRemarks.caption" /></span>
				</div>
			<logic:present name="msg">
				<div>
					<span style="align:center"><font size="4" color="green">${msg
							}</font>
							
							<logic:notEmpty property="property(paramFile)" name="parawiseRemarksForm">
									<a href="<%=basePath%>uploads/<bean:write name="parawiseRemarksForm" property="property(paramFile)"/>" target="_blank">View & Download Parawise Remarks submitted</a>
									</logic:notEmpty>
					</span>
				</div>
			</logic:present>
			<logic:present name="msg1">
				<div>
					<span style="align:center"><font size="4" color="red">${msg1
							}</font>
					</span>
				</div>
			</logic:present>

			<fieldset class="scheduler-border">
				<legend class="scheduler-border-left">Case Particulars</legend>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-groupco">
							<label for="sel1">Court - Case ID<bean:message key="mandatory" />
							</label>
							<%-- <html:text property="property(caseId)" styleId="caseId" size="25"
								styleClass="form-control" maxlength="" style="width:90%;"
								onchange="getCaseDetails();" /> --%>
							
							<html:select property="property(caseId)"
									styleClass="form-control" styleId="caseId"
									style="width:100%;" onchange="getCaseDetails();" >
									<logic:notEmpty property="property(caseList)"
										name="parawiseRemarksForm">
										<html:optionsCollection property="property(caseList)"
											name="parawiseRemarksForm" />
									</logic:notEmpty>
								</html:select>
						</div>
					</div>


					<logic:present name="caseIdDetails">
						<%-- <logic:iterate id=""></logic:iterate> --%>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Court Name: </label>
								<bean:define id="courtName" name="parawiseRemarksForm"></bean:define>
								<bean:write name="courtName" property="property(courtName)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Nature of Petition : </label>
								<bean:define id="caseType" name="parawiseRemarksForm"></bean:define>
								<bean:write name="caseType" property="property(caseType)"></bean:write>
							</div>
						</div>


						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Number: </label>
								<bean:define id="caseNo" name="parawiseRemarksForm"></bean:define>
								<html:hidden property="property(caseNo1)" name="parawiseRemarksForm"/>
								<bean:write name="caseNo" property="property(caseNo)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Year:</label>
								<bean:define id="caseYear" name="parawiseRemarksForm"></bean:define>
								<bean:write name="caseYear" property="property(caseYear)"></bean:write>
								<html:hidden property="property(caseYear1)" name="parawiseRemarksForm"/>

							</div>
						</div>


						<!-- <div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<input type="button" name="btn" value="  Next "
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

			<%
				if (msgInPRform != null)
						out.println("<tr height=100><td colspan=4><font color=red><center><b>"
								+ msgInPRform + "</b></center></font></td></tr>");
			%>

<logic:present name="caseIdDetails">
			<div  id="next_move">

				<logic:notPresent name="msgInPRform" scope="request">

					<fieldset class="scheduler-border">
						<!-- <div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<input type="button" name="btn" value=" Back "
									onclick="hideDetails();"
									class="btn btn-primary text-center pull-right" id="btn_hide"
									style="margin-top: -24px; margin-bottom: -9px; margin-right: 11px;">
							</div>

						</div>
 -->
						<legend class="scheduler-border-left" style="margin-top:20px;">Parawise Remarks Details</legend>

						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
								<div class="form-group">
									<label for="sel1"> Corresponding GP's <bean:message key="mandatory" /></label>
									<html:select property="relatedGp" styleClass="form-control" styleId="relatedGp"
										onchange="checkCaseExists(this)">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty property="property(gps)"
											name="parawiseRemarksForm">
											<html:optionsCollection property="property(gps)"
												name="parawiseRemarksForm" />
										</logic:notEmpty>
									</html:select>

								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">
									<%-- <bean:message key="ParawiseRemarks.dtPRSentToGP" /> --%>
									Date of Submission of Parawise Remarks to GP/SC
									<bean:message key="mandatory" /> </label>
									<div class="input-group date" data-date-format="dd/mm/yyyy">
										<html:text property="property(dtPRSentToGP)" styleId="dtPRSentToGP"
											 styleClass="form-control" readonly="true"/>
										<div class="input-group-addon">
											<span><i class="fa fa-calendar" aria-hidden="true"></i>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">
									<%-- <bean:message key="ParawiseRemarks.dtPRSentToGP" /> --%>
									Date of Approval of Parawise Remarks by GP/SC
									<bean:message key="mandatory" /> </label>
									<div class="input-group date" data-date-format="dd/mm/yyyy">
										<html:text property="property(dtPRApprovedToGP)" styleId="dtPRApprovedToGP"
											styleClass="form-control" readonly="true"/>
										<div class="input-group-addon">
											<span><i class="fa fa-calendar" aria-hidden="true"></i>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">
									<%-- <bean:message key="ParawiseRemarks.dtPRSentToGP" /> --%>
									Date of Receipt of Approved Parawise Remarks from GP/SC
									<bean:message key="mandatory" /> </label>
									<div class="input-group date" data-date-format="dd/mm/yyyy">
										<html:text property="property(dtPRReceiptToGP)" styleId="dtPRReceiptToGP"
											 styleClass="form-control" readonly="true"/>
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
								<label for="sel1"> Upload Parawise Remarks Document </label>  
								
								<logic:notEmpty property="property(documentUploaded)" name="parawiseRemarksForm">
								<html:hidden property="property(documentUploaded)" styleId="documentUploaded" name="parawiseRemarksForm"/>
								<a href="<%=basePath%><bean:write property='property(documentUploaded)' name='parawiseRemarksForm'/>" target="_blank">View document uploaded earlier</a></logic:notEmpty>
								<html:file property="property(document)" />
							</div>
	
						</div>
					</div>
					</fieldset>

					<fieldset class="scheduler-border">
						<legend class="scheduler-border-left"> Remarks Details </legend>

						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
								<logic:present name="paraWiseRemarks">
									<logic:iterate id="remarks" name="paraWiseRemarks">

										<div class="headername">
											<label for="sel1"> User:<span style="color:green">${remarks.USERNAME}</span><br />
												Commented Date : <span style="color:red">${remarks.INSERTED_TIME}</span>
											</label> <br /> ${remarks.OFFICER_REMARKS}
										</div>

									</logic:iterate>
									
									<br/>
									<logic:notEmpty property="property(paramFile)" name="parawiseRemarksForm">
									<a href="<%=basePath%>uploads/<bean:write name="parawiseRemarksForm" property="property(paramFile)"/>" target="_blank">View & Download Parawise Remarks submitted</a>
									</logic:notEmpty>
								</logic:present>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
								<div class="form-group">
									<label for="sel1">Enter Parawise Remarks : </label>
									<script type="text/javascript" src="js/nicEdit-latest.js"></script>
									
									<html:textarea cols="300" styleId="paraRemarks"
										property="paraRemarks" style="width: 100%; height: 250px;">

									</html:textarea>
<script type="text/javascript">
										//         
										bkLib.onDomLoaded(function() {
											new nicEditor({
												fullPanel : true
											}).panelInstance("paraRemarks");
										});
										//
									</script>
									<!-- 	</div> -->
								</div>
							</div>
						</div>
					</fieldset>

				</logic:notPresent>

				<div
					class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mb20 pull-right">
					<%
						if (atr.equals("update")) {
					%>
					<html:submit property="submit" value=" Update "
						onclick="return validate()" styleClass="btn btn-primary text-center"
						styleId="btn_submit" />
					<html:reset styleClass="btn btn-primary text-center" />
					<%
						} else {
					%>
					<html:submit property="btn" value=" Submit " onclick="return validate()"
						styleClass="btn btn-success text-center" styleId="btn_submit" />
					<html:reset styleClass="btn btn-warning text-center" />
					<%
						}
					%>
				</div>
			</div>
			</logic:present>
		</html:form>

	</div>

	<script type="text/javascript">
		$('.input-group.date').datepicker({
			format : "dd/mm/yyyy"
		});

		function validate() {
		
			var remarks =  $(".nicEdit-main").html();
		
			if(document.getElementById("relatedGp").value=="" || document.getElementById("relatedGp").value=="0"){
				alert("Corresponding GP Required");
				document.getElementById("relatedGp").focus();
				return false;
			}
			else if(document.getElementById("dtPRSentToGP").value=="" || document.getElementById("dtPRSentToGP").value=="0"){
				alert("Date of Submission of Draft Parawise Remarks Required");
				document.getElementById("dtPRSentToGP").focus();
				return false;
			}
			
			//alert("nicEdit-main:::"+remarks);
			//alert("nicEdit-remarks:::"+remarks.length);
			//return false;
			 //else  if(document.getElementById("paraRemarks").value=="" || document.getElementById("paraRemarks").value=="0"){
			 else if ( remarks=="" || remarks=="<br>" || remarks.length < 50){
				alert("Parawise Remarks Required min. 50 characters");
				$(".nicEdit-main").focus();
				return false;
			}
			else{
				document.getElementById("action").value = "InsertDetails";
				return true;
			} 
			
		}

		function getCaseDetails() {
			document.getElementById("action").value = "getCaseDetails";
			//alert(document.getElementById("action").value );
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

		function checkCaseExists(sel) {

			if (document.forms[0].caseType.value == "") {
				alert("Please select Case Type")
				return false;
			} else if (document.forms[0].caseNo.value == "") {
				alert("Please select Case No")
				return false;
			} else if (document.forms[0].caseYear.value == "") {
				alert("Please select Case Year")
				return false;
			}
			document.forms[0].isCaseExist.value = "check";
			document.forms[0].submit();
		}
	</script>



</body>
</html>
