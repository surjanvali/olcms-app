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


<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>css/style.css" rel="stylesheet" />
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet" />
<link href="css/bootstrap-datepicker.min.css" rel="stylesheet">
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
	<html:form action="/Judgement" enctype="multipart/form-data">

		<html:hidden property="property(isCaseExist)" value="" />
		<html:hidden property="property(serialNo)" />
		<html:hidden property="property(caseid)" value="" />
		<html:hidden property="property(status)" />
		<input type="hidden" name="mode" id="mode" />
		<html:hidden property="property(noOfRows1)" styleId="noOfRows1" />
		<html:hidden property="property(noOfRows)" styleId="noOfRows" />
		<input type="hidden" name="property(mytempvar1)" />
		<input type="hidden" name="property(mytempvar2)" />

		<div class="container shadow-left-right"
			style="min-height:500px;background:#fff;">

			<div class="row">
			<div class="case_heading" style="margin-top:20px;">
					<span><bean:message key="Judgement.caption" /></span>
				</div>
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
							<label for="sel1">Case ID<bean:message key="mandatory" />
							</label>
							<%-- <html:text property="property(caseId)" styleId="caseId" size="25"
								styleClass="form-control" maxlength="" style="width:90%;"
								onchange="return getCaseDetails() " /> --%>
								
							<html:select property="property(caseId)"
									styleClass="form-control" styleId="caseId"
									style="width:100%;" onchange="getCaseDetails();" >
									<logic:notEmpty property="property(caseList)"
										name="judgementform">
										<html:optionsCollection property="property(caseList)"
											name="judgementform" />
									</logic:notEmpty>
								</html:select>
						</div>
					</div>


					<logic:present name="caseIdDetails">
						<%-- <logic:iterate id=""></logic:iterate> --%>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Court Name: </label>
								<bean:define id="courtName" name="judgementform"></bean:define>
								<bean:write name="courtName" property="property(courtName)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Nature of Petition : </label>
								<bean:define id="caseType" name="judgementform"></bean:define>
								<bean:write name="caseType" property="property(caseType)"></bean:write>
							</div>
						</div>


						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Number: </label>
								<bean:define id="caseNo" name="judgementform"></bean:define>

								<bean:write name="caseNo" property="property(caseNo)"></bean:write>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1">Case Year:</label>
								<bean:define id="caseYear" name="judgementform"></bean:define>
								<bean:write name="caseYear" property="property(caseYear)"></bean:write>


							</div>
						</div>


						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<input type="button" name="btn" value=" Next "
									onclick="showDetails();"
									class="btn btn-info text-center pull-right" id="btn_show"
									style="margin-top: 24px; margin-bottom: -9px; margin-right: 11px;">
							</div>

						</div>
					</logic:present>
					<logic:present name="caseIdDetailsNotFound">
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 " align="right">
							<br>
							<br /> <span style="align:right"><font size="2"
								color="red">${caseIdDetailsNotFound }</font>
							</span>
						</div>
					</logic:present>
				</div>
			</fieldset>

			<fieldset class="scheduler-border" id="next_move"
				style="display:none">
				<legend class="scheduler-border-left">Judgement Information</legend>

				<!-- <div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<input type="button" name="btn" value=" Back "
							onclick="hideDetails();"
							class="btn btn-primary text-center pull-right" id="btn_hide"
							style="margin-top: -24px; margin-bottom: -9px; margin-right: 11px;">
					</div>

				</div> -->

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Bench</label>
							<bean:message key="mandatory" />
							<html:select property="property(bench)" styleClass="form-control" styleId="bench"
								style="width: 525;">
								<option value="0">--select--</option>
								<html:optionsCollection property="benchList" />
							</html:select>
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<table id="table1">
							<tr>
								<td><label for="sel1">Name Of Judge(S)</label>
								<bean:message key="mandatory" />
								</td>
							</tr>
							<tr>
								<td>
									<!-- <div class="form-group"> --> <html:text
										property="property(judgename0)" styleClass="form-control" styleId="judgename0"
										style="width: 450px;" /> <!-- </div> --></td>
								<td><i class="fa fa-plus-circle" id="addrowCriteria"></i>
								</td>
							</tr>
						</table>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Date Of Judgement</label>
							<bean:message key="mandatory" />
							<!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
							<div class="input-group date" data-date-format="dd/mm/yyyy">
								<html:text property="property(judgementDate)" styleId="judgementDate" styleClass="form-control" readonly="true"></html:text>
								<div class="input-group-addon">
									<span><i class="fa fa-calendar" aria-hidden="true"></i>
									</span>
								</div>
							</div>
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Whether GP Appeared</label>
							<bean:message key="mandatory" />
							<html:radio property="property(gpAppeared)" value="Yes" />
							Yes
							<html:radio property="property(gpAppeared)" value="No" />
							No
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
						<div class="form-group">
							<label for="sel1">Result</label>
							<bean:message key="mandatory" />
							<html:radio property="property(result)" value="Yes"
								onclick="checkRadio(this)" />
							Favourable to Govt 
							<html:radio property="property(result)" value="Partial"
								onclick="checkRadio(this)" />
							Partially Favourable to Govt
							<html:radio property="property(result)" value="No"
								onclick="checkRadio(this)" />
							Not Favourable to Govt
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Time Limit For Implementation</label>
							<bean:message key="mandatory" />
							<!--<bean:message key="ParawiseRemarks.dtPRSentToGP" />   -->
							<div class="input-group date" data-date-format="dd/mm/yyyy">
								<html:text property="property(implementationDate)" styleId="implementationDate" size="10" styleClass="form-control"  readonly="readonly"/>
								<div class="input-group-addon">
									<span><i class="fa fa-calendar" aria-hidden="true"></i>
									</span>
								</div>
							</div>
							
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Date of Receipt of Judgement Copy</label>
							<bean:message key="mandatory" />
							<div class="input-group date" data-date-format="dd/mm/yyyy">
								<input name="property(copyDate)" id="copyDate" type="text"
									class="form-control" readonly="readonly">
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
							<table>
								<tr>
									<td colspan="3"><logic:present name="judgementform"
											property="property(result)">
											<logic:notEqual value="Yes" name="judgementform"
												property="property(result)">
												<div id="appealimp">
											</logic:notEqual>
										</logic:present> <logic:notPresent name="judgementform"
											property="property(result)">
											<div id="appealimp" style="display: none;">
										</logic:notPresent> <logic:equal value="Yes" name="judgementform"
											property="property(result)">
											<div id="appealimp" style="display: none;">
										</logic:equal>
										<table width="100%" border="0">
											<tr>
												<td width="75%" class="clsTDForLabel"><label for="sel1">Appeal
														To Be Filed</label>
												<bean:message key="mandatory" /></td>
												<td><html:radio property="property(appealimp)"
														value="Yes" />Yes &nbsp; <html:radio
														property="property(appealimp)" value="No" />No</td>
											</tr>
										</table> <%--</div>
									--%>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Details of Judgement</label>
							<bean:message key="mandatory" />
							<%-- <html:textarea property="property(details)" cols="60" styleClass="form-control" onkeyup="charOnly(this);" styleId="details" ></html:textarea>
							<input readonly="readonly" type="text"
								name="property(detailslimit)" id="details" size="3" value="1000"
								border="0"
								style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
								 --%>
							
							<html:textarea property="property(details)" cols="60" styleClass="form-control"
											onkeyup="charOnly(this);"
											styleId="details" onblur="return checkminimum(this)"></html:textarea>
											<br />
											<div id="detailsChars">Characters left: 1000</div>	
								
								
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">Opinion of GP</label>
							<bean:message key="mandatory" />
							<%-- <html:textarea property="property(gpOpinion)" cols="60" styleClass="form-control" onkeyup="charOnly(this);" styleId="gpOpinion" onblur="return checkminimum(this)"></html:textarea>
							<input readonly="readonly" type="text"
								name="property(gpOpinionlimit)" id="gpOpinion" size="3"
								value="1000" border="0"
								style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
								 --%>
								
								<html:textarea property="property(gpOpinion)" cols="60" styleClass="form-control"
											onkeyup="charOnly(this);"
											styleId="gpOpinion" onblur="return checkminimum(this)"></html:textarea>
											<br />
											<div id="gpOpinionChars">Characters left: 1000</div>	
								
								
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
						<div class="form-group">
							<label for="sel1">ATR (Description of Judgement
								Implementation)</label>
								<bean:message key="mandatory" />
							<%-- <html:textarea property="property(atrDescription)" cols="60"
								styleClass="form-control"
								onkeydown="textAreaCounter(this,document.getElementById('atrDescription'),1000)"
								onkeyup="textAreaCounter(this,document.getElementById('atrDescription'),1000);charOnly(this);"
								styleId="atrDescription" onblur="return checkminimum(this)"></html:textarea>
							<input readonly="readonly" type="text"
								name="property(atrJudgementImplementlimit)" id="atrDescription"
								size="3" value="1000" border="0"
								style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
								 --%>
								
							<html:textarea property="property(atrDescription)" cols="60" styleClass="form-control"
											onkeyup="charOnly(this);"
											styleId="atrDescription" onblur="return checkminimum(this)"></html:textarea>
											<br />
											<div id="atrDescriptionChars">Characters left: 1000</div>		
								
								
								
						</div>
					</div>
				</div>

				<fieldset class="scheduler-border">
					<legend class="scheduler-border-left">Judgement Attachments</legend>

					<table align="center" width="100%">
						<tr>
							<td>
								<table class="table table-bordered table-striped" id="postTable"
									cellpadding="2" cellspacing="2">
									<thead  style="background: #e5f7ff;">
										<tr>
											<th>S.NO</th>
											<th>Document Description</th>
											<th>Attachment</th>
											<th>Action</th>
										</tr>
										</thead>
										<tr>
											<td>1</td>
											<%-- <td><html:textarea property="dynaProperty1(1#1)" cols="60" styleClass="form-control" onkeyup="charOnly(this);"/></td>
											<td><input type="file" name='<%="dynaProperty1(1#2)"%>' id='<%="1#2"%>' onkeyup="charOnly(this);" /></td> --%>
											<td><html:textarea property="property(docDescription0)" styleId="docDescription0"
													cols="60" styleClass="form-control"
													onkeyup="charOnly(this);" />
											</td>
											<td><html:file property="property(document0)" />
											</td>
											<td><i class="fa fa-plus-circle" id="addrow"></i>
											</td>
										</tr>
								</table></td>
						</tr>

						<!-- <tr>
							<td align="right">
								<input type="button"
									style="width: 180px; font-size: 12px; font-style: normal; font-weight: bold; color:black"
									value="Add Another Attachment" onclick="generateRow()" />
								<input type="button"
									style="width: 180px; font-size: 12px; font-style: normal; font-weight: bold;color:black"
									value="Delete Last Attachment"
									onclick="removeRowFromTable();" />
	
							</td>
						</tr> -->
					</table>
				</fieldset>
				<div
					class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-right">
					<input type="button" name="property(btn)" value=" Submit "
						onclick="CheckForm();" class="btn btn-danger text-center"
						id="btn_submit">
				</div>
			</fieldset>
		</div>

