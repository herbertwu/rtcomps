package com.rtcomps.data.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.rtcomps.core.def.Address;
import com.rtcomps.core.def.HomePrice;
import com.rtcomps.data.PriceDao;

public class ZillowHomePrice implements PriceDao {
	private final Logger log =  LoggerFactory.getLogger(ZillowHomePrice.class);
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public HomePrice getPrice(String url, Address addr) {
		try {
	    	HomePrice  result  = restTemplate.postForObject(url, addr, HomePrice.class);
			log.info("Success submitting a task run request: " + result.toString());
			return result;
    	} catch (Exception e ) {
    		String errMsgs = String.format("Failed to run task at schEvent=%s", addr.toString());
    		log.error(errMsgs, e);
    		throw new RuntimeException(errMsgs, e);
    	}
	}

}
