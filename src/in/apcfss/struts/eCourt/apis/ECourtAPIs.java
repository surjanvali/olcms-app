package in.apcfss.struts.eCourt.apis;

import java.math.BigInteger;
import java.net.URLEncoder;

public class ECourtAPIs {

	private static final String deptId = "SE00031";
	private static final String version = "v1.0";
	private static final String apiBaseURL = "https://egw.bharatapi.gov.in/t/ecourts.gov.in/";

	// HIGH COURT API URLS

	private static String hcCNRSearchURL = apiBaseURL + "HighCourt-CNRSearch/v1.0/cnrFullCaseDetails";
	private static String hcShowBusinessURL = apiBaseURL + "HighCourt-ShowBusiness/v1.0/business";
	private static String hcOrderURL = apiBaseURL + "HighCourt-Order/v1.0/order";
	private static String hcCaseNumberURL = apiBaseURL + "HighCourt-CaseNumber/v1.0/caseNumber";
	private static String hcFilingNumberURL = apiBaseURL + "HighCourt-FilingNumber/v1.0/filingNumber";
	private static String hcCurrentStatusURL = apiBaseURL + "HighCourt-CurrentStatus/v1.0/currentStatus";
	private static String hcPartyNameURL = apiBaseURL + "HighCourt-PartyName/v1.0/partyName";
	private static String hcAdvocateNameURL = apiBaseURL + "HighCourt-AdvocateName/v1.0/advocateName";
	private static String hcAdvocateBarRegistrationURL = apiBaseURL + "HighCourt-AdvocateBarRegistration/v1.0/advocateBarRegn";
	private static String hcActURL = apiBaseURL + "HighCourt-Act/v1.0/act";

	private static String causeListBenchURL = apiBaseURL + "HighCourt-CauselistBench/v1.0/causelistBench";
	private static String causeListURL = apiBaseURL + "HighCourt-Causelist/v1.0/causelist_details";
	private static String causeListShowURL = apiBaseURL + "HighCourt-ShowCauselist/v1.0/causelistdisplay";

	private static String caveateNameSearchURL = apiBaseURL + "HighCourt-CaveatName/v1.0/anywhere";
	private static String caveateHistoryURL = apiBaseURL + "HighCourt-CaveatHistory/v1.0/caveatDetails";

	private static String hcStatesMasterURL = apiBaseURL + "HighCourt-StateMaster/v1.0/state";
	private static String hcDistMasterURL = apiBaseURL + "HighCourt-DistrictMaster/v1.0/district";
	private static String hcBenchMasterURL = apiBaseURL + "HighCourt-BenchMaster/v1.0/bench";

	private static String hcActsMasterURL = apiBaseURL + "HighCourt-ActMaster/v1.0/act";
	private static String hcCaseTypesMasterURL = apiBaseURL + "HighCourt-CaseTypeMaster/v1.0/caseType";
	private static String hcCaseOrderPdfURL = apiBaseURL + "HighCourt-Order/v1.0/order";
	

	// DISTRICT COURT API URLS

	private static String dcCNRSearchURL = apiBaseURL + "CNR_Search/v1.0/cnrFullCaseDetails";
	private static String dcCaseBusinessURL = apiBaseURL + "CaseBusiness/v1.0/business";
	private static String dcOrderURL = apiBaseURL + "Order/v1.0/order";
	private static String dcCurrentStatusURL = apiBaseURL + "CurrentStatus/v1.0/currentStatus";
	private static String dcCaseNumberURL = apiBaseURL + "CaseNumber/v1.0/caseNumber";
	private static String dcFilingNumberURL = apiBaseURL + "FilingNumber/v1.0/filingNumber";
	private static String dcPartyNameURL = apiBaseURL + "PartyName/v1.0/partyName";
	private static String dcFirNumberURL = apiBaseURL + "firNumber/v1.0/firNumber";
	private static String dcAdvocateNameURL = apiBaseURL + "AdvocateName/v1.0/advocateName";
	private static String dcAdvocateBarRegURL = apiBaseURL + "AdvocateBarReg/v1.0/advocateBarRegn";
	private static String dcActURL = apiBaseURL + "Act/v1.0/act";

	private static String dcCauseListURL = apiBaseURL + "CourtCauselist/v1.0/court";
	private static String dcAdvocateCauseListURL = apiBaseURL + "AdvocateCauselist/v1.0/advocate";

	private static String dcCaveateNameSearchURL = apiBaseURL + "CaveatName/v1.0/anywhere";
	private static String dcCaveateNoSearchURL = apiBaseURL + "CaveatNumber/v1.0/caveatNumber";
	private static String dcCaveateHistoryURL = apiBaseURL + "CaveatDetails/v1.0/caveatDetails";

