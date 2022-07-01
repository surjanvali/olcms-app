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
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<style>
.myDiv {
	border: 5px outset red;
	background-color: lightblue;
	text-align: center;
}
</style>
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
					High Court Cases List
				</logic:notEmpty>
	</h1>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/EcourtsDeptInstruction"
		styleId="HighCourtCasesListForm">
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
					<div class="ibox-title">Instructions Entry</div>
				</div>
				<div class="ibox-body">
					<!-- <h4 class="m-t-0 header-title">
		<b>High Court Cases List</b>
	</h4>
	<hr /> -->

					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Registration Year</label>
								<html:select styleId="regYear" property="dynaForm(regYear)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<html:option value="ALL">ALL</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(yearsList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(yearsList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Filing From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofFromDate"
										property="dynaForm(dofFromDate)"
										styleClass="form-control datepicker" />
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Filing From Date</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofToDate" property="dynaForm(dofToDate)"
										styleClass="form-control datepicker" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Purpose</label>
								<html:select styleId="purpose" property="dynaForm(purpose)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(purposeList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(purposeList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>

						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)"
									styleClass="form-control select2Class">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="submit" name="submit" value="Get Cases"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>


				</div>
			</div>

			<logic:notEmpty name="CASESLIST">
				<div class="ibox">
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<!-- <th></th> -->
										<th>CINo</th>
										<th>Date of Filing</th>
										<th>Case Type</th>
										<th>Reg.No.</th>
										<th>Reg. Year</th>

										<th>Petitioner</th>

										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>

										<th>Petitioner Advocate</th>
										<th>Respondent Advocate</th>
										<!-- <th>Category</th> -->
										<th>Action</th>

									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }.</td>

											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />
											</td>
											<td><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
													${map.date_of_filing }
												</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.type_name_fil }</td>
											<td>${map.reg_no}</td>
											<td>${map.reg_year }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name }</td>
											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>
											<%-- <td>${map.finance_category }</td> --%>
											<td><input type="button" id="btnShowPopup"
												value="Submit Instructions"
												class="btn btn-sm btn-primary waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}');" />
											</td>

										</tr>

									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="14">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
				<!-- 	<div class="row" style="text-align: center">
					<div class="col-md-12 col-xs-12">
						<input type="submit" name="submit" value="Submit"
							class="btn btn-success" onclick="return fnSubmitCategory();" />
					</div>
				</div> -->

			</logic:notEmpty>
		</div>

	</html:form>
</div>

<!-- Modal  Start-->

<div id="MyPopup" class="modal fade" role="dialog" style="padding-top:200px;">
	<div class="modal-dialog modal-dialog-centered modal-lg">
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
					<iframe src="" id="page" name="model_window" style="width:100%;min-height:600px;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				 <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button> 
				<!-- <div class="btn btn-danger" data-dismiss="modal">Close</div>  -->
				<!-- <input type="submit" name="submit" value="Close" class="btn btn-danger" data-dismiss="modal" onclick="return fnShowCases();" /> -->
				<!-- <div class="form-group">
					<a href="./EcourtsDeptInstruction.do"
						class="btn btn-danger border-0">Close</a>
				</div> -->
			</div>
		</div>
	</div>
</div>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>

<script type="text/javascript">
	function reloadParent() {
		location.reload(true);
	}

	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
		if ($('#example2')) {
			$('#example2').DataTable();
		}

		// alert("Cases filed in the year 2021 listed below. To view and process other cases please use the Year filter or select From and To dates. Press the 'Get Cases' button for the Cases List.");
		$('#panel-modal').modal({
			backdrop : 'static',
			keyboard : false
		});
		$('#panel-modal').modal('show');

		$(".btnClosePopup").click(function() {
			$("#panel-modal").modal("hide");
		});
	});

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function fnShowCases() {

		if (($("#regYear").val() == "" || $("#regYear").val() == "0")
				&& ($("#dofFromDate").val() == null
						|| $("#dofFromDate").val() == "" || $("#dofFromDate")
						.val() == "0")
				&& ($("#purpose").val() == null || $("#purpose").val() == "" || $(
						"#purpose").val() == "0")
				&& ($("#districtId").val() == null
						|| $("#districtId").val() == "" || $("#districtId")
						.val() == "0")) {
			alert("Please select a filter to get the data.");
			return false;
		}

		if (!($("#dofFromDate").val() == null || $("#dofFromDate").val() == "" || $(
				"#dofFromDate").val() == "0")
				&& ($("#dofToDate").val() == null
						|| $("#dofToDate").val() == "" || $("#dofToDate").val() == "0")) {
			alert("Select Filing To Date");
			$("#dofToDate").focus();
			return false;
		}
		// alert("Please select a filter to get the data.");

		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	}

	function viewCaseDetailsPopup1(cino) {
		var heading = "Instructions Details for CINO : " + cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./EcourtsDeptInstruction.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino="
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
</script>