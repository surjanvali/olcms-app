
package in.apcfss.struts.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

/**
 * MyEclipse Struts Creation date: 05-20-2022
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/eofficeEmployeeReport" name="eofficeEmployeeReportForm"
 *                input="/form/eofficeEmployeeReport.jsp" scope="request"
 *                validate="true"
 * @struts.action-forward name="success" path="/form/eofficeEmployeeReport.jsp"
 */
public class EofficeEmployeeReportAction extends DispatchAction {
	/*
	 * Generated Methods
	 */

	/**
	 * Method DispatchAction
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("in unspecified");
		HttpSession session = null;
		String roleId = "";
		try {
			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			if (roleId != null && (roleId.equals("1") || roleId.equals("7"))) {// 1. OLCMS - ADMIN

				request.setAttribute("DISPLAYFILTER", "DISPLAY");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return viewReport(mapping, form, request, response);
	}

	public ActionForward viewReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		HttpSession session = null;
		System.out.println("in viewReport");
		String roleId = "", deptCode = "", distId = "";
		try {
			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			if (roleId.equals("2")) {
				distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			} else {
				distId = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
			}
			con = DatabasePlugin.connect();
			cform.setDynaForm("DCLIST", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			String dist = CommonModels.checkStringObject(distId);
			String tableName = "";

			tableName = AjaxModels.getTableName(CommonModels.checkStringObject(dist), con);

			String sql = "select substr(global_org_name,1,5) as code,global_org_name,count(*) from " + tableName;

			if (roleId.equals("3") || roleId.equals("4")) { // 2. Sect Dept./ MLO Login
				sql += " where substr(trim(global_org_name),1,5) in (select dept_code from dept_new where reporting_dept_code='"
						+ deptCode + "' or dept_code='" + deptCode + "')";
			}
			if (roleId.equals("5") || roleId.equals("9")) { // 2. HOD / NO Login
				sql += " where substr(trim(global_org_name),1,5)='" + deptCode + "'";
			}
			
			sql += " group by global_org_name order by global_org_name";

			System.out.println("SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("HEADING", "Eoffice Employee Details");
				request.setAttribute("regData", data);
			} else
				request.setAttribute("errorMsg", "No Records found to display");

		} catch (Exception e) {
			request.setAttribute("displayMessage", "Exception while retrieveing data.");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("districtId", distId);
			if (roleId != null && (roleId.equals("1") || roleId.equals("7"))) {// 1. OLCMS - ADMIN
				request.setAttribute("DISPLAYFILTER", "DISPLAY");
			}
			if (con != null) {
				con.close();
			}
		}

		return mapping.findForward("success");
	}

	public ActionForward getOfficerWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		HttpSession session = null;
		System.out.println("in getOfficerWise");
		String roleId = "";
		try {
			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			con = DatabasePlugin.connect();
			cform.setDynaForm("DCLIST", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			String dist = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
			String tableName = "";

			tableName = AjaxModels.getTableName(CommonModels.checkStringObject(dist), con);
			// String sql = "select * from " + tableName + " where global_org_name='" +
			// cform.getDynaForm("deptId") + "'";
			String sql = "select * from " + tableName
					+ " where trim(regexp_replace(global_org_name, '\\W', '', 'g'))=trim(regexp_replace('"
					+ cform.getDynaForm("deptId") + "', '\\W', '', 'g')) order by designation_id";
			System.out.println("SQL::" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("HEADING", "Eoffice Employee Details - " + cform.getDynaForm("deptId"));
				request.setAttribute("EMPWISENOS", data);
			} else
				request.setAttribute("errorMsg", "No Records found to display");

		} catch (Exception e) {
			request.setAttribute("displayMessage", "Exception while retrieveing data.");
			e.printStackTrace();
		} finally {
			if (roleId != null && (roleId.equals("1") || roleId.equals("7"))) {// 1. OLCMS - ADMIN

				request.setAttribute("DISPLAYFILTER", "DISPLAY");
			}
			if (con != null) {
				con.close();
			}
		}
		return mapping.findForward("success");
	}
}