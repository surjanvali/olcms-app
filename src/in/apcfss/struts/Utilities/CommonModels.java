package in.apcfss.struts.Utilities;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;

//import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.struts.upload.FormFile;




public class CommonModels {
	
	public static String SUCCESS = "Data saved Successfully.";
	public static String ERROR = "Error while saving Data.";

	
	
 	
 	
 	
 	
	public static String saveDocument(FormFile form_file,
			String upload_to_path, String uniq_id, String attachemnt_name,
			String file_path) throws Exception {
		String uploaded_path = null;

		if (form_file != null && form_file.getFileSize() > 0) {
			
			// FormFile form_file = (FormFile) file_to_upload;
			String type = form_file.getContentType();
			String filename = form_file.getFileName();
			String extension = null;
			if (filename != null && !filename.equals("")) {
				extension = filename.substring(filename.lastIndexOf(".") + 1,
						filename.length());
			}
			// **** Uploading

			File upload_folder = new File(file_path + upload_to_path);
			if (!upload_folder.exists()) {
				upload_folder.mkdirs();
			}

			String file_name = null;

			if (upload_folder.exists()) {
				file_name = uniq_id + "_" + attachemnt_name + "." + extension;
				File file = new File(file_path + upload_to_path, file_name);
				if (!file.exists()) {
					FileOutputStream fcf = new FileOutputStream(file);
					fcf.write(form_file.getFileData());
					fcf.flush();
					fcf.close();
				}
			}
			uploaded_path = upload_to_path + file_name;

			//System.out.println(" uploaded_path >> " + uploaded_path);
		}
		return uploaded_path;
	}
	
	public static boolean checkFileLimitations(FormFile formFile)
	{
		boolean flag = false;
		String fileName = formFile.getFileName();
		fileName = fileName.replaceAll(" ", "_");
		int index = fileName.lastIndexOf(".");
		
		
 		String fileExtension = fileName.substring(index);
		
		 if(fileExtension.equals(".pdf") || fileExtension.equals(".PDF") || fileExtension.equals(".jpg") || fileExtension.equals(".jpeg") || fileExtension.equals(".JPG") || fileExtension.equals(".JPEG") )
         {
			 //|| fileExtension.equals(".png") || fileExtension.equals(".gif") || fileExtension.equals(".bmp") || || fileExtension.equals(".PNG") || fileExtension.equals(".GIF") || fileExtension.equals(".BMP")
			 flag = true;
         }
		 else
		 {
			return flag;
		 }
		 
		// 2MB - 2097152  bytes
		// 1MB - 1048576 bytes
		// 512KB - 524288 bytes
		// 256KB - 262144 bytes
		// 128KB - 131072 bytes
		 
		 if(formFile.getFileSize()<=1048576)
         {
			 flag = true;
         }else
		 {
			flag = false;
		 }
		
		return flag;
	}
	
	public static boolean checkPhotoFileLimitations(FormFile formFile)
	{
		boolean flag = false;
		String fileName = formFile.getFileName();
		fileName = fileName.replaceAll(" ", "_");
		int index = fileName.lastIndexOf(".");
		
		
 		String fileExtension = fileName.substring(index);
		
		 if(fileExtension.equals(".jpg") || fileExtension.equals(".jpeg"))
         {
			 flag = true;
         }
		 else
		 {
			return flag;
		 }
		 
		// 2MB - 2097152  bytes
		// 1MB - 1048576 bytes
		// 512KB - 524288 bytes
		// 256KB - 262144 bytes
		// 128KB - 131072 bytes
		 
		 if(formFile.getFileSize()<=262144)
         {
			 flag = true;
         }else
		 {
			flag = false;
		 }
		
		return flag;
	}
	
	public static String randomTransactionNo(){
		String randomNo=null;
		
		Calendar calendar = Calendar.getInstance();
		//System.out.println("Calender - Time in milliseconds : " + calendar.getTimeInMillis());
		randomNo = calendar.getTimeInMillis()+"";
		
		return "APCOS"+randomNo;
	}

	public static String checkStringObject(Object objVal){
		
		return objVal!=null ? objVal.toString().trim() : "";
	}
	
	public static String checkStringZeroObject(Object objVal){
		
		return !checkStringObject(objVal).equals("") ? objVal.toString().trim() : "0";
	}
	
	
	public static int checkIntObject(Object objVal){
		
		return objVal!=null && !(objVal.toString().trim()).equals("") ? Integer.parseInt(objVal.toString().trim()) : 0;
	}
	public static Double checkDoubleObject(Object objVal){
		
		return objVal!=null && !(objVal.toString().trim()).equals("") ? Double.parseDouble(objVal.toString().trim()) : 0.0;
	}
	
