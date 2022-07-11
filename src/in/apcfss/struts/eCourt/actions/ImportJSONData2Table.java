package in.apcfss.struts.eCourt.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import com.google.gson.Gson;

import plugins.DatabasePlugin;

public class ImportJSONData2Table {
	// final static String dbUrl = "jdbc:postgresql://10.96.54.54:6432/apolcms", dbUserName = "apolcms", dbPassword = "@p0l(m$";
	final static String dbUrl = "jdbc:postgresql://localhost:5432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";

	public static void main(String[] args) throws SQLException {
		String sql = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String resp = "",tableName="";
		int c = 0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			System.out.println("START:"+ DatabasePlugin.getStringfromQuery("select now()", con));
			
			sql = "select * from ecourts_nic_prayers where updated='f' limit 500";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			PrayerDataBean ndb = new PrayerDataBean();
			int result = 0, slno=0;
			while (rs.next()) {
				resp = rs.getString("prayer_data");
				slno = rs.getInt("slno");
				
				if (resp != null && !resp.equals("")) {
					JSONObject gsonObj = new JSONObject(resp);
					
					if (gsonObj != null) {
						ndb = new Gson().fromJson(gsonObj.toString(), PrayerDataBean.class);
						result += saveResult(tableName, con, ndb, slno);
					}
				}
			}
			System.out.println("Records Saved:"+result);
			
			System.out.println("END:"+ DatabasePlugin.getStringfromQuery("select now()", con));
			
			
			/*
			// con.setAutoCommit(false);
			// File file = new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\testdataextract.txt");
			File file = new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\dept_pending_prayer.txt");
			// File file = new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\dept_disposed_prayer.txt");
			// File file = new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\dept_pending_prayer.txt");
			// File file = new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\dept_disposed_prayer.txt");
			
			FileReader readfile = new FileReader(file);
			BufferedReader readbuffer = new BufferedReader(readfile);
			resp = readbuffer.readLine();
			System.out.println("resp:"+resp);
			
			if (resp != null && !resp.equals("")) {
				JSONArray empArray = new JSONArray(resp);
				PrayerDataBean ndb = new PrayerDataBean();
				String tableName = "";
				if (empArray != null) {
					System.out.println("Length:"+empArray.length());
					JSONObject gsonObj = new JSONObject();
					for (int i = 0; i < empArray.length(); i++) {
						System.out.println(i+"::"+empArray.get(i));
						
						gsonObj = (JSONObject) empArray.get(i);

						if (gsonObj != null) {
							ndb = new Gson().fromJson(gsonObj.toString(), PrayerDataBean.class);
							int result = saveResult(tableName, con, ndb);
							if (result == empArray.length()) {
								//con.commit();
								System.out.println("SUCCESS");
							}
							 else { i = empArray.length(); System.out.println("EXIT"); //con.rollback(); }
						}
					}
				}
				System.out.println("Data saved for:" + tableName);
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	public static int saveResult(String tableName, Connection con, PrayerDataBean ndb, int recordId) throws SQLException {
		int a = 0;
		String sql = "";
		Statement st = null;
		try {
			
			sql = "insert into ecourts_case_prayers_data (cino , case_type , reg_year , reg_no , subnature1_desc, prayer_desc ) "
					+ "" + " values " + "('" + checkStringObject(ndb.getCino()) + "', '"
					+ checkStringObject(ndb.getAsreg_case()) + "', '" + checkStringObject(ndb.getReg_year()) + "', '"
					+ checkStringObject(ndb.getReg_no()) + "', '" + checkStringObject(ndb.getSubnature1_desc()) + "', '"
					+ checkStringObject(ndb.getPrayer()) + "')" + "";
			// System.out.println("Insert SQL:" + sql);
			a=DatabasePlugin.executeUpdate(sql, con);
			
			/*
			 * sql =
			 * "insert into ecourts_case_resp_addrs (cino, party_no, res_name, res_addr ) "
			 * + "" + " values " + "('" + checkStringObject(ndb.getCino()) + "', '" +
			 * checkStringObject(ndb.getParty_no()) + "', '" +
			 * checkStringObject(ndb.getRes_name()) + "', '" +
			 * checkStringObject(ndb.getRes_addr()) + "')" + "";
			 */
			// System.out.println("SQL:"+sql);

			sql="update  ecourts_nic_prayers set updated='t' where slno="+recordId+" and updated='f'";
			DatabasePlugin.executeUpdate(sql, con);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SQL:" + sql);
			return 0;

		} finally {
			if (st != null)
				st.close();
		}

		return a;
	}

	public static String checkStringObject(Object myVal) {

		return (myVal != null && !myVal.equals("")) ? myVal.toString().replace("'", " ") : "";

	}
}
