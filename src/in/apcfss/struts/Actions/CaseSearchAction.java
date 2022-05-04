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

public class CaseSearchAction extends DispatchAction{
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		try {
			con = DatabasePlugin.connect();

			request.setAttribute("saveAction", "INSERT");

			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox( "select  sno,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno", con));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
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
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="";
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
			if (!CommonModels.checkStringObject(cform.getDynaForm("caseType")).equals("") && !CommonModels.checkStringObject(cform.getDynaForm("caseType")).equals("0")) {
				sqlCondition += " and a.type_name_reg='" + CommonModels.checkStringObject(cform.getDynaForm("caseType")) + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("cinNo")).equals("")) {
				sqlCondition += " and a.cino='" + CommonModels.checkStringObject(cform.getDynaForm("cinNo")) + "' ";
			}
			
			sql = "select a.*, b.orderpaths from ecourts_case_data a left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " ((select cino, order_document_path,order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)"
					+ " union"
					+ " (select cino, order_document_path,order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 order by sr_no)) c group by cino ) b"
					+ " on (a.cino=b.cino) where 1=1 "
					+ sqlCondition
					+ " ";

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			
			ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);
			
			request.setAttribute("deptCode", deptCode);
			
			cform.setDynaForm("currentDeptId", deptCode);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox( "select  sno,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno", con));
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}
}
