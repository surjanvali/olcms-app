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
body {
	overflow-y: auto;
}
</style>

<div class="page-content fade-in-up">
	<html:form action="/EcourtsDeptInstruction"
		enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<%-- <html:hidden styleId="cino" property="dynaForm(cino)" /> --%>
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
		<logic:notEmpty name="CASESLISTOLD">
			<div class="ibox oldTypediv">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b>Case Details</b>
					</h4>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">

						<table id="example"
							class="table table-striped table-bordered oldTypediv"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>CINo</th>
									<th>Scanned Affidavit</th>
									<!-- <th>Assigned to</th> -->
									<th>Current Status</th>
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

								<logic:iterate id="map" name="CASESLISTOLD" indexId="i">
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

										<td nowrap="nowrap"><span
											style="color: navy;font-weight: bold;text-align: center;">
												${map.current_status}</span> <logic:notEmpty name="map"
												property="fullname">
												<logic:notEqual value=" " name="map" property="fullname">
													<br />${map.fullname}</logic:notEqual>
											</logic:notEmpty> <logic:notEmpty name="map" property="designation">
												<logic:notEqual value=" " name="map" property="designation">
													<br /> ${map.designation}</logic:notEqual>
											</logic:notEmpty> <logic:notEmpty name="map" property="mobile">
												<logic:notEqual value=" " name="map" property="mobile">
													<br /> ${map.mobile}</logic:notEqual>
											</logic:notEmpty> <logic:notEmpty name="map" property="email">
												<logic:notEqual value=" " name="map" property="email">
													<br /> ${map.email}</logic:notEqual>
											</logic:notEmpty> <logic:notEmpty name="map" property="district_name">
												<logic:notEqual value=" " name="map"
													property="district_name">
													<br /> ${map.district_name}</logic:notEqual>
											</logic:notEmpty> <%-- ${map.globalorgname}<br /> 
												 ${map.designation} <br />
												${map.mobile} <br /> ${map.email} <br /> ${map.district_name }--%>
										</td>
										<td nowrap="nowrap"><logic:notEmpty name="map"
												property="date_of_filing">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_of_filing">
																	${map.date_of_filing }
																</logic:notEqual>
											</logic:notEmpty></td>

										<td nowrap="nowrap">${map.type_name_fil }/${map.reg_no}/${map.reg_year }</td>
										<td style="min-width: 350px;text-align: justify;"><logic:notEmpty
												name="map" property="prayer">

												<logic:equal value="-" name="map" property="prayer">
												N/A
												</logic:equal>

												<logic:notEqual value="-" name="map" property="prayer">
										
										
										${map.prayer }
										
										<button class="btn btn-info btn-xs" data-container="body"
														data-toggle="popover" data-trigger="hover"
														data-placement="top" data-content="${map.prayer_full }"
														data-original-title="" title="">View More</button>
												</logic:notEqual>
											</logic:notEmpty></td>

										<td>${map.fil_no}</td>
										<td>${map.fil_year }</td>
										<td nowrap="nowrap"><logic:notEmpty name="map"
												property="date_next_list">
												<logic:notEqual value="0001-01-01" name="map"
													property="date_next_list">
																	${map.date_next_list }
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
									<td colspan="20">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div>



			<!-- ------------- -->

			<div class="ibox">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b>Instructions submitted</b>
					</h4>
				</div>
				<div class="ibox-body">
					<div class="row NewTypediv">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<table id="example"
								class="table table-striped table-bordered NewTypediv"
								style="width:100%">
								<%-- <thead>
							<tr>
								<th>Sl.No</th>
								<th>Description</th>
								<th>Submitted On</th>
								<th>Uploaded Instructions File</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="map" name="existData" indexId="i">
								<tr>
									<td>${i+1 }.</td>
									<td>${map.instructions }</td>
									<td>${map.insert_time}</td>
									<td><logic:notEqual value="-" name="map"
											property="upload_fileno">
											<a href='${map.upload_fileno}' target='_new'
												class="btn btn-sm btn-info">View Uploaded File</a>
										</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>

								</tr>
							</logic:iterate>
						</tbody> --%>


								<thead>
									<tr>
										<th colspan="12">Instructions Submit</th>
									</tr>
									<tr>
										<th>Sl.No</th>
										<th>Description</th>
										<th>Submitted By</th>
										<th>Submitted On</th>
										<th>Uploaded File</th>
										<th>Reply Sent</th>
										<!-- <th>Reply Submitted By</th> -->
										<th>Reply Submitted On</th>
										<th>Reply Uploaded File</th>

										<!-- <th>Reply to Instructions</th> -->
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="existData" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.instructions }</td>
											<td>${map.insert_by}</td>
											<td>${map.insert_time}</td>
											<td><logic:notEqual value="-" name="map"
													property="upload_fileno">
													<a href='${map.upload_fileno}' target='_new'
														class="btn btn-sm btn-info">View Uploaded File</a>
												</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>
											<td>${map.reply_instructions}</td>
											<%-- <td>${map.reply_insert_by}</td> --%>
											<td>${map.reply_insert_time}</td>
											<td><logic:notEqual value="-" name="map"
													property="reply_upload_fileno">
													<a href='${map.reply_upload_fileno}' target='_new'
														class="btn btn-sm btn-info">View Uploaded File</a>
												</logic:notEqual> <logic:equal value="-" name="map"
													property="reply_upload_fileno">
														---
													</logic:equal></td>
											<%-- <td>
												 <input type="button" id="btnShowPopup" value="Reply to Instructions"
												class="btn btn-sm btn-success waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}','${map.legacy_ack_flag}','${map.slno}');" />
											</td> --%>
										</tr>
									</logic:iterate>
								</tbody>


							</table>
						</div>
					</div>
				</div>
			</div>



			<!-- //------------------------- -->

			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">
						<h4 class="m-t-0 header-title">
							<b>Submit New Instruction</b>
						</h4>
					</div>
				</div>
				<div class="ibox-body">
					<html:hidden styleId="cino" property="dynaForm(cino)" />
					<div class="row ">
						<div class="col-md-6 col-xs-12 pull-right">

							<b> Enter Instructions </b>

						</div>
						<div class="col-md-6 col-xs-12">
							<html:textarea styleId="instructions"
								property="dynaForm(instructions)" styleClass="form-control"
								cols="50" rows="5">
							</html:textarea>
						</div>
					</div>
					<div class="row oldTypediv">
						<div class="col-md-6 col-xs-12 pull-right">
							<b> Upload file: </b>
						</div>
						<div class="col-md-6 col-xs-12">
							<html:file property="changeLetter" styleId="changeLetter"
								styleClass="form-control"></html:file>
						</div>
					</div>
				</div>
				<div class="ibox-footer text-center">
					<div class="row">
						<div class="col-md-12 col-xs-12 text-center">
							<input type="submit" name="submit" value="Submit"
								class="btn btn-success"
								onclick="return fnSubmitCategoryLegacy();" />
						</div>
					</div>
				</div>
			</div>

		</logic:notEmpty>




		<logic:notEmpty name="CASESLISTNEW">
			<div class="ibox NewTypediv">
				<div class="ibox-head">
					<div class="ibox-title">
						<h4 class="m-t-0 header-title">
							<b>Case Details</b>
						</h4>
					</div>
				</div>
				<div class="ibox-body">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover NewTypediv"
							id="example">
							<thead>
								<tr>
									<th>Sl.No</th>
									<!-- <th></th> -->
									<th>Ack No.</th>
									<th>Date</th>
									<th>District</th>
									<th>Case Type</th>
									<th>Main Case No.</th>
									<th>Departments / Respondents</th>
									<th>Advocate CC No.</th>
									<th>Advocate Name</th>
									<!-- <th>Remarks</th> -->
									<th>Download / Print</th>
									<!-- <th>Instructions / Daily Status Action</th> -->
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="map" name="CASESLISTNEW" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.ack_no }<logic:notEqual value="-" name="map"
												property="hc_ack_no">

												<span style="color: navy;font-weight: bold;">${map.hc_ack_no }</span>

											</logic:notEqual></td>
										<td nowrap="nowrap">${map.generated_date }</td>
										<td>${map.district_name}</td>
										<td>${map.case_full_name}</td>
										<td>${map.maincaseno }</td>
										<td nowrap="nowrap">${map.dept_descs}</td>
										<td>${map.advocateccno}</td>
										<td>${map.advocatename}</td>
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
											</logic:present> <logic:notEqual value="-" name="map" property="hc_ack_no">
												<a
													href="./uploads/scandocs/${map.hc_ack_no}/${map.hc_ack_no}.pdf"
													target="_new" title="Print Barcode"
													class="btn btn-sm btn-info">
											</logic:notEqual> <logic:equal value="-" name="map" property="hc_ack_no">
												<a href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
													target="_new" title="Print Barcode"
													class="btn btn-sm btn-info">
											</logic:equal> <i class="fa fa-save"></i> <span>Scanned Affidavit</span> </a></td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td><logic:present name="DISPLAYOLD">
											<td colspan="14">&nbsp;
										</logic:present> <logic:notPresent name="DISPLAYOLD">
											<td colspan="12">&nbsp;
										</logic:notPresent></td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div>

			<!-- //-------------------- -->

			<div class="ibox">
				<div class="ibox-title">
					<h4 class="m-t-0 header-title">
						<b> Instructions submitted</b>
					</h4>
				</div>
				<div class="ibox-body">
					<div class="row ">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<table id="example"
								class="table table-striped table-bordered NewTypediv"
								style="width:100%">
								<%-- <thead>
							<tr>
								<th>Sl.No</th>
								<th>Description</th>
								<th>Submitted On</th>
								<th>Uploaded Instructions File</th>
							</tr>
						</thead>
						<tbody>
							<logic:iterate id="map" name="existDataNew" indexId="i">
								<tr>
									<td>${i+1 }.</td>
									<td>${map.instructions }</td>
									<td>${map.insert_time}</td>
									<td><logic:notEqual value="-" name="map"
											property="upload_fileno">
											<a href='${map.upload_fileno}' target='_new'
												class="btn btn-sm btn-info">View Uploaded File</a>
										</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>

								</tr>
							</logic:iterate>
						</tbody> --%>

								<thead>
									<tr>
										<th colspan="12">Instructions Submit</th>
									</tr>
									<tr>
										<th>Sl.No</th>
										<th>Description</th>
										<th>Submitted By</th>
										<th>Submitted On</th>
										<th>Uploaded File</th>
										<th>Reply Sent</th>
										<!-- <th>Reply Submitted By</th> -->
										<th>Reply Submitted On</th>
										<th>Reply Uploaded File</th>

										<!-- <th>Reply to Instructions</th> -->
									</tr>
								</thead>
								<tbody>
									<logic:iterate id="map" name="existDataNew" indexId="i">
										<tr>
											<td>${i+1 }.</td>
											<td>${map.instructions }</td>
											<td>${map.insert_by}</td>
											<td>${map.insert_time}</td>
											<td><logic:notEqual value="-" name="map"
													property="upload_fileno">
													<a href='${map.upload_fileno}' target='_new'
														class="btn btn-sm btn-info">View Uploaded File</a>
												</logic:notEqual> <logic:equal value="-" name="map" property="upload_fileno">
														---
													</logic:equal></td>
											<td>${map.reply_instructions}</td>
											<%-- <td>${map.reply_insert_by}</td> --%>
											<td>${map.reply_insert_time}</td>
											<td><logic:notEqual value="-" name="map"
													property="reply_upload_fileno">
													<a href='${map.reply_upload_fileno}' target='_new'
														class="btn btn-sm btn-info">View Uploaded File</a>
												</logic:notEqual> <logic:equal value="-" name="map"
													property="reply_upload_fileno">
														---
													</logic:equal></td>
											<%-- <td>
												 <input type="button" id="btnShowPopup" value="Reply to Instructions"
												class="btn btn-sm btn-success waves-effect waves-light"
												onclick="javascript:viewCaseDetailsPopup1('${map.cino}','${map.legacy_ack_flag}','${map.slno}');" />
											</td> --%>
										</tr>
									</logic:iterate>
								</tbody>

							</table>
						</div>
					</div>
				</div>
			</div>


			<!-- //------------------------- -->

			<div class="ibox">
				<div class="ibox-head">
					<div class="ibox-title">
						<h4 class="m-t-0 header-title">
							<b>Submit New Instruction</b>
						</h4>
					</div>
				</div>
				<div class="ibox-body">
					<html:hidden styleId="cino" property="dynaForm(cino)" />
					<div class="row ">
						<div class="col-md-6 col-xs-12 pull-right">

							<b> Enter Instructions </b>

						</div>
						<div class="col-md-6 col-xs-12">
							<html:textarea styleId="instructions"
								property="dynaForm(instructions)" styleClass="form-control"
								cols="50" rows="5">
							</html:textarea>
						</div>
					</div>
					<div class="row oldTypediv">
						<div class="col-md-6 col-xs-12 pull-right">
							<b> Upload file: </b>
						</div>
						<div class="col-md-6 col-xs-12">
							<html:file property="changeLetter" styleId="changeLetter"
								styleClass="form-control"></html:file>
						</div>
					</div>
				</div>
				<div class="ibox-footer text-center">
					<div class="row">
						<div class="col-md-12 col-xs-12 text-center">
							<input type="submit" name="submit" value="Submit"
								class="btn btn-success" onclick="return fnSubmitCategoryNew();" />
						</div>
					</div>
				</div>
			</div>






		</logic:notEmpty>

	</html:form>
</div>

<script type="text/javascript">
	/* function fnSubmitCategory() {
		if (($("#instructions").val() == "" || $("#instructions").val() == "0")) {
			alert("Please Enter Instructions");
			return false;
		}
		$("#mode").val("getSubmitCategory");
		$("#HighCourtCasesListForm").submit();
	} */
	
	
	
	
	
	function fnSubmitCategoryNew() {
		if (($("#instructions").val() == "" || $("#instructions").val() == "0")) {
			alert("Please Enter Instructions");
			return false;
		}
		
		$("#mode").val("getSubmitCategoryNew");
		$("#HighCourtCasesListForm").submit();
	}
	
	
	function fnSubmitCategoryLegacy() {
		if (($("#instructions").val() == "" || $("#instructions").val() == "0")) {
			alert("Please Enter Instructions");
			return false;
		}
		
		$("#mode").val("getSubmitCategoryLegacy");
		$("#HighCourtCasesListForm").submit();
	}
	
	
	
	
	
	
</script>