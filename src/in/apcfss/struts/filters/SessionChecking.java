package in.apcfss.struts.filters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionChecking
implements Filter
{

	ArrayList<String> ids = null;
	ArrayList<String> names = null; 

	public void init(FilterConfig arg0)
			throws ServletException
			{
		ids = new ArrayList<String>();
		names = new ArrayList<String>(); 
		//System.out.println("Fist loaded ");
			}

	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterChain)
			throws IOException, ServletException
			{
		String path = ((HttpServletRequest)servletrequest).getRequestURI();

		int p = path.lastIndexOf("/");

		String actionFile = path.substring(p + 1);

		actionFile = actionFile.replace(".done", "").replace(".do", "");
		System.out.println("actionFile in Filter :: "+actionFile);
		String sid = ((HttpServletRequest)servletrequest).getParameter("sid");
		HttpSession session = ((HttpServletRequest)servletrequest).getSession();
		/* List usrServices = (List)session.getAttribute("usrServices");*/
		

		if (actionFile != null && (actionFile.equalsIgnoreCase("Login") || actionFile.equalsIgnoreCase("Logout"))) {
			filterChain.doFilter(servletrequest, servletresponse);
		}

		else if ((session == null) || (session.getAttribute("userid") == null)) {
			RequestDispatcher rd = ((HttpServletRequest) servletrequest).getRequestDispatcher("sessionExpired.do");
			rd.forward(servletrequest, servletresponse);
		} else

		if((session != null) && (session.getAttribute("userid") != null))
		{
			if(sid != null && !sid.equals("") && !sid.equals("null") ){
				
				List services = (List)session.getAttribute("services1");
				
				if(services != null){
					getBreadCrumb(services,sid);
					session.setAttribute("sesAcvtiveLinks", ids);
					Collections.reverse(names);
					session.setAttribute("sesBreadNames", names);
					System.out.println("brreee-------------->"+names);
				}


			}
			else{

				/*if(ids!=null && !ids.isEmpty() && ids.size()>0)
  			session.setAttribute("sesAcvtiveLinks", ids);
  		else*/
				session.setAttribute("sesAcvtiveLinks", "");

			}
			Date curDate = new Date();
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			session.setAttribute("time", time.format(curDate));
			
			filterChain.doFilter(servletrequest, servletresponse);
			
		}
		




		/*else
  {
  	 RequestDispatcher rd = ((HttpServletRequest)servletrequest).getRequestDispatcher("sessionExpired.do");
       rd.forward(servletrequest, servletresponse);
  }*/

			}

	public void destroy()
	{
	}


	public  void getBreadCrumb(List usrServices,String serviceId){

		ids = new ArrayList<String>();
		names = new ArrayList<String>();

		Map m = null;
		for(Object obj : usrServices){

			m = ((Map)obj);

			// System.out.println("ourt  ..." +m.get("serviceid").toString() +" pass :: "+serviceId);


			if(m.get("service_id").toString().equals(serviceId)){
				System.out.println("Found ...");
				getServiceId(usrServices, serviceId, m.get("parent_id").toString(),(String)m.get("service_name"));
				break;
			}
		}
	}

	public  void getServiceId(List usrServices,String sersId,String parentId,String sersName){

		if(!parentId.equals("0")){

			ids.add(sersId);
			names.add(sersName);
			Map m = null;

			for(Object obj : usrServices){

				m = ((Map)obj);

				if(m.get("service_id").toString().equals(parentId)){

					getServiceId(usrServices, m.get("service_id").toString(), m.get("parent_id").toString(),(String)m.get("service_name"));

				}
			}

		}else{
			ids.add(sersId);
			names.add(sersName);
		}

	}

}