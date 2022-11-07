<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<div class="page-content fade-in-up">
	<html:form method="post" action="/EcourtsCaseSearch"
		styleId="HighCourtCasesListForm" enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="cino" property="dynaForm(cino)" />
		<%-- value="${map.cino} --%>
		<html:hidden styleId="total" property="dynaForm(total)" />

		<div class="container-fluid">
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
					<div class="ibox-title">Case Search</div>
				</div>
				<div class="ibox-body">
					<!-- <h4 class="m-t-0 header-title">
		<b>High Court Cases List</b>
	</h4>
	<hr /> -->

					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="ui-radio ui-radio-inline"> <html:radio
										property="dynaForm(oldNewType)" styleId="oldNewType"
										value="New" onchange="caseTypeSelect();">
										<span class="input-span"></span>
										<b>New Cases</b>
									</html:radio>
								</label> <label class="ui-radio ui-radio-inline"> <html:radio
										property="dynaForm(oldNewType)" styleId="oldNewType"
										value="Legacy" onchange="caseTypeSelect();">
										<span class="input-span"></span>
										<b>Legacy Cases</b>
									</html:radio>
								</label>

							</div>
						</div>
					</div>

					<div class="row oldTypediv">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<!-- <label>Select Case Type. (WP/WA/AS/CRP Nos.) </label> -->
								<div class="row col-md-12">
									<div class="row col-md-4">
									<label> Case Type</label>
										<html:select property="dynaForm(caseType1)"
											styleClass="select2Class" style="width: 100%;"
											styleId="caseType1" onchange="getCaseTypedetails();">
											<html:option value="0">---SELECT---</html:option>
											<logic:notEmpty property="dynaForm(caseTypesListShrt)"
												name="CommonForm">
												<html:optionsCollection
													property="dynaForm(caseTypesListShrt)" name="CommonForm" />
											</logic:notEmpty>
										</html:select>
									</div>
									<div class="col-md-4">
									<label> Case Year</label>
										<html:select styleId="regYear1" property="dynaForm(regYear1)"
											style="width: 100%;" onchange="getCaseTypedetails();"
											styleClass="select2Class">
											<html:option value="0">---SELECT---</html:option>
											<logic:notEmpty name="CommonForm"
												property="dynaForm(yearsList)">
												<html:optionsCollection name="CommonForm"
													property="dynaForm(yearsList)" />
											</logic:notEmpty>
										</html:select>
									</div>
									<div class="col-md-4">
									<label> Case Number</label>
										<html:text styleId="mainCaseNo" styleClass="form-control"
											style="width: 100%;" property="dynaForm(mainCaseNo)"
											onchange="getCaseTypedetails();" maxlength="7"
											onkeypress="return isNumberKey(this);" />
									</div>
								</div>
								<!-- <div id="megId"></div> -->
							</div>
						</div>
					</div>

					<div class="row NewTypediv">
						<div class="col-md-4">
						<label> Enter Acknowledge Number </label><span class="input-span">*</span>
						<html:text styleId="ackNoo" styleClass="form-control"
											style="width: 100%;" property="dynaForm(ackNoo)" maxlength="30"
											onkeypress="return isNumberKey(this);" /></div>
					</div>
