package in.apcfss.struts.Utilities;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class ReadYourOwnPDFFile {
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader("E:\\HighCourtsCauseList\\APHC012022-02-1031631001.pdf");
			int n = reader.getNumberOfPages();
			// we retrieve the size of the first page
			Rectangle psize = reader.getPageSize(1);

			System.out.println(reader.getPdfVersion());
			System.out.println(reader.getFileLength());
			System.out.println(psize.getHeight());
			System.out.println(psize.getWidth());
			System.out.println("No. of Pages:"+n);
			
			for(int i =0;i < n; i++) {
				
				System.out.println();
				
			}
			
		} catch (Exception de) {
			de.printStackTrace();
		}
	}
}
