package com.develop.job.tools;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.develop.job.db.dao.bi.IUsers;
import com.develop.job.db.entity.Users;

@Component
public class AuthenticationPathFilter {

	protected final Logger log = LoggerFactory.getLogger(AuthenticationPathFilter.class);

	@Autowired
	private IUsers userI;

	public void autorization(HttpServletRequest httpRequest, String apikey) throws Exception {
		log.info("[{}]| apikey evaluate {}", httpRequest.getSession().getId(), apikey);
		isValid(apikey != null, "MISSING_PATH_API_KEY");
		
		Users user = userI.getUsersByApiKey(apikey);
		isValid(user != null, "INVALID_API_KEY");
	}

	private void isValid(boolean valid, String message) throws Exception {
		if (!valid)
			throw new Exception(message);
	}
}
