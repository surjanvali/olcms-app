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

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {

				con = DatabasePlugin.connect();

				cform.setDynaForm("DCLIST", DatabasePlugin
						.getSelectBox("select short_name,upper(district_name) from district_mst order by 1", con));

				if (CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("DNO")) {
					
					String dist=CommonModels.checkStringObject(cform.getDynaForm("districtId"));
					String tableName="";
					
					tableName = AjaxModels.getTableName(CommonModels.checkStringObject(dist), con);
					
					/*
					 * if(dist!=null && dist.equals("DC-ATP")) tableName="nic_data_atp"; else
					 * if(dist!=null && dist.equals("DC-CHT")) tableName="nic_data_ctr"; else
					 * if(dist!=null && dist.equals("DC-EG")) tableName="nic_data_eg"; else
					 * if(dist!=null && dist.equals("DC-GNT")) tableName="nic_data_gnt"; else
					 * if(dist!=null && dist.equals("DC-KDP")) tableName="nic_data_kdp"; else
					 * if(dist!=null && dist.equals("DC-KNL")) tableName="nic_data_knl"; else
					 * if(dist!=null && dist.equals("DC-KRS")) tableName="nic_data_krishna"; else
					 * if(dist!=null && dist.equals("DC-NLR")) tableName="nic_data_nlr"; else
					 * if(dist!=null && dist.equals("DC-PRK")) tableName="nic_data_pksm"; else
					 * if(dist!=null && dist.equals("DC-SKL")) tableName="nic_data_sklm"; else
					 * if(dist!=null && dist.equals("DC-VSP")) tableName="nic_data_vspm"; else
					 * if(dist!=null && dist.equals("DC-VZM")) tableName="nic_data_vznm"; else
					 * if(dist!=null && dist.equals("DC-WG")) tableName="nic_data_wg";
					 */
					

					sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
							+ "inner join (select distinct employee_id,fullname_en,designation_name_en from "+tableName+") nd on (m.employeeid=nd.employee_id)"
							+ "inner join users u on (m.emailid=u.userid)"
							+ "inner join dept_new d on (m.dept_id=d.dept_code)"
							+ "where m.inserted_by='"+dist+"' order by 1";

					request.setAttribute("HEADING", "Nodal Officer (Legal - District Level) Details ");

				} else if (CommonModels.checkStringObject(cform.getDynaForm("officerType")).equals("NO")) {

					sql = "select m.dept_id,upper(d.description) as description,trim(nd.fullname_en) as fullname_en, trim(nd.designation_name_en) as designation_name_en,m.mobileno,m.emailid from nodal_officer_details m "
							+ "    inner join (select distinct employee_id,fullname_en,designation_name_en from nic_data) nd on (m.employeeid=nd.employee_id)"
							+ "    inner join users u on (m.emailid=u.userid)"
							+ "    inner join dept_new d on (m.dept_id=d.dept_code)"
							+ "    where m.inserted_by ilike '%01'" + "    order by 1";

					request.setAttribute("HEADING", "Nodal Officer (Legal) Details ");

				} else {
					sql = "select d.dept_code as dept_id,upper(d.description) as description,nd.fullname_en, nd.designation_name_en,m.mobileno,m.emailid from mlo_details m "
							+ "inner join (select distinct employee_id,fullname_en,designation_name_en from nic_data) nd on (m.employeeid=nd.employee_id)"
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