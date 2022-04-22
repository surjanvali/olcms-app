<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="page-content fade-in-up">
	<html:form action="/HCCaseStatusAbstractReport"
		styleId="HCCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptId)" styleId="deptId" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
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
			<div class="ibox-body">
				<div class="table-responsive">
					<logic:present name="secdeptwise">
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Sect.Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									
									<th>Pending With Sect.Dept</th>
									<th>Pending With MLO</th>
									<th>Pending With HOD</th>
									<th>Pending With Nodal</th>
									<th>Pending With Section(Sect. Dept.)</th>
									<th>Pending With Section(HOD)</th>
									
									<th>Pending With District Collector</th>
									<th>Pending With District Nodal Officer</th>
									<th>Pending With Section(District)</th>
									
									<th>Pending With GPO</th>
									<th>Closed Cases</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<bean:define id="sdeptTotals" value="0"></bean:define>
								<bean:define id="Mlototals" value="0"></bean:define>
								<bean:define id="hodsTotals" value="0"></bean:define>
								<bean:define id="nodalTotals" value="0"></bean:define>
								<bean:define id="sectionTotals" value="0"></bean:define>
								<bean:define id="gpoTotals" value="0"></bean:define>
								<bean:define id="closedcases" value="0"></bean:define>
								
								<bean:define id="hodSecTotals" value="0"></bean:define>
								<bean:define id="dcTotals" value="0"></bean:define>
								<bean:define id="disthodTotals" value="0"></bean:define>
								<bean:define id="disthodSecTotals" value="0"></bean:define>
								
								
								<logic:iterate id="map" name="secdeptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td><a href="javascript:ShowHODWise('${map.deptcode}','${map.description }');">${map.description }</a></td>
										<td  style="text-align: right;">${map.total_cases }</td>
										<td  style="text-align: right;">${map.withsectdept }</td>
										<td  style="text-align: right;">${map.withmlo }</td>
										<td  style="text-align: right;">${map.withhod }</td>
										<td  style="text-align: right;">${map.withnodal }</td>
										<td  style="text-align: right;">${map.withsection }</td>
										
										<td  style="text-align: right;">${map.withsectionhod }</td>
										<td  style="text-align: right;">${map.withdc }</td>
										<td  style="text-align: right;">${map.withdistno }</td>
										<td  style="text-align: right;">${map.withsectiondist }</td>
										
										<td  style="text-align: right;">${map.withgpo }</td>
										<td  style="text-align: right;">${map.closedcases }</td>

									</tr>
									<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
									<bean:define id="sdeptTotals"
										value="${sdeptTotals + map.withsectdept }"></bean:define>
									<bean:define id="Mlototals" value="${Mlototals + map.withmlo }"></bean:define>
									<bean:define id="hodsTotals"
										value="${hodsTotals + map.withhod }"></bean:define>
									<bean:define id="nodalTotals"
										value="${nodalTotals + map.withnodal }"></bean:define>
									<bean:define id="sectionTotals"
										value="${sectionTotals + map.withsection }"></bean:define>
									<bean:define id="gpoTotals" value="${gpoTotals + map.withgpo }"></bean:define>

									<bean:define id="closedcases" value="${closedcases + map.closedcases }"></bean:define>
									
									<bean:define id="hodSecTotals" value="${hodSecTotals + map.withsectionhod }"></bean:define>
									<bean:define id="dcTotals" value="${dcTotals + map.withdc }"></bean:define>
									<bean:define id="disthodTotals" value="${disthodTotals + map.withdistno }"></bean:define>
									<bean:define id="disthodSecTotals" value="${disthodSecTotals + map.withsectiondist }"></bean:define>
										
										
										
								</logic:iterate>
							</tbody>

							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1"  style="text-align: right;">${Totals }</td>
									<td colspan="1"  style="text-align: right;">${sdeptTotals }</td>
									<td colspan="1"  style="text-align: right;">${Mlototals }</td>
									<td colspan="1"  style="text-align: right;">${hodsTotals }</td>
									<td colspan="1"  style="text-align: right;">${nodalTotals }</td>
									<td colspan="1"  style="text-align: right;">${sectionTotals }</td>
									
									<td colspan="1"  style="text-align: right;">${hodSecTotals }</td>
									<td colspan="1"  style="text-align: right;">${dcTotals }</td>
									<td colspan="1"  style="text-align: right;">${disthodTotals }</td>
									<td colspan="1"  style="text-align: right;">${disthodSecTotals }</td>
									
									<td colspan="1"  style="text-align: right;">${gpoTotals }</td>
									<td colspan="1"  style="text-align: right;">${closedcases }</td>
								</tR>
							</tfoot>

						</table>

					</logic:present>
					<logic:present name="deptwise">
						
						<table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									<th>Pending With Sect.Dept</th>
									<th>Pending With MLO</th>
									<th>Pending With HOD</th>
									<th>Pending With Nodal</th>
									<th>Pending With Section(Sect. Dept.)</th>
									<th>Pending With Section(HOD)</th>
									
									<th>Pending With District Collector</th>
									<th>Pending With District Nodal Officer</th>
									<th>Pending With Section(District)</th>
									
									<th>Pending With GPO</th>
									<th>Closed Cases</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<bean:define id="sdeptTotals" value="0"></bean:define>
								<bean:define id="Mlototals" value="0"></bean:define>
								<bean:define id="hodsTotals" value="0"></bean:define>
								<bean:define id="nodalTotals" value="0"></bean:define>
								<bean:define id="sectionTotals" value="0"></bean:define>
								<bean:define id="gpoTotals" value="0"></bean:define>
								<bean:define id="closedcases" value="0"></bean:define>
								
								<bean:define id="hodSecTotals" value="0"></bean:define>
								<bean:define id="dcTotals" value="0"></bean:define>
								<bean:define id="disthodTotals" value="0"></bean:define>
								<bean:define id="disthodSecTotals" value="0"></bean:define>
								
								
								<logic:iterate id="map" name="deptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td>${map.description }</td>
										<td  style="text-align: right;">${map.total_cases }</td>
										<td  style="text-align: right;">${map.withsectdept }</td>
										<td  style="text-align: right;">${map.withmlo }</td>
										<td  style="text-align: right;">${map.withhod }</td>
										<td  style="text-align: right;">${map.withnodal }</td>
										<td  style="text-align: right;">${map.withsection }</td>
										
										<td  style="text-align: right;">${map.withsectionhod }</td>
										<td  style="text-align: right;">${map.withdc }</td>
										<td  style="text-align: right;">${map.withdistno }</td>
										<td  style="text-align: right;">${map.withsectiondist }</td>
										
										<td  style="text-align: right;">${map.withgpo }</td>
										<td  style="text-align: right;">${map.closedcases }</td>

									</tr>
									<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
									<bean:define id="sdeptTotals"
										value="${sdeptTotals + map.withsectdept }"></bean:define>
									<bean:define id="Mlototals" value="${Mlototals + map.withmlo }"></bean:define>
									<bean:define id="hodsTotals"
										value="${hodsTotals + map.withhod }"></bean:define>
									<bean:define id="nodalTotals"
										value="${nodalTotals + map.withnodal }"></bean:define>
									<bean:define id="sectionTotals"
										value="${sectionTotals + map.withsection }"></bean:define>
									<bean:define id="gpoTotals" value="${gpoTotals + map.withgpo }"></bean:define>

									<bean:define id="closedcases" value="${closedcases + map.closedcases }"></bean:define>
									
									<bean:define id="hodSecTotals" value="${hodSecTotals + map.withsectionhod }"></bean:define>
									<bean:define id="dcTotals" value="${dcTotals + map.withdc }"></bean:define>
									<bean:define id="disthodTotals" value="${disthodTotals + map.withdistno }"></bean:define>
									<bean:define id="disthodSecTotals" value="${disthodSecTotals + map.withsectiondist }"></bean:define>
										
										
										
								</logic:iterate>
							</tbody>

							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1"  style="text-align: right;">${Totals }</td>
									<td colspan="1"  style="text-align: right;">${sdeptTotals }</td>
									<td colspan="1"  style="text-align: right;">${Mlototals }</td>
									<td colspan="1"  style="text-align: right;">${hodsTotals }</td>
									<td colspan="1"  style="text-align: right;">${nodalTotals }</td>
									<td colspan="1"  style="text-align: right;">${sectionTotals }</td>
									
									<td colspan="1"  style="text-align: right;">${hodSecTotals }</td>
									<td colspan="1"  style="text-align: right;">${dcTotals }</td>
									<td colspan="1"  style="text-align: right;">${disthodTotals }</td>
									<td colspan="1"  style="text-align: right;">${disthodSecTotals }</td>
									
									<td colspan="1"  style="text-align: right;">${gpoTotals }</td>
									<td colspan="1"  style="text-align: right;">${closedcases }</td>
								</tR>
							</tfoot>

						</table>
						
						<%-- <table class="table table-striped table-bordered table-hover"
							id="example" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Sect.Department Code</th>
									<th>Description</th>
									<th>Total Cases</th>
									<th>Pending With Sect.Dept</th>
									<th>Pending With MLO</th>
									<th>Pending With HOD</th>
									<th>Pending With Nodal</th>
									<th>Pending With Section</th>
									<th>Pending With GPO</th>
									<th>Closed Cases</th>

								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<bean:define id="sdeptTotals" value="0"></bean:define>
								<bean:define id="Mlototals" value="0"></bean:define>
								<bean:define id="hodsTotals" value="0"></bean:define>
								<bean:define id="nodalTotals" value="0"></bean:define>
								<bean:define id="sectionTotals" value="0"></bean:define>
								<bean:define id="gpoTotals" value="0"></bean:define>
								<bean:define id="closedcases" value="0"></bean:define>
								<logic:iterate id="map" name="deptwise" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.sdeptcode }${map.deptcode }</td>
										<td>${map.description }</td>
										<td  style="text-align: right;">${map.total_cases }</td>
										<td  style="text-align: right;">${map.withsectdept }</td>
										<td  style="text-align: right;">${map.withmlo }</td>
										<td  style="text-align: right;">${map.withhod }</td>
										<td  style="text-align: right;">${map.withnodal }</td>
										<td  style="text-align: right;">${map.withsection }</td>
										<td  style="text-align: right;">${map.withgpo }</td>
										<td  style="text-align: right;">${map.closedcases }</td>
									</tr>
									<bean:define id="Totals" value="${Totals + map.total_cases }"></bean:define>
									<bean:define id="sdeptTotals"
										value="${sdeptTotals + map.withsectdept }"></bean:define>
									<bean:define id="Mlototals" value="${Mlototals + map.withmlo }"></bean:define>
									<bean:define id="hodsTotals"
										value="${hodsTotals + map.withhod }"></bean:define>
									<bean:define id="nodalTotals"
										value="${nodalTotals + map.withnodal }"></bean:define>
									<bean:define id="sectionTotals"
										value="${sectionTotals + map.withsection }"></bean:define>
									<bean:define id="gpoTotals" value="${gpoTotals + map.withgpo }"></bean:define>

									<bean:define id="closedcases"
										value="${closedcases + map.closedcases }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1"  style="text-align: right;">${Totals }</td>
									<td colspan="1"  style="text-align: right;">${sdeptTotals }</td>
									<td colspan="1"  style="text-align: right;">${Mlototals }</td>
									<td colspan="1"  style="text-align: right;">${hodsTotals }</td>
									<td colspan="1"  style="text-align: right;">${nodalTotals }</td>
									<td colspan="1"  style="text-align: right;">${sectionTotals }</td>
									<td colspan="1"  style="text-align: right;">${gpoTotals }</td>
									<td colspan="1"  style="text-align: right;">${closedcases }</td>
								</tR>
							</tfoot>
						</table> --%>
					</logic:present>
				</div>
			</div>
		</div>
	</html:form>
</div>

<script type="text/javascript">
	function ShowHODWise(deptId, deptDesc) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCCaseStatusAbstract").submit();
	}
</script>

<!-- <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.print.min.js"></script>
 <script type="text/javascript" src="//cdn.datatables.net/1.10.15/js/dataTables.bootstrap4.min.js"></script> 
<script>
$('#example').DataTable({
  dom: 'Blfrtip',
  buttons: [ 'excel']
});
</script> -->