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
	String atr = request.getAttribute("updateAtrb")!=null ? (String) request.getAttribute("updateAtrb"):null;
	if (atr == null)
		atr = "insert";
	//	String msgInWPform = (String) request.getAttribute("msgInWPform");
	//String auth = (String) session.getAttribute("canAccess");
	ArrayList otherAdmissions = request.getAttribute("otherAdmissions")!=null ? (ArrayList) request.getAttribute("otherAdmissions") : null;
	//out.println("otherAdmissions -- "+otherAdmissions);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>:: AP-FINANCE ::</title>

<!-- Bootstrap -->
<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>css/style.css" rel="stylesheet" />
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet" />

<link rel="stylesheet" href="css/font-awesome.min.css">

<link href="<%=basePath%>css/multiselect/jquery.multiselect.css" rel="stylesheet" />


</head>
<body>

	<html:form action="/WPAffidavit" styleId="WPAffidavit"
		enctype="multipart/form-data">
		<%
							if (atr.equals("update")) {
						%>
						<html:hidden property="action" value="update" />
						<%
							} else if (atr.equals("insert")) {
						%>
						<html:hidden property="action" value="insert" />
						<%
							}
						%>
						<html:hidden property="isCaseExist" value="" />
						<html:hidden property="serialNo" />
						<html:hidden property="caseid" value="" />
						<html:hidden property="status" />

						<input type="hidden" name="mytempvar1" />
						<input type="hidden" name="mytempvar2" />

		<div class="container shadow-left-right"
			style="min-height:500px;background:#fff;">

			<fieldset class="scheduler-border">
				<legend class="scheduler-border">Case Particulars </legend>



				<input type="hidden" name="status" value=""> <input
					type="hidden" name="dynaProperty(showHide)" value="No">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<!-- <label for="sel1"> Court Name </label> <select
								data-live-search="true" name="proerty(courtType)"
								class="form-control" id="courtType" style="visibility: visible;">
								<option data-tokens="1">High Court</option>
								<option data-tokens="2">AP Administrative court</option>
								<option data-tokens="3">Supreme Court</option>
							</select> 
							
							 -->
							<label for="sel1"> <bean:message key="wpaffd.courttype" />
								<bean:message key="mandatory" /> </label>
							<html:select property="courtType" styleClass="form-control"
								 style="width:90%;"  onchange="return getCaseTypes(this);">
								<option value="">--Select Court Name--</option>
								<html:optionsCollection property="courtList" />
							</html:select>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"><bean:message key="wpaffd.casetype" />
								<bean:message key="mandatory" /> </label>
							<html:select property="caseType" styleClass="form-control"
								styleId="caseIds" style="width:90%;">
								<option value=""></option>
								<html:optionsCollection property="caseTypesList" />
							</html:select>
						</div>
					</div>


					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"><bean:message key="wpaffd.caseno" /> <bean:message
									key="mandatory" /> </label>
							<html:text property="caseNo" size="25" styleClass="form-control"
								maxlength="10" onkeyup="intOnly(this)" style="width:90%;" />
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
						<div class="form-group">
							<label for="sel1"> <bean:message key="wpaffd.caseyear" />
								<bean:message key="mandatory" />
							</label>
							<html:select property="caseYear" styleClass="form-control"
								onchange="editfn(this)" style="width:30%">
								<option value=""></option>
								<html:optionsCollection property="fiftyYears" />
							</html:select>
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-8 col-lg-8">
						<div class="form-group">
							<label for="sel1"> Office File No. : </label>
							<!-- <span class="ben-wrt">  </span> -->
							<input type="text" name="proerty(secyFileNo)"
								class="form-control" id="secyFileNo">
						</div>
					</div>
				</div>

			</fieldset>
			<fieldset class="scheduler-border">
				<legend class="scheduler-border">Petition Information : </legend>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> Govt As:<bean:message key="mandatory" />
							</label> <input type="radio" name="isPetRes" id="isPetRes"
								value="Petitioner">Petitioner <input type="radio"
								name="isPetRes" id="isPetRes" value="Respondent">Respondent
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> Petitioner(s) Name(s) </label>
							<html:textarea style="width:90%;" property="petName"
								styleId="petName" styleClass="form-control" cols="19"
								onkeyup="charOnly(this);" />

							<select name="petNames" class="form-control" id="petNames"
								style="display: none" multiple="multiple"
								style="visibility: visible;">
								<logic:present name="officerList">
									<logic:iterate name="officerList" id="officer">
										<option value="${officer.value }">${officer.label }</option>
									</logic:iterate>
								</logic:present>

							</select>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> Respondent(s) Name(s) </label>
							<html:textarea property="respName" cols="19" styleId="respName"
								styleClass="form-control" onkeyup="charOnly(this);"></html:textarea>
							<select name="property(respNames)" multiple="multiple"
								class="form-control" style="display: none" id="respNames"
								style="visibility: visible;">
								<logic:present name="officerList">

									<logic:iterate name="officerList" id="officer">
										<option value="${officer.value }">${officer.label }</option>
									</logic:iterate>
								</logic:present>
							</select>
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> Advocate Name <span style="color:red"
								id="adv">*</span>
							</label>
							<html:text property="respAdvocate" styleClass="form-control"
								styleId="respAdvocate" style="width:90%;" size="25"
								maxlength="500" />
							<html:select property="respAdvocates" style="width:220px;"
								styleClass="form-control" styleId="resadvList">
								<html:option value="">--select--</html:option>
								<html:optionsCollection property="deptGPsList" />
							</html:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
						<div class="form-group">
							<label for="sel1"><bean:message key="wpaffd.respadvcode" />
							</label>
							<html:text property="respAdvocate" style="width:90%;"
								styleClass="form-control" size="25" maxlength="500" />
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> Date of Petition recieved by the Dept.
							</label> <input type="text" name="property(dtPetRecvByDept)"
								maxlength="70" value="" class="form-control"
								id="dtPetRecvByDept">
						</div>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"><bean:message key="wpaffd.subcategory" />
								<bean:message key="mandatory" />
							</label>
							<html:select property="subCategory" styleClass="form-control"
								style="width:90%;">
								<!--  onchange="getOtherSubs(this)"  -->
								<option value="">--Select Subject--</option>
								<html:optionsCollection property="subjectCategorysList" />
							</html:select>
						</div>

					</div>
				</div>


				<div class="row">

					<%-- <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">


							<label for="sel1"> <bean:message key="wpaffd.stdist" />
								<bean:message key="mandatory" /> </label> <select
								name="property(stDistrict)" class="form-control"
								multiple="multiple" id="stDistrict" style="visibility: visible;"
								onclick="disableDistricts(this.value)">
								<logic:present name="districtsList">
									<logic:iterate name="districtsList" id="districtId">
										<option value="${districtId.value }">${districtId.label
											}</option>
									</logic:iterate>
								</logic:present>
							</select>



						</div>

					</div> --%>
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
						<div class="form-group">
							<label for="sel1"> Parawise/Counter Filing Authority*</label>
							<div style="width: auto;height:50px;">
								<html:checkbox styleId="authpwca" value="" property="authpwca"
									styleClass="form-control"> Counter Not Required</html:checkbox>
							</div>
							<%--  <html:text property="respAdvocateCode" style="width:90%;" size="25" maxlength="500" /> --%>
							<!-- <input type="text" name="dynaProperty(respAdvocateCode)"  class="form-control" id="respAdvocateCode"> -->

						</div>

					</div>

					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<bean:message key="wpaffd.brfdiscrip" />
							<bean:message key="mandatory" />
							<html:textarea property="brfDiscription" cols="60"
								styleClass="form-control"
								onkeydown="textAreaCounter(this,document.getElementById('petitionerslimit'),1000)"
								onkeyup="textAreaCounter(this,document.getElementById('petitionerslimit'),1000);charOnly(this);"
								styleId="petitionerNames" onblur="return checkminimum(this)"></html:textarea>
							<input readonly="readonly" type="text" name="petitionerslimit"
								id="petitionerslimit" size="3" value="1000" border="0"
								style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
						</div>

					</div>


					<!-- </div> -->
					<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
						<div class="form-group">
							<label for="sel1"> <bean:message key="wpaffd.priority" />
							</label>
							<html:select property="priority" styleClass="form-control"
								value="B - Important">
								<html:option value=""></html:option>
								<%--<html:optionsCollection property="prioritiesList" />--%>
								<html:option value="A - Most Critical">A - Most Critical</html:option>
								<html:option value="B - Important">B - Important</html:option>
								<html:option value="C - Ordinary">C - Ordinary</html:option>
							</html:select>
						</div>

					</div>
				</div>
				<fieldset class="scheduler-border">
					<legend class="scheduler-border">Admission Details : </legend>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
							<div class="form-group">
								<label for="sel1"> <bean:message key="wpaffd.dt_filing" />
								</label>
								<html:text property="dtFiling" size="10" maxlength="50"
									styleClass="form-control"
									onblur="ValidateDate(this,document.forms[0].dtFiling)" />
								<script language='javascript'>
									if (!document.layers) {
										document
												.write("<img src='images/calendar.png' onclick='popUpCalendar(this,document.forms[0].dtFiling, \"dd/mm/yyyy\")' value='select' style='font-size:11px'>")
									}
								</script>
								<br>
								<bean:message key="DateFormat" />
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1"> <bean:message key="wpaffd.dt_listing" />
								</label>
								<html:text property="dtListing" size="10" maxlength="50"
									styleClass="form-control"
									onblur="ValidateDate(this,document.forms[0].dtListing)" />
								<script language='javascript'>
									if (!document.layers) {
										document
												.write("<img src='images/calendar.png' onclick='popUpCalendar(this,document.forms[0].dtListing, \"dd/mm/yyyy\")' value='select' style='font-size:11px'>")
									}
								</script>
								<br>
								<bean:message key="DateFormat" />
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
							<div class="form-group">
								<label for="sel1"> <bean:message key="wpaffd.stage" />
									<bean:message key="mandatory" /> </label>
								<html:select property="stage" styleClass="form-control"
									style="width:90%;">
									<!-- onchange="stageOnChange(this)" -->
									<option value=""></option>
									<html:optionsCollection property="stagesList" />
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
							<div class="form-group">
								<label for="sel1"> Remarks </label>
								<html:textarea property="remarks" style="width: 500px;"
									styleClass="form-control"
									onkeydown="textAreaCounter(this,document.getElementById('remLimit'),1000)"
									onkeyup="textAreaCounter(this,document.getElementById('remLimit'),1000);charOnly(this);"
									styleId="remarks" onblur="return checkminimum(this)"></html:textarea>
								<input readonly="readonly" type="text" name="remLimit"
									id="remLimit" size="3" value="1000" border="0"
									style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
							</div>

						</div>
					</div>
				</fieldset>

				<div
					class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-right">
					<input type="button" name="btn" value=" Submit "
						onclick="CheckForm();" class="btn btn-primary text-center"
						id="btn_submit">
				</div>
			</fieldset>
		</div>

		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<script src="<%=basePath %>js/bootstrap.min.js"></script>
		<script src="<%=basePath %>js/ajax.js"></script>
		<script src="<%=basePath %>js/script.js"></script>
		<script src="<%=basePath %>js/multiselect/jquery.multiselect.js"></script>
		<script type="text/javascript">
			$(function() {
				$stDistrict = $('#stDistrict');
				$stDistrict.multiselect();
				$petNames = $('#petNames');
				$petNames.multiselect();
				$respNames = $('#respNames');
				$respNames.multiselect();

				/* 		
				 $(function () {
				 $('#isPetRes input[type=radio]').change(function(){
				 alert ( $(this).val() )
				
				 })
				 }) */
				$(function() {
					$('input:radio[name="isPetRes"]').change(function() {
						if ($(this).val() == 'Petitioner') {
							/*  alert("Petitioner"); */
							$("#petName").hide();
							$("#respNames").hide();
							$("#petNames").show();
							$("#respName").show();
							$("#resadvList").hide();
							$("#respAdvocate").show();
							$("#adv").hide();

						} else {
							$("#respName").hide();
							$("#petNames").hide();
							$("#respNames").show();
							$("#petName").show();
							$("#resadvList").show();
							$("#respAdvocate").hide();
							$("#adv").show();
							/*  alert("Respondent "); */
						}
					});
				});
				$("#respNames").hide();
				$("#petNames").hide();
				$("#resadvList").hide();
				$("#adv").hide();
			});
		</script>

		<script language="JavaScript">
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

				//alert("**"+document.forms[0].caseid.value+"hai"+document.forms[0].action.value);

				document.forms[0].submit();
			}

			/* function stageOnChange(sel)
			{
				if(sel.value=="Admit&Interim" || sel.value=="Admit with Full Stay" || sel.value=="Admit with Part Stay")
				{
					document.forms[0].interimOrder.value = "Yes";
					document.forms[0].interimNo.disabled = false;
					document.forms[0].interimType.disabled = false;
					document.forms[0].interimYear.disabled = false;
					document.forms[0].interimDirection.disabled = false;
					document.forms[0].implement.disabled = false;
					document.forms[0].dueDate.disabled = false;
					document.forms[0].interimDirectionImplemented.disabled = false;
					
					
				}
				else
				{
					document.forms[0].interimOrder.value = "No";
					document.forms[0].interimNo.disabled = true;
					document.forms[0].interimType.disabled = true;
					document.forms[0].interimYear.disabled = true;
					document.forms[0].interimDirection.disabled = true;
					document.forms[0].implement.disabled = true;
					document.forms[0].dueDate.disabled = true;
					document.forms[0].interimDirectionImplemented.disabled = true;
					
				}
			} */

			var i = 0;
			function checkAdvocate(obj) {

				if (i == 0) {
					// ** document.forms[0].mytempvar1.value=document.getElementById('mydiv1').innerHTML;
					document.forms[0].mytempvar2.value = document
							.getElementById('mydiv2').innerHTML;

					i++;
				}

				//if(obj.value=="P")
				//{
				//document.forms[0].petAdvocate.value="";
				//document.forms[0].respAdvocate.value="";
				//	document.getElementById('mydiv1').innerHTML=document.forms[0].mytempvar1.value;
				//	document.getElementById('mydiv2').innerHTML="<input type=\"text\" name=\"respAdvocate\" value=\""+ document.forms[0].respAdvocate.value +"\" size=\"25\" maxlength=\"500\" />";

				//	}
				///else
				//{
				//	//document.forms[0].respAdvocate.value="";
				//document.forms[0].petAdvocate.value="";
				//	document.getElementById('mydiv2').innerHTML=document.forms[0].mytempvar2.value;
				//	document.getElementById('mydiv1').innerHTML="<input type=\"text\" name=\"petAdvocate\"  value=\""+ document.forms[0].respAdvocate.value +"\"  size=\"25\" maxlength=\"500\" />";
				//	 }

				if (obj.value == "P") {

					//document.forms[0].petAdvocate.value="";
					//document.forms[0].respAdvocate.value="";
					//document.getElementById('mydiv1').innerHTML=document.forms[0].mytempvar1.value;
					//document.getElementById('mydiv2').innerHTML="<input type=\"text\" name=\"respAdvocate\" value=\""+ document.forms[0].respAdvocate.value +"\" size=\"25\" maxlength=\"500\" />";

					// ** document.getElementById('mydiv1').style.display="";
					document.getElementById('mydiv2').style.display = "none";
					// ** document.getElementById('mydiv3').style.display="none";
					document.getElementById('mydiv4').style.display = "";

					document.getElementById('mydiv5').style.display = "none";
					document.getElementById('mydiv6').style.display = "";
					document.getElementById('mydiv7').style.display = "";
					document.getElementById('mydiv8').style.display = "none";

					document.getElementById('petname').innerHTML = "Petitioner(s) Name(s)<font color=\"red\">*</font>";
					document.getElementById('respname').innerHTML = "Respondent(s) Name(s) ";

					// ** document.getElementById('petadv').innerHTML= "Petitioner's Advocate Name<font color=\"red\">*</font>";
					//document.getElementById('respadv').innerHTML="Respondent Advocate Name ";
					document.getElementById('respadv').innerHTML = "Advocate Name ";

					// ** getMandalList4('petadvList');
				} else {
					//document.forms[0].respAdvocate.value="";
					//document.forms[0].petAdvocate.value="";
					//document.getElementById('mydiv2').innerHTML=document.forms[0].mytempvar2.value;
					//document.getElementById('mydiv1').innerHTML="<input type=\"text\" name=\"petAdvocate\"  value=\""+ document.forms[0].respAdvocate.value +"\"  size=\"25\" maxlength=\"500\" />";
					// ** document.getElementById('mydiv1').style.display="none";
					document.getElementById('mydiv2').style.display = "";
					// ** document.getElementById('mydiv3').style.display="";
					document.getElementById('mydiv4').style.display = "none";

					document.getElementById('mydiv5').style.display = "";
					document.getElementById('mydiv6').style.display = "none";
					document.getElementById('mydiv7').style.display = "none";
					document.getElementById('mydiv8').style.display = "";

					document.getElementById('respname').innerHTML = "Respondent(s) Name(s) <font color=\"red\">*</font>";
					document.getElementById('petname').innerHTML = "Petitioner(s) Name(s)";

					document.getElementById('respadv').innerHTML = "Advocate Name <font color=\"red\">*</font>";
					// ** document.getElementById('petadv').innerHTML= "Petitioner's Advocate Name";

					// ** getMandalList4('resadvList');
				}

			}
		</script>






		<script language="JavaScript">
			function validateWpaffidavitform1(obj) {
				var formObj = document.forms[0];

				//alert("res :"+document.forms[0].respAdvocate.value)
				//alert("pet :"+document.forms[0].petAdvocate.value)
			}
		</script>

		<script type="text/javascript">
			function CheckForm() {

				var formObj = document.forms[0];

				// ** var fieldRequired = Array("caseType","caseYear","isPetRes","suit","petAdvocate","respAdvocate","deptHod","subCategory","stDistrict","dtAffSentToDept","dtFiling","stage");
				//var fieldRequired = Array("caseType","caseYear","isPetRes","suit","respAdvocate","subCategory","stDistrict","dtAffSentToDept","stage");
				var fieldRequired = Array("caseType", "caseYear", "isPetRes",
						"suit", "respAdvocate", "subCategory",
						"dtAffSentToDept", "stage");
				// Enter field description to appear in the dialog box
				//var fieldDescription = Array("Case Type","Case Year","isPetRes","suit","respAdvocate","subCategory","stDistrict","dtAffSentToDept","stage");
				var fieldDescription = Array("Case Type", "Case Year",
						"isPetRes", "suit", "respAdvocate", "subCategory",
						"dtAffSentToDept", "stage");
				//var fieldRequired = Array("natureofcase","caseType","caseNo","caseYear","isPetRes","suit","petAdvocate","respAdvocate","deptHod","subCategory","section","stDistrict","brfDiscription","stage","authpwca");
				//var fieldDescription = Array("Nature of case","Case Type","caseNo","Case Year","Govt Behaviour (Pet/Res)","Tax/Suit Value","Pet / Respondent Name","Petitioner / Respondent Advocate","HOD","Subject Category","Section","District","Brief description(Minimum 50 chars) of the Case","Stage","Parawise/Counter Filing Authority");
				var found_it;

				for ( var i = 0; i < document.forms[0].isPetRes.length; i++) {
					if (document.forms[0].isPetRes[i].checked) {

						found_it = document.forms[0].isPetRes[i].value

					}
				}
				// alert(found_it);
				if (found_it == "P") {
					// ** fieldRequired=new Array("natureofcase","caseType","caseNo","caseYear","isPetRes","suit","petNames","petAdvocates","deptHod","subCategory","section","stDistrict","brfDiscription","dtFiling","stage","authpwca");
					// ** fieldDescription = new Array("Nature of case","Case Type","Case Number","","Govt Behaviour (Pet/Res)","Tax/Suit Value","Pet Name","Petitioner Advocate","HOD","Subject Category","Section","District","Brief description(Minimum 50 chars) of the Case","Dt of Filing","Stage","Parawise/Counter Filing Authority");
					//fieldRequired=new Array("natureofcase","caseType","caseNo","caseYear","isPetRes","suit","petNames","subCategory","section","stDistrict","brfDiscription","stage","authpwca");
					//fieldRequired=new Array("natureofcase","caseType","caseNo","caseYear","isPetRes","suit","petNames","subCategory","section","brfDiscription","stage","authpwca");
					fieldRequired = new Array("natureofcase", "caseType",
							"caseNo", "caseYear", "isPetRes", "suit",
							"petNames", "subCategory", "brfDiscription",
							"stage", "authpwca");
					//fieldDescription = new Array("Nature of case","Case Type","Case Number","Case Year","Govt Behaviour (Pet/Res)","Tax/Suit Value","Pet Name","HOD","Subject Category","Section","District","Brief description(Minimum 50 chars) of the Case","Stage","Parawise/Counter Filing Authority");
					//fieldDescription = new Array("Nature of case","Case Type","Case Number","Case Year","Govt Behaviour (Pet/Res)","Tax/Suit Value","Pet Name","HOD","Subject Category","Section","Brief description(Minimum 50 chars) of the Case","Stage","Parawise/Counter Filing Authority");
					fieldDescription = new Array("Nature of case", "Case Type",
							"Case Number", "Case Year",
							"Govt Behaviour (Pet/Res)", "Tax/Suit Value",
							"Pet Name", "HOD", "Subject Category",
							"Brief description(Minimum 50 chars) of the Case",
							"Stage", "Parawise/Counter Filing Authority");
					document.getElementById('petname').innerHTML = document
							.getElementById('petname').innerHTML
							+ "<font color=\"red\">*</font>";
				} else {
					//fieldRequired=new Array("natureofcase","caseType","caseNo","caseYear","isPetRes","suit","respNames","respAdvocates","subCategory","section","stDistrict","brfDiscription","stage","authpwca");
					fieldRequired = new Array("natureofcase", "caseType",
							"caseNo", "caseYear", "isPetRes", "suit",
							"respNames", "respAdvocates", "subCategory",
							"section", "brfDiscription", "stage", "authpwca");
					//fieldDescription =new  Array("Nature of case","Case Type","Case Number","Case Year","Govt Behaviour (Pet/Res)","Tax/Suit Value","Respondent Name","Respondent Advocate","HOD","Subject Category","Section","District","Brief description(Minimum 50 chars) of the Case","Stage","Parawise/Counter Filing Authority");
					//fieldDescription =new  Array("Nature of case","Case Type","Case Number","Case Year","Govt Behaviour (Pet/Res)","Tax/Suit Value","Respondent Name","Respondent Advocate","HOD","Subject Category","Section","Brief description(Minimum 50 chars) of the Case","Stage","Parawise/Counter Filing Authority");
					fieldDescription = new Array("Nature of case", "Case Type",
							"Case Number", "Case Year",
							"Govt Behaviour (Pet/Res)", "Tax/Suit Value",
							"Respondent Name", "Respondent Advocate", "HOD",
							"Subject Category",
							"Brief description(Minimum 50 chars) of the Case",
							"Stage", "Parawise/Counter Filing Authority");
					document.getElementById('respname').innerHTML = document
							.getElementById('respname').innerHTML
							+ "<font color=\"red\">*</font>";
				}
				// alert(fieldRequired);
				var result = formCheck(formObj, fieldRequired, fieldDescription);

				if (result) {
					if (document.forms[0].brfDiscription.value.length < 50) {
						alert("Please Enter Brief description Minimum 50 characters")
						document.forms[0].brfDiscription.focus();
						return false;
					}

					/* if(document.forms[0].stage.value=="Admit&Interim" ){
					   
						   if(document.forms[0].interimType.value=="" || document.forms[0].interimType.value=="0"){
						   alert("Please select Interim Type")
						   return false;
						   }else if(document.forms[0].interimNo.value==""){
						   alert("Please Enter Interim No")
						   return false;
						   }else if(document.forms[0].interimYear.value==""){
						   alert("Please select Interim year")
						   return false;
						   }else if(document.forms[0].interimDirection.value==""){
						   alert("Please select Interim Direction")
						   return false;
						   }else{
						   	if(!checkCaseInterim()){
						   		return false;
						   	}
						   	// return false;
						   }
					   } */

				}

				/* if(result){
				document.getElementById("imgLoad").style.display="";
				document.getElementById("buttonLoad").style.display="none";
				} */

				//document.forms[0].submitButton.disabled="true";
				//document.forms[0].action.value="insertORUpdate";
				//if(result==true)
				//	document.forms[0].submitButton.disabled="true";
				if (result == true) {
					var r = confirm("Do you want to submit data?");
					if (r == true) {
						document.getElementById("imgLoad").style.display = "";
						document.forms[0].submit();
					} else
						return false;
				} else
					return false;
			}

			function checkCaseInterim() {
				var ct = document.forms[0].caseType.value;
				var cn = document.forms[0].caseNo.value;
				var cy = document.forms[0].caseYear.value;

				//var it=document.forms[0].interimType.value;
				//var inn=document.forms[0].interimNo.value; 
				//var iy=document.forms[0].interimYear.value;

				if (ct == "" || cn == "" || cy == "") {

					if (ct == "") {
						alert("Enter Case type");
						return false;
					} else if (cn == "") {
						alert("Enter Case number");
						return false;
					} else if (cy == "") {
						alert("Enter Case Year");
						return false;
					}

				}

				/* else if(it=="" || inn=="" ||iy==""){		
					if(it==""){
						alert("Enter Interim type");	
						return false;
					}else if(inn==""){
						alert("Enter Interim number");	
						return false;
					}else if(iy==""){
						alert("Enter Interim Year");	
						return false;
					}
				} */

				else {
					var caseid = ct + "" + cn + "" + cy;
					//var interim=it+""+inn+""+iy;
				}
				return true;
			}

			function getDeptSections() {
				var url = "loadResource.do?restype=getSections&deptId="
						+ document.forms[0].deptHod.value; // sid=  Math.random()  prevent caching
				// alert("HI :: "+url)
				if (window.XMLHttpRequest) { // Non-IE browsers
					req1 = new XMLHttpRequest();
					req1.onreadystatechange = parseMandalListSe;
					try {

						req1.open("GET", url, true);
					} catch (e) {
						alert(e);
					}
					req1.send(null);
				} else if (window.ActiveXObject) { // IE
					req1 = new ActiveXObject("Microsoft.XMLHTTP");
					if (req1) {
						req1.onreadystatechange = parseMandalListSe;

						req1.open("GET", url, true);
						req1.send();
					}
				}
			}

			function parseMandalListSe() {

				if (req1.readyState == 4) { // Complete
					if (req1.status == 200) { // OK response

						document.getElementById("sectionIds").innerHTML = req1.responseText;
					} else {
						alert("Problem: " + req1.statusText);
					}
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

			function parsecasesListadvocates() {

				if (!(xmlHTTP.readyState == 4 || xmlHTTP.readyState == "complete"))
					return;

				var xmldoc = getResponseData(xmlHTTP)
				if (xmldoc) {
					var subcase = xmldoc.getElementsByTagName("advocate");
					document.getElementById(boxid).innerHTML = "";

					for ( var i = 0; i < subcase.length; i++) {
						var nameTextNode = subcase[i].childNodes[0].firstChild;
						var name = nameTextNode.nodeValue;
						var idTextNode = subcase[i].childNodes[1].firstChild;
						var id = (idTextNode != null) ? idTextNode.nodeValue
								: "";
						subdeptsList2[name + "::" + id] = new Array(id, name);
						var myoption = new Option(name, id);
						var len = document.getElementById(boxid).options.length
						document.getElementById(boxid).options[len] = myoption;
					}

					/**if(boxid=='petadvList'){
					document.getElementById(boxid).selectedIndex="<bean:write name="wpaffidavitform" property="petAdvocates"/>";
					}else**/
					if (boxid == "resadvList") {
						document.getElementById(boxid).selectedIndex = "<bean:write name="wpaffidavitform" property="respAdvocates"/>";
					}

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

			function parseMandalList9() {

				if (req.readyState == 4) { // Complete
					if (req.status == 200) { // OK response
						// alert(req.responseText)
						document.getElementById("authpw_ca").innerHTML = req.responseText;

					} else {

						alert("Problem: " + req.statusText);
					}
				}

			}
		</script>

		<script type="text/javascript">
		
		
		
		var xmlHTTP;
			var subdeptsList2 = new Array();

		function getCaseTypes(obj) {
				document.getElementById("caseIds").innerHTML = "<img src='images/ajax-loader.gif'/>";

				xmlHTTP = getXMLRequest();
				var url = "loadResource.do"
						+ "?sid=" + Math.random()
						+ "&restype=casetypes&cbo=true&flag=OriginalCase&courtId="+ obj.value; // sid=  Math.random()  prevent caching
				if (xmlHTTP) {
					xmlHTTP.onreadystatechange = parsecasesList;
					xmlHTTP.open('GET', url, true); // GET method
					xmlHTTP.send(null); // always use null for GET method
				}

			}

			function parsecasesList() {

				if (!(xmlHTTP.readyState == 4 || xmlHTTP.readyState == "complete"))
					return;
				var xmldoc = getResponseData(xmlHTTP)
				while (subdeptsList2.length > 0)
					subdeptsList2.pop();

				if (xmldoc) {
					var subcase = xmldoc.getElementsByTagName("subcase");
					//var interimTypes=xmldoc.getElementsByTagName("interims");
					document.getElementById("caseIds").innerHTML = "";//.length
					//document.getElementById("interims").innerHTML="";

					for ( var i = 0; i < subcase.length; i++) {
						var nameTextNode = subcase[i].childNodes[0].firstChild;
						var name = nameTextNode.nodeValue;
						var idTextNode = subcase[i].childNodes[1].firstChild;
						var id = (idTextNode != null) ? idTextNode.nodeValue
								: "";
						subdeptsList2[name + "::" + id] = new Array(id, name);
						var myoption = new Option(name, id);
						var len = document.getElementById("caseIds").options.length
						document.getElementById("caseIds").options[len] = myoption;
					}
					document.getElementById("caseIds").selectedIndex = "<bean:write name="wpaffidavitform" property="caseType"/>";

					/*for(var i=0;i<interimTypes.length; i++)
					{
						var nameTextNode = interimTypes[i].childNodes[0].firstChild;
						var name = nameTextNode.nodeValue;
						var idTextNode = interimTypes[i].childNodes[1].firstChild;
						var id = (idTextNode != null) ? idTextNode.nodeValue : "" ;
						subdeptsList2[name+"::"+id]=new Array(id, name);
						var myoption = new Option( name, id );
						var len = document.getElementById("interims").options.length
						document.getElementById("interims").options[len]=myoption;
					}
					document.getElementById("interims").selectedIndex="<bean:write name="wpaffidavitform" property="interimType"/>";
					
					 */

				} else
					alert("NO XMLRequest Found ! Cannot proceed !");

			}

			function getOtherSubs(obj) {
				if (obj.value == "Others") {
					document.getElementById('otherSubDiv1').style.display = "";
					document.getElementById('otherSubDiv2').style.display = "";
				} else {
					document.getElementById('otherSubDiv1').style.display = "none";
					document.getElementById('otherSubDiv2').style.display = "none";
				}

			}
		</script>
		<script type="text/javascript">
			function disableDistricts(val) {
				var obj = document.forms[0].stDistrict;

				if (val == "AndhraPradesh")
					for ( var i = 0; i < obj.length; i++) {
						if (obj[i].value != "AndhraPradesh") {

							if (obj[i].disabled == true)
								obj[i].disabled = false
							else
								obj[i].disabled = true;
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

			/* function setCaseNo(obj){
			 if(obj.value==document.forms[0].caseType.value)
			 document.forms[0].interimNo.value=document.forms[0].caseNo.value
			 else
			 document.forms[0].interimNo.value="";
			 return true;
			 } */
		</script>

	</html:form>
</body>
</html>