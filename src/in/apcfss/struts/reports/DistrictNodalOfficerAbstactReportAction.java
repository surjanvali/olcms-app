package in.apcfss.struts.reports;

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

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class DistrictNodalOfficerAbstactReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			con = DatabasePlugin.connect();
			request.setAttribute("HEADING", "District Nodal Officer (Legal) Abstract ");

			sql = "select dist_id as  distid,upper(b.district_name) as district_name,count(*) as acks From nodal_officer_details a "
					+ "inner join district_mst b on (a.dist_id=b.district_id) ";
			
			if(!deptCode.equals("") && !deptCode.equals("0"))
					sql+=" where a.dept_id='"+deptCode+"'";
					
			sql+=" group by a.dist_id,b.district_name order by district_name ";

			System.out.println("SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("DISTWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
		// return showDeptWise(mapping, cform, request, response);
	}

	public ActionForward showCaseWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null,deptCode="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {

				con = DatabasePlugin.connect();

				String dist = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
				String tableName = "";
				System.out.println("dist--" + dist);
				tableName = AjaxModels.getTableName(CommonModels.checkStringObject(dist), con);

				request.setAttribute("HEADING", "District Nodal Officer (Legal) Details - "
						+ cform.getDynaForm("district_name") + " District ");

				sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
						+ "inner join (select distinct employee_id,fullname_en,designation_name_en, designation_id from "
						+ tableName + ") nd on (m.employeeid=nd.employee_id and m.designation=nd.designation_id)"
						+ "inner join users u on (m.emailid=u.userid)"
						+ "inner join dept_new d on (m.dept_id=d.dept_code)" + "where m.dist_id='" + dist
						+ "'";
				
				if(!deptCode.equals("") && !deptCode.equals("0"))
							sql+=" and  m.dept_id='"+deptCode+"'";
				
						sql+= " order by 1";

				System.out.println("SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("EMPWISEDATA", data);
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