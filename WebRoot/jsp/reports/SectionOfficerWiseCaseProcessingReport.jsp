<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div class="page-content fade-in-up">
	<div class="ibox">
		<div class="ibox-head">
			<div class="ibox-title">Section Officer Wise Case Processing
				Report</div>
		</div>
		<div class="ibox-body">
			<html:form method="post"
				action="/SectionOfficerWiseCaseProcessingReport"
				styleId="CaseAssignmentStatusForm">
				<html:hidden property="dynaForm(email)" styleId="email" />
				<html:hidden property="dynaForm(section)" styleId="section" />
				<html:hidden styleId="mode" styleClass="form-control"
					property="mode" />
				<html:hidden styleId="cINO" property="dynaForm(cINO)" />

				<div class="container-fluid">
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
					<div class="table-responsive">
						<div class="col-sm-3 col-md-3 col-lg-3  col-xs-3" >
						      Legacy / New <br/><br/>
						</div>
   						<div class="col-sm-6 col-md-6 col-lg-6  col-xs-6" >
							<html:select styleId="section_code" property="dynaForm(section_code)" styleClass="select2Class"
								style="width: 100%;">
								<html:option value="0">---SELECT---</html:option>
								<html:option value="L">---Legacy---</html:option>
								<html:option value="N">---New---</html:option>
							</html:select>
						</div>
						<div class="col-sm-3 col-md-3 col-lg-3  col-xs-3" >
							<br/>
						     
						     <input type="button" name="Submit" value="Get Details" class="btn btn-success pull-right" onclick="getdata();" />
						     <br/>
						     <br/>
						</div>
						<br/>
						     <br/>
					</div>
					<div class="table-responsive">
						<logic:notEmpty name="CASESLIST">
							<div class="table-responsive">
								<table id="example" class="table table-striped table-bordered"
									style="width:100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Section Officer Details</th>
											<th>count</th>
										</tr>
									</thead>
									<tbody>

										<logic:iterate id="map" name="CASESLIST" indexId="i">
											<tr>
												<td>${i+1 }</td>

												<td nowrap="nowrap">${map.global_org_name}<br />
													${map.fullname_en} - ${map.designation_name_en} <br />
													${map.mobile1} - ${map.email}
												</td>
												<%-- <td>${map.total}</td> --%>
												<td style="text-align: center;"><a
													href="javascript:getCasesList('${map.email}','${section}');">${map.total}</a></td>
											</tr>
											<bean:define id="total" value="${total + map.total }"></bean:define>
										</logic:iterate>
									</tbody>
									<tfoot>
										<tR>
											<td colspan="2">Totals</td>
											<td colspan="1" style="text-align: center;">${total }</td>

										</tR>
									</tfoot>
								</table>
							</div>
						</logic:notEmpty>

						<logic:present name="CASESLISTDETAILS">
                            <logic:equal value="N" name="section">
                               <table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>Ack  No</th>
										<th>Service Type</th>
										<th>Advocate Name</th>
										<th>Advocate Code</th>
										<th>Case Type</th>
										<th>Main Case Number</th>
										<th>Petitioner Name</th>
										<th>Date</th>
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLISTDETAILS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><input type="button" id="btnShowPopup"
												value="${map.ack_no}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.ack_no}');" />

											</td>
											<td>${map.servicetpye }</td>
											<td>${map.advocatename }</td>
											<td>${map.advocateccno }</td>
											<td>${map.casetype }</td>
											<td>${map.maincaseno }</td>
											<td>${map.petitioner_name }</td>
											<td>${map.inserted_time }</td>

										</tr>

									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="19">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
                            </logic:equal>
                            <logic:equal value="L" name="section">
							<table id="example" class="table table-striped table-bordered"
								style="width:100%">
								<thead>
									<tr>
										<th>Sl.No</th>
										<th>CINo</th>
										<th>Scanned Affidavit</th>
										<!-- <th>Assigned to</th> -->
										<th>Date of Filing</th>
										<!-- <th>Case Type</th>
									<th>Reg.No.</th>
									<th>Reg. Year</th> -->

										<th>Case Reg No.</th>
										<th>Prayer</th>

										<th>Filing No.</th>
										<th>Filing Year</th>
										<th>Date of Next List</th>
										<th>Bench</th>
										<th>Judge Name</th>
										<th>Petitioner</th>
										<th>District</th>
										<th>Purpose</th>
										<th>Respondents</th>
										<th>Petitioner Advocate</th>
										<th>Respondent Advocate</th>
										<th>Orders</th>
									</tr>
								</thead>
								<tbody>

									<logic:iterate id="map" name="CASESLISTDETAILS" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td><input type="button" id="btnShowPopup"
												value="${map.cino}"
												class="btn btn-sm btn-info waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup('${map.cino}');" />

											</td>
											<td><logic:notEmpty name="map"
													property="scanned_document_path1">
													<logic:notEqual value="-" name="map"
														property="scanned_document_path1">
														<%-- ./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf --%>
														<a href="./${map.scanned_document_path}" target="_new"
															class="btn btn-sm btn-info"><i
															class="glyphicon glyphicon-save"></i><span>Scanned
																Affidavit</span></a>
													</logic:notEqual>
												</logic:notEmpty></td>
											<%-- <td nowrap="nowrap">${map.globalorgname}<br />
												${map.fullname} - ${map.designation} <br />
												${map.mobile} - ${map.email}
											</td> --%>
											<td><logic:notEmpty name="map" property="date_of_filing">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>

											<%-- <td>${map.type_name_fil }</td>
										<td>${map.reg_no}</td>
										<td>${map.reg_year }</td> prayer --%>
											<td>${map.type_name_fil }/${map.reg_no}/ ${map.reg_year }</td>
											<td style="width: 300px;">${map.prayer }</td>

											<td>${map.fil_no}</td>
											<td>${map.fil_year }</td>
											<td><logic:notEmpty name="map" property="date_next_list">
													<logic:notEqual value="0001-01-01" name="map"
														property="date_next_list">
																	${map.date_of_filing }
																</logic:notEqual>
												</logic:notEmpty></td>
											<td>${map.bench_name }</td>
											<td>Hon'ble Judge : ${map.coram }</td>
											<td>${map.pet_name }</td>
											<td>${map.dist_name }</td>
											<td>${map.purpose_name }</td>
											<td>${map.res_name },${map.address}</td>

											<td>${map.pet_adv }</td>
											<td>${map.res_adv }</td>
											<td style="text-align: center;">${map.orderpaths }</td>
										</tr>

									</logic:iterate>
								</tbody>
								<tfoot>
									<tR>
										<td colspan="19">&nbsp;</td>
									</tR>
								</tfoot>
							</table>
							</logic:equal>
						</logic:present>

					</div>
				</div>
			</html:form>
		</div>
	</div>
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
						style="width:100%;min-height:350px;;border:0px;"> </iframe>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal">
					Close</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script>
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
			;
		}
		;
	};
	function getCasesList(email,section) {
		$("#email").val(email);
		$("#section").val(section);
		$("#mode").val("getCasesDetails");
		$("#CaseAssignmentStatusForm").submit();
	}

	function getdata()
	{
		if(document.forms[0].elements["dynaForm(section_code)"].value=="0")
	 	{
	       alert("Please Select The Lagacy / New");		
		}else{
			$("#mode").val("getCasesList");
			document.forms[0].submit();
		}
	}
	function sendBack(cid) {
		$("#cINO").val(cid);
		$("#mode").val("sendCaseBack");
		document.forms[0].submit();
	}
</script>
