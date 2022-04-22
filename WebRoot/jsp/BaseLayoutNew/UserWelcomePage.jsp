<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>

<link rel="shortcut icon" href="./images/aplogo.ico">
<!-- <link rel="shortcut icon" href="assetsnew/images/favicon.ico"> -->

<title>APOLCMS</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width initial-scale=1.0">

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
body {
	overflow-y: auto;
}

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
label{
	font-weight: bold;
}
</style>

</head>

<body class="fixed-navbar">
	<div class="page-wrapper">
		<!-- START HEADER-->
		<tiles:insert attribute="Header" name="Header" />
		<!-- END HEADER-->
		<!-- START SIDEBAR-->
		<tiles:insert attribute="Menu" name="Menu" />

		<!-- END SIDEBAR-->
		<div class="content-wrapper">
			<!-- START PAGE CONTENT-->
			<tiles:insert attribute="content" name="content" />
			<!-- END PAGE CONTENT-->
			<tiles:insert attribute="footer" name="footer" />
		</div>
	</div>



	<!-- BEGIN THEME CONFIG PANEL- ->
    <div class="theme-config">
        <div class="theme-config-toggle"><i class="fa fa-cog theme-config-show"></i><i class="ti-close theme-config-close"></i></div>
        <div class="theme-config-box">
            <div class="text-center font-18 m-b-20">SETTINGS</div>
            <div class="font-strong">LAYOUT OPTIONS</div>
            <div class="check-list m-b-20 m-t-10">
                <label class="ui-checkbox ui-checkbox-gray">
                    <input id="_fixedNavbar" type="checkbox" checked>
                    <span class="input-span"></span>Fixed navbar</label>
                <label class="ui-checkbox ui-checkbox-gray">
                    <input id="_fixedlayout" type="checkbox">
                    <span class="input-span"></span>Fixed layout</label>
                <label class="ui-checkbox ui-checkbox-gray">
                    <input class="js-sidebar-toggler" type="checkbox">
                    <span class="input-span"></span>Collapse sidebar</label>
            </div>
            <div class="font-strong">LAYOUT STYLE</div>
            <div class="m-t-10">
                <label class="ui-radio ui-radio-gray m-r-10">
                    <input type="radio" name="layout-style" value="" checked="">
                    <span class="input-span"></span>Fluid</label>
                <label class="ui-radio ui-radio-gray">
                    <input type="radio" name="layout-style" value="1">
                    <span class="input-span"></span>Boxed</label>
            </div>
            <div class="m-t-10 m-b-10 font-strong">THEME COLORS</div>
            <div class="d-flex m-b-20">
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Default">
                    <label>
                        <input type="radio" name="setting-theme" value="default" checked="">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-white"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Blue">
                    <label>
                        <input type="radio" name="setting-theme" value="blue">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-blue"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Green">
                    <label>
                        <input type="radio" name="setting-theme" value="green">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-green"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Purple">
                    <label>
                        <input type="radio" name="setting-theme" value="purple">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-purple"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Orange">
                    <label>
                        <input type="radio" name="setting-theme" value="orange">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-orange"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Pink">
                    <label>
                        <input type="radio" name="setting-theme" value="pink">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-pink"></div>
                        <div class="color-small bg-ebony"></div>
                    </label>
                </div>
            </div>
            <div class="d-flex">
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="White">
                    <label>
                        <input type="radio" name="setting-theme" value="white">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Blue light">
                    <label>
                        <input type="radio" name="setting-theme" value="blue-light">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-blue"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Green light">
                    <label>
                        <input type="radio" name="setting-theme" value="green-light">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-green"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Purple light">
                    <label>
                        <input type="radio" name="setting-theme" value="purple-light">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-purple"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Orange light">
                    <label>
                        <input type="radio" name="setting-theme" value="orange-light">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-orange"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
                <div class="color-skin-box" data-toggle="tooltip" data-original-title="Pink light">
                    <label>
                        <input type="radio" name="setting-theme" value="pink-light">
                        <span class="color-check-icon"><i class="fa fa-check"></i></span>
                        <div class="color bg-pink"></div>
                        <div class="color-small bg-silver-100"></div>
                    </label>
                </div>
            </div>
        </div>
    </div>
    <!- - END THEME CONFIG PANEL-->
	<!-- BEGIN PAGA BACKDROPS-->
	<div class="sidenav-backdrop backdrop"></div>
	<div class="preloader-backdrop">
		<div class="page-preloader">Loading</div>
	</div>
	<!-- END PAGA BACKDROPS-->
	<!-- CORE PLUGINS-->

	<script src="./assetsnew/vendors/popper.js/dist/umd/popper.min.js"
		type="text/javascript"></script>
	<script src="./assetsnew/vendors/bootstrap/dist/js/bootstrap.min.js"
		type="text/javascript"></script>
	<script src="./assetsnew/vendors/metisMenu/dist/metisMenu.min.js"
		type="text/javascript"></script>
	<script
		src="./assetsnew/vendors/jquery-slimscroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<!-- PAGE LEVEL PLUGINS-->
	<!-- <script src="./assetsnew/vendors/chart.js/dist/Chart.min.js"
		type="text/javascript"></script>
	<script
		src="./assetsnew/vendors/jvectormap/jquery-jvectormap-2.0.3.min.js"
		type="text/javascript"></script>
	<script
		src="./assetsnew/vendors/jvectormap/jquery-jvectormap-world-mill-en.js"
		type="text/javascript"></script>
	<script
		src="./assetsnew/vendors/jvectormap/jquery-jvectormap-us-aea-en.js"
		type="text/javascript"></script> -->
	<!-- CORE SCRIPTS-->
	<script src="assetsnew/js/app.min.js" type="text/javascript"></script>
	<!-- PAGE LEVEL SCRIPTS-->
	<!-- <script src="./assetsnew/js/scripts/dashboard_1_demo.js" type="text/javascript"></script> -->
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
	</script>



	<script type="text/javascript">
		$(function() {
			/* $('#example-table').DataTable({
				"scrollX" : true,
				"pagingType": "full_numbers",
			    "dom": 'T<"clear">lfrtip',
			    
			    search: {
			        return: true
			    }
			 }); */

			/* $('.input-group.date').datepicker({
			    todayBtn: "linked",
			    keyboardNavigation: false,
			    forceParse: false,
			    calendarWeeks: true,
			    autoclose: true
			}); */

		})
	</script>

</body>
</html>