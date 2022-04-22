package entryforms.filing;

import common.LoadResourceAction;
import common.plugin.DatabasePlugin;
import generaldata.GeneralOperations;
import generaldata.GeneralQueries;
import generaldata.GeneralSQL;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class AppealFormBean extends ActionForm {
	
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
