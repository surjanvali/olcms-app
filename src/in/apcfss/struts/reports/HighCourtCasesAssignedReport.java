package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class HighCourtCasesAssignedReport extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesAssignedReport..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		return getCasesList(mapping, cform, request, response);
	}

	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesAssignedReport..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "";
		try {

			con = DatabasePlugin.connect();
			/*
			 * if (cform.getDynaForm("dofFromDate") != null &&
			 * !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
			 * sqlCondition += " and date_of_filing >= to_date('" +
			 * cform.getDynaForm("dofFromDate") + "','dd-mm-yyyy') "; } if
			 * (cform.getDynaForm("dofToDate") != null &&
			 * !cform.getDynaForm("dofToDate").toString().contentEquals("")) { sqlCondition
			 * += " and date_of_filing <= to_date('" + cform.getDynaForm("dofToDate") +
			 * "','dd-mm-yyyy') "; } if (cform.getDynaForm("purpose") != null &&
			 * !cform.getDynaForm("purpose").toString().contentEquals("") &&
			 * !cform.getDynaForm("purpose").toString().contentEquals("0")) { sqlCondition
			 * += " and trim(purpose_name)='" +
			 * cform.getDynaForm("purpose").toString().trim() + "' "; } if
			 * (cform.getDynaForm("districtId") != null &&
			 * !cform.getDynaForm("districtId").toString().contentEquals("") &&
			 * !cform.getDynaForm("districtId").toString().contentEquals("0")) {
			 * sqlCondition += " and trim(dist_name)='" +
			 * cform.getDynaForm("districtId").toString().trim() + "' "; }
			 */

			sql = "select * from ecourts_case_emp_assigned_dtls a inner join ecourts_case_data b on (a.cino=b.cino)"
					+ " inner join (select distinct employee_id,employee_identity,global_org_name,fullname_en,designation_name_en,mobile1, email from nic_data where coalesce(employee_id,'')!='') c"
					+ " on (a.emp_id=c.employee_id and a.emp_section=trim(c.employee_identity))" 
					//+ " where dept_id='" + session.getAttribute("dept_id") + "' " 
					+ " where a.inserted_by='" + session.getAttribute("userid") + "' " 
					+ " order by a.inserted_time";
			

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
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

	public ActionForward sendCaseBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesAssignedReport..............................................................................sendCaseBack");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", cIno = "";
		try {
			cIno = CommonModels.checkStringObject(cform.getDynaForm("cINO"));
			if (cIno != null && !cIno.equals("")) {
				
				con = DatabasePlugin.connect();
				
				sql="select coalesce(case_status,0) as case_status,dept_code from ecourts_case_data where cino='"+cIno+"'";
				
				List<Map> currData = DatabasePlugin.executeQuery(con, sql);
				
				int currentStatus = Integer.parseInt(((Map)currData.get(0)).get("case_status").toString());
				
				String currDept = ((Map)currData.get(0)).get("dept_code").toString();
				
				int backStatus=0;
				
				String roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
				
				if(roleId.equals("4")) {//MLO
					backStatus = 2;
				}
				else if(roleId.equals("5")) {//NO
					backStatus = 4;
				}
				else if(roleId.equals("10")) {//NO-DIST
					backStatus = 8;
				}
				
				/*if(currentStatus==5 && currDept.substring(4,5).equals("01")) {
					backStatus = 2;
				}else if(currentStatus==5 && !currDept.substring(4,5).equals("01")) {
					backStatus = 4;
				}
				else if(currentStatus==9) {
					backStatus = 4;
				}
				else if(currentStatus==10) {
					backStatus = 8;
				}*/
				
				sql = "update ecourts_case_data set assigned=false, assigned_to=null, case_status="+backStatus+" where cino='" + cIno + "' ";
				System.out.println("UPDATE SQL:"+sql);
				int a = DatabasePlugin.executeUpdate(sql, con);
				
				sql = "insert into ecourts_case_emp_assigned_dtls_log select * from ecourts_case_emp_assigned_dtls where cino='" + cIno + "' ";
				System.out.println("UPDATE SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
						+ "values ('" + cIno + "','CASE SENT BACK','"+session.getAttribute("userid")+"', '"+request.getRemoteAddr()+"', '"+session.getAttribute("userid")+"', null )";
				System.out.println("INSERT ACTIVITIES SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql = "delete from ecourts_case_emp_assigned_dtls where cino='" + cIno + "' ";
				System.out.println("DELETE SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if(a > 0) {
					request.setAttribute("successMsg", "Case reverted back to MLO/No");
				}
				
			} else {
				request.setAttribute("errorMsg", "Invalid CIN No.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getCasesList(mapping, cform, request, response);
	}
}