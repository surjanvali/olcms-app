package in.apcfss.struts.eCourt.apis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthTokenGen {
	
	
	public static void main(String[] args) {
		String authToken="";
		try {
			authToken = getAuthToken();
		} catch (JSONException e) {
			e.printStackTrace();
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
