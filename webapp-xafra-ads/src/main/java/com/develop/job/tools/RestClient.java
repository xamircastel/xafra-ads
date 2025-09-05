package com.develop.job.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.develop.job.exception.RequestException;
import com.google.gson.JsonParseException;

@Component
public class RestClient {

	private static final Logger log = LoggerFactory.getLogger(RestClient.class);

	@Autowired
	private RestTemplate client;

	public static final RestClient INSTANCE = new RestClient();
	protected HttpHeaders headers = new HttpHeaders();

	private static final int DEFAULT_READ_TIMEOUT = 60000;
	private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
		clientHttpRequestFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
		return clientHttpRequestFactory;
	}

	public <T, R> R executeApi(HttpMethod Method, String url, final T data, Class<R> returnClazz,
			Map<String, String> headers) throws RequestException {
		final String jsonValue = data != null ? Utils.toEntyJson(data) : "";
		HttpEntity<String> entity = new HttpEntity<String>(jsonValue, loadHeader(headers, jsonValue));
		try {
			client.setRequestFactory(getClientHttpRequestFactory());
			ResponseEntity<String> response = client.exchange(url, Method, entity, String.class);
			log.debug("response url={}, response {}", url, response.getBody());
			return deserialize(returnClazz, response);
		} catch (HttpClientErrorException | HttpServerErrorException cse) {
			log.error("client or server ERROR url={}, status={}, response={}", url, cse.getRawStatusCode(),
					cse.getResponseBodyAsString());
			throw new RequestException(cse, "Error request API", cse.getResponseBodyAsString(), cse.getStatusText(), cse.getRawStatusCode(), cse.getStatusCode());
		} catch (Exception e) {
			throw (e);
		}
	}

	private HttpHeaders loadHeader(Map<String, String> hea, String json) {
		if (json != null && !"".equals(json))
			this.headers.set("Content-Type", "application/json");
		if (hea != null)
			this.headers.setAll(hea);
		return this.headers;
	}

	@SuppressWarnings("unchecked")
	private <R> R deserialize(Class<R> returnClazz, ResponseEntity<String> response) {
		try {
			if (response != null && response.getBody() != null && !response.getBody().equals("")) {
				return Utils.toJsonEntity(response.getBody(), returnClazz);
			}
		} catch (JsonParseException e) {
			if (response.getBody() != null) {
				return (R) response.getBody();
			} else
				throw (e);
		}
		return null;
	}

}
