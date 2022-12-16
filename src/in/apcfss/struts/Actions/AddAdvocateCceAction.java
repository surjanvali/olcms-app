package in.apcfss.struts.Actions;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.Itext_pdf_setting;
import plugins.DatabasePlugin;

public class AddAdvocateCceAction extends DispatchAction {

	
	
	public static String generateNewAckNo() {
		String ackNoNew = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmissms");
		System.out.println("" + sdf.format(new Date()));
		ackNoNew = sdf.format(new Date());
		return ackNoNew;
	}

	public static void main(String[] args) {
		// generateAckBarCodePdf("REV01-L1820220613115422600", null);
		//generateAckBarCodePdf128("FIN010000672", null);
		//Date todaysDate = new Date();
		//DateFormat df = new SimpleDateFormat("dd-Mon-yyyy HH:mm:ss:am");
		//String testDateString = df.format(todaysDate);
		
	}
	

	public ActionForward saveDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		int advocateCode = 0;
		String advocateName="";
		String hcAckNo=null;
		String ackNo = null;//generateNewAckNo();
		int respondentIds=0;
		String 	deptdist=null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}

			if (isTokenValid(request)) {

				con = DatabasePlugin.connect();
				
				int slno=0;
				
				advocateCode = CommonModels.checkIntObject(cform.getDynaForm("advocateCode"));
				advocateName = CommonModels.checkStringObject(cform.getDynaForm("advocateName"));
				
				
				String sql1="select count(*) from ecourts_mst_advocate_ccs where advocate_code=?";
				ps = con.prepareStatement(sql1);
				ps.setInt(1, advocateCode);
				rs=ps.executeQuery();
				int ex_adv_code_count=0;
				if(rs!=null && rs.next())
				{
					ex_adv_code_count=rs.getInt(1);
				}
				if(ex_adv_code_count==0)
				{
				
				sql1="select max(slno)+1 from ecourts_mst_advocate_ccs limit 1";
				ps = con.prepareStatement(sql1);
				rs=ps.executeQuery();
				
				 
				
				
				if(rs!=null && rs.next())
				{
					slno=rs.getInt(1);
				}
				//System.out.println("slno======>"+slno);
				
				if (advocateCode != 0  && advocateName != null && !advocateName.contentEquals("")) {
					ps.close();
					int i = 0;
					sql = "insert into ecourts_mst_advocate_ccs ( slno,advocate_code, advocate_name)"
							+ "values (?,?,upper(?))";
					System.out.println("sql--"+sql);
					ps = con.prepareStatement(sql);
					ps.setInt(++i, slno);
					ps.setInt(++i, advocateCode);
					ps.setString(++i, advocateName);
					System.out.println("ps..........."+ps);
					
					int a = ps.executeUpdate();
					
					
					
					if (a > 0) {
						request.setAttribute("successMsg","Advocate cce details saved successfully");
						
							
						}else {
							request.setAttribute("errorMsg", "Invalid Advocate cce details. Kindly try again.");
						}
						
					} else
						request.setAttribute("errorMsg","Error in saving Invalid Advocate cce details. Kindly try again with valid data.");
						request.setAttribute("saveAction", "INSERT");
				} 
				else {
					request.setAttribute("errorMsg", "Your Advocate Code already exists.Kindly try again.");
				}
			}
			 else {
				request.setAttribute("errorMsg",
						"Error: Submitting duplicate Advocate cce details. Kindly try again with valid data.");
			}
			
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg",
					"Exception Occurred while saving Advocate cce details. Kindly try again with valid data.");
			request.removeAttribute("successMsg");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return unspecified(mapping, cform, request, response);
	}
	
	
	 @Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			System.out.println("" + sdf.format(new Date()));
			cform.setDynaForm("ackDate", sdf.format(new Date()));
			
			saveToken(request);
			//DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
	 
	
}