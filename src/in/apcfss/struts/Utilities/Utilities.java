package in.apcfss.struts.Utilities;

import java.util.regex.Pattern;

public class Utilities {
	public static String getDefaultNull(Object val) {
		return val != null ? val.toString() : null;
	}
	public static int getDefaultInt(Object val) {
		return (val != null && Pattern.matches("[0-9]", val.toString()) == true) ? Integer.parseInt(val.toString()) : null;
	}
	public static int getDefaultDouble(Object val) {
		return (val != null && Pattern.matches("[0-9.]", val.toString()) == true) ? Integer.parseInt(val.toString()) : null;
	}
}
