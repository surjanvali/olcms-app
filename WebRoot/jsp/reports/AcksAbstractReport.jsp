<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<%-- <h1 class="page-title">
		<logic:notEmpty name="HEADING">
					New Cases Abstract Report
				</logic:notEmpty>
	</h1> --%>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/AcksAbstractReport"
		styleId="acksAbstractFormId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(inserted_by)" styleId="inserted_by" />
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
					<div class="ibox-title">New Cases Abstract Report</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Department</label>
								<html:select styleId="deptId" property="dynaForm(deptId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(deptList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="fromDate" property="dynaForm(fromDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">To Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="toDate" property="dynaForm(toDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Type</label>
								<html:select styleId="caseTypeId"
									property="dynaForm(caseTypeId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(caseTypesList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(caseTypesList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Advocate Name</label>
								<html:text styleId="advcteName" property="dynaForm(advcteName)"
									styleClass="form-control" maxlength="25" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Petitioner Name</label>
								<html:text styleId="petitionerName"
									property="dynaForm(petitionerName)" styleClass="form-control"
									maxlength="50" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="button" name="showcases" value="Show Cases"
								class="btn btn-success" onclick="return fnShowCases();" /> <input
								type="button" name="showcases2" value="Show Department Wise"
								class="btn btn-success" onclick="return fnShowDeptWise();" /> <input
								type="button" name="showcases3" value="Show District Wise"
								class="btn btn-success" onclick="return fnShowDistWise();" />

							<logic:present name="SHOWUSERWISE">
								<input type="button" name="showcases4" value="Show User Wise"
									class="btn btn-success" onclick="return fnShowUserWise();" />
							</logic:present>
						</div>
					</div>
				</div>
			</div>
			<logic:present name="DISTWISEACKS">
				<div class="ibox">
					<!-- <div class="ibox-head">
					<div class="ibox-title">New Cases Abstract</div>
				</div> -->
					<div class="ibox-body">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>District</th>
										<th>New Cases Registered</th>
									</tr>
								</thead>
								<tbody>
									<bean:define id="totTot" value="0"></bean:define>
									<logic:iterate id="map" name="DISTWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><a href="javascript:showDistCases('${map.distid}');">
													${map.district_name } </a></td>
											<td style="text-align: right;">${map.acks }</td>
										</tr>
										<bean:define id="totTot" value="${totTot + map.acks }"></bean:define>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="2" style="text-align: center;">Totals</td>
										<td style="text-align: right;">${totTot }</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</logic:present>
			<logic:present name="DEPTWISEACKS">
				<div class="ibox">
					<!-- <div class="ibox-head">
					<div class="ibox-title">New Cases Abstract</div>
				</div> -->
					<div class="ibox-body">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Primary Respondent / Department</th>
										<th>New Cases Registered</th>
									</tr>
								</thead>
								<tbody>
									<bean:define id="totTot" value="0"></bean:define>
									<logic:iterate id="map" name="DEPTWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><a
												href="javascript:showDeptCases('${map.dept_code}');">
													${map.dept_code} - ${map.description }</a></td>
											<td style="text-align: right;">${map.acks }</td>
										</tr>
										<bean:define id="totTot" value="${totTot + map.acks }"></bean:define>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="2" style="text-align: center;">Totals</td>
										<td style="text-align: right;">${totTot }</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</logic:present>
			<logic:present name="USERWISEACKS">
				<div class="ibox">
					<!-- <div class="ibox-head">
					<div class="ibox-title">New Cases Abstract</div>
				</div> -->
					<div class="ibox-body">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>User</th>
										<th>New Cases Registered</th>
									</tr>
								</thead>
								<tbody>
									<bean:define id="totTot" value="0"></bean:define>
									<logic:iterate id="map" name="USERWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><a
												href="javascript:showUserWise('${map.inserted_by}');">
													${map.inserted_by } </a></td>
											<td style="text-align: right;">${map.acks }</td>
										</tr>
										<bean:define id="totTot" value="${totTot + map.acks }"></bean:define>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="2" style="text-align: center;">Totals</td>
										<td style="text-align: right;">${totTot }</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</logic:present>

			<logic:notEmpty name="CASEWISEACKS">
				<div class="ibox">
					<div class="ibox-body">
						<div class="table-responsive">
							<table class="table table-striped table-bordered table-hover"
								id="example">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Ack No.</th>
										<th>HC Ack No.</th>
										<th>Date</th>
										<th>District</th>
										<th>Case Type</th>
										<th>Main Case No.</th>
										<th>Departments / Respondents</th>
										<th>Advocate CC No.</th>
										<th>Advocate Name</th>
										<th>Petitioner</th>
										<th>Download / Print</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASEWISEACKS" indexId="i">
										<tr>
											<td>${i+1 }</td>
											<td>${map.ack_no }</td>
											<td><logic:notEqual value="-" name="map"
													property="hc_ack_no">

													<span style="color: navy;font-weight: bold;">${map.hc_ack_no }</span>

												</logic:notEqual></td>
											<td nowrap="nowrap">${map.generated_date }</td>
											<td>${map.district_name }</td>
											<td>${map.case_full_name }</td>
											<td>${map.maincaseno }</td>
											<td nowrap="nowrap">${map.dept_descs }</td>
											<td>${map.advocateccno }</td>
											<td>${map.advocatename }</td>
											<td>${map.petitioner_name }</td>



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
												</logic:present> <!-- //id="btnShowPopup"  --> <%-- <input type="button"
												id="btnShowPopup" value="Scanned Affidavit"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" /> --%>

												<logic:present name="map" property="ack_no">


													<logic:notEqual value="-" name="map" property="hc_ack_no">
														<a
															href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
															target="_new" title="Print Barcode"
															class="btn btn-sm btn-info">
													</logic:notEqual>


													<logic:equal value="-" name="map" property="hc_ack_no">
														<a
															href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
															target="_new" title="Print Barcode"
															class="btn btn-sm btn-info">
													</logic:equal>



													<i class="fa fa-save"></i>
													<span>Scanned Affidavit</span>
													</a>
												</logic:present></td>
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

<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:200px;">
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
				<!-- <div class="row">
					<div class="col-sm-12" style="margin:0px 0px 0px 0px;"> -->
				<iframe src="" id="pdf_view" name="model_window"
					style="width:100%;min-height:600px;border:0px;"> </iframe>
				<!-- </div>
				</div> -->
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- <div id="header"></div>
	<div id="pdf_view"></div>
		<div class="col-sm-10" style="margin:20px 0px 20px 0px;"></div>
	
	<div id="footer"></div> -->


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>



<script>
	$('.datepicker').datepicker({
		uiLibrary : 'bootstrap4'
	});

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
	});

	function viewCaseDetailsPopup(ack_no) {
		var heading = "View Case Details for ACK No : " + ack_no;
		var srclink = "";
		if (ack_no != null && ack_no != "" && ack_no != "0") {
			srclink = "./AcksAbstractReport.do?mode=getAck_no&SHOWPOPUP=SHOWPOPUP&cino="
					+ ack_no;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#pdf_view").prop("src", srclink)

				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};

	function fnShowDistWise() {
		$("#mode").val("showDistWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowUserWise() {
		$("#mode").val("showUserWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowDeptWise() {
		$("#mode").val("showDeptWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}
	function fnShowCases() {
		$("#mode").val("showCaseWise");
		$("#acksAbstractFormId").submit();
		//return true;
	}

	function showDeptCases(deptid) {
		$("#districtId").val($("#districtId").val());
		$("#deptId").val(deptid);
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		// $("#acksAbstractForm").submit();
		$("#acksAbstractFormId").submit();
		//$(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + $("#districtId").val() + "&deptId=" + deptid + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}

	function showDistCases(distid) {
		$("#districtId").val(distid);
		$("#deptId").val($("#deptId").val());
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		//alert("districtId:" + $("#districtId").val());
		//alert("deptId:" + $("#deptId").val());
		$("#acksAbstractFormId").submit();
		// document.forms[0].submit(); */
		// $(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + distid + "&deptId=" + $("#deptId").val() + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());

	}
	function showUserWise(inserted_by) {
		$("#inserted_by").val(inserted_by);
		$("#inserted_by").val($("#inserted_by").val());
		$("#mode").val("showCaseWise");
		//alert("mode:" + $("#mode").val());
		// alert("inserted_by:" + $("#inserted_by").val());
		//alert("deptId:" + $("#deptId").val());
		$("#acksAbstractFormId").submit();
		// document.forms[0].submit(); */
		// $(location).attr( "href", "./AcksAbstractReport.do?mode=showCaseWise&districtId=" + distid + "&deptId=" + $("#deptId").val() + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());

	}
</script>