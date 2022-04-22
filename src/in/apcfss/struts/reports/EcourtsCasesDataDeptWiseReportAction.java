package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

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

import plugins.DatabasePlugin;

public class EcourtsCasesDataDeptWiseReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
			/*else if (!(roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7"))*/
			
			
			
			else{
				
				con = DatabasePlugin.connect();
					
				if(roleId.trim().equals("3"))
				{
					sql="select a.dept_id,d.sdeptcode||d.deptcode as deptshortname ,upper(trim(d.description)) as description,count(distinct cino) total, sum(case when assigned is true then 1 else 0 end) as assigned "
							+ "from ecourts_case_data a inner join dept d on (a.dept_code=d.sdeptcode||d.deptcode) where d.sdeptcode='"+userId.substring(0,3)+"'"
							+ "group by a.dept_id,d.description,d.sdeptcode||d.deptcode "
							+ "order by d.sdeptcode||d.deptcode";
					getHODWiseReport(mapping, form, request, response);
				}
				else
				
					sql="select a.dept_id,d.sdeptcode||d.deptcode as deptshortname ,upper(trim(d.description)) as description,count(a.*) total, sum(case when assigned is true then 1 else 0 end) as assigned "
							+ "from ecourts_case_data a inner join dept d on (a.dept_code=d.sdeptcode||d.deptcode) "
							+ "group by a.dept_id,d.description,d.sdeptcode||d.deptcode "
							+ "order by d.sdeptcode||d.deptcode";
				
					request.setAttribute("HEADING", "Department wise Highcourt Cases Abstract Report");
				System.out.println("SQL:"+sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("DEPTWISEHCCASES", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getHODWiseReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null, sectDept="";
		String tableName="nic_data";
		CommonForm cform = (CommonForm) form;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			sectDept = CommonModels.checkStringObject(cform.getDynaForm("deptId"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7")) {
				
				con = DatabasePlugin.connect();
				
				if (roleId.trim().equals("3")) {
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers from dept d" +
							" left join nodal_officer_details a on (a.inserted_by=d.sdeptcode||d.deptcode)" +
							" where a.inserted_by='"+userId+"' " +
							" group by inserted_by, d.description, d.sdeptcode||d.deptcode order by 4 desc, d.description";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept d where d.sdeptcode||d.deptcode='"+userId+"'", con));
					
				}
				else if (roleId.trim().equals("2")) {
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers from dept d" +
							" left join nodal_officer_details a on (a.inserted_by=d.sdeptcode||d.deptcode)" +
							" where a.inserted_by='"+userId+"' " +
							" group by inserted_by, d.description, d.sdeptcode||d.deptcode order by 4 desc, d.description";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for District");
					
				}
				else if (roleId.trim().equals("1") || roleId.trim().equals("7")) {
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers from dept d" +
							" left join nodal_officer_details a on (a.inserted_by=d.sdeptcode||d.deptcode)" +
							// " where d.deptcode='01' " +
							" group by inserted_by, d.description, d.sdeptcode||d.deptcode order by 4 desc, d.description";
					
					
					
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers "
							+ "from (select * from dept where sdeptcode='"+sectDept.substring(0, 3)+"') d "
							+ "left join nodal_officer_details a on (a.inserted_by=d.sdeptcode||d.deptcode) "
							+ "group by inserted_by, d.description, d.sdeptcode||d.deptcode "
							+ "order by 4 desc, d.description";
				
					//request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report");
					request.setAttribute("HEADING", "HOD wise Nodal Officer (Legal) Registered Abstract Report,<br /> Department : "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept d where d.sdeptcode||d.deptcode='"+sectDept+"'", con));
				}
				System.out.println("SQL:"+sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("HODDEPTWISENOS", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	
	public ActionForward getOfficerWise(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null, deptId=null;
		String tableName="nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7")) {
				
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				con = DatabasePlugin.connect();
				if (roleId.trim().equals("3")) {
					
					sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_details a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " where substring(global_org_name,1,3)='"
							+ userId.substring(0, 3)
							+ "' ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.dept_id=d.sdeptcode||d.deptcode) "
							+ " "
							+ " where a.user_id='"
							+ userId
							+ "' ";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept d where d.sdeptcode||d.deptcode='"+deptId+"'", con));
					
				}
				else if (roleId.trim().equals("2")) {
					
					sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_details a " +
							"inner join ( " +
							"select distinct employee_id,fullname_en from "+tableName+") b on (a.employeeid=b.employee_id) " +
							"inner join ( " +
							"select distinct designation_id, designation_name_en from "+tableName+"  " +
							") c on (a.designation=c.designation_id) " +
							"inner join dept d on (a.dept_id=d.sdeptcode||d.deptcode) " +
							"where a.user_id='"+ userId+ "' and a.dept_id='" + deptId + "'";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for District and "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept d where d.sdeptcode||d.deptcode='"+deptId+"'", con));
					
				}
				else if (roleId.trim().equals("1") || roleId.trim().equals("7")) {
					
					sql = "select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en,d.description from nodal_officer_details a "
							+ " inner join (select distinct employee_id,fullname_en from "
							+ tableName
							+ ") b on (a.employeeid=b.employee_id) "
							+ " inner join (select distinct designation_id, designation_name_en from "
							+ tableName
							+ " ) c on (a.designation=c.designation_id)"
							+ " inner join dept d on (a.dept_id=d.sdeptcode||d.deptcode) "
							+ " where user_id='" + deptId + "'";
				
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report,<br /> HOD : "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept d where d.sdeptcode||d.deptcode='"+deptId+"'", con));
				}
				
				System.out.println("SQL:" + sql);
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("EMPWISENOS", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}