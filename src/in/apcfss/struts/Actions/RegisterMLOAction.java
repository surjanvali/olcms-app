package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;

import java.io.PrintWriter;
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

import plugins.DatabasePlugin;

/*
 * @BALAKRISHNA - 12-08-2021
 * Creation of MLO Details
 * Updating MLO Details
 * Deleting MLO Details
 * 
 */

public class RegisterMLOAction extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		System.out
				.println("RegisterMLOAction..............................................................................unspecified()");

		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null;
		String deptCode = null;
		try {
			
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			
			con = DatabasePlugin.connect();
			
			sql="select distinct designation_id::int4, designation_name_en from nic_data where substring(global_org_name,1,5)='"
			+ deptCode.substring(0,5)+ "' and trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4 desc ";
			System.out.println("SQL:"+sql);
			cform.setDynaForm(
					"designationList",
					DatabasePlugin
							.getSelectBox(
									sql,
									con));

			sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en from mlo_details a "
					+ " inner join (select distinct employee_id,designation_id,designation_name_en,fullname_en from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id) "
					//+ "inner join (select distinct designation_id, designation_name_en from nic_data where substring(global_org_name,1,5)='"
					//+ deptCode.substring(0,5) + "' ) c on (a.designation=c.designation_id)" + "" + ""
					+ "where a.user_id='" + deptCode + "'";
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("List_data", data);
			
			
			request.setAttribute("saveAction", "INSERT");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			cform.setDynaForm("deptId", deptCode);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("registerMLO");
	}

	public ActionForward saveEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		System.out
				.println("RegisterMLOAction..............................................................................saveEmployeeDetails()");

		Connection con = null;
		PreparedStatement ps = null;

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null
				|| session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}

		String sql = null;
		String designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null;
		int status = 0;
		CommonForm cform = (CommonForm) form;
		String deptCode = null;
		try {

			if (!(session.getAttribute("role_id").toString().trim().equals("3") || session.getAttribute("role_id").toString().trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
			} else if (session.getAttribute("role_id").toString().trim().equals("3") || session.getAttribute("role_id").toString().trim().equals("2")) {

				 deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				
				designationId = (String) cform.getDynaForm("designationId");
				employeeId = (String) cform.getDynaForm("employeeId");
				mobileNo = (String) cform.getDynaForm("mobileNo");
				emailId = (String) cform.getDynaForm("emailId");
				aadharNo = (String) cform.getDynaForm("aadharNo");

				if (DatabasePlugin.NonZeroValidation(designationId) == true
						&& DatabasePlugin.NonZeroValidation(employeeId) == true
						&& DatabasePlugin.NonZeroValidation(mobileNo) == true
						&& mobileNo.trim().length() == 10
						&& DatabasePlugin.NullValidation(emailId) == true
						&& DatabasePlugin.NonZeroValidation(aadharNo) == true
						&& aadharNo.trim().length() == 12) {
					designationId = designationId.trim();
					employeeId = employeeId.trim();
					mobileNo = mobileNo.trim();
					emailId = emailId.trim();
					aadharNo = aadharNo.trim();

					con = DatabasePlugin.connect();

					sql = "insert into mlo_details (user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time) values (?,?,?,?,  ?,?,?,?, now())";
					//con.setAutoCommit(false);
					ps = con.prepareStatement(sql);

					int column = 0;

					ps.setString(++column, deptCode);
					ps.setString(++column, designationId);
					ps.setString(++column, employeeId);
					ps.setString(++column, mobileNo);

					ps.setString(++column, emailId);
					ps.setString(++column, aadharNo);
					ps.setString(++column, (String) session.getAttribute("userid"));
					ps.setString(++column, request.getRemoteAddr());

					status = ps.executeUpdate();

					if (status == 1){
						
						sql="insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code) "
								+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '"+(String) session.getAttribute("userid")+"', now(),'"+request.getRemoteAddr()
								+"', '"+ CommonModels.checkIntObject(session.getAttribute("dept_id"))+"','"+deptCode
								// +"'  from mlo_details a inner join (select distinct employee_id,fullname_en from nic_data) b on (a.employeeid=b.employee_id) where employeeid='"+employeeId+"'";
								+ "'  from mlo_details a inner join (select distinct employee_id,designation_id,designation_name_en,fullname_en from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id) "
								+ "where employeeid='"+employeeId+"' and a.designation='"+designationId+"' ";
						System.out.println("USER INSERT SQL:"+sql);
						status=DatabasePlugin.executeUpdate(sql, con);
						
						sql="insert into user_roles (userid, role_id) values ('"+emailId+"','4')";
						System.out.println("USER ROLE INSERT SQL:"+sql);
						status+=DatabasePlugin.executeUpdate(sql, con);
						if(status==2) {
							//con.commit();
							String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
							String templateId="1007784197678878760";
							// mobileNo="9618048663";
							SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
							//request.setAttribute("successMsg", "Employee details saved succesfully and User credentials have been created. Login with the Email Id as User Id and olcms@2021 as Password.");
							request.setAttribute("successMsg", "Mid Level Officer (Legal) details Registered & Login Credentails created successfully. Login details sent to MLO Registered Mobile no.");
						}
						else {
							//con.rollback();
							request.setAttribute("errorMsg", "Mid Level Officer (Legal) not Registered due to wrong data submitted or Already registered.");
						}
						
						}
					else
						request.setAttribute("errorMsg", "Mid Level Officer (Legal) not Registered due to wrong data submitted or Already registered");

				}
			} else {
				request.setAttribute("errorMsg", "Mid Level Officer (Legal) not Registered due to wrong data submitted or Already registered.");
			}
			request.setAttribute("saveAction", "INSERT");
			cform.setDynaForm("deptId", deptCode);
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Mid Level Officer (Legal) not Registered due to wrong data submitted or Already registered");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("designationId", null);
			cform.setDynaForm("employeeId", null);
			cform.setDynaForm("mobileNo", null);
			cform.setDynaForm("emailId", null);
			cform.setDynaForm("aadharNo", null);
			DatabasePlugin.close(con, ps, null);
		}

		return unspecified(mapping, cform, request, response);
	}

	
	public ActionForward editEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("RegisterMLOAction..............................................................................editEmployee()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = ""; 
		try {
				if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
				String mloId=CommonModels.checkStringObject(cform.getDynaForm("mloId"));

			con = DatabasePlugin.connect();
			
			// sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno from mlo_details where user_id='" + session.getAttribute("userid") + "' and slno='"+mloId+"'";
			
			sql="select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, fullname_en, designation_name_en from mlo_details a inner join nic_data b on (a.employeeid=b.employee_id) where user_id='"+session.getAttribute("userid") + "' and slno='"+mloId+"' order by designation::int4";
			
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
			{
				Map myData = (Map)data.get(0);
				cform.setDynaForm("designationId", myData.get("designation"));
				cform.setDynaForm("employeeId", myData.get("employeeid"));
				cform.setDynaForm("mobileNo", myData.get("mobileno"));
				cform.setDynaForm("emailId", myData.get("emailid"));
				cform.setDynaForm("aadharNo", myData.get("aadharno"));
				cform.setDynaForm("mloId", myData.get("slno"));
				cform.setDynaForm("designationName", myData.get("designation_name_en"));
				cform.setDynaForm("employeeName", myData.get("fullname_en"));
				
				request.setAttribute("saveAction", "UPDATE");
			}
			else{
				request.setAttribute("errorMsg", "Mid Level Officer (Legal) not Registered due to wrong data submitted or Already registered");
			}
			cform.setDynaForm("deptId", session.getAttribute("userid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			sql="select distinct designation_id::int4, designation_name_en from nic_data where substring(global_org_name,1,5)='"
					+ session.getAttribute("userid")
					+ "'  and trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4 desc ";
			System.out.println("SQL:"+sql);
			cform.setDynaForm( "designationList", DatabasePlugin.getSelectBox( sql,con));
			
			if(cform.getDynaForm("designationId")!=null)
			{
				sql="select distinct employee_id, fullname_en from nic_data where substring(global_org_name,1,5)='"
					+ session.getAttribute("userid")
					+ "' and designation_id='"
					+ cform.getDynaForm("designationId")
					+ "' order by 1 desc";
				System.out.println("SQL:"+sql);
				cform.setDynaForm("employeeList", DatabasePlugin.getSelectBox(sql,con));
			}
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
			saveToken(request);
		}

		return mapping.findForward("registerMLO");
	}
	
	
	public ActionForward updateEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		System.out
				.println("RegisterMLOAction..............................................................................updateEmployeeDetails()");

		Connection con = null;
		PreparedStatement ps = null;

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null
				|| session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}


		String sql = null;
		String mloId = null, designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null;
		int status = 0;
		CommonForm cform = (CommonForm) form;

		try {

			if (!(session.getAttribute("role_id").toString().trim().equals("3") || session.getAttribute("role_id").toString().trim().equals("2"))) {
				request.setAttribute("errorMsg",
						"Unauthorized to access this service");
			} else if (session.getAttribute("role_id").toString().trim().equals("3") || session.getAttribute("role_id").toString().trim().equals("2")) {

				mloId = (String) cform.getDynaForm("mloId");

				designationId = (String) cform.getDynaForm("designationId");
				employeeId = (String) cform.getDynaForm("employeeId");
				mobileNo = (String) cform.getDynaForm("mobileNo");
				emailId = (String) cform.getDynaForm("emailId");
				aadharNo = (String) cform.getDynaForm("aadharNo");

				if (DatabasePlugin.NonZeroValidation(mloId) == true
						&& DatabasePlugin.NonZeroValidation(designationId) == true
						&& DatabasePlugin.NonZeroValidation(employeeId) == true
						&& DatabasePlugin.NonZeroValidation(mobileNo) == true
						&& mobileNo.trim().length() == 10
						&& DatabasePlugin.NullValidation(emailId) == true
						&& DatabasePlugin.NonZeroValidation(aadharNo) == true
						&& aadharNo.trim().length() == 12) {
					mloId = mloId.trim();

					designationId = designationId.trim();
					employeeId = employeeId.trim();
					mobileNo = mobileNo.trim();
					emailId = emailId.trim();
					aadharNo = aadharNo.trim();

					con = DatabasePlugin.connect();

					sql = "insert into mlo_details_bkp (select  ?,?,now(),* from mlo_details where slno=? and employeeid=?)";
					ps = con.prepareStatement(sql);
					int column = 0;

					ps.setString(++column,
							(String) session.getAttribute("userid"));
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);
					ps.setString(++column, employeeId);
					ps.executeUpdate();

					ps = con.prepareStatement(sql);

					sql = "update mlo_details set mobileno=?, emailid=?, aadharno=?, updated_by=?, updated_ip=?, updated_time=now() " +
							"where slno=? and employeeid=?";

					ps = con.prepareStatement(sql);

					column = 0;

					ps.setString(++column, mobileNo);
					ps.setString(++column, emailId);
					ps.setString(++column, aadharNo);
					ps.setString(++column, (String) session.getAttribute("userid"));
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);

					ps.setString(++column, employeeId);

					status = ps.executeUpdate();

					if (status == 1)
						request.setAttribute("successMsg",
								"Mid Level Officer (Legal) details Registered & Login Credentails created successfully. Login details sent to MLO Registered Mobile no.");
					else
						request.setAttribute("errorMsg",
								"Mid Level Officer (Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");

				}
			} else {
				request.setAttribute("errorMsg",
						"Mid Level Officer (Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
			}
			cform.setDynaForm("deptId", session.getAttribute("userid").toString());
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Mid Level Officer (Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", null);
			cform.setDynaForm("designationId", null);
			cform.setDynaForm("employeeId", null);
			cform.setDynaForm("mobileNo", null);
			cform.setDynaForm("emailId", null);
			cform.setDynaForm("aadharNo", null);

			DatabasePlugin.close(con, ps, null);
		}

		return unspecified(mapping, cform, request, response);
	}

	public ActionForward deleteEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		System.out
				.println("RegisterMLOAction..............................................................................deleteEmployeeDetails()");
		Connection con = null;
		PreparedStatement ps = null;

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null
				|| session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null;
		String mloId = null;
		int status = 0;
		CommonForm cform = (CommonForm) form;

		try {

			if (!session.getAttribute("role_id").toString().trim().equals("3")) {
				request.setAttribute("errorMsg",
						"Unauthorized to access this service");
			} else if (session.getAttribute("role_id").toString().trim()
					.equals("3")) {

				mloId = (String) cform.getDynaForm("mloId");


				if (DatabasePlugin.NonZeroValidation(mloId) == true) {
					mloId = mloId.trim();

					con = DatabasePlugin.connect();

					sql = "insert into mlo_details_deleted (select  ?,?,now(),* from mlo_details where slno=?)";
					ps = con.prepareStatement(sql);
					int column = 0;

					ps.setString(++column,
							(String) session.getAttribute("userid"));
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);
					//ps.setString(++column, employeeId);
					ps.executeUpdate();

					sql = "delete from mlo_details where slno=?";
					ps = con.prepareStatement(sql);
					column = 0;
					ps.setString(++column, mloId);
					//ps.setString(++column, employeeId);
					status = ps.executeUpdate();

					if (status == 1)
						request.setAttribute("successMsg",
								"Mid Level Officer details Deleted succesfully.");
					else
						request.setAttribute("errorMsg",
								"Mid Level Officer deletion failed.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Error: Invalid data is submitted.");
			}
			cform.setDynaForm("deptId", session.getAttribute("userid").toString());
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Error: Invalid data is submitted!!!");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}

		return unspecified(mapping, cform, request, response);
	}

	public ActionForward sendCredentialsSMS(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null, respMsg="";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			con = DatabasePlugin.connect();
			
			String emailId = CommonModels.checkStringObject(request.getParameter("empUserId"));
			String userType = CommonModels.checkStringObject(request.getParameter("userType"));
			String mobileNo = null;
			if (emailId != null && !emailId.equals("") && userType != null && !userType.equals("")) {
				
				String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
				String templateId="1007784197678878760";
				
				if(userType.equals("MLO")) {
					sql = "select mobileno from mlo_details where emailid='"+emailId+"'";
				}
				else if(userType.equals("NO")) {
					sql="select mobileno from nodal_officer_details where emailid='"+emailId+"'";
				}
				else if(userType.equals("SECTION")) {
					sql="select distinct mobile1 from nic_data where email='"+emailId+"'";
				}
				
				System.out.println("SQL:"+sql);
				
				mobileNo = DatabasePlugin.getStringfromQuery(sql, con);
				
				System.out.println(mobileNo+""+smsText+""+templateId);
				if(mobileNo!=null && !mobileNo.equals("")) {
					// mobileNo = "9618048663";
					SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
					respMsg="<font color=green>SMS alert sent successfully to the Employee.</font>";
				}	
				else
					respMsg="<font color=red>Error in sending SMS alert. No Mobile no. details. Kindly update mobile no. and try again.</font>";
			} else
				respMsg="<font color=red>Error : Invalid Data.</font>";
			
			System.out.println("respMsg:"+respMsg);
			
			out.println(respMsg);
			out.flush();
		} catch (Exception e) {
			out.println("<font color=red>Error : Invalid Data.</font>");
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

/*
 * 
 * 
 * <definition name="registerMLO" extends="UserLayout"> <put name="content"
 * value="/jsp/registerMLO.jsp" /> </definition>
 */

/*
 * create table mlo_details ( slno serial, user_id varchar(50), designation
 * varchar(15), employeeid varchar(15), mobileno varchar(10), emailid
 * varchar(150), aadharno varchar(12), inserted_by varchar(50), inserted_ip
 * varchar(150), inserted_time timestamp default now(),
 * 
 * updated_by varchar(50), updated_ip varchar(150), updated_time timestamp,
 * 
 * primary key (employeeid), unique (slno), unique (mobileno), unique (emailid),
 * unique (aadharno) ) go create table mlo_details_bkp (
 * 
 * deleted_by varchar(50), deleted_ip varchar(150), deleted_time timestamp,
 * 
 * slno numeric, user_id varchar(50),
 * 
 * designation varchar(15), employeeid varchar(15), mobileno varchar(10),
 * emailid varchar(150), aadharno varchar(12),
 * 
 * inserted_by varchar(50), inserted_ip varchar(150), inserted_time timestamp,
 * 
 * updated_by varchar(50), updated_ip varchar(150), updated_time timestamp,
 * 
 * primary key (employeeid), unique (slno), unique (mobileno), unique (emailid),
 * unique (aadharno)
 * 
 * ) go create table mlo_details_deleted (
 * 
 * deleted_by varchar(50), deleted_ip varchar(150), deleted_time timestamp,
 * 
 * slno numeric, user_id varchar(50),
 * 
 * designation varchar(15), employeeid varchar(15), mobileno varchar(10),
 * emailid varchar(150), aadharno varchar(12),
 * 
 * inserted_by varchar(50), inserted_ip varchar(150), inserted_time timestamp,
 * 
 * updated_by varchar(50), updated_ip varchar(150), updated_time timestamp,
 * 
 * primary key (employeeid), unique (slno), unique (mobileno), unique (emailid),
 * unique (aadharno)
 * 
 * )
 */