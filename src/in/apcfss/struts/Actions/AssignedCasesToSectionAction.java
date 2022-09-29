package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;

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

import plugins.DatabasePlugin;

public class AssignedCasesToSectionAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="";

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

			
			/*if(roleId!=null && roleId.equals("4")) { // MLO / NO
				condition=" and a.case_status=2";
			}
			else if(roleId!=null && roleId.equals("5")) { // NO
				condition=" and a.case_status=4";
			}
			else if(roleId!=null && roleId.equals("8")) { // SECTION OFFICER
				condition=" and a.case_status=5 and assigned_to='"+userId+"'";
			}
			
			else if(roleId!=null && roleId.equals("3")) { // SECT DEPT
				condition=" and a.case_status=1";
			}
			else if(roleId!=null && roleId.equals("9")) { // HOD
				condition=" and a.case_status=3";
			}
			else if(roleId!=null && roleId.equals("4")) { // DC
				condition=" and a.case_status=7 and dist_id='"+distId+"'";
			}
			/*
			  else if(roleId!=null && roleId.equals("5")) { // Dist-NO
			  condition=" and a.case_status=8"; }
			 * /
			else if(roleId!=null && roleId.equals("6")) { // GPO
				condition=" and a.case_status=4";
			}*/
			
			
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
					+ " " //sql = "select a.*, prayer from ecourts_case_data a left join nic_prayer_data np on (a.cino=np.cino) where a.cino='" + cIno + "'";
					+ " ,coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, ra.address, "
					+ " case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full"
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
					// + " left join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) "
					
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
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	public ActionForward getCino(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, cIno = null, viewDisplay=null, target="caseview";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			viewDisplay = CommonModels.checkStringObject(request.getParameter("SHOWPOPUP"));
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			if(!viewDisplay.equals("") && viewDisplay.equals("SHOWPOPUP")) {
				target = "casepopupview";
				cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			}
			else {
				cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			}
			
			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();
	
				// sql = "select * from ecourts_case_data where cino='" + cIno + "'";
				sql = "select a.*, prayer from ecourts_case_data a left join nic_prayer_data np on (a.cino=np.cino) where a.cino='" + cIno + "'";
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
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
	
				sql = "select a.*,b.address from ecourts_res_extra_party a left join nic_resp_addr_data b on (a.cino=b.cino and coalesce(trim(a.party_no),'0')::int4=b.party_no-1) where a.cino='" + cIno + "' order by coalesce(trim(a.party_no),'0')::int4 ";
				sql="select b.party_no,b.res_name as party_name, b.address from nic_resp_addr_data b left join ecourts_res_extra_party a on (b.cino=a.cino and b.party_no-1=coalesce(trim(a.party_no),'0')::int4) where b.cino='"+cIno+"' order by b.party_no";
				System.out.println("RESP ADDR SQL:"+sql);
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("RESEXTRAPARTYLIST", data);
				}
				
				/*
				sql=" select * from ecourts_olcms_case_details_log where cino='"+cIno+"'";
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("OLCMSCASEDATALOG", data);
			*/
				
				sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on";
				System.out.println("ecourts activities SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("ACTIVITIESDATA", data);
			
			
			sql = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document, "
					+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
					+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
					+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
					+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
					+ " pwr_uploaded, to_char(pwr_submitted_date,'dd/mm/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'dd/mm/yyyy') as pwr_received_date, "
					+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'dd/mm/yyyy') as pwr_gp_approved_date, appeal_filed, "
					+ " appeal_filed_copy, to_char(appeal_filed_date,'dd/mm/yyyy') as appeal_filed_date, pwr_uploaded_copy "
					+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
			
			data = DatabasePlugin.executeQuery(sql, con);
			request.setAttribute("OLCMSCASEDATA", data);
	
				request.setAttribute("HEADING", "Case Details for CINO : " + cIno);
			}
			else {
				request.setAttribute("errorMsg", "Invalid Cino.");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
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
						sql = "select emailid, full_name||' ('|| replace(emailid,'@ap.gov.in','') ||')' from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b on (a.emailid=b.gp_id) where b.dept_code='"+deptCode+"' order by emailid";
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
				
				/*
					sql=" select * from ecourts_olcms_case_details_log where cino='"+cIno+"'";
					data = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("OLCMSCASEDATALOG", data);
				*/
				
				List<Map<String, Object>> data_status = DatabasePlugin.executeQuery(sql, con);
				
				sql=" select case_status,ecourts_case_status from ecourts_case_data where cino='" + cIno + "' ";
				data_status = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("OLCMSCASEDATA", data_status);
				
				String caseStatus=(String)data_status.get(0).get("ecourts_case_status").toString();
				if( (caseStatus.equals(null)  || !caseStatus.equals("Private"))) {
				
				sql = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document, "
						+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
						+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
						+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
						+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
						+ " pwr_uploaded, to_char(pwr_submitted_date,'mm/dd/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'mm/dd/yyyy') as pwr_received_date, "
						+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'mm/dd/yyyy') as pwr_gp_approved_date, appeal_filed, "
						+ " appeal_filed_copy, to_char(appeal_filed_date,'mm/dd/yyyy') as appeal_filed_date, pwr_uploaded_copy "
						+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "'";
				
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
				}
				
				if (data_status != null) {
					
					Map caseData1 = (Map)data_status.get(0);
				
					//cform.setDynaForm("remarks" , caseData1.get("remarks"));
					cform.setDynaForm("ecourtsCaseStatus" , caseData1.get("ecourts_case_status"));
					
					///cform.setDynaForm("actionToPerform" , caseData1.get("action_to_perfom"));
					
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
	
	public ActionForward fileCounterAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));

			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();

				// sql = "select a.* from ecourts_case_data a where a.cino='" + cIno + "'";
				sql = "select a.*, prayer from ecourts_case_data a left join nic_prayer_data np on (a.cino=np.cino) where a.cino='" + cIno + "'";

				System.out.println("ecourts SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASEDATA", data);
					
					sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks from ecourts_case_activities where cino = '"+cIno+"' and action_type='COUNTER FILE' order by inserted_on";
					System.out.println("ecourts activities SQL:" + sql);
					data = DatabasePlugin.executeQuery(sql, con);
					
					request.setAttribute("ACTIVITIESDATA", data);
					
					request.setAttribute("HEADING", "File Counter for Case :" + cIno);
				} else {
					request.setAttribute("errorMsg", "No Records Found to display.");
				}
			} else {
				request.setAttribute("errorMsg", "Invalid Cino.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("filecounter");
	}

	public ActionForward saveCounterFileData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String newStatus = "";
			String assgiend_backUser = DatabasePlugin.getSingleValue(con, "select inserted_by as assignedby from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on desc");
			if(roleId!=null && roleId.equals("4")) { // MLO / NO
				newStatus="4";
				assgiend_backUser = "GPO";
			}else if(roleId!=null && roleId.equals("5")) { // NO
				newStatus="4";
				assgiend_backUser = "GPO";
			}else if(roleId!=null && roleId.equals("6")) { // GPO
				newStatus="";
			}else if(roleId!=null && roleId.equals("8")) { // SECTION OFFICER
				newStatus="3";
			}
			
			if (cIno != null && !cIno.equals("")) {

				System.out.println("counterRemarks::" + cform.getDynaForm("counterRemarks"));

				con = DatabasePlugin.connect();
				con.setAutoCommit(false);

				sql = "update ecourts_case_emp_assigned_dtls set counter_filed=true, counter_file_remarks='"
						+ cform.getDynaForm("counterRemarks") + "',counter_filed_on=now(), counter_filed_ip='"
						+ request.getRemoteAddr() + "' where cino='" + cIno + "'";
				System.out.println("ecourts SQL:" + sql);
				int a = DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
						+ "values ('" + cIno + "','COUNTER FILE','"+userId+"', '"+request.getRemoteAddr()+"', null, '"+cform.getDynaForm("counterRemarks")+"')";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="update ecourts_case_data set case_status='"+newStatus+"', assigned_to='"+assgiend_backUser+"' where cino='" + cIno + "' ";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				
				if (a==3) {
					request.setAttribute("successMsg", "Counter Filed successfully for Cino :" + cIno);
					con.commit();
				} else {
					con.rollback();
					request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
				}
			} else {
				request.setAttribute("errorMsg", "Invalid Cino :" + cIno);
			}
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
	
	//NEW METHOD TO SAVE UPLOADS 17-01-2022
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
			
			/*
			String newStatus = "";
			String assgiend_backUser = DatabasePlugin.getSingleValue(con, "select inserted_by as assignedby from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on desc");
			if(roleId!=null && roleId.equals("4")) { // MLO / NO
				newStatus="4";
				assgiend_backUser = "GPO";
			}else if(roleId!=null && roleId.equals("5")) { // NO
				newStatus="4";
				assgiend_backUser = "GPO";
			}else if(roleId!=null && roleId.equals("6")) { // GPO
				newStatus="";
			}else if(roleId!=null && roleId.equals("8")) { // SECTION OFFICER
				newStatus="3";
			}*/
			
			if (cIno != null && !cIno.equals("")) {
				
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				FileUploadUtilities fuu = new FileUploadUtilities();
				
				String petition_document = "",filePath="", newFileName="", counter_filed_document="", action_taken_order="", judgement_order="", appeal_filed_copy="", pwr_uploaded_copy="";
				
				FormFile myDoc;
				String updateSql="";
				String actionPerformed="";
				String remarks = "";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") ?  cform.getDynaForm("actionToPerform").toString()  : "CASE DETAILS UPDATED";
				remarks = cform.getDynaForm("remarks").toString().replace("'", "");
				if(cform.getDynaForm("petitionDocument")!=null && !cform.getDynaForm("petitionDocument").toString().equals("")) {
					myDoc = (FormFile)cform.getDynaForm("petitionDocument");
					filePath="uploads/petitions/";
					newFileName="petition_"+CommonModels.randomTransactionNo();
					petition_document = fuu.saveFile(myDoc, filePath, newFileName);
					
					updateSql += ", petition_document='"+petition_document+"'";
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
							+ "values ('" + cIno + "','Uploaded Petition','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+petition_document+"')";
					DatabasePlugin.executeUpdate(sql, con);
				}
				
				
				// STATUS CLOSED
				if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("Closed"))
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
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Judgement Order','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+judgement_order+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					
					if(cform.getDynaForm("appealFiled")!=null && cform.getDynaForm("appealFiled").toString().equals("Yes") && cform.getDynaForm("appealFileCopy")!=null && !cform.getDynaForm("appealFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("appealFileCopy");
						filePath="uploads/appealcopies/";
						newFileName="appealcopy_"+CommonModels.randomTransactionNo();
						appeal_filed_copy = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", appeal_filed_copy='"+appeal_filed_copy+"'";
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Appeal Copy','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+appeal_filed_copy+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					
					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {
						
						// sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
						
						sql="insert into ecourts_olcms_case_details_log (cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated) "
								+ "select cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated  from ecourts_olcms_case_details where cino='"+cIno+"'";
						
						DatabasePlugin.executeUpdate(sql, con);
						
						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', appeal_filed='"
								+ cform.getDynaForm("appealFiled") + "',appeal_filed_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt")) + "','mm/dd/yyyy'), remarks='"
								+ remarks + "', last_updated_by='" + userId
								+ "', last_updated_on=now(), action_to_perfom='"+cform.getDynaForm("actionToPerform")
								+"' " + updateSql + " where cino='" + cIno + "'";
					}
					else {
						
						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, petition_document, appeal_filed, appeal_filed_copy, judgement_order, action_taken_order"
								+ ", last_updated_by, last_updated_on, remarks, appeal_filed_date, action_to_perfom) "
								+ " values ('" + cIno + "', '" 
								+ cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ petition_document + "','" 
								+ cform.getDynaForm("appealFiled") + "', '"
								+ appeal_filed_copy + "', '" 
								+ judgement_order + "', '" 
								+ action_taken_order + "', '" 
								+ userId + "', now(),'" 
								+ remarks + "',to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt"))+"','mm/dd/yyyy'),'"+cform.getDynaForm("actionToPerform")+"')";
					}
					
					System.out.println("SQL:"+sql);
					
					int a = DatabasePlugin.executeUpdate(sql, con);
					
					sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"',section_officer_updated='T' where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					if (a == 3) {
						request.setAttribute("successMsg", "Case details updated successfully for Cino :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino :" + cIno);
					}
				}
				else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("Pending")){
					
					
					if(cform.getDynaForm("counterFiled")!=null && cform.getDynaForm("counterFiled").toString().equals("Yes") 
							&& cform.getDynaForm("counterFileCopy")!=null  && !cform.getDynaForm("counterFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("counterFileCopy");
						filePath="uploads/counters/";
						newFileName="counter_"+CommonModels.randomTransactionNo();
						counter_filed_document = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", counter_filed_document='"+counter_filed_document+"'";
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Counter','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+counter_filed_document+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					
					
					if(cform.getDynaForm("parawiseRemarksSubmitted")!=null && cform.getDynaForm("parawiseRemarksSubmitted").toString().equals("Yes") 
							&& cform.getDynaForm("parawiseRemarksCopy")!=null  && !cform.getDynaForm("parawiseRemarksCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("parawiseRemarksCopy");
						filePath = "uploads/parawiseremarks/";
						newFileName = "parawiseremarks_"+CommonModels.randomTransactionNo();
						pwr_uploaded_copy = fuu.saveFile(myDoc, filePath, newFileName);
						
						updateSql += ", pwr_uploaded_copy='"+pwr_uploaded_copy+"'";
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Parawise Remarks','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+pwr_uploaded_copy+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					
					
					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {
						
						sql="insert into ecourts_olcms_case_details_log (cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated) "
								+ "select cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated  from ecourts_olcms_case_details where cino='"+cIno+"'";
						
						DatabasePlugin.executeUpdate(sql, con);
						
						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', counter_filed='"
								+ cform.getDynaForm("counterFiled") + "', remarks='" + remarks
								+ "', last_updated_by='" + userId + "', last_updated_on=now() " + updateSql
								+ ", corresponding_gp='" + cform.getDynaForm("relatedGp") + "', pwr_uploaded='"
								+ cform.getDynaForm("parawiseRemarksSubmitted") + "', pwr_submitted_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt"))
								+ "','mm/dd/yyyy'), pwr_received_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("dtPRReceiptToGP")) + "','mm/dd/yyyy'),pwr_approved_gp='"
								+ cform.getDynaForm("pwr_gp_approved") + "',"
								+ " pwr_gp_approved_date=to_date('" + CommonModels.checkStringObject(cform.getDynaForm("dtPRApprovedToGP"))
								+ "','mm/dd/yyyy'), action_to_perfom='"+cform.getDynaForm("actionToPerform")  + "' where cino='" + cIno + "'";
						
					}
					else {
						
						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, petition_document, counter_filed_document, last_updated_by, last_updated_on, "
								+ "counter_filed, remarks,  corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, "
								+ "pwr_gp_approved_date, pwr_uploaded_copy, action_to_perfom) "
								+ " values ('" + cIno + "', '" + cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ petition_document + "','" + counter_filed_document + "','" + userId + "', now(),'"
								+ cform.getDynaForm("counterFiled") + "', '" + remarks + "', '"

								+ cform.getDynaForm("relatedGp") + "', '"
								+ cform.getDynaForm("parawiseRemarksSubmitted") + "'," 
								
								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','mm/dd/yyyy'), " 
								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("dtPRReceiptToGP")) + "','mm/dd/yyyy'), '"
								+ cform.getDynaForm("pwr_gp_approved") + "'," 
								
								+ " to_date('" + cform.getDynaForm("dtPRApprovedToGP") + "','mm/dd/yyyy'), '" 
								+ pwr_uploaded_copy + "','"+cform.getDynaForm("actionToPerform")+"')";
					
					}
					System.out.println("SQL:"+sql);
					
					int a = DatabasePlugin.executeUpdate(sql, con);
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', mlo_no_updated='T' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					else {
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', section_officer_updated='T' where cino='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					if (a == 3) {
						request.setAttribute("successMsg", "Case details updated successfully for Cino :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino :" + cIno);
					}
					
				}else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("Private")){
					
					int a=0;
					
					
						sql="update ecourts_case_data set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', case_status='98' "
								+ " where cino='"+cIno+"' and dept_code='"+deptCode+"'  "; 
						 a = DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a-----3------------"+a);
					
					
					System.out.println("update-----4------------"+sql);
					System.out.println("a-sql-----------"+a);
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					System.out.println("a----5-------------"+a);
					
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated Successfully for Cino No :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Cino No :" + cIno);
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
	
	public ActionForward forwardCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null,  deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null, distId=null;
		int a=0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			//deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String newStatus = "", msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				System.out.println("deptCode::"+deptCode.substring(3, 5));
				String fwdOfficer="";
				
				if(roleId!=null && roleId.equals("4")) {//FROM MLO to SECT DEPT.
					fwdOfficer = deptCode;
					newStatus = "1";
					msg = "Case details ("+cIno+") forwarded successfully to Secretary.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and mlo_no_updated='T' and case_status='2'";
					
				}
				else if(roleId!=null && roleId.equals("5")) {//FROM NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and mlo_no_updated='T' and case_status='4'";
					
				}
				else if(roleId!=null && roleId.equals("10")) {//FROM Dist-NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and mlo_no_updated='T' and case_status='8'";
					
				}
				
				else if((roleId!=null && roleId.equals("8")) && (deptCode.substring(3, 5)=="01" || deptCode.substring(3, 5).equals("01"))) { //FROM SECTION-SECT. TO MLO
					fwdOfficer = DatabasePlugin.selectString("select trim(emailid) from mlo_details where user_id='"+deptCode+"'", con);
					newStatus = "2";
					msg = "Case details ("+cIno+") forwarded successfully to MLO.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='5'";
				} 
				else if(roleId!=null && roleId.equals("11")){// FROM SECTION(HOD) TO NO-HOD
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and user_id not like '%DC-%'", con);
					newStatus = "4";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='9'";
					
				}
				else if(roleId!=null && roleId.equals("12")){// FROM SECTION(DIST) TO NO-HOD-DIST
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"", con);
					newStatus = "8";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='10'";
				}
				
				System.out.println("SQL:"+sql);
				a = DatabasePlugin.executeUpdate(sql, con);
				
				if (a > 0) {
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks) "
							+ " values ('" + cIno + "','CASE FORWARDED', '"+userId+"', '"+request.getRemoteAddr()+"', '"+fwdOfficer+"', '"+cform.getDynaForm("remarks")+"')";
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
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
	
	public ActionForward sendBackCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null,  deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null, distId=null;
		int a=0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			//deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String newStatus = "", msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				System.out.println("deptCode::"+deptCode.substring(3, 5));
				
				sql="select emp_user_id from ecourts_case_emp_assigned_dtls ad inner join ecourts_case_activities ea on (ad.cino=ea.cino) "
						+ "where ea.cino='"+cIno+"' and ea.action_type ilike 'CASE ASSSIGNED%' and ea.inserted_by='"+userId+"'";
				
				String backToSectionUser = DatabasePlugin.getStringfromQuery(sql , con);
				
				if(roleId!=null && roleId.equals("4")) {//FROM MLO to Section.
					newStatus = "5";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='2'";
					
				}
				else if(roleId!=null && roleId.equals("5")) {//FROM NO TO Section
					newStatus = "9";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='4'";
					
				}
				else if(roleId!=null && roleId.equals("10")) {//FROM Dist-NO TO Dist-Section
					newStatus = "10";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where cino='"+cIno+"' and section_officer_updated='T' and case_status='8'";
					
				}
				
				
				System.out.println("SQL:"+sql);
				a = DatabasePlugin.executeUpdate(sql, con);
				
				if (a > 0) {
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks) "
							+ " values ('" + cIno + "','CASE SENT BACK', '"+userId+"', '"+request.getRemoteAddr()+"', '"+backToSectionUser+"', '"+cform.getDynaForm("remarks")+"')";
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
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
	}
	
	
	public ActionForward forwardCaseDetails2GP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			String newStatus = "", msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				System.out.println("deptCode::"+deptCode.substring(3, 5));
				String fwdOfficer = CommonModels.checkStringObject(cform.getDynaForm("gpCode"));
				
				if(roleId!=null && roleId.equals("3")) {//FROM SECT DEPT TO GP.
					newStatus = "6";
					msg = "Case details ("+cIno+") forwarded successfully to GP for Approval.";
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"'  and mlo_no_updated='T' and case_status='1'";//and section_officer_updated='T' 
					
				}
				else if(roleId!=null && roleId.equals("9")) {//FROM HOD TO GP
					newStatus = "6";
					msg = "Case details ("+cIno+") forwarded successfully to GP for Approval.";
					
					sql="update ecourts_case_data set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where cino='"+cIno+"' and mlo_no_updated='T' and case_status='3'"; //and section_officer_updated='T' 
					
				}
				
				System.out.println("SQL:"+sql);
				a = DatabasePlugin.executeUpdate(sql, con);
				
				if (a > 0) {
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks) "
							+ " values ('" + cIno + "','CASE FORWARDED TO GP', '"+userId+"', '"+request.getRemoteAddr()+"', '"+fwdOfficer+"', '"+cform.getDynaForm("remarks")+"')";
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
			request.setAttribute("errorMsg", "Error while filing Counter for Cino :" + cIno);
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return unspecified(mapping, cform, request, response);
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
				
				String actionPerformed="";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("0") ?  cform.getDynaForm("actionToPerform").toString()+" Approved"  : "CASE DETAILS UPDATED";
				
				msg = "Case details ("+cIno+") updated successfully.";
				
				// sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
				
				sql="insert into ecourts_olcms_case_details_log (cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated) "
						+ "select cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated  from ecourts_olcms_case_details where cino='"+cIno+"'";
				
				
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if(cform.getDynaForm("actionToPerform").toString().equals("Parawise Remarks")) {
					/*sql="update ecourts_case_data set pwr_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);*/
					
					sql="update ecourts_olcms_case_details set pwr_approved_gp='Yes',pwr_gp_approved_date=current_date  where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					msg = "Parawise Remarks Approved successfully for Case ("+cIno+").";
				}
				else if(cform.getDynaForm("actionToPerform").toString().equals("Counter Affidavit")) {
					/*sql="update ecourts_case_data set counter_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);*/
					
					msg = "Counter Affidavit Approved successfully for Case ("+cIno+").";
					
					sql="update ecourts_olcms_case_details set counter_approved_gp='T',counter_approved_date=current_date, counter_approved_by='"+userId+"' where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("Yes")) {
					/*sql="update ecourts_case_data set pwr_approved='T', counter_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					*/
					sql="update ecourts_olcms_case_details set counter_approved_gp='T',counter_approved_date=current_date, counter_approved_by='"+userId+"' where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					msg = "Counter Affidavit Approved successfully for Case ("+cIno+").";
					
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("No") && CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksSubmitted")).equals("Yes")) {
					
					/*sql="update ecourts_case_data set pwr_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					*/
					
					sql="update ecourts_olcms_case_details set pwr_approved_gp='Yes',pwr_gp_approved_date=current_date  where cino='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					msg = "Parawise Remarks Approved successfully for Case ("+cIno+").";
					
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
				
				// sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
				
				sql="insert into ecourts_olcms_case_details_log (cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated) "
						+ "select cino,petition_document, counter_filed_document,   judgement_order,action_taken_order,last_updated_by,last_updated_on,  counter_filed,remarks,ecourts_case_status,corresponding_gp,pwr_uploaded,pwr_submitted_date,pwr_received_date,pwr_approved_gp,pwr_gp_approved_date,appeal_filed,appeal_filed_copy,appeal_filed_date,pwr_uploaded_copy,counter_approved_gp,action_to_perfom,counter_approved_date,counter_approved_by,respondent_slno,cordered_impl_date,dismissed_copy,final_order_status,no_district_updated  from ecourts_olcms_case_details where cino='"+cIno+"'";
				
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