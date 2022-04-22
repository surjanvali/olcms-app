package in.apcfss.struts.Forms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CommonFormBean extends ActionForm{
	
	private HashMap<String, Object> dynaProperties;
	private HashMap hMapOthers1 = new HashMap();
	ArrayList rolesList=new ArrayList();
	
	public void reset(ActionMapping mapping, HttpServletRequest request){
		dynaProperties = new HashMap<String, Object>();
	}
	public Object getDynaProperties(String key) {
		return dynaProperties.get(key);
	}

	public void setDynaProperties(String key, Object value) {
		this.dynaProperties.put(key, value);
	}
	
	public HashMap gethMapOthers1() {
		return hMapOthers1;
	}

	public void sethMapOthers1(HashMap hMapOthers1) {
		this.hMapOthers1 = hMapOthers1;
	}
	public String[] getDynaProperties1(String key)
	{
		return (String[]) this.hMapOthers1.get(key);
	}
	
	public void setDynaProperties1(String key,String[] value)
	{
		this.hMapOthers1.put(key,value);
	}
	
	
	String mode;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	String[] languagesKnown;
	
	public String[] getLanguagesKnown() {
		return languagesKnown;
	}

	public ArrayList getRolesList() {
		return rolesList;
	}
	public void setRolesList(ArrayList rolesList) {
		this.rolesList = rolesList;
	}
	public void setLanguagesKnown(String[] languagesKnown) {
		this.languagesKnown = languagesKnown;
	}
}
