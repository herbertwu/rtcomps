package com.rtcomps.microservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rtcomps.core.def.HomePrice;


@RestController
@RequestMapping("/testcases")
public class HomePriceController {

	@Autowired
	public HomePriceController() {
	}

	@RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public HomePrice getPrice() throws Exception {
		return null;
	}

}
