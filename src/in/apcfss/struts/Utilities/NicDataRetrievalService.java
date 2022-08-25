package in.apcfss.struts.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class NicDataRetrievalService {

	
	static {
	    // System.setProperty("javax.net.debug", "all");
	    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}
	
	/*private static String nicURL = "";
	
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Krishna.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Guntur.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Chittoor.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Eastgodavari.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Kadapa.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Kurnool.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Nellore.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Prakasam.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Srikakulam.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Visakhapatnam.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Vizianagaram.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Westgodavari.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Ananthapur.php";
	nicURL = "https://demo.eoffice.ap.gov.in/TTReports/Apsecretariat.php"; */

	public static void main(String[] args) throws Exception {

		String[] nicURLs = {
				"https://demo.eoffice.ap.gov.in/TTReports/Apsecretariat.php",
				/*
				"https://demo.eoffice.ap.gov.in/TTReports/Krishna.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Guntur.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Chittoor.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Eastgodavari.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Kadapa.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Kurnool.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Nellore.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Prakasam.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Srikakulam.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Visakhapatnam.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Vizianagaram.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Westgodavari.php",
				"https://demo.eoffice.ap.gov.in/TTReports/Ananthapur.php"
				*/
		};
		String resp = "";

		  final String dbUrl = "jdbc:postgresql://localhost/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
		  // final String dbUrl = "jdbc:postgresql://10.96.54.54:6432/apolcms", dbUserName = "apolcms", dbPassword = "@p0l(m$";
		 
		String sql = "";
		Connection con = null;
		Statement st = null;
		int c = 0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			// con.setAutoCommit(false);

			for (String nicURL : nicURLs) {

				if (nicURL != null && !nicURL.equals("")) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
					Date d1 = new Date();
					System.out.println(nicURL+"::" + sdf.format(d1));
					String distName = nicURL.substring(nicURL.lastIndexOf("/")+1, nicURL.lastIndexOf("."));
					String mmyyyy = sdf.format(d1);

					String tableName = "nic_data_"+ mmyyyy.trim()
							+ "_"+ distName.toLowerCase().trim() 
							+ "";
					//System.out.println("response=" + resp);
					// c += createDbScript(tableName, con);
					
					resp = sendPostRequest(nicURL);
					//System.out.println("response=" + resp);
					if(resp!=null && !resp.equals(""))
						//System.out.println("response=" + resp);
					{
						JSONArray empArray = new JSONArray(resp);
						NicDataBean ndb = new NicDataBean();
						if (empArray != null) {
							JSONObject gsonObj = new JSONObject();
							for (int i = 0; i < empArray.length(); i++) {
								gsonObj = (JSONObject) empArray.get(i);
								
								if(gsonObj !=null)
								{
									ndb = new Gson().fromJson(gsonObj.toString(), NicDataBean.class);
									int result = saveResult(tableName, con, ndb);
									if(result > 0)
										c++ ;
									else {
										i = empArray.length();
										System.out.println("EXIT");
									}
								}	
								
							}
						}
						System.out.println("Data saved for:" + tableName);
					}
				}
			}
			System.out.println("SQLS executed Count:" + c);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	}

	public static String sendPostRequest(String requestUrl) {
		StringBuffer jsonString = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// String userpass = "apcfms:@pcfm$";

			// String basicAuth = "Basic " +
			// javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			// connection.setRequestProperty ("Authorization", basicAuth);

			//connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			// connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			// OutputStreamWriter writer = new
			// OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			// writer.write(payload);
			// writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line="";
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return jsonString.toString();
	}

	public static String checkString(Object myVal) {

		return (myVal != null && !myVal.equals("")) ? myVal.toString() : "";

	}

	public static int saveResult(String tableName, Connection con,
			NicDataBean ndb) throws SQLException {
		int a = 0;
		String sql = "";
		Statement st = null;
		try {
			sql = "insert into "
					+ tableName
					+ " (employee_code, employee_id, entity, global_org_id, global_org_name, fullname_en, designation_id, designation_name_en, post_name_en, org_unit_name_en, parentou, marking_abbr, mobile1, mobile2, uid, email, address_type, address1, address2, city, post_office, is_primary, is_ou_head, statecode, state, districtcode, district, tehsilcode, tehsil, villagecode, village, panchayatcode, panchayat, habitationcode, habitation, constituencycode, constituency) "
					+ ""
					+ " values "
					+ "('" + checkStringObject(ndb.getEmployee_code()) + "', '"
					+ checkStringObject(ndb.getEmployee_id()) + "', '" + checkStringObject(ndb.getEntity()) + "', '"
					+ checkStringObject(ndb.getGlobal_org_id()) + "', '" + checkStringObject(ndb.getGlobal_org_name())
					+ "', '" + checkStringObject(ndb.getFullname_en()) + "', '"
					+ checkStringObject(ndb.getDesignation_id()) + "', '"
					+ checkStringObject(ndb.getDesignation_name_en()) + "', '"
					+ checkStringObject(ndb.getPost_name_en()) + "', '" + checkStringObject(ndb.getOrg_unit_name_en())
					+ "', '" + checkStringObject(ndb.getParentou()) + "', '" + checkStringObject(ndb.getMarking_abbr()) + "', '"
					
					+ checkStringObject(ndb.getMobile1()) + "', '" + checkStringObject(ndb.getMobile2()) + "', '" + checkStringObject(ndb.getUid()) + "', '" + checkStringObject(ndb.getEmail())
					+ "', '" + checkStringObject(ndb.getAddress_type()) + "', '" + checkStringObject(ndb.getAddress1()) + "', '" + checkStringObject(ndb.getAddress2()) + "', '"
					+ checkStringObject(ndb.getCity()) + "', '" + checkStringObject(ndb.getPost_office()) + "', '" + checkStringObject(ndb.getIs_primary()) + "', '"
					+ checkStringObject(ndb.getIs_ou_head()) + "', '" + checkStringObject(ndb.getStatecode()) + "', '" + checkStringObject(ndb.getState()) + "', '"
					+ checkStringObject(ndb.getDistrictcode()) + "', '" + checkStringObject(ndb.getDistrict()) + "', '" + checkStringObject(ndb.getTehsilcode()) + "', '"
					+ checkStringObject(ndb.getTehsil()) + "', '" + checkStringObject(ndb.getVillagecode()) + "', '" + checkStringObject(ndb.getVillage()) + "', '"
					+ checkStringObject(ndb.getPanchayatcode()) + "', '" + checkStringObject(ndb.getPanchayat()) + "', '" + checkStringObject(ndb.getHabitationcode()) + "', '"
					+ checkStringObject(ndb.getHabitation()) + "', '" + checkStringObject(ndb.getConstituencycode()) + "', '" + checkStringObject(ndb.getConstituency()) + "')"
					+ "";
			
			System.out.println("SQL:"+sql);
			
			st = con.createStatement();
			a += st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
			
		} finally {
			if (st != null)
				st.close();
		}

		return a;
	}

	public static int createDbScript(String tableName, Connection con)
			throws SQLException {
		int b = 0;
		Statement st = null;
		try {
			String sql = "create table "
					+ tableName
					+ " ( employee_code varchar(25), employee_id varchar(25), entity varchar(50), global_org_id varchar(25), global_org_name varchar(500), fullname_en varchar(500),"
					+ " designation_id varchar(250), designation_name_en varchar(500), post_name_en varchar(500), org_unit_name_en varchar(500), parentou varchar(250), marking_abbr varchar(250),"
					+ " mobile1 varchar(15), mobile2 varchar(15), uid varchar(25), email varchar(250), address_type varchar(20), address1 varchar(250), address2 varchar(250), city varchar(150),"
					+ " post_office varchar(150), is_primary varchar(10), is_ou_head varchar(25), statecode varchar(5), state varchar(100), districtcode varchar(5), district varchar(100),"
					+ " tehsilcode varchar(25), tehsil varchar(150), villagecode varchar(15), village varchar(150), panchayatcode varchar(25), panchayat varchar(150), habitationcode varchar(25),"
					+ " habitation varchar(250), constituencycode varchar(25), constituency varchar(250))";

			System.out.println("CREATE SQL:" + sql);
			st = con.createStatement();
			b += st.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null)
				st.close();
		}
		return b;
	}
	
	public static String checkStringObject(Object obj) {
		
		return obj!=null ? obj.toString().replace("'", "").trim() : "";
	}
	
}