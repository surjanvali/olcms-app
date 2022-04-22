<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!-- START PAGE CONTENT-->

<div class="page-content fade-in-up">
	<html:form action="/CaseTypesMaster">
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
					<h4 class="m-t-0 header-title">
						<b><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty> </b>
					</h4>
				</div>
			</div>
			<logic:present name="CaseMaster">
				<div class="ibox-body">

					<table class="table table-striped table-bordered table-hover"
						id="example">
						<thead>
							<tr>
								<th>Sl.No</th>
								<th>CaseType ID</th>
								<th>Est Code</th>
								<th>Case Type Code</th>
								<th>Case Type Name</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="map" name="CaseMaster" indexId="i">
								<tr>
									<td>${i+1 }</td>
									<td>${map.case_type_id }</td>
									<td>${map.est_code }</td>
									<td>${map.case_type_code }</td>
									<td>${map.case_type_name }</td>
								</tr>

							</logic:iterate>

						</tbody>
						<tfoot>
							<tR>
								<td colspan="5">&nbsp;</td>

							</tR>
						</tfoot>
					</table>
				</div>
			</logic:present>
		</div>
	</html:form>
</div>