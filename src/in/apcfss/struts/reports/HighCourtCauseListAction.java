package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.FileUploadUtilities;
import in.apcfss.struts.eCourt.apis.ECourtAPIs;
import in.apcfss.struts.eCourt.apis.ECourtsCryptoHelper;
import in.apcfss.struts.eCourt.apis.EHighCourtAPI;
import in.apcfss.struts.eCourt.apis.HASHHMACJava;
import in.apcfss.struts.eCourt.apis.HighCourtCauseListAPI;
import in.apcfss.struts.eCourt.apis.HighCourtCauseListBenchAPI;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class HighCourtCauseListAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			request.setAttribute("HEADING", "High Court Cause List ");
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			//System.out.println("--"+sdf.format(new Date()));
			cform.setDynaForm("list_date" , sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		//return mapping.findForward("success");
		return ShowCauselist(mapping, cform, request, response);
	}

	public ActionForward ShowCauselist(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null, date = null;

		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			date = (String) cform.getDynaForm("list_date");
			System.out.println("date:::::::::::::::" + date);
			con = DatabasePlugin.connect();
			sql = "select a.slno ,a.est_code , a. causelist_date , a.bench_id , a. causelist_id , "
					+ "cause_list_type ,coalesce(causelist_document,'') as document,"
					+ " b.judge_name from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) where a.causelist_date='"
					+ date + "' group by a.slno ,a.est_code ,"
					+ " a. causelist_date , a.bench_id , a. causelist_id , cause_list_type ,b.judge_name";
			
			sql="select distinct a.est_code , a. causelist_date , a.bench_id , a. causelist_id , cause_list_type ,coalesce(causelist_document,'') as document, b.judge_name "
					+ "from ecourts_causelist_bench_data a  left join  ecourts_causelist_data b on (a.bench_id=b.bench_id) where a.causelist_date=to_date('"
					+ date + "','mm/dd/yyyy') and coalesce(causelist_document,'') not like '%status_code%'";
			

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("causelist", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");

			request.setAttribute("HEADING", "High Court Cause List ");
			cform.setDynaForm("list_date" , cform.getDynaForm("list_date"));
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward retrieveCauselist(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		CommonForm cform = (CommonForm) form;
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		try {
			con = DatabasePlugin.connect();
			String opVal = ECourtAPIs.getSelectParam(11);
			 String estCode="APHC01", causelistDate="2022-01-20";
			 causelistDate = (String) cform.getDynaForm("list_date");
			inputStr = "est_code="+estCode+"|causelist_date="+causelistDate;//ECourtAPIs.getInputStringValue(opVal);
			// 1. Encoding Request Token
			byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");

			targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);

			System.out.println(totalCount + ":URL : " + targetURL);
			System.out.println("Input String : " + inputStr);

			authToken = EHighCourtAPI.getAuthToken();
			String resp = "";
			 if (opVal != null && !opVal.equals("")) {
				try {
					resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (resp != null && !resp.equals("")) {
				try {
					
					HighCourtCauseListBenchAPI.processApiResponse(resp, estCode, causelistDate, con);
					
					getBenchesCauselist(con, causelistDate);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
		return ShowCauselist(mapping, cform, request, response);
	}
	
	public static void getBenchesCauselist(Connection con, String causelistDate) throws Exception {
		
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		try {
			String opVal = ECourtAPIs.getSelectParam(12);
			
			String estCode="APHC01";
			
			sql="SELECT slno, est_code, causelist_date, bench_id, judge_name, inserted_time FROM apolcms.ecourts_causelist_data where  causelist_date=to_date('"+causelistDate+"','dd/mm/yyyy') order by causelist_date";
			
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				causelistDate = rs.getString("causelist_date");
				inputStr = "est_code="+estCode+"|causelist_date="+causelistDate+"|bench_id="+rs.getString("bench_id");//ECourtAPIs.getInputStringValue(opVal);
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");
	
				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);
	
				System.out.println(totalCount + ":URL : " + targetURL);
				System.out.println("Input String : " + inputStr);
	
				authToken = EHighCourtAPI.getAuthToken();
				String resp = "";
				 if (opVal != null && !opVal.equals("")) {
					try {
						resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
	
				if (resp != null && !resp.equals("")) {
					try {
						HighCourtCauseListAPI.processApiResponse(resp, estCode, causelistDate, con);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	
	}
	
}