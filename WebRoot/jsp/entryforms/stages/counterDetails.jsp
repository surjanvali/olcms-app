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

<title>My JSP 'CounterDetails.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>css/style.css" rel="stylesheet" />
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet" />
<link href="<%=basePath%>css/bootstrap-datepicker.min.css" rel="stylesheet">
<script
	src="jQuery/jquery.min.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/script.js"></script>
<script src="<%=basePath%>js/bootstrap-datepicker.min.js"></script>

<style type="text/css">
 element.style{
 	width: 300px;
 }
 
.headername {
	color: rgb(91, 78, 85);
	border-left-style: solid;
	border-bottom-width: 5px;
	border-color: rgb(255, 127, 56);
	border-width: 4px;
	font-size: 16px;
	margin-bottom: 20px;
	padding : 5px 10px 5px 5px;
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

		<html:form action="/CounterAffidavit" enctype="multipart/form-data">
		<html:hidden property="action" styleId="action" />
		<html:hidden property="property(isCaseExist)" value="" />

			
			<div class="case_heading"  style="margin-top:20px;">
					<span><bean:message key="CounterAffidavit.caption" /></span>
				</div>
			<logic:present name="msg">
				<div>
					<span style="align:center"><font size="4" color="green">${msg
							}</font>
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
								<div class="form-group">
									<label for="sel1">Case ID<bean:message
											key="mandatory" /> </label>
									<%-- <html:text property="property(caseId)" styleId="caseId" size="25" styleClass="form-control"
										maxlength="" style="width:90%;"  onchange="getCaseDetails();"/> --%>
									
									<html:select property="property(caseId)"
									styleClass="form-control" styleId="caseId"
									style="width:100%;" onchange="getCaseDetails();" >
									<logic:notEmpty property="property(caseList)"
										name="counterForm">
										<html:optionsCollection property="property(caseList)"
											name="counterForm" />
									</logic:notEmpty>
								</html:select>
								</div>
							</div>
						
						<logic:present name="caseIdDetails">
						<%-- <logic:iterate id=""></logic:iterate> --%>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">Court Name:  </label>
									<bean:define id="courtName" name="counterForm"></bean:define>
									<bean:write name="courtName" property="property(courtName)"></bean:write>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">Nature of Petition : </label>
										<bean:define id="caseType" name="counterForm"></bean:define>
									<bean:write name="caseType" property="property(caseType)"></bean:write>
								</div>
							</div>
						
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">Case Number:  </label>
									<bean:define id="caseNo" name="counterForm"></bean:define>
									<html:hidden property="property(caseNo1)" name="counterForm"/>
									<bean:write name="caseNo" property="property(caseNo)"></bean:write>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
								<div class="form-group">
									<label for="sel1">Case Year:</label>
										<bean:define id="caseYear" name="counterForm"></bean:define>
										<bean:write name="caseYear" property="property(caseYear)"></bean:write>
										<html:hidden property="property(caseYear1)" name="counterForm"/>
									
								</div>
							</div>
							
							<!-- <div class="row" >
							    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							       <input type="button" name="btn" value="Next "  class="btn btn-primary text-center pull-right" id="btn_show" style="margin-top: 24px; margin-bottom: -9px; margin-right: 11px;">
							    </div>
							</div> -->
						</logic:present>
						<logic:present name="caseIdDetailsNotFound">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 " align="right">
							<br><br/>
							<span style="align:right"><font size="2" color="red">${caseIdDetailsNotFound }</font></span>
						</div>
					</logic:present>
					</div>	
				</fieldset>

				<%-- <%
					if (msgInPRform != null)
							out.println("<tr height=100><td colspan=4><font color=red><center><b>"
									+ msgInPRform + "</b></center></font></td></tr>");
				%> --%>
			<%-- <logic:notPresent name="msgInPRform" scope="request"> --%>
			<logic:present name="caseIdDetails">
				<div  class="" id="next_move" >
				<fieldset class="scheduler-border" >
					<!-- <div class="row">
					    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					       <input type="button" name="btn" value=" Back " onclick="hideDetails();" class="btn btn-primary text-center pull-right" id="btn_hide" style="margin-top: -24px; margin-bottom: -9px; margin-right: 11px;">
					    </div>
					    
					</div> -->
					<legend class="scheduler-border-left">Counter Affidavit Details</legend>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1"> Corresponding GP's <bean:message key="mandatory" /></label>
								<%-- <html:select property="property(relatedGp)" styleClass="form-control"
									onchange="checkCaseExists(this)">
									<logic:notEmpty property="property(relatedGps)"
													name="counterForm">
												<html:optionsCollection property="property(relatedGps)"
												name="counterForm" />
											</logic:notEmpty>
								</html:select> --%>
										<html:select property="property(relatedGp)" styleId="relatedGp"  style="height: 50; width: 525; " styleClass="form-control">
											<logic:notEmpty property="property(relatedGps)"
													name="counterForm">
												<html:optionsCollection property="property(relatedGps)"
												name="counterForm" />
											</logic:notEmpty>
										</html:select>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Date of Submission of Counter Affidavit to GP/SC <bean:message key="mandatory" /></label>
									<div class="input-group date"  data-date-format="dd/mm/yyyy">
										<html:text property="property(approvedDate)"  styleId="approvedDate" styleClass="form-control"  readonly="true"/>
										<div class="input-group-addon">
										<span><i class="fa fa-calendar" aria-hidden="true"></i></span>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Date of Counter Affidavit Filed in Court <bean:message key="mandatory" /></label><!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
									<div class="input-group date"  data-date-format="dd/mm/yyyy">
										<html:text property="property(submissionDate)"  styleId="submissionDate" styleClass="form-control"  readonly="true"/>
										<div class="input-group-addon">
										<span><i class="fa fa-calendar" aria-hidden="true"></i></span>
									</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="row">
					

					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1"> Upload Counter Affidavit Document </label>  
							
							<logic:notEmpty property="property(documentUploaded)" name="counterForm">
							<html:hidden property="property(documentUploaded)" styleId="documentUploaded" name="counterForm"/>
							<a href="<%=basePath%><bean:write property='property(documentUploaded)' name='counterForm'/>" target="_blank">View document uploaded earlier</a></logic:notEmpty>
							<html:file property="property(document)" />
						</div>

					</div>
				</div>
				
			
				</fieldset>
				<fieldset class="scheduler-border" >
					<legend class="scheduler-border-left"> Remarks Details</legend>

					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<logic:present name="CounterRemarks" >
							<logic:iterate id="remarks" name="CounterRemarks">
							
							<div class="headername">
							<label for="sel1"> 
							User:<span style="color:green">${remarks.USERNAME}</span><br />
								Commented Date : <span style="color:red">${remarks.INSERTED_TIME}</span></label>
							<br />
							${remarks.REMARKS}
							</div>
							
							
							</logic:iterate>
							</logic:present>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
							<div class="form-group">
								<label for="sel1">Enter your Comments: </label>
								<!-- <div id="sample" class="col-xs-12 col-sm-12 col-md-6 col-lg-6 "> -->
									<script type="text/javascript"
										src="js/nicEdit-latest.js"></script>
									<script type="text/javascript">
										bkLib.onDomLoaded(function() {
											new nicEditor({
												fullPanel : true
											}).panelInstance('counterRemarks');
										});
									</script>
									<html:textarea cols="600" styleId="counterRemarks" property="property(counterRemarks)" style="width: 100%; height: 250px;">
									</html:textarea>
							<!-- 	</div> -->
							</div>
							<html:submit property="btn" value=" Save " onclick="return submitCounterDetails();" styleClass="btn btn-success text-center" styleId="btn_submit" />
						</div>
					</div>
				</fieldset>
				</div>
				</logic:present>
			<%-- </logic:notPresent> --%>
		</html:form>
	</div>
	
	<script type="text/javascript">
	
		$(document).ready(function(){
			$(".input-group.date").datepicker({format: "dd/mm/yyyy"})  ;
		});
	
		function getCaseDetails()
		{
			document.getElementById("action").value = "getCaseDetails";
			document.forms[0].submit();
		}
		
		function showDetails(){
			$("#next_move").show();
			$("#btn_show").hide();
		}
		
		function hideDetails(){
			$("#btn_show").show();
			$("#next_move").hide();
		}
				
		function checkCaseExists(sel) {

			if (document.forms[0].caseType.value == "") {
				alert("Please select Case Type");
				return false;
			} else if (document.forms[0].caseNo.value == "") {
				alert("Please select Case No");
				return false;
			} else if (document.forms[0].caseYear.value == "") {
				alert("Please select Case Year");
				return false;
			}
			document.forms[0].isCaseExist.value = "check";
			document.forms[0].submit();
		}

		function getGps() {
			document.forms[0].isCaseExist.value = "getGps";
			document.forms[0].submit();
		}
		
		function submitCounterDetails() {
			
			var remarks =  $(".nicEdit-main").html();
			
			if (document.getElementById("relatedGp").value == null || document.getElementById("relatedGp").value == "0") {
				alert("Selct GP ");
				document.getElementById("relatedGp").focus();
				return false;
			} else if (document.getElementById("approvedDate").value == null || document.getElementById("approvedDate").value == "") {
				alert(" Date Of Receipt Of Approved Parawise Remarks ");
				document.getElementById("approvedDate").focus();
				return false;
			} else if (document.getElementById("submissionDate").value == null || document.getElementById("submissionDate").value == "") {
				alert("Enter Date Of Submission Of Counter Affidavit To GP");
				document.getElementById("submissionDate").focus();
				return false;
			} /* else if (document.getElementById("counterRemarks").value == null || document.getElementById("counterRemarks").value == "") {
				alert("Please Write counter Remarks");
				document.getElementById("counterRemarks").focus();
				return false;
			} */
			
			/* else if ($(".nicEdit-main").html()==""){
			alert("remarks required");
			return false;
			} */
			else if ( remarks=="" || remarks=="<br>" || remarks.length < 50){
				alert("Counter Affidavit Remarks Required min. 50 characters");
				$(".nicEdit-main").focus();
				return false;
			}
			else if (confirm("Do you want save Counter Affidavit Details")) {
				document.getElementById("action").value = "insertDetails";
				return true;
			} else { return false; }
		}
	</script>
</body>
</html>