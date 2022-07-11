package in.apcfss.struts.eCourts.schedulars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;

import in.apcfss.struts.eCourt.actions.ImportJSONData2Table;
import in.apcfss.struts.eCourt.actions.PrayerDataBean;
import plugins.DatabasePlugin;

public class Job2 implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {

		String sql = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String resp = "",tableName="";
		int c = 0;
		try {
			// Class.forName("org.postgresql.Driver");
			con = DatabasePlugin.connect();
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
						result += ImportJSONData2Table.saveResult(tableName, con, ndb, slno);
					}
				}
			}
			System.out.println("Records Saved:"+result);
			
			System.out.println("END:"+ DatabasePlugin.getStringfromQuery("select now()", con));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DatabasePlugin.closeConnection(con);
			}
		}
	

	}
}
