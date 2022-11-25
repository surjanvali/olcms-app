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
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"51445",secure:"51454"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script>
</head>
<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-562"
	data-genuitec-path="/apolcms/WebRoot/jsp/reports/CauseListCases.jsp">
	<logic:present name="CAUSELISTCASES">
		<div class="table-responsive">
			<table id="example" class="table table-striped table-bordered"
				style="width:100%">
				<%-- <thead>
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
				</tbody> --%>
				<thead>
					<tr>
						<th>Sl.No</th>
						<th>Causelist Date</th>
						<th>CINo</th>
						<th>Respondent</th>
						<th>Date of Filing</th>
						<th>Case Reg. No.</th>
						<th>Bench Id</th>
						<th>Judge Name</th>
						<th>Petitioner</th>
						<th>Department</th>
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
							<td>${map.res_name}</td>
							<td><logic:notEmpty name="map" property="date_of_filing">
									<logic:notEqual value="0001-01-01" name="map"
										property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
								</logic:notEmpty></td>
							<td>${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
							<td>${map.bench_id}</td>
							<td>${map.coram}</td>
							<td>${map.pet_name }</td>
							<td>${map.description}</td>

							<td>${map.pet_adv }</td>
							<td>${map.res_adv }</td>
						</tr>
					</logic:iterate>
				</tbody>
				<tfoot>
					<tR>
						<td colspan="12">&nbsp;</td>
					</tR>
				</tfoot>
			</table>
		</div>
	</logic:present>
</body>
</html>