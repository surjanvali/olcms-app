package in.apcfss.struts.Actions;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.commons.Itext_pdf_setting;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
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
import org.apache.struts.upload.FormFile;

import plugins.DatabasePlugin;

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
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);
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

			sql = "select slno , ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip,"
					+ " upper(trim(district_name)) as district_name," 
					// + "depm.description as dept_name,"
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id "
					+ "from ecourts_gpo_ack_dtls a "
					+ "left join district_mst dm on (a.distid=dm.district_id)"
					// + "left join dept_new depm on (a.deptid=depm.dept_id)"
					+ "left join case_type_master cm on (a.casetype=cm.sno) where a.inserted_by='"
					+ session.getAttribute("userid")
					+ "' and a.delete_status is false order by district_name, inserted_time";
			
			sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
					+ "upper(trim(case_full_name)) as  case_full_name, a.ack_file_path, services_id,"
					+ "STRING_AGG(gd.dept_code,',') as dept_codes,STRING_AGG(gd.description,', ') as dept_descs "
					+ " from ecourts_gpo_ack_dtls a left join district_mst dm on (a.distid=dm.district_id)"
					+ "left join case_type_master cm on (a.casetype=cm.sno) "
					+ "left join (select ack_no,dm.dept_code,dm.description from ecourts_gpo_ack_depts inner join dept_new dm using (dept_code)) gd on (a.ack_no=gd.ack_no)"
					+ "where a.inserted_by='"+session.getAttribute("userid")
					+"' and a.delete_status is false "
					+ "group by slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, district_name,"
					+ "case_full_name,a.ack_file_path, services_id, inserted_time "
					+ "order by district_name, inserted_time";
			
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

	public ActionForward displayAckForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

			request.setAttribute("saveAction", "INSERT");

			cform.setDynaForm("caseTypesList", DatabasePlugin.getSelectBox( "select  sno,upper(trim(case_full_name)) as case_full_name from case_type_master order by sno", con));
			cform.setDynaForm("serviceTypesList", DatabasePlugin.getSelectBox( "select  service_desc,upper(trim(service_desc)) as service_desc from ecourts_mst_services order by 1", con));
			cform.setDynaForm("deptList", DatabasePlugin.getSelectBox(
					//"select dept_id,sdeptcode||deptcode||'-'||upper(trim(description)) as description from dept order by sdeptcode||deptcode",
					"select dept_code as dept_code,dept_code||' - '||upper(description) as description from dept_new where display=true order by dept_code",
					con));
			cform.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by trim(district_name)",
					con));
			cform.setDynaForm("gpsList", DatabasePlugin.getSelectBox(
					"select emailid,first_name||' '||last_name||' - '||designation from ecourts_mst_gps",
					con));
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
						+ "left join case_type_master cm on (a.casetype=cm.sno) where a.inserted_by='"
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
						"select dept_id,sdeptcode||deptcode||'-'||upper(trim(description)) as description from dept order by sdeptcode||deptcode",
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
				
				String ackNo = null;//generateNewAckNo();
				int distId = cform.getDynaForm("distId") != null ? Integer.parseInt(cform.getDynaForm("distId").toString()) : 0;
				System.out.println("deptIds:"+cform.getDeptId());
				String deptCodes[] = cform.getDeptId() != null ? cform.getDeptId() : null;
				// int deptId = cform.getDynaForm("deptId") != null ? Integer.parseInt(cform.getDynaForm("deptId").toString()) : 0;
				
				if(distId>0 && deptCodes != null && deptCodes.length > 0) {
					sql="select '"+deptCodes[0]+"'||lpad('"+distId+"'::text,2,'0')||to_char(now(),'yyyyMMddmmssms')";
					ackNo = DatabasePlugin.getStringfromQuery(sql, con);
				// ackNo = DatabasePlugin.getStringfromQuery("select sdeptcode||deptcode||lpad('"+distId+"'::text,2,'0')||to_char(now(),'yyyyMMddmmssms') from dept where dept_id='"+deptId+"'", con);
				}
				
				if (ackNo != null && !ackNo.contentEquals("")) {

					int i = 1;
					sql = "insert into ecourts_gpo_ack_dtls (ack_no , distid , petitioner_name, advocatename ,advocateccno , casetype , maincaseno , remarks ,  "
							+ "inserted_by , inserted_ip , services_id)"
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
					ps.setString(++i, cform.getDynaForm("serviceType") != null ? cform.getDynaForm("serviceType").toString() : "");
					
					/*
					FormFile myDoc=null; String filePath="",newFileName="", ackDocument="" ;
					FileUploadUtilities fuu = new FileUploadUtilities();
					if(cform.getChangeLetter()!=null && !cform.getChangeLetter().toString().equals("")) {
						myDoc = (FormFile)cform.getChangeLetter();
						filePath="uploads/ackdocuments/";
						newFileName="petition_"+CommonModels.randomTransactionNo();
						ackDocument = fuu.saveFile(myDoc, filePath, newFileName);
						ps.setString(++i, ackDocument);
						
					}
					else {
						ps.setString(++i, ackDocument);
					}
					ps.setString(++i, cform.getDynaForm("gpCode") != null ? cform.getDynaForm("gpCode").toString() : "");
					*/
					int a = ps.executeUpdate();
					
					// INSERT DEPARTMENTS
					if(deptCodes != null && deptCodes.length > 0) {
						ps.close();
						sql="insert into ecourts_gpo_ack_depts (ack_no, dept_code) values (?,?)";
						ps = con.prepareStatement(sql);
						
						for(String deptCode : deptCodes) {
							System.out.println("depCode::"+deptCode);
							if(deptCode != null && !deptCode.equals("")) {
								i=1;
								ps.setString(i, ackNo);
								ps.setString(++i, deptCode);
								ps.addBatch();
							}
						}
						ps.executeBatch();
					}
					/*/ INSERT RESPONDENTS
					String respondentIds[] = cform.getDynaForm("respondentIds") != null ? cform.getDynaForm("respondentIds").toString().split(",") : null;
					if(respondentIds !=null && respondentIds.length > 0) {
						
						ps.close();
						sql="insert into ecourts_gpo_ack_respondents (ack_no, respondent_name, respondent_address) values (?,?,?)";
						ps = con.prepareStatement(sql);
						
						for(String respondentId : respondentIds) {
							i=1;
							if(respondentId !=null && !respondentId.equals("")) {
								ps.setString(i, ackNo);
								ps.setString(++i, cform.getDynaForm("respondantName_"+respondentId).toString());
								ps.setString(++i, cform.getDynaForm("respondantAddr_"+respondentId).toString());
								ps.addBatch();
							}
						}
						ps.executeBatch();
					}*/
					
					if (a > 0) {
						
						
						sql = "select slno , ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , " +
								"inserted_ip, upper(trim(district_name)) as district_name,"
								//+ "depm.description as dept_name,"
								+ "upper(trim(case_full_name)) as  case_full_name, services_id   from ecourts_gpo_ack_dtls a "
								+ "left join district_mst dm on (a.distid=dm.district_id)"
								//+ "left join dept depm on (a.deptid=depm.dept_id)"
								+ "left join case_type_master cm on (a.casetype=cm.sno) where a.inserted_by='"
								+ session.getAttribute("userid") + "' and ack_no='"+ackNo+"'";
						
						sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
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
							cform.setDynaForm("serviceType", ackData.get("services_id").toString());
							
						}
						
						String ackPath = generateAckPdf(ackNo, cform);
						System.out.println("ackPath::"+ackPath);
						if(ackPath!=null && !ackPath.equals("")){
							sql=" update ecourts_gpo_ack_dtls set ack_file_path='"+generateAckPdf(ackNo, cform)+"' where ack_no='"+ackNo+"'";
							DatabasePlugin.executeUpdate(sql, con);
							
							request.setAttribute("successMsg",
									"Acknowledgement details saved successfully with Ack No.:" + ackNo);
							
							con.commit();
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
			String ackNo = cform.getDynaForm("ackId") != null ? cform.getDynaForm("ackId").toString() : "";

			if (ackNo != null && !ackNo.contentEquals("")) {
				int i = 1;
				sql = "update ecourts_gpo_ack_dtls set delete_status=true where ack_no=?";
				ps = con.prepareStatement(sql);
				ps.setString(i, ackNo);
				int a = ps.executeUpdate();
				if (a > 0) {
					request.setAttribute("successMsg", "Ack No.:" + ackNo + " deleted successfully.");
				} else
					request.setAttribute("errorMsg", "Error in deleting Ack No.:" + ackNo);
			} else {
				request.setAttribute("errorMsg", "Invalid Acknowledgement No. Kindly try again.");
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception Occurred while deletion.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getAcknowledementsList(mapping, cform, request, response);
	}

	public static String generateNewAckNo() {
		String ackNoNew = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssms");
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
				sql="select slno , a.ack_no , distid , advocatename ,advocateccno , casetype , maincaseno , remarks ,  inserted_by , inserted_ip, upper(trim(district_name)) as district_name, "
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
					cform.setDynaForm("serviceType", ackData.get("services_id").toString());
					
				}
				
				System.out.println("ACK PATH:"+generateAckPdf(ackNo, cform));
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
			
			table.addCell(pdfsetting.cell("Acknowledgement No. : ",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+ackNo+"",2,Element.ALIGN_LEFT,para,Font.NORMAL)); // CLIENT ORG ADDRESS
			// table.addCell(pdfsetting.cell(" ",1,Element.ALIGN_RIGHT,para,Font.NORMAL));
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
			
			table.addCell(pdfsetting.cell("Services Type :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("serviceType"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Main Case No. :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("mainCaseNo"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			table.addCell(pdfsetting.cell("Remarks :",1,Element.ALIGN_RIGHT,para,Font.BOLD));
			table.addCell(pdfsetting.cell(""+cform.getDynaForm("remarks"),1,Element.ALIGN_LEFT,para,Font.NORMAL));
			
			document.add(table);
			
			document.add(pdfsetting.para("",para,Paragraph.ALIGN_JUSTIFIED,8,10));
			document.add(pdfsetting.para("____________________________________________________________________________",subhead,Paragraph.ALIGN_CENTER,0,2));
			/*pdfsetting.border = 0;
			table = pdfsetting.table(5,100);
			table.addCell(pdfsetting.cell("M. Maheshwara Reddy",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
			table.addCell(pdfsetting.cell("Executive Director",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
			table.addCell(pdfsetting.cell("APCOS",5,Element.ALIGN_RIGHT,para,Font.NORMAL));
			document.add(table);*/
			
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
			code39Image.scalePercent(100);
			code39Image.setAlignment(1);
			
			document.add(code39Image);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(document!=null)
				document.close();
		}
		return pdfFilePath;
	}
}