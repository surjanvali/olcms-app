package in.apcfss.struts.reports;

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

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class AllHighCourtCasesReportAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		String tableName = "nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else {

				con = DatabasePlugin.connect();

				sql = "select a.dept_id,d.sdeptcode||d.deptcode as deptshortname ,upper(trim(d.description)) as description,count(*) total, sum(case when assigned is true then 1 else 0 end) as assigned "
						+ "from ecourts_case_data a inner join dept_new d on (a.dept_id=d.dept_id) where d.sdeptcode='"
						+ userId.substring(0, 3) + "'" + "group by a.dept_id,d.description,d.sdeptcode||d.deptcode "
						+ "order by d.sdeptcode||d.deptcode";
				
				sql="select d.dept_code as deptshortname ,upper(trim(d.description)) as description,count(*) total, sum(case when assigned is true then 1 else 0 end) as assigned "
						+ " from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) where (d.dept_code='"+userId+"' or d.reporting_dept_code='"+userId+"') "
						+ " group by d.dept_code,upper(trim(d.description)) order by d.dept_code";

				request.setAttribute("HEADING", "Department wise Highcourt Cases Abstract Report");
				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("DEPTWISEHCCASES", data);
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

	public ActionForward showAllCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null,
				condition = "";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			String hodCode = (String)request.getParameter("hodCode");

			sql = "select a.*, b.orderpaths from ecourts_case_data a left join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " ((select cino, order_document_path,order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)" + " union"
					+ " (select cino, order_document_path,order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code = d.dept_code) where d.dept_code='"
					+ hodCode + "' ";
			
			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", "Cases List");
			} else {
				request.setAttribute("errorMsg", "No Records Found to display.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

}