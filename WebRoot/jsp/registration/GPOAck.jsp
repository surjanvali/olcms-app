<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- PAGE LEVEL STYLES
-->
<style type="text/css" data-genuitec-lp-enabled="false"
	data-genuitec-file-id="wc1-501"
	data-genuitec-path="/apolcms/WebRoot/jsp/registration/GPOAck.jsp">
label {
	font-weight: bold;
}
</style>
<!--  <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'> -->

<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />

<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING"> ${HEADING } </logic:notEmpty>
	</h1>
</div> --%>
<div class="page-content fade-in-up">



	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="dashboard-cat-title">
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
			</div>
		</div>
	</div>

	<div class="ibox">
		<div class="ibox-head">
			<div class="ibox-title" style="width: 100%;">
				<logic:empty name="HEADING">
					<logic:notEmpty name="ACKDATA">
						<b>Acknowledgements Generated Today</b>
						<span class="pull-right"><input type="button" name="Submit"
							value="Submit Acknowledgement" class="btn btn-primary pull-right"
							onclick="showAckEntry();" /> </span>
					</logic:notEmpty>

					<logic:empty name="ACKDATA">Acknowledgement Generation</logic:empty>
				</logic:empty>

				<logic:notEmpty name="HEADING">
					${HEADING}
				</logic:notEmpty>

			</div>
			<div class="ibox-tools">
				<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
				<!-- <a
					class="dropdown-toggle" data-toggle="dropdown"><i
					class="fa fa-ellipsis-v"></i> </a>  <div class="dropdown-menu dropdown-menu-right">
					<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
						2</a>
				</div> -->
			</div>
		</div>
		<div class="ibox-body">
			<html:form method="post" action="/GPOAck"
				enctype="multipart/form-data">
				<html:hidden styleId="mode" property="mode" />
				<html:hidden styleId="ackId" property="dynaForm(ackId)" />
				<html:hidden styleId="ackType" property="dynaForm(ackType)" />
				<html:hidden styleId="respondentIds"
					property="dynaForm(respondentIds)" value="1" />
				<logic:empty name="ACKDATA">
					<logic:empty name="DEPTACKDATA">

						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="form-group">

									<table id="RESPSTABID" class="table table-bordered"
										style="width: 100%;">
										<thead>
											<tr>
												<th colspan="10">Respondents <span class="pull-right"><input
														type="button" value="Add" class="btn btn-sm btn-success"
														name="add" id="addResp" /> <input type="button"
														value="Remove" class="btn btn-sm btn-danger" name="remove"
														id="removeResp" /></span></th>
											</tr>
											<tr>
												<th style="width: 10%;">Sl No.</th>
												<th style="width: 60%;">Respondent Department</th>
												<th style="width: 30%;">Service Type</th>
												<!-- <th style="width: 20%;">Designation</th>
												<th style="width: 20%;">District</th>
												<th style="width: 20%;">Mandal</th>
												<th style="width: 20%;">Village</th> -->
											</tr>
										</thead>
										<tbody>
											<tr id="1">
												<td>1.</td>
												<td><html:select styleId="deptId1"
														property="dynaForm(deptId1)" styleClass="select2Class"
														style="width: 100%;"
														onchange="showRevenueClassification(1);">
														<html:option value="0">---SELECT---</html:option>
														<logic:notEmpty name="CommonForm"
															property="dynaForm(deptList)">
															<html:optionsCollection name="CommonForm"
																property="dynaForm(deptList)" />
														</logic:notEmpty>
													</html:select> <html:select styleId="deptCategory1"
														property="dynaForm(deptCategory1)"
														styleClass="form-control"
														style="width: 100%;display : none;">
														<html:option value="0">---SELECT---</html:option>
														<html:option value="General">General</html:option>
														<html:option value="Assignment">Assignment</html:option>
														<html:option value="Land Acquisition">Land Acquisition</html:option>

													</html:select></td>
												<td><html:select property="dynaForm(serviceType1)"
														styleClass="select2Class" style="width: 100%;"
														styleId="serviceType1">
														<html:option value="0">---SELECT---</html:option>
														<html:option value="NON-SERVICES">NON-SERVICES</html:option>
														<logic:notEmpty property="dynaForm(serviceTypesList)"
															name="CommonForm">
															<html:optionsCollection
																property="dynaForm(serviceTypesList)" name="CommonForm" />
														</logic:notEmpty>
													</html:select></td>

												<%-- <td><html:select styleId="designation1"
														property="dynaForm(designation1)" styleClass="select2Class"
														style="width: 100%;">
														<html:option value="0">---SELECT---</html:option>
														<logic:notEmpty name="CommonForm"
															property="dynaForm(designationList)">
															<html:optionsCollection name="CommonForm"
																property="dynaForm(designationList)" />
														</logic:notEmpty>
													</html:select></td>

												<td><html:select styleId="distId1"
														property="dynaForm(distId1)" styleClass="select2Class"
														style="width: 100%;">
														<html:option value="0">---SELECT---</html:option>
														<logic:notEmpty name="CommonForm"
															property="dynaForm(distList)">
															<html:optionsCollection name="CommonForm"
																property="dynaForm(distList)" />
														</logic:notEmpty>
													</html:select></td>
												<td><html:select styleId="mandalId1"
														property="dynaForm(mandalId1)" styleClass="select2Class"
														style="width: 100%;">
														<html:option value="0">---SELECT---</html:option>
														<logic:notEmpty name="CommonForm"
															property="dynaForm(mdlList)">
															<html:optionsCollection name="CommonForm"
																property="dynaForm(mdlList)" />
														</logic:notEmpty>
													</html:select></td>
												<td><html:select styleId="villageId1"
														property="dynaForm(villageId1)" styleClass="select2Class"
														style="width: 100%;">
														<html:option value="0">---SELECT---</html:option>
														<logic:notEmpty name="CommonForm"
															property="dynaForm(vilList)">
															<html:optionsCollection name="CommonForm"
																property="dynaForm(vilList)" />
														</logic:notEmpty>
													</html:select></td> --%>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> District <bean:message key="mandatory" />
									</label>
									<html:select styleId="distId" property="dynaForm(distId)"
										styleClass="select2Class" style="width: 100%;">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty name="CommonForm"
											property="dynaForm(distList)">
											<html:optionsCollection name="CommonForm"
												property="dynaForm(distList)" />
										</logic:notEmpty>
									</html:select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Petitioner Name <bean:message key="mandatory" />
									</label>
									<html:text styleId="petitionerName" styleClass="form-control"
										property="dynaForm(petitionerName)" maxlength="150" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Advocate CC No. <bean:message key="mandatory" />
									</label>
									<html:text styleId="advocateCCno" styleClass="form-control"
										property="dynaForm(advocateCCno)" maxlength="10" />
								</div>
							</div>

							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Advocate Name <bean:message key="mandatory" />
									</label>
									<html:text styleId="advocateName" styleClass="form-control"
										property="dynaForm(advocateName)" maxlength="150" />
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="form-group">
									<div>
										<label class="ui-radio ui-radio-inline"> <html:radio
												property="dynaForm(caseCategory)" styleId="caseCategory"
												value="Arbitration">
												<span class="input-span"></span>
												<strong>Arbitration</strong>
											</html:radio>
										</label> <label class="ui-radio ui-radio-inline"> <html:radio
												property="dynaForm(caseCategory)" styleId="caseCategory"
												value="Appeal">
												<span class="input-span"></span>
												<strong>Appeal</strong>
											</html:radio>
										</label> <label class="ui-radio ui-radio-inline"> <html:radio
												property="dynaForm(caseCategory)" styleId="caseCategory"
												value="Others">
												<span class="input-span"></span>
												<strong>Others</strong>
											</html:radio>
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label for="sel1"><bean:message key="wpaffd.casetype" />
										<bean:message key="mandatory" /> </label>
									<html:select property="dynaForm(caseType)"
										styleClass="select2Class" style="width: 100%;"
										styleId="caseType">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty property="dynaForm(caseTypesList)"
											name="CommonForm">
											<html:optionsCollection property="dynaForm(caseTypesList)"
												name="CommonForm" />
										</logic:notEmpty>
									</html:select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label for="sel1">Mode of Filing <bean:message
											key="mandatory" />
									</label>
									<html:select property="dynaForm(filingMode)"
										styleClass="select2Class" style="width: 100%;"
										styleId="filingMode">
										<html:option value="0">---SELECT---</html:option>
										<html:option value="Normal">NORMAL</html:option>
										<html:option value="Lunch Motion">LUNCH MOTION</html:option>
										<html:option value="Fair List Case">FAIR LIST CASE</html:option>

										<html:option value="Tomorrow Normal">TOMORROW NORMAL</html:option>
										<html:option value="Tomorrow Lunch Motion">TOMORROW LUNCH MOTION</html:option>
										<html:option value="Tomorrow Fair List Case">TOMORROW FAIR LIST CASE</html:option>
									</html:select>
								</div>
							</div>
						</div>

						<div class="row" id="OLDCASEDIV">
							<%-- <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label for="sel1"><bean:message key="wpaffd.casetype" />
									<bean:message key="mandatory" /> </label>
								<html:select property="dynaForm(caseType)"
									styleClass="select2Class" style="width: 100%;"
									styleId="caseType">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty property="dynaForm(caseTypesList)"
										name="CommonForm">
										<html:optionsCollection property="dynaForm(caseTypesList)"
											name="CommonForm" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div> --%>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label>Case Registration Year <bean:message
											key="mandatory" /></label>
									<html:select styleId="regYear" property="dynaForm(regYear)"
										styleClass="select2Class" style="width:100%;">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty name="CommonForm"
											property="dynaForm(yearsList)">
											<html:optionsCollection name="CommonForm"
												property="dynaForm(yearsList)" />
										</logic:notEmpty>
									</html:select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label class="font-bold">Case Reg. No <bean:message
											key="mandatory" /></label>

									<html:text styleId="regNo" property="dynaForm(regNo)"
										styleClass="form-control" maxlength="6" />

								</div>
							</div>
						</div>



						<%-- <div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Services / Non-Services <bean:message
											key="mandatory" />
									</label>
									<html:select property="dynaForm(serviceNonService)"
										styleClass="select2Class" style="width: 100%;"
										styleId="serviceNonService">
										<html:option value="0">---SELECT---</html:option>
										<html:option value="SERVICES">SERVICES</html:option>
										<html:option value="NON-SERVICES">NON-SERVICES</html:option>

									</html:select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group servicesDiv">
									<label> Services <bean:message key="mandatory" />
									</label>
									<html:select property="dynaForm(serviceType)"
										styleClass="select2Class" style="width: 100%;"
										styleId="serviceType">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty property="dynaForm(serviceTypesList)"
											name="CommonForm">
											<html:optionsCollection property="dynaForm(serviceTypesList)"
												name="CommonForm" />
										</logic:notEmpty>
									</html:select>
								</div>
							</div>
						</div> --%>
						<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Main Case No. (WP/WA/AS/CRP Nos.) </label>
									<html:text styleId="mainCaseNo" styleClass="form-control"
										property="dynaForm(mainCaseNo)" maxlength="25" />
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group">
									<label> Remarks </label>
									<html:textarea styleId="remarks" styleClass="form-control"
										property="dynaForm(remarks)" rows="5" cols="50" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<logic:present name="saveAction">
									<!-- <input type="button" name="Submit" value="show Ack List"
									class="btn btn-primary pull-right" onclick="showAckListAll();" /> &nbsp;&nbsp; -->
									<input type="button" name="Submit" value="show Acks List"
										class="btn btn-primary pull-right" onclick="showAckList();" /> &nbsp;&nbsp;
												
												<logic:equal value="INSERT" name="saveAction">
										<input type="button" name="Submit" value="Submit"
											class="btn btn-success pull-right" onclick="saveAck();" />
									</logic:equal>

									<logic:equal value="UPDATE" name="saveAction">

										<input type="button" name="Submit" value="Update"
											class="btn btn-success pull-right" onclick="updateAck();" />
									</logic:equal>
								</logic:present>
							</div>
						</div>
					</logic:empty>
				</logic:empty>
				<logic:notEmpty name="ACKDATA">
					<div class="row">
						<div class="col-md-12">
							<div class="table-responsive">
								<table class="table table-striped table-bordered table-hover"
									id="example">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Ack No.</th>
											<th>Date</th>
											<th>District</th>
											<th>Department</th>
											<th>Case Type</th>
											<logic:present name="DISPLAYOLD">
												<th>Case Year</th>
												<th>Case Reg. No.</th>
											</logic:present>
											<th>Main Case No.</th>
											<th>Advocate CC No.</th>
											<th>Advocate Name</th>

											<th>Mode of Filing</th>

											<th>Remarks</th>
											<th>Download / Print</th>
											<logic:notPresent name="DISPLAYOLD">
												<th>Action</th>
											</logic:notPresent>
										</tr>
									</thead>
									<tbody>
										<logic:iterate id="map" name="ACKDATA" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.ack_no }</td>
												<td nowrap="nowrap">${map.generated_date }</td>
												<td>${map.district_name }</td>
												<td>${map.dept_descs }</td>
												<td>${map.case_full_name }</td>

												<logic:present name="DISPLAYOLD">
													<td>${map.reg_year }</td>
													<td>${map.reg_no }</td>
												</logic:present>

												<td>${map.maincaseno }</td>
												<td>${map.advocateccno }</td>
												<td>${map.advocatename }</td>

												<td>${map.mode_filing }</td>


												<td>${map.remarks }</td>
												<td>
													<%-- <button type="button" class="btn btn-sm btn-warning"
														title="Edit Acknowledgement"
														onclick="editAck('${map.ack_no}')">
														<i class="fa fa-edit"></i><span>Edit</span>
													</button> --%> <logic:present name="map"
														property="ack_file_path">
														<a href="./${map.ack_file_path}" target="_new"
															title="Print Acknowledgement" class="btn btn-sm btn-info">
															<i class="fa fa-save"></i> <span>Acknowledgement</span> <!-- <span>Download</span> -->
														</a>
														<br />
													</logic:present> <logic:present name="map" property="barcode_file_path">
														<a href="./${map.barcode_file_path}" target="_new"
															title="Print Barcode" class="btn btn-sm btn-info"> <i
															class="fa fa-save"></i> <span>Barcode</span> <!-- <span>Download</span> -->
														</a>
														<br />
													</logic:present> <a
													href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
													target="_new" title="Print Barcode"
													class="btn btn-sm btn-info"> <i class="fa fa-save"></i>
														<span>Scanned Affidavit</span> <!-- <span>Download</span> -->
												</a> <%-- <logic:notPresent name="map" property="ack_file_path">
														<button type="button" class="btn btn-sm btn-info"
															onclick="downloadAck('${map.ack_no}')">
															<i class="fa fa-save"></i> <span>Download</span>
														</button>
													</logic:notPresent>--%> <%-- <button type="button" class="btn btn-sm btn-info"
														onclick="downloadAck('${map.ack_no}')">
														<i class="fa fa-save"></i> <span>Download 2</span>
													</button>  --%>
												</td>
												<logic:notPresent name="DISPLAYOLD">
													<td><button type="button"
															class="btn btn-sm btn-danger"
															title="Delete Acknowledgement"
															onclick="deleteAck('${map.ack_no}')">
															<i class="fa fa-trash"></i><span>Delete</span>
														</button></td>
												</logic:notPresent>
											</tr>
										</logic:iterate>
									</tbody>
									<tfoot>
										<tR>
											<logic:present name="DISPLAYOLD">
												<td colspan="14">
											</logic:present>
											<logic:notPresent name="DISPLAYOLD">
												<td colspan="13">
											</logic:notPresent>


											&nbsp;
											</td>
										</tR>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</logic:notEmpty>


				<logic:notEmpty name="DEPTACKDATA">
					<div class="row">
						<div class="col-md-12">
							<div class="table-responsive">
								<table class="table table-striped table-bordered table-hover"
									id="example">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Ack No.</th>
											<th>Date</th>
											<th>District</th>
											<th>Case Type</th>
											<!-- <th>Main Case No.</th> -->
											<th>Departments / Respondents</th>
											<th>Advocate CC No.</th>
											<th>Advocate Name</th>

											<!-- <th>Remarks</th> -->
											<th>Download / Print</th>
										</tr>
									</thead>
									<tbody>
										<logic:iterate id="map" name="DEPTACKDATA" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.ack_no }</td>
												<td nowrap="nowrap">${map.generated_date }</td>
												<td>${map.district_name }</td>
												<td>${map.case_full_name }</td>
												<td nowrap="nowrap">${map.dept_descs }</td>
												<td>${map.advocateccno }</td>
												<td>${map.advocatename }</td>


												<%-- <td>${map.remarks }</td> --%>
												<td style="text-align: left;"><logic:present name="map"
														property="ack_file_path">
														<a href="./${map.ack_file_path}" target="_new"
															title="Print Acknowledgement" class="btn btn-sm btn-info">
															<i class="fa fa-save"></i> <span>Acknowledgement</span> <!-- <span>Download</span> -->
														</a>
														<br />
													</logic:present> <logic:present name="map" property="barcode_file_path">
														<a href="./${map.barcode_file_path}" target="_new"
															title="Print Barcode" class="btn btn-sm btn-info"> <i
															class="fa fa-save"></i> <span>Barcode</span> <!-- <span>Download</span> -->
														</a>
														<br />
													</logic:present> <a
													href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
													target="_new" title="Print Barcode"
													class="btn btn-sm btn-info"> <i class="fa fa-save"></i>
														<span>Scanned Affidavit</span> <!-- <span>Download</span> -->
												</a></td>
											</tr>
										</logic:iterate>
									</tbody>
									<tfoot>
										<tR>
											<logic:present name="DISPLAYOLD">
												<td colspan="13">
											</logic:present>
											<logic:notPresent name="DISPLAYOLD">
												<td colspan="11">
											</logic:notPresent>
											&nbsp;
											</td>
										</tR>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</logic:notEmpty>
			</html:form>
		</div>
	</div>
