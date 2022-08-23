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

public class HighCourtCasesCategoryUpdationAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, sqlCondition = "", dept_code = "";
		CommonForm cform = (CommonForm) form;
		try {
			System.out.println(
					"HCCaseStatusAbstractReport..............................................................................unspecified()");
			System.out.println("unspecified unspecified unspecified");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			dept_code = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			else if (roleId.equals("5") || roleId.equals("9")) {

				return HODwisedetails(mapping, form, request, response);
			} else // if(roleId.equals("3") || roleId.equals("4"))
			{
				con = DatabasePlugin.connect();

				String str1 = "";

				if ((roleId.equals("4") || roleId.equals("5") || roleId.equals("3")) && dept_code.equals("FIN01")) {

					str1 = " ";
				} else {

					str1 = " where b.dept_code='" + dept_code + "' ";
				}

				if (roleId.equals("1") || roleId.equals("7"))
					str1 = " ";

				sql = "select x.reporting_dept_code as deptcode, upper(d1.description) as description,   sum(a1) as a1 ,sum(a2) as a2 , sum(b1) as b1 ,  sum(b2) as b2, sum(c1) as c1 ,sum(c2)as c2  from "
						+ "( select c.dept_code , case when reporting_dept_code='CAB01' then c.dept_code else reporting_dept_code end as reporting_dept_code,"
						+ " c.dept_code as deptcode,upper(c.description) as description, "
						+ "  coalesce(sum (case  when a.finance_category='A1' then 1 end),'0') as A1 ,"
						+ " coalesce(sum (case  when a.finance_category='A2' then 1 end),'0') as A2 ,"
						+ " coalesce(sum (case  when a.finance_category='B1' then 1 end),'0') as B1 ,"
						+ " coalesce(sum (case  when a.finance_category='B2' then 1 end),'0') as B2 ,"
						+ " coalesce(sum (case  when a.finance_category='C1' then 1 end),'0') as C1 ,"
						+ " coalesce(sum (case  when a.finance_category='C2' then 1 end),'0') as C2  "
						+ " from ecourts_case_category_wise_data a "
						+ " inner join ecourts_case_data b on (a.cino=b.cino)      "
						+ "  inner join dept_new c on (b.dept_code=c.dept_code)     " + str1 + "  "
						+ " group by description,c.dept_code,c.reporting_dept_code order by c.description )  "
						+ " x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code) "
						+ " group by x.reporting_dept_code, d1.description,a1 ,a2 , b1 ,  b2 , c1 ,c2 order by 1";

				request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

				System.out.println("unspecified SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("unspecified data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("secdeptwise", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {
				DatabasePlugin.closeConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mapping.findForward("success");
	}

	public ActionForward HODwisedetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, deptId = null, deptName = "", sqlCondition = "";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			System.out.println("deptId---" + CommonModels.checkStringObject(session.getAttribute("dept_code")));

			if (roleId.equals("5") || roleId.equals("9")) {
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery(
						"select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
				// CommonModels.checkStringObject(session.getAttribute("dept_code"));
			} else {

				if (request.getParameter("deptId").toString().equals(null)) {
					deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
					deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
				} else {

					deptId = CommonModels.checkStringObject(request.getParameter("deptId").toString());
					deptName = CommonModels.checkStringObject(request.getParameter("deptName"));
				}
			}

			sql = " select c.dept_code as deptcode,upper(c.description) as description, "
					+ " coalesce(sum (case  when a.finance_category='A1' then 1 end),'0') as A1 ,"
					+ "coalesce(sum (case  when a.finance_category='A2' then 1 end),'0') as A2 ,"
					+ "coalesce(sum (case  when a.finance_category='B1' then 1 end),'0') as B1 ,"
					+ "coalesce(sum (case  when a.finance_category='B2' then 1 end),'0') as B2 ,"
					+ "coalesce(sum (case  when a.finance_category='C1' then 1 end),'0') as C1 ,"
					+ "coalesce(sum (case  when a.finance_category='C2' then 1 end),'0') as C2 "
					+ " from ecourts_case_category_wise_data a  inner join ecourts_case_data b on (a.cino=b.cino)       "
					+ " inner join dept_new c on (b.dept_code=c.dept_code) " + " where ( c.reporting_dept_code='"
					+ deptId + "' or b.dept_code='" + deptId + "' ) " // request.getParameter("deptId").toString()
					+ " group by description,c.dept_code order by c.description";

			request.setAttribute("HEADING", "HOD Wise High Court Cases Abstract Report for " + deptName);
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("deptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {

				DatabasePlugin.closeConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		String sql = null, sqlCondition = "", actionType = "", deptName = "", heading = "", roleId = null,
				deptCode = null, caseStatus = null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			heading = "Cases List for " + deptName;

			String fromRep = CommonModels.checkStringObject(request.getParameter("fromRep"));
			String deptId = CommonModels.checkStringObject(request.getParameter("deptId"));

			if (fromRep.equals("SD")) {
				sqlCondition = " and (c.reporting_dept_code='" + deptId + "' or c.dept_code='" + deptId + "') ";
			} else if (fromRep.equals("HOD")) {
				sqlCondition = " and c.dept_code='" + deptId + "'";
			}

			if (!request.getParameter("reportType").toString().equals("All")) {
				sql = "SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,"
						+ " purpose_name,res_name,pet_name,pet_adv,res_adv, d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,e.cfms_bill,e.bill_amount  "
						+ " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code)"
						+ "  inner join (select cino, string_agg(cfms_bill_id,',') as cfms_bill, sum(COALESCE(NULLIF(trim(cfms_bill_amount),''), '0')::int4) as bill_amount"
						+ "  from cfms_bill_data_mst group by cino) e on (a.cino=e.cino)  where  d.finance_category='"
						+ request.getParameter("reportType").toString() + "' " + "  " + sqlCondition
						+ "   ORDER BY d.finance_category ";
			} else {
				sql = "SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,"
						+ " purpose_name,res_name,pet_name,pet_adv,res_adv, d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,e.cfms_bill,e.bill_amount  "
						+ " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code)"
						+ "  inner join (select cino, string_agg(cfms_bill_id,',') as cfms_bill, sum(COALESCE(NULLIF(trim(cfms_bill_amount),''), '0')::int4) as bill_amount"
						+ "  from cfms_bill_data_mst group by cino) e on (a.cino=e.cino)  where  1=1 " + sqlCondition
						+ "    ORDER BY d.finance_category ";
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

			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}

}