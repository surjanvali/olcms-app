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
<!-- <link rel="stylesheet" href="css/bootstrap.min.css">
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
	type="text/css" /> -->
	
<!-- GLOBAL MAINLY STYLES-->
<link href="./assetsnew/vendors/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="./assetsnew/vendors/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link href="./assetsnew/vendors/themify-icons/css/themify-icons.css"
	rel="stylesheet" />
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/jvectormap/jquery-jvectormap-2.0.3.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<!-- <link href="./assetsnew/vendors/bootstrap-datepicker/dist/css/bootstrap-datepicker3.min.css" rel="stylesheet" /> -->
<script src="js/disable_f5.js"></script>
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js"
	type="text/javascript"></script>
<!-- PAGE LEVEL STYLES-->


<!-- PLUGINS STYLES-->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.4/css/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="https://cdn.datatables.net/buttons/2.2.2/css/buttons.dataTables.min.css">
<style>

table.dataTable thead th, table.dataTable tfoot td {
	padding: 10px 18px 6px 18px;
	border-top: 0px;
	background-color: #374f65;
	color: #fff;
}

table.dataTable {
	width: 100%;
	margin: 0 auto;
	clear: both;
	border-collapse: collapse;
	border-spacing: 0;
}

.table>thead>tr>th {
	vertical-align: middle;
	text-align: center;
}

.table>tfoot>tr>td, .table>tbody>tr>td {
	vertical-align: middle;
}

table.dataTable tfoot th, table.dataTable tfoot td {
	padding: 10px 18px 6px 18px;
	border-top: 0px solid #111;
}

.table-responsive {
	overflow-x: auto;
	font-family: 'Poppins', sans-serif;
}

.modal-lg {
	min-width: 85% !important;
}

label {
	font-weight: bold;
}
</style>
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
	
	<!-- PAGE LEVEL PLUGINS-->
	<script src="./assetsnew/vendors/DataTables/datatables.min.js"
		type="text/javascript"></script>

	<!-- <script src="./assetsnew/vendors/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js" type="text/javascript"></script> -->
	<!-- <script src="https://code.jquery.com/jquery-3.5.1.js"></script> -->
	<script
		src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdn.datatables.net/buttons/2.2.2/js/dataTables.buttons.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
	<script
		src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.html5.min.js"></script>
	<script
		src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.print.min.js"></script>
	<script>
		if ($('#example')) {
			$('#example').DataTable(
					{
						dom : 'Blfrtip',
						buttons : [ 'print', {
							extend : 'pdf',
							orientation : 'landscape'
						}, 'excel' ],
						"lengthMenu" : [ [ 10, 25, 50, 100, -1 ],
								[ 10, 25, 50, 100, "All" ] ]
					});
		}
		if ($('#example2')) {
			$('#example2').DataTable(
					{
						dom : 'Blfrtip',
						buttons : [ 'print', {
							extend : 'pdf',
							orientation : 'landscape'
						}, 'excel' ],
						"lengthMenu" : [ [ 10, 25, 50, 100, -1 ],
								[ 10, 25, 50, 100, "All" ] ]
					});
		}
	</script>
</body>
</html>