package in.apcfss.struts.eCourt.apis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

import org.json.JSONObject;

import plugins.DatabasePlugin;

public class ImportEcourtPDFs {
	static final String apolcmsDataBase = "jdbc:postgresql://172.16.98.2:9432/apolcms";
	static final String apolcmsUserName = "apolcms";
	static final String apolcmsPassword = "apolcms";
	
	static final String apolcmsOrdersTable = "ecourts_case_interimorder";
	// static final String apolcmsOrdersTable = "ecourts_case_finalorder";
	static final String orderFileName = apolcmsOrdersTable.substring(apolcmsOrdersTable.lastIndexOf("_")+1);

	public static void main(String[] args) {
		
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";int i=0;
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql="";
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);
			
			String opVal = ECourtAPIs.getSelectParam(44);
			
			System.out.println("opVal:"+opVal);
			
			sql = "select cino,order_no,to_char(order_date,'yyyy-mm-dd') as order_date, cino||'-"+orderFileName+"-'||order_no as filename from "+apolcmsOrdersTable+" where order_document_path is null ";//limit 50 where cino='APHC010294162021'
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {

				inputStr = "cino="+rs.getString("cino").trim()+"|order_no="+rs.getString("order_no").trim()+"|order_date="+rs.getString("order_date").trim();
				System.out.println("INPUT-STR:"+inputStr);
				
				// 1. Encoding Request Token
				byte[] hmacSha256 = HASHHMACJava.calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
				request_token = String.format("%032x", new BigInteger(1, hmacSha256));
				// 2. Encoding Request String
				requeststring = URLEncoder.encode(ECourtsCryptoHelper.encrypt(inputStr.getBytes()), "UTF-8");
				
				targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);
				
				System.out.println("URL : "+targetURL);
				System.out.println((++i)+":Input String : "+inputStr);
				
				authToken = EHighCourtAPI.getAuthToken();
				String resp="";
				if(opVal!=null && (opVal.equals("hcCurrentStatus") || opVal.equals("dcCurrentStatus"))) {
					resp = EHighCourtAPI.sendPostRequest(targetURL, authToken);
				}
				else if(opVal!=null && !opVal.equals("")){
					resp = EHighCourtAPI.sendGetRequest(targetURL, authToken);
				}
				if (resp != null && !resp.equals("")) {
					processPDForderResponse(resp, rs.getString("filename").trim(), con, rs.getString("cino").trim());
				}
			}
			System.out.println("FINAL END:"+i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void processPDForderResponse(String resp, String fileName, Connection con, String cino)  throws Exception{

	    String response_str = "";String response_token = "";String version = "";String decryptedRespStr = "";String sql = "";
	    resp = resp.trim();
	    // System.out.println("processPDForderResponse RESP:"+resp);
	    if ((resp != null) && (!resp.equals("")) && (!resp.contains("INVALID_TOKEN")) && (!resp.contains("RECORD_NOT_FOUND")))
		{
			JSONObject jObj = new JSONObject(resp);
			response_str = jObj.getString("response_str").toString();
			// System.out.println("response_str::"+response_str);
			if ((response_str != null) && (!response_str.equals(""))) {
		        decryptedRespStr = ECourtsCryptoHelper.decrypt(response_str.getBytes());
		    }
			
			/*
				FileWriter file = new FileWriter("D:\\HighCourtsOrders\\" + fileName + ".txt");
				file.write("\n");
				file.write(decryptedRespStr.replace("\"", ""));
				file.flush();
				file.close();
				System.out.println("TEXT File Saved");
			*/
			
			File pdfFile = new File("E:\\HighCourtsCaseOrders\\" + fileName + ".pdf");
			FileOutputStream fos = new FileOutputStream(pdfFile);
			byte[] decoder = Base64.getDecoder().decode(decryptedRespStr.replace("\"", "").replace("\\", ""));
			fos.write(decoder);
			System.out.println("PDF File Saved");
			// sql="update ecourts_case_interimorder set order_document_path='HighCourtsCaseOrders/"+fileName+".pdf' where cino||'-interimorder-'||order_no='"+fileName+"'";
			sql="update "+apolcmsOrdersTable+" set order_document_path='HighCourtsCaseOrders/"+fileName+".pdf' where cino||'-"+orderFileName+"-'||order_no='"+fileName+"'";
			DatabasePlugin.executeUpdate(sql, con);
			
		}
	    else
		{
			// sql = "update ecourts_cinos set ecourts_response='" + resp + "' where cino='" + cino + "'";
			 sql = "update "+apolcmsOrdersTable+" set order_document_path='" + resp + "' where cino||'-"+orderFileName+"-'||order_no='"+fileName+"'";
			 DatabasePlugin.executeUpdate(sql, con);

			System.out.println("Invalid/Empty Response");
		}
	}
}