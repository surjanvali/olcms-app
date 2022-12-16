<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div class="page-content fade-in-up">
<html:form method="post" action="/PullBackNewCases">

	<html:hidden styleId="mode" styleClass="form-control" property="mode" />
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
	</div>
	
		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">High Court Cases List - Assigned</div>
			</div>
			<div class="ibox-body">
				<logic:notEmpty name="CASESLIST">
					<div class="table-responsive">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Acknowledge Number</th>
									<th>Advocate Name</th>
									<th>Advocate CC No</th>
									<th>Main Case No</th>
									<th>Date</th>
									<th>Petitioner Name</th>
									<th>Mode Filing</th>
									<th>Assigned To</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.ack_no}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" /></td>
											<%-- <td>
												<div class="form-group">
													<label class="ui-checkbox"> <input type="checkbox"
														name="caseIds" value="${map.ack_no}@${map.respondent_slno}" id="caseIds" /> <span
														class="input-span"></span></label>
												</div>
											</td> --%>
										
										<td>${map.advocatename }</td>
										<td>${map.advocateccno}</td>
										<td>${map.maincaseno}</td>
										<td>${map.inserted_time}</td>
										<td>${map.petitioner_name}</td>
										<td>${map.mode_filing}</td>
										<td>${map.assigned_to}</td>
										<td><input type="submit" name="submit" value="Pull Back"
											onclick="sendBack('${map.ack_no}@${map.respondent_slno}');"
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
