<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
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

.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover
	{
	color: #555;
	cursor: default;
	background-color: #e3f5f5;
	border-bottom: solid 3px #333230;
}

.nav-tabs li a:hover {
	color: #555;
	cursor: pointer;
	background-color: #e3f5f5;
	border-bottom: solid 3px #333230;
}

.tabs-vertical-env .nav.tabs-vertical li>a, .nav.tabs-vertical li>a:hover
	{
	color: #313a46;
	text-align: left;
	border-bottom: 1px solid #333230;
}

.tabs-vertical-env .nav.tabs-vertical {
	background-color: #cdd2d3;
}
}
</style>
<div class="content-wrapper">


	<!-- Main content -->
	<div class="content">
		<div class="container bg-white" style="padding-top: 70px;">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 card">
					<html:form method="post" action="/CaseRegistration">

						<html:hidden styleId="mode" styleClass="form-control"
							property="mode" />
						<html:hidden property="dynaForm(mloId)" styleClass="form-control"
							styleId="mloId" />
						<html:hidden property="dynaForm(deptId)" styleClass="form-control"
							styleId="deptId" />


						<div class="container-fluid">
							<!-- <div class="row">
			            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			                <div class="dashboard-cat-title">
			                    Message
			                </div>
			            </div>
			        </div> -->

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
								<b>Case Registration</b>
							</h4>
							<hr />
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<div class="form-group">
										<label> Case Type </label>
										<html:select styleId="designationId"
											property="dynaForm(designationId)" styleClass="select2Class">
											<html:option value="0">---SELECT---</html:option>
											<logic:notEmpty name="CommonForm"
												property="dynaForm(designationList)">
												<html:optionsCollection name="CommonForm"
													property="dynaForm(designationList)" />
											</logic:notEmpty>
										</html:select>
									</div>
								</div>

								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<div class="form-group">
										<label> Filing No.</label>
										<html:text styleId="mobileNo" styleClass="form-control"
											property="dynaForm(mobileNo)" maxlength="10" size="12"
											onkeypress="return isNumberKey(event)" />
									</div>
								</div>

								<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
									<div class="form-group">
										<label> Year</label>
										<html:text styleId="emailId" styleClass="form-control"
											property="dynaForm(emailId)" />

										<!-- <input type="button" name="Submit" value="Submit"
											class="btn btn-success pull-right" onclick="addEmployee();" /> -->
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<input type="button" name="Submit" value="Submit"
										class="btn btn-success pull-right" onclick="addEmployee();" />
								</div>
							</div>
							<div class="card-box">
								<!-- <h4 class="header-title m-t-0 m-b-30">Tabs Vertical Left</h4> -->

								<div class="tabs-vertical-env">
									<ul class="nav tabs-vertical">
										<li class="active"><a href="#petitioner" role="tab" data-toggle="tab">Petitioner </a></li>
										<li><a href="#respondent" role="tab" data-toggle="tab">Respondent </a></li>
										<li><a href="#extrainformation" role="tab" data-toggle="tab">Extra Information </a></li>
										<li><a href="#subordinatecourt" role="tab" data-toggle="tab">Subordinate Court </a></li>
										<li><a href="#actsection" role="tab" data-toggle="tab">Act Section </a></li>
										<li><a href="#policestation" role="tab" data-toggle="tab">Police Station </a></li>
										<li><a href="#mvc" role="tab" data-toggle="tab">MVC </a> </li>
										<li><a href="#extraparty" role="tab" data-toggle="tab">Extra Party </a></li>
										<li><a href="#applicationfiling" role="tab" data-toggle="tab">Application Filing </a></li>
										<li><a href="#casedetails" role="tab" data-toggle="tab">Case Details </a></li>
										<li><a href="#subjectcategorylabel" role="tab" data-toggle="tab">Subject Category Label </a></li>
										<li><a href="#searchcaveat" role="tab" data-toggle="tab">Search Caveat </a></li>
										<li><a href="#registration" role="tab" data-toggle="tab">Registration </a></li>

									</ul>

									<div class="tab-content">
										<div class="tab-pane active" id="petitioner">petitioner</div>
										<div class="tab-pane" id="respondent">respondent</div>
										<div class="tab-pane" id="extrainformation">extrainformation</div>
										<div class="tab-pane" id="subordinatecourt">subordinatecourt</div>
										<div class="tab-pane" id="actsection">actsection</div>
										<div class="tab-pane" id="policestation">policestation</div>
										<div class="tab-pane" id="mvc">mvc</div>
										<div class="tab-pane" id="extraparty">extraparty</div>
										<div class="tab-pane" id="applicationfiling">applicationfiling</div>
										<div class="tab-pane" id="casedetails">casedetails</div>
										<div class="tab-pane" id="subjectcategorylabel">subjectcategorylabel</div>
										<div class="tab-pane" id="searchcaveat">searchcaveat</div>
										<div class="tab-pane" id="registration">registration</div>

									</div>
								</div>
							</div>


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

<script src="https://apbudget.apcfss.in/js/select2.js"></script>
<script src="<%=basePath%>js/aadharverification.js"></script>

<script type="text/javascript">
			function backFn(){
				document.forms[0].mode.value="unspecified";
			 		document.forms[0].submit(); 
			}

		   $(document).ready(function() {
			    
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
