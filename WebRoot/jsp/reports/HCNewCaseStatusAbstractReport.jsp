<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!-- <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'> -->
<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>	

<link href="./assetsnew/vendors/select2/dist/css/select2.min.css" rel="stylesheet" />
<!-- <link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet" type="text/css" /> -->

<!-- PLUGINS STYLES-->
<!-- <link href="./assetsnew/vendors/DataTables/datatables.min.css" rel="stylesheet" /> -->
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" /> 

<div class="page-content fade-in-up">
	<html:form action="/HCNewCaseStatusAbstractReport"
		styleId="HCNewCaseStatusAbstract">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(deptName)" styleId="deptName" />
		<html:hidden property="dynaForm(caseStatus)" styleId="caseStatus" />
		<html:hidden property="dynaForm(reportLevel)" styleId="reportLevel" />
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

		<logic:notPresent name="SHOWFILTERS">
			<html:hidden styleId="deptId" property="dynaForm(deptId)" />
		</logic:notPresent>

		<logic:present name="SHOWFILTERS">
			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">High Court New Cases Abstract Report</div>
				</div>
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Type</label>
								<html:select styleId="caseTypeId"
									property="dynaForm(caseTypeId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(caseTypesList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(caseTypesList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Department</label>
								<html:select styleId="deptId" property="dynaForm(deptId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(deptList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(deptList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>

						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>District</label>
								<html:select styleId="districtId"
									property="dynaForm(districtId)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm" property="dynaForm(distList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(distList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Case Registration Year</label>
								<html:select styleId="regYear" property="dynaForm(regYear)"
									styleClass="form-control select2Class">
									<html:option value="0">---ALL---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(yearsList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(yearsList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Registration (From
									Date)</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofFromDate"
										property="dynaForm(dofFromDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="font-bold">Date of Registration (To Date)</label>
								<div class="input-group date">
									<span class="input-group-addon bg-white"><i
										class="fa fa-calendar"></i></span>
									<html:text styleId="dofToDate" property="dynaForm(dofToDate)"
										styleClass="form-control datepicker" />

								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Petitioner Name</label>
								<html:text styleId="petitionerName"
									property="dynaForm(petitionerName)" styleClass="form-control" />

							</div>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="form-group">
								<label>Respondent Name</label>
								<html:text styleId="respodentName"
									property="dynaForm(respodentName)" styleClass="form-control" />

							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="button" name="getreport" value="Get Report"
								class="btn btn-success" onclick="return fnShowCases();" />
						</div>
					</div>
				</div>
			</div>

		</logic:present>

		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b><logic:notEmpty name="HEADING"> ${HEADING } (New Cases) </logic:notEmpty> </b>
					</h4>
				</div>
			</div>
			<div class="ibox-body">
				<div class="table-responsive">
					<logic:present name="secdeptwisenewcases">
						<table class="table table-striped table-bordered table-hover"
							id="example2" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Sect. Department Code</th>
									<th>Sect. Department Name</th>
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
									<th>GoI</th>
									<th>PSU</th>
									<th>Private</th>
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
								<bean:define id="goiTotals" value="0"></bean:define>
								<bean:define id="psuTotals" value="0"></bean:define>
								<bean:define id="privateTotals" value="0"></bean:define>

								<bean:define id="hodSecTotals" value="0"></bean:define>
								<bean:define id="dcTotals" value="0"></bean:define>
								<bean:define id="disthodTotals" value="0"></bean:define>
								<bean:define id="disthodSecTotals" value="0"></bean:define>


								<logic:iterate id="map" name="secdeptwisenewcases" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td><a
											href="javascript:ShowHODWiseNew('${map.deptcode}','${map.description }');">${map.description }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','ALL','SD');">${map.total_cases }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withSD','SD');">${map.withsectdept }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withMLO','SD');">${map.withmlo }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withHOD','SD');">${map.withhod }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withNO','SD');">${map.withnodal }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withSDSec','SD');">${map.withsection }</a></td>

										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withHODSec','SD');">${map.withsectionhod }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDC','SD');">${map.withdc }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDistNO','SD');">${map.withdistno }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDistSec','SD');">${map.withsectiondist }</a></td>

										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withGP','SD');">${map.withgpo }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','closed','SD');">${map.closedcases }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','goi','SD');">${map.goi }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','psu','SD');">${map.psu }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','Private','SD');">${map.privatetot }</a></td>

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
										<bean:define id="goiTotals"
										value="${goiTotals + map.goi }"></bean:define>
										<bean:define id="psuTotals"
										value="${psuTotals + map.psu }"></bean:define>
										<bean:define id="privateTotals"
										value="${privateTotals + map.privatetot }"></bean:define>

									<bean:define id="hodSecTotals"
										value="${hodSecTotals + map.withsectionhod }"></bean:define>
									<bean:define id="dcTotals" value="${dcTotals + map.withdc }"></bean:define>
									<bean:define id="disthodTotals"
										value="${disthodTotals + map.withdistno }"></bean:define>
									<bean:define id="disthodSecTotals"
										value="${disthodSecTotals + map.withsectiondist }"></bean:define>



								</logic:iterate>
							</tbody>

							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
									<td colspan="1" style="text-align: right;">${sdeptTotals }</td>
									<td colspan="1" style="text-align: right;">${Mlototals }</td>
									<td colspan="1" style="text-align: right;">${hodsTotals }</td>
									<td colspan="1" style="text-align: right;">${nodalTotals }</td>
									<td colspan="1" style="text-align: right;">${sectionTotals }</td>

									<td colspan="1" style="text-align: right;">${hodSecTotals }</td>
									<td colspan="1" style="text-align: right;">${dcTotals }</td>
									<td colspan="1" style="text-align: right;">${disthodTotals }</td>
									<td colspan="1" style="text-align: right;">${disthodSecTotals }</td>

									<td colspan="1" style="text-align: right;">${gpoTotals }</td>
									<td colspan="1" style="text-align: right;">${closedcases }</td>
									<td colspan="1" style="text-align: right;">${goiTotals }</td>
									<td colspan="1" style="text-align: right;">${psuTotals }</td>
									<td colspan="1" style="text-align: right;">${privateTotals }</td>
									
								</tR>
							</tfoot>

						</table>

					</logic:present>
					<logic:present name="deptwisenewcases">

						<table class="table table-striped table-bordered table-hover"
							id="example2" cellspacing="0" width="100%">
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
									<th>GoI</th>
									<th>PSU</th>
									<th>Private</th>
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
								<bean:define id="goiTotals" value="0"></bean:define>
								<bean:define id="psuTotals" value="0"></bean:define>
								<bean:define id="privateTotals" value="0"></bean:define>

								<bean:define id="hodSecTotals" value="0"></bean:define>
								<bean:define id="dcTotals" value="0"></bean:define>
								<bean:define id="disthodTotals" value="0"></bean:define>
								<bean:define id="disthodSecTotals" value="0"></bean:define>


								<logic:iterate id="map" name="deptwisenewcases" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.deptcode }</td>
										<td>${map.description }</td>
										<%-- <td  style="text-align: right;">${map.total_cases }</td>
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
										<td  style="text-align: right;">${map.closedcases }</td> --%>

										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','ALL','HOD');">${map.total_cases }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withSD','HOD');">${map.withsectdept }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withMLO','HOD');">${map.withmlo }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withHOD','HOD');">${map.withhod }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withNO','HOD');">${map.withnodal }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withSDSec','HOD');">${map.withsection }</a></td>

										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withHODSec','HOD');">${map.withsectionhod }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDC','HOD');">${map.withdc }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDistNO','HOD');">${map.withdistno }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withDistSec','HOD');">${map.withsectiondist }</a></td>

										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','withGP','HOD');">${map.withgpo }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','closed','HOD');">${map.closedcases }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','goi','HOD');">${map.goi }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','psu','HOD');">${map.psu }</a></td>
										<td style="text-align: right;"><a
											href="javascript:showCasesWiseNew('${map.deptcode}','${map.description }','Private','HOD');">${map.privatetot }</a></td>

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
										<bean:define id="goiTotals"
										value="${goiTotals + map.goi }"></bean:define>
										<bean:define id="psuTotals"
										value="${psuTotals + map.psu }"></bean:define>
										<bean:define id="privateTotals"
										value="${privateTotals + map.privatetot }"></bean:define>

									<bean:define id="hodSecTotals"
										value="${hodSecTotals + map.withsectionhod }"></bean:define>
									<bean:define id="dcTotals" value="${dcTotals + map.withdc }"></bean:define>
									<bean:define id="disthodTotals"
										value="${disthodTotals + map.withdistno }"></bean:define>
									<bean:define id="disthodSecTotals"
										value="${disthodSecTotals + map.withsectiondist }"></bean:define>



								</logic:iterate>
							</tbody>

							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
									<td colspan="1" style="text-align: right;">${sdeptTotals }</td>
									<td colspan="1" style="text-align: right;">${Mlototals }</td>
									<td colspan="1" style="text-align: right;">${hodsTotals }</td>
									<td colspan="1" style="text-align: right;">${nodalTotals }</td>
									<td colspan="1" style="text-align: right;">${sectionTotals }</td>

									<td colspan="1" style="text-align: right;">${hodSecTotals }</td>
									<td colspan="1" style="text-align: right;">${dcTotals }</td>
									<td colspan="1" style="text-align: right;">${disthodTotals }</td>
									<td colspan="1" style="text-align: right;">${disthodSecTotals }</td>

									<td colspan="1" style="text-align: right;">${gpoTotals }</td>
									<td colspan="1" style="text-align: right;">${closedcases }</td>
									<td colspan="1" style="text-align: right;">${goiTotals }</td>
									<td colspan="1" style="text-align: right;">${psuTotals }</td>
									<td colspan="1" style="text-align: right;">${privateTotals }</td>
								</tR>
							</tfoot>

						</table>


					</logic:present>

					<logic:present name="CASESLIST">

						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Ack No</th>
									<!-- <th>Ack No</th> -->
									<!-- <th>Scanned Affidavit</th> -->
									<th>Case Type</th>
									<!-- <th>Reg.No.</th>
									<th>Reg. Year</th> -->
									<th>Main Case No</th>
									<th>Case Category</th>
									<th>Petitioner Advocate</th>
									<th>Respondent Advocate</th>
									<th>Respondent Advocate No</th>
									<th>District</th>
									<th>Download/Print</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="CASESLIST" indexId="i">
									<tr>
										<td>${i+1 }.</td>
										<td><input type="button" id="btnShowPopup"
											value="${map.ack_no}"
											class="btn btn-sm btn-info waves-effect waves-light"
											onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" />
											</td>
											<%-- <td>${map.ack_no }<logic:notEqual value="-" name="map"
													property="hc_ack_no">

													<span style="color: navy;font-weight: bold;">${map.hc_ack_no }</span>

												</logic:notEqual>
											</td> --%>
										
										<%-- <td><logic:notEmpty name="map"
												property="scanned_document_path1">
												<logic:notEqual value="-" name="map"
													property="scanned_document_path1">
													<a href="./${map.scanned_document_path1}" target="_new"
														class="btn btn-sm btn-info"><i
														class="glyphicon glyphicon-save"></i><span>Scanned
															Affidavit</span></a>
												</logic:notEqual>
											</logic:notEmpty></td> --%>
											
										<td>${map.case_short_name }</td>
										<%-- <td>${map.reg_no}</td>
										<td>${map.reg_year}</td> --%>
										<td>${map.maincaseno}</td>
										<td>${map.case_category }</td>
										<td>${map.petitioner_name}</td>
										<td>${map.advocatename}</td>
										<td>${map.advocateccno}</td>
										<td>${map.district_name}</td>
										<td style="text-align: center;" nowrap="nowrap"><logic:present
													name="map" property="ack_file_path">
													<a href="./${map.ack_file_path}" target="_new"
														title="Print Acknowledgement" class="btn btn-sm btn-info">
														<i class="fa fa-save"></i> <span>Acknowledgement</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <logic:present name="map" property="barcode_file_path">
													<a href="./${map.barcode_file_path}" target="_new"
														title="Print Barcode" class="btn btn-sm btn-info"> <i
														class="fa fa-save"></i> <span>Barcode</span> <!-- <span>Download</span> -->
													</a>
												</logic:present> <!-- //id="btnShowPopup"  --> <%-- <input type="button"
												id="btnShowPopup" value="Scanned Affidavit"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" /> --%>

												<logic:present name="map" property="ack_no">


													<logic:notEqual value="-" name="map" property="hc_ack_no">
														<a
															href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
															target="_new" title="Print Barcode"
															class="btn btn-sm btn-info">
													</logic:notEqual>


													<logic:equal value="-" name="map" property="hc_ack_no">
														<a
															href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
															target="_new" title="Print Barcode"
															class="btn btn-sm btn-info">
													</logic:equal>

													<i class="fa fa-save"></i>
													<span>Scanned Affidavit</span>
													</a>
												</logic:present></td>
										
									</tr>

								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="20">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:present>

					<logic:present name="DISPWISE">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Disposal Type</th>
									<th>Cases Count</th>

								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<logic:iterate id="map" name="DISPWISE" indexId="i">
									<tr>
										<td>${i+1 }.</td>

										<td><a
											href="./HCNewCaseStatusAbstractReport.do?mode=getCasesDeptWiseList&src=dashBoard&caseCategory=${map.disposal_type}">${map.disposal_type }</a></td>
										<td style="text-align: right;">${map.casescount}</td>
										<bean:define id="Totals" value="${Totals + map.casescount }"></bean:define>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:present>
					
					<logic:present name="DIPTWISECASES">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<!-- <th>Disposal Type</th> -->
									<th>Department</th>
									<th>Cases Count</th>

								</tr>
							</thead>
							<tbody>
								<bean:define id="Totals" value="0"></bean:define>
								<logic:iterate id="map" name="DIPTWISECASES" indexId="i">
									<tr>
										<td>${i+1 }.</td>

										<%-- <td>${map.disposal_type}</td> --%>
										<td><a
											href="./HCNewCaseStatusAbstractReport.do?mode=getCasesList&src=dashBoard&caseCategory=${map.disposal_type}&deptType=${map.description}">${map.description }</a></td>
										<td style="text-align: right;">${map.casescount}</td>
										<bean:define id="Totals" value="${Totals + map.casescount }"></bean:define>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${Totals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:present>

				</div>
			</div>
		</div>
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
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>
<script src="./assetsnew/vendors/select2/dist/js/select2.full.min.js" type="text/javascript"></script>
	
<script type="text/javascript">
	$(document).ready(function() {
		$(".select2Class").select2();
		$('.input-group.date').datepicker({
			format : "dd-mm-yyyy"
		});
	});

	function fnShowCases() {
		$("#mode").val("unspecified");
		$("#HCNewCaseStatusAbstract").submit();
	}

	function ShowHODWiseNew(deptId, deptDesc) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#mode").val("HODwisedetails");
		$("#HCNewCaseStatusAbstract").submit();
	}
	function showCasesWiseNew(deptId, deptDesc, status,level) {
		$("#deptId").val(deptId);
		$("#deptName").val(deptDesc);
		$("#caseStatus").val(status);
		$("#reportLevel").val(level);
		$("#mode").val("getCasesList");
		$("#HCNewCaseStatusAbstract").submit();
	}

	function viewCaseDetailsPopup(ack_no) {
		//alert("hai....");
		var heading = "View Case Details for ACK NO : " + ack_no;
		var srclink = "";
		if (ack_no != null && ack_no != "" && ack_no != "0") {
			srclink = "./HCNewCaseStatusAbstractReport.do?mode=getAckNo&SHOWPOPUP=SHOWPOPUP&cino="
					+ ack_no;
			// alert("LINK:"+srclink);
			if (srclink != "") {
				$("#MyPopup .modal-title").html(heading);
				$("#page").prop("src", srclink)
				//$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}
			;
		}
		;
	};
</script>
