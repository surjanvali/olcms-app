package in.apcfss.struts.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class HCCaseDocsUploadStatusAbstractReportNew extends DispatchAction{
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null,deptId=null,sqlCondition = "",condition="";
		CommonForm cform = (CommonForm) form;
		try {
			session = request.getSession();
			con = DatabasePlugin.connect();
			
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			
			
			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and b.inserted_time >= to_date('" + cform.getDynaForm("dofFromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and b.inserted_time <= to_date('" + cform.getDynaForm("dofToDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(b.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null
					&& !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and b.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
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
				sqlCondition += " and replace(replace(advocatename,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("petitionerName") + "%'";

			}

			if (cform.getDynaForm("respodentName") != null
					&& !cform.getDynaForm("respodentName").toString().contentEquals("")
					&& !cform.getDynaForm("respodentName").toString().contentEquals("0")) {
				sqlCondition += " and replace(replace(res_name,' ',''),'.','') ilike  '%"
						+ cform.getDynaForm("respodentName") + "%'";

			}
			
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			else  if(roleId.equals("5") || roleId.equals("9") || roleId.equals("10")) {
				
				 return HODwisedetails(mapping, form, request, response); 
			}
			if ((roleId.equals("6"))) {
				condition = " left join ecourts_mst_gp_dept_map egm on (egm.dept_code=dn.dept_code) ";
				sqlCondition += " and egm.gp_id='" + userId + "'";
			}
			if ((roleId.equals("1") || roleId.equals("7"))) {
				
				sqlCondition += " ";
				
			}
			
				
			sql=" select a1.reporting_dept_code as dept_code,"
					+ " upper(trim(dn1.description)) as description,"
					+ " sum(total_resp1) as cases_respondent_one,"
					+ " sum(total_resp_other) as cases_respondent_other,"
					+ " sum(totalcases) as total,"
					+ " sum(closed_cases) as closed_cases,"
					+ ""
					+ " sum(pwrcounter_uploaded) as pwrcounter_uploaded, "
					+ " sum(pwrcounter_not_uploaded) as pwrcounter_not_uploaded ,"
					+ " sum(pwrcounter_approved_by_gp) as pwrcounter_approved_by_gp ,"
					+ " sum(pwrcounter_rejected_by_gp) as pwrcounter_rejected_by_gp ,"
					+ ""
					+ " sum(counter_uploaded) as counter_uploaded,	"
					+ " sum(counter_not_uploaded) as counter_not_uploaded,	"
					+ " sum(counter_approved_gp) as counter_approved_gp  ,"
					+ " sum(counter_rejected_gp) as counter_rejected_gp "
					+ ""
					+ " from ( select case when reporting_dept_code='CAB01' then a.dept_code else reporting_dept_code end as reporting_dept_code,a.dept_code,"
					+ " upper(trim(dn.description)) as description,"
					+ " sum(case when a.respondent_slno=1 then 1 else 0 end) as total_resp1, "
					+ " sum(case when a.respondent_slno > 1 then 1 else 0 end) as total_resp_other, "
					+ " count(*) as totalcases,"
					+ " sum(case when a.ecourts_case_status='Closed' then 1 else 0 end) as closed_cases,	"
					+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is not null and length(pwr_uploaded_copy)>10 then 1 else 0 end) as pwrcounter_uploaded,"
					+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is null then 1 else 0 end) as pwrcounter_not_uploaded,	"
					+ " sum(case when ecod.pwr_approved_gp='Yes' then 1 else 0 end) as pwrcounter_approved_by_gp,	"
					+ " sum(case when ecod.pwr_approved_gp='No' then 1 else 0 end) as pwrcounter_rejected_by_gp,	"
					+ " sum(case when a.ecourts_case_status='Pending' and counter_filed_document is not null and length(counter_filed_document)>10 then 1 else 0 end) as counter_uploaded,	"
					+ " sum(case when a.ecourts_case_status='Pending' and counter_filed_document is null then 1 else 0 end) as counter_not_uploaded,	"
					+ " sum(case when counter_approved_gp='Yes' then 1 else 0 end) as counter_approved_gp,	"
					+ " sum(case when counter_approved_gp='NO' then 1 else 0 end) as counter_rejected_gp"
					+ " from ecourts_gpo_ack_depts  a  left join ecourts_olcms_case_details ecod on(a.ack_no=ecod.cino and a.respondent_slno=ecod.respondent_slno)	"
					+ " left join ecourts_gpo_ack_dtls  b using(ack_no) inner join dept_new dn on (a.dept_code=dn.dept_code)  "+condition+" "
					+ " where  b.ack_type='NEW' " + sqlCondition ;
			sql+=" group by dn.reporting_dept_code,a.dept_code,dn.description,pwr_gp_approved_date,counter_approved_date ) a1 inner join dept_new dn1 on (a1.reporting_dept_code=dn1.dept_code)  "
					+ " group by a1.reporting_dept_code,dn1.description order by 1 ";
			
			
			System.out.println("un--->"+sql);
			
			request.setAttribute("HEADING", "Sect. Dept. Wise Case processing Abstract Report");
			
			request.setAttribute("details", DatabasePlugin.executeQuery(con, sql));
			
		} catch (Exception e) {
		request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
		e.printStackTrace();
		} finally {
			
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		//DatabasePlugin.closeConnection(con);
		}
		
		return mapping.findForward("success");
	}

	public ActionForward HODwisedetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, deptId = null, deptName = "",sqlCondition="";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if (roleId.equals("5") || roleId.equals("9") || roleId.equals("10")) {
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery("select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
				
			} else {
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			}
			
			
			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and b.inserted_time >= to_date('" + cform.getDynaForm("dofFromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and b.inserted_time <= to_date('" + cform.getDynaForm("dofToDate")
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
			
			sql=" select a1.reporting_dept_code as dept_code,"
					+ " upper(trim(dn1.description)) as description,"
					+ " sum(total_resp1) as cases_respondent_one,"
					+ " sum(total_resp_other) as cases_respondent_other,"
					+ " sum(totalcases) as total,"
					+ " sum(closed_cases) as closed_cases,"
					+ ""
					+ " sum(pwrcounter_uploaded) as pwrcounter_uploaded, "
					+ " sum(pwrcounter_not_uploaded) as pwrcounter_not_uploaded ,"
					+ " sum(pwrcounter_approved_by_gp) as pwrcounter_approved_by_gp ,"
					+ " sum(pwrcounter_rejected_by_gp) as pwrcounter_rejected_by_gp ,"
					+ ""
					+ " sum(counter_uploaded) as counter_uploaded,	"
					+ " sum(counter_not_uploaded) as counter_not_uploaded,	"
					+ " sum(counter_approved_gp) as counter_approved_gp  ,"
					+ " sum(counter_rejected_gp) as counter_rejected_gp "
					+ ""
					+ " from ( select case when reporting_dept_code='CAB01' then a.dept_code else reporting_dept_code end as reporting_dept_code,a.dept_code,"
					+ " upper(trim(dn.description)) as description,"
					+ " sum(case when a.respondent_slno=1 then 1 else 0 end) as total_resp1, "
					+ " sum(case when a.respondent_slno > 1 then 1 else 0 end) as total_resp_other, "
					+ " count(*) as totalcases,"
					+ " sum(case when a.ecourts_case_status='Closed' then 1 else 0 end) as closed_cases,	"
					+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is not null and length(pwr_uploaded_copy)>10 then 1 else 0 end) as pwrcounter_uploaded,"
					+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is null then 1 else 0 end) as pwrcounter_not_uploaded,	"
					+ " sum(case when ecod.pwr_approved_gp='Yes' then 1 else 0 end) as pwrcounter_approved_by_gp,	"
					+ " sum(case when ecod.pwr_approved_gp='No' then 1 else 0 end) as pwrcounter_rejected_by_gp,	"
					+ "	"
					+ " sum(case when a.ecourts_case_status='Pending' and counter_filed_document is not null and length(counter_filed_document)>10 then 1 else 0 end) as counter_uploaded,	"
					+ " sum(case when a.ecourts_case_status='Pending' and counter_filed_document is null then 1 else 0 end) as counter_not_uploaded,	"
					+ " sum(case when counter_approved_gp='Yes' then 1 else 0 end) as counter_approved_gp,	"
					+ " sum(case when counter_approved_gp='NO' then 1 else 0 end) as counter_rejected_gp"
					+ ""
					+ " from ecourts_gpo_ack_depts  a  left join ecourts_olcms_case_details ecod on(a.ack_no=ecod.cino and a.respondent_slno=ecod.respondent_slno)	"
					+ " left join ecourts_gpo_ack_dtls  b using(ack_no) inner join dept_new dn on (a.dept_code=dn.dept_code) "
					+ " where  b.ack_type='NEW'  and (a.dept_code='" + deptId+"' or reporting_dept_code='"+deptId+"')" ;   
					
						
						if(roleId.equals("2") || roleId.equals("10")){
							sql+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
						}
						
						//sql+=" group by dn.reporting_dept_code,a.dept_code,dn.description ";
						
						sql+=" group by dn.reporting_dept_code,a.dept_code,dn.description,pwr_gp_approved_date,counter_approved_date ) a1 inner join dept_new dn1 on (a1.reporting_dept_code=dn1.dept_code)  "
						+ " group by a1.reporting_dept_code,dn1.description order by 1 ";

			request.setAttribute("HEADING", "HOD Wise Case processing Abstract for " + deptName);
			System.out.println("SQL:" + sql);
			
			request.setAttribute("deptwise", DatabasePlugin.executeQuery(con, sql));
			
			
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		String caseStatus=null;String heading = "";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			String inserted_by = CommonModels.checkStringObject(cform.getDynaForm("inserted_by"));
			String respondenttype = CommonModels.checkStringObject(cform.getDynaForm("respondenttype"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));

			String sqlCondition = "";
			con = DatabasePlugin.connect();
			
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.distid='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}

			
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				// sqlCondition += " and ad.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
				deptCode = cform.getDynaForm("deptId").toString().trim();
			}
			 
			
			if (cform.getDynaForm("advcteName") != null && !cform.getDynaForm("advcteName").toString().contentEquals("")) {
				sqlCondition += " and replace(replace(advocatename,' ',''),'.','') ilike  '%"+cform.getDynaForm("advcteName")+"%'";
			}
			if (cform.getDynaForm("petitionerName") != null && !cform.getDynaForm("petitionerName").toString().contentEquals("")) {
				sqlCondition += " and replace(replace(petitioner_name,' ',''),'.','') ilike  '%"+cform.getDynaForm("petitionerName")+"%'";
			}
			if (request.getParameter("districtId") != null
					&& !CommonModels.checkStringObject(request.getParameter("districtId")).contentEquals("")
					&& !CommonModels.checkStringObject(request.getParameter("districtId")).contentEquals("0")) {
				sqlCondition += " and a.distid='" + request.getParameter("districtId").toString().trim() + "' ";

				cform.setDynaForm("districtId", request.getParameter("districtId"));
			}
			
			if (request.getParameter("advcteName") != null
					&& !CommonModels.checkStringObject(request.getParameter("advcteName")).contentEquals("")) {
				sqlCondition += " and replace(replace(advocatename,' ',''),'.','') ilike  '%"+cform.getDynaForm("advcteName")+"%'";
				cform.setDynaForm("advcteName", request.getParameter("advcteName"));
			}
			
			if(!caseStatus.equals("")) {
				
				if(caseStatus.equals("TOTAL")){
					sqlCondition= "   ";
					heading+=" Total Cases List";
				}

				if(caseStatus.equals("CLOSED")){
						sqlCondition= " and coalesce(ad.ecourts_case_status,'')='Closed'  ";
						heading+=" Closed Cases List";
					}
				
				
				if(caseStatus.equals("PWRUPLOADED")) {
					sqlCondition= " and pwr_uploaded_copy is not null and length(pwr_uploaded_copy)>10  ";
					heading+=" Parawise Remarks Uploaded Cases List";
				}
				
				
				if(caseStatus.equals("PWRNOTUPLOADED")){
					sqlCondition= " and ad.ecourts_case_status='Pending' and pwr_uploaded_copy is null ";//c
					heading+=" Parawise Remarks Uploaded Cases List";
				}
				
				if(caseStatus.equals("PWRAPPROVEDBYGP")){
					sqlCondition=" and pwr_approved_gp='Yes' ";
					heading+=" and Parawise Remarks Approved By Gp";
				}
				
				if(caseStatus.equals("PWRREJEDBYGP")){
					sqlCondition=" and pwr_approved_gp='No' ";
					heading+=" and Parawise Remarks Rejected By Gp";
				}

				if(caseStatus.equals("COUNTERUPLOADED")) {
					sqlCondition=" and counter_filed_document is not null and length(counter_filed_document)>10 ";
					heading+=" Counter Uploaded Cases";
				}
				
				if(caseStatus.equals("COUNTERNOTUPLOADED")){
					sqlCondition=" and ad.ecourts_case_status='Pending' and counter_filed_document is null  ";
					heading+=" Counter Not Uploaded Cases";
				}
				

				if(caseStatus.equals("GPCOUNTERAPPROVED")){
					sqlCondition=" and counter_approved_gp='Yes' ";
					heading+=" and Counters Filed Approved By Gp";
				}
				

				if(caseStatus.equals("GPCOUNTERREJECTED")){
					sqlCondition=" and counter_approved_gp='No' ";
					heading+=" and Counters Filed Rejected By Gp";
				}
				

				
				
				if(caseStatus.equals("ALL")) {
					sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='" + deptCode+ "') "; 
				}
				
				if(caseStatus.equals("HOD")) {
					sqlCondition += " and dmt.dept_code='" + deptCode + "' "; 
				}
			}
			
			if(respondenttype.equals("1"))
			{
				sqlCondition += " and ad.respondent_slno=1 ";
			}
			else if(respondenttype.equals("2"))
			{
				sqlCondition += " and ad.respondent_slno>1 ";
			}else if(respondenttype.equals("ALL")) {
				sqlCondition += " and (dmt.dept_code='" + deptCode + "' or dmt.reporting_dept_code='" + deptCode+ "') "; 
			}
			
			if(respondenttype.equals("HOD")) {
				sqlCondition += " and dmt.dept_code='" + deptCode + "' "; 
			}
			
			
			String condition = "";
			if ((roleId.equals("6"))) {
				condition = " inner join ecourts_mst_gp_dept_map egm on (egm.dept_code=ad.dept_code) ";
			}

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition+=" and ad.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}

			
			if (cform.getDynaForm("inserted_by") != null
					&& !cform.getDynaForm("inserted_by").toString().contentEquals("")
					&& !cform.getDynaForm("inserted_by").toString().contentEquals("0")) {
				sqlCondition += " and a.inserted_by='" + inserted_by + "' ";
			}

			sql = "select  a.slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , ecod.remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs,inserted_time, coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ "from ecourts_gpo_ack_depts ad  inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
					+ "left join ecourts_olcms_case_details ecod on(ad.ack_no=ecod.cino and ad.respondent_slno=ecod.respondent_slno)"
					+ "inner join district_mst dm on (a.distid=dm.district_id) "
					+ "inner join dept_new dmt on (ad.dept_code=dmt.dept_code)  " + condition + " "
					+ "inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ "where a.delete_status is false and ack_type='NEW' " + sqlCondition
					+ "order by inserted_time desc";

			System.out.println("SQL:" + sql);

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			//System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);

			} else {
				request.setAttribute("errorMsg", "No details found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getAckNo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, cIno = null, viewDisplay=null, target="newcaseview";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			viewDisplay = CommonModels.checkStringObject(request.getParameter("SHOWPOPUP"));
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			if(!viewDisplay.equals("") && viewDisplay.equals("SHOWPOPUP")) {
				target = "newcasepopupview";
				cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			}
			else {
				cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			}
			
			System.out.println("cIno---"+cIno);
			
			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();
	
				
				sql = " select advocatename,advocateccno,cm.case_short_name,maincaseno,inserted_time,petitioner_name,"
						+ " services_flag,reg_year,reg_no,mode_filing,case_category,dm.district_name from  ecourts_gpo_ack_dtls a"
						+ " left join district_mst dm on (a.distid=dm.district_id) "
						+ " left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name)"
						+ "where ack_no='" + cIno + "'";

				System.out.println("sql---"+sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
	
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("USERSLIST", data);
	
				}
	
				sql="select cino,action_type,inserted_by,inserted_on,assigned_to,remarks as remarks, coalesce(uploaded_doc_path,'-') as uploaded_doc_path from ecourts_case_activities where cino = '"+cIno+"' order by inserted_on";
				System.out.println("ecourts activities SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				request.setAttribute("ACTIVITIESDATA", data);
				request.setAttribute("HEADING", "Case Details for ACK NO : " + cIno);
			}
			else {
				request.setAttribute("errorMsg", "Invalid ACK NO.");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}

}