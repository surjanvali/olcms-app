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
import in.apcfss.struts.eCourt.apis.HighCourtCauseListPDFAPI;

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
			
			sql="select distinct c.est_code,c.bench_id, string_agg('<a href=\"./'||c.causelist_document||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||c.bench_id||'</span></a><br/>','- ') as document,judge_name "
					+ " from  ( select * from ( select a.est_code,a.bench_id, a.causelist_document,a.causelist_date,b.judge_name "
					+ " from ecourts_causelist_bench_data  a left join ecourts_causelist_data b on (a.bench_id=b.bench_id)   where a.causelist_document is not null "
					+ " and  POSITION('RECORD_NOT_FOUND' in a.causelist_document) = 0 and POSITION('INVALID_TOKEN' in a.causelist_document) = 0 "
					+ " and a.causelist_date=to_date('9/2/2022','mm/dd/yyyy') and coalesce(causelist_document,'') not like '%status_code%' ) x1   "
					+ " order by x1.bench_id, x1.causelist_date desc ) c group by c.bench_id, c.est_code,judge_name";
			

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

			//System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("causelist", data);
			else
				request.setAttribute("errorMsg", "No Causelist details received for Today.");

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
					
					getHighCourtCauseListPDFs(con, causelistDate);
					
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
		} 
	}
	
	
	public static void getHighCourtCauseListPDFs(Connection con, String causelistDate) {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		try {
			String opVal = ECourtAPIs.getSelectParam(13);
			
			String estCode="APHC01", bench_id="",causelist_id="" ;
			
			sql="SELECT slno, est_code, causelist_date, bench_id, inserted_time,causelist_id, cause_list_type FROM apolcms.ecourts_causelist_bench_data where "
					//+ "slno=36";
					+ "causelist_document is null and causelist_id is not null order by causelist_date";
			System.out.println("SQL:"+sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				causelistDate = rs.getString("causelist_date"); 
				bench_id = rs.getString("bench_id"); 
				causelist_id = rs.getString("causelist_id"); 
				
				if(causelistDate!=null && bench_id!=null && causelist_id!=null && !causelistDate.equals("") && !bench_id.equals("") && !causelist_id.equals(""))
				{
					inputStr = "est_code="+estCode+"|causelist_date="+causelistDate+"|bench_id="+bench_id+"|causelist_id="+causelist_id;//ECourtAPIs.getInputStringValue(opVal);
					System.out.println("inputStr:"+inputStr);
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
						 HighCourtCauseListPDFAPI.processPDForderResponse(resp, estCode, causelistDate, bench_id,causelist_id, con);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public ActionForward deptWiseCauseListCases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

            sql=" select to_char(ecc.causelist_date::date,'dd-mm-yyyy') as causelist_date, ecd.dept_code,upper(d.description) as description, count(*) as casescount from ecourts_causelist_cases ecc "
            		+ "inner join ecourts_case_data ecd on (ecc.case_no=ecd.type_name_reg||'/'||ecd.reg_no||'/'||ecd.reg_year) "
            		//+ "left join nic_resp_addr_data ra on (ecd.cino=ra.cino and party_no=1) "
            		+ "left join dept_new d on (ecd.dept_code=d.dept_code) "
            		+ "where ecc.causelist_date::date = to_date('"+date+"','mm-dd-yyyy') group by ecd.dept_code,d.description, ecc.causelist_date order by casescount desc ";

            System.out.println("SQL:" + sql);
            List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

            System.out.println("data=" + data);
            if (data != null && !data.isEmpty() && data.size() > 0) {
                request.setAttribute("DEPTCAUSELISTCASES", data);
            }
            else
            	request.setAttribute("errorMsg", "No Causelist details received for Today.");

            request.setAttribute("HEADING", "Dept. Wise Cause List Cases on Dt.:"+date);
            cform.setDynaForm("list_date" , cform.getDynaForm("list_date"));

        } catch (Exception e) {
            request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
            e.printStackTrace();
        } finally {
            DatabasePlugin.closeConnection(con);
        }
        return mapping.findForward("success");
    }
	
	public ActionForward getCauseReportList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PreparedStatement ps = null;
        CommonForm cform = (CommonForm) form;
        Connection con = null;
        HttpSession session = null;
        String userId = null, roleId = null, sql = null, causelist_date = null,deptCode=null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", 
				 caseStatus = null, distId=null;
		try {

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
            
			causelist_date = CommonModels.checkStringObject(cform.getDynaForm("causelist_date"));
            deptCode = deptCode!=null && !deptCode.equals("") ? deptCode : CommonModels.checkStringObject(cform.getDynaForm("dept_code"));
            deptCode = deptCode!=null && !deptCode.equals("") ? deptCode : CommonModels.checkStringObject(request.getParameter("dept_code"));
            
            if(causelist_date ==null || causelist_date.equals("")) {
            	causelist_date = CommonModels.checkStringObject(request.getParameter("causelist_date"));
            }
            System.out.println("date:::::::::::::::" + causelist_date);
            con = DatabasePlugin.connect();

            sql = "select ecc.causelist_date::date as causelist_date ,a.*, "
					+ ""
					+ "coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address "
					+ " from ecourts_causelist_cases ecc "
                    + " inner join ecourts_case_data a on (ecc.case_no=a.type_name_reg||'/'||a.reg_no||'/'||a.reg_year) "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ " left join"
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					+ " on (a.cino=b.cino) "
					+ " inner join dept_new d on (a.dept_code=d.dept_code)  "
					+ " where d.display = true and ecc.causelist_date::date = to_date('" + causelist_date+ "','mm-dd-yyyy') ";
            
            if(roleId.equals("2") || roleId.equals("10")) {
            	sql+=" and a.dist_id='"+distId+"'";
            }
            
            if(!roleId.equals("2")) {
            	sql+=" and a.dept_code='"+deptCode+"'";
            }

            System.out.println("SQL:" + sql);
            List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

            System.out.println("data=" + data);
            if (data != null && !data.isEmpty() && data.size() > 0)
            	request.setAttribute("CASESLIST", data);
            else
            	request.setAttribute("errorMsg", "No Causelist details received for Today.");

            request.setAttribute("HEADING", "High Court Cause List ");
            cform.setDynaForm("list_date" , cform.getDynaForm("list_date"));

            request.setAttribute("show", "causeReportList");

        } catch (Exception e) {
            request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
            e.printStackTrace();
        } finally {
            DatabasePlugin.closeConnection(con);
        }
        return mapping.findForward("success");
    }
	
	
	public ActionForward getDashboardCauseList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PreparedStatement ps = null;
        CommonForm cform = (CommonForm) form;
        Connection con = null;
        HttpSession session = null;
        String userId = null, roleId = null, sql = null, causelist_date = null,deptCode=null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", 
				 caseStatus = null, distId=null;
		try {

			session = request.getSession();
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
            con = DatabasePlugin.connect();

            sql = "select ecc.causelist_date::date as causelist_date ,a.*, "
					+ ""
					+ " coalesce(trim(a.scanned_document_path),'-') as scanned_document_path1, b.orderpaths, prayer, ra.address "
					+ " from ecourts_causelist_cases ecc "
                    + " inner join ecourts_case_data a on (ecc.case_no=a.type_name_reg||'/'||a.reg_no||'/'||a.reg_year) "
					+ " left join nic_prayer_data np on (a.cino=np.cino)"
					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
					+ " left join"
					
					+ " ("
					+ " select cino, string_agg('<a href=\"./'||order_document_path||'\" target=\"_new\" class=\"btn btn-sm btn-info\"><i class=\"glyphicon glyphicon-save\"></i><span>'||order_details||'</span></a><br/>','- ') as orderpaths"
					+ " from "
					+ " (select * from (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_interimorder where order_document_path is not null and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) x1" + " union"
					+ " (select cino, order_document_path,order_date,order_details||' Dt.'||to_char(order_date,'dd-mm-yyyy') as order_details from ecourts_case_finalorder where order_document_path is not null"
					+ " and  POSITION('RECORD_NOT_FOUND' in order_document_path) = 0"
					+ " and POSITION('INVALID_TOKEN' in order_document_path) = 0 ) order by cino, order_date desc) c group by cino ) b"
					
					+ " on (a.cino=b.cino) "
					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
					+ " where d.display = true and ecc.causelist_date::date=current_date  ";
            
            if(roleId.equals("2") || roleId.equals("10")) {
            	sql+=" and a.dist_id='"+distId+"'";
            }
            
            if(!roleId.equals("2") && !roleId.equals("1") && !roleId.equals("7")) {
            	sql+=" and a.dept_code='"+deptCode+"'";
            }
            sql+=" order by ecc.causelist_date::date desc";
            System.out.println("SQL:" + sql);
            List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

            if (data != null && !data.isEmpty() && data.size() > 0)
            	request.setAttribute("CASESLIST", data);
            else
                request.setAttribute("errorMsg", "No Causelist details received for Today.");

            request.setAttribute("HEADING", "High Court Cause List ");
            cform.setDynaForm("list_date" , cform.getDynaForm("list_date"));

            request.setAttribute("show", "causeReportList");

        } catch (Exception e) {
            request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
            e.printStackTrace();
        } finally {
            DatabasePlugin.closeConnection(con);
        }
        return mapping.findForward("success");
    }
	
	
	public ActionForward usersCauseList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {PreparedStatement ps = null;
            CommonForm cform = (CommonForm) form;
            Connection con = null;
            HttpSession session = null;
            String userId = null, roleId = null, causelist_date = null,deptCode=null, sqlCondition = "", actionType = "", deptId = "", deptName = "", heading = "", 
    				 caseStatus = null, distId=null;
    		try {

    			session = request.getSession();
    			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));
    			deptCode = CommonModels.checkStringObject(session.getAttribute("dept_code"));
    			distId = CommonModels.checkStringObject(session.getAttribute("dist_id"));
                con = DatabasePlugin.connect();

                String sqlYesterday="";
                String sqlToday="";
                String sqlTomorrow="";
                
                sqlYesterday = "select ecc.causelist_date::date as causelist_date ,a.*, "
    					+ " ra.address "
    					+ " from ecourts_causelist_cases ecc "
                        + " inner join ecourts_case_data a on (ecc.case_no=a.type_name_reg||'/'||a.reg_no||'/'||a.reg_year) "
    					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
    					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
    					+ " where d.display = true  and ecc.causelist_date::date=current_date-1 ";
                if(roleId.equals("2") || roleId.equals("10")) {
                	sqlYesterday+=" and a.dist_id='"+distId+"'";
                }
                if(!roleId.equals("2")) {
                	sqlYesterday+=" and a.dept_code='"+deptCode+"'";
                }
                sqlYesterday+=" order by ecc.causelist_date::date desc ";
                System.out.println("sqlYesterday:" + sqlYesterday);
                List<Map<String, Object>> data = DatabasePlugin.executeQuery(sqlYesterday, con);
                if (data != null && !data.isEmpty() && data.size() > 0)
                	request.setAttribute("CAUSELISTCASESYESTERDAY", data);
                else
                	request.setAttribute("errorMsgYesterday", "No Causelist details received for Yesterday.");
                
                sqlToday = "select ecc.causelist_date::date as causelist_date ,a.*, "
    					+ " ra.address "
    					+ " from ecourts_causelist_cases ecc "
                        + " inner join ecourts_case_data a on (ecc.case_no=a.type_name_reg||'/'||a.reg_no||'/'||a.reg_year) "
    					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
    					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
    					+ " where d.display = true  and ecc.causelist_date::date=current_date ";
                if(roleId.equals("2") || roleId.equals("10")) {
                	sqlToday+=" and a.dist_id='"+distId+"'";
                }
                if(!roleId.equals("2")) {
                	sqlToday+=" and a.dept_code='"+deptCode+"'";
                }
                sqlToday+=" order by ecc.causelist_date::date desc ";
                System.out.println("sqlToday:" + sqlToday);
                data = DatabasePlugin.executeQuery(sqlToday, con);
                if (data != null && !data.isEmpty() && data.size() > 0)
                	request.setAttribute("CAUSELISTCASESTODAY", data);
                else
                	request.setAttribute("errorMsgToday", "No Causelist details received for Today.");
                
                
                sqlTomorrow = "select ecc.causelist_date::date as causelist_date ,a.*, "
    					+ " ra.address "
    					+ " from ecourts_causelist_cases ecc "
                        + " inner join ecourts_case_data a on (ecc.case_no=a.type_name_reg||'/'||a.reg_no||'/'||a.reg_year) "
    					+ " left join nic_resp_addr_data ra on (a.cino=ra.cino and party_no=1) "
    					+ " inner join dept_new d on (a.dept_code=d.dept_code) "
    					+ " where d.display = true  and ecc.causelist_date::date=current_date+1 ";
                if(roleId.equals("2") || roleId.equals("10")) {
                	sqlTomorrow+=" and a.dist_id='"+distId+"'";
                }
                if(!roleId.equals("2")) {
                	sqlTomorrow+=" and a.dept_code='"+deptCode+"'";
                }
                sqlTomorrow+=" order by ecc.causelist_date::date desc ";
                System.out.println("sqlTomorrow:" + sqlTomorrow);
                 data = DatabasePlugin.executeQuery(sqlTomorrow, con);

                if (data != null && !data.isEmpty() && data.size() > 0)
                	request.setAttribute("CAUSELISTCASESTOMORROW", data);
                else
                	request.setAttribute("errorMsgTomorow", "No Causelist details received for Tomorrow.");
                
                
                request.setAttribute("HEADING", "High Court Cause List ");

            } catch (Exception e) {
                request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
                e.printStackTrace();
            } finally {
                DatabasePlugin.closeConnection(con);
            }
            return mapping.findForward("causelistpopup");
      }
	
}