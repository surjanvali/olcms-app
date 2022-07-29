package in.apcfss.struts.filters;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class XSSRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest {

	private static Pattern[] patterns = new Pattern[] {

			// Possible Script fragments TO BE AVOIDED IN THE Input data

			/*
			 * 1) Avoids: Cross Site Scripting (XSS), 2) All Inputs fields are validated
			 * 
			 */

			Pattern.compile("<javascript>(.*?)</javascript>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<html>(.*?)</html>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onmouseover=\"(.*?)\"", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onmouseout=\"(.*?)\"", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onmouseover=\'(.*?)\'", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onmouseout=\'(.*?)\'", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("alert\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</html>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<html(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("<", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile(">", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("[\\\"\\\'][\\s]*((?i)javascript):(.*)[\\\"\\\']",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)script)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)eval)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)alert)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)html)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)java)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("((?i)javascript)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

	// Default Constructor to Instantiate the Class
	public XSSRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	// Checking all the requested form values sent by the user
	@Override
	public String[] getParameterValues(String parameter) {

		// Obtaining all the request form values sent by the user
		String[] values = super.getParameterValues(parameter);

		if (values == null) {
			return null;
		}

		// Count of the request form values sent by the user
		int count = values.length;

		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {

			// Forwarding each request form value to stripXSS() method sent by the user for
			// filtering unintended content
			// After filtering saving all the requested form values in the encodedValues
			// array
			encodedValues[i] = stripXSS(values[i]);
		}

		// Return the server filtered content
		return encodedValues;
	}

	// This method returns the value of the wrapped request object
	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		// Forwarding value to stripXSS() method for filtering unintended content
		return stripXSS(value);
	}

	// This method returns the value of the HTTP header like query string or the URL
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		// Forwarding value to stripXSS() method for filtering unintended content
		return stripXSS(value);
	}

	// stripXSS() method to filter unintended content in the variable 'value'

	private String stripXSS(String value) {
		if (value != null) {

			try {
				// Avoid null characters
				value = value.replaceAll("\0", "");

				// Remove all sections that match a fragment
				for (Pattern scriptPattern : patterns) {
					value = scriptPattern.matcher(value).replaceAll(""); // If unintended script id found, replacing it
																			// with the space or empty string
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}