package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import plugins.DatabasePlugin;

public class LegalCellCaseMappingAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, empId = null, empSection = null, empPost = null, condition="", deptId="", deptCode="", distId="";

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			
			deptId = CommonModels.checkStringObject(session.getAttribute("dept_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));

			empId = CommonModels.checkStringObject(session.getAttribute("empId"));
			empSection = CommonModels.checkStringObject(session.getAttribute("empSection"));
			empPost = CommonModels.checkStringObject(session.getAttribute("empPost"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			Date curDate = new Date();
		      SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		      
		      String stringDate = format.format(curDate);
		      System.out.println(stringDate);
		      
		      format = new SimpleDateFormat("dd/M/yyyy");
		      stringDate = format.format(curDate);
		      System.out.println("---"+stringDate);

			
			if (cform.getDynaForm("dofFromDate") != null
					&& !cform.getDynaForm("dofFromDate").toString().contentEquals("")) {
				stringDate += " and causelist_date >= to_date('" + cform.getDynaForm("dofFromDate")
				+ "','dd-mm-yyyy') ";
			}
			/*
			 * sql =
			 * "select  distinct a.cino,type_name_fil,reg_no,reg_year,date_of_filing,coram,pet_name,dist_name,pet_adv,res_adv,b.response,case_status,a.assigned_to,c.inserted_on,"
			 * +
			 * "case when case_status='19' and response is null then 'Pending For Response'  else 'Completed' end as status  from ecourts_case_data a  "
			 * +
			 * "left JOIN ecourts_case_activities c ON (c.cino=a.cino  and c.inserted_by=a.assigned_to  )"
			 * + "left join ecourts_response_ag_mst b on (a.cino=b.cino)   " +
			 * "where  case_status in ('18','19')  and coalesce(a.ecourts_case_status,'')!='Closed'  order by c.inserted_on asc"
			 * ;
			 */
			
			sql= " select distinct c.cino,c.dept_code,c.type_name_reg,c.reg_no,c.reg_year,d.prayer, a.est_code , a. causelist_date , a.bench_id , a. causelist_id , cause_list_type ,coalesce(causelist_document,'') as document, b.judge_name "
					+ "from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) inner join  ecourts_case_data c on (a.bench_id=c.bench_id)  and (a.cause_list_type=c.causelist_type)  "
					+ " inner join nic_prayer_data d  on (c.cino=d.cino) "
					+ "where  a.causelist_date=to_date('1/9/2022','dd/mm/yyyy')  limit 5    ";   //a.causelist_date=to_date('"+stringDate+"','dd/mm/yyyy')-1
			
			System.out.println("AssignedCasesToSectionAction unspecified SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("CASESLIST", data);
				request.setAttribute("HEADING", " Cases Assigned");
			} else {
				request.setAttribute("errorMsg", "You have Zero cases to Process.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

}