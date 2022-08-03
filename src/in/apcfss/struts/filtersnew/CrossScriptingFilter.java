package in.apcfss.struts.filtersnew;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CrossScriptingFilter implements Filter {

    private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
    	HttpServletRequest httpRequest=(HttpServletRequest) request;
    	httpResponse.setHeader("x-frame-options", "DENY");
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    	httpResponse.setHeader("Pragma", "no-cache");
    	httpResponse.setHeader("Set-Cookie", "HttpOnly; SameSite=strict");
    	httpResponse.setHeader("X-XSS-Protection","1; mode=block");
    //	httpResponse.setHeader("Content-Security-Policy", "script-src 'self' 'unsafe-inline' 'unsafe-eval';  frame-src 'self';  form-action 'self'; reflected-xss block");
    	httpResponse.setHeader("Content-Security-Policy","script-src 'self' 'unsafe-inline' 'unsafe-eval'; base-uri 'self';form-action 'self'");//connect-src 'self';
    	httpResponse.setHeader("Strict-Transport-Security","max-age=31536000; includeSubDomains; preload");
    	httpResponse.setHeader("Allow: OPTIONS", "GET, HEAD, POST");
    	System.out.println("Method Type------------------>"+httpRequest.getMethod());
    	if(httpRequest.getMethod().equalsIgnoreCase("GET") || httpRequest.getMethod().equalsIgnoreCase("POST"))
    	{	
    		chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    	}else{
    		System.out.println("--------------------------Method Not Allowed ---------------------------");
    		PrintWriter out=response.getWriter();
    		out.println("Accepts ONLY GET and POST Methods .........");
    		out.flush();
    		out.close();
    		return;
    	}
    }
 
}
