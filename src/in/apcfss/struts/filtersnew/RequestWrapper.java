package in.apcfss.struts.filtersnew;




import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class RequestWrapper extends HttpServletRequestWrapper {

	  public RequestWrapper(HttpServletRequest servletRequest) {
	        super(servletRequest);
	    }

	    public String[] getParameterValues(String parameter) {

	      String[] values = super.getParameterValues(parameter);
	      if (values==null)  {
	                  return null;
	          }
	      int count = values.length;
	      String[] encodedValues = new String[count];
	      for (int i = 0; i < count; i++) {
	                 encodedValues[i] = cleanXSS(values[i]);
	       }
	      return encodedValues;
	    } 

	    public String getParameter(String parameter) {
	          String value = super.getParameter(parameter);
	          if (value == null) {
	                 return null;
	                  }
	          return cleanXSS(value);
	    }

	    public String getHeader(String name) {
	        String value = super.getHeader(name);
	        if (value == null)
	            return null;
	        return cleanXSS(value);

	    }

	     public static String cleanXSS(String value) {
	                //You'll need to remove the spaces from the html entities below
	    	//System.out.println("Get cleanXSS:::::"+value);
	        
	    	 
	    	 if (value != null) {
 String[] AttackPattern={"1'","1' "," 1'","1' OR","1' OR '1'=1","1'%20","1 '%20","'%20","'%20OR","' OR","'%20OR%20'1","'%20'1","1 '%20OR%20'1'%3d'1","'%20OR%20'1'%3d'1"," '%20",
" CREATE","CREATE TABLE"," ALTER","ALTER TABLE", " DROP"," DROP TABLE","DROP TABLE"," RENAME"," INSERT INTO"," UPDATE"," DELETE FROM"," REVOKE"," @@VERSION", "PG_SLEEP", "PG_SLEEP", "SCHEMANAME", "PG_CATALOG",   "PG_TABLES",
						" EXECUTE",
						" UNION"," WAITFOR"," GROUP BY"," ORDER BY"," SHUTDOWN"," RESTART", "WAITFOR DELAY",  "WAITFOR", "DELAY", "SLEEP",
						" SELECT *", "SELECT * FROM", "SELECT *FROM", "SELECT*FROM",
						"SELECT COUNT", "SELECT MAX", "SELECT UPPER", "SELECT LOWER",
						"PG_TERMINATE_BACKEND", "PG_STAT_ACTIVITY", 
						
						"SELECT CONCAT", "CONCAT",
						"CASE WHEN", 
						
						"COUNT(", "COUNT(*)",
						
						"XMLType",
						
						" <?xml version"," TRUNCATE", " CASE WHEN"," IS NOT NULL",
						
						" 1 = 1--"," 1 = 1",
						"%201%20=%201--","%201%20=%201",
						
						"'='","' = '","'  =  '", 
						"'='","'%20=%20'","'%20%20=%20%20'",
						
						"1 AND ", "1 AND '",
						"1%20AND%20", "1%20AND%20'",
						
						" AND 1=1",
						"%20AND%201=1",
						
						"1 AND 1=1", "1 AND '1'='1'", "'1' AND '1'='1'",
						"1%20AND%201=1", "1%20AND%20'1'='1'", "'1'%20AND%20'1'='1'",
						
						"'AND'", "' AND '", "'  AND  '", "'   AND   '",
						"'AND'", "'%20AND%20'", "'%20%20AND%20%20'" , "'%20%20%20AND%20%20%20'",
						
						"AND'", " AND '", "  AND  '", 
						"AND'", "%20AND%20'", "%20%20AND%20%20'",
						
						"'OR'", "' OR '", "'  OR  '",
						"'OR'", "'%20OR%20'", "'%20%20OR%20%20'" , "'%20%20%20OR%20%20%20'",
						
						"1=1", "1 = 1", "1  =  1",
						"%20=%20","%20%20=%20%20","%20%20%20=%20%20%20",
						
						" OR 1=1", "  OR  1=1", "   OR   1=1",
						"%20OR%201=1", "%20%20OR%20%201=1", "%20%20%20OR%20%20%201=1",
						
						" OR ", " OR  ", " OR   ", " OR    ", " OR     ",
						"%20OR%20", "%20OR%20%20", "%20OR%20%20%20", "%20OR%20%20%20%20", "%20OR%20%20%20%20%20",
						
						"  OR ", "   OR ", "    OR ",
						"%20%20OR%20", "%20%20%20OR%20", "%20%20%20%20OR%20",
						
						"  OR  ", "   OR   ", "    OR    ", "     OR     ",
						"%20%20OR%20%20", "%20%20%20OR%20%20%20", "%20%20%20%20OR%20%20%20%20", "%20%20%20%20%20OR%20%20%20%20%20",
						
						"%20OR%201=1", "%20%20OR%20%201=1", "%20%20%20OR%20%20%201=1",
						" OR%20", "%20OR%20%20", "%20OR%20%20%20", "%20OR%20%20%20%20", "%20OR%20%20%20%20%20",
						"%20%20OR%20", "%20%20%20OR%20", "%20%20%20%20OR%20",
						"%20%20OR%20%20", "%20%20%20OR%20%20%20", "%20%20%20%20OR%20%20%20%20", "%20%20%20%20%20OR%20%20%20%20%20",
						
						
						"'' or ''=''",
						"''%20or%20''=''",
						
						"'1'or ", "'1' or ",
						"'1'or%20", "'1'%20or%20",
						
						"'1' = '1'", "'2' = '2'", "'3' = '3'",
						"1 = '1'", "2 = '2'", "3 = '3'",
						"'1' = 1", "'2' = 2", "'3' = 3",
						
						"'1'%20=%20'1'", "'2'%20=%20'2'", "'3'%20=%20'3'",
						"1%20=%20'1'", "2%20=%20'2'", "3%20=%20'3'",
						"'1'%20=%201", "'2'%20=%202", "'3'%20=%203",
						
						"'1'='1'", "'2'='2'", "'3'='3'",
						"1='1'", "2='2'", "3='3'",
						"'1'=1", "'2'=2", "'3'=3",
						
						"'1'=1", "'2'=2", "'3'=3",
						
						"'1'or%20", "'1'%20or%20",
						
						"or 1=1", "or '1'=1", "or 1='1'", "or '1'='1'",
						"or 2=2", "or '2'=2", "or 2='2'", "or '2'='2'",
						"or 3=3", "or '3'=3", "or 3='3'", "or '3'='3'",
						
						"or%201=1", "or%20'1'=1", "or%201='1'", "or%20'1'='1'",
						"or%202=2", "or%20'2'=2", "or%202='2'", "or%20'2'='2'",
						"or%203=3", "or%20'3'=3", "or%203='3'", "or%20'3'='3'",
						
						"and 1=1", "and '1'=1", "and 1='1'", "and '1'='1'",
						"and 2=2", "and '2'=2", "and 2='2'", "and '2'='2'",
						"and 3=3", "and '3'=3", "and 3='3'", "and '3'='3'",
						
						"and%201=1", "and%20'1'=1", "and%201='1'", "and%20'1'='1'",
						"and%202=2", "and%20'2'=2", "and%202='2'", "and%20'2'='2'",
						"and%203=3", "and%20'3'=3", "and%203='3'", "and%20'3'='3'",
						
						
						"and'1'=1", "and'1'='1'",
						"'and'1'=1", "'and'1'='1'",
						
						"or'1'=1", "or '1'='1'",
						"'or'1'=1", "'or'1'='1'",
						
						"--"," -- ", "--'",
						"%20--%20"
						
						,">", "<" , "<>",
						
						";", "&lt;" , "&gt;", "&amp;", "&quot;",
						
						"+or+", "+OR+",  "+and+",  "+AND+"
						
						
						};
	 	       		
	 	        
	 	       for(int i=0;i<AttackPattern.length;i++)
	 	       {
	 	    	   value=value.replace(AttackPattern[i], "");
	 	       }
	 	      for(int i=0;i<AttackPattern.length;i++)
	 	       {
	 	    	   value=value.replace(AttackPattern[i].toLowerCase(), "");
	 	       }
	             // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
	             // avoid encoded attacks.
	             // value = ESAPI.encoder().canonicalize(value);
	    		// System.out.println("Get cleanXSS:::::"+value);
	             // Avoid null characters
	             value = value.replaceAll("", "");
	  
	             // Avoid anything between script tags
	             Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Avoid anything in a src='...' type of expression
	             scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");

	             // Avoid anything in a src="..." type of expression
	             scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Remove any lonesome </script> tag
	             scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Remove any lonesome <script ...> tag
	             scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             
	             
	             // Avoid eval(...) expressions
	             scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Avoid expression(...) expressions
	             scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Avoid javascript:... expressions
	             scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	             scriptPattern = Pattern.compile("JAVASCRIPT:", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Avoid vbscript:... expressions
	             scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	             scriptPattern = Pattern.compile("script:", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	             scriptPattern = Pattern.compile("script", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	  
	             // Avoid onload= expressions
	             scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	              scriptPattern = Pattern.compile("script(.*?)", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	             scriptPattern = Pattern.compile("(.*?)script", Pattern.CASE_INSENSITIVE);
	             value = scriptPattern.matcher(value).replaceAll("");
	             
	             value = value.replace("<", "").replace(">", "");
	 	        value = value.replace("(", "").replace(")", "");
	 	        value = value.replaceAll("eval\\((.*)\\)", "");
	 	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "");
	 	        value = value.replaceAll("[\\\"\\\'][\\s]*JAVASCRIPT:(.*)[\\\"\\\']", "");
	 	        value = value.replace("script", "");
	 	        value = value.replace("vs", "");
	 	        value = value.replace("marquee", "");
	 	        value = value.replace("SCRIPT", "");
	 	        
	 	      
	           System.out.println("Final Value--------->"+value);
	         }
	         return value;
	    	 
	    }
}