<!-- 14327031
go

INC0091229
05010202007

uid:14120817
pwd:ddsagar

inc0093446
-
workflow -->

		<!-- <script type="text/javascript" src="js/date-picker.js"></script> -->
		<!-- <script type="text/javascript" src="js/popcalendar.js"></script> -->
		<script type="text/javascript" language="javascript" src="js/ajax.js"></script>
		<script type="text/javascript" src="js/CommonFunction.js"></script>

		<script src="jQuery/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/script.js"></script>
		<script src="js/bootstrap-datepicker.min.js"></script>
		<script>
			$('.input-group.date').datepicker({
				format : "dd/mm/yyyy"
			});
		</script>

		<script language="JavaScript">
			function getCaseDetails() {
				document.getElementById("mode").value = "getCaseDetails";
				//alert($("#mode").val());
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

			function textToTrim(i) {
				i.value = i.value.trim();
				textAreaCounter(i, document.getElementById('atrDescription'),
						1000);
			}

			function charOnly(i) {
				if (i.value.length > 0) {
					i.value = i.value.replace(/[^a-zA-z-.()0-9\s]+/g, '');
				}
			}
			function editfn(sel) {
				var ctype = document.forms[0].caseType.value;
				var cno = document.forms[0].caseNo.value;
				var cyear = document.forms[0].caseYear.value;
				var str = ctype + cno + "/" + cyear;
				document.forms[0].caseid.value = str;
				document.forms[0].isCaseExist.value = "check";
				document.forms[0].submit();
			}

			function checkRadio(val) {
				if (val.value != "Yes") {
					document.getElementById("appealimp").style.display = "";
				} else {
					document.getElementById("appealimp").style.display = "none";
				}
			}

			$(document)
					.ready(
							function() {
								var counter = 1;
								var displauCounter = 2;

								$("#addrow")
										.on(
												"click",
												function() {
													var sno = $("#postTable tr").length;
													//alert(counter);
													var newRow = $("<tr>");
													var cols = "";

													cols += '<td>' + sno + '</td>';
													cols += '<td><textarea  class="form-control" name="property(docDescription'
															+ counter + ')" id="" /></td>';
													cols += '<td><input type="file"  class="form-control" name="property(document'
															+ counter + ')" /></td>';
													cols += '<td><i class=\"fa fa-minus-circle\" id="deleteRowCriteria"/></td>';

													newRow.append(cols);
													$("#postTable").append(newRow);
													counter++;
													displauCounter++;
												});

								$("#postTable").on("click",
										"#deleteRowCriteria", function(event) {
											$(this).closest("tr").remove();
											counter -= 1;
											displauCounter -= 1;
										});
							
								var counter = 1;
								var displauCounter = 2;

								$("#addrowCriteria").on("click",
												function() {
													var newRow = $("<tr>");
													var cols = "";

													cols += '<td><input type="text"  class="form-control" name="property(judgename' + counter + ')" id="judgename' + counter + '"/></td>';
													cols += '<td><i class=\"fa fa-minus-circle\" id="deleteRowCriteria"/></td>';

													newRow.append(cols);
													$("#table1").append(newRow);
													counter++;
													displauCounter++;
												});

								$("#table1").on("click", "#deleteRowCriteria",
										function(event) {
											$(this).closest("tr").remove();
											counter -= 1;
											displauCounter -= 1;
										});
										
								$("#details").keyup(function(){
									  $("#detailsChars").text("Characters left: " + (1000 - $(this).val().length));
									  if($("#details").val().length > 1000)
									  		$("#details").val($(this).val().substr(0,1000));
									});
								
								$("#gpOpinion").keyup(function(){
									  $("#gpOpinionChars").text("Characters left: " + (1000 - $(this).val().length));
									  if($("#gpOpinion").val().length > 1000)
									  		$("#gpOpinion").val($(this).val().substr(0,1000));
									});			
										
									$("#atrDescription").keyup(function(){
									  $("#atrDescriptionChars").text("Characters left: " + (1000 - $(this).val().length));
									  if($("#atrDescription").val().length > 1000)
									  		$("#atrDescription").val($(this).val().substr(0,1000));
									});				
							});

			function CheckForm() {
				
				var noOfRows1 = $("#postTable tr").length;
				var noOfRows = $("#table1 tr").length;
				noOfRows = noOfRows - 1;
				noOfRows1 = noOfRows1 - 1;
				
				if(document.getElementById("bench").value=="" || document.getElementById("bench").value=="0"){
					alert("Select Bench.");
					document.getElementById("bench").focus();
					return false;
				}
				else if(document.getElementById("judgementDate").value=="" || document.getElementById("judgementDate").value=="0"){
					alert("Judgement Date required.");
					document.getElementById("judgementDate").focus();
					return false;
				}
				
				else if (document.forms[0].elements["property(gpAppeared)"][0].checked == false && document.forms[0].elements["property(gpAppeared)"][1].checked == false )
				{
					alert("Whether GP Appeared.");
					document.forms[0].elements["property(gpAppeared)"][0].focus();
					return false;
				}
				else if (document.forms[0].elements["property(result)"][0].checked == false && document.forms[0].elements["property(result)"][1].checked == false && document.forms[0].elements["property(result)"][2].checked == false )
				{
					alert("Judgement Result.");
					document.forms[0].elements["property(result)"][0].focus();
					return false;
				}
				else if(document.getElementById("implementationDate").value=="" || document.getElementById("implementationDate").value=="0"){
					alert("Implementation Date required.");
					document.getElementById("implementationDate").focus();
					return false;
				}
				else if(document.getElementById("copyDate").value=="" || document.getElementById("copyDate").value=="0"){
					alert("Copy Date required.");
					document.getElementById("copyDate").focus();
					return false;
				}
				
				else if(document.getElementById("details").value=="" || document.getElementById("details").value=="0"){
					alert("Judgement details required.");
					document.getElementById("details").focus();
					return false;
				}
				else if(document.getElementById("gpOpinion").value=="" || document.getElementById("gpOpinion").value=="0"){
					alert("GP Opinion required.");
					document.getElementById("gpOpinion").focus();
					return false;
				}
				else if(document.getElementById("atrDescription").value=="" || document.getElementById("atrDescription").value=="0"){
					alert("ATR description required.");
					document.getElementById("atrDescription").focus();
					return false;
				}
				
				else{
				//var formObj = document.forms[0];
				
				//alert("noofRows1:" + noOfRows1);
				var r = confirm("Do you want to submit data?");
				//document.forms[0].mode.value = "insertDetails";
				document.forms[0].noOfRows.value = noOfRows;
				document.forms[0].noOfRows1.value = noOfRows1;
				//alert(document.forms[0].noOfRows1.value);
				document.getElementById("mode").value = "insertDetails";
				document.forms[0].submit();
				}
			}

			function getSelectedCheckbox(buttonGroup) {
				// Go through all the check boxes. return an array of all the ones
				// that are selected (their position numbers). if no boxes were checked,
				// returned array will be empty (length will be zero)
				var retArr = new Array();
				var lastElement = 0;

				if (buttonGroup[0]) { // if the button group is an array (one check box is not an array)
					for ( var i = 0; i < buttonGroup.length; i++) {
						if (buttonGroup[i].checked) {
							retArr.length = lastElement;
							// retArr[lastElement] = i;
							retArr[lastElement] = buttonGroup[i].value;
							lastElement++;
						}
					}
				} else { // There is only one check box (it's not an array)
					if (buttonGroup.checked) { // if the one check box is checked
						retArr.length = lastElement;
						retArr[lastElement] = 0; // return zero as the only array value
					}
				}
				return retArr;
			} // Ends the "getSelectedCheckbox" function

			var subdeptsList2 = new Array();
			var boxid;
			function getMandalList4(obj) {
				boxid = obj;
				var gpIds = getSelectedCheckbox(document.forms[0].elements[obj]);
				var url = "loadResource.do" + "?sid=" + Math.random()
						+ "&restype=depts&cbo=true&courtid="
						+ document.forms[0].courtType.value; // sid=  Math.random()  prevent caching

				//alert("URL :: "+url)
				xmlHTTP = getXMLRequest();
				if (xmlHTTP) {
					xmlHTTP.onreadystatechange = parsecasesListadvocates;
					xmlHTTP.open('GET', url, true); // GET method
					xmlHTTP.send(null); // always use null for GET method
				}
			}

			function GetSelectedResp(obj) {
				//alert(document.forms[0].elements[obj])
				var gpIds = getSelectedCheckbox(document.forms[0].elements[obj]);
				var url = "loadResource.do" + "?sid=" + Math.random()
						+ "&restype=pwcautharity&cbo=true&gpIds=" + gpIds; // sid=  Math.random()  prevent caching
				if (window.XMLHttpRequest) { // Non-IE browsers
					req = new XMLHttpRequest();
					req.onreadystatechange = parseMandalList9;
					try {

						req.open("GET", url, true);
					} catch (e) {
						alert(e);
					}
					req.send(null);
				} else if (window.ActiveXObject) { // IE
					req = new ActiveXObject("Microsoft.XMLHTTP");
					if (req) {
						req.onreadystatechange = parseMandalList9;

						req.open("GET", url, true);
						req.send();
					}
				}
			}
			
			function disableCheck() {
				init();

				var obj = getSelectedCheckbox(document.forms[0].stDistrict)
						+ "";
				obj = obj.split(",");

				for ( var i = 0; i < obj.length; i++) {
					if (obj[i] == "AndhraPradesh") {
						disableDistricts("AndhraPradesh");
						break;
					}
				}
			}

			function checkminimum(obj) {
				if (obj.value != "") {
					if ((obj.value).length < 50) {
						alert("Please Enter Brief description Minimum 50 characters")
						//document.forms[0].brfDiscription.focus();
						return false;
					}
				}
			}
		</script>

	</html:form>
</body>
</html>