package entryforms.filing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CASPFundsForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	/**
	 * Method validate
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}

	/**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
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