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

public class HCCaseStatusAbstractReport extends DispatchAction {
	@Override
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
			
			else  if(roleId.equals("5") || roleId.equals("9")) {
				
				return HODwisedetails(mapping, form, request, response);
			}
			else //if(roleId.equals("3") || roleId.equals("4"))
			{
			con = DatabasePlugin.connect();

			sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases  from ("
					+ "select a.dept_code , case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,count(*) as total_cases, "
					+ "sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept, "
					+ "sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo, "
					+ "sum(case when case_status=3  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod, "
					+ "sum(case when case_status=4  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal, "
					+ "sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection, "
					+ "sum(case when case_status=7  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc, "
					+ "sum(case when case_status=8  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno, "
					+ "sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod, "
					+ "sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist, "
					+ "sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo, "
					+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases "
					+ "from ecourts_case_data a "
					+ "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true ";
				
			
			if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql+=" and (reporting_dept_code='"+session.getAttribute("dept_code")+"' or a.dept_code='"+session.getAttribute("dept_code")+"')";
			
				sql+= "group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
					+ "group by x.reporting_dept_code, d1.description order by 1";
			

			request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

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

	public ActionForward HODwisedetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, deptId = null, deptName="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			if(roleId.equals("5") || roleId.equals("9"))
			{
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery("select upper(description) as description from dept_new where dept_code='"+deptId+"'", con);
				// CommonModels.checkStringObject(session.getAttribute("dept_code"));
			}
			else {
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			}
			String dept = deptId.substring(0, 3);

			sql = "select sdeptcode,deptcode,description,count(*) as total_cases"
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
					+ " inner join dept on (sdeptcode||deptcode=dept_code) where substr(dept_code,1,3)='" + dept + "' "
					+ " group by sdeptcode,deptcode,dept_code,description" + " order by sdeptcode,deptcode";
			
			sql="select a.dept_code as deptcode , upper(d.description) as description,count(*) as total_cases, "
					+ "sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept, "
					+ "sum(case when (case_status is null or case_status=2) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo, "
					+ "sum(case when case_status=3 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod, "
					+ "sum(case when case_status=4 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal, "
					+ "sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection, "
					+ "sum(case when case_status=7 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc, "
					+ "sum(case when case_status=8 and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno, "
					+ "sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod, "
					+ "sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist, "
					+ "sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo, "
					+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases "
					+ "from ecourts_case_data a "
					+ "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true and (reporting_dept_code='"+deptId+"' or a.dept_code='"+deptId+"') "
					+ "group by a.dept_code , d.description order by 1";
			
			sql="select a.dept_code as deptcode , upper(d.description) as description,count(*) as total_cases, "
					+ "sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept, "
					+ "sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo, "
					+ "sum(case when case_status=3  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod, "
					+ "sum(case when case_status=4  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal, "
					+ "sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection, "
					+ "sum(case when case_status=7  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc, "
					+ "sum(case when case_status=8  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno, "
					+ "sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod, "
					+ "sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist, "
					+ "sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo, "
					+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases "
					+ "from ecourts_case_data a "
					+ "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true and (d.reporting_dept_code='"+deptId+"' or a.dept_code='"+deptId+"') "
					+ "group by a.dept_code , d.description order by 1";
			

			request.setAttribute("HEADING", "HOD Wise High Court Cases Abstract Report for "+deptName);
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("deptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
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
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			
			heading="Cases List for "+deptName;
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("withSD")){
						sqlCondition= " and case_status=1 and coalesce(ecourts_case_status,'')!='Closed' ";
						heading+=" Pending at Sect Dept. Login";
					}
				if(caseStatus.equals("withMLO")) {
					sqlCondition=" and (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at MLO Login";
				}
				if(caseStatus.equals("withHOD")) {
					sqlCondition=" and case_status=3  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at HOD Login";
				}
				if(caseStatus.equals("withNO")) {
					sqlCondition= " and case_status=4  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at Nodal Officer(HOD) Login";
				}
				if(caseStatus.equals("withSDSec")) {
					sqlCondition=" and case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at Section Officers Login (Sect Dept.)";
				}
				if(caseStatus.equals("withDC")) {
					sqlCondition= " and case_status=7  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at District Collector Login";
				}
				if(caseStatus.equals("withDistNO")) {
					sqlCondition=" and case_status=8  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at Nodal Officer(District) Login";
				}
				if(caseStatus.equals("withHODSec")) {
					sqlCondition=" and case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at Section Officer(HOD) Login";
				}
				if(caseStatus.equals("withDistSec")) {
					sqlCondition=" and case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at Sction Officer(District) Login";
				}
				if(caseStatus.equals("withGP")) {
					sqlCondition= " and case_status=6 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading+=" Pending at GP Login";
				}
				if(caseStatus.equals("closed")) {
					sqlCondition= " and case_status=99 or coalesce(ecourts_case_status,'')='Closed' ";
					heading+=" All Closed Cases ";
				}
			}
			
			
			if(actionType.equals("SDWISE")) {
			}
			else if(actionType.equals("HODWISE")) {
			}
			
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
			
			sql+=" and (reporting_dept_code='"+deptCode+"' or a.dept_code='"+deptCode+"') "+sqlCondition;
			
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
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}
	
}