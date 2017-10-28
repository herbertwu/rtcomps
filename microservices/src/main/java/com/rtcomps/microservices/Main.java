package com.rtcomps.microservices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = { "com.jgreenlight"})
@EnableAutoConfiguration
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	ObjectMapper objectMapper() {
	    return Jackson2ObjectMapperBuilder
	            .json()
	            .featuresToEnable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
	            .build();
	}

}