<br>
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="submit" value="Get Cases"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>


				</div>
			</div>

			<logic:notEmpty name="CASESLISTOLD">
				<div class="ibox oldTypediv">
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example"
								class="table table-striped table-bordered oldTypediv"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CINo</th>
										<th>Scanned Affidavit</th>
										<th>Current Status</th>
										<th>Date of Filing</th>
										<!-- <th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th> -->

										<th>Case Reg No.</th>
										<th>Prayer</th>

										<th>Filing No.</th>
										<th>Filing Year</th>
										<th>Date of Next List</th>
										<th>Bench</th>
										<th>Judge Name</th>
										<th>Petitioner</th>
										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>
										<th>Petitioner Advocate</th>
										<th>Respondent Advocate</th>
										<th>Orders</th>
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLISTOLD" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

											</td>
											<td><logic:notEmpty name="map"
													property="scanned_document_path1">
													<logic:notEqual value="-" name="map"
														property="scanned_document_path1">
														<%-- ./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf --%>
														<a href="./${map.scanned_document_path}" target="_new"
															class="btn btn-sm btn-info"><i
															class="glyphicon glyphicon-save"></i><span>Scanned
																Affidavit</span></a>
													</logic:notEqual>
												</logic:notEmpty></td>
											<td nowrap="nowrap"><span
												style="color: navy;font-weight: bold;text-align: center;">
													${map.current_status}</span> <logic:notEmpty name="map"
													property="fullname">
													<logic:notEqual value=" " name="map" property="fullname">
														<br />${map.fullname}</logic:notEqual>
												</logic:notEmpty> <logic:notEmpty name="map" property="designation">
													<logic:notEqual value=" " name="map" property="designation">
														<br /> ${map.designation}</logic:notEqual>
												</logic:notEmpty> <logic:notEmpty name="map" property="mobile">
													<logic:notEqual value=" " name="map" property="mobile">
														<br /> ${map.mobile}</logic:notEqual>
												</logic:notEmpty> <logic:notEmpty name="map" property="email">
													<logic:notEqual value=" " name="map" property="email">
														<br /> ${map.email}</logic:notEqual>
												</logic:notEmpty> <logic:notEmpty name="map" property="district_name">
													<logic:notEqual value=" " name="map"
														property="district_name">
														<br /> ${map.district_name}</logic:notEqual>
												</logic:notEmpty> <%-- ${map.globalorgname}<br /> 
												 ${map.designation} <br />
												${map.mobile} <br /> ${map.email} <br /> ${map.district_name }--%>
											</td>
											<td nowrap="nowrap"><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>

											<td nowrap="nowrap">${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
										<td style="min-width: 350px;t
											ext-align: justify;">
											<logic:notEmpty
												name="map" property="prayer">

												<logic:equal value="-" name="map" property="prayer">
												N/A
												</logic:equal>

												<logic:notEqual value="-" name="map" property="prayer">
										
										
										${map.prayer }
										
										<button class="btn btn-info btn-xs" data-container="body"
														data-toggle="popover" data-trigger="hover"
														data-placement="top" data-content="${map.prayer_full }"
														data-original-title="" title="">View More</button>
												</logic:notEqual>
											</logic:notEmpty></td>

											<td>${map.fil_no}</td>
											<td>${map.fil_year }</td>
											<td><logic:notEmpty name="map" property="date_next_list">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.bench_name }</td>
											<td>Hon'ble Judge : ${map.coram }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name },${map.address}</td>

											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>
											<td style="text-align: center;">${map.orderpaths }</td>
										</tr>

									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="20">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</logic:notEmpty>


			<logic:notEmpty name="CASESLISTNEW">
				<div class="ibox NewTypediv">
					<div class="ibox-body">
						<div class="table-responsive">
							<table
								class="table table-striped table-bordered table-hover NewTypediv"
								id="example">
								<thead>
									<tr>
										<th>Sl.No</th>
										<!-- <th></th> -->
										<th>Ack No.</th>
										<th>Date</th>
										<th>District</th>
										<th>Case Type</th>
										<th>Main Case No.</th>
										<th>Departments / Respondents</th>
										<th>Advocate CC No.</th>
										<th>Advocate Name</th>
										<!-- <th>Remarks</th> -->
										<th>Download / Print</th>
										<!-- <th>Instructions / Daily Status Action</th> -->
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASESLISTNEW" indexId="i">
										<tr>
											<td>${i+1 }</td>

											<td>${map.ack_no }<logic:notEqual value="-" name="map"
													property="hc_ack_no">

													<span style="color: navy;font-weight: bold;">${map.hc_ack_no }</span>

												</logic:notEqual></td>
											<td nowrap="nowrap">${map.generated_date }</td>
											<td>${map.district_name}</td>
											<td>${map.case_full_name}</td>
											<td>${map.maincaseno }</td>
											<td nowrap="nowrap">${map.dept_descs}</td>
											<td>${map.advocateccno}</td>
											<td>${map.advocatename}</td>



											<%-- <td>${map.remarks }</td> --%>
											<td style="text-align: center;" nowrap="nowrap"><logic:present
													name="map" property="ack_file_path">
													<a href="./${map.ack_file_path}" target="_new"
														title="Print Acknowledgement" class="btn btn-sm btn-info">
														<i class="fa fa-save"></i> <span>Acknowledgement</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <logic:present name="map" property="barcode_file_path">
													<a href="./${map.barcode_file_path}" target="_new"
														title="Print Barcode" class="btn btn-sm btn-info"> <i
														class="fa fa-save"></i> <span>Barcode</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <logic:notEqual value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
														target="_new" title="Print Barcode"
														class="btn btn-sm btn-info">
												</logic:notEqual> <logic:equal value="-" name="map" property="hc_ack_no">
													<a
														href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
														target="_new" title="Print Barcode"
														class="btn btn-sm btn-info">
												</logic:equal> <i class="fa fa-save"></i> <span>Scanned Affidavit</span> </a></td>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<logic:present name="DISPLAYOLD">
											<td colspan="14">
										</logic:present>
										<logic:notPresent name="DISPLAYOLD">
											<td colspan="12">
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
		</div>
	</html:form>
