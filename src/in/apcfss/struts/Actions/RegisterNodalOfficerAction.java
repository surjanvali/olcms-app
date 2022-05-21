package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;

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

public class RegisterNodalOfficerAction extends DispatchAction {
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
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {

				con = DatabasePlugin.connect();
				// cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' order by sdeptcode", con));


				if(roleId.trim().equals("2"))
				{
					
					String distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
					tableName = AjaxModels.getTableName(distId, con);
					
					/*
					 * if(userId!=null && userId.equals("DC-ATP")) tableName="nic_data_atp"; else
					 * if(userId!=null && userId.equals("DC-CHT")) tableName="nic_data_ctr"; else
					 * if(userId!=null && userId.equals("DC-EG")) tableName="nic_data_eg"; else
					 * if(userId!=null && userId.equals("DC-GNT")) tableName="nic_data_gnt"; else
					 * if(userId!=null && userId.equals("DC-KDP")) tableName="nic_data_kdp"; else
					 * if(userId!=null && userId.equals("DC-KNL")) tableName="nic_data_knl"; else
					 * if(userId!=null && userId.equals("DC-KRS")) tableName="nic_data_krishna";
					 * else if(userId!=null && userId.equals("DC-NLR")) tableName="nic_data_nlr";
					 * else if(userId!=null && userId.equals("DC-PRK")) tableName="nic_data_pksm";
					 * else if(userId!=null && userId.equals("DC-SKL")) tableName="nic_data_sklm";
					 * else if(userId!=null && userId.equals("DC-VSP")) tableName="nic_data_vspm";
					 * else if(userId!=null && userId.equals("DC-VZM")) tableName="nic_data_vznm";
					 * else if(userId!=null && userId.equals("DC-WG")) tableName="nic_data_wg";
					 */
					
					sql="select dept_code,dept_code||'-'||upper(trim(description)) as description from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName
							+") and deptcode!='01'  and  dept_code not in (select dept_id from  nodal_officer_details where dist_id='"+CommonModels.checkStringObject(session.getAttribute("dist_id"))+"') order by sdeptcode,deptcode";
					
				}else {
					sql="select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where display=true and (reporting_dept_code='" + deptCode 
							+ "' or deptcode='"+deptCode+"') and deptcode!='01'  and  dept_code not in (select dept_id from  nodal_officer_details where coalesce(dist_id,0)=0 ) order by sdeptcode,deptcode";
				}
				
				
				
				System.out.println("DEPT SQL:"+sql);
				cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox(sql, con));
				
				if(roleId.trim().equals("2"))
				{
					sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,upper(trim(d.description)) as description,d.dept_code " +
						"from nodal_officer_details a " +
						"inner join ( " +
						"select distinct employee_id,fullname_en from "+tableName+" " +
						") b on (a.employeeid=b.employee_id) " +
						"inner join ( " +
						"select distinct designation_id, designation_name_en from "+tableName+" " +
						") c on (a.designation=c.designation_id) " +
						"inner join dept_new d on (a.dept_id=d.dept_code) " +
						"where a.user_id='"+ userId + "'";
					
					
				}else
				{
					/*
					 sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,upper(trim(d.description)) as description from nodal_officer_details a " +
							" inner join (select distinct employee_id,fullname_en from "+tableName+") b on (a.employeeid=b.employee_id) " +
							" inner join (select distinct designation_id, designation_name_en from "+tableName+" where substring(global_org_name,1,3)='"+ deptcode.substring(0,3)+ "' ) c on (a.designation=c.designation_id)" +
							" inner join dept_new d on (a.dept_id=d.dept_code) " +
							"" +
							"where d.reporting_dept_code='"
							+ userId + "'";
					
					sql="select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,upper(trim(d.description)) as description,d.dept_code"
							+ " from nodal_officer_details a"
							+ " inner join (select distinct employee_id,fullname_en from nic_data) b on (a.employeeid=b.employee_id)"
							+ " inner join (select distinct designation_id, designation_name_en from nic_data) c on (a.designation=c.designation_id)"
							+ " left join dept_new d on (a.dept_id=d.dept_code) where d.reporting_dept_code='"+ userId + "' and a.dist_id is null";
					*/
					
					sql = "select d.dept_code, upper(trim(d.description)) as description,slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en "
							+ "from dept_new d "
							+ "inner join (select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en, a.dept_id from nodal_officer_details a "
							+ "inner join (select distinct employee_id,fullname_en,designation_name_en, designation_id from nic_data) b on (a.employeeid=b.employee_id and a.designation=b.designation_id)"
							+ " "
							// + "inner join (select distinct employee_id,fullname_en from nic_data) b on (a.employeeid=b.employee_id) "
							// + "inner join (select distinct designation_id, designation_name_en from nic_data ) c on (a.designation=c.designation_id) "
							+ "where coalesce(a.dist_id,0)=0 ) b on (d.dept_code = b.dept_id) where (reporting_dept_code='"
							+ deptCode + "' or dept_code='" + deptCode + "') and substr(dept_code,4,2)!='01' and d.display= true   order by 1" + "";
					
				}
				

