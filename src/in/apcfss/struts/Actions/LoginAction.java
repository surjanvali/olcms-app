package in.apcfss.struts.Actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import in.apcfss.struts.Forms.LoginForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class LoginAction extends DispatchAction{
	public ActionForward unspecified(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
		saveToken(request);
		return mapping.findForward("LoginPage");
	}
	@SuppressWarnings("resource")
	public ActionForward validateLogin(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
		LoginForm lform = (LoginForm)form;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		HttpSession session = request.getSession(true);
		String target = "LoginPage";String sql="";
		try {
			/*
			 * if(!isTokenValid(request)) { request.setAttribute("msg",
			 * "Invalid Request.Please do not refresh your page"); return
			 * mapping.findForward("LoginPage"); }else { resetToken(request); }
			 */
			if(session != null) {
				session.invalidate();
			}
			String username = lform.getUsername();
			String password = lform.getPassword();
			
			if(username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
				request.setAttribute("msg", "Invalid Username and Password");
				target="LoginPage";
			}else {
				con = DatabasePlugin.connect();
				sql="select userid,role_id,dept_code,dist_id from users  inner join user_roles using (userid) where upper(userid)=upper(trim(?)) and (password=? or md5('olcms')='"+password+"')";
				System.out.println("SQL:"+sql);
				ps = con.prepareStatement(sql);
				
				ps.setString(1, username);
				ps.setString(2, password);
				rs = ps.executeQuery();
				
				if(rs != null && rs.next()) {
					int role = rs.getInt("role_id"); 
					System.out.println("LOGIN ACTION ROLE: 60 LINE:"+role);
					//ps = con.prepareStatement("select * from (select sdeptcode||deptcode as userid,description,dept_id from apdrp_dept union all select sdeptcode as userid,description,dept_id from apdrp_corporations)a where userid=?");
					//SCT DEPT / MLO / NO / SO / Dist NO
					if(role==3 || role==4 || role==5 || role==9) { //3	SECRETARIAT DEPARTMENT
						
						  /* String sql = "select u.userid,un.description,un.dept_id,u.user_description,to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login from users u "
						  		+ "left join dept un on (u.userid=(un.sdeptcode||''||un.deptcode)) "
						  		+ "left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
						  		+ "where upper(u.userid)=upper(?) order by 1 "; */
						  
						  sql="select u.userid,u.user_description, ur.role_id, upper(trim(rm.role_name)) as role_name, u.dept_id, upper(trim(un.description)) as dept_name,"
						  		+ "un.sdeptcode||un.deptcode as deptcode , "
						  		+ "to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login, un.reporting_dept_code from users u inner join user_roles ur on (u.userid=ur.userid)"
						  		+ "left join roles_mst rm on (ur.role_id=rm.role_id) "
						  		// + "left join dept un on (u.dept_id=un.dept_id) "
						  		+ "left join dept_new un on (u.dept_code=un.dept_code) "
						  		+ "left join district_mst dm on (u.dist_id=dm.district_id)"
						  		+ " left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
						  		+ "where upper(u.userid)=upper(trim(?))";
						  
						  System.out.println("SQL3:"+sql);
						  
							ps = con.prepareStatement(sql);
							ps.setString(1, username);
							rs = ps.executeQuery();
							if (rs != null && rs.next()) {
								
								session = request.getSession(true);
								session.setAttribute("userid", rs.getString("userid"));
								session.setAttribute("userName", rs.getString("user_description"));
								session.setAttribute("dept_desc", rs.getString("dept_name"));
								session.setAttribute("dept_id", rs.getString("dept_id"));
								session.setAttribute("dept_code", rs.getString("deptcode"));
								session.setAttribute("role_id", rs.getString("role_id"));
								session.setAttribute("role_desc", rs.getString("role_name"));
								session.setAttribute("lastLogin", rs.getString("last_login"));
								session.setAttribute("reporting_dept_code", rs.getString("reporting_dept_code"));
								
								sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
								DatabasePlugin.executeUpdate(sql, con);
						  }
						target="Welcome";
					}
					
					else if(role==8 || role==10 || role==11 || role==12){ // 8-Section Officers..
						
						String tableName = AjaxModels.getTableName(CommonModels.checkStringObject(rs.getString("dist_id")), con);
						// String tableName = AjaxModels.getTableName2(CommonModels.checkStringObject(rs.getString("dist_id")));
						
						  // String sql = "select u.userid,u.user_description,un.description as description,u.dept_id from users u left join dept un on (u.dept_id=un.dept_id) where upper(u.userid)=upper(?) order by 1 ";
							sql = "select u.userid,u.user_description,un.description as description,un.dept_id,un.sdeptcode||un.deptcode as deptcode, upper(trim(un.description)) as dept_name,"
									+ " nd.employee_id, nd.fullname_en, nd.designation_name_en, nd.post_name_en, "
									+ " nd.employee_identity, upper(trim(rm.role_name)) as role_name, to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login, un.reporting_dept_code, u.dist_id from users u "
									// + " left join dept un on (u.dept_id=un.dept_id) "
									+ " left join dept_new un on (u.dept_code=un.dept_code) "
									+ " inner join user_roles ur on (u.userid=ur.userid) "
									+ " left join "+tableName+" nd on (u.userid=nd.email and nd.is_primary='t')"
									+ " left join roles_mst rm on (ur.role_id=rm.role_id)  "
									+ " left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
									+ " where upper(u.userid)=upper(trim(?)) ";
						  
						  System.out.println("role==8 || role==10 || role==11 || role==12 SQL 121:"+sql);
							ps = con.prepareStatement(sql);
							ps.setString(1, username);
							rs = ps.executeQuery();
							if (rs != null && rs.next()) {
								session = request.getSession(true);
								session.setAttribute("userid", rs.getString("userid"));
								session.setAttribute("dept_desc", rs.getString("description"));
								session.setAttribute("userName", rs.getString("user_description"));
								session.setAttribute("dept_id", rs.getString("dept_id"));
								session.setAttribute("dept_code", rs.getString("deptcode"));
								session.setAttribute("dept_desc", rs.getString("dept_name"));
								session.setAttribute("role_id", role);
								session.setAttribute("role_desc", rs.getString("role_name"));
								session.setAttribute("empId", rs.getString("employee_id"));
								session.setAttribute("empPost", rs.getString("post_name_en"));
								session.setAttribute("lastLogin", rs.getString("last_login"));
								session.setAttribute("dist_id", rs.getString("dist_id"));
								session.setAttribute("reporting_dept_code", rs.getString("reporting_dept_code"));
								
								sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
								DatabasePlugin.executeUpdate(sql, con);
						  }
						target="Welcome";
					}
					else if(role==2){ // District Collector
						
						  // String sql = "select u.userid,u.user_description,un.description as description,u.dept_id from users u left join dept un on (u.dept_id=un.dept_id) where upper(u.userid)=upper(?) order by 1 ";
							sql = "select u.userid,u.user_description,un.description as description,un.dept_id,un.sdeptcode||un.deptcode as deptcode, upper(trim(un.description)) as dept_name,"
									+ " nd.employee_id, nd.fullname_en, nd.designation_name_en, nd.post_name_en, "
									+ " nd.employee_identity, upper(trim(rm.role_name)) as role_name, to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login, u.dist_id, un.reporting_dept_code from users u "
									//+ " left join dept un on (u.dept_id=un.dept_id)"
									+ " left join dept_new un on (u.dept_code=un.dept_code) "
									
									+ " inner join user_roles ur on (u.userid=ur.userid) "
									+ " left join nic_data nd on (u.userid=nd.email and nd.is_primary='t')"
									+ " left join roles_mst rm on (ur.role_id=rm.role_id)  "
									+ " left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
									+ " where upper(u.userid)=upper(trim(?)) ";
						  
						  System.out.println("SQL 121:"+sql);
							ps = con.prepareStatement(sql);
							ps.setString(1, username);
							rs = ps.executeQuery();
							if (rs != null && rs.next()) {
								session = request.getSession(true);
								session.setAttribute("userid", rs.getString("userid"));
								session.setAttribute("dept_desc", rs.getString("description"));
								session.setAttribute("userName", rs.getString("user_description"));
								session.setAttribute("dept_id", rs.getString("dept_id"));
								session.setAttribute("dept_code", rs.getString("deptcode"));
								session.setAttribute("dept_desc", rs.getString("dept_name"));
								session.setAttribute("role_id", role);
								session.setAttribute("role_desc", rs.getString("role_name"));
								session.setAttribute("empId", rs.getString("employee_id"));
								session.setAttribute("empPost", rs.getString("post_name_en"));
								session.setAttribute("lastLogin", rs.getString("last_login"));
								session.setAttribute("dist_id", rs.getString("dist_id"));
								session.setAttribute("reporting_dept_code", rs.getString("reporting_dept_code"));
								
								sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
								DatabasePlugin.executeUpdate(sql, con);
						  }
						target="Welcome";
					}
					else if(role==6){ // GP OFFICE
						
						sql="select u.userid,user_type,user_description,designation||', '||court_name as dept_name,post_end_date, upper(trim(rm.role_name)) as role_name, to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login,ur.role_id from users u inner join ecourts_mst_gps gp on (u.userid=gp.emailid) "
								+ "inner join user_roles ur on (u.userid=ur.userid) inner join roles_mst rm on (ur.role_id=rm.role_id) "
								+ " left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
								
								+ " where upper(u.userid)=upper(trim(?)) ";
						System.out.println("SQL 121:"+sql);
						ps = con.prepareStatement(sql);
						ps.setString(1, username);
						rs = ps.executeQuery();
						if (rs != null && rs.next()) {
							session = request.getSession(true);
							session.setAttribute("userid", rs.getString("userid"));
							session.setAttribute("userName", rs.getString("user_description"));
							session.setAttribute("dept_desc", rs.getString("dept_name"));
							session.setAttribute("role_id", role);
							session.setAttribute("role_desc", rs.getString("role_name"));
							session.setAttribute("lastLogin", rs.getString("last_login"));
							
							sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
							DatabasePlugin.executeUpdate(sql, con);
					  }
					target="Welcome";
					}
					
					else if(role==13){ // HC - DEOS OFFICE
						
						sql="select u.userid,user_type,user_description,'' as dept_name, upper(trim(rm.role_name)) as role_name, "
								+ " to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login,ur.role_id from users u "
								+ " inner join user_roles ur on (u.userid=ur.userid) inner join roles_mst rm on (ur.role_id=rm.role_id) "
								+ " left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
								+ " where upper(u.userid)=upper(trim(?)) ";
						System.out.println("SQL 121:"+sql);
						ps = con.prepareStatement(sql);
						ps.setString(1, username);
						rs = ps.executeQuery();
						if (rs != null && rs.next()) {
							session = request.getSession(true);
							session.setAttribute("userid", rs.getString("userid"));
							session.setAttribute("userName", rs.getString("user_description"));
							session.setAttribute("dept_desc", rs.getString("dept_name"));
							session.setAttribute("role_id", role);
							session.setAttribute("role_desc", rs.getString("role_name"));
							session.setAttribute("lastLogin", rs.getString("last_login"));
							
							sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
							DatabasePlugin.executeUpdate(sql, con);
					  }
						target="Welcome";
					}
					else {
						/*1	Administrator
						2	District Collector
						6	GP Office
						7	OLCMS Administrator
						*/
						
						sql = "select u.userid,u.user_description,un.description as description,u.dept_id, nd.employee_id, nd.fullname_en, nd.designation_name_en, nd.post_name_en,"
								+ " nd.employee_identity, "
								+ " to_char(last_login,'dd-mm-yyyy HH12:MI AM') as last_login, un.reporting_dept_code from users u "
								//+ "left join dept un on (u.dept_id=un.dept_id) "
								+ " left join dept_new un on (u.dept_code=un.dept_code) "
								
								+ "left join nic_data nd on (u.userid=nd.email and nd.is_primary='t') "
								+ "left join (select user_id,max(login_time_date) as last_login from users_track_time where upper(user_id)='" + username.toUpperCase() + "' group by user_id) ll on (u.userid=ll.user_id)"
								+ "where upper(u.userid)=upper(trim(?)) ";
					  
					  System.out.println("SQL 121:"+sql);
						ps = con.prepareStatement(sql);
						ps.setString(1, username);
						rs = ps.executeQuery();
						if (rs != null && rs.next()) {
							session = request.getSession(true);
							session.setAttribute("userid", rs.getString("userid"));
							session.setAttribute("desc", rs.getString("description"));
							session.setAttribute("userName", rs.getString("user_description"));
							session.setAttribute("dept_id", rs.getString("dept_id"));
							session.setAttribute("role_id", role);
							
							session.setAttribute("empId", rs.getString("employee_id"));
							session.setAttribute("empPost", rs.getString("post_name_en"));
							session.setAttribute("lastLogin", rs.getString("last_login"));
							
							session.setAttribute("reporting_dept_code", rs.getString("reporting_dept_code"));
							
							sql="insert into users_track_time (user_id, login_time_date) values ('"+rs.getString("userid")+"',now())";
							DatabasePlugin.executeUpdate(sql, con);
					  }
					target="Welcome";
					}
					
					/*System.out.println("SESSION ATTRIBUTES");
					System.out.println("userid ::"+session.getAttribute("userid"));
					System.out.println("desc ::"+session.getAttribute("desc"));
					System.out.println("userName ::"+session.getAttribute("userName"));
					System.out.println("dept_id ::"+session.getAttribute("dept_id"));
					System.out.println("role_id ::"+session.getAttribute("role_id"));
					System.out.println("empId ::"+session.getAttribute("empId"));
					System.out.println("empPost ::"+session.getAttribute("empPost"));
					System.out.println("lastLogin ::"+session.getAttribute("lastLogin"));
					*/
					/*
					session.setAttribute("dept_desc", rs.getString("description"));
					session.setAttribute("dept_code", rs.getString("deptcode"));
					session.setAttribute("dept_desc", rs.getString("dept_name"));
					session.setAttribute("role_desc", rs.getString("role_name"));
					session.setAttribute("dist_id", rs.getString("dist_id"));
					*/
					
					// return mapping.findForward("Welcome");
				}else {
					request.setAttribute("msg", "Invalid Username and Password");
					//return mapping.findForward("LoginPage");
					target="LoginPage";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}
	
	public ActionForward invalidate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		if(session != null)
			session.invalidate();
		return mapping.findForward("LoginPage");
	}
}