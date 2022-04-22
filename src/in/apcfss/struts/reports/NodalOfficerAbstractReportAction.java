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

public class NodalOfficerAbstractReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null, deptCode=null, deptId=null, deptName=null;
		// String tableName="nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptName = CommonModels.checkStringObject(session.getAttribute("dept_desc"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("1") || roleId.trim().equals("7")||roleId.trim().equals("3")||roleId.trim().equals("4"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("1") || roleId.trim().equals("7")||roleId.trim().equals("3")||roleId.trim().equals("4")) {
				
				con = DatabasePlugin.connect();
				String heading ="Nodal Officer (Legal) Registered Abstract Report";
					
					sql="select sdeptcode||deptcode as deptcode,upper(description) as deptname, coalesce(hods,0) as hods, coalesce(nodalofficers,0) as nodalofficers"
							+ " from dept_new d"
							+ " left join (select sdeptcode,count(*) as hods from dept where deptcode!='01' group by sdeptcode) b using (sdeptcode)"
							+ " left join (select user_id,count(*) as nodalofficers from nodal_officer_details where user_id ilike '%01%' group by user_id) nd on (nd.user_id=sdeptcode||deptcode)"
							+ " where deptcode='01'"
							+ " ";
					
					sql="select a1.dept_code as deptcode,upper(description) as deptname, coalesce(hods,0) as hods, coalesce(nodalofficers,0) as nodalofficers "
							+ "from (select dept_code,description from dept_new where deptcode='01' and display=true and dept_id is not null) a1 "
							+ "left join(select reporting_dept_code,count(*) as hods from dept_new where deptcode!='01' and display=true group by reporting_dept_code) a2 on (a1.dept_code=a2.reporting_dept_code) "
							+ "join (select user_id,count(*) as nodalofficers from nodal_officer_details where user_id ilike '%01%' and dist_id is null group by user_id) a3 on (a1.dept_code=a3.user_id)"
							+ " where 1=1 ";
					
					sql="select a1.dept_code as deptcode,upper(description) as deptname, coalesce(hods,0) as hods, coalesce(nodalofficers,0) as nodalofficers"
							+ " from (select dept_code,description from dept_new where deptcode='01' and display=true and dept_id is not null) a1"
							+ " left join ("
							+ " select reporting_dept_code,count(dn.*) as hods,count(nd.*) as nodalofficers from dept_new dn left join nodal_officer_details nd on (dn.dept_code=nd.dept_id)"
							+ "	where dn.display=true and dn.deptcode!='01' and coalesce(nd.dist_id,0)=0 group by reporting_dept_code"
							+ "	) a2 on (a1.dept_code=a2.reporting_dept_code) where 1=1 ";
					
					if(roleId.trim().equals("3")||roleId.trim().equals("4")) {
						sql+=" and a1.dept_code='"+deptCode+"' ";
						
						heading +=" - "+deptName;
						
					}
					
					sql+=" order by 1";
				
					request.setAttribute("HEADING", heading);
				
				System.out.println("SQL:"+sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("DEPTWISENOS", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
			
			if(roleId!=null && roleId.equals("7")) {
				request.setAttribute("SHOWNOTREGBTN", "SHOWNOTREGBTN");
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
					sql="select a.inserted_by,d.dept_code,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers from dept_new d" +
							" left join nodal_officer_details a on (a.inserted_by=d.reporting_dept_code)" +
							" where a.inserted_by='"+userId+"' and d.display=true " +
							" group by inserted_by, d.description, d.dept_code order by 4 desc, d.description";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept_new d where d.dept_code='"+userId+"'", con));
					
				}
				else if (roleId.trim().equals("2")) {
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers from dept_new d" +
							" left join nodal_officer_details a on (a.inserted_by=d.reporting_dept_code)" +
							" where a.inserted_by='"+userId+"'  and d.display=true" +
							" group by inserted_by, d.description, d.sdeptcode||d.deptcode order by 4 desc, d.description";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report for District");
					
				}
				else if (roleId.trim().equals("1") || roleId.trim().equals("7")) {
					
					
					sql="select a.inserted_by,d.sdeptcode||d.deptcode,upper(d.description) as dept_desc,count(a.*) as registered_nodal_officers "
							+ "from (select * from dept_new where reporting_dept_code='"+sectDept+"' and and d.display=true) d "
							+ "left join nodal_officer_details a on (a.inserted_by=d.reporting_dept_code) "
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
		String tableName="nic_data";
		String userId = null, roleId = null, sql=null, deptCode=null, deptId=null, deptName=null;
		// String tableName="nic_data";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptName = CommonModels.checkStringObject(session.getAttribute("dept_desc"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			else if (!(roleId.trim().equals("1") || roleId.trim().equals("7")||roleId.trim().equals("3")||roleId.trim().equals("4"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("1") || roleId.trim().equals("7")||roleId.trim().equals("3")||roleId.trim().equals("4")) {
				
				
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				con = DatabasePlugin.connect();
				
				/*if (roleId.trim().equals("3")) {
					
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
				*/
					
				sql = "select d.dept_code, upper(trim(d.description)) as description,slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en "
						+ "from dept_new d "
						+ "left join (select slno, user_id, designation, employeeid, mobileno, emailid, aadharno, b.fullname_en, designation_name_en, a.dept_id from nodal_officer_details a   "
						+ "inner join (select distinct employee_id,fullname_en from " + tableName
						+ ") b on (a.employeeid=b.employee_id)   "
						+ "inner join (select distinct designation_id, designation_name_en from nic_data ) c on (a.designation=c.designation_id)   "
						+ "where user_id='" + deptId
						+ "' ) b on (d.dept_code = b.dept_id) where reporting_dept_code='" + deptId
						+ "' and d.display= true order by 1";
					
					request.setAttribute("HEADING", "Nodal Officer (Legal) Registered Abstract Report,<br /> HOD : "+DatabasePlugin.getStringfromQuery("select initcap(d.description) from dept_new d where d.sdeptcode||d.deptcode='"+deptId+"'", con));
				// }
				
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
	
	public ActionForward getNotRegistered(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else if (!(roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7"))) {
				request.setAttribute("errorMsg", "Unauthorized to access this service");
				return mapping.findForward("InvalidAccess");
			} else if (roleId.trim().equals("3") || roleId.trim().equals("2") || roleId.trim().equals("1")
					|| roleId.trim().equals("7")) {

				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				con = DatabasePlugin.connect();

					sql = "select d.sdeptcode||d.deptcode as dept_code,upper(description) as description,d.dept_id from nodal_officer_details nd right join dept d on (nd.dept_id=d.sdeptcode||d.deptcode and d.deptcode!='01')"
							+ " where nd.dept_id is null and d.deptcode!='01' and user_id ilike '%01%' order by d.sdeptcode,d.deptcode";

					request.setAttribute("HEADING", "Nodal Officer (Legal) Not Registered Departments ");

				System.out.println("SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("NONOTREGDATA", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}