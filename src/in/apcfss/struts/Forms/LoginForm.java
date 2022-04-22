package in.apcfss.struts.Forms;

import org.apache.struts.action.ActionForm;

public class LoginForm extends ActionForm{
	
	private String username;
	private String password;
	private String mode;
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
