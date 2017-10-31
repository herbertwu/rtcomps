package com.rtcomps.core.util;

import java.io.IOException;
import java.net.URI;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnection {

	private static final Logger logger = LoggerFactory.getLogger(HttpConnection.class);
	
	
	public String get(String url) throws Exception {
		SSLContext sslContext =
	            new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

		try {
			logger.debug("HTTP GET:" + url);
			//CloseableHttpClient http = HttpClients.createDefault();
			CloseableHttpClient http = HttpClients.custom().setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			
			try {
				HttpGet get = new HttpGet(new URI(url));
				get.setHeader("content-type", "application/json");
				return http.execute(get, RESPONSE_TO_STRING);
			} finally {
				http.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final ResponseHandler<String> RESPONSE_TO_STRING = new ResponseHandler<String>() {

		@Override
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			checkHttpStatusIs200(response);
			return EntityUtils.toString(response.getEntity());
		}

		private void checkHttpStatusIs200(HttpResponse response) throws ClientProtocolException {
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new ClientProtocolException("http status " + status);
			}
		}
		
	};
	
}
