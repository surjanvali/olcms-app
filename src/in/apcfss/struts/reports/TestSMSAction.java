package in.apcfss.struts.reports;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import in.apcfss.struts.commons.SendSMSAction;

public class TestSMSAction {
	public static void main(String[] args) throws Exception {
		
		
		String mobileNo="9618048663";
		String emailId="surjanvali@apcfss.in";
		String smsText="Your User Id is "+emailId+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
		String templateId="1007784197678878760";
		
		System.out.println(mobileNo+""+smsText+""+templateId);
		if(mobileNo!=null && !mobileNo.equals("")) {
			// mobileNo = "9618048663";
			//SendSMSAction.sendSMS(mobileNo, smsText, templateId, null);
		}
		System.out.println("END");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		System.out.println("--"+sdf.format(new Date()));
		
	}
}
