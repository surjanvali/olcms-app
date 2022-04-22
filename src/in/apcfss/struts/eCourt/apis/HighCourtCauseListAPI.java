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

public class HighCourtCauseListAPI {

	static final String apolcmsDataBase = "jdbc:postgresql://172.16.98.2:9432/apolcms";
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
			String opVal = ECourtAPIs.getSelectParam(12);
			
			String estCode="APHC01", causelistDate="";
			
			sql="SELECT slno, est_code, causelist_date, bench_id, judge_name, inserted_time FROM apolcms.ecourts_causelist_data where  causelist_date=to_date('20/1/2022','dd/mm/yyyy') order by causelist_date";
			
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				causelistDate = rs.getString("causelist_date");
				inputStr = "est_code="+estCode+"|causelist_date="+causelistDate+"|bench_id="+rs.getString("bench_id");//ECourtAPIs.getInputStringValue(opVal);
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
						processApiResponse(resp, estCode, causelistDate, con);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	}

	public static void processApiResponse(String resp, String estCode, String causelistDate, Connection con)
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
			System.out.println("decryptedRespStr:"+decryptedRespStr);
			JSONObject jObjCaseData = new JSONObject(decryptedRespStr);
			ArrayList<String> sqls = new ArrayList();

			JSONObject jObjActsInnerData = new JSONObject();
			if ((jObjCaseData != null)) {
				for (int i = 1; i <= jObjCaseData.length(); i++) {//jObjActsData.length()
					
					if(jObjCaseData.has("case" + i))
					{
						jObjActsInnerData = new JSONObject(jObjCaseData.get("case" + i).toString());
	
						sql = "update ecourts_causelist_data set causelist_id='"+ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "causelist_id")
								+"', cause_list_type='"+ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "cause_list_type").trim()+"' where est_code='"+estCode+"' and causelist_date=to_date('"+causelistDate+"','yyyy-mm-dd') and bench_id='"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "bench_id") + "'";
						
						sql = "insert into ecourts_causelist_bench_data (est_code , causelist_date , bench_id , causelist_id , cause_list_type, inserted_time) values "
								+ " ('" + estCode + "',to_date('" + causelistDate + "','yyyy-mm-dd'),'"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "bench_id") + "','"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "causelist_id")
								+ "','"
								+ ImportECourtsData.checkStringJSONObj(jObjActsInnerData, "cause_list_type").trim()
								+ "', now())";
						
						System.out.println("SQL:"+sql);
						sqls.add(sql);
					}
				}
			}

			int executedSqls = 0;
			if (sqls.size() > 0) {
				//System.out.println("SQLS:"+sqls);
				executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);
			}

			System.out.println("Successfully saved..executedSqls:" + executedSqls);

			System.out.println("END");
		} else {
			
			System.out.println("Invalid/Empty Response::" + "SQL:" + sql);
		}
	}
}
