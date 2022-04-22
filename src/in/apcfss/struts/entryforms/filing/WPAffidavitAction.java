package in.apcfss.struts.entryforms.filing;

import plugins.DatabasePlugin;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import in.apcfss.struts.commons.FileUploadUtilities;


public class WPAffidavitAction  extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WPAffidavitNEWFormBean formBean=(WPAffidavitNEWFormBean)form;
		String target = new String("Expired");
		Connection con=null;
		String user = null;
		try{
			user = (String)request.getSession().getAttribute("userid");

			if(user != null){
				target = new String("DEPT_WP_FORM");
				con=DatabasePlugin.connect();
				formBean.setProperty("courtList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,COURT_NAME FROM COURT_MST order by SNO", true));
				formBean.setProperty("caseTypesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER order by SNO", true));
				formBean.setProperty("subjectCategorysList",DatabasePlugin.getLabelValueBean(con, "SELECT SUBJECT_ID,SUBJECT_NAME FROM SUBJECTS_MST order by SUBJECT_ID", true));
				formBean.setProperty("prioritiesList",DatabasePlugin.getLabelValueBean(con, "select PRIORITY_ID,PRIORITY_NAME from PRIORITY_MST", true));
				formBean.setProperty("stagesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
				formBean.setProperty("districtList",DatabasePlugin.getLabelValueBean(con, "SELECT DISTRICT_ID,DISTRICT_NAME FROM DISTRICT_MST ORDER BY DISTRICT_NAME", true));
				formBean.setProperty("caseStatusList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_STATUS FROM OLCMS_CASE_STATUS_MASTER ORDER BY SNO", true));
				formBean.setProperty("fiftyYears",DatabasePlugin.getLabelValueBean(con, "WITH RECURSIVE nums (n) AS (SELECT TO_CHAR(current_date, 'yyyy')::NUMERIC-50 UNION ALL SELECT n+1 FROM nums WHERE n+1 <= TO_CHAR(current_date, 'yyyy')::NUMERIC) SELECT n  AS year,n  AS year FROM nums order by n desc", true));
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
		return mapping.findForward(target);
	}
	public ActionForward insertDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WPAffidavitNEWFormBean formBean=(WPAffidavitNEWFormBean)form;
		String sql=null;
		Connection con=null;
		String castCertificate=null;
		ResultSet rs =null;

		Statement st=null;
		String caseSRNo=null,districtId=null,caseStatus=null;
		try{
			con=DatabasePlugin.connect();
			con.setAutoCommit(false);
			st=con.createStatement();
			formBean.setProperty("courtList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,COURT_NAME FROM COURT_MST order by SNO", true));
			formBean.setProperty("caseTypesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER order by SNO", true));
			formBean.setProperty("subjectCategorysList",DatabasePlugin.getLabelValueBean(con, "SELECT SUBJECT_ID,SUBJECT_NAME FROM SUBJECTS_MST order by SUBJECT_ID", true));
			formBean.setProperty("prioritiesList",DatabasePlugin.getLabelValueBean(con, "select PRIORITY_ID,PRIORITY_NAME from PRIORITY_MST", true));
			formBean.setProperty("stagesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
			formBean.setProperty("fiftyYears",DatabasePlugin.getLabelValueBean(con, "WITH RECURSIVE nums (n) AS (SELECT TO_CHAR(current_date, 'yyyy')::NUMERIC-50 UNION ALL SELECT n+1 FROM nums WHERE n+1 <= TO_CHAR(current_date, 'yyyy')::NUMERIC) SELECT n  AS year,n  AS year FROM nums order by n desc", true));
			formBean.setProperty("districtList",DatabasePlugin.getLabelValueBean(con, "SELECT DISTRICT_ID,DISTRICT_NAME FROM DISTRICT_MST ORDER BY DISTRICT_NAME", true));
			formBean.setProperty("caseStatusList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_STATUS FROM OLCMS_CASE_STATUS_MASTER ORDER BY SNO", true));
			String case_id;
			int petnoOfRows=0;
			int respnoOfRows=0;
			System.out.println("petnoOfRowspetnoOfRows:::"+formBean.getProperty("petnoOfRows").toString());
			petnoOfRows=Integer.parseInt(formBean.getProperty("petnoOfRows").toString());
			respnoOfRows=Integer.parseInt(formBean.getProperty("respnoOfRows").toString());
			sql="select CASE_SHORT_NAME  from CASE_TYPE_MASTER where SNO= "+formBean.getProperty("caseType");
			rs=st.executeQuery(sql);
			if(rs!= null && rs.next())
				case_id=rs.getString("CASE_SHORT_NAME");
			else
				case_id="";
			String filesepartor="/"; 
			castCertificate = "uploads" + filesepartor + "casefile" + filesepartor;
			case_id=case_id+formBean.getProperty("caseNo")+"/"+formBean.getProperty("caseYear");
			FormFile UploadFile=null;
			if(formBean.getProperty("document" ) !=null && !formBean.getProperty("document" ).equals("") && formBean.getProperty("document" ).toString()!=null && !formBean.getProperty("document" ).toString().equals("")){

				String filesBasePath = getServlet().getServletContext().getRealPath("/");

				if(formBean.getProperty("document" )!= null && !formBean.getProperty("document" ).equals(""))
				{
					UploadFile=(FormFile)formBean.getProperty("document" );
					
					if(UploadFile!=null){
						FileUploadUtilities fuu = new FileUploadUtilities();
						castCertificate = fuu.saveFile(UploadFile, castCertificate, "castUpload"+DatabasePlugin.randomTransactionNo());
					}
					/*
					boolean fileflag=DatabasePlugin.checkFileLimitations(UploadFile);
					if(fileflag){
						castCertificate = DatabasePlugin.saveDocument(UploadFile,castCertificate,request.getSession().getAttribute("userId").toString(),"castUpload"+DatabasePlugin.randomTransactionNo(), filesBasePath);
					}*/
					else
					{
						castCertificate="";
						request.setAttribute("msg", "  Case File is not uploaded due to File Size Exceeds the Limit 1MB");
					}
				}
			}else
				castCertificate="";

			if(formBean.getProperty("caseSRNo" ) !=null && !formBean.getProperty("caseSRNo" ).equals("") && formBean.getProperty("caseSRNo" ).toString()!=null && !formBean.getProperty("caseSRNo" ).toString().equals("")){
				caseSRNo=formBean.getProperty("caseSRNo" ).toString();
			}
			if(formBean.getProperty("districtId" ) !=null && !formBean.getProperty("districtId" ).equals("") && formBean.getProperty("districtId" ).toString()!=null && !formBean.getProperty("districtId" ).toString().equals("")){
				districtId=formBean.getProperty("districtId" ).toString();
			}else
				districtId="0";
			if(formBean.getProperty("caseStatus" ) !=null && !formBean.getProperty("caseStatus" ).equals("") && formBean.getProperty("caseStatus" ).toString()!=null && !formBean.getProperty("caseStatus" ).toString().equals("")){
				caseStatus=formBean.getProperty("caseStatus" ).toString();
			}else
				caseStatus="0";
			ArrayList query_list=new ArrayList();
			long caseNo=getCaseNo(con);
			
			String yearFilling="0",mmFilling="0",ddFilling="0",ddColumn="",respAdvocateCode="0",documentColumn="",ddColumnValue="",documentColumnValue="",disposalColumn="",disposalColumnValue="",disposalTypeColumn="",disposalTypeColumnValue="";
			
			/*if(formBean.getProperty("yearFiling")!=null && !formBean.getProperty("yearFiling").equals(""))
				yearFilling=formBean.getProperty("yearFiling").toString();
			if(formBean.getProperty("mmFilling")!=null && !formBean.getProperty("mmFilling").equals(""))
				mmFilling=formBean.getProperty("mmFilling").toString();
			if(formBean.getProperty("mmFilling")!=null && !formBean.getProperty("mmFilling").equals(""))
				ddFilling=formBean.getProperty("mmFilling").toString();*/
			if(formBean.getProperty("respAdvocateCode")!=null && !formBean.getProperty("respAdvocateCode").equals(""))
				respAdvocateCode=formBean.getProperty("respAdvocateCode").toString();
			
			if(formBean.getProperty("dtListing")!=null && !formBean.getProperty("dtListing").equals("")){
				ddColumn=" DATE_OF_NOTICE, ";
				ddColumnValue=" to_date('"+formBean.getProperty("dtListing")+"','dd/mm/yyyy'),";
			}
			if(castCertificate!=null && !castCertificate.equals("")){
				documentColumn=" CASE_FILE, ";
				documentColumnValue="'"+castCertificate+"',";
			}
			if(formBean.getProperty("disposalDate")!=null && !formBean.getProperty("disposalDate").equals("")){
				disposalColumn=" DISPOSAL_DATE, ";
				disposalColumnValue=" to_date('"+formBean.getProperty("disposalDate")+"','dd/mm/yyyy'),";
			}
			if(formBean.getProperty("disposalType")!=null && !formBean.getProperty("disposalType").equals("")){
				disposalTypeColumn=" DISPOSAL_TYPE, ";
				disposalTypeColumnValue=" to_date('"+formBean.getProperty("disposalType")+"','dd/mm/yyyy'),";
			}
			sql="INSERT INTO olcms_case_details(COURT_TYPE, CASE_TYPE, CASE_NO, CASE_YEAR, OFFICE_FILE_NO,SR_NUMBER,DATE_OF_CASE_FILING, DATE_OF_PETI_RECV_DEPT,DATE_OF_LISTING,SUB_CATEGORY," +
			"DISTRICT_ID,PRIORITY,GOV_AS, ADVACATE_NAME, ADVACATE_CODE,RES_ADVACATE_NAME,RES_ADVACATE_CODE," +
			"CASE_STATUS,"+disposalColumn+" "+disposalTypeColumn+"  DESCRIPTION,  STAGE,REMARKS,JUDGES,CONNECTED_CASE_NUMBER, CASE_ID,UPDATED_DATE," +
			"USER_NAME,"+ddColumn+" "+documentColumn+" SNO) " +
			" VALUES("+formBean.getProperty("courtType")+", "+formBean.getProperty("caseType")+", '"+formBean.getProperty("caseNo")+"',"+formBean.getProperty("caseYear")+", '"+((String)formBean.getProperty("secyFileNo")+"").replaceAll("'", "")+"','"+caseSRNo+"', " +
			" to_date('"+formBean.getProperty("dtPetRecvByDept")+"','dd/mm/yyyy'),to_date('"+formBean.getProperty("dateReg")+"','dd/mm/yyyy'),to_date('"+formBean.getProperty("listingDate")+"','dd/mm/yyyy'),"+formBean.getProperty("subCategory")+"," +
			""+districtId+",'"+formBean.getProperty("priority")+"','"+formBean.getProperty("radiValue")+"','"+formBean.getProperty("petitionerAdvocateName")+"', '"+formBean.getProperty("petitionerAdvocateCode")+"','"+formBean.getProperty("respAdvocate")+"', '"+formBean.getProperty("respAdvocateCode")+"'," +
			""+caseStatus+", "+disposalColumnValue+" "+disposalTypeColumnValue+" '"+ ((String)formBean.getProperty("brfDiscription")+"").replaceAll("'", "").trim()+"', "+formBean.getProperty("stage")+", '"+((String)formBean.getProperty("remarks")+"").replaceAll("'", "").trim()+"', " +
			"'"+((String)formBean.getProperty("judges")+"").replaceAll("'", "")+"','"+((String)formBean.getProperty("connectedCaseNumber")+"").replaceAll("'", "")+"', '"+case_id+"',now()," +
			"'"+request.getSession().getAttribute("userId")+"',"+ddColumnValue+" "+documentColumnValue+" "+caseNo+")";
			System.out.println("sql:: "+sql);
			query_list.add(sql);


			System.out.println("petnoOfRows:: "+petnoOfRows);
			System.out.println("respnoOfRows:: "+respnoOfRows);
			for(int i=1;i<=petnoOfRows;i++)
			{
				sql="INSERT INTO OLCMS_PETITIONER (SNO,CASE_ID, PETITIONER_NAME, PETITIONER_ADDRESS, " +
						"COUNTER_AUTHORITY) " +
						"VALUES( "+caseNo+",'"+case_id+"', '"+((String)formBean.getProperty("petioner_name_"+i)+"").replaceAll("'", "")+"', '"+ ((String)formBean.getProperty("pet_address_"+i)+"").replaceAll("'", "").trim()+"', '"+formBean.getProperty("pet_chekbox_"+i)+"')";
				System.out.println("i="+i+"sql:: "+sql);
				query_list.add(sql);
			}

			for(int i=1;i<=respnoOfRows;i++)
			{

				sql="INSERT INTO OLCMS_RESPONDENT (SNO,CASE_ID, RESPONDENT_NAME, RESPONDENT_ADDRESS, " +
						"COUNTER_AUTHORITY, RESPONDENT_SLNO) " +
						"VALUES("+caseNo+", '"+case_id+"', '"+((String)formBean.getProperty("respondent_name_"+i)+"").replaceAll("'", "")+"', '"+((String)formBean.getProperty("resp_address_"+i)+"").replaceAll("'", "").trim()+"', '"+formBean.getProperty("resp_chekbox_"+i)+"','"+formBean.getProperty("respondent_slno_"+i)+"')";
				System.out.println("i="+i+" respondentsql:: "+sql);
				query_list.add(sql);
			}

			//System.out.println("QUERY LIST SIZE"+query_list.size());
			int[] exe =executeBatch(query_list, con);
			if (exe.length==query_list.size()) {
				con.commit();
				request.setAttribute("msg", " Case Details are Submitted Successfully Case ID::"+case_id);
				formBean.property.clear();
			}else{
				con.rollback();
				request.setAttribute("msg1", " Error in submission ");


			}

		}catch (Exception e) {
			e.printStackTrace();
			con.rollback();
			request.setAttribute("msg1", " Error in submission "+e.getMessage());
		}
		finally{
			try{
				if(con!=null)
					con.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return unspecified(mapping, formBean, request, response);
	}

	public ActionForward getCaseList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WPAffidavitNEWFormBean formBean=(WPAffidavitNEWFormBean)form;
		Connection con=null;
		HttpSession session=request.getSession();
		try{
				con=DatabasePlugin.connect();
				String sql ="";
				if(session.getAttribute("userId")!=null && session.getAttribute("userId").toString().equals("FIN01")){
				 sql ="SELECT CD.SNO,CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CASE_ID,CD.GOV_AS,to_char(DATE_OF_PETI_RECV_DEPT,'dd/mm/yyyy') as DATE_OF_PETI_RECV_DEPT,DESCRIPTION,CD.PRIORITY,CD.CASE_YEAR,OFFICE_FILE_NO,ADVACATE_NAME," +
						"ADVACATE_CODE,to_char(DATE_OF_PETI_RECV_DEPT,'dd/mm/yyyy') as DATE_OF_PETI_RECV_DEPT FROM OLCMS_CASE_DETAILS CD "+ 
						"INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO)"+
						"INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  order by UPDATED_DATE  "; //CASE_ID='"+fb.getCsid()+"'
				}else
				{
					String sqlparam="";
					if(session.getAttribute("roleid")!=null && session.getAttribute("roleid").toString().equals("02"))
						sqlparam="  AND CD.USER_NAME='"+session.getAttribute("userId")+"'";
					else
						sqlparam="  AND CD.USER_NAME IN(SELECT USERID FROM APP_USER_MST_TEST WHERE SECTION_MULTI IN(SELECT SECTION_MULTI FROM APP_USER_MST_TEST WHERE USERID='"+session.getAttribute("userId")+"')) ";
					
					sql ="SELECT CD.SNO,CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CASE_ID,CD.GOV_AS,to_char(DATE_OF_PETI_RECV_DEPT,'dd/mm/yyyy') as DATE_OF_PETI_RECV_DEPT,DESCRIPTION,CD.PRIORITY,CD.CASE_YEAR,OFFICE_FILE_NO,ADVACATE_NAME," +
							"ADVACATE_CODE,to_char(DATE_OF_PETI_RECV_DEPT,'dd/mm/yyyy') as DATE_OF_PETI_RECV_DEPT FROM OLCMS_CASE_DETAILS CD "+ 
							"INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO)"+
							"INNER JOIN COURT_MST CO ON(CD.COURT_TYPE=CO.SNO)  " +
							"WHERE 1=1 "+sqlparam+" order by UPDATED_DATE "; //CASE_ID='"+fb.getCsid()+"'
				}
					
				System.out.println(sql );


				request.setAttribute("CaseDetails", DatabasePlugin.selectQueryMap(sql, con));
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
		formBean.setProperty("dmode", "caseList");
		return mapping.findForward("DEPT_WP_FORM");
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
				sql="SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER WHERE (COURT_ID="+id+" OR COURT_ID='1') order by SNO";
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
	public ActionForward modifyCaseDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WPAffidavitNEWFormBean formBean=(WPAffidavitNEWFormBean)form;
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		String user = null;
		try{
			user = (String)request.getSession().getAttribute("userid");

			if(user != null){
				con=DatabasePlugin.connect();
				st=con.createStatement();
				
				formBean.setProperty("courtList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,COURT_NAME FROM COURT_MST order by SNO", true));
				formBean.setProperty("caseTypesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER order by SNO", true));
				formBean.setProperty("subjectCategorysList",DatabasePlugin.getLabelValueBean(con,"SELECT SUBJECT_ID,SUBJECT_NAME FROM SUBJECTS_MST order by SUBJECT_ID", true));
				formBean.setProperty("prioritiesList",DatabasePlugin.getLabelValueBean(con, "select PRIORITY_ID,PRIORITY_NAME from PRIORITY_MST", true));
				formBean.setProperty("stagesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
				formBean.setProperty("fiftyYears",DatabasePlugin.getLabelValueBean(con, "WITH RECURSIVE nums (n) AS (SELECT TO_CHAR(current_date, 'yyyy')::NUMERIC-50 UNION ALL SELECT n+1 FROM nums WHERE n+1 <= TO_CHAR(current_date, 'yyyy')::NUMERIC) SELECT n  AS year,n  AS year FROM nums order by n desc", true));
				formBean.setProperty("caseStatusList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_STATUS FROM OLCMS_CASE_STATUS_MASTER ORDER BY SNO", true));
				formBean.setProperty("districtList",DatabasePlugin.getLabelValueBean(con, "SELECT DISTRICT_ID,DISTRICT_NAME FROM DISTRICT_MST ORDER BY DISTRICT_NAME", true));
				
				String sql="select COURT_TYPE, CASE_TYPE, CASE_NO, CASE_YEAR,OFFICE_FILE_NO,SR_NUMBER, GOV_AS, ADVACATE_NAME, ADVACATE_CODE,RES_ADVACATE_NAME,RES_ADVACATE_CODE, TO_CHAR(DATE_OF_CASE_FILING,'dd/mm/yyyy') as DATE_OF_CASE_FILING," +
				"TO_CHAR(DATE_OF_PETI_RECV_DEPT,'dd/mm/yyyy') as DATE_OF_PETI_RECV_DEPT,TO_CHAR(DATE_OF_LISTING,'dd/mm/yyyy') as DATE_OF_LISTING,SUB_CATEGORY,DISTRICT_ID, " +
					"DESCRIPTION, PRIORITY,TO_CHAR(DATE_OF_NOTICE,'dd/mm/yyyy') as DATE_OF_NOTICE, TO_CHAR(DISPOSAL_DATE,'dd/mm/yyyy') as DISPOSAL_DATE,DISPOSAL_TYPE, STAGE,JUDGES,CONNECTED_CASE_NUMBER,REMARKS, " +
					"CASE_ID,CASE_STATUS,USER_NAME,CASE_FILE,DAY_OF_CASE_FILING,MM_OF_CASE_FILING,YYYY_OF_CASE_FILING " +
					"from olcms_case_details " +
					"where SNO='"+formBean.getProperty("caseId")+"'";
				System.out.println("sql:: "+sql);
				rs=st.executeQuery(sql);
				if(rs!=null && rs.next())
				{
					formBean.setProperty("courtType",rs.getString("COURT_TYPE"));
					formBean.setProperty("caseType",rs.getString("CASE_TYPE"));
					formBean.setProperty("caseNo",rs.getString("CASE_NO"));
					formBean.setProperty("caseYear",rs.getString("CASE_YEAR"));
					formBean.setProperty("caseSRNo",rs.getString("SR_NUMBER"));
					formBean.setProperty("secyFileNo",rs.getString("OFFICE_FILE_NO"));
					System.out.println("DATE_OF_CASE_FILING="+rs.getString("DATE_OF_CASE_FILING")+"DATE_OF_LISTING"+rs.getString("DATE_OF_LISTING")+"CASE_STATUS="+rs.getString("CASE_STATUS"));
					formBean.setProperty("dtPetRecvByDept",rs.getString("DATE_OF_CASE_FILING"));
					formBean.setProperty("dateReg",rs.getString("DATE_OF_PETI_RECV_DEPT"));
					formBean.setProperty("listingDate",rs.getString("DATE_OF_LISTING"));
					formBean.setProperty("radiValue",rs.getString("GOV_AS"));
					formBean.setProperty("districtId",rs.getString("DISTRICT_ID"));
					formBean.setProperty("petitionerAdvocateName",rs.getString("ADVACATE_NAME"));
					formBean.setProperty("petitionerAdvocateCode",rs.getString("ADVACATE_CODE"));
					formBean.setProperty("respAdvocate",rs.getString("RES_ADVACATE_NAME"));
					formBean.setProperty("respAdvocateCode",rs.getString("RES_ADVACATE_CODE"));
					formBean.setProperty("subCategory",rs.getString("SUB_CATEGORY"));
					formBean.setProperty("brfDiscription",rs.getString("DESCRIPTION"));
					formBean.setProperty("priority",rs.getString("PRIORITY"));
					formBean.setProperty("dtListing",rs.getString("DATE_OF_NOTICE"));
					formBean.setProperty("stage",rs.getString("STAGE"));
					formBean.setProperty("remarks",rs.getString("REMARKS"));
					formBean.setProperty("documentUploaded",rs.getString("CASE_FILE"));
//					formBean.setProperty("dtFiling",rs.getString("DAY_OF_CASE_FILING"));
//					formBean.setProperty("mmFilling",rs.getString("MM_OF_CASE_FILING"));
//					formBean.setProperty("yearFiling",rs.getString("YYYY_OF_CASE_FILING"));
					formBean.setProperty("radiValue",rs.getString("GOV_AS"));
					formBean.setProperty("caseStatus",rs.getString("CASE_STATUS"));
					formBean.setProperty("disposalDate",rs.getString("DISPOSAL_DATE"));
					formBean.setProperty("disposalType",rs.getString("DISPOSAL_TYPE"));
					formBean.setProperty("judges",rs.getString("JUDGES"));
					formBean.setProperty("connectedCaseNumber",rs.getString("CONNECTED_CASE_NUMBER"));
				}
				rs=null;
				int i=1;
				sql="select PETITIONER_NAME, PETITIONER_ADDRESS,COUNTER_AUTHORITY from OLCMS_PETITIONER where SNO='"+formBean.getProperty("caseId")+"'";
				System.out.println("psql:: "+sql);
				rs=st.executeQuery(sql);
				while(rs!=null && rs.next())
				{
					formBean.setProperty("petioner_name_"+i,rs.getString("PETITIONER_NAME"));
					formBean.setProperty("pet_address_"+i,rs.getString("PETITIONER_ADDRESS"));
					formBean.setProperty("pet_chekbox_"+i,rs.getString("COUNTER_AUTHORITY"));
					i++;
				}
				
				request.setAttribute("PETITIONERS", DatabasePlugin.executeQuery(sql, con));
				
				rs=null;
				int j=1;
				sql="select RESPONDENT_NAME, RESPONDENT_ADDRESS,COUNTER_AUTHORITY,RESPONDENT_SLNO from OLCMS_RESPONDENT where SNO='"+formBean.getProperty("caseId")+"'";
				System.out.println("rsql:: "+sql);
				rs=st.executeQuery(sql);
				while(rs!=null && rs.next())
				{
					formBean.setProperty("respondent_name_"+j,rs.getString("RESPONDENT_NAME"));
					if(rs.getString("RESPONDENT_ADDRESS")!=null && !rs.getString("RESPONDENT_ADDRESS").equals("null"))
						formBean.setProperty("resp_address_"+j,rs.getString("RESPONDENT_ADDRESS"));
					else
						formBean.setProperty("resp_address_"+j,"");
					if(rs.getString("COUNTER_AUTHORITY")!=null && !rs.getString("COUNTER_AUTHORITY").equals("null"))
						formBean.setProperty("resp_chekbox_"+j,rs.getString("COUNTER_AUTHORITY"));
					else
						formBean.setProperty("resp_chekbox_"+j,"");
					formBean.setProperty("respondent_slno_"+j,rs.getString("RESPONDENT_SLNO"));
					j++;
				}
				
				request.setAttribute("RESPONDENTS", DatabasePlugin.executeQuery(sql, con));
				

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
		formBean.setProperty("dmode", "modifyCaseList");
		return mapping.findForward("DEPT_WP_FORM");
	}
	
	public ActionForward modifyDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WPAffidavitNEWFormBean formBean=(WPAffidavitNEWFormBean)form;
		String sql=null;
		Connection con=null;
		String castCertificate=null;
		ResultSet rs =null;

		Statement st=null;
		String caseSRNo=null,districtId=null,caseStatus=null;
		try{
			con=DatabasePlugin.connect();
			con.setAutoCommit(false);
			st=con.createStatement();
			formBean.setProperty("courtList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,COURT_NAME FROM COURT_MST order by SNO", true));
			formBean.setProperty("caseTypesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,CASE_SHORT_NAME FROM CASE_TYPE_MASTER order by SNO", true));
			formBean.setProperty("subjectCategorysList",DatabasePlugin.getLabelValueBean(con, "SELECT SUBJECT_ID,SUBJECT_NAME FROM SUBJECTS_MST order by SUBJECT_ID", true));
			formBean.setProperty("prioritiesList",DatabasePlugin.getLabelValueBean(con, "select PRIORITY_ID,PRIORITY_NAME from PRIORITY_MST", true));
			formBean.setProperty("stagesList",DatabasePlugin.getLabelValueBean(con, "SELECT SNO,STAGE_NAME FROM STAGE_MST ORDER BY SNO", true));
			formBean.setProperty("fiftyYears",DatabasePlugin.getLabelValueBean(con, "WITH RECURSIVE nums (n) AS (SELECT TO_CHAR(current_date, 'yyyy')::NUMERIC-50 UNION ALL SELECT n+1 FROM nums WHERE n+1 <= TO_CHAR(current_date, 'yyyy')::NUMERIC) SELECT n  AS year,n  AS year FROM nums order by n desc", true));

			String case_id;
			int petnoOfRows=0;
			int respnoOfRows=0;
			petnoOfRows=Integer.parseInt(formBean.getProperty("petnoOfRows").toString());
			respnoOfRows=Integer.parseInt(formBean.getProperty("respnoOfRows").toString());
			
			sql="select CASE_SHORT_NAME  from CASE_TYPE_MASTER where SNO= "+formBean.getProperty("caseType");
			rs=st.executeQuery(sql);
			if(rs!= null && rs.next())
				case_id=rs.getString("CASE_SHORT_NAME");
			else
				case_id="";
			String filesepartor="/"; 
			castCertificate = "uploads" + filesepartor + "casefile" + filesepartor;
			case_id=case_id+formBean.getProperty("caseNo")+"/"+formBean.getProperty("caseYear");
			FormFile UploadFile=null;
			if(formBean.getProperty("document" ) !=null && !formBean.getProperty("document" ).equals("") && formBean.getProperty("document" ).toString()!=null && !formBean.getProperty("document" ).toString().equals("")){

				String filesBasePath = getServlet().getServletContext().getRealPath("/");

				if(formBean.getProperty("document" )!= null && !formBean.getProperty("document" ).equals(""))
				{
					UploadFile=(FormFile)formBean.getProperty("document" );
					
					if(UploadFile!=null){
						FileUploadUtilities fuu = new FileUploadUtilities();
						castCertificate = fuu.saveFile(UploadFile, castCertificate, "castUpload"+DatabasePlugin.randomTransactionNo());
					}
					/*
					boolean fileflag=DatabasePlugin.checkFileLimitations(UploadFile);
					if(fileflag){
						castCertificate = DatabasePlugin.saveDocument(UploadFile,castCertificate,request.getSession().getAttribute("userId").toString(),"castUpload"+DatabasePlugin.randomTransactionNo(), filesBasePath);
					}*/
					else
					{
						castCertificate="";
						request.setAttribute("msg", "  Case File is not uploaded due to File Size Exceeds the Limit 1MB");
					}
				}
			}else
				castCertificate="";

			ArrayList query_list=new ArrayList();
			long caseNo=getCaseNo(con);
			String yearFilling="0",mmFilling="0",ddFilling="0",ddColumn="",petitionerAdvocateCode="",documentColumn="",respAdvocateCode="",respAdvocateName="",disposalColumn="",disposalTypeColumn="";
			/*if(formBean.getProperty("yearFiling")!=null && !formBean.getProperty("yearFiling").equals(""))
				yearFilling=formBean.getProperty("yearFiling").toString();
			if(formBean.getProperty("mmFilling")!=null && !formBean.getProperty("mmFilling").equals(""))
				mmFilling=formBean.getProperty("mmFilling").toString();
			if(formBean.getProperty("mmFilling")!=null && !formBean.getProperty("mmFilling").equals(""))
				ddFilling=formBean.getProperty("mmFilling").toString();*/
			if(formBean.getProperty("caseSRNo" ) !=null && !formBean.getProperty("caseSRNo" ).equals("") && formBean.getProperty("caseSRNo" ).toString()!=null && !formBean.getProperty("caseSRNo" ).toString().equals("")){
				caseSRNo=formBean.getProperty("caseSRNo" ).toString();
			}
			if(formBean.getProperty("districtId" ) !=null && !formBean.getProperty("districtId" ).equals("") && formBean.getProperty("districtId" ).toString()!=null && !formBean.getProperty("districtId" ).toString().equals("")){
				districtId=formBean.getProperty("districtId" ).toString();
			}else
				districtId="0";
			if(formBean.getProperty("caseStatus" ) !=null && !formBean.getProperty("caseStatus" ).equals("") && formBean.getProperty("caseStatus" ).toString()!=null && !formBean.getProperty("caseStatus" ).toString().equals("")){
				caseStatus=formBean.getProperty("caseStatus" ).toString();
			}else
				caseStatus="0";
			
			if(formBean.getProperty("petitionerAdvocateCode")!=null && !formBean.getProperty("petitionerAdvocateCode").equals(""))
				petitionerAdvocateCode="ADVACATE_CODE='"+formBean.getProperty("petitionerAdvocateCode")+"',";
			
			if(formBean.getProperty("respAdvocate")!=null && !formBean.getProperty("respAdvocate").equals(""))
				respAdvocateName="RES_ADVACATE_NAME='"+formBean.getProperty("respAdvocate")+"',";
			
			if(formBean.getProperty("respAdvocateCode")!=null && !formBean.getProperty("respAdvocateCode").equals(""))
				respAdvocateCode="RES_ADVACATE_CODE='"+formBean.getProperty("respAdvocateCode")+"',";
			
			
			
			if(formBean.getProperty("disposalDate")!=null && !formBean.getProperty("disposalDate").equals(""))
				disposalColumn=" DISPOSAL_DATE=to_date('"+formBean.getProperty("disposalDate")+"','dd/mm/yyyy'),";
			
			if(formBean.getProperty("disposalType")!=null && !formBean.getProperty("disposalType").equals(""))
				disposalTypeColumn="DISPOSAL_TYPE='"+formBean.getProperty("disposalType")+"',";
			
			if(castCertificate!=null && !castCertificate.equals(""))
				documentColumn=" ,CASE_FILE='"+castCertificate+"', ";
			
			if(formBean.getProperty("dtListing")!=null && !formBean.getProperty("dtListing").equals(""))
				ddColumn=" ,DATE_OF_NOTICE=to_date('"+formBean.getProperty("dtListing")+"','dd/mm/yyyy') ";
			
			System.out.println("listingDate="+formBean.getProperty("listingDate"));
			
			sql="UPDATE olcms_case_details SET COURT_TYPE="+formBean.getProperty("courtType")+",CASE_TYPE="+formBean.getProperty("caseType")+",CASE_NO='"+formBean.getProperty("caseNo")+"',CASE_YEAR="+formBean.getProperty("caseYear")+"," +
			"OFFICE_FILE_NO='"+formBean.getProperty("secyFileNo")+"',SR_NUMBER='"+caseSRNo+"',DATE_OF_CASE_FILING=to_date('"+formBean.getProperty("dtPetRecvByDept")+"','dd/mm/yyyy'), DATE_OF_PETI_RECV_DEPT=to_date('"+formBean.getProperty("dateReg")+"','dd/mm/yyyy')," +
			"DATE_OF_LISTING=to_date('"+formBean.getProperty("listingDate")+"','dd/mm/yyyy'),SUB_CATEGORY="+formBean.getProperty("subCategory")+",DISTRICT_ID="+districtId+",PRIORITY='"+formBean.getProperty("priority")+"',GOV_AS='"+formBean.getProperty("radiValue")+"'," +
			"ADVACATE_NAME='"+formBean.getProperty("petitionerAdvocateName")+"',"+petitionerAdvocateCode+" "+respAdvocateName+" "+respAdvocateCode+" CASE_STATUS="+caseStatus+", "+disposalColumn+" "+disposalTypeColumn+" " +
			"DESCRIPTION='"+((String)formBean.getProperty("brfDiscription")+"").replaceAll("'", "")+"',STAGE="+formBean.getProperty("stage")+",REMARKS='"+((String)formBean.getProperty("remarks")+"").replaceAll("'", "")+"'," +
			"JUDGES='"+((String)formBean.getProperty("judges")+"").replaceAll("'", "")+"',CONNECTED_CASE_NUMBER='"+((String)formBean.getProperty("connectedCaseNumber")+"").replaceAll("'", "")+"',CASE_ID='"+case_id+"',UPDATED_DATE=now()," +
			"USER_NAME='"+request.getSession().getAttribute("userId")+"' "+documentColumn+" "+ddColumn+" "+
			"WHERE SNO="+formBean.getProperty("caseId")+"";
			System.out.println("sql:: "+sql);
			query_list.add(sql);


			//System.out.println("petnoOfRows:: "+petnoOfRows);
			//System.out.println("respnoOfRows:: "+respnoOfRows);
			sql = "insert into OLCMS_PETITIONER_BKP  select * from OLCMS_PETITIONER where SNO="+formBean.getProperty("caseId")+"";
			System.out.println("sql"+sql);
			query_list.add(sql);
			
			sql = "insert into OLCMS_RESPONDENT_BKP  select * from OLCMS_RESPONDENT where SNO="+formBean.getProperty("caseId")+"";
			System.out.println("sql"+sql);
			query_list.add(sql);
			
			sql = "DELETE from OLCMS_PETITIONER where SNO="+formBean.getProperty("caseId")+"";
			System.out.println("sql"+sql);
			query_list.add(sql);
			
			sql = "DELETE from OLCMS_RESPONDENT where SNO="+formBean.getProperty("caseId")+"";
			System.out.println("sql"+sql);
			query_list.add(sql);
			
			for(int i=1;i<=petnoOfRows;i++)
			{
				sql="INSERT INTO OLCMS_PETITIONER (SNO,CASE_ID, PETITIONER_NAME, PETITIONER_ADDRESS,COUNTER_AUTHORITY) " +
						"VALUES( "+formBean.getProperty("caseId")+",'"+case_id+"', '"+((String)formBean.getProperty("petioner_name_"+i)+"").replaceAll("'", "")+"', '"+((String)formBean.getProperty("pet_address_"+i)+"").replaceAll("'", "")+"', '"+formBean.getProperty("pet_chekbox_"+i)+"')";
				query_list.add(sql);
			}

			for(int i=1;i<=respnoOfRows;i++)
			{

				sql="INSERT INTO OLCMS_RESPONDENT (SNO,CASE_ID, RESPONDENT_NAME, RESPONDENT_ADDRESS,COUNTER_AUTHORITY, RESPONDENT_SLNO) " +
						"VALUES("+formBean.getProperty("caseId")+", '"+case_id+"', '"+((String)formBean.getProperty("respondent_name_"+i)+"").replaceAll("'", "")+"', '"+((String)formBean.getProperty("resp_address_"+i)+"").replaceAll("'", "")+"', '"+formBean.getProperty("resp_chekbox_"+i)+"','"+formBean.getProperty("respondent_slno_"+i)+"')";
				query_list.add(sql);
			}

			System.out.println("QUERY LIST SIZE"+query_list.size());
			int[] exe =executeBatch(query_list, con);
			if (exe.length==query_list.size()) {
				con.commit();
				request.setAttribute("msg", " Case Details are Updated Successfully Case ID::"+case_id);
				formBean.property.clear();
			}else{
				con.rollback();
				request.setAttribute("msg1", " Error in Updated ");


			}

		}catch (Exception e) {
			e.printStackTrace();
			con.rollback();
			request.setAttribute("msg1", " Error in Updated"+e.getMessage());
		}
		finally{
			try{
				if(con!=null)
					con.close();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return getCaseList(mapping, formBean, request, response);
	}
	public static int[] executeBatch(ArrayList sqls, Connection conn)
	{
		int[] count={0};
		try{
			Statement st;
			st = conn.createStatement();
			for(int i=0; i<sqls.size(); i++)
			{
				//System.out.println("sqls::::"+sqls.get(i));
				st.addBatch((String)sqls.get(i));
			}
			count =st.executeBatch();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public long getCaseNo(Connection con) {
		Statement st=null;
		ResultSet rs=null;
		long caseno=1;
		try {
			st = con.createStatement();
			String sql="select nextval('CASE_NO')";
			rs = st.executeQuery(sql);
			if (rs!=null && rs.next()) {
				caseno = rs.getLong(1);
			}else
				caseno=1;
		} catch (SQLException se) {
			caseno=1;
			se.printStackTrace();
		} finally {
			DatabasePlugin.close(null, st, rs);
		}
		return caseno;
	}
}