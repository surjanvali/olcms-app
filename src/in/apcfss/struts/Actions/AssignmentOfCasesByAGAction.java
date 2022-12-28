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

import com.sun.mail.imap.Utility.Condition;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class AssignmentOfCasesByAGAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
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
			
			cform.setDynaForm("caseNoList", DatabasePlugin.getSelectBox(
					 " select distinct reg_no,reg_no from ecourts_case_data order by reg_no   ", 
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
			
			System.out.println("respondentIds--->"+cform.getDynaForm("caseTypeId1")+cform.getDynaForm("regYear1")+cform.getDynaForm("caseNumber1"));
			String selectedCaseIds ="";
			int selectedCaseIdss =Integer.parseInt((String) cform.getDynaForm("respondentIds"));
			String caseType = "";
			String newCaseIdss="";
			System.out.println("selectedCaseIdss--->"+selectedCaseIdss);
			System.out.println("length--->"+selectedCaseIdss);
			
			String newCaseIdss_final="";
			String cino="";
			String cinos="";
			System.out.println("newCaseIdss--->"+newCaseIdss);
			
			String emp_email = DatabasePlugin.selectString("select emp_email from ag_office_employees   where emp_id='"+CommonModels.checkStringObject(cform.getDynaForm("emp_id"))+"'", con);

			FileUploadUtilities fuu = new FileUploadUtilities();
			FormFile myDoc;

			myDoc = cform.getChangeLetter();

			System.out.println("myDoc---"+myDoc);
			String filePath="uploads/Remarks_AG_file/";
			String newFileName="Remarks_AG_file_"+CommonModels.randomTransactionNo();
			String Remarks_AG_file = fuu.saveFile(myDoc, filePath, newFileName);

			System.out.println("pdfFile--"+Remarks_AG_file);
			
			for(int i=1;i<=selectedCaseIdss;i++) {
				//String newCaseIdss_final= newCaseIdss.replace(newCaseIdss.substring(newCaseIdss.length()-1), "");
			
				newCaseIdss=" '"+cform.getDynaForm("caseTypeId"+i)+"/"+cform.getDynaForm("regYear"+i)+"/"+cform.getDynaForm("caseNumber"+i)+"' "; 
				 //newCaseIdss_final= newCaseIdss.substring(0, newCaseIdss.lastIndexOf(","));
				 
				 sql="select cino,cino from ecourts_case_data a where   a.type_name_reg||'/'||reg_year||'/'||reg_no in ("+newCaseIdss+")";
				 
				 System.out.println("sql--"+sql);
				 List<Map<String, String>> data = DatabasePlugin.executeQuery(sql, con);
				  cino=data.get(0).get("cino").toString();
				//cino=ackData.get("cino").toString());
				
				System.out.println("cino--"+cino);
				
				if(!cino.equals("") && !CommonModels.checkStringObject(cform.getDynaForm("emp_id")).equals("0")) {
					
				
				sql="insert into ecourts_case_activities_agolcms (cino , action_type , inserted_by , inserted_ip, assigned_to , remarks,uploaded_doc_path ) "
						+ "values ('" + cino + "','CASE ASSSIGNED TO AG OFFICE','"+userId+"', '"+request.getRemoteAddr()+"', '"+emp_email+"', '"+CommonModels.checkStringObject(cform.getDynaForm("caseRemarks"))+"','"+Remarks_AG_file+"')";
				
				a+=DatabasePlugin.executeUpdate(sql, con);
				System.out.println(a+":ACTIVITIES SQL:"+sql);
				
				}
				cinos+= " '"+cino+"', ";
				}
			
			
			System.out.println("cinos--"+cinos);
			
				sql = "update ecourts_case_data set agolcms_status='19', agolcms_assigned_to='"+emp_email+"' where cino  in ("+cinos.substring(0, cinos.lastIndexOf(","))+")" ;
				
				System.out.println("UPDATE SQL:"+sql);
				
				a+=DatabasePlugin.executeUpdate(sql, con);
				con.commit();
				request.setAttribute("successMsg", "Case/Cases successfully moved to selected Employee.");
				
			
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Error in assigning Case to Department/HOD. Kindly try again.");
			e.printStackTrace();
		} finally {
			
			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");
			
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
		//return getCasesList(mapping, cform, request, response);
	}
	
}