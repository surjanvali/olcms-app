package in.apcfss.struts.eCourt.apis;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class CURL {
	
	
	public static void main(String[] args) {
		// String command = "curl -k -g \"grant_type=password&username=GOVT-AP@ecourts.gov.in&password=Apcfss@123\" -H \"Authorization: Basic b3F0dV9MaHMxSktDcHhHOGhhWTM3R1VyUHU0YTpYQUJlVkNoNjZEdHNJakMxN0IzOXVyelEwWG9h\" https://egw.bharatapi.gov.in/token";
		
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		try
		{
           String[] stringPost = {"curl", "-X", "POST", "https://egw.bharatapi.gov.in/token",
					            "-H", "Authorization: Basic b3F0dV9MaHMxSktDcHhHOGhhWTM3R1VyUHU0YTpYQUJlVkNoNjZEdHNJakMxN0IzOXVyelEwWG9h",
					            "-g", "grant_type=password&username=GOVT-AP@ecourts.gov.in&password=Apcfss@123"};

	        ProcessBuilder ps = new ProcessBuilder(stringPost);
	        //ps.redirectErrorStream(true);
	        Process pr = ps.start();
	        pr.waitFor();

	        BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

	        String line2 = "";
	        while ((line2 = reader2.readLine()) != null) {
	            output.append(line2 + "");
	        }

	        System.out.println("output====" + output + "===");

	        String sbToString = output.toString();

	        JSONObject jObj = new JSONObject(sbToString);

	        System.out.println(jObj.toString());

	        p1 = Runtime.getRuntime().exec("pwd");
	        p1.waitFor();
	        BufferedReader reader1a = new BufferedReader(new InputStreamReader(p1.getInputStream()));

	        String line1a = "";
	        while ((line1a = reader1a.readLine()) != null) {
	            output.append(line1a + "");
	            System.out.println("output====" + output + "===");
	        }
	    }catch(
		Exception e)
		{
	        System.out.println("===============ERROR===============" + e.getMessage() + "");
	    }
		///
	}
}