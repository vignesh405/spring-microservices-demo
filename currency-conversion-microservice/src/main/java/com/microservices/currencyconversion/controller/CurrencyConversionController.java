package com.microservices.currencyconversion.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.currencyconversion.client.CurrencyExchangeProxy;
import com.microservices.currencyconversion.client.LimitsProxy;
import com.microservices.currencyconversion.model.CurrencyConversion;
import com.microservices.currencyconversion.model.Limits;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private LimitsProxy limitsProxy;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable Double quantity) {
		HashMap<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}",
				CurrencyConversion.class,
				uriVariables);
		
		CurrencyConversion currencyConversion = responseEntity.getBody();
		return new CurrencyConversion(currencyConversion.getId(), from, to, currencyConversion.getConversionMultiple(), BigDecimal.valueOf(quantity), BigDecimal.valueOf(quantity*currencyConversion.getConversionMultiple().doubleValue()), "8100");
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable Double quantity) {
		
		Limits limits = limitsProxy.retrieveLimits();
		if(limits.getMinimum()>quantity || limits.getMaximum()<quantity) {
			throw new RuntimeException("quantity is not within the limits");
		}
		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
		return new CurrencyConversion(currencyConversion.getId(), from, to, currencyConversion.getConversionMultiple(), BigDecimal.valueOf(quantity), BigDecimal.valueOf(quantity*currencyConversion.getConversionMultiple().doubleValue()), currencyConversion.getEnvironment());
	}
}
