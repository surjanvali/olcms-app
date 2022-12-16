<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'> -->
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css' />

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css"
	rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<!-- <link href="assetsnew/css/main.min.css" rel="stylesheet" /> -->

<link href="./multiselect/bootstrap-multiselect.css"
	rel="stylesheet" />
	<script src="./multiselect/multiselect.js"></script>

<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
					General Remarks
				</logic:notEmpty>
	</h1>

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/GeneralRemarks"
		enctype="multipart/form-data" styleId="HighCourtCasesListForm">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="selectedCaseIds"
			property="dynaForm(selectedCaseIds)" />
		<html:hidden styleId="respondentIds"
			property="dynaForm(respondentIds)" />


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
				<div class="ibox-title">General Remarks</div>
			</div>


				<div class="row">

				<div class="col-md-12">
					<div class="ibox">
						<div class="ibox-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="form-group">
										<label for="sel1" id="remaeksTextId">Enter Remarks: </label>
										<script type="text/javascript" src="js/nicEdit-latest.js"></script>
										<script type="text/javascript">
								bkLib.onDomLoaded(function() {
									new nicEditor({
										fullPanel : true
									}).panelInstance('caseRemarks');
								});
							</script>
										<html:textarea cols="600" styleId="caseRemarks"
											property="dynaForm(caseRemarks)"
											style="width: 500%; height: 250px;">
										</html:textarea>
										
										
									</div>
								</div>
							</div>

<br/>

							<div class="row">
								<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
									<b> Select Officer: </b>
								</div>
								<br>
								<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
									<div class="form-group">
										<%-- <html:select styleId="emp_id" property="dynaForm(emp_id)"
											styleClass="select2Class" style="width:100%;">
											<html:option value="0">---SELECT---</html:option>
											<logic:notEmpty name="CommonForm"
												property="dynaForm(AGOFFICELIST)">
												<html:optionsCollection name="CommonForm"
													property="dynaForm(AGOFFICELIST)" />
											</logic:notEmpty>
										</html:select> --%>
										
										<html:select property="dynaForm(emp_id)" styleClass="multiselect-ui form-control select2Class" 
										   styleId="emp_id" multiple="multiple" style="width:100%;">
										   <html:option value="0">---SELECT---</html:option>
											<logic:notEmpty name="CommonForm"
												property="dynaForm(AGOFFICELIST)">
												<html:optionsCollection name="CommonForm"
													property="dynaForm(AGOFFICELIST)" />
											</logic:notEmpty>
										</html:select>
									</div>
									<div class="row" style="text-align: right">
										<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
											<input type="submit" name="submit" value="Submit"
												class="btn btn-success" onclick="return fnSubmitCategory();" />
										</div>
									</div>
									<br/>
									<br/>
									
								</div>
							</div>
							<div class="row" style="text-align: left">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<font color="red"> Note: <b>Officer</b> Select Must and Should be <b>Ctl+Select<b></font>
								</div>
							</div>
						</div>
					</div>
</div>
		</div>
		</div>
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

function reloadParent(){
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
	
	$('#panel-modal').modal({backdrop : 'static', keyboard : false});
	$('#panel-modal').modal('show');
	
	
	$(".btnClosePopup").click(function () {
		$("#panel-modal").modal("hide");
	});
});

	/* function getData() {
		
		if($("#caseTypeId1").val()==null || $("#caseTypeId1").val()=="" || $("#caseTypeId1").val()=="0" ){
			alert("Enter case Type");
			$("#caseTypeId").focus();
			return false;
		} 
		if($("#regYear1").val()==null || $("#regYear1").val()=="" || $("#regYear1").val()=="0" ){
			alert("Enter Reg Year");
			$("#regYear1").focus();
			return false;
		} 
		if($("#caseNumber1").val()==null || $("#caseNumber1").val()=="" || $("#caseNumber1").val()=="0" ){
			alert("Enter case Type");
			$("#caseNumber1").focus();
			return false;
		} 
		
		
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
	} */
	
	
	function countValues(){
		//alert("val--"+val);
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#testselectedCaseIds").val(testval);
		// alert("testselectedCaseIds:"+$("#testselectedCaseIds").val());
	}
	