	public static String[] checkStringArrObject(Object objVal){
		
		return objVal!=null ? (String[])objVal : null;
	}

	public static boolean checkExcelFileLimitations(FormFile formFile) {
		boolean flag = false;
		String fileName = formFile.getFileName();
		fileName = fileName.replaceAll(" ", "_");
		int index = fileName.lastIndexOf(".");

		String fileExtension = fileName.substring(index);

		if (fileExtension.equals(".xls") || fileExtension.equals(".XLS")) {
			flag = true;
		} else {
			return flag;
		}
		// 5MB - 5242880 bytes
		// 2MB - 2097152 bytes
		// 1MB - 1048576 bytes
		// 512KB - 524288 bytes
		// 256KB - 262144 bytes
		// 128KB - 131072 bytes

		if (formFile.getFileSize() <= 5242880) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}
	
	/*public static void sendMailWithLoginCredentials(String candidateId,Connection con){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");
        CommonModels cm = new CommonModels();
        
        Session session = Session.getDefaultInstance(props, new Authenticator() {
		  
                protected PasswordAuthentication getPasswordAuthentication() {    
                return new PasswordAuthentication(ApplicationVariables.noreplyEmailSenderId,ApplicationVariables.noreplyEmailSenderPassword);  
                }    
               });
        try{
        	String sql = "select login_id,password_text,email_id from obm_candidate_login_mst a inner join obm_sponsorlist_details b using (candidate_id) where candidate_id='"+candidateId+"' and is_delete='false'";
			String login_id = null, password = null;
			List userData = DatabasePlugin.executeQuery(con, sql);
			if(userData != null){
				Map innerData = (Map)userData.get(0);
				login_id = cm.checkStringObject(innerData.get("login_id"));
				password = cm.checkStringObject(innerData.get("password_text"));
			
            MimeMessage message = new MimeMessage(session);    
            message.setFrom(new InternetAddress(ApplicationVariables.noreplyEmailSenderId));
            if(login_id != null && password != null){
            	 message.addRecipient(Message.RecipientType.TO,new InternetAddress(cm.checkStringObject(innerData.get("email_id")))); 
                 message.setSubject("CFMS OnBoard Module User Login Credentials");    
                 message.setText("This mail is inform the user that he / she should login into cfms onboarding site and fill the required data.Your userID and Password required to login are mentioned below \n Login ID : " + login_id + " \n Password : "+password+"");
                 Transport.send(message);
                 System.out.println("------------------- Mail Had Sent Successfully ----------------------");
            	}
			}
        }catch (Exception e) {
        	e.printStackTrace();
		}
	}*/
	
	
	/*public static boolean sendMailWithAttachment(String candidateId, Connection con){
		String sql = null;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");
        CommonModels cm = new CommonModels();
        
        Session session = Session.getDefaultInstance(props, new Authenticator() {    
                protected PasswordAuthentication getPasswordAuthentication() {    
                	return new PasswordAuthentication(ApplicationVariables.noreplyEmailSenderId,ApplicationVariables.noreplyEmailSenderPassword);  
                }    
               });
        try{
        	String filename = null;
        	sql = "select login_id,password_text,email_id,call_letter_path from obm_candidate_login_mst a inner join obm_sponsorlist_details b using (candidate_id) where candidate_id='"+candidateId+"' and is_delete='false'";
        	List userData = DatabasePlugin.executeQuery(con, sql);
			if(userData != null){
				Map innerData = (Map)userData.get(0);
				//login_id = cm.checkStringObject(innerData.get("login_id"));
				//password = cm.checkStringObject(innerData.get("password_text"));
				
				filename = cm.checkStringObject(innerData.get("call_letter_path")); 
					
	            MimeMessage message = new MimeMessage(session); 
	            
	            message.setFrom(new InternetAddress(ApplicationVariables.noreplyEmailSenderId));
	            if(filename != null && !filename.equals("")){
	            	 message.addRecipient(Message.RecipientType.TO,new InternetAddress(cm.checkStringObject(innerData.get("email_id"))));   // login_id
	                 BodyPart messageBodyPart1 = new MimeBodyPart();
	                 messageBodyPart1.setText("Call Letter");
	                 File file = new File("NewFirstPdf.pdf");
	       	      	 String path = file.getAbsolutePath();
	       	      	 System.out.println("file path ------------------------ " + path);
	                 MimeBodyPart messageBodyPart2 = new MimeBodyPart();
	                 DataSource source = new FileDataSource(filename);
	                 messageBodyPart2.setDataHandler(new DataHandler(source));  
	                 messageBodyPart2.setFileName(filename);
	                
	                 Multipart multipart = new MimeMultipart();  
	                 multipart.addBodyPart(messageBodyPart1);  
	                 multipart.addBodyPart(messageBodyPart2); 
	                 
	                 message.setContent(multipart );
	                 Transport.send(message);
	                 Transport.send(message);
	                 System.out.println("------------------- Mail Had Sent Successfully ----------------------");
	            }
           }
        }catch (Exception e) {
        	e.printStackTrace();
		}
		return true;
	}*/
	
	
	/*public static boolean sendSimpleMailWithMessage(String recepEmail,String recepMsg){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");
        CommonModels cm = new CommonModels();
        
        Session session = Session.getDefaultInstance(props, new Authenticator() {    
                protected PasswordAuthentication getPasswordAuthentication() {    
                return new PasswordAuthentication(ApplicationVariables.noreplyEmailSenderId,ApplicationVariables.noreplyEmailSenderPassword);  
                }    
               });
        try{
        	MimeMessage message = new MimeMessage(session);    
            message.setFrom(new InternetAddress(ApplicationVariables.noreplyEmailSenderId));
            	 message.addRecipient(Message.RecipientType.TO,new InternetAddress(cm.checkStringObject(recepEmail))); 
                 message.setSubject("Sate Audit Dept.");    
                 message.setText(cm.checkStringObject(recepMsg));
                 Transport.send(message);
                 System.out.println("------------------- Mail Sent Successfully ----------------------");
        }catch (Exception e) {
        	e.printStackTrace();
        	return false;
		}
        return true;
	}*/
	
	
	public static void main(String[] args) {
		//sendMailWithLoginCredentials("130998");
		//sendMailWithAttachment("130998");
		//sendSimpleMailWithMessage("surjanvali@gmail.com", "TEST MAIL");
	}
	
