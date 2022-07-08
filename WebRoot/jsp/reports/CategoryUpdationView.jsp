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

<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h3>

</div>
<div class="page-content fade-in-up">
	<html:form action="/HighCourtCasesCategoryUpdationReport">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden styleId="cino" property="dynaForm(cino)" />
		<html:hidden styleId="respondentIds"
			property="dynaForm(respondentIds)" />
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
			<%-- <logic:iterate id="map" name="CASESLIST" indexId="i"> --%>
			<logic:present name="USERSLIST">
				<logic:iterate id="datamap" name="USERSLIST">
					<html:hidden styleId="cino" property="dynaForm(cino)"
						value="${datamap.cino}" />
					<div class="ibox-body">

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Category: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:select styleId="fin_category"
									value="${datamap.finance_category}"
									property="dynaForm(fin_category)" styleClass="form-control">
									<html:option value="0">---Select Category---</html:option>
									<html:option value="A1">Category - A1</html:option>
									<html:option value="A2">Category - A2</html:option>
									<html:option value="B1">Category - B1</html:option>
									<html:option value="B2">Category - B2</html:option>
									<html:option value="C1">Category - C1</html:option>
									<html:option value="C2">Category - C2</html:option>
								</html:select>
							</div>
						</div>

						<div class="row">
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Name Of the work: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="nameOfwork" styleClass="form-control"
									value="${datamap.work_name}" property="dynaForm(nameOfwork)"
									maxlength="40" />
							</div>

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Est. Cost(Lakhs) : </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="estCost" styleClass="form-control"
									value="${datamap.est_cost}" property="dynaForm(estCost)"
									maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Administrative Sanction(GO/Proceedings).: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="adminSanction" styleClass="form-control"
									value="${datamap.admin_sanction}"
									property="dynaForm(adminSanction)" maxlength="40" />
							</div>
						</div>
						<div class="row">

							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Grant : </b>
							</div>
							
							
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
																
								<html:select styleId="grant"
									value="${datamap.grant_val}"
									property="dynaForm(grant)" styleClass="form-control">
									<html:option value="0">---Select Grant---</html:option>
									<html:option value="SDP">SDP</html:option>
									<html:option value="EAP">EAP</html:option>
									<html:option value="NABARD">NABARD</html:option>
									
								</html:select>	
									
							</div>


							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b>E-File Computer No: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="fileComNo" styleClass="form-control"
									value="${datamap.efile_com_no}" property="dynaForm(fileComNo)"
									maxlength="40" />
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
								<b> Remarks: </b>
							</div>
							<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
								<html:text styleId="remarks" styleClass="form-control"
									value="${datamap.bill_remarks}" property="dynaForm(remarks)"
									maxlength="150" />
							</div>
						</div>
					</div>

					<div class="row">

						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<table id="RESPSTABID" class="table table-bordered">
								<thead>
									<tr>
										<th colspan="10">CFMS Bill Details: <span
											class="pull-right"><input type="button" value="Add"
												class="btn btn-sm btn-success" name="add" id="addResp" /> <input
												type="button" value="Remove" class="btn btn-sm btn-danger"
												name="remove" id="removeResp" /></span></th>
									</tr>
									<tr>
										<th style="width: 10%;">Sl No.</th>
										<th style="width: 60%;">Bill Id</th>
										<th style="width: 60%;">Bill Amount</th>
										<th style="width: 60%;">Bill Status</th>

									</tr>
								</thead>

								<tbody>
									<logic:notPresent name="cfmsdata">
										<tr id="1">
											<td>${i+1}</td>
											<td><html:text styleId="cfmsBill1"
													styleClass="form-control" style="height:30px;width:300px"
													value="${map.cfms_bill_id}" property="dynaForm(cfmsBill1)"
													maxlength="10" /></td>
											<td><html:text styleId="billAmount1"
													styleClass="form-control" style="height:30px;width:300px"
													value="${map.cfms_bill_amount}"
													property="dynaForm(billAmount1)" maxlength="15" /></td>
											<td><html:text styleId="status1"
													styleClass="form-control" style="height:30px;width:300px"
													value="${map.cfms_bill_status}"
													property="dynaForm(status1)" maxlength="50" /></td>
										</tr>
									</logic:notPresent> 
									<logic:present name="cfmsdata">
										<logic:iterate id="map" name="cfmsdata" indexId="i">
											<tr id="1">

												<td>${i+1}</td>
												<td><html:text styleId="cfmsBill${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														value="${map.cfms_bill_id}"
														property="dynaForm(cfmsBill${i+1})" maxlength="10" /></td>
												<td><html:text styleId="billAmount${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														value="${map.cfms_bill_amount}"
														property="dynaForm(billAmount${i+1})" maxlength="15" /></td>
												<td><html:text styleId="status${i+1}"
														styleClass="form-control" style="height:30px;width:300px"
														value="${map.cfms_bill_status}"
														property="dynaForm(status${i+1})" maxlength="50" /></td>

											</tr>
										</logic:iterate>
									</logic:present>
								</tbody>

							</table>

							<!-- 
					<logic:notPresent name="cfmsdata">
						<table id="RESPSTABID" class="table table-bordered"
							style="width: 100%;">
							<thead>
								<tr>
									<th colspan="10">CFMS Bill Details: <span
										class="pull-righttt"><input type="button" value="Add"
											class="btn btn-sm btn-success" name="add" id="addResp" /> <input
											type="button" value="Remove" class="btn btn-sm btn-danger"
											name="remove" id="removeResp" /></span></th>
								</tr>
								<tr>
									<th style="width: 10%;">Sl No.</th>
									<th style="width: 60%;">Bill Id</th>
									<th style="width: 60%;">Bill Amount</th>
									<th style="width: 60%;">Bill Status</th>

								</tr>
							</thead>

							<tbody>

								<tr id="1">

									<td>${i+1}</td>
									<td><html:text styleId="cfmsBill1"
											styleClass="form-control" style="height:30px;width:300px"
											value="${map.cfms_bill_id}" property="dynaForm(cfmsBill1)"
											maxlength="10" /></td>
									<td><html:text styleId="billAmount1"
											styleClass="form-control" style="height:30px;width:300px"
											value="${map.cfms_bill_amount}"
											property="dynaForm(billAmount1)" maxlength="15" /></td>
									<td><html:text styleId="status1" styleClass="form-control"
											style="height:30px;width:300px"
											value="${map.cfms_bill_status}" property="dynaForm(status1)"
											maxlength="50" /></td>

								</tr>
							</tbody>

						</table>
					</logic:notPresent>


					<br />
					<br />
					<logic:present name="cfmsdata">
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="form-group">

									<table id="RESPSTABID" class="table table-bordered"
										style="width: 100%;">
										<thead>
											<tr>
												<th colspan="10">CFMS Bill Details: <span
													class="pull-righttt"><input type="button" value="Add"
														class="btn btn-sm btn-success" name="add" id="addResp" />
														<input type="button" value="Remove"
														class="btn btn-sm btn-danger" name="remove"
														id="removeResp" /></span></th>
											</tr>
											<tr>
												<th style="width: 10%;">Sl No.</th>
												<th style="width: 60%;">Bill Id</th>
												<th style="width: 60%;">Bill Amount</th>
												<th style="width: 60%;">Bill Status</th>

											</tr>
										</thead>
										<logic:iterate id="map" name="cfmsdata" indexId="i">
											<tbody>

												<tr id="1">

													<td>${i+1}</td>
													<td><html:text styleId="cfmsBill${i+1}"
															styleClass="form-control" style="height:30px;width:300px"
															value="${map.cfms_bill_id}"
															property="dynaForm(cfmsBill${i+1})" maxlength="10" /></td>
													<td><html:text styleId="billAmount${i+1}"
															styleClass="form-control" style="height:30px;width:300px"
															value="${map.cfms_bill_amount}"
															property="dynaForm(billAmount${i+1})" maxlength="15" /></td>
													<td><html:text styleId="status${i+1}"
															styleClass="form-control" style="height:30px;width:300px"
															value="${map.cfms_bill_status}"
															property="dynaForm(status${i+1})" maxlength="50" /></td>

												</tr>
											</tbody>
										</logic:iterate>
									</table>
								</div>
							</div>
						</div>
					</logic:present> -->

						</div>
					</div>
				</logic:iterate>
			</logic:present>

			<div class="row" style="text-align: center">
				<div class="col-md-12 col-xs-12">
					<input type="submit" name="submit" value="Submit"
						class="btn btn-success"
						onclick="return fnSubmitCategory('${datamap.cino}');" />
				</div>
			</div>
		</div>





	</html:form>
