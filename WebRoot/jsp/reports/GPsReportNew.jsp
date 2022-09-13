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
									${HEADING }
								</logic:notEmpty>
	</h1> --%>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/GPsReportNew"
		styleId="HighCourtCasesListForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds" property="dynaForm(selectedCaseIds)" />

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


			<logic:notEmpty name="CASEWISEDATA">
				<div class="ibox">
					<div class="ibox-head">
						<div class="ibox-title">
							<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
						</div>
					</div>

					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Case Type</th>
										<th> Ack No</th>
										<th>Registered Date</th>
										<th>Update Daily Status</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="CASEWISEDATA" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.type_name_reg}</td>
											<td><a href="./GPsReportNew.do?mode=caseStatusUpdate&caseCiNo=${map.cino}&caseType=${map.legacy_ack_flag}" class="btn btn-info btn-md"> 
										 ${map.cino}</a></td>
											<td style="text-align: center;">${map.dt_regis }</td>
											<td>
												 <input type="button" id="btnShowPopup" value="Submit Daily Status"
												class="btn btn-sm btn-success waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}','${map.legacy_ack_flag}');" />
											</td>
											<td style="text-align: center;">Pending</td>
											<%-- 
												<td><a href="GPOAck.do?mode=getAcknowledementsListAll&ackDate=${map.ack_date}">${map.ack_date }</a></td>
												<td style="text-align: right;">${map.total}</td>
												<td style="text-align: right;"><a href="GPOAck.do?mode=getAcknowledementsListAll&ackDate=${map.ack_date}&ackType=NEW">${map.new_acks }</a></td>
												<td style="text-align: right;"><a href="GPOAck.do?mode=getAcknowledementsListAll&ackDate=${map.ack_date}&ackType=OLD">${map.existing_acks }</a></td> 
											--%>
										</tr>
									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="6" style="text-align: center;">&nbsp;</td>
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

<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js"
	type="text/javascript"></script>
<!-- <script src="https://apbudget.apcfss.in/js/select2.js"></script> -->
<script>
        $('.datepicker').datepicker({
            uiLibrary: 'bootstrap4'
        });
    </script>
<script type="text/javascript">
function showAckList() {
	$("#mode").val("getAcknowledementsList");
	// alert($("#mode").val());
	$("#HighCourtCasesListForm").submit();
}

function reloadParent(){
	location.reload(true);
}

function showDepts() {
	var chkdVal= $("#officerType:checked").val();	
	/* if($("#caseDept"))
		$("#caseDept").select2('destroy');
	if($("#empDept"))
		$("#empDept").select2('destroy'); */
	// alert($("#empDept").val());
	var data = {
		mode : "AjaxAction",
		typeCode : chkdVal,
		getType : "getDeptList"
	}
	$.post("AjaxModels.do", data).done(function(res) {
		if (res != '' && (chkdVal=="S-HOD" || chkdVal=="D-HOD" || chkdVal=="DC-NO")) {
			$("#caseDept").select2('destroy');
			$("#caseDept").html(res);
			$("#caseDept").select2();
			$("#caseDept").select2("val", "0");
		}
		if (res != '' && (chkdVal=="SD-SO" || chkdVal=="OD-SO" || chkdVal=="DC-SO")) {
			$("#empDept").select2('destroy');
			$("#empDept").html(res);
			$("#empDept").select2();
			$("#empDept").select2("val", "0");
		}
	}).fail(function(exc) {
		alert("Error Occured.Please Try Again");
	});
}

function changeReport(){
	
	var chkdVal= $("#officerType:checked").val();	
	
	$(".depthodDiv").hide();
	$(".depthodSectionDiv").hide();
	$(".distDiv").hide();
	$(".disthodDiv").hide();
	$(".distdepthodSectionDiv").hide();
	
	if(chkdVal=="S-HOD"){
		$(".depthodDiv").show();
	} else if(chkdVal=="D-HOD"){
		$(".depthodDiv").show();
	} else if(chkdVal=="SD-SO"){
		$(".depthodSectionDiv").show();
	} else if(chkdVal=="OD-SO"){
		$(".depthodSectionDiv").show();
	} else if(chkdVal=="DC"){
		$(".distDiv").show();
	}else if(chkdVal=="DC-NO"){
		$(".distDiv").show();
		$(".disthodDiv").show();
	}else if(chkdVal=="DC-SO"){
		$(".depthodSectionDiv").show();
		$(".distdepthodSectionDiv").show();
	} 
	showDepts();
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
	$('#panel-modal').modal({backdrop : 'static', keyboard : false});
	$('#panel-modal').modal('show');
	
	
	$(".btnClosePopup").click(function () {
		$("#panel-modal").modal("hide");
	});
});

	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}
	
	

	

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

	
	
	function viewCaseDetailsPopup(cino) {
		var heading = "View Case Details for CINO : "+cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignedCasesToSection.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino=" + cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
	function viewCaseDetailsPopup1(cino, caseNo) {
		var heading = "View/Submit Daily Status for Case : "+cino;
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./DailyStatusEntry.do?mode=getCino&SHOWPOPUP=SHOWPOPUP&cino=" +cino+"&caseType="+caseNo;
			//srclink = "./EcourtsDeptInstructionNew.do?mode=getCasesList&cino="+cino+"&caseType="+caseNo;
		//	alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
</script>