</div>

<!-- Modal  Start-->

<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:50px;">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header"
				style="background-color: #3498db;color: #fff;">
				<button type="button" class="close" data-dismiss="modal">
					&times;</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<p>
					<iframe src="" id="page" name="model_window"
						style="width:100%;min-height:550px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>

<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function reloadParent() {
		location.reload(true);
	}

	$(document).ready(function() {
		$(".select2Class").select2();
		if ($('#example2')) {
			$('#example2').DataTable();
		}
	});

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function fnShowCases() {

		var chkdVal = $("#oldNewType:checked").val();
		//alert (chkdVal);

		if ((chkdVal == null || chkdVal == "" || chkdVal == "0")) {
			alert("Select Type Of Case");
			$("#oldNewType").focus();
			return false;
		}

		if (chkdVal == "Legacy") {

			if (($("#caseType1").val() == null || $("#caseType1").val() == "" || $(
					"#caseType1").val() == "0")) {
				alert("Select Case Type");
				$("#caseType1").focus();
				return false;
			}

			if (($("#regYear1").val() == null || $("#regYear1").val() == "" || $(
					"#regYear1").val() == "0")) {
				alert("Select Reg Year");
				$("#regYear1").focus();
				return false;
			}

			if (($("#mainCaseNo").val() == null || $("#mainCaseNo").val() == "" || $(
					"#mainCaseNo").val() == "0")) {
				alert("Enter Case No");
				$("#mainCaseNo").focus();
				return false;
			}

		} else {

			if (($("#ackNoo").val() == null || $("#ackNoo").val() == "" )) {
				alert("Enter Ack No");
				$("#ackNoo").focus();
				return false;
			}

		}

		// alert("Please select a filter to get the data.");

		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	}

	function getInstruction(val) {

		alert(val)
		$("#cino").val(val);
		$("#mode").val("getCino");
		$("#HighCourtCasesListForm").submit();
	}

	function viewCaseDetailsPopup1(cino) {
		var heading = "Instructions Details for CINO : " + cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./EcourtsCaseSearch.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="
					+ cino;
			//alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};
	function viewCaseDetailsPopup(cino) {
		var heading = "View Case Details for CINO : " + cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="
					+ cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};

	function getCaseTypedetails() {

		var caseType = $("#caseType1").val();
		var regYear = $("#regYear1").val();
		var mainCaseNo = $("#mainCaseNo").val();

		if ((caseType != null && caseType != "" && caseType != "0")
				&& (regYear != null && regYear != "" && regYear != "0")
				&& (mainCaseNo != null && mainCaseNo != "" && mainCaseNo != "0")) {
			var caseTypeCode = caseType + "/" + regYear + "/" + mainCaseNo;
			// alert("caseTypeCode--"+caseTypeCode)
			var url = "./GPOAck.do?mode=getCaseTypedetails&&caseTypeCode="
					+ caseTypeCode;
			$.post(url, function(data) {
				//alert("data--"+data);

				$("#megId").html(data);
				//$("#caseTypeCode").val(details['1']);

			});
		}
	}

	//-----------------------

	function caseTypeSelect() {
		//$(".oldTypediv").hide();
		//$(".NewTypediv").hide();

		var chkdVal = $("#oldNewType:checked").val();

		//alert("chkdVal--"+chkdVal);

		if (chkdVal == "New") {
			$(".oldTypediv").hide();
			$(".NewTypediv").show();
			//alert("1--"+chkdVal);
		} else {
			$(".oldTypediv").show();
			$(".NewTypediv").hide();
			//alert("2--"+chkdVal);
		}

		//showDepts();
	}

	$(document).ready(function() {

		caseTypeSelect();

	});

	function fnSubmitCategory() {
		if (($("#instructions").val() == "" || $("#instructions").val() == "0")) {
			alert("Please Enter Instructions");
			return false;
		}
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	}
</script>