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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class WPAffidavitFormBean extends ActionForm {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	private String serialNo;
	private String caseNo;
	private String caseType;
	private String caseYear;
	private String secyFileNo;
	private String gpFileNo;
	private String natureofcase;
	private String interimOrder;
	private String interimNo;
	private String interimType;
	private String dueDate;
	private String interimDirectionImplemented;
	private String interimYear;
	String[] deptHod;
	String[] petAdvocates;
	String[] respAdvocates;
	private String courtType;
	private ArrayList courtList;
	private ArrayList caseTypesList;
	private ArrayList sectionList;
	private String petName;
	private String petAdvocate;
	private String respName;
	private String respAdvocate;
	private String respAdvocateCode;
	private String remarks;
	private String suit;
	private String subCategory;
	private String deptHodName;
	private String brfDiscription;
	private String priority;
	private String dtPetRecvByDept;
	private String dtFiling;
	private String dtListing;
	private String stage;
	private String status;
	private String implement;
	private String action;
	private String isPetRes;
	private String prayer;
	private String sno;
	private String caseid;
	private String otherSubject;
	private String interimDirection;
	private String section;
	private String[] stDistrict;
	private String[] petNames;
	private String[] respNames;
	private String[] authpwca;
	

	public String getRespAdvocateCode() {
		return respAdvocateCode;
	}

	public void setRespAdvocateCode(String respAdvocateCode) {
		this.respAdvocateCode = respAdvocateCode;
	}

	public WPAffidavitFormBean() {
	}

	public String getPrayer() {
		return prayer;
	}

	public void setPrayer(String prayer) {
		this.prayer = prayer;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		gpFileNo = (this.secyFileNo = null);
		petName = (this.petAdvocate = this.respName = this.respAdvocate = null);
		stDistrict = null;
		subCategory = (this.brfDiscription = this.priority = null);
		suit = "0";
		dtPetRecvByDept = (this.dtFiling = this.dtListing = this.stage = null);
		action = null;
		deptHod = (this.petAdvocates = this.respAdvocates = null);
		interimDirection = (this.interimNo = this.interimType = this.interimYear = null);
		isPetRes = null;
		respNames = null;
		petNames = null;
		this.request = request;
		remarks = "";
	}

	public ArrayList getSectionList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		String[] user = (String[]) request.getSession().getAttribute("user");
		String uDeptId = "";
		if ((user[3].contains("dd_")) || (user[3].contains("jd_"))
				|| (user[3].contains("ed_")) || (user[3].contains("eo_"))) {
			uDeptId = "999";
		} else if ((user[3].contains("hod_")) || (user[3].contains("dd1"))
				|| (user[3].contains("guest")) || (user[3].contains("gml"))
				|| (user[3].contains("vcmd")) || (user[3].contains("aeo"))
				|| (user[3].contains("so_"))) {
			uDeptId = DatabasePlugin
					.getStringfromQuery("SELECT coalesce(DEPT_ID,0) FROM APP_USER_MST WHERE USERID='"
							+ user[3] + "'");
		}

		try {
			GeneralQueries genqry = new GeneralQueries(ds);

			list = genqry.getSections(uDeptId, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCaseYear() {
		return caseYear;
	}

	public void setCaseYear(String caseYear) {
		this.caseYear = caseYear;
	}

	public String getSecyFileNo() {
		return secyFileNo;
	}

	public void setSecyFileNo(String secyFileNo) {
		this.secyFileNo = secyFileNo;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getGpFileNo() {
		return gpFileNo;
	}

	public void setGpFileNo(String gpFileNo) {
		this.gpFileNo = gpFileNo;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetAdvocate(String petAdvocate) {
		this.petAdvocate = petAdvocate;
	}

	public String getPetAdvocate() {
		return petAdvocate;
	}

	public void setRespName(String respName) {
		this.respName = respName;
	}

	public String getRespName() {
		return respName;
	}

	public void setRespAdvocate(String respAdvocate) {
		this.respAdvocate = respAdvocate;
	}

	public String getRespAdvocate() {
		return respAdvocate;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getSuit() {
		return suit;
	}

	public String getInterimNo() {
		return interimNo;
	}

	public void setInterimNo(String interimNo) {
		this.interimNo = interimNo;
	}

	public String getInterimType() {
		return interimType;
	}

	public void setInterimType(String interimType) {
		this.interimType = interimType;
	}

	public String getInterimYear() {
		return interimYear;
	}

	public void setInterimYear(String interimYear) {
		this.interimYear = interimYear;
	}

	public String getInterimOrder() {
		return interimOrder;
	}

	public void setInterimOrder(String interimOrder) {
		this.interimOrder = interimOrder;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public String getDeptHodName() {
		return deptHodName;
	}

	public void setDeptHodName(String deptHodName) {
		this.deptHodName = deptHodName;
	}

	public void setBrfDiscription(String brfDiscription) {
		this.brfDiscription = brfDiscription;
	}

	public String getBrfDiscription() {
		return brfDiscription;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPriority() {
		return priority;
	}

	public void setDtPetRecvByDept(String dtPetRecvByDept) {
		this.dtPetRecvByDept = dtPetRecvByDept;
	}

	public String getDtPetRecvByDept() {
		return dtPetRecvByDept;
	}

	public void setDtFiling(String dtFiling) {
		this.dtFiling = dtFiling;
	}

	public String getDtFiling() {
		return dtFiling;
	}

	public void setDtListing(String dtListing) {
		this.dtListing = dtListing;
	}

	public String getDtListing() {
		return dtListing;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int setCaseDetails(ResultSet rs, String[] user) {
		ArrayList otherAdmissions = new ArrayList();
		ArrayList list = null;

		int i = 0;
		try {
			if (rs.next()) {

				i = 1;

				setPrayer(rs.getString(24));
				setSerialNo(rs.getString(16));
				setSno(rs.getString(16));
				setGpFileNo((rs.getString(17) != null)
						&& (!rs.getString(17).equals("null")) ? rs
						.getString(17) : "");
				setSecyFileNo((rs.getString(18) != null)
						&& (!rs.getString(18).equals("null")) ? rs
						.getString(18) : "");
				setPetName((rs.getString(2) != null)
						&& (!rs.getString(2).equals("null")) ? rs.getString(2)
						: "");
				setPetAdvocate((rs.getString(4) != null)
						&& (!rs.getString(4).equals("null")) ? rs.getString(4)
						: "");
				setRespName((rs.getString(3) != null)
						&& (!rs.getString(3).equals("null")) ? rs.getString(3)
						: "");
				setRespAdvocate((rs.getString(5) != null)
						&& (!rs.getString(5).equals("null")) ? rs.getString(5)
						: "");
				setSuit(rs.getString(23));

				setSubCategory(rs.getString(20));

				setBrfDiscription(rs.getString(12));
				setPriority(rs.getString(21));
				setDtPetRecvByDept(rs.getString(22));
				setDtFiling(rs.getString(14));
				setDtListing(rs.getString(13));
				setStage(rs.getString(19));
				setStatus(rs.getString(9));
				setIsPetRes(rs.getString(34));
				setCourtType(rs.getString("COURT_ID"));

				String[] pwc = rs.getString("PWC_AUTH") != null ? rs.getString(
						"PWC_AUTH").split(",") : null;
				setAuthpwca(pwc);
				setNatureofcase(rs.getString("NATURE_OF_CASE"));
				setSection(rs.getString("SECTION_NO"));

				request.setAttribute("IS_GOV_PET_RES",
						rs.getString("IS_GOV_PET_RES"));

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return i;
	}

	public ArrayList getAllCaseTypes() {
		ArrayList list = new ArrayList();
		list = GeneralOperations.getOriginalCaseTypes();
		return list;
	}

	public ArrayList getFiftyYears() {
		ArrayList list = new ArrayList();
		list = GeneralOperations.getLast50Years();
		return list;
	}

	public ArrayList getPrioritiesList() {
		ArrayList list = new ArrayList();
		list = GeneralOperations.getPriorities();
		System.out.println("prior :: " + list);
		return list;
	}

	public ArrayList getDistrictsList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		try {
			GeneralQueries genqry = new GeneralQueries(ds);
			String[] user = GeneralOperations.getSessionUser(request);

			list = genqry.getDistrictsList(user[3], true);
		} catch (Exception localException) {
		}

		request.setAttribute("districtsList", list);
		return list;
	}

	public ArrayList getSubjectCategorysList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		Object[] user = (Object[]) request.getSession().getAttribute("user");
		String uDeptId = DatabasePlugin
				.getStringfromQuery("SELECT coalesce(DEPT_ID,0) FROM APP_USER_MST WHERE USERID='"
						+ user[3] + "'");
		try {
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getSubjectCategories(true, uDeptId);
		} catch (Exception localException) {
		}

		return list;
	}

	public ArrayList getDeptHodList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		Object[] user = (Object[]) request.getSession().getAttribute("user");
		System.out.println("userid :: " + user[3]);
		String uDeptId = DatabasePlugin
				.getStringfromQuery("SELECT coalesce(DEPT_ID,0) FROM APP_USER_MST WHERE USERID='" + user[3] + "'");
		try {
			GeneralQueries genqry = new GeneralQueries(ds);

			if (user[3].toString().contains("SECTN")) {
				uDeptId = "0";
			}
			list = genqry.getDeptHodsforGPs(true, uDeptId);
		} catch (Exception localException) {
		}

		request.setAttribute("deptHodList", list);
		return list;
	}

	public ArrayList getDeptGPsList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		String[] user = GeneralOperations.getSessionUser(request);

		try {
			GeneralQueries genqry = new GeneralQueries(ds);

			list = genqry.getDeptGPsforGPs(true, user[0], courtType);
		} catch (Exception localException) {
		}

		request.setAttribute("deptGPsList", list);
		return list;
	}

	public ArrayList getHodGPsList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		String[] user = GeneralOperations.getSessionUser(request);
		try {
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getHodGPs(true, user[0]);
		} catch (Exception localException) {
		}

		return list;
	}

	public ArrayList SubjectCategorysList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		Object[] user = (Object[]) request.getSession().getAttribute("user");
		String uDeptId = DatabasePlugin
				.getStringfromQuery("SELECT coalesce(DEPT_ID,0) FROM APP_USER_MST WHERE USERID='"
						+ user[3] + "'");
		try {
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getSubjectCategories(true, uDeptId);
		} catch (Exception localException) {
		}

		return list;
	}

	public ArrayList getStagesList() {
		ArrayList list = new ArrayList();

		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		try {
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getStages(true, "Original");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList getInterimTypes() {
		ArrayList interims = new ArrayList();
		interims = LoadResourceAction.getcasetypes(
				GeneralSQL.getDataSource(servlet, "lcmsdb"), courtType,
				"InterimCase", false);
		interims = LoadResourceAction.getLabelValueBeans(interims, true);
		return interims;
	}

	public String getIsPetRes() {
		return isPetRes;
	}

	public void setIsPetRes(String isPetRes) {
		this.isPetRes = isPetRes;
	}

	public String[] getPetAdvocates() {
		return petAdvocates;
	}

	public void setPetAdvocates(String[] petAdvocates) {
		this.petAdvocates = petAdvocates;
	}

	public String[] getRespAdvocates() {
		return respAdvocates;
	}

	public void setRespAdvocates(String[] respAdvocates) {
		this.respAdvocates = respAdvocates;
	}

	public void setDeptHod(String[] deptHod) {
		this.deptHod = deptHod;
	}

	public String[] getDeptHod() {
		return deptHod;
	}

	public String getCourtType() {
		return courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	public ArrayList getCourtList() {
		ArrayList list = null;
		try {
			DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getCourts();
		} catch (Exception localException) {
		}

		return list;
	}

	public ArrayList getCaseTypesList() {
		ArrayList list = null;
		try {
			DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
			GeneralQueries genqry = new GeneralQueries(ds);
			list = genqry.getCasetypes(getCourtType(), "OriginalCase", false);
		} catch (Exception localException) {
		}

		return list;
	}

	public void setCaseTypesList(ArrayList caseTypesList) {
		this.caseTypesList = caseTypesList;
	}

	public void setCourtList(ArrayList courtList) {
		this.courtList = courtList;
	}

	public String getOtherSubject() {
		return otherSubject;
	}

	public void setOtherSubject(String otherSubject) {
		this.otherSubject = otherSubject;
	}

	public String[] getStDistrict() {
		return stDistrict;
	}

	public void setStDistrict(String[] stDistrict) {
		this.stDistrict = stDistrict;
	}

	public ArrayList getOfficerList() {
		ArrayList list = new ArrayList();
		DataSource ds = GeneralSQL.getDataSource(servlet, "lcmsdb");
		String[] user = GeneralOperations.getSessionUser(request);
		try {
			GeneralQueries genqry = new GeneralQueries(ds);

			list = genqry.getOfficerList(true, user[0]);
		} catch (Exception localException) {
		}

		request.setAttribute("officerList", list);
		return list;
	}

	public String[] getPetNames() {
		return petNames;
	}

	public void setPetNames(String[] petNames) {
		this.petNames = petNames;
	}

	public String[] getRespNames() {
		return respNames;
	}

	public void setRespNames(String[] respNames) {
		this.respNames = respNames;
	}

	public String getInterimDirection() {
		return interimDirection;
	}

	public void setInterimDirection(String interimDirection) {
		this.interimDirection = interimDirection;
	}

	public String getImplement() {
		return implement;
	}

	public void setImplement(String implement) {
		this.implement = implement;
	}

	public String[] getAuthpwca() {
		return authpwca;
	}

	public void setAuthpwca(String[] authpwca) {
		this.authpwca = authpwca;
	}

	public String getNatureofcase() {
		return natureofcase;
	}

	public void setNatureofcase(String natureofcase) {
		this.natureofcase = natureofcase;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInterimDirectionImplemented() {
		return interimDirectionImplemented;
	}

	public void setInterimDirectionImplemented(
			String interimDirectionImplemented) {
		this.interimDirectionImplemented = interimDirectionImplemented;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
