<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <link href="./assetsnew/vendors/DataTables/datatables.min.css" rel="stylesheet" />
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js" type="text/javascript"></script>
<style>
body {
	overflow-y: auto;
}
</style> -->

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1>
	<%-- <ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="./Welcome.do"><i
				class="la la-home font-20"></i> </a></li>
		<li class="breadcrumb-item"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
		</li>
	</ol> --%>
</div>
<div class="page-content fade-in-up">
	<html:form action="/AssignedCasesToSection"
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
				<logic:notEmpty name="CASESLIST">
					<div class="table-responsive">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>CINo</th>
									<th>Date of Filing</th>
									<th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th>
									<th>Filing No.</th>
									<th>Filing Year</th>
									<!-- <th>Date of Next List</th>
									<th>Bench</th> -->
									<th>Judge Name</th>
									<th>Petitioner</th>
									<th>District</th>
									<th>Purpose</th>
									<th>Respondents</th>
									<th>Orders</th>
									<th style="width: 150px !important;">Action</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>
											<button class="btn btn-sm btn-primary"
												onclick="viewCaseDtls('${map.cino}');">${map.cino}</button>


											<!-- <a href="./AssignedCasesToSection.do?mode=getCino"
											class="btn btn-sm btn-primary"> </a> -->
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
										<td>${map.fil_no}</td>
										<td>${map.fil_year }</td>
										<%-- <td><logic:notEmpty name="map" property="date_next_list">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty>
										</td>
										<td>${map.bench_name }</td> --%>
										<td>Hon'ble Judge : ${map.coram }</td>
										<td>${map.pet_name }</td>
										<td>${map.dist_name }</td>
										<td>${map.purpose_name }</td>
										<td>${map.res_name }</td>
										<td style="text-align: center;">${map.orderpaths }</td>
										<td style="min-width: 150px !important;"><logic:notEqual
												value="-" name="map" property="counter_approved_gp">
												<b>${map.casestatus1 } <br />${map.casestatus2 }<br />
													${map.casestatus3 } <br />${map.casestatus4 }
												</b>
											</logic:notEqual> <logic:equal value="-" name="map"
												property="counter_approved_gp">

												<button class="btn btn-sm btn-primary"
													onclick="caseStatusUpdate('${map.cino}');">Update
													Status</button>
											</logic:equal></td>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="15">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</logic:notEmpty>
			</div>
		</div>
	</html:form>
</div>

<script type="text/javascript">
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}

	function caseStatusUpdate(fileCino) {
		$("#mode").val("caseStatusUpdate");
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
</script>
<!-- <link rel="stylesheet" href="https://cdn.datatables.net/1.11.4/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.2.2/css/buttons.dataTables.min.css">

<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.print.min.js"></script>
<script>
$('#example').DataTable({
  dom: 'Blfrtip',
  buttons: [ 'excel']
});
</script> -->