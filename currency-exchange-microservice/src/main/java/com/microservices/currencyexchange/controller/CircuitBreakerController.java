package com.microservices.currencyexchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	@Retry(name="sample-api", fallbackMethod = "hardCodedResponse")
	public String sampleApiRetry() {
		logger.info("Sample API call received RT");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity(
				"http://localhost:8080/some-dummy-url", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api-cb")
	@CircuitBreaker(name="default", fallbackMethod = "hardCodedResponse")
	//10s => 10000 calls to the sample api
	public String sampleApiCircuitBreaker() {
		logger.info("Sample API call received CB");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity(
				"http://localhost:8080/some-dummy-url", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api-rl")
	@RateLimiter(name="default")
	//10s => 10000 calls to the sample api
	public String sampleApiRateLimiter() {
		logger.info("Sample API call received RL");
		return "sample-api-rl";
	}
	
	@GetMapping("/sample-api-bh")
	@RateLimiter(name="sample-api-bh")
	//10s => 10000 calls to the sample api
	public String sampleApiBulkHead() {
		logger.info("Sample API call received BH");
		return "sample-api-bh";
	}
	
	public String hardCodedResponse(Exception exp) {
		return "fallback response";
	}
}
