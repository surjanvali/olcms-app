package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

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

import plugins.DatabasePlugin;

public class CaseAssignmentStatusReport extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null,deptCode=null;
		String tableName = "nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {

				con = DatabasePlugin.connect();
				
				if(roleId.equals("4")) {
					sql = "select d.sdeptcode||'01' as deptshortname,upper(d.description) as description,count(a.*) as total"
							+ ", sum(case when (substr(a.dept_code,4,2)!='01') then 1 else 0 end) as assigned_to_hod "
							+ ", sum(case when substr(a.dept_code,4,2)='01' and assigned=true then 1 else 0 end) as assigned_to_sect_sec "
							+ ", sum(case when substr(a.dept_code,4,2)!='01' and assigned=true then 1 else 0 end) as assigned_to_hod_sec  from ecourts_case_data a "
							+ " right join (select * from dept where sdeptcode||deptcode='"+deptCode+"') d on (substr(a.dept_code,1,3)=d.sdeptcode) "
							+ " group by substr(a.dept_code,1,3),d.sdeptcode,description order by 1";
				}
				else if(roleId.equals("7")) {
				
					sql = "select d.sdeptcode||'01' as deptshortname,upper(d.description) as description,count(a.*) as total"
						+ ", sum(case when (substr(a.dept_code,4,2)!='01') then 1 else 0 end) as assigned_to_hod "
						+ ", sum(case when substr(a.dept_code,4,2)='01' and assigned=true then 1 else 0 end) as assigned_to_sect_sec "
						+ ", sum(case when substr(a.dept_code,4,2)!='01' and assigned=true then 1 else 0 end) as assigned_to_hod_sec  from ecourts_case_data a "
						+ " right join (select * from dept where deptcode='01') d on (substr(a.dept_code,1,3)=d.sdeptcode) "
						+ " group by substr(a.dept_code,1,3),d.sdeptcode,description order by 1";
				}
				
				/*
				sql = "select sdeptcode||deptcode as deptcode,description,count(*) as total_cases"
						+ ", sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept"
						+ ", sum(case when (case_status is null or case_status=2) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo"
						+ ", sum(case when case_status=3 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod"
						+ ", sum(case when case_status=4 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal"
						+ ", sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection"
					
						+ ", sum(case when case_status=7 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc"
						+ ", sum(case when case_status=8 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno"
						
						+ ", sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod"
						+ ", sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist"
						
						+ ", sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo"
						+ ", sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
						+ " from ecourts_case_data"
						+ " inner join dept on (sdeptcode=substr(dept_code,1,3) and deptcode='01')"
						+ " group by sdeptcode,deptcode,substr(dept_code,1,3),description";
				*/
				
				request.setAttribute("HEADING", "Department wise Highcourt Cases Assignment Status Abstract Report");
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

	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "";
		try {

			con = DatabasePlugin.connect();

			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));

			if (!actionType.equals("") && !deptId.equals("")) {

				if (actionType.equals("assigned2HOD")) {
					sqlCondition = " and substr(a.dept_code,4,2)!='01' ";
					heading = " " + deptName + " Cases assigned to HOD";
				} else if (actionType.equals("assigned2SectSec")) {
					sqlCondition = " and substr(a.dept_code,4,2)='01' and assigned=true ";
					heading = " " + deptName + " Cases assigned to Section (Sect. Dept.)";
				} else if (actionType.equals("assigned2HodSec")) {
					sqlCondition = " and substr(a.dept_code,4,2)!='01' and assigned=true ";
					heading = " " + deptName + " Cases assigned to Section (HOD)";
				} else {
					heading = " " + deptName + " Cases";
				}

				sql = "select a.*, b.orderpaths from ecourts_case_data a left join" + " ("
						+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
						+ " from "
						+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
						+ " union"
						+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
						+ " on (a.cino=b.cino) where substr(a.dept_code,1,3)='" + deptId.substring(0, 3) + "' "
						+ sqlCondition + " ";

				System.out.println("ecourts SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				request.setAttribute("HEADING", heading);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASESLIST", data);
				} else {
					request.setAttribute("errorMsg", "No Records Found");
				}
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}

}