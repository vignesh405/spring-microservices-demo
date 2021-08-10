package com.microservices.currencyconversion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.microservices.currencyconversion.model.Limits;

@FeignClient(name="limits-service",url="${LIMITS_MICRO_SERVICE_HOST:http://localhost}:7000")
public interface LimitsProxy {

	@GetMapping("/limits")
	public Limits retrieveLimits();
}
