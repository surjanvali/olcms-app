package in.apcfss.struts.Actions;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sun.mail.imap.Utility.Condition;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class GeneralRemarksAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GeneralRemarksAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			cform.setDynaForm("purposeList",
					DatabasePlugin
							.getSelectBox(
									"select purpose_name,purpose_name from apolcms.ecourts_case_data where dept_code='"
											+ session.getAttribute("dept_code") + "' group by purpose_name order by 1",
									con));
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select case_short_name,case_full_name from case_type_master order by sno", con));
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			
			cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select trim(dist_name),trim(dist_name) from apolcms.ecourts_case_data where trim(dist_name)!='null' group by trim(dist_name) order by 1",
					con));
			cform.setDynaForm("AGOFFICELIST", DatabasePlugin.getSelectBox(
					"select emp_id,emp_name from  ag_office_employees where emp_id not in ('1','9','10') order by emp_id",
					con));
			
			//cform.setDynaForm("regYear", "2022");
			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);
			cform.setDynaForm("ShowDefault", "ShowDefault");
			
			request.setAttribute("SHOWMESG", "SHOWMESG");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
		//return getCasesList(mapping, cform, request, response);
	}
	
	public ActionForward assignCase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;int a=0;
		try {
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// Assing the case
			// Create User Login details for the Employee if not Exist
			System.out.println("in assignCase --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = (String)request.getSession().getAttribute("userid");
			
			//System.out.println("respondentIds--->"+cform.getDynaForm("caseTypeId1")+cform.getDynaForm("regYear1")+cform.getDynaForm("caseNumber1"));
			//String selectedCaseIds ="";
			//int selectedCaseIdss =Integer.parseInt((String) cform.getDynaForm("respondentIds"));
			//String caseType = "";
		//	String newCaseIdss="";
			//System.out.println("selectedCaseIdss--->"+selectedCaseIdss);
			//System.out.println("length--->"+selectedCaseIdss);
			
			String general_remarks=cform.getDynaForm("caseRemarks").toString();
			//System.out.println("empid=======>"+cform.getDynaForm("respondentIds"));
			String emp_id[]=cform.getDynaForm("respondentIds").toString().split(",");
			
			int lenght_empid=emp_id.length;
			
			String newCaseIdss_final="";
			String cino="";
			String cinos="";
			//System.out.println("newCaseIdss--->"+newCaseIdss);
			
			//String emp_email = DatabasePlugin.selectString("select emp_email from ag_office_employees   where emp_id='"+CommonModels.checkStringObject(cform.getDynaForm("emp_id"))+"'", con);
               sql="SELECT nextval('general_ramarks_serial_no')";
               int srno=Integer.parseInt(DatabasePlugin.getStringValue(sql, con));
               
               sql="INSERT INTO apolcms.ecourts_general_remarks(sn_id, remarks, user_id, ip_address) "
               		+ "    VALUES(?, ?, ?, '"+request.getRemoteAddr()+"')";
                int n=0;
                ps = con.prepareStatement(sql);
				ps.setInt(++n, srno);
				ps.setString(++n, general_remarks);
				ps.setString(++n, userId);
				
				int b = ps.executeUpdate();
				
				/*
				 * if (b > 0) {
				 * request.setAttribute("successMsg","Advocate cce details saved successfully");
				 * } else { request.setAttribute("errorMsg",
				 * "Invalid Advocate cce details. Kindly try again."); }
				 */
				//System.out.println("lenght_empid=====>"+lenght_empid);
			if(b>0)
			{
				ps.close();
				int c =0;
				for(int i=0;i<lenght_empid;i++) 
				{
				
				
					if(!emp_id[i].equals("") && !CommonModels.checkStringObject(emp_id[i]).equals("0")) 
						{
							ps.close();
							//System.out.println("i===>"+i+"====>"+Integer.parseInt(emp_id[i]));
							sql="INSERT INTO apolcms.ecourts_general_remarks_officers(sn_id, officer) "
							  + " VALUES(?, ?)";
							
							n=0;
			                ps = con.prepareStatement(sql);
							ps.setInt(++n, srno);
							ps.setInt(++n, Integer.parseInt(emp_id[i]));
							c+= ps.executeUpdate();
						}
				}
				
			
				
			    if(c>0)
			    {
					con.commit();
					request.setAttribute("successMsg", "General Remarks successfully Added.");
			    }else {
			    	con.rollback();
			    	request.setAttribute("successMsg", "General Remarks Not Added.");
			    }
			}	
			
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in General Remarks. Kindly try again.");
			e.printStackTrace();
		} finally {
			
			cform.setDynaForm("caseRemarks","");
			cform.setDynaForm("emp_id", "");
			DatabasePlugin.close(con, ps, null);
		}
		//return mapping.findForward("success");
		//return getCasesList(mapping, cform, request, response);
		return unspecified(mapping, cform, request, response);
	}
	
}