package in.apcfss.struts.eCourts.schedulars;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class SMSAlertsJob implements Job{
	
	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("SMSAlertsJob Execution :"+new Date());
		try {
			updateData(context);
		} catch (JobExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	static final String apolcmsDataBase = "jdbc:postgresql://localhost:5432/apolcms";
	static final String apolcmsUserName = "apolcms";
	static final String apolcmsPassword = "apolcms";
	*/
	static String sql = "",mobileNo=null, smsText=null;
	static final String templateIdDCR="1307165537262664640", templateIdCC="1307165537167561013";
	
	//public static void main(String[] args) throws SQLException {
	public synchronized void updateData(JobExecutionContext context) throws JobExecutionException, SQLException {
		Connection con = null;
		Statement st =null; ResultSet rs = null;
		try {
			// Class.forName("org.postgresql.Driver");
			// con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);		
			con = DatabasePlugin.connect();		
			int schedularId = Integer.parseInt(DatabasePlugin.selectString("select max(coalesce(slno,0))+1 from ecourts_schedulars", con));
			DatabasePlugin.executeUpdate("insert into ecourts_schedulars (slno, schedular_name, schedular_start_time ) values ('"+schedularId+"','SMSAlertsJobSchedular', now())", con);
			
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
			// MLO CCs SMS 
			sql=" select  dept_code::text,description::text,mobileno::text,report, inserted_time,district_name from ("
					+ "	select dept_code::text,description::text,mobileno,string_agg(case_short_name||'('||total_cases||')'  ,',') as report,inserted_time,district_name from"
					+ "	(( select dn.dept_code,dn.description,nod.mobileno,ctm.case_short_name,to_char(a.inserted_time,'dd-mm-yyyy') as inserted_time,'' as district_name,count(*) as total_cases from ecourts_gpo_ack_dtls a "
					+ "	inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no)  "
					+ "	inner join dept_new dn on (b.dept_code=dn.dept_code) "
					+ "	inner join nodal_officer_details nod on (nod.dept_id=b.dept_code )"
					+ "	inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ "	where mobileno is not null  "
					+ "	group by dn.dept_code,dn.description,nod.mobileno,district_name,case_short_name,a.inserted_time)"
					+ "	union all"
					+ "	(select b.dept_code,dn.description,md.mobileno,ctm.case_short_name,to_char(a.inserted_time,'dd-mm-yyyy') as inserted_time,'' as district_name ,count(*) as total_cases from ecourts_gpo_ack_dtls a "
					+ "	inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no)  "
					+ "	inner join dept_new dn on (b.dept_code=dn.dept_code) "
					+ "	inner join mlo_details md on (md.user_id=dn.dept_code)"
					+ "	inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ "	where mobileno is not null"
					+ "	group by b.dept_code,dn.description,md.mobileno,district_name,case_short_name,a.inserted_time)"
					+ "	union all"
					+ "	(select 0::text as dept_code,0::text as description,dm.mobile_no::text as mobileno,ctm.case_short_name::text,to_char(a.inserted_time,'dd-mm-yyyy') as inserted_time,dm.district_name,count(*) as total_cases from ecourts_gpo_ack_dtls a "
					+ "	inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no) "
					+ "	inner join district_mst dm on (dm.district_id=b.dist_id)"
					+ "	inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ "	where dm.mobile_no is not null"
					+ "	group by dept_code,description,dm.mobile_no,dm.district_name,case_short_name,a.inserted_time)"
					+ "	union all"
					+ "	(select b.dept_code::text,dn.description::text,nod.mobileno::text,ctm.case_short_name::text,to_char(a.inserted_time,'dd-mm-yyyy') as inserted_time,(select district_name from district_mst c where c.district_id=b.dist_id) as district_name,count(*) as total_cases from ecourts_gpo_ack_dtls a "
					+ "	inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no)  "
					+ "	inner join dept_new dn on (b.dept_code=dn.dept_code) "
					+ "	inner join nodal_officer_details nod on (nod.dept_id=b.dept_code and nod.dist_id=b.dist_id)"
					+ "	inner join case_type_master ctm on (a.casetype=ctm.sno::text)"
					+ "	where mobileno is not null "
					+ "	group by b.dept_code,dn.description,nod.mobileno,district_name,case_short_name,a.inserted_time )) k"
					+ "	group by dept_code,description,mobileno,inserted_time,district_name"
					+ "	) x where  x.dept_code='AGC02'  and x.inserted_time::date =to_date('01-07-2022','dd-mm-yyyy') "
					+ "  group by dept_code,description,mobileno,inserted_time,district_name,report  ";
			
			System.out.println("SQL:"+sql);
			
			st = con.createStatement();
			rs = st.executeQuery(sql);
			sendDialyCasesReport(rs, con);
			
						DatabasePlugin.executeUpdate("update ecourts_schedulars set schedular_end_time=now() where slno='"+schedularId+"')", con);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
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
			String report1 = "", report2 = "";
			if(casesReport.length() <= 150) {
				if(casesReport.contains(",")) {
					report1 = casesReport.substring(0, casesReport.lastIndexOf(","));
					report2 = casesReport.substring(casesReport.lastIndexOf(",")+1);
				}
				else {
					report1 = casesReport;
					report2 = "-";
				}
			}
			if(mobileNo!=null && !mobileNo.equals("")) {
				smsText="Daily Cases Report (DCR) : "
						+ report1 + " and "+report2
						+ " registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
						+ " Department :"+CommonModels.checkStringObject(rs.getString("description"))+" "
						+ " Please visit https://apolcms.ap.gov.in for details -APOLCMS Team, GOVTAP";
				
				System.out.println("smsText:"+smsText);
				//mobileNo = "9618048663"; 
				SendSMSAction.sendSMS(mobileNo, smsText, templateIdDCR, con);
			}
		}
	}
	
	public static void sendDialyCCReport(ResultSet rs, Connection con) throws Exception{
		while(rs.next())
		{
			mobileNo = CommonModels.checkStringObject(rs.getString("mobileno"));
			if(mobileNo!=null && !mobileNo.equals("")) {
				smsText="CONTEMPT CASE : "+
						CommonModels.checkStringObject(rs.getString("casescount"))+ " No.of CC registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
						+" Department :"+CommonModels.checkStringObject(rs.getString("description"))+" Please visit https://apolcms.ap.gov.in for details. -APOLCMS Team, GOVTAP";
				
				System.out.println("smsText:"+smsText);
				//mobileNo = "9618048663"; 
				SendSMSAction.sendSMS(mobileNo, smsText, templateIdCC, con);
			}
		}
	}
	
	public static void sendDialyCasesReportDC(ResultSet rs, Connection con) throws Exception{
		while(rs.next())
		{
			mobileNo = CommonModels.checkStringObject(rs.getString("mobileno"));
			String casesReport = CommonModels.checkStringObject(rs.getString("report"));
			String report1="", report2="";
			if(casesReport.length() <= 150) {
				if(casesReport.contains(",")) {
					report1 = casesReport.substring(0, casesReport.lastIndexOf(","));
					report2 = casesReport.substring(casesReport.lastIndexOf(",")+1);
				}
				else {
					report1 = casesReport;
					report2 = "-";
				}
			}
			
			if(mobileNo!=null && !mobileNo.equals("")) {
			smsText="Daily Cases Report (DCR) : "
					+ report1 + " and "+report2
					+ " registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
					+ " Department : ALL and District :"+CommonModels.checkStringObject(rs.getString("district_name"))+" "
					+ " Please visit https://apolcms.ap.gov.in for details -APOLCMS Team, GOVTAP";
			
			System.out.println("smsText:"+smsText);
			//mobileNo = "9618048663"; 
			SendSMSAction.sendSMS(mobileNo, smsText, templateIdDCR, con);
			}
		}
	}
	
	public static void sendDialyCCReportDC(ResultSet rs, Connection con) throws Exception{
		while(rs.next())
		{
			mobileNo = CommonModels.checkStringObject(rs.getString("mobileno"));
			if(mobileNo!=null && !mobileNo.equals("")) {
				smsText="CONTEMPT CASE : "+
						CommonModels.checkStringObject(rs.getString("casescount"))+ " No.of CC registered in APOLCMS on "+CommonModels.checkStringObject(rs.getString("inserted_time"))
						+" Department : ALL and District :"+CommonModels.checkStringObject(rs.getString("district_name"))+" Please visit https://apolcms.ap.gov.in for details. -APOLCMS Team, GOVTAP";
				
				System.out.println("smsText:"+smsText);
				//mobileNo = "9618048663"; 
				SendSMSAction.sendSMS(mobileNo, smsText, templateIdCC, con);
			}
		}
	}
}