				System.out.println("SQL:"+sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);

				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("List_data", data);


				request.setAttribute("saveAction", "INSERT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	public ActionForward getHodEmployeeDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		String tableName="nic_data";
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

				String deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				if(roleId.trim().equals("2"))
				{
					String distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
					tableName = AjaxModels.getTableName(distId, con);
					
					/*
					 * if(userId!=null && userId.equals("DC-ATP")) tableName="nic_data_atp"; else
					 * if(userId!=null && userId.equals("DC-CHT")) tableName="nic_data_ctr"; else
					 * if(userId!=null && userId.equals("DC-EG")) tableName="nic_data_eg"; else
					 * if(userId!=null && userId.equals("DC-GNT")) tableName="nic_data_gnt"; else
					 * if(userId!=null && userId.equals("DC-KDP")) tableName="nic_data_kdp"; else
					 * if(userId!=null && userId.equals("DC-KNL")) tableName="nic_data_knl"; else
					 * if(userId!=null && userId.equals("DC-KRS")) tableName="nic_data_krishna";
					 * else if(userId!=null && userId.equals("DC-NLR")) tableName="nic_data_nlr";
					 * else if(userId!=null && userId.equals("DC-PRK")) tableName="nic_data_pksm";
					 * else if(userId!=null && userId.equals("DC-SKL")) tableName="nic_data_sklm";
					 * else if(userId!=null && userId.equals("DC-VSP")) tableName="nic_data_vspm";
					 * else if(userId!=null && userId.equals("DC-VZM")) tableName="nic_data_vznm";
					 * else if(userId!=null && userId.equals("DC-WG")) tableName="nic_data_wg";
					 */
					
					/*cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName
							+")and deptcode!='01'   and  dept_code not in (select dept_id from  nodal_officer_details where dist_id='"+CommonModels.checkStringObject(session.getAttribute("dist_id"))+"') order by sdeptcode,deptcode", con));
				}else
					cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' and deptcode!='01' order by sdeptcode,deptcode", con));
				*/
					sql="select dept_code,dept_code||'-'||upper(trim(description)) as description from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName
							+") and deptcode!='01'  and  dept_code not in (select dept_id from  nodal_officer_details where dist_id='"+CommonModels.checkStringObject(session.getAttribute("dist_id"))+"') order by sdeptcode,deptcode";
					
				}else {
					sql="select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where display=true and reporting_dept_code='" + userId 
							+ "' and deptcode!='01'  and  dept_code not in (select dept_id from  nodal_officer_details where coalesce(dist_id,0)=0 ) order by sdeptcode,deptcode";
				}
				
				
				System.out.println("DEPT SQL:"+sql);
				cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox(sql, con));
				
				
				
