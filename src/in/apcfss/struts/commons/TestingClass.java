package in.apcfss.struts.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestingClass {

	//final static String dbUrl = "jdbc:postgresql://172.16.98.2:9432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";

	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		try {
			//Class.forName("org.postgresql.Driver");
			//conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			//SendSMSAction.sendSMS("9618048663", "Mobile OTP for Login into CFMS helpdesk:45678", "1007713986799127731", null);
			String deptCode="AGC02";
			System.out.println(deptCode.substring(3,5));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
}
