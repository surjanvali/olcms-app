package in.apcfss.struts.eCourt.apis;

import java.io.FileWriter;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import plugins.DatabasePlugin;



public class ImportECourtsData
{
  // static final String apolcmsDataBase = "jdbc:postgresql://172.16.98.2:9432/apolcms";
  static final String apolcmsDataBase = "jdbc:postgresql://localhost:5432/apolcms";
  static final String apolcmsUserName = "apolcms";
  static final String apolcmsPassword = "apolcms";
  
  /* Error */
  public static void main(String[] args)
    throws Exception
  {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql="";int totalCount=0,successCount=0, failCount=0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);
			
			String opVal = ECourtAPIs.getSelectParam(1);
			
			System.out.println("opVal:"+opVal);
			
			// sql="select cino from ecourts_cinos where ecourts_response is null and cino not in (select cino from ecourts_case_data) order by slno";
			/* sql="select cino from ecourts_cinos_new  where ecourts_response is null and dept_name in ('MISCELLANEOUS','FORESTENERGYENVIRONMENTSCIENCE & TECH.DEPT','EXCISE  (MISC.MATTERS)','TIRUMALA TIRUPATHI DEVASTHANAM (MISC.MATTERS)','PIL Cases','MOTOR VEHICLES ACT','WAKF BOARD (MISC.MATTERS)','MISCELLANEOUS MATTERS','W.C.ACT & E.S.I. ACT','CENTRAL ADMINISTRATIVE TRIBUNAL','TAX REVISION CASES','ROADS AND BUILDINGS','Income Tax Tribunal Appeals','PROVIDENT FUND (MISC.MATTERS)','FINANCE & PLANNING (MISC.MATTERS)','Securitization Act Cases','WPs Challanging the Vires of Rules/Section/Acts. incl Tax','TRANSPORT (MISC.MATTERS)','RAILWAY (MISC.MATTERS)','JUDICIAL MATTERS','SUITS FOR DAMAGES','LEGAL SERVICE AUTHORITY','PORT TRUST/DOCK LABOUR BOARD','DISPOSAL OTHERWISE (PARTLY ALLOWED DISPOSED OF REMANDED ETC','CENTRAL GOVT. UNDERTAKINGS','RURAL BANKS','ARBITRATION ACT','APPSC (MISC.MATTERS)','RIGHT TO INFORMATION ACT','OTHER DECLARATORY SUITS','FACTORIES','CIVIL MISC. SECOND APPEALS','GENERAL ADMINISTRTION DEPT.(MISC.MATTERS)','RENT CONTROL','L.I.C (MISC.MATTERS)','OTHERS','CENTRAL EXCISE APPEAL','CENTRAL EXCISE  & CUSTOMS (MISC.MATTERS)','CUSTOMS AND CENTRAL EXCISE (MISC.MATTERS)','HOUSING BOARD  (MISC.MATTERS)','Debt Recovery tribunal Matters','INCOME TAX AND WELTH TAX (MISC.MATTERS)','EMPLOYMENT GENERATION & YOUTH SERVICES','INSURANCE','FOOD CORPORATION OF INDIA (MISC.MATTERS)','Pocso Act','LAW DEPT.','GUARDIANS & WARDS ACT','LAND GRABBING','SPORTS AUTHORITY','POST AND TELEGRPH DEPARTMENT (MISC.MATTERS)','URBAN LAND CEILING (MISC.MATTERS)','MAINTENANCE','LAND REFORMS','INDIAN SUCCESSION ACT','PARTNERSHIP SUITS','TRUSTS & ENDOWMENTS','HABEAS CORPUS','COMMISSIONS','DEFENCE MATTERS (MISC.MATTERS)','CISF','CONSUMER FORUM','INDUSTRIES & FINANCIAL RECONSTRUCTION','LEGISLATIVE DEPT.(MISC.MATTERS)','LAND GRABBING APPEAL','COURTS','ALL SERVICE MATTERS OTHER THAN MENTIONED','RAILWAY CLAIMS TRIBUNAL','ARBITRATION APLICATION','COMPANY PETITION','LAND REFORMS CASES (MISC.MATTERS)','LATTER PATENT APPEAL','Wealth Tax Appeals','ORIGINAL SIDE APPEALS','ARBTRATION','ARCHAEOLOGY & MUSEUMS','TR CMP','LIABRARIES / GRANDHALAYAS','TAKEN UP CASES','RC','SINGARENI COLLORIES (MISC.MATTERS)','APPLICATION','CIVIL SUITS','INSTITUTES','MARKET COMMITTEE','COMPANY APPLICATION','ELECTION PETITION','SMALL CAUSES','ARBITRATION','Arbitration Act in WP category','CC Suomoto Cases','ENTERTAINMENT TAX','INSOLVENCY','AP HIGH COURT SERVICE MATTERS','COMPANY APPEAL','TENANCY REVISION','AP LAW OFFICERS RECURITMENT  (MISC.MATTERS)','CINEMAS (MISC.MATTERS)','DEPT. OF PERSONEL & TRAINING','EXECUTION PETITION','PUBLIC SECTOR','P.W.D. (MISC.MATTERS)','SECUNDERBAD CONTONMENT MATTERS','TITLE SUITS(IMMOVABLE PROPERTY)')"
					+ "and cino not in (select cino from ecourts_case_data) "
					+ "and cino not in (select cino from ecourts_cinos) "
					+ "order by dept_name limit 50000"; */
			// sql="select cino from ecourts_cinos_2022 where ecourts_response is null and cino in ('APHC010522732021','APHC010488842021','APHC010511362021','APHC010521482021','APHC010510322021','APHC010502132021','APHC010516642021','APHC010500622021','APHC010517472021','APHC010519952021','APHC010501022021','APHC010519832021','APHC010507302021','APHC010518872021','APHC010496942021','APHC010511042021','APHC010517392021','APHC010513312021','APHC010511152021','APHC010507822021','APHC010504922021','APHC010508922021','APHC010522012021','APHC010518522021','APHC010508352021','APHC010516982021','APHC010517102021','APHC010510182021','APHC010503182021','APHC010523112021','APHC010527752021','APHC010516382021','APHC010514472021','APHC010519422021','APHC010520712021')";
			// sql = "select cino from ecourts_cinos_new where cino in ('APHC010860492018','APHC010806812018','APHC010163702018','APHC010809902018','APHC010882852018','APHC010923532018','APHC010163722018','APHC010800242018','APHC010816552018')"; //ecourts_response is null and
			// sql = "select cino from ecourts_cinos_new where cino in ('APHC010202442020','APHC010202432020','APHC010062562018','APHC010214192006')"; //ecourts_response is null and
			// sql = "select cino from ecourts_cinos_new where cino in ('APHC010512122021','APHC010514102021','APHC010516852021','APHC010518262021','APHC010518442021','APHC010521522021','APHC010521642021','APHC010521932021','APHC010522062021','APHC010522302021','APHC010522342021','APHC010522722021','APHC010523122021','APHC010523142021','APHC010523282021','APHC010523412021','APHC010523472021','APHC010523662021','APHC010523982021','APHC010524082021','APHC010524182021','APHC010524262021','APHC010524302021','APHC010524402021','APHC010524462021','APHC010525022021','APHC010525062021','APHC010526072021','APHC010526322021','APHC010527752021','APHC010527982021')"; //ecourts_response is null and
			// sql = "select cino from ecourts_cinos_new where to_char(inserted_time,'dd/mm/yyyy hh:mi:ss')='18/01/2022 02:12:48' and  ecourts_response is not null ";
			// sql = "select distinct cino from ecourts_cinos_new where to_char(inserted_time,'dd/mm/yyyy')=current_date and ecourts_response is null and cino='APHC010000942022'";
			// sql = "select distinct a.cino from ecourts_contempt_cinos a left join ecourts_case_data b using (cino) where b.cino is null ";
			// sql ="select distinct a.cino from ecourts_cinos_new a where cino='APHC010080012022'";
			// sql = "select distinct a.cino from ecourts_cinos_1105_1605 a left join ecourts_case_data b using (cino) where b.cino is null ";
			//sql = "select distinct a.cino from ecourts_cinos_1705_2305 a left join ecourts_case_data b using (cino) where b.cino is null";
			
