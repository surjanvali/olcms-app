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
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class ContemptCasesAbstractReport extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			CommonForm cform = (CommonForm) form;
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else // if(roleId.equals("3") || roleId.equals("4"))
			{
				
				String sqlCondition = "";
				con = DatabasePlugin.connect();
				
				
				sql = "select c.dept_code as deptcode,upper(d.description) as description,count(distinct c.cino) as total_cases "
						+ " from ecourts_contempt_cinos c inner join ecourts_case_data a on (c.cino=a.cino)"
						+ " left join dept_new d on (c.dept_code=d.dept_code) ";

				if (roleId.equals("3") || roleId.equals("4"))
					sql += " where c.dept_code='" + CommonModels.checkStringObject(session.getAttribute("dept_code")) + "'";

				sql += " group by c.dept_code,d.description order by 1";

				request.setAttribute("HEADING", "Dept. Wise Contempt Cases Report");

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("secdeptwise", data);
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
	
	public ActionForward ShowCasesData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HCCaseStatusAbstractReport..............................................................................getCasesList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId = null , userId = null,
				deptCode = null, caseStatus = null;
		
		userId = CommonModels.checkStringObject(session.getAttribute("userid"));
		roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
		
		System.out.println("+"+roleId);
		
		try {

				con = DatabasePlugin.connect();
				
				
				if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
							+ "','yyyy-mm-dd') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
							+ "','yyyy-mm-dd') ";
				}

				sql = "select c.dept_code as deptcode,upper(d.description) as description,count(distinct c.cino) as total_cases "
						+ " from ecourts_contempt_cinos c inner join ecourts_case_data a on (c.cino=a.cino)"
						+ " left join dept_new d on (c.dept_code=d.dept_code) where 1=1 ";

				if (roleId.equals("3") || roleId.equals("4"))
					sql += " and c.dept_code='" + CommonModels.checkStringObject(session.getAttribute("dept_code")) + "' ";

				sql += "" + sqlCondition+" group by c.dept_code,d.description order by 1";
				
				request.setAttribute("HEADING", "Dept. Wise Contempt Cases Report");
				request.setAttribute("HEADING_Date", "Selected From Date: "+cform.getDynaForm("dofFromDate")+"  To date: "+cform.getDynaForm("dofToDate"));
				
				request.setAttribute("to_date", cform.getDynaForm("dofToDate"));
				
				request.setAttribute("fr_date", cform.getDynaForm("dofFromDate"));

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("secdeptwise", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HCCaseStatusAbstractReport..............................................................................getCasesList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId = null,
				deptCode = null, caseStatus = null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String fr_date=request.getParameter("fr_date");
			String to_date=request.getParameter("to_date");
			
			System.out.println("to_date--"+fr_date+to_date);
			heading = "Cases List for " + request.getParameter("deptName");
			
			request.setAttribute("HEADING_Date", "Selected From Date: "+fr_date+"  To date: "+to_date);
			
			if (fr_date != null ) {
				sqlCondition += " and   a.dt_regis >= to_date('" +fr_date+"','yyyy-mm-dd') ";
				cform.setDynaForm("dofFromDate",fr_date);
			}
			
			if (  to_date != null ) {
				sqlCondition += " and   a.dt_regis <= to_date('" +to_date+"','yyyy-mm-dd') ";
				cform.setDynaForm("dofToDate",to_date);
			}
			
			sql = "select a.*, b.orderpaths from ecourts_case_data a inner join ecourts_contempt_cinos cc on (a.cino=cc.cino) left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (cc.dept_code=d.dept_code) where d.display = true and cc.dept_code='"
					+ request.getParameter("deptId") + "' "+sqlCondition+" ";

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}