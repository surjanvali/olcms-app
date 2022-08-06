package in.apcfss.struts.Actions;

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

public class DistrictWiseFinalOrdersImplementationAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		String  empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="";
		try {
			session = request.getSession();
			con = DatabasePlugin.connect();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
			
			
			if(roleId!=null && roleId.equals("4")) { // MLO
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=2";
			}
			else if(roleId!=null && roleId.equals("5")) { // NO
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=4";
			}
			else if(roleId!=null && roleId.equals("8")) { // SECTION OFFICER - SECT. DEPT
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=5 and a.assigned_to='"+userId+"'";
			}
			else if(roleId!=null && roleId.equals("11")) { // SECTION OFFICER - HOD
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=9 and a.assigned_to='"+userId+"'";
			}
			else if(roleId!=null && roleId.equals("12")) { // SECTION OFFICER - DISTRICT
				condition=" and a.dept_code='"+deptCode+"' and dist_id='"+distId+"' and a.case_status=10 and a.assigned_to='"+userId+"'";
			}
			
			
			else if(roleId!=null && roleId.equals("3")) { // SECT DEPT
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=1";
			}
			else if(roleId!=null && roleId.equals("9")) { // HOD
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=3";
			}
			else if(roleId!=null && roleId.equals("2")) { // DC
				condition=" and a.case_status=7 and dist_id='"+distId+"'";
			}
			else if(roleId!=null && roleId.equals("10")) { // DC-NO
				condition=" and a.dept_code='"+deptCode+"' and a.case_status=8 and a.dist_id='"+distId+"'";
			}
			else if(roleId!=null && roleId.equals("6")) { // GPO
				
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				condition=" and a.case_status=6 and e.gp_id='"+userId+"' ";
				
				if(counter_pw_flag.equals("PR")) {
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6'
					condition+=" and pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6'
					condition+=" and pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			

			sql = "select a.*, b.orderpaths , od.pwr_uploaded, od.counter_filed, od.pwr_approved_gp, coalesce(od.counter_approved_gp,'-') as counter_approved_gp "
					+ " ,case when pwr_uploaded='Yes' then 'Parawise Remarks Uploaded' else 'Parawise Remarks not Submitted' end as casestatus1,"
					+ " case when pwr_approved_gp='Yes' then 'Parawise Remarks Approved by GP' else 'Parawise Remarks Not Approved by GP' end as casestatus2,"
					+ " case when counter_filed='Yes' then 'Counter Filed' else 'Counter Not Filed' end as casestatus3,"
					+ " case when counter_approved_gp='T' then 'Counter Approved by GP' else 'Counter Not Approved by GP' end as casestatus4 "
					+ " ,coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, prayer, ra.address "
					+ " from ecourts_case_data a "
					+ " left join nic_prayer_data np on (a.cino=np.cino) "
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ "left join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
					+ " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) "
					+ " "
					+ " left join ecourts_olcms_case_details od on (a.cino=od.cino)"
					+ " where assigned=true "+condition
					+ " and coalesce(a.ecourts_case_status,'')!='Closed' "
					+ " order by a.cino";
			
			System.out.println("AssignedCasesToSectionAction unspecified SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", "Assigned Cases List");
			} else {
				request.setAttribute("errorMsg", "You have Zero cases to Process.");
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
			request.setAttribute("HEADING", "Case Final Order Inmlemented for :" + cIno);
			if (cIno != null && !cIno.equals("")) {
				
				System.out.println("IN CASE STATUS UPDATE METHOD :"+cIno);
				
				con = DatabasePlugin.connect();
				
				// sql = "select * from ecourts_case_data where cino='" + cIno + "'";
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
						sql = "select emailid, short_name from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b on (a.emailid=b.gp_id) where b.dept_code='"+deptCode+"' order by emailid";
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
	
				// sql = "select  * from apolcms.ecourts_res_extra_party where cino='" + cIno + "'";
				sql = "select a.*,b.address from ecourts_res_extra_party a left join nic_resp_addr_data b on (a.cino=b.cino and coalesce(trim(a.party_no),'0')::int4=b.party_no-1) where a.cino='" + cIno + "'";
				sql = "select b.party_no,b.res_name as party_name, b.address from nic_resp_addr_data b left join ecourts_res_extra_party a on (b.cino=a.cino and b.party_no-1=coalesce(trim(a.party_no),'0')::int4) where b.cino='" + cIno + "' order by b.party_no";
				
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("RESEXTRAPARTYLIST", data);
				}

				System.out.println("ecourts SQL:" + sql);
				
				
				sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on";
				System.out.println("ecourts activities SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("ACTIVITIESDATA", data);
				
				sql="select * from ecourts_case_finalorder where cino='" + cIno + "' ";
				System.out.println("ecourts final order SQL:" + sql);
				List<Map<String, Object>> data_final = DatabasePlugin.executeQuery(sql, con);
				String final_order="";
				
				System.out.println("url--"+"https://apolcms.ap.gov.in/"+final_order);
				
				sql = "SELECT cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy, action_to_perfom  "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				
				sql = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document,cordered_impl_date, "
						+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
						+ "case when length(dismissed_copy) > 0 then dismissed_copy else null end as dismissed_copy,"
						+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
						+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
						+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, "
						+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("OLCMSCASEDATA", data);

				if (data != null) {
					
					Map caseData1 = (Map)data.get(0);
					
					if (data_final != null && !data_final.isEmpty() && data_final.size() > 0) {
						///request.setAttribute("RESEXTRAPARTYLIST", data);
						request.setAttribute("final_order", (((Map) data_final.get(0)).get("order_document_path")).toString());
					}
					
					//cform.setDynaForm("petitionDocumentOld" , CommonModels.checkStringObject(caseData1.get("petition_document")));
					//cform.setDynaForm("counterFileCopyOld" , CommonModels.checkStringObject(caseData1.get("counter_filed_document")));
					cform.setDynaForm("judgementOrderOld" , CommonModels.checkStringObject(caseData1.get("judgement_order")));
					cform.setDynaForm("actionTakenOrderOld" , CommonModels.checkStringObject(caseData1.get("action_taken_order")));
					cform.setDynaForm("dismissedFileCopyOld" , CommonModels.checkStringObject(caseData1.get("dismissed_copy")));
					//cform.setDynaForm("" , caseData1.get("last_updated_by"));
					//cform.setDynaForm("" , caseData1.get("last_updated_on"));
					//cform.setDynaForm("counterFiled" , caseData1.get("counter_filed"));
					cform.setDynaForm("remarks" , caseData1.get("remarks"));
					cform.setDynaForm("ecourtsCaseStatus" , caseData1.get("ecourts_case_status"));
					cform.setDynaForm("implementedDt" , caseData1.get("cordered_impl_date"));
					// cform.setDynaForm("relatedGp" , caseData1.get("corresponding_gp"));
					//cform.setDynaForm("parawiseRemarksSubmitted" , caseData1.get("pwr_uploaded"));
					//cform.setDynaForm("parawiseRemarksCopyOld" , CommonModels.checkStringObject(caseData1.get("pwr_uploaded_copy")));
					//cform.setDynaForm("parawiseRemarksDt" , caseData1.get("pwr_submitted_date"));
				//	cform.setDynaForm("dtPRReceiptToGP" , caseData1.get("pwr_received_date"));
					//cform.setDynaForm("pwr_gp_approved" , caseData1.get("pwr_approved_gp"));
					//cform.setDynaForm("dtPRApprovedToGP" , caseData1.get("pwr_gp_approved_date"));
				//	cform.setDynaForm("appealFiled" , caseData1.get("appeal_filed"));
					cform.setDynaForm("appealFileCopyOld" , CommonModels.checkStringObject(caseData1.get("appeal_filed_copy")));
					cform.setDynaForm("appealFiledDt" , caseData1.get("appeal_filed_date"));
					//cform.setDynaForm("actionToPerform" , caseData1.get("action_to_perfom"));
					
					request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
					
				}				
				request.setAttribute("HEADING", "Update Status for Case :" + cIno);
			} else {
				request.setAttribute("errorMsg", "Invalid Cino. / No Records Found to display.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("casestatusupdate");
	}
	public ActionForward updateCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
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
			
			request.setAttribute("HEADING", "Case Final Order Inmlemented for :" + cIno);
			
			
			if (cIno != null && !cIno.equals("")) {
				
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				FileUploadUtilities fuu = new FileUploadUtilities();
				
				String petition_document = "",filePath="", newFileName="", counter_filed_document="",dismissedFileCopy=null, action_taken_order="", judgement_order="", appeal_filed_copy="", pwr_uploaded_copy="";
				
				FormFile myDoc;
				String updateSql="";
				String actionPerformed="";
				String remarks = "";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") ?  cform.getDynaForm("actionToPerform").toString()  : "CASE DETAILS UPDATED";
				remarks = cform.getDynaForm("remarks").toString().replace("'", "");
				
				
				// STATUS CLOSED
				if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("final"))
				{
					if(cform.getDynaForm("actionTakenOrder")!=null  && !cform.getDynaForm("actionTakenOrder").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("actionTakenOrder");
						filePath="uploads/actionorder/";
						newFileName="actionorder_"+CommonModels.randomTransactionNo();
						action_taken_order = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", action_taken_order='"+action_taken_order+"'";
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Action Taken Order','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+action_taken_order+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					
					if(cform.getDynaForm("judgementOrder")!=null  && !cform.getDynaForm("judgementOrder").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("judgementOrder");
						filePath="uploads/judgementorder/";
						newFileName="judgementorder_"+CommonModels.randomTransactionNo();
						judgement_order = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", judgement_order='"+judgement_order+"'";
						
						/*
						 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
						 * + "values ('" + cIno +
						 * "','Uploaded Judgement Order','"+userId+"', '"+request.getRemoteAddr()+"', '"
						 * +remarks+"', '"+judgement_order+"')"; DatabasePlugin.executeUpdate(sql, con);
						 */
					}
					
					
					
					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {
						
						sql="insert into ecourts_olcms_case_details_log "
								+ "(cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy ) "
								+ "select cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy  from ecourts_olcms_case_details where cino='"+cIno+"'";
						DatabasePlugin.executeUpdate(sql, con);
						
						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', "
								+ "judgement_order='"+judgement_order+ "',cordered_impl_date=to_date('"+ CommonModels.checkStringObject(cform.getDynaForm("implementedDt")) + "','dd/mm/yyyy'), remarks='"
								+ remarks + "', last_updated_by='" + userId
								+ "', last_updated_on=now(),action_taken_order='"+action_taken_order+"' where cino='" + cIno + "'";
					}
					else {
						
						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, judgement_order, action_taken_order"
								+ ", last_updated_by, last_updated_on, remarks,cordered_impl_date) "
								+ " values ('" + cIno + "', '" 
								+ cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ judgement_order + "', '" 
								+ action_taken_order + "', '" 
								+ userId + "', now(),'" 
								+ remarks + "',to_date('"+ CommonModels.checkStringObject(cform.getDynaForm("implementedDt")) +"','dd/mm/yyyy'))";
					}
					
					System.out.println("SQL:"+sql);
					
					int a = DatabasePlugin.executeUpdate(sql, con);
					
					sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"',section_officer_updated='T',case_status='99' where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					/*
					 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
					 * + "values ('" + cIno +
					 * "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"
					 * +remarks+"')"; a += DatabasePlugin.executeUpdate(sql, con);
					 */
					
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Cino :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino :" + cIno);
					}
				}
				else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("appeal")){
					
					
					if(cform.getDynaForm("appealFileCopy")!=null && !cform.getDynaForm("appealFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("appealFileCopy");
						filePath="uploads/appealcopies/";
						newFileName="appealcopy_"+CommonModels.randomTransactionNo();
						appeal_filed_copy = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", appeal_filed_copy='"+appeal_filed_copy+"'";
						
						/*
						 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
						 * + "values ('" + cIno +
						 * "','Uploaded Appeal Copy','"+userId+"', '"+request.getRemoteAddr()+"', '"
						 * +remarks+"', '"+appeal_filed_copy+"')"; DatabasePlugin.executeUpdate(sql,
						 * con);
						 */
					}
					
					
					
					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {
						
						sql="insert into ecourts_olcms_case_details_log "
								+ "(cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy ) "
								+ "select cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy  from ecourts_olcms_case_details where cino='"+cIno+"'";
						DatabasePlugin.executeUpdate(sql, con);
						
						
						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', appeal_filed_copy='"
								+ appeal_filed_copy+ "',appeal_filed_date=to_date('"+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt")) + "','dd/mm/yyyy') ,"
								+ "last_updated_by='" + userId + "', last_updated_on=now() "
								+ " where cino='" + cIno + "'";
						
					}
					else {
						
						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, appeal_filed_copy, last_updated_by, last_updated_on, "
								+ " remarks,  appeal_filed_date) "
								+ " values ('" + cIno + "', '" + cform.getDynaForm("ecourtsCaseStatus") + "', '"+appeal_filed_copy+"', '"+userId+"',now(),'" + remarks + "', '"
								+ " to_date('"+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt")) +"','dd/mm/yyyy'))";
					
					}
					System.out.println("SQL:"+sql);
					
					int a = DatabasePlugin.executeUpdate(sql, con);
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', mlo_no_updated='T' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					else {
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', section_officer_updated='T',case_status='99' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					/*
					 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
					 * + "values ('" + cIno +
					 * "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"
					 * +remarks+"')"; a += DatabasePlugin.executeUpdate(sql, con);
					 */
					
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Cino :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino :" + cIno);
					}
					
				}else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("dismissed")){
					
					if( cform.getDynaForm("dismissedFileCopy")!=null && !cform.getDynaForm("dismissedFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("dismissedFileCopy");
						filePath="uploads/dismissedFileCopies/";
						newFileName="dismissedcopy_"+CommonModels.randomTransactionNo();
						dismissedFileCopy = fuu.saveFile(myDoc, filePath, newFileName);
						
						//updateSql += ", appeal_filed_copy='"+dismissedFileCopy+"'";
						
						/*
						 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
						 * + "values ('" + cIno +
						 * "','Uploaded Appeal Copy','"+userId+"', '"+request.getRemoteAddr()+"', '"
						 * +remarks+"', '"+appeal_filed_copy+"')"; DatabasePlugin.executeUpdate(sql,
						 * con);
						 */
					}
					
					
					
					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {
						
						sql="insert into ecourts_olcms_case_details_log "
								+ "(cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy ) "
								+ "select cino, petition_document, counter_filed_document, judgement_order, action_taken_order, last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, pwr_gp_approved_date, appeal_filed, appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, counter_approved_gp, action_to_perfom, counter_approved_date, counter_approved_by, respondent_slno, is_orderimplemented, counter_filed_date, cordered_impl_date, dismissed_copy  from ecourts_olcms_case_details where cino='"+cIno+"'";
						DatabasePlugin.executeUpdate(sql, con);
						
						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', dismissed_copy='"
								+ dismissedFileCopy+ "', last_updated_by='" + userId + "', last_updated_on=now()  where cino='" + cIno + "'";
						
					}
					else {
						
						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, dismissed_copy,  last_updated_by, last_updated_on,  remarks) "
								+ " values ('" + cIno + "', '" + cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ dismissedFileCopy + "','" + userId + "', now(),'" + remarks + "')";
					
					}
					System.out.println("SQL:"+sql);
					
					int a = DatabasePlugin.executeUpdate(sql, con);
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', mlo_no_updated='T' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					else {
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', section_officer_updated='T',case_status='99' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					/*
					 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
					 * + "values ('" + cIno +
					 * "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"
					 * +remarks+"')"; a += DatabasePlugin.executeUpdate(sql, con);
					 */
					
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Cino :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino :" + cIno);
					}
					
				}
				cform.setDynaForm("remarks", "");
			} else {
				request.setAttribute("errorMsg", "Invalid Cino :" + cIno);
			}
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.closeConnection(con);
		}
		// return unspecified(mapping, cform, request, response);
		return caseStatusUpdate(mapping, cform, request, response);
	}


	
}