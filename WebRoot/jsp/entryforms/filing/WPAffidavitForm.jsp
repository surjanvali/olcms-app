<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>:: AP-FINANCE ::</title>

<!-- Bootstrap -->
<link href="<%=basePath%>css/bootstrap.min.css" rel="stylesheet">
<link href="<%=basePath%>css/style.css" rel="stylesheet">
<link href="<%=basePath%>css/menustyles.css" rel="stylesheet">
<link href="<%=basePath%>css/bootstrap-datepicker.min.css"
	rel="stylesheet">

<link rel="stylesheet" href="css/font-awesome.min.css">

<link href="<%=basePath%>css/multiselect/jquery.multiselect.css"
	rel="stylesheet">

<style type="text/css">
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

.case_heading {
	font-size: 17px;
	text-align: center;
	font-weight: bold;
	color: #641e16;
}

legend.scheduler-border-right {
	font-size: 1.2em !important;
	font-weight: bold !important;
	text-align: left !important;
	width: auto;
	padding: 0 10px;
	border-bottom: none;
	padding: 3px 65px;
	color: rgb(255, 255, 255);
	background: rgb(49, 91, 181);
	-webkit-clip-path: polygon(0% 0%, 75% 0%, 100% 50%, 75% 100%, 0 100%);
	clip-path: polygon(0% 0%, 75% 0%, 100% 50%, 75% 100%, 0 100%);
}

.mt20 {
	margin-top: 20px;
}

.mb20 {
	margin-bottom: 20px;
}
</style>
<script>

	function showDisposalColumn() {
		if (document.getElementById("caseStatus").value != null && document.getElementById("caseStatus").value == "2") {
			$("#disposalId").show();
			$("#disposalId2").show();
		} else {
			$("#disposalId").hide();
			$("#disposalId2").hide();
		}
	}
	function getLimitValue() {
		var length = $("#brfDiscription").val().length;
		var final_length = $("#brfDiscriptionlimit").val();
		final_length = final_length - length;
		if (final_length < length) {

		} else
			$("#brfDiscriptionlimit").val(1000 - length);

		var length = $("#remarks").val().length;
		var final_length = $("#remLimit").val();
		final_length = final_length - length;
		if (final_length < length) {

		} else
			$("#remLimit").val(1000 - length);

	}
</script>
</head>

