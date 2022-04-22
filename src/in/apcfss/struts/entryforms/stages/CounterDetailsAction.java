package entryforms.stages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import common.plugin.DatabasePlugin;

public class CounterDetailsAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String target = new String("success");
		Connection con=null;
		HttpSession session = request.getSession();
		String sql="";
		try{

				con=DatabasePlugin.connect();
				CounterDetailsFormBean cdform = (CounterDetailsFormBean) form;
				cdform.reset(mapping, request);
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
				cdform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));
				
				cdform.setProperty("relatedGps", DatabasePlugin.getLabelValueBean(con, "select GP_NO, GP_NAME from GPOFFICE_MST ORDER BY GP_NAME", true));
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(con!=null)
					con.close();
			}catch (Exception e2) {
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
		Connection con=null;
		ResultSet rs =null;
		Statement st=null;
		try{

			HttpSession session = request.getSession();
			con=DatabasePlugin.connect();
			st=con.createStatement();
			CounterDetailsFormBean cdform = (CounterDetailsFormBean) form;
			cdform.reset(mapping, request);
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
			cdform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));
			
			sql="SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR FROM OLCMS_CASE_DETAILS CD" +
					" INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO)"+
					"INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  WHERE CD.SNO='"+cdform.getProperty("caseId")+"'";
			
			sql="SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR,CN.GP_ID,CN.PARAWISE_RMRKS_APRVD_DATE,CN.DATE_OF_COUNTER_AFF_TO_GP,CN.CASE_FILE " +
			"FROM OLCMS_CASE_DETAILS CD " +
			"INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO) " +
			"INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  " + 
			"LEFT JOIN " + 
			"( " +
			"SELECT DISTINCT CASE_ID,GP_ID,TO_CHAR(PARAWISE_RMRKS_APRVD_DATE,'DD/MM/YYYY') AS PARAWISE_RMRKS_APRVD_DATE,TO_CHAR(DATE_OF_COUNTER_AFF_TO_GP,'DD/MM/YYYY') AS DATE_OF_COUNTER_AFF_TO_GP,CASE_FILE FROM COUNTERAFFIDAVIT " +
			") CN ON(CN.CASE_ID=CD.SNO) " +
			"WHERE CD.SNO="+cdform.getProperty("caseId");
			
			System.out.println(sql);
			rs=st.executeQuery(sql);
			if(rs!= null && rs.next()){
				cdform.setProperty("courtName", rs.getString("COURT_NAME"));
				cdform.setProperty("caseType", rs.getString("CASE_SHORT_NAME"));
				cdform.setProperty("caseNo", rs.getString("CASE_NO"));
				cdform.setProperty("caseNo1", rs.getString("CASE_NO"));
				cdform.setProperty("caseYear", rs.getString("CASE_YEAR"));
				cdform.setProperty("caseYear1", rs.getString("CASE_YEAR"));
				cdform.setProperty("relatedGps", rs.getString("GP_ID"));
				cdform.setProperty("submissionDate", rs.getString("DATE_OF_COUNTER_AFF_TO_GP"));
				cdform.setProperty("approvedDate", rs.getString("PARAWISE_RMRKS_APRVD_DATE"));
				cdform.setProperty("documentUploaded",rs.getString("CASE_FILE"));
				request.setAttribute("caseIdDetails", "caseIdDetails");
			
				
				sql="SELECT USERNAME,REMARKS,INSERTED_BY,to_char(INSERTED_TIME,'DD/MM/YYYY') AS INSERTED_TIME FROM COUNTERAFFIDAVIT PR JOIN APP_USER_MST UM ON (UM.USERID=PR.INSERTED_BY) " +
				" WHERE CASE_ID='"+cdform.getProperty("caseId").toString().trim()+"'  order by INSERTED_TIME ";
				System.out.println(sql);
				request.setAttribute("CounterRemarks", DatabasePlugin.selectQueryMap(sql, con));
				
				
				//select ,INSERTED_BY,to_char(INSERTED_TIME,'dd/mm/yyy') from  where CASE_ID
			}else
				request.setAttribute("caseIdDetailsNotFound", "Case Details Not found");
			cdform.setProperty("relatedGps", DatabasePlugin.getLabelValueBean(con, "select GP_NO,GP_NAME from GPOFFICE_MST ORDER BY GP_NAME", true));
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(rs!=null)
					rs.close();
				if(st!=null)
					st.close();
				if(con!=null)
					con.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapping.findForward(target);
	}
	public ActionForward insertDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String sql=null;
		Connection con=null;
		String castCertificate=null,documentColumn="",documentColumnValue="";
		HttpSession session = request.getSession();
		try{
				con=DatabasePlugin.connect();
				con.setAutoCommit(false);
				CounterDetailsFormBean cdform = (CounterDetailsFormBean) form;
				System.out.println("insert");
				
				String case_id=cdform.getProperty("caseId").toString();
				String filesepartor="/"; 
				castCertificate = "uploads" + filesepartor + "counter" + filesepartor;
				case_id=case_id+cdform.getProperty("caseNo1")+"/"+cdform.getProperty("caseYear1");
				FormFile UploadFile=null;
				if(cdform.getProperty("document" ) !=null && !cdform.getProperty("document" ).equals("") && cdform.getProperty("document" ).toString()!=null && !cdform.getProperty("document" ).toString().equals("")){

					String filesBasePath = getServlet().getServletContext().getRealPath("/");

					if(cdform.getProperty("document" )!= null && !cdform.getProperty("document" ).equals(""))
					{
						UploadFile=(FormFile)cdform.getProperty("document" );
						boolean fileflag=DatabasePlugin.checkFileLimitations(UploadFile);
						if(fileflag){
							castCertificate = DatabasePlugin.saveDocument(UploadFile,castCertificate,request.getSession().getAttribute("userId").toString(),"counterFile"+DatabasePlugin.randomTransactionNo(), filesBasePath);
						}
						else
						{
							castCertificate="";
							request.setAttribute("msg", "Counter Affidavit is not uploaded due to File Size Exceeds the Limit 1MB");
						}
					}
				}else
					castCertificate="";
				
				if(castCertificate!=null && !castCertificate.equals("")){
					documentColumn=" CASE_FILE, ";
					documentColumnValue="'"+castCertificate+"',";
				}
				
				ArrayList query_list=new ArrayList();
				sql="INSERT INTO COUNTERAFFIDAVIT(CASE_ID, PARAWISE_RMRKS_APRVD_DATE,DATE_OF_COUNTER_AFF_TO_GP, REMARKS, GP_ID, INSERTED_BY,"+documentColumn+" INSERTED_TIME) " +
						" VALUES('"+cdform.getProperty("caseId")+"', to_date('"+cdform.getProperty("approvedDate")+"','dd/mm/yyyy'),  to_date('"+cdform.getProperty("submissionDate")+"','dd/mm/yyyy')," +
						" '"+((String)cdform.getProperty("counterRemarks")+"").replaceAll("'", "")+"', "+cdform.getProperty("relatedGp")+", '"+session.getAttribute("userId").toString()+"',"+documentColumnValue+" NOW())";

				query_list.add(sql);

				System.out.println("QUERY LIST SIZE"+query_list);
				int exe = DatabasePlugin.executeBatch(query_list, con);
				if (exe == query_list.size()) {
					con.commit();
					cdform.setProperty("counterRemarks", "");
					request.setAttribute("msg", " Counter Details   are Submitted Successfully. Case ID::"+cdform.getProperty("caseId"));
				}else{
					con.rollback();
					request.setAttribute("msg1", " Error in submission ");
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(con!=null)
					con.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return getCaseDetails(mapping, form, request, response);
	}

	
}

