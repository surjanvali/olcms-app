package in.apcfss.struts.Utilities;

import java.io.BufferedReader;
import java.io.File;
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

public class NicDataRetrievalService2 {

	
	static {
	    // System.setProperty("javax.net.debug", "all");
	    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}
	
	
	public static void main(String[] args) throws Exception {
		// "Apsecretariat.php", "Krishna", "Guntur", "Chittoor", "Eastgodavari", "Kadapa", "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam", "Vizianagaram", "Westgodavari", "Ananthapur"
		String[] nicDataDists = { "Apsecretariat","Alluri","Anakapalli","Ananthapur","Annamayya",
									"Bapatla","Chittoor","Eastgodavari","Eluru",
									"Guntur" ,"Kadapa","Kakinada","Konaseema","Krishna",  
									 "Kurnool", "Nandyal","Nellore","Ntr","Palnadu",
									 "Pmanyam", "Prakasam", "Srikakulam", "Srisathyasai","Tirupati",
									 "Visakhapatnam", "Vizianagaram", "Westgodavari"				 
				};

		String resp = "";

		// final String dbUrl = "jdbc:postgresql://172.16.98.2:9432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
		final String dbUrl = "jdbc:postgresql://localhost:5432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
		// final String dbUrl = "jdbc:postgresql://10.96.54.54:6432/apolcms", dbUserName = "apolcms", dbPassword = "@p0l(m$";

		// final String filesPath = "C:\\Users\\Surjan Vali\\Desktop\\nic-emp-data\\";
		final String filesPath = "C:\\Users\\dell\\Desktop\\APOLCMS\\eofficedata-13042022\\";
		
		String sql = "";
		Connection con = null;
		Statement st = null;
		int c = 0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			// con.setAutoCommit(false);

				for (String nicDataDist : nicDataDists) {

					if (nicDataDist != null && !nicDataDist.equals("")) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
					Date d1 = new Date();
					String mmyyyy = sdf.format(d1);
					
					File file = new File(filesPath+nicDataDist+".txt");
					FileReader readfile = new FileReader(file);

					String tableName = "nic_data_"+ mmyyyy.trim()+ "_"+ (file.getName().substring(0,(file.getName().lastIndexOf(".")))).toLowerCase() + "";
					
					// System.out.println("select count(*) from " + tableName);
					// System.out.println("GO");
					
					// System.out.println("pg_dump -t "  + tableName+" -U apolcms apolcms  > E:\\HighCourtsDumps\\eofficedata-16042022\\"+tableName+".sql");
					// System.out.println("apolcms");
					
					System.out.println("psql -h 10.96.54.54 -p 6432 -U apolcms apolcms < E:\\HighCourtsDumps\\eofficedata-16042022\\"+tableName+".sql");
					System.out.println("@p0l(m$");
					
					/*
					c += createDbScript(tableName, con);
					
				    BufferedReader readbuffer = new BufferedReader(readfile);
				    
				    resp = readbuffer.readLine();
				      
					if(resp!=null && !resp.equals(""))
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
					}*/
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
			connection.setRequestMethod("GET");
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
			
			// System.out.println("SQL:"+sql);
			
			st = con.createStatement();
			a += st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SQL:"+sql);
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