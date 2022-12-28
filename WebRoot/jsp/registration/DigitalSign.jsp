<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@page import="cfss.gov.login.SessionDetails"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="javax.smartcardio.CardException"%>
<%@ page import="javax.smartcardio.CardTerminal"%>
<%@ page import="javax.smartcardio.TerminalFactory"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Random rand = new Random(new Random().nextLong());   
String exdata = String.valueOf(rand.nextLong());
%>
<%!  
public String detectCradTypeAndGetName() 
{
String tokenName = null;
try 
{
TerminalFactory factory = TerminalFactory.getDefault();
List<CardTerminal> terminals = factory.terminals().list();
for (CardTerminal terminal : terminals) 
{
if (terminal.isCardPresent()) 
{
String name = terminal.getName();
tokenName = name.substring(0, name.indexOf(" "));
tokenName = tokenName.trim();
}
}
} 
catch (CardException ex) 
{
ex.printStackTrace();
tokenName=null;
}
return tokenName;
}
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <!--  dsk js file -->
 <script type="text/javascript" src="<%=basePath%>js/ds2min.js"></script>
  <!--  dsk js file -->
    <base href="<%=basePath%>">
    
    <title>My JSP 'AffiliationOrder.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style>
.ui-datepicker-title select{color:#000!important;}

</style>

	<script type="text/javascript">
	/*DIGI SIGN*/
        	var subjectName = "";
        	var issuerName = "";
        	var resellerCode = 75;
        	var strHash;
        	var signerName;
        	var offset;
        	var signerCert;
        	var outfile;
        	var res;
        	var myObj = _elk_desksignObj;
        	var filecontent="";
        	var pdf_data = "JVBERY=";
        		function signmem()
        	{
        		myObj.ConfigSigBlock(256,20,30,1,0, "Digitally signed by",120,120);
        		myObj.SignDataInMemoryEx("<%=exdata%>", "", true, true, 0, subjectName, issuerName, "", "", false, 0, resellerCode);
        		
        	}
        		function preInvoke(signingfn,callback)
        	{
			//alert("hloooooooooooooooooooooo");
        	if(!myObj._elk_initialized)
        	{
        	myObj._elk_initialize(callback);
        	window.setTimeout(signingfn, 200);
        	}
        	else
        	signingfn();
        	}

        	function mycallback(apiName, status, params)
        	{
        	
        	if(status != "Success")
        	{
        	alert("Certificate operation failed: " + status);
        	document.getElementById("sign_loading").innerHTML="";
        	return false;
        	}
        	if(apiName == "SignDataInMemoryEx")
        	{
        	document.getElementById("sign_loading").innerHTML="<i class='fa fa-spinner fa-spin' ></i> Preparing to Sign......";

        	  if(typeof params.sig == "undefined" || params.sig == "")
        	{retVal = "";
        	}
        	else{
        	if(apiName == "SignDataInMemoryEx")
        	{
        	retVal = params.sig;
        	var signValue = params.sig;
        	var fileURL =document.getElementById("urls").value;
        	var token_no=document.getElementById("sign_sno").value;
        	var oParameters = { "sig" :signValue,"MyReason":"Remarks :  ", "MyLocation":"","SigBlockTitle":"", "PageNumber":"-1", "SigPosition":"4", "filetosign" :fileURL,"searchText":"Transport Commissioner","coordinatex":"1","coordinatey":"1","project":"jnanabhumi","pkey":"Jnb@123","token_name":"FT","token_sno":token_no};


        	//var x="http://10.10.0.248:8080/digisigning/services/Sign/getCert";
        	var x="https://digisign.apcfss.in/services/Sign/getCert";
        	 $.ajax({url:x,data:oParameters,type:"POST", success: function(result){
        	      console.log(result);
        	     
        	     
        	    var html = result;
        	   
        	      var res1 =html.split("*~*");
        	     
        	       strHash = res1[1] ;
        	      signerName = res1[2] ;
        	       var signerSubjectDN = signerName;
        	      offset=res1[3] ;
        	      signerCert = res1[4] ;
        	      outfile = res1[5] ;
        	      res=res1[0];
        	       
        	 
        	     if(res==1)
        	      myObj.SignEncodedDataInBatch(strHash,1, 0, 0, signerSubjectDN, "", "", "", 1, 0, resellerCode);
        	      else
        	      alert("Status:"+res1[1]);
        	}});
        	}
        	}

        	}
        	else if(apiName == "SignEncodedDataInBatch")
        	{  

        	var signValue = params.sig;
        	var signAPIUrl="https://digisign.apcfss.in/services/Sign/signHash";
        	//var signAPIUrl="http://10.10.0.248:8080/digisigning/services/Sign/signHash";

        	var oParameters = { "signature" :signValue, "offset" :offset, "hash" : strHash, "signerCert":signerCert,"outfile":outfile,"project":"jnanabhumi","pkey":"Jnb@123","res":res};
        	$.ajax({url: signAPIUrl,data:oParameters, type:"POST", success: function(result){
        	      console.log(result);
        	      var html = result;
        	   document.getElementById("sign_loading").innerHTML="";
        	      var res1 =html.split("*~*");
        	     
        	      var signfilecontent = res1[1];
        	       var srcURL = "data:application/pdf;base64," + signfilecontent;
        	       if(res1[0]==1)
        	{
        	sweetAlert("Affiliation Order, Signed Successfully");
        	document.getElementById("signed_file").value=signfilecontent;
        	document.forms[0].key.value = "saveDSK";
        	document.forms[0].submit();
        	}
        	else
        	{
        	sweetAlert("Failed To Sign. Please Try Again");
        	//document.forms[0].mode.value = "signProceeding";
        	//document.forms[0].submit();
        	}
        	/* document.getElementById("idPdfView").innerHTML=("<iframe src='" + srcURL + "' width = '100%' height = '500px''></iframe>"); */
        	   }});
        	   document.getElementById("sign_loading").innerHTML="";  
        	}
        	}

function mycallback2(apiName, status, params)
{

	if(apiName != "SignDataInMemoryEx")
		return; 
	if(status != "Success")
	{
		alert("Certificate operation failed: " + status);
	}
	else
	{
		if(apiName == "SignDataInMemoryEx")
		{
		document.getElementById("sign_loading").innerHTML="<i class='fa fa-spinner fa-spin' ></i> Registering Signature......";
		var sigholder = params.sig;
			var x="https://digisign.apcfss.in/services/Sign/doRegister";
			var oParameters = { "sig" :sigholder,"tbsdata":<%=exdata%>,"project":"jnanabhumi","pkey":"Jnb@123","token_name":"FT"};
		
		$.ajax({url:x,data:oParameters,type:"POST", success: function(result){
				       console.log(result);
				       var html = result;
				       var res1 =html.split("*~*");
				       if(res1[0]==1)
				       {
				        document.getElementById("sign_sno").value=res1[1];
				        preInvoke(signmem,mycallback);
				       }
				      document.getElementById("sign_loading").innerHTML="";
				       }});
		}
	}
	
} 
     /*DIGI SIGN END*/
	</script>
	<script type="text/javascript">
	
	function getdata()
	{
	
	if(document.getElementById("university_ec_doc").value=="")
		{
			document.getElementById("university_ec_doc").focus();
			sweetAlert("Please Upload University EC Resolution Document ");
			return false;
		}
		if(document.getElementById("gort_no").value=="")
		{
			document.getElementById("gort_no").focus();
			sweetAlert("Please Enter Reference Number of the Proceedings of AICTE/APSCHE/PCI/BCI/NCTE that stands as reference in the Affiliation Order 2022-23 at S.no. 1 ");
			return false;
		}
		if(document.getElementById("gort_date").value=="")
		{
			document.getElementById("gort_date").focus();
			sweetAlert("Please Enter Date of Proceedings of AICTE/APSCHE/PCI/BCI/NCTE that stands as reference in the Affiliation Order 2022-23  at S.no. 1 ");
			return false;
		}
		if(document.getElementById("accd_by").value=="" )
		{
			document.getElementById("accd_by").focus();
			sweetAlert("Please Enter Competent Authority that has given Permission ");
			return false;
		}
		
		if(document.getElementById("auth_by").value=="" || document.getElementById("auth_by").value=="0")
		{
			document.getElementById("auth_by").focus();
			sweetAlert("Please Select Authority By ");
			return false;
		}
		
		if(document.getElementById("affl_order_2021_22_date").value=="" )
		{
			document.getElementById("affl_order_2021_22_date").focus();
			sweetAlert("Please Enter Date of Affiliation Order 2021-22");
			return false;
		}
		
		if(document.getElementById("affl_order_proc_no_2021_22").value=="" )
		{
			document.getElementById("affl_order_proc_no_2021_22").focus();
			sweetAlert("Please Enter Proceeding No. of Affiliation Order 2021-22");
			return false;
		}
		
		if(document.getElementById("affl_order_2022_23_date-12").value=="" )
		{
			document.getElementById("affl_order_2022_23_date-12").focus();
			sweetAlert("Please Enter Date of Affiliation Order 2022-23");
			return false;
		}
		
		if(document.getElementById("affl_order_proc_no_2022_23").value=="" )
		{
			document.getElementById("affl_order_proc_no_2022_23").focus();
			sweetAlert("Please Enter Proceeding No. of Affiliation Order 2022-23");
			return false;
		}
		/* if(document.getElementById("auth_name").value=="")
		{
			sweetAlert("Please Enter Authority Name ");
			document.getElementById("auth_name").focus();
			return false;
		} */
		
		/* if(document.getElementById("remarks").value=="")
		{
			sweetAlert("Please Enter Remarks ");
			document.getElementById("remarks").focus();
			return false;
		} */
	
		document.getElementById("aff_data").style.display="block";
		document.getElementById("main_data").style.display="none";
		 
		document.getElementById("procd_no_div").innerHTML =document.getElementById("gort_no").value;
		document.getElementById("procd_date_div").innerHTML =document.getElementById("gort_date").value;
		document.getElementById("accd_by_div").innerHTML =document.getElementById("accd_by").value;
		document.getElementById("auth_by_div").innerHTML =document.getElementById("auth_by").value;
		document.getElementById("auth_by_div2").innerHTML =document.getElementById("auth_by").value;
		document.getElementById("affl_order_proc_no_2021_22_div").innerHTML =document.getElementById("affl_order_proc_no_2021_22").value;
		
		if(document.getElementById("remarks").value!="")
		{
			document.getElementById("remarks_div").innerHTML =document.getElementById("remarks").value;
		}
		
		
	}
	
	function getpdf()
	{
		document.forms[0].key.value = "sign_pdf";
		document.forms[0].submit();
	}
	
	function Back()
	{
		document.forms[0].key.value = "unspecified";
		document.forms[0].submit();
	}
	function Back2(a,b)
	{
		document.getElementById("ub_code").value=a;
		document.getElementById("coll_code").value=b;
		document.forms[0].key.value = "AffiliationOrderData";
		document.forms[0].submit();
	}
	
	$(function() {
              $( "#affl_order_2022_23_date-12" ).datepicker({
             dateFormat : "dd/mm/yy",
			yearRange : '2022:+0',
			changeMonth: true,
			changeYear: true,
			showOn: "focus",
			 inline: true,
			minDate:0,
			maxDate:0
            });
           });
	
	$(function() {
              $( "#affl_order_2021_22_date-12" ).datepicker({
             dateFormat : "dd/mm/yy",
			yearRange : '2021:+0',
			changeMonth: true,
			changeYear: true,
			showOn: "focus",
			 inline: true,
		//	minDate:0
            });
           });
           
   $(function() {
              $( "#affl_order_2020_21_date-12" ).datepicker({
             dateFormat : "dd/mm/yy",
			yearRange : '2020:+0',
			changeMonth: true,
			changeYear: true,
			showOn: "focus",
			 inline: true,
		//	minDate:0
            });
           });
           
   $(function() {
              $( "#procd_date-12" ).datepicker({
             dateFormat : "dd/mm/yy",
			yearRange : '2021:+0',
			changeMonth: true,
			changeYear: true,
			showOn: "focus",
			 inline: true,
		//	minDate:0
            });
           });
           
           $(function() {
            $(".datepicker").datepicker({
             dateFormat : "dd/mm/yy",
			yearRange : '2021:+0',  
			changeYear: true,
			changeMonth: true,
			showOn: "focus",
			 inline: true,
		//	maxDate:0
            
            });
          
         }); 
         
         
         function isAlphaKeyaddress(i) {
	if((i.value).length>0) {
		i.value = i.value.replace(/[^\d\d.\w\s\-\/\,]+/g, '');
	}
	var str=i.value;
	}
                           
	</script>
	
	<script>
var dt = new Date();
document.getElementById('date-time').innerHTML=dt;
</script>

<script>
const d = new Date();
document.getElementById("demo").innerHTML = d;
</script>
	
  </head>
  
  <body>
     <html:form action="/Affl_OrderGen2022" method="post"
		enctype="multipart/form-data" styleClass="form-horizontal">
		
		<input type="hidden" name="key" id="key" >
		<html:hidden property="beanProperties(urls)" styleId="urls"  ></html:hidden>
 		 <html:hidden property="beanProperties(sign_sno)" styleId="sign_sno"  />
 	  	<html:hidden property="beanProperties(signed_file)" styleId="signed_file"  />
		<html:hidden property="beanProperties(coll_code)" styleId="coll_code"  value="${coll_code} " />
		<html:hidden property="beanProperties(ub_code)" styleId="ub_code"  value="${ub_code} " />
		<input type="hidden" name="ub_code" id="ub_code" value="${ub_code} " >
		<%-- <input type="hidden" name="coll_code" id="coll_code" value="${coll_code} "> --%>
		
		<div class="container-fluid" >
			<div><span id="sign_loading" style='font-size: 18px;'></span></div>   
		
		<!-- <center><span class="blink_me"><b><font color="red" "  size="5"> Use Internet Explorer  with DSK for  Affiliation Order </b></font></span></center> -->
		 <logic:present name="success"><table align="center" style="border-style: solid;border-color:#00C851;;border-width: 1px;border-radius:5px; box-shadow:0 10px 20px rgba(0,0,0,0.1);"><tr><td style="background-color: #00C851;;font-size: 20px;color:white;font-weight: normal;"> &nbsp;<i class="fa fa-check" aria-hidden="true"></i>&nbsp;&nbsp;</td><td style="color:#00C851;background-color:white;">&nbsp;${success} &nbsp;  </td> </tr></table><br></logic:present>
		 <logic:present name="failure"><table align="center" style="border-style: solid;border-color:#F70D1A;border-width: 1px;border-radius:5px; box-shadow: 4px 4px 0px #888888;"><tr><td style="background-color: #F70D1A;font-size: 20px;color:white;font-weight: normal;"> &nbsp; <i class="fa fa-exclamation-triangle" aria-hidden="true"  ></i>&nbsp;&nbsp;</td><td style="color:#F70D1A;text-shadow:1px 0px 0px red;">&nbsp;${failure}&nbsp;</td></table> <br></logic:present>
		
		<div id="main_data"> 
		<%-- 		
				 
		<logic:notEmpty name="lat_eop_order_pdf">
		<a href="<%=basePath%>${lat_eop_order_pdf}" target="_new">Latest EOA/EOP (AICTE/APSCHE/BCI/PCI) PDF</a>
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<html:hidden property="beanProperties(lat_eop_order_pdf11)" value="${lat_eop_order_pdf}"
		styleId="lat_eop_order_pdf11" /> 
		</button>
		</logic:notEmpty>
		
		<logic:notEmpty name="final_pdf_path">
		<button type="button" class="btn btn-warning" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="<%=basePath%>${final_pdf_path}" target="_new">Affiliation Order 2021-22</a>
		<html:hidden property="beanProperties(final_pdf_path11)" value="${final_pdf_path}"
		styleId="final_pdf_path11" /> 
		</button>
		</logic:notEmpty>
		
		<logic:notEmpty name="proceeding_upload_university">
		<button type="button" class="btn btn-danger" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="<%=basePath%>${proceeding_upload_university}" target="_new">FFCA Proceedings</a>
		<html:hidden property="beanProperties(proceeding_upload_university11)" value="${proceeding_upload_university}"
		styleId="proceeding_upload_university11" /> 
		</button>
		</logic:notEmpty>
		
		<logic:notEmpty name="upload_pdf">
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="<%=basePath%>${upload_pdf}" target="_new">FFCA Undertaking</a>
		<html:hidden property="beanProperties(upload_pdf11)" value="${upload_pdf}"
		styleId="upload_pdf11" /> 
		</button>
		</logic:notEmpty>
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="AfflRegLoginDoc.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">View Documents</a> 
		</button> --%>
		
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="FFCAAffiliationPrint2022.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">FFCA Report</a> 
		</button>
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="FfcaEmployeeDetails_21.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">Faculty Report</a> 
		</button>
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="Affiliation_Documents.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">Affiliation Documents</a> 
		</button>
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="FfcaCollegeCourseDetails2021.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">Application Category</a> 
		</button>
		
		<button type="button" class="btn btn-success" style="color: #ffffff; ">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="AffiliationPrint2122.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">Report</a> 
		</button>
		
		<button type="button" class="btn btn-danger">
		<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		<a href="AffiliationPrint.edu?key=unspecified&ac_year=2022-23&coll_code=${coll_code}" target="_blank" style="color:#fff;">Application Submitted by College</a> 
		</button>
		
		<!-- <button type="button" class="btn btn-danger"> -->
		
		<logic:notEmpty name="lat_eop_order_pdf">
		<a href="<%=basePath%>${lat_eop_order_pdf}" target="_new" class="btn btn-warning btn-sm"><i class="fa fa-file-pdf-o" aria-hidden="true"></i>EOP/EOA Order</a>
		</logic:notEmpty>
		
		<logic:notEmpty name="manual_affiliation_copy_pdf">
		<a href="<%=basePath%>${manual_affiliation_copy_pdf}" target="_new" class="btn btn-warning btn-sm"><i class="fa fa-file-pdf-o" aria-hidden="true"></i>Permission proceedings of APSCHE/ University</a>
		</logic:notEmpty>
		
		
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
		<h1>
		<span class="headline"> Affiliation Order Details 2022-23</span>
		</h1>
		</div>
		</div>
		<br/><br/>	
			
			 
  <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
 <div class="form-group">
<label class="control-label col-sm-4">Upload University EC Resolution Document: <font color="red">*</font></label>
<div class="col-sm-2">
<html:file property="beanProperties(university_ec_doc)" styleClass="form-control" styleId="university_ec_doc" accept=".pdf"  />
<logic:notEmpty name="university_ec_doc">
<a href="<%=basePath%>${university_ec_doc}" target="_new" class="btn btn-warning btn-sm pull-right">View</a>
</logic:notEmpty>
<html:hidden property="beanProperties(university_ec_doc11)" styleId="university_ec_doc11" value="${university_ec_doc}" /> 
</div> 
</div>
  
  
			
			 
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Reference Number of the Proceedings of AICTE/APSCHE/PCI/BCI/NCTE that stands as reference in the Affiliation Order 2022-23 : <font color="red">*</font> </label>
				<div class="col-sm-2">
				<label class="control-label col-sm-2">1.</label>
			  		<html:text property="beanProperties(gort_no)"   styleId="gort_no" style="width:180px;"  
			  		styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>&nbsp;&nbsp;
			  	</div>
			  	
			  	<label class="control-label col-sm-4">  Date of Proceedings of AICTE/APSCHE/PCI/BCI/NCTE that stands as reference in the 
			  	Affiliation Order 2022-23 : <font color="red">*</font> 1.</label>
				<div class="col-sm-2">
				
			  	<html:text property="beanProperties(gort_date)"   styleId="gort_date" styleClass="form-control datepicker"  readonly="true" maxlength="10" style="width:200px;" ></html:text>
				</div>
			  	
			</div>
			
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Reference Number of the Proceedings/Order/Letter/Report :  </label>
				<div class="col-sm-2">
				<label class="control-label col-sm-2">2.</label>
			  		<html:text property="beanProperties(gort_no2)"   styleId="gort_no2" style="width:180px;"  
			  		styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>&nbsp;&nbsp;
			  	</div>
			  	
			  	<label class="control-label col-sm-4">  Date of Reference Number of the Proceedings/Order/Letter/Report : 2. </label>
				<div class="col-sm-2">
				
			  		<html:text property="beanProperties(gort_date2)"   styleId="gort_date2" styleClass="form-control datepicker"  readonly="true" maxlength="10" style="width:200px;" ></html:text>
				</div>
			  	
			</div>
			
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Reference Number of the Proceedings/Order/Letter/Report :  </label>
				<div class="col-sm-2">
				<label class="control-label col-sm-2">3.</label>
			  		<html:text property="beanProperties(gort_no3)"   styleId="gort_no3" style="width:180px;"  
			  		 styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>&nbsp;&nbsp;
			  	</div>
			  	
			  	<label class="control-label col-sm-4">  Date of Reference Number of the Proceedings/Order/Letter/Report : 3. </label>
				<div class="col-sm-2">
			  	<html:text property="beanProperties(gort_date3)"   styleId="gort_date3" styleClass="form-control datepicker"  readonly="true" maxlength="10" style="width:200px;" ></html:text>
				</div>
			  	
			</div>
			
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Reference Number of the Proceedings/Order/Letter/Report :  </label>
				<div class="col-sm-2">
				<label class="control-label col-sm-2">4.</label>
			  		<html:text property="beanProperties(gort_no4)"   styleId="gort_no4" style="width:180px;"  
			  		styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>&nbsp;&nbsp;
			  	</div>
			  	
			  	<label class="control-label col-sm-4">  Date of Reference Number of the Proceedings/Order/Letter/Report : 4. </label>
				<div class="col-sm-2">
				 
			  		<html:text property="beanProperties(gort_date4)"   styleId="gort_date4" styleClass="form-control datepicker"  readonly="true" maxlength="10" style="width:200px;" ></html:text>
				</div>
			  	 
			</div>
			
			 
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Competent Authority that has given Permission (EOA/EOP/Any Other Permission Proceedings that is pre-requisite for Granting Affiliation): <font color="red">*</font> </label>
				<div class="col-sm-2">
				<html:text property="beanProperties(accd_by)"   styleId="accd_by" style="width:200px;" title="AICTE/APSCHE/PCI/BCI"  
				styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>
			  		 
				</div>
			</div>
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Affiliation Order 2022-23 being approved by: <font color="red">*</font> </label>
				<div class="col-sm-2">
		
			  		<html:select property="beanProperties(auth_by)" styleId="auth_by" style="width:200px;" styleClass="form-control">
				 		<html:option value="REGISTRAR">REGISTRAR</html:option>
				 		
				 		<%-- <html:option value="0">Select Authority By</html:option> --%>
				 		<%-- <html:option value="VICE-CHANCELLOR">VICE-CHANCELLOR</html:option> --%>
				 		
					 </html:select>
				</div>
			</div>
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Date of Affiliation Order 2021-22: <font color="red">*</font> </label>
				<div class="col-sm-2">
			  	<html:text property="beanProperties(affl_order_2021_22_date)" styleId="affl_order_2021_22_date" 
			  	styleClass="form-control datepicker"  readonly="true" maxlength="10" title="DD/MM/YYYY"  style="width:200px;" ></html:text>
			  	
			  <%-- 	value="${coll_details.gort_date}"  --%>
			  	
				</div>
			</div>
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Proceedings Number of Affiliation Order 2021-22: <font color="red">*</font> </label>
				<div class="col-sm-2">
			  		<html:text property="beanProperties(affl_order_proc_no_2021_22)" styleId="affl_order_proc_no_2021_22" style="width:200px;"  
			  		 styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>
			  		 
			  		 <%-- value="${coll_details.gort_no}" --%>
			  		 
				</div>
			</div>
			
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Date of Affiliation Order 2022-23: <font color="red">*</font> </label>
				<div class="col-sm-2">
			  		<html:text property="beanProperties(affl_order_2022_23_date)" styleId="affl_order_2022_23_date-12" 
			  		styleClass="form-control" readonly="true" maxlength="10" title="DD/MM/YYYY"  style="width:200px;" ></html:text>
				</div>
			</div>
			
			<div class="form-group">
							
				<label class="control-label col-sm-4">  Proceedings Number of Affiliation Order 2022-23: <font color="red">*</font> </label>
				<div class="col-sm-2">
		
			  		<html:text property="beanProperties(affl_order_proc_no_2022_23)"   styleId="affl_order_proc_no_2022_23" style="width:200px;"   
			  		styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:text>
			  	
				</div>
			</div>
			
			<div class="form-group">
							
				<label class="control-label col-sm-4"> Remarks:   </label>
				<div class="col-sm-2">
		
			  		<html:textarea property="beanProperties(remarks)"   styleId="remarks" style="width:500px;height:200px"  
			  		styleClass="form-control" onkeyup="isAlphaKeyaddress(this)"></html:textarea>
				</div>
			</div>
			</div>
			
			
			<div class="form-group">
				<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5" align="right">
				<html:button   styleClass="btn btn-warning "  property="test1" styleId="test1"  value="Back" onclick="return Back();"></html:button>
				&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp; 
				<html:button property="myBtn" styleClass="btn btn-success pull-right" 	value="View Data to Proceed"   onclick="return getdata();"></html:button>
				 </div>
				 
			</div>
			</div>
		<br/><br/><br/>
		
		<div id="aff_data" style="display: none">
		
		 <logic:present name="univ_det">
	
	  		<logic:notEmpty name="univ_det">
	 
	 
	
	<p style="text-align:center;">View Sample PDF</p>
	<div style="border:2px solid red;padding-bottom: 50px;padding-top: 20px" class="container">

   <div style="text-align:center;">
   	<p> PROCEEDING OF THE <span id="auth_by_div"></span> 
   	<br/>
   	<span style="color:black; "><b>${univ_det.ub_name}</b></span> 
   	<br/>
   	<span style="color:black">${univ_det.u_address}</span> 
   	<br/>
   	</p> 
      
   </div>
  <!--  <div style="text-align:center;">
   	<p><b> PRESENT : <span id="auth_name_div"></span></b><br/> <span id=" "></span> </p> 
      
   </div> -->
   <p style="float:right;padding-right: 50px"><b>Date : <span id="procd_date_div"></span>(MM/DD/YYYY) </b></p>
   
   
   <br/><br/><br/><br/>
    <p style="text-align: justify;text-indent: -59px;margin-left: 100px; ">Procgs. No. :  
    <b> <span id="affl_order_proc_no_2021_22_div"></span></b> </p>
     
     
   <p style="text-align: justify;text-indent: -59px;margin-left: 100px; ">
   Sub: <span style="">
   <b>${univ_det.ub_name}</b> - Affiliation accorded for the academic year  2022-23 to 
   <b>${coll_details.inst_name}</b> ,( ${coll_details.inst_address} ) 
   <br/> under <b>${coll_details.society_name}</b> , ( ${coll_details.soc_address} )  - Orders - issued - regarding.
   </span>
   </p>
    
    <%-- <p style="text-align: justify;text-indent: -59px;margin-left: 100px; ">Ref : &nbsp;&nbsp; &nbsp;  &nbsp; 
    1. ${coll_details.lat_eoa_ref_no} , Dated  ${coll_details.lat_eop_date} , <br>
   
    <logic:notEmpty name="coll_details" property="lat_go_ref_no" >
    2. ${coll_details.lat_go_ref_no} , Dated ${coll_details.lat_go_date} , <br/>
    3. ${coll_details.lat_affl_ord_ref_no} , Dated ${coll_details.ffca_latest_affl_order} ,<br/>
    4. FFC Report Dated ${coll_details.date_of_insp}  <br/>
    </logic:notEmpty>
    
    <logic:empty name="coll_details" property="lat_go_ref_no">
    2. ${coll_details.lat_affl_ord_ref_no} , Dated ${coll_details.ffca_latest_affl_order} ,<br/>
    3. FFC Report Dated ${coll_details.date_of_insp}  <br/>
    </logic:empty>
   
    
    </p> --%>
    
   <p style="text-align:center;">***</p>
   <br/>
   
   
   <p style="text-align: justify;text-indent: -59px;margin-left: 100px; "><b>ORDER :- </b></p>
   
    <p style="text-align:justify;text-indent: 49px;margin-left: 40px;width: 90%"> 
    
      <b>${coll_details.society_name}</b> , ( ${coll_details.society_addr} ) , has applied for the affiliation of  <b>${coll_details.inst_name}</b> for the academic year 2022-23  to <b>${univ_det.ub_name}</b> to offer its programs in the said College.
   </p>
 
   <p style="text-align:justify;text-indent: 49px;margin-left: 40px;width: 90%"> 
      Considering the approval accorded by <b> <span id="accd_by_div"></span>  </b>   and after careful examination of the facts of the FFC Report ,
       the <span id="auth_by_div2"></span> , <b>${univ_det.ub_name}</b> , ( ${univ_det.u_address} ) ,
       <%-- for the academic year 2021-22 to <b>${univ_det.ub_name}</b> to offer its programs in the said College. --%>
       accords approval, in exercise of the powers conferred under the University Act and subject to ratification of the Executive Council, for provisional / temporary affiliation to
      <b>${coll_details.inst_name}</b> , ( ${coll_details.inst_address} )  under <b>${coll_details.society_name}</b> , ( ${coll_details.society_addr} ),  as per the details given below. 
      
   </p>
     
   <br/>
   <table class="table table-condensed table-bordered table-striped" id="otehr_optional"  style="width: 90%;margin-left: 40px;" >
		<thead class="thead-inverse">
		
		<tr>
		
		 <th style="width: 150px;" align="left"> Name of the College</th>
		<td> ${coll_details.inst_name }</td>
		
		<th >Name of the Society/Trust</th>
		<td>${coll_details.society_name}</td>
		</tr>
		
		<tr>
		<th>Address of the college</th>
		<td> ${coll_details.inst_address}</td>
		
		<th>Address of the Society/Trust</th>
		<td> ${coll_details.society_addr}</td>
		
		</tr>
		
		<tr>
		<th>Year of initial establishment of the College</th>
		<td> ${coll_details.inst_estd}</td>
		
		<th>Status of accommodation</th> 
		<td> ${coll_details.accomodation}</td>
		</tr>
		
		</thead>
		</table>
   
   <br/>
   <p style="text-align: justify;text-indent: -59px;margin-left: 100px; "><span id="remarks_div"></span> </p>
       
   <p style="text-align:justify;text-indent: 49px;margin-left: 40px;width: 90%">
   The above provisional / temporary affiliation is granted subject to fulfillment of deficiencies, if any as furnished hereunder, within three months from the date of issuance of this Order.
   </p>
   
   <br/>
   <logic:notEmpty name="def_details">
   <table class="table table-condensed table-bordered table-striped" style="width: 90%;margin-left: 40px;">
		<thead class="thead-inverse">
			<tr style="background-color: #83cce6;">
			<th style="width: 100px;">Sl.No.</th>
			<th>Content of the deficiency</th>
			<th>Last Date for Rectification of Deficiencies</th>
			</tr>
			</thead>
			<logic:iterate name="def_details" id="def_details" indexId="i">
			<tbody>
			<tr>
			<td>${i+1}</td>
			<td>${def_details.description}</td>
			<td>${def_details.last_date}</td>
			</tr>
			</tbody>
			</logic:iterate>
	</table>
   </logic:notEmpty>
   
   <logic:notEmpty name="crse_details">
   <table id="myTable" class="table table-condensed table-bordered table-striped " style="width: 90%;margin-left: 40px;">
         <thead>
            <tr style="background-color: #83cce6;">
               <th style="width:10px">Course/Program</th>
               <th style="width:10px">Branch /Combination </th>
               <th style="width:10px">Medium</th>
               <th style="width:10px">Level(UG/PG)</th>
               <th style="width:10px">Nature of Affiliation being Accorded</th>
               <th style="width:10px">Affiliation Valid Upto</th>
               <th style="width:10px">Intake</th> 
               <th style="width:10px">Deficiencies(If any)</th>
               
            </tr>
         </thead>
         
         <logic:iterate id="crse_details" name="crse_details" indexId="i">
            <tr>
              <td>  ${crse_details.course_cat_name} </td>
              <td> ${crse_details.course_name} </td>
              <td> ${crse_details.name} </td>
              <td> ${crse_details.graduation_level} </td>
              <td > ${crse_details.affl_nature} </td>
              <td > ${crse_details.affl_valid_upto} </td>
               <td > ${crse_details.intake_approved_competent_authotity} </td>
               <td > ${crse_details.deficiencies_reason} </td>
               
               </tr>
               </logic:iterate>
               </table>
       </logic:notEmpty>        
   
   
    <%-- <div style="padding-right: 150px">
     <html:submit property="myBtn1" styleClass="btn btn-success pull-center" 	value="Back"   onclick="return Back2('${ub_code}','${coll_code}');"></html:submit>
  </div> --%>
   <div  style="padding-left: 500px">
     <html:submit property="myBtn1" styleClass="btn btn-success pull-center" 	value="Back"   onclick="return Back2('${ub_code}','${coll_code}');"></html:submit>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       
          <html:submit property="myBtn" styleClass="btn btn-success pull-center" 	value="Generate Pdf"   onclick="return getpdf();"></html:submit>
      </div>
		
   
   </div>
   </logic:notEmpty> 
   </logic:present>
		
		</div>
		</div>
		<logic:present name="pdf_success">
			<script>  
  				preInvoke(signmem,mycallback2);
  			</script>
		</logic:present>
		</html:form>
  </body>
  
   <!-- <script type="text/javascript">
		$('.datepicker').datepicker
		({
			format: 'dd/mm/yyyy',
		    endDate: '+0d',
		    autoclose: true,
		    Clear: true,
		  
		});
		</script>  -->
</html>
