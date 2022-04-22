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

<link rel="shortcut icon" href="./images/aplogo.ico">
<!-- <link rel="shortcut icon" href="assets/images/favicon.ico"> -->

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



<!-- <link href="assets/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css" />
<link href="assets/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css" /> -->
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="https://cdn.datatables.net/1.10.25/css/dataTables.bootstrap.min.css"
	rel="stylesheet" type="text/css" />



<script src="js/disable_f5.js"></script>
<link href="https://apbudget.apcfss.in/css/select2.css" rel="stylesheet"
	type="text/css" />
<style>
.header-title {
	color: #108688;
	font-size: 19px;
}

body {
	background: #f5f5f5;
}

.top_menubar1 {
	background-image: linear-gradient(to left, rgba(67, 93, 97, 0.52),
		rgba(97, 126, 131, 0.73) ), url('../images/slider.jpg');
	background-repeat: no-repeat;
	background-size: cover;
	background-position: center;
	background-attachment: fixed;
}

.menu_topbar1 {
	background: #425c60;
}

.title h4 {
	font-size: 20px;
	margin-top: 25px;
	margin-left: -10px;
	color: #ffffff;
}

.title h4 span {
	margin-top: 5px;
	font-size: 16px;
}

.footer1 {
	background: #425c60;
}

.footer1 {
	border-top: 1px solid rgba(0, 0, 0, 0.1);
	bottom: 0px;
	color: #58666e;
	text-align: left !important;
	/* padding: 20px 0px; */
	position: absolute;
	right: 0px;
	left: 0px;
}

.bg-white {
	background: #fff;
	min-height: 700px;
}

#topnav .navigation-menu>li a:hover {
	background: #425c60;
	color: #fff;
	text-decoration: none;
}

#topnav .navigation-menu>li .submenu li a:hover {
	background: #425c60;
	color: #fff;
	text-decoration: none;
}

#topnav .navigation-menu li a hover {
	color: #fff;
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
		<img src='./images/gears.gif' /> <br />
		Retrieving your data. Please wait...
	</div>
	<div class="wrapper">

		<header id="topnav">
			<tiles:insert attribute="Header" name="Header" />

			<!-- end topbar-main -->
			<tiles:insert attribute="Menu" name="Menu" />
			<!-- end navbar-custom -->
		</header>

		<!-- end page title end breadcrumb -->
		<br />
		<div class="container-fluid">
			<tiles:insert attribute="content" name="content" />
		</div>

		<!-- Footer -->

		<tiles:insert attribute="footer" name="footer" />

		<!-- End Footer -->

		<!-- end container -->
	</div>
	<!-- end wrapper -->

	<script>
		var resizefunc = [];
	</script>
	<!-- jQuery  -->

	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/jquery.blockUI.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.25/js/dataTables.bootstrap.min.js"></script>



	<!-- App js -->
	<script type="text/javascript">
		setTimeout(function() {
			$("#LOADINGPAGEGIF").html("");
		}, 900);

		if ($('#example')) {
			$('#example').DataTable();
		}
	</script>

</body>
</html>