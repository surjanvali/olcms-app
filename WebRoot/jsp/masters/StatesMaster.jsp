<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- START PAGE CONTENT-->

<div class="page-content fade-in-up">
	<html:form action="/StatesMaster">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(roleId)" styleId="roleId" />
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
					<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
				</div>
			</div>
			<div class="ibox-body">
				<logic:notEmpty name="STATESLIST">
					<table class="table table-striped table-bordered table-hover"
						id="example" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Sl.No</th>
								<th>State Code</th>
								<th>State Name</th>
								<th>Districts</th>
								<!-- <th>Benches</th> -->
							</tr>
						</thead>
						<tbody>
							<bean:define id="distTotals" value="0"></bean:define>
							<%-- <bean:define id="benchTotals" value="0"></bean:define> --%>
							<logic:iterate id="map" name="STATESLIST" indexId="i">
								<tr>
									<td>${i+1 }</td>
									<td>${map.state_code}</td>
									<td>${map.state_name }</td>
									<td style="text-align: right;"><a
										href="./StatesMaster.do?mode=showDistricts&stateId=${map.state_code}&stateName=${map.state_name}">${map.districts }</a></td>
									<%-- <td><a
										href="./StatesMaster.do?mode=showBenches&stateId=${map.state_code}&stateName=${map.state_name}">${map.benches }</a></td> --%>
								</tr>
								<bean:define id="distTotals"
									value="${distTotals + map.districts }"></bean:define>
								<%-- <bean:define id="benchTotals"
									value="${benchTotals + map.benches }"></bean:define> --%>
							</logic:iterate>
						</tbody>
						<tfoot>
							<tR>
								<td colspan="3">Totals</td>
								<td colspan="1" style="text-align: right;">${distTotals }</td>
								<%-- <td colspan="1">${benchTotals }</td> --%>
							</tR>
						</tfoot>
					</table>
				</logic:notEmpty>


				<logic:notEmpty name="DISTLIST">
					<table class="table table-striped table-bordered table-hover"
						id="example" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Sl.No</th>
								<th>State Name</th>
								<th>District Code</th>
								<th>District Name</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="map" name="DISTLIST" indexId="i">
								<tr>
									<td>${i+1 }</td>
									<td>${map.state_name }</td>
									<td>${map.dist_code }</td>
									<td>${map.dist_name }</td>
								</tr>
							</logic:iterate>
						</tbody>
						<tfoot>
							<tR>
								<td colspan="4">&nbsp;</td>
							</tR>
						</tfoot>
					</table>
				</logic:notEmpty>


				<logic:notEmpty name="BENCHLIST">
					<table class="table table-striped table-bordered table-hover"
						id="example" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Sl.No</th>
								<th>State Name</th>
								<th>EST Code</th>
								<th>Bench Id</th>
								<th>Bench Name</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="map" name="BENCHLIST" indexId="i">
								<tr>
									<td>${i+1 }</td>
									<td>${map.state_name }</td>
									<td>${map.est_code}</td>
									<td>${map.bench_id }</td>
									<td>${map.bench_name }</td>
								</tr>

							</logic:iterate>
						</tbody>
						<tfoot>
							<tR>
								<td colspan="5">&nbsp;</td>
							</tR>
						</tfoot>
					</table>
				</logic:notEmpty>

			</div>
		</div>
	</html:form>
</div>

<script type="text/javascript">
	function backFn() {
		document.forms[0].mode.value = "unspecified";
		document.forms[0].submit();
	}
</script>