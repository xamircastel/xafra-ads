package com.develop.job.exception;

import org.springframework.http.HttpStatus;

public class RequestException extends Exception {

	private static final long serialVersionUID = -4526720720597939253L;

	private String body;
	private String statusText;
	private int code;
	private HttpStatus statusCode;

	public RequestException(Throwable cause, String message, String body, String statusText, int code,
			HttpStatus statusCode) {
		super(message, cause);
		this.body = body;
		this.statusCode = statusCode;
		this.statusText = statusText;
		this.code = code;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.hashCode()).append("@RestClientExeption[body=").append(body);
		b.append(", statusText=").append(statusText).append(", code=").append(code);
		b.append(", statusCode=").append(statusCode).append(", cause=").append(this.getMessage());
		return b.toString();
	}
}
