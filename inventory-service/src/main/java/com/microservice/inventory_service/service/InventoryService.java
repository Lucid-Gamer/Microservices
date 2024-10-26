package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

	public List<InventoryResponse> isInStock(List<String> skvCode);
	
}
