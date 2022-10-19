package in.apcfss.struts.reports;

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

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class HCOrdersIssuedReport extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null,sqlCondition="";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			
			if (cform.getDynaForm("fromDate") != null
					&& !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null
					&& !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time <= to_date('" + cform.getDynaForm("toDate")
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
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			
			System.out.println("---"+cform.getDynaForm("districtId"));
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear"))
						+ "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("petitionerName") != null
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("")
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(pet_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("petitionerName") + "%'";

			}

			if (cform.getDynaForm("respodentName") != null
					&& !cform.getDynaForm("respodentName").toString().contentEquals("")
					&& !cform.getDynaForm("respodentName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("respodentName") + "%'";

			}
			
			if (cform.getDynaForm("judgeName") != null
					&& !cform.getDynaForm("judgeName").toString().contentEquals("")
					&& !cform.getDynaForm("judgeName").toString().contentEquals("0")) {
				sqlCondition += " and coram  ilike  '%"
						+ cform.getDynaForm("judgeName") + "%'";

			}
			
			System.out.println("--"+cform.getDynaForm("deptId"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			else if (roleId.equals("5") || roleId.equals("9")) {

				return HODwisedetails(mapping, form, request, response);
			} else // if(roleId.equals("3") || roleId.equals("4"))
			{
				con = DatabasePlugin.connect();
				
				sql="select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases, sum(interim_order_cases) as interim_order_cases, sum(final_order_cases) as final_order_cases,  "
						+ " sum(interim_orders)  as interim_orders, sum(final_orders) as final_orders from "
						+ "(select a.dept_code , case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,"
						+ " count(*) as total_cases, count(distinct io.cino) as interim_order_cases, count(distinct fo.cino) as final_order_cases,sum(coalesce(interim_orders,'0')::int4)  as interim_orders, sum(coalesce(final_orders,'0')::int4) as final_orders "
						+ " from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
						+ " left join (select cino, count(*) as interim_orders from ecourts_case_interimorder where 1=1  group by cino) io on (a.cino=io.cino) "  //"+sqlCondition+"
						+ " left join (select cino, count(*) as final_orders from ecourts_case_finalorder  where 1=1   group by cino) fo on (a.cino=fo.cino) "  //"+sqlCondition+"
						+ " where d.display = true "+sqlCondition+" ";
				
				if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9") || roleId.equals("10"))
					sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
							+ session.getAttribute("dept_code") + "')";
				else if(roleId.equals("2") || roleId.equals("10")){
					sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
				}

				sql += " group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
						+ " group by x.reporting_dept_code, d1.description order by 1";

				request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				//System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("secdeptwise", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			if (con != null) {
				if (roleId.equals("2") || roleId.equals("10")) {
					cform.setDynaForm("distList",
							DatabasePlugin.getSelectBox(
									"select district_id,upper(district_name) from district_mst where district_id='"
											+ session.getAttribute("dist_id") + "' order by district_name",
											con));
				}
				else {
					sql="select district_id,upper(district_name) from district_mst order by 1";
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
				cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
						"select case_short_name,case_full_name from case_type_master order by sno", con));
				cform.setDynaForm("JudgeList", DatabasePlugin.getSelectBox(
						"select distinct coram,coram from ecourts_case_data where coram!='' order by coram", con));
				ArrayList selectData = new ArrayList();
				for (int i = 2022; i > 1980; i--) {
					selectData.add(new LabelValueBean(i + "", i + ""));
				}
				cform.setDynaForm("yearsList", selectData);

				cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
				cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
				cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
				cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
				cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
				cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
				cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
				cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
				cform.setDynaForm("judgeName", cform.getDynaForm("judgeName"));
				request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
				DatabasePlugin.closeConnection(con);
		}
		}
		return mapping.findForward("success");
	}
	
	public ActionForward HODwisedetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, deptId = null, deptName="",sqlCondition="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			if (cform.getDynaForm("fromDate") != null
					&& !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null
					&& !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time <= to_date('" + cform.getDynaForm("toDate")
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
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and b.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear"))
						+ "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("petitionerName") != null
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("")
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(pet_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("petitionerName") + "%'";

			}

			if (cform.getDynaForm("respodentName") != null
					&& !cform.getDynaForm("respodentName").toString().contentEquals("")
					&& !cform.getDynaForm("respodentName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("respodentName") + "%'";

			}
			if (cform.getDynaForm("judgeName") != null
					&& !cform.getDynaForm("judgeName").toString().contentEquals("")
					&& !cform.getDynaForm("judgeName").toString().contentEquals("0")) {
				sqlCondition += " and coram ilike  '%"
						+ cform.getDynaForm("judgeName") + "%'";

			}

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
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

			
			/*
			 * sql="select a.dept_code as deptcode , d.description,count(*) as total_cases, "
			 * +
			 * "count(distinct io.cino) as interim_orders, count(distinct fo.cino) as final_orders from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
			 * + "left join (select distinct cino from ecourts_case_interimorder where 1=1 "
			 * +sqlCondition+") io on (a.cino=io.cino) " +
			 * "left join (select distinct cino from ecourts_case_finalorder  where 1=1 "
			 * +sqlCondition+") fo on (a.cino=fo.cino) " + "where d.display = true ";
			 */

			sql="select a.dept_code as deptcode , d.description,count(*) as total_cases, "
					+ " count(distinct io.cino) as interim_order_cases, count(distinct fo.cino) as final_order_cases,sum(coalesce(interim_orders,'0')::int4)  as interim_orders, sum(coalesce(final_orders,'0')::int4) as final_orders "
					+ " from ecourts_case_data a inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " left join (select cino,count(*) as interim_orders from ecourts_case_interimorder where 1=1 "+sqlCondition+"  group by cino) io on (a.cino=io.cino) "
					+ " left join (select cino,count(*) as final_orders from ecourts_case_finalorder  where 1=1 "+sqlCondition+"  group by cino) fo on (a.cino=fo.cino) "
					+ " where d.display = true "+sqlCondition+" ";
			
				sql += " and (reporting_dept_code='" + deptId + "' or a.dept_code='" + deptId + "') ";
				if(roleId.equals("2")){
					sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"' ";
				}
				
			sql += " group by a.dept_code,d.description order by 1";

			request.setAttribute("HEADING", "HOD Wise High Court Orders Issued Report for "+deptName);
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("deptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			if (con != null) {
				if (roleId.equals("2") || roleId.equals("10")) {
					cform.setDynaForm("distList",
							DatabasePlugin.getSelectBox(
									"select district_id,upper(district_name) from district_mst where district_id='"
											+ session.getAttribute("dist_id") + "' order by district_name",
											con));
				}
				else {
					sql="select district_id,upper(district_name) from district_mst order by 1";
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
				cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
						"select case_short_name,case_full_name from case_type_master order by sno", con));
				cform.setDynaForm("JudgeList", DatabasePlugin.getSelectBox(
						"select distinct coram,coram from ecourts_case_data where coram!='' order by coram", con));
				ArrayList selectData = new ArrayList();
				for (int i = 2022; i > 1980; i--) {
					selectData.add(new LabelValueBean(i + "", i + ""));
				}
				cform.setDynaForm("yearsList", selectData);

				cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
				cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
				cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
				cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
				cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
				cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
				cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
				cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
				cform.setDynaForm("judgeName", cform.getDynaForm("judgeName"));
				request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
				DatabasePlugin.closeConnection(con);
		}
		}
		return mapping.findForward("success");
	}
	
	
	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HCCaseStatusAbstractReport..............................................................................getCasesList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			deptCode = !deptCode.equals("") ? deptCode : CommonModels.checkStringObject(request.getParameter("deptId"));
			
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			caseStatus = !caseStatus.equals("") ? caseStatus : CommonModels.checkStringObject(request.getParameter("caseStatus"));
			
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			actionType = !actionType.equals("") ? actionType : CommonModels.checkStringObject(request.getParameter("actionType"));
			
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			deptName = !deptName.equals("") ? deptName : CommonModels.checkStringObject(request.getParameter("deptName"));
			
			heading="Cases List for "+deptName;
			
			// Interim Orders Issued
			// Final Orders Issued
			// Date Filters
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("IO")){
						heading+=" Interim Orders Issued ";
					}
				if(caseStatus.equals("FO")) {
					heading+=" Final Orders Issued ";
				}
			}
			
			if (request.getParameter("fromDate") != null
					&& !request.getParameter("fromDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time >= to_date('" +request.getParameter("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (request.getParameter("toDate") != null
					&& !request.getParameter("toDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time <= to_date('" + request.getParameter("toDate")
						+ "','dd-mm-yyyy') ";
			}
			if (request.getParameter("caseTypeId") != null
					&& !request.getParameter("caseTypeId").toString().contentEquals("")
					&& !request.getParameter("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + request.getParameter("caseTypeId").toString().trim()
						+ "' ";
			}
			if (request.getParameter("districtId") != null
					&& !request.getParameter("districtId").toString().contentEquals("")
					&& !request.getParameter("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + request.getParameter("districtId").toString().trim() + "' ";
				cform.setDynaForm("districtId", request.getParameter("districtId"));
			}
			if (!CommonModels.checkStringObject(request.getParameter("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(request.getParameter("regYear")) > 0) {
				sqlCondition += " and b.reg_year='" + CommonModels.checkIntObject(request.getParameter("regYear"))
						+ "' ";
			}
			if (request.getParameter("deptId") != null ) {
				sqlCondition += " and a.dept_code='" + request.getParameter("deptId").toString().trim() + "' ";
				cform.setDynaForm("deptId", request.getParameter("deptId"));
			}


			if (request.getParameter("petitionerName") != null
					&& !request.getParameter("petitionerName").toString().contentEquals("")
					&& !request.getParameter("petitionerName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(pet_name,' ',''),'.','') ilike  '%"
						+ request.getParameter("petitionerName") + "%'";

			}

			if (request.getParameter("respodentName") != null
					&& !request.getParameter("respodentName").toString().contentEquals("")
					&& !request.getParameter("respodentName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"
						+ request.getParameter("respodentName") + "%'";

			}
			if (request.getParameter("judgeName") != null
					&& !request.getParameter("judgeName").toString().contentEquals("")
					&& !request.getParameter("judgeName").toString().contentEquals("0")) {
				sqlCondition += " and coram ilike  '%"
						+ request.getParameter("judgeName") + "%'";

			}
			
			System.out.println("---"+request.getParameter("judgeName"));
			
			String condition="";
			//if (roleId.equals("6") )
				//condition= " inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) ";
			
			sql = "select a.*, b.orderpaths, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full, ra.address"
					+ " from ecourts_case_data a "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ " inner join" + " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from (select * from";

			if (caseStatus.equals("IO") && roleId.equals("6"))
				sql += "  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder "
						+ " where 1=1  " + sqlCondition + ") x1";
			
			if (caseStatus.equals("IO") && !roleId.equals("6"))
				sql += "  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0  ) x1";//"+ sqlCondition + \"

			if (caseStatus.equals("FO") && !roleId.equals("6"))
				sql += " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x2";//" + sqlCondition + "
			
			if (caseStatus.equals("FO") && roleId.equals("6"))
				sql += " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder "
						+ " where length(order_document_path) > 10  ) x2";//" + sqlCondition + "

			sql += " order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code)  where d.display = true " + sqlCondition;

			// sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') ";
			if (CommonModels.checkStringObject(request.getParameter("repType")).equals("HOD"))
				sql += " and (a.dept_code='" + deptCode + "') ";
			else  if (CommonModels.checkStringObject(request.getParameter("repType")).equals("SD"))
				sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') ";
			
			
			
			if(roleId.equals("3") || roleId.equals("4")) {
				sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='" + session.getAttribute("dept_code") + "') ";
			}
			if(roleId.equals("5") || roleId.equals("9") || roleId.equals("10")) {
				sql += " and (a.dept_code='" + session.getAttribute("dept_code") + "') ";
			}
			
			
			if(roleId.equals("2") || roleId.equals("10")){
				sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}
			
			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			if (con != null) {
				if (roleId.equals("2") || roleId.equals("10")) {
					cform.setDynaForm("distList",
							DatabasePlugin.getSelectBox(
									"select district_id,upper(district_name) from district_mst where district_id='"
											+ session.getAttribute("dist_id") + "' order by district_name",
											con));
				}
				else {
					sql="select district_id,upper(district_name) from district_mst order by 1";
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
				cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
						"select case_short_name,case_full_name from case_type_master order by sno", con));
				cform.setDynaForm("JudgeList", DatabasePlugin.getSelectBox(
						"select distinct coram,coram from ecourts_case_data where coram!='' order by coram", con));
				ArrayList selectData = new ArrayList();
				for (int i = 2022; i > 1980; i--) {
					selectData.add(new LabelValueBean(i + "", i + ""));
				}
				cform.setDynaForm("yearsList", selectData);

				cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
				cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
				cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
				cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
				cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
				cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
				cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
				cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
				cform.setDynaForm("judgeName", cform.getDynaForm("judgeName"));

				request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
				DatabasePlugin.closeConnection(con);
		}
		}
		return mapping.findForward("success");
	}
	public ActionForward getCasesListNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HCCaseStatusAbstractReport..............................................................................getCasesList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			deptCode = !deptCode.equals("") ? deptCode : CommonModels.checkStringObject(request.getParameter("deptId"));
			
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			caseStatus = !caseStatus.equals("") ? caseStatus : CommonModels.checkStringObject(request.getParameter("caseStatus"));
			
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			actionType = !actionType.equals("") ? actionType : CommonModels.checkStringObject(request.getParameter("actionType"));
			
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			deptName = !deptName.equals("") ? deptName : CommonModels.checkStringObject(request.getParameter("deptName"));
			
			heading="Cases List for "+deptName;
			
			if (cform.getDynaForm("fromDate") != null
					&& !cform.getDynaForm("fromDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time >= to_date('" + cform.getDynaForm("fromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("toDate") != null
					&& !cform.getDynaForm("toDate").toString().contentEquals("")) {
				sqlCondition += " and a.inserted_time <= to_date('" + cform.getDynaForm("toDate")
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
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			
			System.out.println("---"+cform.getDynaForm("districtId"));
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear"))
						+ "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("petitionerName") != null
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("")
					&& !cform.getDynaForm("petitionerName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(pet_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("petitionerName") + "%'";

			}

			if (cform.getDynaForm("respodentName") != null
					&& !cform.getDynaForm("respodentName").toString().contentEquals("")
					&& !cform.getDynaForm("respodentName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("respodentName") + "%'";

			}
			
			if (cform.getDynaForm("judgeName") != null
					&& !cform.getDynaForm("judgeName").toString().contentEquals("")
					&& !cform.getDynaForm("judgeName").toString().contentEquals("0")) {
				sqlCondition += " and coram  ilike  '%"
						+ cform.getDynaForm("judgeName") + "%'";

			}			
			System.out.println("---"+request.getParameter("judgeName"));
			
			sql = "select a.*, b.orderpaths, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, "
					+ " case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, "
					+ " prayer as prayer_full, ra.address from ecourts_case_data a  "
					+ " left join nic_prayer_data np on (a.cino=np.cino) left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1)  "
					+ " inner join ( select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths "
					+ " from (select * from  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder "
					+ " where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0 and POSITION('INVALID_TOKEN' in order_document_path) = 0  ) x1 "
					+ " order by cino, order_date desc) c group by cino ) b on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " where d.display = true   " + sqlCondition;

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			if (con != null) {
				if (roleId.equals("2") || roleId.equals("10")) {
					cform.setDynaForm("distList",
							DatabasePlugin.getSelectBox(
									"select district_id,upper(district_name) from district_mst where district_id='"
											+ session.getAttribute("dist_id") + "' order by district_name",
											con));
				}
				else {
					sql="select district_id,upper(district_name) from district_mst order by 1";
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
				cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
						"select case_short_name,case_full_name from case_type_master order by sno", con));
				cform.setDynaForm("JudgeList", DatabasePlugin.getSelectBox(
						"select distinct coram,coram from ecourts_case_data where coram!='' order by coram", con));
				ArrayList selectData = new ArrayList();
				for (int i = 2022; i > 1980; i--) {
					selectData.add(new LabelValueBean(i + "", i + ""));
				}
				cform.setDynaForm("yearsList", selectData);

				cform.setDynaForm("fromDate", cform.getDynaForm("fromDate"));
				cform.setDynaForm("toDate", cform.getDynaForm("toDate"));
				cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
				cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
				cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
				cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
				cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
				cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
				cform.setDynaForm("judgeName", cform.getDynaForm("judgeName"));

				request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
				DatabasePlugin.closeConnection(con);
		}
		}
		return mapping.findForward("success");
	}
	
}