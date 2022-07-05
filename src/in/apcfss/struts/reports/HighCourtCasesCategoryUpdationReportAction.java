package in.apcfss.struts.reports;

import java.io.PrintWriter;
import java.net.InetAddress;
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
import org.apache.struts.util.LabelValueBean;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;
import plugins.DatabasePlugin;

public class HighCourtCasesCategoryUpdationReportAction extends DispatchAction {

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

			//cform.setDynaForm("regYear", "2022");
			cform.setDynaForm("ShowDefault", "ShowDefault");

			/*ArrayList selectData = new ArrayList();
			for(int i=2022; i > 1990; i--) {
				selectData.add(new LabelValueBean(i+"",i+""));
			}
			cform.setDynaForm("yearsList", selectData);
			 */
			request.setAttribute("SHOWMESG", "SHOWMESG");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}

		// return mapping.findForward("success");
		return getCasesList(mapping, cform, request, response);
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
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			con = DatabasePlugin.connect();
			// cform.setDynaForm("designationList", DatabasePlugin.getSelectBox("select
			// distinct designation_id::int4, designation_name_en from nic_data where
			// substring(global_org_name,1,5)='" + session.getAttribute("userid") + "' and
			// trim(upper(designation_name_en))<>'MINISTER' order by designation_id::int4
			// desc ", con));

			String src = CommonModels.checkStringObject(request.getParameter("src"));

			if(!src.equals("dashBoard")) {
				if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and date_of_filing >= to_date('" + cform.getDynaForm("dofFromDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and date_of_filing <= to_date('" + cform.getDynaForm("dofToDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("purpose") != null && !cform.getDynaForm("purpose").toString().contentEquals("")
						&& !cform.getDynaForm("purpose").toString().contentEquals("0")) {
					sqlCondition += " and trim(purpose_name)='" + cform.getDynaForm("purpose").toString().trim() + "' ";
				}
				if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and trim(dist_name)='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}


				if(CommonModels.checkStringObject(cform.getDynaForm("ShowDefault")).equals("ShowDefault")) {
					sqlCondition += " and reg_year in ('2021','2022') ";
					//heading
					cform.setDynaForm("regYear", "2022");
				}
				else if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
					sqlCondition += " and reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
				}


				if (CommonModels.checkIntObject(cform.getDynaForm("filingYear")) > 0) {
					sqlCondition += " and fil_year='" + CommonModels.checkIntObject(cform.getDynaForm("filingYear")) + "' ";
				}

			}


			if(!roleId.equals("2")) { //District Nodal Officer
				sqlCondition +=" and dept_code='" + deptCode + "' ";
			}

			if(roleId.equals("2")) { //District Collector

				sqlCondition +="  and dist_id='"+distId+"'";//and case_status=7
			}
			else if(roleId.equals("10")) { //District Nodal Officer
				sqlCondition +=" and dist_id='"+distId+"'";// and case_status=8
			}
			else if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
				//sqlCondition +=" and case_status in (3,4)";
			}
			else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
				//sqlCondition +=" and (case_status is null or case_status in (1, 2))";
			}

			sql = "select a.*, b.orderpaths from ecourts_case_data a left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1"
					+ " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) where coalesce(assigned,'f')='f' "
					+ sqlCondition
					+ " and coalesce(ecourts_case_status,'')!='Closed'";

			sql= " select a.*, b.finance_category from ecourts_case_data a left join ecourts_case_category_wise_data b on (a.cino=b.cino) "
					+ " where coalesce(assigned,'f')='f'  "+sqlCondition+" and coalesce(ecourts_case_status,'')!='Closed' order by finance_category";

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);

				sql = "select trim(employee_identity),trim(employee_identity) from nic_data where substr(trim(global_org_name),1,5)='"+deptCode+"' and trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				// sql="select trim(employee_identity),trim(employee_identity) from nic_data where trim(employee_identity)!='NULL' group by trim(employee_identity) order by 1";
				System.out.println("EMP SEC-SQL:"+sql);
				cform.setDynaForm("empSectionList", DatabasePlugin.getSelectBox( sql,con));
				cform.setDynaForm("total", data.size());
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");

			cform.setDynaForm("purposeList",
					DatabasePlugin
					.getSelectBox(
							"select purpose_name,purpose_name from apolcms.ecourts_case_data where dept_code='"
									+ session.getAttribute("dept_code") + "' group by purpose_name order by 1",
									con));
			cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select trim(dist_name),trim(dist_name) from apolcms.ecourts_case_data where trim(dist_name)!='null' group by trim(dist_name) order by 1",
					con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));

			cform.setDynaForm("DCLIST", DatabasePlugin.getSelectBox(
					"select district_id,upper(district_name) from district_mst order by 1",
					con));

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
			DatabasePlugin.close(con, ps, null);
		}

		return mapping.findForward("success");
	}

	public ActionForward getSubmitCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;
		try {

			con = DatabasePlugin.connect();
			//con.setAutoCommit(false);

			System.out.println("in assign2DeptHOD --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = (String)request.getSession().getAttribute("userid");
			String cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			String cInoo = CommonModels.checkStringObject(cform.getDynaForm("cino"));
			System.out.println("cIno---"+cIno+cInoo);
			String  ip = InetAddress.getLocalHost().toString();
			//int x=0;
			int respondentIds = CommonModels.checkIntObject(cform.getDynaForm("respondentIds"));
			String cfmsBill=null,billAmount=null,status=null;
			for(int respondentId=1; respondentId <= respondentIds; respondentId++) {

				//distId = Integer.parseInt(cform.getDynaForm("distId"+respondentId).toString());
				cfmsBill = CommonModels.checkStringObject(cform.getDynaForm("cfmsBill"+1));
				billAmount = CommonModels.checkStringObject(cform.getDynaForm("billAmount"+1));
				status = CommonModels.checkStringObject(cform.getDynaForm("status"+1));
			}


			int i = 1;
			//	sql = "update ecourts_case_data set finance_category='"+cform.getDynaForm("fin_category"+i).toString()+"' where cino='"+cform.getDynaForm("cino"+i).toString()+"' ";


			//sql="select * from ecourts_case_category_wise_data  where cino='" +cInoo+ "' ";
			//List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

			System.out.println("data---"+cInoo);
			
			if (cInoo != null && !cInoo.isEmpty() ) {

				sql=" INSERT INTO apolcms.ecourts_case_category_wise_data( cino, finance_category, work_name, est_cost, admin_sanction, grant_val, "
						+ " cfms_bill, bill_status, bill_amount, efile_com_no, bill_remarks, submit_date, ip_addrs) "
						+ "     VALUES( ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, now(), ?)" ;
				ps = con.prepareStatement(sql);


				ps.setString(i, cInoo != null ? cInoo : "");
				ps.setString(++i, cform.getDynaForm("fin_category") != null ? cform.getDynaForm("fin_category").toString() : "");
				ps.setString(++i, cform.getDynaForm("nameOfwork") != null ? cform.getDynaForm("nameOfwork").toString() : "");
				ps.setString(++i, cform.getDynaForm("estCost") != null ? cform.getDynaForm("estCost").toString() : "");
				ps.setString(++i, cform.getDynaForm("adminSanction") != null ? cform.getDynaForm("adminSanction").toString() : "");
				ps.setString(++i, cform.getDynaForm("grant") != null ? cform.getDynaForm("grant").toString() : "");
				ps.setString(++i, cform.getDynaForm("cfmsBill") != null ? cform.getDynaForm("cfmsBill").toString() : "");
				ps.setString(++i, cform.getDynaForm("status") != null ? cform.getDynaForm("status").toString() : "");
				ps.setString(++i, cform.getDynaForm("billAmount") != null ? cform.getDynaForm("billAmount").toString() : "");
				ps.setString(++i, cform.getDynaForm("fileComNo") != null ? cform.getDynaForm("fileComNo").toString() : "");
				ps.setString(++i, cform.getDynaForm("remarks") != null ? cform.getDynaForm("remarks").toString() : "");
				ps.setString(++i, ip!= null ? ip : "");

				int a = ps.executeUpdate();

				respondentIds = CommonModels.checkIntObject(cform.getDynaForm("respondentIds"));
				
				System.out.println("respondentIds--"+respondentIds);
				if(respondentIds > 0) {

					ps.close();

					sql=" INSERT INTO apolcms.cfms_bill_data_mst( cino, cfms_bill_id,cfms_bill_status,cfms_bill_amount) "
							+ "     VALUES( ?, ?, ?, ?)" ;

					ps = con.prepareStatement(sql);

					for(int respondentId=1; respondentId <= respondentIds; respondentId++) {
						i=1;
						if(respondentId > 0) {

							ps.setString(i, cInoo != null ? cInoo : "");
							ps.setString(++i, cform.getDynaForm("cfmsBill"+respondentId).toString());
							ps.setString(++i, cform.getDynaForm("status"+respondentId).toString());
							ps.setString(++i, cform.getDynaForm("billAmount"+respondentId).toString());
							ps.addBatch();
							System.out.println("sql--"+sql);

						}

					}
					
				ps.executeBatch();
				}
				
				if(a>0 ) {

					request.setAttribute("successMsg", "Data submitted successfully.");
				} else {
					request.setAttribute("errorMsg", "Error in submission. Kindly try again.");
				}

			}
		//	request.setAttribute("errorMsg", "invalid data. Kindly try again.");

		}
		catch (Exception e) {
			//con.rollback();
			request.setAttribute("errorMsg", "Error in Updating. Kindly try again.");
			e.printStackTrace();
		} finally {

			cform.setDynaForm("districtId", cform.getDynaForm("districtId") != null ? cform.getDynaForm("districtId").toString() : "0");
			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate") != null ? cform.getDynaForm("dofFromDate").toString() : "");
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate") != null ? cform.getDynaForm("dofToDate").toString() : "");
			cform.setDynaForm("purpose", cform.getDynaForm("purpose") != null ? cform.getDynaForm("purpose").toString() : "0");
			cform.setDynaForm("regYear", cform.getDynaForm("regYear") != null ? cform.getDynaForm("regYear").toString() : "0");
			cform.setDynaForm("filingYear", cform.getDynaForm("filingYear") != null ? cform.getDynaForm("filingYear").toString() : "0");

			//DatabasePlugin.close(con, ps, null);
		}

		//return mapping.findForward("assignCase2EmpPage");
		return getReport(mapping, cform, request, response);
	}
	public ActionForward getCino(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, cIno = null, viewDisplay=null, target="caseview1";

		System.out.println("getCino");

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			viewDisplay = CommonModels.checkStringObject(request.getParameter("SHOWPOPUP"));

			System.out.println("viewDisplay--"+viewDisplay);

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			if(!viewDisplay.equals("") && viewDisplay.equals("SHOWPOPUP")) {
				target = "casepopupview1";
				cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			}
			else {
				cIno = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			}

			System.out.println("cIno"+cIno);


			if (cIno != null && !cIno.equals("")) {
				con = DatabasePlugin.connect();

				sql = "select * from ecourts_case_data where cino='" + cIno +"'";
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("USERSLIST", data);

				}

				sql = "select * from ecourts_case_category_wise_data where cino='" + cIno +
						"'"; List<Map<String, Object>> dataUpdate = DatabasePlugin.executeQuery(sql,
								con);

						if (dataUpdate != null && !dataUpdate.isEmpty() && dataUpdate.size() > 0) {
							request.setAttribute("USERSLIST", dataUpdate);

						}



						request.setAttribute("HEADING", "Case Details for CINO : " + cIno);
			}
			else {
				request.setAttribute("errorMsg", "Invalid Cino.");
			}



		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}
	public ActionForward getReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, cIno = null, viewDisplay=null, target="caseview1";

		System.out.println("getCino");

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));


			con = DatabasePlugin.connect();

			sql = "select * from ecourts_case_category_wise_data where cino='" + cIno + "'";
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);

			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}
}