package in.apcfss.struts.Forms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class CommonForm extends ActionForm implements Serializable{
	private static final long serialVersionUID = 1L;

	
	private String mode;
	
	private Map<String,Object>dynaForm = new HashMap<String, Object>();

	private FormFile changeLetter;
	
	String[] deptId;
	
	public String[] getDeptId() {
		return deptId;
	}

	public void setDeptId(String[] deptId) {
		this.deptId = deptId;
	}

	public FormFile getChangeLetter() {
		return changeLetter;
	}

	public void setChangeLetter(FormFile changeLetter) {
		this.changeLetter = changeLetter;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

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
	
	public void reset() {
		setDynaForm(new HashMap<String, Object>());
		//setFileUpload(new HashMap<String, FormFile>());
	}
	
	/*
	 * private Map<String,FormFile>fileUpload = new HashMap<String, FormFile>();
	 * 
	 * public Map<String, FormFile> getFileUpload() { return fileUpload; }
	 * 
	 * public void setFileUpload(Map<String, FormFile> fileUpload) { this.fileUpload
	 * = fileUpload; }
	 * 
	 * public FormFile getFileUpload(String key) { return fileUpload.get(key); }
	 * 
	 * public void setFileUpload(String key,FormFile fileUpload) {
	 * this.fileUpload.put(key, fileUpload); }
	 */
}
