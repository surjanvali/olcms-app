package judgement;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import common.plugin.DatabasePlugin;

public class JudgementAction  extends DispatchAction {


	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JudgementFormBean jfbform = (JudgementFormBean) form;
		String target = new String("judgementform");
		Connection con=null;
		HttpSession session = request.getSession();
		String sql="";
		try{

			con=DatabasePlugin.connect();
			
			jfbform.reset(mapping, request);
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
			jfbform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));
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

	public ActionForward insertDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JudgementFormBean jfb=(JudgementFormBean)form;
		String sql=null;
		Connection con=null;
		int noOfRows1=0,noOfRows=0;
		try{
			con=DatabasePlugin.connect();
			con.setAutoCommit(false);

			noOfRows=Integer.parseInt((String)jfb.getProperty("noOfRows"));
			noOfRows1=Integer.parseInt((String)jfb.getProperty("noOfRows1"));
			System.out.println("insert");

			String caseid =jfb.getProperty("caseId").toString();//getCaseNo();
			ArrayList query_list=new ArrayList();

			if (Integer.parseInt(DatabasePlugin.getStringfromQuery("SELECT count(*) from JUDGEMENT WHERE CASE_ID='" + caseid + "' ", con)) > 0) {

				sql="update JUDGEMENT set BENCH='"+ jfb.getProperty("bench")+ "'," +
						" DATE_OF_DISPOSAL=to_date('"+ jfb.getProperty("judgementDate")+ "','dd/mm/yyyy'), WHETHER_GP_APPEARED='"+ jfb.getProperty("gpAppeared")+ "', RESULT_FAVOURTO_GOVT='"+ jfb.getProperty("result")+ "'," +
						" TIMELIMIT_FOR_IMPLEMENTATION=to_date('"+ jfb.getProperty("implementationDate")+ "','dd/mm/yyyy'), DATE_CERTCOPYRECEIVED_BYDEPT=to_date('"+ jfb.getProperty("copyDate")+ "','dd/mm/yyyy'), REMARKS='"+ jfb.getProperty("details")+ "'," +
						" OPINION_OF_GP='"+ jfb.getProperty("gpOpinion")+ "',APPEAL_IMPLEMENT='"+ jfb.getProperty("appealimp")+ "', ATR_OF_JDGMNT_IMPLEMENTATION='"+ jfb.getProperty("atrDescription") + "' " +
						" where CASE_ID='"+ caseid+ "' ";
				query_list.add(sql);

				sql="insert into JUDGEMENT_LOG select * from JUDGEMENT where CASE_ID='"+ caseid+ "' ";
				query_list.add(sql);

			}
			else{
				sql="insert into JUDGEMENT (CASE_ID, BENCH," +
						"DATE_OF_DISPOSAL, WHETHER_GP_APPEARED, RESULT_FAVOURTO_GOVT," +
						"  TIMELIMIT_FOR_IMPLEMENTATION, DATE_CERTCOPYRECEIVED_BYDEPT, REMARKS," +
						" OPINION_OF_GP,APPEAL_IMPLEMENT,ATR_OF_JDGMNT_IMPLEMENTATION)  " +
						"values" +
						" ('"+ caseid+ "','"+ jfb.getProperty("bench")+ "' ," +
						" to_date('"+ jfb.getProperty("judgementDate")+ "','dd/mm/yyyy') ,'"+ jfb.getProperty("gpAppeared")+ "','"+ jfb.getProperty("result")+ "' , " +
						" to_date('"+ jfb.getProperty("implementationDate")+ "','dd/mm/yyyy'),to_date('"+ jfb.getProperty("copyDate")+ "','dd/mm/yyyy'),'"+ jfb.getProperty("details")+ "' , "+
						" '"+ jfb.getProperty("gpOpinion")+ "','"+ jfb.getProperty("appealimp")+ "','"+ jfb.getProperty("atrDescription") + "')";
				query_list.add(sql);
			}

			System.out.println("petnoOfRows:: "+noOfRows1);

			for(int i=0;i<noOfRows1;i++)
			{
				sql="INSERT INTO JUDGMENT_ATTACHMENTS_LOG (SRNO, CASE_ID, ATTACHMENT_PATH, DELETE_FLAG, LASTUPDATED, ATTACHMENT_DESC, IS_DELETED ) SELECT SRNO, CASE_ID, ATTACHMENT_PATH, DELETE_FLAG, LASTUPDATED, ATTACHMENT_DESC, IS_DELETED  FROM  JUDGMENT_ATTACHMENTS WHERE CASE_ID='"+caseid+"'";
				query_list.add(sql);

				sql="INSERT INTO JUDGMENT_ATTACHMENTS  (CASE_ID,ATTACHMENT_DESC, ATTACHMENT_PATH)" +
						"VALUES( '"+caseid+"', '"+jfb.getProperty("docDescription"+i)+"', '"+jfb.getProperty("document"+i)+"')";
				query_list.add(sql);
			}

			for(int i=0;i<noOfRows;i++)
			{
				sql="INSERT INTO JUDGE_NAMES_LOG (SNO, CASE_ID, JUDGE_NAMES, IS_DELETED ) SELECT SNO, CASE_ID, JUDGE_NAMES, IS_DELETED FROM  JUDGE_NAMES WHERE CASE_ID='"+caseid+"'";
				query_list.add(sql);

				sql="INSERT INTO JUDGE_NAMES(case_id, judge_names) VALUES('"+caseid+"', '"+jfb.getProperty("judgename"+i)+"')";
				query_list.add(sql);
			}

			System.out.println("QUERY LIST SIZE"+query_list.size());
			int[] exe =executeBatch(query_list, con);
			if (exe.length==query_list.size()) {
				con.commit();
				request.setAttribute("msg", " Judgement details saved successfully for case Id :"+caseid);
				jfb.reset(mapping, request);
			}else{
				request.setAttribute("msg1", " Error in submission ");
				con.rollback();
			}
		}catch (Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		finally{
			DatabasePlugin.close(con, null, null);
		}
		return getCaseDetails(mapping, form, request, response);
	}

	public ActionForward getCaseDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String target = new String("judgementform");
		String sql = null;
		Connection con=null;
		ResultSet rs =null;
		Statement st=null;
		try{

			HttpSession session = request.getSession();
			con=DatabasePlugin.connect();
			st=con.createStatement();
			JudgementFormBean jfbform = (JudgementFormBean) form;
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
			jfbform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));

			sql="SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR FROM OLCMS_CASE_DETAILS CD "+ 
					"INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO)"+
					"INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  WHERE CD.SNO='"+jfbform.getProperty("caseId")+"'";
			System.out.println(sql);
			rs=st.executeQuery(sql);
			if(rs!= null && rs.next()){

				jfbform.setProperty("courtName", rs.getString("COURT_NAME"));
				jfbform.setProperty("caseType", rs.getString("CASE_SHORT_NAME"));
				jfbform.setProperty("caseNo", rs.getString("CASE_NO"));
				jfbform.setProperty("caseYear", rs.getString("CASE_YEAR"));
				request.setAttribute("caseIdDetails", "caseIdDetails");
			}else
				request.setAttribute("caseIdDetailsNotFound", "Case Details Not found");



		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{DatabasePlugin.close(con, st, rs);}
		return mapping.findForward(target);
	}





	public ActionForward ajax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html");
		PrintWriter out = null;
		String id="";
		Connection con = null;
		String type= null,sql=null;
		try{
			con=DatabasePlugin.connect();
			out = response.getWriter();
			if(request.getParameter("type")!= null && !request.getParameter("type").equals("")) type=request.getParameter("type").toString();
			if(request.getParameter("courtType")!= null && !request.getParameter("courtType").equals("")) id=request.getParameter("courtType").toString();
			if(type != null && type.equals("caseType")){
				sql="SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER WHERE COURT_ID="+id+" order by SNO";
				System.out.println(sql);
				out.println(DatabasePlugin.lvb(sql,con));
			}
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
			if(con!=null)
				con.close();
		}
		return null;
	}

	public static int[] executeBatch(ArrayList sqls, Connection conn)
	{
		int[] count={0};
		try{
			Statement st;
			st = conn.createStatement();
			for(int i=0; i<sqls.size(); i++)
			{
				System.out.println("sqls::::"+sqls.get(i));
				st.addBatch((String)sqls.get(i));
			}
			count =st.executeBatch();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public static ArrayList<LabelValueBean> getLabelValueBean(Connection con,String sql,boolean combo) {
		ArrayList<LabelValueBean> matrix = new ArrayList<LabelValueBean>();

		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try {
			/*System.out.println("lvb");*/
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (combo)
				matrix.add(new LabelValueBean("--Select--","0"));

			while(rs.next()) {
				matrix.add(new LabelValueBean(rs.getString(2),rs.getString(1)));
			}
			/*System.out.println(matrix);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrix;
	}




	public static String lvb(String sql,Connection con){
		StringBuffer val = new StringBuffer();
		ArrayList li=doubleArrayList(sql, con);
		val.append("<option value='0'>Select</option>");
		for(int i=0 ; i <li.size() ;i++){
			val.append("<option value='"+((ArrayList)li.get(i)).get(0)+"'>"+((ArrayList)li.get(i)).get(1)+"</option>");
		}
		return val.toString();
	}
	public static ArrayList<ArrayList<String>> doubleArrayList(String sql,Connection con) {
		ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
		ResultSet rs=null;
		Statement st=null;
		try {
			st=con.createStatement();
			rs=st.executeQuery(sql);
			int columns=columnCount(rs);
			ArrayList<String> subMatrix = null;

			while(rs.next()) {
				subMatrix = new ArrayList<String>();

				for (int i=1;i<=columns;i++) {
					subMatrix.add(rs.getString(i));
				}

				matrix.add(subMatrix);
			}
		} catch (Exception e) {
			System.out.println("Error while copying the double dimensional ArrayList");
			e.printStackTrace();
		}
		return matrix;
	}
	public static int columnCount(ResultSet rs) {

		int columns=0;
		try {
			ResultSetMetaData rm;

			if (rs!=null) {
				rm = rs.getMetaData();
				columns=rm.getColumnCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
}