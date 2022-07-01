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
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>


<!-- THEME STYLES-->
<link href="assetsnew/css/main.min.css" rel="stylesheet" />
<style>
.pdfobject-container {
	height: 600px;
}

.pdfobject {
	border: 1px solid #666;
}
</style>

<!-- START PAGE CONTENT-->
<div class="page-heading">

</div>
<div class="page-content fade-in-up">
	<html:form method="post" action="/AcksAbstractReport"
		styleId="acksAbstractFormId">
		<html:hidden styleId="mode" property="mode" />
		<html:hidden property="dynaForm(urlPath)" styleId="urlPath" />
		
		<div class="container-fluid">
			<div class="ibox">
				<div class="ibox-body">
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
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

					<logic:notPresent name="errorMsg">
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div id="pdf_view"></div>
							</div>
						</div>
					</logic:notPresent>
				</div>
			</div>

			<%-- <h1 class="page-title">
		<logic:notEmpty name="ack_nooo">
					${ack_nooo}
				</logic:notEmpty>
	</h1>  --%>

			<%-- <div class="ibox">
				<div class="ibox-body">
					<div class="table-responsive">
						<table class="table table-striped table-bordered table-hover"
							id="example">
							<thead>
								<tr>
									<th>Sl.No</th>
									<th>Ack No.</th>
									<th>Download / Print</th>
								</tr>
							</thead>
							<tbody>
								<logic:iterate id="map" name="EXISTDATA" indexId="i">
									<tr>
										<td>${i+1 }</td>
										<td>${map.ack_no}</td>

										<td style="text-align: center;" nowrap="nowrap">
											<logic:present
													name="map" property="ack_no">
													 <a href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
												target="_new" title="Print Barcode"
												class="btn btn-sm btn-info"> <i class="fa fa-save"></i>
													<span>Scanned Affidavit</span> 
											</a></logic:present> <logic:notEmpty name="ack_file">
												<logic:present name="map" property="ack_no">
													<div id="pdf_view"></div>
												</logic:present>
												<logic:present name="map" property="ack_no">
													<a
														href="./uploads/scandocs/${map.ack_no}/${map.ack_no}.pdf"
														target="_new" title="Print Barcode"
														class="btn btn-sm btn-info"> <i class="fa fa-save"></i>
														<span>Download </span>
													</a>
												</logic:present>
											</logic:notEmpty> <logic:notEmpty name="ack_nooo">
												${ack_nooo}
											</logic:notEmpty>


										</td>
									</tr>
								</logic:iterate>
							</tbody>
							<tfoot>
								<tR>
									<logic:present name="DISPLAYOLD">
										<td colspan="14">
									</logic:present>
									<logic:notPresent name="DISPLAYOLD">
										<td colspan="12">
									</logic:notPresent>
									&nbsp;
									</td>
								</tR>
							</tfoot>
						</table>
					</div>
				</div>
			</div> --%>
			<%-- <logic:present name="ack_nooo" scope="request">
											
											<i class="fa fa-save"></i>
													<span>Download After 2.00 PM Scanned Affidavit</span> 
											</logic:present>
 --%>
		</div>
	</html:form>
</div>


<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js'></script>

<logic:notPresent name="errorMsg">
	<script src="js/pdfobject.js"></script>
	<script>
		$(document)
				.ready(
						function() {
							
							var urlPath = $("#urlPath").val();
							// "https://apolcms.ap.gov.in/uploads/scandocs/EHE052220220613040345798/EHE052220220613040345798.pdf"
							PDFObject
									.embed(urlPath, "#pdf_view");

						});
	</script>
</logic:notPresent>