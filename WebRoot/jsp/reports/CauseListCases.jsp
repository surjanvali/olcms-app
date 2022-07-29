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
						<th>Causelist Date</th>
						<th>CINo</th>
						<th>Date of Filing</th>
						<th>Case Reg No.</th>
						<th>Petitioner</th>
						<th>Respondents</th>
						<th>Petitioner Advocate</th>
						<th>Respondent Advocate</th>
					</tr>
				</thead>
				<tbody>

					<logic:iterate id="map" name="CAUSELISTCASES" indexId="i">
						<tr>
							<td>${i+1 }.</td>
							<td>${map.causelist_date}</td>
							<td>${map.cino}</td>
							<td><logic:notEmpty name="map" property="date_of_filing">
									<logic:notEqual value="0001-01-01" name="map"
										property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
								</logic:notEmpty></td>


							<td>${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>

							<td>${map.pet_name }</td>
							<td>${map.res_name },${map.address}</td>

							<td>${map.pet_adv }</td>
							<td>${map.res_adv }</td> </tr>
					</logic:iterate>
				</tbody>
				<tfoot>
					<tR>
						<td colspan="9">&nbsp;</td>
					</tR>
				</tfoot>
			</table>
		</div>
	</logic:present>
</body>
</html>