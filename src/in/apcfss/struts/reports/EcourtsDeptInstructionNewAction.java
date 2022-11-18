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

public class EcourtsDeptInstructionNewAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"HighCourtCasesListAction...........................1...................................................unspecified()");
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

			if(roleId.equals("2") || roleId.equals("12")) { //District Collector

				sqlCondition +="  and distid='"+distId+"'";//and case_status=7
			}
			else if(roleId.equals("10")) { //District Nodal Officer
				sqlCondition +=" and distid='"+distId+"' and dept_code='"+deptCode+"'    ";// and case_status=8
			}
			if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
				sqlCondition +=" and dept_code='" + deptCode + "' ";
			}
			else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
				sqlCondition +=" and dept_code='" + deptCode + "' ";
			}
			else if(roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
				sqlCondition +="  and assigned_to='"+userid+"'";
			}
			
			
			if(roleId.equals("6")) {
			
				request.setAttribute("HEADING", "Daily Status Entry");
			}else {
				
				request.setAttribute("HEADING", "Instructions Entry");
			}


			con = DatabasePlugin.connect();

			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));
			
			sql="select b.ack_no,b.ack_no from ecourts_gpo_ack_dtls a "
					+ " inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no) where ack_type='NEW' and respondent_slno='1'  "+sqlCondition+"  order by b.ack_no";
			System.out.println("SQL:"+sql);
			cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( sql, con));
			cform.setDynaForm("oldNewType", "Legacy");
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
		//return getCasesList(mapping, cform, request, response);
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
		String sql = null, sqlCondition = "", roleId="", distId="", deptCode="", userid="",Condition="",target=null;
		try {
			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
			con = DatabasePlugin.connect();

			
			String caseType="";
			
			if(roleId.equals("6")) {   // || !caseType.equals(null)
				  caseType=request.getParameter("caseType").toString();
				  target="successPopup";
				  request.setAttribute("service_type", "service_type");
			 }else {
				 caseType=cform.getDynaForm("oldNewType").toString();
				 target="success";
			 }
			String ackNoo=null;

			String cino=null;
			System.out.println("distId=="+distId);

			if(caseType.equals("New")) {
				
				cform.setDynaForm("oldNewType", "New");
				
				
				
				if(roleId.equals("6") ) {   //|| !caseType.equals(null)
					ackNoo=request.getParameter("cino").toString();
					
					request.setAttribute("service_type", "service_type");
					 
				 }else {
					 ackNoo=cform.getDynaForm("ackNoo").toString();
				 }


				if(roleId.equals("2") || roleId.equals("12")) { //District Collector

					Condition +="  and distid='"+distId+"'";//and case_status=7
				}
				else if(roleId.equals("10")) { //District Nodal Officer
					Condition +=" and distid='"+distId+"' and dept_code='" + deptCode + "'  ";// and case_status=8
				}


				if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
					Condition +=" and dept_code='" + deptCode + "' ";
				}
				else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
					Condition +=" and dept_code='" + deptCode + "' ";
				}
				else if(roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
					Condition +="  and assigned_to='"+userid+"'";
				}

			}else {

				cform.setDynaForm("oldNewType", "Legacy");
				
				if(roleId.equals("6") ) {  //|| !caseType.equals(null)
					cino=request.getParameter("cino").toString();
					
					 
				 }else {
					 cino=cform.getDynaForm("caseType1").toString()+"/"+cform.getDynaForm("mainCaseNo").toString()+"/"+cform.getDynaForm("regYear1").toString();
				 }
				

				if (cform.getDynaForm("caseType1") != null && !cform.getDynaForm("caseType1").toString().contentEquals("")
						&& !cform.getDynaForm("caseType1").toString().contentEquals("0")) {
					sqlCondition += " and a.type_name_reg='" + cform.getDynaForm("caseType1").toString().trim() + "' ";
				}

				if (cform.getDynaForm("mainCaseNo") != null && !cform.getDynaForm("mainCaseNo").toString().contentEquals("")
						&& !cform.getDynaForm("mainCaseNo").toString().contentEquals("0")) {
					sqlCondition += " and a.reg_no='" + cform.getDynaForm("mainCaseNo").toString().trim() + "' ";
				}

				if (cform.getDynaForm("regYear1") != null && !cform.getDynaForm("regYear1").toString().contentEquals("")
						&& !cform.getDynaForm("regYear1").toString().contentEquals("0")) {
					sqlCondition += " and a.reg_year='" + cform.getDynaForm("regYear1").toString().trim() + "' ";
				}


				if(roleId.equals("2") || roleId.equals("12")) { //District Collector

					sqlCondition +="  and a.dist_id='"+distId+"'";//and case_status=7
				}
				else if(roleId.equals("10")) { //District Nodal Officer
					sqlCondition +=" and a.dist_id='"+distId+"'";// and case_status=8
				}

				if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
					sqlCondition +=" and a.dept_code='" + deptCode + "' ";
				}
				else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
					sqlCondition +=" and a.dept_code='" + deptCode + "' ";
				}
				else if(roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
					sqlCondition +="  and assigned_to='"+userid+"'";
				}
				else if(roleId.equals("6") ) {
					sqlCondition +="  and a.cino='"+cino+"'";
				}
			}
			
			if(roleId.equals("6")) {
				
				request.setAttribute("HEADING", "Daily Status Entry");
				request.setAttribute("status_entry", "Daily Status ");
			}else {
				
				request.setAttribute("HEADING", "Instructions Entry");
				request.setAttribute("status_entry", "Instructions ");
			}

			System.out.println("ackNoo--"+ackNoo+"cino---"+cino);

			List<Map<String, Object>> data=null;

			if (caseType.equals("Legacy")) {
				
				sql = "select a.*, "
						+ " nda.fullname_en as fullname, nda.designation_name_en as designation, nda.post_name_en as post_name, nda.email, nda.mobile1 as mobile,dim.district_name , "
						+ " 'Pending at '||ecs.status_description||'' as current_status, coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths,"
						+ " case when (prayer is not null and coalesce(trim(prayer),'')!='' and length(prayer) > 2) then substr(prayer,1,250) else '-' end as prayer, prayer as prayer_full, ra.address from ecourts_case_data a "
						
						+ " left join nic_prayer_data np on (a.cino=np.cino)"
						+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
						+ " left join district_mst dim on (a.dist_id=dim.district_id) "
						+ " inner join ecourts_mst_case_status ecs on (a.case_status=ecs.status_id) "
						+ " left join nic_data_all nda on (a.dept_code=substr(nda.global_org_name,1,5) and a.assigned_to=nda.email and nda.is_primary='t' and coalesce(a.dist_id,'0')=coalesce(nda.dist_id,'0')) "
				
				/*sql = "select a.*, "
						+ "coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address from ecourts_case_data a "
						+ " left join nic_prayer_data np on (a.cino=np.cino)"
						+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "*/
						+ " left join"
						+ " ("
						+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
						+ " from "
						+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
						+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
						+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
						+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
						+ " on (a.cino=b.cino) inner join dept_new d on (a.dept_code=d.dept_code) where d.display = true " + sqlCondition;
				
				System.out.println("ecourts SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASESLISTOLD", data);
					cform.setDynaForm("cino", ((Map) data.get(0)).get("cino"));
					
					sql = "select instructions,to_char(insert_time,'dd-Mon-yyyy hh24:mi:ss PM') as insert_time,coalesce(upload_fileno,'-') as upload_fileno, status_instruction_flag,reply_flag,slno,reply_serno,reply_instructions ,coalesce(reply_upload_fileno,'-') as reply_upload_fileno, reply_insert_time, reply_insert_by  from ecourts_dept_instructions where cino='" + ((Map) data.get(0)).get("cino") + "'  order by insert_time::timestamp desc ";
					System.out.println("sql--" + sql);
					List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("existDataOld", existData);
					
					
					
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
						+ " where a.delete_status is false and ack_type='NEW'    and (a.ack_no='"+ackNoo+"' or a.hc_ack_no='"+ackNoo+"' )  and respondent_slno='1'   "
						+ " order by a.inserted_time desc";

				System.out.println("ecourts SQL:" + sql);
				data = DatabasePlugin.executeQuery(sql, con);
				if (data != null && !data.isEmpty() && data.size() > 0) {
					request.setAttribute("CASESLISTNEW", data);
					
					cform.setDynaForm("cino", ackNoo);
					//	request.setAttribute("cinooo", ackNoo);
					sql = "select instructions, to_char(insert_time,'dd-Mon-yyyy hh24:mi:ss PM') as insert_time,coalesce(upload_fileno,'-') as upload_fileno, status_instruction_flag,reply_flag,slno,reply_serno,reply_instructions ,coalesce(reply_upload_fileno,'-') as reply_upload_fileno, reply_insert_time, reply_insert_by  from ecourts_dept_instructions where cino='" + ackNoo + "'  order by insert_time::timestamp desc ";
					System.out.println("sql--" + sql);
					List<Map<String, Object>> existData = DatabasePlugin.executeQuery(sql, con);
					request.setAttribute("existDataNew", existData);

				} else {
					request.setAttribute("errorMsg", "No Records Found");
				}
			}
			
			cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( "select b.ack_no,b.ack_no from ecourts_gpo_ack_dtls a "
					+ " inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no) where 1=1 and respondent_slno='1'   "+Condition+" order by b.ack_no", con));

			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));

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

		return mapping.findForward(target);
	}
	public ActionForward getSubmitCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String userId=null;int a=0;String cIno = null,uploadedFilePath=null;
		try {
			con = DatabasePlugin.connect();
			//con.setAutoCommit(false);
			
			System.out.println("in assign2DeptHOD --- getSubmitCategory");
			userId = CommonModels.checkStringObject(request.getSession().getAttribute("userid"));
			//cIno = CommonModels.checkStringObject(request.getParameter("cino"));
			cIno = CommonModels.checkStringObject(cform.getDynaForm("cino"));
			String fileCino = CommonModels.checkStringObject(cform.getDynaForm("fileCino"));
			String oldNewType = CommonModels.checkStringObject(cform.getDynaForm("oldNewType"));
			System.out.println("cIno---"+cIno+oldNewType);
			System.out.println("fileCino---"+fileCino);
			
			String fileSeperator=ApplicationVariables.filesepartor;
			String destinationPath = ApplicationVariables.contextPath + "Instruction"+fileSeperator;
			System.out.println("destinationPath:" + destinationPath);

			String sql = null, sqlCondition = "", roleId="", distId="", deptCode="", userid="",Condition=null,status_flag=null;

			userid = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));

			if(roleId.equals("2") || roleId.equals("12")) { //District Collector

				Condition +="  and distid='"+distId+"'";//and case_status=7
			}
			else if(roleId.equals("10")) { //District Nodal Officer
				Condition +=" and distid='"+distId+"'";// and case_status=8
			}


			if(roleId.equals("5") || roleId.equals("9")) {//NO & HOD
				Condition +=" and a.dept_code='" + deptCode + "' ";
			}
			else if(roleId.equals("3") || roleId.equals("4")) {//MLO & Sect. Dept.
				Condition +=" and a.dept_code='" + deptCode + "' ";
			}
			else if(roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
				Condition +="  and assigned_to='"+userid+"'";
			}
			
			
			
			if(roleId.equals("6")) {
			
				request.setAttribute("HEADING", "Daily Status Entry");
				request.setAttribute("status_entry", "Daily Status ");
				 status_flag="D";
			}else {
				
				request.setAttribute("HEADING", "Instructions Entry");
				request.setAttribute("status_entry", "Instructions ");
				status_flag="I";
			}

			FileUploadUtilities fuu = new FileUploadUtilities();
			FormFile myDoc;

			myDoc = cform.getChangeLetter();

			System.out.println("myDoc---"+myDoc);
			String filePath="uploads/Instruction/";
			String newFileName="Instruction_"+CommonModels.randomTransactionNo();
			String Instruction_file = fuu.saveFile(myDoc, filePath, newFileName);

			System.out.println("pdfFile--"+Instruction_file);
			

					sql = "insert into ecourts_dept_instructions (cino, instructions , upload_fileno,dept_code ,dist_code,insert_by,legacy_ack_flag,status_instruction_flag ) "
							+ " values (?,?, ?, ?, ?, ?,?,?)";

					ps = con.prepareStatement(sql);
					int i = 1;
					ps.setString(i, cIno);
					ps.setString(++i, cform.getDynaForm("instructions") != null ? cform.getDynaForm("instructions").toString() : "");
					ps.setString(++i, Instruction_file);
					ps.setString(++i, CommonModels.checkStringObject(session.getAttribute("dept_code")));
					ps.setInt(++i, CommonModels.checkIntObject(session.getAttribute("dist_id")));
					ps.setString(++i, userId);
					ps.setString(++i, oldNewType);
					ps.setString(++i, status_flag);
				 

			System.out.println("sql--"+sql);

			a = ps.executeUpdate();

			System.out.println("a--->"+a);
			if(a>0) {
				sql="insert into ecourts_case_activities (cino , action_type , inserted_by , inserted_ip, remarks,uploaded_doc_path) "
						+ " values ('" + cIno + "','SUBMITTED INSTRUCTIONS TO GP', '"+userId+"', '"+request.getRemoteAddr()+"', '"+cform.getDynaForm("instructions").toString()+"','"+Instruction_file+"')";
				DatabasePlugin.executeUpdate(sql, con);

				request.setAttribute("successMsg", "Instructions data saved successfully.");
			}else {
				request.setAttribute("errorMsg", "Error in submission. Kindly try again.");
			}
			//cform.setDynaForm("AckList", DatabasePlugin.getSelectBox( "select b.ack_no,b.ack_no from ecourts_gpo_ack_dtls a inner join ecourts_gpo_ack_depts b on (a.ack_no=b.ack_no) where 1=1 "+Condition+" order by b.ack_no", con));


			cform.setDynaForm("caseTypesListShrt", DatabasePlugin.getSelectBox( "select  upper(trim(case_short_name)) as sno,upper(trim(case_short_name)) as case_full_name from case_type_master order by sno", con));

			ArrayList selectData = new ArrayList();
			for(int j=2022; j > 1980; j--) {
				selectData.add(new LabelValueBean(j+"",j+""));
			}
			cform.setDynaForm("yearsList", selectData);

			request.setAttribute("HEADING", "Submit Instructions for CINO : " + cIno);

		} catch (Exception e) {
			//con.rollback();
			request.setAttribute("errorMsg", "Error in Submission. Kindly try again.");
			e.printStackTrace();
		} finally {
			cform.setDynaForm("instructions","");
			cform.setDynaForm("oldNewType",cform.getDynaForm("oldNewType"));
			cform.setDynaForm("caseType1",cform.getDynaForm("caseType1"));
			cform.setDynaForm("regYear1",cform.getDynaForm("regYear1"));
			cform.setDynaForm("mainCaseNo",cform.getDynaForm("mainCaseNo"));
			cform.setDynaForm("ackNoo", cform.getDynaForm("ackNoo"));
			
			DatabasePlugin.close(con, ps, null);
		}
		return getCasesList(mapping, cform, request, response);
		//return mapping.findForward("success");
	}
	public ActionForward getGPStatusUpdated(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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


				 if (!CommonModels.checkStringObject(cform.getDynaForm("regYear")).equals("ALL") && CommonModels.checkIntObject(cform.getDynaForm("regYear")) > 0) {
					sqlCondition += " and reg_year='" + CommonModels.checkIntObject(cform.getDynaForm("regYear")) + "' ";
				}


				if (CommonModels.checkIntObject(cform.getDynaForm("filingYear")) > 0) {
					sqlCondition += " and fil_year='" + CommonModels.checkIntObject(cform.getDynaForm("filingYear")) + "' ";
				}

			}


			if(!roleId.equals("2")) { //District Nodal Officer
				sqlCondition +=" and dept_code='" + deptCode + "' ";
			}

			if(roleId.equals("2") || roleId.equals("12")) { //District Collector

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
			else if(roleId.equals("8") || roleId.equals("11") || roleId.equals("12")) {
				sqlCondition +="  and assigned_to='"+userid+"'";
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

			sql= " select a.* from ecourts_case_data a inner join (select distinct cino from ecourts_gpo_daily_status) b on (a.cino=b.cino) where coalesce(ecourts_case_status,'')!='Closed' "+sqlCondition+" order by 1";

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

}