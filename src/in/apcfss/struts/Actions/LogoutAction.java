package in.apcfss.struts.Actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class LogoutAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String target = "success";
		HttpSession session = request.getSession(true);
		try {

			if(session!=null){
				
				if(session.getAttribute("userid")!=null)
					session.removeAttribute("userid");
				if(session.getAttribute("dept_id")!=null)
					session.removeAttribute("dept_id");
				if(session.getAttribute("role_id")!=null)
					session.removeAttribute("role_id");
				session.invalidate();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward(target);
	}

}