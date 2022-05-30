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

public class AcknowledgementAbstractReport extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			con = DatabasePlugin.connect();

			if (roleId.equals("2"))
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+distCode+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2") || roleId.equals("14"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select sno,case_full_name from case_type_master order by sno",
					con));
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		// return mapping.findForward("success");
		return showDeptWise(mapping, cform, request, response);
	}

	public ActionForward showDistWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) 
			{
				sqlCondition += " and d.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and ad.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and inserted_time::date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and inserted_time::date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}

			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2") || roleId.equals("14"))) {
					sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='"+deptCode+"') ";
			}
			
			if ((roleId.equals("1") || roleId.equals("7") || roleId.equals("14"))) {
				request.setAttribute("SHOWUSERWISE", "SHOWUSERWISE");
			}
			
			if (roleId.equals("2")) {
				sqlCondition += " and ad.distid='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
			}
			
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and ad.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			
				
			sql = "select distid,district_name,count(distinct ad.ack_no) as acks from ecourts_gpo_ack_dtls ad "
					+ " inner join district_mst dm on (ad.distid=dm.district_id) "
					+ " inner join ecourts_gpo_ack_depts d on (ad.ack_no=d.ack_no) "
					+ "inner join dept_new dmt on (d.dept_code=dmt.dept_code)" + " where ack_type='NEW' "
					+ sqlCondition + " group by distid,dm.district_name order by district_name";

			System.out.println("SQL:showDistWise" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("DISTWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

			if (roleId.equals("2"))
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+distCode+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2") || roleId.equals("14"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select sno,case_full_name from case_type_master order by sno",
					con));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward showDeptWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and ad.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and d.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and ad.inserted_time::date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and ad.inserted_time::date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2") || roleId.equals("14"))) {
					sqlCondition += " and (dm.dept_code='" + deptCode + "' or dm.reporting_dept_code='"+deptCode+"') ";
			}
			
			if (roleId.equals("2")) {
				sqlCondition += " and ad.distid='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
			}
			
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and ad.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			
			sql = "select d.dept_code,upper(description) as description,count(distinct ad.ack_no) as acks from ecourts_gpo_ack_dtls ad  inner join ecourts_gpo_ack_depts d on (ad.ack_no=d.ack_no) "
					+ "inner join dept_new dm on (d.dept_code=dm.dept_code)"
					+ " where ack_type='NEW' and respondent_slno=1 " + sqlCondition
					+ " group by d.dept_code,description " + " order by d.dept_code,description";

			System.out.println("SQL:showDeptWise" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("DEPTWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

			if (roleId.equals("2"))
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+distCode+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2") || roleId.equals("14"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select sno,case_full_name from case_type_master order by sno",
					con));
			
			if ((roleId.equals("1") || roleId.equals("7") || roleId.equals("14"))) {
				request.setAttribute("SHOWUSERWISE", "SHOWUSERWISE");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
	public ActionForward showUserWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and ad.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and d.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and ad.inserted_time::date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and ad.inserted_time::date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}
			
			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2")  || roleId.equals("14"))) {
					sqlCondition += " and (dm.dept_code='" + deptCode + "' or dm.reporting_dept_code='"+deptCode+"') ";
			}
			
			if (roleId.equals("2")) {
				sqlCondition += " and ad.distid='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
			}
			
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and ad.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
		
			sql = "select inserted_by,count(distinct ad.ack_no) as acks from ecourts_gpo_ack_dtls ad "
					+ " inner join district_mst dm on (ad.distid=dm.district_id) "
					+ " inner join ecourts_gpo_ack_depts d on (ad.ack_no=d.ack_no) "
					+ "inner join dept_new dmt on (d.dept_code=dmt.dept_code)" + " where ack_type='NEW' "
					+ sqlCondition + " group by inserted_by";
			

			System.out.println("SQL:showUserWise" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("USERWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

			if (roleId.equals("2"))
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+distCode+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2") || roleId.equals("14"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select sno,case_full_name from case_type_master order by sno",
					con));

			if ((roleId.equals("1") || roleId.equals("7") || roleId.equals("14"))) {
				request.setAttribute("SHOWUSERWISE", "SHOWUSERWISE");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward showCaseWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			String inserted_by = CommonModels.checkStringObject(cform.getDynaForm("inserted_by"));
			
			System.out.println("inserted_by--"+inserted_by);
			
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and ad.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("fromDate") != null && !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time::date >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null && !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time::date <= to_date('" + cform.getDynaForm("toDate")
						+ "','dd-mm-yyyy') ";
			}

			if (request.getParameter("districtId") != null
					&& !CommonModels.checkStringObject(request.getParameter("districtId")).contentEquals("")
					&& !CommonModels.checkStringObject(request.getParameter("districtId")).contentEquals("0")) {
				sqlCondition += " and a.distid='" + request.getParameter("districtId").toString().trim() + "' ";

				cform.setDynaForm("districtId", request.getParameter("districtId"));
			}
			if (request.getParameter("deptId") != null
					&& !CommonModels.checkStringObject(request.getParameter("deptId")).contentEquals("")
					&& !CommonModels.checkStringObject(request.getParameter("deptId")).contentEquals("0")) {
				sqlCondition += " and ad.dept_code='" + request.getParameter("deptId").toString().trim() + "' ";
				cform.setDynaForm("deptId", request.getParameter("deptId"));
			}
			if (request.getParameter("fromDate") != null
					&& !CommonModels.checkStringObject(request.getParameter("fromDate")).contentEquals("")) {
				sqlCondition += " and a.inserted_time::date >= to_date('" + request.getParameter("fromDate")
						+ "','dd-mm-yyyy') ";
				cform.setDynaForm("fromDate", request.getParameter("fromDate"));
			}
			if (request.getParameter("toDate") != null
					&& !CommonModels.checkStringObject(request.getParameter("toDate")).contentEquals("")) {
				sqlCondition += " and a.inserted_time::date <= to_date('" + request.getParameter("toDate")
						+ "','dd-mm-yyyy') ";
				cform.setDynaForm("toDate", request.getParameter("toDate"));
			}

			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2") || roleId.equals("14"))) {
					sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='"+deptCode+"') ";
			}
			
			if (roleId.equals("2")) {
				sqlCondition += " and a.distid='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
			}
			
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and a.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			if (cform.getDynaForm("inserted_by") != null && !cform.getDynaForm("inserted_by").toString().contentEquals("")
					&& !cform.getDynaForm("inserted_by").toString().contentEquals("0")) {
				sqlCondition += " and a.inserted_by='" + inserted_by+ "' ";
			}
			
			sql = "select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs "
					+ "from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no and respondent_slno=1) "
					+ "inner join district_mst dm on (a.distid=dm.district_id) "
					+ "inner join dept_new dmt on (ad.dept_code=dmt.dept_code)"
					+ "inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ "where a.delete_status is false and ack_type='NEW' " + sqlCondition
					+ "order by inserted_time desc";

			System.out.println("SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASEWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}
			
			
			if (roleId.equals("2"))
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+distCode+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2") || roleId.equals("14"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select sno,case_full_name from case_type_master order by sno",
					con));
			if ((roleId.equals("1") || roleId.equals("7") || roleId.equals("14"))) {
				request.setAttribute("SHOWUSERWISE", "SHOWUSERWISE");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));

			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
}