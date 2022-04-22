package in.apcfss.struts.entryforms.filing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class WPAffidavitNEWFormBean  extends ActionForm{
	public HashMap<String, Object> property;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		property = new HashMap<String, Object>();
	}

	public Object getProperty(String key) {
		return this.property.get(key);
	}

	public void setProperty(String key, Object value) {
		this.property.put(key, value);
	}

}