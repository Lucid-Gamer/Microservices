package com.microservice.order_service.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.order_service.payload.OrderRequest;
import com.microservice.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	public final OrderService orderService;
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
	@TimeLimiter(name = "inventory",fallbackMethod = "fallbackMethod")
	@Retry(name = "inventory",fallbackMethod = "fallbackMethod")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
	}

	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please place order after some time!");
	}
}
