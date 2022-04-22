package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
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

public class ActsMasterAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			sql = "select * from ecourts_mst_acts";
			request.setAttribute("HEADING", "Acts Master Report");

			//System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("ActsMaster", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
}