			sql = "select distinct cino from ecourts_cinos_new limit 1";//where to_char(inserted_time,'dd/mm/yyyy')=current_date and ecourts_response is null";
			System.out.println("SQL:"+sql);
			
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				totalCount++;
				inputStr = "cino=" + rs.getString("cino").trim();//ECourtAPIs.getInputStringValue(opVal);
				
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");
				
				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);
				
				System.out.println(totalCount+":URL : "+targetURL);
				System.out.println("Input String : "+inputStr);
				
				authToken = EHighCourtAPI.getAuthToken();
				String resp="";
				if(opVal!=null && (opVal.equals("hcCurrentStatus") || opVal.equals("dcCurrentStatus"))) {
					resp = EHighCourtAPI.sendPostRequest(targetURL, authToken);
				}
				else if(opVal!=null && !opVal.equals("")){
					try {
						resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				// resp=null;
				if (resp != null && !resp.equals("")) {
					try {
						String response_str="", decryptedRespStr="";
						JSONObject jObj = new JSONObject(resp);
					      if ((jObj.has("response_str")) && (jObj.getString("response_str") != null)) {
					        response_str = jObj.getString("response_str").toString();
					      }
					      
					      if ((response_str != null) && (!response_str.equals(""))) {
					          decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
					        }
					      System.out.println("decryptedRespStr:"+decryptedRespStr);
						 processCNRsearchResponse(resp, opVal, con, rs.getString("cino").trim());
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("FINAL END : Records fetched:"+totalCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(con!=null)
				con.close();
		}
	}
  
  public static void processCNRsearchResponse(String resp, String fileName, Connection con, String cino)
    throws Exception
  {
    String response_str = "";String response_token = "";String version = "";String decryptedRespStr = "";String sql = "";
    resp = resp.trim();
    
    System.out.println("processCNRsearchResponse:"+resp);
    if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN")))
    {
      JSONObject jObj = new JSONObject(resp);
      if ((jObj.has("response_str")) && (jObj.getString("response_str") != null)) {
        response_str = jObj.getString("response_str").toString();
      }
      if ((jObj.has("response_token")) && (jObj.getString("response_token") != null)) {
        response_token = jObj.getString("response_token").toString();
      }
      if ((jObj.has("version")) && (jObj.getString("version") != null)) {
        version = jObj.getString("version").toString();
      }
      if ((response_str != null) && (!response_str.equals(""))) {
        decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
      }
      JSONObject jObjCaseData = new JSONObject(decryptedRespStr);
      ArrayList<String> sqls = new ArrayList();
      
      //FileWriter file = new FileWriter("D:\\HighCourtsResponces\\" + checkStringJSONObj(jObjCaseData, "cino") + ".txt");
      
      sql = "INSERT INTO apolcms.ecourts_case_data(date_of_filing, cino, dt_regis, type_name_fil, type_name_reg, case_type_id, fil_no, fil_year, reg_no, reg_year, date_first_list, date_next_list, pend_disp, date_of_decision, disposal_type, bench_type, causelist_type, bench_name, judicial_branch, coram, short_order, bench_id, court_est_name, est_code, state_name, dist_name, purpose_name, pet_name, pet_adv, pet_legal_heir, res_name, res_adv, res_legal_heir, main_matter, fir_no, police_station, fir_year, lower_court_name, lower_court_caseno, lower_court_dec_dt, trial_lower_court_name, trial_lower_court_caseno, trial_lower_court_dec_dt, date_last_list, main_matter_cino, date_filing_disp, reason_for_rej, category, sub_category) values ( to_date('" + 
      
        jObjCaseData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "cino") + "' ," + 
        " to_date('" + jObjCaseData.get("dt_regis").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "type_name_fil") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "type_name_reg") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "case_type_id") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "fil_no") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "fil_year") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "reg_no") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "reg_year") + "' ," + 
        " to_date('" + jObjCaseData.get("date_first_list").toString() + "', 'yyyy-mm-dd'), " + 
        " to_date('" + jObjCaseData.get("date_next_list").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "pend_disp") + "' ," + 
        " to_date('" + jObjCaseData.get("date_of_decision").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "disposal_type") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "bench_type") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "causelist_type") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "bench_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "judicial_branch") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "coram") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "short_order") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "bench_id") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "court_est_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "est_code") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "state_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "dist_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "purpose_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "pet_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "pet_adv") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "pet_legal_heir") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "res_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "res_adv") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "res_legal_heir") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "main_matter") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "fir_no") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "police_station") + "' ," + 
        "'" + checkIntegerJSONObj(jObjCaseData, "fir_year") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "lower_court_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "lower_court_caseno") + "' ," + 
        " to_date('" + jObjCaseData.get("lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "trial_lower_court_name") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "trial_lower_court_caseno") + "' ," + 
        " to_date('" + jObjCaseData.get("trial_lower_court_dec_dt").toString() + "', 'yyyy-mm-dd'), " + 
        " to_date('" + jObjCaseData.get("date_last_list").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "main_matter_cino") + "' ," + 
        " to_date('" + jObjCaseData.get("date_filing_disp").toString() + "', 'yyyy-mm-dd'), " + 
        "'" + checkStringJSONObj(jObjCaseData, "reason_for_rej") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "category") + "' ," + 
        "'" + checkStringJSONObj(jObjCaseData, "sub_category") + "' )";
      
      //System.out.println("SQL:" + sql);
      sqls.add(sql);
      //file.write("\n" + sql);
      //file.write("\n GO");
      
      System.out.println("acts:" + checkStringJSONObj(jObjCaseData, "acts"));
      JSONObject jObjActsData = new JSONObject();
      JSONObject jObjActsInnerData = new JSONObject();
      if ((checkStringJSONObj(jObjCaseData, "acts") != null) && (checkStringJSONObj(jObjCaseData, "acts") != "null") && (!checkStringJSONObj(jObjCaseData, "acts").equals("")) && (!checkStringJSONObj(jObjCaseData, "acts").equals("[]")))
      {
        jObjActsData = new JSONObject(checkStringJSONObj(jObjCaseData, "acts"));
        for (int i = 1; i <= jObjActsData.length(); i++)
        {
          jObjActsInnerData = new JSONObject(jObjActsData.get("act" + i).toString());
          
          sql = "INSERT INTO apolcms.ecourts_case_acts(cino, act, actname, section) VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjActsInnerData, "actname") + "', '" + checkStringJSONObj(jObjActsInnerData, "section") + "')";
          //System.out.println("ACTS SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("historyofcasehearing:" + checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
      JSONObject jObjHistoryData = new JSONObject();
      JSONObject jObjHistoryInnerData = new JSONObject();
      if ((checkStringJSONObj(jObjCaseData, "historyofcasehearing") != null) && (checkStringJSONObj(jObjCaseData, "historyofcasehearing") != "null") && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("")) && (!checkStringJSONObj(jObjCaseData, "historyofcasehearing").equals("[]")))
      {
        jObjHistoryData = new JSONObject(checkStringJSONObj(jObjCaseData, "historyofcasehearing"));
        for (int i = 1; i <= jObjHistoryData.length(); i++)
        {
          jObjHistoryInnerData = new JSONObject(jObjHistoryData.get("sr_no" + i).toString());
          
          sql = "INSERT INTO apolcms.ecourts_historyofcasehearing(cino, sr_no, judge_name, business_date, hearing_date, purpose_of_listing, causelist_type)  VALUES('" + 
            checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + 
            checkStringJSONObj(jObjHistoryInnerData, "judge_name") + "', " + " to_date('" + jObjHistoryInnerData.get("business_date").toString() + "', 'yyyy-mm-dd'), ";
          if ((jObjHistoryInnerData.get("hearing_date") != null) && (!jObjHistoryInnerData.get("hearing_date").toString().equals("Next Date Not Given"))) {
            sql = sql + " to_date('" + jObjHistoryInnerData.get("hearing_date").toString() + "', 'yyyy-mm-dd'), ";
          } else {
            sql = sql + " to_date(null, 'yyyy-mm-dd'), ";
          }
          sql = 
            sql + " '" + checkStringJSONObj(jObjHistoryInnerData, "purpose_of_listing") + "', '" + checkStringJSONObj(jObjHistoryInnerData, "causelist_type") + "')";
          //System.out.println("historyofcasehearing SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("pet_extra_party:" + checkStringJSONObj(jObjCaseData, "pet_extra_party"));
      JSONObject jObjMainData = new JSONObject();
      JSONObject jObjInnerData = new JSONObject();
      if ((checkStringJSONObj(jObjCaseData, "pet_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "pet_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "pet_extra_party").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "pet_extra_party"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          sql = 
            "INSERT INTO apolcms.ecourts_pet_extra_party(cino, party_no, party_name) VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString()) + "')";
         // System.out.println("pet_extra_party SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("res_extra_party:" + checkStringJSONObj(jObjCaseData, "res_extra_party"));
      if ((checkStringJSONObj(jObjCaseData, "res_extra_party") != null) && (checkStringJSONObj(jObjCaseData, "res_extra_party") != "null") && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("")) && (!checkStringJSONObj(jObjCaseData, "res_extra_party").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "res_extra_party"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          sql = 
          
            "INSERT INTO apolcms.ecourts_res_extra_party(cino, party_no, party_name)  VALUES('" + checkStringJSONObj(jObjCaseData, "cino") + "', " + i + ", '" + checkStringJSONObj(jObjMainData, new StringBuilder("party_no").append(i).toString()) + "')";
          //System.out.println("res_extra_party SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("interimorder:" + checkStringJSONObj(jObjCaseData, "interimorder"));
      if ((checkStringJSONObj(jObjCaseData, "interimorder") != null) && (checkStringJSONObj(jObjCaseData, "interimorder") != "null") && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("")) && (!checkStringJSONObj(jObjCaseData, "interimorder").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "interimorder"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
          sql = "INSERT INTO apolcms.ecourts_case_interimorder(cino, sr_no, order_no, order_date, order_details)  VALUES('" + 
            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
            checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
          if ((jObjInnerData.get("order_date") != null) && (jObjInnerData.get("order_date").toString() != "null")) {
            sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd'),";
          } else {
            sql = sql + " to_date(null, 'yyyy-mm-dd'),";
          }
          sql = sql + " '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";
          
          //System.out.println("interimorder SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
      if ((checkStringJSONObj(jObjCaseData, "finalorder") != null) && (checkStringJSONObj(jObjCaseData, "finalorder") != "null") && (!checkStringJSONObj(jObjCaseData, "finalorder").equals("")) && 
        (!checkStringJSONObj(jObjCaseData, "finalorder").equals("[]")))
      {
        //System.out.println("finalorder:" + checkStringJSONObj(jObjCaseData, "finalorder"));
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "finalorder"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
          sql = "INSERT INTO apolcms.ecourts_case_finalorder(cino, sr_no, order_no, order_date, order_details)  VALUES('" + 
            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
            checkIntegerJSONObj(jObjInnerData, "order_no") + "',";
          if ((jObjInnerData.get("order_date") != null) && (jObjInnerData.get("order_date").toString() != "null")) {
            sql = sql + " to_date('" + jObjInnerData.get("order_date").toString() + "', 'yyyy-mm-dd')";
          } else {
            sql = sql + " to_date(null, 'yyyy-mm-dd')";
          }
          sql = sql + ", '" + checkStringJSONObj(jObjInnerData, "order_details") + "')";
          //System.out.println("finalorder SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("iafiling:" + checkStringJSONObj(jObjCaseData, "iafiling"));
      if ((checkStringJSONObj(jObjCaseData, "iafiling") != null) && (checkStringJSONObj(jObjCaseData, "iafiling") != "null") && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("")) && (!checkStringJSONObj(jObjCaseData, "iafiling").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "iafiling"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
          sql = "INSERT INTO apolcms.ecourts_case_iafiling(cino, sr_no, ia_number, ia_pet_name, ia_pend_disp, date_of_filing) VALUES('" + 
            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
            checkStringJSONObj(jObjInnerData, "ia_number") + "','" + 
            checkStringJSONObj(jObjInnerData, "ia_pet_name") + "','" + 
            checkStringJSONObj(jObjInnerData, "ia_pend_disp") + "',";
          if ((jObjInnerData.has("date_of_filing")) && (jObjInnerData.get("date_of_filing") != null) && (jObjInnerData.get("date_of_filing").toString() != "null")) {
            sql = sql + " to_date('" + jObjInnerData.get("date_of_filing").toString() + "', 'yyyy-mm-dd'))";
          } else {
            sql = sql + " to_date(null, 'yyyy-mm-dd'))";
          }
         // System.out.println("iafiling SQL:" + sql);
          sqls.add(sql);
          //file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("link_cases:" + checkStringJSONObj(jObjCaseData, "link_cases"));
      if ((checkStringJSONObj(jObjCaseData, "link_cases") != null) && (checkStringJSONObj(jObjCaseData, "link_cases") != "null") && (!checkStringJSONObj(jObjCaseData, "link_cases").equals("")) && (!checkStringJSONObj(jObjCaseData, "link_cases").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "link_cases"));
        for (int i = 1; i <= jObjMainData.length(); i++)
        {
          jObjInnerData = new JSONObject(jObjMainData.get("sr_no" + i).toString());
          sql = "INSERT INTO apolcms.ecourts_case_link_cases(cino, sr_no, filing_number, case_number) VALUES('" + 
            checkStringJSONObj(jObjCaseData, "cino") + "'," + i + " ,'" + 
            checkStringJSONObj(jObjInnerData, "filing_number") + "','" + 
            checkStringJSONObj(jObjInnerData, "case_number") + "')";
         // System.out.println("link_cases SQL:" + sql);
          sqls.add(sql);
         // file.write("\n" + sql);
          //file.write("\n GO");
        }
      }
      System.out.println("objections:" + checkStringJSONObj(jObjCaseData, "objections"));
      if ((checkStringJSONObj(jObjCaseData, "objections") != null) && (checkStringJSONObj(jObjCaseData, "objections") != "null") && (!checkStringJSONObj(jObjCaseData, "objections").equals("")) && (!checkStringJSONObj(jObjCaseData, "objections").equals("[]")))
      {
        jObjMainData = new JSONObject(checkStringJSONObj(jObjCaseData, "objections"));
        System.out.println("-" + jObjMainData.length());
        
        sql = "INSERT INTO apolcms.ecourts_case_objections(cino, objection_no, objection_desc, scrutiny_date, objections_compliance_by_date, obj_reciept_date) VALUES('" + 
          checkStringJSONObj(jObjCaseData, "cino") + "',1 ,'" + 
          checkStringJSONObj(jObjMainData, "objection1") + "',";
        if ((jObjMainData.get("scrutiny_date") != null) && (jObjMainData.get("scrutiny_date").toString() != "null")) {
          sql = sql + " to_date('" + jObjMainData.get("scrutiny_date").toString() + "', 'yyyy-mm-dd'),";
        } else {
          sql = sql + " to_date(null, 'yyyy-mm-dd'),";
        }
        if ((jObjMainData.get("objections_compliance_by_date") != null) && (jObjMainData.get("objections_compliance_by_date").toString() != "null")) {
          sql = sql + " to_date('" + jObjMainData.get("objections_compliance_by_date").toString() + "', 'yyyy-mm-dd'),";
        } else {
          sql = sql + " to_date(null, 'yyyy-mm-dd'),";
        }
        if ((jObjMainData.get("obj_reciept_date") != null) && (jObjMainData.get("obj_reciept_date").toString() != "null")) {
          sql = sql + " to_date('" + jObjMainData.get("obj_reciept_date").toString() + "', 'yyyy-mm-dd'))";
        } else {
          sql = sql + " to_date(null, 'yyyy-mm-dd'))";
        }
        //System.out.println("objections SQL:" + sql);
        sqls.add(sql);
       // file.write("\n" + sql);
        //file.write("\n GO");
      }
      int executedSqls = 0;
      if (sqls.size() > 0) {
    	  
    	  // sql="update ecourts_cinos_2022 set ecourts_response=null where cino='"+cino+"' and ecourts_response is not null"; 
    	  // sql="update ecourts_cinos_new set ecourts_response=null where cino='"+cino+"' and ecourts_response is not null"; 
    	  // sql="update ecourts_cinos_2022_1 set ecourts_response=null where cino='"+cino+"' and ecourts_response is not null";
    	  sql="update ecourts_cinos_new set ecourts_response='UPDATED' where cino='"+cino+"' and ecourts_response is not null";
    	  sqls.add(sql);
    	  
    	  executedSqls = DatabasePlugin.executeBatchSQLs(sqls, con);
      }
      /*file.write("\n");
      file.write("\n");
      file.flush();
      file.close();
      */
      System.out.println("Successfully saved...executedSqls:"+executedSqls);
      
      System.out.println("END");
    }
    else
    {
    	// sql="update ecourts_cinos_new set ecourts_response='"+resp+"' where cino='"+cino+"'";
    	//sql="update ecourts_cinos_2022_1 set ecourts_response='"+resp+"' where cino='"+cino+"'";
    	//sql="update ecourts_cinos_2022 set ecourts_response='"+resp+"' where cino='"+cino+"'";
    	sql="update ecourts_cinos_new set ecourts_response='"+resp+"' where cino='"+cino+"'";
    	DatabasePlugin.executeUpdate(sql, con);
    	System.out.println("Invalid/Empty Response::"+"SQL:"+sql);
    }
  }
  
  
  
  
  
  public static int checkIntegerJSONObj(JSONObject jObj, String jObjKey)
    throws NumberFormatException, JSONException
  {
    int a = 0;
    if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals("")) && (jObj.get(jObjKey) != null) && (!jObj.get(jObjKey).toString().trim().equals(""))  && (!jObj.get(jObjKey).toString().trim().equals("null"))) {
      a = Integer.parseInt(jObj.get(jObjKey).toString().trim());
    }
    return a;
  }
  
  public static String checkStringJSONObj(JSONObject jObj, String jObjKey)
    throws JSONException
  {
    String a = "";
    if ((jObj != null) && (jObj.has(jObjKey)) && (jObjKey != null) && (!jObjKey.equals("")) && (jObj.get(jObjKey) != null) && (!jObjKey.equals("null"))) {
      a = jObj.get(jObjKey).toString().trim().replace("'", "");
    }
    return a;
  }
}