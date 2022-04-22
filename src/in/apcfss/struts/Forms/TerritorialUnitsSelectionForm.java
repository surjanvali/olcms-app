package in.apcfss.struts.Forms;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;


public class TerritorialUnitsSelectionForm extends ActionForm{

	private int slno;

	private boolean state_level;
	
	private int no_of_state_level_units;

	private boolean regional;
	
	private int no_of_regional_units;

	private boolean zonal_level;
	
	private int no_of_zonal_level_units;

	private boolean regional_level;
	
	private int no_of_regional_level_units;

	private boolean circle_level;
	
	private int no_of_circle_level_units;

	private boolean divisional;
	
	private int no_of_divisional_units;

	private boolean district_level;
	
	private int no_of_district_level;

	private boolean independent;
	
	private int no_of_independent;


	public int getSlno() {
		return slno;
	}

	public void setSlno(int slno) {
		this.slno = slno;
	}

	public boolean getState_level() {
		return state_level;
	}

	public void setState_level(boolean state_level) {
		this.state_level = state_level;
	}

	public int getNo_of_state_level_units() {
		return no_of_state_level_units;
	}

	public void setNo_of_state_level_units(int no_of_state_level_units) {
		this.no_of_state_level_units = no_of_state_level_units;
	}

	public boolean getRegional() {
		return regional;
	}

	public void setRegional(boolean regional) {
		this.regional = regional;
	}

	public int getNo_of_regional_units() {
		return no_of_regional_units;
	}

	public void setNo_of_regional_units(int no_of_regional_units) {
		this.no_of_regional_units = no_of_regional_units;
	}

	public boolean getZonal_level() {
		return zonal_level;
	}

	public void setZonal_level(boolean zonal_level) {
		this.zonal_level = zonal_level;
	}

	public int getNo_of_zonal_level_units() {
		return no_of_zonal_level_units;
	}

	public void setNo_of_zonal_level_units(int no_of_zonal_level_units) {
		this.no_of_zonal_level_units = no_of_zonal_level_units;
	}

	public boolean getRegional_level() {
		return regional_level;
	}

	public void setRegional_level(boolean regional_level) {
		this.regional_level = regional_level;
	}

	public int getNo_of_regional_level_units() {
		return no_of_regional_level_units;
	}

	public void setNo_of_regional_level_units(int no_of_regional_level_units) {
		this.no_of_regional_level_units = no_of_regional_level_units;
	}

	public boolean getCircle_level() {
		return circle_level;
	}

	public void setCircle_level(boolean circle_level) {
		this.circle_level = circle_level;
	}

	public int getNo_of_circle_level_units() {
		return no_of_circle_level_units;
	}

	public void setNo_of_circle_level_units(int no_of_circle_level_units) {
		this.no_of_circle_level_units = no_of_circle_level_units;
	}

	public boolean getDivisional() {
		return divisional;
	}

	public void setDivisional(boolean divisional) {
		this.divisional = divisional;
	}

	public int getNo_of_divisional_units() {
		return no_of_divisional_units;
	}

	public void setNo_of_divisional_units(int no_of_divisional_units) {
		this.no_of_divisional_units = no_of_divisional_units;
	}

	public boolean getDistrict_level() {
		return district_level;
	}

	public void setDistrict_level(boolean district_level) {
		this.district_level = district_level;
	}

	public int getNo_of_district_level() {
		return no_of_district_level;
	}

	public void setNo_of_district_level(int no_of_district_level) {
		this.no_of_district_level = no_of_district_level;
	}

	public boolean getIndependent() {
		return independent;
	}

	public void setIndependent(boolean independent) {
		this.independent = independent;
	}

	public int getNo_of_independent() {
		return no_of_independent;
	}

	public void setNo_of_independent(int no_of_independent) {
		this.no_of_independent = no_of_independent;
	}

	private String mode;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	private Map<String,Object>dynaForm = new HashMap();
	
	public Map<String, Object> getDynaForm() {
		return dynaForm;
	}

	public void setDynaForm(Map<String, Object> dynaForm) {
		this.dynaForm = dynaForm;
	}
	
	public Object getDynaForm(String key) {
		return dynaForm.get(key);
	}

	public void setDynaForm(String key,Object val) {
		this.dynaForm.put(key, val);
	}
	
	private HashMap hMapOthers1 = new HashMap();
	
	public void setDynaForm1(String key,String[] value)
	{
		this.hMapOthers1.put(key,value);
	}
	public String[] getDynaForm1(String key)
	{
		return (String[]) this.hMapOthers1.get(key);
	}
	
	public void reset() {
		setDynaForm(new HashMap());
	}
	
}
