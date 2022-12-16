package in.apcfss.struts.reports;

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
import org.apache.struts.upload.FormFile;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import plugins.DatabasePlugin;

public class InstructionsReplyCountReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, deptId = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			String show_flag="N";
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			} else {
				if(roleId!=null && (roleId.equals("3") || roleId.equals("7")))
				{
				con = DatabasePlugin.connect();

				//int yearId = CommonModels.checkIntObject(request.getParameter("yearId"));

				sql = "select a.dept_code,d.description as dept_name,sum(case when a.dept_code is not null then 1 else 0 end ) as dept_count, "
						+ "sum(case when a.instructions is not null and a.instructions!='' then 1 else 0 end ) as instructions_count, "
						+ "sum(case when a.reply_instructions is not null and a.reply_instructions!='' then 1 else 0 end ) as reply_instructions_count  "
						+ "from  "
						+ "ecourts_dept_instructions a  "
						+ "inner join  "
						+ "dept_new d on(a.dept_code=d.dept_code) "
						+ "group by 1,2 order by dept_code ";
				

				request.setAttribute("HEADING", "Instructions Reply Count Report");

				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
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
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="", heading=null;
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

			if(roleId!=null && (roleId.equals("3") || roleId.equals("7"))) { // GPO

				String counter_pw_flag = CommonModels.checkStringObject(request.getParameter("pwCounterFlag"));
				
				String viewDisplay = CommonModels.checkStringObject(request.getParameter("SHOWPOPUP"));
				String dept_code = CommonModels.checkStringObject(request.getParameter("dept_code"));
				

				if(!viewDisplay.equals("") && viewDisplay.equals("SHOWPOPUP")) {
					target = "datapopupview";
				}
				
				condition+=" where a.dept_code is not null ";

				if(counter_pw_flag.equals("DeptCount")) 
				{
					heading = "Department Count List for Instructions reply Count View Report";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
					condition+=" and a.dept_code='"+dept_code+"'";
				}else if(counter_pw_flag.equals("InstCount")) 
				{
					heading = "Instruction Count List for Instructions reply Count View Report";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
					condition+=" and a.dept_code='"+dept_code+"' and a.instructions is not null and a.instructions!=''";
				}
				else if(counter_pw_flag.equals("ReplyInstCount")) 
				{
					heading = "Reply Instruction Count List for Instructions reply Count View Report";
					// pwr_uploaded='No' and (coalesce(pwr_approved_gp,'0')='0' or coalesce(pwr_approved_gp,'No')='No' ) and ecd.case_status='6' //  and action_to_perfom='Parawise Remarks'
					condition+=" and a.dept_code='"+dept_code+"' and a.reply_instructions is not null and a.reply_instructions!=''";
				}
			
			}

			sql = "select a.dept_code,d.description as dept_name,a.instructions,a.reply_instructions,mobile_no,cino  "
					+ "from  "
					+ "ecourts_dept_instructions a  "
					+ "inner join  "
					+ "dept_new d on(a.dept_code=d.dept_code) "+condition+" order by dept_code,cino";

			
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

}