	public static String update_statements(String table_name,String candidate_id){
		String sql = "update "+table_name+ " set confirmed='Y' where candidate_id='"+candidate_id+"'"; 
		return sql;
	}
	public static int del_table_data(String sql,Connection con){
		PreparedStatement pstmt = null;
		int j = 0;
		try{
			pstmt = con.prepareStatement(sql);
			j = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return j;
	}
	
	public static boolean NullValidation(String str) {
		boolean flag = true; 

		if(str == null || str.equals("") || str.trim() == null || str.trim().equals("") || str.trim().equals("null"))
			flag = false;

		return flag;
	}

	public static boolean NonZeroValidation(String str) {
		boolean flag = true; 

		if(str == null || str.equals("") || str.trim() == null || str.trim().equals("")  || str.trim().equals("null")|| str.trim().equals("0"))
			flag = false;

		return flag;
	}
	
	
	
	
	/*public static boolean sendEmail2User(String receipient, String msgContent, Connection con) throws SQLException{
		
		return sendSimpleMailWithMessage(receipient, msgContent);
		
	}*/

	public static String arrayToString(String[] string_array){
		String data = "";
		if(string_array != null && string_array.length >= 1){
			data = string_array[0];
			if(string_array.length > 1){
				for(int i = 1;i < string_array.length;i++){
					data +=  "," + string_array[i]; 
				}
			}
		}
		
		return data;
	}
	
	public void sendMail(String mess,String email){
		
		 // Recipient's email ID needs to be mentioned.
		Properties props = new Properties();    
     props.put("mail.smtp.host", "smtp.gmail.com");    
     props.put("mail.smtp.socketFactory.port", "465");    
     props.put("mail.smtp.socketFactory.class",    
               "javax.net.ssl.SSLSocketFactory");    
     props.put("mail.smtp.auth", "true");    
     props.put("mail.smtp.port", "465");    
     //get Session   
     Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {    
	         protected PasswordAuthentication getPasswordAuthentication() {    
				return new PasswordAuthentication("arepallig@gmail.com","9849180842");  
	         }    
     });    
     //compose message    
     try {
	       	 final String user = email;
	         MimeMessage message = new MimeMessage(session);    
	         message.setFrom(new InternetAddress(user));
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(user));    
	         message.setSubject("Your Indent Placed in APCOS Portal is rejected.");    
	         
	         message.setText(mess);
	         message.setContent(mess, "text/html");
	         
	         Transport.send(message);
	         
	       
	         System.out.println("Mail Sent Successfully.");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
}