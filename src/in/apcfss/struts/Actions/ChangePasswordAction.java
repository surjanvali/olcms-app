package in.apcfss.struts.Actions;


import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import in.apcfss.struts.Forms.CommonFormBean;
import in.apcfss.struts.Utilities.CommonModels;
import plugins.DatabasePlugin;

public class ChangePasswordAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("changepswdpage");
	}

	public ActionForward updatePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonFormBean comForm = (CommonFormBean) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql=null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			if(userId !=null && !userId.equals("")){
				//CHECK 1. USER EXIST
				if(CommonModels.checkIntObject(DatabasePlugin.getStringfromQuery("select count(*) from public.users where upper(userid)='" + userId.toUpperCase() + "'", con)) > 0){
					//CHECK 2. OLD PASSWORD
					if(CommonModels.checkIntObject(DatabasePlugin.getStringfromQuery("select count(*) from public.users where upper(userid)='" + userId.toUpperCase() + "' and password='"+CommonModels.checkStringObject(comForm.getDynaProperties("CURRENTPSWD"))+"'", con)) > 0){
						//CHECK 3. OLD PASSWORD & NEW PASSWORDS ARE SAME
						if(!CommonModels.checkStringObject(comForm.getDynaProperties("CONFIRMPSWD")).equals(CommonModels.checkStringObject(comForm.getDynaProperties("CURRENTPSWD")))){
							sql = "update public.users set password='"
									+ CommonModels.checkStringObject(comForm.getDynaProperties("CONFIRMPSWD"))
									+ "', password_text='"
									+ CommonModels.checkStringObject(comForm.getDynaProperties("CONFIRMPSWDTXT"))
									+ "', last_pwd_modify_date=current_date where userid='"
									+ userId + "'  ";
		
							int a = DatabasePlugin.executeUpdate(sql, con);
							if (a > 0) {
								request.setAttribute("successMsg", "Password Updated Successfully.");
							} else {
								request.setAttribute("errorMsg", "Error in Password updation.");
							}
						}
						else{
							request.setAttribute("errorMsg", "New Password is same as Current Password.");
						}
					}
					else{
						request.setAttribute("errorMsg", "Current Password doesn't match with our records.");	
					}
				}
				else{
					request.setAttribute("errorMsg", "Invalid User/ User Doesn't exist.");
				}
			}
			comForm.setDynaProperties("CONFIRMPSWD",  "");
			comForm.setDynaProperties("CONFIRMPSWDTXT",  "");
			comForm.setDynaProperties("NEWPSWD",  "");
			comForm.setDynaProperties("CURRENTPSWD",  "");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("changepswdpage");
	}
}