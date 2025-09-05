package com.develop.job.auth.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import com.commons.help.job.utils.Encryption;
import com.develop.job.db.dao.bi.IUsers;
import com.develop.job.db.entity.Users;

@Component
public class AuthenticationHeaderFilter extends OncePerRequestFilter {

	protected final Logger log = LoggerFactory.getLogger(AuthenticationHeaderFilter.class);

	@Autowired
	private IUsers userI;

	public void closeHttpServlet(HttpServletResponse r, int status, String message) {
		try {
			r.setStatus(status);
			r.addHeader("Content-Type", "text/plain");
			ServletOutputStream out = r.getOutputStream();
			out.write(message.getBytes());
			out.flush();
			out.close();
		} catch (IOException ioException) {
		}
	}

	private void isValid(boolean valid, String message) throws Exception {
		if (!valid)
			throw new Exception(message);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ServletContext context = request.getSession().getServletContext();
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, context);
		try {
			log.info("apikey={}, {}, {}", request.getHeader("apikey"), request.getSession().getId(),
					request.getRemoteAddr());
			String apikey = request.getHeader("apikey");
			String auth = request.getHeader("auth");
			isValid(apikey != null, "MISSING_API_KEY");
			isValid(auth != null, "MISSING_AUTHENTICATION");
			Users user = userI.getUsersByApiKey(apikey);
			isValid(user != null, "INVALID_API_KEY");
			String decrypt = Encryption.decrypt(auth, user.getSharedKey());
			isValid(decrypt != null, "AUTHENTICATION_FAILED");
			validateAuthentication(decrypt);
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("apikey={}, {}, {}", request.getHeader("apikey"), e.getMessage(), request.getSession().getId());
			closeHttpServlet(response, HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}

	private void validateAuthentication(String decrypt) {
		LocalDateTime today = LocalDateTime.now();
		if (decrypt != null) {
			Date dateEnc = new Date(Long.parseLong(decrypt));
			LocalDateTime yesterday = today.minusDays(1);
			Date yesterday1 = Date.from(yesterday.atZone(ZoneId.systemDefault()).toInstant());
			if (dateEnc.compareTo(yesterday1) == -1) {

				throw new IllegalArgumentException("AUTHENTICATION_FAILED1");
			}
		}

	}
}
