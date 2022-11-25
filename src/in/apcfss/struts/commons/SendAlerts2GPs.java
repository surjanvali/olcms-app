package in.apcfss.struts.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class SendAlerts2GPs {
	final static String dbUrl = "jdbc:postgresql://localhost:5432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
	
	//final static String dbUrl = "jdbc:postgresql://172.16.98.2:9432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
	
	public static void main(String[] args) {
		String sql = "", mobileNo=null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			sql="select mobile_no, department as emailid from ecourts_mst_gps order by slno ";
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			System.out.println("sql--"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String smsText="Your User Id is "+rs.getString("emailid")+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
				String templateId="1007784197678878760";
				mobileNo = rs.getString("mobile_no");
				
				System.out.println("sql--"+mobileNo+smsText);
				
				if(mobileNo!=null && !mobileNo.equals("")) {
					 //mobileNo = "9618048663";
					// mobileNo = "8500909816";
					System.out.println("mobileNo::"+mobileNo);
					SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
