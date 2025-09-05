package com.develop.job.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/adsDep")
public class AdsApi {

	private static final Logger log = LoggerFactory.getLogger(AdsApi.class);
	
//	@Autowired
//	private ProcessAds process;
	
	@GetMapping(value="/headers")
	public void getHeaders(HttpServletRequest httpRequest,
		HttpServletResponse httpResponse) {
		log.info("addres {}", httpRequest.getRemoteAddr());
		log.info("host remote {}", httpRequest.getRemoteHost());
		Enumeration<String> eNames = httpRequest.getHeaderNames();
		
		StringBuilder responser= new StringBuilder();
		responser.append("<table border='1' cellpadding='4' cellspacing='0'>\n");
        while (eNames.hasMoreElements()) {
	          String name = (String) eNames.nextElement();
	          String value = httpRequest.getHeader(name);
	          responser.append("<tr><td>").append(name).append("</td><td>").append(value).append("</td></tr>").append("\n");
        }
        
        PrintWriter out;
		try {
			out = httpResponse.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println( responser.toString() );
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        log.info("printer header");
	}
	
	@PostMapping
	@RequestMapping(value="/p/{param}")
	public ResponseEntity<?>  processHeadersAds(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable("param") String params){
		log.info("params {}", params);
		//process.process(params,null);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
