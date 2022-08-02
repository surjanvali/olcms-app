<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<logic:present name="CAUSELISTCASES">
		<div class="table-responsive">
			<table id="example" class="table table-striped table-bordered"
				style="width:100%">
				<thead>
					<tr>
						<th>Sl.No</th>
						<th>District</th>
						<th>Orders Issued</th>
						<th>Orders Implemented</th>
						<th>Appeal Filed</th>
						<th>Pending</th>
						<th>Aciton Taken</th>
					</tr>
				</thead>
				<tbody>

					<logic:iterate id="map" name="CAUSELISTCASES" indexId="i">
						<tr>
							<td>${i+1 }.</td>
							<td>${map.district_name}</td>
							<td>${map.casescount}</td>
							<td>${map.order_implemented }</td>
							<td>${map.appeal_filed}</td>
							<td>${map.pending }</td>
							<td>${map.actoin_taken_percent }</td>
						</tr>
					</logic:iterate>
				</tbody>
				<tfoot>
					<tR>
						<td colspan="7">&nbsp;</td>
					</tR>
				</tfoot>
			</table>
		</div>
	</logic:present>
</body>
</html>