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
	<html:form action="/DailyStatusEntry">
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
					<h4 class="m-t-0 header-title">
						<b><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty> </b>
					</h4>
				</div>
			</div>
			<logic:present name="existData">
			
			<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Daily Status</th>
										<th>Entry Time</th>
										</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="existData" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.daily_status }</td>
											<td>${map.insert_time}</td>
											</tr>
											</logic:iterate></tbody></table>
			</logic:present>
			
			<logic:present name="USERSLIST">
				<logic:iterate id="datamap" name="USERSLIST">
				<html:hidden styleId="cino" property="dynaForm(cino)"
						value="${datamap.cino}" />
			<div class="ibox-body">
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Daily Status: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
						
							<html:textarea styleId="daily_status"
								property="dynaForm(daily_status)" styleClass="form-control"
								cols="50" rows="5">

							</html:textarea>
					</div>

					<%-- <div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Upload file: </b>
					</div>



					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
						<html:file property="dynaForm(uploadfile)" styleId="uploadfile"
							styleClass="form-control"></html:file>
					</div> --%>
				</div>

			</div>
		
</logic:iterate>
		<div class="row" style="text-align: center">
			<div class="col-md-12 col-xs-12">
				<input type="submit" name="submit" value="Submit"
					class="btn btn-success" onclick="return fnSubmitCategory('${datamap.cino}');" />
			</div>
		</div>

</logic:present></div>
</html:form>
</div>

<script type="text/javascript">
	function fnSubmitCategory() {
		//alert((""+$("#filesUploadPath").val("")));
		
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	}
</script>
