package com.microservices.currencyconversion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.microservices.currencyconversion.model.Limits;

@FeignClient(name="limits-service")
public interface LimitsProxy {

	@GetMapping("/limits")
	public Limits retrieveLimits();
}
