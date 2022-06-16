<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.min.css'>
<!-- PLUGINS STYLES-->
<link href="./assetsnew/vendors/DataTables/datatables.min.css"
	rel="stylesheet" />
<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<script src="./assetsnew/vendors/jquery/dist/jquery.min.js"
	type="text/javascript"></script>

<!-- <script src="https://cdn.ckeditor.com/4.17.1/standard/ckeditor.js"></script> -->
<script type="text/javascript" src="//js.nicedit.com/nicEdit-latest.js"></script>

<!-- PAGE LEVEL STYLES-->
<style>
body {
	overflow-y: auto;
}
</style>
<!-- START PAGE CONTENT-->
<%-- <div class="page-heading">
	<h3 class="page-title" style="text-align:center">
		<logic:notEmpty name="HEADING">
									${HEADING }
								</logic:notEmpty>
	</h3>

</div> --%>
<div class="page-content fade-in-up">
	<html:form action="/GPReport" styleId="AssignedCasesToSectionForm"
		enctype="multipart/form-data">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(fileCino)" styleId="fileCino" />
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
				<div class="ibox-title">
					<h4 style="color:Black;">
						<logic:notEmpty name="HEADING">
									${HEADING}
								</logic:notEmpty>
					</h4>
				</div>
			</div>
			<div class="ibox-body">
				<ul class="nav nav-tabs" role="tablist">
					<li class="nav-item"><a class="nav-link" id="home-tab"
						data-toggle="tab" href="#home" role="tab" aria-controls="home"
						aria-selected="false"> <span class="d-block d-sm-none"><i
								class="fas fa-home"></i></span> <span class="d-none d-sm-block">Case
								Details</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="profile-tab"
						data-toggle="tab" href="#profile" role="tab"
						aria-controls="profile" aria-selected="true"> <span
							class="d-block d-sm-none"><i class="fas fa-user"></i></span> <span
							class="d-none d-sm-block">Respondents</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="message-tab"
						data-toggle="tab" href="#message" role="tab"
						aria-controls="message" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="far fa-envelope"></i></span>
							<span class="d-none d-sm-block">IA Filings</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="parawise-tab"
						data-toggle="tab" href="#parawise" role="tab"
						aria-controls="parawise" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Parawise Remarks</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="setting-tab"
						data-toggle="tab" href="#setting" role="tab"
						aria-controls="setting" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Interim Orders</span>
					</a></li>
					<li class="nav-item"><a class="nav-link" id="history-tab"
						data-toggle="tab" href="#history" role="tab"
						aria-controls="history" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Case History</span>
					</a></li>
					
					<li class="nav-item"><a class="nav-link" id="final-tab"
						data-toggle="tab" href="#final" role="tab"
						aria-controls="final" aria-selected="false"> <span
							class="d-block d-sm-none"><i class="fas fa-cog"></i></span> <span
							class="d-none d-sm-block">Final Orders</span>
					</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane" id="home" role="tabpanel"
						aria-labelledby="home-tab">
						<div class="ibox">
							<div class="ibox-body">
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Date of filing: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										2002-09-11</div>

									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Case Type : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">WP</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Filing No.: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">93783</div>
								</div>
								<div class="row">

									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Filing Year: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">2002</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Registration No: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">17778</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Est Code: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">APHC01</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Case ID: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">63</div>

									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Cause Type: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">DAILY
										LIST</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> Bench Name: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">Single
										Bench</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Judicial Branch: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">WRIT
										Section</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Coram: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">A V
										SESHA SAI</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Court Est Name: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">High
										Court of aphc</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b> State Name: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										ANDHRAPRADESH</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>District : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">GUNTUR</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Date Of First List : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										2013-06-10</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Date Of Next List </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										2022-05-06</div>


									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Date Of Decision : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										0001-01-01</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Purpose : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">FINAL
										HEARING</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Petetioner Name: </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">
										T.SIVAIAH, GUNTUR</div>

									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Petetioner Advacate : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">K G
										KRISHNA MURTHY</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Petetioner Legal Heir : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">N</div>
								</div>
								<div class="row">
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Respondent Name : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">MD, APSC
										COOP FIN CORP LTD.,HYD &amp; 2 ORS</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Respondent Advocate : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">CH C
										SATYANARAYANA</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
										<b>Respondent Advocate : </b>
									</div>
									<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">CH C
										SATYANARAYANA</div>
								</div>
								<hr />
								<div class="row">
									<div class="col-md-12">
										<table class="table table-striped table-bordered table-hover"
											cellspacing="0" width="100%">
											<thead>
												<tr>
													<th colspan="4">ACTS List</th>
												</tr>
												<tr>
													<th>Sl No.</th>
													<th>Act</th>
													<th>Act Name</th>
													<th>Section</th>
												</tr>
											</thead>
											<tbody>

												<tr>

													<td>1</td>
													<td>1</td>
													<td>ACT NOT GIVEN</td>
													<td>HC SECTION</td>
												</tr>

											</tbody>
										</table>
									</div>
								</div>


							</div>
						</div>
					</div>
					<div class="tab-pane" id="profile" role="tabpanel"
						aria-labelledby="profile-tab">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-striped table-bordered table-hover"
									cellspacing="0" width="100%">
									<thead>
										<tr>
											<th colspan="3">Respondent List</th>
										</tr>
										<tr>
											<th>Sl No.</th>
											<th>Party No</th>
											<th>Party Name</th>
										</tr>
									</thead>
									<tbody>

										<tr>
											<td>1</td>
											<td>1</td>
											<td>The District Collector, Chairman,</td>
										</tr>

										<tr>
											<td>2</td>
											<td>2</td>
											<td>The Executive Director,</td>
										</tr>

									</tbody>
								</table>
							</div>
						</div>

					</div>
					<div class="tab-pane" id="message" role="tabpanel"
						aria-labelledby="message-tab">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-striped table-bordered table-hover"
									cellspacing="0" width="100%">
									<thead>
										<tr>
											<th colspan="6">IAFilling List</th>
										</tr>
										<tr>
											<th>Sl No.</th>
											<th>Sr No</th>
											<th>IA NO</th>
											<th>IA PetetionerName</th>
											<th>IA PetetionerDispoasal</th>
											<th>IA Date of Filling</th>

										</tr>
									</thead>
									<tbody>

										<tr>
											<td>1</td>
											<td>1</td>
											<td></td>
											<td>T.SIVAIAH, GUNTUR MD, APSC COOP FIN CORP LTD.,HYD &
												2 ORS</td>
											<td>P</td>
											<td>2002-09-11</td>

										</tr>


										<tr>
											<td>2</td>
											<td>2</td>
											<td></td>
											<td>T.SIVAIAH, GUNTUR MD, APSC COOP FIN CORP LTD.,HYD &
												2 ORS</td>
											<td>P</td>
											<td>2002-09-11</td>

										</tr>


										<tr>
											<td>3</td>
											<td>3</td>
											<td></td>
											<td>T.SIVAIAH, GUNTUR MD, APSC COOP FIN CORP LTD.,HYD &
												2 ORS</td>
											<td>P</td>
											<td>2016-02-10</td>

										</tr>


									</tbody>

								</table>

							</div>

						</div>

					</div>
					<div class="tab-pane" id="setting" role="tabpanel"
						aria-labelledby="setting-tab">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-striped table-bordered table-hover"
									cellspacing="0" width="100%">
									<thead>
										<tr>
											<th colspan="6">InterimOrder List</th>
										</tr>
										<tr>
											<th>Sl No.</th>
											<th>Sr No</th>
											<th>Order NO</th>
											<th>Order Date</th>
											<th>Order Details</th>
											<th>Order Document</th>

										</tr>
									</thead>
									<tbody>

										<tr>
											<td>1</td>
											<td>1</td>
											<td>1</td>
											<td>2021-09-14</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-1.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-1</a></td>

										</tr>

										<tr>
											<td>2</td>
											<td>2</td>
											<td>2</td>
											<td>2021-09-21</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-2.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-2</a></td>

										</tr>

										<tr>
											<td>3</td>
											<td>3</td>
											<td>3</td>
											<td>2021-10-27</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-3.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-3</a></td>

										</tr>

										<tr>
											<td>4</td>
											<td>4</td>
											<td>4</td>
											<td>2021-11-10</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-4.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-4</a></td>

										</tr>

										<tr>
											<td>5</td>
											<td>5</td>
											<td>5</td>
											<td>2021-11-17</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-5.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-5</a></td>

										</tr>

										<tr>
											<td>6</td>
											<td>6</td>
											<td>6</td>
											<td>2021-11-25</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-6.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-6</a></td>

										</tr>

										<tr>
											<td>7</td>
											<td>7</td>
											<td>7</td>
											<td>2021-12-13</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-7.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-7</a></td>

										</tr>

										<tr>
											<td>8</td>
											<td>8</td>
											<td>8</td>
											<td>2022-01-03</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-8.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-8</a></td>

										</tr>

										<tr>
											<td>9</td>
											<td>9</td>
											<td>9</td>
											<td>2022-04-22</td>
											<td>Court Proceedings</td>

											<td><a
												href="./HighCourtsCaseOrders/APHC010006572002-interimorder-9.pdf"
												class="btn btn-sm btn-info" target="_new"> Court
													Proceedings-9</a></td>

										</tr>

									</tbody>
								</table>
							</div>
						</div>

					</div>

					<div class="tab-pane" id="history" role="tabpanel"
						aria-labelledby="history-tab">

						<div class="row">
							<div class="col-md-12">
								<table class="table table-striped table-bordered table-hover"
									cellspacing="0" width="100%">
									<thead>
										<tr>
											<th colspan="7">Case History Details</th>
										</tr>
										<tr>
											<th>Sl No.</th>
											<th>Sr No</th>
											<th>Judge Name</th>
											<th>Business Date</th>
											<th>Hearing Date</th>
											<th>Purpose of Listing</th>
											<th>Cause Type</th>

										</tr>
									</thead>
									<tbody>

										<tr>
											<td>1</td>
											<td>1</td>
											<td>A V SESHA SAI</td>
											<td>0001-01-01</td>
											<td>2013-06-10</td>
											<td>Final Hearing Suit Matters -4</td>
											<td></td>



										</tr>

										<tr>
											<td>2</td>
											<td>2</td>
											<td>A V SESHA SAI</td>
											<td>2020-01-20</td>
											<td>2020-01-27</td>
											<td>FINAL HEARING</td>
											<td></td>



										</tr>

										<tr>
											<td>3</td>
											<td>3</td>
											<td>A V SESHA SAI</td>
											<td>2020-01-27</td>
											<td>2020-02-03</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>4</td>
											<td>4</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-07</td>
											<td>2020-02-10</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>5</td>
											<td>5</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-07</td>
											<td>2020-02-10</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>6</td>
											<td>6</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-07</td>
											<td>2020-02-10</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>7</td>
											<td>7</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-03</td>
											<td>2020-02-10</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>8</td>
											<td>8</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-10</td>
											<td>2020-02-17</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>9</td>
											<td>9</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-17</td>
											<td>2020-02-20</td>
											<td>FINAL HEARING</td>
											<td>WEEKLY CAUSE LIST- FINAL HEARING</td>



										</tr>

										<tr>
											<td>10</td>
											<td>10</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-20</td>
											<td>2020-02-25</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>11</td>
											<td>11</td>
											<td>A V SESHA SAI</td>
											<td>2020-02-25</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>12</td>
											<td>12</td>
											<td>A V SESHA SAI</td>
											<td>2020-03-04</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST -2</td>



										</tr>

										<tr>
											<td>13</td>
											<td>13</td>
											<td>A V SESHA SAI</td>
											<td>2020-03-05</td>
											<td>2020-03-10</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST -2</td>



										</tr>

										<tr>
											<td>14</td>
											<td>14</td>
											<td>A V SESHA SAI</td>
											<td>2020-03-10</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>15</td>
											<td>15</td>
											<td>A V SESHA SAI</td>
											<td>2020-03-11</td>
											<td>2020-03-16</td>
											<td>FOR DISMISSAL</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>16</td>
											<td>16</td>
											<td>A V SESHA SAI</td>
											<td>2020-03-16</td>
											<td>2020-03-30</td>
											<td>FOR DISMISSAL</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>17</td>
											<td>17</td>
											<td>A V SESHA SAI</td>
											<td>2020-06-24</td>
											<td>2020-06-25</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>18</td>
											<td>18</td>
											<td>A V SESHA SAI</td>
											<td>2020-06-25</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>19</td>
											<td>19</td>
											<td>A V SESHA SAI</td>
											<td>2020-08-24</td>
											<td>2020-08-31</td>
											<td>FOR ORDERS</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>20</td>
											<td>20</td>
											<td>A V SESHA SAI</td>
											<td>2020-08-31</td>
											<td>2020-09-02</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>21</td>
											<td>21</td>
											<td>A V SESHA SAI</td>
											<td>2020-09-02</td>
											<td>2020-09-07</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>22</td>
											<td>22</td>
											<td>A V SESHA SAI</td>
											<td>2021-03-25</td>
											<td>2021-04-01</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>23</td>
											<td>23</td>
											<td>A V SESHA SAI</td>
											<td>2021-04-06</td>
											<td>2021-04-08</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>24</td>
											<td>24</td>
											<td>A V SESHA SAI</td>
											<td>2021-04-08</td>
											<td>2021-04-22</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>25</td>
											<td>25</td>
											<td>A V SESHA SAI</td>
											<td>2021-09-14</td>
											<td>2021-09-21</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>26</td>
											<td>26</td>
											<td>A V SESHA SAI</td>
											<td>2021-09-21</td>
											<td>2021-09-28</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>27</td>
											<td>27</td>
											<td>A V SESHA SAI</td>
											<td>2021-10-27</td>
											<td>2021-11-10</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>28</td>
											<td>28</td>
											<td>A V SESHA SAI</td>
											<td>2021-11-10</td>
											<td>2021-11-17</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>29</td>
											<td>29</td>
											<td>A V SESHA SAI</td>
											<td>2021-11-17</td>
											<td>2021-11-24</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>30</td>
											<td>30</td>
											<td>A V SESHA SAI</td>
											<td>2021-11-25</td>
											<td>2021-12-01</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>31</td>
											<td>31</td>
											<td>A V SESHA SAI</td>
											<td>2021-12-01</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>32</td>
											<td>32</td>
											<td>A V SESHA SAI</td>
											<td>2021-12-13</td>
											<td>2021-12-20</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>33</td>
											<td>33</td>
											<td>A V SESHA SAI</td>
											<td>2022-01-03</td>
											<td></td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>34</td>
											<td>34</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-15</td>
											<td>2022-02-22</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>35</td>
											<td>35</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-22</td>
											<td>2022-02-23</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>36</td>
											<td>36</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-23</td>
											<td>2022-02-24</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>37</td>
											<td>37</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-24</td>
											<td>2022-02-25</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>38</td>
											<td>38</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-25</td>
											<td>2022-02-28</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>39</td>
											<td>39</td>
											<td>A V SESHA SAI</td>
											<td>2022-02-28</td>
											<td>2022-03-07</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>40</td>
											<td>40</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-07</td>
											<td>2022-03-08</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>41</td>
											<td>41</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-08</td>
											<td>2022-03-09</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>42</td>
											<td>42</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-09</td>
											<td>2022-03-10</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>43</td>
											<td>43</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-10</td>
											<td>2022-03-14</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>44</td>
											<td>44</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-14</td>
											<td>2022-03-15</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>45</td>
											<td>45</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-15</td>
											<td>2022-03-16</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>46</td>
											<td>46</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-16</td>
											<td>2022-03-31</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>47</td>
											<td>47</td>
											<td>A V SESHA SAI</td>
											<td>2022-03-31</td>
											<td>2022-04-01</td>
											<td>FINAL HEARING (SERVICE MATTERS)</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>48</td>
											<td>48</td>
											<td>A V SESHA SAI</td>
											<td>2022-04-01</td>
											<td>2022-04-08</td>
											<td>FOR DISMISSAL</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>49</td>
											<td>49</td>
											<td>A V SESHA SAI</td>
											<td>2022-04-18</td>
											<td>2022-04-22</td>
											<td>FOR DISMISSAL</td>
											<td>DAILY LIST</td>



										</tr>

										<tr>
											<td>50</td>
											<td>50</td>
											<td>A V SESHA SAI</td>
											<td>2022-04-22</td>
											<td>2022-05-06</td>
											<td>FINAL HEARING</td>
											<td>DAILY LIST</td>



										</tr>

									</tbody>

								</table>

							</div>

						</div>

					</div>
					
					<div class="tab-pane" id="final" role="tabpanel"
						aria-labelledby="final-tab">
						
						
						</div>
						
					<div class="tab-pane" id="parawise" role="tabpanel"
						aria-labelledby="parawise-tab">
						
						
						</div>	
				</div>

			</div>
		</div>

		<!-- 

		<div class="ibox">
			<div class="ibox-head">
				<div class="ibox-title">
					<h4 style="color:Black;">
						<logic:notEmpty name="HEADING">
									${HEADING}
								</logic:notEmpty>
					</h4>
				</div>
			</div>
			<div class="ibox-body">


				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Date of filing: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">2016-12-05</div>

					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Case Type : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">WA</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Filing No.: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">227038</div>
				</div>
				<div class="row">

					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Filing Year: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">2016</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Registration No: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">1162</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Est Code: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">APHC01</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Case ID: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">62</div>

					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Cause Type: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2"></div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> Bench Name: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">Division
						Bench</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Judicial Branch: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">WRIT Section</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Coram: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">RAMESH
						RANGANATHAN , J. UMA DEVI</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Court Est Name: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">High Court
						of aphc</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b> State Name: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">ANDHRAPRADESH</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>District : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">VIZIANAGARAM</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Date Of First List : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">2017-08-23</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Date Of Next List </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">2017-08-23</div>


					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Date Of Decision : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">0001-01-01</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Purpose : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">ADMISSION
						(FRESH MATTERS)</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Petetioner Name: </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">S.Vijaya
						Rama Raju,</div>

					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Petetioner Advacate : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">TADDI
						NAGESWARA RAO</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Petetioner Legal Heir : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">N</div>
				</div>
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Respondent Name : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">Nimmaka
						Jayaraju,</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Respondent Advocate : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">GP FOR
						SOCIAL WELFARE (AP)</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2 pull-rightt">
						<b>Respondent Advocate : </b>
					</div>
					<div class="col-xs-6 col-sm-3 col-md-2 col-lg-2">GP FOR
						SOCIAL WELFARE (AP)</div>
				</div>


				<hr />


				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="4">ACTS List</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Act</th>
									<th>Act Name</th>
									<th>Section</th>
								</tr>
							</thead>
							<tbody>

								<tr>

									<td>1</td>
									<td>1</td>
									<td>ACT NOT GIVEN</td>
									<td>HC SECTION</td>
								</tr>

							</tbody>
						</table>
					</div>
				</div>





				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="3">Respondent List</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Party No</th>
									<th>Party Name</th>
								</tr>
							</thead>
							<tbody>

								<tr>
									<td>1</td>
									<td>1</td>
									<td>The Honble Chief Minister of Andhra Pradesh,</td>
								</tr>

								<tr>
									<td>2</td>
									<td>2</td>
									<td>The Secretary General,</td>
								</tr>

								<tr>
									<td>3</td>
									<td>3</td>
									<td>The Chief Secretary,</td>
								</tr>

								<tr>
									<td>4</td>
									<td>4</td>
									<td>The Secretary,</td>
								</tr>

								<tr>
									<td>5</td>
									<td>5</td>
									<td>District Collector &amp; Magistrate,</td>
								</tr>

								<tr>
									<td>6</td>
									<td>6</td>
									<td>Superintendent of Police,</td>
								</tr>

							</tbody>
						</table>
					</div>
				</div>



				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="6">IAFilling List</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Sr No</th>
									<th>IA NO</th>
									<th>IA PetetionerName</th>
									<th>IA PetetionerDispoasal</th>
									<th>IA Date of Filling</th>

								</tr>
							</thead>
							<tbody>

								<tr>
									<td>1</td>
									<td>1</td>
									<td></td>
									<td>S.Vijaya Rama Raju, Nimmaka Jayaraju,</td>
									<td>P</td>
									<td>2016-12-05</td>

								</tr>


								<tr>
									<td>2</td>
									<td>2</td>
									<td></td>
									<td>S.Vijaya Rama Raju, Nimmaka Jayaraju,</td>
									<td>D</td>
									<td>2017-02-27</td>

								</tr>


								<tr>
									<td>3</td>
									<td>3</td>
									<td></td>
									<td>S.Vijaya Rama Raju, Nimmaka Jayaraju,</td>
									<td>D</td>
									<td>2017-02-27</td>

								</tr>


								<tr>
									<td>4</td>
									<td>4</td>
									<td></td>
									<td>S.Vijaya Rama Raju, Nimmaka Jayaraju,</td>
									<td>P</td>
									<td>2017-02-27</td>

								</tr>


								<tr>
									<td>5</td>
									<td>5</td>
									<td></td>
									<td>S.Vijaya Rama Raju, Nimmaka Jayaraju,</td>
									<td>P</td>
									<td>2017-02-27</td>

								</tr>


							</tbody>

						</table>

					</div>

				</div>





				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="4">Case Link List</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Sr No</th>
									<th>Filling NO</th>
									<th>Case Number</th>

								</tr>
							</thead>
							<tbody>

								<tr>
									<td>1</td>
									<td>2</td>
									<td>WA/227038/2016</td>
									<td>WA/1162/2017</td>

								</tr>

								<tr>
									<td>2</td>
									<td>1</td>
									<td>WA/227036/2016</td>
									<td>WA/1161/2017</td>

								</tr>

							</tbody>

						</table>

					</div>

				</div>





				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="7">Case History Details</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Sr No</th>
									<th>Judge Name</th>
									<th>Business Date</th>
									<th>Hearing Date</th>
									<th>Purpose of Listing</th>
									<th>Cause Type</th>

								</tr>
							</thead>
							<tbody>

								<tr>
									<td>1</td>
									<td>1</td>
									<td>RAMESH RANGANATHAN , J. UMA DEVI</td>
									<td>0001-01-01</td>
									<td>2017-08-23</td>
									<td>ADMISSION (FRESH MATTERS)</td>
									<td></td>



								</tr>

								<tr>
									<td>2</td>
									<td>2</td>
									<td></td>
									<td>2017-02-27</td>
									<td>0001-01-01</td>
									<td>Disposed</td>
									<td></td>



								</tr>

							</tbody>

						</table>

					</div>

				</div>






				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped table-bordered table-hover"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th colspan="7">Case Activities</th>
								</tr>
								<tr>
									<th>Sl No.</th>
									<th>Date</th>
									<th>Activity</th>
									<th>Updated By</th>
									<th>Assigned to</th>
									<th>Remarks</th>
									<th>Uploaded Document</th>
								</tr>
							</thead>
							<tbody>

								<tr>
									<td>1</td>
									<td>2022-05-18 11:35:19.815578</td>
									<td>CASE ASSSIGNED TO Section Officer (HOD)</td>
									<td>apswreislegal@gmail.com</td>
									<td>34814</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td>2</td>
									<td>2022-05-20 10:37:57.96328</td>
									<td>CASE SENT BACK</td>
									<td>apswreislegal@gmail.com</td>
									<td>apswreislegal@gmail.com</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td>3</td>
									<td>2022-05-20 10:49:40.500948</td>
									<td>CASE ASSSIGNED</td>
									<td>apswreislegal@gmail.com</td>
									<td>CSO01</td>
									<td></td>
									<td></td>
								</tr>

								<tr>
									<td>4</td>
									<td>2022-05-20 10:52:13.913037</td>
									<td>CASE ASSSIGNED</td>
									<td>apswreislegal@gmail.com</td>
									<td>CSO01</td>
									<td></td>
									<td></td>
								</tr>

							</tbody>

						</table>
					</div>
				</div>







				 <div
					class="col-xs-12 col-sm-12 col-md-6 col-lg-6 mt20 mb20 pull-rightt">
					<input type="button" name="btn" value=" Print"
						onclick=" return print();" class="btn btn-primary text-center"
						id="btn_submit" />
				</div> -->
		<div class="text-right">
			<button class="btn btn-info" type="button"
				onclick="javascript:window.print();">
				<i class="fa fa-print"></i> Print
			</button>
		</div>
	</html:form>
</div>
<!-- 
<script type="text/javascript">
	function print() {
		document.forms[0].mode.value = "printView";
		document.forms[0].submit();
	}
</script>
 -->
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