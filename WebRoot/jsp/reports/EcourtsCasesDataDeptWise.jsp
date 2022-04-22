<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- PAGE LEVEL STYLES-->
<!-- START PAGE CONTENT-->
<div class="page-heading">
	<h1 class="page-title">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h1>
	<%-- <ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="./Welcome.do"><i
				class="la la-home font-20"></i> </a></li>
		<li class="breadcrumb-item"><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty></li>
	</ol> --%>
</div>
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
		<%-- <div class="ibox-head">
			<div class="ibox-title">
				<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
			</div>
		</div> --%>
		<div class="ibox-body">

			<html:form method="post" action="/eCourtCasesData">

				<html:hidden styleId="mode" property="mode" />
				<html:hidden property="dynaForm(deptId)" styleId="deptId" />
				<div class="table-responsive">
					<logic:notEmpty name="DEPTWISEHCCASES">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Department Code</th>
									<th>Department Name</th>
									<th>Total Cases</th>
									<th>Assigned Cases</th>
								</tr>
							</thead>
							<tbody>

								<bean:define id="regTotals" value="0"></bean:define>
								<bean:define id="regAssgTotals" value="0"></bean:define>
								<logic:iterate id="map" name="DEPTWISEHCCASES" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>
											<%-- <a
															href="javascript:showHODWise('${map.inserted_by}');">${map.deptshortname }</a> --%>

											${map.deptshortname }

										</td>
										<td>${map.description }</td>
										<td style="text-align: right;">${map.total }</td>
										<td style="text-align: right;">${map.assigned }</td>
									</tr>
									<bean:define id="regTotals" value="${regTotals + map.total }"></bean:define>
									<bean:define id="regAssgTotals"
										value="${regAssgTotals + map.assigned }"></bean:define>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<td colspan="3">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
									<td colspan="1" style="text-align: right;">${regAssgTotals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>


					<logic:notEmpty name="HODDEPTWISENOS">
						<table id="example" class="table table-striped table-bordered"
							style="width:100%">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>HOD Name</th>
									<th>Registered Count</th>
								</tr>
							</thead>
							<tbody>
								<bean:define id="regTotals" value="0"></bean:define>
								<logic:iterate id="map" name="HODDEPTWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td><a
											href="javascript:showOfficerWise('${map.inserted_by}');">${map.dept_desc }</a>
										</td>
										<td style="text-align: right;">${map.registered_nodal_officers }</td>
									</tr>
									<bean:define id="regTotals"
										value="${regTotals + map.registered_nodal_officers }"></bean:define>
								</logic:iterate>

							</tbody>
							<tfoot>
								<tR>
									<td colspan="2">Totals</td>
									<td colspan="1" style="text-align: right;">${regTotals }</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>

					<logic:notEmpty name="EMPWISENOS">
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
									<th>Aadhaar Number</th>
								</tr>
							</thead>
							<tbody>

								<logic:iterate id="map" name="EMPWISENOS" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.description }</td>
										<td>${map.fullname_en }</td>
										<td>${map.designation_name_en }</td>
										<td>${map.mobileno }</td>
										<td>${map.emailid }</td>
										<td>${map.aadharno }</td>

									</tr>
								</logic:iterate>

							</tbody>
							<tfoot>
								<tR>
									<td colspan="9">&nbsp;</td>
								</tR>
							</tfoot>
						</table>
					</logic:notEmpty>
				</div>
			</html:form>
		</div>
	</div>
</div>
<script type="text/javascript">

			function showOfficerWise(deptId){
				$("#deptId").val(deptId);
				document.forms[0].mode.value="getOfficerWise";
			 	document.forms[0].submit();
			}
			
			function showHODWise(deptId){
				$("#deptId").val(deptId);
				document.forms[0].mode.value="getHODWiseReport";
			 	document.forms[0].submit();
			}

			function backFn(){
				document.forms[0].mode.value="unspecified";
			 		document.forms[0].submit(); 
			}

		   $(document).ready(function() {
			    
			  /*   if ($('#example')) {
					$('#example').dataTable({
						"pageLength" : 50,
						"buttons" : [ 'copy', 'excel', 'pdf', 'colvis' ]
					});
			    }; */
			    
			    $("#deptId").change(function(){
			    	document.forms[0].mode.value="getHodEmployeeDetails";
			 		document.forms[0].submit(); 
			   	});
			   
			   <%--  $("#deptId").change(function(){
			    $("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/ajax-loader.gif" /> <br /> Retrieving your data. Please wait...');
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
				}); --%>
				
			    
			    $("#designationId").change(function(){
			    $("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/ajax-loader.gif" /> <br /> Retrieving your data. Please wait...');
			    $("#employeeId").select2('destroy'); 
					var data = {
						mode : "AjaxAction",
						deptId : $("#deptId").val(),
						designationId : $("#designationId").val(),
						getType : "getEmployeesList"
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
			
			$("#LOADINGPAGEGIF").html('<img src="<%=basePath%>images/ajax-loader.gif" /> <br /> Retrieving your data. Please wait...');
			
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
				
			  if(confirm("Do you want to save employee details?"))
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
