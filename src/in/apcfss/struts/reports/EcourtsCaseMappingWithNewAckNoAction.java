package in.apcfss.struts.reports;

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

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.Utilities.CommonModels;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class EcourtsCaseMappingWithNewAckNoAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			con = DatabasePlugin.connect();

			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst  order by district_name",
								con));
			else
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7") || roleId.equals("2"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else if(roleId.equals("4") || roleId.equals("3"))
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			else 
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));

			cform.setDynaForm("caseTypesList",
					DatabasePlugin.getSelectBox("select sno,case_full_name from case_type_master order by sno", con));
			
cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 2021; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		// return mapping.findForward("success");
		return showCaseWise(mapping, cform, request, response);
	}

	public ActionForward showCaseWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0",userid="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));

			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and ad.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
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
				sqlCondition += " and ad.dist_id='" + request.getParameter("districtId").toString().trim() + "' ";

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

			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2") || roleId.equals("3"))) {
				// sqlCondition += " and (dmt.dept_code='" + deptCode + "' or
				// dmt.reporting_dept_code='"+deptCode+"') ";
				sqlCondition += " and dmt.dept_code='" + deptCode + "' ";
			}

			if (roleId.equals("2")) {// District Collector
				sqlCondition += " and (case_status is null or case_status=7) and ad.dist_id='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
			}

			if (roleId.equals("3")) {// Secretariat Department
				sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='" + deptCode + "') ";
			}

			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and a.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			
			else if (roleId.equals("10")) { // District Nodal Officer
				sqlCondition += " and (case_status is null or case_status=8) and dist_id='" + distCode + "'";
			} else if (roleId.equals("5") || roleId.equals("9")) { // NO & HOD
				sqlCondition += " and (case_status is null or case_status in (3,4))";
			} else if (roleId.equals("3") || roleId.equals("4")) {// MLO & Sect. Dept.
				sqlCondition += " and (case_status is null or case_status in (1, 2))";
			}

			if (cform.getDynaForm("advcteName") != null && !cform.getDynaForm("advcteName").toString().contentEquals("")) {
				sqlCondition += " and replace(replace(advocatename,' ',''),'.','') ilike  '%"+cform.getDynaForm("advcteName")+"%'";
			}
			
			sql = "select slno , a.ack_no,respondent_slno , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ " upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ " to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no::text) as dept_descs, coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ " from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
					+ " left join district_mst dm on (ad.dist_id=dm.district_id) "
					+ " left join dept_new dmt on (ad.dept_code=dmt.dept_code)"
					+ " inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ " where a.delete_status is false and coalesce(assigned,'f')='f' and ack_type='NEW'   " //and maincaseno='' and reg_year='2022' and respondent_slno=1   and reg_year='2022'
					+ sqlCondition + " order by inserted_time desc";

			System.out.println("CASES SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASEWISEACKS", data);
			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst order by district_name",
								con));
			else
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst order by district_name", con));

			if (roleId.equals("1") || roleId.equals("7") )
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
			else if(roleId.equals("2") || roleId.equals("10"))
				
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and substr(dept_code::text,4,5)!='01'  order by dept_code",
						con));
			else
				
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true and reporting_dept_code='"
								+ deptCode + "' or dept_code='" + deptCode + "' order by dept_code",
						con));
			
			cform.setDynaForm("caseTypesList",
					DatabasePlugin.getSelectBox("select sno,case_full_name from case_type_master order by sno", con));
			
cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 2021; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);
			
			if (roleId.equals("2") || roleId.equals("10") ) {
			request.setAttribute("login_type_dc", "login_type_dc");
			}else {
				request.setAttribute("login_type", "login_type");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
			cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
			cform.setDynaForm("caseTypeId",
					cform.getDynaForm("caseTypeId") != null ? cform.getDynaForm("caseTypeId").toString() : "0");
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward submitDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		int distId = 0;
		String deptId="";
		String hcAckNo=null;
		String ackNo = null;//generateNewAckNo();
		int respondentIds=0;
		String 	deptdist=null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}

			if (isTokenValid(request)) {

				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				/*
				 * String ackNoo= cform.getDynaForm("ackNo").toString();
				 * System.out.println("ackNoo--"+ackNoo);
				 */
				 ackNo=CommonModels.checkStringObject(cform.getDynaForm("ackNo"));
				 
				String caseType=(String) cform.getDynaForm("caseType1_"+ackNo.toString());
				String regyear=(String) cform.getDynaForm("regYear1_"+ackNo.toString());
				String mainCase=(String) cform.getDynaForm("mainCaseNo_"+ackNo.toString());
				
				
				String mainCaseNo=caseType+"/"+mainCase+"/"+regyear;
				
				if (ackNo != null && !ackNo.contentEquals("") ) {

					sql = "update ecourts_gpo_ack_dtls set maincaseno_updated='"+mainCaseNo+"',maincaseno='"+mainCaseNo+"',ackno_updated=true where ack_no='"+ackNo+"' ";
					System.out.println("sql--"+sql);
					ps = con.prepareStatement(sql);
					int a = ps.executeUpdate();
					
					/*
					 * sql =
					 * "update ecourts_case_data  set new_ack_updated=true  where type_name_reg||'/'||reg_no||'/'||reg_year='"
					 * +mainCaseNo+"' "; System.out.println("sql--"+sql); ps =
					 * con.prepareStatement(sql); int b = ps.executeUpdate();
					 */
					
					if(a > 0) {// && b>0
						request.setAttribute("successMsg",
								"Acknowledgement details updated successfully with Ack No.:" + ackNo);
						
						con.commit();
						}
						else {
							request.setAttribute("errorMsg", "Failed to save Data. Kindly try again.");
						}
					
				} else {
					request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Error: Submitting duplicate Acknowledgement details. Kindly try again with valid data.");
			}
			
			if(CommonModels.checkStringObject(cform.getDynaForm("ackType")).equals("OLD")) {
				return mapping.findForward("showOldAckList");
			}
			
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg",
					"Exception Occurred while saving Acknowledgement details. Kindly try again with valid data.");
			request.removeAttribute("successMsg");
			e.printStackTrace();
		} finally {
			 cform.setDynaForm("caseType1_"+ackNo,"");
			//cform.setDynaForm("caseType1","");
			cform.setDynaForm("regYear1_"+ackNo,"");
			cform.setDynaForm("mainCaseNo_"+ackNo,"");
			DatabasePlugin.close(con, ps, null);
		}
		return unspecified(mapping, cform, request, response);
	}

}