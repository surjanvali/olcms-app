package in.apcfss.struts.Utilities;

import com.itextpdf.text.Document;

public class ConvertPDFtoXLS {
	public static void main(String[] args) {
		Document document = new Document("Test.pdf");
		// Instantiate ExcelSave Option object
		ExcelSaveOptions excelsave = new ExcelSaveOptions();
		// Save the output to XLS format
		document.save("ConvertedFile.xls", excelsave);
	}
}