				if(CommonModels.NonZeroValidation(deptId)){
					if(roleId.trim().equals("2"))
					{
						sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,upper(trim(d.description)) as description from nodal_officer_details a " +
								"inner join ( " +
								"select distinct employee_id,fullname_en from "+tableName+") b on (a.employeeid=b.employee_id) " +
								"inner join ( " +
								"select distinct designation_id, designation_name_en from "+tableName+"  " +
								") c on (a.designation=c.designation_id) " +
								" inner join dept_new d on (a.dept_id=d.dept_code)  " +
								"where a.user_id='"+ userId+ "' and a.dept_id='" + deptId + "'";
					}else
					{
						sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,upper(trim(d.description)) as description from nodal_officer_details a "
								+ " inner join (select distinct employee_id,fullname_en from "+tableName+") b on (a.employeeid=b.employee_id) "
								+ " inner join (select distinct designation_id, designation_name_en from "+tableName+" where substring(global_org_name,1,3)='"
								+ userId.substring(0, 3)
								+ "' ) c on (a.designation=c.designation_id)"
								+ "  inner join dept_new d on (a.dept_id=d.dept_code)  "
								+ ""
								+ " where d.reporting_dept_code='"
								+ userId
								+ "' and a.dept_id='" + deptId + "'";
						
						sql="select d.dept_code, upper(trim(d.description)) as description,slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en from dept_new d "
								+ "inner join (select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en, a.dept_id from nodal_officer_details a "
								+ "inner join (select distinct employee_id,fullname_en from nic_data) b on (a.employeeid=b.employee_id)   "
								+ "inner join (select distinct designation_id, designation_name_en from nic_data ) c on (a.designation=c.designation_id)   "
								+ "where user_id='"+userId+"' and coalesce(a.dist_id,0)=0) b on (d.dept_code = b.dept_id) where reporting_dept_code='"+userId+"'  and b.dept_id='" + deptId + "' and d.display= true order by 1"
								+ "";
						
					}
					

					System.out.println("SQL:"+sql);
					List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);