	private static String dcStatesMasterURL = apiBaseURL + "StateMaster/v1.0/state";
	private static String dcDistMasterURL = apiBaseURL + "DistrictMaster/v1.0/district";

	private static String dcCourtComplexURL = apiBaseURL + "CourtComplex/v1.0/courtComplex";
	
	private static String dcActsMasterURL = apiBaseURL + "ActMaster/v1.0/act";
	
	private static String dcCaseTypesMasterURL = apiBaseURL + "CaseType/v1.0/caseType";
	
	private static String dcPoliceStatonMasterURL = apiBaseURL + "PoliceStation/v1.0/policeStation";
	
	private static String dcCourtListMasterURL = apiBaseURL + "CourtList/v1.0/courtList";
	//private static String dcCaseOrderPdfURL = apiBaseURL + "HighCourt-Order/v1.0/order";
	
	public static void main(String[] args) {
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		try {
			String opVal = getSelectParam(1);
			
			System.out.println("opVal:"+opVal);
			inputStr = getInputStringValue(opVal);
			
			// 1. Encoding Request Token
			byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");
			
			targetURL = getTargetURL(opVal, requeststring, request_token);
			
			System.out.println("URL : "+targetURL);
			System.out.println("Input String : "+inputStr);
			
			
			authToken = EHighCourtAPI.getAuthToken();
			String resp="";
			if(opVal!=null && (opVal.equals("hcCurrentStatus") || opVal.equals("dcCurrentStatus"))) {
				resp = EHighCourtAPI.sendPostRequest(targetURL, authToken);
			}
			else if(opVal!=null && !opVal.equals("")){
				resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
			}
			if (resp != null && !resp.equals("")) {
				EHighCourtAPI.processResponseToFile(resp, opVal);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSelectParam(int opVal)
	{
		String outString="";
		
		switch(opVal) {
			// HIGH COURT INPUT DATA
			case 1 : outString="hcCNRSearch";break;
			case 2 : outString="hcShowBusiness";break;
			// case 3 : outString="hcOrder";break;
			case 4 : outString="hcCaseNumber";break;
			case 5 : outString="hcFilingNumber";break;
			case 6 : outString="hcCurrentStatus";break;
			case 7 : outString="hcPartyName";break;
			case 8 : outString="hcAdvocateName";break;
			// case 9 : outString="hcAdvocateBarRegistration";break;
			case 10 : outString="hcAct";break;
			case 11 : outString="causeListBench";break;
			case 12 : outString="causeList";break; 
			case 13 : outString="causeListShow";break;
			// case 14 : outString="caveateNameSearch";break;
			// case 15 : outString="caveateHistory";break;
			case 16 : outString="hcStatesMaster";break;
			case 17 : outString="hcDistMaster";break;
			case 18 : outString="hcBenchMaster";break;
			case 19 : outString="hcActsMaster";break;
			case 20 : outString="hcCaseTypesMaster";break;
		
			// DISTRICT COURT INPUT DATA
		
			case 21 : outString="dcCNRSearch";break;
			case 22 : outString="dcCaseBusiness";break;
			case 23 : outString="dcOrder";break;
			case 24 : outString="dcCurrentStatus";break;
			case 25 : outString="dcCaseNumber";break;
			case 26 : outString="dcFilingNumber";break;
			//case 27 : outString="dcPartyName";break;
			//case 28 : outString="dcFirNumber";break;
			case 29 : outString="dcAdvocateName";break;
			case 30 : outString="dcAdvocateBarReg";break;
			case 31 : outString="dcAct";break;
			case 32 : outString="dcCauseList";break;
			case 33 : outString="dcAdvocateCauseList";break;
			case 34 : outString="dcCaveateNameSearch";break;
			case 35 : outString="dcCaveateNoSearch";break;
			case 36 : outString="dcCaveateHistory";break;
			case 37 : outString="dcStatesMaster";break;
			case 38 : outString="dcDistMaster";break;
			case 39 : outString="dcCourtComplex";break;
			case 40 : outString="dcActsMaster";break;
			case 41 : outString="dcCaseTypesMaster";break;
			case 42 : outString="dcPoliceStatonMaster";break;
			case 43 : outString="dcCourtListMaster";break;
			
			case 44 : outString="hcCaseOrderPdf";break;
			case 45 : outString="dcCaseOrderPdf";break;
		}
		return outString;
	}
	
	public static String getInputStringValue(String opVal){
		String inputStr = "";
		switch(opVal) {
		
			// HIGH COURT INPUT DATA
		
			case "hcCNRSearch" : inputStr = "cino=APHC010271782014";break;//APHC010271782014
			case "hcShowBusiness" : inputStr = "cino=APHC010419392018|business_date=2018-09-07";break;
			
			// case "hcOrder" : inputStr = "cino=APHC010665392018|order_no=3|order_date=2021-09-28";break;
			
			case "hcCaseNumber" : inputStr = "est_code=APHC01|case_type=63|reg_no=19419|reg_year=2018";break;
			case "hcFilingNumber" : inputStr = "est_code=APHC01|case_type=63|fil_no=32044|fil_year=2018";break;
			case "hcCurrentStatus" : inputStr = "cino=APHC010665392018";break;
			case "hcPartyName" : inputStr = "est_code=APHC01|pend_disp=P|litigant_name=The State of Andhra Pradesh|reg_year=2018";break;
			case "hcAdvocateName" : inputStr = "est_code=APHC01|pend_disp=P|advocate_name=V KISHORE|reg_year=2018";break;
			// case "hcAdvocateBarRegistration" : inputStr = "est_code=APHC01|pend_disp=P|advocate_bar_regn_no=’value’|reg_year=2018";break;
			case "hcAct" : inputStr = "est_code=APHC01|pend_disp=D|national_act_code=20170430027001|reg_year=2017";break;
		
		
			case "causeListBench" : inputStr = "est_code=APHC01|causelist_date=2019-08-29";break;
			case "causeList" : inputStr = "est_code=APHC01|causelist_date=2019-08-29|bench_id=2570";break; // est_code=”value”|causelist_date=”value” |bench_id=”value”
			case "causeListShow" : inputStr = "est_code=APHC01|bench_id=2570|causelist_id=1001|causelist_date=2019-08-29";break;//(est_code =”value”| bench_id=”value”| causelist_id =”value”|causelist_date =”value”

			// case "caveateNameSearch" : inputStr = "est_code=APHC01|caveat_party_name=|search_type=";break;// est_code=’value’|caveat_party_name=’value’|search_type
			// case "caveateHistory" : inputStr = "est_code=APHC01|caveat_code=";break;// (est_code=’value’|caveat_code=’value’)
	
			case "hcStatesMaster" : inputStr = "";break;
			case "hcDistMaster" : inputStr = "state_code=28";break;
			case "hcBenchMaster" : inputStr = "state_code=28";break;
			
			case "hcActsMaster" : inputStr = "";break;
			case "hcCaseTypesMaster" : inputStr = "est_code=APHC01";break;
			
			// DISTRICT COURT INPUT DATA
			
			case "dcCNRSearch" : inputStr = "cino=APKR110002152018";break;
			case "dcCaseBusiness" : inputStr = "cino=APKR110002152018|business_date=2019-01-31";break;
			case "dcOrder" : inputStr = "cino=APKR110002152018|order_no=3|order_date=2021-09-28";break;
			case "dcCurrentStatus" : inputStr = "cino=APKR110002152018";break;
			case "dcCaseNumber" : inputStr = "est_code=APHC01|case_type=|reg_no=82|reg_year=2018";break;
			case "dcFilingNumber" : inputStr = "est_code=APHC01|case_type=|fil_no=1205|fil_year=2018";break;
			//case "dcPartyName" : inputStr = "est_code=APHC01|pend_disp=D|litigant_name=’value’|reg_year=2018";break;
			//case "dcFirNumber" : inputStr = "est_code=APHC01|police_station_code=’value’|fir_no=’value’|fir_year=’value’)";break;
			case "dcAdvocateName" : inputStr = "est_code=APHC01|pend_disp=P|advocate_name=S. Venkateswara Prasad|reg_year=2018";break;
			case "dcAdvocateBarReg" : inputStr = "est_code=APHC01|pend_disp=P|advocate_bar_regn_no=’value’|reg_year=2018";break;
			case "dcAct" : inputStr = "est_code=APHC01|pend_disp=P|national_act_code=’value’|reg_year=2018";break;
			
			case "dcCauseList" : inputStr = "est_code=”value”|court_no=”vaue”|causelist_date=”value”|ci_cri=”value";break;
			case "dcAdvocateCauseList" : inputStr = "est_code=’value’|advocate_bar_regn_no =’value’|causelist_date=’value’";break;
			
			case "dcCaveateNameSearch" : inputStr = "est_code=’value’|caveat_party_name=’value’|search_type";break;
			case "dcCaveateNoSearch" : inputStr = "est_code=’value’|caveat_no=’value’|caveat_year=’value’";break;
			case "dcCaveateHistory" : inputStr = "est_code=’value’|caveat_code=’value’";break;

			case "dcStatesMaster" : inputStr = "";break;
			case "dcDistMaster" : inputStr = "state_code=28";break;
			case "dcCourtComplex" : inputStr = "state_code=28|dist_code=";break;
			case "dcActsMaster" : inputStr = "";break;
			case "dcCaseTypesMaster" : inputStr = "est_code=APHC01";break;
			case "dcPoliceStatonMaster" : inputStr = "est_code=APHC01";break;
			case "dcCourtListMaster" : inputStr = "est_code=APHC01";break;


			
		}
		return inputStr;
	}
	
	
	public static String getTargetURL(String opVal, String requeststring, String request_token) {
		String targetURL = "";
		switch(opVal) {
		
			// HIGH COURT URLS
			case "hcCNRSearch" : targetURL = hcCNRSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcShowBusiness" : targetURL = hcShowBusinessURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcOrder" : targetURL = hcOrderURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcCaseNumber" : targetURL = hcCaseNumberURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcFilingNumber" : targetURL =  hcFilingNumberURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcCurrentStatus" : targetURL =  hcCurrentStatusURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcPartyName" : targetURL =  hcPartyNameURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcAdvocateName" : targetURL =  hcAdvocateNameURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcAdvocateBarRegistration" : targetURL =  hcAdvocateBarRegistrationURL+ "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcAct" : targetURL =  hcActURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "causeListBench" : targetURL = causeListBenchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "causeList" : targetURL = causeListURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "causeListShow" : targetURL = causeListShowURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "caveateNameSearch" : targetURL = caveateNameSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "caveateHistory" : targetURL = caveateHistoryURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcStatesMaster" : targetURL = hcStatesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcDistMaster" : targetURL = hcDistMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcBenchMaster" : targetURL = hcBenchMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcActsMaster" : targetURL = hcActsMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcCaseTypesMaster" : targetURL = hcCaseTypesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "hcCaseOrderPdf" : targetURL = hcCaseOrderPdfURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			// DISTRICT COURT URLS
			
			case "dcCNRSearch" : targetURL = dcCNRSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaseBusiness" : targetURL = dcCaseBusinessURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcOrder" : targetURL = dcOrderURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCurrentStatus" : targetURL = dcCurrentStatusURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaseNumber" : targetURL = dcCaseNumberURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcFilingNumber" : targetURL = dcFilingNumberURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcPartyName" : targetURL = dcPartyNameURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcFirNumber" : targetURL = dcFirNumberURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcAdvocateName" : targetURL = dcAdvocateNameURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcAdvocateBarReg" : targetURL = dcAdvocateBarRegURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcAct" : targetURL = dcActURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCauseList" : targetURL = dcCauseListURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcAdvocateCauseList" : targetURL = dcAdvocateCauseListURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaveateNameSearch" : targetURL = dcCaveateNameSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaveateNoSearch" : targetURL = dcCaveateNoSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaveateHistory" : targetURL = dcCaveateHistoryURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcStatesMaster" : targetURL = dcStatesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcDistMaster" : targetURL = dcDistMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCourtComplex" : targetURL = dcCourtComplexURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcActsMaster" : targetURL = dcActsMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCaseTypesMaster" : targetURL = dcCaseTypesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcPoliceStatonMaster" : targetURL = dcPoliceStatonMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case "dcCourtListMaster" : targetURL = dcCourtListMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			//case "dcCourtListMaster" : targetURL = dcCourtListMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;

		}
		return targetURL;
	}
	
	
	
	public static String getInputStringValue(int opVal){
		String inputStr = "";
		switch(opVal) {
			case 1 : inputStr = "est_code=APHC01|causelist_date=2019-08-29";break;
			case 2 : inputStr = "est_code=APHC01|causelist_date=2019-08-29|bench_id=2570";break; // est_code=”value”|causelist_date=”value” |bench_id=”value”
			case 3 : inputStr = "est_code=APHC01|bench_id=2570|causelist_id=1001|causelist_date=2019-08-29";break;//(est_code =”value”| bench_id=”value”| causelist_id =”value”|causelist_date =”value”
			
			case 4 : inputStr = "est_code=APHC01|caveat_party_name=|search_type=";break;// est_code=’value’|caveat_party_name=’value’|search_type
			case 5 : inputStr = "est_code=APHC01|caveat_code=";break;// (est_code=’value’|caveat_code=’value’)
	
			case 6 : inputStr = "";break;
			case 7 : inputStr = "state_code=28";break;
			case 8 : inputStr = "state_code=28";break;
			
			case 9 : inputStr = "";break;
			case 10 : inputStr = "est_code=APHC01";break; 
		}
		return inputStr;
	}
	
	
	public static String getTargetURL(int opVal, String requeststring, String request_token) {
		String targetURL = "";
		switch(opVal) {
			case 1 : targetURL = causeListBenchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 2 : targetURL = causeListURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 3 : targetURL = causeListShowURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			
			case 4 : targetURL = caveateNameSearchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 5 : targetURL = caveateHistoryURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			
			case 6 : targetURL = hcStatesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 7 : targetURL = hcDistMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 8 : targetURL = hcBenchMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			
			case 9 : targetURL = hcActsMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			case 10 : targetURL = hcCaseTypesMasterURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;break;
			
		}
		return targetURL;
	}
	
	
	
}