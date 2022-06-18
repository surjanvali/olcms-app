package in.apcfss.struts.eCourts.schedulars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class SMSAlertsJob {
	
	static final String apolcmsDataBase = "jdbc:postgresql://localhost:5432/apolcms";
	static final String apolcmsUserName = "apolcms";
	static final String apolcmsPassword = "apolcms";
	static String sql = "",mobileNo=null, smsText=null;
	static final String templateIdDCR="1307165537262664640", templateIdCC="1307165537167561013";
	
	public static void main(String[] args) throws SQLException {
		Connection con = null;
		Statement st =null; ResultSet rs = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);		
			
			/* CONTEMPT CASES SMS
			// 1. TO ALL District Collectors
			 * 2. TO ALL Secretaries
			 * 3. TO ALL MLOS
			 * 4. TO ALL NOS
			smsText="CONTEMPT CASE : 4 No. of CC registered in APOLCMS on 16-06-2022 "
					+ "District: "
					+ "Please visit https://apolcms.ap.gov.in for details. -APOLCMS Team, GOVTAP";
			System.out.println("smsText:"+smsText);
			System.out.println("mobileNo:"+mobileNo);
			mobileNo = "9618048663"; 
			SendSMSAction.sendSMS(mobileNo, smsText, templateIdCC, con);
			*/
			
			/* DIALY CASE REPORT OF ALLCASE TYPES- SMS
			// 1. TO ALL District Collectors
			 * 2. TO ALL Secretaries
			 * 3. TO ALL MLOS
			 * 4. TO ALL NOS
			 * 5. TO ALL DIST. NODAL OFFICERS
			*/
			
			/*ALL MLOs SQL*/
			sql="select description,mobileno,to_char(inserted_time,'dd-mm-yyyy') as inserted_time,  string_agg(case_short_name||'('||cases||')'  ,',') "
					+ " from ( "
					+ " select b.dept_code,dn.description,ctm.sno,casetype,ctm.case_short_name,mlo.mobileno,a.inserted_time::date,count(*) as cases from  ecourts_gpo_ack_dtls a  "
					+ " inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no) "
					+ " inner join case_type_master ctm on (a.casetype=ctm.sno::text) "
					+ " inner join dept_new dn on (b.dept_code=dn.dept_code) "
					+ " inner join mlo_details mlo on (mlo.user_id=b.dept_code) "
					+ " where a.inserted_time::date = current_date-4  "
					+ " group by ctm.sno,casetype,ctm.case_short_name,b.dept_code,dn.description,mlo.mobileno,a.inserted_time::date  order by b.dept_code,ctm.sno "
					+ " ) x group by description, mobileno, inserted_time "
					+ "";
			System.out.println("SQL:"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			sendDialyCasesReport(rs, con);
			
			/*State LEVEL NOS SQL*/
			sql="select description,mobileno,to_char(inserted_time,'dd-mm-yyyy') as inserted_time,  string_agg(case_short_name||'('||cases||')'  ,',')  as report "
					+ " from ("
					+ " select b.dept_code,dn.description,ctm.sno,casetype,ctm.case_short_name,nd.mobileno,a.inserted_time::date,count(*) as cases from  ecourts_gpo_ack_dtls a "
					+ " inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no)"
					+ " inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ " inner join dept_new dn on (b.dept_code=dn.dept_code)"
					+ " inner join nodal_officer_details nd on (nd.dept_id=b.dept_code and nd.dist_id=0)"
					+ " where a.inserted_time::date = current_date-3 "
					+ " group by ctm.sno,casetype,ctm.case_short_name,b.dept_code,dn.description,nd.mobileno,a.inserted_time::date order by b.dept_code,ctm.sno "
					+ " ) x group by description, mobileno, inserted_time";
			System.out.println("SQL:"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			sendDialyCasesReport(rs, con);
			
			/*District Nodal Officers SMS SQL*/
			sql="select description,mobileno,to_char(inserted_time,'dd-mm-yyyy') as inserted_time,  string_agg(case_short_name||'('||cases||')'  ,',')  as report"
					+ " from ("
					+ " select b.dept_code,dn.description,ctm.sno,casetype,ctm.case_short_name,nd.mobileno,a.inserted_time::date,count(*) as cases from  ecourts_gpo_ack_dtls a "
					+ " inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no)"
					+ " inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ " inner join dept_new dn on (b.dept_code=dn.dept_code)"
					+ " inner join nodal_officer_details nd on (nd.dept_id=b.dept_code and nd.dist_id=b.dist_id)"
					+ " where a.inserted_time::date = current_date-3 "
					+ " group by ctm.sno,casetype,ctm.case_short_name,b.dept_code,dn.description,nd.mobileno,a.inserted_time::date order by b.dept_code,ctm.sno"
					+ " ) x group by description, mobileno, inserted_time";
			System.out.println("SQL:"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			sendDialyCasesReport(rs, con);
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DatabasePlugin.closeConnection(con);
			if(con!=null) {
				con.close();
			}
		}
	}
	
	
	public static void sendDialyCasesReport(ResultSet rs, Connection con) throws Exception{
		while(rs.next())
		{
			mobileNo = CommonModels.checkStringObject(rs.getString("mobileno"));
			String casesReport = CommonModels.checkStringObject(rs.getString("report"));
			String report1="", report2="";
			if(casesReport.length() < 150) {
				report1 = casesReport.substring(0, casesReport.lastIndexOf(","));
				report2 = casesReport.substring(casesReport.lastIndexOf(",")+1);
			}
			
			smsText="Daily Cases Report (DCR) "
					+ report1 + " and "+report2
					+ " registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
					+ " Department :"+CommonModels.checkStringObject(rs.getString("description"))+" "
					+ " Please visit https://apolcms.ap.gov.in for details -APOLCMS Team, GOVTAP";
			
			System.out.println("smsText:"+smsText);
			mobileNo = "9618048663"; 
			SendSMSAction.sendSMS(mobileNo, smsText, templateIdDCR, con);
		}
	}
	
	public static void sendDialyCCReport(ResultSet rs, Connection con) throws Exception{
		while(rs.next())
		{
			mobileNo = CommonModels.checkStringObject(rs.getString("mobileno"));
			String casesReport = CommonModels.checkStringObject(rs.getString("report"));
			String report1="", report2="";
			if(casesReport.length() < 150) {
				report1 = casesReport.substring(0, casesReport.lastIndexOf(","));
				report2 = casesReport.substring(casesReport.lastIndexOf(",")+1);
			}
			
			smsText="CONTEMPT CASE "+
					CommonModels.checkStringObject(rs.getString("casescount"))+ " No.of CC registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
					+" Department :"+CommonModels.checkStringObject(rs.getString("description"))+" Please visit https://apolcms.ap.gov.in for details. -APOLCMS Team, GOVTAP";
			
			System.out.println("smsText:"+smsText);
			mobileNo = "9618048663"; 
			SendSMSAction.sendSMS(mobileNo, smsText, templateIdCC, con);
		}
	}
}