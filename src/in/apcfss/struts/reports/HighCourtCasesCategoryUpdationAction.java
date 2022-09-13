package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

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

import plugins.DatabasePlugin;

public class HighCourtCasesCategoryUpdationAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, sqlCondition = "",dept_code="";
		CommonForm cform = (CommonForm) form;
		try {
			System.out.println( "HCCaseStatusAbstractReport..............................................................................unspecified()");
			System.out.println("unspecified unspecified unspecified");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			dept_code = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			else if (roleId.equals("5") || roleId.equals("9")) {

				return HODwisedetails(mapping, form, request, response);
			} else // if(roleId.equals("3") || roleId.equals("4"))
			{
				con = DatabasePlugin.connect();

				if (cform.getDynaForm("dofFromDate") != null
						&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("dofToDate") != null
						&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
					sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
					+ "','dd-mm-yyyy') ";
				}
				if (cform.getDynaForm("caseTypeId") != null
						&& !cform.getDynaForm("caseTypeId").toString().contentEquals("")
						&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
					sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
							+ "' ";
				}
				if (cform.getDynaForm("districtId") != null
						&& !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
				if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
						&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
					sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear"))
					+ "' ";
				}
				if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
						&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
					sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
				}

				sql = "select x.reporting_dept_code as deptcode, upper(d1.description) as description,sum(total_cases) as total_cases,sum(withsectdept) as withsectdept,sum(withmlo) as withmlo,"
						+ "sum(withhod) as withhod,sum(withnodal) as withnodal,sum(withsection) as withsection, sum(withdc) as withdc, sum(withdistno) as withdistno,sum(withsectionhod) as withsectionhod,"
						+ " sum(withsectiondist) as withsectiondist, sum(withgpo) as withgpo, sum(closedcases) as closedcases  from ("
						+ "select a.dept_code , case when reporting_dept_code='CAB01' then d.dept_code else reporting_dept_code end as reporting_dept_code,count(*) as total_cases, "
						+ "sum(case when case_status=1 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectdept, "
						+ "sum(case when (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withmlo, "
						+ "sum(case when case_status=3  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withhod, "
						+ "sum(case when case_status=4  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withnodal, "
						+ "sum(case when case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsection, "
						+ "sum(case when case_status=7  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdc, "
						+ "sum(case when case_status=8  and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withdistno, "
						+ "sum(case when case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectionhod, "
						+ "sum(case when case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withsectiondist, "
						+ "sum(case when case_status=6 and coalesce(ecourts_case_status,'')!='Closed' then 1 else 0 end) as withgpo, "
						+ "sum(case when case_status=99 or coalesce(ecourts_case_status,'')='Closed' then 1 else 0 end) as closedcases "
						+ "from ecourts_case_data a " + "inner join dept_new d on (a.dept_code=d.dept_code) "
						+ "where d.display = true " + sqlCondition;

				if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
					sql += " and (reporting_dept_code='" + session.getAttribute("dept_code") + "' or a.dept_code='"
							+ session.getAttribute("dept_code") + "')";

				if (roleId.equals("2")) {
					sql += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
					cform.setDynaForm("districtId", session.getAttribute("dist_id"));
				}

				sql += " group by a.dept_code,d.dept_code ,reporting_dept_code ) x inner join dept_new d1 on (x.reporting_dept_code=d1.dept_code)"
						+ " group by x.reporting_dept_code, d1.description order by 1";
				
				String str1="";
				
				if ( (roleId.equals("4") || roleId.equals("5") || roleId.equals("3")) && dept_code.equals("FIN01") ) {
					
					 str1=" ";
				}else {
					
					 str1=" where b.dept_code='"+dept_code+"' ";
				}
				
				
				if ( roleId.equals("1") || roleId.equals("7") ) 
					str1=" ";
					
				
				sql=" select c.dept_code as deptcode,upper(c.description) as description, "
						+ " coalesce(sum (case  when a.finance_category='A1' then 1 end),'0') as A1 ,"
						+ "coalesce(sum (case  when a.finance_category='A2' then 1 end),'0') as A2 ,"
						+ "coalesce(sum (case  when a.finance_category='B1' then 1 end),'0') as B1 ,"
						+ "coalesce(sum (case  when a.finance_category='B2' then 1 end),'0') as B2 ,"
						+ "coalesce(sum (case  when a.finance_category='C1' then 1 end),'0') as C1 ,"
						+ "coalesce(sum (case  when a.finance_category='C2' then 1 end),'0') as C2 "
						+ " from ecourts_case_category_wise_data a  inner join ecourts_case_data b on (a.cino=b.cino)       "
						+ " inner join dept_new c on (b.dept_code=c.dept_code)   "+str1+" group by description,c.dept_code order by c.description";
							

				request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

				System.out.println("unspecified SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("unspecified data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0)
					request.setAttribute("secdeptwise", data);
				else
					request.setAttribute("errorMsg", "No Records found to display");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {
				if(con!=null) {
					if (roleId.equals("2")) {
						cform.setDynaForm("distList",
								DatabasePlugin.getSelectBox(
										"select district_id,upper(district_name) from district_mst where district_id='"
												+ session.getAttribute("dist_id") + "' order by district_name",
												con));
					}
					else {
						sql="select district_id,upper(district_name) from district_mst order by 1";
						System.out.println("DIST SQL:"+sql);
						System.out.println("DIST SQL CON:"+con);
						cform.setDynaForm("distList", DatabasePlugin.getSelectBox(sql, con));
					}

					cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
							"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
							con));
					cform.setDynaForm("caseTypesList", DatabasePlugin
							.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
					ArrayList selectData = new ArrayList();
					for (int i = 2022; i > 1980; i--) {
						selectData.add(new LabelValueBean(i + "", i + ""));
					}
					cform.setDynaForm("yearsList", selectData);

					cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
					cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
					cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
					cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
					cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
					cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
					cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
					cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));

					request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
					DatabasePlugin.closeConnection(con);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mapping.findForward("success");
	}

	public ActionForward HODwisedetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, deptId = null, deptName = "", sqlCondition = "";
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			System.out.println("deptId---"+CommonModels.checkStringObject(session.getAttribute("dept_code")));
			
			if (roleId.equals("5") || roleId.equals("9")) {
				deptId = CommonModels.checkStringObject(session.getAttribute("dept_code"));
				deptName = DatabasePlugin.getStringfromQuery(
						"select upper(description) as description from dept_new where dept_code='" + deptId + "'", con);
				// CommonModels.checkStringObject(session.getAttribute("dept_code"));
			} else {
				
				if(request.getParameter("deptId").toString().equals(null)) {
				deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
				deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
				}else {
					
					deptId = CommonModels.checkStringObject(request.getParameter("deptId").toString());
					deptName = CommonModels.checkStringObject(request.getParameter("deptName"));
				}
			}
			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			/*
			 * if (cform.getDynaForm("deptId") != null &&
			 * !cform.getDynaForm("deptId").toString().contentEquals("") &&
			 * !cform.getDynaForm("deptId").toString().contentEquals("0")) { sqlCondition +=
			 * " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			 * }
			 */

			if (roleId.equals("2")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			
sql=" select c.dept_code as deptcode,upper(c.description) as description, "
		+ " coalesce(sum (case  when a.finance_category='A1' then 1 end),'0') as A1 ,"
		+ "coalesce(sum (case  when a.finance_category='A2' then 1 end),'0') as A2 ,"
		+ "coalesce(sum (case  when a.finance_category='B1' then 1 end),'0') as B1 ,"
		+ "coalesce(sum (case  when a.finance_category='B2' then 1 end),'0') as B2 ,"
		+ "coalesce(sum (case  when a.finance_category='C1' then 1 end),'0') as C1 ,"
		+ "coalesce(sum (case  when a.finance_category='C2' then 1 end),'0') as C2 "
		+ " from ecourts_case_category_wise_data a  inner join ecourts_case_data b on (a.cino=b.cino)       "
		+ " inner join dept_new c on (b.dept_code=c.dept_code) "
		+ " where ( c.reporting_dept_code='" + deptId +"' or b.dept_code='" + deptId +"' ) " //request.getParameter("deptId").toString()
		+ " group by description,c.dept_code order by c.description";
			

			request.setAttribute("HEADING", "HOD Wise High Court Cases Abstract Report for " + deptName);
			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("deptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			try {
				if (roleId.equals("2"))
					cform.setDynaForm("distList",
							DatabasePlugin.getSelectBox(
									"select district_id,upper(district_name) from district_mst where district_id='"
											+ session.getAttribute("dist_id") + "' order by district_name",
											con));
				else
					cform.setDynaForm("distList", DatabasePlugin
							.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
						con));
				cform.setDynaForm("caseTypesList", DatabasePlugin
						.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
				ArrayList selectData = new ArrayList();
				for (int i = 2022; i > 1980; i--) {
					selectData.add(new LabelValueBean(i + "", i + ""));
				}
				cform.setDynaForm("yearsList", selectData);

				cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
				cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
				cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
				cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
				cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
				cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
				cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
				cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
				request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
				DatabasePlugin.closeConnection(con);
			}catch (Exception e) {
				e.printStackTrace();
			}
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
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId = null,
				deptCode = null, caseStatus = null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));

			heading = "Cases List for " + deptName;

			if (!caseStatus.equals("")) {
				if (caseStatus.equals("withSD")) {
					sqlCondition = " and case_status=1 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sect Dept. Login";
				}
				if (caseStatus.equals("withMLO")) {
					sqlCondition = " and (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at MLO Login";
				}
				if (caseStatus.equals("withHOD")) {
					sqlCondition = " and case_status=3  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at HOD Login";
				}
				if (caseStatus.equals("withNO")) {
					sqlCondition = " and case_status=4  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(HOD) Login";
				}
				if (caseStatus.equals("withSDSec")) {
					sqlCondition = " and case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officers Login (Sect Dept.)";
				}
				if (caseStatus.equals("withDC")) {
					sqlCondition = " and case_status=7  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at District Collector Login";
				}
				if (caseStatus.equals("withDistNO")) {
					sqlCondition = " and case_status=8  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(District) Login";
				}
				if (caseStatus.equals("withHODSec")) {
					sqlCondition = " and case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officer(HOD) Login";
				}
				if (caseStatus.equals("withDistSec")) {
					sqlCondition = " and case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sction Officer(District) Login";
				}
				if (caseStatus.equals("withGP")) {
					sqlCondition = " and case_status=6 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at GP Login";
				}
				if (caseStatus.equals("closed")) {
					sqlCondition = " and case_status=99 or coalesce(ecourts_case_status,'')='Closed' ";
					heading += " All Closed Cases ";
				}
			}

			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}




			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			if (actionType.equals("SDWISE")) {
			} else if (actionType.equals("HODWISE")) {
			}
			System.out.println("repType--"+request.getParameter("reportType").toString());
			System.out.println("actionType--"+actionType);
			
			System.out.println("roleId--"+roleId);
			
			String condition="";
			if (roleId.equals("6") )
				condition= " inner join ecourts_mst_gp_dept_map e on (a.dept_code=e.dept_code) ";

			String caseCategory = CommonModels.checkStringObject(request.getParameter("caseCategory"));
			String deptType = CommonModels.checkStringObject(request.getParameter("deptType"));

			if (caseCategory != null && !caseCategory.equals("")) {
			
				sqlCondition += " and trim(disposal_type)='" + caseCategory.trim() + "'  and trim(d.description)='"+deptType.trim()+"'  ";
				heading += caseCategory.trim()+", Department : "+deptType.trim();
			}
			
			
			

			sql = "select a.*,coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths from ecourts_case_data a left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) "+condition+" where d.display = true ";

			if (deptCode != null && !deptCode.equals(""))
				sql += " and (reporting_dept_code='" + deptCode + "' or a.dept_code='" + deptCode + "') ";

			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql += " and a.dept_code='" + CommonModels.checkStringObject(session.getAttribute("dept_code")) + "' ";

			sql += sqlCondition;
			
			if(!request.getParameter("reportType").toString().equals("All")) {
			
				/*
				 * sql="SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,purpose_name,res_name,pet_name,pet_adv,res_adv,"
				 * +
				 * " d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,d.cfms_bill,d.bill_status,d.bill_amount "
				 * +
				 * " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code) "
				 * + " where d.finance_category='"+request.getParameter("reportType").toString()
				 * +"' and a.dept_code='"+request.getParameter("deptId").toString()+"' "
				 * +sqlCondition+"  ORDER BY d.finance_category ";
				 */
			
			sql="SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,"
					+ " purpose_name,res_name,pet_name,pet_adv,res_adv, d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,e.cfms_bill,e.bill_amount  "
					+ " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code)"
					+ "  inner join (select cino, string_agg(cfms_bill_id,',') as cfms_bill, sum(coalesce(cfms_bill_amount,'0')::int4) as bill_amount"
					+ "  from cfms_bill_data_mst group by cino) e on (a.cino=e.cino)  where  d.finance_category='"+request.getParameter("reportType").toString()+"' "
							+ " and a.dept_code='"+request.getParameter("deptId").toString()+"' "+sqlCondition+"   ORDER BY d.finance_category ";
			
			
			}else {
				
				/*
				 * sql="SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,purpose_name,res_name,pet_name,pet_adv,res_adv,"
				 * +
				 * " d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,d.cfms_bill,d.bill_status,d.bill_amount "
				 * +
				 * " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code) "
				 * + " where  1=1 and a.dept_code='"+request.getParameter("deptId").toString()
				 * +"' "+sqlCondition+"  ORDER BY d.finance_category ";
				 */
				
				
				sql="SELECT d.cino,date_of_filing,type_name_fil,fil_no,fil_year,reg_no,reg_year,bench_name,coram,dist_name,"
						+ " purpose_name,res_name,pet_name,pet_adv,res_adv, d.finance_category,d.work_name,d.est_cost,d.admin_sanction,d.grant_val,e.cfms_bill,e.bill_amount  "
						+ " FROM  ecourts_case_data a right join ecourts_case_category_wise_data d    on (a.cino=d.cino) inner join dept_new c on (a.dept_code=c.dept_code)"
						+ "  inner join (select cino, string_agg(cfms_bill_id,',') as cfms_bill, sum(coalesce(cfms_bill_amount,'0')::int4) as bill_amount"
						+ "  from cfms_bill_data_mst group by cino) e on (a.cino=e.cino)  where  1=1 and a.dept_code='"+request.getParameter("deptId").toString()+"' "+sqlCondition+"    ORDER BY d.finance_category ";
				
				
			}
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
			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst where district_id='"
										+ session.getAttribute("dist_id") + "' order by district_name",
										con));
			else
				cform.setDynaForm("distList", DatabasePlugin
						.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
			request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}
	public ActionForward getCasesDeptWiseList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId = null,
				deptCode = null, caseStatus = null,dispType=null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			dispType = CommonModels.checkStringObject(cform.getDynaForm("deptName"));

			if (!caseStatus.equals("")) {
				if (caseStatus.equals("withSD")) {
					sqlCondition = " and case_status=1 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sect Dept. Login";
				}
				if (caseStatus.equals("withMLO")) {
					sqlCondition = " and (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at MLO Login";
				}
				if (caseStatus.equals("withHOD")) {
					sqlCondition = " and case_status=3  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at HOD Login";
				}
				if (caseStatus.equals("withNO")) {
					sqlCondition = " and case_status=4  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(HOD) Login";
				}
				if (caseStatus.equals("withSDSec")) {
					sqlCondition = " and case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officers Login (Sect Dept.)";
				}
				if (caseStatus.equals("withDC")) {
					sqlCondition = " and case_status=7  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at District Collector Login";
				}
				if (caseStatus.equals("withDistNO")) {
					sqlCondition = " and case_status=8  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(District) Login";
				}
				if (caseStatus.equals("withHODSec")) {
					sqlCondition = " and case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officer(HOD) Login";
				}
				if (caseStatus.equals("withDistSec")) {
					sqlCondition = " and case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sction Officer(District) Login";
				}
				if (caseStatus.equals("withGP")) {
					sqlCondition = " and case_status=6 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at GP Login";
				}
				if (caseStatus.equals("closed")) {
					sqlCondition = " and case_status=99 or coalesce(ecourts_case_status,'')='Closed' ";
					heading += " All Closed Cases ";
				}
			}

			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			String caseCategory = CommonModels.checkStringObject(request.getParameter("caseCategory"));

			if (caseCategory != null && !caseCategory.equals("")) {

				if (caseCategory.equals("DISPOSED OF NO COSTS")) {
					sqlCondition += " and (disposal_type='DISPOSED OF NO COSTS')";
				} else if (caseCategory.equals("DISPOSED OF AS INFRUCTUOUS")) {
					sqlCondition += " and (disposal_type='DISPOSED OF AS INFRUCTUOUS')";
				} 
				else if (caseCategory.equals("ALLOWED NO COSTS")) {
					sqlCondition += " and (disposal_type='ALLOWED NO COSTS')";
				} 
				else if (caseCategory.equals("PARTLY ALLOWED NO COSTS")) {
					sqlCondition += " and (disposal_type='PARTLY ALLOWED NO COSTS')";
				} 
				else if (caseCategory.equals("DISMISSED")) {
					sqlCondition += " and (disposal_type='DISMISSED')";
				} 
				else if (caseCategory.equals("DISMISSED AS INFRUCTUOUS")) {
					sqlCondition += " and (disposal_type='DISMISSED AS INFRUCTUOUS')";
				} 
				else if (caseCategory.equals("DISMISSED NO COSTS")) {
					sqlCondition += " and (disposal_type='DISMISSED NO COSTS')";
				} 
				else if (caseCategory.equals("DISMISSED FOR DEFAULT")) {
					sqlCondition += " and (disposal_type='DISMISSED FOR DEFAULT')";
				} 
				else if (caseCategory.equals("DISMISSED AS NON PROSECUTION")) {
					sqlCondition += " and (disposal_type='DISMISSED AS NON PROSECUTION')";
				} 
				else if (caseCategory.equals("DISMISSED AS ABATED")) {
					sqlCondition += " and (disposal_type='DISMISSED AS ABATED')";
				} 
				else if (caseCategory.equals("DISMISSED AS NOT PRESSED")) {
					sqlCondition += " and (disposal_type='DISMISSED AS NOT PRESSED')";
				} 
				else if (caseCategory.equals("WITHDRAWN")) {
					sqlCondition += " and (disposal_type='WITHDRAWN')";
				} 
				else if (caseCategory.equals("CLOSED NO COSTS")) {
					sqlCondition += " and (disposal_type='CLOSED NO COSTS')";
				}
				else if (caseCategory.equals("CLOSED AS NOT PRESSED")) {
					sqlCondition += " and (disposal_type='CLOSED AS NOT PRESSED')";
				}

				else if (caseCategory.equals("REJECTED")) {
					sqlCondition += " and (disposal_type='REJECTED' )";
				}

				else if (caseCategory.equals("ORDERED")) {
					sqlCondition += " and ( disposal_type='ORDERED' )";
				}
				else if (caseCategory.equals("RETURN TO COUNSEL")) {
					sqlCondition += " and (disposal_type='RETURN TO COUNSEL')";
				}

				else if (caseCategory.equals("TRANSFERRED")) {
					sqlCondition += " and (disposal_type='TRANSFERRED')";
				}
			}
			
			String condition="";
			if (roleId.equals("6") )
				condition= " inner join ecourts_mst_gp_dept_map c on a.dept_code=c.dept_code ";

			//sql = "select disposal_type, count(*) as casescount from ecourts_case_data a where 1=1 " + sqlCondition;
			sql="select disposal_type,b.dept_code,b.description,count(*) as casescount from ecourts_case_data a "+condition+" inner join dept_new b on a.dept_code=b.dept_code   where 1=1 " + sqlCondition;


			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql += " and a.dept_code='" + CommonModels.checkStringObject(session.getAttribute("dept_code")) + "' ";

			sql += " group by disposal_type,b.description,b.dept_code";

			heading = "Cases List for Category " + request.getParameter("caseCategory").toString();

			System.out.println("DISPWISE SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("DIPTWISECASES", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst where district_id='"
										+ session.getAttribute("dist_id") + "' order by district_name",
										con));
			else
				cform.setDynaForm("distList", DatabasePlugin
						.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
			request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}

	public ActionForward getCasesGroupList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		String sql = null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", roleId = null,
				deptCode = null, caseStatus = null, dispType = null;
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			dispType = CommonModels.checkStringObject(cform.getDynaForm("deptName"));

			if (!caseStatus.equals("")) {
				if (caseStatus.equals("withSD")) {
					sqlCondition = " and case_status=1 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sect Dept. Login";
				}
				if (caseStatus.equals("withMLO")) {
					sqlCondition = " and (case_status is null or case_status=2)  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at MLO Login";
				}
				if (caseStatus.equals("withHOD")) {
					sqlCondition = " and case_status=3  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at HOD Login";
				}
				if (caseStatus.equals("withNO")) {
					sqlCondition = " and case_status=4  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(HOD) Login";
				}
				if (caseStatus.equals("withSDSec")) {
					sqlCondition = " and case_status=5 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officers Login (Sect Dept.)";
				}
				if (caseStatus.equals("withDC")) {
					sqlCondition = " and case_status=7  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at District Collector Login";
				}
				if (caseStatus.equals("withDistNO")) {
					sqlCondition = " and case_status=8  and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Nodal Officer(District) Login";
				}
				if (caseStatus.equals("withHODSec")) {
					sqlCondition = " and case_status=9 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Section Officer(HOD) Login";
				}
				if (caseStatus.equals("withDistSec")) {
					sqlCondition = " and case_status=10 and coalesce(assigned,'f')='t' and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at Sction Officer(District) Login";
				}
				if (caseStatus.equals("withGP")) {
					sqlCondition = " and case_status=6 and coalesce(ecourts_case_status,'')!='Closed' ";
					heading += " Pending at GP Login";
				}
				if (caseStatus.equals("closed")) {
					sqlCondition = " and case_status=99 or coalesce(ecourts_case_status,'')='Closed' ";
					heading += " All Closed Cases ";
				}
			}

			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis >= to_date('" + cform.getDynaForm("dofFromDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("dofToDate") != null
					&& !cform.getDynaForm("dofToDate").toString().contentEquals("")) {
				sqlCondition += " and a.dt_regis <= to_date('" + cform.getDynaForm("dofToDate")
				+ "','dd-mm-yyyy') ";
			}
			if (cform.getDynaForm("caseTypeId") != null && !cform.getDynaForm("caseTypeId").toString().contentEquals("")
					&& !cform.getDynaForm("caseTypeId").toString().contentEquals("0")) {
				sqlCondition += " and trim(a.type_name_reg)='" + cform.getDynaForm("caseTypeId").toString().trim()
						+ "' ";
			}
			if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL")
					&& CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
				sqlCondition += " and a.reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
			}
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				sqlCondition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
			}

			if (roleId.equals("2") || roleId.equals("10")) {
				sqlCondition += " and a.dist_id='" + session.getAttribute("dist_id") + "' ";
				cform.setDynaForm("districtId", session.getAttribute("dist_id"));
			}

			String caseCategory = CommonModels.checkStringObject(request.getParameter("caseCategory"));
			if (caseCategory != null && !caseCategory.equals("")) {

				if (caseCategory.equals("DISPOSED")) {
					sqlCondition += " and (disposal_type='DISPOSED OF NO COSTS' or disposal_type='DISPOSED OF AS INFRUCTUOUS')";
				} else if (caseCategory.equals("ALLOWED")) {
					sqlCondition += " and (disposal_type='ALLOWED NO COSTS' or disposal_type='PARTLY ALLOWED NO COSTS')";
				} else if (caseCategory.equals("DISMISSED")) {
					sqlCondition += " and (disposal_type='DISMISSED' or disposal_type='DISMISSED AS INFRUCTUOUS' or disposal_type='DISMISSED NO COSTS' or disposal_type='DISMISSED FOR DEFAULT' or disposal_type='DISMISSED AS NON PROSECUTION' or disposal_type='DISMISSED AS ABATED' or disposal_type='DISMISSED AS NOT PRESSED' )";
				} else if (caseCategory.equals("WITHDRAWN")) {
					sqlCondition += " and (disposal_type='WITHDRAWN')";
				} else if (caseCategory.equals("CLOSED")) {
					sqlCondition += " and (disposal_type='CLOSED NO COSTS' or disposal_type='CLOSED AS NOT PRESSED')";
				} else if (caseCategory.equals("RETURNED")) {
					sqlCondition += " and (disposal_type='REJECTED' or disposal_type='ORDERED' or disposal_type='RETURN TO COUNSEL' or disposal_type='TRANSFERRED')";
				}

			}
			
			String condition="";
			if (roleId.equals("6") )
				condition= " inner join ecourts_mst_gp_dept_map b on a.dept_code=b.dept_code ";

			sql = "select disposal_type, count(*) as casescount from ecourts_case_data a "+condition+" where 1=1 " + sqlCondition;

			if (roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
				sql += " and a.dept_code='" + CommonModels.checkStringObject(session.getAttribute("dept_code")) + "' ";
			
			

			sql += " group by disposal_type";
			
			System.out.println("roleId--"+roleId);

			heading = "Cases List for Category " + request.getParameter("caseCategory").toString();

			System.out.println("DISPWISE SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			request.setAttribute("HEADING", heading);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("DISPWISE", data);
			} else {
				request.setAttribute("errorMsg", "No Records Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (roleId.equals("2"))
				cform.setDynaForm("distList",
						DatabasePlugin.getSelectBox(
								"select district_id,upper(district_name) from district_mst where district_id='"
										+ session.getAttribute("dist_id") + "' order by district_name",
										con));
			else
				cform.setDynaForm("distList", DatabasePlugin
						.getSelectBox("select district_id,upper(district_name) from district_mst order by 1", con));

			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					"select dept_code,dept_code||'-'||upper(description) from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("caseTypesList", DatabasePlugin
					.getSelectBox("select case_short_name,case_full_name from case_type_master order by sno", con));
			ArrayList selectData = new ArrayList();
			for (int i = 2022; i > 1980; i--) {
				selectData.add(new LabelValueBean(i + "", i + ""));
			}
			cform.setDynaForm("yearsList", selectData);

			cform.setDynaForm("dofFromDate", cform.getDynaForm("dofFromDate"));
			cform.setDynaForm("dofToDate", cform.getDynaForm("dofToDate"));
			cform.setDynaForm("caseTypeId", cform.getDynaForm("caseTypeId"));
			cform.setDynaForm("districtId", cform.getDynaForm("districtId"));
			cform.setDynaForm("regYear", cform.getDynaForm("regYear"));
			cform.setDynaForm("deptId", cform.getDynaForm("deptId"));
			cform.setDynaForm("petitionerName", cform.getDynaForm("petitionerName"));
			cform.setDynaForm("respodentName", cform.getDynaForm("respodentName"));
			request.setAttribute("SHOWFILTERS", "SHOWFILTERS");
			DatabasePlugin.closeConnection(con);
		}

		return mapping.findForward("success");
	}

}