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
import org.apache.struts.upload.FormFile;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import plugins.DatabasePlugin;

public class GPReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		
		return mapping.findForward("success");
	}
	
	public ActionForward yearWiseCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {
				con = DatabasePlugin.connect();
				
				int yearId = CommonModels.checkIntObject(request.getParameter("yearId"));
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis, cino from ecourts_case_data a "
						+ " inner join dept_new d on (a.dept_code=d.dept_code)   inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
						+ " where reg_year > 0 and d.display = true  and e.gp_id='"+userId+"' ";
				if(yearId > 0)
					sql+="and reg_year='"+yearId+"' ";
				
				sql	+= "order by reg_year,type_name_reg,reg_no";
				request.setAttribute("HEADING", "Cases Registered in the Year:"+yearId);

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("CASEWISEDATA", data);
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
	
	public ActionForward viewInstructionsCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", heading=null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if(roleId!=null && roleId.equals("6")) { // GPO
				
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				condition=" and a.case_status=6 and e.gp_id='"+userId+"' ";
				
				if(counter_pw_flag.equals("PR")) {
					heading = "Parawise Remarks submitted Cases List";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6'
					condition+=" and pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					heading = "Counters filed Cases List";
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6'
					condition+=" and pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			heading = "Instruction Cases List";
			/*
			 * sql =
			 * "select type_name_reg, reg_no, reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis, a.cino, case when length(scanned_document_path) > 10 then scanned_document_path else '-' end as scanned_document_path "
			 * + " from ecourts_case_data a " +
			 * " left join ecourts_olcms_case_details od on (a.cino=od.cino)" +
			 * " left join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) " +
			 * " inner join dept_new d on (a.dept_code=d.dept_code) " +
			 * " where assigned=true "+condition +
			 * " and coalesce(a.ecourts_case_status,'')!='Closed' "+condition;
			 * 
			 * 
			 * sql += "order by reg_year,type_name_reg,reg_no";
			 * request.setAttribute("HEADING", heading);
			 */
			
			 sql="select type_name_reg, reg_no, reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis, a.cino, "
			 		+ " case when length(scanned_document_path) > 10 then scanned_document_path else '-' end as scanned_document_path,legacy_ack_flag "
			 		+ " from (select distinct cino,legacy_ack_flag from ecourts_dept_instructions where legacy_ack_flag='Legacy') a inner join ecourts_case_data d on (a.cino=d.cino)"
			 		+ " where d.dept_code in (select dept_code from ecourts_mst_gp_dept_map where gp_id='"+userId+"')";
			 
			 
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASEWISEDATA", data);
			request.setAttribute("HEADING", heading);
			
			}else {
				request.setAttribute("errorMsg", "No Records found to display");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	public ActionForward viewInstructionsCasesNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", heading=null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if(roleId!=null && roleId.equals("6")) { // GPO
				heading = "Instruction Cases List";
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				condition=" and a.case_status=6 and e.gp_id='"+userId+"' ";
				
				if(counter_pw_flag.equals("PR")) {
					heading = "Parawise Remarks submitted Cases List";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6'
					condition+=" and pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					heading = "Counters filed Cases List";
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6'
					condition+=" and pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			request.setAttribute("HEADING", heading);
			//heading = "Instruction Cases List";
			 sql="select (select case_full_name from case_type_master ctm where ctm.sno::text=e.casetype::text) as type_name_reg, "
			 		+ " e.reg_no, e.reg_year, to_char(e.inserted_time,'dd-mm-yyyy') as dt_regis, a.cino, "
			 		+ "  case when length(ack_file_path) > 10 then ack_file_path else '-' end as scanned_document_path,legacy_ack_flag  "
			 		+ " from (select distinct cino,dept_code,legacy_ack_flag from ecourts_dept_instructions where legacy_ack_flag='New') a "
			 		+ " inner join ecourts_gpo_ack_depts d on (d.ack_no=a.cino) and (d.dept_code=a.dept_code)   inner join ecourts_gpo_ack_dtls e on (d.ack_no=e.ack_no)  "
			 		+ " where d.dept_code in (select dept_code from ecourts_mst_gp_dept_map where gp_id='"+userId+"')";
			 
			 
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CASEWISEDATA", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	public ActionForward viewGPCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", heading=null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if(roleId!=null && roleId.equals("6")) { // GPO
				
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				condition=" and a.case_status=6 and e.gp_id='"+userId+"' ";
				
				if(counter_pw_flag.equals("PR")) {
					heading = "Parawise Remarks submitted Cases List";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
					condition+=" and (pwr_uploaded='No' or pwr_uploaded='Yes') and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					heading = "Counters filed Cases List";
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6' //and action_to_perfom='Counter Affidavit'
					condition+=" and pwr_uploaded='Yes' and coalesce(pwr_approved_gp,'No')='Yes' and (counter_filed='No' or counter_filed='Yes') and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			request.setAttribute("HEADING", heading);
			
			sql = "select type_name_reg,'Legacy' as legacy_ack_flag, reg_no, reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis, a.cino, "
					+ "case when length(scanned_document_path) > 10 then scanned_document_path else '-' end as scanned_document_path from ecourts_case_data a "
					+ " left join ecourts_olcms_case_details od on (a.cino=od.cino)"
					+ " left join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code and a.assigned_to=e.gp_id) "
					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " where assigned=true "+condition
					+ " and coalesce(a.ecourts_case_status,'')!='Closed' ";
			
			sql	+= "order by reg_year,type_name_reg,reg_no";
			request.setAttribute("HEADING", heading);

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CASEWISEDATA", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward viewGPCasesNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", heading=null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if(roleId!=null && roleId.equals("6")) { // GPO
				
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				condition=" and a.case_status=6 and e.gp_id='"+userId+"' ";
				
				if(counter_pw_flag.equals("PR")) {
					heading = "Parawise Remarks submitted Cases List";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
					condition+=" and (pwr_uploaded='No' or pwr_uploaded='Yes') and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					heading = "Counters filed Cases List";
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6' //and action_to_perfom='Counter Affidavit'
					condition+=" and pwr_uploaded='Yes' and coalesce(pwr_approved_gp,'No')='Yes' and (counter_filed='No' or counter_filed='Yes') and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			request.setAttribute("HEADING", heading);
			
			sql = "select (select case_full_name from case_type_master ctm where ctm.sno::text=b.casetype::text) as type_name_reg,'New' as legacy_ack_flag, reg_no, reg_year, inserted_time::date as dt_regis, a.ack_no as cino, "
					+ "case when length(ack_file_path) > 10 then ack_file_path else '-' end as scanned_document_path "
					+ " from ecourts_gpo_ack_depts a inner join ecourts_gpo_ack_dtls b on (a.ack_no=b.ack_no)"
					+ " left join ecourts_olcms_case_details od on (a.ack_no=od.cino)"
					//+" inner join  ecourts_gpo_ack_dtls ad on (a.cino=ad.ack_no) inner join ecourts_gpo_ack_depts d on (ad.ack_no=d.ack_no)"
					+ " left join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code and a.assigned_to=e.gp_id) "
					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " where assigned=true "+condition
					+ " and coalesce(a.ecourts_case_status,'')!='Closed' ";
			
			sql	+= "order by reg_year,type_name_reg,reg_no";
			request.setAttribute("HEADING", heading);

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CASEWISEDATA", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward viewCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {
				con = DatabasePlugin.connect();
				
				String caseType = CommonModels.checkStringObject(request.getParameter("caseType"));
				String caseNo = CommonModels.checkStringObject(request.getParameter("caseNo"));
				int caseYear = CommonModels.checkIntObject(request.getParameter("caseYear"));
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis,cino , case when length(scanned_document_path) > 10 then scanned_document_path else '-' end as scanned_document_path  from ecourts_case_data a "
						+ " inner join dept_new d on (a.dept_code=d.dept_code)   inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
						+ " where reg_year > 0 and d.display = true  and e.gp_id='"+userId+"' ";
				if(caseYear > 0)
					sql += "and reg_year='" + caseYear + "' ";
				
				sql	+= "order by reg_year,type_name_reg,reg_no";
				request.setAttribute("HEADING", "Case Details for Case No.:"+caseType+" "+caseNo+"/"+caseYear);

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("CASEWISEDATA", data);
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
	
	public ActionForward caseStatusUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null,caseType=null;
		try {
			System.out.println("caseStatusUpdate");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			// cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			cIno = CommonModels.checkStringObject(request.getParameter("caseCiNo"));
			caseType = CommonModels.checkStringObject(request.getParameter("caseType"));
			
			if (cIno != null && !cIno.equals("") && caseType.equals("Legacy")) {
				
				//int value=cIno.length();
				
				
				System.out.println("IN CASE STATUS UPDATE METHOD :"+cIno);
				
				con = DatabasePlugin.connect();
				
				//sql = "select * from ecourts_case_data where cino='" + cIno + "'";
				sql = "select a.*, prayer from ecourts_case_data a left join nic_prayer_data np on (a.cino=np.cino) where a.cino='" + cIno + "'";
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map caseData1 = (Map)data.get(0);
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {
						request.setAttribute("SHOWBACKBTN", "SHOWBACKBTN");
					}
					
					if(roleId!=null && roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
						if(CommonModels.checkStringObject(caseData1.get("section_officer_updated")).equals("T")) {
							System.out.println("dept code-3,5:"+deptCode.substring(3, 5));
							
							if(deptCode.substring(3, 5)=="01" || deptCode.substring(3, 5).equals("01")) {
								
								request.setAttribute("SHOWMLOBTN", "SHOWMLOBTN");
							}
							else {
								request.setAttribute("SHOWNOBTN", "SHOWNOBTN");
							}
						}
					}
					else if(roleId!=null && roleId.equals("4") && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) { 
						// MLO TO SECT DEPT
						request.setAttribute("SHOWSECDEPTBTN", "SHOWSECDEPTBTN");
					}
					else if(roleId!=null && (roleId.equals("5") || roleId.equals("10")) && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) { 
						// NO TO HOD/DEPT
						request.setAttribute("SHOWHODDEPTBTN", "SHOWHODDEPTBTN");
					}
					else if((roleId.equals("3") || roleId.equals("9")) && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) {
	
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b using (gp_id) where b.dept_code='"+deptCode+"' order by emailid";
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b on (a.emailid=b.gp_id) where b.dept_code='"+deptCode+"' order by emailid";
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps order by 1";
						cform.setDynaForm("GPSLIST", DatabasePlugin.getSelectBox(sql, con));
						request.setAttribute("SHOWGPBTN", "SHOWGPBTN");
					}
					else if(roleId.equals("6") ) { // GP LOGIN
						request.setAttribute("SHOWGPAPPROVEBTN", "SHOWGPAPPROVEBTN");
					}
					request.setAttribute("USERSLIST", data);
				}
				
				sql = "select * from ecourts_case_acts where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("actlist", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_finalorder where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("orderlist", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_iafiling where cino='" + cIno + "'";
				// sql="select *, replace(ia_number,'</br>','') as ia_no from
				// apolcms.ecourts_case_iafiling where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("IAFILINGLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_interimorder where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("INTERIMORDERSLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_link_cases where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("LINKCASESLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_objections where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("OBJECTIONSLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_historyofcasehearing where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASEHISTORYLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_pet_extra_party where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("PETEXTRAPARTYLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_res_extra_party where cino='" + cIno + "'";
				sql = "select b.party_no,b.res_name as party_name, b.address from nic_resp_addr_data b left join ecourts_res_extra_party a on (b.cino=a.cino and b.party_no-1=coalesce(trim(a.party_no),'0')::int4) where b.cino='" + cIno + "' order by b.party_no";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("RESEXTRAPARTYLIST", data);
				}

				System.out.println("ecourts SQL:" + sql);
				
				
				sql="select cino,action_type,inserted_by,to_char(inserted_on,'dd-Mon-yyyy hh24:mi:ss PM') as inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on desc";
				System.out.println("ecourts activities SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("ACTIVITIESDATA", data);
				
				/*
					sql=" select * from ecourts_olcms_case_details_log where cino='"+cIno+"'";
					data = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("OLCMSCASEDATALOG", data);
				*/
				
				sql = "SELECT cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom  "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				
				sql = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document, "
						+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
						+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
						+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
						+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, "
						+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom, counter_approved_gp "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				System.out.println("SQL:"+sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("OLCMSCASEDATA", data);

				if (data != null) {
					
					Map caseData1 = (Map)data.get(0);
					
					cform.setDynaForm("petitionDocumentOld" , CommonModels.checkStringObject(caseData1.get("petition_document")));
					cform.setDynaForm("counterFileCopyOld" , CommonModels.checkStringObject(caseData1.get("counter_filed_document")));
					cform.setDynaForm("judgementOrderOld" , CommonModels.checkStringObject(caseData1.get("judgement_order")));
					cform.setDynaForm("actionTakenOrderOld" , CommonModels.checkStringObject(caseData1.get("action_taken_order")));
					//cform.setDynaForm("" , caseData1.get("last_updated_by"));
					//cform.setDynaForm("" , caseData1.get("last_updated_on"));
					cform.setDynaForm("counterFiled" , caseData1.get("counter_filed"));
					System.out.println("action_to_perfom:"+caseData1.get("action_to_perfom"));
					System.out.println("remarks::"+caseData1.get("remarks"));
					if(CommonModels.checkStringObject(caseData1.get("action_to_perfom")).equals("Counter Affidavit"))
						cform.setDynaForm("remarks2" , caseData1.get("remarks"));
					
					if(CommonModels.checkStringObject(caseData1.get("action_to_perfom")).equals("Parawise Remarks"))
						cform.setDynaForm("remarks" , caseData1.get("remarks"));
					
					cform.setDynaForm("ecourtsCaseStatus" , caseData1.get("ecourts_case_status"));
					// cform.setDynaForm("relatedGp" , caseData1.get("corresponding_gp"));
					cform.setDynaForm("parawiseRemarksSubmitted" , caseData1.get("pwr_uploaded"));
					cform.setDynaForm("parawiseRemarksCopyOld" , CommonModels.checkStringObject(caseData1.get("pwr_uploaded_copy")));
					cform.setDynaForm("parawiseRemarksDt" , caseData1.get("pwr_submitted_date"));
					cform.setDynaForm("dtPRReceiptToGP" , caseData1.get("pwr_received_date"));
					cform.setDynaForm("pwr_gp_approved" , caseData1.get("pwr_approved_gp"));
					cform.setDynaForm("dtPRApprovedToGP" , caseData1.get("pwr_gp_approved_date"));
					cform.setDynaForm("appealFiled" , caseData1.get("appeal_filed"));
					cform.setDynaForm("appealFileCopyOld" , CommonModels.checkStringObject(caseData1.get("appeal_filed_copy")));
					cform.setDynaForm("appealFiledDt" , caseData1.get("appeal_filed_date"));
					cform.setDynaForm("actionToPerform" , caseData1.get("action_to_perfom"));
					cform.setDynaForm("counter_approved_gp" , caseData1.get("counter_approved_gp"));
					
					System.out.println("actionToPerform:"+cform.getDynaForm("actionToPerform"));
					System.out.println("counter_approved_gp:"+cform.getDynaForm("counter_approved_gp"));
					// 1. View old Parawise Remarks & Enable to Upload Parawise Remarks and send to Department and disable Counter Update.
					if(CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("Parawise Remarks")) {
						//a. APPROVED
						if(cform.getDynaForm("pwr_gp_approved").equals("Yes")) {
							// disable Submission
							request.setAttribute("pwrsuccessMsg", "Parawise Remarks was submitted and approved.");
							request.setAttribute("PWRSUBMITION", "DISABLE");
						}
						//b. NOT APPROVED
						else if(cform.getDynaForm("pwr_gp_approved").equals("No")) {
							// enable upload & entry.
							request.setAttribute("PWRSUBMITION", "ENABLE");
						}
					}
					// 2. View Counter uploaded by Dept. and Disable Parawise Remarks Updation and enable Counter Upload by GP.
					else if(CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("Counter Affidavit")) {
						//a. PWR NOT APPROVED
						if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("No")) {
							request.setAttribute("countererrorMsg", "Parawise Remarks was not submitted/approved.");
							request.setAttribute("COUNTERSUBMITION", "DISABLE");
						}
						//b. PWR APPROVED COUNTER NOT APPROVED
						else if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("Yes") && !CommonModels.checkStringObject(cform.getDynaForm("counter_approved_gp")).equals("T")) {
							request.setAttribute("COUNTERSUBMITION", "ENABLE");
						}
						//c. COUNTER APPROVED
						if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("Yes") && CommonModels.checkStringObject(cform.getDynaForm("counter_approved_gp")).equals("T")) {
							request.setAttribute("countersuccessMsg", "Counter submitted and finalized by GP.");
							request.setAttribute("COUNTERSUBMITION", "DISABLE");
						}
					}
					/*else {
						request.setAttribute("PWRSUBMITION", "ENABLE");
					}*/
					request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
				}
				
				// Dept. Instructions
				sql = "select instructions, to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(insert_by,'0') as insert_by,coalesce(upload_fileno,'-') as upload_fileno from ecourts_dept_instructions where cino='" + cIno + "'  order by 1 ";
				System.out.println("Dept INstructions sql--" + sql);
				List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("DEPTNSTRUCTIONS", existData);
				
				// Daily Case Status Updates by GP
				sql = "select status_remarks, to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(insert_by,'0') as insert_by,coalesce(upload_fileno,'-') as upload_fileno from ecourts_gpo_daily_status where cino='" + cIno + "'  order by 1 ";
				System.out.println("sql--" + sql);
				existData = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("GPDAILYSTATUS", existData);
				
				request.setAttribute("HEADING", " Details for the Case No. :" + cIno);
			
				
			} else if(caseType.equals("New")) {
				
				System.out.println("IN CASE STATUS UPDATE METHOD :"+cIno);
				
				con = DatabasePlugin.connect();
				
				//sql = "select * from ecourts_case_data where cino='" + cIno + "'";
				sql = " select  a.ack_no ,  dept_code,respondent_slno  , (select district_name from district_mst dm where dm.district_id::text=b.distid::text) as district_name,"
						+ " servicetpye  ,    advocatename , advocateccno,   (select case_full_name from case_type_master ctm where ctm.sno::text=b.casetype::text) as casetype,maincaseno , "
						+ "remarks , inserted_time::date ,  inserted_by ,  ack_file_path   ,  "
						+ "petitioner_name    ,  reg_year  , reg_no , mode_filing  , case_category  ,  hc_ack_no  "
						+ " from ecourts_gpo_ack_depts a inner join ecourts_gpo_ack_dtls b on (a.ack_no=b.ack_no) where b.ack_no='" + cIno + "'  and respondent_slno='1'";
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map caseData1 = (Map)data.get(0);
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {
						request.setAttribute("SHOWBACKBTN", "SHOWBACKBTN");
					}
					
					if(roleId!=null && roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
						if(CommonModels.checkStringObject(caseData1.get("section_officer_updated")).equals("T")) {
							System.out.println("dept code-3,5:"+deptCode.substring(3, 5));
							
							if(deptCode.substring(3, 5)=="01" || deptCode.substring(3, 5).equals("01")) {
								
								request.setAttribute("SHOWMLOBTN", "SHOWMLOBTN");
							}
							else {
								request.setAttribute("SHOWNOBTN", "SHOWNOBTN");
							}
						}
					}
					else if(roleId!=null && roleId.equals("4") && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) { 
						// MLO TO SECT DEPT
						request.setAttribute("SHOWSECDEPTBTN", "SHOWSECDEPTBTN");
					}
					else if(roleId!=null && (roleId.equals("5") || roleId.equals("10")) && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) { 
						// NO TO HOD/DEPT
						request.setAttribute("SHOWHODDEPTBTN", "SHOWHODDEPTBTN");
					}
					else if((roleId.equals("3") || roleId.equals("9")) && CommonModels.checkStringObject(caseData1.get("mlo_no_updated")).equals("T")) {
	
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b using (gp_id) where b.dept_code='"+deptCode+"' order by emailid";
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b on (a.emailid=b.gp_id) where b.dept_code='"+deptCode+"' order by emailid";
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps order by 1";
						cform.setDynaForm("GPSLIST", DatabasePlugin.getSelectBox(sql, con));
						request.setAttribute("SHOWGPBTN", "SHOWGPBTN");
					}
					else if(roleId.equals("6") ) { // GP LOGIN
						request.setAttribute("SHOWGPAPPROVEBTN", "SHOWGPAPPROVEBTN");
					}
					request.setAttribute("USERSLIST", data);
				}
				
				sql = "select * from ecourts_case_acts where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("actlist", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_finalorder where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("orderlist", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_iafiling where cino='" + cIno + "'";
				// sql="select *, replace(ia_number,'</br>','') as ia_no from
				// apolcms.ecourts_case_iafiling where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("IAFILINGLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_interimorder where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("INTERIMORDERSLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_link_cases where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("LINKCASESLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_case_objections where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("OBJECTIONSLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_historyofcasehearing where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASEHISTORYLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_pet_extra_party where cino='" + cIno + "'";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("PETEXTRAPARTYLIST", data);
				}
	
				sql = "select  * from apolcms.ecourts_res_extra_party where cino='" + cIno + "'";
				sql = "select b.party_no,b.res_name as party_name, b.address from nic_resp_addr_data b left join ecourts_res_extra_party a on (b.cino=a.cino and b.party_no-1=coalesce(trim(a.party_no),'0')::int4) where b.cino='" + cIno + "' order by b.party_no";
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("RESEXTRAPARTYLIST", data);
				}

				System.out.println("ecourts SQL:" + sql);
				
				
				sql="select cino,action_type,inserted_by,to_char(inserted_on,'dd-Mon-yyyy hh24:mi:ss PM') as inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on desc";
				System.out.println("ecourts activities SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("ACTIVITIESDATA", data);
				
				/*
					sql=" select * from ecourts_olcms_case_details_log where cino='"+cIno+"'";
					data = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("OLCMSCASEDATALOG", data);
				*/
				
				sql = "SELECT cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom  "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				
				sql = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document, "
						+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
						+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
						+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
						+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, "
						+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom, counter_approved_gp "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				System.out.println("SQL:"+sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("OLCMSCASEDATA", data);

				if (data != null) {
					
					Map caseData1 = (Map)data.get(0);
					
					cform.setDynaForm("petitionDocumentOld" , CommonModels.checkStringObject(caseData1.get("petition_document")));
					cform.setDynaForm("counterFileCopyOld" , CommonModels.checkStringObject(caseData1.get("counter_filed_document")));
					cform.setDynaForm("judgementOrderOld" , CommonModels.checkStringObject(caseData1.get("judgement_order")));
					cform.setDynaForm("actionTakenOrderOld" , CommonModels.checkStringObject(caseData1.get("action_taken_order")));
					//cform.setDynaForm("" , caseData1.get("last_updated_by"));
					//cform.setDynaForm("" , caseData1.get("last_updated_on"));
					cform.setDynaForm("counterFiled" , caseData1.get("counter_filed"));
					System.out.println("action_to_perfom:"+caseData1.get("action_to_perfom"));
					System.out.println("remarks::"+caseData1.get("remarks"));
					if(CommonModels.checkStringObject(caseData1.get("action_to_perfom")).equals("Counter Affidavit"))
						cform.setDynaForm("remarks2" , caseData1.get("remarks"));
					
					if(CommonModels.checkStringObject(caseData1.get("action_to_perfom")).equals("Parawise Remarks"))
						cform.setDynaForm("remarks" , caseData1.get("remarks"));
					
					cform.setDynaForm("ecourtsCaseStatus" , caseData1.get("ecourts_case_status"));
					// cform.setDynaForm("relatedGp" , caseData1.get("corresponding_gp"));
					cform.setDynaForm("parawiseRemarksSubmitted" , caseData1.get("pwr_uploaded"));
					cform.setDynaForm("parawiseRemarksCopyOld" , CommonModels.checkStringObject(caseData1.get("pwr_uploaded_copy")));
					cform.setDynaForm("parawiseRemarksDt" , caseData1.get("pwr_submitted_date"));
					cform.setDynaForm("dtPRReceiptToGP" , caseData1.get("pwr_received_date"));
					cform.setDynaForm("pwr_gp_approved" , caseData1.get("pwr_approved_gp"));
					cform.setDynaForm("dtPRApprovedToGP" , caseData1.get("pwr_gp_approved_date"));
					cform.setDynaForm("appealFiled" , caseData1.get("appeal_filed"));
					cform.setDynaForm("appealFileCopyOld" , CommonModels.checkStringObject(caseData1.get("appeal_filed_copy")));
					cform.setDynaForm("appealFiledDt" , caseData1.get("appeal_filed_date"));
					cform.setDynaForm("actionToPerform" , caseData1.get("action_to_perfom"));
					cform.setDynaForm("counter_approved_gp" , caseData1.get("counter_approved_gp"));
					
					System.out.println("actionToPerform:"+cform.getDynaForm("actionToPerform"));
					System.out.println("counter_approved_gp:"+cform.getDynaForm("counter_approved_gp"));
					// 1. View old Parawise Remarks & Enable to Upload Parawise Remarks and send to Department and disable Counter Update.
					if(CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("Parawise Remarks")) {
						//a. APPROVED
						if(cform.getDynaForm("pwr_gp_approved").equals("Yes")) {
							// disable Submission
							request.setAttribute("pwrsuccessMsg", "Parawise Remarks was submitted and approved.");
							request.setAttribute("PWRSUBMITION", "DISABLE");
						}
						//b. NOT APPROVED
						else if(cform.getDynaForm("pwr_gp_approved").equals("No")) {
							// enable upload & entry.
							request.setAttribute("PWRSUBMITION", "ENABLE");
						}
					}
					// 2. View Counter uploaded by Dept. and Disable Parawise Remarks Updation and enable Counter Upload by GP.
					else if(CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("Counter Affidavit")) {
						//a. PWR NOT APPROVED
						if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("No")) {
							request.setAttribute("countererrorMsg", "Parawise Remarks was not submitted/approved.");
							request.setAttribute("COUNTERSUBMITION", "DISABLE");
						}
						//b. PWR APPROVED COUNTER NOT APPROVED
						else if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("Yes") && !CommonModels.checkStringObject(cform.getDynaForm("counter_approved_gp")).equals("T")) {
							request.setAttribute("COUNTERSUBMITION", "ENABLE");
						}
						//c. COUNTER APPROVED
						if(CommonModels.checkStringObject(cform.getDynaForm("pwr_gp_approved")).equals("Yes") && CommonModels.checkStringObject(cform.getDynaForm("counter_approved_gp")).equals("T")) {
							request.setAttribute("countersuccessMsg", "Counter submitted and finalized by GP.");
							request.setAttribute("COUNTERSUBMITION", "DISABLE");
						}
					}
					/*else {
						request.setAttribute("PWRSUBMITION", "ENABLE");
					}*/
					request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
				}
				
				// Dept. Instructions
				sql = "select instructions, to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(insert_by,'0') as insert_by,coalesce(upload_fileno,'-') as upload_fileno from ecourts_dept_instructions where cino='" + cIno + "'  order by 1 ";
				System.out.println("Dept INstructions sql--" + sql);
				List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("DEPTNSTRUCTIONS", existData);
				
				// Daily Case Status Updates by GP
				sql = "select status_remarks, to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(insert_by,'0') as insert_by,coalesce(upload_fileno,'-') as upload_fileno from ecourts_gpo_daily_status where cino='" + cIno + "'  order by 1 ";
				System.out.println("sql--" + sql);
				existData = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("GPDAILYSTATUS", existData);
				
				request.setAttribute("HEADING", " Details for the Case No. :" + cIno);
			}

		else {
				request.setAttribute("errorMsg", "Invalid Cino. / No Records Found to display.");
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.closeConnection(con);
		}
		// return mapping.findForward("casestatusupdate");
		return mapping.findForward("viewCaseData");
	}
	
	public ActionForward gpApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
		int a=0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				sql="select dept_code,dist_id from ecourts_case_data where cino='"+cIno+"'";
				String deptCodeC="", distCodeC="", newStatus="", assigned2Emp="";
				
				List<Map> caseData = DatabasePlugin.executeQuery(con, sql);
				
				if(caseData!=null) {
					Map datainner = (Map)caseData.get(0);
					deptCodeC = CommonModels.checkStringObject(datainner.get("dept_code"));
					distCodeC = CommonModels.checkStringObject(datainner.get("dist_id"));
					System.out.println("deptCodeC::"+deptCodeC);
					System.out.println("distCodeC::"+distCodeC);
					if(deptCodeC.contains("01") && (distCodeC.equals("") || distCodeC.equals("0"))) {//SECTION SECT DEPT
						newStatus="5";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' and assigned_to in (select emailid from mlo_details where user_id='"+deptCodeC+"') order by inserted_on desc limit 1";
						msg = "Returned Case to Section Officer (Sect. Dept.)";
					}
					else if(!deptCodeC.contains("01") && (distCodeC.equals("") || distCodeC.equals("0"))) {//SECTION HOD
						newStatus="9";
						msg = "Returned Case to Section Officer (HOD)";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' "
								+ "and assigned_to in (select emailid from nodal_officer_details where dept_id='"+deptCodeC+"'  and coalesce(dist_id,0) = 0) order by inserted_on desc limit 1";
						
					}
					else if(!distCodeC.equals("") && !distCodeC.equals("0")) {//SECTION DIST
						newStatus="10";
						msg = "Returned Case to Section Officer (District)";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' "
								+ "and assigned_to in (select emailid from nodal_officer_details where dept_id='"+deptCodeC+"' and coalesce(dist_id,0) > 0) order by inserted_on desc limit 1";
						
					}
					
					System.out.println("assigned2Emp::"+sql);
					assigned2Emp = DatabasePlugin.getSingleValue(con, sql);
				}
				
				
				FileUploadUtilities fuu = new FileUploadUtilities();
				String petition_document = "",filePath="", newFileName="", counter_filed_document="", action_taken_order="", judgement_order="", appeal_filed_copy="", pwr_uploaded_copy="";
				
				FormFile myDoc;
				String updateSql="";
				String remarks = "";
				String actionPerformed="";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("0") ?  cform.getDynaForm("actionToPerform").toString()+" Approved"  : "CASE DETAILS UPDATED";
				
				msg = "Case details ("+cIno+") updated successfully.";
				
				sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
				a += DatabasePlugin.executeUpdate(sql, con);
				String sqlCondition2="";
				
				if(cform.getDynaForm("actionToPerform").toString().equals("Parawise Remarks")) {
					
					if(cform.getDynaForm("parawiseRemarksCopy")!=null  && !cform.getDynaForm("parawiseRemarksCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("parawiseRemarksCopy");
						filePath = "uploads/parawiseremarks/";
						newFileName = "parawiseremarks_"+CommonModels.randomTransactionNo();
						pwr_uploaded_copy = fuu.saveFile(myDoc, filePath, newFileName);
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','GP Approved Parawise Remarks','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+pwr_uploaded_copy+"')";
						DatabasePlugin.executeUpdate(sql, con);
						
						sqlCondition2 = ", pwr_uploaded_copy='"+pwr_uploaded_copy+"'";
					}
					
					sql="update ecourts_olcms_case_details set pwr_approved_gp='Yes',pwr_gp_approved_date=current_date"
							+", remarks='" + remarks
							+ "', last_updated_by='" + userId + "', last_updated_on=now() " + sqlCondition2
							+ "  where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					
					sql="update ecourts_case_data set case_status='"+newStatus+"' where cino='"+cIno+"'";
					sql="update ecourts_case_data set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' where cino='"+cIno+"' ";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					
					msg = "Parawise Remarks Approved successfully for Case ("+cIno+").";
				}
				else if(cform.getDynaForm("actionToPerform").toString().equals("Counter Affidavit")) {
					
					if(cform.getDynaForm("counterFileCopy")!=null  && !cform.getDynaForm("counterFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("counterFileCopy");
						filePath="uploads/counters/";
						newFileName="counter_"+CommonModels.randomTransactionNo();
						counter_filed_document = fuu.saveFile(myDoc, filePath, newFileName);
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Counter finalized by GP','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+counter_filed_document+"')";
						DatabasePlugin.executeUpdate(sql, con);
						
						sqlCondition2=", counter_filed_document='" + counter_filed_document + "'";
						
					}
					
					msg = "Counter Affidavit finalized successfully for Case ("+cIno+").";
					
					sql = "update ecourts_olcms_case_details set counter_approved_gp='T',counter_approved_date=current_date, counter_approved_by='"
							+ userId + "', remarks='" + remarks + "', last_updated_by='" + userId
							+ "', last_updated_on=now()" + "" + sqlCondition2
							+ " where cino='" + cIno + "'";
					System.out.println("COUNTER SQL:"+sql);
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				
				if (a > 0) {
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks) "
							+ " values ('" + cIno + "','"+actionPerformed+"', '"+userId+"', '"+request.getRemoteAddr()+"', '"+cform.getDynaForm("remarks")+"')";
					DatabasePlugin.executeUpdate(sql, con);
					
					request.setAttribute("successMsg", msg);
					con.commit();
				} else {
					con.rollback();
					request.setAttribute("errorMsg", "Error while forwarding the case details ("+cIno+").");
				}
			} else {
				request.setAttribute("errorMsg", "Invalid Cino :" + cIno);
			}
		} catch (Exception e) {
			con.rollback();
			request.removeAttribute("successMsg");
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
	
	public ActionForward gpReject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
		int a=0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				String actionPerformed="";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") ?  cform.getDynaForm("actionToPerform").toString()+" Returned"  : "CASE DETAILS UPDATED";
				
				msg = "Case details ("+cIno+") updated successfully.";
				
				// get case data
				
				//send back to section if returned / not approved
				sql="select dept_code,dist_id from ecourts_case_data where cino='"+cIno+"'";
				String deptCodeC="", distCodeC="", newStatus="", assigned2Emp="";
				
				List<Map> caseData = DatabasePlugin.executeQuery(con, sql);
				
				if(caseData!=null) {
					Map datainner = (Map)caseData.get(0);
					deptCodeC = CommonModels.checkStringObject(datainner.get("dept_code"));
					distCodeC = CommonModels.checkStringObject(datainner.get("dist_id"));
					System.out.println("deptCodeC::"+deptCodeC);
					System.out.println("distCodeC::"+distCodeC);
					if(deptCodeC.contains("01") && (distCodeC.equals("") || distCodeC.equals("0"))) {//SECTION SECT DEPT
						newStatus="5";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' and assigned_to in (select emailid from mlo_details where user_id='"+deptCodeC+"') order by inserted_on desc limit 1";
						msg = "Returned Case to Section Officer (Sect. Dept.)";
					}
					else if(!deptCodeC.contains("01") && (distCodeC.equals("") || distCodeC.equals("0"))) {//SECTION HOD
						newStatus="9";
						msg = "Returned Case to Section Officer (HOD)";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' "
								+ "and assigned_to in (select emailid from nodal_officer_details where dept_id='"+deptCodeC+"'  and coalesce(dist_id,0) = 0) order by inserted_on desc limit 1";
						
					}
					else if(!distCodeC.equals("") && !distCodeC.equals("0")) {//SECTION DIST
						newStatus="10";
						msg = "Returned Case to Section Officer (District)";
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"' and action_type='CASE FORWARDED' "
								+ "and assigned_to in (select emailid from nodal_officer_details where dept_id='"+deptCodeC+"' and coalesce(dist_id,0) > 0) order by inserted_on desc limit 1";
						
					}
					
					System.out.println("assigned2Emp::"+sql);
					assigned2Emp = DatabasePlugin.getSingleValue(con, sql);
				}
				
				if(cform.getDynaForm("actionToPerform").toString().equals("Parawise Remarks")) {
					//pwr_approved='F',
					sql="update ecourts_case_data set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' "
							+ ",section_officer_updated=null, mlo_no_updated=null where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Parawise Remarks Returned for Case ("+cIno+").";
				}
				else if(cform.getDynaForm("actionToPerform").toString().equals("Counter Affidavit")) {
					// counter_approved='F',
					sql="update ecourts_case_data set case_status="+newStatus+",assigned_to='"+assigned2Emp+"',section_officer_updated=null , mlo_no_updated=null "
							+ "where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Counter Affidavit Returned for Case ("+cIno+").";
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("Yes")) {
					//pwr_approved='F', counter_approved='F',
					sql="update ecourts_case_data set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					//msg = "Counter Affidavit Returned for Case ("+cIno+").";
					
					sql="update ecourts_olcms_case_details set counter_approved_gp='F' where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("No") && CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksSubmitted")).equals("Yes")) {
					//pwr_approved='F',
					sql="update ecourts_case_data set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Parawise Remarks Returned for Case ("+cIno+").";
				}
				
				sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if (a > 0) {
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, assigned_to) "
							+ " values ('" + cIno + "','"+actionPerformed+"', '"+userId+"', '"+request.getRemoteAddr()+"', '"+cform.getDynaForm("remarks")+"','"+assigned2Emp+"')";
					DatabasePlugin.executeUpdate(sql, con);
					
					request.setAttribute("successMsg", msg);
					con.commit();
				} else {
					con.rollback();
					request.setAttribute("errorMsg", "Error while forwarding the case details ("+cIno+").");
				}
			} else {
				request.setAttribute("errorMsg", "Invalid Cino :" + cIno);
			}
		} catch (Exception e) {
			con.rollback();
			request.removeAttribute("successMsg");
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
}
