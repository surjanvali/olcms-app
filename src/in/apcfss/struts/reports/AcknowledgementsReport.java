package in.apcfss.struts.reports;

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

import in.apcfss.struts.Forms.CommonForm;
import plugins.DatabasePlugin;

public class AcknowledgementsReport extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("IN AcknowledgementsReport");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			sql = "select to_char(inserted_time::date,'dd-mm-yyyy') as ack_date, count(*) as total, sum(case when ack_type='NEW' then 1 else 0 end) as new_acks, "
					+ "sum(case when ack_type='OLD' then 1 else 0 end) as existing_acks"
					+ " from ecourts_gpo_ack_dtls where inserted_by='" + session.getAttribute("userid")
					+ "' group by inserted_time::date order by inserted_time::date desc";

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("ACKDATA", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward userWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("IN AcknowledgementsReport");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			String ackDate=request.getParameter("ackDate").toString();
			
			sql = "select inserted_by, count(*) as total, sum(case when ack_type='NEW' then 1 else 0 end) as new_acks, "
					+ "sum(case when ack_type='OLD' then 1 else 0 end) as existing_acks"
					+ " from ecourts_gpo_ack_dtls where inserted_time::date=to_char('"+ackDate+"','dd-mm-yyyy') "
					+ " group by inserted_by order by  desc";

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("USERWISEACKDATA", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
}
