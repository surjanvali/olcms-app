package in.apcfss.struts.Actions;

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
import org.apache.struts.util.LabelValueBean;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class CaseSearchAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		try {
			con = DatabasePlugin.connect();

			request.setAttribute("saveAction", "INSERT");

			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select  case_short_name,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno",
					con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1990; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		String sql = null, sqlCondition = "", roleId = "", distId = "", deptCode = "";
		try {
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			con = DatabasePlugin.connect();

			if (CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regNo")).equals("")) {
				sqlCondition += " and a.reg_no='" + CommonModels.checkStringObject(cform.getDynaForm("regNo")) + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("caseType")).equals("")
					&& !CommonModels.checkStringObject(cform.getDynaForm("caseType")).equals("0")) {
				sqlCondition += " and a.type_name_reg='" + CommonModels.checkStringObject(cform.getDynaForm("caseType"))
						+ "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("cinNo")).equals("")) {
				sqlCondition += " and a.cino='" + CommonModels.checkStringObject(cform.getDynaForm("cinNo")) + "' ";
			}

			sql = "select cino from ecourts_case_data a where 1=1 " + sqlCondition;
			//sql = "select type_name_fil||'-'||reg_no||'-'||reg_year from ecourts_case_data a where 1=1 " + sqlCondition;
			System.out.println("SQL:"+sql);
			
			
			String cinNo = CommonModels.checkStringObject(DatabasePlugin.getStringfromQuery(sql, con));
			System.out.println("CINNO:"+cinNo);
			GPOAcknowledgementAction gpoAct = new GPOAcknowledgementAction();
			if (cinNo!=null && !cinNo.equals("") && !cinNo.equals("0")) {
				
				
				String barCodeFilePath = gpoAct.generateAckBarCodePdf128(cinNo, cform);
				int a=0;
				if (barCodeFilePath != null) {
					sql = "update ecourts_case_data set barcode_file_path='" + barCodeFilePath + "' where cino='"
							+ cinNo + "'";
					a = DatabasePlugin.executeUpdate(sql, con);
				}
				if(a > 0) { 
					
				sql = "select a.*, b.orderpaths from ecourts_case_data a left join" + " ("
						+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
						+ " from "
						+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
						+ " union"
						+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
						+ " on (a.cino=b.cino) where 1=1 " + sqlCondition + " ";

				System.out.println("ecourts SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				// System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASESLIST", data);
				}else {
					request.setAttribute("errorMsg", "Records Found but failed to fetch data.");
				}
				}
				else {
					request.setAttribute("errorMsg", "Records Found but failed to generate Bar code.");
				}
			} else {
				request.setAttribute("errorMsg", "No Records Found in APOLCMS. Enter the below details to generate case details.");
				
				cform.setDynaForm("ackType", "OLD");
				// return gpoAct.displayAckForm(mapping, cform, request, response);
				return mapping.findForward("displayAckForm");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
					"select  case_short_name,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno",
					con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1990; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}
	
	public ActionForward getAcknowledementsList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			con = DatabasePlugin.connect();
			String ackType = "OLD";

			sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,services_flag,"
					+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs, a.barcode_file_path, reg_year, reg_no "
					+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
					+ "left join case_type_master cm on (a.casetype=cm.case_short_name or a.casetype=cm.sno::text) "
					+ "left join (select ack_no,dm.dept_code,dm.description from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
					+ "where a.inserted_by='"+session.getAttribute("userid")
					+"' and a.delete_status is false and ack_type='"+ackType+"' "
					+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
					+ "case_full_name,a.ack_file_path, services_id, services_flag, inserted_time, a.barcode_file_path, reg_year, reg_no "
					+ "order by inserted_time desc";
			
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("ACKDATA", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found.");
				return unspecified(mapping, cform, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
}
