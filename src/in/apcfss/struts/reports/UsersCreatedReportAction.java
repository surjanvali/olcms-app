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

public class UsersCreatedReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			sql = "select ur.role_id,upper(rm.role_name) as role_name,count(*) as userscount from user_roles ur inner join roles_mst rm on (ur.role_id=rm.role_id) group by ur.role_id,rm.role_name order by ur.role_id";

			request.setAttribute("HEADING", "Role Wise Existing Users Report.");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("ROLEUSERS", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getRoleWiseUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		CommonForm cForm = (CommonForm) form;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			String usersRoleId = (String)request.getParameter("roleId"); //cForm.getDynaForm("roleId")

			sql = "select * from users u inner join user_roles ur on (u.userid=ur.userid) where ur.role_id='"+usersRoleId+"' order by u.userid,u.created_on";

			String userRoleType="";
			if(usersRoleId!=null && usersRoleId.equals("4"))
				userRoleType="MLO";
			else if(usersRoleId!=null && usersRoleId.equals("5"))
				userRoleType="NO";
			else if(usersRoleId!=null && usersRoleId.equals("8"))
				userRoleType="SECTION";
			
			request.setAttribute("HEADING", "Existing Users Report for "+" Role.");

			request.setAttribute("userRoleType", userRoleType);
			
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("USERSLIST", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
}
