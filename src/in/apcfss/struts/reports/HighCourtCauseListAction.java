package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
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
import org.apache.struts.upload.FormFile;

import plugins.DatabasePlugin;

public class HighCourtCauseListAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			request.setAttribute("HEADING", "High Court Cause List ");
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			//System.out.println("--"+sdf.format(new Date()));
			cform.setDynaForm("list_date" , sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		//return mapping.findForward("success");
		return ShowCauselist(mapping, cform, request, response);
	}

	public ActionForward ShowCauselist(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, date = null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			date = (String) cform.getDynaForm("list_date");
			System.out.println("date:::::::::::::::" + date);
			con = DatabasePlugin.connect();
			sql = "select a.slno ,a.est_code , a. causelist_date , a.bench_id , a. causelist_id , "
					+ "cause_list_type ,coalesce(causelist_document,'') as document,"
					+ " b.judge_name from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) where a.causelist_date='"
					+ date + "' group by a.slno ,a.est_code ,"
					+ " a. causelist_date , a.bench_id , a. causelist_id , cause_list_type ,b.judge_name";
			
			sql="select distinct a.est_code , a. causelist_date , a.bench_id , a. causelist_id , cause_list_type ,coalesce(causelist_document,'') as document, b.judge_name "
					+ "from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) where a.causelist_date=to_date('"
					+ date + "','mm/dd/yyyy') and coalesce(causelist_document,'') not like '%status_code%'";
			

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("causelist", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");

			request.setAttribute("HEADING", "High Court Cause List ");
			cform.setDynaForm("list_date" , cform.getDynaForm("list_date"));
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}