</div>

<script src="https://apbudget.apcfss.in/js/select2.js"></script>

<script type="text/javascript">


	function showRevenueClassification(val){
		if($("#deptId"+val).val()=="REV01-L"){
			// show Clasification Select box
				$("#deptCategory"+val).show();
		}
		else{
			// nothing
			$("#deptCategory"+val).hide();
			$("#deptCategory"+val).val("0");
		}
	}

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}
	
	$(document).ready(function() {
		$(".servicesDiv").hide();
		$("#OLDCASEDIV").hide();
	
		//$('#caseCategory [value="Others"]').prop('checked', true);
		$('input[id=caseCategory][value="Others"]').prop('checked', true);
		// $("input[name=type][value=" + value + "]").prop('checked', true);
		
		if($("#ackType").val()=="OLD"){
			$("#OLDCASEDIV").show();			
		}
		
		$("#serviceNonService").change(function(){
			if($("#serviceNonService").val()=="SERVICES"){
				$(".servicesDiv").show();
			}
			else{
				$("#serviceType").val("0");
				$(".servicesDiv").hide();
			}
		});
		
		$("#addResp").click(function(){
			
			
			  let rowfyable = $("#RESPSTABID").closest('table');
			  //let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
			  let rowCount = $("#RESPSTABID tbody tr").length; //$('tbody', rowfyable).rows.length;
			  let rowCount2 = rowCount+1;
			  let prevVal=$("#deptId"+rowCount).val();
			  let service=$("#serviceType"+rowCount).val();
			  let deptCat=$("#deptCategory"+rowCount).val();
			  
			  /*let design=$("#designation"+rowCount).val();
			  let dist=$("#distId"+rowCount).val();
			  let mandal=$("#mandalId"+rowCount).val();
			  let village=$("#villageId"+rowCount).val();*/
			  
			 // alert("prevVal--"+prevVal);
			  if( (prevVal != null && prevVal != "" && prevVal!="0") && (service != null && service != "" && service!="0")
					 // && (design != null && design != "" && design!="0" ) && (dist != null && dist != "" && dist!="0" ) 
					  // && (mandal != null && mandal != "" && mandal!="0" ) && (village != null && village != "" && village!="0" )
					  )
			  {
				  //console.log("rowCount:"+rowCount);
				 // console.log("rowCount2:"+rowCount2);
			
				  $('tbody', rowfyable).append("<tr id='"+rowCount2+"'><td>"+rowCount2+".</td><td>"
				  		+ "<select name='dynaForm(deptId"+rowCount2+")' id='deptId"+rowCount2+"' style='width: 100%;'></select>"
				  		+ "<select name='dynaForm(deptCategory"+rowCount2+")' id='deptCategory"+rowCount2+"' style='width:100%;display:none;' class='form-control'><option value='0'>---SELECT---</option><option value='General'>General</option><option value='Assignment'>Assignment</option><option value='Land Acquisition'>Land Acquisition</option></select>"
				  		+"</td>"
				  		+ "<td><select name='dynaForm(serviceType"+rowCount2+")' id='serviceType"+rowCount2+"' style='width: 100%;' ></select></td></tr>"
				  
				  /*+"<td><select name='dynaForm(designation"+rowCount2+")' id='designation"+rowCount2+"' style='width: 100%;' ></select></td>"
					 +"<td><select name='dynaForm(distId"+rowCount2+")' id='distId"+rowCount2+"' style='width: 100%;' ></select></td>"
					 +"<td><select name='dynaForm(mandalId"+rowCount2+")' id='mandalId"+rowCount2+"' style='width: 100%;' ></select></td>"
					 +"<td><select name='dynaForm(villageId"+rowCount2+")' id='villageId"+rowCount2+"' style='width: 100%;' ></select></td></tr>" */
					    + "");
				  
				  
				  $("#deptId"+rowCount+" option").clone().appendTo("#deptId"+rowCount2);
				  $("#deptId"+rowCount2).select2();
				  $("#deptId"+rowCount2).select2("val", "0");
				  $("#deptId"+rowCount2).change(function(){showRevenueClassification(rowCount2);});
				  
				  /*$("#deptCategory"+rowCount+" option").clone().appendTo("#deptCategory"+rowCount2);
				  $("#deptCategory"+rowCount2).select2();
				  $("#deptCategory"+rowCount2).select2("val", "0");*/
				  
				  $("#serviceType"+rowCount+" option").clone().appendTo("#serviceType"+rowCount2);
				  $("#serviceType"+rowCount2).select2();
				  $("#serviceType"+rowCount2).select2("val", "0");
				 
				  
				  
				  /*$("#designation"+rowCount+" option").clone().appendTo("#designation"+rowCount2);
				  $("#designation"+rowCount2).select2();
				  $("#designation"+rowCount2).select2("val", "0");
				  
				  
				  $("#distId"+rowCount+" option").clone().appendTo("#distId"+rowCount2);
				  $("#distId"+rowCount2).select2();
				  $("#distId"+rowCount2).select2("val", "0");
				  
				  
				  $("#mandalId"+rowCount+" option").clone().appendTo("#mandalId"+rowCount2);
				  $("#mandalId"+rowCount2).select2();
				  $("#mandalId"+rowCount2).select2("val", "0");
				  
				  $("#villageId"+rowCount+" option").clone().appendTo("#villageId"+rowCount2);
				  $("#villageId"+rowCount2).select2();
				  $("#villageId"+rowCount2).select2("val", "0"); */
				
				  // $("#deptId"+rowCount2+" option[value="+prevVal+"]").remove();
				 // $("#serviceType"+rowCount2+" option[value="+service+"]").remove();
				 // $("#designation"+rowCount2+" option[value="+service+"]").remove();
				  //$("#distId"+rowCount2+" option[value="+service+"]").remove();
				 // $("#mandalId"+rowCount2+" option[value="+service+"]").remove();
				 // $("#villageId"+rowCount2+" option[value="+service+"]").remove(); 
				  
				  $("#respondentIds").val($("#RESPSTABID tbody tr").length);
			  }
			  
			  if( (prevVal=="0") )
			  {
				 // alert("else prevVal--"+prevVal);
			  	alert("Select Respondant Department.");
			  	$("#deptId"+rowCount).focus();
			  }
			  if(prevVal=="REV01-L" && (deptCat=="" || deptCat=="0"))
			  {
				 // alert("else prevVal--"+prevVal);
			  	alert("Select Department Category.");
			  	$("#deptCategory"+rowCount).focus();
			  }
			  
			  if(  ( service=="0") )
			  {
			  	alert("Select Service Type");
			  	$("#serviceType"+rowCount).focus();
			  }
			  
			  /*  if( ( design=="0" ) )
			  {
			  	alert("Select Designation");
			  	$("#designation"+rowCount).focus();
			  }
			  
			 if(  ( dist=="0" ) )
			  {
			  	alert("Select District");
			  	$("#distId"+rowCount).focus();
			  
			  }
			  
			  if( ( mandal=="0" ) )
			  {
			  	alert("Select Mandal");
			  	$("#mandalId"+rowCount).focus();
			  }
			  
			  if(( village=="0" ) )
			  {
				alert("Select Village");
			  	$("#villageId"+rowCount).focus();
			  
			  }*/
			  
		});
		$("#removeResp").click(function(){
			let rowfyable = $("#RESPSTABID").closest('table');
			let rowCount = $("#RESPSTABID tbody tr").length;
			if(rowCount > 1){
				$('tbody tr:last', rowfyable).remove();
			}
			$("#respondentIds").val($("#RESPSTABID tbody tr").length);
		});
		
		
 		$("#designationId").change(function() {

			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');

			$("#employeeId").select2('destroy');
			var data = {
				mode : "AjaxAction",
				deptId : $("#deptId").val(),
				designationId : $("#designationId").val(),
				getType : "getEmployeesList"
			}
			$.post("AjaxModels.do", data).done(function(res) {
				$("#employeeId").html(res);
			}).fail(function(exc) {
				$("#employeeId").html("<option value='0'>---SELECT---</option>");
				alert("Error Occured.Please Try Again.");
			});

			$("#employeeId").select2();
			$("#employeeId").select2("val", "0");

			$("#mobileNo").val("");
			$("#emailId").val("");
			$("#aadharNo").val("");

			setTimeout(function() {
				$("#LOADINGPAGEGIF").html("");
			}, 900);
		});

		$('.select2Class').select2();
	});

	function saveAck() {
		if ($("#deptId1").val() == null || $("#deptId1").val() == "" || $("#deptId1").val() == "0") {
			alert("Department Required");
			$("#deptId1").focus();
			return false;
		}else if($("#deptId1").val()=="REV01-L" && ($("#deptCategory1").val()=="" || $("#deptCategory1").val()=="0"))
		  {
		  	alert("Select Department Category.");
		  	$("#deptCategory1").focus();
		  } else if ($("#serviceType1").val() == null || $("#serviceType1").val() == "" || $("#serviceType1").val() == "0") {
			alert("Service Type Required");
			$("#serviceType1").focus();
			return false;
		}else if ($("#distId").val() == null || $("#distId").val() == "" || $("#distId").val() == "0") {
			alert("District Required");
			$("#distId").focus();
			return false;
		} else if ($("#petitionerName").val() == null || $("#petitionerName").val() == "" || $("#petitionerName").val() == "0") {
			alert("Petitioner Name Required");
			$("#petitionerName").focus();
			return false;
		}else if ($("#advocateCCno").val() == null || $("#advocateCCno").val() == "" || $("#advocateCCno").val() == "0") {
			alert("Advocate CC No. Required");
			$("#advocateCCno").focus();
			return false;
		} else if ($("#advocateName").val() == null || $("#advocateName").val() == "" || $("#advocateName").val() == "0") {
			alert("Advocate Name Required");
			$("#advocateName").focus();
			return false;
		} else if ($("#caseType").val() == null || $("#caseType").val() == "" || $("#caseType").val() == "0") {
			alert("Case Type (Nature of Petition) Required");
			$("#caseType").focus();
			return false;
		} 

		else if ($("#filingMode").val() == null || $("#filingMode").val() == "" || $("#filingMode").val() == "0") {
			alert("Mode of Filing Required");
			$("#filingMode").focus();
			return false;
		}  
		/*else if ($("#mainCaseNo").val() == null || $("#mainCaseNo").val() == "" || $("#mainCaseNo").val() == "0") {
			alert("Main Case No. Required");
			$("#mainCaseNo").focus();
			return false;
		} /*  else if ($("#serviceNonService").val() == null || $("#serviceNonService").val() == "" || $("#serviceNonService").val() == "0") {
			alert("Service / Non-Service Required");
			$("#serviceNonService").focus();
			return false;
		} else if ( $("#serviceNonService").val() == "SERVICES" && ($("#serviceType").val() == null || $("#serviceType").val() == "" || $("#serviceType").val() == "0")) {
			alert("Service Type Required");
			$("#serviceType").focus();
			return false;
		} */ /*   else if ($("#remarks").val() == null || $("#remarks").val() == "" || $("#remarks").val() == "0") {
			alert("Remarks Required");
			$("#remarks").focus();
			return false;
		} */ else if (confirm("Do you want to Proceed and save Acknowledgement details?")) {
			document.forms[0].mode.value = "saveAckDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}


	function updateAck() {
		/* if ($("#distId").val() == null || $("#distId").val() == "" || $("#distId").val() == "0") {
			alert("District Required");
			$("#distId").focus();
			return false;
		} else */ if ($("#deptId").val() == null || $("#deptId").val() == "" || $("#deptId").val() == "0") {
			alert("Department Required");
			$("#deptId").focus();
			return false;
		} else if ($("#advocateName").val() == null || $("#advocateName").val() == "" || $("#advocateName").val() == "0") {
			alert("Advocate Name Required");
			$("#advocateName").focus();
			return false;
		} else if ($("#advocateCCno").val() == null || $("#advocateCCno").val() == "" || $("#advocateCCno").val() == "0") {
			alert("Advocate CC No. Required");
			$("#advocateCCno").focus();
			return false;
		} /* else if ($("#caseType").val() == null || $("#caseType").val() == "" || $("#caseType").val() == "0") {
			alert("Case Type Required");
			$("#caseType").focus();
			return false;
		}  */else if ($("#mainCaseNo").val() == null || $("#mainCaseNo").val() == "" || $("#mainCaseNo").val() == "0") {
			alert("Main Case No. Required");
			$("#mainCaseNo").focus();
			return false;
		} else if ($("#remarks").val() == null || $("#remarks").val() == "" || $("#remarks").val() == "0") {
			alert("Remarks Required");
			$("#remarks").focus();
			return false;
		} else if (confirm("Do you want to Proceed and update Acknowledgement details?")) {
			document.forms[0].mode.value = "updateAckDetails";
			document.forms[0].submit();
		}
		else
			return false;
	}


	function showAckEntry() {
		document.forms[0].mode.value = "displayAckForm";
		document.forms[0].submit();
	}
	function showAckListAll() {
		document.forms[0].mode.value = "getAcknowledementsListAll";
		document.forms[0].submit();
	}
	
	function showAckList() {
		document.forms[0].mode.value = "getAcknowledementsList";
		document.forms[0].submit();
	}

	function editAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			document.forms[0].mode.value = "displayAckEditForm";
			document.forms[0].submit();
		}
	}
	
	
	function downloadAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			document.forms[0].mode.value = "downloadAckPDF";
			document.forms[0].submit();
		}
	}
	
	function deleteAck(ackId) {
		if (ackId != null && ackId != "") {
			$("#ackId").val(ackId);
			if (confirm("Do you want to Proceed and Delete Acknowledgement details?")) {
				document.forms[0].mode.value = "deleteAck";
				document.forms[0].submit();
			}
		}
	}

	function populateDetails() {
		$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
		var data = {
			mode : "AjaxAction",
			empId : $("#employeeId").val(),
			getType : "getEmpDetails"
		}
		$.post("AjaxModels.do", data).done(function(res) {
			if (res != '') {
				// alert("RES:"+res);
				// alert("RES1:"+res.split("#")[1]);
				$("#mobileNo").val(res.split("#")[1]);
				$("#emailId").val(res.split("#")[2]);
				$("#aadharNo").val(res.split("#")[3]);
			}
		}).fail(function(exc) {
			alert("Error Occured.Please Try Again");
		});

		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);
	}

	/* <script type="text/javascript">
			   $(document).ready(function() {
				    $("#designationId").change(function(){
						var data = {
							mode : "AjaxAction",
							designationId : $("#designationId").val(),
							getType : "getEmployeesList"
						}
						$.post("AjaxModels.do",data).done(function(res){
							$("#employeeId").html(res);
						}).fail(function(exc){
							$("#employeeId").html("<option value='0'>---SELECT---</option>");
							alert("Error Occured.Please Try Again");
						});
					});
					
				}); */

	function showEdit(val) {
		$("#mloId").val(val);
		document.forms[0].mode.value = "editEmployee";
		document.forms[0].submit();
	}

	function deleteData(val) {
		$("#mloId").val(val);
		document.forms[0].mode.value = "deleteEmployeeDetails";
		document.forms[0].submit();
	}

	function isNumberKey(evt) {
		var charCode = (evt.which) ? evt.which : event.keyCode;
		if (charCode > 31 && (charCode < 48 || charCode > 57))
			return false;
		return true;
	}

	function addressType(i) {
		if (i.value.length > 0) {
			//i.value = i.value.replace(/[^\sA-Za-z0-9:.\/,\\]+/g, '');
			i.value = i.value.replace(/[^\sA-Za-z0-9,\/.\\:;#]+/g, '');
		}
	}

	function OnlyNumbersAndDot(evt) {
		var e = window.event || evt; // for trans-browser compatibility  
		var charCode = e.which || e.keyCode;
		if ((charCode > 45 && charCode < 58) || charCode == 8) {
			return true;
		}
		return false;
	}
	function trim(s) {
		return s.replace(/^\s*/, "").replace(/\s*$/, "");
	}

	function validateEmail(inputText) {
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (inputText.value.match(mailformat)) {
			return true;
		} else {
			alert("You have entered an invalid email address!");
			inputText.focus();
			return false;
		}
	}

	function updateEmployeeDetails() {
		designationId = $("#designationId").val();
		employeeId = $("#employeeId").val();
		mobileNo = $("#mobileNo").val();
		emailId = $("#emailId").val();
		aadharNo = $("#aadharNo").val();

		if (designationId == null || designationId == "" || designationId == "0") {
			alert("Select Designation");
			$("#designationId").focus();
			return false;
		}

		if (employeeId == null || employeeId == "" || employeeId == "0") {
			alert("Select Employee");
			$("#employeeId").focus();
			return false;
		}

		if (mobileNo == null || mobileNo == "" || mobileNo == "0") {
			alert("Enter Mobile Number");
			$("#mobileNo").focus();
			return false;
		}

		if (mobileNo != null && mobileNo != "" && mobileNo.length != 10) {
			alert("Enter 10-digit Mobile Number");
			$("#mobileNo").focus();
			return false;
		}


		if (emailId == null || emailId == "" || emailId == "0") {
			alert("Enter Email Id");
			$("#emailId").focus();
			return false;
		}
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if (!emailId.match(mailformat)) {
			alert("You have entered an invalid email address!");
			$("#emailId").focus();
			return false;
		}

		if (aadharNo == null || aadharNo == "" || aadharNo == "0") {
			alert("Enter Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (aadharNo != null && aadharNo != "" && aadharNo.length != 12) {
			alert("Enter 12-digit Aadhar Number");
			$("#aadharNo").focus();
			return false;
		}

		if (confirm("Do you want to Update employee details?")) {
			document.forms[0].mode.value = "updateEmployeeDetails";
			document.forms[0].submit();
		}
		else
			return false;

	}




	function verifyAadhaar() {
		var aadharNo = $("#aadharNo").val();
		if (validate(aadharNo)) {

		} else {
			alert("Invalid aadhar no.");
			$("#aadharNo").val("");
		}
	}
</script>
