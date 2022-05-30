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
import org.apache.struts.util.LabelValueBean;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
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
	
	
	
	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="";
		try {
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			con = DatabasePlugin.connect();
			// cform.setDynaForm("designationList", DatabasePlugin.getSelectBox("select
			// distinct designation_id::int4, designation_name_en from nic_data where
			// substring(global_org_name,1,5)='" + session.getAttribute("userid") + "' and
			// trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4
			// desc ", con));
			
			String src = CommonModels.checkStringObject(request.getParameter("src"));
			
			if(!src.equals("dashBoard")) {
				if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and date_of_filing >= to_date('" + cform.getDynaForm("dofFromDate")
							+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and date_of_filing <= to_date('" + cform.getDynaForm("dofToDate")
							+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("purpose") != null && !cform.getDynaForm("purpose").toString().contentEquals("")
						&& !cform.getDynaForm("purpose").toString().contentEquals("0")) {
					sqlCondition += " and trim(purpose_name)='" + cform.getDynaForm("purpose").toString().trim() + "' ";
				}
				if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and trim(dist_name)='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
			
			
			if(CommonModels.checkStringObject(cform.getDynaForm("ShowDefault")).equals("ShowDefault")) {
				sqlCondition += " and reg_year in ('2021','2022') ";
				//heading
				cform.setDynaForm("regYear", "2022");
			}
			else if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			
			
			if (CommonModels.checkIntObject(cform.getDynaForm("filingYear")) > 0) {
				sqlCondition += " and fil_year='" + CommonModels.checkIntObject(cform.getDynaForm("filingYear")) + "' ";
			}
			
			}
			
			
			if(!roleId.equals("2")) { //District Nodal Officer
				sqlCondition +=" and dept_code='" + deptCode + "' ";
			}
			
			if(roleId.equals("2")) { //District Collector
				distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
				sqlCondition +=" and case_status=7 and dist_id='"+distId+"'";
			}
			else if(roleId.equals("10")) { //District Nodal Officer
				sqlCondition +=" and case_status=8";
			}
			else if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
				sqlCondition +=" and case_status in (3,4)";
			}
			else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
				sqlCondition +=" and (case_status is null or case_status in (1, 2))";
			}
			
			sql = "select a.*, b.orderpaths from ecourts_case_data a left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
					+ " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) where coalesce(assigned,'f')='f' "
					+ sqlCondition
					+ " and coalesce(ecourts_case_status,'')!='Closed'";

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				
				sql = "select trim(employee_identity),trim(employee_identity) from nic_data where substr(trim(global_org_name),1,5)='"+deptCode+"' and trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				// sql="select trim(employee_identity),trim(employee_identity) from nic_data where trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				System.out.println("EMP SEC-SQL:"+sql);
				cform.setDynaForm("empSectionList", DatabasePlugin.getSelectBox( sql,con));
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");
			
			cform.setDynaForm("purposeList",
					DatabasePlugin
							.getSelectBox(
									"select purpose_name,purpose_name from apolcms.ecourts_case_data where dept_code='"
											+ session.getAttribute("dept_code") + "' group by purpose_name order by 1",
									con));
			cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select trim(dist_name),trim(dist_name) from apolcms.ecourts_case_data where trim(dist_name)!='null' group by trim(dist_name) order by 1",
					con));
			
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			
			cform.setDynaForm("DCLIST", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by 1",
					con));
			
			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);
			
			request.setAttribute("deptCode", deptCode);
			
			cform.setDynaForm("currentDeptId", deptCode);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}

	/*
	public ActionForward assignCase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			userId = (String)request.getSession().getAttribute("userid");
			
			cIno = cform.getDynaForm("cINO") != null ? cform.getDynaForm("cINO").toString() : "";
			
			if (cIno != null && !cIno.equals("")) {
				sql = "insert into ecourts_ack_assignment_dtls (ackno, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by, emp_user_id) values (?, ?, ?, ?, ?, now(), ?, ?, ?)";
				ps = con.prepareStatement(sql);
				int i = 0;

				ps.setString(++i, cIno);
				ps.setString(++i, (String) cform.getDynaForm("empDept"));
				ps.setString(++i, (String) cform.getDynaForm("empSection"));
				ps.setString(++i, (String) cform.getDynaForm("empPost"));
				ps.setString(++i, (String) cform.getDynaForm("employeeId"));
				ps.setString(++i, request.getRemoteAddr());
				
				String emailId = DatabasePlugin.getStringfromQuery(
						"select distinct trim(email) from nic_data where substring(global_org_name,1,5)='"
								+ cform.getDynaForm("empDept") + "' and trim(employee_identity)='"
								+ cform.getDynaForm("empSection") + "' and trim(post_name_en)='"
								+ cform.getDynaForm("empPost") + "' and trim(employee_id)='"
								+ cform.getDynaForm("employeeId") + "' and email is not null ",
						con);

				ps.setString(++i, userId);
				ps.setString(++i, emailId);

				int a = ps.executeUpdate();

				sql = "update ecourts_case_data set assigned=true, assigned_to='"+emailId+"',case_status=5 where cino='" + cIno + "' ";
				a+= DatabasePlugin.executeUpdate(sql, con);

				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
						+ "values ('" + cIno + "','CASE ASSSIGNED','"+userId+"', '"+request.getRemoteAddr()+"', '"+(String) cform.getDynaForm("employeeId")+"', null)";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if (a==3) {
					//request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
					// Create Login to Employee To access assigned Cases.
					// sql="select distinct emailid from nic_data where employee_id='"+cform.getDynaForm("employeeId")+"' and emailid is not null";
					//sql="select email from nic_data where employee_id='"+cform.getDynaForm("employeeId")+"' and employee_identity='"+cform.getDynaForm("empSection")+"' and post_name_en='"+cform.getDynaForm("empPost")+"' and email is not null";
					//String emailId = DatabasePlugin.getStringfromQuery(sql, con);
					int b = 0;
					if(Integer.parseInt(DatabasePlugin.getStringfromQuery("select count(*) from users where userid='"+emailId+"'", con)) > 0 ){
						con.commit();
						request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
					}
					else{
						
						// NEW SECTION OFFICER CREATION
						sql = "insert into section_officer_details (emailid, dept_id,designation,employeeid,mobileno,aadharno,inserted_by,inserted_ip) "
								+ "select distinct b.email,d.sdeptcode||d.deptcode,b.designation_id,b.employee_id,b.mobile1,uid, '"
								+ (String) session.getAttribute("userid") + "', '" + request.getRemoteAddr()
								+ "'::inet from nic_data b inner join dept_new d on (d.dept_code='"
								+ cform.getDynaForm("empDept") + "') where b.employee_id='"+ cform.getDynaForm("employeeId") + "'";
						
						b+=DatabasePlugin.executeUpdate(sql, con);
						
						
						sql="insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code) " +
								"select distinct b.email, md5('olcms@2021'), b.fullname_en, '"+(String) session.getAttribute("userid")
								+"', now(),'"+request.getRemoteAddr() +"'::inet, d.dept_id,d.dept_code from nic_data b inner join dept_new d on (d.dept_code='"+cform.getDynaForm("empDept")
								+"') where b.employee_id='"+cform.getDynaForm("employeeId")+"' ";
						
						System.out.println("SQL:"+sql);
								//+ "select b.email, md5('olcms@2021'), b.fullname_en, '"+(String) session.getAttribute("userid")+"', now(),'"+request.getRemoteAddr()
								//+ "' from ecourts_ack_assignment_dtls a inner join (select distinct employee_id,fullname_en,email from nic_data) b on (a.emp_id=b.employee_id) where a.emp_id='"+cform.getDynaForm("employeeId")+"'";
						
						b+=DatabasePlugin.executeUpdate(sql, con);
						
						sql="select distinct mobile1 from nic_data where employee_id='"+cform.getDynaForm("employeeId")+"' and mobile1 is not null";
						String mobileNo = DatabasePlugin.getStringfromQuery(sql, con);
						
						sql="insert into user_roles (userid, role_id) values ('"+emailId+"','8')";
						//sql="insert into user_roles (userid, role_id) select distinct emailid,'8' from nic_data where employee_id='"+cform.getDynaForm("employeeId")+"')";
						b+=DatabasePlugin.executeUpdate(sql, con);

						if(b==3){	
							// String smsText="Your Userid on Finance Department Portal is:"+emailId+" and olcms@2021 is Password. Please do not share your Userid and Password with Others.  -APFINANCE";
							String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
							String templateId="1007784197678878760";
							
							System.out.println(mobileNo+""+smsText+""+templateId);
							if(mobileNo!=null && !mobileNo.equals("")) {
								// mobileNo = "9618048663";
								SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
							}
							con.commit();
							request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee & User Login created successfully. Login details sent to Registered Mobile No.");
						}
						else{
							con.rollback();
							request.setAttribute("errorMsg", "Error in assigning Case. Kindly try again.");
						}
					}
					
				} else{
					con.rollback();
					request.setAttribute("errorMsg", "Error in assigning Case. Kindly try again.");
				}
			} else
				request.setAttribute("errorMsg", "Error : Invalid CINO.</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Case. Kindly try again.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("assignCase2EmpPage");
	}
	*/
	
	public ActionForward assign2DeptHOD(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;int a=0;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			System.out.println("in assign2DeptHOD --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = (String)request.getSession().getAttribute("userid");
			String login_deptId = (String)request.getSession().getAttribute("dept_code");
			
			//System.out.println(cform.getDynaForm("selectedCaseIds"));
			String selectedCaseIds = "";
			// ArrayList<String> sqls = new ArrayList<String>();
			
			if(!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("caseDept")).equals("0")) {
				for (String newCaseId : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					selectedCaseIds+="'"+newCaseId+"',";
					
					System.out.println("newCaseId::::::"+newCaseId);
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
							+ "values ('" + newCaseId + "','CASE ASSSIGNED','"+userId+"', '"+request.getRemoteAddr()+"', '"+CommonModels.checkStringObject(cform.getDynaForm("caseDept"))+"', null)";
					
					a+=DatabasePlugin.executeUpdate(sql, con);
					System.out.println(a+":ACTIVITIES SQL:"+sql);
				}
				
				if(selectedCaseIds!=null && !selectedCaseIds.equals("")) {
					selectedCaseIds = selectedCaseIds.substring(0,selectedCaseIds.length()-1);
				}
				
				String assign2deptId = DatabasePlugin.getStringfromQuery("select dept_id from dept_new where dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("caseDept"))+"'", con);
				
				/*
				 * sql = "update ecourts_case_data set dept_id='"+assign2deptId+"',dept_code='"+
				 * CommonModels.checkStringObject(cform.getDynaForm("caseDept"))
				 * +"',case_status=3  where cino in (" + selectedCaseIds + ") ";
				 */
				
				sql=" INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
						+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
						+ "    FROM apolcms.ecourts_gpo_ack_depts  where ack_no in (" + selectedCaseIds +") ";
				System.out.println("INSERT SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql = "update ecourts_gpo_ack_depts set  dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("caseDept"))+"', assigned=true "
						//+ ", assigned_to='"+emailId+"',case_status="+newStatusCode+", dist_id="+distCode+" "
								+ " where ack_no in (" + selectedCaseIds + ") and dept_code='"+login_deptId+"' ";
				System.out.println("UPDATE SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				
				con.commit();
				request.setAttribute("successMsg", "Case/Cases successfully moved to selected Department / HOD.");
				
			} else
				request.setAttribute("errorMsg", "Error : Case assignment failed .</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Case to Department/HOD. Kindly try again.");
			e.printStackTrace();
		} finally {
			
			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");
			
			DatabasePlugin.close(con, ps, null);
		}
		//return mapping.findForward("assignCase2EmpPage");
		return getCasesList(mapping, cform, request, response);
	}
	
	public ActionForward assignMultiCases2Section(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			userId = (String)request.getSession().getAttribute("userid");
			String login_deptId = (String)request.getSession().getAttribute("dept_code");
			if(!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("") 
					&& !CommonModels.checkStringObject(cform.getDynaForm("empDept")).equals("0")) {
				
				String distCode = CommonModels.checkStringObject(cform.getDynaForm("caseDist1"));
				String empDeptCode = CommonModels.checkStringObject(cform.getDynaForm("empDept"));

				String tableName = AjaxModels.getTableName(distCode, con);
				
				String assign2deptId = DatabasePlugin.getStringfromQuery("select dept_id from dept_new where dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("empDept"))+"'", con);
				// System.out.println("assign2deptId::"+assign2deptId);
				String emailId = DatabasePlugin.getStringfromQuery("select distinct trim(email) from "+tableName+" where substring(global_org_name,1,5)='"+cform.getDynaForm("empDept")
						+"' and trim(employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(post_name_en)='"+cform.getDynaForm("empPost")+"' and trim(employee_id)='"+cform.getDynaForm("employeeId")+"' and email is not null ", con);
				// System.out.println("emailId:"+emailId);
				for (String cIno : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					if (cIno != null && !cIno.equals("")) {
						sql = "insert into ecourts_ack_assignment_dtls (ackno, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by, emp_user_id) values (?, ?, ?, ?, ?, now(), ?, ?, ?)";
						// System.out.println("INSERT SQL:"+sql);
						ps = con.prepareStatement(sql);
						int i = 0;

						ps.setString(++i, cIno);
						ps.setString(++i, (String) cform.getDynaForm("empDept"));
						ps.setString(++i, (String) cform.getDynaForm("empSection"));
						ps.setString(++i, (String) cform.getDynaForm("empPost"));
						ps.setString(++i, (String) cform.getDynaForm("employeeId"));
						ps.setString(++i, request.getRemoteAddr());
						ps.setString(++i, userId);
						ps.setString(++i, emailId);

						int a = ps.executeUpdate();
						
						String newStatusCode="0", activityDesc="";
						if(Integer.parseInt(distCode)>0) { // Dist. - Section Officer
							newStatusCode = "10";
							activityDesc = "CASE ASSSIGNED TO Section Officer (District)";
						}
						else if (empDeptCode.contains("01")) { // Sect. Dept. - Section Officer
							newStatusCode="5";
							activityDesc = "CASE ASSSIGNED TO Section Officer (Sect. Dept.)";
						}
						else { // HOD - Section Officer.
							newStatusCode="9";
							activityDesc = "CASE ASSSIGNED TO Section Officer (HOD)";
						}

						
						sql=" INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
								+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
								+ "    FROM apolcms.ecourts_gpo_ack_depts where ack_no='" + cIno + "'";

						System.out.println("INSERT SQL:"+sql);
						int b= DatabasePlugin.executeUpdate(sql, con);
						
						sql = "update ecourts_gpo_ack_depts set dept_code='"+empDeptCode+"', assigned=true, assigned_to='"+emailId+"',case_status="+   //dept_code='"+assign2deptId+"', 
								newStatusCode+", dist_id="+distCode+" where ack_no='" + cIno + "' and dept_code='"+login_deptId+"' ";
						System.out.println("UPDATE SQL:"+sql);
						a += DatabasePlugin.executeUpdate(sql, con);

						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + cIno + "','"+activityDesc+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+(String) cform.getDynaForm("employeeId")+"', '"+(String) cform.getDynaForm("caseRemarks")+"','"+distCode+"')";
						
						a += DatabasePlugin.executeUpdate(sql, con);
						System.out.println("a:----"+a);
						
						if(a==3) {
							request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
						}
						else{
							request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again--.");
						}
					}
				}


				int b = 0;
				if(Integer.parseInt(DatabasePlugin.getStringfromQuery("select count(*) from users where trim(userid)='"+emailId.trim()+"'", con)) > 0 ){
					con.commit();
					request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
				}
				else{
					
					String newRoleId="8";
					if(Integer.parseInt(distCode)>0) { // Dist. - Section Officer
						newRoleId = "12";
					}
					else if (CommonModels.checkStringObject(cform.getDynaForm("empDept")).contains("01")) { // Sect. Dept. - Section Officer
						newRoleId="8";
					}
					else { // HOD - Section Officer.
						newRoleId="11";
					}
					
					// NEW SECTION OFFICER CREATION
					sql = "insert into section_officer_details (emailid, dept_id,designation,employeeid,mobileno,aadharno,inserted_by,inserted_ip, dist_id) "
							+ "select distinct b.email,d.sdeptcode||d.deptcode,b.designation_id,b.employee_id,b.mobile1,uid, '"
							+ (String) session.getAttribute("userid") + "', '" + request.getRemoteAddr()
							+ "'::inet,"+distCode+" from "+tableName+" b inner join dept_new d on (d.dept_code='"
							+ cform.getDynaForm("empDept") + "') where b.employee_id='"+ cform.getDynaForm("employeeId") + "'";
					System.out.println("NEW SECTION OFFICER CREATION SQL:"+sql);
					b += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, user_type, dist_id) " +
							"select distinct b.email, md5('olcms@2021'), b.fullname_en, '"+(String) session.getAttribute("userid")
							+"', now(),'"+request.getRemoteAddr() +"'::inet, d.dept_id,d.dept_code,"+newRoleId+","+distCode+" from "+tableName+" b inner join dept_new d on (d.dept_code='"+cform.getDynaForm("empDept")
							+"') where b.employee_id='"+cform.getDynaForm("employeeId")+"' ";
					
					System.out.println("USER CREATION SQL:"+sql);
					
					b+=DatabasePlugin.executeUpdate(sql, con);
					
					sql="select distinct mobile1 from "+tableName+" where employee_id='"+cform.getDynaForm("employeeId")+"' and mobile1 is not null";
					System.out.println("MOBILE SQL:"+sql);
					String mobileNo = DatabasePlugin.getStringfromQuery(sql, con);
					
					sql="insert into user_roles (userid, role_id) values ('"+emailId+"','"+newRoleId+"')";
					System.out.println("INSERT ROLE SQL:"+sql);
					b+=DatabasePlugin.executeUpdate(sql, con);

					if(b==3){	
						// String smsText="Your Userid on Finance Department Portal is:"+emailId+" and olcms@2021 is Password. Please do not share your Userid and Password with Others.  -APFINANCE";
						String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
						String templateId="1007784197678878760";
						
						System.out.println(mobileNo+""+smsText+""+templateId);
						if(mobileNo!=null && !mobileNo.equals("")) {
							mobileNo = "8500909816";
							System.out.println("mobileNo::"+mobileNo);
							SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
						}
						con.commit();
						request.setAttribute("successMsg", "Cases successfully Assigned to Selected Employee & User Login created successfully. Login details sent to Registered Mobile No.");
					}
					else{
						con.rollback();
						request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again.");
					}
				}
			}else
				request.setAttribute("errorMsg", "Error : Invalid Data.</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again.");
			request.removeAttribute("successMsg");
			e.printStackTrace();
		} finally {
			
			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");
			
			DatabasePlugin.close(con, ps, null);
		}
		//return mapping.findForward("assignCase2EmpPage");
		return getCasesList(mapping, cform, request, response);
	}
	
	public ActionForward assign2DistCollector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			userId = (String)request.getSession().getAttribute("userid");
			String login_deptId = (String)request.getSession().getAttribute("dept_code");
			//System.out.println(cform.getDynaForm("selectedCaseIds"));
			String selectedCaseIds = "";
			// ArrayList<String> sqls = new ArrayList<String>();
			String officerType="";
			officerType = CommonModels.checkStringObject(cform.getDynaForm("officerType"));
			if(!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("")) {
				for (String newCaseId : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					selectedCaseIds+="'"+newCaseId+"',";
					
					if(officerType.equals("DC")) {
					
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + newCaseId + "','CASE ASSSIGNED','"+userId+"', '"+request.getRemoteAddr()+"', '"+CommonModels.checkStringObject(cform.getDynaForm("caseDist"))+"', null,'"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
					else if(officerType.equals("DC-NO")) {
						
						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + newCaseId + "','CASE ASSSIGNED','"+userId+"', '"+request.getRemoteAddr()+"', '"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"', null, '"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"')";
						DatabasePlugin.executeUpdate(sql, con);
					}
				}
				
				if(selectedCaseIds!=null && !selectedCaseIds.equals("")) {
					selectedCaseIds = selectedCaseIds.substring(0,selectedCaseIds.length()-1);
				}
				String successMsg="";
				String assign2deptId = DatabasePlugin.getStringfromQuery("select dept_id from dept_new where dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"'", con);
				
				if(officerType.equals("DC")) {
					
					//sql = "update ecourts_case_data set dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"', dept_id='"+assign2deptId+"', dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))
					//------------------- +"',dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))
					//+"',case_status=7  where cino in (" + selectedCaseIds + ") ";
					
					
					sql=" INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
							+ " SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
							+ "    FROM apolcms.ecourts_gpo_ack_depts  where ack_no in (" + selectedCaseIds + ") ";

					System.out.println("INSERT SQL:"+sql);
					//a += DatabasePlugin.executeUpdate(sql, con);
					
					sql = "update ecourts_gpo_ack_depts set dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"', dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"', assigned=true,case_status=7 "
							//+ " assigned_to='"+emailId+"',case_status="+newStatusCode+", dist_id="+distCode+" "
									+ " where ack_no in (" + selectedCaseIds + ") and dept_code='"+login_deptId+"' ";
					System.out.println("UPDATE SQL:"+sql);
				//	a += DatabasePlugin.executeUpdate(sql, con);
					
					
					
					successMsg = "Case/Cases successfully moved to selected District Collector Login";
					
				}
				else if(officerType.equals("DC-NO")) {
					
				//	sql = "update ecourts_case_data set dept_id='"+assign2deptId+"', dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"',dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))
				//	+"',case_status=8  where cino in (" + selectedCaseIds + ") ";
					
					
					sql=" INSERT INTO apolcms.ecourts_gpo_ack_depts_log(ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id) "
							+ " (SELECT ack_no, dept_code, respondent_slno, assigned, assigned_to, case_status, dist_id "
							+ "    FROM apolcms.ecourts_gpo_ack_depts where ack_no in (" + selectedCaseIds + ")) ";
					System.out.println("INSERT SQL:"+sql);
					
					sql = "update ecourts_gpo_ack_depts set dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"',dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"', assigned=true,case_status=8 "
							//+ " assigned_to='"+emailId+"',case_status="+newStatusCode+", dist_id="+distCode+" "
									+ " where ack_no in (" + selectedCaseIds + ") and dept_code='"+login_deptId+"' ";
					
					successMsg="Case/Cases successfully moved to selected District Nodal Officer Login";
				}
				
				
				System.out.println("UPDATE SQL:"+sql);
				
				DatabasePlugin.executeUpdate(sql, con);
				con.commit();
				request.setAttribute("successMsg", successMsg);
				
			} else
				request.setAttribute("errorMsg", "Error : Case assignment failed .</font>");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Case to Department/HOD. Kindly try again.");
			e.printStackTrace();
		} finally {
			
			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");
			
			DatabasePlugin.close(con, ps, null);
		}
		//return mapping.findForward("assignCase2EmpPage");
		return getCasesList(mapping, cform, request, response);
	}
	
	// AJAX SUBMIT - NOT USED
	public ActionForward assignCase2Employee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null, cIno=null;
		PrintWriter out = null;
		try {
			out = response.getWriter();
			con = DatabasePlugin.connect();

			// Assing the case
			// Create User Login details for the Employee if not Exist
			cIno = (String) request.getParameter("cino");
			if (cIno != null && !cIno.equals("")) {
				sql = "insert into ecourts_ack_assignment_dtls (ackno, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by) values (?, ?, ?, ?, ?, now(), ?, ?)";
				ps = con.prepareStatement(sql);
				int i = 0;

				ps.setString(++i, cIno);
				ps.setString(++i, (String) request.getParameter("emp_dept"));
				ps.setString(++i, (String) request.getParameter("emp_section"));
				ps.setString(++i, (String) request.getParameter("emp_post"));
				ps.setString(++i, (String) request.getParameter("emp_id"));
				ps.setString(++i, request.getRemoteAddr());
				ps.setString(++i, (String) request.getSession().getAttribute("user_id"));
				int a = ps.executeUpdate();

				sql = "update ecourts_case_data set assigned_to=true where cino='" + cIno + "' ";
				a += DatabasePlugin.executeUpdate(sql, con);

				if (a > 0) {
					out.println("<font color=green>Case successfully Assigned to Selected Employee.</font>");
					
					// Create Login to Employee To access assigned Cases.
					
					
				} else
					out.println("<font color=red>Error in assigning Case. Kindly try again.</font>");
			} else
				out.println("<font color=red>Error : Invalid CINO.</font>");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			DatabasePlugin.close(con, ps, null);
		}
		return null;
	}
}
