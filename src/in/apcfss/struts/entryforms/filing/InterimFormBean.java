package entryforms.filing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class InterimFormBean extends ActionForm {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public HashMap<String, Object> property;
private String action = "";

public String getAction() {
	return action;
}

public void setAction(String action) {
	this.action = action;
}

public void reset(ActionMapping mapping, HttpServletRequest request) {
}

private HashMap<String, Object> properties=new HashMap<String, Object>();

public Object getProperty(String key) 
{
	return properties.get(key);
}

public HashMap<String, Object> getProperties() 
{
	return properties;
}

public void setProperties(HashMap<String, Object> properties) 
{
	this.properties = properties;
}
public void setProperty(String key,Object value)
{
	properties.put(key, value);
}

}
