package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;

public class HCCaseDistWiseAbstractNewCasesAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null,condition="";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			
			  else if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
					condition=" and ( a.dept_code='"+session.getAttribute("dept_code")+"')";
				else if(roleId.equals("2")){
					condition=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
				}
			else //if(roleId.equals("3") || roleId.equals("4"))
			{
				condition="";
			con = DatabasePlugin.connect();
				
				sql="select dm.district_id,dm.district_name,count(*) as total_cases,"
						+ "sum(case when ack_file_path is not null and length(ack_file_path)>10 then 1 else 0 end) as olcms_uploads, "
						+ "sum(case when petition_document is not null and length(petition_document)>10 then 1 else 0 end) as petition_uploaded , "
						+ "sum(case when a.ecourts_case_status='Closed' then 1 else 0 end) as closed_cases , "
						+ "sum(case when a.ecourts_case_status='Pending' and counter_filed_document is not null and length(counter_filed_document)>10  then 1 else 0 end) as counter_uploaded,"
						+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is not null and length(pwr_uploaded_copy)>10  then 1 else 0 end) as pwrcounter_uploaded ,"
						+ " sum(case when counter_approved_gp='Yes' then 1 else 0 end) as counter_approved_gp "
						+ "from ecourts_gpo_ack_depts a  inner join ecourts_gpo_ack_dtls egad on (a.ack_no=egad.ack_no)"
						+ "left join ecourts_olcms_case_details b on (b.cino=a.ack_no)"
						+ "inner join district_mst dm on (dm.district_id=a.dist_id)"
						+ "inner join dept_new dn on (dn.dept_code=a.dept_code)  where 1=1 "+condition+" "
						+ "group by dm.district_id,dm.district_name ";
						
			request.setAttribute("HEADING", "Dist. Wise New Case processing Abstract Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("secdeptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

	
	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HCCaseStatusAbstractReport..............................................................................getCasesList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null,condition="";
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			
			heading="Cases List for "+deptName;
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("CLOSED")){
						sqlCondition= " and coalesce(a.ecourts_case_status,'')='Closed' ";
						heading+=" Closed Cases List";
					}
				if(caseStatus.equals("PET")) {
					sqlCondition=" and eocd.petition_document is not null and length(eocd.petition_document)>10 ";
					heading+=" Petition Documets Uploaded";
				}
				if(caseStatus.equals("COUNTERUPLOADED")) {
					sqlCondition=" and eocd.counter_filed_document is not null  and length(eocd.counter_filed_document)>10  ";
					heading+=" Counter Uploaded Cases";
				}
				if(caseStatus.equals("PWRUPLOADED")) {
					sqlCondition= " and eocd.pwr_uploaded_copy is not null  and length(eocd.pwr_uploaded_copy)>10 ";
					heading+=" Parawise Remarks Uploaded Cases List";
				}
				if(caseStatus.equals("GPCOUNTER")) {
					sqlCondition=" and eocd.counter_approved_gp='Yes' ";
					heading+=" and Counters Filed";
				}
				if(caseStatus.equals("SCANNEDDOC")) {
					sqlCondition=" and ack_file_path is not null and length(ack_file_path)>10 ";
					heading+=" and Documents Scanned at APOLCMS Cell, High Court";
				}
			}
			
			if(actionType.equals("SDWISE")) {
			}
			else if(actionType.equals("HODWISE")) {
			}
			
			if(roleId.equals("2")){
				sqlCondition=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}else if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
			{
				sqlCondition = " and (a.dept_code='" + deptCode + "') " ;
			}
			
			
			System.err.println("--"+request.getParameter("distid"));
			
			//if(!request.getParameter("distid").toString().equals("")) {
				if(cform.getDynaForm("distid") != null
						&& !CommonModels.checkStringObject(cform.getDynaForm("distid")).contentEquals("")) {
					condition=" and a.dist_id='"+cform.getDynaForm("distid")+"'";
			}
			
			
			sql =  "select a.ack_no,advocatename,advocateccno,cm.case_short_name,maincaseno,inserted_time,petitioner_name,getack_dept_desc(a.ack_no::text) as dept_descs,	"
					+ " services_flag,reg_year,reg_no,mode_filing,case_category,dm.district_name,coalesce(e.hc_ack_no,'-') as hc_ack_no,barcode_file_path,  coalesce(trim(e.ack_file_path),'-') as scanned_document_path1,'' as orderpaths "
					+ " from  ecourts_gpo_ack_depts  a  inner join ecourts_gpo_ack_dtls e on (a.ack_no=e.ack_no)  inner join dept_new d on (a.dept_code=d.dept_code) inner join district_mst dm on (e.distid=dm.district_id) "
					+ " inner join case_type_master cm on (e.casetype=cm.sno::text or e.casetype=cm.case_short_name) "
					+ "inner join ecourts_olcms_case_details eocd on (a.ack_no=eocd.cino) "
					+ "  where e.ack_type='NEW'   "+sqlCondition+"  "+condition+"   ";
			
			
			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}
}