package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
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

public class OfficersRegisteredReport extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String dist="";

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {

				con = DatabasePlugin.connect();
				cform.setDynaForm("DCLIST", DatabasePlugin.getSelectBox("select district_id,upper(district_name) from district_mst order by district_name", con));
				 dist=CommonModels.checkStringObject(cform.getDynaForm("districtId"));

				if ((CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("DNO") && dist.equals("ALL") )) {
					
					
					 System.out.println("distdist..."+dist);
					
					sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
							+ "inner join (select distinct employee_id,fullname_en,designation_name_en, designation_id from nic_data) nd on (m.employeeid=nd.employee_id and m.designation=nd.designation_id)"
							+ "inner join users u on (m.emailid=u.userid)"
							+ "inner join dept_new d on (m.dept_id=d.dept_code)";
							

					request.setAttribute("HEADING", "Nodal Officer (Legal - District Level) Details ");

						} 
				
				else if (CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("DNO") ) {
					String tableName="";
					tableName = AjaxModels.getTableName(CommonModels.checkStringObject(dist), con);

					sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
							+ "inner join (select distinct employee_id,fullname_en,designation_name_en, designation_id from "+tableName+") nd on (m.employeeid=nd.employee_id and m.designation=nd.designation_id)"
							+ "inner join users u on (m.emailid=u.userid)"
							+ "inner join dept_new d on (m.dept_id=d.dept_code)"
							+ "where m.dist_id='"+dist+"' order by 1";

					request.setAttribute("HEADING", "Nodal Officer (Legal - District Level) Details ");

				} 
				
				else if (CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("NO")) {

					sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
							+ "    inner join (select distinct employee_id,fullname_en,designation_name_en from nic_data) nd on (m.employeeid=nd.employee_id)"
							+ "    inner join users u on (m.emailid=u.userid)"
							+ "    inner join dept_new d on (m.dept_id=d.dept_code)"
							+ "    where m.inserted_by ilike '%01'" + "    order by 1";

					request.setAttribute("HEADING", "Nodal Officer (Legal) Details ");

				} 
				
				
			 else if (CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("MLOSUBJECT")) {

				 		sql="select d.dept_code as dept_id,upper(d.description) as description,b.fullname_en, b.designation_name_en,m.mobileno,m.emailid from mlo_subject_details m " + 
						" inner join (select distinct employee_id,fullname_en,designation_id, designation_name_en from nic_data) b" + 
						" on (m.employeeid=b.employee_id and m.designation=b.designation_id)" + 
						" inner join users u on (m.emailid=u.userid)" + 
						" inner join dept_new d on (m.user_id=d.dept_code) order by 1";

				request.setAttribute("HEADING", "Middle Level Officers (MLO Subject) Details ");

			} 
				else {
					sql = "select d.dept_code as dept_id,upper(d.description) as description,b.fullname_en, b.designation_name_en,m.mobileno,m.emailid from mlo_details m "
							//+ "inner join (select distinct employee_id,fullname_en,designation_id, designation_name_en from nic_data) b on (m.employeeid=b.employee_id and m.designation=b.designation_id)"
							+" inner join (select distinct employee_id,fullname_en,designation_id, designation_name_en,email from nic_data ) b on ((trim(m.emailid)=trim(b.email)) or (m.employeeid=b.employee_id and m.designation=b.designation_id)) "
							+ "inner join users u on (m.emailid=u.userid)"
							+ "inner join dept_new d on (m.user_id=d.dept_code)" + "order by 1";

					request.setAttribute("HEADING", "Middle Level Officers (Legal) Details ");
					cform.setDynaForm("officerType", "MLO");
				}

				System.out.println("SQL:" + sql);

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("EMPWISEDATA", data);
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