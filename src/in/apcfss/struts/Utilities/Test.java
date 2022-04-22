package in.apcfss.struts.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Surjan Vali\\Desktop\\nic-emp-data\\Guntur.txt");
		try {
			FileReader readfile = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		System.out.println(""+file.getName());
		System.out.println(""+file.getName().substring(0,(file.getName().lastIndexOf("."))));
		
	}
}
