<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js"
	type="text/javascript"></script>

<style>
body {
	overflow-y: auto;
}
</style>

<div class="page-content fade-in-up">
	<html:form action="/AssignedCasesByAG" enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<%-- <html:hidden styleId="cino" property="dynaForm(cino)" /> --%>
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
					<%-- <logic:notEmpty name="errorMsg">
						<div class="alert alert-danger" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<i class="mdi mdi-block-helper"></i> <strong>${errorMsg}</strong>
						</div>
					</logic:notEmpty> --%>
				</div>
			</div>
		</div>
				<div class="ibox">
		<div class="ibox-head">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b>Submit Response</b>
					</h4>
				</div>
			</div>
					<div class="ibox-body">
						<html:hidden styleId="cino" property="dynaForm(cino)" />
						<%-- <div class="row ">
							<div class="col-md-6 col-xs-12 pull-right">

								<b> Enter Response </b>

							</div>
							<div class="col-md-6 col-xs-12">
								<html:textarea styleId="responseText"
									property="dynaForm(responseText)" styleClass="form-control"
									cols="50" rows="5">
								</html:textarea>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 col-xs-12 pull-right">
								<b> Upload file: </b>
							</div>
							<div class="col-md-6 col-xs-12">
								<html:file property="changeLetter" styleId="changeLetter"
									styleClass="form-control"></html:file>
							</div>
						</div> --%>
						
						
						<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="form-group">
										<label for="sel1" id="remaeksTextId">Enter Response: </label>
										<script type="text/javascript" src="js/nicEdit-latest.js"></script>
										<script type="text/javascript">
								bkLib.onDomLoaded(function() {
									new nicEditor({
										fullPanel : true
									}).panelInstance('responseText');
								});
							</script>
										<html:textarea cols="600" styleId="responseText"
											property="dynaForm(responseText)"
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
						
						
					</div>
					<div class="ibox-footer text-center">
						<div class="row">
							<div class="col-md-12 col-xs-12 text-center">
								<input type="submit" name="submit" value="Submit"
									class="btn btn-success" onclick="return fnSubmitCategory();" />
							</div>
						</div>
					</div>
				</div>
	
				
	</html:form>
</div>

<script type="text/javascript">
	/* function fnSubmitCategory() {
		if (($("#instructions").val() == "" || $("#instructions").val() == "0")) {
			alert("Please Enter Instructions");
			return false;
		}
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	} */
	
	
	function fnSubmitCategory() {
		/* if (($("#responseText").val() == "" || $("#responseText").val() == null)) {
			alert("Please Enter Response");
			return false;
		} */
		
		var nicE = new nicEditors.findEditor('responseText');
		var question = nicE.getContent();
		
		 if(question==null || question=="" || question=="<br>"){
			alert("Enter Remarks");
			$("#responseText").focus();
			return false;
		} 
		
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	}
	
	
</script>