package in.apcfss.struts.Actions;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.json.JSONException;
import org.json.JSONObject;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.eCourt.apis.ECourtAPIs;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import in.apcfss.struts.eCourt.apis.EHighCourtAPI;
import in.apcfss.struts.eCourt.apis.HASHHMACJava;
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
		String sql = null, roleId = null, deptCode = null, distCode="0";
		String userId = null, sqlCondition="";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distCode = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			
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
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}
			

			sql="select x.cino,x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases  from ("
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
					+ "from ecourts_case_data a "
					+ "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true "+sqlCondition;
				
			
			if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql+=" and (reporting_dept_code='"+session.getAttribute("dept_code")+"' or a.dept_code='"+session.getAttribute("dept_code")+"')";
			
			if (roleId.equals("2")) {
				sql += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId" , session.getAttribute("dist_id"));
			}
			
				sql+= " group by a.dept_code,d.dept_code ,reporting_dept_code,a.cino ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code) "
					+ " group by x.reporting_dept_code, d1.description,x.cino order by 1 limit 25 ";
			

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
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+session.getAttribute("dist_id")+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst order by 1",
						con));

			
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select case_short_name,case_full_name from case_type_master order by sno",
					con));
			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
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
			
			//request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
			
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
		String sql = null, roleId = null, deptCode = null, distCode="0";
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
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim() + "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}
			
			sql="select x.cino,x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod, sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases  from ("
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
					+ "from ecourts_case_data a "
					+ "inner join dept_new d on (a.dept_code=d.dept_code) "
					+ "where d.display = true "+sqlCondition;
				
			
			if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql+=" and (reporting_dept_code='"+session.getAttribute("dept_code")+"' or a.dept_code='"+session.getAttribute("dept_code")+"')";
			
			if (roleId.equals("2")) {
				sql += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId" , session.getAttribute("dist_id"));
			}
			
				sql+= "group by a.dept_code,d.dept_code ,reporting_dept_code,a.cino ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
					+ "group by x.reporting_dept_code, d1.description,x.cino order by 1 limit 25";
			

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
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst where district_id='"+session.getAttribute("dist_id")+"' order by district_name", con));
			else 
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(district_name) from district_mst order by 1",
						con));

			
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select case_short_name,case_full_name from case_type_master order by sno",
					con));
			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
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
			
			//request.setAttribute("SHOWFILTERS", "SHOWFILTERS");

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
		String sql = null, roleId = null, deptCode = null, distCode="0";
		int totalCount=0;
		String inputStr = "", targetURL = "";
		String authToken = "";
		String request_token = "", requeststring = "";
		
		try {
			System.out.println("update--------------"+cform.getDynaForm("cino") );
			con = DatabasePlugin.connect();
			String opVal = ECourtAPIs.getSelectParam(1);
			
			String cino=cform.getDynaForm("cino").toString();
			
			String cinoAll=cino.replaceAll("cino=", "");
			String cinoAll_new=cinoAll.replace("{", "");
			String cinoAll_new1=cinoAll_new.replace("}", "");
			String cinoAll_new2=cinoAll_new1.replace("[", "");
			String cinoAll_final=cinoAll_new2.replace("]", "");
			System.out.println("cinoAll---"+cinoAll);
			System.out.println("cinoAll_final---"+cinoAll_final);
			//while(rs.next()) {
			if(cino!=null) {
				totalCount++;
				inputStr = "cino="+cino;//ECourtAPIs.getInputStringValue(opVal);
				
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");
				
				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);
				
				System.out.println(totalCount+":opVal : "+opVal);
				
				System.out.println(totalCount+":URL : "+targetURL);
				System.out.println("Input String : "+inputStr);
				
				authToken = EHighCourtAPI.getAuthToken();
				System.out.println("authToken---"+authToken);
				String resp="";
				System.out.println("OPVAL:"+opVal);
				
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
				
				System.out.println("resp--"+resp);
				
				if (resp != null && !resp.equals("")) {
					try {
						boolean b = processCNRsearchResponse(resp, opVal, con, cino);
						
						if(b) {
							request.setAttribute("successMsg", "Successfully saved/ Updated data form ecourts.");
						}
						else {
							request.setAttribute("errorMsg", "Error-1 while Updating data form ecourts.");
						}
					}catch (Exception e) {
						request.setAttribute("errorMsg", "Error-2 while Updating data form ecourts.");
						e.printStackTrace();
					}
				}
			}
			System.out.println("FINAL END : Records fetched:"+totalCount);

		} catch (Exception e) {
			request.setAttribute("errorMsg", "Error-3 while Updating data form ecourts.");
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		 return mapping.findForward("success");
		//return showCaseWise(mapping, cform, request, response);
	}
	
	public static boolean processCNRsearchResponse(String resp, String fileName, Connection con, String cino)
		    throws Exception
		  {
		    String response_str = "";String response_token = "";String version = "";String decryptedRespStr = "";String sql = "";
		    resp = resp.trim();
		    System.out.println("processCNRsearchResponse:"+resp);
		    if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN"))&& (!resp.contains("Array(")))
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
		      
				sql = "update apolcms.ecourts_case_data set date_of_filing=to_date('" +

						jObjCaseData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'), " +

						" dt_regis= to_date('" + jObjCaseData.get("dt_regis").toString() + "', 'yyyy-mm-dd'), "
						+ "type_name_fil='" + checkStringJSONObj(jObjCaseData, "type_name_fil") + "' ,"
						+ "type_name_reg='" + checkStringJSONObj(jObjCaseData, "type_name_reg") + "' ,"
						+ "case_type_id='" + checkIntegerJSONObj(jObjCaseData, "case_type_id") + "' ," + "fil_no='"
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
						+ jObjCaseData.get("lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + "trial_lower_court_name='"
						+ checkStringJSONObj(jObjCaseData, "trial_lower_court_name") + "' ," + "trial_lower_court_caseno='" 
						+ checkStringJSONObj(jObjCaseData, "trial_lower_court_caseno") + "' ," + "trial_lower_court_dec_dt=to_date('"
						+ jObjCaseData.get("trial_lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + "date_last_list=to_date('" 
						+ jObjCaseData.get("date_last_list").toString() + "', 'yyyy-mm-dd'), " + "main_matter_cino='"
						+ checkStringJSONObj(jObjCaseData, "main_matter_cino") + "' ," + "date_filing_disp=to_date('"
						+ jObjCaseData.get("date_filing_disp").toString() + "', 'yyyy-mm-dd'), " + "reason_for_rej='"
						+ checkStringJSONObj(jObjCaseData, "reason_for_rej") + "' ," + "category='"
						+ checkStringJSONObj(jObjCaseData, "category") + "' ," + "sub_category='"
						+ checkStringJSONObj(jObjCaseData, "sub_category") + "' " 
						+ " where cino='" + cino + "'  ";
			
		      System.out.println("SQL:" + sql);
		      sqls.add(sql);
		      
		      System.out.println("acts:" + checkStringJSONObj(jObjCaseData, "acts"));
		      JSONObject jObjActsData = new JSONObject();
		      JSONObject jObjActsInnerData = new JSONObject();
		      if ((checkStringJSONObj(jObjCaseData, "acts") != null) && (checkStringJSONObj(jObjCaseData, "acts") != "null") && (!checkStringJSONObj(jObjCaseData, "acts").equals("")) && (!checkStringJSONObj(jObjCaseData, "acts").equals("[]")))
				{
					sql = " select count(*) from ecourts_case_acts where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_acts  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjActsData = new JSONObject(checkStringJSONObj(jObjCaseData, "acts"));
					for (int i = 1; i <= jObjActsData.length(); i++) {
						jObjActsInnerData = new JSONObject(jObjActsData.get("act" + i).toString());

						sql = "INSERT INTO apolcms.ecourts_case_acts(cino, act, actname, section) VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
								+ checkStringJSONObj(jObjActsInnerData, "actname") + "', '"
								+ checkStringJSONObj(jObjActsInnerData, "section") + "')";
						// System.out.println("ACTS SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("historyofcasehearing:" + checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
		      JSONObject jObjHistoryData = new JSONObject();
		      JSONObject jObjHistoryInnerData = new JSONObject();
		      if ((checkStringJSONObj(jObjCaseData, "historyofcasehearing") != null) && (checkStringJSONObj(jObjCaseData, "historyofcasehearing") != "null") && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("")) && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("[]")))
				{

					sql = " select count(*) from ecourts_historyofcasehearing where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_historyofcasehearing  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjHistoryData = new JSONObject(checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
					for (int i = 1; i <= jObjHistoryData.length(); i++) {
						jObjHistoryInnerData = new JSONObject(jObjHistoryData.get("sr_no" + i).toString());

						sql = "INSERT INTO apolcms.ecourts_historyofcasehearing(cino, sr_no, judge_name, business_date, hearing_date, purpose_of_listing, causelist_type)  VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
								+ checkStringJSONObj(jObjHistoryInnerData, "judge_name") + "', " + " to_date('"
								+ jObjHistoryInnerData.get("business_date").toString() + "', 'yyyy-mm-dd'), ";
						if ((jObjHistoryInnerData.get("hearing_date") != null) && (!jObjHistoryInnerData
								.get("hearing_date").toString().equals("Next Date Not Given"))) {
							sql = sql + " to_date('" + jObjHistoryInnerData.get("hearing_date").toString()
									+ "', 'yyyy-mm-dd'), ";
						} else {
							sql = sql + " to_date(null, 'yyyy-mm-dd'), ";
						}
						sql = sql + " '" + checkStringJSONObj(jObjHistoryInnerData, "purpose_of_listing") + "', '"
								+ checkStringJSONObj(jObjHistoryInnerData, "causelist_type") + "')";
						// System.out.println("historyofcasehearing SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("pet_extra_party:" + checkStringJSONObj(jObjCaseData, "pet_extra_party"));
		      JSONObject jObjMainData = new JSONObject();
		      JSONObject jObjInnerData = new JSONObject();
		      if ((checkStringJSONObj(jObjCaseData, "pet_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "pet_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("[]")))
				{
					sql = " select count(*) from ecourts_pet_extra_party where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_pet_extra_party  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "pet_extra_party"));
					for (int i = 1; i <= jObjMainData.length(); i++) {
						sql = "INSERT INTO apolcms.ecourts_pet_extra_party(cino, party_no, party_name) VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
								+ checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString())
								+ "')";
						// System.out.println("pet_extra_party SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("res_extra_party:" + checkStringJSONObj(jObjCaseData, "res_extra_party"));
		      if ((checkStringJSONObj(jObjCaseData, "res_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "res_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("[]")))
				{
					sql = " select count(*) from ecourts_res_extra_party where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_res_extra_party  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "res_extra_party"));
					for (int i = 1; i <= jObjMainData.length(); i++) {
						sql = "INSERT INTO apolcms.ecourts_res_extra_party(cino, party_no, party_name)  VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '"
								+ checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString())
								+ "')";
						// System.out.println("res_extra_party SQL:" + sql);
						sqls.add(sql);
					}
		      }
		      System.out.println("interimorder:" + checkStringJSONObj(jObjCaseData, "interimorder"));
		      if ((checkStringJSONObj(jObjCaseData, "interimorder") != null) && (checkStringJSONObj(jObjCaseData, "interimorder") != "null") && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("")) && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("[]")))
				{
					sql = " select count(*) from ecourts_case_interimorder where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_interimorder  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "interimorder"));
					for (int i = 1; i <= jObjMainData.length(); i++) {
						jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
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

						// System.out.println("interimorder SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
		      if ((checkStringJSONObj(jObjCaseData, "finalorder") != null) && (checkStringJSONObj(jObjCaseData, "finalorder") != "null") && (!checkStringJSONObj(jObjCaseData, "finalorder").equals("")) && 
		        (!checkStringJSONObj(jObjCaseData, "finalorder").equals("[]")))
				{
					sql = " select count(*) from ecourts_case_finalorder where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_finalorder  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "finalorder"));
					for (int i = 1; i <= jObjMainData.length(); i++) {
						jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
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
						// System.out.println("finalorder SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("iafiling:" + checkStringJSONObj(jObjCaseData, "iafiling"));
		      if ((checkStringJSONObj(jObjCaseData, "iafiling") != null) && (checkStringJSONObj(jObjCaseData, "iafiling") != "null") && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("")) && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("[]")))
				{

					sql = " select count(*) from ecourts_case_iafiling where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_iafiling  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

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
							sql = sql + " to_date('" + jObjInnerData.get("date_of_filing").toString()
									+ "', 'yyyy-mm-dd'))";
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

					sql = " select count(*) from ecourts_case_link_cases where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_link_cases  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "link_cases"));
					for (int i = 1; i <= jObjMainData.length(); i++) {
						jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
						sql = "INSERT INTO apolcms.ecourts_case_link_cases(cino, sr_no, filing_number, case_number) VALUES('"
								+ checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'"
								+ checkStringJSONObj(jObjInnerData, "filing_number") + "','"
								+ checkStringJSONObj(jObjInnerData, "case_number") + "')";
						// System.out.println("link_cases SQL:" + sql);
						sqls.add(sql);
					}
				}
		      System.out.println("objections:" + checkStringJSONObj(jObjCaseData, "objections"));
		      if ((checkStringJSONObj(jObjCaseData, "objections") != null) && (checkStringJSONObj(jObjCaseData, "objections") != "null") && (!checkStringJSONObj(jObjCaseData, "objections").equals("")) && (!checkStringJSONObj(jObjCaseData, "objections").equals("[]")))
				{

					sql = " select count(*) from ecourts_case_objections where cino='" + cino + "'";
					String count = DatabasePlugin.getSingleValue(con, sql);
					if (Integer.parseInt(count) > 0) {
						sql = "delete from ecourts_case_objections  where cino='" + cino + "'";
						DatabasePlugin.executeUpdate(sql, con);
					}

					jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "objections"));
					System.out.println("-" + jObjMainData.length());

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
						sql = sql + " to_date('" + jObjMainData.get("obj_reciept_date").toString()
								+ "', 'yyyy-mm-dd'))";
					} else {
						sql = sql + " to_date(null, 'yyyy-mm-dd'))";
					}
					// System.out.println("objections SQL:" + sql);
					sqls.add(sql);
				}
		      int executedSqls = 0;
		      if (sqls.size() > 0) {
		    	  sql="update ecourts_cinos_new set ecourts_response=null where cino='"+cino+"' and ecourts_response is not null"; 
		    	  sqls.add(sql);
		    	  executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);
		      }
		      System.out.println("Successfully saved...executedSqls:"+executedSqls);
		      System.out.println("END");
		      return true;
		    }
		    else {
		    	System.out.println("ERROR RESPONSE");
		    	return false;
		    }
		  }
		  
		  
		  public static int checkIntegerJSONObj(JSONObject jObj, String jObjKey)
		    throws NumberFormatException, JSONException
		  {
		    int a = 0;
		    if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals("")) && (jObj.get(jObjKey) != null) && (!jObj.get(jObjKey).toString().trim().equals(""))  && (!jObj.get(jObjKey).toString().trim().equals("null"))) {
		      a = Integer.parseInt(jObj.get(jObjKey).toString().trim());
		    }
		    return a;
		  }
		  
		  public static String checkStringJSONObj(JSONObject jObj, String jObjKey)
		    throws JSONException
		  {
		    String a = "";
		    if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals("")) && (jObj.get(jObjKey) != null) && (!jObjKey.equals("null"))) {
		      a = jObj.get(jObjKey).toString().trim().replace("'", "");
		    }
		    return a;
		  }
	
	
}
