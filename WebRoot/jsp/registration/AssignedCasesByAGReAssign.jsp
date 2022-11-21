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
	<html:form action="/AssignedCasesByAG" enctype="multipart/form-data"
		styleId="AssignedCasesToSectionForm">
		<html:hidden styleId="mode" property="mode" />
		<%-- <html:hidden property="dynaForm(roleId)" styleId="roleId" /> --%>
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
		<div class="row">
			<div class="col-md-12">
				<div class="ibox">
					<div class="ibox-body">
						<%-- <div class="row ">
							<div class="col-md-3 col-xs-12 pull-right">
								<b> Enter Remarks </b>
							</div>
							<div class="col-md-3 col-xs-12">
								<html:textarea styleId="caseRemarks"
									property="dynaForm(caseRemarks)" styleClass="form-control"
									cols="50" rows="5">
								</html:textarea>
							</div>
						</div>
						<div class="row ">
							<div class="col-md-3 col-xs-12 pull-right">
								<b> Upload file: </b>
							</div>
							<br>
							<div class="col-md-3 col-xs-12">
								<html:file property="changeLetter" styleId="changeLetter"
									styleClass="form-control"></html:file>
							</div>
						</div> --%>
						
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

							<div class="row ">
								<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
									<b> Upload file: </b>
								</div>
								<br>
								<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
									<html:file property="changeLetter" styleId="changeLetter"
										styleClass="form-control"></html:file>
								</div>
							</div>
						
						<hr />
						
						<div class="row">
								<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
									<b> Select Officer: </b>
								</div>
								<br>
								<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
									<div class="form-group">
										<html:select styleId="emp_id" property="dynaForm(emp_id)"
											styleClass="select2Class" style="width:100%;">
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
											<input type="submit" name="submit" value="Assign Cases"
												class="btn btn-success" onclick="return fnSubmitCategory();" />
										</div>
									</div>
								</div>
							</div>
						<%-- <div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label>Select Employee <bean:message key="mandatory" /></label>
									<html:select styleId="emp_id" property="dynaForm(emp_id)"
										styleClass="select2Class" style="width:100%;">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty name="CommonForm"
											property="dynaForm(AGOFFICELIST)">
											<html:optionsCollection name="CommonForm"
												property="dynaForm(AGOFFICELIST)" />
										</logic:notEmpty>
									</html:select>
								</div>
								<div class="row" style="text-align: right">
									<div class="col-md-12 col-xs-12">
										<input type="submit" name="submit" value="Assign Cases"
											class="btn btn-success" onclick="return fnSubmitCategory();" />
									</div>
								</div>
							</div>
						</div> --%>
					</div>
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
	function fnSubmitCategory() {
		
		//alert("wait");
		/* if (($("#caseRemarks").val() == "" || $("#caseRemarks").val() == "0")) {
			alert("Please Enter Remarks");
			return false;
		} */
		
		var nicE = new nicEditors.findEditor('caseRemarks');
		var question = nicE.getContent();
		
		 if(question==null || question=="" || question=="<br>"){
			alert("Enter Remarks");
			$("#caseRemarks").focus();
			return false;
		} 
		
		if (($("#emp_id").val() == "" || $("#emp_id").val() == "0")) {
			alert("Please Select employee");
			return false;
		}
		
		$("#mode").val("caseReAssignSubmit");
		$("#AssignedCasesToSectionForm").submit();
	}
	
</script>