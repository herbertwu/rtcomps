package com.rtcomps.data.httpclient;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.rtcomps.core.def.HomePrice;
import com.rtcomps.core.util.HttpConnection;
import com.rtcomps.core.util.XMLUnmarshaller;

public class RedfinReaderTest {
	@Rule
	public ExpectedException thrown= ExpectedException.none();
	HttpConnection conn = new HttpConnection();
	String targetUrl = "https://www.redfin.com/";
	XMLUnmarshaller<HomePrice> unmarshaller = new XMLUnmarshaller<HomePrice>();
	
	@Test
	public void simpleRead_ok() throws Exception {
		RedfinReader reader = new RedfinReader(conn, targetUrl, unmarshaller);
		String resp = reader.read();
		System.out.println(resp);
	}

}
