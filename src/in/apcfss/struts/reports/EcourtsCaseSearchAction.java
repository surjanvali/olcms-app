package in.apcfss.struts.reports;

import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;


import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import plugins.DatabasePlugin;

public class EcourtsCaseSearchAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction....................1..........................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="", userid="";
		
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			
			con = DatabasePlugin.connect();

			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			//cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( "select ack_no,ack_no from ecourts_gpo_ack_dtls where 1=1  order by ack_no", con));
			cform.setDynaForm("oldNewType", "New");
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		 return mapping.findForward("success");
	}

	public ActionForward getCasesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
			return mapping.findForward("Logout");
		}
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="", userid="";
		try {
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			con = DatabasePlugin.connect();
			
			
			String	cino=cform.getDynaForm("caseType1").toString()+"/"+cform.getDynaForm("mainCaseNo").toString()+"/"+cform.getDynaForm("regYear1").toString();
				
				if (cform.getDynaForm("caseType1") != null && !cform.getDynaForm("caseType1").toString().contentEquals("")
						&& !cform.getDynaForm("caseType1").toString().contentEquals("0")) {
					sqlCondition += " and type_name_reg='" + cform.getDynaForm("caseType1").toString().trim() + "' ";
				}
				
				if (cform.getDynaForm("mainCaseNo") != null && !cform.getDynaForm("mainCaseNo").toString().contentEquals("")
						&& !cform.getDynaForm("mainCaseNo").toString().contentEquals("0")) {
					sqlCondition += " and a.reg_no='" + cform.getDynaForm("mainCaseNo").toString().trim() + "' ";
				}
				
				if (cform.getDynaForm("regYear1") != null && !cform.getDynaForm("regYear1").toString().contentEquals("")
						&& !cform.getDynaForm("regYear1").toString().contentEquals("0")) {
					sqlCondition += " and a.reg_year='" + cform.getDynaForm("regYear1").toString().trim() + "' ";
				}
				
			
				
				sql = "select a.*, "
						+ " nda.fullname_en as fullname, nda.designation_name_en as designation, nda.post_name_en as post_name, nda.email, nda.mobile1 as mobile,dim.district_name , "
						+ " 'Pending at '||ecs.status_description||'' as current_status, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths,"
						+ " case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full, ra.address from ecourts_case_data a "
						
						+ " left join nic_prayer_data np on (a.cino=np.cino)"
						+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
						+ " left join district_mst dim on (a.dist_id=dim.district_id) "
						+ " inner join ecourts_mst_case_status ecs on (a.case_status=ecs.status_id) "
						+ " left join nic_data_all nda on (a.dept_code=substr(nda.global_org_name,1,5) and a.assigned_to=nda.email and nda.is_primary='t' and coalesce(a.dist_id,'0')=coalesce(nda.dist_id,'0')) "
						+ " left join"
						+ " ("
						+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
						+ " from "
						+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
						+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
						+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true   " + sqlCondition;
				
				
				
			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLISTOLD", data);

			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			//cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( "select ack_no,ack_no from ecourts_gpo_ack_dtls   order by ack_no", con));

			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);

				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}

}