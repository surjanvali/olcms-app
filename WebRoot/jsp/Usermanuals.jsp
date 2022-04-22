<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="col-sm-12">
	<div class="card-box" style="margin-top: 50px;">
		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="dashboard-cat-title">
					<logic:equal value="USERMANUAL" name='VIEWTYPE'>
						<div class="row">
							<div class="col-xl-12 col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<h3>User Manual</h3>
							</div>
						</div>
						<hr>
						<table class="table table-bordered table_style">
							<thead>
								<tr>
									<th>Sl.No.</th>
									<th>Name</th>
									<th>Download</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1.</td>
									<td>Restructuring of Districts in Andhra Pradesh Portal on Fixed Assets</td>
									<td style="text-align: center;">
										<a href="uploads/AP-DRPuserManualv2.pdf" target="_blank"><i class="glyphicon glyphicon-download-alt" aria-hidden="true"></i></a>
									</td>
								</tr>
							</tbody>
						</table>
					</logic:equal>
					<logic:equal value="HELPLINE" name='VIEWTYPE'>
						<div class="row">
							<div class="col-xl-12 col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<h3>Helpline</h3>
							</div>
						</div>
						<hr>
						<table class="table table-bordered table_style">
							<thead>
								<tr>
									<th>Sl.No.</th>
									<th>Name</th>
									<th>Mobile</th>
									<th>District</th>
									<th>EMail ID</th>
								</tr>
							</thead>
							<tbody>
								<logic:notEmpty name="helpline">
									<logic:iterate id="map" name="helpline" indexId="i">
										<tr>
											<td>
												${i+1 }
											</td>
											<td>
												${map.name }
											</td>
											<td>
												${map.mobile }
											</td>
											<td>
												${map.district }
											</td>
											<td>
												${map.email}
											</td>
										</tr>
									</logic:iterate>
								</logic:notEmpty>
							</tbody>
						</table>
					</logic:equal>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		if ($("table"))
			$("table").DataTable({
				"lengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"pageLength" : 25
			});
	</script>
</div>