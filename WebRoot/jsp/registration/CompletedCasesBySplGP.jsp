<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
		</logic:notEmpty>
	</h1>
	
</div>
<div class="page-content fade-in-up">
	<html:form action="/CompletedCasesBySplGP"
		styleId="AssignedCasesToSectionForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(roleId)" styleId="roleId" />
		<html:hidden property="dynaForm(fileCino)" styleId="fileCino" />

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
			<%-- <div class="ibox-head">
				<div class="ibox-title">
					<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
				</div>
			</div> --%>
			<div class="ibox-body">

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover"
						id="example" cellspacing="0" width="100%">
						<thead>
							<tr>
									<th>Sl.No</th>
									<th>Case Reg No.</th>
									<th>CINo</th>
									
									<!-- <th>Assigned to</th> -->
									<th>Date of Filing</th>
									<!-- <th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th> -->

									
									<!-- <th>Prayer</th> -->

									<!-- <th>Filing No.</th>
									<th>Filing Year</th> -->
									<!-- <th>Date of Next List</th>
									<th>Bench</th> -->
									<th>Judge Name</th>
									<th>Petitioner</th>
									<th>District</th>
									<!-- <th>Purpose</th>
									<th>Respondents</th> -->
									<th>Petitioner Advocate</th>
									<th>Respondent Advocate</th>
									<!-- <th>Orders</th>
								<th style="width: 150px !important;">Action / Status</th> -->
								<th>Case Assigned To</th>
								<th>Case Status</th>
								<th>AGOLCMS Status</th>
								<th>Response</th>
								
							</tr>
						</thead>
						<tbody>
							<logic:notEmpty name="CASESLIST">
								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td nowrap="nowrap"><b>${map.type_name_fil }/${map.reg_no}/${map.reg_year}</b>
										</td>
										<td>${map.cino}
											<!-- <a href="./AssignedCasesToSection.do?mode=getCino"
											class="btn btn-sm btn-primary"> </a> -->
										</td>

										<%-- <td nowrap="nowrap">${map.globalorgname}<br />
												${map.fullname} - ${map.designation} <br />
												${map.mobile} - ${map.email}
											</td> --%>
										<td nowrap="nowrap"><logic:notEmpty name="map" property="date_of_filing">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty></td>

										<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
										
										<%-- <td style="min-width: 350px;text-align: justify;"><logic:notEmpty
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
											</logic:notEmpty></td> --%>

										<%-- <td>${map.fil_no}</td>
										<td>${map.fil_year }</td> --%>
										<%-- <td nowrap="nowrap"><logic:notEmpty name="map" property="date_next_list">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_next_list">
																	${map.date_next_list }
																</logic:notEqual>
											</logic:notEmpty></td>
										<td>${map.bench_name }</td> --%>
										<td>Hon'ble Judge : ${map.coram }</td>
										<td>${map.pet_name }</td>
										<td>${map.dist_name }</td>
										<%-- <td>${map.purpose_name }</td> --%>
										<%-- <td>${map.res_name }, ${map.address}</td> --%>

										<td>${map.pet_adv }</td>
										<td>${map.res_adv }</td>
										<%-- <td style="text-align: center;">${map.orderpaths }</td> --%>
										<%-- <td style="min-width: 150px !important;color: navy;"><logic:notEqual
												value="-" name="map" property="counter_approved_gp">
												<b>${map.casestatus1 } <br />${map.casestatus2 }<br />
													${map.casestatus3 } <br />${map.casestatus4 }
												</b>
											</logic:notEqual> <logic:notEqual value="T" name="map"
												property="counter_approved_gp">
												<br />

												<button class="btn btn-sm btn-primary"
													onclick="caseStatusUpdate('${map.cino}');">Update
													Status</button>
											</logic:notEqual>
											
											<input type="button" id="btnShowPopup"
												value="Submit Instructions"
												class="btn btn-sm btn-primary waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}');" />
											
											</td> --%>
											<td>${map.assigned_to}</td>
											<td>${map.ecourts_case_status}</td>
											<td>${map.status}</td>
											<td style="min-width: 50px;text-align: center;">${map.response}
										<%-- <button class="btn btn-info btn-xs" data-container="body"
														data-toggle="popover" data-trigger="hover"
														data-placement="top" data-content="${map.response}"
														data-original-title="" title="">View More</button> --%>
												</td>
											
								</logic:iterate>
							</logic:notEmpty>
						</tbody>
						<tfoot>
							<tR>
								<td colspan="19">&nbsp;</td>
							</tR>
						</tfoot>
					</table>
				</div>

			</div>
		</div>
	</html:form>
</div>
<!-- Modal  Start-->

<div id="MyPopup" class="modal fade" role="dialog"
	style="padding-top:200px;">
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
					<iframe src="" id="page" name="model_window"
						style="width:100%;min-height:600px;border:0px;"> </iframe>
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
<script type="text/javascript">
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function caseStatusUpdate(fileCino) {
		$("#mode").val("caseStatusUpdate");
		// alert("MODE:"+$("#mode").val());
		$("#fileCino").val("" + fileCino);
		$("#AssignedCasesToSectionForm").submit();
	}

	function fileCounterAction(fileCino) {
		$("#mode").val("fileCounterAction");
		$("#fileCino").val("" + fileCino);
		$("#AssignedCasesToSectionForm").submit();
	}
	function viewCaseDtls(fileCino) {
		$("#mode").val("getCino");
		$("#fileCino").val("" + fileCino);
		$("#AssignedCasesToSectionForm").submit();
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