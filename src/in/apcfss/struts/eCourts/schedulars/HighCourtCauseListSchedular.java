package in.apcfss.struts.eCourts.schedulars;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import in.apcfss.struts.Actions.UpdateEcourtsDataAction;
import in.apcfss.struts.eCourt.apis.ECourtAPIs;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import in.apcfss.struts.eCourt.apis.EHighCourtAPI;
import in.apcfss.struts.eCourt.apis.HASHHMACJava;
import in.apcfss.struts.eCourt.apis.HighCourtCauseListBenchAPI;
import plugins.DatabasePlugin;

public class HighCourtCauseListSchedular implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("HighCourtCauseListSchedular Execution :" + new Date());
		try {
			retrieveCauseList(context);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// public static void main(String[] args) {
	public synchronized void retrieveCauseList(JobExecutionContext context) throws JobExecutionException, SQLException {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		int totalCount = 0;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);

			String opVal = ECourtAPIs.getSelectParam(11);

			LocalDate newDate1 = LocalDate.now().plusDays(1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

			String estCode = "APHC01", causelistDate = formatter.format(newDate1);// "2022-04-25";//

			inputStr = "est_code=" + estCode + "|causelist_date=" + causelistDate;// ECourtAPIs.getInputStringValue(opVal);
			// 1. Encoding Request Token
			byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

			targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

			System.out.println(totalCount + ":URL : " + targetURL);
			System.out.println("Input String : " + inputStr);

			authToken = EHighCourtAPI.getAuthToken();
			String resp = "";
			if (opVal != null && !opVal.equals("")) {
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
			}

			if (resp != null && !resp.equals("")) {
				HighCourtCauseListBenchAPI.processApiResponse(resp, estCode, causelistDate, con);
				UpdateEcourtsDataAction.retrieveCauseList(estCode, causelistDate, con);
				con.commit();
			}

			System.out.println("CAUSE LIST BENCH END");
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
	}
}