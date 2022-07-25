package in.apcfss.struts.Actions;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.sun.mail.imap.Utility.Condition;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class HighCourtCasesListAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			cform.setDynaForm("purposeList",
					DatabasePlugin
							.getSelectBox(
									"select purpose_name,purpose_name from apolcms.ecourts_case_data where dept_code='"
											+ session.getAttribute("dept_code") + "' group by purpose_name order by 1",
									con));
			
			//cform.setDynaForm("regYear", "2022");
			cform.setDynaForm("ShowDefault", "ShowDefault");
			
			/*ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1990; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);
			*/
			request.setAttribute("SHOWMESG", "SHOWMESG");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		// return mapping.findForward("success");
		return getCasesList(mapping, cform, request, response);
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
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			con = DatabasePlugin.connect();
			// cform.setDynaForm("designationList", DatabasePlugin.getSelectBox("select
			// distinct designation_id::int4, designation_name_en from nic_data where
			// substring(global_org_name,1,5)='" + session.getAttribute("userid") + "' and
			// trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4
			// desc ", con));
			
			String condition1="";
			String condition2="";
			
			String src = CommonModels.checkStringObject(request.getParameter("src"));
			
			if(!src.equals("dashBoard")) {
				if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					// sqlCondition += " and dt_regis >= to_date('" + cform.getDynaForm("dofFromDate") + "','dd-mm-yyyy') ";
					sqlCondition += " and dt_regis >= to_date('" + cform.getDynaForm("dofFromDate") + "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					//sqlCondition += " and dt_regis <= to_date('" + cform.getDynaForm("dofToDate") + "','dd-mm-yyyy') ";
					sqlCondition += " and dt_regis <= to_date('" + cform.getDynaForm("dofToDate") + "','dd-mm-yyyy') ";
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
				sqlCondition += " and a.reg_year in ('2021','2022') ";
				//heading
				cform.setDynaForm("regYear", "2022");
			}
			else if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			
			
			if (CommonModels.checkIntObject(cform.getDynaForm("filingYear")) > 0) {
				sqlCondition += " and a.fil_year='" + CommonModels.checkIntObject(cform.getDynaForm("filingYear")) + "' ";
			}
			
			}
			
			
			if(!roleId.equals("2") && !roleId.equals("6")) { //District Nodal Officer
				sqlCondition +=" and a.dept_code='" + deptCode + "' ";
			}
			
			if(roleId.equals("2")) { //District Collector
				
				sqlCondition +=" and a.case_status=7 and a.dist_id='"+distId+"'";
			}
			else if(roleId.equals("10")) { //District Nodal Officer
				sqlCondition +=" and a.case_status=8 and a.dist_id='"+distId+"'";
			}
			else if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
				sqlCondition +=" and a.case_status in (3,4)";
			}
			else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
				sqlCondition +=" and (a.case_status is null or a.case_status in (1, 2))";
			}
			else if(roleId.equals("15")) {//MLO Subject.
				sqlCondition +=" and  a.case_status=12 ";
			}
			
			
			else if(roleId.equals("6") ) {//GP
				condition1 =" inner join ecourts_mst_gp_dept_map emgd on (a.dept_code=emgd.dept_code) ";
				condition2 =" and a.case_status is null or a.case_status=2 ";
			}
			
			
			sql = "select a.*,coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ ""+condition1+" left join"
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
					+ " and coalesce(ecourts_case_status,'')!='Closed'  "+condition2+" ";

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
			
			sql = "select emailid, b.fullname_en||'-'|| designation_name_en||' - MLO-'||a.subject_desc from mlo_subject_details a "
					+ " inner join (select distinct employee_id,designation_id,designation_name_en,fullname_en from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id) "
					+ " where a.user_id='" + deptCode + "'";
			
			cform.setDynaForm("MLOSUBLIST", DatabasePlugin.getSelectBox(
					sql,
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

	public ActionForward assignCase2EmpPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("HighCourtCasesListAction..............................................................................assignCase2EmpPage()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, cIno="";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			String deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			
			con = DatabasePlugin.connect();
			cIno = (String) request.getParameter("cino");
			
			sql="select count(*) from ecourts_case_emp_assigned_dtls where cino = '"+cIno+"'";
			
			if(Integer.parseInt(DatabasePlugin.getStringfromQuery(sql, con)) == 0){
				
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code, upper(trim(description)) as description from dept_new where display=true order by dept_code",
						con));
				
				//sql="select sdeptcode||deptcode from dept where dept_id='"+session.getAttribute("dept_id")+"'";
				request.setAttribute("deptCode", deptCode);// DatabasePlugin.getStringfromQuery(sql, con));
				
				cform.setDynaForm("cINO", cIno);
				
				//sql="select sdeptcode from dept where dept_id='"+request.getSession().getAttribute("dept_id")+"'"; 
				//String deptCode = DatabasePlugin.getStringfromQuery(sql, con); 
				
				sql = "select trim(employee_identity),trim(employee_identity) from nic_data where substr(trim(global_org_name),1,3)='"+deptCode+"' and trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				// sql="select trim(employee_identity),trim(employee_identity) from nic_data where trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				System.out.println("EMP SEC-SQL:"+sql);
				cform.setDynaForm("empSectionList", DatabasePlugin.getSelectBox( sql,con));
			}
			else{
				request.setAttribute("errorMsg", "Already this case is Assigned. Kindly try another.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("assignCase2EmpPage");
		// return getCasesList(mapping, cform, request, response);
	}
	
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
				sql = "insert into ecourts_case_emp_assigned_dtls (cino, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by, emp_user_id) values (?, ?, ?, ?, ?, now(), ?, ?, ?)";
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
								//+ "' from ecourts_case_emp_assigned_dtls a inner join (select distinct employee_id,fullname_en,email from nic_data) b on (a.emp_id=b.employee_id) where a.emp_id='"+cform.getDynaForm("employeeId")+"'";
						
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
				
				String caseNewDept = CommonModels.checkStringObject(cform.getDynaForm("caseDept"));
				String newStatusCode="4";
					if (caseNewDept.contains("01")) { 
						newStatusCode="2";
					}
					else { 
						newStatusCode="4";
					}
				
				sql = "update ecourts_case_data set dept_id='"+assign2deptId+"',dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("caseDept"))+"',case_status='"+newStatusCode+"'  where cino in (" + selectedCaseIds + ") ";
				
				System.out.println("UPDATE SQL:"+sql);
				
				a+=DatabasePlugin.executeUpdate(sql, con);
				con.commit();
				request.setAttribute("successMsg", "Case/Cases successfully moved to selected Department / HOD.");
				
			} 
			else if(!CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("mloSubjectId")).equals("0")) {
				for (String newCaseId : CommonModels.checkStringObject(cform.getDynaForm("selectedCaseIds")).split(",")) {
					selectedCaseIds+="'"+newCaseId+"',";
					
					System.out.println("newCaseId::::::"+newCaseId);
					
					sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ) "
							+ "values ('" + newCaseId + "','CASE ASSSIGNED TO MLO (SUBJECT)','"+userId+"', '"+request.getRemoteAddr()+"', '"+CommonModels.checkStringObject(cform.getDynaForm("mloSubjectId"))+"', null)";
					
					a+=DatabasePlugin.executeUpdate(sql, con);
					System.out.println(a+":ACTIVITIES SQL:"+sql);
				}
				
				if(selectedCaseIds!=null && !selectedCaseIds.equals("")) {
					selectedCaseIds = selectedCaseIds.substring(0,selectedCaseIds.length()-1);
					
					sql = "update ecourts_case_data set case_status='12', assigned_to='"+CommonModels.checkStringObject(cform.getDynaForm("mloSubjectId"))+"'  where cino in (" + selectedCaseIds + ") ";
					
					System.out.println("UPDATE SQL:"+sql);
					
					a+=DatabasePlugin.executeUpdate(sql, con);
					con.commit();
					request.setAttribute("successMsg", "Case/Cases successfully moved to selected MLO (Subject).");
				}
			}
			
			else
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
						sql = "insert into ecourts_case_emp_assigned_dtls (cino, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by, emp_user_id) values (?, ?, ?, ?, ?, now(), ?, ?, ?)";
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

						sql = "update ecourts_case_data set dept_id='"+assign2deptId+"', dept_code='"+empDeptCode+"', assigned=true, assigned_to='"+emailId+"',case_status="+
						newStatusCode+", dist_id="+distCode+" where cino='" + cIno + "' ";
						System.out.println("UPDATE SQL:"+sql);
						a += DatabasePlugin.executeUpdate(sql, con);

						sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks ,dist_id) "
								+ "values ('" + cIno + "','"+activityDesc+"','"+userId+"', '"+request.getRemoteAddr()+"', '"+(String) cform.getDynaForm("employeeId")+"', '"+(String) cform.getDynaForm("caseRemarks")+"','"+distCode+"')";
						// System.out.println("INSERT ACTIVITIES SQL:"+sql);
						a += DatabasePlugin.executeUpdate(sql, con);
						
						if (a==3) {
							request.setAttribute("successMsg", "Case successfully Assigned to Selected Employee.");
						} else{
							request.setAttribute("errorMsg", "Error in assigning Cases. Kindly try again.");
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
							+ cform.getDynaForm("empDept") + "') "
									+ ""
							+ " where b.employee_id='" + cform.getDynaForm("employeeId")
							+ "' and trim(b.employee_identity)='" + cform.getDynaForm("empSection")
							+ "' and trim(b.post_name_en)='" + cform.getDynaForm("empPost") + "'";

					System.out.println("NEW SECTION OFFICER CREATION SQL:"+sql);
					b += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, user_type, dist_id) " +
							"select distinct b.email, md5('olcms@2021'), b.fullname_en, '"+(String) session.getAttribute("userid")
							+"', now(),'"+request.getRemoteAddr() +"'::inet, d.dept_id,d.dept_code,"+newRoleId+","+distCode+" from "+tableName+" b inner join dept_new d on (d.dept_code='"+cform.getDynaForm("empDept")
							+"') "
							+ " "
							+" where b.employee_id='"+ cform.getDynaForm("employeeId") + "' and trim(b.employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(b.post_name_en)='"+cform.getDynaForm("empPost")+"'";
							//+ "where b.employee_id='"+cform.getDynaForm("employeeId")+"' ";
					
					System.out.println("USER CREATION SQL:"+sql);
					
					b+=DatabasePlugin.executeUpdate(sql, con);
					
					sql="select distinct mobile1 from "+tableName+" b "
							+" where b.employee_id='"+ cform.getDynaForm("employeeId") + "' and trim(b.employee_identity)='"+cform.getDynaForm("empSection")+"' and trim(b.post_name_en)='"+cform.getDynaForm("empPost")+"'"
							+ " and mobile1 is not null";
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
							// mobileNo = "9618048663";
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
					
					sql = "update ecourts_case_data set "
							//+ "dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))+"', dept_id='"+assign2deptId+"',"
							+ " dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))
							// +"',dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))
							+"',case_status=7  where cino in (" + selectedCaseIds + ") ";
					successMsg = "Case/Cases successfully moved to selected District Collector Login";
					
				}
				else if(officerType.equals("DC-NO")) {
					
					sql = "update ecourts_case_data set dept_id='"+assign2deptId+"', dist_id='"+CommonModels.checkIntObject(cform.getDynaForm("caseDist"))+"',dept_code='"+CommonModels.checkStringObject(cform.getDynaForm("distDept"))
					+"',case_status=8  where cino in (" + selectedCaseIds + ") ";
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
				sql = "insert into ecourts_case_emp_assigned_dtls (cino, dept_code, emp_section, emp_post, emp_id, inserted_time, inserted_ip, inserted_by) values (?, ?, ?, ?, ?, now(), ?, ?)";
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