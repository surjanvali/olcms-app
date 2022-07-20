<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'> -->
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>

<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
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
			<div class="ibox-title">
				<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
			</div>
		</div>
		<div class="ibox-body">

			<html:form method="post" action="/HighCourtCauseList"
				styleId="HighCourtCauseList">

				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(list_date)" styleId="list_date" />



				<div class="panel-body">

					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
							Select Date:<font color="red">*</font>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 ">

							<div class="input-group date">
								<span class="input-group-addon bg-white"><i
									class="fa fa-calendar"></i> </span>
								<html:text styleId="causelist_date"
									property="dynaForm(causelist_date)"
									styleClass="form-control datepicker" />
							</div>

						</div>


						<div class="col-xs-12 col-sm-12 col-md-2 col-lg-2">
							<div class='btn btn-success pull-right'
								onclick="showCauseList();">Show Cause List</div>
						</div>

						<!-- <div class="col-xs-12 col-sm-12 col-md-2 col-lg-2">
							<div class='btn btn-success pull-right'
								onclick="causeReportList();">Show CauseList Cases</div>
						</div> -->

					</div>
				</div>
				<c:if test="${show!='causeReportList'}">
					<logic:notEmpty name="causelist">
						<div class="table-responsive">
							<table class="table table-striped table-bordered table-hover"
								id="example">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CauseList Date</th>
										<th>Bench ID</th>
										<th>Judge Name</th>
										<th>CauseList ID</th>
										<th>CauseList Type</th>
										<th>Document</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="causelist" indexId="i">
										<tr>
											<td>${i+1 }</td>
											<td>${map.causelist_date }</td>
											<td>${map.bench_id }</td>
											<td>${map.judge_name }</td>
											<td>${map.causelist_id }</td>
											<td>${map.cause_list_type }</td>
											<td style="text-align: center;">
												<%-- ${map.document }  --%>
												<logic:notEmpty name="map" property="document">
													<logic:notEqual value="" name="map" property="document">
														<a href="./${map.document }" target="_new"
															class="btn btn-sm btn-info"><i class="fa fa-save"></i></a>
													</logic:notEqual>
												</logic:notEmpty>
											</td>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="7">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</logic:notEmpty>
				</c:if>

				<c:if test="${show=='causeReportList'}">
					<logic:notEmpty name="causeReportList">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CINo</th>
										<th>Scanned Affidavit</th>
										<th>Date of Filing</th>

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

									<logic:iterate id="map" name="causeReportList" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td style="text-align: center;"><c:if
													test="${map.cino1!='-'}">
													<input type="button" id="btnShowPopup" value="${map.cino}"
														class="btn btn-sm btn-info waves-effect waves-light"
														onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />
												</c:if> <c:if test="${map.cino1=='-'}">	 - </c:if></td>
											<td><logic:notEmpty name="map"
													property="scanned_document_path1">
													<logic:notEqual value="-" name="map"
														property="scanned_document_path1">
														<a href="./${map.scanned_document_path}" target="_new"
															class="btn btn-sm btn-info"><i
															class="glyphicon glyphicon-save"></i><span>Scanned
																Affidavit</span></a>
													</logic:notEqual>
												</logic:notEmpty></td>
											<td><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>

											<td>${map.type_name_fil }/${map.reg_no} / ${map.reg_year }</td>
											<td style="width: 300px;">${map.prayer }</td>

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
										<td colspan="19">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</logic:notEmpty>
				</c:if>

			</html:form>
		</div>
	</div>
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
				<p>
					<iframe src="" id="page" name="model_window"
						style="width:100%;min-height:600px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<!-- <script
	src='https://cdn.jsdelivr.net/npm/disableautofill/src/jquery.disableAutoFill.min.js'></script> -->

<script>
	$(document).ready(function() {
		$("#causelist_date").attr("autocomplete", "off");
		// $('#HighCourtCauseList').disableAutoFill();

		var endDateNew = new Date();
		endDateNew.setDate(endDateNew.getDate() + 1);

		$('.datepicker').datepicker({
			uiLibrary : 'bootstrap4',
			format : 'mm/dd/yyyy',
			startDate : new Date("2022-01-24"), // Set min Date
			endDate : endDateNew
		// Set max Date
		});

	});
</script>
<script>
	function showCauseList() {
		// alert(document.getElementById("causelist_date").value);
		var date = document.getElementById("causelist_date").value;
		$("#list_date").val(date);
		if ($("#causelist_date").val() == ""
				|| $("#causelist_date").val() == null) {
			alert("Select CauseList Date");
			$("#causelist_date").focus();
			return false;
		}
		$("#mode").val("ShowCauselist");
		$("#HighCourtCauseList").submit();
	}
	
	function causeReportList() {
		var date = document.getElementById("causelist_date").value;
		$("#list_date").val(date);
		if ($("#causelist_date").val() == ""
				|| $("#causelist_date").val() == null) {
			alert("Select CauseList Date");
			$("#causelist_date").focus();
			return false;
		}
		$("#mode").val("getCauseReportList");
		$("#HighCourtCauseList").submit();
	}
	
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
</script>


