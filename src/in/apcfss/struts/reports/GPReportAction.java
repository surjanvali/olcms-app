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

public class GPReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		
		return mapping.findForward("success");
	}
	
	public ActionForward yearWiseCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			} else {
				con = DatabasePlugin.connect();
				
				int yearId = CommonModels.checkIntObject(request.getParameter("yearId"));
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis from ecourts_case_data a "
						+ " inner join dept_new d on (a.dept_code=d.dept_code)   inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
						+ " where reg_year > 0 and d.display = true  and e.gp_id='"+userId+"' ";
				if(yearId > 0)
					sql+="and reg_year='"+yearId+"' ";
				
				sql	+= "order by reg_year,type_name_reg,reg_no";
				request.setAttribute("HEADING", "Cases Registered in the Year:"+yearId);

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("CASEWISEDATA", data);
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
	
	public ActionForward viewCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			} else {
				con = DatabasePlugin.connect();
				
				String caseType = CommonModels.checkStringObject(request.getParameter("caseType"));
				String caseNo = CommonModels.checkStringObject(request.getParameter("caseNo"));
				int caseYear = CommonModels.checkIntObject(request.getParameter("caseYear"));
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis from ecourts_case_data a "
						+ " inner join dept_new d on (a.dept_code=d.dept_code)   inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
						+ " where reg_year > 0 and d.display = true  and e.gp_id='"+userId+"' ";
				if(caseYear > 0)
					sql += "and reg_year='" + caseYear + "' ";
				
				sql	+= "order by reg_year,type_name_reg,reg_no";
				request.setAttribute("HEADING", "Case Details for Case No.:"+caseType+" "+caseNo+"/"+caseYear);

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("CASEWISEDATA", data);
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
	
	public ActionForward viewCaseData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		
		return mapping.findForward("viewCaseData");
	}
}
