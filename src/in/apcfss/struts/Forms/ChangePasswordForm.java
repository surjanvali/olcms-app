/*
 * Created on Mar 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package in.apcfss.struts.Forms;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author root
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChangePasswordForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** oldPassword property */
	private String oldPassword;

	/** newPassword property */
	private String newPassword;
	
	/** retypenewPassword */
	private String retypenewPassword;
	
	private String passwordText;

	// --------------------------------------------------------- Methods

	

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.oldPassword="";
		this.newPassword="";
		this.retypenewPassword="";
		this.passwordText="";
		
	}

	/**
	 * @return Returns the newPassword.
	 */
	public String getNewPassword() {
		return newPassword;
		
	}
	/**
	 * @param newPassword The newPassword to set.
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return Returns the oldPassword.
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param oldPassword The oldPassword to set.
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	/**
	 * @return Returns the retypenewPassword.
	 */
	public String getRetypenewPassword() {
		return retypenewPassword;
	}
	/**
	 * @param retypenewPassword The retypenewPassword to set.
	 */
	public void setRetypenewPassword(String retypenewPassword) {
		this.retypenewPassword = retypenewPassword;
	}
	
	public String getPasswordText() {
		return passwordText;
	}

	public void setPasswordText(String passwordText) {
		this.passwordText = passwordText;
	}
}
