package com.microservice.order_service.service;

import com.microservice.order_service.payload.OrderRequest;

public interface OrderService {
	
	public String placeOrder(OrderRequest orderRequest);

}
