package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;

public class LoginStatusReportAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else if (!(roleId.trim().equals("1") || roleId.trim().equals("7"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("1") || roleId.trim().equals("7")) {

				con = DatabasePlugin.connect();
				if (roleId.trim().equals("1") || roleId.trim().equals("7")) {

					if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("DNO"))
					{

						sql = "select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin ,  "
								+ "case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays, "
								+ "u.user_description,r.role_name,  d.dept_code as dept_code, d.description from nodal_officer_details md "
								+ "inner join dept_new d on (md.dept_id=d.dept_code) inner join users u on (md.emailid=u.userid) "
								+ "inner join user_roles ur on (u.userid=ur.userid) inner join roles_mst r on (ur.role_id=r.role_id) "
								+ "left join (select user_id, min(login_time_date) as firstlogin,count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin from users_track_time group by user_id) a on (md.emailid=a.user_id) "
								+ "where coalesce(md.dist_id,0)> 0 order by d.dept_code";
	
						
						request.setAttribute("HEADING", "District Level Nodal Officer (Legal) Login Status Report ");
						
					}
					else if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("NO"))
					{

						sql = "select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin , case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays, u.user_description,r.role_name, "
								+ "d.dept_code as dept_code, d.description "
								+ "from nodal_officer_details m "
								+ "left join (select user_id, min(login_time_date) as firstlogin,count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin from users_track_time group by user_id) a on (m.emailid=a.user_id)"
								+ "inner join users u on (m.emailid=u.userid)"
								+ "inner join user_roles ur on (u.userid=ur.userid)"
								+ "inner join roles_mst r on (ur.role_id=r.role_id)"
								+ "right join (select * from dept_new where deptcode!='01') d on (u.dept_code=d.dept_code)"
								+ "order by d.dept_code";

						sql = "select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin ,  "
								+ " case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays,"
								+ " u.user_description,r.role_name,  d.dept_code as dept_code, d.description from nodal_officer_details md "
								+ " inner join dept_new d on (md.dept_id=d.dept_code) "
								+ " inner join users u on (md.emailid=u.userid) "
								+ " inner join user_roles ur on (u.userid=ur.userid) "
								+ " inner join roles_mst r on (ur.role_id=r.role_id) "
								+ " left join (select user_id, min(login_time_date) as firstlogin,count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin "
								+ " from users_track_time group by user_id) a on (md.emailid=a.user_id)"
								+ " where coalesce(md.dist_id,0)=0 order by d.dept_code";
	
						
						request.setAttribute("HEADING", "Nodal Officer (Legal) Login Status Report ");
						
					}
					else if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("GP"))
					{
						
						sql="select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin ,  "
								+ "case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays, "
								+ "u.user_description,r.role_name, md.designation as description from ecourts_mst_gps  md "
								+ " inner join users u on (md.emailid=u.userid) inner join user_roles ur on (u.userid=ur.userid) "
								+ "inner join roles_mst r on (ur.role_id=r.role_id) "
								+ "left join (select user_id, min(login_time_date) as firstlogin, count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin  "
								+ "from users_track_time group by user_id) a on (md.emailid=a.user_id) order by designation ";
						
						request.setAttribute("HEADING", "Government Pleders (GP) Login Status Report ");
						
					}
					else if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("MLOS"))
						{
						sql = "select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin , "
								+ " case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays, u.user_description,r.role_name, "
								+ " d.dept_code as dept_code, d.description from mlo_subject_details  md inner join dept_new d on (md.user_id=d.dept_code)"
								+ " inner join users u on (md.emailid=u.userid) inner join user_roles ur on (u.userid=ur.userid) inner join roles_mst r on (ur.role_id=r.role_id)"
								+ " left join (select user_id, min(login_time_date) as firstlogin,"
								+ " count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin "
								+ " from users_track_time group by user_id) a on (md.emailid=a.user_id) order by d.dept_code";
						
						request.setAttribute("HEADING", "MLO Subject Login Status Report ");
						
					}
					else {
						sql = "select u.userid, to_char(firstlogin,'DD/MM/YYYY') as firstlogin, loggedindays , to_char(lastlogin,'DD/MM/YYYY') as lastlogin , "
								+ " case when (lastlogin::date-firstlogin::date) > 1 then (lastlogin::date-firstlogin::date) - (loggedindays -1) else 0 end as notlogedindays, u.user_description,r.role_name, "
								+ " d.dept_code as dept_code, d.description from mlo_details md inner join dept_new d on (md.user_id=d.dept_code)"
								+ " inner join users u on (md.emailid=u.userid) inner join user_roles ur on (u.userid=ur.userid) inner join roles_mst r on (ur.role_id=r.role_id)"
								+ " left join (select user_id, min(login_time_date) as firstlogin,count(distinct login_time_date::date) as loggedindays, max(login_time_date) as lastlogin from users_track_time group by user_id) a on (md.emailid=a.user_id) order by d.dept_code";
	
						request.setAttribute("HEADING", "Middle Level Officer (Legal) Login Status Report ");
					}
				}

				System.out.println("MLOWISEDATA SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("MLOWISEDATA", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");

			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}