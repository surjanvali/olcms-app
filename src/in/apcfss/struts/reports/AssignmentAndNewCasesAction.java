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
import in.apcfss.struts.Utilities.CommonModels;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class AssignmentAndNewCasesAction extends DispatchAction {
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
					+ " where a.delete_status is false and coalesce(assigned,'f')='f' and ack_type='NEW' " //and respondent_slno=1 
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

	public ActionForward assign2DeptHOD(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;// , cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId = null;
		int a = 0;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			System.out.println("in assign2DeptHOD --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = (String) request.getSession().getAttribute("userid");
			String login_deptId = (String) request.getSession().getAttribute("dept_code");
			String[] ids_split=null;
			String selectedCaseIds = "";
			
			if (!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("")
					&& !CommonModels.checkStringObject(cform.getDynaForm("caseDept")).equals("0")) {
				for (String newCaseId : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds"))
						.split(",")) {
					
					String ids=newCaseId;
					 ids_split=ids.split("@");
					System.out.println("ids--"+ids_split[0]);
					System.out.println("ids--"+ids_split[1]);
					
					
					selectedCaseIds += "'" + ids_split[0] + "',";

					System.out.println("newCaseId::::::" + ids_split[0]);

					sql = "insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
							+ "values ('" + ids_split[0] + "','CASE ASSSIGNED','" + userId + "', '"
							+ request.getRemoteAddr() + "', '"
							+ CommonModels.checkStringObject(cform.getDynaForm("caseDept")) + "', null)";

					a += DatabasePlugin.executeUpdate(sql, con);
					System.out.println(a + ":ACTIVITIES SQL:" + sql);
				/*}

				if (selectedCaseIds != null && !selectedCaseIds.equals("")) {
					selectedCaseIds = selectedCaseIds.substring(0, selectedCaseIds.length() - 1);
				}*/

				sql = " INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
						+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
						+ "    FROM apolcms.ecourts_gpo_ack_depts  where ack_no in ('" + ids_split[0] + "') and respondent_slno='"+ids_split[1]+"' ";
				System.out.println("INSERT SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);

				
				String caseNewDept = CommonModels.checkStringObject(cform.getDynaForm("caseDept"));
				String newStatusCode="4";
					if (caseNewDept.contains("01")) { 
						newStatusCode="2";
					}
					else { 
						newStatusCode="4";
					}
				
				
				sql = "update ecourts_gpo_ack_depts set  dept_code='"
						+ CommonModels.checkStringObject(cform.getDynaForm("caseDept")) + "',case_status="+newStatusCode
						+ " where ack_no in ('" + ids_split[0] + "') and dept_code='" + login_deptId + "' and respondent_slno='"+ids_split[1]+"'  ";
				System.out.println("UPDATE SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				}
				con.commit();
				request.setAttribute("successMsg", "Case/Cases successfully moved to selected Department / HOD.");

			} else
				request.setAttribute("errorMsg", "Error : Case assignment failed .</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Case to Department/HOD. Kindly try again.");
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
		return showCaseWise(mapping, cform, request, response);
	}

	public ActionForward assignMultiCases2Section(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId = null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			userId = (String) request.getSession().getAttribute("userid");
			String login_deptId = (String) request.getSession().getAttribute("dept_code");
			String user_dist = (String) request.getSession().getAttribute("dist_id");
			System.out.println("user_dist---"+user_dist);
			
			
			String officerType = CommonModels.checkStringObject(cform.getDynaForm("officerType"));
			if (!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("")
					&& !CommonModels.checkStringObject(cform.getDynaForm("empDept")).equals("0")) {

				String distCode = CommonModels.checkStringObject(cform.getDynaForm("caseDist1"));
				String empDeptCode = CommonModels.checkStringObject(cform.getDynaForm("empDept"));

				String tableName = AjaxModels.getTableName(distCode, con);

				String emailId = DatabasePlugin.getStringfromQuery("select distinct trim(email) from " + tableName
						+ " where substring(global_org_name,1,5)='" + cform.getDynaForm("empDept")
						+ "' and trim(employee_identity)='" + cform.getDynaForm("empSection")
						+ "' and trim(post_name_en)='" + cform.getDynaForm("empPost") + "' and trim(employee_id)='"
						+ cform.getDynaForm("employeeId") + "' and email is not null ", con);
			 System.out.println("emailId:"+emailId);
				for (String cIno : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					
					String ids=cIno;
					String[] ids_split=ids.split("@");
					System.out.println("ids--"+ids_split[0]);
					System.out.println("ids--"+ids_split[1]);
					
					if (ids_split[0] != null && !ids_split[0].equals("")) {
						sql = "insert into ecourts_ack_assignment_dtls (ackno, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by, emp_user_id) values (?, ?, ?, ?, ?, now(), ?, ?, ?)";
					 System.out.println("INSERT SQL:"+sql);
						ps = con.prepareStatement(sql);
						int i = 0;

						ps.setString(++i, ids_split[0]);
						ps.setString(++i, (String) cform.getDynaForm("empDept"));
						ps.setString(++i, (String) cform.getDynaForm("empSection"));
						ps.setString(++i, (String) cform.getDynaForm("empPost"));
						ps.setString(++i, (String) cform.getDynaForm("employeeId"));
						ps.setString(++i, request.getRemoteAddr());
						ps.setString(++i, userId);
						ps.setString(++i, emailId);

						int a = ps.executeUpdate();

						String newStatusCode = "0", activityDesc = "";
						if (Integer.parseInt(distCode) > 0) { // Dist. - Section Officer
							newStatusCode = "10";
							activityDesc = "CASE ASSSIGNED TO Section Officer (District)";
						} else if (empDeptCode.contains("01")) { // Sect. Dept. - Section Officer
							newStatusCode = "5";
							activityDesc = "CASE ASSSIGNED TO Section Officer (Sect. Dept.)";
						} else { // HOD - Section Officer.
							newStatusCode = "9";
							activityDesc = "CASE ASSSIGNED TO Section Officer (HOD)";
						}
						System.out.println("newStatusCode--"+newStatusCode);

						sql = " INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
								+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
								+ "    FROM apolcms.ecourts_gpo_ack_depts where ack_no='" + ids_split[0] + "'";

						System.out.println("INSERT SQL:" + sql);
						a += DatabasePlugin.executeUpdate(sql, con);

						System.out.println("officerType--"+officerType);
						
						if (officerType.equals("DC") || officerType.equals("DC-NO") || officerType.equals("DC-SO")) {
						sql = "update ecourts_gpo_ack_depts set dept_code='" + empDeptCode
								+ "', assigned=true, assigned_to='" + emailId + "',case_status=" + 
								newStatusCode + ", dist_id=" + distCode + " where ack_no='" + ids_split[0] + "' and dist_id=" + user_dist + " and respondent_slno='"+ids_split[1]+"' "; //and dept_code='"+ login_deptId + "'   and respondent_slno='1'
						}else {
						
						sql = "update ecourts_gpo_ack_depts set dept_code='" + empDeptCode
								+ "', assigned=true, assigned_to='" + emailId + "',case_status=" + 
								newStatusCode + ", dist_id=" + distCode + " where ack_no='" + ids_split[0] + "'  and dept_code='"+ login_deptId +"' and respondent_slno='"+ids_split[1]+"' ";   //and respondent_slno='1'
						}
						
						System.out.println("UPDATE SQL:" + sql);
						a += DatabasePlugin.executeUpdate(sql, con);

						sql = "insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + ids_split[0] + "','" + activityDesc + "','" + userId + "', '"
								+ request.getRemoteAddr() + "', '" + (String) cform.getDynaForm("employeeId") + "', '"
								+ (String) cform.getDynaForm("caseRemarks") + "','" + distCode + "')";

						a += DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a:----" + a);

						if (a > 0) {
							request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
						} else {
							request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again--.");
						}
					}
				}

				int b = 0;
				if (Integer.parseInt(DatabasePlugin.getStringfromQuery(
						"select count(*) from users where trim(userid)='" + emailId.trim() + "'", con)) > 0) {
					con.commit();
					request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
				} else {

					String newRoleId = "8";
					if (Integer.parseInt(distCode) > 0) { // Dist. - Section Officer
						newRoleId = "12";
					} else if (CommonModels.checkStringObject(cform.getDynaForm("empDept")).contains("01")) { // Sect. Dept.- Section Officer
						newRoleId = "8";
					} else { // HOD - Section Officer.
						newRoleId = "11";
					}

					// NEW SECTION OFFICER CREATION
					sql = "insert into section_officer_details (emailid, dept_id,designation,employeeid,mobileno,aadharno,inserted_by,inserted_ip, dist_id) "
							+ "select distinct b.email,d.sdeptcode||d.deptcode,b.designation_id,b.employee_id,b.mobile1,uid, '"
							+ (String) session.getAttribute("userid") + "', '" + request.getRemoteAddr() + "'::inet,"
							+ distCode + " from " + tableName + " b inner join dept_new d on (d.dept_code='"
							+ cform.getDynaForm("empDept") + "')"
							+ " where b.employee_id='"+ cform.getDynaForm("employeeId") + "' and trim(b.employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(b.post_name_en)='"+cform.getDynaForm("empPost")+"'";
					//+ " where b.employee_id='" + cform.getDynaForm("employeeId") + "'";
					System.out.println("NEW SECTION OFFICER CREATION SQL:" + sql);
					b += DatabasePlugin.executeUpdate(sql, con);

					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, user_type, dist_id) "
							+ "select distinct b.email, md5('olcms@2021'), b.fullname_en, '"
							+ (String) session.getAttribute("userid") + "', now(),'" + request.getRemoteAddr()
							+ "'::inet, d.dept_id,d.dept_code," + newRoleId + "," + distCode + " from " + tableName
							+ " b inner join dept_new d on (d.dept_code='" + cform.getDynaForm("empDept")
							+ "')"
							+" where b.employee_id='"+ cform.getDynaForm("employeeId") + "' and trim(b.employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(b.post_name_en)='"+cform.getDynaForm("empPost")+"'";
							//+ " where b.employee_id='" + cform.getDynaForm("employeeId") + "' ";

					System.out.println("USER CREATION SQL:" + sql);

					b += DatabasePlugin.executeUpdate(sql, con);

					// sql = "select distinct mobile1 from " + tableName + " where employee_id='"+ cform.getDynaForm("employeeId") + "' and mobile1 is not null";
					sql="select distinct mobile1 from "+tableName+" b "
							+" where b.employee_id='"+ cform.getDynaForm("employeeId") + "' and trim(b.employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(b.post_name_en)='"+cform.getDynaForm("empPost")+"'"
							+ " and mobile1 is not null";
					
					System.out.println("MOBILE SQL:" + sql);
					String mobileNo = DatabasePlugin.getStringfromQuery(sql, con);

					sql = "insert into user_roles (userid, role_id) values ('" + emailId + "','" + newRoleId + "')";
					System.out.println("INSERT ROLE SQL:" + sql);
					b += DatabasePlugin.executeUpdate(sql, con);

					if (b == 3) {
						String smsText = "Your User Id is " + emailId
								+ " and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
						String templateId = "1007784197678878760";
						 //mobileNo = "8500909816";
						System.out.println(mobileNo + "" + smsText + "" + templateId);
						if (mobileNo != null && !mobileNo.equals("")) {
						// mobileNo = "8500909816";
							System.out.println("mobileNo::" + mobileNo);
							SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
						}
						con.commit();
						request.setAttribute("successMsg",
								"Cases successfully Assigned to Selected Employee & User Login created successfully. Login details sent to Registered Mobile No.");
					} else {
						con.rollback();
						request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again.");
					}
				}
			} else
				request.setAttribute("errorMsg", "Error : Invalid Data.</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again.");
			request.removeAttribute("successMsg");
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
		return showCaseWise(mapping, cform, request, response);
	}

	public ActionForward assign2DistCollector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;// , cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId = null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			userId = (String) request.getSession().getAttribute("userid");
			String login_deptId = (String) request.getSession().getAttribute("dept_code");
			String user_dist = (String) request.getSession().getAttribute("dist_id");
			// System.out.println(cform.getDynaForm("selectedCaseIds"));
			String[] ids_split=null;
		
			String selectedCaseIds = "";
			// ArrayList<String> sqls = new ArrayList<String>();
			String officerType = "";
			officerType = CommonModels.checkStringObject(cform.getDynaForm("officerType"));
			if (!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("")) {
				for (String newCaseId : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					
					String ids=newCaseId;
					 ids_split=ids.split("@");
					System.out.println("ids--"+ids_split[0]);
					System.out.println("ids--"+ids_split[1]);
					
					
					selectedCaseIds += "'" + ids_split[0] + "',";

					if (officerType.equals("DC")) {

						sql = "insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + ids_split[0] + "','CASE ASSSIGNED','" + userId + "', '"
								+ request.getRemoteAddr() + "', '"
								+ CommonModels.checkStringObject(cform.getDynaForm("caseDist")) + "', null,'"
								+ CommonModels.checkIntObject(cform.getDynaForm("caseDist")) + "')";
						DatabasePlugin.executeUpdate(sql, con);
					} else if (officerType.equals("DC-NO")) {

						sql = "insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + ids_split[0] + "','CASE ASSSIGNED','" + userId + "', '"
								+ request.getRemoteAddr() + "', '"
								+ CommonModels.checkStringObject(cform.getDynaForm("distDept")) + "', null, '"
								+ CommonModels.checkIntObject(cform.getDynaForm("caseDist")) + "')";
						DatabasePlugin.executeUpdate(sql, con);
					}
				

					/*
					 * if (selectedCaseIds != null && !selectedCaseIds.equals("")) { selectedCaseIds
					 * = selectedCaseIds.substring(0, selectedCaseIds.length() - 1); }
					 */
				
				
				String distDept=CommonModels.checkStringObject(cform.getDynaForm("caseDist"));
				System.out.println("+distDept--"+distDept);
				
				String successMsg = "";
				if (officerType.equals("DC")) {

					sql = " INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
							+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
							+ "    FROM apolcms.ecourts_gpo_ack_depts  where ack_no in ('" + ids_split[0] + "') "
									+ " and dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"' and respondent_slno='"+ids_split[1]+"' ";

					System.out.println("INSERT SQL:" + sql);
					DatabasePlugin.executeUpdate(sql, con);

					sql = "update ecourts_gpo_ack_depts set case_status=7, dist_id='"
							+ CommonModels.checkStringObject(cform.getDynaForm("caseDist")) + "',dept_code='"+cform.getDynaForm("distDept")+"' " // assigned=true,
							+ " where ack_no in ('" + ids_split[0] + "') and dist_id='"+user_dist+"'  and respondent_slno='"+ids_split[1]+"'  ";   //and and respondent_slno=1 
					System.out.println("UPDATE SQL:" + sql);
					DatabasePlugin.executeUpdate(sql, con);

					successMsg = "Case/Cases successfully moved to selected District Collector Login";

				} else if (officerType.equals("DC-NO")) {

					sql = " INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
							+ " (SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
							+ "    FROM apolcms.ecourts_gpo_ack_depts where ack_no in ('" + ids_split[0] + "') and respondent_slno='"+ids_split[1]+"') ";
					System.out.println("INSERT SQL:" + sql);

					DatabasePlugin.executeUpdate(sql, con);

					sql = "update ecourts_gpo_ack_depts set dept_code='"
							+ in.apcfss.struts.Utilities.CommonModels.checkStringObject(cform.getDynaForm("distDept")) + "',dist_id='"
							+ in.apcfss.struts.Utilities.CommonModels.checkIntObject(cform.getDynaForm("caseDist")) + "',case_status=8    "
							+ " where ack_no in ('" + ids_split[0] + "')  and dist_id='"+user_dist+"' and respondent_slno='"+ids_split[1]+"' ";  //and dept_code='" + login_deptId + "' and respondent_slno='1' 
					DatabasePlugin.executeUpdate(sql, con);
					successMsg = "Case/Cases successfully moved to selected District Nodal Officer Login";
				}
				
				System.out.println("UPDATE SQL:" + sql);
				con.commit();
				request.setAttribute("successMsg", successMsg);
				}
				
			} else
				request.setAttribute("errorMsg", "Error : Case assignment failed .</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg",
					"Error in assigning Case to District Collector/ Nodal Officer. Kindly try again.");
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
		return showCaseWise(mapping, cform, request, response);
	}
}