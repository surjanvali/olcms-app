package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class UpdateCaseDeptAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		CommonForm cform = (CommonForm) form;
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

			sql = "select dept_name, dept_code, count(*), trim(regexp_replace(dept_name, '\\W', '', 'g')) as rowid from ecourts_cinos_1705_2305 "
					//+ "where inserted_time::date = current_date"
					+ " group by dept_name,dept_code order by 2 desc,3 desc";
			request.setAttribute("HEADING", "Update Case Department");

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			cform.setDynaForm(
					"dept_list",
					DatabasePlugin
							.getSelectBox(
									"select dept_code as dept_code,dept_code||' - '||upper(description) as description from dept_new order by dept_code",
									con));
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("UpdateCase", data);

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

	public ActionForward submitdetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		CommonForm cform = (CommonForm) form;
		PreparedStatement ps = null;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			String dept_name = cform.getDynaForm("rowVal").toString();
			String dept_code = (String) (cform.getDynaForm("dept_select"
					+ dept_name) != null ? cform.getDynaForm("dept_select"
					+ dept_name) : 0);
			System.out.println("dptcode:::::::::::::::::::::::::::" + dept_code);
			System.out.println("dept_name:::::::::::::::::::::::::::" + dept_name);

			int i = 0;

			if (dept_name != null && !dept_name.equals("") && dept_code != null
					&& !dept_code.equals("0") && !dept_code.equals(""))
			{
				String deptId=DatabasePlugin.getStringfromQuery("select dept_id from dept_new where dept_code='"+dept_code+"'", con);
				int a = 0;
				
				sql="update ecourts_case_data set dept_id="+deptId+" where cino in (select cino from ecourts_cinos_1705_2305 where trim(regexp_replace(dept_name, '\\W', '', 'g'))=trim('"+dept_name+"'))";
				DatabasePlugin.executeUpdate(sql, con);
				
				sql = "update ecourts_cinos_1705_2305 set dept_code=?,dept_id='"+deptId+"' where trim(regexp_replace(dept_name, '\\W', '', 'g'))=trim(?)";
				ps = con.prepareStatement(sql);
				ps.setString(++i, dept_code);
				ps.setString(++i, dept_name);
				a = ps.executeUpdate();
				if (a > 0) {
					request.setAttribute("successMsg", " Department data updated Successfully.");
				} else {
					request.setAttribute("errorMsg",
							"Exception Occurred while Updating  details. Kindly try again with valid data.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Exception Occurred while Updating  details. Invalid data.");
			}
			request.setAttribute("HEADING", "Update Case Department");

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
}
