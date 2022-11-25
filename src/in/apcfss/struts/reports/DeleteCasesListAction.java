package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.struts.util.LabelValueBean;

import plugins.DatabasePlugin;

public class DeleteCasesListAction extends DispatchAction {
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
			
			else  if(roleId.equals("13") || roleId.equals("14")) 
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
		//return mapping.findForward("success");
		return getdata(mapping, cform, request, response);
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
			
			Date curDate = new Date();
		      SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		      
		      String stringDate = format.format(curDate);
		      System.out.println(stringDate);
		      
		      format = new SimpleDateFormat("dd-M-yyyy");
		      stringDate = format.format(curDate);
		      System.out.println("---"+stringDate);
			
			
			String sqlCondition="";

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			else  if(roleId.equals("13") || roleId.equals("14")) 
			{
			
				request.setAttribute("show_flag", "Y");
				
			    con = DatabasePlugin.connect();
			    
			    
			    if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and inserted_time >= to_date('" + cform.getDynaForm("dofFromDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and inserted_time <= to_date('" + cform.getDynaForm("dofToDate")
					+ "','dd-mm-yyyy') ";
				}
				
				//cform.setDynaForm("dofToDate",stringDate);
			     // cform.setDynaForm("dofFromDate",stringDate);
				
				sql="select ack_no,advocatename,advocatename,maincaseno,petitioner_name,to_char(inserted_time,'dd/mm/yyyy') as inserted_time ,inserted_by "
						+ " from ecourts_gpo_ack_dtls_log where 1=1 "+sqlCondition+" order by  inserted_time::timestamp desc";

			System.out.println("SQL:" + sql);
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
					cform.setDynaForm("caseTypesList", DatabasePlugin
							.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
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