package entryforms.stages;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ParawiseRemarksFormBean extends ValidatorForm {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	private String caseYear;
	private String caseNo;
	private String dtGPReqtdForPR;
	private String dtApprovalpw;
	private String caseType;
	private String addressedTo;
	private String dtPRSentToGP;
	private String dtPRRecvdByGP;
	private String dtDeptRecvdPRReqt;
	private String action;

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParaRemarks() {
		return paraRemarks;
	}

	public void setParaRemarks(String paraRemarks) {
		this.paraRemarks = paraRemarks;
	}

	private String relatedGp;
	private String paraRemarks;
	
	ArrayList gps = null;

	private String courtType;
	private ArrayList courtList;
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		return errors;
	}
	public ParawiseRemarksFormBean() {
	}

	public String getCourtType() {
		return courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ArrayList getGps() {
		return gps;
	}

	public ArrayList getCourtList() {
		return courtList;
	}

	public void setCourtList(ArrayList courtList) {
		this.courtList = courtList;
	}



	public String getCaseYear() {
		return caseYear;
	}

	public void setCaseYear(String caseYear) {
		this.caseYear = caseYear;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	
	public String getDtGPReqtdForPR() {
		return dtGPReqtdForPR;
	}

	public void setDtGPReqtdForPR(String dtGPReqtdForPR) {
		this.dtGPReqtdForPR = dtGPReqtdForPR;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getAddressedTo() {
		return addressedTo;
	}

	public void setAddressedTo(String addressedTo) {
		this.addressedTo = addressedTo;
	}

	public String getDtPRSentToGP() {
		return dtPRSentToGP;
	}

	public void setDtPRSentToGP(String dtPRSentToGP) {
		this.dtPRSentToGP = dtPRSentToGP;
	}

	public String getDtPRRecvdByGP() {
		return dtPRRecvdByGP;
	}

	public void setDtPRRecvdByGP(String dtPRRecvdByGP) {
		this.dtPRRecvdByGP = dtPRRecvdByGP;
	}

	public String getDtDeptRecvdPRReqt() {
		return dtDeptRecvdPRReqt;
	}

	public void setDtDeptRecvdPRReqt(String dtDeptRecvdPRReqt) {
		this.dtDeptRecvdPRReqt = dtDeptRecvdPRReqt;
	}

	
	

	public String getRelatedGp() {
		return relatedGp;
	}

	public void setRelatedGp(String relatedGp) {
		this.relatedGp = relatedGp;
	}

	public void setGps(ArrayList gps) {
		this.gps = gps;
	}

	public String getDtApprovalpw() {
		return dtApprovalpw;
	}

	public void setDtApprovalpw(String dtApprovalpw) {
		this.dtApprovalpw = dtApprovalpw;
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
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}
}
