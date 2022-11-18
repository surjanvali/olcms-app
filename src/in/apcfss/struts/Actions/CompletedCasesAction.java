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

public class CompletedCasesAction extends DispatchAction {

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

			sql="select  distinct a.cino,type_name_fil,reg_no,reg_year,date_of_filing,coram,pet_name,dist_name,pet_adv,res_adv,b.response,agolcms_status,a.assigned_to,c.inserted_on,"
					+ " case when agolcms_status='18' and response is not null then 'Completed'  else 'Pending For Response' end as status  from ecourts_case_data a  "
					+ " left JOIN ecourts_case_activities_agolcms c ON (c.cino=a.cino  )"
					+ " left join ecourts_response_ag_mst b on (a.cino=b.cino)   "
					+ " where  agolcms_status in ('18') and c.inserted_by='"+userId+"' and coalesce(a.ecourts_case_status,'')!='Closed'  order by c.inserted_on asc ";
			
			System.out.println("AssignedCasesToSectionAction unspecified SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", "Completed Cases List");
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