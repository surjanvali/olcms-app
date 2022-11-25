package in.apcfss.struts.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.SendSMSAction;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import plugins.DatabasePlugin;

public class DailyStatusEntryByGPAction extends DispatchAction {

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
					.getSelectBox("select purpose_name,purpose_name from apolcms.ecourts_case_data where dept_code='"
									+ session.getAttribute("dept_code") + "' group by purpose_name order by 1",con));

			cform.setDynaForm("ShowDefault", "ShowDefault");

			request.setAttribute("SHOWMESG", "SHOWMESG");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
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
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="",userid="",condition="";;
		try {
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
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
			 * if (cform.getDynaForm("dofToDate") != null &&
			 * !cform.getDynaForm("dofToDate").toString().contentEquals("")) { sqlCondition
			 * += " and causelist_date <= to_date('" + cform.getDynaForm("dofToDate") +
			 * "','dd-mm-yyyy') "; }
			 */
			
			sql= " select distinct c.cino,c.dept_code,c.type_name_reg,c.reg_no,c.reg_year,d.prayer, a.est_code , a. causelist_date , a.bench_id , a. causelist_id , "
					+ " cause_list_type ,coalesce(causelist_document,'') as document, b.judge_name,(select description from dept_new dn where dn.dept_code=c.dept_code)  "
					+ "from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) "
					+ "inner join  ecourts_case_data c on (a.bench_id=c.bench_id)  and (a.cause_list_type=c.causelist_type)  inner join nic_prayer_data d  on (c.cino=d.cino) "
					+ " left join ecourts_causelist_cases e on (e.causelist_date=b.causelist_date) and (e.case_no=c.type_name_reg||'/'||c.reg_no||'/'||c.reg_year)  "
					+ "where a.causelist_date=to_date('"+stringDate+"','dd/mm/yyyy')-1   and assigned_to='"+userid+"'   ";   //'"+stringDate+"'

			System.out.println("ecourts SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			// System.out.println("data=" + data);
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
	public ActionForward getSubmitCategoryNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;int a=0;String cIno = null;
		try {
			con = DatabasePlugin.connect();
			//con.setAutoCommit(false);
			request.setAttribute("HEADING", "Instructions Entry");
			System.out.println("in assign2DeptHOD --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = CommonModels.checkStringObject(request.getSession().getAttribute("userid"));
			String caseType = "New";
			cIno = CommonModels.checkStringObject(cform.getDynaForm("cino"));
			System.out.println("cIno---"+cIno);
			System.out.println("caseType---"+caseType);

			FileUploadUtilities fuu = new FileUploadUtilities();
			FormFile myDoc;

			myDoc = cform.getChangeLetter();

			System.out.println("myDoc---"+myDoc);
			String filePath="uploads/DailyStatus/";
			String newFileName="DailyStatus_"+CommonModels.randomTransactionNo();
			String DailyStatus_file = fuu.saveFile(myDoc, filePath, newFileName);

			System.out.println("pdfFile--"+DailyStatus_file);

			sql = "insert into ecourts_gpo_daily_status (cino, status_remarks , upload_fileno,dept_code ,dist_code,insert_by,legacy_ack_flag ) "
					+ " values (?,?, ?, ?, ?, ?,?)";

			ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i, cIno);
			ps.setString(++i, cform.getDynaForm("daily_status") != null ? cform.getDynaForm("daily_status").toString() : "");
			ps.setString(++i, DailyStatus_file);
			ps.setString(++i, CommonModels.checkStringObject(session.getAttribute("dept_code")));
			ps.setInt(++i, CommonModels.checkIntObject(session.getAttribute("dist_id")));
			ps.setString(++i, userId);
			ps.setString(++i, "New");

			System.out.println("sql--"+sql);

			a = ps.executeUpdate();

			System.out.println("a--->"+a);
			if(a>0) {

				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks,uploaded_doc_path) "
						+ " values ('" + cIno + "','SUBMITTED DAILY CASE STATUS', '"+userId+"', '"+request.getRemoteAddr()+"', '"+cform.getDynaForm("daily_status").toString()+"','"+DailyStatus_file+"')";
				DatabasePlugin.executeUpdate(sql, con);

				request.setAttribute("successMsg", "Dialy Status details saved successfully.");
			}else {
				request.setAttribute("errorMsg", "Error in submission. Kindly try again.");
			}

		} catch (Exception e) {
			//con.rollback();
			request.setAttribute("errorMsg", "Error in Submission. Kindly try again.");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("daily_status","");
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.close(con, ps, null);
		}
		return getCino(mapping, cform, request, response);
	}
	
	public ActionForward getSubmitCategoryLegacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = null;//, cIno=null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;int a=0;String cIno = null;
		try {
			con = DatabasePlugin.connect();
			//con.setAutoCommit(false);
			request.setAttribute("HEADING", "Instructions Entry");
			System.out.println("in assign2DeptHOD --- DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd");
			userId = CommonModels.checkStringObject(request.getSession().getAttribute("userid"));
			String caseType = "Legacy";
			cIno = CommonModels.checkStringObject(cform.getDynaForm("cino"));
			System.out.println("cIno---"+cIno);
			System.out.println("caseType---"+caseType);

			FileUploadUtilities fuu = new FileUploadUtilities();
			FormFile myDoc;

			myDoc = cform.getChangeLetter();

			System.out.println("myDoc---"+myDoc);
			String filePath="uploads/DailyStatus/";
			String newFileName="DailyStatus_"+CommonModels.randomTransactionNo();
			String DailyStatus_file = fuu.saveFile(myDoc, filePath, newFileName);

			System.out.println("pdfFile--"+DailyStatus_file);

			sql = "insert into ecourts_gpo_daily_status (cino, status_remarks , upload_fileno,dept_code ,dist_code,insert_by,legacy_ack_flag ) "
					+ " values (?,?, ?, ?, ?, ?,?)";

			ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i, cIno);
			ps.setString(++i, cform.getDynaForm("daily_status") != null ? cform.getDynaForm("daily_status").toString() : "");
			ps.setString(++i, DailyStatus_file);
			ps.setString(++i, CommonModels.checkStringObject(session.getAttribute("dept_code")));
			ps.setInt(++i, CommonModels.checkIntObject(session.getAttribute("dist_id")));
			ps.setString(++i, userId);
			ps.setString(++i, "Legacy");

			System.out.println("sql--"+sql);

			a = ps.executeUpdate();

			System.out.println("a--->"+a);
			if(a>0) {

				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks,uploaded_doc_path) "
						+ " values ('" + cIno + "','SUBMITTED DAILY CASE STATUS', '"+userId+"', '"+request.getRemoteAddr()+"', '"+cform.getDynaForm("daily_status").toString()+"','"+DailyStatus_file+"')";
				DatabasePlugin.executeUpdate(sql, con);


				request.setAttribute("successMsg", "Dialy Status details saved successfully.");
			}else {
				request.setAttribute("errorMsg", "Error in submission. Kindly try again.");
			}

		} catch (Exception e) {
			//con.rollback();
			request.setAttribute("errorMsg", "Error in Submission. Kindly try again.");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("daily_status","");
			cform.setDynaForm("fileCino", cIno);
			DatabasePlugin.close(con, ps, null);
		}
		return getCino(mapping, cform, request, response);
	}

	public ActionForward getCino(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, cIno = null, target = "DailyStatusEntryByGPView",caseType=null;
		System.out.println("getCino");

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			caseType = CommonModels.checkStringObject(request.getParameter("caseType"));
			List<Map<String, Object>> data=null;
			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			cIno = cIno!=null && !cIno.equals("") ? cIno : CommonModels.checkStringObject(cform.getDynaForm("fileCino"));

			System.out.println("cIno" + cIno);
			System.out.println("caseType---" + caseType);

			if (cIno != null && !cIno.equals("")) {

				cform.setDynaForm("cino", cIno);

				con = DatabasePlugin.connect();

				if (caseType.equals("Legacy")) {

					sql = "select a.*, "
							+ " nda.fullname_en as fullname,'Legacy' as legacy_ack_flag , nda.designation_name_en as designation, nda.post_name_en as post_name, nda.email, nda.mobile1 as mobile,dim.district_name , "
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
							+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true and a.cino='"+cIno+"' ";


					System.out.println("ecourts SQL:" + sql);
					data = DatabasePlugin.executeQuery(sql, con);
					if (data != null && !data.isEmpty() && data.size() > 0) {
						request.setAttribute("CASESLISTOLD", data);
						cform.setDynaForm("cino", ((Map) data.get(0)).get("cino"));

						sql = "select instructions,to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(upload_fileno,'-') as upload_fileno "
								+ " from ecourts_dept_instructions where cino='" + cIno + "' and legacy_ack_flag='Legacy'  order by 1 ";
						System.out.println("sql--" + sql);
						List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
						request.setAttribute("existData", existData);

					} else {
						request.setAttribute("errorMsg", "No Records Found");
					}
				}else {

					sql = "select a.slno ,ad.respondent_slno, a.ack_no,'New' as legacy_ack_flag , distid , advocatename ,advocateccno , casetype , maincaseno , a.remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
							+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
							+ "to_char(a.inserted_time,'dd-mm-yyyy') as generated_date, "
							+ "getack_dept_desc(a.ack_no::text) as dept_descs , coalesce(a.hc_ack_no,'-') as hc_ack_no "
							+ " from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
							+ " left join district_mst dm on (ad.dist_id=dm.district_id) "
							+ " left join dept_new dmt on (ad.dept_code=dmt.dept_code)"
							+ " inner join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name)  "
							+ " where a.delete_status is false and ack_type='NEW'    and (a.ack_no='"+cIno+"' or a.hc_ack_no='"+cIno+"' )  and respondent_slno='1'   "
							+ " order by a.inserted_time desc";

					System.out.println("ecourts SQL:" + sql);
					data = DatabasePlugin.executeQuery(sql, con);
					if (data != null && !data.isEmpty() && data.size() > 0) {
						request.setAttribute("CASESLISTNEW", data);

						cform.setDynaForm("cino", cIno);
						//	request.setAttribute("cinooo", ackNoo);
						sql = "select instructions,to_char(insert_time,'dd-mm-yyyy HH:mi:ss') as insert_time,coalesce(upload_fileno,'-') as upload_fileno "
								+ " from ecourts_dept_instructions where cino='" + cIno + "' and legacy_ack_flag='New'  order by 1 ";
						System.out.println("sql--" + sql);
						List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
						request.setAttribute("existDataNew", existData);

					} else {
						request.setAttribute("errorMsg", "No Records Found");
					}
				}
				request.setAttribute("HEADING", "Submit status for CINO : " + cIno);
			} else {
				request.setAttribute("errorMsg", "Invalid Cino.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward(target);
	}
}