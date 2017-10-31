package com.rtcomps.data.httpclient;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtcomps.core.def.HomePrice;
import com.rtcomps.core.util.HttpConnection;
import com.rtcomps.core.util.XMLUnmarshaller;


public class RedfinReader  {
	private final Logger log =  LoggerFactory.getLogger(RedfinReader.class);
	private HttpConnection conn;
	private String targetUrl;
	private XMLUnmarshaller<HomePrice> unmarshaller;

	public RedfinReader(HttpConnection conn, String targetUrl,
			XMLUnmarshaller<HomePrice> unmarshaller) {
		this.conn = conn;
		this.targetUrl = targetUrl;
		this.unmarshaller = unmarshaller;
	}

	public String read() throws Exception {
		try {
			String response = conn.get(targetUrl);
			//HomePrice xmlDto = unmarshaller.unmarshallXMLPage(response, HomePrice.class);
			log.info("httpResp: " + response);
			return response;
		} catch (Exception e ) {
			throw new RuntimeException("http read read failed: ", e);
		}
	}

}
