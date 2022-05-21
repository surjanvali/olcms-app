package in.apcfss.struts.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class ContemptCasesAbstractReport extends DispatchAction{
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

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else //if(roleId.equals("3") || roleId.equals("4"))
			{
			con = DatabasePlugin.connect();
				/*
				sql="select a1.reporting_dept_code as deptcode,dn1.description,sum(total_cases) as  total_cases, sum(petition_uploaded) as petition_uploaded,sum(closed_cases) as closed_cases, "
						+ " sum(counter_uploaded) as counter_uploaded, sum(pwrcounter_uploaded) as pwrcounter_uploaded,sum(counter_approved_gp) as counter_approved_gp "
						+ " from ( "
						+ " select case when reporting_dept_code='CAB01' then a.dept_code else reporting_dept_code end as reporting_dept_code,a.dept_code,count(*) as total_cases, sum(case when petition_document is not null then 1 else 0 end) as petition_uploaded "
						+ " , sum(case when a.ecourts_case_status='Closed' then 1 else 0 end) as closed_cases ,sum(case when a.ecourts_case_status='Pending' and counter_filed_document is not null then 1 else 0 end) as counter_uploaded"
						+ " , sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is not null then 1 else 0 end) as pwrcounter_uploaded "
						+ " , sum(case when counter_approved_gp='Yes' then 1 else 0 end) as counter_approved_gp from ecourts_case_data a "
						+ " left join apolcms.ecourts_olcms_case_details b using (cino)inner join dept_new dn on (a.dept_code=dn.dept_code) ";

						if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
							sql+=" and (dn.reporting_dept_code='"+session.getAttribute("dept_code")+"' or dn.dept_code='"+session.getAttribute("dept_code")+"')";
						
						
						sql+= " group by reporting_dept_code,a.dept_code) a1"
						
						+ " inner join dept_new dn1 on (a1.reporting_dept_code=dn1.dept_code) "
						+ " group by a1.reporting_dept_code,dn1.description"
						+ " order by 1"; */
						
				 sql="select c.dept_code as deptcode,d.description,count(distinct cino) as total_cases from ecourts_contempt_cinos c left join dept_new d on (c.dept_code=d.dept_code) "
				 		+ "group by c.dept_code,d.description order by 1";

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
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			heading="Cases List for "+request.getParameter("deptName");
			
			sql="select a.*, b.orderpaths from ecourts_case_data a left join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
					+ " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true ";
			
			sql+=" and a.cino in (select cino from ecourts_contempt_cinos a1 inner join dept_new dn where a1.dept_code='"+request.getParameter("deptId")+"' or ) ";
			
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