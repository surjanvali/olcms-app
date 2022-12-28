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
	<html:form action="/FinalOrderCountReport">
		<html:hidden styleId="mode" property="mode" />

				<logic:present name="CASEWISEDATA">
						<div class="ibox">
					<div class="ibox-body">
						<div class="table-responsive">

							<table id="example"
								class="table table-striped table-bordered oldTypediv"
								style="width:100%"><thead>
									<tr class="row1" >
										<th colspan="6">${HEADING}</th>
									</tr>
									<tr class="row1" >
										<th class="cell" >Sl No.</th>
										<th class="cell" >Cino</th>
										<th class="cell" >Department Code</th>
										<th class="cell" >Department Name</th>
										<th class="cell" >Order Document</th>
										<th class="cell" >Mobile</th>
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="ccd" name="CASEWISEDATA" indexId="i">
										<tr class="row1" >

											<td class="cell" >${i+1}</td>
											<td class="cell" >
												<%-- <input type="button" id="btnShowPopup"
													 value="${ccd.cino}"
													class="btn btn-sm btn-info waves-effect waves-light"
													onclick="javascript:viewCaseDetailsPopup('${ccd.cino}');" /> --%>
													
													<a id="btnShowPopup" target="_blank" class="btn btn-sm btn-info waves-effect waves-light"
													onclick="javascript:viewCaseDetailsPopup('${ccd.cino}');" /><font color="white">${ccd.cino}</font></a>
												
												
											 <%--  <button id="btnShowPopup"
													
													class="btn btn-sm btn-info waves-effect waves-light"
													onclick="javascript: return viewCaseDetailsPopup('${ccd.cino}');">${ccd.cino}</button> --%>
											</td>
											<td class="cell" >${ccd.dept_code}</td>
											<td class="cell" >${ccd.dept_name}</td>
											<td class="cell" ><a href="<%=basePath%>${ccd.order_document_path}" target="_blank">View</a></td>
											<td class="cell" >${ccd.mobile_no}</td>
										</tr>
									</logic:iterate>
								</tbody>
							</table>
						</div></div></div>
				</logic:present>
		
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
						style="width:100%;min-height:600px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

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
		}
	}
	
	
	
</script>
<!-- 
function print() {
		document.forms[0].mode.value = "printView";
		document.forms[0].submit();
	}
 -->