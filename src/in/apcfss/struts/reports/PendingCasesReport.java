package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

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

import plugins.DatabasePlugin;

public class PendingCasesReport extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		CommonForm cform = (CommonForm) form;
		try {
			
			//System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			else  if(roleId.equals("17")  || roleId.equals("13") || roleId.equals("14")) 
			{
				request.setAttribute("show_flag", "Y");
			}
			
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			
			try {
				con = DatabasePlugin.connect();
				if(con!=null) {
					if (roleId.equals("2") || roleId.equals("10")) {
						cform.setDynaForm("distList",
								DatabasePlugin.getSelectBox(
										"select upper(district_name) as district_id,upper(district_name) from district_mst where district_id='"
												+ session.getAttribute("dist_id") + "' order by district_name",
												con));
					}
					else {
						sql="select upper(district_name) as district_id,upper(district_name) from district_mst order by 1";
						cform.setDynaForm("distList", DatabasePlugin.getSelectBox(sql, con));
					}

					if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9")
							|| roleId.equals("10")) {
						sql = "select dept_code,dept_code||'-'||upper(description) from dept_new where display=true";
						sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or dept_code='"
								+ session.getAttribute("dept_code") + "')";
						sql += "  order by dept_code ";
						cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(sql, con));
					}
					else {
					cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
							"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
							con));
					}
					cform.setDynaForm("caseTypesList", DatabasePlugin
							.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
					
					cform.setDynaForm("ResAdvList", DatabasePlugin
							.getSelectBox("select distinct res_adv,res_adv from ecourts_case_data where res_adv is not null and res_adv not in('-','&#039;','.','..','...','....','.....','./',';','0','00','000','0000','00000',',','') order by 1 ", con));
					
					
					cform.setDynaForm("categoryServiceList", 
							DatabasePlugin.getSelectBox("SELECT distinct (case when category_service=' ' or category_service is null then 'NON-SERVICE' else category_service end)  as code,(case when category_service=' ' or category_service is null then 'NON-SERVICE' else category_service end) as name FROM ecourts_case_data", con));
					ArrayList selectData = new ArrayList();
					for (int i = 2022; i > 1980; i--) {
						selectData.add(new LabelValueBean(i + "", i + ""));
					}
					cform.setDynaForm("yearsList", selectData);

					/*
					 * cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
					 * cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
					 * cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
					 * cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
					 * cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
					 * cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
					 * cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
					 * cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
					 */

					request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
					DatabasePlugin.closeConnection(con);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		String method="unspecified";
		request.setAttribute("method", method);
		return mapping.findForward("success");
		//return getdata(mapping, cform, request, response);
	}
	
	public ActionForward getdata(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		CommonForm cform = (CommonForm) form;
		try {
			//System.out.println("heiii");
			
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			String sqlCondition="";

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			else  if(roleId.equals("17") || roleId.equals("13") || roleId.equals("14")) 
			{
			
				request.setAttribute("show_flag", "Y");
				
			    con = DatabasePlugin.connect();
			    
			    
			    if(request.getAttribute("method")!=null && request.getAttribute("method").equals("unspecified"))
			    {
			    	cform.setDynaForm("caseTypeId","WP");
				    cform.setDynaForm("regYear","2022");
				   // cform.setDynaForm("categoryServiceId","SERVICE");
				    cform.setDynaForm("categoryServiceId","");
			    }
			    
			    
			    if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("caseTypeId") != null
						&& !cform.getDynaForm("caseTypeId").toString().contentEquals("")
						&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
					sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
							+ "' ";
				}
				if (cform.getDynaForm("districtId") != null
						&& !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and a.dist_name='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
				if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
						&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
					sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear"))
					+ "' ";
				}
				if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
						&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
					sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
				}
				
				if (cform.getDynaForm("categoryServiceId") != null && !cform.getDynaForm("categoryServiceId").toString().contentEquals("")
						&& !cform.getDynaForm("categoryServiceId").toString().contentEquals("0")) {
					
					String subcon="";
					if(cform.getDynaForm("categoryServiceId").equals("NON-SERVICE"))
					{
						subcon=" or a.category_service is null or a.category_service=' '";
					}
					sqlCondition += " and a.category_service='" + cform.getDynaForm("categoryServiceId").toString().trim() + "' "+subcon+" ";
				}
				if (cform.getDynaForm("res_adv_Id") != null && !cform.getDynaForm("res_adv_Id").toString().contentEquals("")
						&& !cform.getDynaForm("res_adv_Id").toString().contentEquals("0")) {
					
					sqlCondition += " and a.res_adv='" + cform.getDynaForm("res_adv_Id").toString().trim() + "' ";
				}

				//
				if (cform.getDynaForm("petitionerName") != null && !cform.getDynaForm("petitionerName").toString().contentEquals("")
						&& !cform.getDynaForm("petitionerName").toString().contentEquals("0")) {
					// sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
					sqlCondition += " and replace(replace(pet_name,' ',''),'.','') ilike  '%"+cform.getDynaForm("petitionerName")+"%'";

				}

				if (cform.getDynaForm("respodentName") != null && !cform.getDynaForm("respodentName").toString().contentEquals("")
						&& !cform.getDynaForm("respodentName").toString().contentEquals("0")) {
					// sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
					sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"+cform.getDynaForm("respodentName")+"%'";

				}
				
				sql="select a.cino,a.type_name_reg as case_type,a.reg_no as main_case_no,a.reg_year as year,a.dept_code,d.description as dept_name,a.dist_name,(case when category_service=' ' or category_service is null then 'NON-SERVICE' else category_service end) as category_service,a.pet_name as petitioner," + 
						" a.pet_adv as petioner_advocate,a.res_name as respondent,a.res_adv as respondent_advocate,"+
					    " case when (p.prayer is not null and coalesce(trim(p.prayer),'')!='' and length(p.prayer) > 2) then substr(p.prayer,1,250) else '-' end as prayer, prayer as prayer_full,"+
					    " (case when a.pend_disp='P' then 'Pending' else 'Disposed' end) as pending_disposed" + 
						" from ecourts_case_data a" + 
						" inner join dept_new d on (a.dept_code=d.dept_code) inner join nic_prayer_data p on(a.cino=p.cino) " +
						" where d.display = true " + sqlCondition+
						" order by dist_name,a.reg_no";

			//System.out.println("SQL r:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
			{
				request.setAttribute("HEADING", "List Of Total Pending cases Against The Government Of Andhra Pradesh Report");
				request.setAttribute("data", data);
			}
			else
				request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {
				con = DatabasePlugin.connect();
				if(con!=null) {
					if (roleId.equals("2") || roleId.equals("10")) {
						cform.setDynaForm("distList",
								DatabasePlugin.getSelectBox(
										"select upper(district_name) as district_id,upper(district_name) from district_mst where district_id='"
												+ session.getAttribute("dist_id") + "' order by district_name",
												con));
					}
					else {
						sql="select upper(district_name) as district_id,upper(district_name) from district_mst order by 1";
						cform.setDynaForm("distList", DatabasePlugin.getSelectBox(sql, con));
					}

					if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9")
							|| roleId.equals("10")) {
						sql = "select dept_code,dept_code||'-'||upper(description) from dept_new where display=true";
						sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or dept_code='"
								+ session.getAttribute("dept_code") + "')";
						sql += "  order by dept_code ";
						cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(sql, con));
					}
					else {
					cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
							"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
							con));
					}
					cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
					
					cform.setDynaForm("ResAdvList", DatabasePlugin
							.getSelectBox("select distinct res_adv,res_adv from ecourts_case_data where res_adv is not null and res_adv not in('-','&#039;','.','..','...','....','.....','./',';','0','00','000','0000','00000',',','') order by 1 ", con));
					
					
					cform.setDynaForm("categoryServiceList", 
							DatabasePlugin.getSelectBox("SELECT distinct (case when category_service=' ' or category_service is null then 'NON-SERVICE' else category_service end)  as code,(case when category_service=' ' or category_service is null then 'NON-SERVICE' else category_service end) as name FROM ecourts_case_data", con));
					
					
					ArrayList selectData = new ArrayList();
					for (int i = 2022; i > 1980; i--) {
						selectData.add(new LabelValueBean(i + "", i + ""));
					}
					cform.setDynaForm("yearsList", selectData);

					cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
					cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
					cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
					cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
					cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
					cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
					cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
					cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

					request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
					DatabasePlugin.closeConnection(con);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		return mapping.findForward("success");
	}

}