function fnAssignCase(){
		
		var testval = [];
		 $('#caseIds:checked').each(function() {
		   testval.push($(this).val());
		 });
		 $("#selectedCaseIds").val(testval);
		 //alert("selectedCaseIds:"+$("#selectedCaseIds").val());
		if($("#selectedCaseIds").val()==null || $("#selectedCaseIds").val()=="" || $("#selectedCaseIds").val()=="0"){
			alert("Select atleast a case to submit.");
			return false;
		}
		if($("#emp_id").val()==null || $("#emp_id").val()=="" || $("#emp_id").val()=="0"){
			alert("Select Emp.");
			$("#emp_id").focus();
			return false;
		}
		else{
			$("#mode").val("assignCase");
			$("#HighCourtCasesListForm").submit();
		}
	}
	
	function fnShowCases() {
		$("#mode").val("getCasesList");
		$("#HighCourtCasesListForm").submit();
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

	
	function assignCaseToEmployee(cino) {
		var heading = "Assign Case to Employee";
		var srclink = "";
		if (cino != null && cino != "" && cino != "0") {
			srclink = "./AssignmentOfCasesByAG.do?mode=assignCase2EmpPage&cino=" + cino;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			};
		};
	};
	
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
	
</script>

<script type="text/javascript">
	function fnSubmitCategory() {
		
		//alert("--"+($("#caseNumber1").val()));
		
		
		
		var nicE = new nicEditors.findEditor('caseRemarks');
		var question = nicE.getContent();
		
		 if(question==null || question=="" || question=="<br>"){
			alert("Enter Remarks");
			$("#caseRemarks").focus();
			return false;
		} 
		 
		
		
		if($("#emp_id").val()==null || $("#emp_id").val()=="" || $("#emp_id").val()=="0"){
			alert("Select Officer");
			$("#emp_id").focus();
			return false;
		}
		
		
		//alert("emp_id====>"+$("#emp_id").val());
			
			
			
		
		
		$("#mode").val("assignCase");
		$("#respondentIds").val($("#emp_id").val());
		$("#HighCourtCasesListForm").submit();
		
	}

	$("#addResp")
			.click(
					function() {
						//alert("wait....")
						let rowfyable = $("#RESPSTABID").closest('table');
						//let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
						let rowCount = $("#RESPSTABID tbody tr").length; //$('tbody', rowfyable).rows.length;
						let rowCount2 = rowCount + 1;
						let caseTypeId = $("#caseTypeId" + rowCount).val();
						let regYear = $("#regYear" + rowCount).val();
						let caseNumber = $("#caseNumber" + rowCount).val();

						//alert("prevVal--"+caseNumber);
						if ((caseTypeId != null && caseTypeId != "" && caseTypeId != "All")
								&& (regYear != null && regYear != "" && regYear != "0") && (caseNumber != null && caseNumber != "" && caseNumber != "0")) {
							console.log("rowCount:"+rowCount);
							// console.log("rowCount2:"+rowCount2);

							$('tbody', rowfyable)
									.append(
											"<tr id='"+rowCount2+"'><td>"
													+ rowCount2
													+ ".</td><td><select name='dynaForm(caseTypeId" + rowCount2 + ")' id='caseTypeId" + rowCount2 + "' style='width: 100%;' ></select></td><td>"
													+ "<select name='dynaForm(regYear" + rowCount2 + ")' id='regYear" + rowCount2 + "' style='width: 100%;' ></select></td>"
													+ "<td><input type='text' name='dynaForm(caseNumber"
													+ rowCount2
													+ ")' id='caseNumber"
													+ rowCount2
													+ "' style='height:30px;width:300px' ></text></td></tr>"+ "");  /* <td><input type='submit' name='submit' value='Case Details'onclick='return getData();' /></td> */
							
							$("#caseTypeId" + rowCount + " option").clone().appendTo("#caseTypeId" + rowCount2);
							$("#caseTypeId" + rowCount2).select2();
							$("#caseTypeId" + rowCount2).select2("val", "0");
							
							
							
							$("#regYear" + rowCount + " option").clone().appendTo("#regYear" + rowCount2);
							$("#regYear" + rowCount2).select2();
							$("#regYear" + rowCount2).select2("val", "0");
							

							$("#respondentIds").val(
									$("#RESPSTABID tbody tr").length);
						}
//alert("caseTypeId---"+caseTypeId);
						
						if ((caseTypeId == "" || caseTypeId == "0")) {
							alert("Pease Select Case Type");
							$("#caseTypeId" + rowCount).focus();
						}
					//	alert("regYear---"+regYear);
						if ((regYear == "" ||regYear == "0")) {
							alert(" Pease Select Case Reg Year");
							$("#regYear" + rowCount).focus();
						}
						//alert("caseNumber---"+caseNumber);
						if ((caseNumber == "")) {
							alert("Enter Case Number");
							$("#caseNumber" + rowCount).focus();
						}

					});

	$("#removeResp").click(function() {
		let rowfyable = $("#RESPSTABID").closest('table');
		let rowCount = $("#RESPSTABID tbody tr").length;
		if (rowCount > 1) {
			$('tbody tr:last', rowfyable).remove();
		}
		else {
			alert ("Should have atleast one valid Case Details")
		}
	});
	
	$(function() {
	    $('.multiselect-ui').multiselect({
	        includeSelectAllOption: true
	    });
	});

</script>


