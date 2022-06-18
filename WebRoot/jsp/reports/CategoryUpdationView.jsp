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

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/HighCourtCasesCategoryUpdationReport">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="cino" property="dynaForm(cino)" />
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
			<%-- <logic:iterate id="map" name="CASESLIST" indexId="i"> --%>
			<logic:present name="USERSLIST">
				<logic:iterate id="datamap" name="USERSLIST">
					<html:hidden styleId="cino" property="dynaForm(cino)"
						value="${datamap.cino}" />
					<div class="ibox-body">
						
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Category: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">


								<html:select styleId="fin_category" value="${datamap.finance_category}"
									property="dynaForm(fin_category)" styleClass="form-control">
									<html:option value="0">---Select Category---</html:option>
									<html:option value="A1">Category - A1</html:option>
									<html:option value="A2">Category - A2</html:option>
									<html:option value="B1">Category - B1</html:option>
									<html:option value="B2">Category - B2</html:option>
									<html:option value="C1">Category - C1</html:option>
									<html:option value="C2">Category - C2</html:option>
								</html:select>
							</div>
						</div>

						<br />
						<br />
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Name Of the work: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="nameOfwork" styleClass="form-control" value="${datamap.work_name}"
									property="dynaForm(nameOfwork)" maxlength="40" />
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Est. Cost(Lakhs) : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="estCost" styleClass="form-control" value="${datamap.est_cost}"
									property="dynaForm(estCost)" maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Administrative Sanction(GO/Proceedings).: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="adminSanction" styleClass="form-control" value="${datamap.admin_sanction}"
									property="dynaForm(adminSanction)" maxlength="40" />
							</div>
						</div>
						<br />
						<br />
						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Grant(SDP/EAP/NABARD/etc..): </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="grant" styleClass="form-control" value="${datamap.grant_val}"
									property="dynaForm(grant)" maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> CFMS BILL NO'S: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="cfmsBill" styleClass="form-control" value="${datamap.cfms_bill}"
									property="dynaForm(cfmsBill)" maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Status: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="status" styleClass="form-control" value="${datamap.bill_status}"
									property="dynaForm(status)" maxlength="40" />
							</div>
						</div>
						<br />
						<br />
						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>Bill Wise Amount: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="billAmount" styleClass="form-control"   value="${datamap.bill_amount}"
									property="dynaForm(billAmount)" maxlength="40" />
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>E-File Computer No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="fileComNo" styleClass="form-control"  value="${datamap.efile_com_no}"
									property="dynaForm(fileComNo)" maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Remarks: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="remarks" styleClass="form-control"  value="${datamap.bill_remarks}"
									property="dynaForm(remarks)" maxlength="150" />
							</div>
						</div>

						<br />
						<br />


					</div>
				</logic:iterate>
				
				<div class="row" style="text-align: center">
				<div class="col-md-12 col-xs-12">
					<input type="submit" name="submit" value="Submit"
						class="btn btn-success"
						onclick="return fnSubmitCategory('${datamap.cino}');" />
				</div>
			</div>
			</logic:present>
			
		</div>
	</html:form>
</div>

<script type="text/javascript">
	function fnSubmitCategory(id) {
		//alert((id));
		
		
		if ($("#fin_category").val() == null
					|| $("#fin_category").val() == "" || $("#fin_category").val() == 0) {
				alert("FIN Category Required");
				$("#fin_category").focus();
				return false;
			}
			
			if ($("#nameOfwork").val() == null
					|| $("#nameOfwork").val() == "") {
				alert("Name Of work Required");
				$("#nameOfwork").focus();
				return false;
			}
			
			if ($("#estCost").val() == null
					|| $("#estCost").val() == "") {
				alert("Estimated Cost Required");
				$("#estCost").focus();
				return false;
			}
			
			if ($("#adminSanction").val() == null
					|| $("#adminSanction").val() == "") {
				alert("Administrative Sanction Required");
				$("#adminSanction").focus();
				return false;
			}
			
			if ($("#grant").val() == null
					|| $("#grant").val() == "") {
				alert("Grant Required");
				$("#grant").focus();
				return false;
			}
			
			if ($("#cfmsBill").val() == null
					|| $("#cfmsBill").val() == "") {
				alert("CFMS Bill Required");
				$("#cfmsBill").focus();
				return false;
			}
			if ($("#status").val() == null
					|| $("#status").val() == "") {
				alert("Bill Status Required");
				$("#status").focus();
				return false;
			}
			if ($("#billAmount").val() == null
					|| $("#billAmount").val() == "") {
				alert("Bill Amount Required");
				$("#billAmount").focus();
				return false;
			}
			if ($("#fileComNo").val() == null
					|| $("#fileComNo").val() == "") {
				alert("E-File Computer No Required");
				$("#fileComNo").focus();
				return false;
			}
			
			if ($("#remarks").val() == null
					|| $("#remarks").val() == "") {
				alert("Remarks Required");
				$("#remarks").focus();
				return false;
			}
			
		$("#cino").val(id);
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	}
</script>
