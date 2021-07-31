package com.microservices.apigateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
	
		String currencyConversionInstance = "lb://currency-conversion";
		String currencyExchangeInstance = "lb://currency-exchange";
		//sample route
		Function<PredicateSpec, Buildable<Route>> routeFunction
			= p -> p.path("/get")
					.filters(f -> f.addRequestHeader("MyHeader", "MyURI")
									.addRequestParameter("MyParam", "MyValue"))
					.uri("http://httpbin.org:80");
		//currency exchange route-custom route, automatic route can be done by the following in application.properties:
//			spring.cloud.gateway.discovery.locator.enabled=true
//					spring.cloud.gateway.discovery.locator.lowerCaseServiceId=true
		Function<PredicateSpec, Buildable<Route>> currencyExchangeRouteFunction
			= p -> p.path("/currency-exchange/**")
					.uri(currencyExchangeInstance);
			
		Function<PredicateSpec, Buildable<Route>> currencyConversionRouteFunction
			= p -> p.path("/currency-conversion/**")
					.filters(f -> f.rewritePath("/currency-conversion", "/currency-conversion-feign"))
					.uri(currencyConversionInstance);
		
		Function<PredicateSpec, Buildable<Route>> retrySampleRouteFunction
			= p -> p.path("/sample-api")
					.uri(currencyExchangeInstance);
			
		Function<PredicateSpec, Buildable<Route>> circuitBreakerSampleRouteFunction
			= p -> p.path("/sample-api-cb")
					.uri(currencyExchangeInstance);
			
		Function<PredicateSpec, Buildable<Route>> rateLimiterSampleRouteFunction
			= p -> p.path("/sample-api-rl")
					.uri(currencyExchangeInstance);
		return builder.routes()
					  .route(routeFunction)
					  .route(currencyExchangeRouteFunction)
					  .route(retrySampleRouteFunction)
					  .route(currencyConversionRouteFunction)
					  .route(circuitBreakerSampleRouteFunction)
					  .route(rateLimiterSampleRouteFunction)
					  .build();
		
	}
}
