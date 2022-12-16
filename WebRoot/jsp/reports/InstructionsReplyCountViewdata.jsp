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
.row {
 border :1px solid black;
}
.pull-rightt{
border:1px 1px 1px 1px solid black;
}
.row > div > div{
  background: lightgrey;
  border: 1px solid grey;
}
.cell{
 border: 1px 1px 1px 1px;
}
</style>

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									<font style="color:green">${HEADING}</font>
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/InstructionsreplyCountReport">
		<html:hidden styleId="mode" property="mode" />
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
		

		<div class="ibox">
			<%-- <div class="ibox-head">
				<div class="ibox-title">
					<h5 style="color:Black;">
						<logic:notEmpty name="cino">
									${cino}
								</logic:notEmpty>
					</h5>
				</div>
			</div> --%>
			<div class="ibox-body table" style="border: 2px solid black;">
				

				<logic:present name="CASEWISEDATA">
						<div class="col-md-12">
							<table cellpadding="0" cellspacing="0" class="table" style="width: 100% !important;border-collapse: collapse;" border="1">
								<thead>
									<tr class="row1" >
										<th colspan="7"><font style="color:#3d80eb">${HEADING}</font></th>
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
						</div>
				</logic:present>

				<div class="text-right">
					<button class="btn btn-info" type="button"
						onclick="javascript:window.print();">
						<i class="fa fa-print"></i> Print
					</button>
				</div>
			</div>
		</div>
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