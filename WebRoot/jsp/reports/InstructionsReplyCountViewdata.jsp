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

</style>

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<%-- <h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									<font style="color:green">${HEADING}</font>
								</logic:notEmpty>
	</h3> --%>

</div>
<div class="page-content fade-in-up">
	<html:form action="/InstructionsreplyCountReport">
		<html:hidden styleId="mode" property="mode" />

				<logic:present name="CASEWISEDATA">
						<div class="ibox">
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example"
								class="table table-striped table-bordered oldTypediv"
								style="width:100%"><thead>
									<tr class="row1" >
										<th colspan="7">${HEADING}</th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Cino</th>
										<th class="cell" >Department Code</th>
										<th class="cell" >Department Name</th>
										<th class="cell" >Instruction</th>
										<th class="cell" >Reply Instruction</th>
										<th class="cell" >Mobile</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="ccd" name="CASEWISEDATA" indexId="i">
										<tr class="row1" >

											<td class="cell" >${i+1}</td>
											<td class="cell" >${ccd.cino}</td>
											<td class="cell" >${ccd.dept_code}</td>
											<td class="cell" >${ccd.dept_name}</td>
											<td class="cell" >${ccd.instructions}</td>
											<td class="cell" >${ccd.reply_instructions}</td>
											<td class="cell" >${ccd.mobile_no}</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div></div></div>
				</logic:present>
		
	</html:form>
</div>
<!-- 
<script type="text/javascript">
	function print() {
		document.forms[0].mode.value = "printView";
		document.forms[0].submit();
	}
</script>
 -->