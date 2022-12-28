package in.apcfss.struts.eCourt.apis;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OlcmsDistCron {

	public static void main(String[] args) throws Exception {
		try {
			
			//String url="http://localhost:8301/apolcmsapi/accept211220221802data0218245586data";
	        String url="https://apolcms.ap.gov.in/apolcmsapi/accept211220221802data0218245586data";
			sendGetRequest(url);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String sendGetRequest(String requestUrl) throws Exception {
		// Sending get request
		System.out.println("sendGetRequest requestUrl:"+requestUrl);
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestMethod("GET");

		System.out.println(conn.getResponseCode() + "-" + conn.getResponseMessage());

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output="";
		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();
		// printing result from response
		// System.out.println("Response:-" + response.toString());
		if(conn.getResponseCode()==200 && (response==null || response.toString().equals(""))) {
			System.out.println("Record Not Found.");
		}
		return response.toString();
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

	public static String sendPostRequest(String requestUrl, String authToken) throws Exception {
		// Sending get request
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + authToken);
		// conn.setRequestProperty("Authorization", "Basic b3F0dV9MaHMxSktDcHhHOGhhWTM3R1VyUHU0YTpYQUJlVkNoNjZEdHNJakMxN0IzOXVyelEwWG9h");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestMethod("POST");

		System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output="";
		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();
		// printing result from response
		// System.out.println("Response:-" + response.toString());
		if(conn.getResponseCode()==200 && (response==null || response.toString().equals(""))) {
			System.out.println("Record Not Found.");
		}
		return response.toString();
	}
}