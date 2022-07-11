package in.apcfss.struts.eCourt.actions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
 
public class CompressFileGzip {
 
    public static void main(String[] args) throws FileNotFoundException {
 
        //String source_filepath = "C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\New folder\\dept_pending_prayer.txt";
        String gzip_filepath = "C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\New folder\\dept_pending_prayer.gzip";
        //String decopressed_filepath = "C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\New folder\\dept_pending_prayer2.txt";
 
       /// CompressFileGzip gZipFile = new CompressFileGzip();
        //gZipFile.gzipFile(source_filepath, gzip_filepath);
        //gZipFile.unGunzipFile(gzip_filepath, decopressed_filepath);
        
       
        try {
        	 FileInputStream fileIn = new FileInputStream(gzip_filepath);
			GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
			byte[] buffer = new byte[1024];
			gZIPInputStream.read(buffer);
			
			//FileReader readfile = new FileReader(file);
			//BufferedReader readbuffer = gZIPInputStream.read(buffer);
			
			//resp = gZIPInputStream.r
			// System.out.println("resp:"+resp);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
 
    public void gzipFile(String source_filepath, String destinaton_zip_filepath) {
 
        byte[] buffer = new byte[1024];
 
        try {
 
            FileOutputStream fileOutputStream = new FileOutputStream(
                    destinaton_zip_filepath);
 
            GZIPOutputStream gzipOuputStream = new GZIPOutputStream(
                    fileOutputStream);
 
            FileInputStream fileInput = new FileInputStream(source_filepath);
 
            int bytes_read;
 
            while ((bytes_read = fileInput.read(buffer)) > 0) {
                gzipOuputStream.write(buffer, 0, bytes_read);
            }
 
            fileInput.close();
 
            gzipOuputStream.finish();
            gzipOuputStream.close();
 
            System.out.println("The file was compressed successfully!");
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    public void unGunzipFile(String compressedFile, String decompressedFile) {
 
        byte[] buffer = new byte[1024];
 
        try {
 
            FileInputStream fileIn = new FileInputStream(compressedFile);
 
            GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
 
            FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
 
            int bytes_read;
 
            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
 
                fileOutputStream.write(buffer, 0, bytes_read);
            }
 
            gZIPInputStream.close();
            fileOutputStream.close();
 
            System.out.println("The file was decompressed successfully!");
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    
    
    public static void readAndImportZippedFile() {
    	
    }
}