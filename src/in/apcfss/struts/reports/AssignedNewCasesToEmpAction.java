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
import org.apache.struts.upload.FormFile;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.Utilities.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import plugins.DatabasePlugin;

public class AssignedNewCasesToEmpAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, userid=null,roleId = null, deptCode = null, distCode="0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			
			System.out.println("userid--"+userid);
			System.out.println("roleId--"+roleId);
			System.out.println("deptCode--"+deptCode);

			String sqlCondition = "";
			String condition1 = "";
			String condition2 = "";
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

			
			/*
			 * if (roleId.equals("2")) { sqlCondition +=
			 * " and (case_status is null or case_status=7) and ad.dist_id='" + distCode +
			 * "' "; cform.setDynaForm("districtId", distCode); } if (roleId.equals("10")) {
			 * // District Nodal Officer sqlCondition +=
			 * " and (case_status is null or case_status=8) and dist_id='" + distCode +
			 * "' and ad.dept_code='"+deptCode+"'  "; }
			 * 
			 * if (roleId.equals("3")) { // sqlCondition += " and (dmt.dept_code='" +
			 * deptCode + "' or dmt.reporting_dept_code='" + deptCode + "') "; } if
			 * (roleId.equals("4")) {// MLO & Sect. Dept. sqlCondition +=
			 * " and ( case_status in (1, 2)) and ad.dept_code='"
			 * +deptCode+"' and section_officer_updated='T' and assigned='true'"; }
			 * 
			 * 
			 * if (roleId.equals("5") || roleId.equals("9")) { // NO & HOD sqlCondition +=
			 * " and ( case_status in (3,4))  "; //and (ad.dept_code='" + deptCode + "') }
			 * 
			 * if (roleId.equals("8")) { sqlCondition +=" and ad.dept_code='"
			 * +deptCode+"' and ad.case_status=5 and ad.assigned_to='"+userid+"'"; } if
			 * (roleId.equals("11")) { sqlCondition +=
			 * " and ad.case_status='9' and ad.assigned_to='"+userid+"' "; } if
			 * (roleId.equals("12")) { sqlCondition +=
			 * " and ad.case_status='10' and ad.assigned_to='"+userid+"' ";
			 * 
			 * }
			 */
			if(roleId!=null && roleId.equals("4")) { // MLO
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=2 and coalesce(assigned,'f')='t'";
			}
			else if(roleId!=null && roleId.equals("5")) { // NO
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=4 and coalesce(assigned,'f')='t'  ";
			}
			else if(roleId!=null && roleId.equals("8")) { // SECTION OFFICER - SECT. DEPT
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=5 and ad.assigned_to='"+userid+"'";
			}
			else if(roleId!=null && roleId.equals("11")) { // SECTION OFFICER - HOD
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=9 and ad.assigned_to='"+userid+"'";
			}
			else if(roleId!=null && roleId.equals("12")) { // SECTION OFFICER - DISTRICT
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.dist_id='"+distCode+"' and ad.case_status=10 and coalesce(assigned,'f')='t'   and ad.assigned_to='"+userid+"'";
			}
			
			
			else if(roleId!=null && roleId.equals("3")) { // SECT DEPT
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=1  and coalesce(assigned,'f')='t'   ";
			}
			else if(roleId!=null && roleId.equals("9")) { // HOD
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=3  and coalesce(assigned,'f')='t' ";
			}
			else if(roleId!=null && roleId.equals("2")) { // DC
				sqlCondition=" and ad.case_status=7 and coalesce(assigned,'f')='t'  and ad.dist_id='"+distCode+"'";
			}
			else if(roleId!=null && roleId.equals("10")) { // DC-NO
				sqlCondition=" and ad.dept_code='"+deptCode+"' and ad.case_status=8 and coalesce(assigned,'f')='t'   and ad.dist_id='"+distCode+"'";
			}
			

			if(roleId!=null && roleId.equals("6")) { // GPO
				
				condition1 = " inner join ecourts_mst_gp_dept_map emgd on (ad.dept_code=emgd.dept_code) "
						+ " inner join ecourts_olcms_case_details eocd on (eocd.cino=ad.ack_no)";
				
				sqlCondition += " and counter_filed='Yes' and ad.case_status='6' and coalesce(assigned,'f')='t' and ad.assigned_to='"+userid+"' ";
				//cform.setDynaForm("districtId", distCode);
				
				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				
				if(counter_pw_flag.equals("PR")) {
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6'
					condition1+=" and pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' )";
				}
				if(counter_pw_flag.equals("COUNTER")) {
					//pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F' and ecd.case_status='6'
					condition1+=" and pwr_uploaded='Yes' and counter_filed='No' and coalesce(counter_approved_gp,'F')='F'";
				}
			}
			
			
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and a.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}

			sql = "select a.slno ,ad.respondent_slno, a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , a.remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ "to_char(a.inserted_time,'dd-mm-yyyy') as generated_date, "
					+ "getack_dept_desc(a.ack_no::text) as dept_descs , coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ " from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
					+ " left join district_mst dm on (ad.dist_id=dm.district_id) "
					+ " left join dept_new dmt on (ad.dept_code=dmt.dept_code)"
					+ " inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "+condition1+"   "
					+ " where a.delete_status is false and ack_type='NEW'  and coalesce(ad.ecourts_case_status,'')!='Closed'  " + sqlCondition
					+ " order by a.inserted_time desc";

			System.out.println("CASES SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
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

			if (roleId.equals("1") || roleId.equals("7")|| roleId.equals("2"))
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
	public ActionForward caseStatusUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null,distCode="0";
		//String sql = null, roleId = null, deptCode = null, distCode="0";
		String cIno1[] = CommonModels.checkStringObject(cform.getDynaForm("fileCino")).split("@");
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			System.out.println("cIno-0-"+cIno1[0]);
			System.out.println("cIno-1-"+cIno1[1]);
			cIno = cIno1[0];
			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();

				String sqlCondition = "";

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

				/*
				 * if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2"))) {
				 * sqlCondition += " and (dmt.dept_code='" + deptCode +
				 * "' or dmt.reporting_dept_code='"+deptCode+"') "; }
				 */

				if (roleId.equals("2")) {
					sqlCondition += " and ad.dist_id='" + distCode + "' ";
					//cform.setDynaForm("districtId", distCode);
				}
				if (roleId.equals("3")) {// Secretariat Department
					sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='" + deptCode + "') ";
				}
				if (roleId.equals("9")) {// Secretariat Department
					sqlCondition += " and (dmt.dept_code='" + deptCode + "') ";
				}
				if (roleId.equals("8")) {
					sqlCondition += " and ad.case_status='5' ";
					//cform.setDynaForm("districtId", distCode);
				}
				if (roleId.equals("11")) {
					sqlCondition += " and ad.case_status='9' ";
					//cform.setDynaForm("districtId", distCode);
				}
				if (roleId.equals("12")) {
					sqlCondition += " and ad.case_status='10' ";
					//cform.setDynaForm("districtId", distCode);
				}

				if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
						&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
					sqlCondition += " and a.casetype='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
				}
				
				/*
				 * if(roleId!=null && roleId.equals("6")) { // GPO
				 * 
				 * condition1 =
				 * " inner join ecourts_mst_gp_dept_map emgd on (ad.dept_code=emgd.dept_code) "
				 * " inner join ecourts_olcms_case_details eocd on (eocd.cino=ad.ack_no)";
				 * 
				 * sqlCondition +=
				 * " and counter_filed='Yes' and ad.case_status='6' and ad.assigned_to='"
				 * +userid+"' ";
				 * 
				 * }
				 */
				if(roleId!=null && roleId.equals("6")) { 
					
					
				}
				

				sql = "select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
						+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs,ad.assigned,ad.assigned_to,ad.case_status,ad.ecourts_case_status,ad.section_officer_updated,ad.mlo_no_updated  "
						+ "from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no ) "
						+ "left join district_mst dm on (a.distid=dm.district_id) "
						+ "left join dept_new dmt on (ad.dept_code=dmt.dept_code)"
						+ "inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
						+ " where a.delete_status is false and ack_type='NEW' and respondent_slno='"+cIno1[1]+"' " + sqlCondition 
						+ " and a.ack_no='"+cIno+"'  order by inserted_time desc";

				System.out.println("CASES SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				//System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("USERSLIST", data);
				} else {
					request.setAttribute("errorMsg", "No details found.");
				}

				System.out.println("data-------"+data);
				
				System.out.println("roleId-------"+roleId);
				System.out.println("deptCode-------"+deptCode);
				
				
				
				//=======================

				if (data != null) {

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
						sql = "select emailid, full_name||' ('|| replace(emailid,'@ap.gov.in','') ||')' from ecourts_mst_gps a inner join ecourts_mst_gp_dept_map b on (a.emailid=b.gp_id) where b.dept_code='"+deptCode+"' order by emailid";

						cform.setDynaForm("GPSLIST", DatabasePlugin.getSelectBox(sql, con));
						request.setAttribute("SHOWGPBTN", "SHOWGPBTN");
					}
					else if(roleId.equals("6") ) { // GP LOGIN
						request.setAttribute("SHOWGPAPPROVEBTN", "SHOWGPAPPROVEBTN");
					}
					
					sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks, "
							+ "    CASE  WHEN length(trim(uploaded_doc_path)) > 10 THEN uploaded_doc_path else '---'  end as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on";
					System.out.println("ecourts activities SQL:" + sql);
					data = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("ACTIVITIESDATA", data);
					 

					request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
					
					
					
					//request.setAttribute("OLCMSCASEDATA", data);
					String	sql_set = "SELECT cino, case when length(petition_document) > 0 then petition_document else null end as petition_document, "
							+ " case when length(counter_filed_document) > 0 then counter_filed_document else null end as counter_filed_document,"
							+ " case when length(judgement_order) > 0 then judgement_order else null end as judgement_order,"
							+ " case when length(action_taken_order) > 0 then action_taken_order else null end as action_taken_order,"
							+ " last_updated_by, last_updated_on, counter_filed, remarks, ecourts_case_status, corresponding_gp, "
							+ " pwr_uploaded, to_char(pwr_submitted_date,'mm/dd/yyyy') as pwr_submitted_date, to_char(pwr_received_date,'mm/dd/yyyy') as pwr_received_date, "
							+ " pwr_approved_gp, to_char(pwr_gp_approved_date,'mm/dd/yyyy') as pwr_gp_approved_date, appeal_filed, "
							+ " appeal_filed_copy, to_char(appeal_filed_date,'mm/dd/yyyy') as appeal_filed_date, pwr_uploaded_copy "
							+ " FROM apolcms.ecourts_olcms_case_details where cino='" + cIno + "' and respondent_slno='"+cIno1[1]+"' ";
					
					System.out.println("sql_set-------"+sql_set);
					
					List<Map<String, Object>> data_set = DatabasePlugin.executeQuery(sql_set, con);
					request.setAttribute("OLCMSCASEDATA", data_set);

					if (data_set != null) {
						
						Map caseData2 = (Map)data_set.get(0);
						
						//cform.setDynaForm("petitionDocumentOld" , CommonModels.checkStringObject(caseData1.get("petition_document")));
						cform.setDynaForm("counterFileCopyOld" , CommonModels.checkStringObject(caseData2.get("counter_filed_document")));
						cform.setDynaForm("judgementOrderOld" , CommonModels.checkStringObject(caseData2.get("judgement_order")));
						cform.setDynaForm("actionTakenOrderOld" , CommonModels.checkStringObject(caseData2.get("action_taken_order")));
						//cform.setDynaForm("" , caseData1.get("last_updated_by"));
						//cform.setDynaForm("" , caseData1.get("last_updated_on"));
						cform.setDynaForm("counterFiled" , caseData2.get("counter_filed"));
						cform.setDynaForm("remarks" , caseData2.get("remarks"));
						cform.setDynaForm("ecourtsCaseStatus" , caseData2.get("ecourts_case_status"));
						// cform.setDynaForm("relatedGp" , caseData1.get("corresponding_gp"));
						cform.setDynaForm("parawiseRemarksSubmitted" , caseData2.get("pwr_uploaded"));
						cform.setDynaForm("parawiseRemarksCopyOld" , CommonModels.checkStringObject(caseData2.get("pwr_uploaded_copy")));
						cform.setDynaForm("parawiseRemarksDt" , caseData2.get("pwr_submitted_date"));
						cform.setDynaForm("dtPRReceiptToGP" , caseData2.get("pwr_received_date"));
						cform.setDynaForm("pwr_gp_approved" , caseData2.get("pwr_approved_gp"));
						cform.setDynaForm("dtPRApprovedToGP" , caseData2.get("pwr_gp_approved_date"));
						cform.setDynaForm("appealFiled" , caseData2.get("appeal_filed"));
						cform.setDynaForm("appealFileCopyOld" , CommonModels.checkStringObject(caseData2.get("appeal_filed_copy")));
						cform.setDynaForm("appealFiledDt" , caseData2.get("appeal_filed_date"));
						cform.setDynaForm("actionToPerform" , caseData2.get("action_to_perfom"));
						
						request.setAttribute("STATUSUPDATEBTN", "STATUSUPDATEBTN");
						
					}	
					
				}				
				request.setAttribute("HEADING", "Update Status for Case :" + cIno);
			} else {
				request.setAttribute("errorMsg", "Invalid Cino. / No Records Found to display.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("fileCino", cIno);
			cform.setDynaForm("resident_id",(cIno1[1]));
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("caseStatusUpdationEmpl");
	}
	public ActionForward updateCaseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, deptId = null, deptCode = null, sql = null, empId = null, empSection = null, empPost = null, cIno = null;
		String resident_id=null;
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
			System.out.println("updateCaseDetails---");
			
			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
System.out.println("resident_id--"+resident_id);
			System.out.println("updateCaseDetails---"+cIno);

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

						sql="insert into ecourts_olcms_case_details_log (cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  , "
								+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
								+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated)"
								+ " select cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  ,"
								+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
								+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated from ecourts_olcms_case_details where cino='"+cIno+"'";
						DatabasePlugin.executeUpdate(sql, con);

						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', appeal_filed='"
								+ cform.getDynaForm("appealFiled") + "',appeal_filed_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt")) + "','mm/dd/yyyy'), remarks='"
								+ remarks + "', last_updated_by='" + userId
								+ "', last_updated_on=now(), action_to_perfom='"+cform.getDynaForm("actionToPerform")
								+"' " + updateSql + " where cino='" + cIno + "' and respondent_slno='"+resident_id+"' ";
					}
					else {

						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, petition_document,counter_filed_document,pwr_uploaded_copy, appeal_filed, appeal_filed_copy, judgement_order, action_taken_order"
								+ ", last_updated_by, last_updated_on, remarks, appeal_filed_date, action_to_perfom,respondent_slno) "
								+ " values ('" + cIno + "', '" 
								+ cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ petition_document + "','" 
								+ counter_filed_document + "','" 
								+ pwr_uploaded_copy + "','" 
								+ cform.getDynaForm("appealFiled") + "', '"
								+ appeal_filed_copy + "', '" 
								+ judgement_order + "', '" 
								+ action_taken_order + "', '" 
								+ userId + "', now(),'" 
								+ remarks + "',to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt"))+"','mm/dd/yyyy'),"
										+ " '"+cform.getDynaForm("actionToPerform")+"','"+resident_id+"')";
					}

					System.out.println("SQL:"+sql);

					int a = DatabasePlugin.executeUpdate(sql, con);

					sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"',section_officer_updated='T' "
							+ " where ack_no='"+cIno+"' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'  ";
					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("update SQL:"+sql);
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a-----------------"+a);
					System.out.println("SQL:"+sql);
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Ack No :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Ack No :" + cIno);
					}
				}
				else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("Pending")){


					if(cform.getDynaForm("counterFiled")!=null && cform.getDynaForm("counterFiled").toString().equals("Yes") 
							&& cform.getDynaForm("counterFileCopy")!=null  && !cform.getDynaForm("counterFileCopy").toString().equals("")) {
						myDoc = (FormFile)cform.getDynaForm("counterFileCopy");
						filePath="uploads/counters/";
						newFileName="counter_"+CommonModels.randomTransactionNo();
						counter_filed_document = fuu.saveFile(myDoc, filePath, newFileName);
						System.out.println("filePath---"+filePath);
						System.out.println("---"+counter_filed_document);
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
						
						System.out.println("---"+pwr_uploaded_copy);

						updateSql += ", pwr_uploaded_copy='"+pwr_uploaded_copy+"'";

						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
								+ "values ('" + cIno + "','Uploaded Parawise Remarks','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"', '"+pwr_uploaded_copy+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}


					if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from ecourts_olcms_case_details where cino='"+cIno+"'")) > 0) {

						//sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
						sql="insert into ecourts_olcms_case_details_log (cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  , "
								+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
								+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated)"
								+ " select cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  ,"
								+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
								+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated from ecourts_olcms_case_details where cino='"+cIno+"'";
						

						DatabasePlugin.executeUpdate(sql, con);

						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', counter_filed='"
								+ cform.getDynaForm("counterFiled") + "', remarks='" + remarks
								+ "', last_updated_by='" + userId + "', last_updated_on=now() " + updateSql
								+ ", corresponding_gp='" + cform.getDynaForm("relatedGp") + "', pwr_uploaded='"
								+ cform.getDynaForm("parawiseRemarksSubmitted") + "', pwr_submitted_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt"))
								+ "','mm/dd/yyyy'), pwr_received_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','mm/dd/yyyy'),pwr_approved_gp='"
								+ cform.getDynaForm("pwr_gp_approved") + "',"
								+ " pwr_gp_approved_date=to_date('" + CommonModels.checkStringObject(cform.getDynaForm("dtPRApprovedToGP"))
								+ "','mm/dd/yyyy'), action_to_perfom='"+cform.getDynaForm("actionToPerform")  + "' "
										+ " where cino='" + cIno + "' and respondent_slno='"+resident_id+"'";

					}
					else {

						sql = "insert into ecourts_olcms_case_details (cino, ecourts_case_status, petition_document, counter_filed_document, last_updated_by, last_updated_on, "
								+ "counter_filed, remarks,  corresponding_gp, pwr_uploaded, pwr_submitted_date, pwr_received_date, pwr_approved_gp, "
								+ "pwr_gp_approved_date, pwr_uploaded_copy, action_to_perfom,respondent_slno) "
								+ " values ('" + cIno + "', '" + cform.getDynaForm("ecourtsCaseStatus") + "', '"
								+ petition_document + "','" + counter_filed_document + "','" + userId + "', now(),'"
								+ cform.getDynaForm("counterFiled") + "', '" + remarks + "', '"

								+ cform.getDynaForm("relatedGp") + "', '"
								+ cform.getDynaForm("parawiseRemarksSubmitted") + "'," 

								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','mm/dd/yyyy'), " 
								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','mm/dd/yyyy'), '"
								+ cform.getDynaForm("pwr_gp_approved") + "'," 

								+ " to_date('" + cform.getDynaForm("dtPRApprovedToGP") + "','mm/dd/yyyy'), '" 
								+ pwr_uploaded_copy + "','"+cform.getDynaForm("actionToPerform")+"','"+resident_id+"')";

					}
					System.out.println("SQL:"+sql);

					int a = DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a-----2------------"+a);
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', mlo_no_updated='T' "
								+ " where ack_no='"+cIno+"' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'  ";
						a += DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a-----3------------"+a);
					}
					
					else {
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', section_officer_updated='T' "
								+ " where ack_no='"+cIno+"' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					System.out.println("update-----4------------"+sql);
					System.out.println("a-sql-----------"+a);
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a----5-------------"+a);
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Ack No :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Ack No :" + cIno);
					}

				}else if(cform.getDynaForm("ecourtsCaseStatus")!=null && cform.getDynaForm("ecourtsCaseStatus").toString().equals("Private")){
					
					int a=0;
					
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', case_status='98' "
								+ " where ack_no='"+cIno+"' and dept_code='"+deptCode+"'  "; //and respondent_slno='"+resident_id+"' 
						 a = DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a-----3------------"+a);
					}
					
					else {
						
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', case_status='98' "
								+ " where ack_no='"+cIno+"' and dept_code='"+deptCode+"' "; //and respondent_slno='"+resident_id+"'
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					
					System.out.println("update-----4------------"+sql);
					System.out.println("a-sql-----------"+a);
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					System.out.println("a----5-------------"+a);
					
					if (a > 0) {
						request.setAttribute("successMsg", "Case details updated successfully for Ack No :" + cIno);
						con.commit();
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error while updating the case details for Ack No :" + cIno);
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
			cform.setDynaForm("fileCino", cIno+"@"+cform.getDynaForm("resident_id"));
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
		String resident_id=null;
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
			
			resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
			
			System.out.println("resident_id--"+resident_id);
			
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
					msg = "Case details ("+cIno+") forwarded successfully to Sect. Department.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='2' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"' ";
					
				}
				else if(roleId!=null && roleId.equals("5")) {//FROM NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='4'  and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"' ";
					
				}
				else if(roleId!=null && roleId.equals("10")) {//FROM Dist-NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='8'  and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'";
					
				}
				
				else if((roleId!=null && roleId.equals("8")) && (deptCode.substring(3, 5)=="01" || deptCode.substring(3, 5).equals("01"))) { //FROM SECTION-SECT. TO MLO
					fwdOfficer = DatabasePlugin.selectString("select trim(emailid) from mlo_details where user_id='"+deptCode+"'", con);
					newStatus = "2";
					msg = "Case details ("+cIno+") forwarded successfully to MLO.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='5'  and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"' ";
				} 
				else if(roleId!=null && roleId.equals("11")){// FROM SECTION(HOD) TO NO-HOD
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and user_id not like '%DC-%'", con);
					newStatus = "4";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='9'  and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'";
					
				}
				else if(roleId!=null && roleId.equals("12")){// FROM SECTION(DIST) TO NO-HOD-DIST
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"", con);
					newStatus = "8";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='10' and dept_code='"+deptCode+"'  and respondent_slno='"+resident_id+"'    ";
					
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
		String  resident_id=null;
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
			
    resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
			
			System.out.println("resident_id--"+resident_id);

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String newStatus = "", msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				System.out.println("deptCode::"+deptCode.substring(3, 5));
				
				sql="select emp_user_id from ecourts_case_emp_assigned_dtls ad inner join ecourts_case_activities ea on (ad.cino=ea.cino) "
						+ "where ea.cino='"+cIno+"' and ea.action_type ilike 'CASE ASSSIGNED%' and ea.inserted_by='"+userId+"'   ";
				
				String backToSectionUser = DatabasePlugin.getStringfromQuery(sql , con);
				
				if(roleId!=null && roleId.equals("4")) {//FROM MLO to Section.
					newStatus = "5";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='2'  and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'    ";
					
				}
				else if(roleId!=null && roleId.equals("5")) {//FROM NO TO Section
					newStatus = "9";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='4' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'   ";
					
				}
				else if(roleId!=null && roleId.equals("10")) {//FROM Dist-NO TO Dist-Section
					newStatus = "10";
					msg = "Case details ("+cIno+") returned back to section successfully";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+backToSectionUser+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='8' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'    ";
					
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
		String resident_id=null;
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

 resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
			
			System.out.println("resident_id--"+resident_id);
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
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='1' and dept_code='"+deptCode+"'  and respondent_slno='"+resident_id+"'  ";
					
				}
				else if(roleId!=null && roleId.equals("9")) {//FROM HOD TO GP
					newStatus = "6";
					msg = "Case details ("+cIno+") forwarded successfully to GP for Approval.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='3' and dept_code='"+deptCode+"' and respondent_slno='"+resident_id+"'   ";
					
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
		String resident_id=null;
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
			  resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
				
				System.out.println("resident_id--"+resident_id);

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String msg="";
			
			if (cIno != null && !cIno.equals("")) {
				// System.out.println("counterRemarks::" + cform.getDynaForm("ecourtsCaseStatus"));
				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				String actionPerformed="";
				actionPerformed = !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("actionToPerform")).equals("0") ?  cform.getDynaForm("actionToPerform").toString()+" Approved"  : "CASE DETAILS UPDATED";
				
				msg = "Case details ("+cIno+") updated successfully.";
				
				//sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"' and respondent_slno='"+resident_id+"'";
				
				sql="insert into ecourts_olcms_case_details_log (cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  , "
						+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
						+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated)"
						+ " select cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  ,"
						+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
						+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated "
						+ "from ecourts_olcms_case_details where cino='"+cIno+"'  and respondent_slno='"+resident_id+"' ";
				
				
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if(cform.getDynaForm("actionToPerform").toString().equals("Parawise Remarks")) {
					/*sql="update ecourts_case_data set pwr_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);*/
					
					sql="update ecourts_olcms_case_details set pwr_approved_gp='Yes',pwr_gp_approved_date=current_date  where cino='"+cIno+"' and respondent_slno='"+resident_id+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					msg = "Parawise Remarks Approved successfully for Case ("+cIno+").";
				}
				else if(cform.getDynaForm("actionToPerform").toString().equals("Counter Affidavit")) {
					/*sql="update ecourts_case_data set counter_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);*/
					
					msg = "Counter Affidavit Approved successfully for Case ("+cIno+").";
					
					sql="update ecourts_olcms_case_details set counter_approved_gp='T',counter_approved_date=current_date, counter_approved_by='"+userId+"' where cino='"+cIno+"' and respondent_slno='"+resident_id+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("Yes")) {
					/*sql="update ecourts_case_data set pwr_approved='T', counter_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					*/
					sql="update ecourts_olcms_case_details set counter_approved_gp='T',counter_approved_date=current_date, counter_approved_by='"+userId+"' where cino='"+cIno+"' and respondent_slno='"+resident_id+"' ";
					a += DatabasePlugin.executeUpdate(sql, con);
					msg = "Counter Affidavit Approved successfully for Case ("+cIno+").";
					
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("No") && CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksSubmitted")).equals("Yes")) {
					
					/*sql="update ecourts_case_data set pwr_approved='T' where cino='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					*/
					
					sql="update ecourts_olcms_case_details set pwr_approved_gp='Yes',pwr_gp_approved_date=current_date  where cino='"+cIno+"' and respondent_slno='"+resident_id+"' ";
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
		String resident_id=null;
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
			
 resident_id=CommonModels.checkStringObject(cform.getDynaForm("resident_id"));
			
			System.out.println("resident_id--"+resident_id);

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
				sql="select dept_code,dist_id from ecourts_gpo_ack_depts where ack_no='"+cIno+"' and respondent_slno='"+resident_id+"'";
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
						sql="select inserted_by from ecourts_case_activities where cino='"+cIno+"'  and action_type='CASE FORWARDED' and assigned_to in (select emailid from mlo_details where user_id='"+deptCodeC+"') order by inserted_on desc limit 1";
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
					sql="update ecourts_gpo_ack_depts set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' "
							+ ",section_officer_updated=null, mlo_no_updated=null where ack_no='"+cIno+"' and respondent_slno='"+resident_id+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Parawise Remarks Returned for Case ("+cIno+").";
				}
				else if(cform.getDynaForm("actionToPerform").toString().equals("Counter Affidavit")) {
					// counter_approved='F',
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+",assigned_to='"+assigned2Emp+"',section_officer_updated=null , mlo_no_updated=null "
							+ "where ack_no='"+cIno+"' and respondent_slno='"+resident_id+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Counter Affidavit Returned for Case ("+cIno+").";
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("Yes")) {
					//pwr_approved='F', counter_approved='F',
					sql="update ecourts_gpo_ack_depts set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' where ack_no='"+cIno+"' and respondent_slno='"+resident_id+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					//msg = "Counter Affidavit Returned for Case ("+cIno+").";
					
					sql="update ecourts_olcms_case_details set counter_approved_gp='F' where cino='"+cIno+"' and respondent_slno='"+resident_id+"' ";
					a += DatabasePlugin.executeUpdate(sql, con);
					
				}
				else if (CommonModels.checkStringObject(cform.getDynaForm("counterFiled")).equals("No") && CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksSubmitted")).equals("Yes")) {
					//pwr_approved='F',
					sql="update ecourts_gpo_ack_depts set  case_status="+newStatus+", assigned_to='"+assigned2Emp+"' where ack_no='"+cIno+"' and respondent_slno='"+resident_id+"' and section_officer_updated='T' and mlo_no_updated='T' ";
					System.out.println("SQL:"+sql);
					a = DatabasePlugin.executeUpdate(sql, con);
					
					//msg = "Parawise Remarks Returned for Case ("+cIno+").";
				}
				
				//sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"' and respondent_slno='"+resident_id+"' ";
				
				sql="insert into ecourts_olcms_case_details_log (cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  , "
						+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
						+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , final_order_status  ,no_district_updated)"
						+ " select cino ,  petition_document   ,    counter_filed_document  ,    judgement_order  , action_taken_order  ,last_updated_by ,  last_updated_on ,  counter_filed , remarks  ,"
						+ "ecourts_case_status ,  corresponding_gp, pwr_uploaded , pwr_submitted_date, pwr_received_date , pwr_approved_gp ,  pwr_gp_approved_date , appeal_filed ,  appeal_filed_copy, appeal_filed_date, pwr_uploaded_copy, "
						+ " counter_approved_gp ,action_to_perfom, counter_approved_date ,counter_approved_by , respondent_slno, cordered_impl_date , dismissed_copy , "
						+ "final_order_status  ,no_district_updated from ecourts_olcms_case_details where cino='"+cIno+"' and respondent_slno='"+resident_id+"' ";
				
				
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
