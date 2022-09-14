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

public class DistrictWiseFinalOrdersImplementation extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	public ActionForward getFinalOrdersImplReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and order_date >= to_date('" + cform.getDynaForm("fromDate") + "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and order_date <= to_date('" + cform.getDynaForm("toDate") + "','dd-mm-yyyy') ";
			}
			
			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				sqlCondition += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
						+ session.getAttribute("dept_code") + "')";

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}
			
			sql=" select dm.district_id, dm.district_name, "
					+ " coalesce(d.casescount,'0') casescount, "
					+ " coalesce(d.order_implemented,'0') order_implemented, "
					+ " coalesce(d.appeal_filed,'0') appeal_filed, "
					+ " coalesce(d.dismissed_copy,'0') dismissed_copy,  "
					+ " coalesce(d.closed,'0') closed,    "
					+ " coalesce(casescount-(order_implemented + appeal_filed+dismissed_copy+closed),'0') as pending,   "
					+ " "
					+ " case when coalesce(d.casescount,'0') > 0 then round((((coalesce(order_implemented,'0')::int4 + coalesce(appeal_filed,'0')::int4 + coalesce(dismissed_copy,'0')::int4 + coalesce(closed,'0')::int4) * 100) / coalesce(d.casescount,'0')) , 2) else 0 end as actoin_taken_percent "
					
					//+ " round(coalesce( (order_implemented::numeric + appeal_filed::numeric+dismissed_copy::numeric+closed::numeric)/(4*100::numeric),'0'),2) as actoin_taken_percent   "
					
					+ " from district_mst dm "
					+ " left join ( select dist_id,count( a.cino) as casescount,   "
					+ " sum(case when length(action_taken_order)> 10   or final_order_status='final' then 1 else 0 end) as order_implemented ,  "
					+ " sum(case when length(appeal_filed_copy)> 10 or final_order_status='appeal'  then 1 else 0 end) as appeal_filed ,"
					+ " sum(case when length(dismissed_copy)> 10 or final_order_status='dismissed' or ocd.ecourts_case_status='dismissed' then 1 else 0 end) as dismissed_copy,"
					+ " sum(case when ocd.ecourts_case_status='Closed' then 1 else 0 end) as closed   from  ecourts_case_data a  "
					+ " inner join ecourts_case_finalorder b on (a.cino=b.cino)  LEFT join dept_new dn on (a.dept_code=dn.dept_code) "
					+ " LEFT join ecourts_olcms_case_details ocd on (a.cino=ocd.cino)   "
					+ " where 1=1  " +sqlCondition 
					+ " group by dist_id ) d on (dist_id=dm.district_id) order by casescount desc ";

			request.setAttribute("HEADING", "District Wise Cases Final Orders Implementation Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("FINALORDERSREPORT", data);
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
		String sql = null, sqlCondition = "", actionType = "", districtId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null,condition="";
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			districtId = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			
			
			System.out.println("caseStatus--"+caseStatus);
			heading="Cases List for "+deptName;
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("CLOSED")){
						sqlCondition+= " and coalesce(a.ecourts_case_status,'')='Closed' ";
						heading+=" Closed Cases List";
					}
				
				if(caseStatus.equals("FINALORDER")) {
					sqlCondition+=" and (length(action_taken_order)> 10   or final_order_status='final') ";
					heading+="  Final orders implemented";
				}
				if(caseStatus.equals("APPEALFILED")) {
					sqlCondition+=" and  (length(appeal_filed_copy)> 10 or final_order_status='appeal') ";
					heading+="  Appeal Final orders";
				}
				if(caseStatus.equals("DISMISSED")) {
					sqlCondition+=" and (length(dismissed_copy)> 10 or final_order_status='dismissed' or ocd.ecourts_case_status='dismissed') ";
					heading+="  Dismissed Final Orders";
				}
				if(caseStatus.equals("PENDING")) {
					sqlCondition+=" and  case_status=7 ";
					heading+="  District Collector Final Orders";
				}
				
			}
		
			if(roleId.equals("2")){
				sqlCondition+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}else if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
			{
				sqlCondition += " and (a.dept_code='" + deptCode + "') " ;
			}
			
			 if(cform.getDynaForm("distid") != null
						&& !CommonModels.checkStringObject(cform.getDynaForm("distid")).contentEquals("")) {
					sqlCondition+=" and a.dist_id='"+cform.getDynaForm("distid")+"'";
			   }
				
			 if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
				
				if (deptCode != null && !deptCode.toString().contentEquals("")
						&& !deptCode.toString().contentEquals("0")) {
					sqlCondition += " and a.dept_code='" + deptCode + "' ";
				}
				
				
				System.out.println("---"+cform.getDynaForm("distid"));
			
			sql = "select a.*, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a  "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1)"
					+ "  left join ecourts_olcms_case_details eocd on (a.cino=eocd.cino)  "
					+ " inner join ( select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths "
					+ " from  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null "
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0 and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) c group by cino ) b on (a.cino=b.cino)"
					+ " inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true   "+sqlCondition+"    ";
			
			
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

	public ActionForward getCCCasesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and dt_regis >= to_date('" + cform.getDynaForm("fromDate") + "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and dt_regis <= to_date('" + cform.getDynaForm("toDate") + "','dd-mm-yyyy') ";
			}
			
			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				sqlCondition += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
						+ session.getAttribute("dept_code") + "')";

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			sql = "select dist_id, district_name, casescount, counterscount" + " from ( "
					+ " select dist_id,dm.district_name,count(distinct a.cino) as casescount,  "
					+ " sum(case when length(counter_filed_document)> 10 then 1 else 0 end) as counterscount   "
					+ "  "
					+ " from district_mst dm left join ecourts_case_data a on (a.dist_id=dm.district_id)"
					+ " inner join dept_new dn on (a.dept_code=dn.dept_code) " + " "
					+ " left join ecourts_olcms_case_details ocd on (a.cino=ocd.cino) "
					+ " where type_name_reg='CC' " +sqlCondition
					+ " group by dist_id, dm.district_name) a1 order by casescount desc";
			
			sql= "select  district_id,dm.district_name,coalesce(d.casescount,'0') as casescount,coalesce(d.counterscount,'0') as counterscount  "
					+ "  from district_mst dm left join "
					+ "( select dist_id,count(distinct a.cino) as casescount,   "
					+ " sum(case when length(counter_filed_document)> 10 then 1 else 0 end) as counterscount  "
					+ " from ecourts_case_data a  "
					+ " LEFT join dept_new dn on (a.dept_code=dn.dept_code)   "
					+ " LEFT join ecourts_olcms_case_details ocd on (a.cino=ocd.cino)  "
					+ " where type_name_reg='CC' " +sqlCondition
					+ " group by a.dist_id  ) d on  (d.dist_id=dm.district_id) order by casescount desc" ;

			request.setAttribute("HEADING", "District Wise Cases Contempt Cases Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CCCASESREPORT", data);
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
	
	public ActionForward getNewCasesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();
			
			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				sqlCondition += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
						+ session.getAttribute("dept_code") + "')";

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}
			
			
			
			sql= "select district_id, district_name,coalesce(d.casescount,'0') casescount,coalesce(d.counterscount,'0') counterscount  "
					+ " from district_mst dm left join  "
					+ " ( select dist_id,count(distinct a.ack_no) as casescount,   "
					+ "  sum(case when length(counter_filed_document)> 10 then 1 else 0 end) as counterscount     "
					+ " from  ecourts_gpo_ack_depts a inner join ecourts_gpo_ack_dtls b on (a.ack_no=b.ack_no)    "
					+ " left join ecourts_olcms_case_details ocd on (a.ack_no=ocd.cino) inner join dept_new dn on (a.dept_code=dn.dept_code) "
					+ " where  ack_type='NEW' and a.respondent_slno='1'   "+sqlCondition //inserted_time::date >= current_date - 30 
					+ "group by dist_id ) d on d.dist_id=dm.district_id order by casescount desc  ";

			request.setAttribute("HEADING", "District Wise Contempt Cases Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("FRESHCASESREPORT", data);
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

	public ActionForward getLegacyCasesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and dt_regis >= to_date('" + cform.getDynaForm("fromDate") + "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and dt_regis <= to_date('" + cform.getDynaForm("toDate") + "','dd-mm-yyyy') ";
			}
			
			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				sqlCondition += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
						+ session.getAttribute("dept_code") + "')";

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}
			
			sql = "select dist_id, district_name, casescount, counterscount from ( "
					+ " select dist_id,dm.district_name,count(distinct a.cino) as casescount,  "
					+ " sum(case when length(counter_filed_document)> 10 then 1 else 0 end) as counterscount   " + "  "
					+ " from district_mst dm left join ecourts_case_data a on (a.dist_id=dm.district_id)"
					+ " inner join dept_new dn on (a.dept_code=dn.dept_code) " + " "
					+ " left join ecourts_olcms_case_details ocd on (a.cino=ocd.cino) "
					+ " where 1=1 "+sqlCondition
					+ " group by dist_id, dm.district_name) a1 order by casescount desc";
			
			sql ="select district_id, district_name,coalesce(d.casescount,'0') casescount,coalesce(d.counterscount,'0') counterscount "
					+ " from district_mst dm left join "
					+ " (select dist_id,count(distinct a.cino) as casescount,   "
					+ " sum(case when length(counter_filed_document)> 10 then 1 else 0 end) as counterscount      "
					+ " from  ecourts_case_data a  inner join dept_new dn on (a.dept_code=dn.dept_code)  "
					+ " left join ecourts_olcms_case_details ocd on (a.cino=ocd.cino)  "
					+ " where 1=1  "+sqlCondition
					+ " group by dist_id ) d on d.dist_id=dm.district_id order by casescount desc ";

			request.setAttribute("HEADING", "District Wise Legacy Cases Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("LEGACYCASESREPORT", data);
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
	
}