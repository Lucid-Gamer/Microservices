package com.microservice.inventory_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

	private final InventoryRepository inventoryRepository;
	
	@Override
	@Transactional(readOnly = true)
	public boolean isInStock(String skvCode) {
		return inventoryRepository.findBySkvCode(skvCode).isPresent();
	}

}
