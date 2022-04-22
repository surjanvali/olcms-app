package in.apcfss.struts.Actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;

public class UsermanualAction extends DispatchAction{
	public ActionForward unspecified(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if(session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		request.setAttribute("VIEWTYPE", "USERMANUAL");
		
		return mapping.findForward("UserManual");
	}
	public ActionForward helpLine(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if(session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabasePlugin.connect();
			ps = con.prepareStatement("select name,mobile,district,email from apdrp_helpline_numbers order by slno");
			rs = ps.executeQuery();
			if(rs != null && rs.next())
				request.setAttribute("helpline", DatabasePlugin.processResultSet(rs));
			
			request.setAttribute("VIEWTYPE", "HELPLINE");
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("UserManual");
	}
}
