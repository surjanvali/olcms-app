package in.apcfss.struts.eCourt.apis;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;

import plugins.DatabasePlugin;

public class HighCourtStateMasterAPI {

	static final String apolcmsDataBase = "jdbc:postgresql://172.16.98.2:9432/apolcms_new";
	static final String apolcmsUserName = "apolcms";
	static final String apolcmsPassword = "apolcms";

	public static void main(String[] args) throws Exception {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);
			String opVal = ECourtAPIs.getSelectParam(16);
			inputStr = "";// ECourtAPIs.getInputStringValue(opVal);
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
				try {
					resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (resp != null && !resp.equals("")) {
				try {
					processStatesMasterResponse(resp, opVal, con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	}

	public static void processStatesMasterResponse(String resp, String fileName, Connection con)
			throws Exception {
		String response_str = "";
		String response_token = "";
		String version = "";
		String decryptedRespStr = "";
		String sql = "";
		resp = resp.trim();

		System.out.println("processMastersResponse"+resp);
		if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN"))) {
			JSONObject jObj = new JSONObject(resp);
			if ((jObj.has("response_str")) && (jObj.getString("response_str") != null)) {
				response_str = jObj.getString("response_str").toString();
			}
			if ((jObj.has("response_token")) && (jObj.getString("response_token") != null)) {
				response_token = jObj.getString("response_token").toString();
			}
			if ((jObj.has("version")) && (jObj.getString("version") != null)) {
				version = jObj.getString("version").toString();
			}
			if ((response_str != null) && (!response_str.equals(""))) {
				decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
			}
			JSONObject jObjCaseData = new JSONObject(decryptedRespStr);
			ArrayList<String> sqls = new ArrayList();

			System.out.println("state:" + ImportECourtsData.checkStringJSONObj(jObjCaseData, "state"));
			JSONObject jObjActsData = new JSONObject();
			JSONObject jObjActsInnerData = new JSONObject();
			if ((ImportECourtsData.checkStringJSONObj(jObjCaseData, "state") != null)
					&& (ImportECourtsData.checkStringJSONObj(jObjCaseData, "state") != "null")
					&& (!ImportECourtsData.checkStringJSONObj(jObjCaseData, "state").equals(""))
					&& (!ImportECourtsData.checkStringJSONObj(jObjCaseData, "state").equals("[]"))) {
				jObjActsData = new JSONObject(ImportECourtsData.checkStringJSONObj(jObjCaseData, "state"));
				for (int i = 1; i <= 30; i++) {//jObjActsData.length()
					if(jObjActsData.has("state" + i))
					{
						jObjActsInnerData = new JSONObject(jObjActsData.get("state" + i).toString());
	
						sql = "INSERT INTO apolcms.ecourts_mst_states(slno ,state_code , state_name ) VALUES(" + i + ",'"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "state_code") + "',  '"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "state_name") + "')";
						sqls.add(sql);
					}
				}
			}

			int executedSqls = 0;
			if (sqls.size() > 0) {
				executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);
			}

			System.out.println("Successfully saved...executedSqls:" + executedSqls);

			System.out.println("END");
		} else {
			
			System.out.println("Invalid/Empty Response::" + "SQL:" + sql);
		}
	}
}