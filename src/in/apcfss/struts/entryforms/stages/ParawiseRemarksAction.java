package entryforms.stages;

import java.awt.Color;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import common.plugin.DatabasePlugin;

public class ParawiseRemarksAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ParawiseRemarksFormBean fb = (ParawiseRemarksFormBean) form;
		String target = new String("PR_FORM");
		Connection con=null;
		String sql="";
		try{
			con=DatabasePlugin.connect();
			HttpSession session = request.getSession();
			fb.reset(mapping, request);
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
			fb.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));
			fb.setProperty("gps",DatabasePlugin.getLabelValueBean(con, "select gp_no,gp_name from gpoffice_mst order by gp_name", true));
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
		String target = new String("PR_FORM");
		String sql = null;
		Connection con=null;
		ResultSet rs =null;
		Statement st=null;
		try{
			System.out.println("in getCaseDetails");
			HttpSession session = request.getSession();
			con=DatabasePlugin.connect();
			st=con.createStatement();
			ParawiseRemarksFormBean prform = (ParawiseRemarksFormBean) form;
			//prform.reset(mapping, request);
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
			prform.setProperty("caseList",DatabasePlugin.getLabelValueBean(con, sql, true));
			prform.setProperty("gps",DatabasePlugin.getLabelValueBean(con, "select gp_no,gp_name from gpoffice_mst order by gp_name", true));
			ArrayList values = new ArrayList();
			sql="SELECT CO.COURT_NAME,CM.CASE_SHORT_NAME,CASE_NO,CD.CASE_YEAR,PR.GP_ID,PR.CASE_FILE,GP.gp_name," +
					"TO_CHAR(PR.DATE_OF_APPROVALBYGP,'DD/MM/YYYY') AS DATE_OF_APPROVALBYGP,TO_CHAR(PR.DATE_REQUEST_OF_PR_RECV_DEPT,'DD/MM/YYYY') AS DATE_REQUEST_OF_PR_RECV_DEPT,TO_CHAR(PR.DATE_OF_PR_SENTTOGP,'DD/MM/YYYY') AS PR_SUBMITTED_DATE " +
					" FROM OLCMS_CASE_DETAILS CD  " +
					" LEFT JOIN PARAWISEREMARKS PR ON(PR.CASE_ID=CD.SNO) " +
					" INNER JOIN CASE_TYPE_MASTER CM ON(CD.CASE_TYPE=CM.SNO) " +
					"INNER JOIN  COURT_MST CO ON(CD.COURT_TYPE=CO.SNO) LEFT JOIN  GPOFFICE_MST GP ON(GP.GP_NO::VARCHAR=PR.GP_ID)    " +
					" WHERE CD.SNO='"+prform.getProperty("caseId").toString().trim()+"'";
			System.out.println(sql);
			rs=st.executeQuery(sql);
			if(rs!= null && rs.next()){
				prform.setProperty("courtName", rs.getString("COURT_NAME"));
				prform.setProperty("caseType", rs.getString("CASE_SHORT_NAME"));
				prform.setProperty("caseNo", rs.getString("CASE_NO"));
				prform.setProperty("caseYear", rs.getString("CASE_YEAR"));
				prform.setProperty("caseNo1", rs.getString("CASE_NO"));
				prform.setProperty("caseYear1", rs.getString("CASE_YEAR"));
				prform.setProperty("documentUploaded",rs.getString("CASE_FILE"));
				request.setAttribute("caseIdDetails", "caseIdDetails");
				prform.setRelatedGp( rs.getString("GP_ID"));
				prform.setProperty("dtPRSentToGP",  rs.getString("PR_SUBMITTED_DATE"));
				prform.setProperty("dtPRApprovedToGP",  rs.getString("DATE_OF_APPROVALBYGP"));
				prform.setProperty("dtPRReceiptToGP",  rs.getString("DATE_REQUEST_OF_PR_RECV_DEPT"));
				
				sql="SELECT USERNAME,OFFICER_REMARKS,INSERTED_BY,to_char(INSERTED_TIME,'DD/MM/YYYY') AS INSERTED_TIME,TO_CHAR(PR.DATE_OF_PR_SENTTOGP,'DD/MM/YYYY') AS PR_SUBMITTED_DATE," +
				"TO_CHAR(PR.DATE_OF_APPROVALBYGP,'DD/MM/YYYY') AS DATE_OF_APPROVALBYGP,TO_CHAR(PR.DATE_REQUEST_OF_PR_RECV_DEPT,'DD/MM/YYYY') AS DATE_REQUEST_OF_PR_RECV_DEPT FROM PARAWISEREMARKS PR " +
						" JOIN APP_USER_MST UM ON (UM.USERID=PR.INSERTED_BY) " +
						" WHERE CASE_ID='"+prform.getProperty("caseId").toString().trim()+"'  order by INSERTED_TIME ";
				System.out.println(sql);
				values=DatabasePlugin.selectQueryMap(sql, con);
				request.setAttribute("paraWiseRemarks", DatabasePlugin.selectQueryMap(sql, con));
				
				String pdfFile=getFileName(prform.getProperty("caseId").toString(), session.getAttribute("userId").toString(), rs.getString("COURT_NAME"),rs.getString("CASE_SHORT_NAME"),rs.getString("CASE_NO"),rs.getString("CASE_YEAR"),rs.getString("gp_name"),values);
				prform.setProperty("paramFile", pdfFile);
			}else
				request.setAttribute("caseIdDetailsNotFound", "Case Details Not found");



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


	public ActionForward InsertDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String sql = null;
		Connection con=null;
		ResultSet rs =null;
		Statement st=null;
		String castCertificate=null,documentColumn="",documentColumnValue="";
		try{
			System.out.println("in getCaseDetails");
			HttpSession session = request.getSession();

			con=DatabasePlugin.connect();
			st=con.createStatement();
			ParawiseRemarksFormBean prform = (ParawiseRemarksFormBean) form;
			//prform.reset(mapping, request);
			String caseid =prform.getProperty("caseId").toString();
			String case_id=prform.getProperty("caseId").toString();
			String filesepartor="/"; 
			castCertificate = "uploads" + filesepartor + "parawiseremarks" + filesepartor;
			case_id=case_id+prform.getProperty("caseNo1")+"/"+prform.getProperty("caseYear1");
			FormFile UploadFile=null;
			if(prform.getProperty("document" ) !=null && !prform.getProperty("document" ).equals("") && prform.getProperty("document" ).toString()!=null && !prform.getProperty("document" ).toString().equals("")){

				String filesBasePath = getServlet().getServletContext().getRealPath("/");

				if(prform.getProperty("document" )!= null && !prform.getProperty("document" ).equals(""))
				{
					UploadFile=(FormFile)prform.getProperty("document" );
					boolean fileflag=DatabasePlugin.checkFileLimitations(UploadFile);
					if(fileflag){
						castCertificate = DatabasePlugin.saveDocument(UploadFile,castCertificate,request.getSession().getAttribute("userId").toString(),"parawise"+DatabasePlugin.randomTransactionNo(), filesBasePath);
					}
					else
					{
						castCertificate="";
						request.setAttribute("msg", "  Parawise Remarks Document is not uploaded due to File Size Exceeds the Limit 1MB");
					}
				}
			}else
				castCertificate="";
			
			if(castCertificate!=null && !castCertificate.equals("")){
				documentColumn=" CASE_FILE, ";
				documentColumnValue="'"+castCertificate+"'";
			}
			
			sql="INSERT INTO PARAWISEREMARKS(CASE_ID,DATE_OF_PR_SENTTOGP, DATE_REQUEST_OF_PR_RECV_DEPT,DATE_OF_APPROVALBYGP, " +
			"GP_ID,DATE_OF_PR_RECIVEDBYGP, OFFICER_REMARKS,  INSERTED_BY,"+documentColumn+" INSERTED_TIME) VALUES " +
			"('"+caseid+"', to_date('"+prform.getProperty("dtPRSentToGP")+"','dd/mm/yyyy'),  to_date('"+prform.getProperty("dtPRReceiptToGP")+"','dd/mm/yyyy'),  to_date('"+prform.getProperty("dtPRApprovedToGP")+"','dd/mm/yyyy'),'"+prform.getRelatedGp()+"',now(), '"+((String)prform.getParaRemarks()+"").replaceAll("'", "")+"', " +
			"'"+ session.getAttribute("userId").toString()+"',"+documentColumnValue+" now())";

			System.out.println(sql);
			int i=st.executeUpdate(sql);
			System.out.println("inserted falg detailas:::"+i);

			if(i>0){
				request.setAttribute("msg", "Parawise Remarks submitted Sucessfully");
			}else
				request.setAttribute("msg1", "Error in Submission");

			prform.setParaRemarks("");

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
		return getCaseDetails(mapping, form, request, response);
	}
	
	private String getFileName(String caseId, String userId, String courtName, String caseShortName, String caseNo,String caseYear,String gpNAme,ArrayList promotionDetails)
	{

		System.out.println("getFileName()");
		String fileName="";
		int subTableIndex = 0;
		Document document = null;
		PdfWriter writer = null;
		try 
		{
			String contextName="olcmsfin";
			String filesepartor=System.getProperty("file.separator");
			String contextPath=System.getProperty("catalina.base")+filesepartor+"webapps"+filesepartor+contextName+filesepartor;
			String SERVICE_RULES_PDF=contextPath+"uploads"+filesepartor;
			
			PdfPTable table = null;
			PdfPCell col = null;
			float[] f=null;
			Paragraph para = null;
			document = new Document(PageSize.A4);
			writer = PdfWriter.getInstance(document, new FileOutputStream(SERVICE_RULES_PDF+caseId+"_finalsubmit" + ".pdf"));
			

			document.open();
			table = new PdfPTable(4);

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			table.setTotalWidth(50);

			f=new float[]{0.25f, 0.25f, 0.25f, 0.25f};
			table.setWidths(f);
			table.setWidthPercentage(95);


			col = new PdfPCell(new Phrase("\n\n\n Parawise Remarks for the Case number : "+caseNo+" \n \n", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD,new Color(0, 0, 0))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_CENTER);
			col.setBorder(Rectangle.BOX);
			col.setBorderColor(new Color(0xFF,0xFF,0xFF));
			col.setBackgroundColor(new Color(0xFF,0xFF,0xFF));
			col.setColspan(4);
			table.addCell(col);
			
			col = new PdfPCell(new Phrase("Court Name", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			col.setBackgroundColor(new Color(30,28,28));
			table.addCell(col);
			
			col = new PdfPCell(new Phrase(courtName+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			table.addCell(col);
			
			col = new PdfPCell(new Phrase("Nature of Petition", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
			col.setMinimumHeight(8f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			col.setBackgroundColor(new Color(30,28,28));
			table.addCell(col);
			
			col = new PdfPCell(new Phrase(caseShortName+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			table.addCell(col);
			
			col = new PdfPCell(new Phrase("Case Number", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			col.setBackgroundColor(new Color(30,28,28));
			table.addCell(col);
			
			col = new PdfPCell(new Phrase(caseNo+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			table.addCell(col);
			
			col = new PdfPCell(new Phrase("Case Year", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
			col.setMinimumHeight(8f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			col.setBackgroundColor(new Color(30,28,28));
			table.addCell(col);
			
			col = new PdfPCell(new Phrase(caseYear+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
			col.setMinimumHeight(10f);
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
			col.setBorder(Rectangle.BOX);
			table.addCell(col);
			
			
			
			if(promotionDetails!=null && !promotionDetails.isEmpty() && promotionDetails.size()>0)
			{
				int row = 1;
				Map m = null;
				
				for(Object o : promotionDetails)
				{
					m = (HashMap) o;
					
					System.out.println("hiii for loop row="+row);
					
					
					col = new PdfPCell(new Phrase("Corresponding GP's", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					col.setBackgroundColor(new Color(30,28,28));
					table.addCell(col);
					
					col = new PdfPCell(new Phrase(gpNAme+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					table.addCell(col);
					
					col = new PdfPCell(new Phrase("Date of Submission of Parawise Remarks to GP/SC", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
					col.setMinimumHeight(8f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					col.setBackgroundColor(new Color(30,28,28));
					table.addCell(col);
					
					col = new PdfPCell(new Phrase(m.get("PR_SUBMITTED_DATE")+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					table.addCell(col);
					
					col = new PdfPCell(new Phrase("Date of Approval of Parawise Remarks by GP/SC", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					col.setBackgroundColor(new Color(30,28,28));
					table.addCell(col);
					
					col = new PdfPCell(new Phrase(m.get("DATE_OF_APPROVALBYGP")+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					table.addCell(col);
					
					col = new PdfPCell(new Phrase("Date of Receipt of Approved Parawise Remarks from GP/SC", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0xFF, 0xFF, 0xFF))));
					col.setMinimumHeight(8f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					col.setBackgroundColor(new Color(30,28,28));
					table.addCell(col);
					
					col = new PdfPCell(new Phrase(m.get("DATE_REQUEST_OF_PR_RECV_DEPT")+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.BOX);
					table.addCell(col);
					
					col = new PdfPCell(new Phrase("\n Parawise Remarks submitted: \n", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL,new Color(0, 0, 0))));
					col.setMinimumHeight(10f);
					col.setHorizontalAlignment(Element.ALIGN_LEFT);
					col.setBorder(Rectangle.RIGHT);
					col.setBorderColor(new Color(0xFF,0xFF,0xFF));
					col.setBackgroundColor(new Color(0xFF,0xFF,0xFF));
					col.setColspan(4);
					table.addCell(col);
					
					if(m.get("OFFICER_REMARKS")!=null && !m.get("OFFICER_REMARKS").equals("null")){
						table.addCell(setColValue1(m.get("OFFICER_REMARKS")+"", 4, 0,"left"));
					}else
					{
						table.addCell(setColValue1("", 4, 0,"left"));
					}
					
					if(m.get("USERNAME")!=null && !m.get("USERNAME").equals("null")){
						table.addCell(setColValue1(m.get("USERNAME")+" \t\t"+" submitted by "+m.get("INSERTED_TIME"), 4, 0,"right"));
					}else
					{
						table.addCell(setColValue1("", 4, 0,"right"));
					}
					row++;
				}
			}
			
			

			document.add(table);

			fileName=caseId+"_finalsubmit" + ".pdf";
		}
		catch (Exception e) 
		{
			if(document!=null)
				document.close();
			if(writer!=null)
				writer.close();
			
			e.printStackTrace();
		}
		finally
		{
			
			if(document!=null)
				document.close();
			if(writer!=null)
				writer.close();
		}
		return fileName;
	}
	public  PdfPCell setColValue(String s, int colspan, int rowSpan)
	{
		PdfPCell col =new PdfPCell(new Phrase(s+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
		col.setMinimumHeight(10f);
		col.setHorizontalAlignment(Element.ALIGN_LEFT);
		col.setBorder(Rectangle.BOX);
		if(colspan!=0)
			col.setColspan(colspan);
		if(rowSpan!=0)
			col.setRowspan(rowSpan);
		return col;
	}
	
	public  PdfPCell setColValue1(String s, int colspan, int rowSpan,String align)
	{
		PdfPCell col =new PdfPCell(new Phrase(s+"", FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL,new Color(0, 0, 0))));
		col.setMinimumHeight(10f);
		if(align.equals("left"))
			col.setHorizontalAlignment(Element.ALIGN_LEFT);
		else
			col.setHorizontalAlignment(Element.ALIGN_RIGHT);
		col.setBorder(Rectangle.RIGHT);
		col.setBorder(Rectangle.LEFT);
		if(colspan!=0)
			col.setColspan(colspan);
		if(rowSpan!=0)
			col.setRowspan(rowSpan);
		return col;
	}
}

