package in.apcfss.struts;

import in.apcfss.struts.Utilities.CommonModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class WelcomePageAction extends DispatchAction{
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("userid") == null || session.getAttribute("role_id") == null || session.getAttribute("userid").toString().trim().equals("") || session.getAttribute("role_id").toString().trim().equals(""))
			return mapping.findForward("Login");
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String target = "UserWelcomePage";
		try {
			System.out.println("userid:::"+session.getAttribute("userid"));
			con = DatabasePlugin.connect();

			String roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			String userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			// String deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			String deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			String distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			String sql = "SELECT service_name,target,show_icon as icon,has_childs as has_child,parent_id,a.service_id,display_id FROM services a inner join role_services b on (a.service_id=b.service_id) where b.role_id=?  "
					+ " "
					+ " union "
					+ " SELECT service_name,target,show_icon as icon,has_childs as has_child,parent_id,a1.service_id,display_id FROM services a1 inner join user_services b1 on (a1.service_id=b1.service_id) where b1.user_id=? "
					+ " "
					+ " order by 7,5,6";

			System.out.println("in WelcomePageAction sql ............ " + sql);
			System.out.println("roleId:"+roleId);
			System.out.println("deptCode:"+deptCode);
			ps = con.prepareStatement(sql);
			DatabasePlugin.setDefaultParameters(ps, 1, roleId, "Int");
			DatabasePlugin.setDefaultParameters(ps, 2, userid, "String");
			rs = ps.executeQuery();
			List<Map<String, Object>> services = DatabasePlugin.processResultSet(rs);
			session.setAttribute("services", services);
			
			if(roleId!=null && !roleId.equals("")){
				target="UserWelcomePageNew";
				
				if(roleId.equals("1") || roleId.equals("7")) { // ADMIN LOGIN
					sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases, sum(goi) as goi, sum(psu) as psu, sum(privatetot) as privatetot  from ("
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
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases, "
							+ "sum(case when case_status=96 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as goi, "
							+ "sum(case when case_status=97 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as psu, "
							+ "sum(case when case_status=98 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as privatetot "
							+ "from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ "where d.display = true ";
						
						sql+= "group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
							+ "group by x.reporting_dept_code, d1.description order by 1";
					
					request.setAttribute("HEADING", "High Court Cases Abstract Report");

					System.out.println("SQL:" + sql);
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
					// System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("secdeptwise", data);
					request.setAttribute("showReport1", "showReport1");
					
					sql="select count(*) as total, "
							+ " sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							//+ " sum(case when (case_status=7) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ " sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data ";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) from ecourts_gpo_ack_dtls where ack_type='NEW'";
					request.setAttribute("NEWCASES", DatabasePlugin.getStringfromQuery(sql, con));
					
					/*sql="select count(distinct ack_no) as total, "
							+ "sum(case when (case_status is null or case_status=7) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=7) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ "  from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' ";
					
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts); */
					
					sql="select sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_finalorder";
					request.setAttribute("FINALORDERS", DatabasePlugin.getStringfromQuery(sql, con));
					
					sql="select count(distinct cino) ||','||sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_interimorder";
					String interimData[]=DatabasePlugin.getStringfromQuery(sql, con).split(",");
					
					request.setAttribute("INTERIMCASES", interimData[0]);
					request.setAttribute("INTERIMORDERS", interimData[1]);
					
					
					sql="select "
							+ " sum(case when disposal_type='DISPOSED OF NO COSTS' or disposal_type='DISPOSED OF AS INFRUCTUOUS' then 1 else 0 end) as disposed,"
							+ " sum(case when disposal_type='ALLOWED NO COSTS' or disposal_type='PARTLY ALLOWED NO COSTS' then 1 else 0 end) as allowed,"
							+ " sum(case when disposal_type='DISMISSED' or disposal_type='DISMISSED AS INFRUCTUOUS' or disposal_type='DISMISSED NO COSTS' or disposal_type='DISMISSED FOR DEFAULT' or disposal_type='DISMISSED AS NON PROSECUTION' or disposal_type='DISMISSED AS ABATED' or disposal_type='DISMISSED AS NOT PRESSED'  then 1 else 0 end) as dismissed ,"
							+ " sum(case when disposal_type='WITHDRAWN' then 1 else 0 end) as withdrawn,"
							+ " sum(case when disposal_type='CLOSED NO COSTS' or disposal_type='CLOSED AS NOT PRESSED' then 1 else 0 end) as closed,"
							+ " sum(case when disposal_type='REJECTED' or disposal_type='ORDERED' or disposal_type='RETURN TO COUNSEL' or disposal_type='TRANSFERRED' then 1 else 0 end) as returned"
							+ " from ecourts_case_data";
					List<Map<Object, String>> disposedCasesStatus = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("disposedCasesStatus", disposedCasesStatus);
					
					
				}
				else if(roleId.equals("3") || roleId.equals("4")  || roleId.equals("5") || roleId.equals("9")) {
					
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
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases, "
							+ "sum(case when case_status=96 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as goi, "
							+ "sum(case when case_status=97 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as psu, "
							+ "sum(case when case_status=98 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as privatetot "
							+ "from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ "where d.display = true and (reporting_dept_code='"+deptCode+"' or a.dept_code='"+deptCode+"') "
							+ "group by a.dept_code , d.description order by 1";

					System.out.println("SQL:" + sql);
					request.setAttribute("HEADING", "Abstract Report");
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
					// System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("deptwise", data);
					request.setAttribute("showReport1", "showReport1");
					
					/*sql="select count(*)  from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls ad1 on (ad.ack_no=ad1.ack_no) "
							+ "inner join dept_new d on (ad.dept_code=d.dept_code)  "
							+ "where ack_type='NEW' and (reporting_dept_code='"+deptCode+"' or ad.dept_code='"+deptCode+"') and respondent_slno=1  ";
					System.out.println("ACK SQL:"+sql);
					request.setAttribute("NEWCASES", DatabasePlugin.getStringfromQuery(sql, con));*/
					
					
					/*sql="select count(distinct ack_no) as total, "
							+ "sum(case when (case_status is null or case_status=7) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=7) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ "  from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dist_id='"+distId+"'";
					
					List<Map<String, Object>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);*/
					
					
					sql="select sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_finalorder b inner join ecourts_case_data a on (a.cino=b.cino) "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ "where d.display = true and (reporting_dept_code='"+deptCode+"' or a.dept_code='"+deptCode+"') ";
					
					request.setAttribute("FINALORDERS", DatabasePlugin.getStringfromQuery(sql, con));
					
					sql="select count(distinct a.cino) ||','||sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_interimorder b "
							+ "inner join ecourts_case_data a on (a.cino=b.cino) "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ "where d.display = true and (reporting_dept_code='"+deptCode+"' or a.dept_code='"+deptCode+"') ";
					
					String interimData[]=DatabasePlugin.getStringfromQuery(sql, con).split(",");
					
					request.setAttribute("INTERIMCASES", interimData[0]);
					request.setAttribute("INTERIMORDERS", interimData[1]);
					
					sql="select "
							+ " sum(case when disposal_type='DISPOSED OF NO COSTS' or disposal_type='DISPOSED OF AS INFRUCTUOUS' then 1 else 0 end) as disposed,"
							+ " sum(case when disposal_type='ALLOWED NO COSTS' or disposal_type='PARTLY ALLOWED NO COSTS' then 1 else 0 end) as allowed,"
							+ " sum(case when disposal_type='DISMISSED' or disposal_type='DISMISSED AS INFRUCTUOUS' or disposal_type='DISMISSED NO COSTS' or disposal_type='DISMISSED FOR DEFAULT' or disposal_type='DISMISSED AS NON PROSECUTION' or disposal_type='DISMISSED AS ABATED' or disposal_type='DISMISSED AS NOT PRESSED'  then 1 else 0 end) as dismissed ,"
							+ " sum(case when disposal_type='WITHDRAWN' then 1 else 0 end) as withdrawn,"
							+ " sum(case when disposal_type='CLOSED NO COSTS' or disposal_type='CLOSED AS NOT PRESSED' then 1 else 0 end) as closed,"
							+ " sum(case when disposal_type='REJECTED' or disposal_type='ORDERED' or disposal_type='RETURN TO COUNSEL' or disposal_type='TRANSFERRED' then 1 else 0 end) as returned"
							+ " from ecourts_case_data a "
							+ " inner join dept_new d on (a.dept_code=d.dept_code) "
							+ " where d.display = true and (reporting_dept_code='"+deptCode+"' or a.dept_code='"+deptCode+"') ";
					
					List<Map<Object, String>> disposedCasesStatus = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("disposedCasesStatus", disposedCasesStatus);
				}
				
				//District Collector
				if(roleId.equals("2")) {
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=2 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status=7) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=7) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dist_id='"+distId+"'";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
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
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases, "
							+ "sum(case when case_status=96 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as goi, "
							+ "sum(case when case_status=97 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as psu, "
							+ "sum(case when case_status=98 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as privatetot "
							+ "from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ "where d.display = true and a.dist_id='"+distId+"' "
							+ "group by a.dept_code , d.description order by 1";

					System.out.println("SQL:" + sql);
					request.setAttribute("HEADING", "Abstract Report");
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
					// System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("deptwise", data);
					request.setAttribute("showReport1", "showReport1");
					
					
					// sql="select count(*) from ecourts_gpo_ack_dtls ad  where ack_type='NEW' and distid='"+distId+"' ";
					// request.setAttribute("NEWCASES", DatabasePlugin.getStringfromQuery(sql, con));
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=7) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=7) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ "  from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dist_id='"+distId+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
					
					sql="select sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_finalorder b inner join ecourts_case_data a on (a.cino=b.cino) where a.dist_id='"+distId+"' ";
					request.setAttribute("FINALORDERS", DatabasePlugin.getStringfromQuery(sql, con));
					
					
					sql="select count(distinct b.cino) ||','||sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_interimorder b inner join ecourts_case_data a on (a.cino=b.cino) where a.dist_id='"+distId+"' ";
					String interimData[]=DatabasePlugin.getStringfromQuery(sql, con).split(",");
					request.setAttribute("INTERIMCASES", interimData[0]);
					request.setAttribute("INTERIMORDERS", interimData[1]);
					
					sql="select "
							+ " sum(case when disposal_type='DISPOSED OF NO COSTS' or disposal_type='DISPOSED OF AS INFRUCTUOUS' then 1 else 0 end) as disposed,"
							+ " sum(case when disposal_type='ALLOWED NO COSTS' or disposal_type='PARTLY ALLOWED NO COSTS' then 1 else 0 end) as allowed,"
							+ " sum(case when disposal_type='DISMISSED' or disposal_type='DISMISSED AS INFRUCTUOUS' or disposal_type='DISMISSED NO COSTS' or disposal_type='DISMISSED FOR DEFAULT' or disposal_type='DISMISSED AS NON PROSECUTION' or disposal_type='DISMISSED AS ABATED' or disposal_type='DISMISSED AS NOT PRESSED'  then 1 else 0 end) as dismissed ,"
							+ " sum(case when disposal_type='WITHDRAWN' then 1 else 0 end) as withdrawn,"
							+ " sum(case when disposal_type='CLOSED NO COSTS' or disposal_type='CLOSED AS NOT PRESSED' then 1 else 0 end) as closed,"
							+ " sum(case when disposal_type='REJECTED' or disposal_type='ORDERED' or disposal_type='RETURN TO COUNSEL' or disposal_type='TRANSFERRED' then 1 else 0 end) as returned"
							+ " from ecourts_case_data a "
							+ " inner join dept_new d on (a.dept_code=d.dept_code) "
							+ " where d.display = true and a.dist_id='"+distId+"' ";
					
					List<Map<Object, String>> disposedCasesStatus = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("disposedCasesStatus", disposedCasesStatus);
					
				}else if(roleId.equals("3")) { // Sect. Dept.
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=2 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status=1) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=1) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dept_code='"+deptCode+"'";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=1) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=1) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ " from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dept_code='"+deptCode+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
				}else if(roleId.equals("9")) { // HOD
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=2 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status=3) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=3) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dept_code='"+deptCode+"'";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=3) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=3) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ "  from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dept_code='"+deptCode+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
				}else if(roleId.equals("4")) { // MLO
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=2 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=2) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=2) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dept_code='"+deptCode+"'";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=2) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=2) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ " from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dept_code='"+deptCode+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
					
					sql="select count(*) from ecourts_dept_instructions where dept_code='"+deptCode+"'";
					System.out.println("instruction SQL:"+sql);
					request.setAttribute("instructions", DatabasePlugin.getStringfromQuery(sql, con));
					
					
					
					
				}
				else  if(roleId.equals("5")) { // NODAL OFFICER
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=4 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status=4) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=4) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dept_code='"+deptCode+"'";
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=4) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=4) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ " from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dept_code='"+deptCode+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
				}
				else  if(roleId.equals("10")) { // District NODAL OFFICER
					// sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=4 and coalesce(ecourts_case_status,'')!='Closed'";
					sql="select count(*) as total, "
							+ "sum(case when (case_status=8) and coalesce(assigned,'f')='f' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=8) and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data where dept_code='"+deptCode+"' and dist_id='"+distId+"'";
					System.out.println("SQL:"+sql);
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) as total, "
							+ "sum(case when (case_status is null or case_status=8) and coalesce(assigned,'f')='f' then 1 else 0 end) as assignment_pending,"
							+ "sum(case when (case_status=8) and coalesce(assigned,'f')='t' then 1 else 0 end) as approval_pending,"
							+ "sum(case when case_status=99 then 1 else 0 end) as closedcases"
							+ " from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and ad2.dept_code='"+deptCode+"'  and ad2.dist_id='"+distId+"'";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
				}
				
				
				else if(roleId.equals("8")) { // Section Officer
					// sql="select emp_id,count(*) as assigned from ecourts_case_emp_assigned_dtls where emp_id='"+empId+"' group by emp_id";
					sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=5 and coalesce(ecourts_case_status,'')!='Closed'";
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					
					sql="select count(*) as assigned from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and assigned=true and assigned_to='"+userid+"' and case_status=5 ";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
				}
				else if(roleId.equals("11")) { // Section Officer (HOD)
					// sql="select emp_id,count(*) as assigned from ecourts_case_emp_assigned_dtls where emp_id='"+empId+"' group by emp_id";
					sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=9 and coalesce(ecourts_case_status,'')!='Closed'";
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					
					sql="select count(*) as assigned from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and assigned=true and assigned_to='"+userid+"' and case_status=9 ";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
				}else if(roleId.equals("12")) { // Section Officer (DIST - HOD)
					// sql="select emp_id,count(*) as assigned from ecourts_case_emp_assigned_dtls where emp_id='"+empId+"' group by emp_id";
					sql="select count(*) as assigned from ecourts_case_data where assigned=true and assigned_to='"+userid+"' and case_status=10 and coalesce(ecourts_case_status,'')!='Closed'";
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					sql="select count(*) as assigned from ecourts_gpo_ack_dtls ad1 inner join ecourts_gpo_ack_depts ad2 on (ad1.ack_no=ad2.ack_no)  where ack_type='NEW' and assigned=true and assigned_to='"+userid+"' and case_status=10 ";
					System.out.println("NEWCASESCOUNTS:"+sql);
					dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("NEWCASESCOUNTS", dashboardCounts);
					
				}
				else if(roleId.equals("1") || roleId.equals("7")) {
					
					// request.setAttribute("SHOWABSTRACTS", "SHOWABSTRACTS");
				}
				
				else if(roleId.equals("6")) { // GP NEW CODE
					
					/*/ LEGACY CASES DATA
					sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases, sum(goi) as goi, sum(psu) as psu, sum(privatetot) as privatetot   from ("
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
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases, "
							+ "sum(case when case_status=96 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as goi, "
							+ "sum(case when case_status=97 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as psu, "
							+ "sum(case when case_status=98 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as privatetot "
							+ "from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ " inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
							+ "where d.display = true  and e.gp_id='"+userid+"' ";
					
						sql+= "group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
							+ "group by x.reporting_dept_code, d1.description order by 1";
					
					request.setAttribute("HEADING", "High Court Cases Abstract Report"); */

					System.out.println("SQL:" + sql);
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
					// System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("secdeptwise", data);
					request.setAttribute("showReport1", "showReport1");
					
					sql="select reg_year,count(*) as casescount from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code)   inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
							+ "where reg_year > 0 and d.display = true  and e.gp_id='"+userid+"' "
							+ "group by reg_year order by reg_year desc";
					System.out.println("YEARLY COUNT SQL:"+sql);
					request.setAttribute("YEARWISECASES", DatabasePlugin.executeQuery(con, sql));
					
					/*
					 * sql="select count(*) from ecourts_dept_instructions where insert_by='"+userid
					 * +"'"; System.out.println("instruction SQL:"+sql);
					 * request.setAttribute("INSTRUCTIONSCOUNT", DatabasePlugin.executeQuery(con,
					 * sql));
					 */
					
					
					
					sql="select count(*) From ecourts_olcms_case_details a "
							+ "inner join ecourts_case_data ecd on (a.cino=ecd.cino)  "
							+ "inner join ecourts_mst_gp_dept_map emgd on (ecd.dept_code=emgd.dept_code and ecd.assigned_to=emgd.gp_id) "
							+ "where pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6' "
							+ "and emgd.gp_id='"+userid+"'";
					System.out.println("COUNTERS SQL:"+sql);
					request.setAttribute("counterFileCount", DatabasePlugin.getStringfromQuery(sql, con));
					
					//System.out.println("counterFile--"+sql);
					sql="select count(*) From ecourts_olcms_case_details a "
							+ "inner join ecourts_case_data ecd on (a.cino=ecd.cino)  "
							+ "inner join ecourts_mst_gp_dept_map emgd on (ecd.dept_code=emgd.dept_code and ecd.assigned_to=emgd.gp_id) "
							+ "where pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' "
							+ "and emgd.gp_id='"+userid+"'  ";
					System.out.println("PARAWISE COUNT SQL:"+sql);
					request.setAttribute("parawiseCount", DatabasePlugin.getStringfromQuery(sql, con));
					
					target = "gpDashboard";
				}
				
				
				else if(roleId.equals("61")) { // GPO OLD CODE
					
					// LEGACY CASES DATA
					sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases, sum(goi) as goi, sum(psu) as psu, sum(privatetot) as privatetot   from ("
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
							+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases, "
							+ "sum(case when case_status=96 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as goi, "
							+ "sum(case when case_status=97 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as psu, "
							+ "sum(case when case_status=98 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as privatetot "
							+ "from ecourts_case_data a "
							+ "inner join dept_new d on (a.dept_code=d.dept_code) "
							+ " inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
							+ "where d.display = true  and e.gp_id='"+userid+"' ";
					
						sql+= "group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
							+ "group by x.reporting_dept_code, d1.description order by 1";
					
					request.setAttribute("HEADING", "High Court Cases Abstract Report");

					System.out.println("SQL:" + sql);
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
					// System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("secdeptwise", data);
					request.setAttribute("showReport1", "showReport1");
					
					sql="select count(*) as total, "
							+ " sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as assignment_pending,"
							+ " sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases"
							+ "  from ecourts_case_data a inner join ecourts_mst_gp_dept_map b on (a.dept_code=b.dept_code) where b.gp_id='"+userid+"'";
					
					System.out.println("total--"+sql);
					
					List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("dashboardCounts", dashboardCounts);
					
					//sql="select count(*)  from ecourts_gpo_ack_dtls a inner join ecourts_mst_gp_dept_map b on (a.gp_id=b.gp_id) where ack_type='NEW' ";
					sql="select count(*) from ecourts_gpo_ack_dtls ad inner join ecourts_gpo_ack_depts d on (ad.ack_no=d.ack_no) inner join dept_new dm on (d.dept_code=dm.dept_code) "
							+ " inner join ecourts_mst_gp_dept_map egm on (egm.dept_code=d.dept_code)  where ack_type='NEW' and respondent_slno=1 and egm.gp_id='"+userid+"'";
					//System.out.println("NEW--"+sql);
					request.setAttribute("NEWCASES", DatabasePlugin.getStringfromQuery(sql, con));
					
					
					// OLD CASES
					sql="select count(*) From ecourts_olcms_case_details a "
							+ "inner join ecourts_case_data ecd on (a.cino=ecd.cino)  "
							+ "inner join ecourts_mst_gp_dept_map emgd on (ecd.dept_code=emgd.dept_code) "
							+ "inner join ecourts_mst_gp_dept_map ad on (ad.dept_code =ecd.dept_code ) "
							+ "where counter_filed='Yes' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6' "
							+ "and emgd.gp_id='"+userid+"'";
					System.out.println("COUNTERS SQL:"+sql);
					request.setAttribute("counterFileCount", DatabasePlugin.getStringfromQuery(sql, con));
					//System.out.println("counterFile--"+sql);
					sql="select count(*) From ecourts_olcms_case_details a "
							+ "inner join ecourts_case_data ecd on (a.cino=ecd.cino)  "
							+ "inner join ecourts_mst_gp_dept_map emgd on (ecd.dept_code=emgd.dept_code) "
							+ "inner join ecourts_mst_gp_dept_map ad on (ad.dept_code =ecd.dept_code ) "
							+ "where pwr_uploaded='Yes' and coalesce(pwr_approved_gp,'No')='No' and ecd.case_status='6' "
							+ "and emgd.gp_id='"+userid+"'  ";
					System.out.println("PARAWISE COUNT SQL:"+sql);
					request.setAttribute("parawiseCount", DatabasePlugin.getStringfromQuery(sql, con));
					//System.out.println("parawise--"+sql);
					
					sql="select sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_finalorder a "
							+ "inner join ecourts_case_data b  on (a.cino=b.cino) inner join ecourts_mst_gp_dept_map c on (b.dept_code=c.dept_code) where c.gp_id='"+userid+"'";
					
					request.setAttribute("FINALORDERS", DatabasePlugin.getStringfromQuery(sql, con));
					//System.out.println("FINALORDERS--"+sql);
					
					  sql="select count(distinct a.cino) ||','||sum(case when length(order_document_path) > 10 then 1 else 0 end) as orders from ecourts_case_interimorder a "
					  		+ "inner join ecourts_case_data b  on (a.cino=b.cino) inner join ecourts_mst_gp_dept_map c on (b.dept_code=c.dept_code) where c.gp_id='"+userid+"'"; 
					  String interimData[]=DatabasePlugin.getStringfromQuery(sql,con).split(",");
					  //System.out.println("INTERIM--"+sql);
					  request.setAttribute("INTERIMCASES", interimData[0]);
					  request.setAttribute("INTERIMORDERS", interimData[1]);
					
					sql="select "
							+ " sum(case when disposal_type='DISPOSED OF NO COSTS' or disposal_type='DISPOSED OF AS INFRUCTUOUS' then 1 else 0 end) as disposed,"
							+ " sum(case when disposal_type='ALLOWED NO COSTS' or disposal_type='PARTLY ALLOWED NO COSTS' then 1 else 0 end) as allowed,"
							+ " sum(case when disposal_type='DISMISSED' or disposal_type='DISMISSED AS INFRUCTUOUS' or disposal_type='DISMISSED NO COSTS' or disposal_type='DISMISSED FOR DEFAULT' or disposal_type='DISMISSED AS NON PROSECUTION' or disposal_type='DISMISSED AS ABATED' or disposal_type='DISMISSED AS NOT PRESSED'  then 1 else 0 end) as dismissed ,"
							+ " sum(case when disposal_type='WITHDRAWN' then 1 else 0 end) as withdrawn,"
							+ " sum(case when disposal_type='CLOSED NO COSTS' or disposal_type='CLOSED AS NOT PRESSED' then 1 else 0 end) as closed,"
							+ " sum(case when disposal_type='REJECTED' or disposal_type='ORDERED' or disposal_type='RETURN TO COUNSEL' or disposal_type='TRANSFERRED' then 1 else 0 end) as returned"
							+ " from ecourts_case_data a inner join ecourts_mst_gp_dept_map b on (a.dept_code=b.dept_code) where b.gp_id='"+userid+"'";
					List<Map<Object, String>> disposedCasesStatus = DatabasePlugin.executeQuery(con, sql);
					request.setAttribute("disposedCasesStatus", disposedCasesStatus);
					
					
				}
				else if(roleId.equals("13") || roleId.equals("14")) { // HC-DEOs
					
				}
				
				else {
					// sql="select count(*) as totalcases, sum(case when assigned=true then 1 else 0 end) as assigned from ecourts_case_data where dept_id in (select dept_id from dept where sdeptcode||deptcode=(select sdeptcode||'01' from dept where dept_id="+deptId+"))";
					// List<Map<Object, String>> dashboardCounts = DatabasePlugin.executeQuery(con, sql);
					// request.setAttribute("dashboardCounts", dashboardCounts);
					
					target = "UserWelcomePageNew";
				}
				
				// sql="select * from ecourts_case_activities where inserted_by='"+userid+"' order by inserted_on desc limit 10 ";
				sql = "select cino, action_type, inserted_by, to_char(inserted_on,'dd-mm-yyyy hh:MM:ss') as inserted_time , assigned_to, remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path, dist_id , dn.dept_code||' - '||dn.description as deptdesc,"
						+ " nd.fullname_en,nd.post_name_en , dm.district_name " + "from ecourts_case_activities ca "
						+ "left join (select distinct employee_id,fullname_en,post_name_en from nic_data) nd on (ca.assigned_to=employee_id) "
						+ "left join dept_new dn on (ca.assigned_to=dn.dept_code) "
						+ "left join district_mst dm on (ca.dist_id=dm.district_id) " + "where inserted_by='" + userid
						+ "' and cino is not null and cino!='null' order by inserted_on desc limit 10";
				System.out.println("RECENT ACTIVITY SQL:" + sql);
				request.setAttribute("recentActivities", DatabasePlugin.executeQuery(con, sql));
			}
			/*
			  else if(roleId!=null && !roleId.equals("") && !roleId.equals("8") &&
			  !roleId.equals("1") && !roleId.equals("7")){ target="UserWelcomePage"; }
			 */else{
				//target="UserWelcomePageNew";
			}
			System.out.println("target:"+target);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}
}