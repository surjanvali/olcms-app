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

public class ChangeRequestsAbstractReportAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		String tableName = "nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			} else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg",
						"Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")
					|| roleId.trim().equals("1")) {

				deptId = CommonModels.checkStringObject(cform
						.getDynaForm("deptId"));
				con = DatabasePlugin.connect();
				if (roleId.trim().equals("3")) {

					sql = "select slno, 'MLO (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_change_requests a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " where substring(global_org_name,1,3)='"
							+ userId.substring(0, 3)
							+ "' ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) "
							+ " " + " where a.user_id='" + userId + "' and a.officer_type='MLO'" +
									" union " +
									" select slno, 'Nodal Officer (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description " +
									"from nodal_officer_change_requests a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " where substring(global_org_name,1,3)='"
							+ userId.substring(0, 3)
							+ "' ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) "
							+ " " + " where a.user_id='" + userId + "' and a.officer_type='NO'";

					request.setAttribute("HEADING",
							"Change Requests Registered Report ");

				} else if (roleId.trim().equals("2")) {

					sql = "select slno, 'MLO (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_change_requests a "
							+ "inner join ( "
							+ "select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ "inner join ( "
							+ "select distinct designation_id, designation_name_en from "
							+ tableName
							+ "  "
							+ ") c on (a.designation=c.designation_id) "
							+ "inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) "
							+ "where a.user_id='" + userId + "' and a.officer_type='MLO'" +
									" union " +
									" select slno, 'Nodal Officer (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_change_requests a "
							+ "inner join ( "
							+ "select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ "inner join ( "
							+ "select distinct designation_id, designation_name_en from "
							+ tableName
							+ "  "
							+ ") c on (a.designation=c.designation_id) "
							+ "inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) "
							+ "where a.user_id='" + userId + "'  and a.officer_type='NO'";

					request.setAttribute("HEADING",
							"Change Requests Registered Report ");

				} else if (roleId.trim().equals("1")) {

					sql = "select slno, 'MLO (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_change_requests a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " where substring(global_org_name,1,3)='"
							+ userId.substring(0, 3)
							+ "' ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) where a.officer_type='MLO'"
							+ " union " 
							+ " select slno, 'Nodal Officer (Legal)' as officer_type, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description " +
							"from nodal_officer_change_requests a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " where substring(global_org_name,1,3)='"
							+ userId.substring(0, 3)
							+ "' ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.user_id=d.sdeptcode||d.deptcode) where a.officer_type='NO'"
							+ " " + " ";

					request.setAttribute("HEADING", "Change Requests Registered Report ");
				}

				System.out.println("SQL:"+sql);
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("EMPWISENOS", data);
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