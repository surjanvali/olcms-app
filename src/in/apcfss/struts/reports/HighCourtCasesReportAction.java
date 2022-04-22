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

public class HighCourtCasesReportAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null,
				condition = "", deptId = "", deptCode = "";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			if (roleId != null && roleId.equals("4")) { // MLO / NO
				condition = " and a.case_status=2";
			} else if (roleId != null && roleId.equals("5")) { // NO
				condition = " and a.case_status=4";
			} else if (roleId != null && roleId.equals("8")) { // SECTION OFFICER
				condition = " and a.case_status=5 and assigned_to='" + userId + "'";
			}

			else if (roleId != null && roleId.equals("3")) { // SECT DEPT
				condition = " and a.case_status=4";
			} else if (roleId != null && roleId.equals("9")) { // HOD
				condition = " and a.case_status=4";
			} else if (roleId != null && roleId.equals("6")) { // GPO
				condition = " and a.case_status=4";
			}

			sql = "select a.*, b.orderpaths from ecourts_case_data a left join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " ((select cino, order_document_path,order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)" + " union"
					+ " (select cino, order_document_path,order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)) c group by cino ) b"
					+ " on (a.cino=b.cino) where dept_code='" + deptCode + "' " + condition
					+ " and coalesce(ecourts_case_status,'')!='Closed'";
			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", "Assigned Cases List");
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
