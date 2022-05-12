package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
				
				con = DatabasePlugin.connect();
				// cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where sdeptcode='" + userId.substring(0,3) + "' order by sdeptcode", con));
				
				if(roleId.trim().equals("2"))
				{
					cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where dept_code in (select distinct substring(global_org_name,1,5) from "+tableName+") and deptcode!='01' order by sdeptcode", con));
				}else
					cform.setDynaForm("deptsList", DatabasePlugin.getSelectBox( "select dept_code,description  from dept_new where reporting_dept_code='" + deptCode + "' and deptcode!='01' order by sdeptcode", con));
				
				if(cform.getDynaForm("officerType")!=null && !CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("0")){
					
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
						cform.setDynaForm(
								"designationList",
								DatabasePlugin
										.getSelectBox(
												"select distinct designation_id::int4, designation_name_en from "+tableName+" where substring(global_org_name,1,5)='"
														+ session.getAttribute("userid")
														+ "' order by designation_id::int4",
												con));
						
						cform.setDynaForm("deptId", userId);
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
		String designationId = null, employeeId = null, mobileNo = null, emailId = null, aadharNo = null, deptCode=null;
		int status = 0;
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
				
				designationId = (String) cform.getDynaForm("designationId");
				employeeId = (String) cform.getDynaForm("employeeId");
				mobileNo = (String) cform.getDynaForm("mobileNo");
				emailId = (String) cform.getDynaForm("emailId");
				aadharNo = (String) cform.getDynaForm("aadharNo");

				if(Integer.parseInt(DatabasePlugin.getSingleValue(con, "select count(*) from nodal_officer_details where employeeid='"+employeeId+"'"))== 0){
				
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

					sql = "insert into nodal_officer_change_requests (dept_id, user_id, designation, employeeid, mobileno, emailid, aadharno, inserted_by, inserted_ip, inserted_time, change_reasons, change_letter_path, officer_type) " +
							"values (?,?,?,?,?,?,?,?,?,now(),?,?,?)";

					ps = con.prepareStatement(sql);

					int column = 0;

					ps.setString(++column, (String) cform.getDynaForm("deptId"));
					ps.setString(++column, userId);
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
					
					if(letterPath !=null)
						status = ps.executeUpdate();

					if (status == 1){
						request.setAttribute("successMsg", "Nodal Officer Change Request registered succesfully.");
					}
					else
						request.setAttribute("errorMsg", "Change Request not registered. Please resubmit once again");
				}else {
					request.setAttribute("errorMsg",
							"Change Request not registered. Please resubmit once again");
				}
			} else{
				request.setAttribute("errorMsg",
						"Change Request not registered. Please resubmit once again");
			}
			}
			
			request.setAttribute("saveAction", "INSERT");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Change Request not registered. Please resubmit once again");
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
	
}
