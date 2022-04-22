<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>

<link rel="shortcut icon" href="assets/images/favicon.ico">

<title>APOLCMS</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" src="js/md5.js" type="text/javascript"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link rel="stylesheet" href="assets/css/font-awesome.min.css">
<link href="assets/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="assets/css/core.css" rel="stylesheet" type="text/css" />
<link href="assets/css/components.css" rel="stylesheet" type="text/css" />
<link href="assets/css/icons.css" rel="stylesheet" type="text/css" />
<link href="assets/css/pages.css" rel="stylesheet" type="text/css" />
<link href="assets/css/menu.css" rel="stylesheet" type="text/css" />
<link href="assets/css/responsive.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />

<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />

<script src="js/disable_f5.js"></script>
<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />
<style>

body {
	background: #f5f5f5;
}



.bg-white {
	background: #fff;
	min-height: 700px;
}

</style>
<style type="text/css">
.mydiv {
	position: fixed;
	text-align: center;
	top: 50%;
	left: 50%;
	margin-top: -9em;
	margin-left: -6em;
	/* border: 1px solid #ccc; */
	background: transparent;
	z-index: 999999;
	display: inline-block;
}
</style>
</head>
<body>
	<div id="LOADINGPAGEGIF" class="mydiv">
		<img src='./images/gears.gif' /> <br /> Retrieving your data. Please
		wait...
	</div>
		<div class="container-fluid">
			<tiles:insert attribute="content" name="content" />
		</div>
		<!-- end container -->
	<!-- end wrapper -->



	<script src="assets/js/bootstrap.min.js"></script>

	<!-- App js -->
	<script type="text/javascript">
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);

		if ($('#example')) {
			//$('#example').DataTable();
		}
	</script>
</body>
</html>