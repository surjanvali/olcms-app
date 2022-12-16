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

public class NextListingStatusAbstractReport2 extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		String condition = "",sqlCondition="";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			con = DatabasePlugin.connect();
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			else  if(roleId.equals("5") || roleId.equals("9")) {
				
				return HODwisedetails(mapping, form, request, response);
			}else if ((roleId.equals("6"))) {
				condition = " inner join ecourts_mst_gp_dept_map emgm on (a.dept_code=emgm.dept_code)  ";
				sqlCondition += " and emgm.gp_id='" + userId + "'";
			}
			//else //if(roleId.equals("3") || roleId.equals("4"))
			//{
				
				sql="select a1.reporting_dept_code as deptcode,dn1.description,sum(total) as  total,sum(today) as today, sum(tomorrow) as tomorrow,sum(week1) as week1, "
						+ " sum(week2) as week2, sum(week3) as week3,sum(week4) as week4 "
						+ " from ( "
						+ " select case when reporting_dept_code='CAB01' then a.dept_code else reporting_dept_code end as reporting_dept_code,a.dept_code,count(*) as total"
						+ ",sum(case when date_next_list = current_date then 1 else 0 end) as today,"
						+ "sum(case when date_next_list = current_date+1 then 1 else 0 end) as tomorrow,"
						+ "sum(case when date_next_list > current_date and date_next_list <= current_date+7  then 1 else 0 end) as week1,  "
						+ "sum(case when date_next_list > current_date+7 and date_next_list <= current_date+14  then 1 else 0 end) as week2,  "
						+ "sum(case when date_next_list > current_date+14 and date_next_list <= current_date+21  then 1 else 0 end) as week3,  "
						+ "sum(case when date_next_list > current_date+21 and date_next_list <= current_date+28  then 1 else 0 end) as week4"
						+ " from ecourts_case_data a "
						+ " inner join dept_new dn on (a.dept_code=dn.dept_code) "+condition+" ";

						if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
							sql+=" and (dn.reporting_dept_code='"+session.getAttribute("dept_code")+"' or dn.dept_code='"+session.getAttribute("dept_code")+"')";
						else if(roleId.equals("2")){
							sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
						}
						
						
						sql+= " "+sqlCondition+" group by reporting_dept_code,a.dept_code) a1"
						
						+ " inner join dept_new dn1 on (a1.reporting_dept_code=dn1.dept_code) "
						+ " group by a1.reporting_dept_code,dn1.description"
						+ " order by 1";

			request.setAttribute("HEADING", "Sect. Dept. Wise Cases Hearing Abstract Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("secdeptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			//}
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
		String userId = null, roleId = null, sql = null, deptId = null, deptName = "";
		String condition = "",sqlCondition="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			if (roleId.equals("5") || roleId.equals("9")) {
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery(
						"select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
				// CommonModels.checkStringObject(session.getAttribute("dept_code"));
			} else {
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			}
			 if ((roleId.equals("6"))) {
				condition = " inner join ecourts_mst_gp_dept_map emgm on (a.dept_code=emgm.dept_code)  ";
				sqlCondition += " and emgm.gp_id='" + userId + "'";
			}
			 if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2") || roleId.equals("14") || roleId.equals("17"))) {
					sqlCondition += " and (a.dept_code='" + deptId + "' or dn.reporting_dept_code='" + deptId+ "') ";
				}
			 if(roleId.equals("2")){
					sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
				}
			
			sql = "select a.dept_code as deptcode,dn.description,"
				+ " count(*) as total"
				+ ",sum(case when date_next_list = current_date then 1 else 0 end) as today,"
				+ "sum(case when date_next_list = current_date+1 then 1 else 0 end) as tomorrow,"
				+ "sum(case when date_next_list > current_date and date_next_list <= current_date+7  then 1 else 0 end) as week1,  "
				+ "sum(case when date_next_list > current_date+7 and date_next_list <= current_date+14  then 1 else 0 end) as week2,  "
				+ "sum(case when date_next_list > current_date+14 and date_next_list <= current_date+21  then 1 else 0 end) as week3,  "
				+ "sum(case when date_next_list > current_date+21 and date_next_list <= current_date+28  then 1 else 0 end) as week4"
				+ " from ecourts_case_data a "


					+ " inner join dept_new dn on (a.dept_code=dn.dept_code)  "+condition+" "
					+ " where dn.display = true  "+sqlCondition+" ";
			
					
					
					sql+= "group by a.dept_code,dn.description order by 1";

			request.setAttribute("HEADING", "HOD Wise Cases Abstract (Hearing) for " + deptName);
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
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
				if(caseStatus.equals("today")){
						sqlCondition= " and date_next_list = current_date ";
						heading+=" Hearing on Today";
					}
				if(caseStatus.equals("tomorrow")) {
					sqlCondition=" and date_next_list = current_date+1 ";
					heading+=" Hearing on Tomorrow";
				}
				if(caseStatus.equals("week1")) {
					sqlCondition=" and date_next_list > current_date and date_next_list <= current_date+7  ";
					heading+=" Hearing on next 7 days";
				}
				if(caseStatus.equals("week2")) {
					sqlCondition= " and date_next_list > current_date+7 and date_next_list <= current_date+14 ";
					heading+=" Hearing on next 8 - 14 days";
				}
				if(caseStatus.equals("week3")) {
					sqlCondition=" and date_next_list > current_date+14 and date_next_list <= current_date+21 ";
					heading+=" Hearing on next 15 - 21 days";
				}
				if(caseStatus.equals("week4")) {
					sqlCondition=" and date_next_list > current_date+21 and date_next_list <= current_date+28 ";
					heading+=" Hearing on next 21 - 28 days";
				}
			}
			
			sql = "select a.*, "
					+ "";
					//+ "coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a "
				sql += " nda.fullname_en as fullname, nda.designation_name_en as designation, nda.post_name_en as post_name, nda.email, nda.mobile1 as mobile,dim.district_name , ";
				sql += " 'Pending at '||ecs.status_description||'' as current_status, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths,"
					+ " case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full, ra.address from ecourts_case_data a "
					
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ " left join district_mst dim on (a.dist_id=dim.district_id) "
					+ " inner join ecourts_mst_case_status ecs on (a.case_status=ecs.status_id) "
					+ " left join nic_data_all nda on (a.dept_code=substr(nda.global_org_name,1,5) and a.assigned_to=nda.email and nda.is_primary='t' and coalesce(a.dist_id,'0')=coalesce(nda.dist_id,'0')) "
					
					+ " left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true ";
			
			
			if(roleId.equals("2")){
				sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}
			
			sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') " + sqlCondition;
			
			sql +=" order by date_next_list asc";
			
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