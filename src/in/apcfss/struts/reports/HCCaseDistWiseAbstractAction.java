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

public class HCCaseDistWiseAbstractAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null,condition="",deptCode="",districtId="";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			districtId = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
			
			System.out.println("dist"+districtId);
			
			con = DatabasePlugin.connect();
			
			if (cform.getDynaForm("districtId") != null
					&& !cform.getDynaForm("districtId").toString().contentEquals("")
					&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
				condition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
			}
			
			if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
					&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
				condition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
				
			}
			

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			
			
			  else if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9")) {
					condition+=" and ( a.dept_code='"+session.getAttribute("dept_code")+"')";
					cform.setDynaForm("deptId", session.getAttribute("dept_code"));
			  }else if(roleId.equals("2")){
					condition+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
					cform.setDynaForm("districtId", session.getAttribute("dist_id"));
				}
			
			else //if(roleId.equals("3") || roleId.equals("4"))
			{
				//condition="";
				
				if (cform.getDynaForm("districtId") != null
						&& !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					condition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
				
				if (cform.getDynaForm("deptId") != null && !cform.getDynaForm("deptId").toString().contentEquals("")
						&& !cform.getDynaForm("deptId").toString().contentEquals("0")) {
					condition += " and a.dept_code='" + cform.getDynaForm("deptId").toString().trim() + "' ";
				}
				
			}
				
				sql="select dm.district_id,dm.district_name,count(*) as total_cases,"
						
						+ "sum(case when case_status=7 then 1 else 0 end) as pending_dc, "
						+ "sum(case when case_status=8 then 1 else 0 end) as pending_dno, "
						+ "sum(case when case_status=10 then 1 else 0 end) as pending_dsec, "
						
						+ "sum(case when scanned_document_path is not null and length(scanned_document_path)>10 then 1 else 0 end) as olcms_uploads, "
						+ "sum(case when petition_document is not null and length(petition_document)>10 then 1 else 0 end) as petition_uploaded , "
						+ "sum(case when a.ecourts_case_status='Closed' then 1 else 0 end) as closed_cases , "
						+ "sum(case when a.ecourts_case_status='Pending' and counter_filed_document is not null and length(counter_filed_document)>10  then 1 else 0 end) as counter_uploaded,"
						+ " sum(case when a.ecourts_case_status='Pending' and pwr_uploaded_copy is not null and length(pwr_uploaded_copy)>10  then 1 else 0 end) as pwrcounter_uploaded ,"
						+ " sum(case when counter_approved_gp='Yes' then 1 else 0 end) as counter_approved_gp,"
						+ "sum(case when final_order_status='final' then 1 else 0 end) as final_order ,"
						+ "sum(case when final_order_status='appeal' then 1 else 0 end) as appeal_order ,"
						+ "sum(case when final_order_status='dismissed' then 1 else 0 end) as dismissed_order  "
						+ "from ecourts_case_data a "
						+ "left join apolcms.ecourts_olcms_case_details b on (b.cino=a.cino)"
						+ "inner join ecourts_case_finalorder ecf on (a.cino=ecf.cino)"
						+ "inner join district_mst dm on (dm.district_id=a.dist_id)"
						+ "inner join dept_new dn on (dn.dept_code=a.dept_code) where 1=1 "+condition+" "
						+ "group by dm.district_id,dm.district_name ";
						
			request.setAttribute("HEADING", "Dist. Wise Final Order Case processing Abstract Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("secdeptwise", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
			}
		 catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			
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
		String sql = null, sqlCondition = "", actionType = "", districtId = "", deptName = "", heading = "", roleId=null,deptCode=null, caseStatus=null,condition="";
		try {

			con = DatabasePlugin.connect();

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(cform.getDynaForm("deptId"));
			districtId = CommonModels.checkStringObject(cform.getDynaForm("districtId"));
			caseStatus = CommonModels.checkStringObject(cform.getDynaForm("caseStatus"));
			actionType = CommonModels.checkStringObject(cform.getDynaForm("actionType"));
			deptName = CommonModels.checkStringObject(cform.getDynaForm("deptName"));
			
			
			System.out.println("caseStatus--"+caseStatus);
			heading="Cases List for "+deptName;
			
			if(!caseStatus.equals("")) {
				if(caseStatus.equals("CLOSED")){
						sqlCondition+= " and coalesce(a.ecourts_case_status,'')='Closed' ";
						heading+=" Closed Cases List";
					}
				if(caseStatus.equals("PET")) {
					sqlCondition+=" and eocd.petition_document is not null and length(eocd.petition_document)>10 ";
					heading+=" Petition Documets Uploaded";
				}
				if(caseStatus.equals("COUNTERUPLOADED")) {
					sqlCondition+=" and eocd.counter_filed_document is not null  and length(eocd.counter_filed_document)>10  ";
					heading+=" Counter Uploaded Cases";
				}
				if(caseStatus.equals("PWRUPLOADED")) {
					sqlCondition+= " and eocd.pwr_uploaded_copy is not null  and length(eocd.pwr_uploaded_copy)>10 ";
					heading+=" Parawise Remarks Uploaded Cases List";
				}
				if(caseStatus.equals("GPCOUNTER")) {
					sqlCondition+=" and eocd.counter_approved_gp='Yes' ";
					heading+=" and Counters Filed";
				}
				if(caseStatus.equals("SCANNEDDOC")) {
					sqlCondition+=" and scanned_document_path is not null and length(scanned_document_path)>10 ";
					heading+=" and Documents Scanned at APOLCMS Cell, High Court";
				}
				if(caseStatus.equals("FINALORDER")) {
					sqlCondition+=" and final_order_status='final' ";
					heading+="  Final orders implemented";
				}
				if(caseStatus.equals("APPEALORDER")) {
					sqlCondition+=" and final_order_status='appeal' ";
					heading+="  Appeal Final orders";
				}
				if(caseStatus.equals("DISMISSEDORDER")) {
					sqlCondition+=" and final_order_status='dismissed' ";
					heading+="  Dismissed Final Orders";
				}
				if(caseStatus.equals("DC")) {
					sqlCondition+=" and  case_status=7 ";
					heading+="  District Collector Final Orders";
				}
				if(caseStatus.equals("DNO")) {
					sqlCondition+=" and case_status=8 ";
					heading+=" District Nodal officer Final Orders";
				}
				if(caseStatus.equals("DSEC")) {
					sqlCondition+=" and case_status=10 ";
					heading+="   secretariat Final Orders";
				}
			}
		
			if(roleId.equals("2")){
				sqlCondition+=" and a.dist_id='"+request.getSession().getAttribute("dist_id")+"'";
			}else if(roleId.equals("3") || roleId.equals("4") || roleId.equals("5") || roleId.equals("9"))
			{
				sqlCondition += " and (a.dept_code='" + deptCode + "') " ;
			}
			
			 if(cform.getDynaForm("distid") != null
						&& !CommonModels.checkStringObject(cform.getDynaForm("distid")).contentEquals("")) {
					sqlCondition+=" and a.dist_id='"+cform.getDynaForm("distid")+"'";
			   }
				
			 if (cform.getDynaForm("districtId") != null && !cform.getDynaForm("districtId").toString().contentEquals("")
						&& !cform.getDynaForm("districtId").toString().contentEquals("0")) {
					sqlCondition += " and a.dist_id='" + cform.getDynaForm("districtId").toString().trim() + "' ";
				}
				
				if (deptCode != null && !deptCode.toString().contentEquals("")
						&& !deptCode.toString().contentEquals("0")) {
					sqlCondition += " and a.dept_code='" + deptCode + "' ";
				}
				
				
				System.out.println("---"+cform.getDynaForm("distid"));
			
			sql = "select a.*, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a  "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1)"
					+ "  left join ecourts_olcms_case_details eocd on (a.cino=eocd.cino)  "
					+ " inner join ( select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths "
					+ " from  (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null "
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0 and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) c group by cino ) b on (a.cino=b.cino)"
					+ " inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true   "+sqlCondition+"    ";
			
			
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