package in.apcfss.struts.entryforms.filing;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;


public class AppealAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Connection con = null;
		String sql="";
		try {
			HttpSession session = request.getSession();
			AppealFormBean apform = (AppealFormBean) form;
			con = DatabasePlugin.connect();
			apform.reset(mapping, request);

			if(session.getAttribute("userId")!=null && session.getAttribute("userId").toString().equals("FIN01")){
				sql="SELECT SNO,((CASE WHEN COURT_TYPE=21 THEN 'Supreme Court' WHEN COURT_TYPE=2 THEN 'HIGH COURT' WHEN COURT_TYPE=14 THEN 'AP Administrative Tribunal' ELSE '' END)||'---'|| CASE_ID) AS CASE_ID " +
						"FROM OLCMS_CASE_DETAILS  " +
						"ORDER BY UPDATED_DATE DESC";
			}else
			{
				String sqlparam="";
				if(session.getAttribute("roleid")!=null && session.getAttribute("roleid").toString().equals("02"))
					sqlparam="  AND USER_NAME='"+session.getAttribute("userId")+"'";
				else
					sqlparam="  AND USER_NAME IN(SELECT USERID FROM APP_USER_MST_TEST WHERE SECTION_MULTI IN(SELECT SECTION_MULTI FROM APP_USER_MST_TEST WHERE USERID='"+session.getAttribute("userId")+"')) ";


				sql="SELECT SNO,((CASE WHEN COURT_TYPE=21 THEN 'Supreme Court' WHEN COURT_TYPE=2 THEN 'HIGH COURT' WHEN COURT_TYPE=14 THEN 'AP Administrative Tribunal' ELSE '' END)||'---'|| CASE_ID) AS CASE_ID " + 
						"FROM OLCMS_CASE_DETAILS WHERE 1=1 "+sqlparam+" " +
						"ORDER BY UPDATED_DATE DESC";
			}
			//System.out.println("sql="+sql);
			apform.setProperty("caseList",plugins.DatabasePlugin.getLabelValueBean(con, sql, true));

			apform.setProperty("stages", DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
			apform.setProperty("filedBys", DatabasePlugin.getLabelValueBean(con, "select SNO,TYPE from OLCMS_FILLEDBY_MST", true));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, null, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward getCaseDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String sql = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {

			HttpSession session = request.getSession();
			con = DatabasePlugin.connect();
			st = con.createStatement();
			AppealFormBean apform = (AppealFormBean) form;
			apform.reset(mapping, request);

			if(session.getAttribute("userId")!=null && session.getAttribute("userId").toString().equals("FIN01")){
				sql="SELECT SNO,((CASE WHEN COURT_TYPE=21 THEN 'Supreme Court' WHEN COURT_TYPE=2 THEN 'HIGH COURT' WHEN COURT_TYPE=14 THEN 'AP Administrative Tribunal' ELSE '' END)||'---'|| CASE_ID) AS CASE_ID " +
						"FROM OLCMS_CASE_DETAILS  " +
						"ORDER BY UPDATED_DATE DESC";
			}else
			{
				String sqlparam="";
				if(session.getAttribute("roleid")!=null && session.getAttribute("roleid").toString().equals("02"))
					sqlparam="  AND USER_NAME='"+session.getAttribute("userId")+"'";
				else
					sqlparam="  AND USER_NAME IN(SELECT USERID FROM APP_USER_MST_TEST WHERE SECTION_MULTI IN(SELECT SECTION_MULTI FROM APP_USER_MST_TEST WHERE USERID='"+session.getAttribute("userId")+"')) ";


				sql="SELECT SNO,((CASE WHEN COURT_TYPE=21 THEN 'Supreme Court' WHEN COURT_TYPE=2 THEN 'HIGH COURT' WHEN COURT_TYPE=14 THEN 'AP Administrative Tribunal' ELSE '' END)||'---'|| CASE_ID) AS CASE_ID " + 
						"FROM OLCMS_CASE_DETAILS WHERE 1=1 "+sqlparam+" " +
						"ORDER BY UPDATED_DATE DESC";
			}
			System.out.println("sql="+sql);
			apform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));

			sql = "SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR FROM OLCMS_CASE_DETAILS CD INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO) INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  " +
					"WHERE CD.SNO='"+ apform.getProperty("caseId") + "'";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			if (rs != null && rs.next()) {
				apform.setProperty("courtName", rs.getString("COURT_NAME"));
				apform.setProperty("caseType", rs.getString("CASE_SHORT_NAME"));
				apform.setProperty("caseNo", rs.getString("CASE_NO"));
				apform.setProperty("caseYear", rs.getString("CASE_YEAR"));
				request.setAttribute("caseIdDetails", "caseIdDetails");

				//SET APPEAL PREV DETAILS
				sql="SELECT SNO, CASE_ID, TO_CHAR(DATE_OF_FILING,'DD/MM/YYYY') AS DATE_OF_FILING, TO_CHAR(DATE_OF_LISTING,'DD/MM/YYYY') AS DATE_OF_LISTING, APPEAL_DESC, FILLED_BY, STAGE, TO_CHAR(DATE_OF_MATERIAL_CALLED,'DD/MM/YYYY') AS DATE_OF_MATERIAL_CALLED, TO_CHAR(DATE_OF_MATERIAL_SENT,'DD/MM/YYYY') AS DATE_OF_MATERIAL_SENT, UPDATE_TIME, USER_NAME, IP_ADDRESS     FROM TTDOLCMS.OLCMS_APPEAL_INFORMATION WHERE  CASE_ID='"
						+ apform.getProperty("caseId") + "'";

				List appealData = DatabasePlugin.executeQuery(sql, con);

				if(appealData  != null){
					System.out.println("appealData:"+appealData);
					Map appealCaseData = (Map)appealData.get(0);

					apform.setProperty("filingdate", appealCaseData.get("date_of_filing"));
					apform.setProperty("listingdate", appealCaseData.get("date_of_listing"));
					apform.setProperty("appealDescription", appealCaseData.get("appeal_desc"));
					// apform.setProperty("atrDescription", appealCaseData.get("ATR_DESC"));
					apform.setProperty("materialCalled", appealCaseData.get("date_of_material_called"));
					apform.setProperty("materialSent", appealCaseData.get("date_of_material_sent"));
					apform.setProperty("filedBy", appealCaseData.get("filled_by"));
					apform.setProperty("stage", appealCaseData.get("stage"));
				}

			} else
				request.setAttribute("caseIdDetailsNotFound", "Case Details Not found");

			apform.setProperty("stages", DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
			apform.setProperty("filedBys", DatabasePlugin.getLabelValueBean(con, "select SNO,TYPE from OLCMS_FILLEDBY_MST", true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, st, rs);
		}
		return unspecified(mapping, form, request, response);
	}



	public ActionForward insertDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		System.out.println("insert");
		AppealFormBean apform = (AppealFormBean) form;
		String sql=null;
		Connection con=null;
		try{
			con=DatabasePlugin.connect();
			con.setAutoCommit(false);

			ArrayList query_list=new ArrayList();


			if (Integer.parseInt(DatabasePlugin.getStringfromQuery("SELECT count(*) from OLCMS_APPEAL_INFORMATION WHERE CASE_ID='" + apform.getProperty("caseId") + "' ", con)) > 0) {

				sql="UPDATE OLCMS_APPEAL_INFORMATION set DATE_OF_FILING=to_date('"+apform.getProperty("filingdate")+"','dd/mm/yyyy'), DATE_OF_LISTING=to_date('"+apform.getProperty("listingdate")+"','dd/mm/yyyy'),  APPEAL_DESC='"+apform.getProperty("appealDescription")+"', FILLED_BY="+apform.getProperty("filedBy")+", STAGE='"+apform.getProperty("stage")+"', DATE_OF_MATERIAL_CALLED=to_date('"+apform.getProperty("materialCalled")+"','dd/mm/yyyy'), DATE_OF_MATERIAL_SENT=to_date('"+apform.getProperty("materialSent")+"','dd/mm/yyyy'), UPDATE_TIME=NOW() where CASE_ID='"+apform.getProperty("caseId")+"'";

			}
			else{

				sql="INSERT INTO TTDOLCMS.OLCMS_APPEAL_INFORMATION( CASE_ID, DATE_OF_FILING, DATE_OF_LISTING, APPEAL_DESC, FILLED_BY, STAGE, DATE_OF_MATERIAL_CALLED, DATE_OF_MATERIAL_SENT, UPDATE_TIME, USER_NAME, IP_ADDRESS) " +
						" VALUES( '"+apform.getProperty("caseId")+"', to_date('"+apform.getProperty("filingdate")+"','dd/mm/yyyy'), to_date('"+apform.getProperty("listingdate")+"','dd/mm/yyyy'), '"+apform.getProperty("appealDescription")+"', "+apform.getProperty("filedBy")+", '"+apform.getProperty("stage")+"', to_date('"+apform.getProperty("materialCalled")+"','dd/mm/yyyy'), to_date('"+apform.getProperty("materialSent")+"','dd/mm/yyyy'), NOW(), '"+request.getSession().getAttribute("userId")+"',  '"+request.getRemoteAddr()+"')";
			}
			query_list.add(sql);

			System.out.println("QUERY LIST SIZE"+query_list.size());
			int exe = DatabasePlugin.executeBatchSQLs(query_list, con);
			if (exe == query_list.size()) {
				con.commit();
				request.setAttribute("msg", " Appeal Details saved successfully for Case Id : "+apform.getProperty("caseId"));
			}else{
				con.rollback();
				request.setAttribute("msg1", " Error in submission ");
			}

			apform.setProperty("caseId", apform.getProperty("caseId"));
			apform.setProperty("stages", DatabasePlugin.getLabelValueBean(con, "select SLNO,STAGE_SH_NAME from STAGES_MST", true));
			apform.setProperty("filedBys", DatabasePlugin.getLabelValueBean(con, "select SNO,TYPE from OLCMS_FILLEDBY_MST", true));

		}catch (Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		finally{
			DatabasePlugin.close(con, null, null);
		}
		return getCaseDetails(mapping, apform, request, response);
	}


	public ActionForward insertUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		HttpSession session = request.getSession();
		WPAffidavitFormBean wpform = (WPAffidavitFormBean) form;
		String target = new String("success");
		String action = null;
		boolean insertUpdateOp = false;
		Connection con=null;
		String interimDisplay = null;
		String caseIdDisplay = null;
		try{


			//wpform.reset(mapping, request);

			wpform.getDeptGPsList();
			wpform.getDeptHodList();
			wpform.getDistrictsList();
			wpform.getOfficerList();

			String isCaseExist = request.getParameter("isCaseExist");
			System.out.println("isCaseExist::" + isCaseExist);

			con=DatabasePlugin.connect();
			insertUpdateOp = insertUpdateData(con, wpform, action,session.getAttribute("userId").toString());
			System.out.println("insertUpdateOpinsertUpdateOp"+insertUpdateOp);
			if (insertUpdateOp) {

				target = "success";
				System.out.println("wpform.getInterimOrder() :: "+ wpform.getInterimOrder());

				if (wpform.getInterimOrder() != null) {

					if (wpform.getInterimOrder().equals("Yes")) {
						insertUpdateOp = insertUpdateDataInterim(con,wpform, action,session.getAttribute("userId").toString());
						if (insertUpdateOp) {
							request.setAttribute(
									"msgInterim",
									action.equals("insert") ? "Interim Inserted Successfully with Interim CaseId :"
											+ interimDisplay + " "
											: "Interim Updated Successfully ");
						} else {
							request.setAttribute(
									"msgInterim",
									action.equals("insert") ? "Interim Inserted Failed"
											: "Interim Updation Failed");
						}
					}
				}

				if (action.equals("insert")) {
					request.setAttribute("msg",
							"Inserted Successfully With the Serial No  and  CaseId : "
									+ caseIdDisplay);
				} else if (action.equals("update")) {
					request.setAttribute("msg", "Updated Successfully ");
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DatabasePlugin.close(con, null, null);
		}
		return mapping.findForward(target);
	}

	boolean insertUpdateData(Connection conn, WPAffidavitFormBean wpform,
			String action,String userId) throws Exception{
		int count=0;
		try{
			ArrayList<String> sqlsNew = new ArrayList<String>();
			String caseid = "";
			int seq=0;
			if ((wpform.getCaseNo() != null) && (!wpform.getCaseNo().equals(""))) {
				caseid = wpform.getCaseType() + wpform.getCaseNo() + "/"
						+ wpform.getCaseYear();
			} else {
				caseid = "";
			}

			if ((wpform.getStatus() == null) || (wpform.getStatus().equals(""))) {
				if ((wpform.getStage().equals("PostedAdmission"))
						|| (wpform.getStage().equals("NoticeAdmission"))) {
					wpform.setStatus("Filing");
				} else {
					wpform.setStatus("Admitted");
				}
			} else if (wpform.getStatus().equals("Disposed")) {
				wpform.setStage("Judgement Pronounced");
			} else if (wpform.getStatus().equals("Pending")) {
				wpform.setStage("PostedHearing");
			}

			//String[] department = wpform.getDeptHod();
			String[] department = {"40"};
			String[] resps = wpform.getRespNames();
			String[] pets = wpform.getPetNames();
			String[] respAdvocates = wpform.getRespAdvocates();
			String[] petAdvocates = wpform.getPetAdvocates();
			String[] districts = wpform.getStDistrict();
			String subject = wpform.getSubCategory();
			subject = (subject != null) && ("Others".equals(subject)) && (wpform.getOtherSubject() != null) && (!"".equals(wpform.getOtherSubject())) ? wpform.getOtherSubject() : subject;
			String petAdv = "";
			String resAdv = "";
			String resAdvCode = "";
			String petName = "";
			String resName = "";
			int temp = 0;
			int temp1 = 0;

			if ((wpform.getIsPetRes() != null)
					&& (wpform.getIsPetRes().equals("R"))) 
			{
				petAdv = wpform.getPetAdvocate();
				resAdv = wpform.getRespAdvocates()[0];
				temp = wpform.getRespAdvocates().length;
			}
			if ((wpform.getIsPetRes() != null)
					&& (wpform.getIsPetRes().equals("P"))) 
			{

				resAdv = wpform.getRespAdvocate();
				resAdvCode = wpform.getRespAdvocateCode();
				if(resAdvCode==null || resAdvCode.equals(""))
					resAdvCode = "-";
			}

			if ((wpform.getIsPetRes() != null)
					&& (wpform.getIsPetRes().equals("R"))) {
				petName = wpform.getPetName();
				resName = wpform.getRespNames()[0];
				temp1 = wpform.getRespNames().length;
			}
			if ((wpform.getIsPetRes() != null)
					&& (wpform.getIsPetRes().equals("P"))) {
				petName = wpform.getPetNames()[0];
				resName = wpform.getRespName();
				temp1 = wpform.getPetNames().length;
			}

			String pwcString = "";
			String[] pwcAuth = wpform.getAuthpwca();
			if ((pwcAuth != null) && (pwcAuth.length != 0)) {
				int l = pwcAuth.length;
				for (int i = 0; i < l; i++) {
					if (i + 1 == l) {
						pwcString = pwcString + pwcAuth[i];
					} else {
						pwcString = pwcString + pwcAuth[i] + ",";
					}
				}
			}

			//caseIdDisplay = caseid;
			String[] sqls = new String[10 + department.length + temp+ districts.length + temp1];
			if (action.equals("insert")) {

				seq = Integer.parseInt(DatabasePlugin.getStringfromQuery("select nextval('lcms_seq')", conn));
				//sqls[0] =

				sqlsNew.add("INSERT INTO LCMS_MASTER ( case_id, name_of_petitioner , name_of_respondent , name_of_petetioner_advocate , name_of_respondent_advocate , advocate_code, department , basecase_no , parentcase_no , status , type_of_case ," +
						"date_of_entry , brief_description ,  date_of_appearance , date_of_admittance , district , serial_no , type , year , " +
						//"gp_office_file_no , " +
						"secretariat_file_no , stage , IS_GOV_PET_RES,CASE_ENTERED_BY,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO,REMARKS)  " +
						"VALUES (upper('"+ caseid+ "'),'"+ petName+ "','"+ resName+ "','"+ petAdv+ "','"+ resAdv+ "','"+ resAdvCode+ "','"+ department[0]+ "','BASECASE','BASECASE','"+ wpform.getStatus()+ "','CASE'," +
						"NOW(),'"+ wpform.getBrfDiscription()+ "',to_date('"+ wpform.getDtListing()+ "','dd/mm/yyyy'),to_date('"+ wpform.getDtFiling()+ "','dd/mm/yyyy'),'"+ wpform.getStDistrict()[0]+ "',"+ seq+ ",'"+ wpform.getCaseType()+ "'," +
						"'"+ wpform.getCaseYear()+ "','"+ wpform.getSecyFileNo()+ "','"+ wpform.getStage()+ "','"+ wpform.getIsPetRes()+ "','"+ userId+ "',"+ wpform.getCourtType()+ ",'"+ pwcString+ "','"+ wpform.getNatureofcase()+ "',"+ wpform.getSection() + ",'" + wpform.getRemarks() + "')");

				//System.out.println("sqlll--18--->"+sqls[0]);

				//sqls[1] =

				sqlsNew.add("insert into case_master(case_id, filedby, CATEGORY, priority, DATE_OF_AFDVTRECEIVE_FROMGP, suit_value, serial_no) VALUES(upper('"
						+ caseid
						+ "'),'"
						+ petName
						+ "','"
						+ subject
						+ "','"
						+ wpform.getPriority()
						+ "',"
						+ "to_date('"
						+ wpform.getDtPetRecvByDept()
						+ "','dd/mm/yyyy'),'"
						+ wpform.getSuit() + "'," + seq + ")");

				//System.out.println("sql1--17--->"+sqls[1]);

				for (int i = 0; i < department.length; i++) {
					//sqls[(i + 2)] = 
					sqlsNew.add("insert into CASE_HOD_DEPTS ( SNO, CASE_ID_REF, HOD_DEPT, CASE_SERIAL_NO ) VALUES ((select coalesce(max(SNO),0)+1 from CASE_HOD_DEPTS),upper('"
							+ caseid + "')," + department[i] + "," + seq + ") ");

					//System.out.println("sqls[(i + 2)]--16--->"+sqls[(i + 2)]);
				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) {
					for (int i = 0; i < respAdvocates.length; i++) {
						//sqls[(i + 3 + department.length)] = 
						sqlsNew.add("insert into CASE_GPS ( CASE_ID, GP_IDS, SERIAL_NO_REF, SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + respAdvocates[i] + "','" + seq + "',(select coalesce(max(SR_NO),0)+1 from CASE_GPS)) ");

						//System.out.println("sqls[(i + 3 + department.length)]-15---->"+sqls[(i + 3 + department.length)]);
					}
				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("P"))) {
					if (petAdvocates != null) {
						for (int i = 0; i < petAdvocates.length; i++)
						{
							//sqls[(i + 3 + department.length)] = 
							sqlsNew.add("insert into CASE_GPS ( CASE_ID, GP_IDS, SERIAL_NO_REF, SR_NO ) VALUES (upper('"
									+ caseid
									+ "'),'"
									+ petAdvocates[i]
											+ "','"
											+ seq + "',(select coalesce(max(SR_NO),0)+1 from CASE_GPS)) ");

							//System.out.println("sqls[(i + 3 + department.length)] -14---->"+sqls[(i + 3 + department.length)] );
						}

						//System.out.println("sqls[(i + 3 + department.length)]----->"+sqls[(i + 3 + department.length)]);
					}
				}
				for (int i = 0; i < districts.length; i++) {
					//sqls[(i + 4 + department.length + temp)] = 
					sqlsNew.add("insert into CASE_DISTRICTS ( SNO, CASE_ID, DISTRICT, CASE_SERIAL_NO ) VALUES ((select coalesce(max(SNO),0)+1 from CASE_DISTRICTS),upper('"
							+ caseid + "'),'" + districts[i] + "'," + seq + ") ");

					//System.out.println("sqls[(i + 4 + department.length + temp)] -13---->"+sqls[(i + 4 + department.length + temp)] );

				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) 
				{
					for (int i = 0; i < resps.length; i++) 
					{
						//sqls[(i + 5 + department.length + temp + districts.length)] = 
						sqlsNew.add("insert into CASES_PET_RESP ( CASE_ID, PET_RESP_ID, IS_PET_RESP,  CASE_SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + resps[i] + "','R'," + seq + ") ");

						//System.out.println("sqls[(i + 5 + department.length + temp + districts.length)]-12---->"+sqls[(i + 5 + department.length + temp + districts.length)]);
					}
				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("P"))) {
					for (int i = 0; i < pets.length; i++) {
						//sqls[(i + 5 + department.length + temp + districts.length)] = 
						sqlsNew.add("insert into CASES_PET_RESP ( CASE_ID, PET_RESP_ID, IS_PET_RESP,  CASE_SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + pets[i] + "','P'," + seq + ") ");

						//System.out.println("sqls[(i + 5 + department.length + temp + districts.length)]-11---->"+sqls[(i + 5 + department.length + temp + districts.length)]);
					}

				}
			} else if (action.equals("update")) {
				seq = Integer.parseInt(wpform.getSerialNo());
				//sqls[0] =

				sqlsNew.add("UPDATE LCMS_MASTER set PWC_AUTH='" + pwcString
						+ "',name_of_petitioner = '" + petName + "', "
						+ "name_of_respondent = '" + resName
						+ "', name_of_petetioner_advocate = '" + petAdv + "', "
						+ "name_of_respondent_advocate = '" + resAdv
						+ ", advocate_code = '" + resAdvCode
						+ "', department ='" + department[0] + "', "
						+ "brief_description='" + wpform.getBrfDiscription()
						+ "' , " + "date_of_appearance=to_date('"
						+ wpform.getDtListing() + "','dd/mm/yyyy') , "
						+ "date_of_admittance=to_date('" + wpform.getDtFiling()
						+ "','dd/mm/yyyy') , " + "district='"
						+ wpform.getStDistrict()[0] + "' , "
						//+ "gp_office_file_no ='" + wpform.getGpFileNo() + "', "
						+ "secretariat_file_no ='" + wpform.getSecyFileNo() + "', "
						+ "status = '" + wpform.getStatus() + "' ," + "SECTION_NO="
						+ wpform.getSection() + ", " + "stage='"
						+ wpform.getStage() + "', " + "IS_GOV_PET_RES='"
						+ wpform.getIsPetRes() + "',NATURE_OF_CASE='"
						+ wpform.getNatureofcase() + "'  " + " ,REMARKS = '"
						+ wpform.getRemarks() + "' " + "where case_id=upper('"
						+ caseid + "') and TYPE_OF_CASE='CASE' ");
				//System.out.println("update SQL[0] ::=--10 " + sqls[0]);
				//sqls[1] = 
				sqlsNew.add("update case_master set filedby = '" + petName
						+ "' , CATEGORY='" + subject + "', priority='"
						+ wpform.getPriority()
						+ "', DATE_OF_AFDVTRECEIVE_FROMGP=to_date('"
						+ wpform.getDtPetRecvByDept()
						+ "','dd/mm/yyyy'), suit_value='" + wpform.getSuit()
						+ "' where case_id='" + caseid + "'");

				//System.out.println("sqls[1]-9---->"+sqls[1]);

				//sqls[2] = 
				sqlsNew.add("delete from CASE_HOD_DEPTS where CASE_SERIAL_NO=" + seq);

				//System.out.println("sqls[2]-8---->"+sqls[2]);


				for (int i = 0; i < department.length; i++) {
					//sqls[(i + 3)] =

					sqlsNew.add("insert into CASE_HOD_DEPTS ( SNO, CASE_ID_REF, HOD_DEPT, CASE_SERIAL_NO )  VALUES ((select coalesce(max(SNO),0)+1 from CASE_HOD_DEPTS),upper('"
							+ caseid
							+ "'),"
							+ department[i]
									+ ","
									+ " (select SERIAL_NO from LCMS_MASTER where CASE_ID=upper('"
									+ caseid + "') and TYPE_OF_CASE='CASE')) ");

					//System.out.println("sqls[(i + 3)]-7---->"+sqls[(i + 3)]);

				}
				//sqls[(3 + department.length)] = 
				sqlsNew.add("delete from CASE_GPS  where SERIAL_NO_REF=" + seq);

				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) {
					for (int i = 0; i < respAdvocates.length; i++) {
						//sqls[(i + 4 + department.length)] = 
						sqlsNew.add("insert into CASE_GPS ( CASE_ID, GP_IDS, SERIAL_NO_REF, SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + respAdvocates[i] + "','" + seq + "',(select coalesce(max(SR_NO),0)+1 from CASE_GPS)) ");

						//System.out.println("sqls[(i + 4 + department.length)]-6---->"+sqls[(i + 4 + department.length)]);

					}
				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("P"))) {
					if (petAdvocates != null) {
						for (int i = 0; i < petAdvocates.length; i++)
						{
							//sqls[(i + 4 + department.length)] = 
							sqlsNew.add("insert into CASE_GPS ( CASE_ID, GP_IDS, SERIAL_NO_REF, SR_NO ) VALUES (upper('"
									+ caseid
									+ "'),'"
									+ petAdvocates[i]
											+ "','"
											+ seq + "',(select coalesce(max(SR_NO),0)+1 from CASE_GPS)) ");

							//System.out.println("sqls[(i + 4 + department.length)]-5---->"+sqls[(i + 4 + department.length)]);
						}
					}
				}
				//sqls[(4 + department.length + temp)] =
				sqlsNew.add("delete from CASE_DISTRICTS  where CASE_SERIAL_NO=" + seq);
				for (int i = 0; i < districts.length; i++) {
					//sqls[(i + 5 + department.length + temp)] = 
					sqlsNew.add("insert into CASE_DISTRICTS ( SNO, CASE_ID, DISTRICT, CASE_SERIAL_NO ) VALUES ((select coalesce(max(SNO),0)+1 from CASE_DISTRICTS),upper('"
							+ caseid + "'),'" + districts[i] + "'," + seq + ") ");


					///System.out.println("sqls[(i + 5 + department.length + temp)]-4---->"+sqls[(i + 5 + department.length + temp)]);
				}
				sqls[(5 + department.length + temp + districts.length)] = ("delete from CASES_PET_RESP where CASE_SR_NO=" + seq);
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) {
					for (int i = 0; i < resps.length; i++) {
						//sqls[(i + 6 + department.length + temp + districts.length)] = 
						sqlsNew.add("insert into CASES_PET_RESP ( CASE_ID, PET_RESP_ID, IS_PET_RESP,  CASE_SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + resps[i] + "','R'," + seq + ") ");

						//System.out.println("sqls[(i + 6 + department.length + temp + districts.length)]-3---->"+sqls[(i + 6 + department.length + temp + districts.length)]);
					}
				}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("P"))) {
					for (int i = 0; i < pets.length; i++) {
						//sqls[(i + 6 + department.length + temp + districts.length)] = 
						sqlsNew.add("insert into CASES_PET_RESP ( CASE_ID, PET_RESP_ID, IS_PET_RESP,  CASE_SR_NO ) VALUES (upper('"
								+ caseid + "'),'" + pets[i] + "','P'," + seq + ") ");

						//System.out.println("sqls[(i + 6 + department.length + temp + districts.length)]-2---->"+sqls[(i + 6 + department.length + temp + districts.length)]);
					}
				}
			}



			//sqls[(sqls.length - 2)] =

			sqlsNew.add("INSERT INTO TB_ORIGINAL_CASES_FROM (SERIAL_NO,  CASE_ID, USER_ID,  DATE_OF_ENTRY,  NAME_OF_PETITIONER,   NAME_OF_RESPONDENT,   NAME_OF_PETETIONER_ADVOCATE, NAME_OF_RESPONDENT_ADVOCATE,   advocate_code, DEPARTMENT,   BRIEF_DESCRIPTION ,  DATE_OF_APPEARANCE,  DATE_OF_ADMITTANCE ,  DISTRICT ,  " +
					//"GP_OFFICE_FILE_NO , " +
					"SECRETARIAT_FILE_NO , STAGE , CATEGORY , PRIORITY , DATE_OF_AFDVTRECEIVE_FROMGP, SUIT_VALUE  ) values ( "+ seq+ ", '"
					+ caseid
					+ "', '"
					+ userId
					+ "', NOW(),'"
					+ petName
					+ "', '"
					+ resName
					+ "',"
					+ " '"
					+ petAdv
					+ "','"
					+ resAdv
					+ "','"
					+ resAdvCode
					+ "','"
					+ department[0]
							+ "',"
							+ "'"
							+ wpform.getBrfDiscription()
							+ "',to_date('"
							+ wpform.getDtListing()
							+ "','dd/mm/yyyy'),to_date('"
							+ wpform.getDtFiling()
							+ "',"
							+ "'dd/mm/yyyy'),'"
							+ wpform.getStDistrict()[0]
									+ "','"
									//+ wpform.getGpFileNo()
									//+ "','"
									+ wpform.getSecyFileNo()
									+ "',"
									+ "'"
									+ wpform.getStage()
									+ "','"
									+ subject
									+ "','"
									+ wpform.getPriority()
									+ "',"
									+ "to_date('"
									+ wpform.getDtPetRecvByDept()
									+ "','dd/mm/yyyy'), '"
									+ wpform.getSuit() + "')");

			//System.out.println("sqls[(sqls.length - 2)]-1---->"+sqls[(sqls.length - 2)]);

			if ((subject != null) && ("Others".equals(subject))
					&& (wpform.getOtherSubject() != null)
					&& (!"".equals(wpform.getOtherSubject()))) {
				//sqls[(sqls.length - 1)] = 
				sqlsNew.add("insert into SUBJECTS_MST (SUBJECT_ID, SUBJECT_NAME, DESCRIPTION, PARENT_SUBJECT, SUB_SR_NO)  values ('"
						+ wpform.getOtherSubject()
						+ "','"
						+ wpform.getOtherSubject()
						+ "','"
						+ wpform.getOtherSubject() + "','0',(select coalesce(max(SUB_SR_NO),0)+1 from SUBJECTS_MST))");

				//System.out.println("sqls[(sqls.length - 1)]-22---->"+sqls[(sqls.length - 1)]);
			}
			//System.out.println("sqls:"+sqls);
			System.out.println("sqls:"+sqlsNew);

			count=0;
			if(sqlsNew.size() > 0)
			{						
				//count = DatabasePlugin.executeBatch(sqlsNew);		 			
			}
			count=1;
			//System.out.println("dbop:"+dbop);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  count>0?true:false;
	}

	int checkForCaseExistance(Connection conn, WPAffidavitFormBean wpform,
			String caseid, HttpServletRequest request) {
		Statement st = null;
		ResultSet rs = null;
		int i = 0;
		HttpSession session = request.getSession();
		String user[]=null;
		String sql = "";
		user = GeneralOperations.getSessionUser(request);
		System.out.println(user[2]);

		if ((user[2].equals("01")) || (user[2].equals("02"))) {

			/*sql =

			"SELECT T1.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE, NAME_OF_RESPONDENT_ADVOCATE,  DEPARTMENT, BASECASE_NO, PARENTCASE_NO, STATUS, TYPE_OF_CASE, TO_CHAR(DATE_OF_ENTRY, 'dd/mm/yyyy') DATE_OF_ENTRY,  BRIEF_DESCRIPTION, TO_CHAR(T1.DATE_OF_APPEARANCE, 'dd/mm/yyyy') DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'dd/mm/yyyy') DATE_OF_ADMITTANCE, DISTRICT, SERIAL_NO, GP_OFFICE_FILE_NO,  SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY,  TO_CHAR(DATE_OF_AFDVTRECEIVE_FROMGP, 'dd/mm/yyyy')  DATE_OF_AFDVTRECEIVE_FROMGP, SUIT_VALUE,PRAYER, SECY_ID, T3.CASE_ID CHILD_CASE, T3.TYPE_OF_CASE CHILD_CASE_TYPE,  STATUS CHILD_CASE_STATUS, BASECASE_NO CHILD_CASE_BASECASE_NO,  TO_CHAR(DATE_OF_APPEARANCE, 'Month DD, YYYY')  CHILD_CASE_DATE_OF_APPEARANCE, TO_CHAR(DATE_OF_ADMITTANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_ADMITTANCE,  TO_CHAR(DATE_OF_PR_SENTTOGP, 'Month DD, YYYY') CHILD_CASE_DATE_OF_PR_SENTTOGP,   TO_CHAR(DATE_OF_CA_FILEDBYGPINHC, 'Month DD, YYYY') CHILD_CASE_DATE_CA_FILEDINHC,IS_GOV_PET_RES,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO   FROM ( SELECT A.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE, NAME_OF_RESPONDENT_ADVOCATE, DEPARTMENT, BASECASE_NO,  PARENTCASE_NO, STATUS, TYPE_OF_CASE, DATE_OF_ENTRY,  BRIEF_DESCRIPTION, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DISTRICT, A.SERIAL_NO, YEAR,  GP_OFFICE_FILE_NO, SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY, DATE_OF_AFDVTRECEIVE_FROMGP,   APPEARANCE, DATE_OF_AFDVTRECEIVE_FROMHC,  SUIT_VALUE,PRAYER,IS_GOV_PET_RES,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO FROM  LCMS_MASTER A,  CASE_MASTER B WHERE A.SERIAL_NO = B.SERIAL_NO AND  A.CASE_ID=upper('"
					+ caseid
					+ "') and TYPE_OF_CASE='CASE'  ) T1 left join HOD_MST C "
					+ "ON (T1.DEPARTMENT = TO_CHAR(C.HOD_NO)) left join HOD_DEPTLINK D ON (C.HOD_NO = D.HOD_NO) left join DEPARTMENT_MST E ON (D.DEPT_NO = E.DEPT_NO) left join  (SELECT T2.CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DATE_OF_PR_SENTTOGP, DATE_OF_CA_FILEDBYGPINHC FROM (SELECT CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE,DATE_OF_ADMITTANCE FROM LCMS_MASTER WHERE BASECASE_NO=upper('"
					+ caseid
					+ "')\t) T2 left join PARAWISEREMARKS A ON (T2.CASE_ID = A.CASE_ID) left join COUNTERAFFIDAVIT B ON (T2.CASE_ID = B.CASE_ID)) T3 ON (T1.CASE_ID = T3.BASECASE_NO) ORDER BY T3.TYPE_OF_CASE";*/

			sql="SELECT  T1.CASE_ID,  T1.NAME_OF_PETITIONER, T1.NAME_OF_RESPONDENT, T1.NAME_OF_PETETIONER_ADVOCATE, T1.NAME_OF_RESPONDENT_ADVOCATE, T1.advocate_code, T1.DEPARTMENT, T1.BASECASE_NO, T1.PARENTCASE_NO, T1.STATUS, T1.TYPE_OF_CASE," +
					"TO_CHAR(T1.DATE_OF_ENTRY, 'dd/mm/yyyy') DATE_OF_ENTRY, T1.BRIEF_DESCRIPTION, TO_CHAR(T1.DATE_OF_APPEARANCE, 'dd/mm/yyyy') DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'dd/mm/yyyy') DATE_OF_ADMITTANCE, " +
					"T1.DISTRICT, T1.SERIAL_NO, T1.GP_OFFICE_FILE_NO, T1.SECRETARIAT_FILE_NO, T1.STAGE, T1.CATEGORY, T1.PRIORITY, TO_CHAR(T1.DATE_OF_AFDVTRECEIVE_FROMGP, 'dd/mm/yyyy')  DATE_OF_AFDVTRECEIVE_FROMGP, " +
					"T1.SUIT_VALUE,T1.PRAYER, E.SECY_ID, T3.CASE_ID CHILD_CASE, T3.TYPE_OF_CASE CHILD_CASE_TYPE,  T1.STATUS CHILD_CASE_STATUS, T1.BASECASE_NO CHILD_CASE_BASECASE_NO, " +
					"TO_CHAR(T1.DATE_OF_APPEARANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_ADMITTANCE, " +
					"TO_CHAR(T3.DATE_OF_PR_SENTTOGP, 'Month DD, YYYY') CHILD_CASE_DATE_OF_PR_SENTTOGP,   TO_CHAR(T3.DATE_OF_CA_FILEDBYGPINHC, 'Month DD, YYYY') CHILD_CASE_DATE_CA_FILEDINHC ," +
					"T1.IS_GOV_PET_RES,T1.COURT_ID,T1.PWC_AUTH,T1.NATURE_OF_CASE,T1.SECTION_NO   " +
					"FROM ( SELECT A.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE, NAME_OF_RESPONDENT_ADVOCATE, advocate_code, DEPARTMENT, BASECASE_NO,  " +
					"PARENTCASE_NO, STATUS, TYPE_OF_CASE, DATE_OF_ENTRY,  BRIEF_DESCRIPTION, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DISTRICT, A.SERIAL_NO, YEAR,  " +
					"GP_OFFICE_FILE_NO, SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY, DATE_OF_AFDVTRECEIVE_FROMGP,   APPEARANCE, DATE_OF_AFDVTRECEIVE_FROMHC, " +
					"SUIT_VALUE,PRAYER,IS_GOV_PET_RES,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO FROM  LCMS_MASTER A,  CASE_MASTER B WHERE A.SERIAL_NO = B.SERIAL_NO AND " +
					"A.CASE_ID=upper('"+caseid+"') and TYPE_OF_CASE='CASE'  ) T1 left join HOD_MST C " +
					"ON (T1.DEPARTMENT = TO_CHAR(C.HOD_NO)) left join HOD_DEPTLINK D ON (C.HOD_NO = D.HOD_NO) left join DEPARTMENT_MST E ON (D.DEPT_NO = E.DEPT_NO) left join  " +
					"(SELECT T2.CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DATE_OF_PR_SENTTOGP," +
					"DATE_OF_CA_FILEDBYGPINHC FROM (SELECT CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE,DATE_OF_ADMITTANCE FROM LCMS_MASTER WHERE " +
					"BASECASE_NO=upper('"+caseid+"')) T2 left join PARAWISEREMARKS A ON (T2.CASE_ID = A.CASE_ID) left join COUNTERAFFIDAVIT B ON (T2.CASE_ID = B.CASE_ID)) T3 " +
					"ON (T1.CASE_ID = T3.BASECASE_NO) ORDER BY T3.TYPE_OF_CASE";

		} else if ((user[2].equals("03")) || (user[2].equals("04"))) {

			/*sql =

			"SELECT T1.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE, NAME_OF_RESPONDENT_ADVOCATE,  DEPARTMENT, BASECASE_NO, PARENTCASE_NO, STATUS, TYPE_OF_CASE, TO_CHAR(DATE_OF_ENTRY, 'dd/mm/yyyy') DATE_OF_ENTRY, BRIEF_DESCRIPTION, TO_CHAR(T1.DATE_OF_APPEARANCE, 'dd/mm/yyyy') DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'dd/mm/yyyy') DATE_OF_ADMITTANCE, DISTRICT, SERIAL_NO, GP_OFFICE_FILE_NO, SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY, TO_CHAR(DATE_OF_AFDVTRECEIVE_FROMGP, 'dd/mm/yyyy')  DATE_OF_AFDVTRECEIVE_FROMGP, SUIT_VALUE,PRAYER, SECY_ID, T3.CASE_ID CHILD_CASE, T3.TYPE_OF_CASE CHILD_CASE_TYPE,  STATUS CHILD_CASE_STATUS, BASECASE_NO CHILD_CASE_BASECASE_NO,   TO_CHAR(DATE_OF_APPEARANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_APPEARANCE,  TO_CHAR(DATE_OF_ADMITTANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_ADMITTANCE,  TO_CHAR(DATE_OF_PR_SENTTOGP, 'Month DD, YYYY') CHILD_CASE_DATE_OF_PR_SENTTOGP,   TO_CHAR(DATE_OF_CA_FILEDBYGPINHC, 'Month DD, YYYY') CHILD_CASE_DATE_CA_FILEDINHC ,IS_GOV_PET_RES,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO  FROM ( SELECT A.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE,  NAME_OF_RESPONDENT_ADVOCATE, DEPARTMENT, BASECASE_NO,  PARENTCASE_NO, STATUS, TYPE_OF_CASE, DATE_OF_ENTRY,  BRIEF_DESCRIPTION, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DISTRICT, A.SERIAL_NO, YEAR, GP_OFFICE_FILE_NO, SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY, DATE_OF_AFDVTRECEIVE_FROMGP,  APPEARANCE, DATE_OF_AFDVTRECEIVE_FROMHC, SUIT_VALUE,PRAYER ,IS_GOV_PET_RES,COURT_ID,PWC_AUTH,NATURE_OF_CASE,SECTION_NO  FROM  LCMS_MASTER A, CASE_MASTER B WHERE  A.SERIAL_NO = B.SERIAL_NO AND A.CASE_ID=upper('"
					+ caseid
					+ "') and TYPE_OF_CASE='CASE' )"
					+ " T1 left join HOD_MST C ON "
					+ " (T1.DEPARTMENT = TO_CHAR(C.HOD_NO)) left join HOD_DEPTLINK D ON (C.HOD_NO = D.HOD_NO) left join "
					+ " DEPARTMENT_MST E ON (D.DEPT_NO = E.DEPT_NO) left join  ( SELECT T2.CASE_ID, TYPE_OF_CASE, STATUS,"
					+ " BASECASE_NO, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DATE_OF_PR_SENTTOGP, DATE_OF_CA_FILEDBYGPINHC FROM "
					+ " (SELECT CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE,  DATE_OF_ADMITTANCE FROM "
					+ " LCMS_MASTER WHERE BASECASE_NO=upper('"
					+ caseid
					+ "')  \t) T2 left join PARAWISEREMARKS A ON "
					+ " (T2.CASE_ID = A.CASE_ID) left join COUNTERAFFIDAVIT B ON (T2.CASE_ID = B.CASE_ID)) T3 ON "
					+ " (T1.CASE_ID = T3.BASECASE_NO) ORDER BY T3.TYPE_OF_CASE";*/

			sql="select " +
					"T1.CASE_ID,  T1.NAME_OF_PETITIONER, T1.NAME_OF_RESPONDENT, T1.NAME_OF_PETETIONER_ADVOCATE, T1.NAME_OF_RESPONDENT_ADVOCATE, T1.advocate_code, T1.DEPARTMENT, T1.BASECASE_NO, T1.PARENTCASE_NO, T1.STATUS, T1.TYPE_OF_CASE," +
					"TO_CHAR(T1.DATE_OF_ENTRY, 'dd/mm/yyyy') DATE_OF_ENTRY, T1.BRIEF_DESCRIPTION, TO_CHAR(T1.DATE_OF_APPEARANCE, 'dd/mm/yyyy') DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'dd/mm/yyyy') DATE_OF_ADMITTANCE," +
					"T1.DISTRICT, T1.SERIAL_NO, T1.GP_OFFICE_FILE_NO, T1.SECRETARIAT_FILE_NO, T1.STAGE, T1.CATEGORY, T1.PRIORITY, TO_CHAR(T1.DATE_OF_AFDVTRECEIVE_FROMGP, 'dd/mm/yyyy')  DATE_OF_AFDVTRECEIVE_FROMGP, " +
					"T1.SUIT_VALUE,T1.PRAYER, E.SECY_ID, T3.CASE_ID CHILD_CASE, T3.TYPE_OF_CASE CHILD_CASE_TYPE,  T1.STATUS CHILD_CASE_STATUS, T1.BASECASE_NO CHILD_CASE_BASECASE_NO," +
					"TO_CHAR(T1.DATE_OF_APPEARANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_APPEARANCE,  TO_CHAR(T1.DATE_OF_ADMITTANCE, 'Month DD, YYYY') CHILD_CASE_DATE_OF_ADMITTANCE," +
					"TO_CHAR(T3.DATE_OF_PR_SENTTOGP, 'Month DD, YYYY') CHILD_CASE_DATE_OF_PR_SENTTOGP,   TO_CHAR(T3.DATE_OF_CA_FILEDBYGPINHC, 'Month DD, YYYY') CHILD_CASE_DATE_CA_FILEDINHC ," +
					"T1.IS_GOV_PET_RES,T1.COURT_ID,T1.PWC_AUTH,T1.NATURE_OF_CASE,T1.SECTION_NO FROM ( SELECT A.CASE_ID, NAME_OF_PETITIONER, NAME_OF_RESPONDENT," +
					"NAME_OF_PETETIONER_ADVOCATE,  NAME_OF_RESPONDENT_ADVOCATE, advocate_code, DEPARTMENT, BASECASE_NO,  PARENTCASE_NO, STATUS, TYPE_OF_CASE, DATE_OF_ENTRY,  BRIEF_DESCRIPTION, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DISTRICT," +
					"A.SERIAL_NO, YEAR, GP_OFFICE_FILE_NO, SECRETARIAT_FILE_NO, STAGE, CATEGORY, PRIORITY, DATE_OF_AFDVTRECEIVE_FROMGP,  APPEARANCE, DATE_OF_AFDVTRECEIVE_FROMHC, SUIT_VALUE,PRAYER ,IS_GOV_PET_RES,COURT_ID," +
					"PWC_AUTH,NATURE_OF_CASE,SECTION_NO  FROM  LCMS_MASTER A, CASE_MASTER B WHERE  A.SERIAL_NO = B.SERIAL_NO AND A.CASE_ID=upper('"+caseid+"') and TYPE_OF_CASE='CASE' ) " +
					"T1 left join HOD_MST C ON (T1.DEPARTMENT = TO_CHAR(C.HOD_NO)) left join HOD_DEPTLINK D ON (C.HOD_NO = D.HOD_NO) left join  " +
					"DEPARTMENT_MST E ON (D.DEPT_NO = E.DEPT_NO) left join  ( SELECT T2.CASE_ID, TYPE_OF_CASE, STATUS," +
					"BASECASE_NO, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, DATE_OF_PR_SENTTOGP, DATE_OF_CA_FILEDBYGPINHC FROM " +
					"(SELECT CASE_ID, TYPE_OF_CASE, STATUS, BASECASE_NO, DATE_OF_APPEARANCE,  DATE_OF_ADMITTANCE FROM " +
					"LCMS_MASTER WHERE BASECASE_NO=upper('"+caseid+"')  ) T2 left join PARAWISEREMARKS A ON " +
					"(T2.CASE_ID = A.CASE_ID) left join COUNTERAFFIDAVIT B ON (T2.CASE_ID = B.CASE_ID)) T3 ON (T1.CASE_ID = T3.BASECASE_NO) ORDER BY T3.TYPE_OF_CASE";
		}

		System.out.println("Chk Case  sql --- " + sql);
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			i = wpform.setCaseDetails(rs, user);

			if ((i == 1)
					&& (wpform.getStage() != null)
					&& ((wpform.getStage().equals("Admit&Interim"))
							|| (wpform.getStage().equals("Admit&Notice"))
							|| (wpform.getStage()
									.equals("Admit with Part Stay")) || (wpform
											.getStage().equals("Admit with Full Stay")))) {
				String interimString = "SELECT LM.CASE_ID,TYPE,replace(substr(upper(LM.CASE_ID),0,length(LM.CASE_ID)-5),upper(type),'') caseno, YEAR,BRIEF_DESCRIPTION,to_char(INTERIM_IMPLEMENT,'dd/mm/yyyy') INTERIM_IMPLEMENT,to_char(DUE_DATE_IMPLEMENT,'DD/MM/YYYY') DUE_DATE_IMPLEMENT,ATR_OF_IMPLIMENTATION   FROM LCMS_MASTER LM ,APPEAL_INTERIM_MASTER AIM where  LM.CASE_ID=AIM.CASE_ID and LM.PARENTCASE_NO='"
						+

						caseid + "' and LM.TYPE_OF_CASE='INTERIM'";
				System.out.println("interimString :: " + interimString);
				List interimdetails = DatabasePlugin.executeQuery(conn,
						interimString);
				if ((interimdetails != null) && (interimdetails.size() != 0)) {
					wpform.setInterimType((String) ((Map) interimdetails.get(0))
							.get("TYPE"));
					wpform.setInterimNo((String) ((Map) interimdetails.get(0))
							.get("CASENO"));
					wpform.setInterimYear((String) ((Map) interimdetails.get(0))
							.get("YEAR"));
					wpform.setInterimDirection((String) ((Map) interimdetails
							.get(0)).get("BRIEF_DESCRIPTION"));
					wpform.setImplement((String) ((Map) interimdetails.get(0))
							.get("INTERIM_IMPLEMENT"));
					wpform.setDueDate((String) ((Map) interimdetails.get(0))
							.get("DUE_DATE_IMPLEMENT"));
					wpform.setInterimDirectionImplemented((String) ((Map) interimdetails
							.get(0)).get("ATR_OF_IMPLIMENTATION"));

					wpform.setInterimOrder("Yes");
					System.out.println((String) ((Map) interimdetails.get(0))
							.get("CASENO")
							+ ":: "
							+ (String) ((Map) interimdetails.get(0))
							.get("YEAR")
							+ " :: "
							+ (String) ((Map) interimdetails.get(0))
							.get("BRIEF_DESCRIPTION"));
				}
			}

			sql = "select CASE_ID, GP_IDS,SR_NO,SERIAL_NO_REF FROM CASE_GPS  where  CASE_ID=upper('"
					+ caseid + "')";

			List gpTemp = DatabasePlugin.executeQuery(conn, sql);
			if (gpTemp != null) {
				String[] gps = new String[gpTemp.size()];
				if (gpTemp != null)
					for (int j = 0; j < gpTemp.size(); j++)
						gps[j] = (String) ((Map) gpTemp.get(j)).get("GP_IDS");
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) {
					wpform.setRespAdvocates(gps);
				} else {
					wpform.setPetAdvocates(gps);
				}
			}

			String sql1 = "select * from (select coalesce((select OFFICER_NAME from OFFICER_MST ofm where ofm.SR_NO=prsg.PET_RESP_ID),'--') as name, PET_RESP_ID  FROM CASES_PET_RESP prsg   where  CASE_SR_NO in (select SERIAL_NO from LCMS_MASTER where TYPE_OF_CASE='CASE' and CASE_ID=upper('"
					+

					caseid + "')) ) aa  group by NAME,PET_RESP_ID ";
			System.out.println("PW SUBMIT AUTH :: " + sql1);
			ArrayList values = DatabasePlugin.selectQuery(conn, sql1);
			values = DatabasePlugin.getLabelValueBeans(values, false);
			request.setAttribute("gpnamesCase", values);
			wpform.getDeptHodList();

			sql = "select SNO, CASE_ID_REF, HOD_DEPT, CASE_SERIAL_NO FROM CASE_HOD_DEPTS where  CASE_ID_REF=upper('"
					+ caseid + "')";

			List deptsTemp = DatabasePlugin.executeQuery(conn, sql);
			if (deptsTemp != null) {
				String[] depts = new String[deptsTemp.size()];
				if (deptsTemp != null) {
					for (int j = 0; j < deptsTemp.size(); j++)
						depts[j] = (String) ((Map) deptsTemp.get(j))
						.get("HOD_DEPT");
				}
				wpform.setDeptHod(depts);
			}

			sql = "select CASE_ID, DISTRICT, SNO, CASE_SERIAL_NO FROM CASE_DISTRICTS where  CASE_ID=upper('"
					+ caseid + "')";

			List distTemp = DatabasePlugin.executeQuery(conn, sql);
			if (distTemp != null) {
				String[] dist = new String[distTemp.size()];
				if (distTemp != null) {
					for (int j = 0; j < distTemp.size(); j++)
						dist[j] = (String) ((Map) distTemp.get(j))
						.get("DISTRICT");
				}
				wpform.setStDistrict(dist);
			}

			sql = "select CASE_ID, PET_RESP_ID, IS_PET_RESP, CASE_SR_NO FROM CASES_PET_RESP where  CASE_ID=upper('"
					+ caseid + "')";

			List temp = DatabasePlugin.executeQuery(conn, sql);
			if (temp != null) {
				String[] offs = new String[temp.size()];
				if (temp != null)
					for (int j = 0; j < temp.size(); j++)
					{
						offs[j] = (String) ((Map) temp.get(j)).get("PET_RESP_ID").toString();
					}
				if ((wpform.getIsPetRes() != null)
						&& (wpform.getIsPetRes().equals("R"))) {
					wpform.setRespNames(offs);
				} else {
					wpform.setPetNames(offs);
				}
			}

			sql = "SELECT STAGE,coalesce(REMARKS,' ') as REMARKS FROM LCMS_MASTER WHERE CASE_ID=upper('"
					+ caseid + "')";
			List temp1 = DatabasePlugin.executeQuery(conn, sql);
			if (temp1 != null) {
				for (int j = 0; j < temp1.size(); j++) {
					wpform.setRemarks((String) ((Map) temp1.get(j))
							.get("REMARKS"));
					wpform.setStage((String) ((Map) temp1.get(j)).get("STAGE"));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		} finally {

			try {

			} catch (Exception e2) {

				e2.printStackTrace();
			}

		}
		return i;
	}

	boolean insertUpdateDataInterim(Connection ds, WPAffidavitFormBean fb,
			String action,String userId) throws SQLException {
		boolean dbop = false;
		int InterimSeq = 0;
		String interimDisplay = null;
		String caseIdDisplay = null;
		String caseUser = null;
		String[] user;
		int seq;
		String ErrorMsg = " InterlocutoryAction - insertUpdateData - Error";
		String parentCaseid = fb.getCaseType() + fb.getCaseNo() + "/"
				+ fb.getCaseYear();
		caseIdDisplay = parentCaseid;
		String interimCaseid = fb.getInterimType() + fb.getInterimNo() + "/"
				+ fb.getInterimYear();
		interimDisplay = interimCaseid;
		System.out.println(":: " + parentCaseid + " :: " + interimCaseid
				+ " :: ");
		String petitionerName = "";
		String petitionerAdvocate = "";
		String respondentName = "";
		String respondentAdvocate = "";
		String[] department = fb.getDeptHod();
		int count = 0;
		List temp = new ArrayList();
		List temp1 = new ArrayList();
		List temp2 = new ArrayList();
		List temp3 = new ArrayList();
		try {
			String sql = "SELECT count(*) FROM LCMS_MASTER where CASE_ID='"
					+ interimCaseid + "' and TYPE_OF_CASE='INTERIM'";
			System.out.println("interim sql::" + sql);
			Object count1 = DatabasePlugin.executeScalar(ds,
					"SELECT count(*) FROM LCMS_MASTER where CASE_ID='"
							+ interimCaseid + "' and TYPE_OF_CASE='INTERIM'");
			count = (count1 != null) && (!count1.equals("")) ? Integer
					.parseInt(count1.toString()) : 0;

					Object pcsrNo = DatabasePlugin
							.executeScalar(
									ds,
									"select SERIAL_NO from LCMS_MASTER where TYPE_OF_CASE='CASE' and CASE_ID=upper('"
											+ parentCaseid + "')");
					temp = DatabasePlugin.executeQuery(ds,
							"select HOD_DEPT from CASE_HOD_DEPTS where CASE_SERIAL_NO="
									+ pcsrNo);
					temp1 = DatabasePlugin
							.executeQuery(ds,
									"select GP_IDS from CASE_GPS where SERIAL_NO_REF="
											+ pcsrNo);

					System.out.println("temp1psrno-->"+pcsrNo);
					temp2 = DatabasePlugin.executeQuery(ds,
							"select PET_RESP_ID from CASES_PET_RESP where CASE_SR_NO="
									+ pcsrNo);
					temp3 = DatabasePlugin.executeQuery(ds,
							"select DISTRICT from CASE_DISTRICTS where CASE_SERIAL_NO="
									+ pcsrNo);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("temp;;"+temp);
		System.out.println("temp1;;"+temp1);
		System.out.println("temp2;;"+temp2);
		System.out.println("temp3;;"+temp3);

		if(temp!=null && temp1!=null && temp2!=null && temp3!=null)
		{



			String[] sqls = new String[3 + temp.size() + temp1.size() + 10
			                           + temp2.size() + temp3.size()];

		}

		else if(temp!=null  && temp2!=null && temp3!=null)
		{


			String[] sqls = new String[3 + temp.size() + 10
			                           + temp2.size() + temp3.size()];

		}

		else if(temp1!=null  && temp2!=null && temp3!=null)
		{


			String[] sqls = new String[3 + temp1.size() + 10
			                           + temp2.size() + temp3.size()];

		}

		else if(temp!=null && temp3!=null)
		{


			String[] sqls = new String[3 + temp.size() + 10
			                           + temp3.size()];

		}

		else if(temp!=null  && temp2!=null)
		{


			String[] sqls = new String[3 + temp.size() + 10
			                           + temp2.size()];

		}

		else if(temp2!=null && temp3!=null)
		{


			String[] sqls = new String[3 +  10
			                           + temp2.size() + temp3.size()];

		}

		if ((fb.getStatus() == null) || (fb.getStatus().equals(""))) {
			if ((fb.getStage().equals("PostedAdmission"))
					|| (fb.getStage().equals("NoticeAdmission"))) {
				fb.setStatus("Filing");
			} else {
				fb.setStatus("Admitted");
			}
		} else if (fb.getStatus().equals("Disposed")) {
			fb.setStage("Judgement Pronounced");
		} else if (fb.getStatus().equals("Pending")) {
			fb.setStage("PostedHearing");
		}

		String chrPetRes = "";
		String petAdv = "";
		String resAdv = "";
		String resAdvCode = "";
		String petName = "";
		String resName = "";

		if ((fb.getIsPetRes() != null) && (fb.getIsPetRes().equals("R"))) {
			petAdv = fb.getPetAdvocate();
			resAdv = fb.getRespAdvocates()[0];
		}
		if ((fb.getIsPetRes() != null) && (fb.getIsPetRes().equals("P"))) {
			petAdv = fb.getPetAdvocates()[0];
			resAdv = fb.getRespAdvocate();
			resAdvCode = fb.getRespAdvocateCode();
			if(resAdvCode==null || resAdvCode.equals(""))
				resAdvCode = "-";

		}

		if ((fb.getIsPetRes() != null) && (fb.getIsPetRes().equals("R"))) {
			petName = fb.getPetName();
			resName = fb.getRespNames()[0];
		}
		if ((fb.getIsPetRes() != null) && (fb.getIsPetRes().equals("P"))) {
			petName = fb.getPetNames()[0];
			resName = fb.getRespName();
		}
		String pwcString = "";
		String[] pwcAuth = fb.getAuthpwca();
		if ((pwcAuth != null) && (pwcAuth.length != 0)) {
			int l = pwcAuth.length;
			for (int i = 0; i < l; i++) {
				if (i + 1 == l) {
					pwcString = pwcString + pwcAuth[i];
				} else {
					pwcString = pwcString + pwcAuth[i] + ",";
				}
			}
		}

		System.out.println(":::   " + petName + resName + petAdv + resAdv + " "
				+ fb.getIsPetRes());
		if (count == 0) {
			InterimSeq = Integer.parseInt(DatabasePlugin.getStringfromQuery("select nextval('lcms_seq')", ds));

			ArrayList<String> sqlsNew = new ArrayList<String>();
			//sqls[0] =

			sqlsNew.add("INSERT INTO LCMS_MASTER (case_id, name_of_petitioner , name_of_respondent , name_of_petetioner_advocate , name_of_respondent_advocate, advocate_code, lcms_master, department, basecase_no, parentcase_no, status, type_of_case,\tdate_of_entry, brief_description, date_of_appearance, date_of_admittance, district, serial_no, type, year, " +
					//"gp_office_file_no, " +
					"secretariat_file_no, stage, COURT_ID, IS_GOV_PET_RES,CASE_ENTERED_BY,PWC_AUTH,NATURE_OF_CASE)  VALUES (upper('"
					+ interimCaseid
					+ "'),'"
					+ petName
					+ "','"
					+ resName
					+ "','"
					+ petAdv
					+ "',"
					+ "'"
					+ resAdv
					+ "','"
					+ resAdvCode
					+ "','"
					+ department[0]
							+ "','"
							+ parentCaseid
							+ "',"
							+ "'"
							+ parentCaseid
							+ "','"
							+ fb.getStatus()
							+ "','INTERIM', NOW(),'"
							+ fb.getInterimDirection()
							+ "', to_date('"
							+ fb.getDtListing()
							+ "',"
							+ "'dd/mm/yyyy'), to_date('"
							+ fb.getDtFiling()
							+ "','dd/mm/yyyy'),'"
							+ fb.getStDistrict()[0]
									+ "',"
									+ InterimSeq
									+ ","
									+ "'"
									+ fb.getInterimType()
									+ "','"
									+ fb.getInterimYear()
									+ "','"
									//+ fb.getGpFileNo()
									//+ "',"
									+ "'"
									+ fb.getSecyFileNo()
									+ "','"
									+ fb.getStage()
									+ "',"
									+ fb.getCourtType()
									+ ",'"
									+ fb.getIsPetRes()
									+ "','"
									+userId
									+ "','"
									+ pwcString
									+ "','"
									+ fb.getNatureofcase() + "')");

			//sqls[1] =

			sqlsNew.add("insert into appeal_interim_master(INTERIM_IMPLEMENT,CASE_ID, NO_OF_CASES, DATE_OF_PROCESSFEE_CALLEDBYGP, DATE_OF_PROCESSFEE_PAIDTOGP, AMOUNT_OF_PROCESSFEE, DATE_OF_MATERIAL_CALLEDBYGP, DATE_OF_MATERIAL_SENTTOGP, SERIAL_NO, FILEDBY,DUE_DATE_IMPLEMENT,ATR_OF_IMPLIMENTATION)  values(to_date('"
					+ fb.getImplement()
					+ "','dd/mm/yyyy'),upper('"
					+ interimCaseid
					+ "'), '', to_date('','dd/mm/yyyy'), to_date('','dd/mm/yyyy'), '',"
					+ " to_date('','dd/mm/yyyy'), to_date('','dd/mm/yyyy'), '"
					+ InterimSeq
					+ "', '"
					+ fb.getIsPetRes()
					+ "',to_date('"
					+ fb.getDueDate()
					+ "','dd/mm/yyyy'),'"
					+ fb.getInterimDirectionImplemented() + "')");
		} else {


			ArrayList<String> sqlsNew=new ArrayList<String>();
			try {
				InterimSeq = Integer.parseInt(DatabasePlugin.executeScalar(
						ds,
						"SELECT SERIAL_NO FROM LCMS_MASTER where type_of_case='INTERIM' and CASE_ID='"
								+ interimCaseid + "'").toString());
			} catch (Exception e) {
				System.out.println("Error in Seq Interim");
			}

			//sqls[0] =

			sqlsNew.add("update LCMS_MASTER set NAME_OF_PETITIONER='" + petName
					+ "', NAME_OF_RESPONDENT='" + resName + "', "
					+ " NAME_OF_PETETIONER_ADVOCATE='" + petAdv
					+ "', NAME_OF_RESPONDENT_ADVOCATE='" + resAdv
					+ "', advocate_code='" + resAdvCode
					+ "', DEPARTMENT='" + department[0] + "', "
					+ " BASECASE_NO = '" + parentCaseid
					+ "' , PARENTCASE_NO= '" + parentCaseid + "', STATUS='"
					+ fb.getStatus() + "', " + " BRIEF_DESCRIPTION='"
					+ fb.getInterimDirection()
					+ "', DATE_OF_ADMITTANCE=to_date('" + fb.getDtFiling()
					+ "','dd/mm/yyyy'), " + " DATE_OF_APPEARANCE=to_date('"
					+ fb.getDtListing() + "','dd/mm/yyyy'), DISTRICT='"
					+ fb.getStDistrict()[0] + "', " 
					//+ " GP_OFFICE_FILE_NO=' "+ fb.getGpFileNo() + "', "
					+ " SECRETARIAT_FILE_NO='"
					+ fb.getSecyFileNo() + "', STAGE='" + fb.getStage() + "', "
					+ " IS_GOV_PET_RES='" + fb.getIsPetRes() + "',PWC_AUTH='"
					+ pwcString + "',NATURE_OF_CASE='" + fb.getNatureofcase()
					+ "' " + "  where CASE_ID=upper('" + interimCaseid + "') and type_of_case='INTERIM'");
			//System.out.println("Interim UPdate :: " + sqls[0]);

			//sqls[1] =

			sqlsNew.add("update appeal_interim_master set  INTERIM_IMPLEMENT=to_date('"
					+ fb.getImplement()
					+ "','dd/mm/yyyy'),"
					+ " NO_OF_CASES='', DATE_OF_PROCESSFEE_CALLEDBYGP=to_date('','dd/mm/yyyy'), DATE_OF_PROCESSFEE_PAIDTOGP=to_date('','dd/mm/yyyy'), "
					+ " AMOUNT_OF_PROCESSFEE='', DATE_OF_MATERIAL_CALLEDBYGP=to_date('','dd/mm/yyyy'), "
					+ " DATE_OF_MATERIAL_SENTTOGP=to_date('','dd/mm/yyyy'), FILEDBY='"
					+ fb.getIsPetRes() + "'," + " DUE_DATE_IMPLEMENT=to_date('"
					+ fb.getDueDate()
					+ "','dd/mm/yyyy'),ATR_OF_IMPLIMENTATION='"
					+ fb.getInterimDirectionImplemented() + "' "
					+ " where CASE_ID=upper('" + interimCaseid + "')");
		}


		ArrayList<String> sqlsNew=new ArrayList<String>();
		//sqls[2] =

		sqlsNew.add("INSERT INTO TB_INTERLOCUTORY_FORM (SERIAL_NO, CASE_ID, DATE_OF_ENTRY ,  USER_ID, BASECASE_NO, PARENTCASE_NO ,  NAME_OF_PETITIONER,  NAME_OF_RESPONDENT, NAME_OF_PETETIONER_ADVOCATE , NAME_OF_RESPONDENT_ADVOCATE , DEPARTMENT,  DISTRICT, " +
				//"GP_OFFICE_FILE_NO, " +
				"SECRETARIAT_FILE_NO, BRIEF_DESCRIPTION, DATE_OF_APPEARANCE, DATE_OF_ADMITTANCE, FILEDBY , STAGE ) VALUES ("
				+ InterimSeq
				+ ", '"
				+ interimCaseid
				+ "', NOW(), '"
				+ userId
				+ "', '"
				+ parentCaseid
				+ "','"
				+ parentCaseid
				+ "', "
				+ "'"
				+ petitionerName
				+ "','"
				+ respondentName
				+ "','"
				+ petitionerAdvocate
				+ "','"
				+ respondentAdvocate
				+ "',"
				+ " '"
				+ department[0]
						+ "', '"
						+ fb.getStDistrict()[0]
								+ "', '"
								//+ fb.getGpFileNo()
								//+ "','"
								+ fb.getSecyFileNo()
								+ "', "
								+ "'"
								+ fb.getBrfDiscription()
								+ "', to_date('"
								+ fb.getDtListing()
								+ "','dd/mm/yyyy'),"
								+ " to_date('"
								+ fb.getDtFiling()
								+ "','dd/mm/yyyy'),  '"
								+ fb.getIsPetRes()
								+ "' ,'"
								+ fb.getStage() + "' )");

		//sqls[3] = 
		sqlsNew.add("delete from  CASE_HOD_DEPTS where CASE_SERIAL_NO=" + InterimSeq);
		//sqls[4] = 
		sqlsNew.add("delete from  CASE_GPS where SERIAL_NO_REF=" + InterimSeq);

		for (int i = 0; i < temp.size(); i++) {
			//sqls[(4 + i + 1)] = 

			sqlsNew.add("insert into CASE_HOD_DEPTS ( SNO, CASE_ID_REF, HOD_DEPT, CASE_SERIAL_NO ) VALUES ((select coalesce(max(SNO),0)+1 from CASE_HOD_DEPTS),upper('"
					+ interimCaseid
					+ "'),'"
					+ ((Map) temp.get(i)).get("HOD_DEPT") + "','" + InterimSeq + "') ");
		}
		for (int i = 0; i < temp1.size(); i++) {
			//sqls[(4 + temp.size() + i + 1)] =

			sqlsNew.add("insert into CASE_GPS  ( CASE_ID, GP_IDS, SERIAL_NO_REF, SR_NO )  VALUES (upper('"
					+ interimCaseid
					+ "'),'"
					+ ((Map) temp1.get(i)).get("GP_IDS")
					+ "','"
					+ InterimSeq
					+ "'," + " (select coalesce(max(SR_NO),0)+1 from CASE_GPS))");
		}
		//sqls[(4 + temp.size() + temp1.size() + 1)] = 

		sqlsNew.add("delete from  CASES_PET_RESP where CASE_SR_NO=" + InterimSeq);
		//sqls[(4 + temp.size() + temp1.size() + 2)] = 
		sqlsNew.add("delete from  CASE_DISTRICTS where CASE_SERIAL_NO=" + InterimSeq);

		for (int i = 0; i < temp2.size(); i++) {
			//sqls[(5 + temp.size() + temp1.size() + 2 + i)] =

			sqlsNew.add("insert into CASES_PET_RESP  ( CASE_ID, PET_RESP_ID, IS_PET_RESP,  CASE_SR_NO ) VALUES  (upper('"
					+ interimCaseid
					+ "'),'"
					+ ((Map) temp2.get(i)).get("PET_RESP_ID")
					+ "','"
					+ fb.getIsPetRes() + "'," + InterimSeq + ") ");
		}
		for (int i = 0; i < temp3.size(); i++) {
			//sqls[(5 + temp.size() + temp1.size() + 3 + i + temp2.size())] = 
			sqlsNew.add("insert into CASE_DISTRICTS ( SNO, CASE_ID, DISTRICT, CASE_SERIAL_NO ) VALUES ((select coalesce(max(SNO),0)+1 from CASE_DISTRICTS),upper('"
					+ interimCaseid
					+ "'),'"
					+ ((Map) temp3.get(i)).get("DISTRICT") + "'," + InterimSeq + ") ");
		}



		int count1=0;
		if(sqlsNew.size() > 0)
			count1 = DatabasePlugin.executeBatch(sqlsNew);

		//System.out.println("dbop:"+dbop);
		return  count1>0?true:false;
		//dbop = GeneralSQL.executeInsertUpdateBatch(ds, sqls, ErrorMsg);

		//return dbop;
	}

	boolean checkForValidCase(DataSource ds, String caseid, String loginUser,String roleId) {
		Connection conn = null;
		boolean flag = false;
		String sql = "";
		sql = "select CASE_ENTERED_BY from LCMS_MASTER where CASE_ID='"+ caseid + "' ";

		try {
			String caseUser = StringUtils.trimToEmpty((String) DatabasePlugin.executeScalar(ds, sql));

			if (caseUser==null || caseUser.equals("")) {
				return false;
			}

			if (caseUser!=null && caseUser.equals(loginUser)) 
			{
				flag = true;
			} 
			else 
			{
				List parentUsers = DatabasePlugin.executeQuery(ds,
						"SELECT CHILD_USERID FROM DESIGNATION_MAPPED where PARENT_USERID='"
								+ loginUser + "'");
				if ((parentUsers != null) && (parentUsers.size() != 0)) {
					Map m = null;
					for (int i = 0; i < parentUsers.size(); i++) {
						m = (Map) parentUsers.get(i);
						if (caseUser.equals(m.get("CHILD_USERID").toString())) {
							flag = true;
							break;
						}
					}
				} else {
					flag = false;
				}
			}

			if ((roleId != null) && (roleId.equals("03"))) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}

		return flag;
	}

}