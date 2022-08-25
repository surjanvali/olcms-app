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
			
			sql = "select dist_id, district_name, casescount,order_implemented,appeal_filed,  "
					+ " casescount-(order_implemented + appeal_filed) as pending,   "
					+ " (order_implemented + appeal_filed)/(4*100) as actoin_taken_percent " + " from ( "
					+ " select dist_id,dm.district_name,count(distinct a.cino) as casescount,  "
					+ " sum(case when length(action_taken_order)> 10 then 1 else 0 end) as order_implemented , "
					+ " sum(case when length(appeal_filed_copy)> 10 then 1 else 0 end) as appeal_filed " + "  "
					+ " from district_mst dm left join ecourts_case_data a on (a.dist_id=dm.district_id)"
					+ " inner join ecourts_case_finalorder b on (a.cino=b.cino)  "
					+ " inner join dept_new dn on (a.dept_code=dn.dept_code) " + " "
					+ " left join ecourts_olcms_case_details ocd on (a.cino=ocd.cino) "
					+ " where 1=1 " +sqlCondition
					+ " group by dist_id, dm.district_name) a1 order by casescount desc";
			
			sql=" select dm.district_id, dm.district_name, coalesce(d.casescount,'0') casescount,"
					+ " coalesce(d.order_implemented,'0') order_implemented,  coalesce(d.appeal_filed,'0') appeal_filed,  "
					+ " coalesce(casescount-(order_implemented + appeal_filed),'0')     as pending,   "
					+ "  coalesce( (order_implemented + appeal_filed)/(4*100),'0')    as actoin_taken_percent  "
					+ " from district_mst dm left join ("
					+ " select dist_id,count(distinct a.cino) as casescount,   "
					+ " sum(case when length(action_taken_order)> 10 then 1 else 0 end) as order_implemented , "
					+ " sum(case when length(appeal_filed_copy)> 10 then 1 else 0 end) as appeal_filed    "
					+ "from  ecourts_case_data a  "
					+ "inner join ecourts_case_finalorder b on (a.cino=b.cino)  "
					+ "LEFT join dept_new dn on (a.dept_code=dn.dept_code)  "
					+ "LEFT join ecourts_olcms_case_details ocd on (a.cino=ocd.cino) "
					+ "  where 1=1   " +sqlCondition
					+ " group by dist_id ) d on (dist_id=dm.district_id) order by casescount desc";

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
					+ " where 1=1  "+sqlCondition // inserted_time::date >= current_date - 30 
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