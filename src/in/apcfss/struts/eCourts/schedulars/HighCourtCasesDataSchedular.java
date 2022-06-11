package in.apcfss.struts.eCourts.schedulars;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import in.apcfss.struts.Actions.UpdateEcourtsDataAction;
import in.apcfss.struts.eCourt.apis.ECourtAPIs;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import in.apcfss.struts.eCourt.apis.EHighCourtAPI;
import in.apcfss.struts.eCourt.apis.HASHHMACJava;
import plugins.DatabasePlugin;

public class HighCourtCasesDataSchedular implements Job {
	
	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("Print Print Struts 1.3 + Quartz 2.1.5 integration example ~");
		// updateData(context);
	}
	
	public synchronized void updateData(JobExecutionContext context) throws JobExecutionException {
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql = null;
		int totalCount = 0;
		String inputStr = "", targetURL = "";
		String authToken = "";
		String request_token = "", requeststring = "";
		try {
			con = DatabasePlugin.connect();
			String opVal = ECourtAPIs.getSelectParam(1);
			String cino = "";// cform.getDynaForm("cino").toString();
			sql = "select cino from ecourts_case_data a where last_updated_ecourts<=now()::date - integer '2' order by last_updated_ecourts asc limit 250";
			// sql="select cino from ecourts_case_data where cino='APHC010183002019'";
			System.out.println("SQLLLLLLLLLLLLLL:::::::" + sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				// if (cino != null) {
				cino = rs.getString("cino").toString().trim();
				totalCount++;
				inputStr = "cino=" + cino;// ECourtAPIs.getInputStringValue(opVal);

				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":URL : " + targetURL);
				authToken = EHighCourtAPI.getAuthToken();
				String resp = "";
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);

				System.out.println("resp--" + resp);

				if (resp != null && !resp.equals("")) {
					boolean b = UpdateEcourtsDataAction.processCNRsearchResponse(resp, opVal, con, cino);
				}
			}
			System.out.println("FINAL END : Records fetched:" + totalCount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
	}
}