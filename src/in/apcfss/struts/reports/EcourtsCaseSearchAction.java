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
			
			String caseType=cform.getDynaForm("oldNewType").toString();
			
			String ackNoo=null;
			
			String cino=null;
			
			
			if(caseType.equals("New")) {
				
				cform.setDynaForm("oldNewType", "New");
				
				ackNoo=cform.getDynaForm("ackNoo").toString();
			
			}else {
				
				cform.setDynaForm("oldNewType", "Legacy");
				
				cino=cform.getDynaForm("caseType1").toString()+"/"+cform.getDynaForm("mainCaseNo").toString()+"/"+cform.getDynaForm("regYear1").toString();
				
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
				
			}
			
			System.out.println("ackNoo--"+ackNoo+"cino---"+cino);
			
			List<Map<String, Object>> data=null;
			
			if (caseType.equals("Legacy")) {
				
				sql = "select a.*, "
						+ "coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a "
						+ " left join nic_prayer_data np on (a.cino=np.cino)"
						+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
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
			 data = DatabasePlugin.executeQuery(sql, con);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLISTOLD", data);

			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
			}else {
				
				sql = "select a.slno ,ad.respondent_slno, a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , a.remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
						+ "to_char(a.inserted_time,'dd-mm-yyyy') as generated_date, "
						+ "getack_dept_desc(a.ack_no::text) as dept_descs , coalesce(a.hc_ack_no,'-') as hc_ack_no "
						+ " from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
						+ " left join district_mst dm on (ad.dist_id=dm.district_id) "
						+ " left join dept_new dmt on (ad.dept_code=dmt.dept_code)"
						+ " inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name)  "
						+ " where a.delete_status is false and ack_type='NEW'    and (a.ack_no='"+ackNoo+"' or a.hc_ack_no='"+ackNoo+"' )    "
						+ " order by a.inserted_time desc";

			System.out.println("ecourts SQL:" + sql);
		 data = DatabasePlugin.executeQuery(sql, con);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLISTNEW", data);

			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}
			}

			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			//cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( "select ack_no,ack_no from ecourts_gpo_ack_dtls   order by ack_no", con));

			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);

			
			if(caseType.equals("New")) {
				cform.setDynaForm("cino", ackNoo);
			//	request.setAttribute("cinooo", ackNoo);
				}else {
				cform.setDynaForm("cino", ((Map) data.get(0)).get("cino"));
				//request.setAttribute("cinooo", ((Map) data.get(0)).get("cino"));
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			/*
			 * cform.setDynaForm("caseType1",""); cform.setDynaForm("regYear1","");
			 * cform.setDynaForm("mainCaseNo",""); cform.setDynaForm("ackNoo","");
			 */
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}

}