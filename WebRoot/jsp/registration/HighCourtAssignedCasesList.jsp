<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div class="page-content fade-in-up">
	<div class="ibox">
		<div class="ibox-head">
				<div class="ibox-title">
					High Court Cases List - Assigned
				</div>
			</div>
		<div class="ibox-body">
			<html:form method="post" action="/HighCourtCasesAssignedList">

				<html:hidden styleId="mode" styleClass="form-control"
					property="mode" />
				<html:hidden styleId="cINO" property="dynaForm(cINO)" />

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

					<logic:notEmpty name="CASESLIST">
						<div class="table-responsive">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CINo</th>
										<th>Date of Filing</th>
										<th>Case Type</th>
										<th>Reg.No.</th>
										<th>Reg. Year</th>
										<th>File No.</th>
										<th>File Year</th>
										<th>Date of Next List</th>
										<th>Bench</th>
										<th>Judge Name</th>
										<th>Petitioner</th>
										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>
										<th>Orders</th>
										<th>Assigned To</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLIST" indexId="i">
										<tr>
											<td>${i+1 }</td>
											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />
											<td><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.type_name_fil }</td>
											<td>${map.reg_no}</td>
											<td>${map.reg_year }</td>
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
											<td>${map.res_name }</td>
											<td>${map.orderpaths }</td>
											<td nowrap="nowrap">${map.global_org_name}<br />
												${map.fullname_en} - ${map.designation_name_en} <br />
												${map.mobile1} - ${map.email}
											</td>
											<td><input type="submit" name="submit" value="Pull Back"
												onclick="sendBack('${map.cino}');"
												class="btn btn-sm btn-warning" /></td>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="18">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
						</div>
					</logic:notEmpty>

				</div>
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
						style="width:100%;min-height:350px;;border:0px;"> </iframe>
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
<script>
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

	function sendBack(cid) {
		$("#cINO").val(cid);
		$("#mode").val("sendCaseBack");
		document.forms[0].submit();
	}
</script>
