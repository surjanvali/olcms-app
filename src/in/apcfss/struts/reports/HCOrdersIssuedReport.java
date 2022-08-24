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

public class HCOrdersIssuedReport extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null,sqlCondition="";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			else if (roleId.equals("5") || roleId.equals("9")) {

				return HODwisedetails(mapping, form, request, response);
			} else // if(roleId.equals("3") || roleId.equals("4"))
			{
				con = DatabasePlugin.connect();
				
				
				if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
					sqlCondition += " and order_date >= to_date('" + cform.getDynaForm("fromDate")
							+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
					sqlCondition += " and order_date <= to_date('" + cform.getDynaForm("toDate")
							+ "','dd-mm-yyyy') ";
				}
				

				sql = "select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases, sum(interim_orders) as interim_orders, sum(final_orders) as final_orders "
						+ " from (select a.dept_code , case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,count(*) as total_cases, "
						+ "count(distinct io.cino) as interim_orders, count(distinct fo.cino) as final_orders from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
						+ "left join (select distinct cino from ecourts_case_interimorder where 1=1 "+sqlCondition+") io on (a.cino=io.cino) "
						+ "left join (select distinct cino from ecourts_case_finalorder  where 1=1 "+sqlCondition+") fo on (a.cino=fo.cino) "
						+ "where d.display = true ";
				
				sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases, sum(interim_order_cases) as interim_order_cases, sum(final_order_cases) as final_order_cases,  "
						+ " sum(interim_orders)  as interim_orders, sum(final_orders) as final_orders from "
						+ "(select a.dept_code , case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,"
						+ " count(*) as total_cases, count(distinct io.cino) as interim_order_cases, count(distinct fo.cino) as final_order_cases,sum(coalesce(interim_orders,'0')::int4)  as interim_orders, sum(coalesce(final_orders,'0')::int4) as final_orders "
						+ " from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
						+ " left join (select cino, count(*) as interim_orders from ecourts_case_interimorder where 1=1 "+sqlCondition+" group by cino) io on (a.cino=io.cino) "
						+ " left join (select cino, count(*) as final_orders from ecourts_case_finalorder  where 1=1 "+sqlCondition+" group by cino) fo on (a.cino=fo.cino) "
						+ " where d.display = true ";
				
				if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
					sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
							+ session.getAttribute("dept_code") + "')";
				else if(roleId.equals("2") || roleId.equals("10")){
					sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
				}

				sql += " group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
						+ " group by x.reporting_dept_code, d1.description order by 1";

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
		String userId = null, roleId = null, sql = null, deptId = null, deptName="",sqlCondition="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			if(roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
			{
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery("select upper(description) as description from dept_new where dept_code='"+deptId+"'", con);
				// CommonModels.checkStringObject(session.getAttribute("dept_code"));
			}
			else {
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				deptId = !deptId.equals("") ? deptId : CommonModels.checkStringObject(request.getParameter("deptId"));
				deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
				deptName = !deptName.equals("") ? deptName : CommonModels.checkStringObject(request.getParameter("deptName"));
			}

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and order_date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and order_date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").toString().contentEquals("")) {
				sqlCondition += " and order_date >= to_date('" + request.getParameter("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (request.getParameter("toDate") != null && !request.getParameter("toDate").toString().contentEquals("")) {
				sqlCondition += " and order_date <= to_date('" + request.getParameter("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			sql="select a.dept_code as deptcode , d.description,count(*) as total_cases, "
					+ "count(distinct io.cino) as interim_orders, count(distinct fo.cino) as final_orders from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "left join (select distinct cino from ecourts_case_interimorder where 1=1 "+sqlCondition+") io on (a.cino=io.cino) "
					+ "left join (select distinct cino from ecourts_case_finalorder  where 1=1 "+sqlCondition+") fo on (a.cino=fo.cino) "
					+ "where d.display = true ";

			sql="select a.dept_code as deptcode , d.description,count(*) as total_cases, "
					+ " count(distinct io.cino) as interim_order_cases, count(distinct fo.cino) as final_order_cases,sum(coalesce(interim_orders,'0')::int4)  as interim_orders, sum(coalesce(final_orders,'0')::int4) as final_orders "
					+ " from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " left join (select cino,count(*) as interim_orders from ecourts_case_interimorder where 1=1 "+sqlCondition+" group by cino) io on (a.cino=io.cino) "
					+ " left join (select cino,count(*) as final_orders from ecourts_case_finalorder  where 1=1 "+sqlCondition+" group by cino) fo on (a.cino=fo.cino) "
					+ " where d.display = true ";
			
				sql += " and (reporting_dept_code='" + deptId + "' or a.dept_code='" + deptId + "') ";
				if(roleId.equals("2")){
					sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"' ";
				}
				
			sql += " group by a.dept_code,d.description order by 1";

			request.setAttribute("HEADING", "HOD Wise High Court Orders Issued Report for "+deptName);
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
			deptCode = !deptCode.equals("") ? deptCode : CommonModels.checkStringObject(request.getParameter("deptId"));
			
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			caseStatus = !caseStatus.equals("") ? caseStatus : CommonModels.checkStringObject(request.getParameter("caseStatus"));
			
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			actionType = !actionType.equals("") ? actionType : CommonModels.checkStringObject(request.getParameter("actionType"));
			
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			deptName = !deptName.equals("") ? deptName : CommonModels.checkStringObject(request.getParameter("deptName"));
			
			heading="Cases List for "+deptName;
			
			// Interim Orders Issued
			// Final Orders Issued
			// Date Filters
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("IO")){
						heading+=" Interim Orders Issued ";
					}
				if(caseStatus.equals("FO")) {
					heading+=" Final Orders Issued ";
				}
			}
			
			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and order_date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and order_date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").toString().contentEquals("")) {
				sqlCondition += " and order_date >= to_date('" + request.getParameter("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (request.getParameter("toDate") != null && !request.getParameter("toDate").toString().contentEquals("")) {
				sqlCondition += " and order_date <= to_date('" + request.getParameter("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			String condition="";
			//if (roleId.equals("6") )
				//condition= " inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) ";
			
			
			
			sql = "select a.*, b.orderpaths, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full, ra.address from ecourts_case_data a "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					
					+ ""+condition+" inner join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from (select * from";

			if (caseStatus.equals("IO") && roleId.equals("6"))
				sql += "  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder "
						+ " where 1=1  " + sqlCondition + ") x1";
			
			if (caseStatus.equals("IO") && !roleId.equals("6"))
				sql += "  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 " + sqlCondition + ") x1";

			if (caseStatus.equals("FO") && !roleId.equals("6"))
				sql += " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 " + sqlCondition + ") x2";
			
			if (caseStatus.equals("FO") && roleId.equals("6"))
				sql += " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder "
						+ " where length(order_document_path) > 10  " + sqlCondition + ") x2";

			sql += " order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code)  where d.display = true ";

			// sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') ";
			if (CommonModels.checkStringObject(request.getParameter("repType")).equals("HOD"))
				sql += " and (a.dept_code='" + deptCode + "') ";
			else  if (CommonModels.checkStringObject(request.getParameter("repType")).equals("SD"))
				sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') ";
			
			
			
			if(roleId.equals("3") || roleId.equals("4")) {
				sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='" + session.getAttribute("dept_code") + "') ";
			}
			if(roleId.equals("5") || roleId.equals("9") || roleId.equals("10")) {
				sql += " and (a.dept_code='" + session.getAttribute("dept_code") + "') ";
			}
			
			
			if(roleId.equals("2") || roleId.equals("10")){
				sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}
			
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