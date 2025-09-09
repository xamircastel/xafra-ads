package com.develop.job.ads.to;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdsRequest {

	private String params;
	private String trakingId;
	private String xafraTrackingId; // Nuevo campo para tracking automático XAFRA
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;

	public AdsRequest() {
	}

	public AdsRequest(String p, String t, HttpServletRequest reqs, HttpServletResponse resp) {
		this.params = p;
		this.trakingId = t;
		this.httpRequest = reqs;
		this.httpResponse = resp;
	}

	public AdsRequest(String t, HttpServletRequest reqs, HttpServletResponse resp) {
		this.trakingId = t;
		this.httpRequest = reqs;
		this.httpResponse = resp;
	}

	// Constructor para tracking automático con xafraTrackingId
	public AdsRequest(String p, String t, String xafraTracking, HttpServletRequest reqs, HttpServletResponse resp) {
		this.params = p;
		this.trakingId = t;
		this.xafraTrackingId = xafraTracking;
		this.httpRequest = reqs;
		this.httpResponse = resp;
	}

}