					System.out.println("data=" + data);
					if (data != null && !data.isEmpty() && data.size() > 0){
						request.setAttribute("List_data", data);
					}
					else{
						request.setAttribute("saveAction", "INSERT");
					}
				}
				else{
					request.setAttribute("errorMsg", "Invalid Hod.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			cform.setDynaForm(
					"designationList",
					DatabasePlugin
					.getSelectBox(
							"select distinct designation_id::int4, designation_name_en from "+tableName+" where substring(global_org_name,1,5)='"
									+ cform.getDynaForm("deptId")
									+ "'  and trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4 desc",
									con));
			cform.setDynaForm("deptId" , cform.getDynaForm("deptId"));
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}


	public ActionForward saveEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("RegisterNodalOfficerAction..............................................................................saveEmployeeDetails()");
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		String designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null;
		int status = 0;
		String tableName="nic_data";
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
				String distId = CommonModels.checkIntObject(session.getAttribute("dist_id"))+"";
				tableName = AjaxModels.getTableName(distId, con);
				
				/*
				 * if(userId!=null && userId.equals("DC-ATP")) tableName="nic_data_atp"; else
				 * if(userId!=null && userId.equals("DC-CHT")) tableName="nic_data_ctr"; else
				 * if(userId!=null && userId.equals("DC-EG")) tableName="nic_data_eg"; else
				 * if(userId!=null && userId.equals("DC-GNT")) tableName="nic_data_gnt"; else
				 * if(userId!=null && userId.equals("DC-KDP")) tableName="nic_data_kdp"; else
				 * if(userId!=null && userId.equals("DC-KNL")) tableName="nic_data_knl"; else
				 * if(userId!=null && userId.equals("DC-KRS")) tableName="nic_data_krishna";
				 * else if(userId!=null && userId.equals("DC-NLR")) tableName="nic_data_nlr";
				 * else if(userId!=null && userId.equals("DC-PRK")) tableName="nic_data_pksm";
				 * else if(userId!=null && userId.equals("DC-SKL")) tableName="nic_data_sklm";
				 * else if(userId!=null && userId.equals("DC-VSP")) tableName="nic_data_vspm";
				 * else if(userId!=null && userId.equals("DC-VZM")) tableName="nic_data_vznm";
				 * else if(userId!=null && userId.equals("DC-WG")) tableName="nic_data_wg";
				 */
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

					sql = "insert into nodal_officer_details (dept_id, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, dist_id) " +
							"values (?,?,?,?,?,?,?,?,?,now(),?)";

					ps = con.prepareStatement(sql);

					int column = 0;

					ps.setString(++column, (String) cform.getDynaForm("deptId"));
					ps.setString(++column,userId);
					ps.setString(++column, designationId);
					ps.setString(++column, employeeId);
					ps.setString(++column, mobileNo);

					ps.setString(++column, emailId);
					ps.setString(++column, aadharNo);
					ps.setString(++column,
							userId);
					ps.setString(++column, request.getRemoteAddr());
					
					ps.setInt(++column, CommonModels.checkIntObject(session.getAttribute("dist_id")));

					status = ps.executeUpdate();

					if (status == 1){

						if(tableName.equals("nic_data")) {
						
							/*sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, user_type) select a.emailid, md5('olcms@2021'), b.fullname_en, '"
									+ userId + "', now(),'" + request.getRemoteAddr()
									+ "',d.dept_id,d.sdeptcode||d.deptcode as deptcode, 5 from nodal_officer_details a inner join (distinct employee_id,fullname_en,designation_id,designation_name_en, substr(global_org_name,1,5) as dept_code from "
									+ tableName + ") b on (a.employeeid=b.employee_id and a.dept_id=) " + ""
									+ "  inner join dept_new d on (d.dept_code='"
									+ (String) cform.getDynaForm("deptId") + "')" + "" + " where employeeid='" + employeeId
									+ "'";*/
							
							sql="insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, user_type) select a.emailid, md5('olcms@2021'), b.fullname_en, '"
									+ userId + "', now(),'" + request.getRemoteAddr()
									+ "',d.dept_id,d.sdeptcode||d.deptcode as deptcode, 5 from nodal_officer_details a "
									+ "inner join (select distinct employee_id,fullname_en,designation_id,designation_name_en, substr(global_org_name,1,5) as dept_code  from nic_data) b on (a.employeeid=b.employee_id and a.dept_id=b.dept_code and a.designation=b.designation_id) "
									+ "inner join dept_new d on (d.dept_code=b.dept_code) "
									+ " where employeeid='"+employeeId+"' and a.dept_id='"+(String) cform.getDynaForm("deptId")+"'";
							
						}
						else {
							sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, dept_id, dept_code, dist_id, user_type) select a.emailid, md5('olcms@2021'), b.fullname_en, '"
									+ userId + "', now(),'" + request.getRemoteAddr()
									+ "',d.dept_id,d.dept_code as deptcode,"+CommonModels.checkIntObject(session.getAttribute("dist_id"))
									+", 10 from nodal_officer_details a "
									
									// + "inner join (select distinct employee_id,fullname_en from " + tableName + ") b on (a.employeeid=b.employee_id) " + ""
									+ "inner join (select distinct employee_id,fullname_en,designation_id,designation_name_en, substr(global_org_name,1,5) as dept_code  from " + tableName + ") b on (a.employeeid=b.employee_id and a.dept_id=b.dept_code and a.designation=b.designation_id) "
									+ "inner join dept_new d on (d.dept_code=b.dept_code) "
									
									// + " inner join dept_new d on (d.dept_code='"+ (String) cform.getDynaForm("deptId") + "')" 
									+ " inner join district_mst dm on (dm.short_name='"+userId+"')" 
									+ " where employeeid='"+employeeId+"' and a.dept_id='"+(String) cform.getDynaForm("deptId")+"'";
							
							
							
						}
						
						System.out.println("SQL::"+sql);
						
						status+=DatabasePlugin.executeUpdate(sql, con);
						if(tableName.equals("nic_data")) {
							sql="insert into user_roles (userid, role_id) values ('"+emailId+"','5')";
						}
						else {
							sql="insert into user_roles (userid, role_id) values ('"+emailId+"','10')";
						}
						status+=DatabasePlugin.executeUpdate(sql, con);
						System.out.println("STATUS:"+status);
						if(status==3) {
							con.commit();
							// String smsText="Your Userid on Finance Department Portal is:"+emailId+" and olcms@2021 is Password. Please do not share your Userid and Password with Others.  -APFINANCE";
							String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
							String templateId="1007784197678878760";
							SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
	
							request.setAttribute("successMsg", "Nodal Officer details saved & User Login created succesfully. Login details sent to Registered Mobile No.");
						}
						else {
							con.rollback();
							request.setAttribute("errorMsg", "Error while registering Nodal Officer(Legal).");
						}
					}
					else
						request.setAttribute("errorMsg", "Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
			}
			request.setAttribute("saveAction", "INSERT");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
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



	public ActionForward editEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("RegisterNodalOfficerAction..............................................................................editEmployee()");
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		String tableName="nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String mloId=CommonModels.checkStringObject(cform.getDynaForm("mloId"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");

				return mapping.findForward("InvalidAccess");

			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {

				con = DatabasePlugin.connect();
				String distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
				tableName = AjaxModels.getTableName(distId, con);
				
				// sql = "select slno, dept_id, user_id, designation, employeeid, mobileno, emailid, aadharno from nodal_officer_details where user_id='"+ userId + "' and slno='"+mloId+"'";
				/*
				 * if(userId!=null && userId.equals("DC-ATP")) tableName="nic_data_atp"; else
				 * if(userId!=null && userId.equals("DC-CHT")) tableName="nic_data_ctr"; else
				 * if(userId!=null && userId.equals("DC-EG")) tableName="nic_data_eg"; else
				 * if(userId!=null && userId.equals("DC-GNT")) tableName="nic_data_gnt"; else
				 * if(userId!=null && userId.equals("DC-KDP")) tableName="nic_data_kdp"; else
				 * if(userId!=null && userId.equals("DC-KNL")) tableName="nic_data_knl"; else
				 * if(userId!=null && userId.equals("DC-KRS")) tableName="nic_data_krishna";
				 * else if(userId!=null && userId.equals("DC-NLR")) tableName="nic_data_nlr";
				 * else if(userId!=null && userId.equals("DC-PRK")) tableName="nic_data_pksm";
				 * else if(userId!=null && userId.equals("DC-SKL")) tableName="nic_data_sklm";
				 * else if(userId!=null && userId.equals("DC-VSP")) tableName="nic_data_vspm";
				 * else if(userId!=null && userId.equals("DC-VZM")) tableName="nic_data_vznm";
				 * else if(userId!=null && userId.equals("DC-WG")) tableName="nic_data_wg";
				 */
				
				
				sql = "select slno, dept_id, user_id, designation, employeeid, mobileno, emailid, aadharno, fullname_en, designation_name_en from nodal_officer_details  a inner join "+tableName+" b on (a.employeeid=b.employee_id) where user_id='"+ userId + "' and slno='"+mloId+"'";

				System.out.println("SQL:"+sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,con);

				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
				{
					Map myData = (Map)data.get(0);
					cform.setDynaForm("deptId", myData.get("dept_id"));
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
					request.setAttribute("errorMsg", "No details found with the given details.");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			//cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' order by sdeptcode", con));
			if(roleId.trim().equals("2"))
			{
				cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName+")and deptcode!='01' order by sdeptcode,deptcode", con));
			}else
				cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,dept_code||'-'||upper(trim(description)) as description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' and deptcode!='01' order by sdeptcode,deptcode", con));

			cform.setDynaForm(
					"designationList",
					DatabasePlugin
					.getSelectBox(
							"select distinct designation_id::int4, designation_name_en from "+tableName+" where substring(global_org_name,1,5)='"
									+ cform.getDynaForm("deptId")
									+ "'  and trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4 desc ",
									con));

			cform.setDynaForm(
					"employeeList",
					DatabasePlugin
					.getSelectBox(
							"select distinct employee_id, fullname_en from "+tableName+" where substring(global_org_name,1,5)='"
									+ cform.getDynaForm("deptId")
									+ "' and designation_id='"
									+ cform.getDynaForm("designationId")
									+ "' order by fullname_en",
									con));
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
			saveToken(request);
		}

		return mapping.findForward("success");
	}


	public ActionForward updateEmployeeDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out
		.println("RegisterNodalOfficerAction..............................................................................updateEmployeeDetails()");

		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		String designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null;
		int status = 0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String mloId=CommonModels.checkStringObject(cform.getDynaForm("mloId"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");

				return mapping.findForward("InvalidAccess");

			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {
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

					designationId = designationId.trim();
					employeeId = employeeId.trim();
					mobileNo = mobileNo.trim();
					emailId = emailId.trim();
					aadharNo = aadharNo.trim();

					con = DatabasePlugin.connect();

					sql = "insert into nodal_officer_details_bkp (select  *,?,?,now() from nodal_officer_details where slno=? and employeeid=?)";
					ps = con.prepareStatement(sql);
					int column = 0;

					ps.setString(++column, userId);
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);
					ps.setString(++column, employeeId);
					ps.executeUpdate();
					ps = con.prepareStatement(sql);

					sql = "update nodal_officer_details set dept_id=?, user_id=?, designation=?, mobileno=?, emailid=?, aadharno=?, updated_by=?, updated_ip=?, " +
							"updated_time=now() where slno=? and employeeid=?";
					ps = con.prepareStatement(sql);

					column = 0;

					ps.setString(++column, (String) cform.getDynaForm("deptId"));
					ps.setString(++column, userId);
					ps.setString(++column, designationId);
					ps.setString(++column, mobileNo);
					ps.setString(++column, emailId);
					ps.setString(++column, aadharNo);
					ps.setString(++column, userId);
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);
					ps.setString(++column, employeeId);

					status = ps.executeUpdate();

					if (status == 1)
						request.setAttribute("successMsg", "Nodal Officer(Legal) details Registered & Login Credentails created successfully. Login details sent to Registered Mobile no.");
					else
						request.setAttribute("errorMsg", "Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
			}

		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Nodal Officer(Legal) not Registered due to wrong data submitted or Mobile No. (or) email Id (or) Aadhaar has been already registered.");
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
			HttpServletResponse response) throws Exception {

		System.out
		.println("RegisterNodalOfficerAction..............................................................................deleteEmployeeDetails()");

		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		int status = 0;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String mloId=CommonModels.checkStringObject(cform.getDynaForm("mloId"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");

				return mapping.findForward("InvalidAccess");

			} else if (roleId.trim().equals("3") || roleId.trim().equals("2")) {

				if (DatabasePlugin.NonZeroValidation(mloId) == true) {
					con = DatabasePlugin.connect();
					sql = "insert into nodal_officer_details_delete (select  *,?,?,now() from mlo_details where slno=?)";
					ps = con.prepareStatement(sql);
					int column = 0;

					ps.setString(++column, userId);
					ps.setString(++column, request.getRemoteAddr());
					ps.setString(++column, mloId);
					ps.executeUpdate();

					sql = "delete from nodal_officer_details_delete where slno=?";
					ps = con.prepareStatement(sql);
					column = 0;
					ps.setString(++column, mloId);
					ps.executeUpdate();

					status = ps.executeUpdate();

					if (status == 1)
						request.setAttribute("successMsg", "Nodal Officer details deleted succesfully.");
					else
						request.setAttribute("errorMsg", "Failed to delete Nodal Officer details.");
				}
			} else {
				request.setAttribute("errorMsg", "Error: Invalid data is submitted.");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Error: Invalid data is submitted!!!");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return unspecified(mapping, cform, request, response);
	}
}