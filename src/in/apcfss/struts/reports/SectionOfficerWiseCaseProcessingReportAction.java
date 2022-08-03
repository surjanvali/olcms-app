package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
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

public class SectionOfficerWiseCaseProcessingReportAction extends DispatchAction {

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
			String roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			System.out.println("roleId--"+roleId);
			System.out.println("distCode--"+distCode);
			String dist_table="";
			dist_table = AjaxModels.getTableName(distCode, con);

			/*if(distCode.equals("1")) {
				dist_table="nic_data_anakapalli";
			}else if(distCode.equals("2")) {
				dist_table="nic_data_ananthapur";

			}else if(distCode.equals("3")) {
				dist_table="nic_data_annamayya";
			}else if(distCode.equals("4")) {
				dist_table="nic_data_bapatla";
			}else if(distCode.equals("5")) {
				dist_table="nic_data_chittoor";
			}else if(distCode.equals("6")) {
				dist_table="nic_data_eastgodavari";
			}else if(distCode.equals("7")) {
				dist_table="nic_data_eluru";
			}else if(distCode.equals("8")) {
				dist_table="nic_data_guntur";
			}else if(distCode.equals("9")) {
				dist_table="nic_data_kadapa";
			}else if(distCode.equals("10")) {
				dist_table="nic_data_kakinada";
			}else if(distCode.equals("11")) {
				dist_table="nic_data_konaseema";
			}else if(distCode.equals("12")) {
				dist_table="nic_data_krishna";
			}else if(distCode.equals("13")) {
				dist_table="nic_data_kurnool";
			}else if(distCode.equals("14")) {
				dist_table="nic_data_nandyal";
			}else if(distCode.equals("15")) {
				dist_table="nic_data_nellore";
			}else if(distCode.equals("16")) {
				dist_table="nic_data_ntr";
			}else if(distCode.equals("17")) {
				dist_table="nic_data_palnadu";
			}else if(distCode.equals("18")) {
				dist_table="nic_data_pmanyam";
			}else if(distCode.equals("19")) {
				dist_table="nic_data_prakasam";
			}else if(distCode.equals("20")) {
				dist_table="nic_data_srikakulam";
			}else if(distCode.equals("21")) {
				dist_table="nic_data_srisathyasai";
			}else if(distCode.equals("22")) {
				dist_table="nic_data_nandyal";
			}else if(distCode.equals("23")) {
				dist_table="nic_data_tirupati";
			}else if(distCode.equals("24")) {
				dist_table="nic_data_visakhapatnam";
			}
			else if(distCode.equals("25")) {
				dist_table="nic_data_vizianagaram";
			}
			else if(distCode.equals("26")) {
				dist_table="nic_data_westgodavari";
			}*/


			/*
			 * sql =
			 * "select * from ecourts_case_emp_assigned_dtls a inner join ecourts_case_data b on (a.cino=b.cino)"
			 * +
			 * " inner join (select distinct employee_id,employee_identity,global_org_name,fullname_en,designation_name_en,mobile1, email from nic_data where coalesce(employee_id,'')!='') c"
			 * + " on (a.emp_id=c.employee_id and a.emp_section=trim(c.employee_identity))"
			 * + " where a.inserted_by='" + session.getAttribute("userid") + "' " +
			 * " order by a.inserted_time";
			 */


			if(session.getAttribute("role_id").equals("4") || session.getAttribute("role_id").equals("5")) {

				sql="select fullname_en,global_org_name,designation_name_en,mobile1,email,emp_id ,count(*)  as total "
						+ " from ecourts_case_emp_assigned_dtls a inner join ecourts_case_data b on (a.cino=b.cino)"
						+ " inner join (select distinct employee_id,employee_identity,global_org_name,fullname_en,designation_name_en,mobile1, email "
						+ " from nic_data where coalesce(employee_id,'')!='') c on (a.emp_id=c.employee_id and a.emp_section=trim(c.employee_identity)) "
						+ " where a.inserted_by='" + session.getAttribute("userid") +"' group by fullname_en,designation_name_en,global_org_name,mobile1,email,emp_id ";

			}else {//if(session.getAttribute("role_id").equals("10"))
				sql="select fullname_en,global_org_name,designation_name_en,emp_id,count(*)  as total from ecourts_case_emp_assigned_dtls a inner join ecourts_case_data b on (a.cino=b.cino)"
						+ " inner join (select distinct employee_id,employee_identity,global_org_name,fullname_en,designation_name_en,mobile1, email,districtcode "
						+ "from "+dist_table+" where coalesce(employee_id,'')!='' and districtcode='"+distCode+"') c on (a.emp_id=c.employee_id and a.emp_section=trim(c.employee_identity)) "
						+ " where a.inserted_by='" + session.getAttribute("userid") + "' and districtcode='"+distCode+"' group by fullname_en,designation_name_en,global_org_name,mobile1,email,emp_id ";
			}

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
	public ActionForward getCasesDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		String sql = null, sqlCondition = "", actionType = "", email = "", deptName = "", heading = "";
		try {

			con = DatabasePlugin.connect();

			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			email = CommonModels.checkStringObject(cform.getDynaForm("email"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));

			heading = " Section Officer Wise Case Processing Report  ";


			sql = "select a.*, "
					+ "coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ " left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code)  where assigned_to='" + email+ "' ";




			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLISTDETAILS", data);
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