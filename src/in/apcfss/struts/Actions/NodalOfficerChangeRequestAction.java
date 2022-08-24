package in.apcfss.struts.Actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.json.JSONException;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class NodalOfficerChangeRequestAction extends DispatchAction{
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null, deptCode=null;
		String tableName="nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			}  else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {
				String distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
				
				con = DatabasePlugin.connect();
				// cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' order by sdeptcode", con));
				
				if(roleId.trim().equals("2"))
				{
					tableName = AjaxModels.getTableName(distId, con);
					sql="select nod.dept_id,nod.dept_id||'-'||upper(dn.description) as description from nodal_officer_details nod inner join dept_new dn on (nod.dept_id=dn.dept_code) where coalesce(dist_id,0)="+distId+" order by 1";
					// cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName+") and deptcode!='01' order by sdeptcode", con));
					cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( sql, con));
					cform.setDynaForm("officerType","NO");
					cform.setDynaForm("distId",distId);
				}else {
					sql="select nod.dept_id,nod.dept_id||'-'||upper(dn.description) as description from nodal_officer_details nod inner join dept_new dn on (nod.dept_id=dn.dept_code)"
							+ " where dn.reporting_dept_code='" + deptCode + "' and coalesce(nod.dist_id,0)=0 order by 1";
					cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( sql, con));
					
					// cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where reporting_dept_code='" + deptCode + "' and deptcode!='01' order by sdeptcode", con));
				}
				
				if(cform.getDynaForm("officerType")!=null && !CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("") 
						&& !CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("0")){
					
					if(roleId.trim().equals("2"))
					{
						sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description, change_reasons, change_letter_path, change_req_approved " +
								"from nodal_officer_change_requests a " +
								"inner join ( " +
								"select distinct employee_id,fullname_en,designation_id, designation_name_en from "+tableName+" " + ") b on (a.employeeid=b.employee_id and a.designation=b.designation_id) " +
								/*"inner join (" +
								"select distinct designation_id, designation_name_en from "+tableName+"  " +  //where substring(global_org_name,1,3)='" + userId.substring(0, 3) + "'
								") c on (a.designation=c.designation_id) " + */
								"inner join dept_new d on (a.dept_id=d.dept_code) " +
								"where a.user_id='" + deptCode + "' and officer_type='"+cform.getDynaForm("officerType")+"'";
						
						sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description, change_reasons, change_letter_path, change_req_approved "
								+ " from nodal_officer_change_requests a "
								+ " inner join (select distinct employee_id,fullname_en,designation_id, designation_name_en from "+tableName+") b on (a.employeeid=b.employee_id and a.designation=b.designation_id) "
								// + " inner join (select distinct  from "+tableName+" where substring(global_org_name,1,5)='" + userId.substring(0, 5) + "') c on () "
								+ " inner join dept_new d on (a.dept_id=d.dept_code) "
								+ " " 
								+ " where a.user_id='" + deptCode + "' and officer_type='"+cform.getDynaForm("officerType")+"'";
						
						
						
					}else
					{
						sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description, change_reasons, change_letter_path, change_req_approved "
								+ " from nodal_officer_change_requests a "
								+ " inner join (select distinct employee_id,fullname_en,designation_id, designation_name_en from "+tableName+") b on (a.employeeid=b.employee_id and a.designation=b.designation_id) "
								// + " inner join (select distinct  from "+tableName+" where substring(global_org_name,1,5)='" + userId.substring(0, 5) + "') c on () "
								+ " inner join dept_new d on (a.dept_id=d.dept_code) "
								+ " " 
								+ " where a.user_id='" + deptCode + "' and officer_type='"+cform.getDynaForm("officerType")+"'";
					}
					
					
					System.out.println("SQL:"+sql);
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);
	
					System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0)
						request.setAttribute("List_data", data);
				
					if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("MLO")){
						
						sql="select distinct designation_id::int4, designation_name_en from "+tableName+" where substring(global_org_name,1,5)='"
								+ deptCode.substring(0,5)
								+ "' order by designation_id::int4";
						System.out.println("SQL:"+sql);
						cform.setDynaForm(
								"designationList",
								DatabasePlugin
										.getSelectBox(
												sql,
												con));
						
						// cform.setDynaForm("deptId", userId);
						cform.setDynaForm("deptId", deptCode);
						request.setAttribute("saveAction", "INSERT");
					}
					else if(CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("NO")){
						request.setAttribute("saveAction", "UPDATE");
					}
					cform.setDynaForm("mobileNo", null);
					cform.setDynaForm("emailId", null);
					cform.setDynaForm("aadharNo", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cform.setDynaForm("roleId",roleId);
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	
	
	
	
	public ActionForward saveEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("NodalOfficerChangeRequestAction..............................................................................saveEmployeeDetails()");
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		String designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null, deptCode=null; int distId=0;
		int status = 0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			
			
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {

				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				
				if(roleId.trim().equals("2")) {
					distId = CommonModels.checkIntObject(session.getAttribute("dist_id"));
					deptCode = userId;
				}
				else if(roleId.trim().equals("3")) {
					deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
					distId = 0;
				}
				
				cform.setDynaForm("dist_id", distId);
				
				designationId = (String) cform.getDynaForm("designationId");
				employeeId = (String) cform.getDynaForm("employeeId");
				mobileNo = (String) cform.getDynaForm("mobileNo");
				emailId = (String) cform.getDynaForm("emailId");
				aadharNo = (String) cform.getDynaForm("aadharNo");

				//if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from nodal_officer_details where emailid='"+emailId+"'"))== 0){
				
					if (DatabasePlugin.NonZeroValidation(designationId) == true
							&& DatabasePlugin.NonZeroValidation(employeeId) == true
							&& DatabasePlugin.NonZeroValidation(mobileNo) == true
							&& mobileNo.trim().length() == 10
							&& DatabasePlugin.NullValidation(emailId) == true
							&& DatabasePlugin.NonZeroValidation(aadharNo) == true
							&& aadharNo.trim().length() == 12 
							&& cform.getDynaForm("changeReasons")!=null
							&& cform.getChangeLetter()!=null
							) {
						designationId = designationId.trim();
						employeeId = employeeId.trim();
						mobileNo = mobileNo.trim();
						emailId = emailId.trim();
						aadharNo = aadharNo.trim();
	
						sql = "insert into nodal_officer_change_requests (dept_id, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, change_reasons, change_letter_path, officer_type, dist_id) " +
								"values (?,?,?,?,?,?,?,?,?,now(),?,?,?,?)";
	
						ps = con.prepareStatement(sql);
	
						int column = 0;
	
						ps.setString(++column, (String) cform.getDynaForm("deptId"));
						ps.setString(++column, deptCode);
						ps.setString(++column, designationId);
						ps.setString(++column, employeeId);
						ps.setString(++column, mobileNo);
	
						ps.setString(++column, emailId);
						ps.setString(++column, aadharNo);
						ps.setString(++column, userId);
						ps.setString(++column, request.getRemoteAddr());
						ps.setString(++column,(String) cform.getDynaForm("changeReasons"));
						
						
						String letterPath = null;
						if(cform.getChangeLetter()!=null){
							FormFile letter = (FormFile)cform.getChangeLetter();	
							FileUploadUtilities fuu = new FileUploadUtilities();
							letterPath = fuu.saveFile(letter, "uploads/changerequests/", "ChangeReqLetter"+generateRandomNo());
						}
						ps.setString(++column, letterPath);
						ps.setString(++column,(String) cform.getDynaForm("officerType"));
						ps.setInt(++column, distId);
						
						if(letterPath !=null)
							status = ps.executeUpdate();
	
						if (status == 1){
							cform.setDynaForm("userId", userId);
							approveChangeRequest(con, cform, request);
							// request.setAttribute("successMsg", "Nodal Officer Change Request registered succesfully.");
							request.setAttribute("successMsg", "Nodal Officer Changed succesfully.");
							con.commit();
						}
						else
							request.setAttribute("errorMsg", "Error while processing the Change Request. Please resubmit.");
					}else {
						request.setAttribute("errorMsg", "Change Request not registered due to Invalid Input. Please resubmit.");
					}
					/*
					  } else{ request.setAttribute("errorMsg",
					  "The selected employee has been already registered as Nodal Officer. So cannot be registered."
					  ); }
					 */
			}
			
			request.setAttribute("saveAction", "INSERT");
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error while processing the Change Request.");
			request.removeAttribute("successMsg");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("deptId", null);
			cform.setDynaForm("designationId", null);
			cform.setDynaForm("employeeId", null);
			cform.setDynaForm("mobileNo", null);
			cform.setDynaForm("emailId", null);
			cform.setDynaForm("aadharNo", null);
			cform.setDynaForm("changeReasons", null);

			DatabasePlugin.close(con, ps, null);
		}
		return unspecified(mapping, cform, request, response);
	}

	public static String generateRandomNo() {
		String randomNo = null;
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		randomNo = "" + sdf.format(d1);
		return randomNo;
	}
	
	
	public static void approveChangeRequest(Connection con, CommonForm cform, HttpServletRequest request) throws SQLException, JSONException {
		String sql = "";
		Statement st = null;
		ResultSet rs = null;
		String oficerType = "", deptCode = "", emailId = "", mobileNo = "", tableName = "nic_data";
		int distId = 0;
		System.out.println("IN approveChangeRequest");
		sql = "SELECT user_id, dept_id, designation, employeeid, mobileno, emailid, aadharno, change_reasons, change_letter_path, change_req_approved, "
				+ " inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time, officer_type, slno, dist_id "
				+ " FROM apolcms.nodal_officer_change_requests where "
				+ " change_req_approved is false and "
				+ " officer_type='"+cform.getDynaForm("officerType")+"' and dept_id='"+cform.getDynaForm("deptId")+"' and coalesce(dist_id,'0')='"+
				CommonModels.checkIntObject(cform.getDynaForm("dist_id"))+"'";//and officer_type='NO' and dept_id='REV02'";
		
		System.out.println("approveChangeRequest SQL:"+sql);
		
		st = con.createStatement();
		rs = st.executeQuery(sql);
		int a = 0;
		while (rs.next()) {
			oficerType = rs.getString("officer_type");
			deptCode = rs.getString("dept_id");
			emailId = rs.getString("emailid");
			mobileNo = rs.getString("mobileno");
			distId = CommonModels.checkIntObject(rs.getString("dist_id"));
			
			System.out.println("oficerType:"+oficerType);
			System.out.println("deptCode:"+deptCode);
			System.out.println("emailId:"+emailId);
			System.out.println("mobileNo:"+mobileNo);
			System.out.println("distId:"+distId);
			
			if(Integer.parseInt(DatabasePlugin.getStringfromQuery("select count(*) from users where userid='"+emailId+"'", con)) > 0) {
				// 1. Log in Users Table
				sql="insert into users_log select * from users where userid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				// 2. Log in MLO
				sql="insert into mlo_details_deleted(deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time)"
						+ " SELECT '"+cform.getDynaForm("userId")+"','"+request.getRemoteAddr()+"', now(),slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time "
						+ " FROM apolcms.mlo_details where emailid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql= "delete FROM apolcms.mlo_details where emailid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				// 3. LOG in NO
				sql="insert into apolcms.nodal_officer_details_delete (deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time)"
						+ " SELECT '"+cform.getDynaForm("userId")+"','"+request.getRemoteAddr()+"', now(), slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time "
						+ " FROM apolcms.nodal_officer_details where emailid='"+emailId+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql= "delete FROM apolcms.nodal_officer_details where emailid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from user_roles where userid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from users where userid='"+emailId+"'";
				System.out.println("SQL:"+sql);
				a += DatabasePlugin.executeUpdate(sql, con);
			}
			a = 0;
			if(CommonModels.checkStringObject(oficerType).equals("MLO")) {
				System.out.println("MLO TYPE");
				sql="insert into mlo_details_deleted(deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time)"
						+ " SELECT '"+cform.getDynaForm("userId")+"','"+rs.getString("inserted_ip")+"', now(),slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time "
						+ " FROM apolcms.mlo_details where user_id='"+deptCode+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				String existingId = DatabasePlugin.getSingleValue(con, "select emailid FROM apolcms.mlo_details where user_id='"+deptCode+"'");
				System.out.println("existingId:"+existingId);	
				
				sql="delete from user_roles where userid='"+existingId+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from users where userid='"+existingId+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from mlo_details where user_id='"+deptCode+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into mlo_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip) "
						+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip from nodal_officer_change_requests where user_id='"+deptCode+"' and change_req_approved is false and officer_type='MLO'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type) "
						+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
						+ "', now(), null, dm.dept_id,'" + deptCode
						+ "', '4'  from mlo_details a inner join (select distinct employee_id,fullname_en,designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
						+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
						+ "where employeeid='" + rs.getString("employeeid") + "'";
				
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into user_roles (userid, role_id) values ('"+emailId+"','4')";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
			}
			else if(CommonModels.checkStringObject(oficerType).equals("NO")) {
				
				tableName = AjaxModels.getTableName(distId+"", con);
				
				System.out.println("NO TYPE");
				sql="insert into apolcms.nodal_officer_details_delete (deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time)"
						+ " SELECT '"+deptCode+"','"+rs.getString("inserted_ip")+"', now(), slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time "
						+ " FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId;
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from user_roles where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from users where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
									
				sql="delete from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
					
				sql="insert into nodal_officer_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, dist_id, dept_id) "
							+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, coalesce(dist_id,0), dept_id from nodal_officer_change_requests "
							+ "where dept_id='"+deptCode+"'  and change_req_approved is false and officer_type='NO' and coalesce(dist_id,0)="+distId+"";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				int userRole=5;//State Nodal Officer
				if(distId > 0)
					userRole=10;// District Nodal Officer
				
				sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type, dist_id) "
						+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
						+ "', now(), null, dm.dept_id,'" + deptCode
						+ "', '"+userRole+"', dist_id from nodal_officer_details a inner join (select distinct employee_id,fullname_en,designation_id from "+tableName+") b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
						+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
						+ "where employeeid='" + rs.getString("employeeid") + "'";
				
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into user_roles (userid, role_id) values ('"+emailId+"','"+userRole+"')";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
				System.out.println("SQL:" + sql);
				a += DatabasePlugin.executeUpdate(sql, con);
			}
			System.out.println("executed SQLS :"+a);
			if(a > 0) {
				String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
				String templateId="1007784197678878760";
				System.out.println("smsText:"+smsText);
				System.out.println("mobileNo:"+mobileNo);
				// mobileNo = "9618048663"; 
				SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
			}
		}
	}
	
	public static void approveChangeRequestOld(Connection con, CommonForm cform, HttpServletRequest request) throws SQLException, JSONException {

		String sql = "";
		Statement st = null;
		ResultSet rs = null;
		String oficerType = "", deptCode = "", emailId = "", mobileNo = "", designationId="", employeeId="", aadharNo="";int distId=0;
			
			int a = 0;
			
				oficerType = (String)cform.getDynaForm("officerType");
				deptCode = (String)cform.getDynaForm("deptId");
				mobileNo = (String) cform.getDynaForm("mobileNo");
				emailId = (String) cform.getDynaForm("emailId");
				distId = CommonModels.checkIntObject(cform.getDynaForm("dist_id"));
				designationId = (String) cform.getDynaForm("designationId");
				employeeId = (String) cform.getDynaForm("employeeId");
				aadharNo = (String) cform.getDynaForm("aadharNo");
				
				System.out.println("oficerType:"+oficerType);
				System.out.println("deptCode:"+deptCode);
				System.out.println("emailId:"+emailId);
				System.out.println("mobileNo:"+mobileNo);
				System.out.println("distId:"+distId);
				
				if(Integer.parseInt(DatabasePlugin.getStringfromQuery("select count(*) from users where userid='"+emailId+"'", con)) > 0) {
					// 1. Log in Users Table
					sql="insert into users_log select * from users where userid='"+emailId+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					// 2. Log in MLO
					sql="insert into mlo_details_deleted(deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+request.getRemoteAddr()+"', now(),slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.mlo_details where emailid='"+emailId+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql= "delete FROM apolcms.mlo_details where emailid='"+emailId+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					// 3. LOG in NO
					sql="insert into apolcms.nodal_officer_details_delete (deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+request.getRemoteAddr()+"', now(), slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.nodal_officer_details where emailid='"+emailId+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql= "delete FROM apolcms.nodal_officer_details where emailid='"+emailId+"'";
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from user_roles where userid='"+emailId+"'";
				}
				
				
				
				if(CommonModels.checkStringObject(oficerType).equals("MLO")) {
					
					sql="insert into mlo_details_deleted(deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+request.getRemoteAddr()+"', now(),slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.mlo_details where user_id='"+deptCode+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					String existingId = DatabasePlugin.getSingleValue(con, "select emailid FROM apolcms.mlo_details where user_id='"+deptCode+"'");
					System.out.println("existingId:"+existingId);	
					
					sql="delete from user_roles where userid='"+existingId+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from users where userid='"+existingId+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from mlo_details where user_id='"+deptCode+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into mlo_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip) "
							+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip from nodal_officer_change_requests where user_id='"+deptCode+"' and change_req_approved is false and officer_type='MLO'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type) "
							+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
							+ "', now(), null, dm.dept_id,'" + deptCode
							+ "', '4'  from mlo_details a inner join (select distinct employee_id,fullname_en,designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
							+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
							+ "where employeeid='" + employeeId + "'";
					
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into user_roles (userid, role_id) values ('"+emailId+"','4')";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				else if(CommonModels.checkStringObject(oficerType).equals("NO")) {
				
					sql="insert into apolcms.nodal_officer_details_delete (deleted_by, deleted_ip, deleted_time, slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time)"
							+ " SELECT '"+deptCode+"','"+rs.getString("inserted_ip")+"', now(), slno, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id, dept_id, updated_by, updated_ip, updated_time "
							+ " FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId;
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from user_roles where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="delete from users where userid in (select emailid FROM apolcms.nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+")";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
										
					sql="delete from nodal_officer_details where dept_id='"+deptCode+"' and coalesce(dist_id,0)="+distId+"";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
						
					sql="insert into nodal_officer_details(user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, dist_id, dept_id) "
								+ " select user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, coalesce(dist_id,0), dept_id from nodal_officer_change_requests "
								+ "where dept_id='"+deptCode+"'  and change_req_approved is false and officer_type='NO' and coalesce(dist_id,0)="+distId+"";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					int userRole=10;
					if(distId > 0)
						userRole=5;
					
					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id , dept_code, user_type, dist_id) "
							+ "select a.emailid, md5('olcms@2021'), b.fullname_en, '" + deptCode
							+ "', now(), null, dm.dept_id,'" + deptCode
							+ "', '"+userRole+"', dist_id from nodal_officer_details a inner join (select distinct employee_id,fullname_en,designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
							+ " left join dept_new dm on (dm.dept_code='"+deptCode+"') "
							+ "where employeeid='" + rs.getString("employeeid") + "'";
					
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="insert into user_roles (userid, role_id) values ('"+emailId+"','"+userRole+"')";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
					
					sql="update nodal_officer_change_requests set change_req_approved=true where slno='"+rs.getInt("slno")+"'";
					System.out.println("SQL:" + sql);
					a += DatabasePlugin.executeUpdate(sql, con);
				}
				System.out.println("executed SQLS :"+a);
				if(a > 0) {
					String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
					String templateId="1007784197678878760";
					System.out.println("smsText:"+smsText);
					System.out.println("mobileNo:"+mobileNo);
					mobileNo = "9618048663"; 
					SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
				}
				/*
				 * } else { System.out.println("USER ALREADY EXISTS WITH DIFFERENT ROLE"); }
				 */
	}
}