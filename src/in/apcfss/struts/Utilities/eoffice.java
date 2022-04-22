package in.apcfss.struts.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class eoffice{
	
	static {
	    // System.setProperty("javax.net.debug", "all");
	    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}
	
	public static void main(String[] args) throws Exception{

		//String payload= generatePayloadString(AES.encrypt("934019928590", "GWV@2020")); // aadhar number is sent in encrypted format, & GWV@2020 is the secret key for encrytion
		//String payload= generatePayloadString("934019928590"); // aadhar number is sent in encrypted format, & GWV@2020 is the secret key for encrytion
		String requestUrl="https://demo.eoffice.ap.gov.in/TTReports/Krishna.php";
		//String requestUrl="http://172.16.150.52/cfmsho/services/fisheries";
		//String resp=sendPostRequest(requestUrl, payload);
		String resp=sendPostRequest(requestUrl);
			System.out.println("response="+resp);
		}

		//public static String sendPostRequest(String requestUrl, String payload) {
		public static String sendPostRequest(String requestUrl) {	
		StringBuffer jsonString = new StringBuffer();
		try {
		URL url = new URL(requestUrl);
		// HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		//String userpass = "apcfms:@pcfm$";
		
		//String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		//connection.setRequestProperty ("Authorization", basicAuth);

		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		//OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		//writer.write(payload);
		//writer.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String line;
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


		public static String generatePayloadString(String deptCode) throws Exception{

			String myString = null;
			try {
			myString = "{\"BENID\": \""+deptCode+"\"}";
			deptCode="934019928590";
			myString = "{\"BENID\": \""+deptCode+"\"}";
			
			
	
			} catch (Exception e) {
			e.printStackTrace();
			}
			System.out.println("myString:" + myString);
	
			return myString;
		}

		public static String checkString(Object myVal){

			return (myVal !=null && !myVal.equals("")) ? myVal.toString() : "";

		}	
}