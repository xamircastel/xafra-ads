package com.develop.job.ads.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ads extends HttpServlet {

	private static final long serialVersionUID = 1919862006393038732L;

	private static final Logger logger = LoggerFactory.getLogger(Ads.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		  Enumeration<String> eNames = req.getHeaderNames();
		  StringBuilder responser= new StringBuilder();
		  responser.append("<table border='1' cellpadding='4' cellspacing='0'>\n");
          while (eNames.hasMoreElements())
          {
	          String name = (String) eNames.nextElement();
	          String value = req.getHeader(name);
	          responser.append("<tr><td>").append(name).append("</td><td>").append(value).append("</td></tr>").append("\n");
          }
         
          Map<String,String[]> params = req.getParameterMap();
          params.keySet().forEach(s->{
        	  responser.append("<tr><td>").append(s).append("</td><td>").append(req.getParameter(s)).append("</td></tr>").append("\n");
          });
          
          responser.append("<table>");
          response(resp, responser.toString());
          logger.info("termina httpServletRequest");
	}

	private void response(HttpServletResponse resp, String msg) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println( msg );
		out.println("</body>");
		out.println("</html>");
	}
	
}
