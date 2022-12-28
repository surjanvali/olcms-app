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

public class PullBackNewCasesAction extends DispatchAction {

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

			/*
			 * sql =
			 * "select distinct b.cino,* from ecourts_case_emp_assigned_dtls a inner join ecourts_case_data b on (a.cino=b.cino)"
			 * +
			 * " inner join (select distinct employee_id,employee_identity,global_org_name,fullname_en,designation_name_en,mobile1, email from nic_data where coalesce(employee_id,'')!='') c"
			 * + " on ( a.emp_section=trim(c.employee_identity))" //a.emp_id=c.employee_id
			 * and //+ " where dept_id='" + session.getAttribute("dept_id") + "' " +
			 * " where a.inserted_by='" + session.getAttribute("userid") + "' ";
			 */

			
			/*
			 * sql=" select a.ack_no,* from ecourts_gpo_ack_dtls a inner join ecourts_gpo_ack_depts c on (a.ack_no=c.ack_no) "
			 * + "inner join section_officer_details b on (c.assigned_to=b.emailid)  " +
			 * "where b.inserted_by='"+session.getAttribute("userid")+"'   order by b.emailid";
			 */
			
			sql=" select a.ack_no,* from ecourts_gpo_ack_dtls a inner join ecourts_gpo_ack_depts f on (a.ack_no=f.ack_no)  "
					+ " inner join (select emailid,inserted_by from nodal_officer_details b  where b.inserted_by='"+session.getAttribute("userid")+"' "
					+ " union all "
					+ " select emailid,inserted_by from mlo_details c  where c.inserted_by='"+session.getAttribute("userid")+"'  "
					+ " union all "
					+ " select emailid,inserted_by from section_officer_details d where d.inserted_by='"+session.getAttribute("userid")+"') e on (f.assigned_to=e.emailid) order by e.emailid";

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
		String[] ids_split=null;
		String sql = null, cIno = "", roleId=null, deptCode=null, distCode=null;
		try {

			int a=0;
			cIno = CommonModels.checkStringObject(cform.getDynaForm("cINO"));

			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (cIno != null && !cIno.equals("")) {

				con = DatabasePlugin.connect();
				
				String ids=cIno;
				 ids_split=ids.split("@");
				System.out.println("ids--"+ids_split[0]);
				System.out.println("ids--"+ids_split[1]);
				

				// sql="select coalesce(case_status,0) as case_status,dept_code from ecourts_case_data where cino='"+cIno+"'";
				// List<Map> currData = DatabasePlugin.executeQuery(con, sql);
				// int currentStatus = Integer.parseInt(((Map)currData.get(0)).get("case_status").toString());
				// String currDept = ((Map)currData.get(0)).get("dept_code").toString();

				int backStatus=0;

				if(roleId.equals("4")) {//MLO
					backStatus = 2;
				}
				else if(roleId.equals("5")) {//NO
					backStatus = 4;
				}
				else if(roleId.equals("10")) {//NO-DIST
					backStatus = 8;
					//distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
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
				if(roleId.equals("10")) {

					sql = "update ecourts_gpo_ack_depts set assigned=false, assigned_to=null, case_status="+backStatus+", dept_code='"+deptCode+"',dist_id='"+distCode+"'  where ack_no='" + ids_split[0] + "'   and respondent_slno='"+ids_split[1]+"'  ";
					System.out.println("UPDATE SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
				}else if(roleId.equals("2")) {

					sql = "update ecourts_gpo_ack_depts set assigned=false, assigned_to=null, case_status="+backStatus+", dist_id='"+distCode+"'  where ack_no='" + ids_split[0] + "' and respondent_slno='"+ids_split[1]+"' ";
					System.out.println("UPDATE SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
				}else {

					sql = "update ecourts_gpo_ack_depts set assigned=false, assigned_to=null, case_status="+backStatus+", dept_code='"+deptCode+"'  where ack_no='" + ids_split[0] + "' and respondent_slno='"+ids_split[1]+"' ";
					System.out.println("UPDATE SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);

				}

				sql = "insert into ecourts_case_emp_assigned_dtls_log select * from ecourts_case_emp_assigned_dtls where cino='" + ids_split[0] + "' ";
				System.out.println("UPDATE SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);

				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
						+ "values ('" + ids_split[0] + "','CASE SENT BACK','"+session.getAttribute("userid")+"', '"+request.getRemoteAddr()+"', '"+session.getAttribute("userid")+"', null )";
				System.out.println("INSERT ACTIVITIES SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);

				sql = "delete from ecourts_case_emp_assigned_dtls where cino='" + ids_split[0] + "' ";
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