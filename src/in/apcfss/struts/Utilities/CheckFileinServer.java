package in.apcfss.struts.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;

public class CheckFileinServer {
	public static void main(String[] args) throws SQLException {
		final String dbUrl = "jdbc:postgresql://172.16.98.2:9432/apolcms", dbUserName = "apolcms",
				dbPassword = "apolcms";
		// final String dbUrl = "jdbc:postgresql://10.96.54.54:6432/apolcms", dbUserName
		// = "apolcms", dbPassword = "@p0l(m$";
		String sql = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int c = 0;File myFile = null;
		String  fileURL = "", fileName = "";
		try {
			fileURL = "https://apolcms.ap.gov.in/uploads/scandocs/AGC011720220512104557955/AGC011720220512104557955.pdf";
			System.out.println("fileURL:"+fileURL);
			//URI u = new URI(fileURL);
			//myFile = new File((u.toURL()).getFile());
			
			URL url = new URL(fileURL);
			myFile = new File(url.getFile());
			
			// System.out.println("myFile:"+myFile.getAbsolutePath());
			
			if (myFile != null && myFile.exists()) {
				// file exist
				System.out.println("file exist");
			} else {
				// file dosenst exist
				System.out.println("file dosenst exist");
			}
			/*
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

			sql = "";

			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while (rs.next()) {

				// check file exist in server / not
				// https://apolcms.ap.gov.in/uploads/scandocs/AGC011720220512104557955/AGC011720220512104557955.pdf
				// fileURL = "https://apolcms.ap.gov.in/uploads/scandocs/" + fileName + "/" + fileName + ".pdf";
				fileURL = "https://apolcms.ap.gov.in/uploads/scandocs/AGC011720220512104557955/AGC011720220512104557955.pdf";
				myFile = new File(fileURL);

				if (myFile != null && myFile.exists()) {
					// file exist
					System.out.println("file exist");
				} else {
					// file dosenst exist
					System.out.println("file dosenst exist");
				}
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}
	}
	
	private static void fetchIndexSafely(String url) throws FileAlreadyExistsException {
        File f = new File(url);
        if (f.exists()) {
            throw new FileAlreadyExistsException(f.getAbsolutePath());
        } else {
            try {
                URL u = new URL(url);
                FileUtils.copyURLToFile(u, f);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Matcher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Matcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	
}