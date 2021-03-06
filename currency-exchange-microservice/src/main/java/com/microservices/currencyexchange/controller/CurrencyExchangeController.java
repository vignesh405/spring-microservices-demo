package com.microservices.currencyexchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.currencyexchange.model.CurrencyExchange;
import com.microservices.currencyexchange.repository.CurrencyExchangeRepository;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private CurrencyExchangeRepository repository;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(
			@PathVariable String from,
			@PathVariable String to
			) {
		logger.info("retrieveExchangeValue called with {} to {}",from,to);
		String port = environment.getProperty("local.server.port");
		
		String host = environment.getProperty("HOSTNAME");
		String version = "v11";
//		CurrencyExchange currencyExchange = new CurrencyExchange(1L,from,to,BigDecimal.valueOf(10));
		CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
		if(currencyExchange==null) {
			throw new RuntimeException("Unable to Find data for "+from+" and "+to);
		}
		currencyExchange.setEnvironment(port + " " + version + " " + host);
		return currencyExchange;
	}
}
