package in.apcfss.struts.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SQLIFilter implements Filter {

	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		// System.out.println("SQLIFilter @ init");
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req1 = (HttpServletRequest) request;

		boolean chk = true;
		String name = null, value = null;

		// Possible ATTACK PATTERNS TO BE AVOIDED IN THE SQL INJECTION

		String[] AttackPattern = { "1'", "1' ", " 1'", "1' OR", "1' OR '1'=1", "1'%20", "1 '%20", "'%20", "'%20OR",
				"' OR", "'%20OR%20'1", "'%20'1", "1 '%20OR%20'1'%3d'1", "'%20OR%20'1'%3d'1", " '%20",
				// " CREATE"," ALTER"," DROP"," DROP TABLE"," RENAME"," SELECT"," SELECT *","
				// SELECT * FROM"," INSERT INTO"," UPDATE"," DELETE FROM"," REVOKE","
				// @@VERSION",
				" CREATE", " ALTER", " DROP", " DROP TABLE", " RENAME", " INSERT INTO", " UPDATE", " DELETE FROM",
				" REVOKE", " @@VERSION", " EXEC", " UNION", " WAITFOR", " GROUP BY", " ORDER BY", " SHUTDOWN",
				" RESTART", " <?xml version", " TRUNCATE", " CASE WHEN", " IS NOT NULL", " 1 = 1--", " 1 = 1" };

		// BELOW PATTERNS REQUIRED IN THIS APPLICATION and are ALLOWED
		// "AND", " OR", " GRANT" "'"," '", ";"," CHAR"," INT"

		// VERIFY THE ENTIRE REQUESTED DATA SENT BY THE USER, WHETHER EXIST IN THE
		// ATTACK PATTERN OR NOT
		String qry = req1.getQueryString(); // Query String

		System.out.println("SQLIFilter getQueryString : " + req1.getQueryString());
		System.out.println("SQLIFilter getRequestURL : " + req1.getRequestURL());

		int i = 0, fnd;
		for (i = 0; i < AttackPattern.length; i++) {
			if (qry != null) {

				fnd = (qry.toLowerCase()).indexOf(AttackPattern[i].toLowerCase()); // Total Query String checking with
																					// the individual attack patterns
																					// declared above
				if (fnd != -1) // IF VULNERABILITY FOUND
				{
					chk = false; // SET CHK VARIABLE TO FALSE

					// PRINT THE ATTACK PATTERN FOUND
					System.out.println("Attack Pattern Present (SQL Injection @ Qry String): Patter Value="
							+ AttackPattern[i] + "\n Qry String=" + qry);

					// PRINT THE REQUESTED URL IN WHICH THE ATTACK PATTERN FOUND
					System.out.println("SQLIFilter RequestURL : " + req1.getRequestURL());
					break;
				}

			}

		} // AttackPattern.length for loop (i for loop)

		if (chk == true) // IF ATTACK NOT FOUND IN the Query String and checking the data entered by user
							// (Form properties values)
		{
			// Get the values of all request parameters individually
			Enumeration enumm = req1.getParameterNames();
			for (; enumm.hasMoreElements();) {

				// Get the name of the Form property
				name = (String) enumm.nextElement();

				// Get the value of the Form property
				value = req1.getParameter(name);

				System.out.println("name : " + name + "  value : " + value);

				for (i = 0; i < AttackPattern.length; i++) {

					// Checking the Form property values exist in the attack pattern or not
					if (value.equalsIgnoreCase(AttackPattern[i].toLowerCase().trim())) // Attack pattern found in the
																						// request paramter value
					{
						chk = false; // Attack Pattern Present, SET CHK VARIABLE FALSE

						// Print the Form property Name, Parameter Value and the corresponding Attacking
						// Pattern matched
						System.out.println("Attack Pattern Present (SQL Injection @ request parameter): Param name="
								+ name + ", Param value=" + value + ", Pattern value=" + AttackPattern[i] + ", Index="
								+ i);

						// PRINT THE REQUESTED URL IN WHICH THE ATTACK PATTERN FOUND
						System.out.println("SQLIFilter RequestURL : " + req1.getRequestURL());
						break;
					}

					if (chk == false) // Verify the CHK variable. if chk==false (attack patter found) and if chk==true
										// (attack patter not found) in this inner for loop
					{
						break;
					}
				}
				if (chk == false) // Verify the CHK variable. if chk==false (attack patter found) and if chk==true
									// (attack patter not found) in the enum for loop
				{
					break;
				}
			} // for loop enumm
		} // if chk = true
			// Completed checking the attack pattern
		if (chk == true) // Verify the CHK variable.
		{
			// Process the request, if chk==true (attack patter not found)
			// Redirect to user requested service
			chain.doFilter(request, response);
		} else if (chk == false) // Verify the CHK variable.
		{
			// Verify the CHK variable, if chk==false (attack patter found)
			// Redirect to session expire page
			// Set the session expire page if the any insecure request is made to
			// application
			RequestDispatcher rd = request.getRequestDispatcher("sessionExpired.do");
			rd.forward(request, response); // Redirect to session expire page
		}
	}
}