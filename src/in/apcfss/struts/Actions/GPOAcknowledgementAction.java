package in.apcfss.struts.Actions;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.Itext_pdf_setting;
import plugins.DatabasePlugin;

public class GPOAcknowledgementAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			System.out.println("" + sdf.format(new Date()));
			cform.setDynaForm("ackDate", sdf.format(new Date()));
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);
	}

	public ActionForward getAcknowledementsList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................getAcknowledementsList()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		String ackDate="";
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			String ackType = "NEW";
					//cform.getDynaForm("ackType") !=null && !cform.getDynaForm("ackType").equals("0") ? cform.getDynaForm("ackType").toString() : "NEW";

			/*sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,services_flag,"
					+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs, a.barcode_file_path, to_char(inserted_time,'dd-mm-yyyy') as generated_date, reg_year, reg_no, ack_type "
					+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
					+ "left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ "left join (select ack_no,dm.dept_code,dm.description from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
					+ "where a.inserted_by='"+session.getAttribute("userid")
					+"' and a.delete_status is false and ack_type='"+ackType+"' and inserted_time::date=current_date "
					+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
					+ "case_full_name,a.ack_file_path, services_id, services_flag, inserted_time, a.barcode_file_path, reg_year, reg_no, ack_type "
					+ "order by inserted_time desc";*/
			
			sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ " upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag,"
					+ " STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description||'-'||servicetpye||case when coalesce(gd.dept_category,'0')!='0' then '-'||gd.dept_category else '' end ,', ') as dept_descs,a.barcode_file_path, to_char(inserted_time,'dd-mm-yyyy') as generated_date,STRING_AGG(gd.servicetpye,',')  as servicetpye,STRING_AGG(gd.designation,',')  as designation "
					+ " , mode_filing, case_category, coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
					+ " left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ " left join (select ack_no,dm.dept_code,dm.description, respondent_slno,servicetpye,designation, coalesce(dept_category,'0') as dept_category from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code) order by ack_no, respondent_slno) gd on (a.ack_no=gd.ack_no)"
					+ " where a.inserted_by='"+session.getAttribute("userid")
					+"' and a.delete_status is false and ack_type='"+ackType+"' and inserted_time::date=current_date"
					+ " group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
					+ " case_full_name,a.ack_file_path, services_id,services_flag, inserted_time,a.barcode_file_path, reg_year, reg_no, ack_type, a.mode_filing, a.case_category, a.hc_ack_no "
					+ " order by inserted_time desc";
			
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("ACKDATA", data);
			} else {
				return displayAckForm(mapping, cform, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward getAcknowledementsListAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................getAcknowledementsListAll()");
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
			String ackType = CommonModels.checkStringObject(request.getParameter("ackType"));
			String ackDate=request.getParameter("ackDate").toString();
			// cform.getDynaForm("ackType") !=null && !cform.getDynaForm("ackType").equals("0") ? cform.getDynaForm("ackType").toString() : "NEW";

			sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ " upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,services_flag,"
					+ " STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description||'-'||servicetpye||case when coalesce(gd.dept_category,'0')!='0' then '-'||gd.dept_category else '' end ,', ') as dept_descs,"
					+ " a.barcode_file_path, to_char(inserted_time,'dd-mm-yyyy') as generated_date, reg_year, reg_no, ack_type,STRING_AGG(gd.servicetpye,',')  as servicetpye,STRING_AGG(gd.designation,',')  as designation "
					+ " , mode_filing, case_category, coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ ""
					+ "from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
					+ " left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					//+ "left join (select ack_no,dm.dept_code,dm.description,servicetpye,designation, coalesce(dept_category,'Normal') as dept_category from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
					+ " left join (select ack_no,dm.dept_code,dm.description, respondent_slno,servicetpye,designation, coalesce(dept_category,'Normal') as dept_category from ecourts_gpo_ack_depts "
					+ " inner join dept_new dm using (dept_code) order by ack_no, respondent_slno) gd on (a.ack_no=gd.ack_no)"
					+ " where a.inserted_by='"+session.getAttribute("userid")
					+"' and a.delete_status is false";
			
			if(!CommonModels.checkStringObject(ackType).equals(""))
				sql+=" and ack_type='"+ackType+"'";
			
			sql+=" and to_char(inserted_time::date,'dd-mm-yyyy')='"+ackDate+"'"
					+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
					+ "case_full_name,a.ack_file_path, services_id, services_flag, inserted_time, a.barcode_file_path, reg_year, reg_no, ack_type,a.mode_filing,a.case_category "
					+ "order by inserted_time desc";
			
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				
				if(ackType.equals("OLD")) {
					request.setAttribute("DISPLAYOLD", "DISPLAYOLD");
				}
				request.setAttribute("HEADING", "Acknowledgements Generated on Dt.:"+ackDate);
				request.setAttribute("ACKDATA", data);
			} else {
				return displayAckForm(mapping, cform, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward displayAckForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................displayAckForm()");
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

			request.setAttribute("saveAction", "INSERT");

			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox( "select  sno,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno", con));
			cform.setDynaForm("serviceTypesList", DatabasePlugin.getSelectBox( "select  service_desc,upper(trim(service_desc)) as service_desc from ecourts_mst_services order by 1", con));
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					//"select dept_id,sdeptcode||deptcode||'-'||upper(trim(description)) as description from dept order by sdeptcode||deptcode",
					"select  dept_code as dept_code,dept_code||' - '||upper(description) as description from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by trim(district_name)",
					con));
			cform.setDynaForm("mdlList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by trim(district_name)",
					con));
			cform.setDynaForm("vilList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by trim(district_name)",
					con));
			cform.setDynaForm("designationList", DatabasePlugin.getSelectBox(
					"select distinct designation_id,upper(trim(designation_name_en)) designation_name_en from nic_Data where designation_id is not null and designation_id!='' and designation_name_en  is not null  and designation_name_en!='' order by 2",
					con));
			cform.setDynaForm("gpsList", DatabasePlugin.getSelectBox(
					"select emailid,first_name||' '||last_name||' - '||designation from ecourts_mst_gps",
					con));
			if(CommonModels.checkStringObject(cform.getDynaForm("ackType")).equals("") || CommonModels.checkStringObject(cform.getDynaForm("ackType")).equals("0")) {
				cform.setDynaForm("ackType", "NEW");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward displayAckEditForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

			String ackNo = cform.getDynaForm("ackId") != null ? cform.getDynaForm("ackId").toString() : "";
			if (ackNo != null && !ackNo.contentEquals("")) {
				
				sql = "select slno , ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name,"
						//+ "depm.description as dept_name,"
						+ "upper(trim(case_full_name)) as  case_full_name   from ecourts_gpo_ack_dtls a "
						+ "left join district_mst dm on (a.distid=dm.district_id)"
						//+ "left join dept depm on (a.deptid=depm.dept_id)"
						+ "left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) where a.inserted_by='"
						+ session.getAttribute("userid") + "' and ack_no='"+ackNo+"'";
				/*
				sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,"
						+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs "
						+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
						+ "left join case_type_master cm on (a.casetype=cm.sno) "
						+ "left join (select ack_no,dm.dept_code,dm.description from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
						+ "where a.inserted_by='"+session.getAttribute("userid")+"' and a.delete_status is false "
						+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
						+ "case_full_name,a.ack_file_path, services_id, inserted_time"
						+ "order by district_name, inserted_time";
				*/
				
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map<String, Object> ackData=(Map<String, Object>)data.get(0);
					
					cform.setDynaForm("ackId", ackNo);
					cform.setDynaForm("distId", ackData.get("distid").toString());
					// cform.setDynaForm("deptId", ackData.get("deptid").toString());
					cform.setDynaForm("advocateName", ackData.get("advocatename").toString());
					cform.setDynaForm("advocateCCno", ackData.get("advocateccno").toString());
					cform.setDynaForm("caseType", ackData.get("casetype").toString());
					cform.setDynaForm("mainCaseNo", ackData.get("maincaseno").toString());
					cform.setDynaForm("remarks", ackData.get("remarks").toString());
					
				} else {
					
					return displayAckForm(mapping, cform, request, response);
				}
				
				cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox(
						"select  sno,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno",
						con));
				cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
						"select  dept_code,dept_code||'-'||upper(trim(description)) as description from dept_new where display=true order by dept_code",
						con));
				cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
						"select district_id,upper(trim(district_name)) as district_name from district_mst order by trim(district_name)",
						con));
			} else {
				request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
				return getAcknowledementsList(mapping, cform, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("saveAction", "UPDATE");
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward saveAckDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

			if (isTokenValid(request)) {

				con = DatabasePlugin.connect();
				con.setAutoCommit(false);
				String hcAckNo=null;
				String ackNo = null;//generateNewAckNo();
				int respondentIds = CommonModels.checkIntObject(cform.getDynaForm("respondentIds"));
				
				int distId = 0;
				String deptId="";
				distId = Integer.parseInt(cform.getDynaForm("distId").toString());
				 
				for(int respondentId=1; respondentId <= respondentIds; respondentId++) {
					
					 //distId = Integer.parseInt(cform.getDynaForm("distId"+respondentId).toString());
					 deptId = CommonModels.checkStringObject(cform.getDynaForm("deptId"+1));
				}
				
				
				
				System.out.println("distId===="+distId);
				//System.out.println("deptId===="+deptId);
				
				// != null ? Integer.parseInt(cform.getDynaForm("distId").toString()) : 0;
				//System.out.println("deptIds:"+cform.getDeptId());
				//String deptCodes[] = cform.getDeptId() != null ? cform.getDeptId() : null;
				// != null ? Integer.parseInt(cform.getDynaForm("deptId1").toString()) : 0;
				
				if(distId>0 && deptId!=null && !deptId.equals("") && !deptId.equals("0")) {
					sql="select '"+deptId+"'||lpad('"+distId+"'::text,2,'0')||to_char(now(),'yyyyMMddhhmiSSms')";
					ackNo = DatabasePlugin.getStringfromQuery(sql, con);
				// ackNo = DatabasePlugin.getStringfromQuery("select sdeptcode||deptcode||lpad('"+distId+"'::text,2,'0')||to_char(now(),'yyyyMMddmmssms') from dept where dept_id='"+deptId+"'", con);
				
					sql="select '"+deptId.substring(0, 5)+"'||LPAD(nextval ('ecourts_gpo_hc_ack_gen_seq')::TEXT, 7,'0')";
					hcAckNo = DatabasePlugin.getStringfromQuery(sql, con);
				}
				System.out.println("ackNo--"+ackNo+hcAckNo);
				/*
				 * String designation =
				 * CommonModels.checkStringObject(cform.getDynaForm("deptId1")); if(distId>0 &&
				 * deptId!=null && !deptId.equals("") && !deptId.equals("0")) {
				 * sql="select '"+deptId+"'||lpad('"+distId+
				 * "'::text,2,'0')||to_char(now(),'yyyyMMddhhmiSSms')"; ackNo =
				 * DatabasePlugin.getStringfromQuery(sql, con); }
				 */
				
				if (ackNo != null && !ackNo.contentEquals("") && hcAckNo != null && !hcAckNo.contentEquals("")) {

					int i = 1;
					sql = "insert into ecourts_gpo_ack_dtls (ack_no, distid, petitioner_name, advocatename ,advocateccno ,casetype , maincaseno , remarks ,  " //casetype
							+ "inserted_by , inserted_ip , ack_type, reg_year, reg_no, mode_filing, case_category, hc_ack_no)"  //,designation,mandalid,villageid
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";//,?,?,?    , ?, ?
					System.out.println("sql--"+sql);
					ps = con.prepareStatement(sql);
					ps.setString(i, ackNo);
					ps.setInt(++i, distId);
					ps.setString(++i, cform.getDynaForm("petitionerName") != null ? cform.getDynaForm("petitionerName").toString() : "");
					ps.setString(++i, cform.getDynaForm("advocateName") != null ? cform.getDynaForm("advocateName").toString() : "");
					ps.setString(++i, cform.getDynaForm("advocateCCno") != null ? cform.getDynaForm("advocateCCno").toString() : "");
					ps.setInt(++i, Integer.parseInt( cform.getDynaForm("caseType") != null ? cform.getDynaForm("caseType").toString() : "0"));
					ps.setString(++i, cform.getDynaForm("mainCaseNo") != null ? cform.getDynaForm("mainCaseNo").toString() : "");
					ps.setString(++i, cform.getDynaForm("remarks") != null ? cform.getDynaForm("remarks").toString() : "");
					ps.setString(++i, session.getAttribute("userid").toString());
					ps.setString(++i, request.getRemoteAddr());
					//ps.setString(++i, cform.getDynaForm("serviceType1") != null ? cform.getDynaForm("serviceType1").toString() : "");
					//ps.setString(++i, cform.getDynaForm("serviceType1") != null ? cform.getDynaForm("serviceType1").toString() : "");
					ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("ackType")));
					ps.setInt(++i, CommonModels.checkIntObject(cform.getDynaForm("regYear")));
					//ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("designation1")));
					//ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("mandalId1")));
					//ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("villageId1")));
					ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("regNo")));
					ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("filingMode")));
					ps.setString(++i, CommonModels.checkStringObject(cform.getDynaForm("caseCategory")));
					ps.setString(++i, hcAckNo);
					
					
					int a = ps.executeUpdate();
					
					 respondentIds = CommonModels.checkIntObject(cform.getDynaForm("respondentIds"));
					if(respondentIds > 0) {
						
						ps.close();
						sql="insert into ecourts_gpo_ack_depts (ack_no, dept_code, respondent_slno, servicetpye, dept_category"
								// + ",designation,mandalid,villageid"
								+ ") values (?,?,?,?,?"
								// + ",?,?,?"
								+ ")";
						ps = con.prepareStatement(sql);
						
						for(int respondentId=1; respondentId <= respondentIds; respondentId++) {
							i=1;
							if(respondentId > 0) {
								ps.setString(i, ackNo);
								ps.setString(++i, cform.getDynaForm("deptId"+respondentId).toString());
								ps.setInt(++i, respondentId);
								/*ps.setString(++i, cform.getDynaForm("designation"+respondentId).toString());
								ps.setString(++i, cform.getDynaForm("mandalId"+respondentId).toString());
								ps.setString(++i, cform.getDynaForm("villageId"+respondentId).toString());
								*/
								ps.setString(++i, cform.getDynaForm("serviceType"+respondentId).toString());
								ps.setString(++i, cform.getDynaForm("deptCategory"+respondentId).toString());
								ps.addBatch();
							}
						}
						ps.executeBatch();
					}
					
					if (a > 0) {
						
						sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
								+ " upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag,"
								+ " STRING_AGG(gd.dept_code,',') as dept_codes, STRING_AGG(gd.description||'-'||servicetpye||case when coalesce(gd.dept_category,'0')!='0' then '-'||gd.dept_category else '' end ,', ') as dept_descs, to_char(inserted_time,'dd-mm-yyyy') as generated_date,STRING_AGG(gd.servicetpye,',')  as servicetpye,STRING_AGG(gd.designation,',')  as designation "
								+ " , mode_filing, case_category from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
								+ " left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
								+ " left join (select ack_no,dm.dept_code,dm.description, respondent_slno,servicetpye,designation, coalesce(dept_category,'Normal') as dept_category from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code) order by ack_no, respondent_slno) gd on (a.ack_no=gd.ack_no)"
								+ " where a.inserted_by='"+session.getAttribute("userid")
								+"' and a.delete_status is false and a.ack_no='"+ackNo+"'"
								+ " group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
								+ " case_full_name,a.ack_file_path, services_id,services_flag, inserted_time, a.mode_filing, a.case_category "
								+ " order by district_name, inserted_time";
						
						System.out.println("SQL:" + sql);
						List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
						System.out.println("data=" + data);
						if (data != null && !data.isEmpty() && data.size() > 0) {
							
							Map<String, Object> ackData=(Map<String, Object>)data.get(0);
							
							cform.setDynaForm("ackId", ackNo);
							cform.setDynaForm("distId", ackData.get("distid").toString());
							//cform.setDynaForm("deptId", ackData.get("deptid").toString());
							cform.setDynaForm("advocateName", ackData.get("advocatename").toString());
							cform.setDynaForm("advocateCCno", ackData.get("advocateccno").toString());
							cform.setDynaForm("caseType", ackData.get("casetype").toString());
							cform.setDynaForm("caseTypeName", ackData.get("case_full_name").toString());
							cform.setDynaForm("mainCaseNo", ackData.get("maincaseno").toString());
							cform.setDynaForm("remarks", ackData.get("remarks").toString());
							cform.setDynaForm("dept_name", ackData.get("dept_descs").toString());
							cform.setDynaForm("district_name", ackData.get("district_name").toString());
							cform.setDynaForm("serviceType", ackData.get("servicetpye").toString());
							cform.setDynaForm("serviceNonService", ackData.get("services_flag").toString());
							cform.setDynaForm("generatedDate", ackData.get("generated_date").toString());
							cform.setDynaForm("filingMode", ackData.get("mode_filing").toString());
							
						}
						
						String ackPath = generateAckPdf(ackNo, cform);
						String barCodeFilePath = generateAckBarCodePdf128(hcAckNo, cform);
						System.out.println("ackPath::"+ackPath);
						System.out.println("barCodeFilePath::"+barCodeFilePath);
						
						if(ackPath!=null && !ackPath.equals("")){
							sql=" update ecourts_gpo_ack_dtls set ack_file_path='"+ackPath+"', barcode_file_path='"+barCodeFilePath+"' where ack_no='"+ackNo+"'";
							System.out.println("---SQL::"+sql);
							
							int b =DatabasePlugin.executeUpdate(sql, con);
							if(b > 0) {
							request.setAttribute("successMsg",
									"Acknowledgement details saved successfully with Ack No.:" + ackNo);
							
							con.commit();
							}
							else {
								request.setAttribute("errorMsg", "Failed to save Data. Kindly try again.");
							}
						}else {
							request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
						}
						
					} else
						request.setAttribute("errorMsg",
								"Error in saving Acknowledgement details. Kindly try again with valid data.");
					request.setAttribute("saveAction", "INSERT");
				} else {
					request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
				}
			} else {
				request.setAttribute("errorMsg",
						"Error: Submitting duplicate Acknowledgement details. Kindly try again with valid data.");
			}
			
			if(CommonModels.checkStringObject(cform.getDynaForm("ackType")).equals("OLD")) {
				return mapping.findForward("showOldAckList");
			}
			
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg",
					"Exception Occurred while saving Acknowledgement details. Kindly try again with valid data.");
			request.removeAttribute("successMsg");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return unspecified(mapping, cform, request, response);
	}

	public ActionForward updateAckDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			String ackNo = cform.getDynaForm("ackId") != null ? cform.getDynaForm("ackId").toString() : "";

			if (ackNo != null && !ackNo.contentEquals("")) {

				int i = 1;
				sql = "update ecourts_gpo_ack_dtls set distid=?, deptid=? , advocatename=? ,advocateccno=? , casetype=? , maincaseno=? , remarks=? where ack_no=? ";

				ps = con.prepareStatement(sql);

				ps.setInt(i, Integer
						.parseInt(cform.getDynaForm("distId") != null ? cform.getDynaForm("distId").toString() : "0"));
				ps.setInt(++i, Integer
						.parseInt(cform.getDynaForm("deptId") != null ? cform.getDynaForm("deptId").toString() : "0"));

				ps.setString(++i,
						cform.getDynaForm("advocateName") != null ? cform.getDynaForm("advocateName").toString() : "");
				ps.setString(++i,
						cform.getDynaForm("advocateCCno") != null ? cform.getDynaForm("advocateCCno").toString() : "");

				ps.setInt(++i, Integer.parseInt(
						cform.getDynaForm("caseType") != null ? cform.getDynaForm("caseType").toString() : "0"));

				ps.setString(++i,
						cform.getDynaForm("mainCaseNo") != null ? cform.getDynaForm("mainCaseNo").toString() : "");
				ps.setString(++i, cform.getDynaForm("remarks") != null ? cform.getDynaForm("remarks").toString() : "");
				ps.setString(++i, ackNo);

				int a = ps.executeUpdate();

				if (a > 0) {

					request.setAttribute("successMsg", "Ack No.:" + ackNo + " details updated successfully.");
				} else
					request.setAttribute("errorMsg",
							"Error in saving Acknowledgement details. Kindly try again with valid data.");
			} else {
				request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception Occurred while saving Acknowledgement details. Kindly try again with valid data.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);

	}

	public ActionForward deleteAck(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			con.setAutoCommit(false);
			
			String ackNo = cform.getDynaForm("ackId") != null ? cform.getDynaForm("ackId").toString() : "";

			if (ackNo != null && !ackNo.contentEquals("")) {
				
				sql="insert into ecourts_gpo_ack_depts_log select * from ecourts_gpo_ack_depts where ack_no='"+ackNo+"'";
				int a = DatabasePlugin.executeUpdate(sql, con);
				
				sql="insert into ecourts_gpo_ack_dtls_log select * from ecourts_gpo_ack_dtls  where ack_no='"+ackNo+"'";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from ecourts_gpo_ack_depts  where ack_no='"+ackNo+"'";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				sql="delete from ecourts_gpo_ack_dtls  where ack_no='"+ackNo+"'";
				a += DatabasePlugin.executeUpdate(sql, con);
				
				if (a > 0) {
					con.commit();
					request.setAttribute("successMsg", "Ack No.:" + ackNo + " deleted successfully.");
				} else {
					con.rollback();
					request.setAttribute("errorMsg", "Error in deleting Ack No.:" + ackNo);
				}
			} else {
				request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
			}
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Exception Occurred while deletion.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);
	}

	public static String generateNewAckNo() {
		String ackNoNew = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmissms");
		System.out.println("" + sdf.format(new Date()));
		ackNoNew = sdf.format(new Date());
		return ackNoNew;
	}

	public ActionForward downloadAckPDF(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("GPOAcknowledgementAction..............................................................................unspecified()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null || session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			String ackNo = cform.getDynaForm("ackId") != null ? cform.getDynaForm("ackId").toString() : "";
			if (ackNo != null && !ackNo.contentEquals("")) {
				con = DatabasePlugin.connect();
				
				/*sql = "select slno , ack_no , distid ,  advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , " +
						"inserted_ip, upper(trim(district_name)) as district_name,"
						//+ "depm.description as dept_name,"
						+ "upper(trim(case_full_name)) as  case_full_name   from ecourts_gpo_ack_dtls a "
						+ "left join district_mst dm on (a.distid=dm.district_id)"
						//+ "left join dept depm on (a.deptid=depm.dept_id)"
						+ "left join case_type_master cm on (a.casetype=cm.sno) where a.inserted_by='"
						+ session.getAttribute("userid") + "' and ack_no='"+ackNo+"'";
				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map<String, Object> ackData=(Map<String, Object>)data.get(0);
					
					cform.setDynaForm("ackId", ackNo);
					cform.setDynaForm("distId", ackData.get("distid").toString());
					//cform.setDynaForm("deptId", ackData.get("deptid").toString());
					cform.setDynaForm("advocateName", ackData.get("advocatename").toString());
					cform.setDynaForm("advocateCCno", ackData.get("advocateccno").toString());
					cform.setDynaForm("caseType", ackData.get("casetype").toString());
					cform.setDynaForm("caseTypeName", ackData.get("case_full_name").toString());
					cform.setDynaForm("mainCaseNo", ackData.get("maincaseno").toString());
					cform.setDynaForm("remarks", ackData.get("remarks").toString());
					//cform.setDynaForm("dept_name", ackData.get("dept_name").toString());
					cform.setDynaForm("district_name", ackData.get("district_name").toString());
				}
				*/
				/*sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,"
						+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs "
						+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
						+ "left join case_type_master cm on (a.casetype=cm.sno) "
						+ "left join (select ack_no,dm.dept_code,dm.description from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
						+ "where a.inserted_by='"+session.getAttribute("userid")
						+"' and a.delete_status is false and a.ack_no='"+ackNo+"'"
						+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
						+ "case_full_name,a.ack_file_path, services_id, inserted_time "
						+ "order by district_name, inserted_time";
				
				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map<String, Object> ackData=(Map<String, Object>)data.get(0);
					
					cform.setDynaForm("ackId", ackNo);
					cform.setDynaForm("distId", ackData.get("distid").toString());
					//cform.setDynaForm("deptId", ackData.get("deptid").toString());
					cform.setDynaForm("advocateName", ackData.get("advocatename").toString());
					cform.setDynaForm("advocateCCno", ackData.get("advocateccno").toString());
					cform.setDynaForm("caseType", ackData.get("casetype").toString());
					cform.setDynaForm("caseTypeName", ackData.get("case_full_name").toString());
					cform.setDynaForm("mainCaseNo", ackData.get("maincaseno").toString());
					cform.setDynaForm("remarks", ackData.get("remarks").toString());
					cform.setDynaForm("dept_name", ackData.get("dept_descs").toString());
					cform.setDynaForm("district_name", ackData.get("district_name").toString());
					cform.setDynaForm("serviceType", ackData.get("services_id").toString()); }*/
					
				
				sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
						+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,services_flag,"
						+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs,gd.servicetpye,gd.designation "
						+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
						+ "left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
						+ "left join (select ack_no,dm.dept_code,dm.description, respondent_slno,servicetpye,designation from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code) order by ack_no, respondent_slno) gd on (a.ack_no=gd.ack_no)"
						+ "where a.inserted_by='"+session.getAttribute("userid")
						+"' and a.delete_status is false and a.ack_no='"+ackNo+"'"
						+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
						+ "case_full_name,a.ack_file_path, services_id,services_flag, inserted_time,gd.servicetpye,gd.designation "
						+ "order by district_name, inserted_time";
				
				System.out.println("SQL:" + sql);
				List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
				System.out.println("data=" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					
					Map<String, Object> ackData=(Map<String, Object>)data.get(0);
					
					cform.setDynaForm("ackId", ackNo);
					cform.setDynaForm("distId", ackData.get("distid").toString());
					//cform.setDynaForm("deptId", ackData.get("deptid").toString());
					cform.setDynaForm("advocateName", ackData.get("advocatename").toString());
					cform.setDynaForm("advocateCCno", ackData.get("advocateccno").toString());
					cform.setDynaForm("caseType", ackData.get("casetype").toString());
					cform.setDynaForm("caseTypeName", ackData.get("case_full_name").toString());
					cform.setDynaForm("mainCaseNo", ackData.get("maincaseno").toString());
					cform.setDynaForm("remarks", ackData.get("remarks").toString());
					cform.setDynaForm("dept_name", ackData.get("dept_descs").toString());
					cform.setDynaForm("district_name", ackData.get("district_name").toString());
					cform.setDynaForm("serviceType", ackData.get("servicetpye").toString());
					cform.setDynaForm("serviceNonService", ackData.get("services_flag").toString());
				}
				
				//System.out.println("ACK BAR CODE PATH:"+generateAckBarCodePdf128(ackNo, cform));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);
	}	
	
	
	public String generateAckPdf(String ackNo,CommonForm cform){
		//uploads/letters/Invoice
		Document document = null;
		PdfWriter writer = null;
		PdfPTable table = null;

		Map<String,Object>data = null;
		
		String pdfFilePath ="";
		Map<String,Object>clientInfodata = null;
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
		String invoiceDt = dateOnly.format(cal.getTime());
		try{
			
			String fileName = ackNo+".pdf";
			pdfFilePath = ApplicationVariables.ackPath + fileName;
			Itext_pdf_setting pdfsetting = new Itext_pdf_setting();
			int head = 14;
			int subhead = 12;
			int para = 11;
			int para2 = 8;


			document = new Document(PageSize.A4 ,5,5,5,5);
			document.setMargins(30, 30, 30, 30);
			document.setPageSize(PageSize.A4);
			writer = PdfWriter.getInstance(document, new FileOutputStream(ApplicationVariables.contextPath+pdfFilePath));
			BaseFont bf_courier = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);

			HeaderFooter footer = new HeaderFooter(new Phrase("Page No.:"+document.getPageNumber(), new Font(bf_courier, 8, Font.NORMAL)), true);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_RIGHT);
			document.setFooter(footer);

			document.open();
			Font ft1 = FontFactory.getFont(FontFactory.HELVETICA, para);

			String image = getServlet().getServletContext().getRealPath("/")+"images"+System.getProperty("file.separator")+"aplogo.png";
			Image img = Image.getInstance(image);
			
			/*
			img.setWidthPercentage(5);
            */
			
			System.out.println("WIDTH:"+PageSize.A4.getWidth()/2);
			System.out.println("HEIGHT:"+PageSize.A4.getHeight()/2);
			
			img.setAbsolutePosition(PageSize.A4.getWidth()/2 - 40, (float) (PageSize.A4.getHeight()/2)+300);
			img.scalePercent(60, 50);
			img.scaleToFit(100f, 100f);
        	img.setAlignment(1);
        	
        	document.add(img);
            
			document.add(pdfsetting.para("ANDHRA PRADESH ONLINE LEGAL CASE MONITORING SYSTEM (APOLCMS)",head,Paragraph.ALIGN_CENTER,90,3));
			document.add(pdfsetting.para("GOVERNMENT OF ANDHRA PRADESH",subhead,Paragraph.ALIGN_CENTER,0,2));
			
			document.add(pdfsetting.para("____________________________________________________________________________",subhead,Paragraph.ALIGN_CENTER,0,2));
			document.add(pdfsetting.para("",para,Paragraph.ALIGN_JUSTIFIED,8,10));
			
			/*PdfContentByte  pdfContentByte = writer.getDirectContent();
			
			BarcodeEAN barcodeEAN = new BarcodeEAN();
			barcodeEAN.setCodeType(BarcodeEAN.EAN13);
			barcodeEAN.setCode(ackNo);
			Image codeEANImage = barcodeEAN.createImageWithBarcode(pdfContentByte, null, null);
			codeEANImage.setAbsolutePosition(10, 100);
			codeEANImage.scalePercent(100);
			document.add(codeEANImage); */
			
			table = pdfsetting.table(2,100);
			pdfsetting.border = 0;
			
			System.out.println("---"+cform.getDynaForm("dept_name"));
			
			table.addCell(pdfsetting.cell("Acknowledgement No. : ",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+ackNo+"",2,Element.ALIGN_LEFT,para,Font.NORMAL)); // CLIENT ORG ADDRESS
			// table.addCell(pdfsetting.cell(" ",1,Element.ALIGN_RIGHT,para,Font.NORMAL));
			table.addCell(pdfsetting.cell("Date :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("generatedDate"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("District :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("district_name"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Department :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("dept_name"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Advocate Name :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("advocateName"),1,Element.ALIGN_LEFT,para,Font.NORMAL));

			table.addCell(pdfsetting.cell("Advocate CC No. :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("advocateCCno"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Case Type :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("caseTypeName"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Mode of Filing :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("filingMode"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Main Case No. :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("mainCaseNo"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Remarks :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("remarks"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			document.add(table);
			
			document.add(pdfsetting.para("",para,Paragraph.ALIGN_JUSTIFIED,8,10));
			document.add(pdfsetting.para("____________________________________________________________________________",subhead,Paragraph.ALIGN_CENTER,0,2));
//			/*pdfsetting.border = 0;
//			table = pdfsetting.table(5,100);
//			table.addCell(pdfsetting.cell("M. Maheshwara Reddy",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
//			table.addCell(pdfsetting.cell("Executive Director",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
//			table.addCell(pdfsetting.cell("APCOS",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
//			document.add(table);*/
			
			PdfContentByte cb = writer.getDirectContent();
			
			Barcode39 barcode39 = new Barcode39();
			barcode39.setCode(ackNo);
			System.out.println(""+pdfFilePath);
			// barcode39.setCode(pdfFilePath);
			//barcode39.setCode("https://apolcms.ap.gov.in/uploads/acks/AGC0114202203070354959.pdf");
			Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
			 
			
			/*Barcode128 barcode39 = new Barcode128();
			// barcode39.setCode(ackNo);
			System.out.println("http://localhost:8080/apolcms/uploads/acks/"+pdfFilePath);
			// barcode39.setCode("http://localhost:8080/apolcms/uploads/acks/"+pdfFilePath);
			barcode39.setCode("https://apolcms.ap.gov.in/uploads/acks/AGC0114202203070354959.pdf");
			Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
			*/
			//code39Image.setAbsolutePosition(PageSize.A4.getWidth()/2 + 150, (float) (PageSize.A4.getHeight()/2)+300);
			//code39Image.scalePercent(60, 50);
			//code39Image.scaleToFit(100f, 100f);
			//code39Image.setAbsolutePosition(10, 100);
			// code39Image.scalePercent(100);
			
			code39Image.scalePercent(100, 100);
			code39Image.setAlignment(1);
			
			document.add(code39Image);
			System.out.println("pdfFilePath:"+pdfFilePath);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(document!=null)
				document.close();
		}
		return pdfFilePath;
	}
	
	public static void main(String[] args) {
		// generateAckBarCodePdf("REV01-L1820220613115422600", null);
		//generateAckBarCodePdf128("FIN010000672", null);
		//Date todaysDate = new Date();
		//DateFormat df = new SimpleDateFormat("dd-Mon-yyyy HH:mm:ss:am");
		//String testDateString = df.format(todaysDate);
		
	}
	
	public static String generateAckBarCodePdf128(String ackNo, CommonForm cform) {
		// uploads/letters/Invoice
		Document document = null;
		PdfWriter writer = null;
		String pdfFilePath = "", filepath = "";
		try {
			String fileName = ackNo + "_barCode.pdf";
			pdfFilePath = ApplicationVariables.ackPath + fileName;
			
			/*
			 * Date todaysDate = new Date(); DateFormat df = new
			 * SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); String testDateString =
			 * df.format(todaysDate);
			 */
			
			LocalDateTime da_ti2 = LocalDateTime.now();
	        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a");
	        String testDateString = da_ti2.format(dtf1);
			
			
			Itext_pdf_setting pdfsetting = new Itext_pdf_setting();
			int subhead = 8;
			
			document = new Document(PageSize.A6.rotate());
			document.setMargins(10,10,10,10);
			document.setPageSize(PageSize.A6);

			//filepath = "E:\\Apache Software Foundation\\Tomcat 9.0\\webapps\\apolcms\\" + pdfFilePath;
			//filepath ="D:\\"+fileName;
			 filepath = ApplicationVariables.contextPath+pdfFilePath;
			System.out.println("filepath:" + filepath);
			writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
			document.open();
			// System.out.println("WIDTH A6:" + PageSize.A6.getWidth());
			// System.out.println("HEIGHT A6:" + PageSize.A6.getHeight());

			PdfContentByte cb = writer.getDirectContent();
			Barcode128 barcode128 = new Barcode128();
			barcode128.setCode(ackNo);
			document.add(pdfsetting.para("Acknowledgement No.:",subhead,Paragraph.ALIGN_LEFT,0,2));
			
			Image code128Image = barcode128.createImageWithBarcode(cb, null, null);
			code128Image.scaleToFit(250f, 250f);
			code128Image.scaleAbsoluteHeight(50f);
			
			// System.out.println("--"+code128Image.getScaledHeight());
			// System.out.println("--"+code128Image.getScaledWidth());
			
			document.add(code128Image);
			document.add(pdfsetting.para(testDateString+"                                      "+"APOLCMS" ,subhead,Paragraph.ALIGN_LEFT,2,2));
			//document.add(pdfsetting.para("APOLCMS",subhead,Paragraph.ALIGN_RIGHT,0,2));
			// System.out.println("BAR CODE pdfFilePath:" + pdfFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
		}
		return pdfFilePath;
	}
	
	
	public static String generateAckBarCodePdf1(String ackNo,CommonForm cform){
		//uploads/letters/Invoice
		Document document = null;
		PdfWriter writer = null;
		String pdfFilePath ="", filepath="";
		try{
			String fileName = ackNo+"_barCode-A1.pdf";
			pdfFilePath = ApplicationVariables.ackPath + fileName;

			document = new Document(PageSize.A6.rotate());
			//document.setMargins(20, 40, 60, 80);
			document.setPageSize(PageSize.A6);
			
			// writer = PdfWriter.getInstance(document, new FileOutputStream(ApplicationVariables.contextPath+pdfFilePath));
			filepath = "E:\\Apache Software Foundation\\Tomcat 9.0\\webapps\\apolcms\\"+pdfFilePath;
			// filepath = ApplicationVariables.contextPath+pdfFilePath;
			System.out.println("filepath:"+filepath);
					
			writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
			//BaseFont bf_courier = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
			/*
			HeaderFooter footer = new HeaderFooter(new Phrase("Page No.:"+document.getPageNumber(), new Font(bf_courier, 8, Font.NORMAL)), true);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_RIGHT);
			document.setFooter(footer);
			*/

			document.open();
			System.out.println("WIDTH A6:"+PageSize.A6.getWidth());
			System.out.println("HEIGHT A6:"+PageSize.A6.getHeight());
			
			PdfContentByte cb = writer.getDirectContent();
			
			Barcode39 barcode39 = new Barcode39();
			barcode39.setCode(ackNo);
			// System.out.println(""+pdfFilePath);
			// barcode39.setCode(pdfFilePath);
			//barcode39.setCode("https://apolcms.ap.gov.in/uploads/acks/AGC0114202203070354959.pdf");
			Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
			 
			
			/*Barcode128 barcode39 = new Barcode128();
			// barcode39.setCode(ackNo);
			System.out.println("http://localhost:8080/apolcms/uploads/acks/"+pdfFilePath);
			// barcode39.setCode("http://localhost:8080/apolcms/uploads/acks/"+pdfFilePath);
			barcode39.setCode("https://apolcms.ap.gov.in/uploads/acks/AGC0114202203070354959.pdf");
			Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
			*/
			//code39Image.setAbsolutePosition( ( PageSize.A9.getWidth()/2  - 100) , (float) (PageSize.A9.getHeight()/2)+50);
			//code39Image.scalePercent(125, 125);
			//System.out.println("125:"+pdfFilePath);
			code39Image.scaleToFit(250f, 250f);
			code39Image.scaleAbsoluteHeight(50f);
			//code39Image.setWidthPercentage(100);
			//code39Image.setAbsolutePosition(10, 100);
			// code39Image.scalePercent(100);
			code39Image.setAlignment(1);
			
			
			document.add(code39Image);
			
			// document.add(code39Image);
			
			System.out.println("BAR CODE pdfFilePath:"+pdfFilePath);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(document!=null)
				document.close();
		}
		return pdfFilePath;
	}

	public ActionForward deptWiseCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................getAcknowledementsListAll()");
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
			String deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			
					//cform.getDynaForm("ackType") !=null && !cform.getDynaForm("ackType").equals("0") ? cform.getDynaForm("ackType").toString() : "NEW";

			sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, case when services_id='0' then null else services_id end as services_id,services_flag, "
					+ "to_char(inserted_time,'dd-mm-yyyy') as generated_date, getack_dept_desc(a.ack_no) as dept_descs, coalesce(a.hc_ack_no,'-') as hc_ack_no "
					+ "from ecourts_gpo_ack_depts ad inner join ecourts_gpo_ack_dtls a on (ad.ack_no=a.ack_no) "
					+ "left join district_mst dm on (a.distid=dm.district_id) "
					+ "left join case_type_master cm on (a.casetype=cm.sno::text or a.casetype=cm.case_short_name) "
					+ "where a.delete_status is false and ack_type='NEW' and ad.dept_code='"+deptCode+"' "
					+ "order by inserted_time";
			
			System.out.println("SQL:"+sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				
				request.setAttribute("DEPTACKDATA", data);
			} 
			else {
				request.setAttribute("errorMsg", "No details found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}
}