package judgement;


import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class JudgementFormBean  extends ActionForm{
	public HashMap<String, Object> property;

	private String mode;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		property = new HashMap<String, Object>();
	}

	public Object getProperty(String key) {
		return this.property.get(key);
	}

	public void setProperty(String key, Object value) {
		this.property.put(key, value);
	}

	public ArrayList getBenchList() {
		ArrayList list = new ArrayList();
		list.add(new LabelValueBean("Single", "Single"));
		list.add(new LabelValueBean("Division", "Division"));
		list.add(new LabelValueBean("Full", "Others"));

		return list;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
