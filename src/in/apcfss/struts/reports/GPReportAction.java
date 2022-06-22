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
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis from ecourts_case_data a "
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
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6'
					condition+=" and pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					heading = "Counters filed Cases List";
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6'
					condition+=" and pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			
			sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis,a.cino from ecourts_case_data a "
					+ " left join ecourts_olcms_case_details od on (a.cino=od.cino)"
					+ " left join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " where assigned=true "+condition
					+ " and coalesce(a.ecourts_case_status,'')!='Closed' "+condition;
					
			
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
				
				sql = "select type_name_reg,reg_no,reg_year, to_char(dt_regis,'dd-mm-yyyy') as dt_regis,cino from ecourts_case_data a "
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
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
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

			if (cIno != null && !cIno.equals("")) {
				
				System.out.println("IN CASE STATUS UPDATE METHOD :"+cIno);
				
				con = DatabasePlugin.connect();
				
				sql = "select * from ecourts_case_data where cino='" + cIno + "'";
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
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("RESEXTRAPARTYLIST", data);
				}

				System.out.println("ecourts SQL:" + sql);
				
				
				sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on";
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
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom "
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
					
					request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
					
				}				
				request.setAttribute("HEADING", " Case NO. :" + cIno);
			} else {
				request.setAttribute("errorMsg", "Invalid Cino. / No Records Found to display.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		// return mapping.findForward("casestatusupdate");
		return mapping.findForward("viewCaseData");
	}
	
	public ActionForward viewCaseData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		
		
		return mapping.findForward("viewCaseData");
	}
}
