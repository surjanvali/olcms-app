package in.apcfss.struts.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.json.JSONException;
import org.json.JSONObject;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.eCourt.apis.ECourtAPIs;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import in.apcfss.struts.eCourt.apis.EHighCourtAPI;
import in.apcfss.struts.eCourt.apis.HASHHMACJava;
import in.apcfss.struts.eCourt.apis.HighCourtCauseListAPI;
import in.apcfss.struts.eCourt.apis.HighCourtCauseListBenchAPI;
import plugins.DatabasePlugin;

public class UpdateEcourtsDataAction extends DispatchAction {

	@Override

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		String userId = null, sqlCondition = "";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			con = DatabasePlugin.connect();

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("distList", DatabasePlugin
					.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	public ActionForward showCaseWise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.date_of_filing >= to_date('" + cform.getDynaForm("dofFromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.date_of_filing <= to_date('" + cform.getDynaForm("dofToDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			sql = "select x.cino,x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases  from ("
					+ "select a.dept_code ,a.cino, case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,count(*) as total_cases, "
					+ "sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept, "
					+ "sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo, "
					+ "sum(case when case_status=3  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod, "
					+ "sum(case when case_status=4  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal, "
					+ "sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection, "
					+ "sum(case when case_status=7  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc, "
					+ "sum(case when case_status=8  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno, "
					+ "sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod, "
					+ "sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist, "
					+ "sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo, "
					+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases "
					+ "from ecourts_case_data a " + "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true " + sqlCondition;

			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
						+ session.getAttribute("dept_code") + "')";

			if (roleId.equals("2")) {
				sql += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			sql += "group by a.dept_code,d.dept_code ,reporting_dept_code,a.cino ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
					+ "group by x.reporting_dept_code, d1.description,x.cino order by 1 ";

			request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CASEWISEACKS", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst where district_id='"
										+ session.getAttribute("dist_id") + "' order by district_name",
								con));
			else
				cform.setDynaForm("distList", DatabasePlugin
						.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			// request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}

	public ActionForward updateCino(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		ResultSet rs = null;
		Statement st = null;
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		int totalCount = 0;
		String inputStr = "", targetURL = "";
		String authToken = "";
		String request_token = "", requeststring = "";

		try {
			System.out.println("update--------------" + cform.getDynaForm("cino"));
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.date_of_filing >= to_date('" + cform.getDynaForm("dofFromDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.date_of_filing <= to_date('" + cform.getDynaForm("dofToDate")
						+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (cform.getDynaForm("cino") != null && !cform.getDynaForm("cino").toString().contentEquals("")
					&& !cform.getDynaForm("cino").toString().contentEquals("0")) {
				sqlCondition += " and a.cino='" + cform.getDynaForm("cino").toString().trim() + "' ";
			}

			String opVal = ECourtAPIs.getSelectParam(1);

			String cino = "";// cform.getDynaForm("cino").toString();

			sql = "select cino from ecourts_case_data a where last_updated_ecourts<=now()::date - integer '2' "
					+ sqlCondition + " order by last_updated_ecourts asc limit 2500";
			// sql="select cino from ecourts_case_data where cino='APHC010183002019'";
			System.out.println("SQLLLLLLLLLLLLLL:::::::"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				// if (cino != null) {
				cino = rs.getString("cino").toString().trim();
				totalCount++;
				inputStr = "cino=" + cino;// ECourtAPIs.getInputStringValue(opVal);

				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":opVal : " + opVal);

				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);

				authToken = EHighCourtAPI.getAuthToken();
				System.out.println("authToken---" + authToken);
				String resp = "";
				System.out.println("OPVAL:" + opVal);

				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);

				//System.out.println("resp--" + resp);

				if (resp != null && !resp.equals("")) {
					boolean b = processCNRsearchResponse(resp, opVal, con, cino);

					if (b) {
						request.setAttribute("successMsg", "Successfully saved/ Updated data form ecourts.");
					} else {
						request.setAttribute("errorMsg", "Error-1 while Updating data form ecourts.");
					}
				}
			}
			System.out.println("FINAL END : Records fetched:" + totalCount);
			request.setAttribute("successMsg", "Successfully saved/ Updated "+totalCount+" records data form ecourts.");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Error-3 while Updating data form ecourts.");
			e.printStackTrace();
		} finally {

			cform.setDynaForm("distList", DatabasePlugin
					.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
		// return showCaseWise(mapping, cform, request, response);
	}

	
	public ActionForward importNewCinosData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println( "HighCourtCasesListAction....................................uploadandRetrieveEcourtsData()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		ResultSet rs = null;
		Statement st = null;
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		int totalCount = 0;
		String inputStr = "", targetURL = "";
		String authToken = "";
		String request_token = "", requeststring = "";

		try {
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			String opVal = ECourtAPIs.getSelectParam(1);
			
			String cino = "";

			sql = "SELECT cino FROM apolcms.ecourts_cinos_new where inserted_time::date = current_date and coalesce(ecourts_response,'')=''";
			
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				// if (cino != null) {
				cino = rs.getString("cino").toString().trim();
				totalCount++;
				inputStr = "cino=" + cino;// ECourtAPIs.getInputStringValue(opVal);

				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":opVal : " + opVal);

				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);

				authToken = EHighCourtAPI.getAuthToken();
				System.out.println("authToken---" + authToken);
				String resp = "";
				System.out.println("OPVAL:" + opVal);

				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);

				//System.out.println("resp--" + resp);

				if (resp != null && !resp.equals("")) {
					boolean b = processCNRsearchResponseInsert(resp, opVal, con, cino);

					if (b) {
						request.setAttribute("successMsg", "Successfully saved/ Updated data form ecourts.");
					} else {
						request.setAttribute("errorMsg", "Error-1 while Updating data form ecourts.");
					}
				}
			}
			System.out.println("FINAL END : Records fetched:" + totalCount);

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Error-3 while Updating data form ecourts.");
			e.printStackTrace();
		} finally {

			cform.setDynaForm("distList", DatabasePlugin
					.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
		// return showCaseWise(mapping, cform, request, response);
	}
	
	//EXCE LUPLOAD METHOD
	public ActionForward uploadandRetrieveEcourtsData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println( "HighCourtCasesListAction....................................uploadandRetrieveEcourtsData()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		ResultSet rs = null;
		Statement st = null;
		String sql = null, roleId = null, deptCode = null, distCode = "0";
		int totalCount = 0;
		String inputStr = "", targetURL = "";
		String authToken = "";
		String request_token = "", requeststring = "";

		try {
			String sqlCondition = "";
			con = DatabasePlugin.connect();

			String opVal = ECourtAPIs.getSelectParam(1);
			FormFile cinoExcelFile = cform.getChangeLetter();
			// Read from excel and import into database
			// saveDataToTableFromExcel(cinoExcelFile, con);
			
			String cino = "";// cform.getDynaForm("cino").toString();

			sql = "SELECT cino FROM apolcms.ecourts_cinos_new where inserted_time::date=current_date";
			
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				// if (cino != null) {
				cino = rs.getString("cino").toString().trim();
				totalCount++;
				inputStr = "cino=" + cino;// ECourtAPIs.getInputStringValue(opVal);

				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":opVal : " + opVal);

				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);

				authToken = EHighCourtAPI.getAuthToken();
				System.out.println("authToken---" + authToken);
				String resp = "";
				System.out.println("OPVAL:" + opVal);

				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);

				//System.out.println("resp--" + resp);

				if (resp != null && !resp.equals("")) {
					boolean b = processCNRsearchResponseInsert(resp, opVal, con, cino);

					if (b) {
						request.setAttribute("successMsg", "Successfully saved/ Updated data form ecourts.");
					} else {
						request.setAttribute("errorMsg", "Error-1 while Updating data form ecourts.");
					}
				}
			}
			System.out.println("FINAL END : Records fetched:" + totalCount);

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Error-3 while Updating data form ecourts.");
			e.printStackTrace();
		} finally {

			cform.setDynaForm("distList", DatabasePlugin
					.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
		// return showCaseWise(mapping, cform, request, response);
	}
	
	public static boolean processCNRsearchResponse(String resp, String fileName, Connection con, String cino)
			throws Exception {
		String response_str = "";
		String response_token = "";
		String version = "";
		String decryptedRespStr = "";
		String sql = "";
		resp = resp.trim();
		//System.out.println("processCNRsearchResponse:" + resp);
		if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN")) && (!resp.contains("Array("))) {
			JSONObject jObj = new JSONObject(resp);
			if ((jObj.has("response_str")) && (jObj.getString("response_str") != null)) {
				response_str = jObj.getString("response_str").toString();
			}
			if ((jObj.has("response_token")) && (jObj.getString("response_token") != null)) {
				response_token = jObj.getString("response_token").toString();
			}
			if ((jObj.has("version")) && (jObj.getString("version") != null)) {
				version = jObj.getString("version").toString();
			}
			if ((response_str != null) && (!response_str.equals(""))) {
				decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
			}
			
			if(decryptedRespStr!=null && !decryptedRespStr.equals(""))
			{
			JSONObject jObjCaseData = new JSONObject(decryptedRespStr);
			ArrayList<String> sqls = new ArrayList();

			sql = "update apolcms.ecourts_case_data set date_of_filing=to_date('" +

					jObjCaseData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'), " +

					" dt_regis= to_date('" + jObjCaseData.get("dt_regis").toString() + "', 'yyyy-mm-dd'), "
					+ "type_name_fil='" + checkStringJSONObj(jObjCaseData, "type_name_fil") + "' ," + "type_name_reg='"
					+ checkStringJSONObj(jObjCaseData, "type_name_reg") + "' ," + "case_type_id='"
					+ checkIntegerJSONObj(jObjCaseData, "case_type_id") + "' ," + "fil_no='"
					+ checkIntegerJSONObj(jObjCaseData, "fil_no") + "' ," + "fil_year='"
					+ checkIntegerJSONObj(jObjCaseData, "fil_year") + "' ," + "reg_no='"
					+ checkIntegerJSONObj(jObjCaseData, "reg_no") + "' ," + "reg_year='"
					+ checkIntegerJSONObj(jObjCaseData, "reg_year") + "' ," + "date_first_list=to_date('"
					+ jObjCaseData.get("date_first_list").toString() + "', 'yyyy-mm-dd'), date_next_list=to_date('"
					+ jObjCaseData.get("date_next_list").toString() + "', 'yyyy-mm-dd'), " + "pend_disp='"
					+ checkStringJSONObj(jObjCaseData, "pend_disp") + "' ," + "date_of_decision=to_date('"
					+ jObjCaseData.get("date_of_decision").toString() + "', 'yyyy-mm-dd'), " + "disposal_type='"
					+ checkStringJSONObj(jObjCaseData, "disposal_type") + "' ," + "bench_type='"
					+ checkIntegerJSONObj(jObjCaseData, "bench_type") + "' ," + "causelist_type='"
					+ checkStringJSONObj(jObjCaseData, "causelist_type") + "' ," + "bench_name='"
					+ checkStringJSONObj(jObjCaseData, "bench_name") + "' ," + "judicial_branch='"
					+ checkStringJSONObj(jObjCaseData, "judicial_branch") + "' ," + "coram='"
					+ checkStringJSONObj(jObjCaseData, "coram") + "' ," + "short_order='"
					+ checkStringJSONObj(jObjCaseData, "short_order") + "' ," + "bench_id='"
					+ checkIntegerJSONObj(jObjCaseData, "bench_id") + "' ," + "court_est_name='"
					+ checkStringJSONObj(jObjCaseData, "court_est_name") + "' ," + "est_code='"
					+ checkStringJSONObj(jObjCaseData, "est_code") + "' ," + "state_name='"
					+ checkStringJSONObj(jObjCaseData, "state_name") + "' ," + "dist_name='"
					+ checkStringJSONObj(jObjCaseData, "dist_name") + "' ," + "purpose_name='"
					+ checkStringJSONObj(jObjCaseData, "purpose_name") + "' ," + "pet_name='"
					+ checkStringJSONObj(jObjCaseData, "pet_name") + "' ," + "pet_adv='"
					+ checkStringJSONObj(jObjCaseData, "pet_adv") + "' ," + "pet_legal_heir='"
					+ checkStringJSONObj(jObjCaseData, "pet_legal_heir") + "' ," + "res_name='"
					+ checkStringJSONObj(jObjCaseData, "res_name") + "' ," + "res_adv='"
					+ checkStringJSONObj(jObjCaseData, "res_adv") + "' ," + "res_legal_heir='"
					+ checkStringJSONObj(jObjCaseData, "res_legal_heir") + "' ," + "main_matter='"
					+ checkStringJSONObj(jObjCaseData, "main_matter") + "' ," + "fir_no='"
					+ checkStringJSONObj(jObjCaseData, "fir_no") + "' ," + "police_station='"
					+ checkStringJSONObj(jObjCaseData, "police_station") + "' ," + "fir_year='"
					+ checkIntegerJSONObj(jObjCaseData, "fir_year") + "' ," + "lower_court_name='"
					+ checkStringJSONObj(jObjCaseData, "lower_court_name") + "' ," + "lower_court_caseno='"
					+ checkStringJSONObj(jObjCaseData, "lower_court_caseno") + "' ," + "lower_court_dec_dt=to_date('"
					+ jObjCaseData.get("lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), "
					+ "trial_lower_court_name='" + checkStringJSONObj(jObjCaseData, "trial_lower_court_name") + "' ,"
					+ "trial_lower_court_caseno='" + checkStringJSONObj(jObjCaseData, "trial_lower_court_caseno")
					+ "' ," + "trial_lower_court_dec_dt=to_date('"
					+ jObjCaseData.get("trial_lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), "
					+ "date_last_list=to_date('" + jObjCaseData.get("date_last_list").toString() + "', 'yyyy-mm-dd'), "
					+ "main_matter_cino='" + checkStringJSONObj(jObjCaseData, "main_matter_cino") + "' ,"
					+ "date_filing_disp=to_date('" + jObjCaseData.get("date_filing_disp").toString()
					+ "', 'yyyy-mm-dd'), " + "reason_for_rej='" + checkStringJSONObj(jObjCaseData, "reason_for_rej")
					+ "' ," + "category='" + checkStringJSONObj(jObjCaseData, "category") + "' ," + "sub_category='"
					+ checkStringJSONObj(jObjCaseData, "sub_category") + "', last_updated_ecourts=now() "
					+ " where cino='" + cino + "'  ";

			System.out.println("SQL:" + sql);
			sqls.add(sql);

			sql = " select count(*) from ecourts_case_acts where cino='" + cino + "'";
			String count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_case_acts  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_historyofcasehearing where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_historyofcasehearing  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_pet_extra_party where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_pet_extra_party  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_res_extra_party where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_res_extra_party  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_case_iafiling where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_case_iafiling  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_case_link_cases where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_case_link_cases  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}
			sql = " select count(*) from ecourts_case_objections where cino='" + cino + "'";
			count = DatabasePlugin.getSingleValue(con, sql);
			if (Integer.parseInt(count) > 0) {
				sql = "delete from ecourts_case_objections  where cino='" + cino + "'";
				System.out.println("SQL:" + sql);
				DatabasePlugin.executeUpdate(sql, con);
			}

			/*
			 * sql = " select count(*) from ecourts_case_interimorder where cino='" + cino +
			 * "'"; count = DatabasePlugin.getSingleValue(con, sql); if
			 * (Integer.parseInt(count) > 0) { sql =
			 * "delete from ecourts_case_interimorder  where cino='" + cino + "'";
			 * DatabasePlugin.executeUpdate(sql, con); } sql =
			 * " select count(*) from ecourts_case_finalorder where cino='" + cino + "'";
			 * count = DatabasePlugin.getSingleValue(con, sql); if (Integer.parseInt(count)
			 * > 0) { sql = "delete from ecourts_case_finalorder  where cino='" + cino +
			 * "'"; DatabasePlugin.executeUpdate(sql, con); }
			 */

			// System.out.println("acts:" + checkStringJSONObj(jObjCaseData, "acts"));
			JSONObject jObjActsData = new JSONObject();
			JSONObject jObjActsInnerData = new JSONObject();
			if ((checkStringJSONObj(jObjCaseData, "acts") != null)
					&& (checkStringJSONObj(jObjCaseData, "acts") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "acts").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "acts").equals("[]"))) {
				jObjActsData = new JSONObject(checkStringJSONObj(jObjCaseData, "acts"));
				for (int i = 1; i <= jObjActsData.length(); i++) {
					jObjActsInnerData = new JSONObject(jObjActsData.get("act" + i).toString());

					sql = "INSERT INTO apolcms.ecourts_case_acts(cino, act, actname, section) VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
							+ checkStringJSONObj(jObjActsInnerData, "actname") + "', '"
							+ checkStringJSONObj(jObjActsInnerData, "section") + "')";
					System.out.println("ACTS SQL:" + sql);
					sqls.add(sql);
				}
			}
			// System.out.println("historyofcasehearing:" + checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
			JSONObject jObjHistoryData = new JSONObject();
			JSONObject jObjHistoryInnerData = new JSONObject();
			if ((checkStringJSONObj(jObjCaseData, "historyofcasehearing") != null)
					&& (checkStringJSONObj(jObjCaseData, "historyofcasehearing") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("[]"))) {

				jObjHistoryData = new JSONObject(checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
				for (int i = 1; i <= jObjHistoryData.length(); i++) {
					jObjHistoryInnerData = new JSONObject(jObjHistoryData.get("sr_no" + i).toString());

					sql = "INSERT INTO apolcms.ecourts_historyofcasehearing(cino, sr_no, judge_name, business_date, hearing_date, purpose_of_listing, causelist_type)  VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
							+ checkStringJSONObj(jObjHistoryInnerData, "judge_name") + "', " + " to_date('"
							+ jObjHistoryInnerData.get("business_date").toString() + "', 'yyyy-mm-dd'), ";
					if ((jObjHistoryInnerData.get("hearing_date") != null)
							&& (!jObjHistoryInnerData.get("hearing_date").toString().equals("Next Date Not Given"))) {
						sql = sql + " to_date('" + jObjHistoryInnerData.get("hearing_date").toString()
								+ "', 'yyyy-mm-dd'), ";
					} else {
						sql = sql + " to_date(null, 'yyyy-mm-dd'), ";
					}
					sql = sql + " '" + checkStringJSONObj(jObjHistoryInnerData, "purpose_of_listing") + "', '"
							+ checkStringJSONObj(jObjHistoryInnerData, "causelist_type") + "')";
					System.out.println("historyofcasehearing SQL:" + sql);
					sqls.add(sql);
				}
			}
			//System.out.println("pet_extra_party:" + checkStringJSONObj(jObjCaseData, "pet_extra_party"));
			JSONObject jObjMainData = new JSONObject();
			JSONObject jObjInnerData = new JSONObject();
			if ((checkStringJSONObj(jObjCaseData, "pet_extra_party") != null)
					&& (checkStringJSONObj(jObjCaseData, "pet_extra_party") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("[]"))) {
				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "pet_extra_party"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					sql = "INSERT INTO apolcms.ecourts_pet_extra_party(cino, party_no, party_name) VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
							+ checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString())
							+ "')";
					System.out.println("pet_extra_party SQL:" + sql);
					sqls.add(sql);
				}
			}
			//System.out.println("res_extra_party:" + checkStringJSONObj(jObjCaseData, "res_extra_party"));
			if ((checkStringJSONObj(jObjCaseData, "res_extra_party") != null)
					&& (checkStringJSONObj(jObjCaseData, "res_extra_party") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("[]"))) {
				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "res_extra_party"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					sql = "INSERT INTO apolcms.ecourts_res_extra_party(cino, party_no, party_name)  VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
							+ checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString())
							+ "')";
					System.out.println("res_extra_party SQL:" + sql);
					sqls.add(sql);
				}
			}

			//System.out.println("iafiling:" + checkStringJSONObj(jObjCaseData, "iafiling"));
			if ((checkStringJSONObj(jObjCaseData, "iafiling") != null)
					&& (checkStringJSONObj(jObjCaseData, "iafiling") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "iafiling").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "iafiling").equals("[]"))) {

				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "iafiling"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
					sql = "INSERT INTO apolcms.ecourts_case_iafiling(cino, sr_no, ia_number, ia_pet_name, ia_pend_disp, date_of_filing) VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'"
							+ checkStringJSONObj(jObjInnerData, "ia_number") + "','"
							+ checkStringJSONObj(jObjInnerData, "ia_pet_name") + "','"
							+ checkStringJSONObj(jObjInnerData, "ia_pend_disp") + "',";
					if ((jObjInnerData.has("date_of_filing")) && (jObjInnerData.get("date_of_filing") != null)
							&& (jObjInnerData.get("date_of_filing").toString() != "null")) {
						sql = sql + " to_date('" + jObjInnerData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'))";
					} else {
						sql = sql + " to_date(null, 'yyyy-mm-dd'))";
					}
					System.out.println("iafiling SQL:" + sql);
					sqls.add(sql);
				}
			}
			//System.out.println("link_cases:" + checkStringJSONObj(jObjCaseData, "link_cases"));
			if ((checkStringJSONObj(jObjCaseData, "link_cases") != null)
					&& (checkStringJSONObj(jObjCaseData, "link_cases") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "link_cases").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "link_cases").equals("[]"))) {

				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "link_cases"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
					sql = "INSERT INTO apolcms.ecourts_case_link_cases(cino, sr_no, filing_number, case_number) VALUES('"
							+ checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'"
							+ checkStringJSONObj(jObjInnerData, "filing_number") + "','"
							+ checkStringJSONObj(jObjInnerData, "case_number") + "')";
					System.out.println("link_cases SQL:" + sql);
					sqls.add(sql);
				}
			}
			//System.out.println("objections:" + checkStringJSONObj(jObjCaseData, "objections"));
			if ((checkStringJSONObj(jObjCaseData, "objections") != null)
					&& (checkStringJSONObj(jObjCaseData, "objections") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "objections").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "objections").equals("[]"))) {

				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "objections"));
				//System.out.println("-" + jObjMainData.length());

				sql = "INSERT INTO apolcms.ecourts_case_objections(cino, objection_no, objection_desc, scrutiny_date, objections_compliance_by_date, obj_reciept_date) VALUES('"
						+ checkStringJSONObj(jObjCaseData, "cino") + "',1 ,'"
						+ checkStringJSONObj(jObjMainData, "objection1") + "',";
				if ((jObjMainData.get("scrutiny_date") != null)
						&& (jObjMainData.get("scrutiny_date").toString() != "null")) {
					sql = sql + " to_date('" + jObjMainData.get("scrutiny_date").toString() + "', 'yyyy-mm-dd'),";
				} else {
					sql = sql + " to_date(null, 'yyyy-mm-dd'),";
				}
				if ((jObjMainData.get("objections_compliance_by_date") != null)
						&& (jObjMainData.get("objections_compliance_by_date").toString() != "null")) {
					sql = sql + " to_date('" + jObjMainData.get("objections_compliance_by_date").toString()
							+ "', 'yyyy-mm-dd'),";
				} else {
					sql = sql + " to_date(null, 'yyyy-mm-dd'),";
				}
				if ((jObjMainData.get("obj_reciept_date") != null)
						&& (jObjMainData.get("obj_reciept_date").toString() != "null")) {
					sql = sql + " to_date('" + jObjMainData.get("obj_reciept_date").toString() + "', 'yyyy-mm-dd'))";
				} else {
					sql = sql + " to_date(null, 'yyyy-mm-dd'))";
				}
				System.out.println("objections SQL:" + sql);
				sqls.add(sql);
			}

			//System.out.println("interimorder:" + checkStringJSONObj(jObjCaseData, "interimorder"));
			if ((checkStringJSONObj(jObjCaseData, "interimorder") != null)
					&& (checkStringJSONObj(jObjCaseData, "interimorder") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "interimorder").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "interimorder").equals("[]"))) {
				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "interimorder"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());

					sql = "select count(*) from ecourts_case_interimorder where cino='"
							+ checkStringJSONObj(jObjCaseData, "cino") + "' and sr_no=" + i + " and order_no='"
							+ checkIntegerJSONObj(jObjInnerData, "order_no") + "'";
					if (Integer.parseInt(DatabasePlugin.getStringfromQuery(sql, con)) == 0) {

						sql = "INSERT INTO apolcms.ecourts_case_interimorder(cino, sr_no, order_no, order_date, order_details)  VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'"
								+ checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
						if ((jObjInnerData.get("order_date") != null)
								&& (jObjInnerData.get("order_date").toString() != "null")) {
							sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd'),";
						} else {
							sql = sql + " to_date(null, 'yyyy-mm-dd'),";
						}
						sql = sql + " '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";

						System.out.println("interimorder SQL:" + sql);
						sqls.add(sql);
					}
				}
			}
			//System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
			if ((checkStringJSONObj(jObjCaseData, "finalorder") != null)
					&& (checkStringJSONObj(jObjCaseData, "finalorder") != "null")
					&& (!checkStringJSONObj(jObjCaseData, "finalorder").equals(""))
					&& (!checkStringJSONObj(jObjCaseData, "finalorder").equals("[]"))) {
				jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "finalorder"));
				for (int i = 1; i <= jObjMainData.length(); i++) {
					jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());

					sql = "select count(*) from ecourts_case_finalorder where cino='"
							+ checkStringJSONObj(jObjCaseData, "cino") + "' and sr_no=" + i + " and order_no='"
							+ checkIntegerJSONObj(jObjInnerData, "order_no") + "'";
					if (Integer.parseInt(DatabasePlugin.getStringfromQuery(sql, con)) == 0) {
						sql = "INSERT INTO apolcms.ecourts_case_finalorder(cino, sr_no, order_no, order_date, order_details)  VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'"
								+ checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
						if ((jObjInnerData.get("order_date") != null)
								&& (jObjInnerData.get("order_date").toString() != "null")) {
							sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd')";
						} else {
							sql = sql + " to_date(null, 'yyyy-mm-dd')";
						}
						sql = sql + ", '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";
						System.out.println("finalorder SQL:" + sql);
						sqls.add(sql);
					}
				}
			}

			int executedSqls = 0;
			if (sqls.size() > 0) {
				sql = "update ecourts_cinos_new set ecourts_response=null where cino='" + cino
						+ "' and ecourts_response is not null";
				sqls.add(sql);
				executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);

				retrieveOrderDocuments(con, cino, "ecourts_case_interimorder");
				retrieveOrderDocuments(con, cino, "ecourts_case_finalorder");
			}
			System.out.println("Successfully saved...executedSqls:" + executedSqls);
			}
			else {
				System.out.println("DATA NOT UPDATED INVALID DECRYPT STRING");	
			}
			System.out.println("END");
			return true;
		} else {
			System.out.println("ERROR RESPONSE");
			return false;
		}
	}

	public static void retrieveOrderDocuments(Connection con, String cino, String apolcmsOrdersTable) throws Exception {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		int i = 0;
		String authToken = "";
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		String opVal = ECourtAPIs.getSelectParam(44);
		String orderFileName = apolcmsOrdersTable.substring(apolcmsOrdersTable.lastIndexOf("_") + 1);
		System.out.println("opVal:" + opVal);

		sql = "select cino,order_no,to_char(order_date,'yyyy-mm-dd') as order_date, cino||'-" + orderFileName
				+ "-'||order_no as filename from " + apolcmsOrdersTable + " where cino='" + cino
				+ "' and order_document_path is null";
		System.out.println("SQL:" + sql);
		st = con.createStatement();
		rs = st.executeQuery(sql);

		while (rs.next()) {

			inputStr = "cino=" + rs.getString("cino").trim() + "|order_no=" + rs.getString("order_no").trim()
					+ "|order_date=" + rs.getString("order_date").trim();
			System.out.println("INPUT-STR:" + inputStr);

			// 1. Encoding Request Token
			byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

			targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

			System.out.println("URL : " + targetURL);
			System.out.println((++i) + ":Input String : " + inputStr);

			authToken = EHighCourtAPI.getAuthToken();
			String resp = "";
			if (opVal != null && (opVal.equals("hcCurrentStatus") || opVal.equals("dcCurrentStatus"))) {
				resp = EHighCourtAPI.sendPostRequest(targetURL, authToken);
			} else if (opVal != null && !opVal.equals("")) {
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
			}
			if (resp != null && !resp.equals("")) {
				processPDForderResponse(resp, rs.getString("filename").trim(), con, rs.getString("cino").trim(),
						apolcmsOrdersTable);
			}
		}
		System.out.println("FINAL END:" + i);

	}

	public static void processPDForderResponse(String resp, String fileName, Connection con, String cino,
			String apolcmsOrdersTable) throws Exception {

		String response_str = "";
		String response_token = "";
		String version = "";
		String decryptedRespStr = "";
		String sql = "";
		resp = resp.trim();
		String orderFileName = apolcmsOrdersTable.substring(apolcmsOrdersTable.lastIndexOf("_") + 1);
		// System.out.println("processPDForderResponse RESP:"+resp);
		if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN"))
				&& (!resp.contains("RECORD_NOT_FOUND"))) {
			JSONObject jObj = new JSONObject(resp);
			response_str = jObj.getString("response_str").toString();
			// System.out.println("response_str::"+response_str);
			if ((response_str != null) && (!response_str.equals(""))) {
				decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
			}
			if(decryptedRespStr!=null && !decryptedRespStr.equals("")) {
			String fileSeperator=ApplicationVariables.filesepartor;
			String destinationPath = ApplicationVariables.contextPath + "HighCourtsCaseOrders"+fileSeperator;
			System.out.println("destinationPath:" + destinationPath);

			// String
			// filesUploadPath=ApplicationVariables.contextPath+"\\uploads\\HighCourtsCauseList\\"+causelistDate+"\\";

			File upload_folder = new File(destinationPath);
			if (!upload_folder.exists()) {
				upload_folder.mkdirs();
			}
			if (upload_folder.exists()) {
				File pdfFile = new File(destinationPath + fileName + ".pdf");
				System.out.println("pdfFile.exists()::" + pdfFile.exists());

				if (!pdfFile.exists()) {

					FileOutputStream fos = new FileOutputStream(pdfFile);
					byte[] decoder = Base64.getDecoder().decode(decryptedRespStr.replace("\"", "").replace("\\", ""));
					fos.write(decoder);
					System.out.println("PDF File Saved");
					sql = "update " + apolcmsOrdersTable + " set order_document_path='HighCourtsCaseOrders/" + fileName
							+ ".pdf', updated_time=now() where cino||'-" + orderFileName + "-'||order_no='" + fileName
							+ "'";
					DatabasePlugin.executeUpdate(sql, con);
				} else {
					System.out.println("File Already exist.");
				}
			}
			else {
				System.out.println("DATA NOT UPDATED INVALID DECRYPT STRING");	
			}
			}
		} else {
			sql = "update " + apolcmsOrdersTable + " set order_document_path='" + resp + "' where cino||'-"
					+ orderFileName + "-'||order_no='" + fileName + "'";
			DatabasePlugin.executeUpdate(sql, con);

			System.out.println("Invalid/Empty Response");
		}
	}

	public static int checkIntegerJSONObj(JSONObject jObj, String jObjKey) throws NumberFormatException, JSONException {
		int a = 0;
		if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals(""))
				&& (jObj.get(jObjKey) != null) && (!jObj.get(jObjKey).toString().trim().equals(""))
				&& (!jObj.get(jObjKey).toString().trim().equals("null"))) {
			a = Integer.parseInt(jObj.get(jObjKey).toString().trim());
		}
		return a;
	}

	public static String checkStringJSONObj(JSONObject jObj, String jObjKey) throws JSONException {
		String a = "";
		if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals(""))
				&& (jObj.get(jObjKey) != null) && (!jObjKey.equals("null"))) {
			a = jObj.get(jObjKey).toString().trim().replace("'", "");
		}
		return a;
	}

	public ActionForward retrieveCauseList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		CommonForm cform = (CommonForm) form;
		try {

			con = DatabasePlugin.connect();
			con.setAutoCommit(false);

			String opVal = ECourtAPIs.getSelectParam(11);

			String estCode = "APHC01", causelistDate = (String) cform.getDynaForm("causeListDate");// "2022-04-25";//
																									// 2022-04-06

			inputStr = "est_code=" + estCode + "|causelist_date=" + causelistDate;// ECourtAPIs.getInputStringValue(opVal);
			// 1. Encoding Request Token
			byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

			targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

			System.out.println(totalCount + ":URL : " + targetURL);
			System.out.println("Input String : " + inputStr);

			authToken = EHighCourtAPI.getAuthToken();
			String resp = "";
			if (opVal != null && !opVal.equals("")) {
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
			}

			if (resp != null && !resp.equals("")) {
				HighCourtCauseListBenchAPI.processApiResponse(resp, estCode, causelistDate, con);
				retrieveCauseList(estCode, causelistDate, con);
				con.commit();
				request.setAttribute("successMsg", "Successfully saved/ Updated Causelist data from ecourts.");
			}

			System.out.println("CAUSE LIST BENCH END");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Error-1 while Updating Cause list data from ecourts.");
			con.rollback();
			e.printStackTrace();
		} finally {

			cform.setDynaForm("distList", DatabasePlugin
					.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}

	public static void retrieveCauseList(String estCode, String causelistDate, Connection con) throws Exception {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		ResultSet rs = null;
		Statement st = null;

		ResultSet rs1 = null;
		Statement st1 = null;

		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		String opVal = ECourtAPIs.getSelectParam(12);

		sql = "select distinct to_char(causelist_date,'dd/mm/yyyy') as causelist_date1, causelist_date from ecourts_causelist_data"
				+ " where causelist_date=to_date('" + causelistDate + "','yyyy-mm-dd')";
		System.out.println("retrieveCauseList SQL:" + sql);
		st1 = con.createStatement();
		rs1 = st1.executeQuery(sql);

		while (rs1.next()) {
			sql = "SELECT slno, est_code, causelist_date, bench_id, judge_name, inserted_time FROM apolcms.ecourts_causelist_data where causelist_date=to_date('"
					+ rs1.getString("causelist_date1") + "','dd/mm/yyyy') order by causelist_date ";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				causelistDate = rs.getString("causelist_date");
				inputStr = "est_code=" + estCode + "|causelist_date=" + causelistDate + "|bench_id="
						+ rs.getString("bench_id");// ECourtAPIs.getInputStringValue(opVal);
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);

				authToken = EHighCourtAPI.getAuthToken();
				String resp = "";
				if (opVal != null && !opVal.equals("")) {
					resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);

				}

				if (resp != null && !resp.equals("")) {
					HighCourtCauseListAPI.processApiResponse(resp, estCode, causelistDate, con);
					retrieveCauselistPdfs(con, causelistDate);

				}
			}
		}
		System.out.println("CAUSE LIST END");

	}

	public static void retrieveCauselistPdfs(Connection con, String causelistDate) throws Exception {

		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		String opVal = ECourtAPIs.getSelectParam(13);

		String estCode = "APHC01", bench_id = "", causelist_id = "";

		sql = "SELECT slno, est_code, causelist_date, bench_id, inserted_time,causelist_id, cause_list_type FROM apolcms.ecourts_causelist_bench_data where causelist_date=to_date('"
				+ causelistDate + "','yyyy-mm-dd')"
				// + "slno=36";
				+ " and causelist_document is null and causelist_id is not null order by causelist_date";
		System.out.println("retrieveCauselistPdfs SQL:" + sql);
		st = con.createStatement();
		rs = st.executeQuery(sql);

		while (rs.next()) {
			// causelistDate = rs.getString("causelist_date");
			bench_id = rs.getString("bench_id");
			causelist_id = rs.getString("causelist_id");

			if (causelistDate != null && bench_id != null && causelist_id != null && !causelistDate.equals("")
					&& !bench_id.equals("") && !causelist_id.equals("")) {
				inputStr = "est_code=" + estCode + "|causelist_date=" + causelistDate + "|bench_id=" + bench_id
						+ "|causelist_id=" + causelist_id;// ECourtAPIs.getInputStringValue(opVal);
				System.out.println("inputStr:" + inputStr);
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"),
						inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);

				authToken = EHighCourtAPI.getAuthToken();
				String resp = "";
				if (opVal != null && !opVal.equals("")) {
					resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
				}

				if (resp != null && !resp.equals("")) {
					processPDForderResponse(resp, estCode, causelistDate, bench_id, causelist_id, con);
				}
			}
		}
		System.out.println("END");
	}

	public static void processPDForderResponse(String resp, String estCode, String causelistDate, String bench_id,
			String causelist_id, Connection con) throws Exception {

		String response_str = "";
		String response_token = "";
		String version = "";
		String decryptedRespStr = "";
		String sql = "";
		resp = resp.trim();
		System.out.println("processPDForderResponse RESP:" + resp);
		if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN"))
				&& (!resp.contains("RECORD_NOT_FOUND")) && !resp.contains("Causelist Not Uploaded")) {
			JSONObject jObj = new JSONObject(resp);
			response_str = jObj.getString("response_str").toString();
			// System.out.println("response_str::"+response_str);
			if ((response_str != null) && (!response_str.equals(""))) {
				decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
			}

			//System.out.println("decryptedRespStr:" + decryptedRespStr);
			if(decryptedRespStr!=null && !decryptedRespStr.equals("")) {
				String fileSeperator=ApplicationVariables.filesepartor;
				String filesUploadPath = ApplicationVariables.contextPath +fileSeperator +"uploads"+fileSeperator+"HighCourtsCauseList"+fileSeperator
						+ causelistDate + fileSeperator;
	
				File upload_folder = new File(filesUploadPath);
				if (!upload_folder.exists()) {
					upload_folder.mkdirs();
				}
				if (upload_folder.exists()) {
					File pdfFile = new File(filesUploadPath + estCode + causelistDate + bench_id + causelist_id + ".pdf");
					FileOutputStream fos = new FileOutputStream(pdfFile);
					byte[] decoder = Base64.getDecoder().decode(decryptedRespStr.replace("\"", "").replace("\\", ""));
					fos.write(decoder);
					System.out.println("PDF File Saved");
	
					sql = "update ecourts_causelist_bench_data set causelist_document='uploads/HighCourtsCauseList/"
							+ causelistDate + "/" + estCode + causelistDate + bench_id + causelist_id
							+ ".pdf' where est_code='" + estCode + "' and causelist_date=to_date('" + causelistDate
							+ "','yyyy-mm-dd') and bench_id='" + bench_id + "' and causelist_id='" + causelist_id + "'";
					System.out.println("UPDATE SQL:" + sql);
					DatabasePlugin.executeUpdate(sql, con);
				}
			}else {
				System.out.println("DATA NOT UPDATED INVALID DECRYPT STRING");	
			}
		}else {
			sql = "update ecourts_causelist_bench_data set causelist_document='" + resp + "' where est_code='" + estCode
					+ "' and causelist_date=to_date('" + causelistDate + "','yyyy-mm-dd') and bench_id='" + bench_id
					+ "' and causelist_id='" + causelist_id + "'";
			System.out.println("UPDATE SQL:" + sql);
			DatabasePlugin.executeUpdate(sql, con);
			System.out.println("Invalid/Empty Response");
		}
	}

	
	public static boolean saveDataToTableFromExcel(FormFile cinoExcelFile,Connection con) throws Exception, IOException {
		String sql = "";
		
		InputStream myFile = cinoExcelFile.getInputStream();
		
		sql = "insert into apolcms.ecourts_cinos_new (cino, case_type, reg_no, reg_year, dept_name, ecourts_response, dept_id, dept_code, inserted_time) "
				+ " values ()";
		
		return true;
	}
	
	
	public static boolean processCNRsearchResponseInsert(String resp, String fileName, Connection con, String cino)
			throws Exception {
	    String response_str = "";String response_token = "";String version = "";String decryptedRespStr = "";String sql = "";
	    resp = resp.trim();
	    boolean savedstatus=false; 
	    System.out.println("processCNRsearchResponse:"+resp);
	    if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN")))
	    {
	      JSONObject jObj = new JSONObject(resp);
	      if ((jObj.has("response_str")) && (jObj.getString("response_str") != null)) {
	        response_str = jObj.getString("response_str").toString();
	      }
	      if ((jObj.has("response_token")) && (jObj.getString("response_token") != null)) {
	        response_token = jObj.getString("response_token").toString();
	      }
	      if ((jObj.has("version")) && (jObj.getString("version") != null)) {
	        version = jObj.getString("version").toString();
	      }
	      if ((response_str != null) && (!response_str.equals(""))) {
	        decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
	      }
	      JSONObject jObjCaseData = new JSONObject(decryptedRespStr);
	      ArrayList<String> sqls = new ArrayList();
	      
	      sql = "INSERT INTO apolcms.ecourts_case_data (date_of_filing, cino, dt_regis, type_name_fil, type_name_reg, case_type_id, fil_no, fil_year, reg_no, reg_year, date_first_list, "
	      		+ "date_next_list, pend_disp, date_of_decision, disposal_type, bench_type, causelist_type, bench_name, judicial_branch, coram, short_order, bench_id, court_est_name, "
	      		+ "est_code, state_name, dist_name, purpose_name, pet_name, pet_adv, pet_legal_heir, res_name, res_adv, res_legal_heir, main_matter, fir_no, police_station, fir_year, "
	      		+ "lower_court_name, lower_court_caseno, lower_court_dec_dt, trial_lower_court_name, trial_lower_court_caseno, trial_lower_court_dec_dt, date_last_list, main_matter_cino, "
	      		+ "date_filing_disp, reason_for_rej, category, sub_category,last_updated_ecourts) values ( to_date('" + 
	      
	        jObjCaseData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "cino") + "' ," + 
	        " to_date('" + jObjCaseData.get("dt_regis").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "type_name_fil") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "type_name_reg") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "case_type_id") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "fil_no") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "fil_year") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "reg_no") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "reg_year") + "' ," + 
	        " to_date('" + jObjCaseData.get("date_first_list").toString() + "', 'yyyy-mm-dd'), " + 
	        " to_date('" + jObjCaseData.get("date_next_list").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "pend_disp") + "' ," + 
	        " to_date('" + jObjCaseData.get("date_of_decision").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "disposal_type") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "bench_type") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "causelist_type") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "bench_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "judicial_branch") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "coram") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "short_order") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "bench_id") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "court_est_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "est_code") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "state_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "dist_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "purpose_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "pet_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "pet_adv") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "pet_legal_heir") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "res_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "res_adv") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "res_legal_heir") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "main_matter") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "fir_no") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "police_station") + "' ," + 
	        "'" + checkIntegerJSONObj(jObjCaseData, "fir_year") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "lower_court_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "lower_court_caseno") + "' ," + 
	        " to_date('" + jObjCaseData.get("lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "trial_lower_court_name") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "trial_lower_court_caseno") + "' ," + 
	        " to_date('" + jObjCaseData.get("trial_lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + 
	        " to_date('" + jObjCaseData.get("date_last_list").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "main_matter_cino") + "' ," + 
	        " to_date('" + jObjCaseData.get("date_filing_disp").toString() + "', 'yyyy-mm-dd'), " + 
	        "'" + checkStringJSONObj(jObjCaseData, "reason_for_rej") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "category") + "' ," + 
	        "'" + checkStringJSONObj(jObjCaseData, "sub_category") + "', now())";
	      
	      //System.out.println("SQL:" + sql);
	      sqls.add(sql);
	      
	      System.out.println("acts:" + checkStringJSONObj(jObjCaseData, "acts"));
	      JSONObject jObjActsData = new JSONObject();
	      JSONObject jObjActsInnerData = new JSONObject();
	      if ((checkStringJSONObj(jObjCaseData, "acts") != null) && (checkStringJSONObj(jObjCaseData, "acts") != "null") && (!checkStringJSONObj(jObjCaseData, "acts").equals("")) && (!checkStringJSONObj(jObjCaseData, "acts").equals("[]")))
	      {
	        jObjActsData = new JSONObject(checkStringJSONObj(jObjCaseData, "acts"));
	        for (int i = 1; i <= jObjActsData.length(); i++)
	        {
	          jObjActsInnerData = new JSONObject(jObjActsData.get("act" + i).toString());
	          
	          sql = "INSERT INTO apolcms.ecourts_case_acts(cino, act, actname, section) VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjActsInnerData, "actname") + "', '" + checkStringJSONObj(jObjActsInnerData, "section") + "')";
	          //System.out.println("ACTS SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("historyofcasehearing:" + checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
	      JSONObject jObjHistoryData = new JSONObject();
	      JSONObject jObjHistoryInnerData = new JSONObject();
	      if ((checkStringJSONObj(jObjCaseData, "historyofcasehearing") != null) && (checkStringJSONObj(jObjCaseData, "historyofcasehearing") != "null") && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("")) && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("[]")))
	      {
	        jObjHistoryData = new JSONObject(checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
	        for (int i = 1; i <= jObjHistoryData.length(); i++)
	        {
	          jObjHistoryInnerData = new JSONObject(jObjHistoryData.get("sr_no" + i).toString());
	          
	          sql = "INSERT INTO apolcms.ecourts_historyofcasehearing(cino, sr_no, judge_name, business_date, hearing_date, purpose_of_listing, causelist_type)  VALUES('" + 
	            checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + 
	            checkStringJSONObj(jObjHistoryInnerData, "judge_name") + "', " + " to_date('" + jObjHistoryInnerData.get("business_date").toString() + "', 'yyyy-mm-dd'), ";
	          if ((jObjHistoryInnerData.get("hearing_date") != null) && (!jObjHistoryInnerData.get("hearing_date").toString().equals("Next Date Not Given"))) {
	            sql = sql + " to_date('" + jObjHistoryInnerData.get("hearing_date").toString() + "', 'yyyy-mm-dd'), ";
	          } else {
	            sql = sql + " to_date(null, 'yyyy-mm-dd'), ";
	          }
	          sql = 
	            sql + " '" + checkStringJSONObj(jObjHistoryInnerData, "purpose_of_listing") + "', '" + checkStringJSONObj(jObjHistoryInnerData, "causelist_type") + "')";
	          //System.out.println("historyofcasehearing SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("pet_extra_party:" + checkStringJSONObj(jObjCaseData, "pet_extra_party"));
	      JSONObject jObjMainData = new JSONObject();
	      JSONObject jObjInnerData = new JSONObject();
	      if ((checkStringJSONObj(jObjCaseData, "pet_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "pet_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "pet_extra_party"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          sql = 
	            "INSERT INTO apolcms.ecourts_pet_extra_party(cino, party_no, party_name) VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString()) + "')";
	         // System.out.println("pet_extra_party SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("res_extra_party:" + checkStringJSONObj(jObjCaseData, "res_extra_party"));
	      if ((checkStringJSONObj(jObjCaseData, "res_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "res_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "res_extra_party"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          sql = 
	          
	            "INSERT INTO apolcms.ecourts_res_extra_party(cino, party_no, party_name)  VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString()) + "')";
	          //System.out.println("res_extra_party SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("interimorder:" + checkStringJSONObj(jObjCaseData, "interimorder"));
	      if ((checkStringJSONObj(jObjCaseData, "interimorder") != null) && (checkStringJSONObj(jObjCaseData, "interimorder") != "null") && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("")) && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "interimorder"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
	          sql = "INSERT INTO apolcms.ecourts_case_interimorder(cino, sr_no, order_no, order_date, order_details)  VALUES('" + 
	            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
	            checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
	          if ((jObjInnerData.get("order_date") != null) && (jObjInnerData.get("order_date").toString() != "null")) {
	            sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd'),";
	          } else {
	            sql = sql + " to_date(null, 'yyyy-mm-dd'),";
	          }
	          sql = sql + " '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";
	          
	          //System.out.println("interimorder SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
	      if ((checkStringJSONObj(jObjCaseData, "finalorder") != null) && (checkStringJSONObj(jObjCaseData, "finalorder") != "null") && (!checkStringJSONObj(jObjCaseData, "finalorder").equals("")) && 
	        (!checkStringJSONObj(jObjCaseData, "finalorder").equals("[]")))
	      {
	        //System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "finalorder"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
	          sql = "INSERT INTO apolcms.ecourts_case_finalorder(cino, sr_no, order_no, order_date, order_details)  VALUES('" + 
	            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
	            checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
	          if ((jObjInnerData.get("order_date") != null) && (jObjInnerData.get("order_date").toString() != "null")) {
	            sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd')";
	          } else {
	            sql = sql + " to_date(null, 'yyyy-mm-dd')";
	          }
	          sql = sql + ", '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";
	          //System.out.println("finalorder SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("iafiling:" + checkStringJSONObj(jObjCaseData, "iafiling"));
	      if ((checkStringJSONObj(jObjCaseData, "iafiling") != null) && (checkStringJSONObj(jObjCaseData, "iafiling") != "null") && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("")) && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "iafiling"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
	          sql = "INSERT INTO apolcms.ecourts_case_iafiling(cino, sr_no, ia_number, ia_pet_name, ia_pend_disp, date_of_filing) VALUES('" + 
	            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
	            checkStringJSONObj(jObjInnerData, "ia_number") + "','" + 
	            checkStringJSONObj(jObjInnerData, "ia_pet_name") + "','" + 
	            checkStringJSONObj(jObjInnerData, "ia_pend_disp") + "',";
	          if ((jObjInnerData.has("date_of_filing")) && (jObjInnerData.get("date_of_filing") != null) && (jObjInnerData.get("date_of_filing").toString() != "null")) {
	            sql = sql + " to_date('" + jObjInnerData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'))";
	          } else {
	            sql = sql + " to_date(null, 'yyyy-mm-dd'))";
	          }
	         // System.out.println("iafiling SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("link_cases:" + checkStringJSONObj(jObjCaseData, "link_cases"));
	      if ((checkStringJSONObj(jObjCaseData, "link_cases") != null) && (checkStringJSONObj(jObjCaseData, "link_cases") != "null") && (!checkStringJSONObj(jObjCaseData, "link_cases").equals("")) && (!checkStringJSONObj(jObjCaseData, "link_cases").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "link_cases"));
	        for (int i = 1; i <= jObjMainData.length(); i++)
	        {
	          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
	          sql = "INSERT INTO apolcms.ecourts_case_link_cases(cino, sr_no, filing_number, case_number) VALUES('" + 
	            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
	            checkStringJSONObj(jObjInnerData, "filing_number") + "','" + 
	            checkStringJSONObj(jObjInnerData, "case_number") + "')";
	         // System.out.println("link_cases SQL:" + sql);
	          sqls.add(sql);
	        }
	      }
	      System.out.println("objections:" + checkStringJSONObj(jObjCaseData, "objections"));
	      if ((checkStringJSONObj(jObjCaseData, "objections") != null) && (checkStringJSONObj(jObjCaseData, "objections") != "null") && (!checkStringJSONObj(jObjCaseData, "objections").equals("")) && (!checkStringJSONObj(jObjCaseData, "objections").equals("[]")))
	      {
	        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "objections"));
	        System.out.println("-" + jObjMainData.length());
	        
	        sql = "INSERT INTO apolcms.ecourts_case_objections(cino, objection_no, objection_desc, scrutiny_date, objections_compliance_by_date, obj_reciept_date) VALUES('" + 
	          checkStringJSONObj(jObjCaseData, "cino") + "',1 ,'" + 
	          checkStringJSONObj(jObjMainData, "objection1") + "',";
	        if ((jObjMainData.get("scrutiny_date") != null) && (jObjMainData.get("scrutiny_date").toString() != "null")) {
	          sql = sql + " to_date('" + jObjMainData.get("scrutiny_date").toString() + "', 'yyyy-mm-dd'),";
	        } else {
	          sql = sql + " to_date(null, 'yyyy-mm-dd'),";
	        }
	        if ((jObjMainData.get("objections_compliance_by_date") != null) && (jObjMainData.get("objections_compliance_by_date").toString() != "null")) {
	          sql = sql + " to_date('" + jObjMainData.get("objections_compliance_by_date").toString() + "', 'yyyy-mm-dd'),";
	        } else {
	          sql = sql + " to_date(null, 'yyyy-mm-dd'),";
	        }
	        if ((jObjMainData.get("obj_reciept_date") != null) && (jObjMainData.get("obj_reciept_date").toString() != "null")) {
	          sql = sql + " to_date('" + jObjMainData.get("obj_reciept_date").toString() + "', 'yyyy-mm-dd'))";
	        } else {
	          sql = sql + " to_date(null, 'yyyy-mm-dd'))";
	        }
	        //System.out.println("objections SQL:" + sql);
	        sqls.add(sql);
	      }
	      int executedSqls = 0;
	      if (sqls.size() > 0) {
	    	  savedstatus = true;
	    	  sql="update ecourts_cinos_new set ecourts_response=null where cino='"+cino+"' and ecourts_response is not null"; 
	    	  sqls.add(sql);
	    	  
	    	  sql="update ecourts_case_data set dept_code=ecourts_cinos_new.dept_code from ecourts_cinos_new where ecourts_case_data.cino=ecourts_cinos_new.cino and ecourts_case_data.cino='"+cino+"'"; 
	    	  sqls.add(sql);
	    	  
	    	  executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);
	    	  
	    	  retrieveOrderDocuments(con, cino, "ecourts_case_interimorder");
			  retrieveOrderDocuments(con, cino, "ecourts_case_finalorder");
	    	  
	      }
	      System.out.println("Successfully saved...executedSqls:"+executedSqls);
	      
	      System.out.println("END");
	    }
	    else
	    {
	    	sql="update ecourts_cinos_new set ecourts_response='"+resp+"' where cino='"+cino+"'";
	    	DatabasePlugin.executeUpdate(sql, con);
	    	System.out.println("Invalid/Empty Response::"+"SQL:"+sql);
	    }
	    
	    return savedstatus;
	  }

}