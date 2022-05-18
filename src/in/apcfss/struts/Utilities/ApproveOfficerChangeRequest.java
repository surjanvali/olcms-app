package in.apcfss.struts.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class ApproveOfficerChangeRequest {
	// final static String dbUrl = "jdbc:postgresql://localhost:5432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
	final static String dbUrl = "jdbc:postgresql://10.96.54.54:6432/apolcms", dbUserName = "apolcms", dbPassword = "@p0l(m$";

	public static void main(String[] args) throws Exception {
		String sql = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String oficerType = "", deptCode = "", emailId = "", mobileNo = "";int distId=0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			con.setAutoCommit(false);

			sql = "SELECT user_id, dept_id, designation, employeeid, mobileno, emailid, aadharno, change_reasons, change_letter_path, change_req_approved, "
					+ " inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time, officer_type, slno, dist_id "
					+ " FROM apolcms.nodal_officer_change_requests where "
					+ " change_req_approved is false and "
					+ " officer_type='MLO' and dept_id='REV01'";//and officer_type='NO' and dept_id='REV02'";
			
			System.out.println("SQL:"+sql);
			
			st = con.createStatement();
			rs = st.executeQuery(sql);
			int a = 0;
			while (rs.next()) {
				oficerType = rs.getString("officer_type");
				deptCode = rs.getString("dept_id");
				emailId = rs.getString("emailid");
				mobileNo = rs.getString("mobileno");
				distId = CommonModels.checkIntObject(rs.getString("dist_id"));
				
				System.out.println("oficerType:"+oficerType);
				System.out.println("deptCode:"+deptCode);
				System.out.println("emailId:"+emailId);
				System.out.println("mobileNo:"+mobileNo);
				System.out.println("distId:"+distId);
				
				if(Integer.parseInt(DatabasePlugin.getStringfromQuery("select count(*) from users where userid='"+emailId+"'", con)) == 0) {
				
				if(CommonModels.checkStringObject(oficerType).equals("MLO")) {
					
					sql="insert into mlo_details_deleted(deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+rs.getString("inserted_ip")+"', now(),slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.mlo_details where user_id='"+deptCode+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					String existingId = DatabasePlugin.getSingleValue(con, "select emailid FROM apolcms.mlo_details where user_id='"+deptCode+"'");
					System.out.println("existingId:"+existingId);	
					
					sql="delete from user_roles where userid='"+existingId+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from users where userid='"+existingId+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from mlo_details where user_id='"+deptCode+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into mlo_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip) "
							+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip from nodal_officer_change_requests where user_id='"+deptCode+"' and change_req_approved is false and officer_type='MLO'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type) "
							+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
							+ "', now(), null, dm.dept_id,'" + deptCode
							+ "', '4'  from mlo_details a inner join (select distinct employee_id,fullname_en,designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
							+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
							+ "where employeeid='" + rs.getString("employeeid") + "'";
					
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into user_roles (userid, role_id) values ('"+emailId+"','4')";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				else if(CommonModels.checkStringObject(oficerType).equals("NO")) {
				
					sql="insert into apolcms.nodal_officer_details_delete (deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+rs.getString("inserted_ip")+"', now(), slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId;
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from user_roles where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from users where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
										
					sql="delete from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
						
					sql="insert into nodal_officer_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, dist_id, dept_id) "
								+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, coalesce(dist_id,0), dept_id from nodal_officer_change_requests "
								+ "where dept_id='"+deptCode+"'  and change_req_approved is false and officer_type='NO' and coalesce(dist_id,0)="+distId+"";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					int userRole=10;
					if(distId > 0)
						userRole=5;
					
					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type, dist_id) "
							+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
							+ "', now(), null, dm.dept_id,'" + deptCode
							+ "', '"+userRole+"', dist_id from nodal_officer_details a inner join (select distinct employee_id,fullname_en,designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
							+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
							+ "where employeeid='" + rs.getString("employeeid") + "'";
					
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into user_roles (userid, role_id) values ('"+emailId+"','"+userRole+"')";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				System.out.println("executed SQLS :"+a);
				if(a > 0) {
					String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
					String templateId="1007784197678878760";
					System.out.println("smsText:"+smsText);
					System.out.println("mobileNo:"+mobileNo);
					// mobileNo = "9618048663"; 
					// SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
				}
				}
				else {
					System.out.println("USER ALREADY EXISTS WITH DIFFERENT ROLE");
				}
			}
			con.commit();	
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		} finally {
			if(con!=null) {
				con.close();
			}
		}
	}
}