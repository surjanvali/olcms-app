<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>
<style>
.btn-success {
	background-color: #40babc !important;
	border: 1px solid #40babc !important;
	border-radius: 0px;
}

.btn-success:hover {
	background-color: #40babc !important;
	border: 1px solid #40babc !important;
	border-radius: 0px;
}
</style>
<div class="content-wrapper">
	<!-- Main content -->
	<div class="content">
		<div class="container bg-white" style="padding-top: 70px;">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 card">
					<html:form method="post" action="/MLOAbstract">

						<html:hidden styleId="mode" property="mode" />
						<html:hidden property="dynaForm(deptId)" styleId="deptId" />

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

							<!-- <div class="card-box"> -->
							<h4 class="m-t-0 header-title">
								<b><logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty> </b>
							</h4>
							<logic:notEmpty name="EMPWISENOS">
								<hr />
								<div class="row">
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
									</div>
								</div>
							</logic:notEmpty>
							<!-- </div> -->
						</div>
					</html:form>
				</div>
			</div>
			<!-- /.row -->
		</div>
		<!-- /.container-fluid -->
	</div>
	<!-- /.content -->
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/js/bootstrap-datepicker.min.js'></script>

<script src="https://apbudget.apcfss.in/js/select2.js"></script>
<script src="<%=basePath%>js/aadharverification.js"></script>

<script type="text/javascript">

			function showOfficerWise(deptId){
				$("#deptId").val(deptId);
				document.forms[0].mode.value="getOfficerWise";
			 	document.forms[0].submit();
			}

			function backFn(){
				document.forms[0].mode.value="unspecified";
			 	document.forms[0].submit(); 
			}

		   $(document).ready(function() {
			    if ($('#example')) {
					$('#example').dataTable({
						"pageLength" : 10,
						"buttons" : [ 'copy', 'excel', 'pdf', 'colvis' ]
					});
			    };
			    
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