<body>
	<div class="content-wrapper">


		<!-- Main content -->
		<div class="content">
			<div class="container bg-white" style="padding-top: 70px;">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 card">
						<html:form action="/WPAffidavit" styleId="WPAffidavitNew"
							enctype="multipart/form-data">
							<input type="hidden" id="action" name="action" />
							<html:hidden property="property(caseId)" styleId="caseId" />
							<html:hidden property="property(petnoOfRows)"
								styleId="petnoOfRows" />
							<html:hidden property="property(respnoOfRows)"
								styleId="respnoOfRows" />
							<html:hidden property="property(radiValue)" styleId="radiValue" />
							<br />
							<logic:equal value="caseList" property="property(dmode)"
								name="wpaffidavitnewform">
								<div class="container" style="background:#fff;align:right">

									<span style="float:right"><font size="4"><input
											type="button" value="ADD NEW CASE DETAILS"
											class="btn btn-primary" onclick="addNewCase()"
											style="font-weight: bold;" /> </font> </span>
								</div>
								<logic:present name="CaseDetails">
									<logic:present name="msg">
										<div>
											<span style="align:center"><font size="4"
												color="green">${msg
								}</font> </span>
										</div>
									</logic:present>
									<logic:present name="msg1">
										<div>
											<span style="align:center"><font size="4" color="red">${msg1
								}</font>
											</span>
										</div>
									</logic:present>
									<div class="case_heading">
										<span>LIST OF COURT CASES ENTERED </span>
									</div>
									<div class="box-body">
										<div class="inner-content-div"
											style="min-height: 20px;overflow: scroll;">
											<table style='font-size:12px' width="100%" id="example"
												class="table table-bordered table-striped">
												<thead style="background: #e5f7ff;">
													<tr>
														<th style="text-align: center">Sl.No</th>
														<th style="text-align: center">Court Name</th>
														<th style="text-align: center">Case ID</th>
														<th style="text-align: center">Government As
															Respondent/Petitioner</th>
														<th style="text-align: center">File No</th>
														<th style="text-align: center">Advocate Name</th>
														<th style="text-align: center">Date of Case Filing</th>
														<th style="text-align: center">Priority</th>
														<th style="text-align: center">Action</th>
													</tr>
												</thead>
												<tbody>

													<logic:iterate id="row" indexId="i" name="CaseDetails">
														<tr>
															<td>${i+1 }</td>
															<td>${row.COURT_NAME }</td>
															<td>${row.CASE_ID }</td>
															<td>${row.GOV_AS }</td>
															<td>${row.OFFICE_FILE_NO }</td>
															<td>${row.ADVACATE_NAME }</td>
															<td>${row.DATE_OF_PETI_RECV_DEPT }</td>
															<td>${row.PRIORITY }</td>
															<td><input type="button" value="Modify"
																onclick="modifyCaseDetails(${row.SNO})" /></td>
														</tr>
													</logic:iterate>
													<logic:notPresent name="CaseDetails">
														<tr>
															<td colspan="8">Details Not Found</td>
														</tr>
													</logic:notPresent>
												</tbody>
											</table>
										</div>
									</div>
								</logic:present>
							</logic:equal>
							<logic:notEqual value="caseList" property="property(dmode)"
								name="wpaffidavitnewform">
								<logic:notEqual value="modifyCaseList"
									property="property(dmode)" name="wpaffidavitnewform">

									<div>
										<span style="align:center"><font size="4" color="red">${msg1
						}</font>
										</span>
									</div>
									<div class="container "
										style="min-height:500px;background:#fff;">

										<div class="case_heading col-sm-6">
											<span class="pull-left">NEW CASE DETAILS ENTRY</span>
										</div>
										<div class="col-sm-6">
											<span style=""><font size="4"><input
													type="button" value="View & Modify Case Details"
													class="btn btn-success pull-right" onclick="getList()"
													style="font-weight: bold;" /> </font> </span>
										</div>
										<logic:present name="msg">
											<div>
												<span style="align:center"><font size="4"
													color="green">${msg
								}</font> </span>
											</div>
										</logic:present>
										<logic:present name="msg1">
											<div>
												<span style="align:center"><font size="4" color="red">${msg1
								}</font>
												</span>
											</div>
										</logic:present>
										<fieldset class="scheduler-border" style="clear: both;">
											<legend class="scheduler-border-left">Case Primary
												Details </legend>
											<input type="hidden" name="status" value=""> <input
												type="hidden" name="property(showHide)" value="No">

											<div class="row">
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.courttype" /> <bean:message key="mandatory" />
														</label>
														<html:select property="property(courtType)"
															styleClass="form-control" styleId="courtType"
															style="width:100%;">
															<logic:notEmpty property="property(courtList)"
																name="wpaffidavitnewform">
																<html:optionsCollection property="property(courtList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"><bean:message
																key="wpaffd.casetype" /> <bean:message key="mandatory" />
														</label>
														<html:select property="property(caseType)"
															styleClass="form-control" styleId="caseType"
															style="width:100%;">
															<logic:notEmpty property="property(caseTypesList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(caseTypesList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>


												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"><bean:message
																key="wpaffd.caseno" /> <bean:message key="mandatory" />
														</label>
														<html:text property="property(caseNo)" styleId="caseNo"
															size="25" styleClass="form-control" maxlength="20"
															style="width:100%;" onkeypress="intSlashHifnOnly(this);" />
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.caseyear" /> <bean:message key="mandatory" />
														</label>
														<html:select property="property(caseYear)"
															styleClass="form-control" styleId="caseYear"
															style="width:100%;">
															<logic:notEmpty property="property(fiftyYears)"
																name="wpaffidavitnewform">
																<html:optionsCollection property="property(fiftyYears)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
													<div class="form-group">
														<label for="sel1"> Office File No. : </label>
														<!-- <span class="ben-wrt">  </span> -->
														<input type="text" name="property(secyFileNo)"
															class="form-control" id="secyFileNo">
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1">SR Number <bean:message
																key="mandatory" />
														</label>
														<html:text property="property(caseSRNo)"
															styleId="caseSRNo" size="25" styleClass="form-control"
															maxlength="20" style="width:100%;"
															onkeypress="intSlashHifnOnly(this);" />
													</div>
												</div>


												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Filling Date : <bean:message
																key="mandatory" />
														</label>
														<div class="input-group date"
															data-date-format="dd.mm.yyyy">
															<input name="property(dtPetRecvByDept)"
																id="dtPetRecvByDept" type="text" class="form-control"
																readonly="readonly">
															<div class="input-group-addon">
																<span><i class="fa fa-calendar"
																	aria-hidden="true"></i> </span>
															</div>
														</div>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Registration Date : <bean:message
																key="mandatory" />
														</label>
														<div class="input-group date"
															data-date-format="dd.mm.yyyy">
															<input name="property(dateReg)" id="dateReg" type="text"
																class="form-control" readonly="readonly">
															<div class="input-group-addon">
																<span><i class="fa fa-calendar"
																	aria-hidden="true"></i> </span>
															</div>
														</div>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Listing Date : <bean:message
																key="mandatory" />
														</label>
														<div class="input-group date"
															data-date-format="dd.mm.yyyy">
															<input name="property(listingDate)" id="listingDate"
																type="text" class="form-control" readonly="readonly">
															<div class="input-group-addon">
																<span><i class="fa fa-calendar"
																	aria-hidden="true"></i> </span>
															</div>
														</div>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"><bean:message
																key="wpaffd.subcategory" /> <bean:message
																key="mandatory" /> </label>
														<html:select property="property(subCategory)"
															styleClass="form-control" styleId="subCategory"
															style="width:100%;">
															<logic:notEmpty property="property(subjectCategorysList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(subjectCategorysList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1">District <bean:message
																key="mandatory" />
														</label>
														<html:select property="property(districtId)"
															styleClass="form-control" styleId="districtId"
															style="width:100%;">
															<logic:notEmpty property="property(districtList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(districtList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.priority" /> <bean:message key="mandatory" />
														</label>
														<html:select property="property(priority)"
															styleClass="form-control" styleId="priority"
															style="width:100%;">
															<logic:notEmpty property="property(prioritiesList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(prioritiesList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>

												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Govt. As :<bean:message
																key="mandatory" />
														</label> <input type="radio" name="isPetRes" id="isPetRes"
															value="Petitioner">Petitioner <input type="radio"
															name="isPetRes" id="isPetRes" value="Respondent">Respondent
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Petitioner Advocate Name: <bean:message
																key="mandatory" />
														</label>
														<html:text property="property(petitionerAdvocateName)"
															styleClass="form-control"
															styleId="petitionerAdvocateName" style="width:100%;"
															size="25" onkeyup="validatetextCharacters(this)"
															maxlength="500" />
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
													<div class="form-group">
														<label for="sel1">Petitioner Advocate Code </label>
														<html:text property="property(petitionerAdvocateCode)"
															style="width:100%;" styleId="petitionerAdvocateCode"
															styleClass="form-control" size="25" maxlength="500" />
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Respondent Advocate Name: <bean:message
																key="mandatory" />
														</label>
														<html:text property="property(respAdvocate)"
															styleClass="form-control" styleId="respAdvocate"
															style="width:100%;" size="25"
															onkeyup="validatetextCharacters(this)" maxlength="500" />
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
													<div class="form-group">
														<label for="sel1">Respondent Advocate Code </label>
														<html:text property="property(respAdvocateCode)"
															style="width:100%;" styleId="respAdvocateCode"
															styleClass="form-control" size="25" maxlength="500" />
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.brfdiscrip" /> <bean:message
																key="mandatory" />
														</label>
														<html:textarea property="property(brfDiscription)"
															styleId="brfDiscription" cols="60"
															styleClass="form-control"
															onkeydown="textAreaCounter(this,document.getElementById('brfDiscriptionlimit'),1000)"
															onkeyup="return textToTrim(this);textAreaCounter(this,document.getElementById('brfDiscriptionlimit'),1000);charOnly(this);"
															onblur="return checkminimum(this,'Brief description of the Case')"></html:textarea>
														<input readonly="readonly" type="text"
															name="property(brfDiscriptionlimit)"
															id="brfDiscriptionlimit" size="3" value="1000" border="0"
															style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
													</div>

												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1">Case Status <bean:message
																key="mandatory" />
														</label>
														<html:select property="property(caseStatus)"
															styleClass="form-control" styleId="caseStatus"
															style="width:100%;" onchange="showDisposalColumn()">
															<logic:notEmpty property="property(caseStatusList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(caseStatusList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4"
													style="display:none" id="disposalId">
													<div class="form-group">
														<label for="sel1"> Disposal Date : <bean:message
																key="mandatory" />
														</label>
														<div class="input-group date"
															data-date-format="dd.mm.yyyy">
															<input name="property(disposalDate)" id="disposalDate"
																type="text" class="form-control" readonly="readonly">
															<div class="input-group-addon">
																<span><i class="fa fa-calendar"
																	aria-hidden="true"></i> </span>
															</div>
														</div>
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4"
													style="display:none" id="disposalId2">
													<div class="form-group">
														<label for="sel1">Disposal Type <bean:message
																key="mandatory" />
														</label>
														<html:select property="property(disposalType)"
															styleClass="form-control" styleId="disposalType"
															style="width:100%;">
															<logic:notEmpty property="property(disposalTypeList)"
																name="wpaffidavitnewform">
																<html:optionsCollection
																	property="property(disposalTypeList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>
											</div>
										</fieldset>


										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 "
												id="div1">
												<fieldset class="scheduler-border">
													<legend class="scheduler-border-left">
														Petitioner Details
														<bean:message key="mandatory" />
													</legend>
													<table id="table1" class="table_row table table-hover">
														<thead style="background: #e5f7ff;">
															<tr>
																<th>Sno</th>
																<th>Petitioner Name</th>
																<th>Address</th>
																<th>Parawise/Counter Filing Authority*</th>
																<th>Action</th>

															</tr>
														</thead>
														<tbody>
															<tr id="tr1_1">
																<td>1</td>
																<td><html:text property="property(petioner_name_1)"
																		styleClass="form-control" styleId="petioner_name_1"
																		style="width: 100%;"></html:text></td>
																<td><html:textarea
																		property="property(pet_address_1)"
																		styleClass="form-control" styleId="pet_address_1"
																		style="width: 100%;"></html:textarea></td>
																<td><html:checkbox
																		property="property(pet_chekbox_1)"
																		styleId="pet_chekbox_1" style=" margin-left: 43px; " /></td>
																<td><a href="javascript:addrow1(1);"><img
																		src="images/add.png" style="width: 25px; height:25px;" />
																</a></td>
															</tr>

														</tbody>
													</table>
												</fieldset>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 "
												id="div2">
												<fieldset class="scheduler-border">
													<legend class="scheduler-border-left">
														Respondent Details
														<bean:message key="mandatory" />
													</legend>
													<table id="table2" class="table_row table table-hover">
														<thead style="background: #e5f7ff;">
															<tr>
																<th>Sno</th>
																<th>Respondent Name</th>
																<th>Address</th>
																<th>Parawise/Counter Filing Authority*</th>
																<th>Action</th>

															</tr>
														</thead>
														<tbody>
															<tr id="tr2_1">
																<td><html:select
																		property="property(respondent_slno_1)"
																		styleClass="form-control" styleId="respondent_slno_1"
																		style="width: 100%;">
																		<html:option value="0">----</html:option>
																		<html:option value="1">1</html:option>
																		<html:option value="2">2</html:option>
																		<html:option value="3">3</html:option>
																		<html:option value="4">4</html:option>
																		<html:option value="5">5</html:option>
																		<html:option value="6">6</html:option>
																		<html:option value="7">7</html:option>
																		<html:option value="8">8</html:option>
																		<html:option value="9">9</html:option>
																		<html:option value="10">10</html:option>
																	</html:select></td>
																<td><html:text
																		property="property(respondent_name_1)"
																		styleClass="form-control" styleId="respondent_name_1"
																		style="width: 100%;"></html:text></td>
																<td><html:textarea
																		property="property(resp_address_1)"
																		styleClass="form-control" styleId="resp_address_1"
																		style="width: 100%;"></html:textarea></td>
																<td><html:checkbox
																		property="property(resp_chekbox_1)"
																		styleId="resp_chekbox_1" style=" margin-left: 43px; " /></td>
																<td align="center"><a href="javascript:addrow2(1);"><img
																		src="images/add.png" style="width: 25px; height:25px;" />
																</a></td>
															</tr>
														</tbody>
													</table>
												</fieldset>
											</div>
										</div>

									</div>
									<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
										<fieldset class="scheduler-border">
											<legend class="scheduler-border-left">Admission
												Details : </legend>
											<div class="row">
												<%-- <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
						<div class="form-group">
							<label for="sel1"> <bean:message key="wpaffd.dt_filing" />
							</label>
							<!-- <div class="input-group date" data-date-format="dd.mm.yyyy"> -->
							<div style="display: inline-flex;width: 100%;">
								<html:select property="property(dtFiling)"
									styleClass="form-control" styleId="dtFiling"
									style="width:33.33%;">
									<html:option value="">DD</html:option>
									<html:option value="0">00</html:option>
									<html:option value="1">01</html:option>
									<html:option value="2">02</html:option>
									<html:option value="3">03</html:option>
									<html:option value="4">04</html:option>
									<html:option value="5">05</html:option>
									<html:option value="6">06</html:option>
									<html:option value="7">07</html:option>
									<html:option value="8">08</html:option>
									<html:option value="9">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
									<html:option value="13">13</html:option>
									<html:option value="14">14</html:option>
									<html:option value="15">15</html:option>
									<html:option value="16">16</html:option>
									<html:option value="17">17</html:option>
									<html:option value="18">18</html:option>
									<html:option value="19">19</html:option>
									<html:option value="20">20</html:option>
									<html:option value="21">21</html:option>
									<html:option value="22">22</html:option>
									<html:option value="23">23</html:option>
									<html:option value="24">24</html:option>
									<html:option value="25">25</html:option>
									<html:option value="26">26</html:option>
									<html:option value="27">27</html:option>
									<html:option value="28">28</html:option>
									<html:option value="29">29</html:option>
									<html:option value="30">30</html:option>
									<html:option value="31">31</html:option>
								</html:select>
								&nbsp;&nbsp;&nbsp;


								<html:select property="property(mmFilling)"
									styleClass="form-control" styleId="mmFilling"
									style="width:33.33%;">
									<html:option value="">MM</html:option>
									<html:option value="0">00</html:option>
									<html:option value="1">01</html:option>
									<html:option value="2">02</html:option>
									<html:option value="3">03</html:option>
									<html:option value="4">04</html:option>
									<html:option value="5">05</html:option>
									<html:option value="6">06</html:option>
									<html:option value="7">07</html:option>
									<html:option value="8">08</html:option>
									<html:option value="9">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
								</html:select>
								&nbsp;&nbsp;&nbsp;

								<html:select property="property(yearFiling)"
									styleClass="form-control" styleId="yearFiling"
									style="width:33.33%;">
									<html:option value="">YYYY</html:option>
									<html:option value="0">00</html:option>
									<html:option value="2019">2019</html:option>
									<html:option value="2018">2018</html:option>
									<html:option value="2017">2017</html:option>
									<html:option value="2016">2016</html:option>
									<html:option value="2015">2015</html:option>
									<html:option value="2014">2014</html:option>
									<html:option value="2013">2013</html:option>
									<html:option value="2012">2012</html:option>
									<html:option value="2011">2011</html:option>
									<html:option value="2010">2010</html:option>
									<html:option value="2009">2009</html:option>
									<html:option value="2008">2008</html:option>
									<html:option value="2007">2007</html:option>
									<html:option value="2006">2006</html:option>
									<html:option value="2005">2005</html:option>
									<html:option value="2004">2004</html:option>
									<html:option value="2003">2003</html:option>
									<html:option value="2002">2002</html:option>
									<html:option value="2002">2001</html:option>
								</html:select>
								<!-- <input name="property(dtFiling)" id="dtFiling" type="text"
								class="form-control" readonly="readonly">
							<div class="input-group-addon">
								<span><i class="fa fa-calendar" aria-hidden="true"></i>
								</span>
							</div> -->

							</div>
						</div>
					</div> --%>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.stage" /> <bean:message key="mandatory" />
														</label>
														<html:select property="property(stage)"
															styleClass="form-control" styleId="stage"
															style="width:100%;">
															<logic:notEmpty property="property(stagesList)"
																name="wpaffidavitnewform">
																<html:optionsCollection property="property(stagesList)"
																	name="wpaffidavitnewform" />
															</logic:notEmpty>
														</html:select>
													</div>
												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> <bean:message
																key="wpaffd.dt_listing" />
														</label>
														<div class="input-group date"
															data-date-format="dd.mm.yyyy">
															<input name="property(dtListing)" id="dtListing"
																type="text" class="form-control" readonly="readonly">
															<div class="input-group-addon">
																<span><i class="fa fa-calendar"
																	aria-hidden="true"></i> </span>
															</div>
														</div>
													</div>
												</div>


												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Hon'ble Judges </label>
														<html:text property="property(judges)"
															style="width: 100%;" styleClass="form-control"
															styleId="judges" />
													</div>

												</div>
											</div>
											<div class="row">
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Connected Case Numbers </label>
														<html:textarea property="property(connectedCaseNumber)"
															style="width: 100%;" styleClass="form-control"
															styleId="connectedCaseNumber"></html:textarea>
													</div>

												</div>
												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Remarks </label>
														<html:textarea property="property(remarks)"
															style="width: 100%;" styleClass="form-control"
															onkeydown="textToTrim(this,remLimit); return textAreaCounter(this,document.getElementById('remLimit'),1000)"
															onkeyup=" textToTrim(this,remLimit);return textAreaCounter(this,document.getElementById('remLimit'),1000);charOnly(this);"
															styleId="remarks"
															onblur="return checkminimum(this,'Remarks')"></html:textarea>
														<input readonly="readonly" type="text" name="remLimit"
															id="remLimit" size="3" value="1000" border="0"
															style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
													</div>

												</div>

												<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
													<div class="form-group">
														<label for="sel1"> Upload scanned copy of Case
															file </label>
														<html:file property="property(document)" />
													</div>

												</div>
											</div>
										</fieldset>
									</div>

									<div
										class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mb20 pull-right">
										<input type="button" name="btn" value=" Submit "
											onclick="submitData();" class="btn btn-danger text-center"
											id="btn_submit">
									</div>
								</logic:notEqual>
							</logic:notEqual>

							<logic:equal value="modifyCaseList" property="property(dmode)"
								name="wpaffidavitnewform">


								<div class="row">
									<div class="case_heading">
										<span>UPDATE CASE PARTICULARS </span>
									</div>
								</div>
								<div>
									<span style="align:center"><font size="4" color="red">${msg1
						}</font>
									</span>
								</div>
								<div class="container "
									style="min-height:500px;background:#fff;">
									<div style="float:right;margin-top:10px;">
										<span style="align:right"><font size="4"><input
												type="button" value="Back TO Case List"
												class="btn btn-primary" onclick="getList()"
												style="font-weight: bold;" /> </font> </span> &nbsp;&nbsp; <span
											style="align:right"><font size="4"><input
												type="button" value="ADD NEW CASE DETAILS"
												class="btn btn-primary" onclick="addNewCase()"
												style="font-weight: bold;" /> </font> </span>
									</div>
									<logic:present name="msg">
										<div>
											<span style="align:center"><font size="4"
												color="green">${msg
								}</font> </span>
										</div>
									</logic:present>
									<logic:present name="msg1">
										<div>
											<span style="align:center"><font size="4" color="red">${msg1
								}</font>
											</span>
										</div>
									</logic:present>
									<fieldset class="scheduler-border" style="clear: both;">
										<legend class="scheduler-border">Case Particulars </legend>
										<input type="hidden" name="status" value=""> <input
											type="hidden" name="property(showHide)" value="No">

										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> <bean:message
															key="wpaffd.courttype" /> <bean:message key="mandatory" />
													</label>
													<html:select property="property(courtType)"
														styleClass="form-control" styleId="courtType"
														style="width:100%;">
														<logic:notEmpty property="property(courtList)"
															name="wpaffidavitnewform">
															<html:optionsCollection property="property(courtList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"><bean:message
															key="wpaffd.casetype" /> <bean:message key="mandatory" />
													</label>
													<html:select property="property(caseType)"
														styleClass="form-control" styleId="caseType"
														style="width:100%;">
														<logic:notEmpty property="property(caseTypesList)"
															name="wpaffidavitnewform">
															<html:optionsCollection
																property="property(caseTypesList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>


											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"><bean:message key="wpaffd.caseno" />
														<bean:message key="mandatory" /> </label>
													<html:text property="property(caseNo)" styleId="caseNo"
														size="25" styleClass="form-control" maxlength="20"
														style="width:100%;" onkeypress="intSlashHifnOnly(this);" />
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
												<div class="form-group">
													<label for="sel1"> <bean:message
															key="wpaffd.caseyear" /> <bean:message key="mandatory" />
													</label>
													<html:select property="property(caseYear)"
														styleClass="form-control" styleId="caseYear"
														style="width:100%;">
														<logic:notEmpty property="property(fiftyYears)"
															name="wpaffidavitnewform">
															<html:optionsCollection property="property(fiftyYears)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
												<div class="form-group">
													<label for="sel1"> Office File No. : </label>
													<!-- <span class="ben-wrt">  </span> -->
													<html:text property="property(secyFileNo)"
														styleId="secyFileNo" size="25" styleClass="form-control"
														style="width:100%;" />
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1">SR Number <bean:message
															key="mandatory" />
													</label>
													<html:text property="property(caseSRNo)" styleId="caseSRNo"
														size="25" styleClass="form-control" maxlength="20"
														style="width:100%;" onkeypress="intSlashHifnOnly(this);" />
												</div>
											</div>


											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Filling Date : <bean:message
															key="mandatory" />
													</label>
													<div class="input-group date" data-date-format="dd.mm.yyyy">
														<html:text property="property(dtPetRecvByDept)"
															styleId="dtPetRecvByDept" size="25"
															styleClass="form-control" maxlength="20"
															style="width:100%;" readonly="readonly" />
														<div class="input-group-addon">
															<span><i class="fa fa-calendar" aria-hidden="true"></i>
															</span>
														</div>
													</div>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Registration Date : <bean:message
															key="mandatory" />
													</label>
													<div class="input-group date" data-date-format="dd.mm.yyyy">
														<html:text property="property(dateReg)" styleId="dateReg"
															size="25" styleClass="form-control" maxlength="20"
															style="width:100%;" readonly="readonly" />
														<div class="input-group-addon">
															<span><i class="fa fa-calendar" aria-hidden="true"></i>
															</span>
														</div>
													</div>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Listing Date : <bean:message
															key="mandatory" />
													</label>
													<div class="input-group date" data-date-format="dd.mm.yyyy">
														<html:text property="property(listingDate)"
															styleId="listingDate" size="25" styleClass="form-control"
															maxlength="20" style="width:100%;" readonly="readonly" />
														<div class="input-group-addon">
															<span><i class="fa fa-calendar" aria-hidden="true"></i>
															</span>
														</div>
													</div>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"><bean:message
															key="wpaffd.subcategory" /> <bean:message
															key="mandatory" /> </label>
													<html:select property="property(subCategory)"
														styleClass="form-control" styleId="subCategory"
														style="width:100%;">
														<logic:notEmpty property="property(subjectCategorysList)"
															name="wpaffidavitnewform">
															<html:optionsCollection
																property="property(subjectCategorysList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1">District <bean:message
															key="mandatory" />
													</label>
													<html:select property="property(districtId)"
														styleClass="form-control" styleId="districtId"
														style="width:100%;">
														<logic:notEmpty property="property(districtList)"
															name="wpaffidavitnewform">
															<html:optionsCollection property="property(districtList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> <bean:message
															key="wpaffd.priority" /> <bean:message key="mandatory" />
													</label>
													<html:select property="property(priority)"
														styleClass="form-control" styleId="priority"
														style="width:100%;">
														<logic:notEmpty property="property(prioritiesList)"
															name="wpaffidavitnewform">
															<html:optionsCollection
																property="property(prioritiesList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>

											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Govt As :<bean:message
															key="mandatory" />
													</label> <input type="radio" name="isPetRes" id="isPetRes"
														value="Petitioner">Petitioner <input type="radio"
														name="isPetRes" id="isPetRes" value="Respondent">Respondent
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Petitioner Advocate Name: <bean:message
															key="mandatory" />
													</label>
													<html:text property="property(petitionerAdvocateName)"
														styleClass="form-control" styleId="petitionerAdvocateName"
														style="width:100%;" size="25"
														onkeyup="validatetextCharacters(this)" maxlength="500" />
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
												<div class="form-group">
													<label for="sel1">Petitioner Advocate Code </label>
													<html:text property="property(petitionerAdvocateCode)"
														style="width:100%;" styleId="petitionerAdvocateCode"
														styleClass="form-control" size="25" maxlength="500" />
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Respondent Advocate Name: <bean:message
															key="mandatory" />
													</label>
													<html:text property="property(respAdvocate)"
														styleClass="form-control" styleId="respAdvocate"
														style="width:100%;" size="25"
														onkeyup="validatetextCharacters(this)" maxlength="500" />
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
												<div class="form-group">
													<label for="sel1">Respondent Advocate Code </label>
													<html:text property="property(respAdvocateCode)"
														style="width:100%;" styleId="respAdvocateCode"
														styleClass="form-control" size="25" maxlength="500" />
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> <bean:message
															key="wpaffd.brfdiscrip" /> <bean:message key="mandatory" />
													</label>
													<html:textarea property="property(brfDiscription)"
														styleId="brfDiscription" cols="60"
														styleClass="form-control"
														onkeydown="textAreaCounter(this,document.getElementById('brfDiscriptionlimit'),1000)"
														onkeyup="return textToTrim(this);textAreaCounter(this,document.getElementById('brfDiscriptionlimit'),1000);charOnly(this);"
														onblur="return checkminimum(this,'Brief description of the Case')"></html:textarea>
													<input readonly="readonly" type="text"
														name="property(brfDiscriptionlimit)"
														id="brfDiscriptionlimit" size="3" value="1000" border="0"
														style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
												</div>

											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1">Case Status <bean:message
															key="mandatory" />
													</label>
													<html:select property="property(caseStatus)"
														styleClass="form-control" styleId="caseStatus"
														style="width:100%;" onchange="showDisposalColumn()">
														<logic:notEmpty property="property(caseStatusList)"
															name="wpaffidavitnewform">
															<html:optionsCollection
																property="property(caseStatusList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>

											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4"
												style="display:none" id="disposalId">
												<div class="form-group">
													<label for="sel1"> Disposal Date : <bean:message
															key="mandatory" />
													</label>
													<div class="input-group date" data-date-format="dd.mm.yyyy">
														<input name="property(disposalDate)" id="disposalDate"
															type="text" class="form-control" readonly="readonly">
														<div class="input-group-addon">
															<span><i class="fa fa-calendar" aria-hidden="true"></i>
															</span>
														</div>
													</div>
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4"
												style="display:none" id="disposalId2">
												<div class="form-group">
													<label for="sel1">Disposal Type <bean:message
															key="mandatory" />
													</label>
													<html:select property="property(disposalType)"
														styleClass="form-control" styleId="disposalType"
														style="width:100%;">
														<logic:notEmpty property="property(disposalTypeList)"
															name="wpaffidavitnewform">
															<html:optionsCollection
																property="property(disposalTypeList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>
										</div>

									</fieldset>
									<fieldset class="scheduler-border">
										<legend class="scheduler-border">Petition Information
											: </legend>


										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 "
												id="div1">
												<fieldset class="scheduler-border">
													<legend class="scheduler-border-left">
														Petitioner Details
														<bean:message key="mandatory" />
													</legend>
													<table id="table1" class="table_row table table-hover">
														<thead style="background: #e5f7ff;">
															<tr>
																<th>Sno</th>
																<th>Petitioner Name dfdd</th>
																<th>Address</th>
																<th>Parawise/Counter Filing Authority*</th>
																<th><a href="javascript:addrow1(1);"><img
																		src="images/add.png" style="width: 25px; height:25px;" />
																</a></th>

															</tr>
														</thead>
														<tbody>

															<logic:present name="PETITIONERS">
																<logic:iterate id="itr1" name="PETITIONERS" indexId="j">

																	<tr id="tr1_${j+1 }">
																		<td>${j+1 }</td>
																		<td><html:text
																				property="property(petioner_name_${j+1 })"
																				styleClass="form-control"
																				styleId="petioner_name_${j+1 }" style="width: 100%;"></html:text></td>
																		<td><html:textarea
																				property="property(pet_address_${j+1 })"
																				styleClass="form-control"
																				styleId="pet_address_${j+1 }" style="width: 100%;"></html:textarea></td>
																		<td><html:checkbox
																				property="property(pet_chekbox_${j+1 })"
																				styleId="pet_chekbox_${j+1 }"
																				style=" margin-left: 43px; " /></td>
																		<td></td>
																	</tr>
																</logic:iterate>
															</logic:present>
															<logic:notPresent name="PETITIONERS">
																<tr id="tr1_1">
																	<td>1</td>
																	<td><html:text
																			property="property(petioner_name_1)"
																			styleClass="form-control" styleId="petioner_name_1"
																			style="width: 100%;"></html:text></td>
																	<td><html:textarea
																			property="property(pet_address_1)"
																			styleClass="form-control" styleId="pet_address_1"
																			style="width: 100%;"></html:textarea></td>
																	<td><html:checkbox
																			property="property(pet_chekbox_1)"
																			styleId="pet_chekbox_1" style=" margin-left: 43px; " /></td>
																	<td></td>
																</tr>
															</logic:notPresent>
														</tbody>
													</table>
												</fieldset>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 "
												id="div2">
												<fieldset class="scheduler-border">
													<legend class="scheduler-border-right">
														Respondent Details
														<bean:message key="mandatory" />
													</legend>
													<table id="table2" class="table_row table table-hover">
														<thead style="background: #e5f7ff;">
															<tr>
																<th>Sno</th>
																<th>Respondent Name</th>
																<th>Address</th>
																<th>Parawise/Counter Filing Authority*</th>
																<th><a href="javascript:addrow2(1);"><img
																		src="images/add.png" style="width: 25px; height:25px;" />
																</a></th>

															</tr>
														</thead>
														<tbody>
															<logic:present name="RESPONDENTS">
																<logic:iterate id="itr1" name="RESPONDENTS" indexId="j">
																	<tr id="tr2_${j+1 }">
																		<td><html:select
																				property="property(respondent_slno_${j+1 })"
																				styleClass="form-control"
																				styleId="respondent_slno_${j+1 }"
																				style="width: 100%;">
																				<html:option value="0">----</html:option>
																				<html:option value="1">1</html:option>
																				<html:option value="2">2</html:option>
																				<html:option value="3">3</html:option>
																				<html:option value="4">4</html:option>
																				<html:option value="5">5</html:option>
																				<html:option value="6">6</html:option>
																				<html:option value="7">7</html:option>
																				<html:option value="8">8</html:option>
																				<html:option value="9">9</html:option>
																				<html:option value="10">10</html:option>
																			</html:select></td>
																		<td><html:text
																				property="property(respondent_name_${j+1 })"
																				styleClass="form-control"
																				styleId="respondent_name_${j+1 }"
																				style="width: 100%;"></html:text></td>
																		<td><html:textarea
																				property="property(resp_address_${j+1 })"
																				styleClass="form-control"
																				styleId="resp_address_${j+1 }" style="width: 100%;"></html:textarea></td>
																		<td><html:checkbox
																				property="property(resp_chekbox_${j+1 })"
																				styleId="resp_chekbox_${j+1 }"
																				style=" margin-left: 43px; " /></td>
																		<td align="center"></td>
																	</tr>

																</logic:iterate>
															</logic:present>

															<logic:notPresent name="RESPONDENTS">
																<tr id="tr2_1">
																	<td><html:select
																			property="property(respondent_slno_1)"
																			styleClass="form-control" styleId="respondent_slno_1"
																			style="width: 100%;">
																			<html:option value="0">----</html:option>
																			<html:option value="1">1</html:option>
																			<html:option value="2">2</html:option>
																			<html:option value="3">3</html:option>
																			<html:option value="4">4</html:option>
																			<html:option value="5">5</html:option>
																			<html:option value="6">6</html:option>
																			<html:option value="7">7</html:option>
																			<html:option value="8">8</html:option>
																			<html:option value="9">9</html:option>
																			<html:option value="10">10</html:option>
																		</html:select></td>
																	<td><html:text
																			property="property(respondent_name_1)"
																			styleClass="form-control" styleId="respondent_name_1"
																			style="width: 100%;"></html:text></td>
																	<td><html:textarea
																			property="property(resp_address_1)"
																			styleClass="form-control" styleId="resp_address_1"
																			style="width: 100%;"></html:textarea></td>
																	<td><html:checkbox
																			property="property(resp_chekbox_1)"
																			styleId="resp_chekbox_1" style=" margin-left: 43px; " /></td>
																	<td align="center"><a
																		href="javascript:addrow2(1);"><img
																			src="images/add.png"
																			style="width: 25px; height:25px;" /> </a></td>
																</tr>
															</logic:notPresent>
														</tbody>
													</table>
												</fieldset>
											</div>
										</div>

									</fieldset>
								</div>

								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
									<fieldset class="scheduler-border">
										<legend class="scheduler-border">Admission Details :
										</legend>
										<div class="row">
											<%-- <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4  ">
						<div class="form-group">
							<label for="sel1"> <bean:message key="wpaffd.dt_filing" />
							</label>
							<!-- <div class="input-group date" data-date-format="dd.mm.yyyy"> -->
							<div style="display: inline-flex;width: 100%;">
								<html:select property="property(dtFiling)"
									styleClass="form-control" styleId="dtFiling"
									style="width:33.33%;">
									<html:option value="">DD</html:option>
									<html:option value="0">00</html:option>
									<html:option value="1">01</html:option>
									<html:option value="2">02</html:option>
									<html:option value="3">03</html:option>
									<html:option value="4">04</html:option>
									<html:option value="5">05</html:option>
									<html:option value="6">06</html:option>
									<html:option value="7">07</html:option>
									<html:option value="8">08</html:option>
									<html:option value="9">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
									<html:option value="13">13</html:option>
									<html:option value="14">14</html:option>
									<html:option value="15">15</html:option>
									<html:option value="16">16</html:option>
									<html:option value="17">17</html:option>
									<html:option value="18">18</html:option>
									<html:option value="19">19</html:option>
									<html:option value="20">20</html:option>
									<html:option value="21">21</html:option>
									<html:option value="22">22</html:option>
									<html:option value="23">23</html:option>
									<html:option value="24">24</html:option>
									<html:option value="25">25</html:option>
									<html:option value="26">26</html:option>
									<html:option value="27">27</html:option>
									<html:option value="28">28</html:option>
									<html:option value="29">29</html:option>
									<html:option value="30">30</html:option>
									<html:option value="31">31</html:option>
								</html:select>
								&nbsp;&nbsp;&nbsp;


								<html:select property="property(mmFilling)"
									styleClass="form-control" styleId="mmFilling"
									style="width:33.33%;">
									<html:option value="">MM</html:option>
									<html:option value="0">00</html:option>
									<html:option value="1">01</html:option>
									<html:option value="2">02</html:option>
									<html:option value="3">03</html:option>
									<html:option value="4">04</html:option>
									<html:option value="5">05</html:option>
									<html:option value="6">06</html:option>
									<html:option value="7">07</html:option>
									<html:option value="8">08</html:option>
									<html:option value="9">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
								</html:select>
								&nbsp;&nbsp;&nbsp;

								<html:select property="property(yearFiling)"
									styleClass="form-control" styleId="yearFiling"
									style="width:33.33%;">
									<html:option value="">YYYY</html:option>
									<html:option value="0">00</html:option>
									<html:option value="2019">2019</html:option>
									<html:option value="2018">2018</html:option>
									<html:option value="2017">2017</html:option>
									<html:option value="2016">2016</html:option>
									<html:option value="2015">2015</html:option>
									<html:option value="2014">2014</html:option>
									<html:option value="2013">2013</html:option>
									<html:option value="2012">2012</html:option>
									<html:option value="2011">2011</html:option>
									<html:option value="2010">2010</html:option>
									<html:option value="2009">2009</html:option>
									<html:option value="2008">2008</html:option>
									<html:option value="2007">2007</html:option>
									<html:option value="2006">2006</html:option>
									<html:option value="2005">2005</html:option>
									<html:option value="2004">2004</html:option>
									<html:option value="2003">2003</html:option>
									<html:option value="2002">2002</html:option>
									<html:option value="2002">2001</html:option>
								</html:select>
								<!-- <input name="property(dtFiling)" id="dtFiling" type="text"
								class="form-control" readonly="readonly">
							<div class="input-group-addon">
								<span><i class="fa fa-calendar" aria-hidden="true"></i>
								</span>
							</div> -->

							</div>
						</div>
					</div> --%>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> <bean:message
															key="wpaffd.dt_listing" />
													</label>
													<div class="input-group date" data-date-format="dd.mm.yyyy">
														<html:text property="property(dtListing)"
															styleId="dtListing" styleClass="form-control"
															readonly="readonly" />
														<div class="input-group-addon">
															<span><i class="fa fa-calendar" aria-hidden="true"></i>
															</span>
														</div>
													</div>
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> <bean:message key="wpaffd.stage" />
														<bean:message key="mandatory" />
													</label>
													<html:select property="property(stage)"
														styleClass="form-control" styleId="stage"
														style="width:100%;">
														<logic:notEmpty property="property(stagesList)"
															name="wpaffidavitnewform">
															<html:optionsCollection property="property(stagesList)"
																name="wpaffidavitnewform" />
														</logic:notEmpty>
													</html:select>
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Hon'ble Judges </label>
													<html:text property="property(judges)" style="width: 100%;"
														styleClass="form-control" styleId="judges" />
												</div>

											</div>
										</div>
										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Connected Case Numbers </label>
													<html:textarea property="property(connectedCaseNumber)"
														style="width: 100%;" styleClass="form-control"
														styleId="connectedCaseNumber"></html:textarea>
												</div>
											</div>
											<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">
												<div class="form-group">
													<label for="sel1"> Remarks </label>
													<html:textarea property="property(remarks)"
														style="width: 100%;" styleClass="form-control"
														onkeydown="textToTrim(this,remLimit); return textAreaCounter(this,document.getElementById('remLimit'),1000)"
														onkeyup=" textToTrim(this,remLimit);return textAreaCounter(this,document.getElementById('remLimit'),1000);charOnly(this);"
														styleId="remarks"
														onblur="return checkminimum(this,'Remarks')"></html:textarea>
													<input readonly="readonly" type="text" name="remLimit"
														id="remLimit" size="3" value="1000" border="0"
														style="background-color: #cccccc; border: 0px solid; font-size: 11px; font-family: sans-serif; font-style: normal;">
												</div>

											</div>

											<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 ">
												<div class="form-group">
													<label for="sel1"> Upload scanned copy of Case file
													</label>

													<logic:notEmpty property="property(documentUploaded)"
														name="wpaffidavitnewform">
														<html:hidden property="property(documentUploaded)"
															styleId="documentUploaded" name="wpaffidavitnewform" />
														<a
															href="<%=basePath%><bean:write property='property(documentUploaded)' name='wpaffidavitnewform'/>"
															target="_blank">View document uploaded earlier</a>
													</logic:notEmpty>
													<html:file property="property(document)" />
												</div>

											</div>
										</div>
									</fieldset>
								</div>
								<script>
									showDisposalColumn();getLimitValue();
								</script>
								<div
									class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-right">
									<input type="button" name="btn" value=" UPDATE "
										onclick="updateData();" class="btn btn-primary text-center"
										id="btn_submit">
								</div>
							</logic:equal>
						</html:form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="jQuery/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/script.js"></script>
	<script src="js/multiselect/jquery.multiselect.js"></script>
	<script src="js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript">
			
		
			$(".input-group.date").datepicker({
				format : "dd/mm/yyyy"
			});

			$(function() {

				$("#courtType")
						.change(
								function() {
									var theResponse = null;
									$.ajax({
												type : 'POST',
												url : "WPAffidavit.do"
														+ '?action=ajax&courtType='
														+ $(this).val()
														+ '&type=caseType',
												dataType : "html",
												async : false,
												success : function(res) {
													theResponse = res;
													document
															.getElementById("caseType").innerHTML = res;
												},
												error : function() {
													alert('Error occured');
												}
											});
								});

				
			});
			$("#brfDiscription").keypress(function() {
					var length = $("#brfDiscription").val().length;
					var final_length = $("#brfDiscriptionlimit").val();
					final_length = final_length - length;
					if (final_length < length) {

					} else
						$("#brfDiscriptionlimit").val(1000 - length);

				});
			$("#remarks").keypress(function() {
					var length = $("#remarks").val().length;
					var final_length = $("#remLimit").val();
					final_length = final_length - length;
					if (final_length < length) {

					} else
						$("#remLimit").val(1000 - length);

				});
			
			function limitText(gpOpinion, limitCount, limitNum) {
				if (gpOpinion.value.length > limitNum) {
					gpOpinion.value = gpOpinion.value.substring(0, limitNum);
				} else {
					limitCount.value = limitNum - gpOpinion.value.length;
				}
			}
			/* function textToTrim(i,label) {
			alert();
				i.value = i.value.trim();
				textAreaCounter(i, document.getElementById('+label+'), 1000);
			}
			 */
			var arrVal1 = [];
			arrVal1.splice(0, 1, 1);
			function addrow1() {
				var noOfRows = $("#table1 tr").length;
				arrVal1.splice(noOfRows - 1, 1, noOfRows);
				var tr = "<tr id='tr1_"+noOfRows+"'>" + "<td>"
						+ noOfRows
						+ "</td>"

						+ "<td><input type=\"text\" name=\"property(petioner_name_"
						+ noOfRows
						+ ")\" class='form-control' id=\"petioner_name_"
						+ noOfRows
						+ "\"  style='width: 100%;'  onblur=\"getallowanceAmount(this,"
						+ noOfRows
						+ ")\" /></td>"
						+ "<td><textarea  name=\"property(pet_address_"
						+ noOfRows
						+ ")\" class='form-control' id=\"pet_address_"
						+ noOfRows
						+ "\"  style='width:100%;'   /></td>"
						+ "<td><input type=\"checkbox\" name=\"property(pet_chekbox_"
						+ noOfRows
						+ ")\" id=\"pet_chekbox_"
						+ noOfRows
						+ "\"  style='width:100px;'   /></td>"
						+ "<td><a href='javascript:deleterow1("
						+ noOfRows
						+ ");'><img src='images/delete.png' style='width: 25px; height:25px;' /></a></td>"
						+ "</tr>";
				$("#table1").append(tr);
				$("select[name='property(allowanceName_" + noOfRows + ")']")
						.html(
								$("select[name='property(allowanceName_1)']")
										.html());
				$("select[name='property(allowanceType_" + noOfRows + ")']")
						.html(
								$("select[name='property(allowanceType_1)']")
										.html());
			}

			function deleterow1(id) {
				arrVal1.splice(id - 1, 1);
				$("#tr1_" + id).remove();
			}

			function addrow2() {
				var noOfRows = $("#table2 tr").length;
				arrVal1.splice(noOfRows - 1, 1, noOfRows);
				var tr = "<tr id='tr2_"+noOfRows+"'>" + "<td>"
						//+ noOfRows
						+ "   <select name=\"property(respondent_slno_"+ noOfRows+ ")\" id='respondent_slno_"+noOfRows+"' style='width: 100%;' class='form-control'><option value='0'>----</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option></select>  </td>"
						+ "<td><input type=\"text\" name=\"property(respondent_name_"+ noOfRows+ ")\" class='form-control' id=\"respondent_name_"+ noOfRows+ "\"  style='width: 100p%;'  onblur=\"getallowanceAmount(this,"
						+ noOfRows
						+ ")\" /></td>"
						+ "<td><textarea  name=\"property(resp_address_"
						+ noOfRows
						+ ")\" class='form-control' id=\"resp_address_"
						+ noOfRows
						+ "\"  style='width:100p%;'   /></td>"
						+ "<td><input type=\"checkbox\" name=\"property(resp_chekbox_"
						+ noOfRows
						+ ")\" id=\"resp_chekbox_"
						+ noOfRows
						+ "\"  style='width:100px;'   /></td>"
						+ "<td><a href='javascript:deleterow2("
						+ noOfRows
						+ ");'><img src='images/delete.png' style='width: 25px; height:25px;' /></a></td>"
						+ "</tr>";
				$("#table2").append(tr);
				$("select[name='property(allowanceName_" + noOfRows + ")']")
						.html(
								$("select[name='property(allowanceName_1)']")
										.html());
				$("select[name='property(allowanceType_" + noOfRows + ")']")
						.html(
								$("select[name='property(allowanceType_1)']")
										.html());
			}
			function deleterow2(id) {
				arrVal1.splice(id - 1, 1);
				$("#tr2_" + id).remove();
			}

			function charOnly(i) {
				alert(i);
				if (i.value.length > 0) {
					i.value = i.value.replace(/[^a-zA-z-.()0-9\s]+/g, '');
				}
			}
			function validatetextCharacters(txb) {
				txb.value = txb.value.replace(/[^\A-Za-z\-.() ]/g, '');
			}

			function checkminimum(obj, label) {
				if (obj.value != "") {
					if ((obj.value).length < 50) {
						alert("Please Enter " + label
								+ " Minimum 50 characters");
						//document.forms[0].brfDiscription.focus();
						return false;
					}

				}

			}
			function numericOnly(txb) {
				txb.value = txb.value.replace(/[^\d]/g, '');
			}
			
	
	function charOnly(i) 
	{
		if(i.value.length>0) 
		{
		i.value = i.value.replace(/[^\sA-Za-z.\/-]+/g, ''); 
		}
	}
	
	function isNumberKey(evt)
	{
	    var charCode = (evt.which) ? evt.which : event.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	        return false;
	    return true;
	}
		
	function intOnly(i) 
	{
		if(i.value.length>0) 
		{ 
			i.value = i.value.replace(/[^\d]+/g, ''); 
		}
	}
		
	function trim(s) 
	{
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
	}
	

			function submitData() {

				var radiValue = $('input[name=isPetRes]:checked').val();
				var petnoOfRows = $("#table1 tr").length;
				petnoOfRows = petnoOfRows - 1;
				$("#petnoOfRows").val(petnoOfRows);
				var respnoOfRows = $("#table2 tr").length;
				respnoOfRows = respnoOfRows - 1;
				$("#respnoOfRows").val(respnoOfRows);
				$("#radiValue").val(radiValue);
				/* alert(radiValue); */
				if (document.getElementById("courtType").value != null
						&& document.getElementById("courtType").value == "0") {
					alert("Select Court ");
					document.getElementById("courtType").focus();
					return false;
				} else if (document.getElementById("caseType").value != null
						&& document.getElementById("caseType").value == "0") {
					alert(" Select Nature of Petition ");
					document.getElementById("caseType").focus();
					return false;
				} else if (document.getElementById("caseNo").value != null
						&& document.getElementById("caseNo").value == "") {
					alert("Enter Case NO");
					document.getElementById("caseNo").focus();
					return false;
				} else if (document.getElementById("caseYear").value != null
						&& document.getElementById("caseYear").value == "0") {
					alert("Select Case Year");
					document.getElementById("caseYear").focus();
					return false;
				} 
				
				else if (document.forms[0].isPetRes[0].checked==false && document.forms[0].isPetRes[1].checked==false){
					
					alert("select whether Govt As Petitioner / Respondent");
					document.forms[0].isPetRes[0].focus();
					return false;
				}
				
				
				else if (document.getElementById("respAdvocate").value != null
						&& document.getElementById("respAdvocate").value == "") {
					alert("Enter Advacate-on-record ");
					document.getElementById("respAdvocate").focus();
					return false;
				} else if (document.getElementById("dtPetRecvByDept").value != null
						&& document.getElementById("dtPetRecvByDept").value == "0") {
					alert("Enter Date of Petition recieved by the Dept.");
					document.getElementById("dtPetRecvByDept").focus();
					return false;
				} else if (document.getElementById("subCategory").value != null
						&& document.getElementById("subCategory").value == "0") {
					alert("Select Sub  Category");
					document.getElementById("subCategory").focus();
					return false;
				} else if (document.getElementById("brfDiscription").value != null
						&& document.getElementById("brfDiscription").value == "") {
					alert("Please Enter Description");
					document.getElementById("brfDiscription").focus();
					return false;
				} else if (document.getElementById("priority").value != null
						&& document.getElementById("priority").value == "0") {
					alert("Select Priority");
					document.getElementById("priority").focus();
					return false;
				} else if (document.getElementById("stage").value != null
						&& document.getElementById("stage").value == "0") {
					alert("Select Stage");
					document.getElementById("stage").focus();
					return false;
				}
				
				
				///
				//alert("petnoOfRows="+petnoOfRows);
				//alert("respnoOfRows="+respnoOfRows);
				
				for(var i  =1;i<=petnoOfRows;i++)
				{
					if(document.getElementById("petioner_name_"+i).value == null || trim(document.getElementById("petioner_name_"+i).value) == "") 
					{
						alert("Enter Petitioner Name in Row "+i);
						document.getElementById("petioner_name_"+i).value="";
						document.getElementById("petioner_name_"+i).focus();
						return false;
					}
					
					if(document.getElementById("pet_address_"+i).value == null || trim(document.getElementById("pet_address_"+i).value) == "") 
					{
						alert("Enter Petitioner Address in Row "+i);
						document.getElementById("pet_address_"+i).value="";
						document.getElementById("pet_address_"+i).focus();
						return false;
					}
					
				}
				
				for(var i  =1;i<=respnoOfRows;i++)
				{
					if(document.getElementById("respondent_slno_"+i).value == null || trim(document.getElementById("respondent_slno_"+i).value) == "0") 
					{
						alert("Select Respondent serial number in Row "+i);
						document.getElementById("respondent_slno_"+i).value="0";
						document.getElementById("respondent_slno_"+i).focus();
						return false;
					}
					
					if(document.getElementById("respondent_name_"+i).value == null || trim(document.getElementById("respondent_name_"+i).value) == "") 
					{
						alert("Enter Respondent Name in Row "+i);
						document.getElementById("respondent_name_"+i).value="";
						document.getElementById("respondent_name_"+i).focus();
						return false;
					}
					
					if(document.getElementById("resp_address_"+i).value == null || trim(document.getElementById("resp_address_"+i).value) == "") 
					{
						alert("Enter Respondent Address in Row "+i);
						document.getElementById("resp_address_"+i).value="";
						document.getElementById("resp_address_"+i).focus();
						return false;
					}
					
				}
				
				///
				
				if (confirm("Do you want submit Case Details")) {
					document.getElementById("action").value = "insertDetails";
					document.forms[0].submit();
				} else
					return false;
					
			}
			
			function updateData() {

				var radiValue = $('input[name=isPetRes]:checked').val();
				var petnoOfRows = $("#table1 tr").length;
				petnoOfRows = petnoOfRows - 1;
				$("#petnoOfRows").val(petnoOfRows);
				var respnoOfRows = $("#table2 tr").length;
				respnoOfRows = respnoOfRows - 1;
				$("#respnoOfRows").val(respnoOfRows);
				$("#radiValue").val(radiValue);
				/* alert(radiValue); */
				if (document.getElementById("courtType").value != null
						&& document.getElementById("courtType").value == "0") {
					alert("Select Court ");
					document.getElementById("courtType").focus();
					return false;
				} else if (document.getElementById("caseType").value != null
						&& document.getElementById("caseType").value == "0") {
					alert(" Select Nature of Petition ");
					document.getElementById("caseType").focus();
					return false;
				} else if (document.getElementById("caseNo").value != null
						&& document.getElementById("caseNo").value == "") {
					alert("Enter Case NO");
					document.getElementById("caseNo").focus();
					return false;
				} else if (document.getElementById("caseYear").value != null
						&& document.getElementById("caseYear").value == "0") {
					alert("Select Case Year");
					document.getElementById("caseYear").focus();
					return false;
				} 
				
				else if (document.forms[0].isPetRes[0].checked==false && document.forms[0].isPetRes[1].checked==false){
					
					alert("select whether Govt As Petitioner / Respondent");
					document.forms[0].isPetRes[0].focus();
					return false;
				}
				
				
				else if (document.getElementById("respAdvocate").value != null
						&& document.getElementById("respAdvocate").value == "") {
					alert("Enter Advacate-on-record ");
					document.getElementById("respAdvocate").focus();
					return false;
				} /* else if (document.getElementById("respAdvocateCode").value != null
						&& document.getElementById("respAdvocateCode").value == "") {
					alert("Enter Advocate Code");
					document.getElementById("respAdvocateCode").focus();
					return false;
				} */ else if (document.getElementById("dtPetRecvByDept").value != null
						&& document.getElementById("dtPetRecvByDept").value == "0") {
					alert("Enter Date of Petition recieved by the Dept.");
					document.getElementById("dtPetRecvByDept").focus();
					return false;
				} else if (document.getElementById("subCategory").value != null
						&& document.getElementById("subCategory").value == "0") {
					alert("Select Sub  Category");
					document.getElementById("subCategory").focus();
					return false;
				} else if (document.getElementById("brfDiscription").value != null
						&& document.getElementById("brfDiscription").value == "") {
					alert("Please Enter Description");
					document.getElementById("brfDiscription").focus();
					return false;
				} else if (document.getElementById("priority").value != null
						&& document.getElementById("priority").value == "0") {
					alert("Select Priority");
					document.getElementById("priority").focus();
					return false;
				} else if (document.getElementById("stage").value != null
						&& document.getElementById("stage").value == "0") {
					alert("Select Stage");
					document.getElementById("stage").focus();
					return false;
				}
				
				
				///
				
				//alert("petnoOfRows="+petnoOfRows);
				//alert("respnoOfRows="+respnoOfRows);
				
				for(var i  =1;i<=petnoOfRows;i++)
				{
					if(document.getElementById("petioner_name_"+i).value == null || trim(document.getElementById("petioner_name_"+i).value) == "") 
					{
						alert("Enter Petitioner Name in Row "+i);
						document.getElementById("petioner_name_"+i).value="";
						document.getElementById("petioner_name_"+i).focus();
						return false;
					}
					
					if(document.getElementById("pet_address_"+i).value == null || trim(document.getElementById("pet_address_"+i).value) == "") 
					{
						alert("Enter Petitioner Address in Row "+i);
						document.getElementById("pet_address_"+i).value="";
						document.getElementById("pet_address_"+i).focus();
						return false;
					}
					
				}
				
				for(var i  =1;i<=respnoOfRows;i++)
				{
					if(document.getElementById("respondent_slno_"+i).value == null || trim(document.getElementById("respondent_slno_"+i).value) == "0") 
					{
						alert("Select Respondent serial number in Row "+i);
						document.getElementById("respondent_slno_"+i).value="0";
						document.getElementById("respondent_slno_"+i).focus();
						return false;
					}
					
					if(document.getElementById("respondent_name_"+i).value == null || trim(document.getElementById("respondent_name_"+i).value) == "") 
					{
						alert("Enter Respondent Name in Row "+i);
						document.getElementById("respondent_name_"+i).value="";
						document.getElementById("respondent_name_"+i).focus();
						return false;
					}
					
					if(document.getElementById("resp_address_"+i).value == null || trim(document.getElementById("resp_address_"+i).value) == "") 
					{
						alert("Enter Respondent Address in Row "+i);
						document.getElementById("resp_address_"+i).value="";
						document.getElementById("resp_address_"+i).focus();
						return false;
					}
					
				}
				
				///
				//alert("listingDate="+document.getElementById("listingDate").value);
				
				if (confirm("Do you want update Case Details")) {
					document.getElementById("action").value = "modifyDetails";
					document.forms[0].submit();
				} else
					return false;
					
			}
			
			
			
			function getList() {
				document.getElementById("action").value = "getCaseList";
				document.forms[0].submit();
			}
			function modifyCaseDetails(id)
			{
				document.getElementById("caseId").value = id;
				document.getElementById("action").value = "modifyCaseDetails";
				document.forms[0].submit();
			}
			function addNewCase()
			{
				document.getElementById("action").value = "unspecified";
				document.forms[0].submit();
			}
			
		</script>

	<script type="text/javascript">
		function intSlashHifnOnly(i) {
				if(i.value.length>0) {
				    //i.value=parseFloat(i.value); 
					i.value = i.value.replace(/[^\d/-]+/g, '');
				}
			}
						$(document).ready(
										function() {
										
											if(document.getElementById("radiValue").value=="Respondent")
												document.forms[0].isPetRes[1].checked=true;
											else
												document.forms[0].isPetRes[0].checked=true;
											var exportTitle = " Cases Report";
											
											if($('#example'))
												$('#example') .DataTable(
															{
																lengthMenu : [
																		[
																				10,
																				25,
																				50,
																				100,
																				-1 ],
																		[
																				10,
																				25,
																				50,
																				100,
																				"All" ] ],
																pageLength : 5,
																dom : 'B<"clear">lfrtip',//B<"clear">lfrtip
																bSortCellsTop : true,
																paging : true,
																title : exportTitle,
																buttons : [
																		{
																			extend : 'excel',
																			footer : true,
																			header : true,
																			title : exportTitle,
																			exportOptions : {
																				columns : ':visible'

																			}
																		},
																		{//'excel',  {
																			extend : 'pdfHtml5',
																			orientation : 'landscape',
																			pageSize : 'LEGAL',
																			title : exportTitle,
																			footer : true,
																			header : true
																		},
																		{
																			extend : 'print',
																			footer : true,
																			title : exportTitle,
																			header : true
																		} ]

															});
											/* $("#excelExportImg").click(function() {
												$("#example").table2excel({
													// exclude CSS class
													exclude : ".noExl",
													name : "Excel Document Name"
												});
											}); 
											
											"lengthMenu" : [ [ 10, 25, 50, 100, -1 ], [ 10, 25, 50, 100, "All" ] ],
												"pageLength" : 25,
												"dom" : 'T<"clear">lfrtip'
												dom : 'Bfrtip',
											
											 */
											 
											 
											 
										});
					</script>



</body>
</html>