package in.apcfss.struts.reports;

import java.io.PrintWriter;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.SendSMSAction;
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

			if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2"))) {
				sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='"+deptCode+"') ";
			}

			if (roleId.equals("2")) {
				sqlCondition += " and a.distid='" + distCode + "' ";
				cform.setDynaForm("districtId", distCode);
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

			sql = "select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs "
					+ "from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no and respondent_slno=1) "
					+ "inner join district_mst dm on (a.distid=dm.district_id) "
					+ "inner join dept_new dmt on (ad.dept_code=dmt.dept_code)"
					+ "inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ "where a.delete_status is false and ack_type='NEW' " + sqlCondition
					+ "order by inserted_time desc";

			System.out.println("CASES SQL:" + sql);

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
			System.out.println("caseStatusUpdate--");

			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			System.out.println("cIno--"+cIno);

			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();

				//sql = "select * from ecourts_gpo_ack_dtls where ack_no='" + cIno + "'";//user list
				
				
				

					
					
				
				

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

				if (!(roleId.equals("1") || roleId.equals("7") || roleId.equals("2"))) {
					sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='"+deptCode+"') ";
				}

				if (roleId.equals("2")) {
					sqlCondition += " and a.distid='" + distCode + "' ";
					cform.setDynaForm("districtId", distCode);
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

				sql = "select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
						+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs,ad.assigned,ad.assigned_to,ad.case_status,ad.ecourts_case_status,ad.section_officer_updated,ad.mlo_no_updated  "
						+ "from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no and respondent_slno=1) "
						+ "inner join district_mst dm on (a.distid=dm.district_id) "
						+ "inner join dept_new dmt on (ad.dept_code=dmt.dept_code)"
						+ "inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
						+ "where a.delete_status is false and ack_type='NEW' " + sqlCondition
						+ "order by inserted_time desc";

				System.out.println("CASES SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("USERSLIST", data);
				} else {
					request.setAttribute("errorMsg", "No details found.");
				}

				System.out.println("sql-------"+sql);
				System.out.println("data-------"+data);
				
				System.out.println("roleId-------"+roleId);
				System.out.println("deptCode-------"+deptCode);
				
				

				//request.setAttribute("OLCMSCASEDATA", data);

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
	
						sql="select emailid, first_name||' '||last_name||' - '||designation from ecourts_mst_gps order by emailid";
						cform.setDynaForm("GPSLIST", DatabasePlugin.getSelectBox(sql, con));
						request.setAttribute("SHOWGPBTN", "SHOWGPBTN");
					}
					else if(roleId.equals("6") ) { // GP LOGIN
						request.setAttribute("SHOWGPAPPROVEBTN", "SHOWGPAPPROVEBTN");
					}
					 

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
		return mapping.findForward("caseStatusUpdationEmpl");
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
			System.out.println("updateCaseDetails---");
			cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));

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
				/*
				 * if(cform.getDynaForm("petitionDocument")!=null &&
				 * !cform.getDynaForm("petitionDocument").toString().equals("")) { myDoc =
				 * (FormFile)cform.getDynaForm("petitionDocument");
				 * filePath="uploads/petitions/";
				 * newFileName="petition_"+CommonModels.randomTransactionNo(); petition_document
				 * = fuu.saveFile(myDoc, filePath, newFileName);
				 * 
				 * updateSql += ", petition_document='"+petition_document+"'";
				 * 
				 * sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks, uploaded_doc_path ) "
				 * + "values ('" + cIno +
				 * "','Uploaded Petition','"+userId+"', '"+request.getRemoteAddr()+"', '"
				 * +remarks+"', '"+petition_document+"')"; DatabasePlugin.executeUpdate(sql,
				 * con); }
				 */


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

						sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";
						DatabasePlugin.executeUpdate(sql, con);

						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', appeal_filed='"
								+ cform.getDynaForm("appealFiled") + "',appeal_filed_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt")) + "','dd/mm/yyyy'), remarks='"
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
								+ CommonModels.checkStringObject(cform.getDynaForm("appealFiledDt"))+"','dd/mm/yyyy'),'"+cform.getDynaForm("actionToPerform")+"')";
					}

					System.out.println("SQL:"+sql);

					int a = DatabasePlugin.executeUpdate(sql, con);

					sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"',section_officer_updated='T' where ack_no='"+cIno+"'";
					a += DatabasePlugin.executeUpdate(sql, con);

					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a-----------------"+a);
					if (a == 3) {
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

						sql="insert into ecourts_olcms_case_details_log select * from ecourts_olcms_case_details where cino='"+cIno+"'";

						DatabasePlugin.executeUpdate(sql, con);

						sql = "update ecourts_olcms_case_details set ecourts_case_status='"
								+ cform.getDynaForm("ecourtsCaseStatus") + "', counter_filed='"
								+ cform.getDynaForm("counterFiled") + "', remarks='" + remarks
								+ "', last_updated_by='" + userId + "', last_updated_on=now() " + updateSql
								+ ", corresponding_gp='" + cform.getDynaForm("relatedGp") + "', pwr_uploaded='"
								+ cform.getDynaForm("parawiseRemarksSubmitted") + "', pwr_submitted_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt"))
								+ "','dd/mm/yyyy'), pwr_received_date=to_date('"
								+ CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','dd/mm/yyyy'),pwr_approved_gp='"
								+ cform.getDynaForm("pwr_gp_approved") + "',"
								+ " pwr_gp_approved_date=to_date('" + CommonModels.checkStringObject(cform.getDynaForm("dtPRApprovedToGP"))
								+ "','dd/mm/yyyy'), action_to_perfom='"+cform.getDynaForm("actionToPerform")  + "' where cino='" + cIno + "'";

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

								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','dd/mm/yyyy'), " 
								+ " to_date('" + CommonModels.checkStringObject(cform.getDynaForm("parawiseRemarksDt")) + "','dd/mm/yyyy'), '"
								+ cform.getDynaForm("pwr_gp_approved") + "'," 

								+ " to_date('" + cform.getDynaForm("dtPRApprovedToGP") + "','dd/mm/yyyy'), '" 
								+ pwr_uploaded_copy + "','"+cform.getDynaForm("actionToPerform")+"')";

					}
					System.out.println("SQL:"+sql);

					int a = DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a-----2------------"+a);
					if(roleId!=null && (roleId.equals("4") || roleId.equals("5") || roleId.equals("10"))) {//MLO / NO / Dist-NO
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', mlo_no_updated='T' where ack_no='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a-----3------------"+a);
					}
					
					else {
						sql="update ecourts_gpo_ack_depts set ecourts_case_status='"+cform.getDynaForm("ecourtsCaseStatus")+"', section_officer_updated='T' where ack_no='"+cIno+"'";
						a += DatabasePlugin.executeUpdate(sql, con);
					}
					System.out.println("a-----4------------"+a);
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks ) "
							+ "values ('" + cIno + "','"+actionPerformed+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+remarks+"')";
					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("a----5-------------"+a);
					if (a == 3) {
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
					msg = "Case details ("+cIno+") forwarded successfully to Sect. Department.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='2'";
					
				}
				else if(roleId!=null && roleId.equals("5")) {//FROM NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='4'";
					
				}
				else if(roleId!=null && roleId.equals("10")) {//FROM Dist-NO TO HOD
					fwdOfficer = deptCode;
					newStatus = "3";
					msg = "Case details ("+cIno+") forwarded successfully to HOD.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and mlo_no_updated='T' and case_status='8'";
					
				}
				
				
				
				else if((roleId!=null && roleId.equals("8")) && (deptCode.substring(3, 5)=="01" || deptCode.substring(3, 5).equals("01"))) { //FROM SECTION-SECT. TO MLO
					fwdOfficer = DatabasePlugin.selectString("select trim(emailid) from mlo_details where user_id='"+deptCode+"'", con);
					newStatus = "2";
					msg = "Case details ("+cIno+") forwarded successfully to MLO.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='5'";
				} 
				else if(roleId!=null && roleId.equals("11")){// FROM SECTION(HOD) TO NO-HOD
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and user_id not like '%DC-%'", con);
					newStatus = "4";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='9'";
					
				}
				else if(roleId!=null && roleId.equals("12")){// FROM SECTION(DIST) TO NO-HOD-DIST
					fwdOfficer = DatabasePlugin.selectString("select emailid from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"", con);
					newStatus = "8";
					msg = "Case details ("+cIno+") forwarded successfully to Nodal Officer.";
					
					sql="update ecourts_gpo_ack_depts set case_status="+newStatus+", assigned_to='"+fwdOfficer+"' where ack_no='"+cIno+"' and section_officer_updated='T' and case_status='10'";
					
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

}
