package com.currencyexchangeratetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCircuitBreaker
@EnableSwagger2
public class CurrencyexchangeratetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyexchangeratetrackerApplication.class, args);
	}
}