</div>



<script type="text/javascript">
	function fnSubmitCategory(id) {
		//alert((id));

		if ($("#fin_category").val() == null || $("#fin_category").val() == ""
				|| $("#fin_category").val() == 0) {
			alert("FIN Category Required");
			$("#fin_category").focus();
			return false;
		}

		if ($("#nameOfwork").val() == null || $("#nameOfwork").val() == "") {
			alert("Name Of work Required");
			$("#nameOfwork").focus();
			return false;
		}

		if ($("#estCost").val() == null || $("#estCost").val() == "") {
			alert("Estimated Cost Required");
			$("#estCost").focus();
			return false;
		}
		if(isNaN($("#estCost").val())){
			alert("Estimated Cost field should be a number");
			$("#estCost").focus();
			return false;
		 }

		if ($("#adminSanction").val() == null
				|| $("#adminSanction").val() == "") {
			alert("Administrative Sanction Required");
			$("#adminSanction").focus();
			return false;
		}

		if ($("#grant").val() == null || $("#grant").val() == "" || $("#grant").val() == 0) {
			alert("Grant Required");
			$("#grant").focus();
			return false;
		}

		/* 		 if ($("#cfmsBill").val() == null || $("#cfmsBill").val() == "") {
		 alert("CFMS Bill Required");
		 $("#cfmsBill").focus();
		 return false;
		 }
		 if ($("#status").val() == null || $("#status").val() == "") {
		 alert("Bill Status Required");
		 $("#status").focus();
		 return false;
		 }
		 if ($("#billAmount").val() == null || $("#billAmount").val() == "") {
		 alert("Bill Amount Required");
		 $("#billAmount").focus();
		 return false;
		 }  */
		if ($("#fileComNo").val() == null || $("#fileComNo").val() == "") {
			alert("E-File Computer No Required");
			$("#fileComNo").focus();
			return false;
		}

		if ($("#remarks").val() == null || $("#remarks").val() == "") {
			alert("Remarks Required");
			$("#remarks").focus();
			return false;
		}

		$("#cino").val(id);
		$("#mode").val("getSubmitCategory");
		$("#respondentIds").val(
				$("#RESPSTABID tbody tr").length);
		$("#HighCourtCasesListForm").submit();
	}

	$("#addResp")
			.click(
					function() {
						//alert("wait....")
						let rowfyable = $("#RESPSTABID").closest('table');
						//let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
						let rowCount = $("#RESPSTABID tbody tr").length; //$('tbody', rowfyable).rows.length;
						let rowCount2 = rowCount + 1;
						let cfmsBill = $("#cfmsBill" + rowCount).val();
						let billAmount = $("#billAmount" + rowCount).val();
						let status = $("#status" + rowCount).val();

						//alert("prevVal--"+prevVal);
						if ((cfmsBill != null && cfmsBill != "" && cfmsBill != "0")
								&& (billAmount != null && billAmount != "" && billAmount != "0")) {
							//console.log("rowCount:"+rowCount);
							// console.log("rowCount2:"+rowCount2);

							$('tbody', rowfyable)
									.append(
											"<tr id='"+rowCount2+"'><td>"
													+ rowCount2
													+ ".</td><td>"
													//+ "<text name='dynaForm(cfmsBill"<input type="text" name="dynaForm(nameOfwork)" maxlength="40" value="" id="nameOfwork" class="form-control">
													+ "<input type='text' name='dynaForm(cfmsBill"
													+ rowCount2
													+ ")' id='cfmsBill"
													+ rowCount2
													+ "' style='height:30px;width:300px'></text></td><td>"
													+ "<input type='text' name='dynaForm(billAmount"
													+ rowCount2
													+ ")' id='billAmount"
													+ rowCount2
													+ "' style='height:30px;width:300px'></text>"
													+ "</td>"
													+ "<td><input type='text' name='dynaForm(status"
													+ rowCount2
													+ ")' id='status"
													+ rowCount2
													+ "' style='height:30px;width:300px' ></text></td></tr>"

													+ "");

							$("#respondentIds").val(
									$("#RESPSTABID tbody tr").length);
						}

						if ((cfmsBill == "")) {
							alert("Pease enter Bill Id");
							$("#cfmsBill" + rowCount).focus();
						}

						if ((billAmount == "")) {
							alert(" Enter Bill Amount");
							$("#billAmount" + rowCount).focus();
						}

						if ((status == "")) {
							alert(" Enter Bill Status");
							$("#status" + rowCount).focus();
						}

					});

	$("#removeResp").click(function() {
		let rowfyable = $("#RESPSTABID").closest('table');
		let rowCount = $("#RESPSTABID tbody tr").length;
		if (rowCount > 1) {
			$('tbody tr:last', rowfyable).remove();
		}
		else {
			alert ("Should have atleast one valid Bill Details")
		}
	});

	/* =============================
		
		$("#addResp_up")
			.click(
					function() {
						//alert("wait....")
						let rowfyable = $("#RESPSTABID_UP").closest('table');
						//let randomNo = Math.floor(Math.random() * (100 - 2) + 1);
						let rowCount = $("#RESPSTABID_UP tbody tr").length; //$('tbody', rowfyable).rows.length;
						let rowCount2 = rowCount + 1;
						let cfmsBill = $("#cfmsBill" + rowCount).val();
						let billAmount = $("#billAmount" + rowCount).val();
						let status = $("#status" + rowCount).val();

						alert("prevVal--"+rowCount);
						if ((cfmsBill != null && cfmsBill != "" && cfmsBill != "0")
								&& (billAmount != null && billAmount != "" && billAmount != "0")) {
							//console.log("rowCount:"+rowCount);
							// console.log("rowCount2:"+rowCount2);

							$('tbody', rowfyable)
									.append(
											"<tr id='"+rowCount2+"'><td>"
													+ rowCount2
													+ ".</td><td>"
													+ "<input type='text' name='dynaForm(cfmsBill"
													+ rowCount2
													+ ")' id='cfmsBill"
													+ rowCount2
													+ "' style='height:30px;width:300px'></text></td><td>"
													+ "<input type='text' name='dynaForm(billAmount"
													+ rowCount2
													+ ")' id='billAmount"
													+ rowCount2
													+ "' style='height:30px;width:300px'></text>"
													+ "</td>"
													+ "<td><input type='text' name='dynaForm(status"
													+ rowCount2
													+ ")' id='status"
													+ rowCount2
													+ "' style='height:30px;width:300px' ></text></td></tr>"

													+ "");


							$("#respondentIds").val(
									$("#RESPSTABID_UP tbody tr").length);
						}

						if ((cfmsBill == "")) {
							alert("Pease enter Bill Id");
							$("#cfmsBill" + rowCount).focus();
						}

						if ((billAmount == "")) {
							alert(" Enter Bill Amount");
							$("#billAmount" + rowCount).focus();
						}

						if ((status == "")) {
							alert(" Enter Bill Status");
							$("#status" + rowCount).focus();
						}

					});
	$("#removeResp").click(function() {
		let rowfyable = $("#RESPSTABID_UP").closest('table');
		let rowCount = $("#RESPSTABID_UP tbody tr").length;
		if (rowCount > 1) {
			$('tbody tr:last', rowfyable).remove();
		}
		$("#respondentIds").val($("#RESPSTABID_UP tbody tr").length);
	});
	 */
</script>
