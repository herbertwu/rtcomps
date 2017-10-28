package com.rtcomps.microservices;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/testcases")
public class CompsPriceController {

	@Autowired
	public CompsPriceController() {
	}

	@RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> listAllTestCases() throws Exception {
		return Arrays.asList("FooTest", "BarTest");
	}

}
