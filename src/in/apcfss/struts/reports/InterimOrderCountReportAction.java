package in.apcfss.struts.reports;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class InterimOrderCountReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null, sqlCondition = "",condition="",deptName="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String show_flag="N";
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {
				con = DatabasePlugin.connect();
				if ((roleId.equals("6"))) {
					condition += " inner join ecourts_mst_gp_dept_map egm on (egm.dept_code=d.dept_code) ";
					sqlCondition += " and egm.gp_id='" + userId + "'";
				}
				
				if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				{
					
					deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
					deptName = DatabasePlugin.getStringfromQuery(
							"select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
					
					sqlCondition+=" and (d.reporting_dept_code='"+deptId+"' or b.dept_code='"+deptId+"')";
					
				}

				if (roleId.equals("2") || roleId.equals("10")) 
				{
					sqlCondition += " and a.dist_code='" + session.getAttribute("dist_id") + "' ";
					cform.setDynaForm("districtId", session.getAttribute("dist_id"));
				}
				if( roleId.equals("7"))
				{
					
				}
				
				
				if(roleId!=null)
				{
				

				//int yearId = CommonModels.checkIntObject(request.getParameter("yearId"));

				/*
				 * sql =
				 * "select a.dept_code,d.description as dept_name,sum(case when a.dept_code is not null then 1 else 0 end ) as dept_count, "
				 * +
				 * " sum(case when a.instructions is not null and a.instructions!='' then 1 else 0 end ) as instructions_count, "
				 * +
				 * " sum(case when a.reply_instructions is not null and a.reply_instructions!='' then 1 else 0 end ) as reply_instructions_count  "
				 * + " from  " + " ecourts_dept_instructions a  " + " inner join  " +
				 * " dept_new d on(a.dept_code=d.dept_code) "+condition+" " +
				 * " where 1=1 "+sqlCondition+" " + " group by 1,2 order by dept_code ";
				 */
				sql = " select distinct x.reporting_dept_code AS dept_code,d1.description  AS dept_name,'S' as flag,"
						+ " sum(order_document_path) as order_document_path  "
						+ " from(select b.dept_code,case when reporting_dept_code='CAB01'  "
						+ " then d.dept_code else reporting_dept_code end as reporting_dept_code,count(order_document_path) as order_document_path "
						+ " from ecourts_case_interimorder a inner join ecourts_case_data b on(a.cino=b.cino) "
						+ " inner join  dept_new d on(b.dept_code=d.dept_code) "+condition+" "
						+ " where 1=1 "+sqlCondition+" "
						+ " group by b.dept_code,d.dept_code,reporting_dept_code order by b.dept_code ) x "
						+ " inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code) "
						+ " GROUP BY "
						+ "	1,2 order by 1";
				

				request.setAttribute("HEADING", "Interim Order Count Report");

				System.out.println("SQL:==>" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("sec_show", "Secretariat ");
					request.setAttribute("DATA", data);
					show_flag="Y";
				}else {
					request.setAttribute("errorMsg", "No Records found to display"); 
				}
				
			}
		  }
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getDetailsFromSecDept(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null, sqlCondition = "",condition="",deptName="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String show_flag="N";
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {
				con = DatabasePlugin.connect();
				if ((roleId.equals("6"))) {
					condition += " inner join ecourts_mst_gp_dept_map egm on (egm.dept_code=d.dept_code) ";
					sqlCondition += " and egm.gp_id='" + userId + "'";
				}
				

				if (roleId.equals("2") || roleId.equals("10")) 
				{
					sqlCondition += " and a.dist_code='" + session.getAttribute("dist_id") + "' ";
					cform.setDynaForm("districtId", session.getAttribute("dist_id"));
				}
				if( roleId.equals("7"))
				{
					
				}
				
				
				if(roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
				{
					deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
					deptName = DatabasePlugin.getStringfromQuery("select upper(description) as description from dept_new where dept_code='"+deptId+"'", con);
					// CommonModels.checkStringObject(session.getAttribute("dept_code"));
				}
				else {
					deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
					deptId = !deptId.equals("") ? deptId : CommonModels.checkStringObject(request.getParameter("deptId"));
					deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
					deptName = !deptName.equals("") ? deptName : CommonModels.checkStringObject(request.getParameter("deptName"));
				}
				
				
				if(roleId!=null)
				{
					sqlCondition+=" and (d.reporting_dept_code='"+deptId+"' or b.dept_code='"+deptId+"')";
					/*if(request.getParameter("flag")!=null && request.getParameter("flag").equals("S"))
					{
						sqlCondition+=" and (d.reporting_dept_code='"+deptId+"' or a.dept_code='"+deptId+"')";
					}else {
						sqlCondition+=" and (a.dept_code='"+deptId+"')";
					}*/
				//int yearId = CommonModels.checkIntObject(request.getParameter("yearId"));

				/*
				 * sql =
				 * "select a.dept_code,d.description as dept_name,sum(case when a.dept_code is not null then 1 else 0 end ) as dept_count, "
				 * +
				 * " sum(case when a.instructions is not null and a.instructions!='' then 1 else 0 end ) as instructions_count, "
				 * +
				 * " sum(case when a.reply_instructions is not null and a.reply_instructions!='' then 1 else 0 end ) as reply_instructions_count  "
				 * + " from  " + " ecourts_dept_instructions a  " + " inner join  " +
				 * " dept_new d on(a.dept_code=d.dept_code) "+condition+" " +
				 * " where 1=1 "+sqlCondition+" " + " group by 1,2 order by dept_code ";
				 */
				sql = " select b.dept_code,d.description as dept_name,'N' as flag,"
						+ " count(order_document_path) as order_document_path  "
						+ " from ecourts_case_interimorder a inner join ecourts_case_data b on(a.cino=b.cino)  "
						+ "inner join  dept_new d on(b.dept_code=d.dept_code) "+condition+" "
						+ " where 1=1 "+sqlCondition+" "
						+ " group by 1,2 order by b.dept_code ";
				

				request.setAttribute("HEADING", "Interim Order Count Report");

				System.out.println("SQL:==>" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("sec_show", "");
					request.setAttribute("DATA", data);
					show_flag="Y";
				}else {
					request.setAttribute("errorMsg", "No Records found to display"); 
				}
				
			}
		  }
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}


	public ActionForward viewdata(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", 
				heading=null,sqlCondition="",deptName="",leftcondition="";
        String target="success";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			/*
			 * deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			 * deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			 * 
			 * empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			 * empSection =
			 * CommonModels.checkStringObject(session.getAttribute("empSection")); empPost =
			 * CommonModels.checkStringObject(session.getAttribute("empPost")); distId =
			 * CommonModels.checkStringObject(session.getAttribute("dist_id"));
			 */

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			
			if ((roleId.equals("6"))) {
				leftcondition += " inner join ecourts_mst_gp_dept_map egm on (egm.dept_code=d.dept_code) ";
				sqlCondition += " and egm.gp_id='" + userId + "'";
			}
			
			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
			{
				
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery(
						"select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
				
				
				
			}

			if (roleId.equals("2") || roleId.equals("10")) 
			{
				sqlCondition += " and a.dist_code='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}
			if( roleId.equals("7"))
			{
				
			}
			

			if(roleId!=null) { // GPO

				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				String viewDisplay = CommonModels.checkStringObject(request.getParameter("SHOWPOPUP"));
				deptId = CommonModels.checkStringObject(request.getParameter("deptId"));
				
				//sqlCondition+=" and (d.reporting_dept_code='"+dept_code+"' or a.dept_code='"+dept_code+"')";
				
				if(request.getParameter("flag")!=null && request.getParameter("flag").equals("S"))
				{
					sqlCondition+=" and (d.reporting_dept_code='"+deptId+"' or b.dept_code='"+deptId+"')";
				}else {
					sqlCondition+=" and (b.dept_code='"+deptId+"')";
				}
				if(!viewDisplay.equals("") && viewDisplay.equals("SHOWPOPUP")) {
					target = "datapopupview";
				}
				
				condition+=" where b.dept_code is not null "+sqlCondition+"";

				
				
				heading = "Interim Order View Report";
				// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
				condition+="  and a.order_document_path is not null and a.order_document_path!=''";
			
			}

			sql = "select b.dept_code,d.description as dept_name,mobile_no,a.cino,order_document_path   "
					+ " from   "
					+ " ecourts_case_interimorder a  inner join ecourts_case_data b on(a.cino=b.cino)  "
					+ " inner join   "
					+ " dept_new d on(b.dept_code=d.dept_code) "+leftcondition+" "+condition+" order by b.dept_code,cino";

			
			request.setAttribute("HEADING", heading);

			String show_flag="N";

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				
				request.setAttribute("CASEWISEDATA", data);
				show_flag="Y";
			}else {
				request.setAttribute("errorMsg", "No Records found to display"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("datapopupview");
	}
	
	//@SuppressWarnings("rawtypes")
	public ActionForward getRequestedData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		//Connection con=null;
		//ResultSet rs = null;
		//Statement st = null;
		
		//String str="";
		StringBuffer str = new StringBuffer("{");
		StringBuffer tempStr = new StringBuffer("");
		try
		{
			//con = DatabasePlugin.connect();
			//st = con.createStatement();
			//GetQueryDetails gqd=new GetQueryDetails();
			
			//String district_code = request.getParameter("district_code");
			
			
			// Mandals
			//ArrayList mandalsListMap=gqd.getQuery_ArraylistParam_map("select MANDAL_CODE as \"value\", MANDAL_NAME as \"label\" from CSE_MANDAL_MST where FLAG = 'Y' and DISTRICT_CODE = '"+district_code +"' order by MANDAL_NAME");
			boolean status=true;
			if(status)
			{
				System.out.println("status=====>"+status);
				/*
				 * tempStr.append("<option value='0'>Select Mandal</option>"); for(Object
				 * mandalsList:mandalsListMap) { HashMap map1=(HashMap) mandalsList;
				 * tempStr.append("<option value='"+map1.get("value")+"'>"+map1.get("label")+
				 * "</option>"); }
				 */
				tempStr.append("6");
				str.append(" \"data_exists\":\"true\", \"data_list\":\" "+tempStr+" \" ");
			}
			else
				str.append(" \"data_exists\":\"false\" ");
			
			// Divisions
			/*
			 * ArrayList divisionsListMap=gqd.
			 * getQuery_ArraylistParam_map("select DIVISION_CODE as \"value\", DIVISION_NAME as \"label\" from CSE_DIVISION_MST where FLAG = 'Y' and DISTRICT_CODE = '"
			 * +district_code +"' order by DIVISION_NAME");
			 * 
			 * tempStr = new StringBuffer(""); if(divisionsListMap.size()>0) {
			 * tempStr.append("<option value='0'>Select Division</option>"); for(Object
			 * divisionsList:divisionsListMap) { HashMap map1=(HashMap) divisionsList;
			 * tempStr.append("<option value='"+map1.get("value")+"'>"+map1.get("label")+
			 * "</option>"); }
			 * str.append(", \"divisions_exists\":\"true\",  \"divisions_list\":\" "
			 * +tempStr+" \" "); } else str.append(", \"divisions_exists\":\"false\" ");
			 */
			
			str.append("} ");
			
			
			response.getWriter().println(str.toString());
		}
		catch(Exception e)
		{
		}
		finally
		{
			/*
			 * if(st!=null) st.close(); if(con!=null) con.close();
			 */
		}
		return null;
	}

}
