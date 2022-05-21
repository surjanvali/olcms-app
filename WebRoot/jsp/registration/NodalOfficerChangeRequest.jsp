<%-- <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> --%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />
<div class="page-content fade-in-up">

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
			<div class="ibox-title">MLO / Nodal Officer (Legal)
				Registration</div>
			<div class="ibox-tools">
				<a class="ibox-collapse"><i class="fa fa-minus"></i> </a>
				<!-- <a
					class="dropdown-toggle" data-toggle="dropdown"><i
					class="fa fa-ellipsis-v"></i> </a>  <div class="dropdown-menu dropdown-menu-right">
					<a class="dropdown-item">option 1</a> <a class="dropdown-item">option
						2</a>
				</div> -->
			</div>
		</div>
		<div class="ibox-body">
			<html:form method="post" action="/NodalOfficerChangeReq"
				enctype="multipart/form-data">

				<html:hidden styleId="mode" styleClass="form-control"
					property="mode" />
				<html:hidden property="dynaForm(mloId)" styleClass="form-control" styleId="mloId" />
				<html:hidden property="dynaForm(distId)" styleClass="form-control" styleId="distId" />

				<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="form-group">
							<label> Change Request For <bean:message key="mandatory" />
							</label>
							<html:select styleId="officerType"
								property="dynaForm(officerType)" styleClass="select2Class"
								style="width : 100%;">
								<html:option value="0">---SELECT---</html:option>
								<logic:notEmpty name="role_id" scope="session">
									<logic:notEqual value="2" name="role_id" scope="session">
										<html:option value="MLO">Middle Level Officer (Legal)</html:option>
									</logic:notEqual>
								</logic:notEmpty>
								<html:option value="NO">Nodal Officer (Legal)</html:option>
							</html:select>
						</div>

					</div>
				</div>
				<logic:present name="saveAction">
					<div class="row">
						<logic:equal value="UPDATE" name="saveAction">

							<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
								<div class="form-group" style="display: inline;">
									<label> Registered Head of the Department (HoD) <bean:message
											key="mandatory" />
									</label>
									<html:select styleId="deptId" property="dynaForm(deptId)"
										styleClass="select2Class" style="width : 100%;">
										<html:option value="0">---SELECT---</html:option>
										<logic:notEmpty name="CommonForm"
											property="dynaForm(deptsList)">
											<html:optionsCollection name="CommonForm"
												property="dynaForm(deptsList)" />
										</logic:notEmpty>
									</html:select>

								</div>
							</div>
						</logic:equal>
						<logic:equal value="INSERT" name="saveAction">
							<html:hidden styleId="deptId" property="dynaForm(deptId)" />
						</logic:equal>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Designation <bean:message key="mandatory" />
								</label>
								<html:select styleId="designationId"
									property="dynaForm(designationId)" styleClass="select2Class"
									style="width : 100%;">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(designationList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(designationList)" />
									</logic:notEmpty>
								</html:select>

							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Employee <bean:message key="mandatory" />
								</label>
								<html:select styleId="employeeId"
									property="dynaForm(employeeId)" styleClass="select2Class"
									style="width : 100%;" onchange="populateDetails();">
									<html:option value="0">---SELECT---</html:option>
									<logic:notEmpty name="CommonForm"
										property="dynaForm(employeeList)">
										<html:optionsCollection name="CommonForm"
											property="dynaForm(employeeList)" />
									</logic:notEmpty>
								</html:select>
							</div>
						</div>

					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Mobile <bean:message key="mandatory" />
								</label>
								<html:text styleId="mobileNo" styleClass="form-control"
									property="dynaForm(mobileNo)" maxlength="10" size="12"
									onkeypress="return isNumberKey(event)" />
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Email Id <bean:message key="mandatory" />
								</label>
								<html:text styleId="emailId" styleClass="form-control"
									property="dynaForm(emailId)"
									onchange=" return validateEmail(this)" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Aadhar Number <bean:message key="mandatory" />
								</label>
								<html:text styleId="aadharNo" styleClass="form-control"
									property="dynaForm(aadharNo)" maxlength="12" size="12"
									onkeypress="return isNumberKey(event)"
									onchange="verifyAadhaar();" />
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Reason for change Request <bean:message
										key="mandatory" />
								</label>
								<html:textarea styleId="changeReasons" styleClass="form-control"
									property="dynaForm(changeReasons)" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
							<div class="form-group">
								<label> Upload Letter <bean:message key="mandatory" />
								</label>
								<html:file styleId="changeLetter" styleClass="form-control"
									property="changeLetter" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<input type="button" name="Submit" value="Submit"
								class="btn btn-success pull-right" onclick="addEmployee();" />
						</div>
					</div>

				</logic:present>
				<logic:notEmpty name="List_data">
					<hr />
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="table-responsive">
								<table id="example" class="table table-striped table-bordered"
									style="width:100%">
									<thead>
										<tr>
											<th>Sl.No</th>
											<th>Department Name</th>
											<th>Employee Name</th>
											<th>Designation</th>
											<th>Mobile</th>
											<th>Email Id</th>
											<th>Aadhar Number</th>
											<th>Reasons</th>
											<th>Document</th>
											<th>Request Status</th>
										</tr>
									</thead>
									<tbody>

										<logic:iterate id="map" name="List_data" indexId="i">
											<tr>
												<td>${i+1 }</td>
												<td>${map.description }</td>
												<td>${map.fullname_en }</td>
												<td>${map.designation_name_en }</td>
												<td>${map.mobileno }</td>
												<td>${map.emailid }</td>
												<td>${map.aadharno }</td>
												<td>${map.change_reasons }</td>
												<td><a href="${map.change_letter_path }" target="_new"
													class="btn btn-sm btn-info"><i class="fa fa-save"></i>
												</a></td>

												<%-- <td>
															<button type="button" class="btn btn-sm btn-info"
																onclick="showEdit('${map.slno}')">
																<i class="glyphicon glyphicon-edit"></i> <span>Edit</span>
															</button> <button type="button" class="btn btn-sm btn-danger"
																onclick="deleteData('${map.slno}')">
																<i class="glyphicon glyphicon-trash"></i> <span>Delete</span>
															</button>
														</td> --%>
												<td><logic:notEmpty name="map"
														property="change_req_approved">
														<logic:equal value="true" name="map"
															property="change_req_approved">
															<div class="btn btn-success">Approved</div>
														</logic:equal>
														<logic:equal value="false" name="map"
															property="change_req_approved">
															<div class="btn btn-info">Not Approved</div>
														</logic:equal>

													</logic:notEmpty></td>
											</tr>
										</logic:iterate>

									</tbody>
									<tfoot>
										<tR>
											<td colspan="10">&nbsp;</td>
										</tR>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</logic:notEmpty>

				<!-- </div> -->
			</html:form>
		</div>

	</div>
</div>
<!-- /.row -->

<script src="https://apbudget.apcfss.in/js/select2.js"></script>
<script src="<%=basePath%>js/aadharverification.js"></script>

<script type="text/javascript">
		   $(document).ready(function() {
			    
			    
			    $("#officerType").change(function(){
			    	document.forms[0].mode.value="unspecified";
			  		document.forms[0].submit(); 
			    });
			    
			    if($("#officerType").val()=="MLO"){
			    		$("#HODID").hide();
		    	}
		    	else if($("#officerType").val()=="NO"){
		    		$("#HODID").show();
		    	}
			    
			    
			    $("#deptId").change(function(){
			    $("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
			    $("#designationId").select2('destroy');
					var data = {
						mode : "AjaxAction",
						deptId : $("#deptId").val(),
						getType : "getDesignationsList"
					}
					$.post("AjaxModels.do",data).done(function(res){
						$("#designationId").html(res);
					}).fail(function(exc){
						$("#designationId").html("<option value='0'>---SELECT---</option>");
						alert("Error Occured.Please Try Again");
					});
					$("#designationId").select2();
					$("#designationId").select2("val", "0");
					
					$("#mobileNo").val("");
					$("#emailId").val("");
					$("#aadharNo").val("");
							
					setTimeout(function() {$("#LOADINGPAGEGIF").html("");}, 900);
				});
				
			    
			    $("#designationId").change(function(){
			    $("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
			    $("#employeeId").select2('destroy'); 
					var data = {
						mode : "AjaxAction",
						deptId : $("#deptId").val(),
						designationId : $("#designationId").val(),
						getType : "getEmployeesList",
						//serviceType : "changeReq",
						userType : $("#officerType").val()
						
					}
					$.post("AjaxModels.do",data).done(function(res){
						$("#employeeId").html(res);
					}).fail(function(exc){
						$("#employeeId").html("<option value='0'>---SELECT---</option>");
						alert("Error Occured.Please Try Again.");
					});
					
					$("#employeeId").select2();
					$("#employeeId").select2("val", "0");
					$("#mobileNo").val("");
					$("#emailId").val("");
					$("#aadharNo").val("");
					
					setTimeout(function() {$("#LOADINGPAGEGIF").html("");}, 900);
				});
				
			    $('.select2Class').select2();
			});
			
			function populateDetails(){
			
			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/gears.gif" /> <br /> Retrieving your data. Please wait...');
			
				var data = {
						mode : "AjaxAction",
						empId : $("#employeeId").val(),
						getType : "getEmpDetails"
					}
					$.post("AjaxModels.do",data).done(function(res){
						if(res!=''){
							// alert("RES:"+res);
							// alert("RES1:"+res.split("#")[1]);
							$("#mobileNo").val(res.split("#")[1]);
							$("#emailId").val(res.split("#")[2]);
							$("#aadharNo").val(res.split("#")[3]);
						}
					}).fail(function(exc){
						alert("Error Occured.Please Try Again");
					});
				setTimeout(function() {$("#LOADINGPAGEGIF").html("");}, 900);	
			}
		   
		   function showEdit(val){
	   			$("#mloId").val(val);
	   			document.forms[0].mode.value="editEmployee";
			  document.forms[0].submit();   
		   }
		   
		   function deleteData(val){
	   			$("#mloId").val(val);
	   			document.forms[0].mode.value="deleteEmployeeDetails";
			  document.forms[0].submit();   
		   }
		   
		   function isNumberKey(evt)
		   	{
		   		var charCode = (evt.which) ? evt.which : event.keyCode;
		   		if (charCode > 31 && (charCode < 48 || charCode > 57))
		       		return false;
		   		return true;
			}
			
			function addressType(i) 
			{
				if(i.value.length>0) 
				{
				//i.value = i.value.replace(/[^\sA-Za-z0-9:.\/,\\]+/g, '');
				i.value = i.value.replace(/[^\sA-Za-z0-9,\/.\\:;#]+/g, '');  
				}
			}
			
			function OnlyNumbersAndDot(evt){  
				    var e = window.event || evt; // for trans-browser compatibility  
				    var charCode = e.which || e.keyCode;  
				    if ((charCode > 45 && charCode < 58) || charCode == 8)
				    {  
				        return true;  
				    }  
		    		return false;  
			}
			function trim(s) 
			{
		   	return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
			}
		   
		   function validateEmail(inputText) 
		    {
			   var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			   if(inputText.value.match(mailformat))
			   {
			   		return true;
			   }
			   else
			   {
			   		alert("You have entered an invalid email address!");
			   		inputText.focus();
			   		return false;
			   }
		    }
		   
		  function updateEmployeeDetails(){
		  	
			   designationId = $("#designationId").val();
			   employeeId = $("#employeeId").val();
			   mobileNo = $("#mobileNo").val();
			   emailId = $("#emailId").val();
			   aadharNo = $("#aadharNo").val();
			   
			   if($("#deptId").val() == null || $("#deptId").val() == "" || $("#deptId").val() == "0") 
			   {
			   		alert("Select Department");
			   		$("#deptId").focus();
			   		return false;
			   }
			   if(designationId == null || designationId == "" || designationId == "0") 
			   {
			   		alert("Select Designation");
			   		$("#designationId").focus();
			   		return false;
			   }
			   
			   if(employeeId == null || employeeId == "" || employeeId == "0") 
			   {
			   		alert("Select Employee");
			   		$("#employeeId").focus();
			   		return false;
			   }
			   
			   if(mobileNo == null || mobileNo == "" || mobileNo == "0") 
			   {
			   		alert("Enter Mobile Number");
			   		$("#mobileNo").focus();
			   		return false;
			   }
			   
			   if(mobileNo != null && mobileNo != "" && mobileNo.length != 10) 
			   {
			   		alert("Enter 10-digit Mobile Number");
			   		$("#mobileNo").focus();
			   		return false;
			   }
			   
			   
			   if(emailId == null || emailId == "" || emailId == "0") 
			   {
				   alert("Enter Email Id");
			   	   $("#emailId").focus();
			   	   return false;
			   }
			   var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			   if(!emailId.match(mailformat))
			   {
			   		alert("You have entered an invalid email address!");
			   		$("#emailId").focus();
			   		return false;
			   }
			   
			   if(aadharNo == null || aadharNo == "" || aadharNo == "0") 
			   {
				   alert("Enter Aadhar Number");
			   		$("#aadharNo").focus();
			   		return false;
			   }
			   
			   if(aadharNo != null && aadharNo != "" && aadharNo.length != 12) 
			   {
			   		alert("Enter 12-digit Aadhar Number");
			   		$("#aadharNo").focus();
			   		return false;
			   }
				
			  if(confirm("Do you want to Update employee details?"))
			  {
				  document.forms[0].mode.value="updateEmployeeDetails";
				  document.forms[0].submit();   
			  }
			  else 
				  return false; 
		  
		  }
		   
		   function addEmployee()
		   {
			   designationId = $("#designationId").val();
			   employeeId = $("#employeeId").val();
			   mobileNo = $("#mobileNo").val();
			   emailId = $("#emailId").val();
			   aadharNo = $("#aadharNo").val();
			   
			   if($("#deptId").val() == null || $("#deptId").val() == "" || $("#deptId").val() == "0") 
			   {
			   		alert("Select Department");
			   		$("#deptId").focus();
			   		return false;
			   }
			   
			   if(designationId == null || designationId == "" || designationId == "0") 
			   {
			   		alert("Select Designation");
			   		$("#designationId").focus();
			   		return false;
			   }
			   
			   if(employeeId == null || employeeId == "" || employeeId == "0") 
			   {
			   		alert("Select Employee");
			   		$("#employeeId").focus();
			   		return false;
			   }
			   
			   if(mobileNo == null || mobileNo == "" || mobileNo == "0") 
			   {
			   		alert("Enter Mobile Number");
			   		$("#mobileNo").focus();
			   		return false;
			   }
			   
			   if(mobileNo != null && mobileNo != "" && mobileNo.length != 10) 
			   {
			   		alert("Enter 10-digit Mobile Number");
			   		$("#mobileNo").focus();
			   		return false;
			   }
			   
			   
			   if(emailId == null || emailId == "" || emailId == "0") 
			   {
				   alert("Enter Email Id");
			   	   $("#emailId").focus();
			   	   return false;
			   }
			   
			   var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			   if(!emailId.match(mailformat))
			   {
			   		alert("You have entered an invalid email address!");
			   		$("#emailId").focus();
			   		return false;
			   }
			   
			   if(aadharNo == null || aadharNo == "" || aadharNo == "0") 
			   {
				   alert("Enter Aadhar Number");
			   		$("#aadharNo").focus();
			   		return false;
			   }
			   
			   if(aadharNo != null && aadharNo != "" && aadharNo.length != 12) 
			   {
			   		alert("Enter 12-digit Aadhar Number");
			   		$("#aadharNo").focus();
			   		return false;
			   }
				
			  if(confirm("Do you want to save Change Request details?"))
			  {
				  document.forms[0].mode.value="saveEmployeeDetails";
				  document.forms[0].submit();   
			  }
			  else 
				  return false; 
		   }
		   
		   
		 function verifyAadhaar() {
			var aadharNo = $("#aadharNo").val();
			if(validate(aadharNo)) {
				
			} else {
				alert("Invalid aadhar no.");
				$("#aadharNo").val("");
			}
		}
	
		   </script>
