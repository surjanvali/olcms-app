package in.apcfss.struts.filtersnew;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MultipartFilter implements Filter {

    // Init ---------------------------------------------------------------------------------------

    private long maxFileSize;

    
    // Actions ------------------------------------------------------------------------------------

    /**
     * Configure the 'maxFileSize' parameter.
     * @throws ServletException If 'maxFileSize' parameter value is not numeric.
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // Configure maxFileSize.
        String maxFileSize = filterConfig.getInitParameter("maxFileSize");
        if (maxFileSize != null) {
            if (!maxFileSize.matches("^\\d+$")) {
                throw new ServletException("MultipartFilter 'maxFileSize' is not numeric.");
            }
            this.maxFileSize = Long.parseLong(maxFileSize);
        }
    }

    /**
     * Check the type request and if it is a HttpServletRequest, then parse the request.
     * @throws ServletException If parsing of the given HttpServletRequest fails.
     * @see javax.servlet.Filter#doFilter(
     *      javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException, IOException
    {
        // Check type request.
        if (request instanceof HttpServletRequest) {
            // Cast back to HttpServletRequest.
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Parse HttpServletRequest.
            HttpServletRequest parsedRequest = parseRequest(httpRequest);

            // Continue with filter chain.
            chain.doFilter(parsedRequest, response);
        } else {
            // Not a HttpServletRequest.
            chain.doFilter(request, response);
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // I am a boring method.
    }

    // Helpers ------------------------------------------------------------------------------------

    /**
     * Parse the given HttpServletRequest. If the request is a multipart request, then all multipart
     * request items will be processed, else the request will be returned unchanged. During the
     * processing of all multipart request items, the name and value of each regular form field will
     * be added to the parameterMap of the HttpServletRequest. The name and File object of each form
     * file field will be added as attribute of the given HttpServletRequest. If a
     * FileUploadException has occurred when the file size has exceeded the maximum file size, then
     * the FileUploadException will be added as attribute value instead of the FileItem object.
     * @param request The HttpServletRequest to be checked and parsed as multipart request.
     * @return The parsed HttpServletRequest.
     * @throws ServletException If parsing of the given HttpServletRequest fails.
     */
    @SuppressWarnings("unchecked") // ServletFileUpload#parseRequest() does not return generic type.
    private HttpServletRequest parseRequest(HttpServletRequest request) throws ServletException {

    	System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEE");
        // Check if the request is actually a multipart/form-data request.
        if (!ServletFileUpload.isMultipartContent(request)) {
            // If not, then return the request unchanged.
        //	System.out.println("--------------Not a Multi Part-------");
            return request;
        }else{
        	//System.out.println("-------------- Multi Part-------");
        }

        // Prepare the multipart request items.
        // I'd rather call the "FileItem" class "MultipartItem" instead or so. What a stupid name ;)
        List<FileItem> multipartItems = null;

        try {
            // Parse the multipart request items.
            multipartItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            // Note: we could use ServletFileUpload#setFileSizeMax() here, but that would throw a
            // FileUploadException immediately without processing the other fields. So we're
            // checking the file size only if the items are already parsed. See processFileField().
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request: " + e.getMessage());
        }

        // Prepare the request parameter map.
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();

        // Loop through multipart request items.
        for (FileItem multipartItem : multipartItems) {
            if (multipartItem.isFormField()) {
                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                processFormField(multipartItem, parameterMap);
               // System.out.println("-------------- Multi Part--- parameterMap fileds ----"+parameterMap);
               // System.out.println("-------------- Multi Part--- parameterMap ----"+multipartItem);
            } else {
            	processFormFieldForFile(multipartItem, parameterMap);
            //	parameterMap.put(key, value);
            	//processFileField(multipartItem, request);
                // Process form file field (input type="file").
               // processFileField(multipartItem, request);
              //  System.out.println("-------------- Multi Part--- file filed----"+request);
               // System.out.println("-------------- Multi Part--- file ----"+multipartItem);
            }
        }

        // Wrap the request with the parameter map which we just created and return it.
        return wrapRequest(request, parameterMap);
    }
    
    private void processFormFieldForFile(FileItem formField, Map<String, String[]> parameterMap) {
        parameterMap.put(formField.getFieldName(),parameterMap.get(formField.getFieldName()) );
    }

    /**
     * Process multipart request item as regular form field. The name and value of each regular
     * form field will be added to the given parameterMap.
     * @param formField The form field to be processed.
     * @param parameterMap The parameterMap to be used for the HttpServletRequest.
     */
    private void processFormField(FileItem formField, Map<String, String[]> parameterMap) {
        String name = formField.getFieldName();
        
       // System.out.println("the type "+name +"-----------"+ formField.getClass());
        
        String value = formField.getString();
        String[] values = parameterMap.get(name);

        if (values == null) {
            // Not in parameter map yet, so add as new value.
        	
	 	        
	 	        
        	// System.out.println("name   --->"+name);
             //System.out.println("values --->"+rePlaceAll(value));
        	//System.out.println(" actual value --->"+value + "replaced value --------"+rePlaceAll(value));
              parameterMap.put(name, new String[] { rePlaceAll(value) });
             
              
        } else {
            // Multiple field values, so add new value to existing array.
            int length = values.length;
            String[] newValues = new String[length + 1];
            System.arraycopy(values, 0, newValues, 0, length);
            newValues[length] = value;
           
 	        
           // System.out.println("name else  --->"+newValues);
           // System.out.println("values else--->"+newValues);
            parameterMap.put(name, newValues);
        }
       // System.out.println("parameterMap================"+parameterMap);
    }

    private String rePlaceAll(String value) {
    	System.out.println("value---------------"+value);
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
    	  
    		value = value.replaceAll("<", " ").replaceAll(">", " ");
 	        value = value.replaceAll("\\(", " ").replaceAll("\\)", " ");
 	        value = value.replaceAll("'", " ");
 	        value = value.replaceAll("eval\\((.*)\\)", "");
 	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
 	        value = value.replaceAll("[\\\"\\\'][\\s]*JAVASCRIPT:(.*)[\\\"\\\']", "\"\"");
 	        value = value.replaceAll("script", "");
 	        value = value.replaceAll("SCRIPT", "");
 	        value = value.replaceAll("alert", "");
 	        value = value.replaceAll("ALERT", "");
 	       value = value.replaceAll("'", "");
 	       System.out.println("value-----"+value);
		return value;
	}

	/**
     * Process multipart request item as file field. The name and FileItem object of each file field
     * will be added as attribute of the given HttpServletRequest. If a FileUploadException has
     * occurred when the file size has exceeded the maximum file size, then the FileUploadException
     * will be added as attribute value instead of the FileItem object.
     * @param fileField The file field to be processed.
     * @param request The involved HttpServletRequest.
     */
    private void processFileField(FileItem fileField, HttpServletRequest request) {
    	System.out.println("in the test");
        if (fileField.getName().length() <= 0) {
        	System.out.println("in the test 1");
            // No file uploaded.
            request.setAttribute(fileField.getFieldName(), null);
        } else if (maxFileSize > 0 && fileField.getSize() > maxFileSize) {
        	System.out.println("in the test 2");
            // File size exceeds maximum file size.
            request.setAttribute(fileField.getFieldName(), new FileUploadException(
                "File size exceeds maximum file size of " + maxFileSize + " bytes."));
            // Immediately delete temporary file to free up memory and/or disk space.
            fileField.delete();
        } else {
        	System.out.println("in the test 3"+fileField.getFieldName());
            // File uploaded with good size.
            request.setAttribute(fileField.getFieldName(), fileField);
        }
    }

    // Utility (may be refactored to public utility class) ----------------------------------------

    /**
     * Wrap the given HttpServletRequest with the given parameterMap.
     * @param request The HttpServletRequest of which the given parameterMap have to be wrapped in.
     * @param parameterMap The parameterMap to be wrapped in the given HttpServletRequest.
     * @return The HttpServletRequest with the parameterMap wrapped in.
     */
    private static HttpServletRequest wrapRequest(
        HttpServletRequest request, final Map<String, String[]> parameterMap)
    {
        return new HttpServletRequestWrapper(request) {
            public Map<String, String[]> getParameterMap() {
            	// System.out.println("getParameterMap================"+parameterMap);
            	 
            	 for(int i=0;i<parameterMap.size();i++)
            	 {
            		 parameterMap.get(i);
            		// System.out.println("the get Param Value is ===="+parameterMap.get(i));
            	 }
            	 
            	/* value = value.replaceAll("<", " ").replaceAll(">", " ");
   	 	        value = value.replaceAll("\\(", " ").replaceAll("\\)", " ");
   	 	        value = value.replaceAll("'", " ");
   	 	        value = value.replaceAll("eval\\((.*)\\)", "");
   	 	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
   	 	        value = value.replaceAll("[\\\"\\\'][\\s]*JAVASCRIPT:(.*)[\\\"\\\']", "\"\"");
   	 	        value = value.replaceAll("script", "");
   	 	        value = value.replaceAll("SCRIPT", "");*/
                return parameterMap;
            }
            public String[] getParameterValues(String name) {
            	 //System.out.println("getParameterValues================"+name);
                return parameterMap.get(name);
            }
            public String getParameter(String name) {
                String[] params = getParameterValues(name);
              //  System.out.println("params================"+params);
                return params != null && params.length > 0 ? params[0] : null;
            }
            public Enumeration<String> getParameterNames() {
                return Collections.enumeration(parameterMap.keySet());
            }
        };
    }

}