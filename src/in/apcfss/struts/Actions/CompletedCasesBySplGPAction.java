package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class CompletedCasesBySplGPAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			/*
			 * sql =
			 * "select  distinct a.cino,type_name_fil,reg_no,reg_year,date_of_filing,coram,pet_name,dist_name,pet_adv,res_adv,b.response,agolcms_status,a.assigned_to,c.inserted_on,"
			 * +
			 * "case when agolcms_status='19' and response is null then 'Pending For Response'  else 'Completed' end as status  from ecourts_case_data a  "
			 * +
			 * "left JOIN ecourts_case_activities_agolcms c ON (c.cino=a.cino  and c.inserted_by=a.assigned_to  )"
			 * + "left join ecourts_response_ag_mst b on (a.cino=b.cino)   " +
			 * "where  agolcms_status in ('18','19')  and coalesce(a.ecourts_case_status,'')!='Closed'  order by c.inserted_on asc"
			 * ;
			 */
			
			sql="select distinct a.cino,type_name_fil,reg_no,reg_year,date_of_filing,coram,pet_name,dist_name,pet_adv,res_adv, "
					+ "CASE "
					+ " WHEN counter_filed='Yes' THEN 'Counter Filed' "
					+ " WHEN pwr_uploaded='Yes' THEN 'Parawise Remarks Uploaded' "
					+ " WHEN pwr_approved_gp='Yes' THEN 'Parawise Remarks Approved By GP' "
					+ "WHEN counter_approved_gp='Yes' THEN 'Counter Approved By GP' "
					+ "WHEN appeal_filed='Yes' THEN 'Appeal Filed' "
					+ "when case_status = '1' then 'Sect. Dept' "
					+ "when case_status = '2' then 'MLO' "
					+ "when case_status = '3' then 'HOD' "
					+ "when case_status = '4' then 'Nodal Officer' "
					+ "when case_status = '5' then 'Section Officer' "
					+ "when case_status = '6' then 'GPO' "
					+ "when case_status = '99' then 'Case Closed' "
					+ "when case_status = '7' then 'District Collector' "
					+ "when case_status = '8' then 'District Nodal Officer' "
					+ "when case_status = '9' then 'Section Officer(HOD)' "
					+ "when case_status = '10' then 'Section Officer(District)' "
					+ "when case_status = '98' then 'Private' "
					+ "when case_status = '97' then 'PSU' "
					+ "when case_status = '96' then 'GoI' "
					+ "when case_status = '12' then 'MLO(Subject)' "
					+ "when case_status = '18' then 'AG Office' "
					+ "when case_status = '20' then 'AG Office Clerk' "
					+ "WHEN judgement_order is not null THEN 'Final Order Given' "
					+ "else '--' "
					+ "END AS ecourts_case_status, "
					+ "case when agolcms_status='19' and response is null then 'Pending For Response'  else 'Completed' end as status,B.response,c.inserted_on,coalesce(a.assigned_to,'--') as assigned_to  "
					+ "from ecourts_case_data a  "
					+ "left join ecourts_response_ag_mst b on (b.cino=a.cino) "
					+ "left JOIN ecourts_olcms_case_details eocd on (eocd.cino=a.cino)   "
					+ "left JOIN ecourts_case_activities_agolcms c ON (c.cino=a.cino  and c.inserted_by=a.assigned_to  )  "
					+ "where  agolcms_status in ('18','19') and coalesce(a.ecourts_case_status,'')!='Closed'  order by c.inserted_on asc ";
			
			System.out.println("AssignedCasesToSectionAction unspecified SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", " Cases Assigned");
			} else {
				request.setAttribute("errorMsg", "You have Zero cases to Process.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

}