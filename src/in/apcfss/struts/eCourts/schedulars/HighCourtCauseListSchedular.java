package in.apcfss.struts.eCourts.schedulars;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

public class HighCourtCauseListSchedular {
	
	static final String apolcmsDataBase = "jdbc:postgresql://10.96.54.54:6432/apolcms";
	static final String apolcmsUserName = "apolcms";
	static final String apolcmsPassword = "@p0l(m$";
	
	public static void main(String[] args) {

		
		String request_token = "", requeststring = "";
		String inputStr = "", targetURL = "";
		String authToken = "";
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String sql = "";
		int totalCount = 0, successCount = 0, failCount = 0;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(apolcmsDataBase, apolcmsUserName, apolcmsPassword);
			String opVal = "causeListBench";
			
			 String estCode="APHC01", causelistDate="2022-04-25";//2022-04-06
			
			inputStr = "est_code="+estCode+"|causelist_date="+causelistDate;//ECourtAPIs.getInputStringValue(opVal);
			// 1. Encoding Request Token
			byte[] hmacSha256 = calcHmacSha256("15081947".getBytes("UTF-8"), inputStr.getBytes("UTF-8"));
			request_token = String.format("%032x", new BigInteger(1, hmacSha256));
			// 2. Encoding Request String
			requeststring = URLEncoder.encode(encrypt(inputStr.getBytes()), "UTF-8");

			//targetURL = ECourtAPIs.getTargetURL(opVal, requeststring, request_token);
			targetURL = causeListBenchURL + "?dept_id="+deptId+"&request_str=" + requeststring + "&request_token=" + request_token + "&version=" + version;

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
					processApiResponse(resp, estCode, causelistDate, con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("CAUSE LIST BENCH END");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	
	}
	
	
	static public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
		byte[] hmacSha256 = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
			mac.init(secretKeySpec);
			hmacSha256 = mac.doFinal(message);
		} catch (Exception e) {
			throw new RuntimeException("Failed to calculate hmac-sha256", e);
		}
		return hmacSha256;
	}
	
	
	
	public static final String initializationVector="abcdef987654";
	public static final String authenticationKey = "PxaCV2s2kzKI";
	
	public static String encrypt(byte[] payload)
	{
		byte[] iv = initializationVector.getBytes(StandardCharsets.UTF_8); // change with your IV
		byte[] key = authenticationKey.getBytes(StandardCharsets.UTF_8);// change with your Key
		byte[] ivBytes = new byte[16];
		byte[] keyBytes = new byte[16];
		System.arraycopy(iv, 0, ivBytes, 0, iv.length);
		System.arraycopy(key, 0, keyBytes, 0, key.length);
		try {
	        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes,"AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
	        byte[] encrypted = cipher.doFinal(payload);
	        //return Base64.encodeBase64String(encrypted);
	        
	        return Base64.getEncoder().encodeToString(encrypted);
	        
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        throw new RuntimeException(ex);
	    }
	}
	
	public static String decrypt(byte[] cipherText)
	{
		byte[] iv =initializationVector.getBytes(StandardCharsets.UTF_8);// change with your IV
		byte[] key = authenticationKey.getBytes(StandardCharsets.UTF_8);// change with your Key
	  byte[] ivBytes = new byte[16];
		byte[] keyBytes = new byte[16];
		System.arraycopy(iv, 0, ivBytes, 0, iv.length);
		System.arraycopy(key, 0, keyBytes, 0, key.length);
		try {
	        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes,"AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
	   
			  byte[] encrypted =
			  cipher.doFinal(Base64.getDecoder().decode(cipherText)); return new
			  String(encrypted,StandardCharsets.UTF_8);
			 
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        throw new RuntimeException(ex);
	    }
	}

	public static String getAuthToken() throws JSONException {
		System.out.println("in getAuthToken");
		String authToken = "";
		
		String urlToken = "https://egw.bharatapi.gov.in/token?grant_type=password&username=GOVT-AP@ecourts.gov.in&password=Apcfss@123&scope=";
		System.out.println("Token URL:" + urlToken);
		String respAuth = sendPostRequest(urlToken);
		System.out.println("" + respAuth);
		if (respAuth != null && !respAuth.equals("")) {

			JSONObject jobj1 = new JSONObject(respAuth);
			authToken = jobj1.has("access_token") ? jobj1.get("access_token").toString() : "";
			System.out.println("authToken:" + authToken);
		}
		
		return authToken;
	}
	
	public static String sendPostRequest(String requestUrl) {
		StringBuffer jsonString = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", "Basic b3F0dV9MaHMxSktDcHhHOGhhWTM3R1VyUHU0YTpYQUJlVkNoNjZEdHNJakMxN0IzOXVyelEwWG9h");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return jsonString.toString();
	}
	
}
