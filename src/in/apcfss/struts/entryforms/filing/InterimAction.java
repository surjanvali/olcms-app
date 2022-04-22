package entryforms.filing;

import java.sql.Connection;
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

import common.plugin.DatabasePlugin;

public class InterimAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String target = new String("success");
		Connection con = null;
		HttpSession session = request.getSession();
		String sql="";
		try {

			con = DatabasePlugin.connect();

			InterimFormBean intform = (InterimFormBean) form;

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
			intform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));

			intform.setProperty("stages", DatabasePlugin.getLabelValueBean( con, "select SNO,STAGE_NAME from STAGE_MST", true));
			intform.setProperty("filedBys", DatabasePlugin.getLabelValueBean(con, "select SNO,TYPE from OLCMS_FILLEDBY_MST", true));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapping.findForward(target);
	}

	public ActionForward getCaseDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String target = new String("success");
		String sql = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		HttpSession session = request.getSession();
		try {

			con = DatabasePlugin.connect();
			st = con.createStatement();
			InterimFormBean intForm = (InterimFormBean) form;
			//intForm.reset(mapping, request);


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
			intForm.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));

			sql = "SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR FROM OLCMS_CASE_DETAILS CD INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO) INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  " +
					"WHERE CD.SNO='"+ intForm.getProperty("caseId") + "'";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			if (rs != null && rs.next()) {
				intForm.setProperty("courtName", rs.getString("COURT_NAME"));
				intForm.setProperty("caseType", rs.getString("CASE_SHORT_NAME"));
				intForm.setProperty("caseNo", rs.getString("CASE_NO"));
				intForm.setProperty("caseYear", rs.getString("CASE_YEAR"));
				request.setAttribute("caseIdDetails", "caseIdDetails");

				// SET PREVIOUS INTERIM DETAILS

				sql = "SELECT SNO, CASE_ID, TO_CHAR(DATE_OF_FILING,'DD/MM/YYYY') AS DATE_OF_FILING,  TO_CHAR(DATE_OF_LISTING,'DD/MM/YYYY') AS DATE_OF_LISTING , INTERIM_DESC, ATR_DESC, FILLED_BY, STAGE,  " +
						"TO_CHAR(DUE_DATE_IMPL_OF_INTERIM,'DD/MM/YYYY') AS DUE_DATE_IMPL_OF_INTERIM, TO_CHAR(DATE_OF_IMPL_OF_INTERIM,'DD/MM/YYYY') AS DATE_OF_IMPL_OF_INTERIM , UPDATE_TIME, USER_NAME, IP_ADDRESS " +
						"FROM OLCMS_INTERIM_INFORMATION WHERE CASE_ID='"+ intForm.getProperty("caseId") + "'";

				System.out.println("interim SQL:"+sql);

				List interimData = DatabasePlugin.executeQuery(sql, con);
				
				if(interimData  != null){
					//System.out.println("interimDataSQL:"+interimData);
					Map interimCaseData = (Map)interimData.get(0);

					intForm.setProperty("filingdate", interimCaseData.get("date_of_filing"));
					intForm.setProperty("listingdate", interimCaseData.get("date_of_listing"));
					intForm.setProperty("interimlDescription", interimCaseData.get("interim_desc"));
					intForm.setProperty("atrDescription", interimCaseData.get("atr_desc"));
					intForm.setProperty("dueDate", interimCaseData.get("due_date_impl_of_interim"));
					intForm.setProperty("orderDate", interimCaseData.get("date_of_impl_of_interim"));
					intForm.setProperty("filedBy", interimCaseData.get("filled_by"));
					intForm.setProperty("stage", interimCaseData.get("stage"));
				}
			} else
				request.setAttribute("caseIdDetailsNotFound", "Case Details Not found");

			intForm.setProperty("stages", DatabasePlugin.getLabelValueBean(con, "select SNO,STAGE_NAME from STAGE_MST", true));
			intForm.setProperty("filedBys", DatabasePlugin.getLabelValueBean(con, "select SNO,TYPE from OLCMS_FILLEDBY_MST",true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, st, rs);
		}
		return mapping.findForward(target);
	}


	public ActionForward insertDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		InterimFormBean intForm = (InterimFormBean) form;
		String sql = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			st = con.createStatement();

			System.out.println("insert");
			ArrayList query_list = new ArrayList();

			if (Integer.parseInt(DatabasePlugin.getStringfromQuery("SELECT count(*) from OLCMS_INTERIM_INFORMATION WHERE CASE_ID='"
					+ intForm.getProperty("caseId") + "' ", con)) > 0) {

				sql = "UPDATE OLCMS_INTERIM_INFORMATION set DATE_OF_FILING = to_date('"+ intForm.getProperty("filingdate")+ "','dd/mm/yyyy'), DATE_OF_LISTING=to_date('"+ intForm.getProperty("listingdate")
						+ "','dd/mm/yyyy'), "
						+ " INTERIM_DESC='"
						+ intForm.getProperty("interimlDescription")
						+ "',ATR_DESC='"
						+ intForm.getProperty("atrDescription")
						+ "',FILLED_BY="
						+ intForm.getProperty("filedBy")
						+ ", STAGE='"
						+ intForm.getProperty("stage")
						+ "', DUE_DATE_IMPL_OF_INTERIM=to_date('"
						+ intForm.getProperty("dueDate")
						+ "','dd/mm/yyyy'), "
						+ "DATE_OF_IMPL_OF_INTERIM=to_date('"
						+ intForm.getProperty("orderDate")
						+ "','dd/mm/yyyy'), UPDATE_TIME=NOW() WHERE CASE_ID='"
						+ intForm.getProperty("caseId") + "' ";
			} else {
				sql = "INSERT INTO OLCMS_INTERIM_INFORMATION( CASE_ID, DATE_OF_FILING, DATE_OF_LISTING, "
						+ " INTERIM_DESC,ATR_DESC, FILLED_BY, STAGE, DUE_DATE_IMPL_OF_INTERIM, DATE_OF_IMPL_OF_INTERIM, UPDATE_TIME, USER_NAME, IP_ADDRESS) "
						+ " VALUES( '"
						+ intForm.getProperty("caseId")
						+ "', to_date('"
						+ intForm.getProperty("filingdate")
						+ "','dd/mm/yyyy'), to_date('"
						+ intForm.getProperty("listingdate")
						+ "','dd/mm/yyyy'),"
						+ " '"
						+ intForm.getProperty("interimlDescription")
						+ "',  '"
						+ intForm.getProperty("atrDescription")
						+ "',"
						+ intForm.getProperty("filedBy")
						+ ", '"
						+ intForm.getProperty("stage")
						+ "', "
						+ " to_date('"
						+ intForm.getProperty("dueDate")
						+ "','dd/mm/yyyy'), to_date('"
						+ intForm.getProperty("orderDate")
						+ "','dd/mm/yyyy'), NOW(), '"
						+ request.getSession().getAttribute("userId")
						+ "',  '" + request.getRemoteAddr() + "')";
			}
			query_list.add(sql);

			int exe = DatabasePlugin.executeBatch(query_list, con);
			if (exe == query_list.size()) {
				con.commit();
				request.setAttribute("msg", " Interlocutory/Interim  Details saved Successfully for Case Id : " + intForm.getProperty("caseId"));
			} else {
				con.rollback();
				request.setAttribute("msg1", " Error in submission ");
			}

			intForm.setProperty("caseId", intForm.getProperty("caseId"));
		} catch (Exception e) {
			e.printStackTrace();
			con.rollback();
		} finally {
			DatabasePlugin.close(con, st, rs);
		}
		return getCaseDetails(mapping, intForm, request, response);
	}
}