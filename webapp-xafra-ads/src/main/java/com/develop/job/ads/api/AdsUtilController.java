package com.develop.job.ads.api;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commons.help.job.utils.Encryption;

@RestController
@RequestMapping(value="/util")
public class AdsUtilController {

	private static final Logger log = LoggerFactory.getLogger(AdsUtilController.class);
	
	@Value("${password.encription}")
	private String passEncription;
	@Value("${user.encription}")
	private String userEncryp;
	
	private HttpServletRequest req;
	
	@PostMapping("/encryption")
	public ResponseEntity<?>  encryp(@RequestHeader HttpHeaders headers,
			@RequestBody(required = false) String encryp, 
			@RequestHeader("user") String user, HttpServletRequest request) throws UnsupportedEncodingException{
		req = request;
		if(encryp!=null) {
			log.info("params={}, usuario={}", encryp, user);
			log.info("Adrr={}, user={}, session={}, host={}", req.getRemoteAddr(), 
					req.getRemoteUser(), req.getRequestedSessionId(), req.getRemoteHost());
			if(userEncryp.equals(user)) {
				try {
					String encription = Encryption.encrypt(encryp, passEncription);
					encription = encription.replace("/", "$").replaceAll("=", "");
					return ResponseEntity.status(HttpStatus.OK).body(encription);
				} catch (Exception e) {
					log.error("Error en encriptación", e);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
	}
	
	@GetMapping("/get")
	public ResponseEntity<String> getBaseUrl(@RequestHeader HttpHeaders headers) {
	    InetSocketAddress host = headers.getHost();
	    String url = "http://" + host.getHostName() + ":" + host.getPort();
	    headers.forEach((key, value)->{
	    	log.info("header key={}, Value={}", key, value);
	    });
	    
	    return new ResponseEntity<String>(String.format("Base URL = %s", url), HttpStatus.OK);
	}
}
